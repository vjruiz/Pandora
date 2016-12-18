package com.athenea.activiti.principal.servicetask.procesogenerico.calculaprocesos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.spring.model.FlujoTareasProgramadas;
import com.spring.servicecore.CSFlujoTareas;
import com.utilidades.bbdd.ConexionBD;

public class CPGenericoLanzadorTareas implements CalculaProcesos{
	
    private static Logger log = Logger.getLogger(CPGenericoLanzadorTareas.class);
	
	private Connection conexion;
	private String variablesSelect;
	private Statement statementSelect;
    private ResultSet resulSetConsultaRealizada;
    private CSFlujoTareas capaServiciosFlujoTareasProgramadas;
	
	{ // bloque de inicializacion de variables de instancia
		conexion = null;
		variablesSelect = null;
		statementSelect = null;
		resulSetConsultaRealizada = null;
	}
	
	public CPGenericoLanzadorTareas() {		
	}
	
	public Connection getConexion() {
		return conexion;
	}

	public void setConexion(Connection conexion) {
		this.conexion = conexion;
	}

	public String getVariablesSelect() {
		return variablesSelect;
	}

	public void setVariablesSelect(String variablesSelect) {
		this.variablesSelect = variablesSelect;
	}

	public Statement getStatementSelect() {
		return statementSelect;
	}

	public void setStatementSelect(Statement statementSelect) {
		this.statementSelect = statementSelect;
	}

	public ResultSet getResulSetConsultaRealizada() {
		return resulSetConsultaRealizada;
	}

	public void setResulSetConsultaRealizada(ResultSet resulSetConsultaRealizada) {
		this.resulSetConsultaRealizada = resulSetConsultaRealizada;
	}


	public ArrayList< Map<?, ?>> execute(String nombreProceso, ApplicationContext contextSpring) 
	{		
		ArrayList<Map<?, ?>> arrayProcesosEjecutar = new ArrayList<Map<?, ?>>();
		ArrayList<FlujoTareasProgramadas> flujosVigentes = null;
		Map<String, Object> variableMap = null;
		
		try {
			this.setConexion(this.realizarConexion("encelado"));
			
			capaServiciosFlujoTareasProgramadas = new CSFlujoTareas();	
			flujosVigentes = capaServiciosFlujoTareasProgramadas.recuperarListadoVigente();		
			variableMap = new HashMap<String, Object>();
			
			for(FlujoTareasProgramadas flujoTratado:flujosVigentes)
				{
	
						variableMap = null;
						variableMap = new HashMap<String, Object>();
					
					String ultimaEjecucionCasoParque = 
							this.recuperarDatosAsociadosCasoParque(flujoTratado.getId_flujo_tarea());
					
					switch (ultimaEjecucionCasoParque) {
						case "noEjecutar":
						
							break;
							
						case "ejecutar":
							
							variableMap.put("caso", flujoTratado);
							arrayProcesosEjecutar.add(variableMap);	
							break;
	
						default:							
							try {
								if (this.validarFechas(ultimaEjecucionCasoParque,
													   flujoTratado.getDs_periodicidad_ejecucion().substring(0, 5))) 
								{
									// añadimos los datos al array de flujos a lanzar
									variableMap.put("caso", flujoTratado);
									arrayProcesosEjecutar.add(variableMap);	
								}
							} catch (ParseException e) {
								log.fatal("Error calculando las tareas que se deben ejecutar.- " + flujoTratado.getId_flujo_tarea(), e);
							}; 
							break;
					}																
					
				}
		} catch (SQLException e1) {
			log.fatal("Error realizando conexion encelado",e1);
		}
		finally 
		{
			this.cerrarRecursosDB2();
		}
		
		return  arrayProcesosEjecutar;
	}	
	
	private boolean validarFechas (String ultimaEjecucionCasoParque, String Ds_periodicidad_ejecucion) 
			throws ParseException 
	{						
		boolean resultadoComparacion = false;
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm"); 
		String dateMomentoActual = sdf.format(new Date());
		
		Calendar fechaDia = Calendar.getInstance();	
		String dateMomentoProgramado = dateMomentoActual.substring(0, 10) + " "
									+  Ds_periodicidad_ejecucion.replace("AM", "").trim();
		
			
/*		int diaMes = fechaDia.get(Calendar.DAY_OF_MONTH);
		int horaActual = fechaDia.get(Calendar.HOUR_OF_DAY);
		int minutoActual = fechaDia.get(Calendar.MINUTE);
		String 	momentoActual = diaMes + " " + horaActual + ":" + minutoActual;

		String momentoProgramado = diaMes + " " + Ds_periodicidad_ejecucion; 	*/					
		
		//inicializamos la variable para no incluir la tarea en caso de error en el calculo
		int resultadoComparacionMomentoActual = 0;
		int resultadoComparacionMomentoProgramado = 0;
						
			//Si la horaini que vas a comparar es anterior a horafin, el método devolverá un valor menor a cero. 
			//	Si la horaini que vas a comparar es posterior al argumento horafin, el método devolverá un valor mayor a cero. 
			resultadoComparacionMomentoActual = this.compararFechas(dateMomentoActual,
																	dateMomentoProgramado);	
			//SI DEVUELVE 1 SIGNIFICA QUE EL dateMomentoProgramado ES MAYOR QUE dateMomentoActual
			// POR LO QUE SE ENTIENDE no SE DEBE EJECUTAR, si es menor, es decir, dateMomentoProgramado no ha sobrepaso
			if (( resultadoComparacionMomentoActual < 0) || ( resultadoComparacionMomentoActual == 0)) 
			{
				resultadoComparacionMomentoProgramado = this.compararFechas(ultimaEjecucionCasoParque,
																			dateMomentoProgramado);	
				//SI DEVUELVE 1 SIGNIFICA QUE EL dateMomentoProgramado ES MAYOR QUE ultimaEjecucionCasoParque
				// POR LO QUE SE ENTIENDE SE DEBE EJECUTAR.
				if (( resultadoComparacionMomentoProgramado > 0) || ( resultadoComparacionMomentoProgramado == 0)) 
							resultadoComparacion = true;
			}; 

		return resultadoComparacion;
	}
	
	private String recuperarDatosAsociadosCasoParque(String claveTareaEjecutada)
	{
		String variablesSelectFuncion;
		Statement statementSelectFuncion;
		ResultSet resulSetConsultaRealizada = null;
		String estadoUltimaEjecucion = null;
		
		String cadenaUEjecucion = null;
		
			try 
			{
				statementSelectFuncion = this.getConexion().createStatement();
				variablesSelectFuncion = 
						"select  to_char((fecha_ejecucion),'dd/MM/yyyy HH24:MI'),resultado " +
	                    "from encelado.im_datos_ejecucion  where entidad = '" + 
	                    claveTareaEjecutada + 
	                    "' and resultado != 'NOK' " +
	                    "order by fecha_ejecucion desc limit 1";
				resulSetConsultaRealizada = statementSelectFuncion.executeQuery(variablesSelectFuncion);

			
				 if ((resulSetConsultaRealizada != null) && (resulSetConsultaRealizada.next())) 
				 {
					 	cadenaUEjecucion = resulSetConsultaRealizada.getString(1);
		            	estadoUltimaEjecucion = resulSetConsultaRealizada.getString(2);
		                //DD:HH:MI:SS
		            	//01234567890
		            	if (estadoUltimaEjecucion.equals("En Proceso")) cadenaUEjecucion = "noEjecutar";
				 }
				 else
				 {
					 	cadenaUEjecucion = "ejecutar";
				 }
				 

			} 
			catch (SQLException e) {
				log.warn("Imposible calcular timer ultima ejecucioncaso" + claveTareaEjecutada,e);
			}
    	
    	return cadenaUEjecucion;
	}
	
	private int compararFechas(String horaMinIniComparar,String horaMinFinComparar) throws ParseException
	{
		//Si las horas son iguales, el método devolverá un cero. 
		//Si la horaini que vas a comparar es anterior a horafin, el método devolverá un valor menor a cero. 
		//Si la horaini que vas a comparar es posterior al argumento horafin, el método devolverá un valor mayor a cero. 
		
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date fechaIni = dateFormat.parse(horaMinIniComparar,new ParsePosition(0));
        Date fechaFin = dateFormat.parse(horaMinFinComparar,new ParsePosition(0));
        
        if (fechaIni.before(fechaFin))
        {
        	return 1;
        } else return -1;

        //return horaIni.compareTo(horaFin);

		
	}

	private Connection realizarConexion(String tipoConexion) throws SQLException
	{
		
		Connection nuevaConexion;

		// ---Conexion------------//

		nuevaConexion = ConexionBD.getInstance().devolverConexion(tipoConexion);
		//nuevaConexion.setAutoCommit(true);

		// ---Conexion realizada

		return nuevaConexion;
		
	}
	
	private void cerrarRecursosDB2() 
	{
		try 
		{

			if (conexion != null) {conexion.close();};
			if (statementSelect != null) {statementSelect.close();};
			if (resulSetConsultaRealizada != null) {resulSetConsultaRealizada.close();};
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
