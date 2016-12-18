package com.spring.servicecore;

import java.sql.Date;
import java.util.ArrayList;

public interface ICapaServicios {

	public boolean altaObjeto(String registroEntrada);
	public boolean modificarObjeto(String registroEntrada);	
	public Object recuperarObjeto(Object registroEntrada);	
	public  <T> ArrayList<T> recuperarListado();
	public <T> ArrayList<T> recuperarListadoVigente();
	public <T> boolean borrarListadoObjetos(ArrayList<T> registroEntrada);	

}

