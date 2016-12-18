package com.spring.daocore;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.model.FlujoTareasProgramadas;
import com.spring.model.TareaProgramada;
import com.utilidades.bbdd.ConexionBD;


public class DaoFlujoTareas implements IDao
{	
	Connection conexion;
	Statement sentenciaSelect;
	Statement sentencia_insert;
    Statement sentencia_Delete;
 	ResultSet resultadoApoyo;
 	ResultSet resultadoApoyoTareas;

	String querySelectApoyo;
	String queryInsertApoyo;	
	
 	{
 		conexion = null;
 		sentenciaSelect = null;
 		sentencia_insert = null;
 		sentencia_Delete = null;
 	 	resultadoApoyo  = null;
 	 	resultadoApoyoTareas  = null;
 	 	querySelectApoyo = null;
 		queryInsertApoyo = null;	 	
 		
 	}
 	
	public DaoFlujoTareas() {
	}	
		

	public Connection getConexion() {
		return conexion;
	}



	public void setConexion(Connection conexion) {
		this.conexion = conexion;
	}



	public Statement getSentenciaSelect() {
		return sentenciaSelect;
	}



	public void setSentenciaSelect(Statement sentenciaSelect) {
		this.sentenciaSelect = sentenciaSelect;
	}



	public Statement getSentencia_insert() {
		return sentencia_insert;
	}



	public void setSentencia_insert(Statement sentencia_insert) {
		this.sentencia_insert = sentencia_insert;
	}



	public Statement getSentencia_Delete() {
		return sentencia_Delete;
	}



	public void setSentencia_Delete(Statement sentencia_Delete) {
		this.sentencia_Delete = sentencia_Delete;
	}



	public ResultSet getResultadoApoyo() {
		return resultadoApoyo;
	}



	public void setResultadoApoyo(ResultSet resultadoApoyo) {
		this.resultadoApoyo = resultadoApoyo;
	}



	public ResultSet getResultadoApoyoTareas() {
		return resultadoApoyoTareas;
	}



	public void setResultadoApoyoTareas(ResultSet resultadoApoyoTareas) {
		this.resultadoApoyoTareas = resultadoApoyoTareas;
	}



	public String getQuerySelectApoyo() {
		return querySelectApoyo;
	}



	public void setQuerySelectApoyo(String querySelectApoyo) {
		this.querySelectApoyo = querySelectApoyo;
	}



	public String getQueryInsertApoyo() {
		return queryInsertApoyo;
	}



	public void setQueryInsertApoyo(String queryInsertApoyo) {
		this.queryInsertApoyo = queryInsertApoyo;
	}



	@Override
	public boolean altaObjeto(String registroEntrada) 
	{
		boolean controlInsert = false;
		FlujoTareasProgramadas objetoAlta = null;				
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objetoAlta = objectMapper.readValue(registroEntrada, FlujoTareasProgramadas.class);
		} 
		catch (IOException e1) {
			e1.printStackTrace();
		}
				
		controlInsert = this.realizarInsertDatosObj(objetoAlta);			
				
		return controlInsert;
	}
	
	private boolean realizarInsertDatosObj(FlujoTareasProgramadas objetoAlta) 
	{
		boolean controlInsert = false;

		//primero insertamos las tareas asociadas
		if (this.insertarListadoTareas(objetoAlta.getListado_tareas_definidas()))	
		{
				String queryApoyo = 
						"INSERT INTO encelado.enc_task_flujos_tareas("
						+ "id_flujo_tarea, ds_flujo_tarea, fx_ini_vigencia, fx_fin_vigencia, "
						+ "ds_periodicidad_ejecucion)"
						+ " VALUES ("
						+ "'" + objetoAlta.getId_flujo_tarea() + "'," 
						+ "'" + objetoAlta.getDs_flujo_tarea() + "'," 
						+ "'" + objetoAlta.getFx_ini_vigencia() + "',"
						+ "'" + objetoAlta.getFx_fin_vigencia() + "',"
						+ "'" + objetoAlta.getDs_periodicidad_ejecucion() + "'"	
						+ ")"
						;
		
				try {
					conexion = this.realizarConexion("encelado");										
					sentencia_insert  = conexion.createStatement();
					int valorDevuelto = sentencia_insert.executeUpdate(queryApoyo);					
					
					if (valorDevuelto != 1)
					{
						controlInsert = false;
					} else 
					{
						controlInsert = true;							
					}
					
					System.out.println("insert en tareas " + valorDevuelto + controlInsert + queryApoyo);
					
				} catch (SQLException e) 
				{
					e.printStackTrace();
					controlInsert = false;			
				}
				finally 
				{
					this.cerrarRecursosDB2();
				}
		} else
		{
			System.out.println("error en la inserccion");
			controlInsert = false;
		}		
		
		
		return controlInsert;
	}

	@Override
	public boolean modificarObjeto(String registroEntrada) 
	{
		boolean controlModificacion = false;
		FlujoTareasProgramadas objetoModificar = null;
		
		try {
			objetoModificar = new ObjectMapper().readValue(registroEntrada, FlujoTareasProgramadas.class);
		} 
		catch (IOException e1) {
			e1.printStackTrace();
		}
					
		controlModificacion = this.borrarObjeto(objetoModificar);
		
		if (controlModificacion)
			{
			controlModificacion = this.realizarInsertDatosObj(objetoModificar);
			};		

		return controlModificacion;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> ArrayList<T> recuperarListado() {
		
		ArrayList<FlujoTareasProgramadas> arraySalida = null;
		
		querySelectApoyo = "select id_flujo_tarea, ds_flujo_tarea, fx_ini_vigencia, fx_fin_vigencia,  "
				+ "ds_periodicidad_ejecucion"
				+ " from encelado.enc_task_flujos_tareas";		
		
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
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> ArrayList<T> recuperarListadoVigente() {
		
		ArrayList<FlujoTareasProgramadas> arraySalida = null;
		
		querySelectApoyo = "select id_flujo_tarea, ds_flujo_tarea, fx_ini_vigencia, fx_fin_vigencia,  "
				+ "ds_periodicidad_ejecucion"
				+ " from encelado.enc_task_flujos_tareas"
				+ " where fx_ini_vigencia < current_date"
				+ "   and fx_fin_vigencia > current_date"
				;		
		
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
		FlujoTareasProgramadas registroEntradaMetodo = (FlujoTareasProgramadas) registroEntrada;

		querySelectApoyo = "select id_flujo_tarea, ds_flujo_tarea, fx_ini_vigencia, fx_fin_vigencia,  "
				+ "ds_periodicidad_ejecucion"
				+ " from encelado.enc_task_flujos_tareas"
				+ " where id_flujo_tarea = '" + registroEntradaMetodo.getId_flujo_tarea().toLowerCase() + "'";
		
		try {
			conexion = this.realizarConexion("encelado");							
			
			resultadoApoyo = this.recuperarDatos(querySelectApoyo);

			if (resultadoApoyo != null) 
			{
				while (resultadoApoyo.next()) 
				{
					registroEntradaMetodo.setId_flujo_tarea(resultadoApoyo.getString(1));
					registroEntradaMetodo.setDs_flujo_tarea(resultadoApoyo.getString(2));	
					registroEntradaMetodo.setFx_ini_vigencia(resultadoApoyo.getDate(3));	
					registroEntradaMetodo.setFx_fin_vigencia(resultadoApoyo.getDate(4));
					registroEntradaMetodo.setDs_periodicidad_ejecucion(resultadoApoyo.getString(5));
					registroEntradaMetodo.setListado_tareas_definidas(
							this.rellenarArrayTareas(registroEntradaMetodo.getId_flujo_tarea()));
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
		
		FlujoTareasProgramadas objetoDeseaBorrar = new FlujoTareasProgramadas();
		
		for (T claveCopiaBDdeseaBorrar:registroEntrada) {			
			objetoDeseaBorrar.setId_flujo_tarea((String) claveCopiaBDdeseaBorrar);				
			this.borrarObjeto(this.devolverObjeto(objetoDeseaBorrar));
		}
		
		return true;
	}
	
	private boolean borrarObjeto(Object registroEntrada) 
	{		
		int controlDelete;
		boolean controlDeleteBoolean = false;
		
		//primero insertamos las tareas asociadas
		if (this.borrarListadoTareas(((FlujoTareasProgramadas) registroEntrada).getListado_tareas_definidas()))	
		{			
			
			String queryDeleteApoyo = "delete from encelado.enc_task_flujos_tareas"
					+ " where id_flujo_tarea = '" + ((FlujoTareasProgramadas) registroEntrada).getId_flujo_tarea()+"'";

			try {
				conexion = this.realizarConexion("encelado");										
				sentencia_Delete  = conexion.createStatement();
				controlDelete = sentencia_Delete.executeUpdate(queryDeleteApoyo);
				// 
				if (controlDelete < 0) {
					
					System.out.println("Error en delete" + controlDelete);
					controlDeleteBoolean = false;
					
				} else controlDeleteBoolean = true;
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();		
			}
			finally 
			{
				this.cerrarRecursosDB2();
			}
		}

		return controlDeleteBoolean;
	}	

	private ArrayList<FlujoTareasProgramadas> rellenarArray(ResultSet rsentrada) throws SQLException
	{
		//variables de trabajo del metodo
		ArrayList<FlujoTareasProgramadas> arraySalida = new ArrayList<FlujoTareasProgramadas>();
		
		while (rsentrada.next()) 
		{
			FlujoTareasProgramadas objetoSalida = new FlujoTareasProgramadas();
			
			objetoSalida.setId_flujo_tarea(resultadoApoyo.getString(1));
			objetoSalida.setDs_flujo_tarea(resultadoApoyo.getString(2));	
			objetoSalida.setFx_ini_vigencia(resultadoApoyo.getDate(3));	
			objetoSalida.setFx_fin_vigencia(resultadoApoyo.getDate(4));
			objetoSalida.setDs_periodicidad_ejecucion(resultadoApoyo.getString(5));	
			objetoSalida.setListado_tareas_definidas(this.rellenarArrayTareas(objetoSalida.getId_flujo_tarea()));
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
	
	private boolean insertarListadoTareas(ArrayList<TareaProgramada> listadoEntrada) 
	{
		boolean controlInsert = true;
		int valorDevuelto;
		
		for (TareaProgramada objetoAlta:listadoEntrada) {		
			
			String queryApoyo = 
					"INSERT INTO encelado.enc_task_tareas_programadas("
					+ "id_flujo_tarea_asoc, tipo_tarea, id_tarea_asoc)"
					+ " VALUES ("
					+ "'" + objetoAlta.getId_flujo_tarea_asoc() + "'," 
					+ "'" + objetoAlta.getTipo_tarea() + "'," 
					+ "'" + objetoAlta.getId_tarea_asoc() + "'"						
					+ ")"
					;

			try {
				conexion = this.realizarConexion("encelado");										
				sentencia_insert  = conexion.createStatement();
				valorDevuelto = sentencia_insert.executeUpdate(queryApoyo);
				
				if (valorDevuelto != 1)
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
		}
		
		return controlInsert;		
	}	
	
	private boolean borrarListadoTareas(ArrayList<TareaProgramada> listadoEntrada) 
	{
		TareaProgramada objetoBorrar = null;		
		
		for (TareaProgramada objetoAlta:listadoEntrada) {
			objetoBorrar = objetoAlta;
		}
		
		return this.borrarTarea(objetoBorrar);
	}
	
	private boolean borrarTarea(TareaProgramada objetoBorrar) 
	{
		int controlDelete;
		boolean controlDeleteBoolean = false;
		
		String queryDeleteApoyo = "DELETE FROM encelado.enc_task_tareas_programadas"
				+ " where id_flujo_tarea_asoc = '" + objetoBorrar.getId_flujo_tarea_asoc()+"'";

		try {
			conexion = this.realizarConexion("encelado");										
			sentencia_Delete  = conexion.createStatement();
			controlDelete = sentencia_Delete.executeUpdate(queryDeleteApoyo);
			// 
			if (controlDelete < 0) {
				
				System.out.println("Error en delete" + controlDelete);
				controlDeleteBoolean = false;
				
			} else controlDeleteBoolean = true;
			
		} catch (SQLException e) {
			e.printStackTrace();		
		}
		finally 
		{
			this.cerrarRecursosDB2();
		}

		return controlDeleteBoolean;		
	}	
	
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> rellenarArrayTareas(String claveFlujo) {
		
		ArrayList<TareaProgramada> arraySalida = null;
		
		querySelectApoyo = "SELECT id_flujo_tarea_asoc, tipo_tarea, id_tarea_asoc "				
				+ " from encelado.enc_task_tareas_programadas"
				+ " where id_flujo_tarea_asoc = '" + claveFlujo + "'";		
		
		try {
			//conexion = this.realizarConexion("encelado");			
			resultadoApoyoTareas = this.recuperarDatos(querySelectApoyo);
			// solo si tenemos datos de la copiaBD entramos a rellenar el objeto					
			if (resultadoApoyoTareas != null) 
			{
				arraySalida = recuperarTareas(resultadoApoyoTareas);
			} 
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		catch (NullPointerException e) {
			System.out.println("no existen datos");;
		}
		
		return (ArrayList<T>) arraySalida;
		
	}

	private ArrayList<TareaProgramada> recuperarTareas(ResultSet rsentrada) throws SQLException 			
	{
		//variables de trabajo del metodo
		ArrayList<TareaProgramada> arraySalida = new ArrayList<TareaProgramada>();
		
		while (rsentrada.next()) 
		{

			TareaProgramada objetoSalida = new TareaProgramada();
			
			objetoSalida.setId_flujo_tarea_asoc(rsentrada.getString(1));
			objetoSalida.setTipo_tarea(rsentrada.getString(2));	
			objetoSalida.setId_tarea_asoc(rsentrada.getString(3));	
			arraySalida.add(objetoSalida);
					
		}
		
		return arraySalida;		
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


	
}
