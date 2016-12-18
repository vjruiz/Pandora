package com.spring.daocore;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.model.BBDDDefinida;
import com.utilidades.bbdd.ConexionBD;

@Repository("daoaddbbdd")
public class DaoAddBBDD implements IDao
{	
	Connection conexion;
	Statement sentenciaSelect;
	Statement sentencia_insert;
    Statement sentencia_Delete;
 	ResultSet resultadoApoyo;

	//Variables de trabajo del programa
	String querySelectApoyo;
	String queryInsertApoyo;
 	String administrativoApoyo; 	
	
 	{
 		conexion = null;
 		sentenciaSelect = null;
 		sentencia_insert = null;
 		sentencia_Delete = null;
 	 	resultadoApoyo  = null;
 	 	querySelectApoyo = null;
 		queryInsertApoyo = null;
 	 	administrativoApoyo = null; 	 	
 		
 	}
 	
	public DaoAddBBDD() {
	}
	
	@Override
	public boolean altaObjeto(String registroEntrada) 
	{
		boolean controlInsert = false;
		BBDDDefinida objetoAlta = null;
		
		try {
			objetoAlta = new ObjectMapper().readValue(registroEntrada, BBDDDefinida.class);
		} 
		catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String queryApoyo = 
				"INSERT INTO encelado.enc_bbdd_conexiones_definidas("
				+ "id_conexion_bd, ob_conexion_bd, id_usuario, id_contrasena, ds_driver_bbdd,"
				+ "ds_cadena_conexion_bbdd,resto_ds_cadena_conexion_bbdd)"	
				+ " VALUES ("
				+ "'" + objetoAlta.getId_conexion_bd() + "'," 
				+ "'" + objetoAlta.getOb_conexion_bd() + "'," 
				+ "'" + objetoAlta.getId_usuario() + "',"
				+ "'" + objetoAlta.getId_contrasena() + "',"
				+ "'" + objetoAlta.getDs_driver_bbdd() + "',"
				+ "'" + objetoAlta.getDs_cadena_conexion_bbdd() + "',"
				+ "'" + objetoAlta.getResto_ds_cadena_conexion_bbdd() + "'"				
				+ ");"
				;				

		try {
			conexion = this.realizarConexion("encelado");										
			sentencia_insert  = conexion.createStatement();
			int valorDevuelto = sentencia_insert.executeUpdate(queryApoyo);
			
			if (valorDevuelto < 0)
			{
				controlInsert = false;
			} else 
			{
				controlInsert = true;				
			}
			
		} catch (SQLException e) 
		{
			e.printStackTrace();
			controlInsert = false;			
		}
		finally 
		{
			this.cerrarRecursosDB2();
		}
		return controlInsert;
	}
	
	
	@Override
	public boolean modificarObjeto(String registroEntrada) {
		boolean controlModificacion = false;
		BBDDDefinida objetoModificar = null;
		
		try {
			objetoModificar = new ObjectMapper().readValue(registroEntrada, BBDDDefinida.class);
		} 
		catch (IOException e1) {
			e1.printStackTrace();
		}
		
		controlModificacion = this.borrarObjeto(objetoModificar);
		if (controlModificacion)
			{
			controlModificacion = this.altaObjeto(registroEntrada);
			};
		

		return controlModificacion;
	}		
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> ArrayList<T> recuperarListado() {
		
		ArrayList<BBDDDefinida> arraySalida = null;
		
		querySelectApoyo = "select id_conexion_bd, ob_conexion_bd, id_usuario, id_contrasena, "
				+ "ds_driver_bbdd, ds_cadena_conexion_bbdd,resto_ds_cadena_conexion_bbdd"
				+ " from encelado.enc_bbdd_conexiones_definidas";		
		
		try 
		{
			conexion = this.realizarConexion("encelado");			
			resultadoApoyo = this.recuperarDatos(querySelectApoyo);
			// solo si tenemos datos de la copiaBD entramos a rellenar el objeto					
			if (resultadoApoyo != null) 
			{
				arraySalida = rellenarArray(resultadoApoyo);
			}
						
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NullPointerException e) {
			System.out.println("no existen datos");;
		}
		finally {
			this.cerrarRecursosDB2();
		}
		
		return (ArrayList<T>) arraySalida;
		
	}

	@Override
	public Object devolverObjeto(Object registroEntrada) {
		
		BBDDDefinida registroEntradaMetodo = (BBDDDefinida) registroEntrada;
		
		querySelectApoyo = "select id_conexion_bd, ob_conexion_bd, id_usuario, id_contrasena, "
				+ "ds_driver_bbdd, ds_cadena_conexion_bbdd,resto_ds_cadena_conexion_bbdd"
				+ " from encelado.enc_bbdd_conexiones_definidas"
				+ " where id_conexion_bd = '" + registroEntradaMetodo.getId_conexion_bd() +"'";
		
		try {
			conexion = this.realizarConexion("encelado");							
			
			resultadoApoyo = this.recuperarDatos(querySelectApoyo);

			if (resultadoApoyo != null) 
			{
				while (resultadoApoyo.next()) 
				{
					registroEntradaMetodo.setId_conexion_bd(resultadoApoyo.getString(1));
					registroEntradaMetodo.setOb_conexion_bd(resultadoApoyo.getString(2));
					registroEntradaMetodo.setId_usuario(resultadoApoyo.getString(3));
					registroEntradaMetodo.setId_contrasena(resultadoApoyo.getString(4));
					registroEntradaMetodo.setDs_driver_bbdd(resultadoApoyo.getString(5));			
					registroEntradaMetodo.setDs_cadena_conexion_bbdd(resultadoApoyo.getString(6));
					registroEntradaMetodo.setResto_ds_cadena_conexion_bbdd(resultadoApoyo.getString(7));
				}				
			} 
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			this.cerrarRecursosDB2();
		}
		
		return registroEntradaMetodo;
	}
	
	@Override
	public <T> boolean borrarListadoObjetos(ArrayList<T> registroEntrada) {
		
		BBDDDefinida objetoDeseaBorrar = new BBDDDefinida();
		for (T claveObjetoeseaBorrar:registroEntrada) {			
			objetoDeseaBorrar.setId_conexion_bd ( (String) claveObjetoeseaBorrar);
			this.borrarObjeto(objetoDeseaBorrar);
		}
		
		return true;
	}
	
	private boolean borrarObjeto(Object registroEntrada) 
	{		
		//variables de trabajo del metodo
		int controlDelete;
		BBDDDefinida objetoDeseaBorrar = (BBDDDefinida) registroEntrada;
		
		String queryDeleteApoyo = "delete from encelado.enc_bbdd_conexiones_definidas"
				+ " where id_conexion_bd  = '" + objetoDeseaBorrar.getId_conexion_bd()+ "'";

		try {
			conexion = this.realizarConexion("encelado");										
			sentencia_Delete  = conexion.createStatement();
			controlDelete = sentencia_Delete.executeUpdate(queryDeleteApoyo);
			// solo si tenemos datos de la copiaBD entramos a rellenar el objeto
			if (controlDelete < 0) {
				
				System.out.println("Error en delete" + controlDelete);
				return false;
				
			} else return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		finally {
			this.cerrarRecursosDB2();
		}

	}	

	private ArrayList<BBDDDefinida> rellenarArray(ResultSet rsentrada) throws SQLException
	{
		//variables de trabajo del metodo
		ArrayList<BBDDDefinida> arraySalida = new ArrayList<BBDDDefinida>();

		
		while (rsentrada.next()) 
		{

			BBDDDefinida objetoSalida = new BBDDDefinida();
			
			objetoSalida.setId_conexion_bd(rsentrada.getString(1));
			objetoSalida.setOb_conexion_bd(rsentrada.getString(2));
			objetoSalida.setId_usuario(rsentrada.getString(3));
			objetoSalida.setId_contrasena(rsentrada.getString(4));
			objetoSalida.setDs_driver_bbdd(rsentrada.getString(5));
			objetoSalida.setDs_cadena_conexion_bbdd(rsentrada.getString(6));;		
			objetoSalida.setResto_ds_cadena_conexion_bbdd(rsentrada.getString(7));			

			arraySalida.add(objetoSalida);
					
		}
		
		return arraySalida;
		
	}
	
	private Connection realizarConexion(String tipoConexion) throws SQLException
	{
		
		Connection nuevaConexion;

		// ---Conexion------------//

		nuevaConexion = ConexionBD.getInstance().devolverConexion(tipoConexion);
		//nuevaConexion.setAutoCommit(true);

		// ---Conexion realizada

		return nuevaConexion;
		
	}

	private ResultSet recuperarDatos(String querySelect) 
	{
		    
		ResultSet resultado = null;

		try {
			System.out.println(querySelect);
			sentenciaSelect = conexion.createStatement();
			resultado = sentenciaSelect.executeQuery(querySelect);			

		} catch (SQLException e) {

			e.printStackTrace();
			System.out.println(e);
			resultado = null;

		}

		return resultado;
	}
	
	private void cerrarRecursosDB2() 
	{
		try 
		{
			
			if (conexion != null) {conexion.close();};
			if (sentenciaSelect != null) {sentenciaSelect.close();};
			if (sentencia_insert != null) {sentencia_insert.close();};
			if (sentencia_Delete != null) {sentencia_Delete.close();};
			if (resultadoApoyo!= null) {resultadoApoyo.close();};
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public <T> ArrayList<T> recuperarListadoVigente() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
