package com.inventario.primefaces.beans;

import com.inventario.jpa.data.*;
import com.inventario.spring.service.DetalleEquipoServicio;
import com.inventario.spring.service.ModificarEquipoServicio;
import com.inventario.util.constante.Constantes;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class ModificarEquipoBean {

	/*ATRIBUTOS*/
	@ManagedProperty("#{modificarEquipoServicio}")
	private ModificarEquipoServicio modificarEquipoServicio;

	private EquipoEntity equipo = new EquipoEntity();
	private HistorialInventarioEntity historial = new HistorialInventarioEntity();

	private String observacion;
	private String incidencia;

	private Boolean modificacion = false;

	private Date fechaActual = new Date();

	private List<MarcaEntity> marcas = new ArrayList<MarcaEntity>();
	private MarcaEntity marca = new MarcaEntity();

	private List<ModeloEntity> modelos  = new ArrayList<ModeloEntity>();
	private ModeloEntity modelo = new ModeloEntity();


	/*METODOS*/

	@PostConstruct
	public void cargarEquipo() {

		//Obtener parametro
		equipo.setNumSerie(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("numSerie"));

		equipo = modificarEquipoServicio.obtenerEquipo(equipo.getNumSerie());

			marcas = modificarEquipoServicio.cargarMarcas();
		marca = equipo.getModelo().getMarca();

		modelos = modificarEquipoServicio.cargarModelos(equipo.getModelo().getMarca());
		modelo = equipo.getModelo();

	}

	public void cargarModelos() {

		modelo = new ModeloEntity();

		if(!marca.getNombre().equals("")){

			//Validar existencia de marca
			if (modificarEquipoServicio.obtenerMarcaPorNombre(marca.getNombre()) != null){

				setMarca(modificarEquipoServicio.obtenerMarcaPorNombre(marca.getNombre()));
				modelos = modificarEquipoServicio.cargarModelos(getMarca());

			}else{
				marca.setId(0);
				modelos = new ArrayList<ModeloEntity>();
			}

		}else{
			modelos = new ArrayList<ModeloEntity>();
		}

	}


	public String bt_modificar(){

		try {

			crearHistorial();

			//Validar existencia de modelo
			if (modificarEquipoServicio.obtenerModeloPorNombre(modelo.getNombre(), marca.getId()) != null) {
				setModelo(modificarEquipoServicio.obtenerModeloPorNombre(modelo.getNombre(), marca.getId()));
			} else {
				modelo.setId(0);
			}

			modificacion = modificarEquipoServicio.modificarEquipo(marca, modelo, historial, equipo);

			if (modificacion == true) {

				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "EXITO! El recurso se modifico satisfactoriamente", null));

				return "Exito";

			}else {
				FacesContext.getCurrentInstance().addMessage("mensajesError", new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR! No se pudo modificar el recurso", null));
				RequestContext.getCurrentInstance().update("mensajesError");

				return "";

			}

		}catch (Exception e){
			FacesContext.getCurrentInstance().addMessage("mensajesError", new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR! No se pudo modificar el recurso", null));
			RequestContext.getCurrentInstance().update("mensajesError");

			return "";

		}

	}

	public void crearHistorial(){

		historial.setFechaGestion(fechaActual);
		historial.setResponsableSoporte("12345678");  //USUARIO DE LA SESSION
		historial.setDescripcion(observacion);
		historial.setCategoria(modificarEquipoServicio.obtenerCategoriaHistorial(Constantes.D_CAT_HISTORIAL_MODIFICACION));

		if(!incidencia.equals(""))
			historial.setIdIncidencia(incidencia);

	}

	public String bt_cancelar(){

		return "detalleEquipo.xhtml?faces-redirect=true&numSerie=" + equipo.getNumSerie();
	}

	/*GET & SET*/

	public ModificarEquipoServicio getModificarEquipoServicio() {
		return modificarEquipoServicio;
	}

	public void setModificarEquipoServicio(ModificarEquipoServicio modificarEquipoServicio) {
		this.modificarEquipoServicio = modificarEquipoServicio;
	}

	public EquipoEntity getEquipo() {
		return equipo;
	}

	public void setEquipo(EquipoEntity equipo) {
		this.equipo = equipo;
	}

	public HistorialInventarioEntity getHistorial() {
		return historial;
	}

	public void setHistorial(HistorialInventarioEntity historial) {
		this.historial = historial;
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

	public Boolean getModificacion() {
		return modificacion;
	}

	public void setModificacion(Boolean modificacion) {
		this.modificacion = modificacion;
	}

	public Date getFechaActual() {
		return fechaActual;
	}

	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	public List<MarcaEntity> getMarcas() {
		return marcas;
	}

	public void setMarcas(List<MarcaEntity> marcas) {
		this.marcas = marcas;
	}

	public MarcaEntity getMarca() {
		return marca;
	}

	public void setMarca(MarcaEntity marca) {
		this.marca = marca;
	}

	public List<ModeloEntity> getModelos() {
		return modelos;
	}

	public void setModelos(List<ModeloEntity> modelos) {
		this.modelos = modelos;
	}

	public ModeloEntity getModelo() {
		return modelo;
	}

	public void setModelo(ModeloEntity modelo) {
		this.modelo = modelo;
	}
}

