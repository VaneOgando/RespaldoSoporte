package com.inventario.spring.service;

import com.inventario.jpa.data.*;
import com.inventario.util.constante.Constantes;
import org.hibernate.Criteria;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Component
public class DetalleAccesorioServicio {

	/*ATRIBUTO*/
	protected EntityManager entityManager;


	/*METODOS*/
	@Transactional
	 public AccesorioEntity obtenerAccesorio(int idAccesorio) throws DataAccessException {

		List<AccesorioEntity> resultList = getEntityManager().createNamedQuery("HQL_ACCESORIO_POR_ID")
														.setParameter("idAccesorio", idAccesorio)
														.getResultList();
		if(resultList.size() < 1){
			return null;
		}else{
			return resultList.get(0);
		}

	}

	@Transactional
	public List<HistorialInventarioEntity> obtenerHistorialAccesorio(int idAccesorio) throws DataAccessException {


		List<HistorialInventarioEntity> resultList = getEntityManager().createNamedQuery("HQL_HISTORIAL_POR_ACCESORIO")
													.setParameter("idAccesorio", idAccesorio)
													.getResultList();

		return resultList;
	}

	@Transactional
	public List<EstadoEntity> obtenerCambioEstado(EstadoEntity estadoActual) throws DataAccessException {

		List<EstadoEntity> resultList = getEntityManager().createNamedQuery("HQL_ESTADO_A_CAMBIAR")
				.setParameter("idEstadoActual", estadoActual.getId())
				.getResultList();

		return resultList;
	}

	@Transactional
	public String obtenerUsuarioAsignado(int idAccesorio) throws DataAccessException {

		List<Object> resultList = getEntityManager().createNamedQuery("SQL_HISTORIAL_USUARIO_ASIGNADO_ACCESORIO")
							.setParameter(1, idAccesorio)
							.setParameter(2, Constantes.D_CAT_HISTORIAL_ASIGNACION)
							.getResultList();

		if(resultList.size() < 1){
			return null;
		}else{
			return resultList.get(0).toString();
		}

	}

	@Transactional
	public EstadoEntity obtenerEstado(int idEstado) throws DataAccessException {

		List<EstadoEntity> resultList = getEntityManager().createNamedQuery("HQL_ESTADO_POR_ID")
				.setParameter("idEstado", idEstado)
				.getResultList();

		if(resultList.size() < 1){
			return null;
		}else{
			return resultList.get(0);
		}

	}

	@Transactional
	public CategoriaEntity obtenerCategoriaHistorial(int idCategoria) throws DataAccessException {

		List<CategoriaEntity> resultList = getEntityManager().createNamedQuery("HQL_CATEGORIA_POR_ID")
				.setParameter("idCategoria", idCategoria)
				.getResultList();

		if(resultList.size() < 1){
			return null;
		}else{
			return resultList.get(0);
		}

	}

	@Transactional
	public boolean cambiarEstado(AccesorioEntity accesorio, EstadoEntity estado, HistorialInventarioEntity historial) throws DataAccessException {

		boolean cambiarEstado = false;

		try{

			accesorio.setEstado(estado);
			entityManager.merge(accesorio);

			historial.setAccesorio(accesorio);
			entityManager.persist(historial);

			cambiarEstado = true;

		}catch(Exception e){
			cambiarEstado = false;
			throw e;
		}finally {

			entityManager.close();
			return cambiarEstado;

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
