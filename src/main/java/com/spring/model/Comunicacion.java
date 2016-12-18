package com.spring.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component("comunicacion")
public class Comunicacion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3683687261914085091L;
	
	private String id_env_correo;
	private String ds_titulo_correo;   
	private String ds_cuerpo_correo;     
	private String ds_destinatarios_cc;     
	private String ds_destinatarios_para ;
	private String id_server_asoc;				
	
	{
		id_env_correo  = null;
		ds_titulo_correo = null;
		ds_cuerpo_correo  = null; 
		ds_destinatarios_cc  = null;
		ds_destinatarios_para = null;     
		id_server_asoc = null;		
	}
	
   	public Comunicacion() {
	}

	public Comunicacion(String id_env_correo, String ds_titulo_correo, String ds_cuerpo_correo,
			String ds_destinatarios_cc, String ds_destinatarios_para, String id_server_asoc) {
		super();
		this.id_env_correo = id_env_correo;
		this.ds_titulo_correo = ds_titulo_correo;
		this.ds_cuerpo_correo = ds_cuerpo_correo;
		this.ds_destinatarios_cc = ds_destinatarios_cc;
		this.ds_destinatarios_para = ds_destinatarios_para;
		this.id_server_asoc = id_server_asoc;
	}

	public String getId_env_correo() {
		return id_env_correo;
	}

	public void setId_env_correo(String id_env_correo) {
		this.id_env_correo = id_env_correo;
	}

	public String getDs_titulo_correo() {
		return ds_titulo_correo;
	}

	public void setDs_titulo_correo(String ds_titulo_correo) {
		this.ds_titulo_correo = ds_titulo_correo;
	}

	public String getDs_cuerpo_correo() {
		return ds_cuerpo_correo;
	}

	public void setDs_cuerpo_correo(String ds_cuerpo_correo) {
		this.ds_cuerpo_correo = ds_cuerpo_correo;
	}

	public String getDs_destinatarios_cc() {
		return ds_destinatarios_cc;
	}

	public void setDs_destinatarios_cc(String ds_destinatarios_cc) {
		this.ds_destinatarios_cc = ds_destinatarios_cc;
	}

	public String getDs_destinatarios_para() {
		return ds_destinatarios_para;
	}

	public void setDs_destinatarios_para(String ds_destinatarios_para) {
		this.ds_destinatarios_para = ds_destinatarios_para;
	}

	public String getId_server_asoc() {
		return id_server_asoc;
	}

	public void setId_server_asoc(String id_server_asoc) {
		this.id_server_asoc = id_server_asoc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ds_cuerpo_correo == null) ? 0 : ds_cuerpo_correo.hashCode());
		result = prime * result + ((ds_destinatarios_cc == null) ? 0 : ds_destinatarios_cc.hashCode());
		result = prime * result + ((ds_destinatarios_para == null) ? 0 : ds_destinatarios_para.hashCode());
		result = prime * result + ((ds_titulo_correo == null) ? 0 : ds_titulo_correo.hashCode());
		result = prime * result + ((id_env_correo == null) ? 0 : id_env_correo.hashCode());
		result = prime * result + ((id_server_asoc == null) ? 0 : id_server_asoc.hashCode());
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
		Comunicacion other = (Comunicacion) obj;
		if (ds_cuerpo_correo == null) {
			if (other.ds_cuerpo_correo != null)
				return false;
		} else if (!ds_cuerpo_correo.equals(other.ds_cuerpo_correo))
			return false;
		if (ds_destinatarios_cc == null) {
			if (other.ds_destinatarios_cc != null)
				return false;
		} else if (!ds_destinatarios_cc.equals(other.ds_destinatarios_cc))
			return false;
		if (ds_destinatarios_para == null) {
			if (other.ds_destinatarios_para != null)
				return false;
		} else if (!ds_destinatarios_para.equals(other.ds_destinatarios_para))
			return false;
		if (ds_titulo_correo == null) {
			if (other.ds_titulo_correo != null)
				return false;
		} else if (!ds_titulo_correo.equals(other.ds_titulo_correo))
			return false;
		if (id_env_correo == null) {
			if (other.id_env_correo != null)
				return false;
		} else if (!id_env_correo.equals(other.id_env_correo))
			return false;
		if (id_server_asoc == null) {
			if (other.id_server_asoc != null)
				return false;
		} else if (!id_server_asoc.equals(other.id_server_asoc))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Comunicacion [id_env_correo=" + id_env_correo + ", ds_titulo_correo=" + ds_titulo_correo
				+ ", ds_cuerpo_correo=" + ds_cuerpo_correo + ", ds_destinatarios_cc=" + ds_destinatarios_cc
				+ ", ds_destinatarios_para=" + ds_destinatarios_para + ", id_server_asoc=" + id_server_asoc + "]";
	}   	   
   	
}
