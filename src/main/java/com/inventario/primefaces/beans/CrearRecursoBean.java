package com.inventario.primefaces.beans;

import com.inventario.jpa.data.*;
import com.inventario.spring.service.CrearRecursoServicio;

import com.inventario.util.constante.Constantes;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean
@ViewScoped
public class CrearRecursoBean {

	/*ATRIBUTOS*/
	@ManagedProperty("#{crearRecursoServicio}")
	private CrearRecursoServicio crearRecursoServicio;

	private EquipoEntity equipo;
	private AccesorioEntity accesorio;

	private String opcion = "0";
	private String observacion;
	private String incidencia;
	private Boolean creacion = false;

	private Date fechaActual = new Date();

	private List<MarcaEntity> marcas;
	private MarcaEntity marca;

	private List<ModeloEntity> modelos;
	private ModeloEntity modelo;

	private List<CategoriaEntity> categorias;
	private CategoriaEntity categoria;

	private EstadoEntity estado;
	private HistorialInventarioEntity historial;

	/*METODOS*/
	@PostConstruct
	public void init(){

		cambiarRecursoOpcion();
	}

	public void cambiarRecursoOpcion() {

		inicializarListas();
		marcas = cargarMarcas();

		if(opcion.equals("1")){
			categorias = cargarCategorias();
		}

	}

	public List<MarcaEntity> cargarMarcas() {

		List<MarcaEntity> results = crearRecursoServicio.cargarMarcas();

		return results;
	}

	public void cargarModelos() {

		if(!marca.getNombre().equals("")){

			//Validar existencia de marca
			if (crearRecursoServicio.obtenerMarcaPorNombre(marca.getNombre()) != null){

				setMarca(crearRecursoServicio.obtenerMarcaPorNombre(marca.getNombre()));
				modelos = crearRecursoServicio.cargarModelos(getMarca());

			}else{
				marca.setId(0);
				modelos = new ArrayList<ModeloEntity>();
			}

		}else{
			modelos = new ArrayList<ModeloEntity>();
		}

	}

	public List<CategoriaEntity> cargarCategorias() {

		List<CategoriaEntity> results = crearRecursoServicio.cargarCategorias("accesorio");

		return results;
	}

	public String bt_crearRecurso(){

		try {

			if (opcion.equals("0")) {
				if (equipo.getFechaCompra() == null) {
					equipo.setFechaCompra(fechaActual);
				}
			} else {
				if (accesorio.getFechaCompra() == null) {
					accesorio.setFechaCompra(fechaActual);
				}

				//Validar existencia de categoria
				if (crearRecursoServicio.obtenerCategoriaPorNombre(categoria.getNombre(), "accesorio") != null) {
					setCategoria(crearRecursoServicio.obtenerCategoriaPorNombre(categoria.getNombre(), "accesorio"));
				} else {
					categoria.setId(0);
				}

			}

			estado = crearRecursoServicio.obtenerEstado(Constantes.D_ID_ESTADO_CREACION);
			crearHistorial();

			//Validar existencia de modelo
			if (crearRecursoServicio.obtenerModeloPorNombre(modelo.getNombre(), marca.getId()) != null) {
				setModelo(crearRecursoServicio.obtenerModeloPorNombre(modelo.getNombre(), marca.getId()));
			} else {
				modelo.setId(0);
			}

			creacion = crearRecursoServicio.crearRecurso(marca, modelo, categoria, estado, historial, equipo, accesorio, opcion);

			if (creacion == true) {

				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "EXITO! El recurso se ingreso satisfactoriamente", null));

				return "Exito";

			}else {
				FacesContext.getCurrentInstance().addMessage("mensajesError", new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR! No se pudo ingresar el recurso", null));
				RequestContext.getCurrentInstance().update("mensajesError");

				inicializarListas();
				return "";

			}

		}catch (Exception e){
			FacesContext.getCurrentInstance().addMessage("mensajesError", new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR! No se pudo ingresar el recurso", null));
			RequestContext.getCurrentInstance().update("mensajesError");

			inicializarListas();
			return "";

		}

	}

	public void crearHistorial(){

		historial.setFechaGestion(fechaActual);
		historial.setResponsableSoporte("12345678");  //USUARIO DE LA SESSION
		historial.setCategoria(crearRecursoServicio.obtenerCategoriaHistorial(Constantes.D_CAT_HISTORIAL_CREACION));

		if(!incidencia.equals(""))
			historial.setIdIncidencia(incidencia);

		if(!observacion.equals("")){
			historial.setDescripcion(observacion);
		}else{

			if(opcion.equals("0")) {
				historial.setDescripcion(Constantes.D_DESC_HISTORIAL_CREACION_EQ + equipo.getNombre());
			}else{
				historial.setDescripcion(Constantes.D_DESC_HISTORIAL_CREACION_ACC + accesorio.getNombre());
			}
		}

	}


	public void inicializarListas(){

		equipo 	   = new EquipoEntity();
		accesorio  = new AccesorioEntity();

		marcas     = new ArrayList<MarcaEntity>();
		modelos    = new ArrayList<ModeloEntity>();
		categorias = new ArrayList<CategoriaEntity>();

		marca 	  = new MarcaEntity();
		modelo 	  = new ModeloEntity();
		categoria = new CategoriaEntity();

		estado 	   = new EstadoEntity();
		historial  = new HistorialInventarioEntity();
	}

	public String bt_cancelar(){

		return "Cancelar";
	}


	/*GET & SET*/
	public CrearRecursoServicio getCrearRecursoServicio() {
		return crearRecursoServicio;
	}

	public void setCrearRecursoServicio(CrearRecursoServicio crearRecursoServicio) {
		this.crearRecursoServicio = crearRecursoServicio;
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

	public String getOpcion() {
		return opcion;
	}

	public void setOpcion(String opcion) {
		this.opcion = opcion;
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

	public Boolean getCreacion() {
		return creacion;
	}

	public void setCreacion(Boolean creacion) {
		this.creacion = creacion;
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

	public CategoriaEntity getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaEntity categoria) {
		this.categoria = categoria;
	}

	public List<CategoriaEntity> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<CategoriaEntity> categorias) {
		this.categorias = categorias;
	}

	public EstadoEntity getEstado() {
		return estado;
	}

	public void setEstado(EstadoEntity estado) {
		this.estado = estado;
	}

	public HistorialInventarioEntity getHistorial() {
		return historial;
	}

	public void setHistorial(HistorialInventarioEntity historial) {
		this.historial = historial;
	}

}

