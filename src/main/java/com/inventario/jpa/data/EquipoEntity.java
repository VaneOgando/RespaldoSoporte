package com.inventario.jpa.data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="EQUIPO")

@NamedQueries(value= {

		@NamedQuery(name = "HQL_EQUIPO",
				query = "SELECT e FROM EquipoEntity e JOIN e.estado es JOIN e.modelo mo JOIN mo.marca ma " +
						"WHERE (:idEstado is null or :idEstado = '0' or es.id = :idEstado) AND (:idModelo is null or :idModelo = '0' or mo.id = :idModelo)" +
						"AND (:idMarca is null or :idMarca = '0' or ma.id = :idMarca)"),

		@NamedQuery(name = "HQL_EQUIPO_POR_NUMSERIE",
				query = "SELECT e FROM EquipoEntity e JOIN e.estado es JOIN e.modelo mo JOIN mo.marca ma " +
						"WHERE e.numSerie = :numSerie")


})

public class EquipoEntity{
	@Id
	@Column(name = "NUMSERIE")
	private String numSerie;
	@Column(name = "EQUIPO")
	private String nombre;
	@Column(name = "PROCESADOR")
	private String procesador;
	@Column(name = "MEMORIARAM")
	private String memoriaRam;
	@Column(name = "DISCODURO")
	private String discoDuro;
	@Column(name = "SISTOPERATIVO")
	private String sistOperativo;
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

	@OneToMany(mappedBy = "equipo")
	private List<HistorialInventarioEntity> historiales;



	/*GET AND SET*/

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

	public String getProcesador() {
		return procesador;
	}

	public void setProcesador(String procesador) {
		this.procesador = procesador;
	}

	public String getMemoriaRam() {
		return memoriaRam;
	}

	public void setMemoriaRam(String memoriaRam) {
		this.memoriaRam = memoriaRam;
	}

	public String getDiscoDuro() {
		return discoDuro;
	}

	public void setDiscoDuro(String discoDuro) {
		this.discoDuro = discoDuro;
	}

	public String getSistOperativo() {
		return sistOperativo;
	}

	public void setSistOperativo(String sistOperativo) {
		this.sistOperativo = sistOperativo;
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

	public List<HistorialInventarioEntity> getHistoriales() {
		return historiales;
	}

	public void setHistoriales(List<HistorialInventarioEntity> historiales) {
		this.historiales = historiales;
	}
}
