package com.spring.servicecore;



import java.util.ArrayList;

import com.spring.daocore.DaoAddSever;


//@Component
public class CSaddServer implements ICapaServicios {
	
	private DaoAddSever daoClaseServer = new DaoAddSever();

	@Override
	public <T> ArrayList<T> recuperarListado() 
	{
		return daoClaseServer.recuperarListado();
	}

	@Override
	public Object recuperarObjeto(Object registroEntrada) 
	{			
		return daoClaseServer.devolverObjeto(registroEntrada);
	}

	@Override
	public <T> boolean borrarListadoObjetos(ArrayList<T> registroEntrada) 
	{		
		return daoClaseServer.borrarListadoObjetos(registroEntrada);
	}

	@Override
	public boolean altaObjeto(String registroEntrada) {
		return daoClaseServer.altaObjeto(registroEntrada);
	}

	@Override
	public boolean modificarObjeto(String registroEntrada) {
		return daoClaseServer.modificarObjeto(registroEntrada);
	}

	@Override
	public <T> ArrayList<T> recuperarListadoVigente() {
		// TODO Auto-generated method stub
		return null;
	}

}
