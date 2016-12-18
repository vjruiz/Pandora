package com.spring.servicecore;



import java.util.ArrayList;

import com.spring.daocore.DaoComunicacion;


//@Component
public class CScomunicacion implements ICapaServicios {
	

	private DaoComunicacion daoClaseComunicacion = new DaoComunicacion();

	public CScomunicacion() {
	}

	@Override
	public <T> ArrayList<T> recuperarListado() 
	{
		return daoClaseComunicacion.recuperarListado();

	}

	@Override
	public Object recuperarObjeto(Object registroEntrada) 
	{			
		return daoClaseComunicacion.devolverObjeto(registroEntrada);
	}

	@Override
	public <T> boolean borrarListadoObjetos(ArrayList<T> registroEntrada) 
	{		
		return daoClaseComunicacion.borrarListadoObjetos(registroEntrada);
	}

	@Override
	public boolean altaObjeto(String registroEntrada) {
		return daoClaseComunicacion.altaObjeto(registroEntrada);
	}

	@Override
	public boolean modificarObjeto(String registroEntrada) {
		return daoClaseComunicacion.modificarObjeto(registroEntrada);
	}

	@Override
	public <T> ArrayList<T> recuperarListadoVigente() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
