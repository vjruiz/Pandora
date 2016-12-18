package com.athenea.activiti.principal.servicetask.casosparque;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.athenea.activiti.principal.servicetask.TareasDeFlujo;
import com.utilidades.logger.LoggerResultado;

import java.sql.*;

public class RecuperaDatosBDCC implements TareasDeFlujo{

    private static Logger log = Logger.getLogger(RecuperaDatosMiga.class);
	private int in_proceso_invocante = 0;
	private DelegateExecution delegateExecution = null;
	
	public void execute (DelegateExecution delegateExecution, ApplicationContext contextSpring){
		
		String telefono = null;
		String queryQY = null;
		int contadorTratados = 0;
		boolean resultadoEjecucion = true;
		
	    this.in_proceso_invocante = 
	    		Integer.parseInt((String) delegateExecution.getVariable("id_proceso_invocante"));
	    this.delegateExecution = delegateExecution;
	    
		delegateExecution.setVariable("actividad", delegateExecution.getCurrentActivityName());
		delegateExecution.setVariable("resultado", "OK");
		delegateExecution.setVariable("error", "  ");
		//PSQL
		String cadenaConexion = "jdbc:postgresql://127.0.0.1/encelado?user=postgres&password=1234";
		String queryInsert = null;

		//String queryDelete = null;
		String querySelect = null;
		Connection conexion = null;
        Statement sentencia = null;
        ResultSet resultado = null;
        Statement sentencia_insert = null;
        Statement sentencia_insert2 = null;
        Statement sentencia_insert3 = null;
        Statement sentencia_insert4 = null;
        //ResultSet resultado_insert = null;
        Statement sentencia_delete = null;

        
		//ORACLE
		String cadenaConexion_ORACLE = "jdbc:oracle:thin:t603149/VJ7860ru@10.51.16.106:1521/pt2bi0";
		Connection conexion_ORACLE = null;
        Statement sentencia_ORACLE = null;
        Statement sentencia_ORACLE2 = null;
        Statement sentencia_ORACLE3 = null;
        //Statement sentencia_insert_ORACLE = null;
      
        ResultSet resultado_ORACLE = null;
        ResultSet resultado_ORACLE2 = null;
        ResultSet resultado_ORACLE3 = null;
        
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection(cadenaConexion);
            sentencia = conexion.createStatement();
            sentencia_insert = conexion.createStatement();
            sentencia_insert2 = conexion.createStatement();
            sentencia_insert3 = conexion.createStatement();
            sentencia_insert4 = conexion.createStatement();
            sentencia_delete = conexion.createStatement();
            System.out.println("antes select entrada");
                                         
            	querySelect = "select distinct (ds_referencia) "
            			+ " from encelado.im_datos_proceso "
            			+ "where id_proceso = "  
            			+ this.in_proceso_invocante
            			+  " and co_referencia in ('abono') ";
            
            resultado = sentencia.executeQuery(querySelect);

            while (resultado.next()) {
                
                telefono = resultado.getString(1);
                queryQY =         "select DISTINCT(CODIGO_CLIENTE) from QYORA0.V_QY_PS_CONTRATABLES_POR_NCIAL A ";
                       queryQY += "WHERE A.NUMERO_COMERCIAL_ASOCIADO IN (";
                       queryQY += "  SELECT B.NUMERO_COMERCIAL_ASOCIADO FROM QYORA0.V_QY_PS_CONTRATABLES_POR_NCIAL B ";
                       queryQY += "WHERE (B.NUMERO_COMERCIAL = '"+ telefono.trim() + "'                          ";
                       queryQY += "OR B.NUMERO_COMERCIAL_ASOCIADO = '"+ telefono.trim() + "') AND B.FECHA_FIN_VIGENCIA > CURRENT_DATE)AND A.FECHA_FIN_VIGENCIA > CURRENT_DATE ";
                       queryQY += "UNION ";
                       queryQY += "select DISTINCT(NUMERO_COMERCIAL) from QYORA0.V_QY_PS_CONTRATABLES_POR_NCIAL D ";
                       queryQY += "WHERE D.NUMERO_COMERCIAL IN (                                   ";
                       queryQY += "SELECT C.NUMERO_COMERCIAL_ASOCIADO FROM QYORA0.V_QY_PS_CONTRATABLES_POR_NCIAL C ";  
                       queryQY += "WHERE (C.NUMERO_COMERCIAL = '"+ telefono.trim() + "'                          ";
                       queryQY += "OR C.NUMERO_COMERCIAL_ASOCIADO = '"+ telefono.trim() + "')AND C.FECHA_FIN_VIGENCIA > CURRENT_DATE) AND D.FECHA_FIN_VIGENCIA > CURRENT_DATE" ;
                       System.out.println(queryQY);
                Class.forName("oracle.jdbc.OracleDriver");
                conexion_ORACLE = DriverManager.getConnection(cadenaConexion_ORACLE);
                sentencia_ORACLE = conexion_ORACLE.createStatement();
                sentencia_ORACLE2 = conexion_ORACLE.createStatement();
                sentencia_ORACLE3 = conexion_ORACLE.createStatement();
                resultado_ORACLE = sentencia_ORACLE.executeQuery(queryQY);
                while (resultado_ORACLE.next()) {
                	
                	queryQY = "SELECT * FROM QYORA0.V_QY_AGF_CODIFICACION_ORIGEN " ;                                                 
                    queryQY += "WHERE CODIGO_AGRUPACION_PRECEDENCIA = '" + resultado_ORACLE.getString(1).trim()+"' " ;
                    queryQY += "AND FECHA_FIN_VIGENCIA > CURRENT_DATE" ;
                    
                    resultado_ORACLE3 = sentencia_ORACLE3.executeQuery(queryQY);
                    
                    if(resultado_ORACLE3.next())
                    {
                    	queryInsert = "INSERT INTO encelado.im_bdcc_ppal values ('"+ this.in_proceso_invocante  ;
                        queryInsert +=	"','" +telefono.trim() ; 
                        queryInsert +=	"','" +resultado_ORACLE3.getString("CODIGO_AGRUPACION_PRECEDENCIA")	  ;
                        queryInsert +="','" +resultado_ORACLE3.getString("CODIGO_AGRUPACION")	 ;
                        //queryInsert +="','" +resultado_ORACLE3.getString("CODIGO_CENTRAL_TELEFONICA")	  ;
                        queryInsert +="','" +resultado_ORACLE3.getInt("CODIGO_CENTRAL_TELEFONICA")	  ;
                        queryInsert +="','" +resultado_ORACLE3.getString("CODIGO_CLIENTE")	  ;
                        queryInsert +="','" +resultado_ORACLE3.getString("SISTEMA_PROCEDENCIA")  ;
                        queryInsert +="','" +resultado_ORACLE3.getString("IND_TIPO_AGRUPACION") + "')";
                        
                        sentencia_insert4.executeUpdate(queryInsert);
                    }
                	
                	queryQY = "SELECT A.*,B.* " ;                                                 
                    queryQY += "FROM QYORA0.V_QY_PS_CONTRATADOS A LEFT JOIN QEORA0.V_QE_PS_CONTRAT_CLIE_TARIFARIO B " ;
                    queryQY += "ON A.CODIGO_PRODUCTO_GENERICO=B.PRODUCTO_SERVICIO " ;
                    queryQY += "AND  A.NUMERO_ORDEN_PRODUCTO_SERVICIO = B.CONTRATABLE " ;
                    queryQY += "AND A.CODIGO_REGIMEN_COMERCIAL =B.REGIMEN_COMRECIAL " ;
                    queryQY += "AND A.CODIGO_CLIENTE_TARIFARIO =B.CLIENTE_TARIFARIO " ;
                    queryQY += "AND (B.ULTIMA_MODIFICACION)> CURRENT_TIMESTAMP         " ;
                    queryQY += "WHERE (A.CODIGO_COMERCIAL_SERVICIO='" + resultado_ORACLE.getString(1).trim()+ "') " ;              
                    queryQY += "And (A.FECHA_FIN_VIGENCIA)> CURRENT_DATE " ;                                      
                    //queryQY += "ORDER BY V_QY_PS_CONTRATADOS.IND_PRODUCTO_PPAL_LINEA;" ;
                    
                    resultado_ORACLE2 = sentencia_ORACLE2.executeQuery(queryQY);
                    
                    while (resultado_ORACLE2.next()) {
                    	queryInsert = "INSERT INTO encelado.im_bdcc_equipos values ('"+ this.in_proceso_invocante  ;
 

                        queryInsert +=	"','" +resultado_ORACLE2.getString(2)	 ; 
                        queryInsert +=	"','" +resultado_ORACLE2.getString(3)	  ;
                        queryInsert +="','" +resultado_ORACLE2.getString(4)	 ;
                        queryInsert +="','" +resultado_ORACLE2.getString(15)	  ;
                        queryInsert +="','" +resultado_ORACLE2.getString(40)	  ;
                        queryInsert +="','" +resultado_ORACLE2.getString(5)  ;
                        queryInsert +="','" +resultado_ORACLE2.getString(6)  ;
                        queryInsert +="','" +resultado_ORACLE2.getString(26)  ;
                        queryInsert +="','" +resultado_ORACLE2.getString(27)  ;
                        if (resultado_ORACLE2.getString(53) == null)
                        {
                            queryInsert +=	"','" +resultado_ORACLE2.getString(3)	  ;
                        }
                        else {

                        	queryInsert +="','" +resultado_ORACLE2.getString(53)  ;
                        }
                        queryInsert +="','" +    telefono.trim();
                        queryInsert +=	"','" +resultado_ORACLE2.getString(1)	 ;  
                        queryInsert +="','" +resultado_ORACLE2.getString(43)	  ;
                        queryInsert +=	"','" +resultado_ORACLE2.getString(20);  
                        queryInsert +=	"','" +resultado_ORACLE2.getString(17) + "');"	 ;                         
                        sentencia_insert3.executeUpdate(queryInsert);
                    }
                }
                
                contadorTratados ++;
                               
            }
            
			LoggerResultado.actualizarResultadoLogger(
					(String) this.delegateExecution.getVariable("id_tarea_asoc"), 
					this.in_proceso_invocante, 
					"OK", 
					"Resultado Correcto", 
					contadorTratados);
            
            
 
        } catch (Exception e) {
			log.fatal("Proceso MIGA",e);
			try {
				LoggerResultado.actualizarResultadoLogger(
						(String) this.delegateExecution.getVariable("id_tarea_asoc"), 
						this.in_proceso_invocante, 
						"NOK", 
						"control Exception proceso BDCC", 
						contadorTratados);
			} catch (SQLException e2) {
				log.warn("Error actualizando resultado tabla control flujos tarea gmc.- " , e2);
			}
        	resultadoEjecucion = false;
                        
        } finally {
            if (resultado != null) {
                try {
                    resultado.close();
                } catch (Exception e) {
                	log.warn("resultado.close BDCC.- " , e);
                }
            }
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (Exception e) {
                	log.warn("sentencia.close() BDCC.- " , e);
                }
            }
            if (sentencia_insert != null) {
                try {
                	sentencia_insert.close();
                } catch (Exception e) {
                	log.warn("sentencia_insert.close() BDCC.- " , e);
                }
            }
            if (sentencia_insert2 != null) {
                try {
                	sentencia_insert2.close();
                } catch (Exception e) {
                	log.warn("sentencia_insert2.close() BDCC.- " , e);
                }
            }
            if (sentencia_insert3 != null) {
                try {
                	sentencia_insert3.close();
                } catch (Exception e) {
                	log.warn("sentencia_insert3.close() BDCC.- " , e);
                }
            }
            if (sentencia_delete != null) {
                try {
                	sentencia_delete.close();
                } catch (Exception e) {
                	log.warn("sentencia_delete.close() BDCC.- " , e);
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (Exception e) {
                	log.warn("conexion.close() BDCC.- " , e);
                }
            }
            if (resultado_ORACLE != null) {
                try {
                    resultado_ORACLE.close();
                } catch (Exception e) {
                	log.warn("resultado_ORACLE.close() BDCC.- " , e);
                }
            }
            if (sentencia_ORACLE != null) {
                try {
                    sentencia_ORACLE.close();
                } catch (Exception e) {
                	log.warn("sentencia_ORACLE.close() BDCC.- " , e);
                }
            }
            if (conexion_ORACLE != null) {
                try {
                    conexion_ORACLE.close();
                } catch (Exception e) {
                	log.warn("conexion_ORACLE.close() BDCC.- " , e);
                }
            }
        }

	}
	
}
