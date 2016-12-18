/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.athenea.pool.conexiones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.athenea.enums.TipoConexiones;

/**
 *
 * @author vjruiz
 */
public class ConexionBD {
    
    private ConexionBD(){
    }
    
    public Connection devolverConexion (String destinoConexion){
        
        String cadenaConexion;
        Connection conexion = null;
        TipoConexiones enumOpciones;
        
        System.out.println("devolverConexion" + destinoConexion);
        
        enumOpciones = TipoConexiones.valueOf(destinoConexion);
        
        try {
            Class.forName("org.postgresql.Driver").newInstance();
            cadenaConexion = enumOpciones.getDireccionBBDD() + enumOpciones.getUserPassBBDD();   
            conexion = DriverManager.getConnection(cadenaConexion);
            
        } catch (ClassNotFoundException ex) {
            System.out.println("devolverConexion.-Error de conexion" + ex);
        } catch (InstantiationException e) {
            System.out.println("devolverConexion.- Error de conexion" + e);
		} catch (IllegalAccessException e) {
            System.out.println("devolverConexion.- Error de conexion" + e);
		} catch (SQLException e) {
            System.out.println("devolverConexion.- Error de conexion" + e);
		} 
        
        return conexion;
    }
    
    public static ConexionBD getInstance() {
        return NewSingletonHolder.INSTANCE;
    }
    
    private static class NewSingletonHolder {

        private static ConexionBD INSTANCE = new ConexionBD();
    }
}
