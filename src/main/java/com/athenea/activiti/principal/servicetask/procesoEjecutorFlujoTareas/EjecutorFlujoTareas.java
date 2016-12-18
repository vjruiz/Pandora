/*
 */
package com.athenea.activiti.principal.servicetask.procesoEjecutorFlujoTareas;

import com.athenea.activiti.principal.servicetask.TareasDeFlujo;
import com.spring.daocore.DaoAddBBDD;
import com.spring.model.BBDDDefinida;
import com.spring.model.Comunicacion;
import com.spring.model.CopiaBD;
import com.spring.model.FlujoTareasProgramadas;
import com.spring.model.Reporte;
import com.spring.model.ServerDefinido;
import com.spring.model.TareaProgramada;
import com.spring.servicecore.CSaddBBDD;
import com.spring.servicecore.CSaddServer;
import com.spring.servicecore.CSintegraBD;
import com.spring.servicecore.CSreportes;
import com.utilidades.bbdd.ConexionBD;
import com.utilidades.bbdd.CopiaDatosTabla;
import com.utilidades.ficheros.ExportadorExcel;
import com.utilidades.ficheros.jasper.EjecutarReportes;
import com.utilidades.ficheros.jasper.dataSource.AbstractDataSource;
import com.utilidades.logger.LoggerResultado;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author vjruiz
 */
public class EjecutorFlujoTareas implements TareasDeFlujo{

    private static Logger log = Logger.getLogger(EjecutorFlujoTareas.class);
	private FlujoTareasProgramadas flujoTratado;
	private ArrayList<TareaProgramada> listadoTareasEjecutar; 
	private CSintegraBD capaServiciosCopiaBD;
	private CopiaBD copiaBDejecutar;	
	private CSaddBBDD capaServiciosSettingBD;
	private CSreportes capaServiciosReportes;
	private CSaddServer capaServiciosServerMail;
	private ResultSet resultadoDatosOrigenFiltro = null;	

	public EjecutorFlujoTareas() {
	}

	{ // bloque de inicializacion de variables de instancia

	}

	public FlujoTareasProgramadas getFlujoTratado() {
		return flujoTratado;
	}

	public void setFlujoTratado(FlujoTareasProgramadas flujoTratado) {
		this.flujoTratado = flujoTratado;
	}

	public ArrayList<TareaProgramada> getListadoTareasEjecutar() {
		return listadoTareasEjecutar;
	}

	public void setListadoTareasEjecutar(ArrayList<TareaProgramada> listadoTareasEjecutar) {
		this.listadoTareasEjecutar = listadoTareasEjecutar;
	}

	public CSintegraBD getCapaServiciosCopiaBD() {
		return capaServiciosCopiaBD;
	}

	public void setCapaServiciosCopiaBD(CSintegraBD capaServiciosCopiaBD) {
		this.capaServiciosCopiaBD = capaServiciosCopiaBD;
	}

	public CopiaBD getCopiaBDejecutar() {
		return copiaBDejecutar;
	}

	public void setCopiaBDejecutar(CopiaBD copiaBDejecutar) {
		this.copiaBDejecutar = copiaBDejecutar;
	}

	public CSaddBBDD getCapaServiciosSettingBD() {
		return capaServiciosSettingBD;
	}

	public void setCapaServiciosSettingBD(CSaddBBDD capaServiciosSettingBD) {
		this.capaServiciosSettingBD = capaServiciosSettingBD;
	}

	public CSreportes getCapaServiciosReportes() {
		return capaServiciosReportes;
	}

	public void setCapaServiciosReportes(CSreportes capaServiciosReportes) {
		this.capaServiciosReportes = capaServiciosReportes;
	}

	public CSaddServer getCapaServiciosServerMail() {
		return capaServiciosServerMail;
	}

	public void setCapaServiciosServerMail(CSaddServer capaServiciosServerMail) {
		this.capaServiciosServerMail = capaServiciosServerMail;
	}

	public void execute(DelegateExecution delegateExecution,ApplicationContext contextSpring)  {
		
		delegateExecution.setVariable("actividad", delegateExecution.getCurrentActivityName());
		delegateExecution.setVariable("resultado", "OK");
		delegateExecution.setVariable("error", "  ");
			
		//Recuperamos el flujo que debemos ejecutar.
		flujoTratado = (FlujoTareasProgramadas) delegateExecution.getVariable("caso");
		listadoTareasEjecutar = flujoTratado.getListado_tareas_definidas();
		
		int inTarea = Integer.parseInt(delegateExecution.getProcessInstanceId());
		

		
		boolean resultadoEjecucion = true;		
		
		for(TareaProgramada tareaEjecutar:listadoTareasEjecutar)
		{
			
			System.out.println("inicio bucle ejecutor tareas"
					+ "*************************************"
					+ tareaEjecutar.getId_tarea_asoc()
					+ "*************************************"
					);
			try {
				LoggerResultado.insertarRegistroLogger(
														tareaEjecutar.getId_flujo_tarea_asoc(),
														tareaEjecutar.getId_tarea_asoc(), 
														inTarea, 
														"En Proceso", 
														"En Proceso", 
														0);
			} catch (SQLException e1) {
				log.warn("Error insertando tabla control flujos.- " + tareaEjecutar.getId_tarea_asoc(), e1);
			}
			
			switch (tareaEjecutar.getTipo_tarea()) {
				case "copiaBD":
						resultadoEjecucion = this.ejecutarCopiaBD(tareaEjecutar,delegateExecution);
						break;
				case "reporte":
					    resultadoEjecucion = this.ejecutarReporte(tareaEjecutar, delegateExecution);
						break;								
				case "servicioNA":
					    resultadoEjecucion = this.ejecutarServicioNA(tareaEjecutar, delegateExecution);				    
						break;							
				default:
						break;
			}													
		}				
	}
	
	private boolean ejecutarCopiaBD(TareaProgramada tareaEjecutar,DelegateExecution delegateExecution)
	{
		boolean controlEjecucion = false;
		int idProceso = Integer.parseInt(delegateExecution.getProcessInstanceId());
		
		try {
			copiaBDejecutar = null;		
			CopiaDatosTabla copiadorTablas = new CopiaDatosTabla();			
							
			
			copiaBDejecutar = new CopiaBD();
			copiaBDejecutar.setId_copia_bd(tareaEjecutar.getId_tarea_asoc());
			
			capaServiciosCopiaBD = new CSintegraBD();
			copiaBDejecutar = 
						(CopiaBD) capaServiciosCopiaBD.recuperarObjeto(copiaBDejecutar);
	
			if (copiaBDejecutar.getTipo_dato().equals("filtroProceso"))
			{
				controlEjecucion = this.ejecutarFiltroEnProceso(copiaBDejecutar, delegateExecution);
			}else
			{
				try {
					System.out.println(copiaBDejecutar.getId_tabla_destino_copiabd());
					String queryExtraccionOrigenDatos = copiaBDejecutar.getDs_codigo_sql_asoc_copiabd().replace("\"", "\'");
					
					if (copiaBDejecutar.getInd_extraccion_proceso().equals("si"))
					{
						queryExtraccionOrigenDatos = queryExtraccionOrigenDatos 
												+ " and a.id_proceso = " + idProceso;
					}
					
					if (copiaBDejecutar.getId_tabla_destino_copiabd().equals("im_datos_proceso"))
					{
						System.out.println("tipo dato" + copiaBDejecutar.getTipo_dato());
						
						controlEjecucion = copiadorTablas.copiaDatosTablaDefectoMotor(
								copiaBDejecutar.getId_conexion_bd_asoc(), 
								queryExtraccionOrigenDatos, 
								copiaBDejecutar.getId_tabla_destino_copiabd(),
								copiaBDejecutar.getId_copia_bd(),
								copiaBDejecutar.getTipo_dato(),
								delegateExecution);	
					}else
					{
						controlEjecucion = copiadorTablas.copiaDatosMotor(
								copiaBDejecutar.getId_conexion_bd_asoc(), 
								queryExtraccionOrigenDatos, 
								copiaBDejecutar.getId_tabla_destino_copiabd(),
								delegateExecution);					
					}
					
					
				} 
				catch (SQLException e) {
					log.fatal("Error DB2 Ejecutando CopiaBD .- " + copiaBDejecutar.getId_copia_bd(), e);
					controlEjecucion = false;
				}
				catch (NullPointerException e)
				{
					log.fatal("Error NullPointerException Ejecutando CopiaBD .- " + copiaBDejecutar.getId_copia_bd(), e);		
					controlEjecucion = false;
				}
			}
			
		}catch (Exception e)
		{
			log.fatal("Error recuperando COPIABD del DAO .- " + copiaBDejecutar.getId_copia_bd(), e);		
			controlEjecucion = false;
		}
		if (!controlEjecucion)
		{
			try {
				LoggerResultado.actualizarResultadoLoggerSinTotal(
						tareaEjecutar.getId_tarea_asoc(), 
						Integer.parseInt(delegateExecution.getProcessInstanceId()),					
						"NOK", 
						"Error ejecutando el CopiaBD."
						);
			} catch (NumberFormatException e) {
				log.warn("Error recuperando ID proceso en la actualizacion del error en la ejecucion de un CopiaBD" , e);
			} catch (SQLException e) {
				log.warn("Error SQL actualizacion el error en la ejecucion de un CopiaBD" + tareaEjecutar.getId_tarea_asoc(), e);
			}
		}else
		{
			int contadorCopiados = 0;
			try
			{
				contadorCopiados = (int) delegateExecution.getVariable("contadorRegistrosCopiados");
			}catch (Exception e) {
				log.warn("error dejando log de copiaBD, no puedo calcular el contador de registros copiados" + tareaEjecutar.getId_tarea_asoc(),e);
			}
			
			try {
				
				LoggerResultado.actualizarResultadoLogger(
						tareaEjecutar.getId_tarea_asoc(), 
						idProceso, 
						"OK", 
						"Resultado Correcto", 
						contadorCopiados);
			} catch (SQLException e) {
				log.warn("error dejando log de copiaBD" + tareaEjecutar.getId_tarea_asoc(),e);
			}
		}
		return controlEjecucion;
		
	}
	
	private boolean ejecutarReporte(TareaProgramada tareaEjecutar,DelegateExecution delegateExecution)
	{
		boolean controlEjecucionReporte = true;
		Reporte reporteEjecutar = null;
		
		try 
		{
			int idProceso = Integer.parseInt(delegateExecution.getProcessInstanceId());
			reporteEjecutar = new Reporte();
			
			reporteEjecutar.setId_reporte(tareaEjecutar.getId_tarea_asoc());
	
			capaServiciosReportes = new CSreportes();
			reporteEjecutar = (Reporte) capaServiciosReportes.recuperarObjeto(reporteEjecutar);		
			
			if (!reporteEjecutar.getId_archivo_jasper().equals(""))
			{		
				controlEjecucionReporte =  this.ejecutarReporteJasper(reporteEjecutar);
			} else
			{		
				controlEjecucionReporte =  this.ejecutarReporteNormal(reporteEjecutar,idProceso,delegateExecution);
			}		
		}
		catch (Exception e)
		{
			log.fatal("Error recuperando Reporte del DAO .- " + reporteEjecutar.getId_reporte(), e);		
			controlEjecucionReporte = false;
		}
		
		if (!controlEjecucionReporte)
		{
			try {
				LoggerResultado.actualizarResultadoLogger(
						tareaEjecutar.getId_tarea_asoc(), 
						Integer.parseInt(delegateExecution.getProcessInstanceId()),					
						"NOK", 
						"Error ejecutando el reporte.",
						0
						);
			} catch (NumberFormatException e) {
				log.warn("Error recuperando ID proceso en la actualizacion del error en la ejecucion de un Reporte" , e);
			} catch (SQLException e) {
				log.warn("Error SQL actualizacion el error en la ejecucion de un Reporte" + tareaEjecutar.getId_tarea_asoc(), e);
			}
		} else
		{
			if (!copiaBDejecutar.getTipo_dato().equals("filtroProceso"))
			{
				try
				{
					int registrosFichero = (int) delegateExecution.getVariable("contadorRegistrosExportados");
					LoggerResultado.actualizarResultadoLogger(
							tareaEjecutar.getId_tarea_asoc(), 
							Integer.parseInt(delegateExecution.getProcessInstanceId()),					
							"OK", 
							"Resultado Correcto" ,
							registrosFichero
							);
					
				} catch (NumberFormatException e) {
					log.warn("Error recuperando ID proceso en la actualizacion del error en la ejecucion de un Reporte" , e);
				} catch (SQLException e) {
					log.warn("Error SQL actualizacion el error en la ejecucion de un Reporte" + tareaEjecutar.getId_tarea_asoc(), e);
				}
			}
			
		}
		
		return controlEjecucionReporte;
		
	}
	
	private boolean ejecutarReporteNormal(Reporte reporteEjecutar,int idProceso,DelegateExecution delegateExecution)
	{
		boolean controlExportacion = true;
		ResultSet resulsetDatosEjecuccion = null;
		String queryReporteEjecutar = null;
		BBDDDefinida baseDatosPrueba = null;
		
		try
		{
			// variables de trabajo para la exportacion
			// se prepara el objeto base de datos que nos da la informacion necesaria, para a conexion
			// que necesita la exportacion del reporte
			baseDatosPrueba = new BBDDDefinida();
			baseDatosPrueba.setId_conexion_bd(reporteEjecutar.getId_conexion_bd_asoc());				
			capaServiciosSettingBD = new CSaddBBDD();
			baseDatosPrueba = (BBDDDefinida) capaServiciosSettingBD.recuperarObjeto(baseDatosPrueba);
			
			//una vez recuperado el objeto de base de datos se realiza la prueba
			try {
				if (reporteEjecutar.getInd_extraccion_proceso().equals("si"))
				{
					queryReporteEjecutar = reporteEjecutar.getDs_in_data_source()
										+ " and a.id_proceso = " + idProceso;
					System.out.println("queryReporteEjecutar" + queryReporteEjecutar);
					resulsetDatosEjecuccion = 
							baseDatosPrueba.realizarPruebaConexion(queryReporteEjecutar);	
				}else
				{
					resulsetDatosEjecuccion = 
							baseDatosPrueba.realizarPruebaConexion(reporteEjecutar.getDs_in_data_source());					
				}

				
				ExportadorExcel generarExcel = new ExportadorExcel();
                // se compone el nombre del fichero.
                Calendar calendario = new GregorianCalendar();
				@SuppressWarnings("unused")
				File archivoXLS = generarExcel.generarExcel(resulsetDatosEjecuccion, 
															reporteEjecutar.getId_path_fichero_salida(), 
															(reporteEjecutar.getId_fichero_salida()
																		+ "."
																		+ calendario.get(Calendar.HOUR_OF_DAY)
																		+ calendario.get(Calendar.MINUTE)
																		+ calendario.get(Calendar.SECOND)
																		+ reporteEjecutar.getFormato_fichero_salida()),
															delegateExecution);
				
		        // si existe una comunicacion por correo asociada la ejecutamos.
		        if ((reporteEjecutar.getComunicacionAsociada() != null) &&
		        	(!reporteEjecutar.getComunicacionAsociada().getId_env_correo().equals("")))	
		        {
		        	controlExportacion = realizarEnvioCorreo(reporteEjecutar.getComunicacionAsociada());
		        }	
				
			} catch (SQLException e) 
			{
				controlExportacion = false;
				log.fatal("Error SQL exportando fichero" + reporteEjecutar.getId_fichero_salida(), e);
			}	
		}
		catch (Exception e)
		{
			log.fatal("Error exportado fichero al recuperar conexion del DAO .- " + reporteEjecutar.getId_reporte(), e);		
			controlExportacion = false;
		}finally 
		{
			try
			{
				if (resulsetDatosEjecuccion != null)resulsetDatosEjecuccion.close();;
				if (baseDatosPrueba != null) baseDatosPrueba.cerrarConexiones(); ;				
			} catch(SQLException e3)
			{
				log.warn("Error cerrando recursos DB2." , e3);
			}

		}
		
		
		return controlExportacion;
		
	}
	
	private boolean ejecutarReporteJasper(Reporte reporteEjecutar)
	{
		boolean controlExportacion = true;				
			// variables de trabajo para la exportacion
			// se prepara el objeto base de datos que nos da la informacion necesaria, para a conexion
			// que necesita la exportacion del reporte
		BBDDDefinida baseDatosPrueba = null;
		Connection conexionDestino = null;
		try
		{
			baseDatosPrueba = new BBDDDefinida();
			baseDatosPrueba.setId_conexion_bd(reporteEjecutar.getId_conexion_bd_asoc());
			
			capaServiciosSettingBD = new CSaddBBDD();
			baseDatosPrueba = (BBDDDefinida) capaServiciosSettingBD.recuperarObjeto(baseDatosPrueba);		
			
	        AbstractDataSource<?> datasource = null;    
	        
			String cadenaConexionUsuPass = 
					baseDatosPrueba.getDs_cadena_conexion_bbdd() + baseDatosPrueba.getResto_ds_cadena_conexion_bbdd()
					+"user="
					+ baseDatosPrueba.getId_usuario()
					+"&password="
					+ baseDatosPrueba.getId_contrasena();	
			
				 conexionDestino = (Connection) new ConexionBD(
						 	baseDatosPrueba.getDs_driver_bbdd(),
							cadenaConexionUsuPass,
							null,
							null).devolverConexionConstructor();
			
			EjecutarReportes ejecutarReportes = EjecutarReportes.getInstance();
			
	        try {
				datasource = 
				        (AbstractDataSource<?>) Class.forName(
				        		reporteEjecutar.getId_data_source_reporte()).newInstance();
				
		        //se crea el datasource.
		        datasource.loadDataSource(
		        		conexionDestino,                                 
		        		reporteEjecutar.getDs_in_data_source(),
		                null);
		        
		        @SuppressWarnings("unused")
				JasperPrint viewer = ejecutarReportes.ejecutarInforme(          
		        		conexionDestino, 
		                new FileInputStream(reporteEjecutar.getId_path_file_in_report()
		                				  + reporteEjecutar.getId_archivo_jasper()),
		                reporteEjecutar.getId_path_fichero_salida(),
		                reporteEjecutar.getId_fichero_salida(), 
		                reporteEjecutar.getFormato_fichero_salida().substring(1, reporteEjecutar.getFormato_fichero_salida().length()),
		                null,
		                datasource);
		        
		        // si existe una comunicacion por correo asociada la ejecutamos.
		        if (reporteEjecutar.getComunicacionAsociada() != null)
		        {
		        	controlExportacion = realizarEnvioCorreo(reporteEjecutar.getComunicacionAsociada());
		        }		        
		        
			} catch (InstantiationException e) {
				controlExportacion = false;
				log.fatal("Error exportando fichero" + reporteEjecutar.getId_fichero_salida(), e);
			} catch (IllegalAccessException e) {
				controlExportacion = false;
				log.fatal("Error exportando fichero" + reporteEjecutar.getId_fichero_salida(), e);
			} catch (ClassNotFoundException e) {
				controlExportacion = false;
				log.fatal("Error exportando fichero" + reporteEjecutar.getId_fichero_salida(), e);
			} catch (FileNotFoundException e) {
				controlExportacion = false;
				log.fatal("Error exportando fichero" + reporteEjecutar.getId_fichero_salida(), e);
			} catch (JRException e) {
				controlExportacion = false;
				log.fatal("Error exportando fichero" + reporteEjecutar.getId_fichero_salida(), e);
			} catch (SQLException e) {
				controlExportacion = false;
				log.fatal("Error exportando fichero" + reporteEjecutar.getId_fichero_salida(), e);
			}			
		}
		catch (Exception e)
		{
				log.fatal("Error exportado fichero Jasper al recuperar conexion del DAO .- " 
							+ reporteEjecutar.getId_reporte(), e);		
				controlExportacion = false;
		}finally 
		{
			try
			{
				if (conexionDestino != null) conexionDestino.close();;
				if (baseDatosPrueba != null) baseDatosPrueba.cerrarConexiones(); ;				
			} catch(SQLException e3)
			{
				log.warn("Error cerrando recursos DB2." , e3);
			}

		}
	        
		return controlExportacion;
		
	}
	
	public boolean realizarEnvioCorreo(Comunicacion objetoComunicacion) 		
	{
		//se prepara el objeto base de datos para realizar la prueba
		ServerDefinido servidorPrueba = new ServerDefinido();	
		servidorPrueba.setId_server(objetoComunicacion.getId_server_asoc());
		
		capaServiciosServerMail = new CSaddServer();
		servidorPrueba = (ServerDefinido) capaServiciosServerMail.recuperarObjeto(servidorPrueba);		
		
		return servidorPrueba.enviarCorreo(objetoComunicacion.getDs_titulo_correo(),
										objetoComunicacion.getDs_cuerpo_correo(),
										objetoComunicacion.getDs_destinatarios_para(),
										objetoComunicacion.getDs_destinatarios_cc());		
	 }
	
	private boolean ejecutarServicioNA(TareaProgramada tareaEjecutar,DelegateExecution delegateExecution)
	{
		boolean controlLanzamiento = true;
		
		Map<String, Object> variableMap = new HashMap<String, Object>();
				
		RuntimeService runtimeService = delegateExecution.getEngineServices().getRuntimeService();
		variableMap.put("servicioNA", tareaEjecutar.getId_tarea_asoc());
		variableMap.put("error", "NO");
		variableMap.put("resultado", "OK");
		String id_proceso_invocante = delegateExecution.getProcessInstanceId();
		variableMap.put("id_proceso_invocante", id_proceso_invocante);
		variableMap.put("id_flujo_tarea_asoc", tareaEjecutar.getId_flujo_tarea_asoc());
		variableMap.put("id_tarea_asoc", tareaEjecutar.getId_tarea_asoc());
		
		System.out.println("***************************************");
		System.out.println("lanzo peticion de ejecucion de servicio.- " 
				+ tareaEjecutar.getId_tarea_asoc());
		System.out.println("***************************************");
		
		try {
			runtimeService.startProcessInstanceByKey(
					"procesoEjecutorServiciosNA", 
					variableMap);
			
		}
		  catch (ActivitiObjectNotFoundException e)
		{
			log.fatal("Error ejecutando servicionNA.- " + tareaEjecutar.getId_tarea_asoc(), e);
			controlLanzamiento = false;
		} catch (NullPointerException e)
		{
			log.fatal("Error ejecutando servicionNA.- " + tareaEjecutar.getId_tarea_asoc(), e);	
			controlLanzamiento = false;			
		}
		if (!controlLanzamiento)
		{
			try {
				LoggerResultado.actualizarResultadoLogger(
						tareaEjecutar.getId_tarea_asoc(), 
						Integer.parseInt(delegateExecution.getProcessInstanceId()),					
						"NOK", 
						"Error ejecutando el flujo de servicios NA." ,
						0
						);
			} catch (NumberFormatException e) {
				log.warn("Error recuperando ID proceso en la actualizacion del error en la ejecucion de un SevicioNA" , e);
			} catch (SQLException e) {
				log.warn("Error SQL actualizacion el error en la ejecucion de un SevicioNA" + tareaEjecutar.getId_tarea_asoc(), e);
			}
		}
		return controlLanzamiento;
		
		
	}
	
	private boolean ejecutarFiltroEnProceso(CopiaBD tareaEjecutar,DelegateExecution delegateExecution)
	{
		boolean controlEjecucion = true;
		int contadorTratados = 0;
		Connection conexion = null;
		Statement sentenciaSelect = null;
	 	ResultSet resultadoDatosOrigen = null;	
		String querySelectApoyoOrigen = null;
		String querySelectApoyoFiltro = null;
		

		try {
			// recuperamos los datos que esta tratando el proceso para filtrarlos con los
			// datos declarados en el filtro.
			querySelectApoyoOrigen = "SELECT id_copia_bd_asoc, id_proceso,"
					    + "co_referencia, ds_referencia"
						+ " FROM encelado.im_datos_proceso"
						+ " where id_proceso = "  
						+ delegateExecution.getProcessInstanceId();
			//query para recuperar los datos del filtro.
			querySelectApoyoFiltro = tareaEjecutar.getDs_codigo_sql_asoc_copiabd().replace("\"", "\'");
			
			conexion = this.realizarConexion("encelado");
			this.recuperarResulset(conexion, querySelectApoyoOrigen);			
			
			contadorTratados = this.ejecutarFiltro(resultadoDatosOrigenFiltro, 
													this.recuperarDatosOrigen(tareaEjecutar.getId_conexion_bd_asoc(),
																			 querySelectApoyoFiltro),
												   conexion,
												   Integer.parseInt(delegateExecution.getProcessInstanceId()));
			
			delegateExecution.setVariable("contadorRegistrosCopiados", contadorTratados);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.fatal("Error ejecutando Filtro entre Tareas.- " + tareaEjecutar.getId_copia_bd(), e);
			controlEjecucion = false;
			
		} finally 
		{
			if (conexion != null) {try {
				conexion.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}};
			if (sentenciaSelect != null) {try {
				sentenciaSelect.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}};
			if (resultadoDatosOrigen!= null) {try {
				resultadoDatosOrigen.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}};
		}		
		
		return controlEjecucion;
		
	}	
	
	private void recuperarResulset(Connection conexion,String querySelectApoyo) 
			throws SQLException
	{
		Statement sentenciaSelect = null;
		resultadoDatosOrigenFiltro = null;

		try {
			//conexion = this.realizarConexion("encelado");
			sentenciaSelect = conexion.createStatement();
			resultadoDatosOrigenFiltro = sentenciaSelect.executeQuery(querySelectApoyo);	
		} catch (SQLException e) {
			log.fatal("Error ejecutando extraccion.- " + querySelectApoyo, e);
		}
		
	}
	
	private ArrayList<String> recuperarDatosOrigen(String id_conexion_BD_Origen,String ds_codigo_sql_origen) 
			throws SQLException
	{
		// para pruebas quitar el new
		BBDDDefinida baseDatosOrigenDatos = new BBDDDefinida();
		DaoAddBBDD capaServiciosSettingBD = new DaoAddBBDD();
		
		// recuperamos la conexion origen y los datos a insertar
		baseDatosOrigenDatos.setId_conexion_bd(id_conexion_BD_Origen);
		baseDatosOrigenDatos =  (BBDDDefinida) capaServiciosSettingBD.devolverObjeto(baseDatosOrigenDatos);
		
		
		//variables de trabajo del metodo
		ArrayList<String> arraySalida = new ArrayList<String>();
		ResultSet datosFiltro = baseDatosOrigenDatos.realizarPruebaConexion(ds_codigo_sql_origen);
		while (datosFiltro.next()) {
			String claveFiltro = (String) datosFiltro.getString(1);
			arraySalida.add(claveFiltro);	
		}
		
		baseDatosOrigenDatos.cerrarConexiones();
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
	
	private int ejecutarFiltro(ResultSet resulsetOrigenDatos,
								ArrayList<String> arrayFiltro,
								Connection conexion,int id_proceso) throws SQLException
	{
		int contadorFiltrados = 0;
		//variables de trabajo del metodo		
	    Statement sentencia_insert;		
	    Statement sentencia_Delete = null;
		
		//Borramos los datos anteriores
		//borramos la foto anterior
		String queryDeleteApoyo = "delete from encelado.im_datos_proceso"
							+ " where id_proceso = "  + id_proceso;							
		
		sentencia_Delete  = conexion.createStatement();
		sentencia_Delete.executeUpdate(queryDeleteApoyo);	
	    
		// realizamos el filtro.
		boolean pasaElFiltro = false;
		sentencia_insert  = conexion.createStatement();
		while (resultadoDatosOrigenFiltro.next()) {
			String claveOrigen = (String) resulsetOrigenDatos.getString("ds_referencia");
			claveOrigen = claveOrigen.trim();
			// buscamos la clave del filtro dentro del array
			for (String claveFiltro : arrayFiltro) {
				claveFiltro = claveFiltro.trim();
				if (claveFiltro.equals(claveOrigen))
				{					
					pasaElFiltro = true;
					break;
				}
			}
			
			if (pasaElFiltro)
			{
				String queryApoyo = 
						"INSERT INTO encelado.im_datos_proceso("
						+ "id_copia_bd_asoc,id_proceso, co_referencia, ds_referencia)"
						+ " VALUES ("
						+ "'" + resulsetOrigenDatos.getString("id_copia_bd_asoc") + "',"
						+ "'" + resulsetOrigenDatos.getString("id_proceso") + "'," 
						+ "'" + resulsetOrigenDatos.getString("co_referencia") + "'," 
						+ "'" + resulsetOrigenDatos.getString("ds_referencia") + "'"					
						+ ")"
						;
				
				
				sentencia_insert.executeUpdate(queryApoyo);	
				contadorFiltrados ++;
				pasaElFiltro = false;
			}
		}
		
		sentencia_insert.close();
		sentencia_Delete.close();
		
		return contadorFiltrados;
		
	}	
		
}

