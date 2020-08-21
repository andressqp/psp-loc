package co.edu.usco.psp.service;

import java.time.LocalDateTime;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import co.edu.usco.psp.model.Proyecto;
import co.edu.usco.psp.repository.IProyectoRepository;

@Service
public class ProyectoService {
	
	@Autowired
	private IProyectoRepository repo;

	public void guardar(Proyecto proyecto) {
		
		proyecto.setFecha(LocalDateTime.now());
		
		proyecto.getLstMetrica().forEach(metrica -> {

			metrica.getLstAtributo().forEach(atributo -> {

				atributo.setMetricaId(metrica);
			});

			metrica.setProyectoId(proyecto);
		});
		
		this.repo.save(proyecto);
	}
	

}
