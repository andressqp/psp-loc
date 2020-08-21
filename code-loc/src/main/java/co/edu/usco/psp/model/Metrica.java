package co.edu.usco.psp.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "metrica")
public class Metrica {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "clase", length = 300, nullable = false)
	private String clase;

	@Column(name = "cantidad_metodos", nullable = false)
	private Integer cantidadMetodos;

	@Column(name = "cantidad_lineas_logica", nullable = false)
	private Integer cantidadLineasLogica;

	@Column(name = "cantidad_lineas_comentadas", nullable = false)
	private Integer cantidadLineasComentadas;
	
	@Column(name = "cantidad_lineas_muertas", nullable = false)
	private Integer cantidadLineasMuertas;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "proyecto_id", nullable = false)
	private Proyecto proyectoId;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "metricaId")
	private List<Atributo> lstAtributo;

	@Override
	public String toString() {
		return "Metrica [id=" + id + ", clase=" + clase + ", cantidadMetodos=" + cantidadMetodos
				+ ", cantidadLineasLogica=" + cantidadLineasLogica + ", cantidadLineasComentadas="
				+ cantidadLineasComentadas + ", cantidadLineasMuertas=" + cantidadLineasMuertas + ", proyectoId="
				+ proyectoId + ", lstAtributo=" + lstAtributo + "]";
	}


}
