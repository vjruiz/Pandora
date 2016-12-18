package com.spring.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component("tareaProgramada")
public class TareaProgramada implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3683687261914085091L;
	
	private String id_flujo_tarea_asoc;
	private String id_tarea_asoc;    
	private String tipo_tarea;	
	
	{
		id_flujo_tarea_asoc  = null;
		id_tarea_asoc = null;
		tipo_tarea  = null; 	
	}
	
   	public TareaProgramada() {
	}

	public TareaProgramada(String id_flujo_tarea_asoc, String id_tarea_asoc, String tipo_tarea) {
		super();
		this.id_flujo_tarea_asoc = id_flujo_tarea_asoc;
		this.id_tarea_asoc = id_tarea_asoc;
		this.tipo_tarea = tipo_tarea;
	}

	public String getId_flujo_tarea_asoc() {
		return id_flujo_tarea_asoc;
	}

	public void setId_flujo_tarea_asoc(String id_flujo_tarea_asoc) {
		this.id_flujo_tarea_asoc = id_flujo_tarea_asoc;
	}

	public String getId_tarea_asoc() {
		return id_tarea_asoc;
	}

	public void setId_tarea_asoc(String id_tarea_asoc) {
		this.id_tarea_asoc = id_tarea_asoc;
	}

	public String getTipo_tarea() {
		return tipo_tarea;
	}

	public void setTipo_tarea(String tipo_tarea) {
		this.tipo_tarea = tipo_tarea;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id_flujo_tarea_asoc == null) ? 0 : id_flujo_tarea_asoc.hashCode());
		result = prime * result + ((id_tarea_asoc == null) ? 0 : id_tarea_asoc.hashCode());
		result = prime * result + ((tipo_tarea == null) ? 0 : tipo_tarea.hashCode());
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
		TareaProgramada other = (TareaProgramada) obj;
		if (id_flujo_tarea_asoc == null) {
			if (other.id_flujo_tarea_asoc != null)
				return false;
		} else if (!id_flujo_tarea_asoc.equals(other.id_flujo_tarea_asoc))
			return false;
		if (id_tarea_asoc == null) {
			if (other.id_tarea_asoc != null)
				return false;
		} else if (!id_tarea_asoc.equals(other.id_tarea_asoc))
			return false;
		if (tipo_tarea == null) {
			if (other.tipo_tarea != null)
				return false;
		} else if (!tipo_tarea.equals(other.tipo_tarea))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TareaProgramada [id_flujo_tarea_asoc=" + id_flujo_tarea_asoc + ", id_tarea_asoc=" + id_tarea_asoc
				+ ", tipo_tarea=" + tipo_tarea + "]";
	}   	   

}
