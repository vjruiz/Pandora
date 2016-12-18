package com.spring.servicecore;



import java.util.ArrayList;

import com.spring.daocore.DaoAddBBDD;

//@Component
public class CSaddBBDD implements ICapaServicios {
	
	private DaoAddBBDD daoClaseBBDD = new DaoAddBBDD();

	@Override
	public <T> ArrayList<T> recuperarListado() 
	{
		return daoClaseBBDD.recuperarListado();

	}

	@Override
	public Object recuperarObjeto(Object registroEntrada) 
	{			
		return daoClaseBBDD.devolverObjeto(registroEntrada);
	}

	@Override
	public <T> boolean borrarListadoObjetos(ArrayList<T> registroEntrada) 
	{		
		return daoClaseBBDD.borrarListadoObjetos(registroEntrada);
	}

	@Override
	public boolean altaObjeto(String registroEntrada) {
		// TODO Auto-generated method stub
		return daoClaseBBDD.altaObjeto(registroEntrada);
	}

	@Override
	public boolean modificarObjeto(String registroEntrada) {
		return daoClaseBBDD.modificarObjeto(registroEntrada);
	}

	@Override
	public <T> ArrayList<T> recuperarListadoVigente() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
