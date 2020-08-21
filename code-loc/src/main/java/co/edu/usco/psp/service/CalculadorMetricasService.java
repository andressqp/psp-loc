package co.edu.usco.psp.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.usco.psp.exception.ArchivoNoEncontradoException;
import co.edu.usco.psp.exception.TipoArchivoException;
import co.edu.usco.psp.model.Atributo;
import co.edu.usco.psp.model.Metrica;
import co.edu.usco.psp.util.FormateadorRutaClase;

@Service
public class CalculadorMetricasService {

	private Metrica metrica;
	private boolean esDentroClase;
	private boolean esDentroMetodo;
	private int llavesAbiertas;
	private String nombreClase;
	private boolean esDentroConstructor;
	private File clase;
	private Map<String, Integer> mapAtributos;
	private String lineaActual;

	@Autowired
	private FormateadorRutaClase formateadorRutaClase;

	public Metrica calcularMetricas(File clase) throws IOException {

		this.validarExistenciaClase(clase);

		this.reiniciarVariables();
		this.clase = clase;

		String rutaClase = this.clase.getAbsolutePath();

		this.nombreClase = this.formateadorRutaClase.getNombreClaseDeRuta(rutaClase);

		try (Stream<String> stream = Files.lines(Paths.get(rutaClase))) {

			stream.forEach(this::analizarLineaCodigo);

			this.contruirMetrica();

			return this.metrica;

		} catch (IOException e) {
			throw e;
		}

	}

	private void contruirMetrica() {

		String rutaClase = this.clase.getAbsolutePath();
		this.metrica.setClase(this.formateadorRutaClase.rutaAPaqueteNombreClase(rutaClase));

		List<Atributo> lstAtributo = new ArrayList<>();

		this.mapAtributos.entrySet().forEach(atributo -> {

			String nombreAtributo = atributo.getKey();
			Integer cantidadUso = atributo.getValue();

			lstAtributo.add(Atributo.builder().nombre(nombreAtributo).cantidadUso(cantidadUso).build());
		});

		this.metrica.setLstAtributo(lstAtributo);
	}

	private void validarExistenciaClase(File clase) {

		if (!clase.exists()) {

			throw new ArchivoNoEncontradoException("No existe clase en la ruta -> " + clase.getAbsolutePath());

		} else if (!clase.isFile()) {

			throw new TipoArchivoException("La clase no es un fichero -> " + clase.getAbsolutePath());
		}
	}

	private void analizarLineaCodigo(String linea) {

		this.lineaActual = linea.trim();

		if (this.lineaActual.length() == 0) {

			this.sumarLineasMuertas();

		} else if (this.lineaActual.startsWith("//") || this.lineaActual.startsWith("/*")
				|| this.lineaActual.startsWith("*") || linea.startsWith("*/")) {

			this.sumarLineasComentada();

		} else if (this.lineaActual.contains("{") && !this.esDentroClase) {

			this.esDentroClase = true;

		} else if (this.esDentroClase) {

			this.analizarLineaDentroClase();
		}

		this.sumarLineasLogica();
	}

	private void sumarLineasComentada() {

		Integer cantidadComentarios = this.metrica.getCantidadLineasComentadas();

		if (cantidadComentarios != null) {

			cantidadComentarios++;

		} else {

			cantidadComentarios = 1;

		}

		this.metrica.setCantidadLineasComentadas(cantidadComentarios);
	}

	private void sumarLineasLogica() {

		Integer cantidadLineaLogica = this.metrica.getCantidadLineasLogica();

		if (cantidadLineaLogica != null) {

			cantidadLineaLogica++;

		} else {

			cantidadLineaLogica = 1;
		}

		this.metrica.setCantidadLineasLogica(cantidadLineaLogica);

	}
	
	private void sumarLineasMuertas() {

		Integer cantidadLineasMuertas = this.metrica.getCantidadLineasMuertas();

		if (cantidadLineasMuertas != null) {

			cantidadLineasMuertas++;

		} else {

			cantidadLineasMuertas = 1;

		}

		this.metrica.setCantidadLineasMuertas(cantidadLineasMuertas);
	}

	private void sumarAtributos() {

		String nombreAtributo = this.extraerNombreAtributo();

		this.mapAtributos.put(nombreAtributo, 0);
	}

	private void sumarUsoAtributos() {

		this.mapAtributos.keySet().forEach(atributo -> {

			if (this.lineaActual.contains("this." + atributo)) {

				Integer usoAtributo = this.mapAtributos.get(atributo) + 1;

				this.mapAtributos.put(atributo, usoAtributo);
			}

		});

	}

	private String extraerNombreAtributo() {

		String division[] = this.lineaActual.split("=");

		String nombreAtributo = this.lineaActual;

		if (division.length > 1) {

			nombreAtributo = division[division.length - 2].trim();

		}

		division = nombreAtributo.split(" ");

		nombreAtributo = division[division.length - 1].replace(";", "");

		return nombreAtributo;
	}

	private void sumarMetodos() {

		Integer cantidadMetodos = this.metrica.getCantidadMetodos();

		if (cantidadMetodos != null) {

			cantidadMetodos++;

		} else {

			cantidadMetodos = 1;
		}

		this.metrica.setCantidadMetodos(cantidadMetodos);

	}

	private void analizarLineaDentroConstructor() {

		if (this.lineaActual.contains("{")) {

			this.llavesAbiertas++;

		} else if (this.lineaActual.contains("}")) {

			if (this.llavesAbiertas == 0) {

				this.esDentroConstructor = false;

			} else {

				this.llavesAbiertas++;
			}
		}
	}

	private void analizarLineaDentroClase() {

		if (this.lineaActual.contains("{") && this.lineaActual.contains(this.nombreClase)) {

			this.esDentroConstructor = true;
			this.llavesAbiertas = 0;

		} else if (this.esDentroConstructor) {

			this.analizarLineaDentroConstructor();

		} else if (this.lineaActual.contains("{") && !this.esDentroMetodo) {

			this.esDentroMetodo = true;
			this.sumarMetodos();
			this.llavesAbiertas = 0;

		} else if (this.esDentroMetodo) {

			this.analizarLineaMetodo();
			this.sumarUsoAtributos();

		} else if (this.lineaActual.contains(";") && !this.esDentroMetodo) {

			this.sumarAtributos();

		}
	}

	private void analizarLineaMetodo() {

		if (this.lineaActual.contains("}") && this.lineaActual.contains("{")) {

			return;

		} else if (this.lineaActual.contains("{")) {

			this.llavesAbiertas++;

		} else if (this.lineaActual.contains("}")) {

			this.llavesAbiertas--;

			if (this.llavesAbiertas < 0) {

				this.esDentroMetodo = false;

			}
		}
	}

	private void reiniciarVariables() {

		this.metrica = new Metrica(null, null, 0, 0, 0, 0,null, null);
		this.esDentroClase = false;
		this.esDentroMetodo = false;
		this.llavesAbiertas = 0;
		this.nombreClase = "";
		this.esDentroConstructor = false;
		this.clase = null;
		this.mapAtributos = new HashMap<>();
		this.lineaActual = "";
	}
}
