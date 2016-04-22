package com.inventario.jpa.data;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="MARCA")

@NamedQueries(value={

		@NamedQuery(name = "HQL_MARCA",
				query = "SELECT m FROM MarcaEntity m"),

		@NamedQuery(name = "HQL_MARCA_POR_NOMBRE",
		query = "SELECT m FROM MarcaEntity m " +
				"WHERE upper(m.nombre)  = upper(:nombreMarca)"),

})

public class MarcaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MARCA_SEQ")
	@SequenceGenerator(name="MARCA_SEQ", sequenceName="MARCA_SEQ", allocationSize = 1)
	@Column(name = "IDMARCA")
	private int id;
	@Column(name = "MARCA")
	private String nombre;

	@OneToMany(mappedBy = "marca")
	private List<ModeloEntity> modelos;


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

	public List<ModeloEntity> getModelos() {
		return modelos;
	}

	public void setModelos(List<ModeloEntity> modelos) {
		this.modelos = modelos;
	}
}
