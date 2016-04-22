package com.inventario.primefaces.beans;

//import javax.faces.application.FacesMessage;
//import javax.faces.bean.*;
//import javax.faces.context.ExternalContext;
//import javax.faces.context.FacesContext;
//import javax.naming.*;
//import javax.naming.directory.*;
//
//import com.inventario.util.constante.Constantes;
//
//import com.inventario.spring.service.InicioSesionServicio;
//import org.springframework.ldap.core.AttributesMapper;
//import org.springframework.ldap.core.DirContextOperations;
//import org.springframework.ldap.core.DistinguishedName;
//import org.springframework.ldap.core.LdapTemplate;
//import org.springframework.ldap.core.support.AbstractContextMapper;
//import org.springframework.ldap.filter.AndFilter;
//import org.springframework.ldap.filter.EqualsFilter;
//import org.springframework.ldap.filter.Filter;
//import org.springframework.ldap.support.LdapUtils;
//
//import java.util.Hashtable;
//import java.util.List;
//
//import static org.springframework.ldap.query.LdapQueryBuilder.query;
//
//@ManagedBean
//@SessionScoped
//public class InicioSesionBean {
//
//	/*ATRIBUTOS*/
//	@ManagedProperty("#{inicioSesionServicio}")
//	private InicioSesionServicio inicioSesionServicio;
//	private ExternalContext context;
//
//	private String user;
//	private String pass;
//	private String nombre;
//	private String apellido;
//
//	private boolean respuesta;
//
//	private LdapTemplate ldapTemplate;
//
//	/*METODOS*/
//	public String bt_ingresar_action() {
//		// Llamada al servicio (Controllador)
//		respuesta = inicioSesionServicio.ValidarUsuarioContrasenia(user, pass);
//
//		if (respuesta == true) {
//			this.nombre = "Admin";
//			this.apellido = "Admin";
//			return "consultarInventario";
//
//		}else {
//			this.user 	 = "";
//			this.pass = "";
//
//			FacesContext.getCurrentInstance().addMessage(null,
//					new FacesMessage(FacesMessage.SEVERITY_ERROR, Constantes.ERR_LOGIN_INVALIDO, Constantes.ERR_LOGIN_INVALIDO));
//		}
//		return "";
//	}
//
//
//	public String authenticateUser() throws NamingException {
//
//		Hashtable<String, String> env = new Hashtable<String, String>();
//
//		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
//		env.put(Context.PROVIDER_URL, "ldap://infra.tcs.local/DC=tcs,DC=local" );
//
//		// Needed for the Bind (User Authorized to Query the LDAP server)
//		env.put(Context.SECURITY_AUTHENTICATION, "simple");
//		env.put(Context.SECURITY_PRINCIPAL, "incidencias");
//		env.put(Context.SECURITY_CREDENTIALS, "sDuZv5TfCMOgQ5");
//
//		DirContext ctx;
//
//		try {
//			ctx = new InitialDirContext(env);
//		} catch (NamingException e) {
//			throw new RuntimeException(e);
//		}
//
//		NamingEnumeration<SearchResult> results = null;
//
//		try {
//
//			SearchControls controls = new SearchControls();
//			controls.setSearchScope(SearchControls.SUBTREE_SCOPE); // Search Entire Subtree
//			controls.setCountLimit(1);   //Sets the maximum number of entries to be returned as a result of the search
//			controls.setTimeLimit(5000); // Sets the time limit of these SearchControls in milliseconds
//
//			String searchString = "(&(objectCategory=user)(sAMAccountName=" + user + "))";
//
//			results = ctx.search("", searchString, controls);
//
//			if (results.hasMore()) {
//
//				SearchResult result = results.next();
//				Attributes attrs = result.getAttributes();
//				javax.naming.directory.Attribute dnAttr = attrs.get("distinguishedName");
//				String dn = dnAttr.get().toString();
//
//				// User Exists, Validate the Password
//
//				env.put(Context.SECURITY_PRINCIPAL, dn);
//				env.put(Context.SECURITY_CREDENTIALS, pass);
//
//				new InitialDirContext(env); // Exception will be thrown on Invalid case
//				return "consultarInventario.xhtml";
//			}
//			else
//				return "";
//
//		} catch (AuthenticationException e) { // Invalid Login
//			return "";
//		} catch (NameNotFoundException e) { // The base context was not found.
//
//			return "";
//		} catch (NamingException e) {
//			throw new RuntimeException(e);
//		} finally {
//
//			if (results != null) {
//				try { results.close(); } catch (Exception e) { /* Do Nothing */ }
//			}
//
//			if (ctx != null) {
//				try { ctx.close(); } catch (Exception e) { /* Do Nothing */ }
//			}
//		}
//
//
//	}
//
//	public String autenticarUsuario(){
//
//		DirContext ctx = null;
//		try {
//			ctx = ldapTemplate.getContextSource().getContext( obtenerDNUsuario(user) ,pass);
//			return "consultarInventario.xhtml";
//		} catch (Exception e) {
//			// Context creation failed - authentication did not succeed
//			System.out.println("Fallo inicio sesion");
//			return "";
//		} finally {
//			// It is imperative that the created DirContext instance is always closed
//			LdapUtils.closeContext(ctx);
//		}
//
//	}
//
//	private String obtenerDNUsuario(String usuario) {
//
//		List<String> result = ldapTemplate.search(
//				query().where("uid").is(usuario),
//				new AbstractContextMapper() {
//					protected String doMapFromContext(DirContextOperations ctx) {
//						return ctx.getNameInNamespace();
//					}
//				});
//
//		if(result.size() != 1) {
//			throw new RuntimeException("User not found or not unique");
//		}
//
//		return result.get(0);
//	}
//
//
//	/*GET & SET*/
//	public InicioSesionServicio getInicioSesionServicio() {
//		return inicioSesionServicio;
//	}
//
//	public void setInicioSesionServicio(InicioSesionServicio inicioSesionServicio) {
//		this.inicioSesionServicio = inicioSesionServicio;
//	}
//
//	public String getUser() {
//		return user;
//	}
//
//	public void setUser(String user) {
//		this.user = user;
//	}
//
//	public String getPass() {
//		return pass;
//	}
//
//	public void setPass(String pass) {
//		this.pass = pass;
//	}
//
//	public String getNombre() {
//		return nombre;
//	}
//
//	public void setNombre(String nombre) {
//		this.nombre = nombre;
//	}
//
//	public String getApellido() {
//		return apellido;
//	}
//
//	public void setApellido(String apellido) {
//		this.apellido = apellido;
//	}
//
//	public boolean getRespuesta() {
//		return respuesta;
//	}
//
//	public void setRespuesta(boolean respuesta) {
//		this.respuesta = respuesta;
//	}
//
//	public LdapTemplate getLdapTemplate() {
//		return ldapTemplate;
//	}
//
//	public void setLdapTemplate(LdapTemplate ldapTemplate) {
//		this.ldapTemplate = ldapTemplate;
//	}
//}