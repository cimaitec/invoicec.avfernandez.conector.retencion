package com.cimait.DAO;

import java.util.ArrayList;

public class InfoRetencion {

			
	private String fechaEmision;
	private String dirEstablecimiento;
	private String contribuyenteEspecial;
	private String obligadoContabilidad;
	private String tipoIdentificacionSujetoRetenido;
	private String razonSocialSujetoRetenido;
	private String identificacionSujetoRetenido;
	private String periodoFiscal;

	private ArrayList<ImpuestosRetencion> listImpuestosRetencion;
	private ArrayList<InfoAdicional> listInfoAdicional;
	
	
	public String getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	public String getDirEstablecimiento() {
		return dirEstablecimiento;
	}
	public void setDirEstablecimiento(String dirEstablecimiento) {
		this.dirEstablecimiento = dirEstablecimiento;
	}
	public String getContribuyenteEspecial() {
		return contribuyenteEspecial;
	}
	public void setContribuyenteEspecial(String contribuyenteEspecial) {
		this.contribuyenteEspecial = contribuyenteEspecial;
	}
	public String getObligadoContabilidad() {
		return obligadoContabilidad;
	}
	public void setObligadoContabilidad(String obligadoContabilidad) {
		this.obligadoContabilidad = obligadoContabilidad;
	}
	public String getTipoIdentificacionSujetoRetenido() {
		return tipoIdentificacionSujetoRetenido;
	}
	public void setTipoIdentificacionSujetoRetenido(
			String tipoIdentificacionSujetoRetenido) {
		this.tipoIdentificacionSujetoRetenido = tipoIdentificacionSujetoRetenido;
	}
	public String getRazonSocialSujetoRetenido() {
		return razonSocialSujetoRetenido;
	}
	public void setRazonSocialSujetoRetenido(String razonSocialSujetoRetenido) {
		this.razonSocialSujetoRetenido = razonSocialSujetoRetenido;
	}
	public String getIdentificacionSujetoRetenido() {
		return identificacionSujetoRetenido;
	}
	public void setIdentificacionSujetoRetenido(String identificacionSujetoRetenido) {
		this.identificacionSujetoRetenido = identificacionSujetoRetenido;
	}
	public String getPeriodoFiscal() {
		return periodoFiscal;
	}
	public void setPeriodoFiscal(String periodoFiscal) {
		this.periodoFiscal = periodoFiscal;
	}
	public ArrayList<ImpuestosRetencion> getListImpuestosRetencion() {
		return listImpuestosRetencion;
	}
	public void setListImpuestosRetencion(
			ArrayList<ImpuestosRetencion> listImpuestosRetencion) {
		this.listImpuestosRetencion = listImpuestosRetencion;
	}
	public ArrayList<InfoAdicional> getListInfoAdicional() {
		return listInfoAdicional;
	}
	public void setListInfoAdicional(ArrayList<InfoAdicional> listInfoAdicional) {
		this.listInfoAdicional = listInfoAdicional;
	}
	
	
}
