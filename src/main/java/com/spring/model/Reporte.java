package com.spring.model;

import java.io.Serializable;
import java.sql.Date;

import org.springframework.stereotype.Component;

@Component("reporte")
public class Reporte implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3683687261914085091L;
	
	private String id_reporte;
	private String ob_reporte;   
	private Date fx_ini_vigencia;     
	private Date fx_fin_vigencia; 
	private String ds_periodicidad_ejecucion;     
	private String id_fichero_salida;     
	private String formato_fichero_salida ;
	private String id_path_fichero_salida;		
	private String id_data_source_reporte ;
	private String ds_in_data_source;		
	private String id_conexion_bd_asoc ;
	private String id_path_file_in_report;
	private String id_archivo_jasper ;
	private Comunicacion comunicacionAsociada;	
	private String ind_extraccion_proceso;
	
	{
		id_reporte  = null;
   		ob_reporte = null;
   		fx_ini_vigencia  = null; 
   		fx_fin_vigencia  = null;
   		ds_periodicidad_ejecucion = null;     
   		id_fichero_salida = null;
   		formato_fichero_salida  = null;
   		id_path_fichero_salida = null;
   		id_data_source_reporte  = null;
   		ds_in_data_source = null;
   		id_conexion_bd_asoc  = null;
   		id_path_file_in_report = null;
   		id_archivo_jasper  = null;
   		comunicacionAsociada = null;
   		ind_extraccion_proceso = null;
		
	}
	
   	public Reporte() {
	}

	public Reporte(String id_reporte, String ob_reporte, Date fx_ini_vigencia, Date fx_fin_vigencia,
			String ds_periodicidad_ejecucion, String id_fichero_salida, String formato_fichero_salida,
			String id_path_fichero_salida, String id_data_source_reporte, String ds_in_data_source,
			String id_conexion_bd_asoc, String id_path_file_in_report, String id_archivo_jasper,
			Comunicacion comunicacionAsociada, String ind_extraccion_proceso) {
		super();
		this.id_reporte = id_reporte;
		this.ob_reporte = ob_reporte;
		this.fx_ini_vigencia = fx_ini_vigencia;
		this.fx_fin_vigencia = fx_fin_vigencia;
		this.ds_periodicidad_ejecucion = ds_periodicidad_ejecucion;
		this.id_fichero_salida = id_fichero_salida;
		this.formato_fichero_salida = formato_fichero_salida;
		this.id_path_fichero_salida = id_path_fichero_salida;
		this.id_data_source_reporte = id_data_source_reporte;
		this.ds_in_data_source = ds_in_data_source;
		this.id_conexion_bd_asoc = id_conexion_bd_asoc;
		this.id_path_file_in_report = id_path_file_in_report;
		this.id_archivo_jasper = id_archivo_jasper;
		this.comunicacionAsociada = comunicacionAsociada;
		this.ind_extraccion_proceso = ind_extraccion_proceso;
	}

	public String getId_reporte() {
		return id_reporte;
	}

	public void setId_reporte(String id_reporte) {
		this.id_reporte = id_reporte;
	}

	public String getOb_reporte() {
		return ob_reporte;
	}

	public void setOb_reporte(String ob_reporte) {
		this.ob_reporte = ob_reporte;
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

	public String getId_fichero_salida() {
		return id_fichero_salida;
	}

	public void setId_fichero_salida(String id_fichero_salida) {
		this.id_fichero_salida = id_fichero_salida;
	}

	public String getFormato_fichero_salida() {
		return formato_fichero_salida;
	}

	public void setFormato_fichero_salida(String formato_fichero_salida) {
		this.formato_fichero_salida = formato_fichero_salida;
	}

	public String getId_path_fichero_salida() {
		return id_path_fichero_salida;
	}

	public void setId_path_fichero_salida(String id_path_fichero_salida) {
		this.id_path_fichero_salida = id_path_fichero_salida;
	}

	public String getId_data_source_reporte() {
		return id_data_source_reporte;
	}

	public void setId_data_source_reporte(String id_data_source_reporte) {
		this.id_data_source_reporte = id_data_source_reporte;
	}

	public String getDs_in_data_source() {
		return ds_in_data_source;
	}

	public void setDs_in_data_source(String ds_in_data_source) {
		this.ds_in_data_source = ds_in_data_source;
	}

	public String getId_conexion_bd_asoc() {
		return id_conexion_bd_asoc;
	}

	public void setId_conexion_bd_asoc(String id_conexion_bd_asoc) {
		this.id_conexion_bd_asoc = id_conexion_bd_asoc;
	}

	public String getId_path_file_in_report() {
		return id_path_file_in_report;
	}

	public void setId_path_file_in_report(String id_path_file_in_report) {
		this.id_path_file_in_report = id_path_file_in_report;
	}

	public String getId_archivo_jasper() {
		return id_archivo_jasper;
	}

	public void setId_archivo_jasper(String id_archivo_jasper) {
		this.id_archivo_jasper = id_archivo_jasper;
	}

	public Comunicacion getComunicacionAsociada() {
		return comunicacionAsociada;
	}

	public void setComunicacionAsociada(Comunicacion comunicacionAsociada) {
		this.comunicacionAsociada = comunicacionAsociada;
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
		result = prime * result + ((comunicacionAsociada == null) ? 0 : comunicacionAsociada.hashCode());
		result = prime * result + ((ds_in_data_source == null) ? 0 : ds_in_data_source.hashCode());
		result = prime * result + ((ds_periodicidad_ejecucion == null) ? 0 : ds_periodicidad_ejecucion.hashCode());
		result = prime * result + ((formato_fichero_salida == null) ? 0 : formato_fichero_salida.hashCode());
		result = prime * result + ((fx_fin_vigencia == null) ? 0 : fx_fin_vigencia.hashCode());
		result = prime * result + ((fx_ini_vigencia == null) ? 0 : fx_ini_vigencia.hashCode());
		result = prime * result + ((id_archivo_jasper == null) ? 0 : id_archivo_jasper.hashCode());
		result = prime * result + ((id_conexion_bd_asoc == null) ? 0 : id_conexion_bd_asoc.hashCode());
		result = prime * result + ((id_data_source_reporte == null) ? 0 : id_data_source_reporte.hashCode());
		result = prime * result + ((id_fichero_salida == null) ? 0 : id_fichero_salida.hashCode());
		result = prime * result + ((id_path_fichero_salida == null) ? 0 : id_path_fichero_salida.hashCode());
		result = prime * result + ((id_path_file_in_report == null) ? 0 : id_path_file_in_report.hashCode());
		result = prime * result + ((id_reporte == null) ? 0 : id_reporte.hashCode());
		result = prime * result + ((ind_extraccion_proceso == null) ? 0 : ind_extraccion_proceso.hashCode());
		result = prime * result + ((ob_reporte == null) ? 0 : ob_reporte.hashCode());
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
		Reporte other = (Reporte) obj;
		if (comunicacionAsociada == null) {
			if (other.comunicacionAsociada != null)
				return false;
		} else if (!comunicacionAsociada.equals(other.comunicacionAsociada))
			return false;
		if (ds_in_data_source == null) {
			if (other.ds_in_data_source != null)
				return false;
		} else if (!ds_in_data_source.equals(other.ds_in_data_source))
			return false;
		if (ds_periodicidad_ejecucion == null) {
			if (other.ds_periodicidad_ejecucion != null)
				return false;
		} else if (!ds_periodicidad_ejecucion.equals(other.ds_periodicidad_ejecucion))
			return false;
		if (formato_fichero_salida == null) {
			if (other.formato_fichero_salida != null)
				return false;
		} else if (!formato_fichero_salida.equals(other.formato_fichero_salida))
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
		if (id_archivo_jasper == null) {
			if (other.id_archivo_jasper != null)
				return false;
		} else if (!id_archivo_jasper.equals(other.id_archivo_jasper))
			return false;
		if (id_conexion_bd_asoc == null) {
			if (other.id_conexion_bd_asoc != null)
				return false;
		} else if (!id_conexion_bd_asoc.equals(other.id_conexion_bd_asoc))
			return false;
		if (id_data_source_reporte == null) {
			if (other.id_data_source_reporte != null)
				return false;
		} else if (!id_data_source_reporte.equals(other.id_data_source_reporte))
			return false;
		if (id_fichero_salida == null) {
			if (other.id_fichero_salida != null)
				return false;
		} else if (!id_fichero_salida.equals(other.id_fichero_salida))
			return false;
		if (id_path_fichero_salida == null) {
			if (other.id_path_fichero_salida != null)
				return false;
		} else if (!id_path_fichero_salida.equals(other.id_path_fichero_salida))
			return false;
		if (id_path_file_in_report == null) {
			if (other.id_path_file_in_report != null)
				return false;
		} else if (!id_path_file_in_report.equals(other.id_path_file_in_report))
			return false;
		if (id_reporte == null) {
			if (other.id_reporte != null)
				return false;
		} else if (!id_reporte.equals(other.id_reporte))
			return false;
		if (ind_extraccion_proceso == null) {
			if (other.ind_extraccion_proceso != null)
				return false;
		} else if (!ind_extraccion_proceso.equals(other.ind_extraccion_proceso))
			return false;
		if (ob_reporte == null) {
			if (other.ob_reporte != null)
				return false;
		} else if (!ob_reporte.equals(other.ob_reporte))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Reporte [id_reporte=" + id_reporte + ", ob_reporte=" + ob_reporte + ", fx_ini_vigencia="
				+ fx_ini_vigencia + ", fx_fin_vigencia=" + fx_fin_vigencia + ", ds_periodicidad_ejecucion="
				+ ds_periodicidad_ejecucion + ", id_fichero_salida=" + id_fichero_salida + ", formato_fichero_salida="
				+ formato_fichero_salida + ", id_path_fichero_salida=" + id_path_fichero_salida
				+ ", id_data_source_reporte=" + id_data_source_reporte + ", ds_in_data_source=" + ds_in_data_source
				+ ", id_conexion_bd_asoc=" + id_conexion_bd_asoc + ", id_path_file_in_report=" + id_path_file_in_report
				+ ", id_archivo_jasper=" + id_archivo_jasper + ", comunicacionAsociada=" + comunicacionAsociada
				+ ", ind_extraccion_proceso=" + ind_extraccion_proceso + "]";
	}
	
}
