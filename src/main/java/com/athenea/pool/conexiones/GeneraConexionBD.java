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
public class GeneraConexionBD {
	
	private final String driverBBDD;
	private final String direccionBBDD;
	private final String userBBDD;
	private final String passBBDD;
    
    private String cadenaConexion;
    private Connection conexion = null;
    
    {
    	cadenaConexion = null;
    	conexion = null;  
    }

    private GeneraConexionBD(String driverBBDD,String direccionBBDD,String userBBDD,String passBBDD){
    	this.driverBBDD = driverBBDD;
    	this.direccionBBDD = direccionBBDD;
    	this.userBBDD = userBBDD;
    	this.passBBDD = passBBDD;
    }

    
	public String getDriverBBDD() {
		return driverBBDD;
	}


	public String getDireccionBBDD() {
		return direccionBBDD;
	}


	public String getUserBBDD() {
		return userBBDD;
	}


	public String getPassBBDD() {
		return passBBDD;
	}


	public Connection devolverConexion (){    	    
		System.out.println("Inicio devolverConexion ");
        
        try 
        {
            Class.forName(driverBBDD).newInstance();
            
            cadenaConexion = direccionBBDD + 
            				 userBBDD + "&" + passBBDD;   
            conexion = DriverManager.getConnection(cadenaConexion);           
        } 
          catch (ClassNotFoundException ex) {
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
	
    public static Connection createInstance (String driverBBDD,String direccionBBDD,String userBBDD,String passBBDD) {    	
    		GeneraConexionBD conectBD = new GeneraConexionBD (driverBBDD,direccionBBDD,userBBDD,passBBDD);
            return conectBD.devolverConexion();
    }
    
}
