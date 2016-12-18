package com.utilidades.ficheros;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;

@Component("directorios")
public class ExportadorExcel {		
	
	private int contadorRegistrosExportados;
	
	{
		contadorRegistrosExportados = 0;
	}
	
	public ExportadorExcel() {
		super();
	}
	
	public File generarExcel(ResultSet directorioEntrada,
							String directorioSalida,
							String nombreArchivoSalida,
							DelegateExecution delegateExecution) 
	{
		ArrayList<String> arrayEstructuraCabecera =
				recuperarEstructuraCampos(directorioEntrada);
		
		ArrayList<String> arrayEstructuraDatos =
				recuperarValoresCampos(directorioEntrada,arrayEstructuraCabecera.size());
		
		//creamos el Libro
        @SuppressWarnings("resource")
		HSSFWorkbook libroExcel = new HSSFWorkbook();

        // Se crea una hoja dentro del libro
        HSSFSheet hoja = libroExcel.createSheet();
        
        // Se crea una fila dentro de la hoja para los datos de la cabecera y rellenamos la cabecera  
        int Ifila=0;
        HSSFRow filaCreada = hoja.createRow(Ifila);
        
        int Icelda = 0;
        for (String datoCabecera : arrayEstructuraCabecera) 
        {
        	
            // Se crea una celda dentro de la fila                        
            HSSFCell celdaCreada = filaCreada.createCell(Icelda);   
            
            // Se crea el contenido de la celda y se mete en ella.
            HSSFRichTextString textoRecuperado = new HSSFRichTextString(datoCabecera);
            celdaCreada.setCellValue(textoRecuperado);
            Icelda++;
		}
        
        // Despues de escribir la cabecera nos posicionamos en la siguiente fila
        Ifila++;
        filaCreada = hoja.createRow(Ifila);
        
        Icelda = 0;
        for (String datoDato : arrayEstructuraDatos) 
        {
        	
            // Se crea una celda dentro de la fila                        
            HSSFCell celdaCreada = filaCreada.createCell(Icelda);   
            
            // Se crea el contenido de la celda y se mete en ella.
            HSSFRichTextString textoRecuperado = new HSSFRichTextString(datoDato);
            celdaCreada.setCellValue(textoRecuperado);

            Icelda++;
            // si el indice de datos escritos llega al total de cabecera se pagina la fila
            if (Icelda >=arrayEstructuraCabecera.size())
            {
            	Icelda = 0;
            	Ifila++; 
            	filaCreada = hoja.createRow(Ifila);
            }
            
		}
        
        // Se salva el libro.
        File archivoXLS = null;
        try 
        {
        	archivoXLS = new File(
        			directorioSalida + 
        			nombreArchivoSalida);
        	if(archivoXLS.exists()) archivoXLS.delete();
        	archivoXLS.createNewFile();        	
            FileOutputStream salidaEscribir = new FileOutputStream(archivoXLS);
        	libroExcel.write(salidaEscribir);
        	salidaEscribir.close();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }       

        delegateExecution.setVariable("contadorRegistrosExportados", this.contadorRegistrosExportados);
        
		return archivoXLS;
		
	}
	
	private ArrayList<String> recuperarEstructuraCampos(ResultSet resultadoApoyoDestino) 
	{
		
		ArrayList<String> arraySalida = new ArrayList<String>();		
		
		try 
		{					
			if (resultadoApoyoDestino != null) 
			{
                ResultSetMetaData rsmd = resultadoApoyoDestino.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                	arraySalida.add(rsmd.getColumnName(i));
                }
			}									
		} 
		catch (SQLException e) {
			System.out.println("no existen datos" + e );
		}
		catch (NullPointerException e) {
			System.out.println("no existen datos" + e );
		}
		
		return (ArrayList<String>) arraySalida;
		
	}
	
	private ArrayList<String> recuperarValoresCampos(ResultSet resultadoApoyoDestino, int totalCampos) {
		
		ArrayList<String> arraySalida = new ArrayList<String>();		
		
		try 
		{					
			while (resultadoApoyoDestino.next()) 
			{
                for (int i = 1; i <= totalCampos; i++) {
                	arraySalida.add(resultadoApoyoDestino.getString(i));
                }
                
                this.contadorRegistrosExportados ++;
			}								
		} 
		catch (SQLException e) {
			System.out.println("no existen datos" + e );
		}
		catch (NullPointerException e) {
			System.out.println("no existen datos" + e );
		}
		
		return (ArrayList<String>) arraySalida;
		
	}	
	
}
