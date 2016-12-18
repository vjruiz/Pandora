package com.spring.daocore;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.model.Comunicacion;
import com.spring.model.Reporte;
import com.spring.servicecore.CScomunicacion;
import com.utilidades.bbdd.ConexionBD;


public class DaoReportes implements IDao
{	
	
	Connection conexion;
	Statement sentenciaSelect;
	Statement sentencia_insert;
    Statement sentencia_Delete;
 	ResultSet resultadoApoyo;

	String querySelectApoyo;
	String queryInsertApoyo;

	public CScomunicacion capaServiciosComunicacion = new CScomunicacion();		

 	{
 		conexion = null;
 		sentenciaSelect = null;
 		sentencia_insert = null;
 		sentencia_Delete = null;
 	 	resultadoApoyo  = null;
 	 	querySelectApoyo = null;
 		queryInsertApoyo = null;	 	
 		
 	}	 	
 	
	public DaoReportes() {
		super();
	}

	@Override
	public boolean altaObjeto(String registroEntrada) 
	{
		boolean controlInsert = false;
		Reporte objetoAlta = null;
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objetoAlta = objectMapper.readValue(registroEntrada, Reporte.class);
		} 
		catch (IOException e1) {
			e1.printStackTrace();
		}
				
		//Mientras se desarrolla la pantalla de configuracion de variables del sistema
		// los path de trabajo con los ficheros se configuran de manera estatica.
		
		String pathResidenficherosPlantillas = "c:/encelado/plantillas_exportacion/";
		String pathResidenficherosExportados = "c:/encelado/ficheros_exportados/";
		
		String queryApoyo = 
				"INSERT INTO encelado.enc_report_definidos("
				+ "id_reporte, ob_reporte, fx_ini_vigencia, fx_fin_vigencia, ds_periodicidad_ejecucion,"
				+ "id_fichero_salida, formato_fichero_salida, id_path_fichero_salida,"
				+ "id_data_source_reporte, ds_in_data_source, id_conexion_bd_asoc,"
				+ "id_path_file_in_report, id_archivo_jasper, id_env_correo_asoc,ind_extraccion_proceso)"
				+ " VALUES ("
				+ "'" + objetoAlta.getId_reporte() + "'," 
				+ "'" + objetoAlta.getOb_reporte() + "'," 							
				+ "'" + objetoAlta.getFx_ini_vigencia() + "',"
				+ "'" + objetoAlta.getFx_fin_vigencia() + "',"
			    + "'" + objetoAlta.getDs_periodicidad_ejecucion() + "',"
				+ "'" + objetoAlta.getId_fichero_salida() + "',"
				+ "'" + objetoAlta.getFormato_fichero_salida() + "',"
				+ "'" + pathResidenficherosExportados + "',"
				+ "'" + objetoAlta.getId_data_source_reporte() + "',"
				+ "'" + objetoAlta.getDs_in_data_source().replaceAll("\n", "") + "',"
				+ "'" + objetoAlta.getId_conexion_bd_asoc() + "',"
				+ "'" + pathResidenficherosPlantillas + "',"	
				+ "'" + objetoAlta.getId_archivo_jasper() + "',"	
				+ "'" + objetoAlta.getComunicacionAsociada().getId_env_correo() + "',"
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
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> ArrayList<T> recuperarListado() {
		
		ArrayList<Reporte> arraySalida = null;
		
		querySelectApoyo = "SELECT id_reporte, ob_reporte, fx_ini_vigencia, fx_fin_vigencia,"
				+ "ds_periodicidad_ejecucion,id_fichero_salida, formato_fichero_salida, id_path_fichero_salida,"
				+ "id_data_source_reporte, ds_in_data_source, id_conexion_bd_asoc,"
				+ "id_path_file_in_report, id_archivo_jasper, id_env_correo_asoc,ind_extraccion_proceso"
				+ " FROM encelado.enc_report_definidos";		
		
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
		
		System.out.println(arraySalida);
		
		return (ArrayList<T>) arraySalida;
		
	}
	
	@Override
	public boolean modificarObjeto(String registroEntrada) 
	{
		boolean controlModificacion = false;
		Reporte objetoModificar = null;
		
		try {
			objetoModificar = new ObjectMapper().readValue(registroEntrada, Reporte.class);
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
	
	@Override
	public Object devolverObjeto(Object registroEntrada) 
	{		
		Reporte registroEntradaMetodo = (Reporte) registroEntrada;
		Comunicacion comunicacionAsociada = null;		
		
		querySelectApoyo = "SELECT id_reporte, ob_reporte, fx_ini_vigencia, fx_fin_vigencia,"
				+ "ds_periodicidad_ejecucion,id_fichero_salida, formato_fichero_salida, id_path_fichero_salida,"
				+ "id_data_source_reporte, ds_in_data_source, id_conexion_bd_asoc,"
				+ "id_path_file_in_report, id_archivo_jasper, id_env_correo_asoc,ind_extraccion_proceso"
				+ " FROM encelado.enc_report_definidos"
				+ " where id_reporte = '" + registroEntradaMetodo.getId_reporte() +"'";
		
		System.out.println(querySelectApoyo);
		
		try {
			conexion = this.realizarConexion("encelado");							
			
			resultadoApoyo = this.recuperarDatos(querySelectApoyo);

			if (resultadoApoyo != null) 
			{
				
				while (resultadoApoyo.next()) 
				{
					registroEntradaMetodo.setId_reporte(resultadoApoyo.getString(1));
					registroEntradaMetodo.setOb_reporte(resultadoApoyo.getString(2));
					registroEntradaMetodo.setFx_ini_vigencia(resultadoApoyo.getDate(3));	
					registroEntradaMetodo.setFx_fin_vigencia(resultadoApoyo.getDate(4));					
					registroEntradaMetodo.setDs_periodicidad_ejecucion(resultadoApoyo.getString(5));
					registroEntradaMetodo.setId_fichero_salida(resultadoApoyo.getString(6));
					registroEntradaMetodo.setFormato_fichero_salida(resultadoApoyo.getString(7));
					registroEntradaMetodo.setId_path_fichero_salida(resultadoApoyo.getString(8));
					registroEntradaMetodo.setId_data_source_reporte(resultadoApoyo.getString(9));
					registroEntradaMetodo.setDs_in_data_source(resultadoApoyo.getString(10));	
					registroEntradaMetodo.setId_conexion_bd_asoc(resultadoApoyo.getString(11));	
					registroEntradaMetodo.setId_path_file_in_report(resultadoApoyo.getString(12));					
					registroEntradaMetodo.setId_archivo_jasper(resultadoApoyo.getString(13));
										
					
					String idComunicacionAsociada = resultadoApoyo.getString(14);
					
					comunicacionAsociada = new Comunicacion();
					if ((idComunicacionAsociada.equals("")) ||
						(idComunicacionAsociada == null))
					{						
						registroEntradaMetodo.setComunicacionAsociada(null);						
					}else 
					{		
						comunicacionAsociada.setId_env_correo(idComunicacionAsociada);
						comunicacionAsociada = (Comunicacion) capaServiciosComunicacion.recuperarObjeto(comunicacionAsociada);
						registroEntradaMetodo.setComunicacionAsociada(comunicacionAsociada);						
					}
					registroEntradaMetodo.setInd_extraccion_proceso(resultadoApoyo.getString(15));

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
		
		Reporte objetoDeseaBorrar = new Reporte();
		
		for (T claveDeseaBorrar:registroEntrada) {			
			objetoDeseaBorrar.setId_reporte((String) claveDeseaBorrar);				
			this.borrarObjeto(objetoDeseaBorrar);
		}
		
		return true;
	}
	

	private boolean borrarObjeto(Object registroEntrada) 
	{		
		int controlDelete;
		boolean controlDeleteBoolean = false;
		
		String queryDeleteApoyo = "delete from encelado.enc_report_definidos"
				+ " where id_reporte = '" + ((Reporte) registroEntrada).getId_reporte()+"'";

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

		return controlDeleteBoolean;
	}	

	private ArrayList<Reporte> rellenarArray(ResultSet rsentrada) throws SQLException
	{
		//variables de trabajo del metodo
		ArrayList<Reporte> arraySalida = new ArrayList<Reporte>();
		Comunicacion comunicacionAsociada = null;

		while (rsentrada.next()) {

			Reporte objetoSalida = new Reporte();
			objetoSalida.setId_reporte(rsentrada.getString(1));
			objetoSalida.setOb_reporte(rsentrada.getString(2));

			Date fx_ini_vigencia = rsentrada.getDate(3);
			objetoSalida.setFx_ini_vigencia(fx_ini_vigencia);

			Date fx_fin_vigencia = rsentrada.getDate(4);
			objetoSalida.setFx_fin_vigencia(fx_fin_vigencia);

			objetoSalida.setDs_periodicidad_ejecucion(rsentrada.getString(5));
			objetoSalida.setId_fichero_salida(rsentrada.getString(6));
			objetoSalida.setFormato_fichero_salida(rsentrada.getString(7));
			objetoSalida.setId_path_fichero_salida(rsentrada.getString(8));
			objetoSalida.setId_data_source_reporte(rsentrada.getString(9));
			objetoSalida.setDs_in_data_source(rsentrada.getString(10));
			objetoSalida.setId_conexion_bd_asoc(rsentrada.getString(11));
			objetoSalida.setId_path_file_in_report(rsentrada.getString(12));
			objetoSalida.setId_archivo_jasper(rsentrada.getString(13));

			comunicacionAsociada = new Comunicacion();
			comunicacionAsociada.setId_env_correo(resultadoApoyo.getString(14));
			comunicacionAsociada = (Comunicacion) capaServiciosComunicacion.recuperarObjeto(comunicacionAsociada);
			
			objetoSalida.setInd_extraccion_proceso(resultadoApoyo.getString(15));
			objetoSalida.setComunicacionAsociada(comunicacionAsociada);

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
