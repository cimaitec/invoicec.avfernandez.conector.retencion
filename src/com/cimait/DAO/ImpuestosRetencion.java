package com.cimait.DAO;

public class ImpuestosRetencion {
	private String		codigo;
	private String		codigoRetencion;
	private String 		porcentajeRetener;
	private String 		baseImponible;
	private String 		valorRetenido;
	private String 		codDocSustento;
	private String 		numDocSustento;
	private String 		fechaEmisionDocSustento;
	
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getCodigoRetencion() {
		return codigoRetencion;
	}
	public void setCodigoRetencion(String codigoRetencion) {
		this.codigoRetencion = codigoRetencion;
	}
	public String getPorcentajeRetener() {
		return porcentajeRetener;
	}
	public void setPorcentajeRetener(String porcentajeRetener) {
		this.porcentajeRetener = porcentajeRetener;
	}
	public String getBaseImponible() {
		return baseImponible;
	}
	public void setBaseImponible(String baseImponible) {
		this.baseImponible = baseImponible;
	}
	public String getValorRetenido() {
		return valorRetenido;
	}
	public void setValorRetenido(String valorRetenido) {
		this.valorRetenido = valorRetenido;
	}
	public String getCodDocSustento() {
		return codDocSustento;
	}
	public void setCodDocSustento(String codDocSustento) {
		this.codDocSustento = codDocSustento;
	}
	public String getNumDocSustento() {
		return numDocSustento;
	}
	public void setNumDocSustento(String numDocSustento) {
		this.numDocSustento = numDocSustento;
	}
	public String getFechaEmisionDocSustento() {
		return fechaEmisionDocSustento;
	}
	public void setFechaEmisionDocSustento(String fechaEmisionDocSustento) {
		this.fechaEmisionDocSustento = fechaEmisionDocSustento;
	}

	
}
