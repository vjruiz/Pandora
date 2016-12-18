package com.spring.servicecore;




import java.util.ArrayList;

import com.spring.daocore.DaoFlujoTareas;

//@Component
public class CSFlujoTareas implements ICapaServicios {
	
	
	public DaoFlujoTareas daoClase = new DaoFlujoTareas();
	
	public CSFlujoTareas() {
	}

	public DaoFlujoTareas getDaoClase() {
		return daoClase;
	}

	public void setDaoClase(DaoFlujoTareas daoClase) {
		this.daoClase = daoClase;
	}

	@Override
	public <T> ArrayList<T> recuperarListado() 
	{
		return daoClase.recuperarListado();

	}

	@Override
	public Object recuperarObjeto(Object registroEntrada) 
	{			
		return daoClase.devolverObjeto(registroEntrada);
	}

	@Override
	public <T> boolean borrarListadoObjetos(ArrayList<T> registroEntrada) 
	{		
		return daoClase.borrarListadoObjetos(registroEntrada);
	}

	@Override
	public boolean altaObjeto(String registroEntrada) {
		return daoClase.altaObjeto(registroEntrada);
	}

	@Override
	public boolean modificarObjeto(String registroEntrada) {
		return daoClase.modificarObjeto(registroEntrada);
	}

	@Override
	public <T> ArrayList<T> recuperarListadoVigente() {
		return daoClase.recuperarListadoVigente();
	}
	
}
