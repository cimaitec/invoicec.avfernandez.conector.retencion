package com.cimait.runtime;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import sun.security.action.GetLongAction;




//Propias del Programador
import com.cimait.runtime.Environment;
import com.cimait.xml.CreditoXml;
import com.cimait.xml.FacturaXml;
import com.cimait.xml.RetencionXml;
import com.cimait.microcontainer.DBTransaction;
import com.cimait.microcontainer.DBFacElectronica;
import com.cimait.microcontainer.GenericTransaction;
import com.cimait.DAO.DetalleFactura;
import com.cimait.DAO.FacEmpresa;
import com.cimait.DAO.FacEstablecimiento;
import com.cimait.DAO.FacPuntoEmision;
import com.cimait.DAO.ImpuestosFactura;
import com.cimait.DAO.ImpuestosRetencion;
import com.cimait.DAO.InfoAdicional;
import com.cimait.DAO.InfoCredito;
import com.cimait.DAO.InfoFactura;
import com.cimait.DAO.InfoRetencion;
import com.cimait.Util.CtrlFile;
import com.cimait.Util.Util;

public class Hilo extends GenericTransaction{

	private Connection facturacion=null;
    private int flagbill = 0;

	private FacPuntoEmision puntoEmision;	
	private FacEstablecimiento facEstablecimiento;
	
	//private static String configId = "HiloEmpresa";
	private static String ruc;
	private String establecimiento;
	private String secuencial;
	private String fechaEmision;
	private String tipoDocumento;
	private String sucursal;
	private String ptoEmision;
	private String version;
	
	public Hilo(String ruc, String establecimiento,
			String ptoEmision, String secuencial, String tipoDocumento,
			String sucursal, String fechaEmision, String version) {
		
		classReference = "EmpresaHilo";
		this.ruc = ruc;
		this.secuencial = secuencial;
		this.fechaEmision = fechaEmision;
		this.tipoDocumento = tipoDocumento;
		this.establecimiento = establecimiento;
		this.sucursal = sucursal;
		this.ptoEmision = ptoEmision;
		this.version = version;
		// setLogger();
	}

	@Override
	//Heredado de la Clase GenericTransaction
	public void run() {
		Thread.currentThread().getName();
		Thread.currentThread().getId();
		int flag = 0;
		//VPI para caso especial de Avicola feranandez en error recurente en 
		//"Tipo de Identificacion is Null" --> Al reprocesar no daba el error 
		//Para evitar que quede marcado el registro y vuelva a ser tomado
		boolean flagInsertaError = true;
		//int sleepBloque = 2000;		
		System.out.print("\nServiceConectorFactura::->run::Iniciando Service... ");		
		
		try{
		//Inicializacion de la conexion con la Base de Datos
		
		
		//VPI - GBA
		//empresa = getConnectionBD(empresa,"Empresa", "SQLServer");
		//empresa = ConexionBase.DBManager.get();
		
		facturacion =	ConexionBase.getConnectionBD(facturacion,"Invoice", "SQLServer");
		/*
		if (empresa == null){				
			throw new Exception ("ERRORGENERAL,getConnectionBD,No se logro la conexion a la base panacea");
		}
		*/
		if (facturacion == null){				
			throw new Exception ("ERRORGENERAL,getConnectionBD,No se logro la conexion a la base facturacion");
		}		
			flag=1;
		}catch(Exception e){
			flag=0;
		}
		String numDocumento =  getEstablecimiento()+getPtoEmision()+getSecuencial();
		if (flag==1)	
			
		try {		
				System.out.println("Ruc::"+this.getRuc());
			    FacEmpresa empresa = DBFacElectronica.getRucEmpresa(facturacion, getRuc());
				if (empresa == null){				
					throw new Exception("ERRORBITACORA,getRucEmpresa La Empresa no existe::"+getRuc()+",FacturaElectronica");
				}				
				
				Integer.parseInt(getSecuencial());
				flagbill=1;					
				
				if (flagbill == 1)
				
						facEstablecimiento = DBFacElectronica.getEstablecimiento( facturacion, 
																			getRuc(),
																			getSucursal());
						
						if (facEstablecimiento==null){
							throw new Exception("ERRORBITACORA,getEstablecimiento El Establecimiento no existe:: "+getEstablecimiento()+",Invoice");
						}						
						puntoEmision=DBFacElectronica.getPuntoEmision(facturacion, 
																	  getRuc(), 
																	  facEstablecimiento.getCodEstablecimiento(), 
																	  getPtoEmision(),
																	  getSucursal());
						if (puntoEmision==null){
							throw new Exception("ERRORBITACORA,getPuntoEmision El Punto de Emision no existe::"+getPtoEmision()+",Invoice");
						}	
						numDocumento =  getEstablecimiento()+getPtoEmision()+getSecuencial();
						System.out.println("::------------Informacion General de Bloque-------------::");
						System.out.println("Fecha de Emision::"+this.getFechaEmision());
						System.out.println("Establecimiento::"+this.getEstablecimiento());
						System.out.println("Secuencial::"+this.getSecuencial());
						System.out.println("TipoDocumento::"+this.getTipoDocumento());
						System.out.println("Sucursal::"+this.getSucursal());						
						System.out.println("PtoEmision::"+this.getPtoEmision());						
						System.out.println("::-------------------------::");
						
						//if (this.getTipoDocumento().equals("01")){
							InfoRetencion infRet = new InfoRetencion(); 
							
						infRet = DBTransaction.getTrxInfoTributariaXml(
								getEstablecimiento(), getPtoEmision(), getSecuencial(),this.getSucursal());
							
							if (infRet==null)
								throw new Exception("ERRORBITACORA,getTrxInfoTributariaXml Info Retencion is null >> documento : "+numDocumento);
							
							if (infRet.getTipoIdentificacionSujetoRetenido() == null){
								flagInsertaError = false;
								throw new Exception("ERRORBITACORA,getTrxInfoTributariaXml Error Tipo de Identificacion is Null >> documento : "+numDocumento);

							}
							//infFac.getListInfoAdicional().add(new InfoAdicional("OFICINA", establecimiento.getDirEstablecimiento()));
							//infFac.getListInfoAdicional().add(new InfoAdicional("CAJA", puntoEmision.getCaja()));
							//infFac.getListInfoAdicional().add(new InfoAdicional("MOVIMIENTO", getIdMovimiento()));

							infRet.setDirEstablecimiento(facEstablecimiento.getDirEstablecimiento());
							infRet.setContribuyenteEspecial(new Integer(empresa.getContribEspecial()).toString());
							infRet.setObligadoContabilidad(empresa.getObligContabilidad());						
							
							System.out.println("::------------Informacion General Retencion -------------::");

							ArrayList<ImpuestosRetencion> ListImpuestos = new ArrayList<ImpuestosRetencion>();
							ListImpuestos = DBTransaction.getImpuestosTrx(getEstablecimiento(), getPtoEmision(), getSecuencial(),this.getSucursal());
							if (ListImpuestos==null)
								throw new Exception("ERRORBITACORA,getTotalImpuestoTrxInfoFactura Error el metodo getImpuestosTrx devuelve Null>> documento : "+numDocumento);							
							infRet.setListImpuestosRetencion(ListImpuestos);
							
							
							RetencionXml retXml = new RetencionXml("1.0.0", 
														   empresa, 
														   facEstablecimiento, 
														   puntoEmision, 
														   getSecuencial(), 
														   infRet);
						try{
							if (retXml.crearRetencion(getVersion())){
								//VPI 
								
								DBTransaction.UpdateEstadoTrx(getEstablecimiento(), getPtoEmision(), getSecuencial(),this.getSucursal(),"G");
																				 
								/*
								DBTransaction.insertFactElectronicaTmp(//this.empresa,
										this.getIdMovimiento(), "G",this.getTipoDocumento(), this.getEstablecimiento().getCodEstablecimiento(),this.getCaja(),this.getSecuencial());
										*/
							}else{
								throw new Exception("ERRORGENERAXML, Error el metodo crearFactura no se pudo crear el XML >> documento : "+numDocumento);
							}
						}catch(Exception e){
							throw new Exception(e.getMessage());
						}
						
					//}
				//throw new Exception("ServiceInvoice::Invoice->main :: El archivo de control no contiene el valor correcto.");					

		} catch (Exception e) {	
			
			e.printStackTrace();
			String ls_mail = e.getMessage();//, ls_error = "";//, ls_tipo_error="", ls_database="";			
			//ls_error= e.getMessage();
			//String ls_errores[] = ls_error.split(",");			
			/*ls_tipo_error = ls_errores[0];
			ls_error = ls_errores[1];
			ls_database = ls_errores[2];
			ls_mail = "Tipo de Error:"+ls_tipo_error+"::Error producido en:"+ls_error+"::En la Base de Datos de:"+ls_database;*/
			ls_mail = ls_mail + "\n Registro Afectado ";
			ls_mail = ls_mail + "\n Fecha de Emision::"+this.getFechaEmision();
			ls_mail = ls_mail + "\n >> documento : "+numDocumento;
			ls_mail = ls_mail + "\n Establecimiento::"+this.getEstablecimiento();
			ls_mail = ls_mail + "\n Pto Emision::"+this.getPtoEmision();
			ls_mail = ls_mail + "\n Secuencial::"+this.getSecuencial();
			ls_mail = ls_mail + "\n TipoDocumento::"+this.getTipoDocumento();			
			
			
			Util.enviaEmailSoporteService("message_service_error", ls_mail);
			
			System.out.println("Fecha de Emision::"+this.getFechaEmision());
			System.out.println(">> documento : "+numDocumento);
			System.out.println("Establecimiento ::"+this.getEstablecimiento());						
			System.out.println("Pto Emision::"+this.getPtoEmision());
			System.out.println("Secuencial::"+this.getSecuencial());
			System.out.println("TipoDocumento::"+this.getTipoDocumento());
			System.out.println("::-------------------------::");
				try {
					//VPI -
					if (flagInsertaError){
						DBTransaction.UpdateEstadoTrx(getEstablecimiento(), getPtoEmision(), getSecuencial(),this.getSucursal(),"G");
					}

						
					/*
					DBTransaction.insertFactElectronicaTmp(//this.empresa,
							this.getIdMovimiento(), "E",this.getTipoDocumento(), this.getEstablecimiento().getCodEstablecimiento(),this.getCaja(),this.getSecuencial());
					*/		
				} catch (Exception e1) {
					e1.printStackTrace();
					System.out.println(e1.getLocalizedMessage());
				}

		}finally{			
			try {
				//empresa.close();
				//Se cierra la conexion al finalizar ejecucion del Hilo 
				 if (ConexionBase.DBManager.get()!= null){
					   ConexionBase.cerrarConexionBD();
					 }
				System.out.println("Cerrando conexion del Hilo");
				facturacion.close();			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	
	}

	


	public static String getRuc() {
		return ruc;
	}

	public static void setRuc(String ruc) {
		Hilo.ruc = ruc;
	}

	public FacEstablecimiento getFacEstablecimiento() {
		return facEstablecimiento;
	}

	public void setFacEstablecimiento(FacEstablecimiento facEstablecimiento) {
		this.facEstablecimiento = facEstablecimiento;
	}

	public String getEstablecimiento() {
		return establecimiento;
	}

	public void setEstablecimiento(String establecimiento) {
		this.establecimiento = establecimiento;
	}

	public String getSecuencial() {
		return secuencial;
	}

	public void setSecuencial(String secuencial) {
		this.secuencial = secuencial;
	}

	public String getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public String getPtoEmision() {
		return ptoEmision;
	}

	public void setPtoEmision(String ptoEmision) {
		this.ptoEmision = ptoEmision;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
	
	
	
}