package com.spring.daocore;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.model.ServerDefinido;
import com.utilidades.bbdd.ConexionBD;

public class DaoAddSever implements IDao
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
 	
	public DaoAddSever() {
	}	

	@Override
	public boolean altaObjeto(String registroEntrada) 
	{
		boolean controlInsert = false;
		ServerDefinido objetoAlta = null;
		
		try {
			objetoAlta = new ObjectMapper().readValue(registroEntrada, ServerDefinido.class);
		} 
		catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String queryApoyo = 
				"INSERT INTO encelado.enc_mail_servidores_definidos("
				+ "id_server, ob_server, id_usuario, id_crontrasena, ds_dominio,"
				+ "ds_host_smtp, id_puerto_host)"	
				+ " VALUES ("
				+ "'" + objetoAlta.getId_server() + "'," 
				+ "'" + objetoAlta.getOb_server() + "'," 
				+ "'" + objetoAlta.getId_usuario() + "',"
				+ "'" + objetoAlta.getId_contrasena() + "',"
				+ "'" + objetoAlta.getDs_dominio() + "',"
				+ "'" + objetoAlta.getDs_host_smtp() + "',"
				+ "'" + objetoAlta.getId_puerto_host() + "'"					
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
			this.cerrarRecursosDB2();;
		}
		return controlInsert;
	}
	
	@Override
	public boolean modificarObjeto(String registroEntrada) {
		boolean controlModificacion = false;
		ServerDefinido objetoModificar = null;
		
		try {
			objetoModificar = new ObjectMapper().readValue(registroEntrada, ServerDefinido.class);
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
		
		ArrayList<ServerDefinido> arraySalida = null;
		
		querySelectApoyo = "select id_server, ob_server, id_usuario, id_crontrasena, ds_dominio, "
				+ "ds_host_smtp, id_puerto_host"
				+ " from encelado.enc_mail_servidores_definidos";		
		
		try {
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
		finally 
		{
			this.cerrarRecursosDB2();
		}
		
		return (ArrayList<T>) arraySalida;
		
	}

	@Override
	public Object devolverObjeto(Object registroEntrada) {
		//variables de trabajo del metodo
		ServerDefinido registroEntradaMetodo = (ServerDefinido) registroEntrada;
		
		querySelectApoyo = "select id_server, ob_server, id_usuario, id_crontrasena, ds_dominio, "
				+ "ds_host_smtp, id_puerto_host"
				+ " from encelado.enc_mail_servidores_definidos"
				+ " where id_server = '" + registroEntradaMetodo.getId_server() +"'";
		

		try {
			conexion = this.realizarConexion("encelado");							
			
			resultadoApoyo = this.recuperarDatos(querySelectApoyo);			

			if (resultadoApoyo != null) 
			{
				while (resultadoApoyo.next()) 
				{
					registroEntradaMetodo.setId_server(resultadoApoyo.getString(1));
					registroEntradaMetodo.setOb_server(resultadoApoyo.getString(2));
					registroEntradaMetodo.setId_usuario(resultadoApoyo.getString(3));
					registroEntradaMetodo.setId_contrasena(resultadoApoyo.getString(4));
					registroEntradaMetodo.setDs_dominio(resultadoApoyo.getString(5));			
					registroEntradaMetodo.setDs_host_smtp(resultadoApoyo.getString(6));
					registroEntradaMetodo.setId_puerto_host(resultadoApoyo.getString(7));					
				}				
			} else
			{
				System.out.println("No encuentro el servidor.");
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
		
		ServerDefinido objetoDeseaBorrar = new ServerDefinido();
		
		for (T claveObjetoeseaBorrar:registroEntrada) {			
			objetoDeseaBorrar.setId_server( (String) claveObjetoeseaBorrar);
			this.borrarObjeto(objetoDeseaBorrar);
		}		
		
		return true;
	}
	

	private boolean borrarObjeto(Object registroEntrada) 
	{		
		//variables de trabajo del metodo
		int controlDelete;
		ServerDefinido objetoDeseaBorrar = (ServerDefinido) registroEntrada;
		
		String queryDeleteApoyo = "delete from encelado.enc_mail_servidores_definidos"
				+ " where id_server  = '" + objetoDeseaBorrar.getId_server() +"'";

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

	private ArrayList<ServerDefinido> rellenarArray(ResultSet rsentrada) throws SQLException
	{
		//variables de trabajo del metodo
		ArrayList<ServerDefinido> arraySalida = new ArrayList<ServerDefinido>();

		
		while (rsentrada.next()) 
		{

			ServerDefinido objetoSalida = new ServerDefinido();
			
			objetoSalida.setId_server(rsentrada.getString(1));
			objetoSalida.setOb_server(rsentrada.getString(2));
			objetoSalida.setId_usuario(rsentrada.getString(3));
			objetoSalida.setId_contrasena(rsentrada.getString(4));
			objetoSalida.setDs_dominio(rsentrada.getString(5));			
			objetoSalida.setDs_host_smtp(rsentrada.getString(6));
			objetoSalida.setId_puerto_host(rsentrada.getString(7));

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
