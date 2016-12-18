
package com.utilidades.ficheros.jasper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 * Esta clase genera/devuelve y/o exporta informes tipo JasperReport, tiene dos modos 
 * de uso en base a los parametros de entrada al metodo ejecutarInforme():
 * 
 *  1.- Genera/Devuelve/Exporta el informe JasperPrint a partir del fichero 
 * .jasper que se indique en la entrada. La exportacion se al 
 * realiza al directorio/fichero de salida/tipo archivo
 * que se le indique en la entrada.
 * Los tipos de archivos soportados en la exportacion son.-
 * (html,csv,docx,xls,xlsx,pdf).
 * 
 *  2.- Genera/Devuelve el informe JasperPrint a partir del fichero .jasper 
 * que se indique en la entrada.
 * 
 *      IMPORTANTE .- Esta clase autogestiona su creacion, se debe usar el metodo 
 *      estatico getInstance() para obtener una instancia con la que trabajar.
 * 
 * @author vjruiz
 * @version 21/02/2016/A
 * @see <a href = "http://community.jaspersoft.com/"> Comunidad JaspertReport
 *      para obtener documentacion uso de reportes/ficheros. </a>  
 *          
 */
@SuppressWarnings("deprecation")
public class EjecutarReportes {    

     /**
     * Constructor por defecto de la clase, la misma autogestiona su creacion
     * mediante el metodo estatico getInstance().
     */
    private EjecutarReportes() {
    }
    
     /**
     * Genera/Devuelve/Exporta el informe JasperPrint a partir del fichero 
     * .jasper que se indique en la entrada. La exportacion se al 
     * realiza al directorio/fichero de salida/tipo archivo
     * que se le indique en la entrada.
     * 
     * @param conexion Conexion de BBDD en la que se debe ejecutar el reporte.     
     * @param ficheroJasper Reporte (.jasper) a ejecutar,debe recubrirse en 
     * clase FILE parasu correcto tratamiento.     
     * @param pathFicheroSalida Path del sistema donde se guarda el 
     * ficheroJasper exportado.
     * @param nombreFicheroSalida Nombre del fichero de salida con el que se 
     * guarda el ficheroJasper exportado.No se debe informar la extension, 
     * la funcion concatena el nombre y el tipoReporte.
     * @param tipoReporte Clase de fichero de salida a la que se exportara el 
     * reporte .jasper. Los tipos de archivos soportados en la exportacion son:
     * (html,csv,docx,xls,xlsx,pdf)
     * @param parametros Parametros entrada al reporte .jasper. 
     * (+info en el parametro de clase see).
     * @param datasource DataSource entrada al reporte .jasper. 
     * (+info en el parametro de clase see).
     * 
     * @return Retorna un fichero tipo JasperPrint para su tratamiento.
     * 
     * @throws net.sf.jasperreports.engine.JRException 
     * @throws java.io.FileNotFoundException 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */

    public JasperPrint ejecutarInforme(Connection conexion,
                                FileInputStream ficheroJasper,
                                String pathFicheroSalida,
                                String nombreFicheroSalida,
                                String tipoReporte,
                                Map<String, String> parametros,
                                JRDataSource datasource) 
            throws JRException, FileNotFoundException, 
            ClassNotFoundException, SQLException
    {        	             
        JasperPrint print;
        String ficheroSalida;
        
        print = this.ejecutarInforme(conexion, ficheroJasper, parametros, 
                    datasource);        
                
        // Se monta el nombre del fichero de salida añadiendole la extension 
        // a partir del tipo de reporte solicitado.
        ficheroSalida = pathFicheroSalida 
                + nombreFicheroSalida
                + "." + tipoReporte;       
        System.out.println("tipo reporte" + tipoReporte);
        this.exportarFichero(tipoReporte, print, ficheroSalida);
        return print;
    }
    
     /**
     * Genera/Devuelve el informe JasperPrint a partir del fichero .jasper 
     * que se indique en la entrada.
     * 
     * @param conexion Conexion de BBDD en la que se debe ejecutar el reporte.     
     * @param ficheroJasper Reporte (.jasper) a ejecutar,debe recubrirse en 
     * clase FILE parasu correcto tratamiento.     
     * @param parametros Parametros entrada al reporte .jasper. 
     * (+info en el parametro de clase see).
     * @param datasource DataSource entrada al reporte .jasper. 
     * (+info en el parametro de clase see).
     * 
     * @return Retorna un fichero tipo JasperPrint para su tratamiento.
     * 
     * @throws net.sf.jasperreports.engine.JRException 
     * @throws java.io.FileNotFoundException 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public JasperPrint ejecutarInforme(Connection conexion,FileInputStream ficheroJasper,
                    Map<String, String> parametros,JRDataSource datasource)                        
            throws JRException, FileNotFoundException, 
            ClassNotFoundException, SQLException
    {       
        JasperPrint print;
        System.out.println("ficheroJasper" + ficheroJasper.toString());
        
        // si nos informan DataSource lo utilizamos en lugar de la conexion a BBDD
        if (datasource != null){            
            JasperReport report;
            report = (JasperReport) JRLoader.loadObject(ficheroJasper);        
            print = JasperFillManager.fillReport(report,(Map) parametros, datasource);
        }else {            
            JasperReport report;
            report = (JasperReport) JRLoader.loadObject(ficheroJasper);        
            print = JasperFillManager.fillReport(report,(Map) parametros, conexion);
            }        
        
        return print;
    }
    
    public void exportarFichero(String tipoReporte,JasperPrint jasperPrint,
                                String ficheroSalida) 
            throws JRException            
    {        
        @SuppressWarnings("rawtypes")
		JRExporter exporter = null;

        
        switch (tipoReporte) {
            case "HTML":
            case "html":
                exporter = new JRHtmlExporter();                 
                break;    
            case "CSV":
            case "csv":
                exporter = new JRCsvExporter ();                 
                break;                 
            case "DOCX":
            case "docx":                
                exporter = new JRDocxExporter();                 
                break;                  
            case "XLS":
            case "xls":                
                exporter = new JRXlsExporter();                 
                break;  
            case "XLSX":
            case "xlsx":                  
                exporter = new JRXlsxExporter();                
                break;
            case "PDF":                
            case "pdf":                
                exporter = new JRPdfExporter();
                break;
            default:
                throw new AssertionError();
        } 

        exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint); 
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE,new java.io.File(ficheroSalida));        
        exporter.exportReport();
    }
    
     /**
     * Generador estatico de nuevas instancias de la clase .this
     * 
     * @return Retorna una instancia nueva de tipo EjecutarReportes
     * 
     */    
    public static EjecutarReportes getInstance() {
        return NewSingletonHolder.INSTANCE;
    }
    
    private static class NewSingletonHolder {

        private static EjecutarReportes INSTANCE = new EjecutarReportes();
    }
    
}
