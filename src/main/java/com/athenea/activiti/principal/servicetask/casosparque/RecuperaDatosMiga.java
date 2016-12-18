package com.athenea.activiti.principal.servicetask.casosparque;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.sql.*;

import com.athenea.activiti.principal.servicetask.TareasDeFlujo;
import com.utilidades.bbdd.ConexionBD;
import com.utilidades.logger.LoggerResultado;
import com.telefonica.na.*;

public class RecuperaDatosMiga implements TareasDeFlujo{

    private static Logger log = Logger.getLogger(RecuperaDatosMiga.class);
    
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
	
	public RecuperaDatosMiga() {
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
	    System.out.println("****recuperar datos miga*");
	    System.out.println("*************************");
	    
		//Variables de trabajo del programa
	    String querySelectApoyo;
		this.setDelegateExecution(delegateExecution);
		delegateExecution.setVariable("actividad", delegateExecution.getCurrentActivityName());
		delegateExecution.setVariable("resultado", "OK");
		delegateExecution.setVariable("error", "  ");
		// recuperamos el ID del proceso que nos invoca para recuperar 
		//los datos de MIGA
		
		try {
		
		    this.setIn_proceso_invocante(
		    		Integer.parseInt((String) delegateExecution.getVariable("id_proceso_invocante")));
					
			this.setConexion(this.realizarConexion("encelado"));

			querySelectApoyo = "select distinct(ds_referencia) "
					+ "from encelado.im_datos_proceso "
        			+ " where id_proceso = "  
        			+ this.getIn_proceso_invocante() 
        			+  " and co_referencia in ('abono') "
        			+  " and ds_referencia <> ''";			
			
			this.setResultadoApoyo(this.recuperarDatos(querySelectApoyo));

			// Se comprueba que la funcion devuelve datos en resulset para
			// recuperar de MIGA
			// en caso contrario se provoca error en proceso
			if (this.getResultadoApoyo() == null) {
				delegateExecution.setVariable("resultado", "NOK");
				delegateExecution.setVariable("error", "Error en Recuperacion de Datos para Miga.");
			} else {
				if (this.realizarAutocontencion())
				{
					this.tratarResulset(this.getResultadoApoyo(), delegateExecution);
				}
				
			}

		} 
		catch (SQLException e1) 
		{
			log.fatal("Proceso MIGA",e1);
			try {
				LoggerResultado.actualizarResultadoLogger(
						(String) this.getDelegateExecution().getVariable("id_tarea_asoc"), 
						this.getIn_proceso_invocante(), 
						"NOK", 
						"control SQLException proceso MIGA", 
						0);
			} catch (SQLException e2) {
				log.warn("Error actualizando resultado tabla control flujos tarea gmc.- " , e2);
			}
		} 
		catch (NullPointerException e) 
		{
			log.fatal("Proceso MIGA",e);
			try {
				LoggerResultado.actualizarResultadoLogger(
						(String) this.getDelegateExecution().getVariable("id_tarea_asoc"), 
						this.getIn_proceso_invocante(), 
						"NOK", 
						"control NullPointerException proceso MIGA.", 
						0);
			} catch (SQLException e1) {
				log.warn("Error actualizando resultado tabla control flujos tarea MIGA.- " , e);
			}
		}
		finally
		{
			this.cerrarRecursos();
		}
					
		//

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

			log.fatal("Error recuperando los datos que debe tratar la tarea MIGA .- ", e);
			return null;
		}
	}
	
	private boolean realizarAutocontencion()
	{
		String instruccionDB2 = null;
        Statement sentencia = null;
        
        try {
			sentencia = this.getConexion().createStatement();		
		        
		        instruccionDB2 = "delete from encelado.im_miga_equipos "
		        		+ "where id_proceso = '" 
		        		+ this.getIn_proceso_invocante() + "'";
			        try {
						sentencia.executeUpdate(instruccionDB2);
					} catch (SQLException e) {
						log.warn("Delete encelado.im_miga_equipos.- " , e);
					}
			        
		        instruccionDB2 = "delete from encelado.im_miga_datos_comp "
		        		+ "where id_proceso = '" 
		        		+ this.getIn_proceso_invocante() + "'";        
			        try {
						sentencia.executeUpdate(instruccionDB2);
					} catch (SQLException e) {
						log.warn("Delete encelado.im_miga_datos_comp.- " , e);
					}
			        
		        instruccionDB2 = "delete from encelado.im_miga_datos_cobro "
		        		+ "where id_proceso = '" 
		        		+ this.getIn_proceso_invocante() + "'";        
			        try {
						sentencia.executeUpdate(instruccionDB2);
					} catch (SQLException e) {
						log.warn("Delete encelado.im_miga_datos_cobro.- " , e);
					}
			        
		        instruccionDB2 = "delete from encelado.im_miga_ppal "
		        		+ "where id_proceso = '" 
		        		+ this.getIn_proceso_invocante() + "'";
			        try {
						sentencia.executeUpdate(instruccionDB2);
					} catch (SQLException e) {
						log.warn("Delete encelado.im_miga_ppal.- " , e);
					}
	        
        } catch (SQLException e1) {
        	log.warn("Error en la autocontencion MIGA.- ", e1);
		}finally {
			try {
				sentencia.close();
			} catch (SQLException e) {
				log.warn("Error close sentencia en la autocontencion MIGA.- ", e);
			}
		}	        
		
		return true;
		
	}
	
	private boolean tratarResulset(ResultSet resulsetTratar,DelegateExecution delegateExecution) 
	{		
		String numeroComercial = null;
		String provinciaNumeroComercial = null;
		String mensajeError = null;
		boolean controlEjecucionMetodo = true;
		int contadorTratados = 0;
		//
		// Realizamos la invocacion a NA por cada administrativo de entrada a la funcion.
		//
		try {

			while (resulsetTratar.next()) {								

				numeroComercial  = resulsetTratar.getString(1); 
				
				provinciaNumeroComercial = this.recuperarProvincia(numeroComercial);

				// recuperamos los datos principales.

				this.setServicioNAInvocado(new NAServicio("MGF0001"));
	
				this.rellenarCabeceraNA("MGSD05CO",provinciaNumeroComercial);				
				this.rellenarDatosEntradaServicioNA(numeroComercial,provinciaNumeroComercial);				
					try {
						this.invocacionServicioNA();
						this.grabarTablaSalidaDatosPrincipales();
						contadorTratados ++;
						// reinicializamos el servicio NA
						this.getServicioNAInvocado().unload();
						
						// recuperamos los datos de los equipos.
						this.setServicioNAInvocado(new NAServicio("MGF0002"));
						this.rellenarCabeceraNA("MGSD10CO",provinciaNumeroComercial);				
						this.rellenarDatosEntradaServicioNA(numeroComercial,provinciaNumeroComercial);
						
							try {
								this.invocacionServicioNA();
								this.grabarTablaSalidaDatosEquipos(numeroComercial);
								
								// solo si se completan todas las tablas se contabiliza el registro para la actualizacion
								// del resultado al final del flujo ativiti.								
							}catch (NAWRException e) 
							{
								log.error("Abono sin equipos en MIGA" 
											+ numeroComercial );
										//	+ this.getServicioNAInvocado().getCampo("CABECERA").getString());
								
								if (!this.validarErrorNA())
								{
									mensajeError = "Error invocando ServicioNA MGSD10CO numero comercial" ;
									controlEjecucionMetodo = false;
									break;
								}             	                	                	
			            	}
							
							// reinicializamos el servicio NA para la proxima invocacion.
							this.getServicioNAInvocado().unload();
						
					}catch (NAWRException e) {
	
						log.error("No existe numero comercial" 
								+ numeroComercial );
							//	+ this.getServicioNAInvocado().getCampo("CABECERA").getString());
						
						if (!this.validarErrorNA())
						{
							mensajeError = "Error invocando ServicioNA MGSD05CO numero comercial" ;
							controlEjecucionMetodo = false;
							break;
						}                      	                	                	
	            	}												
				
			}			

		} catch (SQLException e) 
		{
			log.fatal("Error procesando envio MIGA", e);
			mensajeError = "Error procesando envio MIGA" ;
			controlEjecucionMetodo = false;
		} 
		catch (NullPointerException e) 
		{
			log.fatal("No existen datos que tratar en tarea MIGA", e);
			mensajeError = "No existen datos que tratar en tarea MIGA" ;
    		controlEjecucionMetodo = false;	
		}
		catch (Exception e) 
		{
			log.fatal("Error procesando envio MIGA", e);
			mensajeError = "Error procesando envio MIGA" ;
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

		} catch (SQLException e1) {
			log.warn("Error actualizando resultado tabla control flujos tarea MIGA.- " , e1);
		}	
		
    
		return controlEjecucionMetodo;
	}
	
	private String recuperarProvincia(String numeroComercial) 
	{
		String prefijo = numeroComercial.substring(1, 3);
		String prefijo2 = numeroComercial.substring(1, 2);
		String provincia = "";
		String instruccionDB2 = null;
        Statement sentencia = null;
        ResultSet resultado = null;
		   
        try {
			sentencia = this.getConexion().createStatement();		
		        
			instruccionDB2 = "select * from encelado.im_prefijo_abono" 
					+ " where prefijo " 
					+ " in ('" + prefijo + "','" + prefijo2 + "')"
					+ " order by 1 desc";
			
			        try {						
			        	resultado = sentencia.executeQuery(instruccionDB2);
						if (resultado.next()) {
							provincia = resultado.getString(2);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						System.out.println("error resulset encelado.im_prefijo_abono");
					}			        
	        
        } catch (SQLException e1) {
			log.warn("Recuperando Provincia Tarea Miga.-" , e1);
		}	        
		
		return provincia;
		
	}
	
	private void rellenarCabeceraNA(String servicio,String provinciaInvocante) 
			throws NAWRException
	{
			
			this.getServicioNAInvocado().setCampo(
						"CABECERA.IDENTIFICACION-LLAMANTE.NOMBRE", "NH");
			this.getServicioNAInvocado().setCampo(
						"CABECERA.IDENTIFICACION-LLAMANTE.TIPO", "03");
			this.getServicioNAInvocado().setCampo(
						"CABECERA.IDENTIFICACION-SERVICIO.NOMBRE-SERVICIO", servicio);
			this.getServicioNAInvocado().setCampo(
						"CABECERA.IDENTIFICACION-SERVICIO.VERSION-SERVICIO", "04");
			this.getServicioNAInvocado().setCampo(
						"CABECERA.CO-ROUTING", provinciaInvocante); 			
			this.getServicioNAInvocado().setCampo(
					"CABECERA.CO-MIRROR-HOST", "NHMI");
		
	}

	private void rellenarDatosEntradaServicioNA(String numeroComercial, String provinciaNumeroComercial) 
			throws NAWRException
	{
		this.getServicioNAInvocado().setCampo(
				"ENTRADA.PROVINCIA-E", provinciaNumeroComercial);
		this.getServicioNAInvocado().setCampo(
				"ENTRADA.NU-TELEFONO-E", numeroComercial);	
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
		        
			        try {
			        	instruccionDB2 = this.montarInsertPpal();
			        	
			        	if (instruccionDB2 != "")
			        	{
			        		sentencia.executeUpdate(instruccionDB2);
			        		
			        		//insrtamos los datos de cobro
					        try {
					        	instruccionDB2 = this.montarInsertDatosCobro();
					        	
					        	if (instruccionDB2 != "")
					        	{
					        		sentencia.executeUpdate(instruccionDB2);
					        	} else 
					        	{
									log.error("error componiendo Insert im_miga_datos_cobro.");
									controlEjecucion = false;
					        	}
					        	
							} catch (SQLException e) {
								log.error("Insertando datos im_miga_ppal", e);
								controlEjecucion = false;
							}
					        
					        try {
					    		for (
					    				int indice = 1; 
					    				(indice <= this.getServicioNAInvocado().getCampo("SALIDA-1.OCURDTCO").intValue()
					    						|| !controlEjecucion); 
					    				indice ++ 
					    				)
					    			{
						        		instruccionDB2 = this.montarInsertDatosComplementarios(indice) ;			    			
					    						        	
							        	if (instruccionDB2 != "")
							        	{
							        		sentencia.executeUpdate(instruccionDB2);
							        		// comprobamos si el dato complementario es el 33 en cuyo caso
							        		// generamos un registro en la tabla encelado.im_datos_proceso de tipo 
							        		// administrativo.
							        		this.insertDatoComplementario33(indice);
							        		
							        	} else 
							        	{
							        		log.error("error componiendo Insert im_miga_datos_cobro.");
											controlEjecucion = false;
							        	}
					    			}
					        	
							} catch (SQLException e) {
								log.error("Error SQL Insertando datos im_miga_datos_cobro", e);
								controlEjecucion = false;
							} catch (NAWRException e) {
								log.error("Error NA al recorrer array datos cobro.", e);
								controlEjecucion = false;
							}
			        	} else 
			        	{
			        		log.error("componiendo Insert  encelado.im_miga_ppal");
			        	}
			        } 
			        catch (SQLException e) {
			        	log.error("Ejecutando Insert  encelado.im_miga_ppal");
					}
	        
        } catch (SQLException e1) {
			log.error("Error Creando Statement MIGA", e1);
			controlEjecucion = false;
		} 
		return controlEjecucion;	        
				
	}
	
	private String montarInsertPpal() 
	{
		try {
			return  "INSERT INTO encelado.im_miga_ppal values "
					+ "('" 
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.NUMTELEF").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.CLACENTR").getString() + "','" 			    
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.NUMEXTEN").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.ESTABONO").getString() + "','"			    
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.CLAVSEGU").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.FECHALTA").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.TIPOLINE").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.CENACAMN").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.TELACAMN").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.EXTACAMN").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.SITECAMN").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.IDENTITU").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.TITUABON").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.CLACALLE").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.CLAVENTI").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.DISTPOST").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.NUMCASAE").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.TEXTDOMI").getString() + "','" 
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.TIPODOCU").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.NUMEDOCU").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.CLASABON").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.TARIURBA").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.TARISERM").getString() + "','"
					+ this.getServicioNAInvocado().getCampo("SALIDA-1.CLAPROFE").getString() + "','"
					+ this.getServicioNAInvocado().getCampo("SALIDA-1.TEXPROFE").getString() + "','"
					+ this.getServicioNAInvocado().getCampo("SALIDA-1.TEXTINSR").getString() + "','"
					+ this.getServicioNAInvocado().getCampo("SALIDA-1.ENTAPGUI").getString() + "','"
					+ this.getServicioNAInvocado().getCampo("SALIDA-1.CENTELCA").getString() + "','"
					+ this.getServicioNAInvocado().getCampo("SALIDA-1.TELEFOCA").getString() + "','"
					+ this.getServicioNAInvocado().getCampo("SALIDA-1.EXTTELCA").getString() + "','"
					+ this.getServicioNAInvocado().getCampo("INDICADORES.LINEAS-ENLACE").getString() + "','"
					+ this.getServicioNAInvocado().getCampo("INDICADORES.LINEAS-PROLON").getString() + "','"
				    + this.getIn_proceso_invocante() +"')";
		} catch (NAWRException e) {
			log.warn("Recuperando DatosNA montarInsertPpal MIGA.- " , e);
			return "";

		}				
	}

	private String montarInsertDatosCobro() 
	{
		try {
			return  "INSERT INTO encelado.im_miga_datos_cobro values "
					+ "('" 
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.NUMTELEF").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.FECHAULA").getString() + "','" 			    
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.FECHADED").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.CLASISCO").getString() + "','"			    
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.ENTICOBR").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.CUENCORR").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.NUMPLAZO").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.DEUDPEND").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.IMPOPLAZ").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.CLAVESPE").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.CENCOBRO").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.TELCOBRO").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.EXTCOBRO").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.TITUCUCO").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.ODTCPCOB").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.NUMINTCA").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.NUMEXTCA").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.NUMERLOC").getString() + "','" 
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.OTROSDAT.TIPCLIEN").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.OTROSDAT.CONFIDNI").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.OTROSDAT.DTCOMP15.DATCO15C").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.OTROSDAT.DTCOMP15.DATCO15T").getString() + "','"
				    + this.getServicioNAInvocado().getCampo("SALIDA-1.OTROSDAT.DTCOMP15.DATCO15E").getString() + "','"
					+ this.getServicioNAInvocado().getCampo("SALIDA-1.OTROSDAT.NUMSANGR").getString() + "','"
					+ this.getServicioNAInvocado().getCampo("SALIDA-1.FECALCPA").getString() + "','"
					+ this.getServicioNAInvocado().getCampo("SALIDA-1.DOMICORR").getString() + "','"
					+ this.getServicioNAInvocado().getCampo("SALIDA-1.LOCACORR").getString() + "','"					
				    + this.getIn_proceso_invocante() +"')";
		} catch (NAWRException e) {
			log.warn("Recuperando DatosNA montarInsertDatosCobro MIGA.- " , e);
			return "";
		}				
	}		
	
	private String montarInsertDatosComplementarios(int indice) 
	{		
		try {
			return  "INSERT INTO encelado.im_miga_datos_comp values "
    				+ "('" 
    				+ this.getServicioNAInvocado().getCampo("SALIDA-1.NUMTELEF").getString() + "','"
    				+ this.getServicioNAInvocado().getCampo("SALIDA-1.DATOCOMP.CLAVDTCO", indice).getString() + "','"    				
    	    		+ this.getServicioNAInvocado().getCampo("SALIDA-1.DATOCOMP.TEXTDTCO", indice).getString() + "','"
				    + this.getIn_proceso_invocante() +"')"; 
		} catch (NAWRException e) {
			log.warn("Recuperando DatosNA montarInsertDatosComplementarios MIGA.- " , e);
			return "";
		}
	}
	
	private void insertDatoComplementario33(int indice) 
	{
		
		try {
			if (this.getServicioNAInvocado().getCampo("SALIDA-1.DATOCOMP.CLAVDTCO", indice).getString().equals("33"))
			{
				 String numeroComercialRelacionado =
						this.getServicioNAInvocado().getCampo("SALIDA-1.DATOCOMP.TEXTDTCO", indice).getString().trim();
				
				 Statement sentencia = null;
					String instruccionDB2 = null;
			        
			        try {
						sentencia = this.getConexion().createStatement();		
					        
						        try {
						        	instruccionDB2 = 
						        			"INSERT INTO encelado.im_datos_proceso("
											+ "id_copia_bd_asoc,id_proceso, co_referencia, ds_referencia)"
											+ " VALUES ("
											+ "'" + this.getDelegateExecution().getVariable("id_flujo_tarea_asoc") + "'," 
											+ "'" + this.getIn_proceso_invocante() + "'," 
											+ "'administrativo'," 
											+ "'" + numeroComercialRelacionado + "'"					
											+ ")"
											;
						        	
						        	if (instruccionDB2 != "")
						        	{
						        		sentencia.executeUpdate(instruccionDB2);
						        	} else 
						        	{
						        		log.error("error componiendo Insert encelado.im_datos_proceso." + numeroComercialRelacionado);
						        	}
						        	
								} catch (SQLException e) {
									log.error("error Insert administratico encelado.im_datos_proceso MIGA." + numeroComercialRelacionado, e);
								}							        						        													        
				        
			        } catch (SQLException e1) {
						log.error("error createStatement en insert administratico encelado.im_datos_proceso MIGA." + numeroComercialRelacionado, e1);
					} 							
				
			}
		} catch (NAWRException e) {
			log.warn("Recuperando DatosNA insertDatoComplementario33 MIGA.- " , e);
		}
	}
	
	private boolean grabarTablaSalidaDatosEquipos(String numeroComercial) 
	{
        Statement sentencia = null;
		String instruccionDB2 = null;
        
        try {
			sentencia = this.getConexion().createStatement();		
			        
			        try {
			    		for (
			    				int indice = 1; 
			    				indice <= this.getServicioNAInvocado().getCampo("SALIDA.NUMEQUIP").intValue(); 
			    				indice ++ 
			    				)
			    			{
				        		instruccionDB2 = this.montarInsertDatosEquipos(indice,numeroComercial) ;			    			
			    						        	
					        	if (instruccionDB2 != "")
					        	{
					        		sentencia.executeUpdate(instruccionDB2);
					        	} else 
					        	{
					        		log.error("error componiendo Insert im_miga_equipos." + numeroComercial);
					        	}
			    			}
			        	
					} catch (SQLException e) {
						log.error("error createStatement en insert im_miga_equipos MIGA." + numeroComercial, e);
					} catch (NAWRException e) {						
						log.error("Error al recorrer array en insert equipos MIGA." + numeroComercial, e);
					}
			        
	        
        } catch (SQLException e1) {
			log.error("error createStatement en im_miga_equipos equipos MIGA." + numeroComercial, e1);
		} 
		return true;	        	
		
	}
	
	private String montarInsertDatosEquipos(int indice,String numeroComercial) throws NAWRException 
	{		
		float cuotaEquipo = 0;
		
			cuotaEquipo = (this.getServicioNAInvocado().getCampo("SALIDA.EQUIPOS.IMPOTOTA", indice).floatValue()
						 / 100);
			
			return  "INSERT INTO encelado.im_miga_equipos values "
    				+ "('" 
    				+ numeroComercial + "','"
    				+ this.getServicioNAInvocado().getCampo("SALIDA.EQUIPOS.CODIEQUI", indice).getString() + "','"    				
    	    		+ this.getServicioNAInvocado().getCampo("SALIDA.EQUIPOS.UNIDEQUI", indice).intValue() + "',"
    	    		+ cuotaEquipo + "," +
				    + this.getIn_proceso_invocante() +")"; 

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
			log.warn("error cerrando recursos tarea MIGA.", e);
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
			log.warn("Evaluando cabeceraNA invocacion GMC.- " ,e);
		}	
		
		return controlValidacion;
		
	}
	
}
