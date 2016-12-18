package com.athenea.activiti.principal.servicetask.casosparque;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.context.ApplicationContext;

import java.sql.*;

import com.athenea.activiti.principal.servicetask.TareasDeFlujo;
import com.telefonica.na.*;

public class RecuperaDatosDT implements TareasDeFlujo{

	
	//Definimos las variables de NA a utilizar
	
	// 1- La del servicio NA
	private static NAServicio st = null;
	//private static NAServicio sEquipos = null;
	
	// 2- La de los dampos de salida del servicio
	
	private static NACampo SN30TELEFONO  = null;  
	private static NACampo SN30NUISN     = null;  
	private static NACampo SN30MODALIDAD = null;
	private static NACampo SN30FXALTA    = null;
	private static NACampo SN30FXBAJA    = null;
	private static NACampo SN30NUISNSAL  = null;
	private static NACampo SN30SITUACION = null;
	private static NACampo SN30DESTINOS  = null;
	
	
	public void execute(DelegateExecution delegateExecution, ApplicationContext contextSpring)
	{
		//Get access to the connector input parameters
		//delegateExecution.getProcessInstanceId();
		delegateExecution.setVariable("actividad", delegateExecution.getCurrentActivityName());
		delegateExecution.setVariable("resultado", "OK");
		delegateExecution.setVariable("error", "  ");
		//TODO execute your business logic here 
	
		//WARNING : Set the output of the connector execution. If outputs are not set, connector fails
		//setResultado(resultado);
		//setError(error);
		//setActividad(actividad);
		// variables
				String telefono = "";
				String prefijo = "";
				String prefijo2 = "";
				String provincia = "";
				String cadenaSalida = "";
				String modalidad = "";
				
				Integer indice = 0;
				Float importe ;
				Boolean hayDatos = false;
				
				//PSQL
				String cadenaConexion = "jdbc:postgresql://127.0.0.1/encelado?user=postgres&password=1234";

				String querySelect = null;
				String querySelect2 = null;
				String queryInsert = null;
				String queryDelete = null;
				//String salidaMiga = null;
				Connection conexion = null;
		        Statement sentencia = null;
		        Statement sentencia2 = null;
		        ResultSet resultado = null;
		        ResultSet resultado2 = null;
		        //ResultSet resultado3 = null;
		        Statement sentencia_insert = null;
		        Statement sentencia_insert2 = null;
		        Statement sentencia_insert3 = null;
		        Statement sentencia_insert4 = null;
		        Statement sentencia_insert5 = null;
		        Statement sentencia_insert6 = null;
		        Statement sentencia_delete = null;
		        Statement sentencia_delete2 = null;
		        Statement sentencia_delete3 = null;

		        //Statement sentencia_delete = null;
				
		        // Primero vamos a recuperar los abonos con ITSM o pedido del caso y vamos
		        // a acceder a MIGA
		        
		        try {
		        	Class.forName("org.postgresql.Driver"); //Postgresql
		            conexion = DriverManager.getConnection(cadenaConexion);
		            sentencia = conexion.createStatement();
		            sentencia2 = conexion.createStatement();
		            sentencia_insert  = conexion.createStatement();
		            sentencia_insert2 = conexion.createStatement();
		            sentencia_insert3 = conexion.createStatement();
		            sentencia_insert4 = conexion.createStatement();
		            sentencia_insert5 = conexion.createStatement();
		            sentencia_insert6 = conexion.createStatement();
		            sentencia_delete  = conexion.createStatement();
		            sentencia_delete2 = conexion.createStatement();
		            sentencia_delete3 = conexion.createStatement();
		            queryDelete = "delete from encelado.im_dt_ppal where id_proceso = '" 
		            			+ delegateExecution.getProcessInstanceId() + "'";
		            sentencia.executeUpdate(queryDelete);

		            	querySelect = "select distinct (ds_referencia) "
		            			+ " from encelado.im_datos_proceso "
		            			+ " where id_proceso = "  
		            			+ delegateExecution.getProcessInstanceId() 
		            			+  " and co_referencia in ('abono') ";
		            
		            resultado = sentencia.executeQuery(querySelect);
		            
		            if (resultado.next())
		            {
		            	hayDatos = true;
		            }
		            else
		            {
		            	hayDatos = false;
		            }

		        }catch (Exception e) {
		        	//System.out.println("Salida: "  );

		        	e.printStackTrace();
                	delegateExecution.setVariable("resultado", "NOK");
                	delegateExecution.setVariable("error", e.getMessage());
		        }
		            
		        while (hayDatos) {
		            	try{

		            	telefono = resultado.getString(1);
		            	prefijo = telefono.substring(1,3);
		            	prefijo2 = telefono.substring(1,2);
		            	
		            	
		            	querySelect2 = "select * from encelado.im_prefijo_abono "
		            			+ "	where prefijo in ('"  + prefijo + "','" + prefijo2 + "') "
		            			+ " order by 1 desc" ;
		            	
		                resultado2 = sentencia2.executeQuery(querySelect2);
		                
		                if(resultado2.next())
		                {
		                	provincia = resultado2.getString(2);
		                
		               }
		                	
		                
		                	

		                //System.out.println("antes de na");
		        		st = new NAServicio ("DTF0022");
		        		//System.out.println("despues de na");
		        		st.setCampo("CABECERA.IDENTIFICACION-LLAMANTE.NOMBRE", "G1");
		        		st.setCampo("CABECERA.IDENTIFICACION-LLAMANTE.TIPO", "03");
		        		st.setCampo("CABECERA.IDENTIFICACION_SERVICIO.VERSION_SERVICIO", "04");
		        		
		        		st.setCampo("CABECERA.IDENTIFICACION-SERVICIO.NOMBRE-SERVICIO", "DTNS30CO");
		        		st.setCampo("CABECERA.CO-MIRROR-HOST", "G1MI");
		        		st.setCampo("CABECERA.CO_ROUTING", provincia);
		        		st.setCampo("SN30-AREA-DATOS.SN30-DATOS-ENTRADA.SN30-NU-TELEFONO", telefono);	
		        		st.ejecutar();            	
		        		//System.out.println("Ejecutamos el servicio");
			        		    
		        		for (indice = 1; indice <= 10; indice ++ )
		        		{
		        			//System.out.println("Dentro del Array");
		        			//SN30MODALIDAD = (NACampo) st.getCampo("SN30-DATOS-SALIDA.SN30-CO-MODALIDAD", indice);
		        			
		        			//SN30FXALTA    = st.getCampo("SN30-DATOS-SALIDA.SN30-FX-ALTA", indice);
		        			//SN30FXBAJA    = st.getCampo("SN30-DATOS-SALIDA.SN30-FX-BAJA", indice);
		        			//SN30NUISNSAL  = st.getCampo("SN30-DATOS-SALIDA.SN30-NU-ISN-S", indice);
		        			//SN30SITUACION = st.getCampo("SN30-DATOS-SALIDA.SN30-CO-SITUACION", indice);
		        			//SN30DESTINOS  = st.getCampo("SN30-DATOS-SALIDA.SN30-NU-DESTINOS", indice);
		        			
		        			//cadenaSalida = telefono + ": " + st.getCampo("SN30-DATOS-SALIDA.SN30-CO-MODALIDAD", indice).getString() 
		        			//		+ " / " + SN30FXALTA.getString() + " / " +SN30FXBAJA.getString() + " / " +SN30NUISNSAL.getString() + " / " +SN30SITUACION.getString() + " / " +SN30DESTINOS.getString();
		        			//System.out.println(indice);
		        			//System.out.println(st.getCampo("SN30-DATOS-SALIDA.SN30-FX-ALTA", indice).toString());
		        			//System.out.println(st.getCampo("SN30-DATOS-SALIDA.SN30-FX-BAJA", indice).toString());
		        			//System.out.println(st.getCampo("SN30-DATOS-SALIDA.SN30-NU-ISN-S", indice).toString());
		        			//System.out.println(st.getCampo("SN30-DATOS-SALIDA.SN30-CO-SITUACION", indice).toString());
		        			//System.out.println(st.getCampo("SN30-DATOS-SALIDA.SN30-NU-DESTINOS", indice).toString());
		        			
		        			if (st.getCampo("SN30-DATOS-SALIDA.SN30-CO-SITUACION", indice).getString().equals("V") )
		        			{
		        				//System.out.println("Dentro del Array2");
		        					queryInsert ="INSERT INTO encelado.im_dt_ppal values ('" + delegateExecution.getProcessInstanceId() +"','" + telefono + "','" + SN30MODALIDAD.getString();
		        					queryInsert += "','" + st.getCampo("SN30-DATOS-SALIDA.SN30-FX-ALTA", indice).getString() + "','"; 
		        					queryInsert += st.getCampo("SN30-DATOS-SALIDA.SN30-FX-BAJA", indice).getString() + "','" ;
		        					queryInsert += st.getCampo("SN30-DATOS-SALIDA.SN30-NU-ISN-S", indice).getString() + "','" ;
		        					queryInsert += st.getCampo("SN30-DATOS-SALIDA.SN30-CO-SITUACION", indice).getString() + "','"; 
		        					queryInsert += st.getCampo("SN30-DATOS-SALIDA.SN30-NU-DESTINOS", indice).getString()+ "')";
		        					//System.out.println(queryInsert);
		        					sentencia_insert3.executeUpdate(queryInsert);
		        				
		        			}
		        			
		            				            				        			
		        		}
		 		 
		        		st.unload();
		        		
		    	
		            	}catch (Exception e) {
		            		//		System.out.println("Salida: " + exc.getDescripcion() );

		            		e.printStackTrace();
		                	delegateExecution.setVariable("resultado", "NOK");
		                	delegateExecution.setVariable("error", e.getMessage());
		            		try{
		            		queryInsert ="INSERT INTO encelado.im_proceso_error values (" + delegateExecution.getProcessInstanceId() + ",'"+ telefono + "','" + delegateExecution.getVariable("actividad").toString() + "','"  + e.getMessage().substring(0, 99) + "', CURRENT_TIMESTAMP)";
		            		//queryInsert ="INSERT INTO cddn2.im_proceso_detalle values ('" + delegateExecution.getProcessInstanceId() + "','ERRO','" + e.getMessage().substring(0, 99) + "','" + telefono + "')";
		            		//System.out.println(queryInsert);
		            		sentencia_insert5.executeUpdate(queryInsert);
		            		}
		            		catch (Exception e1){
		            		e1.printStackTrace();
		                	delegateExecution.setVariable("resultado", "NOK");
		                	delegateExecution.setVariable("error", e1.getMessage());
		            	}
		            	}
		            	finally{
		            		try {

		                    if (resultado.next())
		                    {
		                    	hayDatos = true;
		                    }
		                    else
		                    {
		                    	hayDatos = false;
		                    }
		                	}catch (Exception ex) {
		                        ex.printStackTrace();
		                    	delegateExecution.setVariable("resultado", "NOK");
		                    	delegateExecution.setVariable("error", ex.getMessage());
		                	}
		            	}
		            	
		        }
		        if (resultado != null) {
		            try {
		                resultado.close();
		            } catch (Exception e) {
		                e.printStackTrace();
	                	delegateExecution.setVariable("resultado", "NOK");
	                	delegateExecution.setVariable("error", e.getMessage());
		            }
		        }
		        if (sentencia != null) {
		            try {
		                sentencia.close();
		            } catch (Exception e) {
		                e.printStackTrace();
	                	delegateExecution.setVariable("resultado", "NOK");
	                	delegateExecution.setVariable("error", e.getMessage());
		            }
		        }
		        if (sentencia_insert != null) {
		            try {
		            	sentencia_insert.close();
		            } catch (Exception e) {
		                e.printStackTrace();
	                	delegateExecution.setVariable("resultado", "NOK");
	                	delegateExecution.setVariable("error", e.getMessage());
		            }
		        }
		        if (sentencia_delete != null) {
		            try {
		            	sentencia_delete.close();
		            } catch (Exception e) {
		                e.printStackTrace();
	                	delegateExecution.setVariable("resultado", "NOK");
	                	delegateExecution.setVariable("error", e.getMessage());
		            }
		        }
		        if (conexion != null) {
		            try {
		                conexion.close();
		            } catch (Exception e) {
		                e.printStackTrace();
	                	delegateExecution.setVariable("resultado", "NOK");
	                	delegateExecution.setVariable("error", e.getMessage());
		            }
		        }

		        }
	}


