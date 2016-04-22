package com.inventario.jpa.data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name="ACCESORIO")

@NamedQueries(value={

		@NamedQuery(name = "HQL_ACCESORIO",
				query = "SELECT a FROM AccesorioEntity a JOIN a.estado es JOIN a.modelo mo JOIN mo.marca ma JOIN a.categoria ca " +
						"WHERE (:idEstado is null or :idEstado = '0' or es.id = :idEstado) " +
						"AND (:idModelo is null or :idModelo = '0' or mo.id = :idModelo)" +
						"AND (:idMarca is null or :idMarca = '0' or ma.id = :idMarca)" +
						"AND (:idCategoria is null or :idCategoria = '0' or ca.id = :idCategoria)"),

		@NamedQuery(name = "HQL_ACCESORIO_POR_ID",
				query = "SELECT a FROM AccesorioEntity a JOIN a.estado es JOIN a.modelo mo JOIN mo.marca ma JOIN a.categoria ca " +
						"WHERE a.id = :idAccesorio")

})

public class AccesorioEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCESORIO_SEQ")
	@SequenceGenerator(name="ACCESORIO_SEQ", sequenceName="ACCESORIO_SEQ", allocationSize = 1)
	@Column(name = "IDACCESORIO")
	private int id;
	@Column(name = "NUMSERIE")
	private String numSerie;
	@Column(name = "ACCESORIO")
	private String nombre;
	@Column(name = "CARACTERISTICAS")
	private String caracteristicas;
	@Column(name = "FECHACOMPRA")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaCompra;

	@ManyToOne
	@JoinColumn(name = "FKESTADO")
	private EstadoEntity estado;

	@ManyToOne
	@JoinColumn(name = "FKMODELO")
	private ModeloEntity modelo;

	@ManyToOne
	@JoinColumn(name = "FKCATEGORIA")
	private CategoriaEntity categoria;

	@OneToMany(mappedBy = "accesorio")
	private List<HistorialInventarioEntity> historiales;


	/*GET AND SET*/

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumSerie() {
		return numSerie;
	}

	public void setNumSerie(String numSerie) {
		this.numSerie = numSerie;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCaracteristicas() {
		return caracteristicas;
	}

	public void setCaracteristicas(String caracteristicas) {
		this.caracteristicas = caracteristicas;
	}

	public Date getFechaCompra() {
		return fechaCompra;
	}

	public void setFechaCompra(Date fechaCompra) {
		this.fechaCompra = fechaCompra;
	}

	public EstadoEntity getEstado() {
		return estado;
	}

	public void setEstado(EstadoEntity estado) {
		this.estado = estado;
	}

	public ModeloEntity getModelo() {
		return modelo;
	}

	public void setModelo(ModeloEntity modelo) {
		this.modelo = modelo;
	}

	public CategoriaEntity getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaEntity categoria) {
		this.categoria = categoria;
	}


	public List<HistorialInventarioEntity> getHistoriales() {
		return historiales;
	}

	public void setHistoriales(List<HistorialInventarioEntity> historiales) {
		this.historiales = historiales;
	}

}
