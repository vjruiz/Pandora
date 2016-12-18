package com.athenea.activiti.principal.servicetask.casosparque;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.sql.*;

import com.athenea.activiti.principal.servicetask.TareasDeFlujo;
import com.utilidades.bbdd.ConexionBD;
import com.utilidades.logger.LoggerResultado;
import com.telefonica.na.*;

public class RecuperaDatosGMC implements TareasDeFlujo{

	
    private static Logger log = Logger.getLogger(RecuperaDatosGMC.class);
	
	//Definimos las variables de NA a utilizar
	private NAServicio servicioNAInvocado = null;
	
	//Definimos las variables de conexion y trabajo con la BBDD
	private Connection conexion = null;
	private Statement sentenciaSelect = null;
	private ResultSet resultadoApoyo  = null;
	private DelegateExecution delegateExecution = null;
	
	private int in_proceso_invocante = 0;
 	
 	{
 		// En el bloque de inicializacion de la clase nos aseguramos que 
 		// las variables criticas se inicialicen con cada
 		// instancia de la clase.
 		
 		servicioNAInvocado = null;
 		conexion = null;
 		sentenciaSelect = null; 		
 		resultadoApoyo  = null;
 		delegateExecution = null;
 		
 	} 	 	
	
	public RecuperaDatosGMC() {
		super();
		// TODO Auto-generated constructor stub
	}


	public NAServicio getServicioNAInvocado() {
		return servicioNAInvocado;
	}


	public void setServicioNAInvocado(NAServicio servicioNAInvocado) {
		this.servicioNAInvocado = servicioNAInvocado;
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


	public ResultSet getResultadoApoyo() {
		return resultadoApoyo;
	}


	public void setResultadoApoyo(ResultSet resultadoApoyo) {
		this.resultadoApoyo = resultadoApoyo;
	}

	public int getIn_proceso_invocante() {
		return in_proceso_invocante;
	}


	public void setIn_proceso_invocante(int in_proceso_invocante) {
		this.in_proceso_invocante = in_proceso_invocante;
	}

	public DelegateExecution getDelegateExecution() {
		return delegateExecution;
	}


	public void setDelegateExecution(DelegateExecution delegateExecution) {
		this.delegateExecution = delegateExecution;
	}


	@Override
	public void execute(DelegateExecution delegateExecution, ApplicationContext contextSpring) 
	{
	    
	    System.out.println("*************************");
	    System.out.println("****recuperar datos GMC*");
	    System.out.println("*************************");
	    
		//Variables de trabajo del programa
	    String querySelectApoyo;
		this.setDelegateExecution(delegateExecution);
		delegateExecution.setVariable("actividad", delegateExecution.getCurrentActivityName());
		delegateExecution.setVariable("resultado", "OK");
		delegateExecution.setVariable("error", "  ");
		
		try {
		
		    this.setIn_proceso_invocante(
		    		Integer.parseInt((String) delegateExecution.getVariable("id_proceso_invocante")));
					
			this.setConexion(this.realizarConexion("encelado"));

			querySelectApoyo = "select distinct(ds_referencia) "
					+ "from encelado.im_datos_proceso "
        			+ " where id_proceso = "  
        			+ this.getIn_proceso_invocante() 
        			+ " and co_referencia in ('administrativo')"
        			+ " and ds_referencia <> '' ";
			
			this.setResultadoApoyo(this.recuperarDatos(querySelectApoyo));

			if (this.getResultadoApoyo() == null) {
				delegateExecution.setVariable("resultado", "NOK");
				delegateExecution.setVariable("error", "Error en Recuperacion de Datos para GMC.");
			} else {
				if (this.realizarAutocontencion())
				{
					this.tratarResulset(this.getResultadoApoyo(), delegateExecution);
				}
				
			}

		} 
		catch (SQLException e1) 
		{
			log.fatal("Proceso GMC",e1);
			try {	
				LoggerResultado.actualizarResultadoLogger(
						(String) this.getDelegateExecution().getVariable("id_tarea_asoc"), 
						this.getIn_proceso_invocante(), 
						"NOK", 
						"control SQLException proceso GMC.", 
						0);
			} catch (SQLException e2) {
				log.warn("Error actualizando resultado tabla control flujos tarea gmc.- " , e2);
			}
		} 
		catch (NullPointerException e) 
		{
			log.fatal("Proceso GMC",e);
			try {
				LoggerResultado.actualizarResultadoLogger(
						(String) this.getDelegateExecution().getVariable("id_tarea_asoc"), 
						this.getIn_proceso_invocante(), 
						"NOK", 
						"control NullPointerException proceso GMC.", 
						0);
			} catch (SQLException e1) {
				log.warn("Error actualizando resultado tabla control flujos tarea GMC.- " , e);
			}
		}
		finally
		{
			this.cerrarRecursos();
		}

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
		try {
			this.setSentenciaSelect(this.getConexion().createStatement());
			return this.getSentenciaSelect().executeQuery(querySelect);

		} catch (SQLException e) {

			log.fatal("Error recuperando los datos que debe tratar la tarea GMC .- ", e);
			return null;
		}
	}
	
	private boolean realizarAutocontencion()
	{
		String instruccionDB2 = null;
        Statement sentencia = null;
        
        try {
			sentencia = this.getConexion().createStatement();		
		        
		        instruccionDB2 = "delete from encelado.im_gmc_ppal "
		        		+ "where id_proceso = '" 
		        		+ this.getIn_proceso_invocante() + "'";
			        try {
						sentencia.executeUpdate(instruccionDB2);
					} catch (SQLException e) {
						log.warn("Delete encelado.im_gmc_ppal.- " , e);
					}
			        
		        instruccionDB2 = "delete from encelado.im_gmc_equipos "
		        		+ "where id_proceso = '" 
		        		+ this.getIn_proceso_invocante() + "'";        
			        try {
						sentencia.executeUpdate(instruccionDB2);
					} catch (SQLException e) {
						log.warn("Delete encelado.im_gmc_equipos.- " , e);
					}
			        
		        instruccionDB2 = "delete from encelado.im_gmc_datos_complementarios "
		        		+ "where id_proceso = '" 
		        		+ this.getIn_proceso_invocante() + "'";        
			        try {
						sentencia.executeUpdate(instruccionDB2);
					} catch (SQLException e) {
						log.warn("Delete encelado.im_gmc_datos_complementarios.- " , e);
					}
			        	        
        } catch (SQLException e1) {
			log.warn("Error en la autocontencion GMC.- ", e1);
		}	        		
		return true;		
	}
	
	private boolean tratarResulset(ResultSet resulsetTratar,DelegateExecution delegateExecution) 
	{		
		String numeroComercial = null;
		String mensajeError = null;
		boolean controlEjecucionMetodo = true;
		int contadorTratados = 0;
		//
		// Realizamos la invocacion a NA por cada administrativo de entrada a la funcion.
		//
		try {

			while (resulsetTratar.next()) {								

				numeroComercial  = resulsetTratar.getString(1); 				

				this.setServicioNAInvocado(new NAServicio("LGF0024"));	
				this.rellenarCabeceraNA("LGSD01CO");				
				this.rellenarDatosEntradaServicioNA(numeroComercial);	
				
					try {					    
						this.invocacionServicioNA();						
						this.grabarTablaSalidaDatosPrincipales();						
						this.getServicioNAInvocado().unload();
						
					}catch (NAWRException e) 
					{
						log.error("No existe numero comercial"  
									+ numeroComercial );
								//	+ this.getServicioNAInvocado().getCampo("CABECERA").getString());
						
						if (!this.validarErrorNA())
						{
							mensajeError = "Error invocando ServicioNA LGSD01CO numero comercial" ;
							controlEjecucionMetodo = false;
							break;
						}             	                	                	
	            	}												
				
			}					

		} 
		catch (SQLException e) 
		{
			log.fatal("Error SQL procesando envio GMC", e);
			mensajeError = "Error SQL procesando envio GMC" ;
			controlEjecucionMetodo = false;  
			
		} 
		catch (NullPointerException e) 
		{
			log.fatal("No existen datos que tratar en tarea GMC", e);
			mensajeError = "No existen datos que tratar en tarea GMC" ;
			controlEjecucionMetodo = false;  		    	
		}
		catch (Exception e) 
		{
			log.fatal("Error procesando envio GMC", e);
			mensajeError = "Error procesando envio GMC" ;
			controlEjecucionMetodo = false;
		}
		
		try 
		{
			if (controlEjecucionMetodo)
			{
				LoggerResultado.actualizarResultadoLogger(
						(String) this.getDelegateExecution().getVariable("id_tarea_asoc"), 
						this.getIn_proceso_invocante(), 
						"OK", 
						"Resultado Correcto", 
						contadorTratados);
			}else
			{
				LoggerResultado.actualizarResultadoLogger(
						(String) this.getDelegateExecution().getVariable("id_tarea_asoc"),  
						this.getIn_proceso_invocante(), 
						"NOK", 
						mensajeError, 
						contadorTratados);	
			}
		}
		catch (SQLException e1) {
			log.warn("Error actualizando resultado tabla control flujos tarea gmc.- " , e1);
		}	
    
		return controlEjecucionMetodo;
	}
	
	private void rellenarCabeceraNA(String servicio) 
			throws NAWRException
	{//JA00FCCO
			
			this.getServicioNAInvocado().setCampo(
						"CABECERA.IDENTIFICACION-LLAMANTE.NOMBRE", "JA");
			this.getServicioNAInvocado().setCampo(
						"CABECERA.IDENTIFICACION-LLAMANTE.TIPO", "01");
			this.getServicioNAInvocado().setCampo(
						"CABECERA.IDENTIFICACION-SERVICIO.NOMBRE-SERVICIO", servicio);
			this.getServicioNAInvocado().setCampo(
						"CABECERA.IDENTIFICACION-SERVICIO.VERSION-SERVICIO", "01");			
			this.getServicioNAInvocado().setCampo(
					"CABECERA.CO-MIRROR-HOST", "NHMI");
			
	}

	private void rellenarDatosEntradaServicioNA(String numeroComercial) 
			throws NAWRException
	{
		this.getServicioNAInvocado().setCampo(
				"LGCVCBCO.CB-NUM-ADMINISTR", numeroComercial);
		this.getServicioNAInvocado().setCampo(
				"CB-PROCEDE-GMIP", "NO");		
		this.getServicioNAInvocado().setCampo(
				"CB-TIPO-OPERACION" , "00F");	
	}
	
	private void invocacionServicioNA() throws NAWRException 
	{
		
		this.getServicioNAInvocado().ejecutar();		
		
	}
	
	private boolean grabarTablaSalidaDatosPrincipales() 
			throws SQLException
	{		
		boolean controlEjecucion = true;
        Statement sentencia = null;
		String instruccionDB2 = null;
        
        try {
			sentencia = this.getConexion().createStatement();		
		        	
					// Insertamos los datos principales
			        try {
			        	instruccionDB2 = this.montarInsertPpal();
			        	
			        	if (instruccionDB2 != "")
			        	{
			        		sentencia.executeUpdate(instruccionDB2);
			        		
			        		// Insertamos los datos complementarios
					        try {
					    		for (
					    				int indice = 1; 
					    				indice <= 10; 
					    				indice ++ 
					    				)
					    			{
						        		instruccionDB2 = this.montarInsertDatosComplementarios(indice) ;			    			
					    						        	
							        	if (instruccionDB2 != "")
							        	{
							        		sentencia.executeUpdate(instruccionDB2);
							        		
							        	} else 
							        	{
							        		continue;
							        	}
					    			}
					        	
							} catch (SQLException e) {
								log.error("Insertando datos im_gmc_datos_complementarios", e);
								controlEjecucion = false;
							} 
					        
			        		// Insertamos los datos de equipos los cuales se entiende que no se insertan si fallan
					        // los datos principales, no asi con los datos complementarios que son independiente de los equipos
					        try {
					    		for (
					    				int indice = 1; 
					    				indice <= this.getServicioNAInvocado().getCampo("LGCVCFCS.CFCS-DATOS-DE-EQUIPO.CFCS-TOT-EQUIPOS").intValue();
					    				indice ++ 
					    				)
					    			{
						        		instruccionDB2 = this.montarInsertDatosEquipos(indice) ;			    			
					    						        	
							        	if (instruccionDB2 != "")
							        	{
							        		sentencia.executeUpdate(instruccionDB2);
							        		
							        	} else 
							        	{
							        		log.error("componiendo Insert encelado.im_gmc_datos_equipos");
							        		controlEjecucion = false;
							        		break;
							        	}
					    			}
					        	
							} catch (SQLException e) {
								log.error("Error SQL Insertando datos im_gmc_datos_equipos", e);
								controlEjecucion = false;
							} catch (NAWRException e) {
								log.error("Error NA al recorrer array datos equipos.", e);
								controlEjecucion = false;
							}
				        
			        	} else 
			        	{
			        		log.error("componiendo Insert  encelado.im_gmc_ppal");
			        	}
			        } 
			        catch (SQLException e) {
			        	log.error("Ejecutando Insert  encelado.im_gmc_ppal",e);
					}				   
	        
        } catch (SQLException e1) {
			log.error("Error Creando Statement GMC", e1);
			controlEjecucion = false;
		} 
		return controlEjecucion;	        
				
	}
	
	private String montarInsertPpal() 
	{
		try {
			return  "INSERT INTO encelado.im_gmc_ppal values "
					+ "(" 
				    + this.getIn_proceso_invocante() +",'"
					+ this.getServicioNAInvocado().getCampo("LGCVCBCO.CB-NUM-ADMINISTR").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("LGCVCFCS.CFCS-DATOS-DEL-CIRCUITO.CFCS-CO-TIPO-DOCUMENTO").getString() + "','" 			    
				    + this.getServicioNAInvocado().getCampo("LGCVCFCS.CFCS-DATOS-DEL-CIRCUITO.CFCS-NU-DOCUMENTO-CLIENTE").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("LGCVCFCS.CFCS-DATOS-DEL-CIRCUITO.CFCS-NO-NOMBRE-TITULAR").getString().trim() + "','"			    
				    + this.getServicioNAInvocado().getCampo("LGCVCFCS.CFCS-DATOS-DEL-CIRCUITO.CFCS-NO-PRIM-APELL-TITULAR").getString().trim() + "','"
				    + this.getServicioNAInvocado().getCampo("LGCVCFCS.CFCS-DATOS-DEL-CIRCUITO.CFCS-NO-SEG-APELL-TITULAR").getString().trim() + "','"
				    + this.getServicioNAInvocado().getCampo("LGCVCFCS.CFCS-DATOS-DEL-CIRCUITO.CFCS-FX-CONFORMIDAD").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("LGCVCFCS.CFCS-DATOS-DEL-CIRCUITO.CFCS-CO-CLASE-CIRCUITO").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("LGCVCFCS.CFCS-DATOS-DEL-CIRCUITO.CFCS-CO-EQUIPO").getString() + "')"
				    ;				    
		} catch (NAWRException e) {
			log.warn("Recuperando DatosNA montarInsertPpal GMC.- " , e);
			return "";
		}				
	}
	
	private String montarInsertDatosComplementarios(int indice) 
	{		
		try {
			String datoComplementario = 
					this.getServicioNAInvocado().getCampo("LGCVCFCS.CFCS-DATOS-DEL-CIRCUITO.CFCS-GR-DATOS-COMPL.CFCS-CO-TEXTO-INF", indice).getString().trim();
			
			
			if ((datoComplementario.equals("")) ||
				(datoComplementario == null) ||
				(datoComplementario.equals(" ")))
				{
					return "";
				}
			else 
			{
				return  "INSERT INTO encelado.im_gmc_datos_complementarios values "
	    				+ "(" 
						+ this.getIn_proceso_invocante() +",'"
	    				+ this.getServicioNAInvocado().getCampo("LGCVCBCO.CB-NUM-ADMINISTR" ).getString() + "','"
	    				+ this.getServicioNAInvocado().getCampo("LGCVCFCS.CFCS-DATOS-DEL-CIRCUITO.CFCS-GR-DATOS-COMPL.CFCS-CO-TEXTO-INF", indice).getString() + "','"    				
	    	    		+ this.getServicioNAInvocado().getCampo("LGCVCFCS.CFCS-DATOS-DEL-CIRCUITO.CFCS-GR-DATOS-COMPL.CFCS-NO-TEXTO-INF", indice).getString() + "')"
					    ;	
			}
			 
		} catch (NAWRException e) {
			log.warn("Recuperando DatosNA montarInsertDatosComplementarios GMC.- " , e);
			return "";
		}
	}
	
	private String montarInsertDatosEquipos(int indice) 
	{		
		
		try {
			
			return  "INSERT INTO encelado.im_gmc_equipos values "
    				+ "(" 
    				+ this.getIn_proceso_invocante() +",'"
    				+ this.getServicioNAInvocado().getCampo("LGCVCBCO.CB-NUM-ADMINISTR" ).getString() + "','"
    				+ this.getServicioNAInvocado().getCampo("LGCVCFCS.CFCS-DATOS-DE-EQUIPO.CFCS-TABLA-EQUIPO.CFCS-CO-EQUIPO-EQPO", indice).getString() + "','"    				
    	    		+ this.getServicioNAInvocado().getCampo("LGCVCFCS.CFCS-DATOS-DE-EQUIPO.CFCS-TABLA-EQUIPO.CFCS-CO-EQUIPO-COEF-USO", indice).getString() + "','"
    	    		+ this.getServicioNAInvocado().getCampo("LGCVCFCS.CFCS-DATOS-DE-EQUIPO.CFCS-TABLA-EQUIPO.CFCS-NU-UNIDADES", indice).getString() + "')"    	    		
    	    		; 
		} catch (NAWRException e) {
			log.warn("Recuperando DatosNA montarInsertDatosEquipos GMC.- ", e);
			return "";
		}
	}
	
	private void cerrarRecursos() 
	{
		this.getServicioNAInvocado().unload();	
		try {
			if (this.getConexion() != null)
			{
				this.getConexion().close();
			}
			if (this.getSentenciaSelect() != null)
			{
				this.getSentenciaSelect().close();
			}
			if (this.getResultadoApoyo() != null)
			{
				this.getResultadoApoyo().close();	
			}
				
		} catch (SQLException e) {
			log.warn("Cerrando Recursos GMC.- " , e);
		}		
			
	}
	
	private boolean validarErrorNA() 
	{
		boolean controlValidacion = false;
		
		try {
/*			String codigoError =
					this.getServicioNAInvocado().getCampo("CABECERA.CODIGO-ERROR.COD-ERROR").getString().trim();
			if (codigoError.equals("0008"))
			{
				controlValidacion = true;
			}*/
			if (this.getServicioNAInvocado().getCampo("CABECERA.RESULTADO-EJECUCION-SERVICIO.TIPO-ERROR").getString().trim().equals("A")	)
			{
				controlValidacion = true;
			}
			
		} catch (NAWRException e) 
		{
			log.warn("Evaluando cabeceraNA invocacion GMC.- " , e);
		}	
		
		return controlValidacion;
		
	}
	
}
