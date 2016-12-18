package com.spring.model;

import java.io.Serializable;
import java.sql.Date;

import org.springframework.stereotype.Component;

@Component("copiaBD")
public class CopiaBD implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3683687261914085091L;
	
	private String id_copia_bd;
	private String id_conexion_bd_asoc;
	private String id_tabla_destino_copiabd;
	private String ds_codigo_sql_asoc_copiabd;
	private String ob_copiabd;	
	private String ds_periodicidad_ejecucion;     
	private Date fx_ini_vigencia;     
	private Date fx_fin_vigencia;     	
	private String id_arupador_tareas;
	private String tipo_dato; 	
	private String ind_extraccion_proceso;
	
   	public CopiaBD() {
	}

	public CopiaBD(String id_copia_bd, String id_conexion_bd_asoc, String id_tabla_destino_copiabd,
			String ds_codigo_sql_asoc_copiabd, String ob_copiabd, String ds_periodicidad_ejecucion,
			Date fx_ini_vigencia, Date fx_fin_vigencia, String id_arupador_tareas, String tipo_dato,
			String ind_extraccion_proceso) {
		super();
		this.id_copia_bd = id_copia_bd;
		this.id_conexion_bd_asoc = id_conexion_bd_asoc;
		this.id_tabla_destino_copiabd = id_tabla_destino_copiabd;
		this.ds_codigo_sql_asoc_copiabd = ds_codigo_sql_asoc_copiabd;
		this.ob_copiabd = ob_copiabd;
		this.ds_periodicidad_ejecucion = ds_periodicidad_ejecucion;
		this.fx_ini_vigencia = fx_ini_vigencia;
		this.fx_fin_vigencia = fx_fin_vigencia;
		this.id_arupador_tareas = id_arupador_tareas;
		this.tipo_dato = tipo_dato;
		this.ind_extraccion_proceso = ind_extraccion_proceso;
	}

	public String getId_copia_bd() {
		return id_copia_bd;
	}

	public void setId_copia_bd(String id_copia_bd) {
		this.id_copia_bd = id_copia_bd;
	}

	public String getId_conexion_bd_asoc() {
		return id_conexion_bd_asoc;
	}

	public void setId_conexion_bd_asoc(String id_conexion_bd_asoc) {
		this.id_conexion_bd_asoc = id_conexion_bd_asoc;
	}

	public String getId_tabla_destino_copiabd() {
		return id_tabla_destino_copiabd;
	}

	public void setId_tabla_destino_copiabd(String id_tabla_destino_copiabd) {
		this.id_tabla_destino_copiabd = id_tabla_destino_copiabd;
	}

	public String getDs_codigo_sql_asoc_copiabd() {
		return ds_codigo_sql_asoc_copiabd;
	}

	public void setDs_codigo_sql_asoc_copiabd(String ds_codigo_sql_asoc_copiabd) {
		this.ds_codigo_sql_asoc_copiabd = ds_codigo_sql_asoc_copiabd;
	}

	public String getOb_copiabd() {
		return ob_copiabd;
	}

	public void setOb_copiabd(String ob_copiabd) {
		this.ob_copiabd = ob_copiabd;
	}

	public String getDs_periodicidad_ejecucion() {
		return ds_periodicidad_ejecucion;
	}

	public void setDs_periodicidad_ejecucion(String ds_periodicidad_ejecucion) {
		this.ds_periodicidad_ejecucion = ds_periodicidad_ejecucion;
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

	public String getId_arupador_tareas() {
		return id_arupador_tareas;
	}

	public void setId_arupador_tareas(String id_arupador_tareas) {
		this.id_arupador_tareas = id_arupador_tareas;
	}

	public String getTipo_dato() {
		return tipo_dato;
	}

	public void setTipo_dato(String tipo_dato) {
		this.tipo_dato = tipo_dato;
	}

	public String getInd_extraccion_proceso() {
		return ind_extraccion_proceso;
	}

	public void setInd_extraccion_proceso(String ind_extraccion_proceso) {
		this.ind_extraccion_proceso = ind_extraccion_proceso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ds_codigo_sql_asoc_copiabd == null) ? 0 : ds_codigo_sql_asoc_copiabd.hashCode());
		result = prime * result + ((ds_periodicidad_ejecucion == null) ? 0 : ds_periodicidad_ejecucion.hashCode());
		result = prime * result + ((fx_fin_vigencia == null) ? 0 : fx_fin_vigencia.hashCode());
		result = prime * result + ((fx_ini_vigencia == null) ? 0 : fx_ini_vigencia.hashCode());
		result = prime * result + ((id_arupador_tareas == null) ? 0 : id_arupador_tareas.hashCode());
		result = prime * result + ((id_conexion_bd_asoc == null) ? 0 : id_conexion_bd_asoc.hashCode());
		result = prime * result + ((id_copia_bd == null) ? 0 : id_copia_bd.hashCode());
		result = prime * result + ((id_tabla_destino_copiabd == null) ? 0 : id_tabla_destino_copiabd.hashCode());
		result = prime * result + ((ind_extraccion_proceso == null) ? 0 : ind_extraccion_proceso.hashCode());
		result = prime * result + ((ob_copiabd == null) ? 0 : ob_copiabd.hashCode());
		result = prime * result + ((tipo_dato == null) ? 0 : tipo_dato.hashCode());
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
		CopiaBD other = (CopiaBD) obj;
		if (ds_codigo_sql_asoc_copiabd == null) {
			if (other.ds_codigo_sql_asoc_copiabd != null)
				return false;
		} else if (!ds_codigo_sql_asoc_copiabd.equals(other.ds_codigo_sql_asoc_copiabd))
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
		if (id_arupador_tareas == null) {
			if (other.id_arupador_tareas != null)
				return false;
		} else if (!id_arupador_tareas.equals(other.id_arupador_tareas))
			return false;
		if (id_conexion_bd_asoc == null) {
			if (other.id_conexion_bd_asoc != null)
				return false;
		} else if (!id_conexion_bd_asoc.equals(other.id_conexion_bd_asoc))
			return false;
		if (id_copia_bd == null) {
			if (other.id_copia_bd != null)
				return false;
		} else if (!id_copia_bd.equals(other.id_copia_bd))
			return false;
		if (id_tabla_destino_copiabd == null) {
			if (other.id_tabla_destino_copiabd != null)
				return false;
		} else if (!id_tabla_destino_copiabd.equals(other.id_tabla_destino_copiabd))
			return false;
		if (ind_extraccion_proceso == null) {
			if (other.ind_extraccion_proceso != null)
				return false;
		} else if (!ind_extraccion_proceso.equals(other.ind_extraccion_proceso))
			return false;
		if (ob_copiabd == null) {
			if (other.ob_copiabd != null)
				return false;
		} else if (!ob_copiabd.equals(other.ob_copiabd))
			return false;
		if (tipo_dato == null) {
			if (other.tipo_dato != null)
				return false;
		} else if (!tipo_dato.equals(other.tipo_dato))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CopiaBD [id_copia_bd=" + id_copia_bd + ", id_conexion_bd_asoc=" + id_conexion_bd_asoc
				+ ", id_tabla_destino_copiabd=" + id_tabla_destino_copiabd + ", ds_codigo_sql_asoc_copiabd="
				+ ds_codigo_sql_asoc_copiabd + ", ob_copiabd=" + ob_copiabd + ", ds_periodicidad_ejecucion="
				+ ds_periodicidad_ejecucion + ", fx_ini_vigencia=" + fx_ini_vigencia + ", fx_fin_vigencia="
				+ fx_fin_vigencia + ", id_arupador_tareas=" + id_arupador_tareas + ", tipo_dato=" + tipo_dato
				+ ", ind_extraccion_proceso=" + ind_extraccion_proceso + "]";
	}

}
