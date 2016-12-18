/*
 */
package com.utilidades.ficheros.jasper.rutinasValidacion;

import java.sql.Connection;

/**
 *
 * @author vjruiz
 */
public abstract class AbstractRutinValidation {
    Connection conexion;
        //public abstract boolean loadDataSource(Connection inputConnection,String queryInputData);
    public abstract void cargarConexion(Connection conexion);
    public abstract String runFlowValidation(String flowValidation);
    
}
