package com.athenea.activiti.principal.servicetask.casosparque;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.context.ApplicationContext;

import java.sql.*;

import com.athenea.activiti.principal.servicetask.TareasDeFlujo;
import com.telefonica.na.*;
public class RecuperaDatosATLAS implements TareasDeFlujo{

	private static NAServicio st = null;
	
	public void execute(DelegateExecution delegateExecution, ApplicationContext contextSpring) {
	
				delegateExecution.setVariable("actividad", delegateExecution.getCurrentActivityName());
				delegateExecution.setVariable("resultado", "OK");
				delegateExecution.setVariable("error", "  ");
	
				String administrativo = "";
				String telefono = "";
				String salida = "";
				Integer indice = 0;
		
				Boolean hayDatos = false;
				
				//PSQL
				String cadenaConexion = "jdbc:postgresql://127.0.0.1/encelado?user=postgres&password=1234";
				String querySelect = null;
				String queryInsert = null;
				String queryDelete = null;
				Connection conexion = null;
		        Statement sentencia = null;
		        ResultSet resultado = null;
		        Statement sentencia_insert = null;
		        Statement sentencia_insert2 = null;
		        Statement sentencia_insert3 = null;

				
		        // Primero vamos a recuperar los administrativos recuperados de la consulta a la tabla im_proceso_detalle
		        
		        try {
		        	Class.forName("org.postgresql.Driver"); //Postgresql
		            conexion = DriverManager.getConnection(cadenaConexion);
		            sentencia = conexion.createStatement();
		            
		            sentencia_insert  = conexion.createStatement();
		            sentencia_insert2 = conexion.createStatement();
		            sentencia_insert3 = conexion.createStatement();
		            
		            queryDelete = "delete from encelado.im_atlas_ppal where id_proceso = '" 
		            			+ delegateExecution.getProcessInstanceId() + "'";
		            sentencia.executeUpdate(queryDelete);
		            
		            queryDelete = "delete from encelado.im_atlas_equipos where id_proceso = '" 
	            			+ delegateExecution.getProcessInstanceId() + "'";
		            sentencia.executeUpdate(queryDelete);

					querySelect =  "select distinct(ds_referencia) "
							+ "from encelado.im_datos_proceso "
		        			+ " where id_proceso = "  
		        			+ delegateExecution.getProcessInstanceId() 
		        			+  " and co_referencia in ('administrativo') ";
		            
		            //System.out.println(querySelect);
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
		        	//		System.out.println("Salida: " + exc.getDescripcion() );

		        	e.printStackTrace();
		        	delegateExecution.setVariable("resultado", "NOK");
		        	delegateExecution.setVariable("error", e.getMessage());
		        }
		            
		        while (hayDatos) {
		            	try{
		            		
		            		
		            		
		            	administrativo = resultado.getString(1);
		            	telefono = resultado.getString(2);
		            	//System.out.println(administrativo);
		            	st = new NAServicio ("A972CPCO");
		        		
		        		st.setCampo("M72CPI.M72CPI-DATOS-CONTROL.M72CPI-CRCT-NU-ADMINS", administrativo);
		        		st.setCampo("M72CPI.M72CPI-DATOS-CONTROL.M72CPI-G-UNOP", "CALDATN2");
		        		st.ejecutar();            	
		        		
		        		queryInsert  = "INSERT INTO encelado.im_atlas_ppal values ('" + delegateExecution.getProcessInstanceId() +"','" ;
		        		queryInsert += st.getCampo("M72CPO.M72CPO-DATOS-SALIDA.M72CPO-CIRCUITO.M72CPO-DCCR-CO-DSGINT").getString() + "','"; 
		        		queryInsert += st.getCampo("M72CPO.M72CPO-DATOS-SALIDA.M72CPO-CIRCUITO.M72CPO-CRCT-NU-ADMINS").getString() + "','" ;
		        		queryInsert += st.getCampo("M72CPO.M72CPO-DATOS-SALIDA.M72CPO-CIRCUITO.M72CPO-TCTO-CO-TIPCTO").getString() + "','" ;
		        		queryInsert += st.getCampo("M72CPO.M72CPO-DATOS-SALIDA.M72CPO-CIRCUITO.M72CPO-ESPE-CO-ESPECI").getString() + "','" ;
		        		queryInsert += st.getCampo("M72CPO.M72CPO-DATOS-SALIDA.M72CPO-CIRCUITO.M72CPO-TCES-NO-NOTCES").getString() + "','" ;
		        		queryInsert += st.getCampo("M72CPO.M72CPO-DATOS-SALIDA.M72CPO-CIRCUITO.M72CPO-VLCD-CA-VELCDD").getString() + "','" ;
		        		queryInsert += st.getCampo("M72CPO.M72CPO-DATOS-SALIDA.M72CPO-CIRCUITO.M72CPO-VLCD-CO-UNIDAD").getString() + "','" ;
		        		queryInsert += st.getCampo("M72CPO.M72CPO-DATOS-SALIDA.M72CPO-CIRCUITO.M72CPO-CRCT-FX-AEFECT").getString() + "','" ;
		        		queryInsert += st.getCampo("M72CPO.M72CPO-DATOS-SALIDA.M72CPO-CIRCUITO.M72CPO-ESCI-CO-ESTADO").getString() + "');" ;  

		        		
		        		sentencia_insert.executeUpdate(queryInsert);	
		        		
		        				        		
		        		
		        		for (indice = 1; indice <= 100; indice ++ )
		        		{
		        		 
		        			//System.out.println("antes del IF de equipos");
		        			if (st.getCampo("M72CPO.M72CPO-DATOS-SALIDA.M72CPO-EQUIPOS.M72CPO-TEAB-CO-TEQPAB", indice).getString().equals("     ") )
		        			{
		        				
		        				indice = 100;
		        			}
		        			else
		        			{
		        					queryInsert ="INSERT INTO encelado.im_atlas_equipos values ('" + delegateExecution.getProcessInstanceId() +"','" + administrativo + "','" + st.getCampo("M72CPO.M72CPO-DATOS-SALIDA.M72CPO-EQUIPOS.M72CPO-TEAB-CO-TEQPAB", indice).getString() + "','" + st.getCampo("M72CPO.M72CPO-DATOS-SALIDA.M72CPO-EQUIPOS.M72CPO-TEAB-NO-TEQPAB", indice).getString() + "','" + st.getCampo("M72CPO.M72CPO-DATOS-SALIDA.M72CPO-EQUIPOS.M72CPO-EQAB-NU-SERIE", indice).getString() + "')";
		        					sentencia_insert2.executeUpdate(queryInsert);
		
		        			}
		            				            				        			
		        		}
		 		 
		        		st.unload();
		        		
		    	
		            	}catch (Exception e) {
		            		//		System.out.println("Salida: " + exc.getDescripcion() );

		            		e.printStackTrace();
		                	delegateExecution.setVariable("resultado", "NOK");
		                	delegateExecution.setVariable("error", e.getMessage());
		            		try{
		            		queryInsert ="INSERT INTO encelado.im_proceso_error values (" + delegateExecution.getProcessInstanceId() + ",'"+ telefono + "','" + delegateExecution.getVariable("actividad").toString() + "','" + administrativo + " - " + e.getMessage().substring(0, 70) + "', CURRENT_TIMESTAMP)";
		            		//queryInsert ="INSERT INTO cddn2.im_proceso_detalle values ('" + delegateExecution.getProcessInstanceId() + "','ERRO','" + e.getMessage().substring(0, 70) + "','" + administrativo + "')";
		            		//System.out.println(queryInsert);
		            		sentencia_insert3.executeUpdate(queryInsert);
		            		}
		            		catch (Exception e1){
		            		e1.printStackTrace();
		                	delegateExecution.setVariable("resultado", "NOK");
		                	delegateExecution.setVariable("error", e1.getMessage());
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
		                        e.printStackTrace();
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
		        if (conexion != null) {
		            try {
		                conexion.close();
		            } catch (Exception e) {
		                e.printStackTrace();
		            	delegateExecution.setVariable("resultado", "NOK");
		            	delegateExecution.setVariable("error", e.getMessage());
		            }
		        }
	            salida = delegateExecution.getVariable("resultado").toString(); 
	            salida += delegateExecution.getVariable("actividad").toString(); 
	            salida += delegateExecution.getVariable("error").toString();
	            System.out.println(salida);
		        }
	}
}
