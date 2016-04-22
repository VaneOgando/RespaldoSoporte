package com.inventario.jpa.data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="EMP")
public class Empleado_original {
	@Id
	@Column(name="EMPNO")
	private long id;
	@Column(name="ENAME")
	private String nombre;
	@Column(name="HIREDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaContratacion;
	@Column(name="SAL")
	private double salario;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFechaContratacion() {
		return fechaContratacion;
	}

	public void setFechaContratacion(Date fechaContratacion) {
		this.fechaContratacion = fechaContratacion;
	}

	public double getSalario() {
		return salario;
	}

	public void setSalario(double salario) {
		this.salario = salario;
	}
}
