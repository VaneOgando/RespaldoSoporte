package com.inventario.spring.service;

import com.inventario.jpa.data.*;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Component
public class ModificarEquipoServicio {

	/*ATRIBUTO*/
	protected EntityManager entityManager;


	/*METODOS*/
	@Transactional
	public EquipoEntity obtenerEquipo(String numSerie) throws DataAccessException {

		List<EquipoEntity> resultList = getEntityManager().createNamedQuery("HQL_EQUIPO_POR_NUMSERIE")
				.setParameter("numSerie", numSerie)
				.getResultList();
		if(resultList.size() < 1){
			return null;
		}else{
			return resultList.get(0);
		}

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
	public boolean modificarEquipo(MarcaEntity marca, ModeloEntity modelo, HistorialInventarioEntity historial, EquipoEntity equipo) throws DataAccessException{

		boolean modificacion = false;

		try {

			if (marca.getId() == 0) { //Marca no existe
				entityManager.persist(marca);
			}

			modelo.setMarca(marca);

			if(modelo.getId() == 0){//modelo no existe
				entityManager.persist(modelo);
			}

			equipo.setModelo(modelo);

			entityManager.merge(equipo);

			historial.setEquipo(equipo);

			entityManager.persist(historial);

			modificacion = true;

		}catch(Exception e){
			modificacion = false;
			throw e;
		}finally {

			entityManager.close();
			return modificacion;

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
