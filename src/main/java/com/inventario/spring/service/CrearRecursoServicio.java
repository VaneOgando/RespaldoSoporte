package com.inventario.spring.service;

import com.inventario.jpa.data.*;
import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.RollbackException;
import javax.transaction.Transactional;
import java.net.CacheRequest;
import java.util.List;

@Component
public class CrearRecursoServicio {

	/*ATRIBUTO*/
	protected EntityManager entityManager;


	/*METODOS*/
	@Transactional
	public List<CategoriaEntity> cargarCategorias(String tipoCategoria) throws DataAccessException {

		List<CategoriaEntity> resultList = getEntityManager().createNamedQuery("HQL_CATEGORIA_POR_TIPO")
				.setParameter("tipoCategoria", tipoCategoria)
				.getResultList();

		return resultList;
	}

	@Transactional
	public List<MarcaEntity> cargarMarcas() throws DataAccessException {

		List<MarcaEntity> resultList = getEntityManager().createNamedQuery("HQL_MARCA")
										.getResultList();

		return resultList;
	}

	@Transactional
	public MarcaEntity obtenerMarcaPorNombre(String nombreMarca) throws DataAccessException {

		List<MarcaEntity> resultList = getEntityManager().createNamedQuery("HQL_MARCA_POR_NOMBRE")
				.setParameter("nombreMarca", nombreMarca)
				.getResultList();

		if (resultList.size() < 1 ){
			return null;
		}else{
			return resultList.get(0);
		}

	}

	@Transactional
	public List<ModeloEntity> cargarModelos(MarcaEntity marca) throws DataAccessException {

		List<ModeloEntity> resultList = getEntityManager().createNamedQuery("HQL_MODELO_POR_MARCA")
				.setParameter("idMarca", marca.getId())
				.getResultList();

		return resultList;
	}

	@Transactional
	public EstadoEntity obtenerEstado(int idEstadoDefecto) throws DataAccessException {

		List<EstadoEntity> resultList = getEntityManager().createNamedQuery("HQL_ESTADO_POR_ID")
										.setParameter("idEstado", idEstadoDefecto)
										.getResultList();

		if(resultList.size() < 1){
			return null;
		}else{
			return resultList.get(0);
		}

	}

	@Transactional
	public CategoriaEntity obtenerCategoriaHistorial(int idCategoriaDefecto) throws DataAccessException {

		List<CategoriaEntity> resultList = getEntityManager().createNamedQuery("HQL_CATEGORIA_POR_ID")
				.setParameter("idCategoria", idCategoriaDefecto)
				.getResultList();

		if(resultList.size() < 1){
			return null;
		}else{
			return resultList.get(0);
		}

	}

	@Transactional
	public ModeloEntity obtenerModeloPorNombre(String nombreModelo, int idMarca) throws DataAccessException {

		List<ModeloEntity> resultList = getEntityManager().createNamedQuery("HQL_MODELO_POR_NOMBRE")
				.setParameter("nombreModelo", nombreModelo)
				.setParameter("idMarca", idMarca)
				.getResultList();

		if (resultList.size() < 1 ){
			return null;
		}else{
			return resultList.get(0);
		}

	}

	@Transactional
	public CategoriaEntity obtenerCategoriaPorNombre(String nombreCategoria, String tipoCategoria) throws DataAccessException {

		List<CategoriaEntity> resultList = getEntityManager().createNamedQuery("HQL_CATEGORIA_POR_NOMBRE_Y_TIPO")
				.setParameter("nombreCategoria", nombreCategoria)
				.setParameter("tipoCategoria", tipoCategoria)
				.getResultList();

		if (resultList.size() < 1 ){
			return null;
		}else{
			return resultList.get(0);
		}

	}

	@Transactional
	public boolean crearRecurso(MarcaEntity marca, ModeloEntity modelo, CategoriaEntity categoria, EstadoEntity estado, HistorialInventarioEntity historial, EquipoEntity equipo, AccesorioEntity accesorio, String opcion) throws DataAccessException{

		boolean creacion = false;

		try {

			if (marca.getId() == 0){ //Marca no existe
				entityManager.persist(marca);
			}

			modelo.setMarca(marca);

			if(modelo.getId() == 0){//modelo no existe
				entityManager.persist(modelo);
			}

			if(opcion.equals("0")) {

				equipo.setEstado(estado);
				equipo.setModelo(modelo);
				entityManager.persist(equipo);

				historial.setEquipo(equipo);

			}else if(opcion.equals("1")){

				if(categoria.getId() == 0){//categoria no existe
					categoria.setTipoCategoria("accesorio");
					entityManager.persist(categoria);
				}

				accesorio.setEstado(estado);
				accesorio.setModelo(modelo);
				accesorio.setCategoria(categoria);
				entityManager.persist(accesorio);

				historial.setAccesorio(accesorio);

			}

			entityManager.persist(historial);

			creacion = true;

		}catch(Exception e){
			creacion = false;
			throw e;
		}finally {

			entityManager.close();
			return creacion;

		}

	}


	/*GET & SET*/
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
