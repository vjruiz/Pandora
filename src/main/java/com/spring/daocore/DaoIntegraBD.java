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
import com.spring.model.CopiaBD;
import com.utilidades.bbdd.ConexionBD;
import com.utilidades.bbdd.CreateTabla;

public class DaoIntegraBD implements IDao
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
 	
	public DaoIntegraBD() {
	}	

	@Override
	public boolean altaObjeto(String registroEntrada) 
	{
		boolean controlInsert = false;
		String nombreTablaCreada;
		CopiaBD objetoAlta = null;
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
//			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
//			objectMapper.setDateFormat(df);
			objetoAlta = objectMapper.readValue(registroEntrada, CopiaBD.class);
		} 
		catch (IOException e1) {
			e1.printStackTrace();
		}
		
		nombreTablaCreada = 
				this.crearTablaUsuario(
						objetoAlta.getId_tabla_destino_copiabd(),
						objetoAlta.getId_conexion_bd_asoc(),
						objetoAlta.getDs_codigo_sql_asoc_copiabd()
						);
		
		if (nombreTablaCreada != null)
		{
				// se ajusta el nombre al recibido por la rutina creadora de tablas
				objetoAlta.setId_tabla_destino_copiabd(nombreTablaCreada);
				
				controlInsert = this.realizarInsertDatosObj(objetoAlta);			
		}
		
		return controlInsert;
	}
	
	private boolean realizarInsertDatosObj(CopiaBD objetoAlta) 
	{
		boolean controlInsert = false;
	
		String codigoSQLAsociado = objetoAlta.getDs_codigo_sql_asoc_copiabd().replaceAll("\n", "");
		codigoSQLAsociado = codigoSQLAsociado.replace("\"", "\''");
		
		String queryApoyo = 
				"INSERT INTO encelado.enc_bbdd_copiasbd_definidas("
				+ "id_copia_bd, id_conexion_bd_asoc, id_tabla_destino_copiabd, ds_codigo_sql_asoc_copiabd, "
				+ "ob_copiabd,ds_periodicidad_ejecucion,fx_ini_vigencia,fx_fin_vigencia,id_arupador_tareas,"
				+ "tipo_dato,ind_extraccion_proceso)"
				+ " VALUES ("
				+ "'" + objetoAlta.getId_copia_bd().toLowerCase() + "'," 
				+ "'" + objetoAlta.getId_conexion_bd_asoc() + "'," 
				+ "'" + objetoAlta.getId_tabla_destino_copiabd() + "',"
				+ "'" + codigoSQLAsociado + "',"
				+ "'" + objetoAlta.getOb_copiabd().replaceAll("\n", " ")  + "',"
				+ "'" + objetoAlta.getDs_periodicidad_ejecucion() + "',"
				+ "'" + objetoAlta.getFx_ini_vigencia() + "',"
				+ "'" + objetoAlta.getFx_fin_vigencia() + "',"
				+ "'" + objetoAlta.getId_arupador_tareas() + "',"
				+ "'" + objetoAlta.getTipo_dato() + "',"
				+ "'" + objetoAlta.getInd_extraccion_proceso() + "'"				
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
	public boolean modificarObjeto(String registroEntrada) 
	{
		boolean controlModificacion = false;
		CopiaBD objetoModificar = null;
		
		try {
			objetoModificar = new ObjectMapper().readValue(registroEntrada, CopiaBD.class);
		} 
		catch (IOException e1) {
			e1.printStackTrace();
		}
		
		controlModificacion = this.borrarObjeto(objetoModificar,false);
		if (controlModificacion)
			{
			controlModificacion = this.realizarInsertDatosObj(objetoModificar);
			};		

		return controlModificacion;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> ArrayList<T> recuperarListado() {
		
		ArrayList<CopiaBD> arraySalida = null;
		
		querySelectApoyo = "select id_copia_bd, id_conexion_bd_asoc, id_tabla_destino_copiabd, "
				+ "ds_codigo_sql_asoc_copiabd,ob_copiabd, ds_periodicidad_ejecucion,"
				+ " fx_ini_vigencia, fx_fin_vigencia,id_arupador_tareas,tipo_dato,ind_extraccion_proceso"
				+ " from encelado.enc_bbdd_copiasbd_definidas";	
		
		try {
			conexion = this.realizarConexion("encelado");			
			resultadoApoyo = this.recuperarDatos(querySelectApoyo);
			// solo si tenemos datos de la copiaBD entramos a rellenar el objeto					
			if (resultadoApoyo != null) 
			{
				arraySalida = rellenarArray(resultadoApoyo);
			} 
			
		} catch (SQLException e) {
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
	public Object devolverObjeto(Object registroEntrada) 
	{
		CopiaBD registroEntradaMetodo = (CopiaBD) registroEntrada;

		querySelectApoyo = "select id_copia_bd, id_conexion_bd_asoc, id_tabla_destino_copiabd, "
				+ "ds_codigo_sql_asoc_copiabd,ob_copiabd, ds_periodicidad_ejecucion,"
				+ " fx_ini_vigencia, fx_fin_vigencia,id_arupador_tareas,tipo_dato,ind_extraccion_proceso"
				+ " from encelado.enc_bbdd_copiasbd_definidas"
				+ " where id_copia_bd = '" + registroEntradaMetodo.getId_copia_bd().toLowerCase() + "'";
		
		System.out.println(querySelectApoyo);
		try {
			conexion = this.realizarConexion("encelado");							
			
			resultadoApoyo = this.recuperarDatos(querySelectApoyo);

			if (resultadoApoyo != null) 
			{
				while (resultadoApoyo.next()) 
				{
					registroEntradaMetodo.setId_copia_bd(resultadoApoyo.getString(1));
					registroEntradaMetodo.setId_conexion_bd_asoc(resultadoApoyo.getString(2));
					registroEntradaMetodo.setId_tabla_destino_copiabd(resultadoApoyo.getString(3));
					registroEntradaMetodo.setDs_codigo_sql_asoc_copiabd(resultadoApoyo.getString(4));
					registroEntradaMetodo.setOb_copiabd(resultadoApoyo.getString(5));			
					registroEntradaMetodo.setDs_periodicidad_ejecucion(resultadoApoyo.getString(6));
					registroEntradaMetodo.setFx_ini_vigencia(resultadoApoyo.getDate(7));	
					registroEntradaMetodo.setFx_fin_vigencia(resultadoApoyo.getDate(8));	
					registroEntradaMetodo.setId_arupador_tareas(resultadoApoyo.getString(9));
					registroEntradaMetodo.setTipo_dato(resultadoApoyo.getString(10));
					registroEntradaMetodo.setInd_extraccion_proceso(resultadoApoyo.getString(11));					
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
		
		CopiaBD copiaBDdeseaBorrar = new CopiaBD();
		
		for (T claveCopiaBDdeseaBorrar:registroEntrada) {			
			copiaBDdeseaBorrar.setId_copia_bd((String) claveCopiaBDdeseaBorrar);				
			this.borrarObjeto(this.devolverObjeto(copiaBDdeseaBorrar),true);
		}
		
		return true;
	}
	

	private boolean borrarObjeto(Object registroEntrada,boolean controlBorradoTablaFisica) 
	{		
		//variables de trabajo del metodo
		int controlDelete;
		boolean controlDeleteBoolean = true;
		boolean controlBorradoFisico = true;
		CreateTabla creadorDeTablas = new CreateTabla();
		
		CopiaBD copiaBDentrada = (CopiaBD) registroEntrada;
		
		if(controlBorradoTablaFisica)					
		{
			controlBorradoFisico = creadorDeTablas.BorrarTabla(copiaBDentrada.getId_tabla_destino_copiabd());
		}
		
		System.out.println(controlBorradoFisico);
		
		if (controlBorradoFisico)
		{
			String queryDeleteApoyo = "delete from encelado.enc_bbdd_copiasbd_definidas"
					+ " where id_copia_bd = '" + copiaBDentrada.getId_copia_bd().toLowerCase()+ "'";

			try {
				conexion = this.realizarConexion("encelado");										
				sentencia_Delete  = conexion.createStatement();
				controlDelete = sentencia_Delete.executeUpdate(queryDeleteApoyo);
				// solo si tenemos datos de la copiaBD entramos a rellenar el objeto
				if (controlDelete < 0) {
					
					System.out.println("Error en delete" + controlDelete);
					controlDeleteBoolean = false;
					
				} else controlDeleteBoolean = true;
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				controlDeleteBoolean = false;
			}
			finally 
			{
				this.cerrarRecursosDB2();
			}
		} else {controlDeleteBoolean = false;}
		
		System.out.println("controlDeleteBoolean " + controlDeleteBoolean);
		
		return controlDeleteBoolean;		
		
	}	

	private ArrayList<CopiaBD> rellenarArray(ResultSet rsentrada) throws SQLException
	{
		//variables de trabajo del metodo
		ArrayList<CopiaBD> arraySalida = new ArrayList<CopiaBD>();

		
		while (rsentrada.next()) 
		{

			CopiaBD objetoSalida = new CopiaBD();
			
			objetoSalida.setId_copia_bd(rsentrada.getString(1));
			objetoSalida.setId_conexion_bd_asoc(rsentrada.getString(2));
			objetoSalida.setId_tabla_destino_copiabd(rsentrada.getString(3));
			objetoSalida.setDs_codigo_sql_asoc_copiabd(rsentrada.getString(4));
			objetoSalida.setOb_copiabd(rsentrada.getString(5));			
			objetoSalida.setDs_periodicidad_ejecucion(rsentrada.getString(6));

			
			Date fx_ini_vigencia = rsentrada.getDate(7);
			objetoSalida.setFx_ini_vigencia(fx_ini_vigencia);
			
			Date fx_fin_vigencia = rsentrada.getDate(8);
			objetoSalida.setFx_fin_vigencia(fx_fin_vigencia);

			objetoSalida.setId_arupador_tareas(rsentrada.getString(9));
			objetoSalida.setTipo_dato(resultadoApoyo.getString(10));
			objetoSalida.setInd_extraccion_proceso(resultadoApoyo.getString(11));
			
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
	
	private String crearTablaUsuario(String id_tabla_nueva,String id_conexion_BD_Asoc,String ds_codigo_sql_asoc) 
	{	
		String nombreTablaCreada;
		CreateTabla creadorDeTablas = new CreateTabla();
		nombreTablaCreada = creadorDeTablas.CrearTabla(id_tabla_nueva, id_conexion_BD_Asoc, ds_codigo_sql_asoc);
		
		return nombreTablaCreada;				
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
