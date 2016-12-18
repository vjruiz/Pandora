package com.athenea.activiti.principal.servicetask.casosparque;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.context.ApplicationContext;

import com.athenea.activiti.principal.servicetask.TareasDeFlujo;
import com.utilidades.bbdd.ConexionBD;


public class GrabarOK implements TareasDeFlujo{

	private String nombreTarea;
	private int contadorTratados;
	private int id_proceso_invocante;
	
	
	public String getNombreTarea() {
		return nombreTarea;
	}


	public void setNombreTarea(String nombreTarea) {
		this.nombreTarea = nombreTarea;
	}

	public int getContadorTratados() {
		return contadorTratados;
	}

	public void setContadorTratados(int contadorTratados) {
		this.contadorTratados = contadorTratados;
	}

	public int getId_proceso_invocante() {
		return id_proceso_invocante;
	}


	public void setId_proceso_invocante(int id_proceso_invocante) {
		this.id_proceso_invocante = id_proceso_invocante;
	}


	public void execute(DelegateExecution delegateExecution, ApplicationContext contextSpring) {
		
	    
	    System.out.println("*************************");
	    System.out.println("****GrabarOK*************");
	    System.out.println("*************************");
	    
		
		this.setNombreTarea( (String) delegateExecution.getVariable("servicioNA"));
		this.setContadorTratados(
				Integer.parseInt((String) delegateExecution.getVariable("contadorTratados")));
	    this.setId_proceso_invocante(
	    		Integer.parseInt((String) delegateExecution.getVariable("id_proceso_invocante")));
	    
		Connection conexionEncelado;
		Statement sentencia_update; 
		
		conexionEncelado = ConexionBD.getInstance().devolverConexion("encelado");	
		
		String queryApoyo = 
				"UPDATE encelado.im_datos_ejecucion"
				+ " SET resultado='OK',"
				+ " total_tratado ='" + this.getContadorTratados() +"'" 				
				+ " where entidad='" + this.getNombreTarea() + "'"
				+ " and id_proceso='" + this.getId_proceso_invocante() + "'"
				;					

		try {
			sentencia_update = conexionEncelado.createStatement();
			sentencia_update.executeUpdate(queryApoyo);
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			System.out.println("*****************************");
			System.out.println("**error DB2 grabando el error tarea" + this.getNombreTarea() );
			System.out.println("*****************************");
		}
			
	}
}
