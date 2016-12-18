package com.spring.servicecore;



import java.util.ArrayList;

import com.spring.daocore.DaoReportes;

//@Component
public class CSreportes implements ICapaServicios {

	private DaoReportes daoClaseRepotes = new DaoReportes();

	public CSreportes() {
	}

	@Override
	public <T> ArrayList<T> recuperarListado() 
	{
		return daoClaseRepotes.recuperarListado();

	}

	@Override
	public Object recuperarObjeto(Object registroEntrada) 
	{			
		return daoClaseRepotes.devolverObjeto(registroEntrada);
	}

	@Override
	public <T> boolean borrarListadoObjetos(ArrayList<T> registroEntrada) 
	{		
		return daoClaseRepotes.borrarListadoObjetos(registroEntrada);
	}

	@Override
	public boolean altaObjeto(String registroEntrada) {
		return daoClaseRepotes.altaObjeto(registroEntrada);
	}

	@Override
	public boolean modificarObjeto(String registroEntrada) {
		return daoClaseRepotes.modificarObjeto(registroEntrada);
	}

	@Override
	public <T> ArrayList<T> recuperarListadoVigente() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
