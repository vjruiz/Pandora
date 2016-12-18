package com.spring.daocore;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.model.Comunicacion;
import com.utilidades.bbdd.ConexionBD;

public class DaoComunicacion implements IDao
{	
	
	Connection conexion;
	Statement sentenciaSelect;
	Statement sentencia_insert;
    Statement sentencia_Delete;
 	ResultSet resultadoApoyo;

	String querySelectApoyo;
	String queryInsertApoyo;	

 	{
 		conexion = null;
 		sentenciaSelect = null;
 		sentencia_insert = null;
 		sentencia_Delete = null;
 	 	resultadoApoyo  = null;
 	 	querySelectApoyo = null;
 		queryInsertApoyo = null;	 	
 		
 	}	 	
 	
	public DaoComunicacion() {
		super();
	}
	
	@Override
	public boolean altaObjeto(String registroEntrada) 
	{
		boolean controlInsert = false;
		Comunicacion objetoAlta = null;
		
		System.out.println(registroEntrada);
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objetoAlta = objectMapper.readValue(registroEntrada, Comunicacion.class);
		} 
		catch (IOException e1) {
			e1.printStackTrace();
		}
				
		String queryApoyo = 
				"INSERT INTO encelado.enc_mail_comunicaciones_definidas("
				+ "id_env_correo, ds_titulo_correo, ds_cuerpo_correo, ds_destinatarios_cc,"
				+ "ds_destinatarios_para, id_server_asoc)"
				+ " VALUES ("
				+ "'" + objetoAlta.getId_env_correo() + "'," 
				+ "'" + objetoAlta.getDs_titulo_correo() + "'," 
				+ "'" + objetoAlta.getDs_cuerpo_correo().replaceAll("\n", "") + "',"
				+ "'" + objetoAlta.getDs_destinatarios_cc() + "',"
				+ "'" + objetoAlta.getDs_destinatarios_para() + "',"				
				+ "'" + objetoAlta.getId_server_asoc() + "'"	
				+ ")"
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
	public Object devolverObjeto(Object registroEntrada) 
	{	
		Comunicacion registroEntradaMetodo = (Comunicacion) registroEntrada;
		
		querySelectApoyo = "SELECT id_env_correo, ds_titulo_correo, ds_cuerpo_correo, ds_destinatarios_cc,"
				+ "ds_destinatarios_para, id_server_asoc"
				+ "  FROM encelado.enc_mail_comunicaciones_definidas"
				+ " where id_env_correo = '" + registroEntradaMetodo.getId_env_correo() +"'";
		
		try {
			conexion = this.realizarConexion("encelado");							
			
			resultadoApoyo = this.recuperarDatos(querySelectApoyo);

			if (resultadoApoyo != null) 
			{
				while (resultadoApoyo.next()) 
				{
					registroEntradaMetodo.setId_env_correo(resultadoApoyo.getString(1));
					registroEntradaMetodo.setDs_titulo_correo(resultadoApoyo.getString(2));

					registroEntradaMetodo.setDs_cuerpo_correo(resultadoApoyo.getString(3));
					registroEntradaMetodo.setDs_destinatarios_cc(resultadoApoyo.getString(4));
					registroEntradaMetodo.setDs_destinatarios_para(resultadoApoyo.getString(5));
					registroEntradaMetodo.setId_server_asoc(resultadoApoyo.getString(6));										
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
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> ArrayList<T> recuperarListado() {
		
		ArrayList<Comunicacion> arraySalida = null;
		
		querySelectApoyo = "SELECT id_env_correo, ds_titulo_correo, ds_cuerpo_correo, ds_destinatarios_cc,"
				+ "ds_destinatarios_para, id_server_asoc"
				+ " FROM encelado.enc_mail_comunicaciones_definidas";		
		
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
	
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> recuperarListadoPorFecha(Date fx_ini_vigencia,Date fx_fin_vigencia) {
		
		ArrayList<Comunicacion> arraySalida = null;
		
		querySelectApoyo = "SELECT id_env_correo, ds_titulo_correo, ds_cuerpo_correo, ds_destinatarios_cc,"
				+ "ds_destinatarios_para, id_server_asoc"
				+ " FROM encelado.enc_mail_comunicaciones_definidas"
				+ " where fx_ini_vigencia = '" + fx_ini_vigencia.toString() + "'"
				+ " and fx_fin_vigencia = '" + fx_fin_vigencia.toString() + "'";		
		
		System.out.println(querySelectApoyo);
		
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
	public <T> boolean borrarListadoObjetos(ArrayList<T> registroEntrada) {
		
		Comunicacion objetoDeseaBorrar = new Comunicacion();
		
		for (T claveDeseaBorrar:registroEntrada) {			
			objetoDeseaBorrar.setId_env_correo((String) claveDeseaBorrar);				
			this.borrarObjeto(objetoDeseaBorrar);
		}
		
		return true;
	}
	

	private boolean borrarObjeto(Object registroEntrada) 
	{		
		int controlDelete;
		boolean controlDeleteBoolean = false;
		
		String queryDeleteApoyo = "delete from encelado.enc_mail_comunicaciones_definidas"
				+ " where id_env_correo = '" + ((Comunicacion) registroEntrada).getId_env_correo()+"'";

		try {
			conexion = this.realizarConexion("encelado");										
			sentencia_Delete  = conexion.createStatement();
			controlDelete = sentencia_Delete.executeUpdate(queryDeleteApoyo);
			// 
			if (controlDelete == 0) {				
				controlDeleteBoolean = true;				
			} 
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}
		finally 
		{
			this.cerrarRecursosDB2();
		}	

		return controlDeleteBoolean;
	}	

	private ArrayList<Comunicacion> rellenarArray(ResultSet rsentrada) throws SQLException
	{
		//variables de trabajo del metodo
		ArrayList<Comunicacion> arraySalida = new ArrayList<Comunicacion>();
		
		while (rsentrada.next()) 
		{

			Comunicacion objetoSalida = new Comunicacion();
			
			objetoSalida.setId_env_correo(rsentrada.getString(1));
			objetoSalida.setDs_titulo_correo(rsentrada.getString(2));

			objetoSalida.setDs_cuerpo_correo(rsentrada.getString(3));
			objetoSalida.setDs_destinatarios_cc(rsentrada.getString(4));
			objetoSalida.setDs_destinatarios_para(rsentrada.getString(5));
			objetoSalida.setId_server_asoc(rsentrada.getString(6));
	
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

	@Override
	public boolean modificarObjeto(String registroEntrada) {
		// TODO Auto-generated method stub
		return false;
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
