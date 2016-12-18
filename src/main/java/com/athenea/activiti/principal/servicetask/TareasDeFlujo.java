package com.athenea.activiti.principal.servicetask;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.context.ApplicationContext;

public interface TareasDeFlujo {
	
	public void execute(DelegateExecution delegateExecution,ApplicationContext contextSpring);

}
