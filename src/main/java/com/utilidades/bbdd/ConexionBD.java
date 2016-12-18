/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utilidades.bbdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author vjruiz
 */
public class ConexionBD {

    String driverConexion;
    String cadenaConexion;
    Connection conexion;
    TipoConexiones enumOpciones;
    
    {
    	driverConexion = null;
        cadenaConexion = null;
        conexion = null;
        enumOpciones = null;
    }
	
    private ConexionBD(){
    }           

	public ConexionBD(String driverConexion, String cadenaConexion, Connection conexion, TipoConexiones enumOpciones) {
		super();
		this.driverConexion = driverConexion;
		this.cadenaConexion = cadenaConexion;
		this.conexion = conexion;
		this.enumOpciones = enumOpciones;
	}

	public String getDriverConexion() {
		return driverConexion;
	}

	public void setDriverConexion(String driverConexion) {
		this.driverConexion = driverConexion;
	}

	public String getCadenaConexion() {
		return cadenaConexion;
	}

	public void setCadenaConexion(String cadenaConexion) {
		this.cadenaConexion = cadenaConexion;
	}

	public Connection getConexion() {
		return conexion;
	}

	public void setConexion(Connection conexion) {
		this.conexion = conexion;
	}

	public TipoConexiones getEnumOpciones() {
		return enumOpciones;
	}

	public void setEnumOpciones(TipoConexiones enumOpciones) {
		this.enumOpciones = enumOpciones;
	}

	public Connection devolverConexion (String destinoConexion){           
        
		this.setEnumOpciones(TipoConexiones.valueOf(destinoConexion));
        this.setDriverConexion(enumOpciones.getDriverBBDD());
        this.setCadenaConexion(enumOpciones.getDireccionBBDD() + enumOpciones.getUserPassBBDD());	
        
        try 
        {
        	
            Class.forName(driverConexion).newInstance();                        
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
	
	public Connection devolverConexionConstructor ()
	{                   
        
        try 
        {        	        	
        	
            Class.forName(driverConexion).newInstance();                        
            conexion = DriverManager.getConnection(cadenaConexion);
            
        } catch (ClassNotFoundException ex) {
            System.out.println("devolverConexionConstructor.-Error de conexion" + ex);
        } catch (InstantiationException e) {
            System.out.println("devolverConexionConstructor.- Error de conexion" + e);
		} catch (IllegalAccessException e) {
            System.out.println("devolverConexionConstructor.- Error de conexion" + e);
		} catch (SQLException e) {
            System.out.println("devolverConexionConstructor.- Error de conexion" + e);
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
