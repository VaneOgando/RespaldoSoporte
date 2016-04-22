package com.inventario.primefaces.beans;

import com.inventario.jpa.data.*;
//import com.inventario.spring.service.GenerarReporteServicio;
import com.inventario.spring.service.GestionarRecursoServicio;
import com.inventario.util.constante.Constantes;
//import net.sf.jasperreports.engine.*;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import org.apache.commons.collections.map.HashedMap;

import org.primefaces.context.RequestContext;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

@ManagedBean
@ViewScoped
public class GestionarRecursoBean {

	/*ATRIBUTOS*/
	@ManagedProperty("#{gestionarRecursoServicio}")
	private GestionarRecursoServicio gestionarRecursoServicio;

	//@ManagedProperty("#{generarReporteServicio}")
	//private GenerarReporteServicio generarReporteServicio;

	private FacesContext context = FacesContext.getCurrentInstance();

	private HistorialInventarioEntity historial;
	private EquipoEntity equipo;
	private EstadoEntity estado;
	private Date fechaActual = new Date();

	private String opcionGestion;
	private String opcionRecurso;
	private String opcionDatatable;

	private Boolean gestion = false;

	private List<AccesorioEntity> accesoriosGestion;
	private List<String> accesoriosSeleccion;

	private List<Object> items = new ArrayList<Object>();
	private List<Object> itemsBuscados = null;
	private Object itemSeleccionado = null;

	//private JasperPrint reporteDescarga;


	/*METODOS*/
	@PostConstruct
	public void init(){

		bt_limpiarGestion();

		if (context.isPostback()){

			opcionGestion = context.getExternalContext().getRequestParameterMap().get("opcionGestion");
			opcionRecurso = context.getExternalContext().getRequestParameterMap().get("opcionRecurso");

			if ( opcionRecurso.equals("E") ){

				equipo = gestionarRecursoServicio.obtenerEquipo( context.getExternalContext().getRequestParameterMap().get("numSerie") );

				if ( opcionGestion.equals("D") ){
					historial.setUsuarioAsignado( gestionarRecursoServicio.buscarUsuarioAsignadoE( equipo ) );
				}

			}else if (opcionRecurso.equals("A")){

				accesoriosGestion.add( gestionarRecursoServicio.obtenerAccesorio( Integer.parseInt( context.getExternalContext().getRequestParameterMap().get("id") ) ) );

				if (opcionGestion.equals("D")){
					historial.setUsuarioAsignado( gestionarRecursoServicio.buscarUsuarioAsignadoA( accesoriosGestion.get(0) ) );
				}
			}

		}else {
			opcionGestion = "A";
		}

	}

	public void bt_limpiarGestion(){

		historial = new HistorialInventarioEntity();
		equipo = new EquipoEntity();

		accesoriosGestion = new ArrayList<AccesorioEntity>();
		accesoriosSeleccion = new ArrayList<String>();

	}

	public void desplegarDialogo(){

		itemsBuscados = null;
		itemSeleccionado = null;

		RequestContext.getCurrentInstance().execute("PF('itemTabla').clearFilters()");
		RequestContext.getCurrentInstance().update("datatable");
		RequestContext.getCurrentInstance().execute("PF('dialogoRecursoGestion').show()");

	}


	public void buscarUsuarios(){

		opcionDatatable = "U";
		items = new ArrayList<Object>();

		items.add(new Empleado("87654321", "Vanessa"));
		items.add(new Empleado("11111111", "Vanessa2"));

		desplegarDialogo();
	}


	public void buscarEquipos(){
		opcionDatatable = "E";

		//Equipos disponibles
		if (opcionGestion.equals("A")){
			items = gestionarRecursoServicio.buscarEquipos(Constantes.D_ID_ESTADO_CREACION);
		}else{ //Equipos asignados

			items = gestionarRecursoServicio.buscarEquipos(Constantes.D_ID_ESTADO_ASIGNACION);

			//Usuario especifico
			if (historial.getUsuarioAsignado() != null){

				int i = 0;

				while (i < items.size()){

					String usuario = gestionarRecursoServicio.buscarUsuarioAsignadoE((EquipoEntity) items.get(i));

					if(!historial.getUsuarioAsignado().equals(usuario)){
						items.remove(i);
						i--;
					}

					i++;
				}
			}

		}

		desplegarDialogo();
	}

	public void agregarAccesorios(){
		opcionDatatable = "A";

		//Accesorios disponibles
		if (opcionGestion.equals("A")){
			items = gestionarRecursoServicio.buscarAccesorios(Constantes.D_ID_ESTADO_CREACION);

		}else { //Accesorios asignados

			items = gestionarRecursoServicio.buscarAccesorios(Constantes.D_ID_ESTADO_ASIGNACION);

			//Usuario especifico
			if (historial.getUsuarioAsignado() != null) {

				int i = 0;

				while (i < items.size()) {

					String usuario = gestionarRecursoServicio.buscarUsuarioAsignadoA((AccesorioEntity) items.get(i));

					if (!historial.getUsuarioAsignado().equals(usuario)) {
						items.remove(i);
						i--;
					}

					i++;
				}
			}
		}

		eliminarItemsGestionados();
		desplegarDialogo();
	}

	public void eliminarItemsGestionados(){

		int i = 0;
		while (i < accesoriosGestion.size()) {

			int j = 0;
			while (j < items.size()){

				if( ((AccesorioEntity) items.get(j)).getId() == accesoriosGestion.get(i).getId() ){
					items.remove(j);

					j = items.size();
				}
				j++;
			}

			i++;
		}


	}

	public void eliminarAccesorios(){

		int i = 0;
		while (i < accesoriosSeleccion.size()) {

			int j = 0;
			while (j < accesoriosGestion.size()){

				if(accesoriosGestion.get(j).getId() == Integer.parseInt( accesoriosSeleccion.get(i) )){
					accesoriosGestion.remove(j);

					j = accesoriosGestion.size();
				}
				j++;
			}

			i++;
		}

	}

	public void bt_seleccionarRecurso(){

		if (itemSeleccionado != null ) {

			if (opcionDatatable.equals("U")){

				if (opcionGestion.equals("D")){

					equipo = new EquipoEntity();
					accesoriosGestion = new ArrayList<AccesorioEntity>();
					accesoriosSeleccion = new ArrayList<String>();
				}

				historial.setUsuarioAsignado( ((Empleado) itemSeleccionado).getId());

			}else if (opcionDatatable.equals("E")){

				equipo = (EquipoEntity) itemSeleccionado;

				if (opcionGestion.equals("D")){
					historial.setUsuarioAsignado(gestionarRecursoServicio.buscarUsuarioAsignadoE( equipo ));
				}

			}else if(opcionDatatable.equals("A")){

				if (opcionGestion.equals("D")){
					historial.setUsuarioAsignado(gestionarRecursoServicio.buscarUsuarioAsignadoA((AccesorioEntity) itemSeleccionado));
				}

				accesoriosGestion.add( (AccesorioEntity) itemSeleccionado );

			}

			RequestContext.getCurrentInstance().update("gestionarRecurso:formularioGestion");
			RequestContext.getCurrentInstance().execute("PF('dialogoRecursoGestion').hide()");

		}else{
			if(items.size() > 0) {
				FacesContext.getCurrentInstance().addMessage("mensajesDialogo", new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR! Seleccione un item", null));
				RequestContext.getCurrentInstance().update("datatable:mensajesDialogo");
			}else {
				RequestContext.getCurrentInstance().execute("PF('dialogoRecursoGestion').hide()");
			}
		}

	}

	public void bt_gestionarRecurso(){

		try {
			//Validar seleccion de recurso
			if ( equipo.getNumSerie() == null && accesoriosGestion.size() == 0 ) {
				FacesContext.getCurrentInstance().addMessage("mensajesError", new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR! Por favor seleccione un recurso a gestionar", null));
				RequestContext.getCurrentInstance().update("mensajesError");
			} else {

				historial.setFechaGestion(fechaActual);
				historial.setResponsableSoporte("12345678");  //USUARIO DE SESION

				if (opcionGestion.equals("A")){
					historial.setCategoria( gestionarRecursoServicio.obtenerCategoria(Constantes.D_CAT_HISTORIAL_ASIGNACION) );
					estado = gestionarRecursoServicio.obtenerEstado( Constantes.D_ID_ESTADO_ASIGNACION );
				}else{
					historial.setCategoria( gestionarRecursoServicio.obtenerCategoria(Constantes.D_CAT_HISTORIAL_DEVOLUCION) );
					estado = gestionarRecursoServicio.obtenerEstado( Constantes.D_ID_ESTADO_CREACION );
				}

				gestion = gestionarRecursoServicio.gestionarRecurso(historial, equipo, accesoriosGestion, estado);

				if (gestion == true) {

					//reporteDescarga = gestionarRecursoServicio.generarReporteEquipo(equipo, historial);

					FacesContext.getCurrentInstance().addMessage("mensajesError", new FacesMessage(FacesMessage.SEVERITY_INFO, "EXITO! El/los recurso(s) se gestionaron satisfactoriamente", null));
					RequestContext.getCurrentInstance().update("mensajesError");

				}else {
					FacesContext.getCurrentInstance().addMessage("mensajesError", new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR! No se pudo gestionar el/los recurso(s)", null));
					RequestContext.getCurrentInstance().update("mensajesError");

				}

			}

		}catch (Exception e){
			FacesContext.getCurrentInstance().addMessage("mensajesError", new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR! No se pudo gestionar el/los recurso(s)", null));
			RequestContext.getCurrentInstance().update("mensajesError");


		}

		bt_limpiarGestion();
	}

//	public void descargarReporte(){
//
//		try {
//			gestionarRecursoServicio.descargarReporte(reporteDescarga);
//		}catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}


	public String bt_cancelar(){

		return "Cancelar";
	}




	/*GET & SET*/

	public GestionarRecursoServicio getGestionarRecursoServicio() {
		return gestionarRecursoServicio;
	}

	public void setGestionarRecursoServicio(GestionarRecursoServicio gestionarRecursoServicio) {
		this.gestionarRecursoServicio = gestionarRecursoServicio;
	}
//
//	public GenerarReporteServicio getGenerarReporteServicio() {
//		return generarReporteServicio;
//	}
//
//	public void setGenerarReporteServicio(GenerarReporteServicio generarReporteServicio) {
//		this.generarReporteServicio = generarReporteServicio;
//	}

	public HistorialInventarioEntity getHistorial() {
		return historial;
	}

	public void setHistorial(HistorialInventarioEntity historial) {
		this.historial = historial;
	}

	public EquipoEntity getEquipo() {
		return equipo;
	}

	public void setEquipo(EquipoEntity equipo) {
		this.equipo = equipo;
	}

	public EstadoEntity getEstado() {
		return estado;
	}

	public void setEstado(EstadoEntity estado) {
		this.estado = estado;
	}

	public String getOpcionGestion() {
		return opcionGestion;
	}

	public void setOpcionGestion(String opcionGestion) {
		this.opcionGestion = opcionGestion;
	}

	public String getOpcionDatatable() {
		return opcionDatatable;
	}

	public void setOpcionDatatable(String opcionDatatable) {
		this.opcionDatatable = opcionDatatable;
	}

	public String getOpcionRecurso() {
		return opcionRecurso;
	}

	public void setOpcionRecurso(String opcionRecurso) {
		this.opcionRecurso = opcionRecurso;
	}

	public Boolean getGestion() {
		return gestion;
	}

	public void setGestion(Boolean gestion) {
		this.gestion = gestion;
	}

	public Date getFechaActual() {
		return fechaActual;
	}

	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	public List<String> getAccesoriosSeleccion() {
		return accesoriosSeleccion;
	}

	public void setAccesoriosSeleccion(List<String> accesoriosSeleccion) {
		this.accesoriosSeleccion = accesoriosSeleccion;
	}

	public List<AccesorioEntity> getAccesoriosGestion() {
		return accesoriosGestion;
	}

	public void setAccesoriosGestion(List<AccesorioEntity> accesoriosGestion) {
		this.accesoriosGestion = accesoriosGestion;
	}

	public List<Object> getItems() {
		return items;
	}

	public void setItems(List<Object> items) {
		this.items = items;
	}

	public List<Object> getItemsBuscados() {
		return itemsBuscados;
	}

	public void setItemsBuscados(List<Object> itemsBuscados) {
		this.itemsBuscados = itemsBuscados;
	}

	public Object getItemSeleccionado() {
		return itemSeleccionado;
	}

	public void setItemSeleccionado(Object itemSeleccionado) {
		this.itemSeleccionado = itemSeleccionado;
	}


}

