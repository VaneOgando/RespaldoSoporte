package com.inventario.jpa.data;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="EMP")
public class Empleado {
	@Id
	@Column(name="EMPNO")
	private String id;
	@Column(name="ENAME")
	private String nombre;
	@Column(name="HIREDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaContratacion;
	@Column(name="SAL")
	private double salario;

	public Empleado(){

	}

	public Empleado(String id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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
