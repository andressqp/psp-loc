package co.edu.usco.psp.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import co.edu.usco.psp.model.Metrica;
import co.edu.usco.psp.model.Proyecto;
import co.edu.usco.psp.repository.IProyectoRepository;
import me.tongfei.progressbar.ProgressBar;

@Service
public class MetricaService {

	@Autowired
	private BuscadorClasesService buscadorClasesService;

	@Autowired
	private CalculadorMetricasService calculadorMetricasService;

	@Autowired
	private IProyectoRepository proyectoRepo;

	public void calcularMetricas(String rutaProyecto) throws IOException {

		List<File> lstClass = this.buscadorClasesService.buscarClases(rutaProyecto);
		List<Metrica> lstMetrica = new ArrayList<>();

		for (File clase : ProgressBar.wrap(lstClass, "Calculando")) {

			Metrica metrica = this.calculadorMetricasService.calcularMetricas(clase);

			lstMetrica.add(metrica);

		}

		this.guardar(rutaProyecto, lstMetrica);
	}
	
	@Transactional
	public void guardar(String rutaProyecto, List<Metrica> entities) {
		
		System.out.println("Guardando metricas...");

		String division[] = rutaProyecto.split("/");
		String nombreProyecto = division[division.length - 1];
		

		Proyecto proyecto = Proyecto.builder().nombre(nombreProyecto).lstMetrica(entities).build();
		
		proyecto.setFecha(LocalDateTime.now());
		
		proyecto.getLstMetrica().forEach(metrica -> {
			System.out.println("Clase: "+metrica.getClase());
			System.out.println("cantidad lineas logica: "+metrica.getCantidadLineasLogica());
			System.out.println("cantidad lineas comentadas: "+metrica.getCantidadLineasComentadas());
			System.out.println("cantidad lineas Muertas: "+metrica.getCantidadLineasMuertas());
			System.out.println("cantidad de metodos: "+metrica.getCantidadMetodos());
			metrica.getLstAtributo().forEach(atributo -> {

				atributo.setMetricaId(metrica);
			});

			metrica.setProyectoId(proyecto);
		});
		
		
		this.proyectoRepo.save(proyecto);
		
	}

}
