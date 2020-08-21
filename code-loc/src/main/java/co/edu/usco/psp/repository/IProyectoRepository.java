package co.edu.usco.psp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.usco.psp.model.Proyecto;

public interface IProyectoRepository extends JpaRepository<Proyecto, Integer> {

	@Query("select new co.edu.usco.psp.model.Proyecto(p.id,p.nombre,p.fecha) from Proyecto p order by p.fecha desc")
	List<Proyecto> buscarOrdenadoPorFechaDesc();
}
