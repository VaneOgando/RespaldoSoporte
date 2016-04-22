package com.inventario.spring.service;

//import net.sf.jasperreports.engine.*;
//import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;
//import org.springframework.ldap.core.LdapTemplate;
//import org.springframework.ldap.filter.EqualsFilter;
//import org.springframework.ldap.filter.Filter;
//import org.springframework.stereotype.Component;
//
//import javax.faces.context.FacesContext;
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import javax.transaction.Transactional;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class GenerarReporteServicio {
//
//	/*ATRIBUTOS*/
//
//
//	/*METODOS*/
//
//	public GenerarReporteServicio() {}
//
//	@Transactional
//	public JasperPrint descargarReporte(String reporte, Map<String, Object> parametros, String nombreArchivo){
//
//		try {
//			//Buscar reporte
//			String  jasperReport=  FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/reportes/"+ reporte +".jasper");
//
//			//Crear el reporte
//			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
///*
//			HttpServletResponse httpServletResponse=(HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
//			httpServletResponse.addHeader("Content-disposition", "attachment; filename="+ nombreArchivo );
//
//			ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
//			JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
//			servletOutputStream.close();
//
//			FacesContext.getCurrentInstance().responseComplete();
//			*/
//
//			return jasperPrint;
//
//		} catch (JRException e) {
//			e.printStackTrace();
//			return null;
//		}/* catch (IOException e) {
//			e.printStackTrace(); (IOException e) {
//			e.printStackTrace();
//		}*/
//	}
//
//	public void exportar(JasperPrint jasperPrint, String nombreArchivo) throws IOException {
//
//		try{
//			HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//			httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + nombreArchivo);
//
//			ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
//			JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
//			servletOutputStream.close();
//
//			FacesContext.getCurrentInstance().responseComplete();
//
//		}catch (JRException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/*GET & SET*/
//
//}
