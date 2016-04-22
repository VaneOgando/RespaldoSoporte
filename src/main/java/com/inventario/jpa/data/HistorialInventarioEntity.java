package com.inventario.jpa.data;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="HISTORIALINVENTARIO")

@NamedQueries(value={

		@NamedQuery(name = "HQL_HISTORIAL_POR_EQUIPO",
				query = "SELECT h FROM HistorialInventarioEntity h JOIN h.equipo e JOIN h.categoria ca " +
						"WHERE e.numSerie = :numSerie AND ca.tipoCategoria = 'historial' " +
						"ORDER BY h.id DESC"),

		@NamedQuery(name = "HQL_HISTORIAL_POR_ACCESORIO",
				query = "SELECT h FROM HistorialInventarioEntity h JOIN h.accesorio a JOIN h.categoria ca " +
						"WHERE a.id = :idAccesorio AND ca.tipoCategoria = 'historial' " +
						"ORDER BY h.id DESC")


})

@NamedNativeQueries({
		@NamedNativeQuery(name = "SQL_HISTORIAL_USUARIO_ASIGNADO_EQUIPO", query="Select usuario FROM (SELECT h.usuarioAsig as usuario FROM HistorialInventario h " +
																									"WHERE h.fkequipo = ? AND h.FKCATEGORIA = ? ORDER BY h.IDHISTORIALINV DESC) " +
																				"where rownum = 1"),

		@NamedNativeQuery(name = "SQL_HISTORIAL_USUARIO_ASIGNADO_ACCESORIO", query="Select usuario FROM (SELECT h.usuarioAsig as usuario FROM HistorialInventario h " +
																									"WHERE h.fkaccesorio = ? AND h.FKCATEGORIA = ? ORDER BY h.IDHISTORIALINV DESC) " +
																					"where rownum = 1")

})

public class HistorialInventarioEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HISTINV_SEQ")
	@SequenceGenerator(name="HISTINV_SEQ", sequenceName="HISTINV_SEQ", allocationSize = 1)
	@Column(name = "IDHISTORIALINV")
	private int id;
	@Column(name = "FECHAGESTION")
	@Temporal(TemporalType.DATE)
	private Date fechaGestion;
	@Column(name = "DESCRIPCION")
	private String descripcion;
	@Column(name = "IDINCIDENCIA")
	private String idIncidencia;
	@Column(name = "RESPSOPORTE")
	private String responsableSoporte;
	@Column(name = "USUARIOASIG")
	private String usuarioAsignado;

	@ManyToOne
	@JoinColumn(name = "FKCATEGORIA")
	private CategoriaEntity categoria;

	@ManyToOne
	@JoinColumn(name = "FKEQUIPO")
	private EquipoEntity equipo;


	@ManyToOne
	@JoinColumn(name = "FKACCESORIO")
	private AccesorioEntity accesorio;


	/*GET AND SET*/

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getFechaGestion() {
		return fechaGestion;
	}

	public void setFechaGestion(Date fechaGestion) {
		this.fechaGestion = fechaGestion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getIdIncidencia() {
		return idIncidencia;
	}

	public void setIdIncidencia(String idIncidencia) {
		this.idIncidencia = idIncidencia;
	}

	public String getResponsableSoporte() {
		return responsableSoporte;
	}

	public void setResponsableSoporte(String responsableSoporte) {
		this.responsableSoporte = responsableSoporte;
	}

	public String getUsuarioAsignado() {
		return usuarioAsignado;
	}

	public void setUsuarioAsignado(String usuarioAsignado) {
		this.usuarioAsignado = usuarioAsignado;
	}

	public CategoriaEntity getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaEntity categoria) {
		this.categoria = categoria;
	}

	public EquipoEntity getEquipo() {
		return equipo;
	}

	public void setEquipo(EquipoEntity equipo) {
		this.equipo = equipo;
	}

	public AccesorioEntity getAccesorio() {
		return accesorio;
	}

	public void setAccesorio(AccesorioEntity accesorio) {
		this.accesorio = accesorio;
	}
}
