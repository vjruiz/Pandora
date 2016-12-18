package com.athenea.activiti.principal.servicetask.procesogenerico.calculaprocesos;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.context.ApplicationContext;

public interface CalculaProcesos {
	
	//public Map<String, Map<String, Object>> execute(String nombreProceso,ApplicationContext contextSpring);
	public ArrayList< Map<?, ?>> execute(String nombreProceso,ApplicationContext contextSpring);
}
