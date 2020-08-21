package co.edu.usco.psp.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.usco.psp.exception.ArchivoNoEncontradoException;
import co.edu.usco.psp.exception.NoMavenProjectException;

@Service
public class BuscadorClasesService {

	public List<File> buscarClases(String rutaProyecto) {
		File proyecto = new File(rutaProyecto);

		
		if (!proyecto.exists()) {

			String mensaje = "La ruta del proyecto no existe";
			throw new ArchivoNoEncontradoException(mensaje);

		} else if (!esProyectoMaven(proyecto)) {

			String mensaje = "El proyecto seleccionado no es un Proyecto java";
			throw new NoMavenProjectException(mensaje);
		}
		String rutaResources = String.format("%s/%s", proyecto.getAbsolutePath(), "src");
		return buscarClasesEnCarpeta(new File(rutaResources));
	}

	private List<File> buscarClasesEnCarpeta(File carpeta) {
		List<File> lstClass = new ArrayList<>();

		for (String nombreArchivo : carpeta.list()) {

			String rutaArchivo = String.format("%s/%s", carpeta.getAbsolutePath(), nombreArchivo);
			File archivo = new File(rutaArchivo);
			if (archivo.isFile() && rutaArchivo.contains(".java")) {
				lstClass.add(archivo);

			} else if (archivo.isDirectory()) {

				lstClass.addAll(this.buscarClasesEnCarpeta(archivo));
			}
		}

		return lstClass;
	}

	private boolean esProyectoMaven(File proyecto) {
		boolean esProyectoMaven = false;

		for (String nombreArchivo : proyecto.list()) {
			if (nombreArchivo.contains("pom.xml")) {

				esProyectoMaven = true;
				break;
			}
		}

		return esProyectoMaven;
	}
	

}
