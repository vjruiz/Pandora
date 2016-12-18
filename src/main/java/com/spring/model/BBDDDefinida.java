package com.spring.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.athenea.activiti.principal.servicetask.casosparque.RecuperaDatosMiga;
import com.utilidades.bbdd.ConexionBD;

@Component("bbdddefinida")
public class BBDDDefinida implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3683687261914085091L;
    private static Logger log = Logger.getLogger(BBDDDefinida.class);
	
	private String id_conexion_bd;
	private String ob_conexion_bd;
	private String id_usuario;
	private String id_contrasena;	
	private String ds_driver_bbdd;     
	private String ds_cadena_conexion_bbdd;
	private String resto_ds_cadena_conexion_bbdd;
	private Connection conexionDestino = null;
	private ResultSet resulsetSalida = null;
	private Statement sentenciaSelect = null;
    	 
	
   	public BBDDDefinida() {
	}
	
	public BBDDDefinida(String id_conexion_bd, String ob_conexion_bd, String id_usuario, String id_contrasena,
			String ds_driver_bbdd, String ds_cadena_conexion_bbdd, String resto_ds_cadena_conexion_bbdd,
			Connection conexionDestino, ResultSet resulsetSalida, Statement sentenciaSelect) {
		super();
		this.id_conexion_bd = id_conexion_bd;
		this.ob_conexion_bd = ob_conexion_bd;
		this.id_usuario = id_usuario;
		this.id_contrasena = id_contrasena;
		this.ds_driver_bbdd = ds_driver_bbdd;
		this.ds_cadena_conexion_bbdd = ds_cadena_conexion_bbdd;
		this.resto_ds_cadena_conexion_bbdd = resto_ds_cadena_conexion_bbdd;
		this.conexionDestino = conexionDestino;
		this.resulsetSalida = resulsetSalida;
		this.sentenciaSelect = sentenciaSelect;
	}

	public Connection getConexionDestino() {
		return conexionDestino;
	}

	public void setConexionDestino(Connection conexionDestino) {
		this.conexionDestino = conexionDestino;
	}

	public ResultSet getResulsetSalida() {
		return resulsetSalida;
	}

	public void setResulsetSalida(ResultSet resulsetSalida) {
		this.resulsetSalida = resulsetSalida;
	}

	public Statement getSentenciaSelect() {
		return sentenciaSelect;
	}

	public void setSentenciaSelect(Statement sentenciaSelect) {
		this.sentenciaSelect = sentenciaSelect;
	}

	public String getId_conexion_bd() {
		return id_conexion_bd;
	}

	public void setId_conexion_bd(String id_conexion_bd) {
		this.id_conexion_bd = id_conexion_bd;
	}

	public String getOb_conexion_bd() {
		return ob_conexion_bd;
	}

	public void setOb_conexion_bd(String ob_conexion_bd) {
		this.ob_conexion_bd = ob_conexion_bd;
	}

	public String getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(String id_usuario) {
		this.id_usuario = id_usuario;
	}

	public String getId_contrasena() {
		return id_contrasena;
	}

	public void setId_contrasena(String id_contrasena) {
		this.id_contrasena = id_contrasena;
	}

	public String getDs_driver_bbdd() {
		return ds_driver_bbdd;
	}

	public void setDs_driver_bbdd(String ds_driver_bbdd) {
		this.ds_driver_bbdd = ds_driver_bbdd;
	}

	public String getDs_cadena_conexion_bbdd() {
		return ds_cadena_conexion_bbdd;
	}

	public void setDs_cadena_conexion_bbdd(String ds_cadena_conexion_bbdd) {
		this.ds_cadena_conexion_bbdd = ds_cadena_conexion_bbdd;
	}	
	
	public String getResto_ds_cadena_conexion_bbdd() {
		return resto_ds_cadena_conexion_bbdd;
	}

	public void setResto_ds_cadena_conexion_bbdd(String resto_ds_cadena_conexion_bbdd) {
		this.resto_ds_cadena_conexion_bbdd = resto_ds_cadena_conexion_bbdd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ds_cadena_conexion_bbdd == null) ? 0 : ds_cadena_conexion_bbdd.hashCode());
		result = prime * result + ((ds_driver_bbdd == null) ? 0 : ds_driver_bbdd.hashCode());
		result = prime * result + ((id_conexion_bd == null) ? 0 : id_conexion_bd.hashCode());
		result = prime * result + ((id_contrasena == null) ? 0 : id_contrasena.hashCode());
		result = prime * result + ((id_usuario == null) ? 0 : id_usuario.hashCode());
		result = prime * result + ((ob_conexion_bd == null) ? 0 : ob_conexion_bd.hashCode());
		result = prime * result
				+ ((resto_ds_cadena_conexion_bbdd == null) ? 0 : resto_ds_cadena_conexion_bbdd.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BBDDDefinida other = (BBDDDefinida) obj;
		if (ds_cadena_conexion_bbdd == null) {
			if (other.ds_cadena_conexion_bbdd != null)
				return false;
		} else if (!ds_cadena_conexion_bbdd.equals(other.ds_cadena_conexion_bbdd))
			return false;
		if (ds_driver_bbdd == null) {
			if (other.ds_driver_bbdd != null)
				return false;
		} else if (!ds_driver_bbdd.equals(other.ds_driver_bbdd))
			return false;
		if (id_conexion_bd == null) {
			if (other.id_conexion_bd != null)
				return false;
		} else if (!id_conexion_bd.equals(other.id_conexion_bd))
			return false;
		if (id_contrasena == null) {
			if (other.id_contrasena != null)
				return false;
		} else if (!id_contrasena.equals(other.id_contrasena))
			return false;
		if (id_usuario == null) {
			if (other.id_usuario != null)
				return false;
		} else if (!id_usuario.equals(other.id_usuario))
			return false;
		if (ob_conexion_bd == null) {
			if (other.ob_conexion_bd != null)
				return false;
		} else if (!ob_conexion_bd.equals(other.ob_conexion_bd))
			return false;
		if (resto_ds_cadena_conexion_bbdd == null) {
			if (other.resto_ds_cadena_conexion_bbdd != null)
				return false;
		} else if (!resto_ds_cadena_conexion_bbdd.equals(other.resto_ds_cadena_conexion_bbdd))
			return false;
		return true;
	}	

	@Override
	public String toString() {
		return "BBDDDefinida [id_conexion_bd=" + id_conexion_bd + ", ob_conexion_bd=" + ob_conexion_bd + ", id_usuario="
				+ id_usuario + ", id_contrasena=" + id_contrasena + ", ds_driver_bbdd=" + ds_driver_bbdd
				+ ", ds_cadena_conexion_bbdd=" + ds_cadena_conexion_bbdd + ", resto_ds_cadena_conexion_bbdd="
				+ resto_ds_cadena_conexion_bbdd + "]";
	}

	public ResultSet realizarPruebaConexion(String comandoPrueba) throws SQLException {
		
		String cadenaConexionUsuPass;
		
		if (this.getDs_driver_bbdd().equals("oracle.jdbc.driver.OracleDriver"))
		{		
			cadenaConexionUsuPass = 
					this.getDs_cadena_conexion_bbdd() 					
					+ this.getId_usuario()
					+"/"
					+ this.getId_contrasena()	
					+"@"
					+ this.getResto_ds_cadena_conexion_bbdd();
	
		}else 
		{
			cadenaConexionUsuPass = 
				this.getDs_cadena_conexion_bbdd() + this.getResto_ds_cadena_conexion_bbdd()
				+"user="
				+ this.getId_usuario()
				+"&password="
				+ this.getId_contrasena();	
		}
			 conexionDestino = (Connection) new ConexionBD(
						this.getDs_driver_bbdd(),
						cadenaConexionUsuPass,
						null,
						null).devolverConexionConstructor();	
			 sentenciaSelect = conexionDestino.createStatement();
			 resulsetSalida = sentenciaSelect.executeQuery(comandoPrueba);
/*			 System.out.println("prueba conexion correcta.- " +
					 cadenaConexionUsuPass);*/
		
		return resulsetSalida;
			
	}
	
	public void cerrarConexiones()
	{
		try 
		{
			if (conexionDestino != null) {conexionDestino.close();}
			if (resulsetSalida != null) {resulsetSalida.close();}
			if (sentenciaSelect != null) {sentenciaSelect.close();}
		} catch (SQLException e)
		{
			log.warn("Error cerrando la conexcion en la copiaBD", e);
		}

	}

}
