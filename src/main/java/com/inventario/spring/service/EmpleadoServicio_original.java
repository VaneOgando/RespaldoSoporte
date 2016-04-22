package com.inventario.spring.service;

import com.inventario.jpa.data.Empleado;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class EmpleadoServicio_original {
	@PersistenceContext
	private EntityManager em;

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Transactional
	public void registrar(Empleado emp) {
		// Guardar empleado
		this.em.persist(emp);
	}

}
