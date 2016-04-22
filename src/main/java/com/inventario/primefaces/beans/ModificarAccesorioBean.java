package com.inventario.primefaces.beans;

import com.inventario.jpa.data.*;
import com.inventario.spring.service.ModificarAccesorioServicio;
import com.inventario.util.constante.Constantes;

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
public class ModificarAccesorioBean {

	/*ATRIBUTOS*/
	@ManagedProperty("#{modificarAccesorioServicio}")
	private ModificarAccesorioServicio modificarAccesorioServicio;

	private AccesorioEntity accesorio = new AccesorioEntity();
	private HistorialInventarioEntity historial = new HistorialInventarioEntity();

	private String observacion;
	private String incidencia;

	private Boolean modificacion = false;

	private Date fechaActual = new Date();

	private List<MarcaEntity> marcas = new ArrayList<MarcaEntity>();
	private MarcaEntity marca = new MarcaEntity();

	private List<ModeloEntity> modelos  = new ArrayList<ModeloEntity>();
	private ModeloEntity modelo = new ModeloEntity();

	private List<CategoriaEntity> categorias  = new ArrayList<CategoriaEntity>();
	private CategoriaEntity categoria = new CategoriaEntity();



	/*METODOS*/

	@PostConstruct
	public void cargarAccesorio() {

		//Obtener parametro
		accesorio.setId(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id")));

		accesorio = modificarAccesorioServicio.obtenerAccesorio(accesorio.getId());

		marcas = modificarAccesorioServicio.cargarMarcas();
		marca = accesorio.getModelo().getMarca();

		modelos = modificarAccesorioServicio.cargarModelos(accesorio.getModelo().getMarca());
		modelo = accesorio.getModelo();

		categorias = modificarAccesorioServicio.cargarCategorias("accesorio");
		categoria = accesorio.getCategoria();

	}

	public void cargarModelos() {

		modelo = new ModeloEntity();

		if(!marca.getNombre().equals("")){

			//Validar existencia de marca
			if (modificarAccesorioServicio.obtenerMarcaPorNombre(marca.getNombre()) != null){

				setMarca(modificarAccesorioServicio.obtenerMarcaPorNombre(marca.getNombre()));
				modelos = modificarAccesorioServicio.cargarModelos(getMarca());

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
			if (modificarAccesorioServicio.obtenerModeloPorNombre(modelo.getNombre(), marca.getId()) != null) {
				setModelo(modificarAccesorioServicio.obtenerModeloPorNombre(modelo.getNombre(), marca.getId()));
			} else {
				modelo.setId(0);
			}

			//Validar existencia de categoria
			if (modificarAccesorioServicio.obtenerCategoriaPorNombre(categoria.getNombre(), "accesorio") != null) {
				setCategoria(modificarAccesorioServicio.obtenerCategoriaPorNombre(categoria.getNombre(), "accesorio"));
			} else {
				categoria.setId(0);
			}

			modificacion = modificarAccesorioServicio.modificarAccesorio(marca, modelo, categoria, historial, accesorio);

			if (modificacion == true) {

				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "EXITO! El recurso se modifico satisfactoriamente", null));

				return "Exito";

			}else {
				FacesContext.getCurrentInstance().addMessage("mensajesError", new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR! No se pudo modificar el recurso", null));
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

				return "";

			}

		}catch (Exception e){
			FacesContext.getCurrentInstance().addMessage("mensajesError", new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR! No se pudo modificar el recurso", null));
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

			return "";

		}

	}

	public void crearHistorial(){

		historial.setFechaGestion(fechaActual);
		historial.setResponsableSoporte("12345678");  //USUARIO DE LA SESSION
		historial.setDescripcion(observacion);
		historial.setCategoria(modificarAccesorioServicio.obtenerCategoriaHistorial(Constantes.D_CAT_HISTORIAL_MODIFICACION));

		if(!incidencia.equals(""))
			historial.setIdIncidencia(incidencia);

	}

	public String bt_cancelar(){

		return "detalleAccesorio.xhtml?faces-redirect=true&id=" + accesorio.getId();
	}


	/*GET & SET*/
	public ModificarAccesorioServicio getModificarAccesorioServicio() {
		return modificarAccesorioServicio;
	}

	public void setModificarAccesorioServicio(ModificarAccesorioServicio modificarAccesorioServicio) {
		this.modificarAccesorioServicio = modificarAccesorioServicio;
	}

	public AccesorioEntity getAccesorio() {
		return accesorio;
	}

	public void setAccesorio(AccesorioEntity accesorio) {
		this.accesorio = accesorio;
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

	public List<CategoriaEntity> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<CategoriaEntity> categorias) {
		this.categorias = categorias;
	}

	public CategoriaEntity getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaEntity categoria) {
		this.categoria = categoria;
	}
}

