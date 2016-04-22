package com.inventario.primefaces.beans;

import com.inventario.jpa.data.*;
import com.inventario.spring.service.ConsultarInventarioServicio;
import com.inventario.spring.service.DetalleEquipoServicio;
import com.inventario.util.constante.Constantes;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean
@ViewScoped
public class DetalleEquipoBean {

	/*ATRIBUTOS*/
	@ManagedProperty("#{detalleEquipoServicio}")
	private DetalleEquipoServicio detalleEquipoServicio;

	private EquipoEntity equipo = new EquipoEntity();

	private EstadoEntity estadoACambiar = new EstadoEntity();
	private HistorialInventarioEntity historialCambioEstado = new HistorialInventarioEntity();

	private List<EstadoEntity> estados = new ArrayList<EstadoEntity>();
	private List<HistorialInventarioEntity> historial = new ArrayList<HistorialInventarioEntity>();
	private List<HistorialInventarioEntity> itemsBuscados;

	private String usuario = null;
	private String observacion;
	private String incidencia;

	private Date fechaActual = new Date();

	private Boolean cambiarEstado = false;
	private Boolean asignado = false;
	private Boolean disponible = false;


	/*METODOS*/
	public void cargarDetalleEquipo() {

		itemsBuscados = null;

		equipo 	  = detalleEquipoServicio.obtenerEquipo(equipo.getNumSerie());
		historial = detalleEquipoServicio.obtenerHistorialEquipo(equipo.getNumSerie());

		//Equipo asignado, posee usuario
		if (equipo.getEstado().getId() == Constantes.D_ID_ESTADO_ASIGNACION){
			asignado = true;
			usuario = detalleEquipoServicio.obtenerUsuarioAsignado(equipo.getNumSerie());
		}

		//Equipo disponible para asignar
		if (equipo.getEstado().getId() == Constantes.D_ID_ESTADO_CREACION){
			disponible = true;
		}

	}

	public String bt_modificarEquipo(){

		if (equipo.getEstado().getId() == Constantes.D_ID_ESTADO_ELIMINADO){

			FacesContext.getCurrentInstance().addMessage("mensajesError", new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR! Este recurso se encuentra fuera del inventario", null));
			return "";

		}else{
			return "modificarEquipo.xhtml";
		}

	}

	public void validarEstado(){

		if (equipo.getEstado().getId() == Constantes.D_ID_ESTADO_ELIMINADO) {
			FacesContext.getCurrentInstance().addMessage("mensajesError", new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR! Este recurso se encuentra fuera del inventario", null));
		}else if (equipo.getEstado().getId() == Constantes.D_ID_ESTADO_ASIGNACION) {
			FacesContext.getCurrentInstance().addMessage("mensajesError", new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR! Este recurso se encuentra asignado, por favor realizar su respectiva devolución", null));

		} else {
			estados	= detalleEquipoServicio.obtenerCambioEstado(equipo.getEstado());
			RequestContext.getCurrentInstance().execute("PF('dialogoCambioEstado').show()");
		}

	}

	public void bt_cambiarEstado(){

		try {

			estadoACambiar = detalleEquipoServicio.obtenerEstado(estadoACambiar.getId());
			crearHistorialCambioEstado();

			cambiarEstado = detalleEquipoServicio.cambiarEstado(equipo, estadoACambiar, historialCambioEstado);

			if (cambiarEstado == true) {
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
				FacesContext.getCurrentInstance().addMessage("mensajesError", new FacesMessage(FacesMessage.SEVERITY_INFO, "EXITO! El estado se cambio satisfactoriamente", null));
				FacesContext.getCurrentInstance().getExternalContext().redirect("detalleEquipo.xhtml?numSerie=" + equipo.getNumSerie());

			} else {
				FacesContext.getCurrentInstance().addMessage("mensajesError", new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR! El estado no se pudo cambiar", null));
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("mensajesError", new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR! El estado no se pudo cambiar", null));
		}finally {
			RequestContext.getCurrentInstance().execute("PF('dialogoCambioEstado').hide()");
			RequestContext.getCurrentInstance().update("mensajesError");

		}

	}

	public void confirmarEliminacion(){

		boolean eliminar = false;
		if (estadoACambiar.getId() == Constantes.D_ID_ESTADO_ELIMINADO){

			RequestContext.getCurrentInstance().execute("if (confirm('seguro?') return true; )");
		}
	}

	public void crearHistorialCambioEstado(){

		historialCambioEstado.setFechaGestion(fechaActual);
		historialCambioEstado.setDescripcion(observacion);
		historialCambioEstado.setResponsableSoporte("12345678");  //USUARIO DE LA SESSION

		if(!incidencia.equals(""))
			historialCambioEstado.setIdIncidencia(incidencia);

		if (estadoACambiar.getId() == Constantes.D_ID_ESTADO_ELIMINADO){
			historialCambioEstado.setCategoria(detalleEquipoServicio.obtenerCategoriaHistorial(Constantes.D_CAT_HISTORIAL_ELIMINACION));
		}else {
			historialCambioEstado.setCategoria(detalleEquipoServicio.obtenerCategoriaHistorial(Constantes.D_CAT_HISTORIAL_CAMBIO_ESTADO));
		}

	}


	/*GET & SET*/
	public DetalleEquipoServicio getDetalleEquipoServicio() {
		return detalleEquipoServicio;
	}

	public void setDetalleEquipoServicio(DetalleEquipoServicio detalleEquipoServicio) {
		this.detalleEquipoServicio = detalleEquipoServicio;
	}

	public EquipoEntity getEquipo() {
		return equipo;
	}

	public void setEquipo(EquipoEntity equipo) {
		this.equipo = equipo;
	}

	public List<HistorialInventarioEntity> getHistorial() {
		return historial;
	}

	public void setHistorial(List<HistorialInventarioEntity> historial) {
		this.historial = historial;
	}

	public List<HistorialInventarioEntity> getItemsBuscados() {
		return itemsBuscados;
	}

	public void setItemsBuscados(List<HistorialInventarioEntity> itemsBuscados) {
		this.itemsBuscados = itemsBuscados;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public List<EstadoEntity> getEstados() {
		return estados;
	}

	public void setEstados(List<EstadoEntity> estados) {
		this.estados = estados;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getIncidencia() {
		return incidencia;
	}

	public void setIncidencia(String incidencia) {
		this.incidencia = incidencia;
	}

	public Date getFechaActual() {
		return fechaActual;
	}

	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	public EstadoEntity getEstadoACambiar() {
		return estadoACambiar;
	}

	public void setEstadoACambiar(EstadoEntity estadoACambiar) {
		this.estadoACambiar = estadoACambiar;
	}

	public HistorialInventarioEntity getHistorialCambioEstado() {
		return historialCambioEstado;
	}

	public void setHistorialCambioEstado(HistorialInventarioEntity historialCambioEstado) {
		this.historialCambioEstado = historialCambioEstado;
	}

	public Boolean getCambiarEstado() {
		return cambiarEstado;
	}

	public void setCambiarEstado(Boolean cambiarEstado) {
		this.cambiarEstado = cambiarEstado;
	}

	public Boolean getAsignado() {
		return asignado;
	}

	public void setAsignado(Boolean asignado) {
		this.asignado = asignado;
	}

	public Boolean getDisponible() {
		return disponible;
	}

	public void setDisponible(Boolean disponible) {
		this.disponible = disponible;
	}
}

