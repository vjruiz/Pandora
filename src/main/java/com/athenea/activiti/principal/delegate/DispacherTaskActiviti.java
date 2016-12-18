package com.athenea.activiti.principal.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.athenea.activiti.principal.servicetask.TareasDeFlujo;


public class DispacherTaskActiviti implements JavaDelegate{
	
	private ApplicationContext contextSpring;
	private TareasDeFlujo servicioDesplieglaProcesos;	

	public void execute(DelegateExecution delegateExecution) throws Exception{

		System.out.println("Inicio DispacherTaskActiviti");

		contextSpring = new FileSystemXmlApplicationContext ("C:/encelado/config/ConfigSpringAtheneaProcesosActiviti-context.xml");
		//
		
		try 
		{				
			String nameProcess = (String) delegateExecution.getProcessDefinitionId().toString();
			nameProcess = nameProcess.substring(0,nameProcess.indexOf(":"));
			String nameServiceTask = (String) delegateExecution.getCurrentActivityId();
			String nameBeans = nameProcess + "." + nameServiceTask;
			System.out.println("ServiceTask recogida Spring.-" + nameBeans);					
			
			//recogemos el nombre de la actividad que nos invoca y le pedimos a Spring que nos devuelva el servicio 
			//encargado de ejecutarla, si falla ver catch NoSuchBeanDefinitionException.	
			servicioDesplieglaProcesos = 
					(TareasDeFlujo) contextSpring.getBean(nameBeans);					
			
			//Desplegamos todos los procesos.
			servicioDesplieglaProcesos.execute(delegateExecution,contextSpring);	
			
			System.out.println("Fin proceso .- Caso " + delegateExecution.getVariable("caso"));
			System.out.println("Resultado .- " + delegateExecution.getVariable("resultado"));
			System.out.println("itsm .- " + delegateExecution.getVariable("itsm"));
						
			
		} catch (NoSuchBeanDefinitionException e) 		
		{ // si al acceder a por un Bean a Spring este no se encuentra se entiende que se desea ejecutar la tarea del proceso en
		  // vacio, recuerda este es el dispacher de servicios invocados por un proceso.
			System.out.println("********************************************" );			
			System.out.println("INFOR .- Bean no definido en archivo Spring." + 
									(String) delegateExecution.getCurrentActivityId());
			System.out.println("********************************************" );					
		}catch (NullPointerException e) {
			// TODO: handle exception
    		System.out.println("control NullPointerException");
		}
		       
		delegateExecution.setVariable("resultado", "OK");
		delegateExecution.setVariable("error", "  ");	
			    
		System.out.println("Fin DispacherTaskActiviti");
	}

}
