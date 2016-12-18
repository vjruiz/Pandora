package com.spring.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

@Component("flujotareas")
public class FlujoTareasProgramadas implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3683687261914085091L;
	
	private String id_flujo_tarea;
	private String ds_flujo_tarea;   
	private Date fx_ini_vigencia;     
	private Date fx_fin_vigencia;     
	private String ds_periodicidad_ejecucion;	
	private ArrayList<TareaProgramada> listado_tareas_definidas;
	
	{
		id_flujo_tarea  = null;
		ds_flujo_tarea = null;
		fx_ini_vigencia  = null; 
		fx_fin_vigencia  = null;
		ds_periodicidad_ejecucion = null; 
		listado_tareas_definidas = null;
	}
	
   	public FlujoTareasProgramadas() {
	}

	public FlujoTareasProgramadas(String id_flujo_tarea, String ds_flujo_tarea, Date fx_ini_vigencia,
			Date fx_fin_vigencia, String ds_periodicidad_ejecucion,
			ArrayList<TareaProgramada> listado_tareas_definidas) {
		super();
		this.id_flujo_tarea = id_flujo_tarea;
		this.ds_flujo_tarea = ds_flujo_tarea;
		this.fx_ini_vigencia = fx_ini_vigencia;
		this.fx_fin_vigencia = fx_fin_vigencia;
		this.ds_periodicidad_ejecucion = ds_periodicidad_ejecucion;
		this.listado_tareas_definidas = listado_tareas_definidas;
	}

	public String getId_flujo_tarea() {
		return id_flujo_tarea;
	}

	public void setId_flujo_tarea(String id_flujo_tarea) {
		this.id_flujo_tarea = id_flujo_tarea;
	}

	public String getDs_flujo_tarea() {
		return ds_flujo_tarea;
	}

	public void setDs_flujo_tarea(String ds_flujo_tarea) {
		this.ds_flujo_tarea = ds_flujo_tarea;
	}

	public Date getFx_ini_vigencia() {
		return fx_ini_vigencia;
	}

	public void setFx_ini_vigencia(Date fx_ini_vigencia) {
		this.fx_ini_vigencia = fx_ini_vigencia;
	}

	public Date getFx_fin_vigencia() {
		return fx_fin_vigencia;
	}

	public void setFx_fin_vigencia(Date fx_fin_vigencia) {
		this.fx_fin_vigencia = fx_fin_vigencia;
	}

	public String getDs_periodicidad_ejecucion() {
		return ds_periodicidad_ejecucion;
	}

	public void setDs_periodicidad_ejecucion(String ds_periodicidad_ejecucion) {
		this.ds_periodicidad_ejecucion = ds_periodicidad_ejecucion;
	}

	public ArrayList<TareaProgramada> getListado_tareas_definidas() {
		return listado_tareas_definidas;
	}

	public void setListado_tareas_definidas(ArrayList<TareaProgramada> listado_tareas_definidas) {
		this.listado_tareas_definidas = listado_tareas_definidas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ds_flujo_tarea == null) ? 0 : ds_flujo_tarea.hashCode());
		result = prime * result + ((ds_periodicidad_ejecucion == null) ? 0 : ds_periodicidad_ejecucion.hashCode());
		result = prime * result + ((fx_fin_vigencia == null) ? 0 : fx_fin_vigencia.hashCode());
		result = prime * result + ((fx_ini_vigencia == null) ? 0 : fx_ini_vigencia.hashCode());
		result = prime * result + ((id_flujo_tarea == null) ? 0 : id_flujo_tarea.hashCode());
		result = prime * result + ((listado_tareas_definidas == null) ? 0 : listado_tareas_definidas.hashCode());
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
		FlujoTareasProgramadas other = (FlujoTareasProgramadas) obj;
		if (ds_flujo_tarea == null) {
			if (other.ds_flujo_tarea != null)
				return false;
		} else if (!ds_flujo_tarea.equals(other.ds_flujo_tarea))
			return false;
		if (ds_periodicidad_ejecucion == null) {
			if (other.ds_periodicidad_ejecucion != null)
				return false;
		} else if (!ds_periodicidad_ejecucion.equals(other.ds_periodicidad_ejecucion))
			return false;
		if (fx_fin_vigencia == null) {
			if (other.fx_fin_vigencia != null)
				return false;
		} else if (!fx_fin_vigencia.equals(other.fx_fin_vigencia))
			return false;
		if (fx_ini_vigencia == null) {
			if (other.fx_ini_vigencia != null)
				return false;
		} else if (!fx_ini_vigencia.equals(other.fx_ini_vigencia))
			return false;
		if (id_flujo_tarea == null) {
			if (other.id_flujo_tarea != null)
				return false;
		} else if (!id_flujo_tarea.equals(other.id_flujo_tarea))
			return false;
		if (listado_tareas_definidas == null) {
			if (other.listado_tareas_definidas != null)
				return false;
		} else if (!listado_tareas_definidas.equals(other.listado_tareas_definidas))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FlujoTareasProgramadas [id_flujo_tarea=" + id_flujo_tarea + ", ds_flujo_tarea=" + ds_flujo_tarea
				+ ", fx_ini_vigencia=" + fx_ini_vigencia + ", fx_fin_vigencia=" + fx_fin_vigencia
				+ ", ds_periodicidad_ejecucion=" + ds_periodicidad_ejecucion + ", listado_tareas_definidas="
				+ listado_tareas_definidas + "]";
	}
   	
   	
}
