/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utilidades.logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.utilidades.bbdd.ConexionBD;

/**
 *
 * @author vjruiz
 */
public class LoggerResultado {   
	
	
	public static void insertarRegistroLogger(String Id_flujo_tarea,String id_tarea, 
												 int idProceso, String resultado,
											  String ds_error, int contadorTratados) throws SQLException 
	{
		Connection conexionEncelado;
		Statement sentencia_insert; 
		
		conexionEncelado = ConexionBD.getInstance().devolverConexion("encelado");	
		
		String queryApoyo = 
				"INSERT INTO encelado.im_datos_ejecucion("
				+ "id_flujo_tarea_asoc,entidad,fecha_ejecucion, id_proceso,resultado,total_tratado, ds_error)"
				+ " VALUES ("
				+ "'" + Id_flujo_tarea + "',"
				+ "'" + id_tarea + "',"
				+ "current_timestamp," 
				+ "'" + idProceso + "',"
				+ "'" + resultado + "',"				
				+ "'" + contadorTratados + "',"
				+ "'" + ds_error + "'"				
				+ ")"
				;					

		sentencia_insert = conexionEncelado.createStatement();
		int valorDevuelto = sentencia_insert.executeUpdate(queryApoyo);
		
		conexionEncelado.close();
		sentencia_insert.close();
						
	}

	public static void actualizarResultadoLogger(String id_tarea, int idProceso, 
									String resultado,String ds_error, int contadorTratados) throws SQLException 
	{
		Connection conexionEncelado;
		Statement sentencia_update; 
		
		conexionEncelado = ConexionBD.getInstance().devolverConexion("encelado");	
		
		String queryApoyo = 
				"UPDATE encelado.im_datos_ejecucion"
				+ " SET resultado='" + resultado  +"',"
				+ " total_tratado=" + contadorTratados  +","
				+ " ds_error ='" + ds_error +"'" 				
				+ " where entidad='" + id_tarea + "'"
				+ " and id_proceso='" + idProceso + "'"
				;					

		System.out.println( "actualizarResultadoLogger" + queryApoyo);
		sentencia_update = conexionEncelado.createStatement();
		int valorDevuelto = sentencia_update.executeUpdate(queryApoyo);
		
		conexionEncelado.close();
		sentencia_update.close();
						
	}
	
	public static void actualizarResultadoLoggerSinTotal(
						String id_tarea, int idProceso, 
						String resultado, String ds_error) 
				throws SQLException 
	{
		Connection conexionEncelado;
		Statement sentencia_update;

		conexionEncelado = ConexionBD.getInstance().devolverConexion("encelado");

		String queryApoyo = "UPDATE encelado.im_datos_ejecucion" 
						+ " SET resultado='" + resultado + "'," 
						+ " ds_error ='" + ds_error + "'" 
						+ " where entidad='"+ id_tarea + "'" 
						+ " and id_proceso='" + idProceso + "'"
						;

		System.out.println("actualizarResultadoLogger" + queryApoyo);
		sentencia_update = conexionEncelado.createStatement();
		int valorDevuelto = sentencia_update.executeUpdate(queryApoyo);
		
		conexionEncelado.close();
		sentencia_update.close();

	}
    
}
