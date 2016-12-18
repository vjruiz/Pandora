package com.spring.servicecore;



import java.util.ArrayList;

import com.spring.daocore.DaoIntegraBD;



//@Component
public class CSintegraBD implements ICapaServicios {
	
	private DaoIntegraBD daoClaseIntegraBD = new DaoIntegraBD();


	public CSintegraBD() {
	}	

	@Override
	public <T> ArrayList<T> recuperarListado() 
	{
		return daoClaseIntegraBD.recuperarListado();

	}

	@Override
	public Object recuperarObjeto(Object registroEntrada) 
	{			
		return daoClaseIntegraBD.devolverObjeto(registroEntrada);
	}

	@Override
	public <T> boolean borrarListadoObjetos(ArrayList<T> registroEntrada) 
	{		
		return daoClaseIntegraBD.borrarListadoObjetos(registroEntrada);
	}

	@Override
	public boolean altaObjeto(String registroEntrada) {
		return daoClaseIntegraBD.altaObjeto(registroEntrada);
	}

	@Override
	public boolean modificarObjeto(String registroEntrada) {
		return daoClaseIntegraBD.modificarObjeto(registroEntrada);
	}

	@Override
	public <T> ArrayList<T> recuperarListadoVigente() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
