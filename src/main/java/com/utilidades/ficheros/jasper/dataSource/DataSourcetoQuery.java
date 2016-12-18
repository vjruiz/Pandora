/*
 */
package com.utilidades.ficheros.jasper.dataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.utilidades.ficheros.jasper.rutinasValidacion.AbstractRutinValidation;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author vjruiz
 */
public class DataSourcetoQuery extends AbstractDataSource {
    
    private int indicePoscionInicialBarraInvertida = 0;
    private int indicePoscionFinalBarraInvertida = -1;
    private String registroEntradaReport;
    
    public DataSourcetoQuery () {
        listData = new ArrayList();
    }

    @Override
    public List getListData() {
        return listData;
    }

    @Override
    public void setListaData(List listData) {
        this.listData = listData;
    }    

    @Override
    public boolean next() throws JRException {
        return ++indiceParticipanteActual < listData.size();
    }

    @Override
    public String toString() {
        return "DataSourcetoQuery .-" 
                + "Entrada Lista Almacenada =" + listData 
                + ", indiceParticipanteActual=" + indiceParticipanteActual;
    }

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {

       String valorEtiquetaReport = null;
        
        if (!"class".equals(jrf.getName())){
            
            if (indicePoscionFinalBarraInvertida == -1){
                
                registroEntradaReport = null;                
                registroEntradaReport = (String) listData.get(indiceParticipanteActual);                
                indicePoscionFinalBarraInvertida = registroEntradaReport.indexOf("$/");
                
            }

            indicePoscionInicialBarraInvertida = indicePoscionFinalBarraInvertida;
            indicePoscionInicialBarraInvertida = 0;            

            valorEtiquetaReport =         
                registroEntradaReport.substring(indicePoscionInicialBarraInvertida, 
                                                indicePoscionFinalBarraInvertida);

            indicePoscionFinalBarraInvertida = indicePoscionFinalBarraInvertida + 2;


            registroEntradaReport =  
                    registroEntradaReport.substring(indicePoscionFinalBarraInvertida, 
                                                registroEntradaReport.length());

            indicePoscionFinalBarraInvertida = registroEntradaReport.indexOf("$/");
        
        }
        
         return valorEtiquetaReport;

    }

    @Override
    //public boolean addElement(Object objetoEntrada) {  
    public boolean addElement(Object registroInput) {                 
        return this.listData.add(registroInput);
    }
    
    @Override
    public boolean loadDataSource(Connection inputConnection,
                String queryInputData,AbstractRutinValidation flowValidation) {
        
        boolean controlResultado;

        try {
            sentenciaSelectTrabajo = inputConnection.createStatement();            
            controlResultado = this.loadDataSource(
                    sentenciaSelectTrabajo.executeQuery(queryInputData),
                    flowValidation); 
        } catch (SQLException ex) {
            Logger.getLogger(DataSourcetoQuery.class.getName()).log(Level.SEVERE, null, ex);
            controlResultado = false;
        }
        
        return controlResultado;
    }
    
    /**
     *
     * @param resultado
     * @param flowValidation
     * @return
     */
    @Override
    public boolean loadDataSource(ResultSet resultado,
                                AbstractRutinValidation flowValidation) {
        
        boolean controlResultado;
        StringBuilder apoyoComposicion = new StringBuilder( 1000 );
        String controlValidacion;
        
        try {
            while (resultado.next()) {    
                
                ResultSetMetaData rsmd = resultado.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    apoyoComposicion.append(resultado.getString(i)).append("$/");
                }
                                
                // si viene informado la clase flowValidation se la invoca antes de incluir el 
                // registro para que sea validado, se incluyen los registros validados con los valores
                // que nos devuelven las rutinas ( por si es necesario modifcar valores calculados)
                // Se espera que las rutinas devuelvan un String con la siguiente estructura:
                //  1 posicion para el resultado validacion:
                //    0.- Registro aceptado 1.- registro rechazado
                //  1 posicion separadora 
                //    informada con '$;'
                //  1 String varchar con la descricion del error ( si no se produce error se informa
                //    un espacio.
                //  1 posicion separadora 
                //    informada con '$/'
                //  El resto del registro estara compuesto por las
                // etiquetas (cabecera informe) + '$;' + valorEtiqueta + '$/' que se incluiran en el 
                // informe
                //
                // En caso contrario se añade directamente el registro para que luego
                // aparezca en el informe.
                if (flowValidation != null){
                    controlValidacion = flowValidation.runFlowValidation(apoyoComposicion.toString());
                    this.tratarRespuestaFlowValidation(controlValidacion);                    
                } else {                    
                    this.addElement(apoyoComposicion.toString());
                    
                }
                apoyoComposicion.delete(0, 1000);
                
            }
            controlResultado=true;
            
        }      catch (SQLException ex) {
            controlResultado=false;
            Logger.getLogger(DataSourcetoQuery.class.getName()).log(Level.SEVERE, null, ex);
        }            
        
        return controlResultado;
    }
    
    private String tratarRespuestaFlowValidation (String resultadoComunicacion)
    {
        int resultadoEjecucion;
        String dsObservacionesEjecucion = null;
                
        resultadoEjecucion = 
            Integer.parseInt(resultadoComunicacion.substring(0, 1));

        if (resultadoEjecucion == 0){        
            this.addElement(resultadoComunicacion.substring(3, resultadoComunicacion.length()));
        }  
        
        // se devuelve el resultado de las rutinas por si en versiones posteriores 
        // se considera necesario tratar los registros que se rechazan.
        return resultadoEjecucion + dsObservacionesEjecucion;
            
    }
}
