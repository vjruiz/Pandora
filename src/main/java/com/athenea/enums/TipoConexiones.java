package com.athenea.enums;

public enum TipoConexiones {
	hadesindra("com.mysql.jdbc.Driver","jdbc:mysql://10.51.5.218:3306/hades?","user=hades_cons&password=consulta"),
	HADESINDRA("com.mysql.jdbc.Driver","jdbc:mysql://10.51.5.218:3306/hades?","user=hades_cons&password=consulta"),
	hadestelefonica("com.mysql.jdbc.Driver","jdbc:mysql://10.14.7.66:3306/hades?","user=hades_cons&password=consulta"),
	HADESTELEFONICA("com.mysql.jdbc.Driver","jdbc:mysql://10.14.7.66:3306/hades?","user=hades_cons&password=consulta"),
	GIPEINDRA("com.mysql.jdbc.Driver","jdbc:mysql://10.51.5.218:3307/gipe?","user=gipe_cons&password=consulta"),
	gipeindra("com.mysql.jdbc.Driver","jdbc:mysql://10.51.5.218:3307/gipe?","user=gipe_cons&password=consulta"),
	GIPETELEFONICA("com.mysql.jdbc.Driver","jdbc:mysql://10.51.5.218:3307/gipe?","user=gipe_cons&password=consulta"),
	gipetelefonica("com.mysql.jdbc.Driver","jdbc:mysql://10.51.5.218:3307/gipe?","user=gipe_cons&password=consulta"),
	ATHENEA("org.postgresql.Driver","jdbc:postgresql://127.0.0.1/imagine?","user=postgres&password=1234"),
	athenea("org.postgresql.Driver","jdbc:postgresql://127.0.0.1/imagine?","user=postgres&password=1234");

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
