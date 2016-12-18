package com.utilidades.ficheros;

import java.io.File;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component("directorios")
public class Directorios {				
	
	public Directorios() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> ArrayList<T> recuperarListadoFicheros(String directorioEntrada) {
		
		ArrayList<String> arraySalida = new ArrayList<String>();		
		File directorio = new File(directorioEntrada);
		
		if (directorio.exists())
		{ 
			File[] ficheros = directorio.listFiles();
			for (int x=0;x<ficheros.length;x++){
				arraySalida.add(ficheros[x].getName());
				System.out.println(ficheros[x].getName());
			}			
		}
		else { 
			//Directorio no existe 
		}

		return (ArrayList<T>) arraySalida;
		
	}
	
}
