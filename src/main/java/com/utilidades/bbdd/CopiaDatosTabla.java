package com.utilidades.bbdd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.activiti.engine.delegate.DelegateExecution;

import com.spring.daocore.DaoAddBBDD;
import com.spring.model.BBDDDefinida;
import com.utilidades.logger.LoggerResultado;


public class CopiaDatosTabla {
	
	private DaoAddBBDD capaServiciosSettingBD;
	private int contadorRegistrosCopiados = 0;
	private BBDDDefinida baseDatosOrigenDatos;

	private BBDDDefinida baseDatosOrigen;	
	private Connection conexionEncelado;
 	private ResultSet resultadoApoyoOrigen;
 	private String queryApoyoInsert;
	private Statement sentenciaSelect;
	private Statement sentencia_insert; 
    private Statement sentencia_Delete;
	private StringBuffer estructuraCabeceraInsert;	
	private StringBuffer estructuraDatosInsert;
	private String querySelectApoyo;
 	private ResultSet resultadoApoyo;
	
	
 	{ 		
 		conexionEncelado = null;
 		resultadoApoyoOrigen = null;
 		querySelectApoyo = null;
 		sentenciaSelect = null;
 		resultadoApoyo = null;
 	 	estructuraCabeceraInsert = null;
 	 	estructuraDatosInsert = null;
 	 	queryApoyoInsert = null;
 	} 	 
	
	public CopiaDatosTabla() {
		super();
	}

	public boolean copiaDatos(String id_conexion_BD_Origen,
							String ds_codigo_sql_origen, 
							String id_tabla_destino_copiabd) 
					throws SQLException 
	{						
			boolean controlInsert = false;
			// recuperamos los datos de origen
			resultadoApoyoOrigen = this.recuperarDatosOrigen(id_conexion_BD_Origen, ds_codigo_sql_origen.replace("\"", "\'"));

			// con los datos recuperados de origen los insertamos en destino.
			conexionEncelado = this.realizarConexionEncelado("encelado");	
			
			

			controlInsert = insertarDatosDestino(id_tabla_destino_copiabd,
								resultadoApoyoOrigen,
								conexionEncelado);

			this.cerrarRecursosDB2();
			
			return controlInsert;
	}
	
	public boolean copiaDatosMotor(String id_conexion_BD_Origen, 
								String ds_codigo_sql_origen,
								String id_tabla_destino_copiabd,
								DelegateExecution delegateExecution) throws SQLException 
	{
		boolean controlInsert = false;
		// recuperamos los datos de origen
		resultadoApoyoOrigen = this.recuperarDatosOrigen(id_conexion_BD_Origen, ds_codigo_sql_origen);

		// con los datos recuperados de origen los insertamos en destino.
		conexionEncelado = this.realizarConexionEncelado("encelado");
		
		//borramos la foto anterior
		String queryDeleteApoyo = "delete from " + id_tabla_destino_copiabd; 
		sentencia_Delete  = conexionEncelado.createStatement();
		sentencia_Delete.executeUpdate(queryDeleteApoyo);

		//copiamos la nueva foto
		controlInsert = insertarDatosDestino(id_tabla_destino_copiabd, resultadoApoyoOrigen, conexionEncelado);

		this.cerrarRecursosDB2();

		delegateExecution.setVariable("contadorRegistrosCopiados", this.contadorRegistrosCopiados);
		
		return controlInsert;
	}
	
	public boolean copiaDatosTablaDefecto(String id_conexion_BD_Origen, 
										String ds_codigo_sql_origen,
										String id_tabla_destino_copiabd,
										String id_copia_bd_asoc,
										String tipo_dato) throws SQLException 
	{
		boolean controlInsert = false;
		// recuperamos los datos de origen
		resultadoApoyoOrigen = this.recuperarDatosOrigen(id_conexion_BD_Origen, ds_codigo_sql_origen.replace("\"", "\'"));

		// con los datos recuperados de origen los insertamos en destino.
		conexionEncelado = this.realizarConexionEncelado("encelado");

		controlInsert = insertarDatosDestinoTablaDefecto(resultadoApoyoOrigen, 
								conexionEncelado,id_copia_bd_asoc,tipo_dato);

		this.cerrarRecursosDB2();		

		return controlInsert;
	}
	
	public boolean copiaDatosTablaDefectoMotor(String id_conexion_BD_Origen, 
											String ds_codigo_sql_origen,
											String id_tabla_destino_copiabd, 
											String id_copia_bd_asoc, 
											String tipo_dato,
											DelegateExecution delegateExecution) 
									throws SQLException 
	{
		boolean controlInsert = false;
		int idProceso = Integer.parseInt(delegateExecution.getProcessInstanceId());		
		//
		// recuperamos los datos de origen

			resultadoApoyoOrigen = 
				this.recuperarDatosOrigen(id_conexion_BD_Origen, 
										ds_codigo_sql_origen);
		// con los datos recuperados de origen los insertamos en destino.
		conexionEncelado = this.realizarConexionEncelado("encelado");
		
		
		//borramos la foto anterior
		String queryDeleteApoyo = "delete from encelado.im_datos_proceso"
							+ " where id_proceso = "  
							+ delegateExecution.getProcessInstanceId()
							+ " and id_copia_bd_asoc = '"
							+ id_copia_bd_asoc
							+ "'";
		sentencia_Delete  = conexionEncelado.createStatement();
		sentencia_Delete.executeUpdate(queryDeleteApoyo);

		controlInsert = insertarDatosDestinoTablaDefectoMotor(
				resultadoApoyoOrigen, 
				conexionEncelado, 
				id_copia_bd_asoc,
				tipo_dato,
				idProceso);

		this.cerrarRecursosDB2();
		
		delegateExecution.setVariable("contadorRegistrosCopiados", this.contadorRegistrosCopiados);

		return controlInsert;
	}
	
	private ResultSet recuperarDatosOrigen(String id_conexion_BD_Origen,String ds_codigo_sql_origen) 
			throws SQLException
	{
		// para pruebas quitar el new
		baseDatosOrigenDatos = new BBDDDefinida();
		capaServiciosSettingBD = new DaoAddBBDD();
		
		// recuperamos la conexion origen y los datos a insertar
		baseDatosOrigenDatos.setId_conexion_bd(id_conexion_BD_Origen);
		baseDatosOrigen =  (BBDDDefinida) capaServiciosSettingBD.devolverObjeto(baseDatosOrigenDatos);
		
		return baseDatosOrigen.realizarPruebaConexion(ds_codigo_sql_origen);		
	}
	
	
	private boolean insertarDatosDestino(String id_tabla_nueva,ResultSet resultadoDatosOrigen,
										Connection conexionDestino)
							throws SQLException 
	{
		ArrayList<String> estructuraTabla = this.recuperarEstructuraCampos(resultadoDatosOrigen);
		StringBuffer estructuraApoyo = new StringBuffer();
		int contadorCamposCabecera = 0;
		int contadorCamposInsertados = 0;
		boolean controlInsert = false;
		
		estructuraCabeceraInsert = new StringBuffer();
		estructuraDatosInsert = new StringBuffer();
		
		estructuraCabeceraInsert.append("INSERT INTO " + id_tabla_nueva + " (" );
		
		for (String nombreCampoTabla:estructuraTabla) {		
			contadorCamposCabecera++;
			estructuraApoyo.append(nombreCampoTabla + "  ," );
		}				
		
		estructuraCabeceraInsert.append(
				estructuraApoyo.substring(0, estructuraApoyo.length() - 1) + ") VALUES (");		
		
		while (resultadoDatosOrigen.next()) 
		{		
			while (contadorCamposInsertados < contadorCamposCabecera) {
				contadorCamposInsertados++;				
				estructuraDatosInsert.append("'"+resultadoDatosOrigen.getString(contadorCamposInsertados)+"',");				
			}
				queryApoyoInsert = 
						estructuraCabeceraInsert.toString() +
						estructuraDatosInsert.substring(0, estructuraDatosInsert.length() - 1) +
						")";
				//Reiniciamos la estructura de datos de los campos
				estructuraDatosInsert.delete(0, estructuraDatosInsert.length());
				contadorCamposInsertados = 0;				
				
				sentencia_insert = conexionDestino.createStatement();
				int valorDevuelto = sentencia_insert.executeUpdate(queryApoyoInsert);
				
				if (valorDevuelto < 0)
				{
					controlInsert = false;
				} else 
				{
					controlInsert = true;
					this.contadorRegistrosCopiados ++;
				}		
				
				
		}	

		return controlInsert;
		
	}	

	private boolean insertarDatosDestinoTablaDefecto(ResultSet resultadoDatosOrigen,
			Connection conexionDestino,String id_copia_bd_asoc,String tipo_dato) throws SQLException 
	{

		boolean controlInsert = false;
		int idProceso = calcularMaximo(id_copia_bd_asoc);

		while (resultadoDatosOrigen.next()) 
		{
			String queryApoyo = 
					"INSERT INTO encelado.im_datos_proceso("
					+ "id_copia_bd_asoc,id_proceso, co_referencia, ds_referencia)"
					+ " VALUES ("
					+ "'" + id_copia_bd_asoc + "',"
					+ "'" + idProceso + "'," 
					+ "'" + tipo_dato + "'," 
					+ "'" + resultadoDatosOrigen.getString(1) + "'"					
					+ ")"
					;					

			sentencia_insert = conexionEncelado.createStatement();
			int valorDevuelto = sentencia_insert.executeUpdate(queryApoyo);

			if (valorDevuelto < 0) {
				controlInsert = false;
			} else {
				controlInsert = true;
				this.contadorRegistrosCopiados ++;
			}
		}

		return controlInsert;

	}	

	private boolean insertarDatosDestinoTablaDefectoMotor(
					ResultSet resultadoDatosOrigen,
					Connection conexionDestino,
					String id_copia_bd_asoc,
					String tipo_dato,
					int idProceso) throws SQLException 
	{

		boolean controlInsert = false;
		int contadorTratados = 0;

		while (resultadoDatosOrigen.next()) 
		{
			String queryApoyo = 
					"INSERT INTO encelado.im_datos_proceso("
					+ "id_copia_bd_asoc,id_proceso, co_referencia, ds_referencia)"
					+ " VALUES ("
					+ "'" + id_copia_bd_asoc + "',"
					+ "'" + idProceso + "'," 
					+ "'" + tipo_dato + "'," 
					+ "'" + resultadoDatosOrigen.getString(1) + "'"					
					+ ")"
					;					

			sentencia_insert = conexionEncelado.createStatement();
			int valorDevuelto = sentencia_insert.executeUpdate(queryApoyo);			

			if (valorDevuelto < 0) {
				controlInsert = false;
			} else {
				controlInsert = true;
				this.contadorRegistrosCopiados ++;
			}
		}		
										
		return controlInsert;

	}	
	
	private int calcularMaximo(String id_copia_bd_asoc) 
	{
		
		int totalMasUno = 0;
		
		querySelectApoyo = "SELECT max (id_proceso) as maximo "
				+ " FROM encelado.im_datos_proceso"
				+ " where id_copia_bd_asoc = '" + id_copia_bd_asoc + "'";		
		
		try {
					
			resultadoApoyo = this.recuperarDatos(querySelectApoyo);
			// solo si tenemos datos de la copiaBD entramos a rellenar el objeto					
			if (resultadoApoyo != null) 
			{
				while (resultadoApoyo.next()) 
				{
					totalMasUno = resultadoApoyo.getInt("maximo");				
				}				
			} 
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		catch (NullPointerException e) {
			System.out.println("no existen datos");;
		}
		
		return totalMasUno + 9999;
		
	}
	
	private ArrayList<String> recuperarEstructuraCampos(ResultSet resultadoDatosOrigen) 
	{
		
		ArrayList<String> arraySalida = new ArrayList<String>();		
		
		try 
		{		
								
			if (resultadoDatosOrigen != null) 
			{
                ResultSetMetaData rsmd = resultadoDatosOrigen.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                	arraySalida.add(rsmd.getColumnName(i));
                }
			}									
		} 
		catch (SQLException e) {
			System.out.println("no existen datos" + e );
		}
		catch (NullPointerException e) {
			System.out.println("no existen datos" + e );
		}
		
		return (ArrayList<String>) arraySalida;
		
	}	
	
	private Connection realizarConexionEncelado(String tipoConexion) throws SQLException
	{
		
		Connection nuevaConexion;
		
			nuevaConexion = ConexionBD.getInstance().devolverConexion(tipoConexion);
		
		return nuevaConexion;
		
	}
	
	private void cerrarRecursosDB2() 
	{
		try 
		{
			if (resultadoApoyoOrigen != null) {resultadoApoyoOrigen.close();};
			if (resultadoApoyo != null) {resultadoApoyo.close();};
			if (sentencia_insert != null) {sentencia_insert.close();};
			if (sentencia_Delete != null) {sentencia_Delete.close();};	
			if (sentenciaSelect != null) {sentenciaSelect.close();};	
			if (baseDatosOrigenDatos !=null){baseDatosOrigenDatos.cerrarConexiones();}
			if (baseDatosOrigen !=null){baseDatosOrigen.cerrarConexiones();}
			if (conexionEncelado != null) {conexionEncelado.close();};
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}	

	private ResultSet recuperarDatos(String querySelect) 
	{
		    
		ResultSet resultado = null;

		try {
			sentenciaSelect = conexionEncelado.createStatement();
			resultado = sentenciaSelect.executeQuery(querySelect);			

		} catch (SQLException e) {

			e.printStackTrace();
			System.out.println(e);
			resultado = null;

		}

		return resultado;
	}
	
}
