/*
 */
package com.utilidades.ficheros;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author vjruiz
 */

public class ConvertirXLSHTML {
    
    final private StringBuilder out = new StringBuilder(65536);
    final private SimpleDateFormat sdf;
    final private HSSFWorkbook book;
    final private HSSFPalette palette;
    final private FormulaEvaluator evaluator;
    private short colIndex;
    private int rowIndex, mergeStart, mergeEnd;
    // Row -> Column -> Pictures
    private Map<Integer, Map<Short, List<HSSFPictureData>>> 
            pix = new HashMap<Integer, Map<Short, List<HSSFPictureData>>>();

    /**
     * Generates HTML from the InputStream of an Excel file. Generates sheet
     * name in HTML h1 element.
     * 
     * @param in
     *            InputStream of the Excel file.
     * @throws IOException
     *             When POI cannot read from the input stream.
     */
    public ConvertirXLSHTML(final InputStream in,int indexSheet) throws IOException {
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (in == null) {
            book = null;
            palette = null;
            evaluator = null;
            return;
        }
        book = new HSSFWorkbook(in);
        palette = book.getCustomPalette();
        evaluator = book.getCreationHelper().createFormulaEvaluator();

        table(book.getSheetAt(indexSheet));            
    }

    /**
     * (Each Excel sheet produces an HTML table) Generates an HTML table with no
     * cell, border spacing or padding.
     * 
     * @param sheet
     *            The Excel sheet.
     */
    private void table(final HSSFSheet sheet) {
        
        if (sheet == null) {
            return;
        }        
        
        if (sheet.getDrawingPatriarch() != null) {
            final List<HSSFShape> shapes = sheet.getDrawingPatriarch()
                    .getChildren();
            
            //final HSSFShape shapes = sheet.getDrawingPatriarch().g;
                                
            for (int i = 0; i < shapes.size(); ++i) {
            
                if (shapes.get(i) instanceof HSSFPicture) {
                    System.out.println("dentro del if");
                    try {
                        // Gain access to private field anchor.
                        final HSSFShape pic = shapes.get(i);
                        final Field f = HSSFShape.class
                                .getDeclaredField("anchor");
                        f.setAccessible(true);
                        final HSSFClientAnchor anchor = (HSSFClientAnchor) f
                                .get(pic);
                        // Store picture cell row, column and picture data.
                        if (!pix.containsKey(anchor.getRow1())) {
                            pix.put(anchor.getRow1(),
                                    new HashMap<Short, List<HSSFPictureData>>());
                        }
                        if (!pix.get(anchor.getRow1()).containsKey(
                                anchor.getCol1())) {
                            pix.get(anchor.getRow1()).put(anchor.getCol1(),
                                    new ArrayList<HSSFPictureData>());
                        }
                        pix.get(anchor.getRow1())
                                .get(anchor.getCol1())
                                .add(book.getAllPictures().get(
                                        ((HSSFPicture) pic).getPictureIndex()));
                    } catch (final Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        out.append("<table cellspacing='0' style='border-spacing:0; border-collapse:collapse;'>\n");
        for (rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows(); ++rowIndex) {
            tr(sheet.getRow(rowIndex));
        }
        out.append("</table>\n");
    }

    /**
     * (Each Excel sheet row becomes an HTML table row) Generates an HTML table
     * row which has the same height as the Excel row.
     * 
     * @param row
     *            The Excel row.
     */
    @SuppressWarnings("deprecation")
	private void tr(final HSSFRow row) {
        if (row == null) {
            return;
        }
        out.append("<tr ");
        // Find merged cells in current row.
        for (int i = 0; i < row.getSheet().getNumMergedRegions(); ++i) {
            final CellRangeAddress merge = row.getSheet().getMergedRegion(i);
            if (rowIndex >= merge.getFirstRow()
                    && rowIndex <= merge.getLastRow()) {
                mergeStart = merge.getFirstColumn();
                mergeEnd = merge.getLastColumn();
                break;
            }
        }
        out.append("style='");
        if (row.getHeight() != -1) {
            out.append("height: ")
                    .append(Math.round(row.getHeight() / 20.0 * 1.33333))
                    .append("px; ");
        }
        out.append("'>\n");
        for (colIndex = 0; colIndex < row.getLastCellNum(); ++colIndex) {
            td(row.getCell(colIndex));
        }
        out.append("</tr>\n");
    }

    /**
     * (Each Excel sheet cell becomes an HTML table cell) Generates an HTML
     * table cell which has the same font styles, alignments, colours and
     * borders as the Excel cell.
     * 
     * @param cell
     *            The Excel cell.
     */
    private void td(final HSSFCell cell) {
        int colspan = 1;
        if (colIndex == mergeStart) {
            // First cell in the merging region - set colspan.
            colspan = mergeEnd - mergeStart + 1;
        } else if (colIndex == mergeEnd) {
            // Last cell in the merging region - no more skipped cells.
            mergeStart = -1;
            mergeEnd = -1;
            return;
        } else if (mergeStart != -1 && mergeEnd != -1 && colIndex > mergeStart
                && colIndex < mergeEnd) {
            // Within the merging region - skip the cell.
            return;
        }
        out.append("<td ");
        if (colspan > 1) {
            out.append("colspan='").append(colspan).append("' ");
        }
        if (cell == null) {
            out.append("/>\n");
            return;
        }
        out.append("style='");
        final HSSFCellStyle style = cell.getCellStyle();
        // Text alignment
        switch (style.getAlignment()) {
        case CellStyle.ALIGN_LEFT:
            out.append("text-align: left; ");
            break;
        case CellStyle.ALIGN_RIGHT:
            out.append("text-align: right; ");
            break;
        case CellStyle.ALIGN_CENTER:
            out.append("text-align: center; ");
            break;
        default:
            break;
        }
        // Font style, size and weight
        final HSSFFont font = style.getFont(book);
        if (font.getBoldweight() == HSSFFont.BOLDWEIGHT_BOLD) {
            out.append("font-weight: bold; ");
        }
        if (font.getItalic()) {
            out.append("font-style: italic; ");
        }
        if (font.getUnderline() != HSSFFont.U_NONE) {
            out.append("text-decoration: underline; ");
        }
        out.append("font-size: ")
                .append(Math.floor(font.getFontHeightInPoints() * 0.8))
                .append("pt; ");
        // Cell background and font colours
        final short[] backRGB = style.getFillForegroundColorColor()
                .getTriplet();
        final HSSFColor foreColor = palette.getColor(font.getColor());
        if (foreColor != null) {
            final short[] foreRGB = foreColor.getTriplet();
            if (foreRGB[0] != 0 || foreRGB[1] != 0 || foreRGB[2] != 0) {
                out.append("color: rgb(").append(foreRGB[0]).append(',')
                        .append(foreRGB[1]).append(',').append(foreRGB[2])
                        .append(");");
            }
        }
        if (backRGB[0] != 0 || backRGB[1] != 0 || backRGB[2] != 0) {
            out.append("background-color: rgb(").append(backRGB[0]).append(',')
                    .append(backRGB[1]).append(',').append(backRGB[2])
                    .append(");");
        }
        // Border
        if (style.getBorderTop() != HSSFCellStyle.BORDER_NONE) {
            out.append("border-top-style: solid; ");
        }
        if (style.getBorderRight() != HSSFCellStyle.BORDER_NONE) {
            out.append("border-right-style: solid; ");
        }
        if (style.getBorderBottom() != HSSFCellStyle.BORDER_NONE) {
            out.append("border-bottom-style: solid; ");
        }
        if (style.getBorderLeft() != HSSFCellStyle.BORDER_NONE) {
            out.append("border-left-style: solid; ");
        }
        out.append("'>");
        String val = "";
        try {
            switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                val = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                // POI does not distinguish between integer and double, thus:
                final double original = cell.getNumericCellValue(),
                rounded = Math.round(original);
                if (Math.abs(rounded - original) < 0.00000000000000001) {
                    val = String.valueOf((int) rounded);
                } else {
                    val = String.valueOf(original);
                }
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                final CellValue cv = evaluator.evaluate(cell);
                switch (cv.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    out.append(cv.getBooleanValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    out.append(cv.getNumberValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    out.append(cv.getStringValue());
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                case Cell.CELL_TYPE_ERROR:
                    break;
                default:
                    break;
                }
                break;
            default:
                // Neither string or number? Could be a date.
                try {
                    val = sdf.format(cell.getDateCellValue());
                } catch (final Exception e1) {
                }
            }
        } catch (final Exception e) {
            val = e.getMessage();
        }
        if ("null".equals(val)) {
            val = "";
        }
        if (pix.containsKey(rowIndex)) {
            if (pix.get(rowIndex).containsKey(colIndex)) {
                for (final HSSFPictureData pic : pix.get(rowIndex)
                        .get(colIndex)) {
                    out.append("<img alt='Image in Excel sheet' src='data:");
                    out.append(pic.getMimeType());
                    out.append(";base64,");
                    try {
                        out.append(new String(
                                Base64.encodeBase64(pic.getData()), "US-ASCII"));
                    } catch (final UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    out.append("'/>");
                }
            }
        }
        out.append(val);
        out.append("</td>\n");
    }

    public String getHTML() {
        return out.toString();
    }
}