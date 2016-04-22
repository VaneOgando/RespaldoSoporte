package com.inventario.jpa.data;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="MODELO")

@NamedQueries(value={

		@NamedQuery(name = "HQL_MODELO_POR_MARCA",
				query = "SELECT mo FROM ModeloEntity mo JOIN mo.marca ma WHERE  ma.id = :idMarca"),

		@NamedQuery(name = "HQL_MODELO_POR_NOMBRE",
				query = "SELECT mo FROM ModeloEntity mo JOIN mo.marca ma " +
						"WHERE upper(mo.nombre) = upper(:nombreModelo) AND ma.id = :idMarca"),

})

public class ModeloEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MODELO_SEQ")
	@SequenceGenerator(name="MODELO_SEQ", sequenceName="MODELO_SEQ", allocationSize = 1)
	@Column(name = "IDMODELO")
	private int id;
	@Column(name = "MODELO")
	private String nombre;

	@ManyToOne
	@JoinColumn(name = "FKMARCA")
	private MarcaEntity marca;

	@OneToMany(mappedBy = "modelo")
	private List<EquipoEntity> equipos;

	@OneToMany(mappedBy = "modelo")
	private List<AccesorioEntity> accesorios;

	/*GET AND SET*/

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public MarcaEntity getMarca() {
		return marca;
	}

	public void setMarca(MarcaEntity marca) {
		this.marca = marca;
	}

	public List<EquipoEntity> getEquipos() {
		return equipos;
	}

	public void setEquipos(List<EquipoEntity> equipos) {
		this.equipos = equipos;
	}

	public List<AccesorioEntity> getAccesorios() {
		return accesorios;
	}

	public void setAccesorios(List<AccesorioEntity> accesorios) {
		this.accesorios = accesorios;
	}
}
