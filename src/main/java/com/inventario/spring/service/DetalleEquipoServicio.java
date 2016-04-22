package com.inventario.spring.service;

import com.inventario.jpa.data.*;
import com.inventario.util.constante.Constantes;
import com.sun.corba.se.spi.ior.ObjectKey;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Component
public class DetalleEquipoServicio {

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
	public List<HistorialInventarioEntity> obtenerHistorialEquipo(String numSerie) throws DataAccessException {

		List<HistorialInventarioEntity> resultList = getEntityManager().createNamedQuery("HQL_HISTORIAL_POR_EQUIPO")
													.setParameter("numSerie", numSerie)
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
	public String obtenerUsuarioAsignado(String numSerie) throws DataAccessException {

		List<Object> resultList = getEntityManager().createNamedQuery("SQL_HISTORIAL_USUARIO_ASIGNADO_EQUIPO")
							.setParameter(1, numSerie)
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
	public boolean cambiarEstado(EquipoEntity equipo, EstadoEntity estado, HistorialInventarioEntity historial) throws DataAccessException {

		boolean cambiarEstado = false;

		try{

			equipo.setEstado(estado);
			entityManager.merge(equipo);

			historial.setEquipo(equipo);
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
