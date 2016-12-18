package com.spring.daocore;

import java.sql.Date;
import java.util.ArrayList;

public interface IDao {

	public boolean altaObjeto(String registroEntrada);	
	public boolean modificarObjeto(String registroEntrada);	
	public Object devolverObjeto(Object registroEntrada);
	public <T> ArrayList<T> recuperarListado();
	public <T> ArrayList<T> recuperarListadoVigente();
	public <T> boolean borrarListadoObjetos(ArrayList<T> registroEntrada);
	
}
