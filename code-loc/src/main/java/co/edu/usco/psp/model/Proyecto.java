package co.edu.usco.psp.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "proyecto")
public class Proyecto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "nombre", length = 200, nullable = false)
	private String nombre;

	@Column(name = "fecha", nullable = true)
	private LocalDateTime fecha;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "proyectoId")
	private List<Metrica> lstMetrica;

	public Proyecto(Integer id, String nombre, LocalDateTime fecha) {
		this.id = id;
		this.nombre = nombre;
		this.fecha = fecha;
	}

}
