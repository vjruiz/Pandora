package com.utilidades.bbdd;

public enum TipoConexiones {
	encelado("org.postgresql.Driver","jdbc:postgresql://127.0.0.1/encelado?","user=postgres&password=1234");

	private final String driverBBDD; //Driver de la BBDD
	private final String direccionBBDD; //cadena conexion de la BBDD
	private final String userPassBBDD; //Usuario y pass de acceso a la BBDD
	
	
	TipoConexiones (String driverBBDD, String direccionBBDD, String userPassBBDD) 
	{ 

        this.driverBBDD = driverBBDD;

        this.direccionBBDD = direccionBBDD;
        
        this.userPassBBDD = userPassBBDD;       

    }


	public String getDriverBBDD() {
		return driverBBDD;
	}


	public String getDireccionBBDD() {
		return direccionBBDD;
	}


	public String getUserPassBBDD() {
		return userPassBBDD;
	} 
	
}

