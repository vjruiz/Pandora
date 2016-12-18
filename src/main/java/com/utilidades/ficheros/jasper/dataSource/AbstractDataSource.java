/*
 */
package com.utilidades.ficheros.jasper.dataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.utilidades.ficheros.jasper.rutinasValidacion.AbstractRutinValidation;

import net.sf.jasperreports.engine.JRDataSource;

/**
 *
 * @author vjruiz
 * @param <T>
 */
public abstract class AbstractDataSource <T> implements JRDataSource {
    public List<T> listData;
    public Statement sentenciaSelectTrabajo; 
    public int indiceParticipanteActual = -1;

    public abstract List<?> getListData();
    public abstract void setListaData(List<?> listData);
    /**
     *
     * @param resultado
     * @param flowValidation
     * @return
     */    
    public abstract boolean loadDataSource(ResultSet resultado,
                                    AbstractRutinValidation flowValidation);
    public abstract boolean loadDataSource(Connection inputConnection,
                                    String queryInputData,
                                    AbstractRutinValidation flowValidation);
    
    public abstract boolean addElement(Object registroInput);
    
    
}
