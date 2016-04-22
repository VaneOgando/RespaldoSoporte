package com.inventario.jpa.data;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="CATEGORIA")

@NamedQueries(value={

	@NamedQuery(name = "HQL_CATEGORIA_POR_TIPO",
			query = "SELECT ca FROM CategoriaEntity ca " +
					"WHERE ca.tipoCategoria = :tipoCategoria"),

	@NamedQuery(name = "HQL_CATEGORIA_POR_ID",
			query = "SELECT ca FROM CategoriaEntity ca " +
					"WHERE ca.id = :idCategoria"),

	@NamedQuery(name = "HQL_CATEGORIA_POR_NOMBRE_Y_TIPO",
			query = "SELECT ca FROM CategoriaEntity ca " +
					"WHERE upper(ca.nombre) = upper(:nombreCategoria) AND ca.tipoCategoria = :tipoCategoria")

})

public class CategoriaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATEGORIA_SEQ")
	@SequenceGenerator(name="CATEGORIA_SEQ", sequenceName="CATEGORIA_SEQ", allocationSize = 1)
	@Column(name = "IDCATEGORIA")
	private int id;
	@Column(name = "CATEGORIA")
	private String nombre;
	@Column(name = "TIPOCATEGORIA")
	private String tipoCategoria;


	@OneToMany(mappedBy = "categoria")
	private List<AccesorioEntity> accesorios;

	@OneToMany(mappedBy = "categoria")
	private List<HistorialInventarioEntity> historiales;


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

	public String getTipoCategoria() {
		return tipoCategoria;
	}

	public void setTipoCategoria(String tipoCategoria) {
		this.tipoCategoria = tipoCategoria;
	}

	public List<AccesorioEntity> getAccesorios() {
		return accesorios;
	}

	public void setAccesorios(List<AccesorioEntity> accesorios) {
		this.accesorios = accesorios;
	}

	public List<HistorialInventarioEntity> getHistoriales() {
		return historiales;
	}

	public void setHistoriales(List<HistorialInventarioEntity> historiales) {
		this.historiales = historiales;
	}
}
