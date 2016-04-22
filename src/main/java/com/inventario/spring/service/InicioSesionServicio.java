package com.inventario.spring.service;
//
//import org.springframework.ldap.core.LdapTemplate;
//import org.springframework.ldap.filter.AndFilter;
//import org.springframework.ldap.filter.EqualsFilter;
//import org.springframework.ldap.filter.Filter;
//import org.springframework.stereotype.Component;
//
//import javax.naming.directory.DirContext;
//
//@Component
//public class InicioSesionServicio {
//
//	/*ATRIBUTOS*/
//	private LdapTemplate ldapTemplate;
//
//
//	/*METODOS*/
//	public boolean ValidarUsuarioContrasenia(String usuario, String contrasenia){
//
//		//Conexion a LDAP, validar contraseña d usuario correcta, y usuario del departamento d soporte
//
//		if (usuario.equals("admin") && contrasenia.equals("admin"))
//			return true;
//
//		return false;
//	}
//
//	public boolean autenticarUsuarioSoporte(String usuario, String contrasenia){
//
//		Filter filter = new EqualsFilter("sAMAccountName", usuario);
//		boolean authed = ldapTemplate.authenticate("", filter.encode(), contrasenia);
//
//		System.out.println("Authenticated: " + authed);
//
//		return authed;
//
//	}
//
//
//	/*GET & SET*/
//	public void setLdapTemplate(LdapTemplate ldapTemplate) {
//		this.ldapTemplate = ldapTemplate;
//	}
//
//	public LdapTemplate getLdapTemplate() {
//		return ldapTemplate;
//	}
//}
