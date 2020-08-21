package co.edu.usco.psp.util;

import org.springframework.stereotype.Component;

@Component
public class FormateadorRutaClase {

	public String rutaAPaqueteNombreClase(String ruta) {
		
		String division[] = ruta.split("/java/");
		String nombreSinFormato = division[division.length - 1].replace(".java", "");
		
		String nombreClase = nombreSinFormato.replace("/", ".");
		
		return nombreClase;
	}
	
	public String getNombreClaseDeRuta(String ruta) {
		
		String division[] = ruta.split("/");
		String nombreClase = division[division.length - 1].replace(".java", "");
		
		return nombreClase;
	}
}
