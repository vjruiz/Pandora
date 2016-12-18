/*
 */
package com.athenea.activiti.principal.servicetask.procesogenerico;

import java.util.ArrayList;
import java.util.Map;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.context.ApplicationContext;

import com.athenea.activiti.principal.servicetask.TareasDeFlujo;
import com.athenea.activiti.principal.servicetask.procesogenerico.calculaprocesos.CalculaProcesos;

/**
 *
 * @author vjruiz
 */
public class DesplegarProcesosService implements TareasDeFlujo{


	public DesplegarProcesosService() {
	}

	{ // bloque de inicializacion de variables de instancia

	}


	@SuppressWarnings("unchecked")
	public void execute(DelegateExecution delegateExecution,ApplicationContext contextSpring)  {

		System.out.println("Inicio DesplegarProcesosService");
				
		RuntimeService runtimeService = delegateExecution.getEngineServices().getRuntimeService();

		try {
			String claveProcesoArranca = null;
			//Map<String, Map<String, Object>> hashMapProcesosLanzar;
			ArrayList<?> arrayListProcesosLanzar;
			CalculaProcesos calculaProcesos;
					
			try 
			{
				// se comprueba si el proceso ha sido arrancado con la clave DesplegarProcesosService informada en memoria
				// lo que significa que el procesoGenerico ha sido lanzado para la ejecucion de un proceso concreto
				// es decir no hay que ir al servicio a recoger los procesos que se deben arrancar:			
				claveProcesoArranca = (String) delegateExecution.getVariable("DesplegarProcesosService").toString();
				
			}
			  catch (NullPointerException e)
			{
				// no es muy elegante pero cuando delegateExecution no encuentra variable pues toma un error y 
				// en este caso pues nos interesa continuar a ver si encontramos algun sevicio que nos de procesos para ejecutar.
				System.out.println("Proceso sin Variable DesplegarProcesosService informada, se llama a rutina "
						+ "calcula procesos");			
			}
			
			if (claveProcesoArranca == null)
				// si la claveProcesoArranca no esta informada se invoca al servicio encargado de devolvernos los flujos a invocar.
			{			
				// se calcula el nombre del proceso en el que estamos para recuperar el beans correspondiente de Spring
				// encargado de calcular los procesos que se deben arrancar.
					String nameProcess = (String) delegateExecution.getProcessDefinitionId().toString();
					nameProcess = nameProcess.substring(0,nameProcess.indexOf(":"));
					String nameBeans = nameProcess + ".CalculaProcesos" ;
					
				// se solicita a Spring el servicio encargado de calcular los procesos que se deben arrancar. 
					calculaProcesos = (CalculaProcesos) contextSpring.getBean(nameBeans);													
					
					// controlamos con el try el NullPointerException que se genera si la rutina que 
					// calcula los procesos no devuelve ninguno con el que trabajar
					try 
					{
						arrayListProcesosLanzar = calculaProcesos.execute(nameProcess, contextSpring);									
						
						if (arrayListProcesosLanzar != null)
						{																			
							int i= 0;
							for(i=0; i< arrayListProcesosLanzar.size(); i++){
	
								System.out.println("lanzo proceso.- " 
											+ arrayListProcesosLanzar.get(i).toString());
								runtimeService.startProcessInstanceByKey(
										"procesoEjecutorFlujoTareas", 
										(Map<String, Object>) arrayListProcesosLanzar.get(i));																
					        }
							
							if (i == 0)
							{
								System.out.println("*******************************");								
								System.out.println("No existen procesos que lanzar.");
								System.out.println("*******************************");
							}
							
							
						} else	System.out.println("DesplegarProcesoService .- no encontrados procesos que lanzar.");
						
					}
					  catch (NullPointerException e)
					{
						System.out.println("El servicio encargado de consultar/devolver los casos de parque "
								+ "no ha encontrado procesos para ejecutar.");			
					}					
			} 			
			else 
			{
				runtimeService.startProcessInstanceByKey(claveProcesoArranca);				
			}
							
		}
		  catch (ActivitiObjectNotFoundException e)
		{
			System.out.println("No existe el proceso de entrada");			
		}
		
		System.out.println("Fin DesplegarProcesosService.");
		                                          
	}
	
}
