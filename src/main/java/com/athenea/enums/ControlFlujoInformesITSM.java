package com.athenea.enums;

public enum ControlFlujoInformesITSM {
	CargaTablasHades("com.athenea.principal.delegate.servicios.tareas.CargarDatosHades"),
	CargarDiarioITSMs("com.athenea.principal.delegate.servicios.tareas.CargarDiarioInformesITSM"),
	ControlPeticionReportes("com.athenea.principal.delegate.servicios.tareas.ControlPeticionReportes"),
	EnviarCorreo("com.athenea.principal.delegate.servicios.tareas.EnviarCorreo")
	;

	private final String nombreClase; //Nombre de la clase asociada a la tarea por la que se pregunta.	
	
	
	ControlFlujoInformesITSM (String nombreClase) 
	{ 

        this.nombreClase = nombreClase;

    }


	public String getNombreClase() {
		return nombreClase;
	}
	
	

}
