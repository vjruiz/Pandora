package com.utilidades.bbdd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.spring.daocore.DaoAddBBDD;
import com.spring.daocore.IDao;
import com.spring.model.BBDDDefinida;

@Component("createTabla")
public class CreateTabla {
	
	@Autowired
	@Qualifier("daoaddbbdd")
	private IDao daoClase;
	
	private Connection conexionEncelado;	
	private Statement sentencia_create_drop;		
	private StringBuffer estructuraCampos;	
	
	
 	{
 		conexionEncelado = null;
 		sentencia_create_drop = null;
 	 	estructuraCampos = null; 		
 	} 	 
	
	public CreateTabla() {
		super();
	}

	public String CrearTabla(String id_tabla_nueva,String id_conexion_BD_Asoc,String ds_codigo_sql_asoc) 
	{
		
		String nombreTabla = null;		
				
		try {
			nombreTabla = "encelado." + id_tabla_nueva;
			
			conexionEncelado = this.realizarConexionEncelado("encelado");										
			 
			this.ejecutarSentencia(this.devolverSentenciaCreate
					(nombreTabla,
					this.recuperarEstructuraCampos(id_conexion_BD_Asoc, 
													ds_codigo_sql_asoc))
					);
			
		} catch (PSQLException e) {
			e.printStackTrace();
			nombreTabla = null;
		}
		 catch (SQLException e) {
				e.printStackTrace();
				nombreTabla = null;
		}		
		finally {
			this.cerrarRecursosDB2();
		}
		
		return nombreTabla;
		
	}
	
	
	public boolean BorrarTabla(String id_tabla_borrar) 
	{
		
		boolean controlBorrado = false;
		
		System.out.println("BorrarTabla .- " + id_tabla_borrar);
				
		try {				
			conexionEncelado = this.realizarConexionEncelado("encelado");		
						 
			this.ejecutarSentencia(this.devolverSentenciaDrop(id_tabla_borrar));
			controlBorrado = true;
			
		}catch (PSQLException e) {
				e.printStackTrace();
		}
		 catch (SQLException e) {
				e.printStackTrace();
		}
		finally {
			this.cerrarRecursosDB2();
		}
		
		return controlBorrado;
		
	}
	
	private ArrayList<String> recuperarEstructuraCampos(String id_conexion_BD_Asoc,String ds_codigo_sql_asoc) {
		
		ArrayList<String> arraySalida = new ArrayList<String>();
		
		BBDDDefinida baseDatosExterna = new BBDDDefinida();
		baseDatosExterna.setId_conexion_bd(id_conexion_BD_Asoc);
		//baseDatosExterna = (BBDDDefinida) daoClase.devolverObjeto(baseDatosExterna);
		daoClase = new DaoAddBBDD();
		baseDatosExterna =		
				(BBDDDefinida) daoClase.devolverObjeto(baseDatosExterna);

		
		try 
		{		
			ResultSet resultadoApoyoDestino = baseDatosExterna.realizarPruebaConexion(ds_codigo_sql_asoc);
								
			if (resultadoApoyoDestino != null) 
			{
                ResultSetMetaData rsmd = resultadoApoyoDestino.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                	arraySalida.add(rsmd.getColumnName(i));
                }
			}
						
			
		} 
		catch (SQLException e) {
			System.out.println("no existen datos" + e );
		}
		catch (NullPointerException e) {
			System.out.println("no existen datos" + e );
		}
		
		return (ArrayList<String>) arraySalida;
		
	}
	
	private String devolverSentenciaCreate(String id_tabla_nueva,ArrayList<String> estructuraTabla) 
	{		
		estructuraCampos = new StringBuffer();
		StringBuffer estructuraApoyo = new StringBuffer();
		
		estructuraCampos.append("CREATE TABLE " + id_tabla_nueva + " (" );
		
		for (String nombreCampoTabla:estructuraTabla) {			
			estructuraApoyo.append(nombreCampoTabla + "  character varying," );
		}		
		System.out.println(estructuraApoyo.substring(0, estructuraApoyo.length() - 1));
		estructuraCampos.append(
				estructuraApoyo.substring(0, estructuraApoyo.length() - 1));
		
		estructuraCampos.append(
				") " + 
				"WITH (" +
						"OIDS=FALSE" +
					 "); "+
				"ALTER TABLE " + id_tabla_nueva +
				" OWNER TO postgres;");				
		
		System.out.println(estructuraCampos.toString());
		
		return estructuraCampos.toString();
		
	}
	
	
	private String devolverSentenciaDrop(String id_tabla_borrar) 
	{				
		
		return "DROP TABLE encelado." + id_tabla_borrar;
		
	}
	
	private Connection realizarConexionEncelado(String tipoConexion) throws SQLException
	{
		
		Connection nuevaConexion;
		
			nuevaConexion = ConexionBD.getInstance().devolverConexion(tipoConexion);
		
		return nuevaConexion;
		
	}

	private void ejecutarSentencia(String sentenciaEntrada) 
	{		    

		try {
			sentencia_create_drop = conexionEncelado.createStatement();
			sentencia_create_drop.executeUpdate(sentenciaEntrada);
		} 	
		catch (SQLException e) {

			e.printStackTrace();
			System.out.println(e);
		}		
		
	}
	
	private void cerrarRecursosDB2() 
	{
		try 
		{
			
			if (conexionEncelado != null) {conexionEncelado.close();};
			if (sentencia_create_drop != null) {sentencia_create_drop.close();};
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}	

}
