package com.cimait.microcontainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.cimait.DAO.DetalleFactura;
import com.cimait.DAO.ImpuestosRetencion;
import com.cimait.DAO.InfoRetencion;
import com.cimait.DAO.Transaction;
import com.cimait.DAO.FacEmpresa;
import com.cimait.DAO.FacPuntoEmision;
import com.cimait.DAO.ImpuestosFactura;
import com.cimait.DAO.InfoAdicional;
import com.cimait.DAO.InfoCredito;
import com.cimait.DAO.InfoFactura;
import com.cimait.DAO.InfoTributaria;
import com.cimait.runtime.ConexionBase;
import com.cimait.runtime.Environment;

public class DBTransaction {
	
	//private static String userSchemaDb = "cicf10.dbo.";
	private static String userSchemaDb = Environment.c.getString("Empresa.DB.Empresa.userSchemaDb");

	public static ArrayList<Transaction> getTrx(int rownum, String sucursal) {
		
		//Connection Con = conDatabase;
		//VPI - GBA
		Connection Con = ConexionBase.DBManager.get();
		
		ResultSet Rs = null;
		PreparedStatement pst = null;
		ArrayList<Transaction> ListTrx = new ArrayList<Transaction>();
		try {
			String sql = Environment.c
					.getString("Empresa.DB.Empresa.sql.getTrx");
			sql = sql.replace("[rownum]", new Integer(rownum).toString());
			sql = sql.replace("[ESQUEMA]", userSchemaDb);
			
			pst = Con.prepareStatement(sql);
			pst.setString(1, sucursal);
			Rs = pst.executeQuery();
			while (Rs.next()) {
				// System.out.println("Fecha:"+Rs.getString(3));
				Transaction trx = new Transaction(Rs.getString(1),
						Rs.getString(2), Rs.getString(3), Rs.getString(4),
						Rs.getString(5), Rs.getString(6));
				ListTrx.add(trx);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				Rs.close();
				pst.close();
				//Con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return ListTrx;
	}

	public static int UpdateEstadoTrx(String establecimento, String ptoEmision, String secuencial,String local, 
									  String estado) throws Exception{
		
		Connection Con = ConexionBase.DBManager.get();	
		int li_result=0;
		PreparedStatement pst = null;
		try{    	
			String sql = Environment.c.getString("Empresa.DB.Empresa.sql.UpdateEstadoTrx");
			sql = sql.replace("[ESQUEMA]", userSchemaDb);
			
	    	pst = Con.prepareStatement(sql);
	    	
	    	pst.setString(1, estado);	    
			pst.setString(2, establecimento+ptoEmision);//CNuSerie
			pst.setString(3, secuencial);
			pst.setString(4, local);
	    	
	    	li_result = pst.executeUpdate();	    	
	    	
		}catch(Exception e){
	    	e.printStackTrace();
	    	throw new Exception("ERRORDATABASE,UpdateEstadoTrx Error al Actualizar el >> documento ::"+establecimento+ptoEmision+secuencial+"->"+e.getMessage());
		}finally {
	    	try {
		    	pst.close();
		    	//Con.close();
	    	} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return li_result;
	}
	
	
	
	public static InfoRetencion getTrxInfoTributariaXml(String establecimento, String ptoEmision, String secuencial,String local) {

		Connection Con = ConexionBase.DBManager.get();
		ResultSet Rs = null;
		PreparedStatement pst = null;
		InfoRetencion infoRetencionTrx = new InfoRetencion();

		ArrayList<InfoAdicional> listInfoAdic = new ArrayList<InfoAdicional>();
		try {
			String sql = Environment.c
					.getString("Empresa.DB.Empresa.sql.InfoTributariaXmlTrx");
			sql = sql.replace("[ESQUEMA]", userSchemaDb);

			pst = Con.prepareStatement(sql);

			pst.setString(1, establecimento+ptoEmision);//CNuSerie
			pst.setString(2, secuencial);
			pst.setString(3, local);

			Rs = pst.executeQuery();
			while (Rs.next()) {
				
				infoRetencionTrx.setFechaEmision(Rs.getString("fechaEmision"));
				infoRetencionTrx.setTipoIdentificacionSujetoRetenido(Rs
						.getString("tipoIdentificacionSujetoRetenido"));
				infoRetencionTrx.setRazonSocialSujetoRetenido(Rs
						.getString("razonSocialSujetoRetenido"));
				infoRetencionTrx.setIdentificacionSujetoRetenido(Rs
						.getString("identificacionSujetoRetenido"));
				infoRetencionTrx.setPeriodoFiscal(Rs
						.getString("periodoFiscal"));
				

				if ((Rs.getString("direccion").length() > 0)
						|| (Rs.getString("direccion") != null)) {
					InfoAdicional infoAdic = new InfoAdicional("DIRECCION",
							Rs.getString("direccion"));
					listInfoAdic.add(infoAdic);
				}
				
				if ((Rs.getString("telefono").length() > 0)
						|| (Rs.getString("telefono") != null)) {
					InfoAdicional infoAdic = new InfoAdicional("TELEFONO",
							Rs.getString("telefono"));
					listInfoAdic.add(infoAdic);
				}
				if ((Rs.getString("emailSujetoRetenido").length() > 0)
						|| (Rs.getString("emailSujetoRetenido") != null)) {
					InfoAdicional infoAdic = new InfoAdicional("EMAIL",
							Rs.getString("emailSujetoRetenido"));
					listInfoAdic.add(infoAdic);
				}
				if ((Rs.getString("codigoProveedor").length() > 0)
						|| (Rs.getString("codigoProveedor") != null)) {
					InfoAdicional infoAdic = new InfoAdicional("codigoProveedor",
							Rs.getString("codigoProveedor"));
					listInfoAdic.add(infoAdic);
				}

				// VPI se agregan campos de informacion adicional
				if ((Rs.getString("vendedor").length() > 0)
						|| (Rs.getString("vendedor") != null)) {
					InfoAdicional infoAdic = new InfoAdicional("VENDEDOR",
							Rs.getString("vendedor"));
					listInfoAdic.add(infoAdic);
				}

				if ((Rs.getString("origenDocumento").length() > 0)
						|| (Rs.getString("origenDocumento") != null)) {
					InfoAdicional infoAdic = new InfoAdicional("ORIGEN",
							Rs.getString("origenDocumento"));
					listInfoAdic.add(infoAdic);
				}

			}
			infoRetencionTrx.setListInfoAdicional(listInfoAdic);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				Rs.close();
				pst.close();
				//Con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return infoRetencionTrx;
	}


	public static ArrayList<ImpuestosRetencion> getImpuestosTrx(String establecimento, String ptoEmision, String secuencial,String local){
		
		//Connection Con = conDatabase;	
		Connection Con = ConexionBase.DBManager.get();
		ResultSet Rs= null;
		PreparedStatement pst = null;
		ArrayList<ImpuestosRetencion> listImpuestos= new ArrayList<ImpuestosRetencion>();
		
		try{  
			String sql = Environment.c.getString("Empresa.DB.Empresa.sql.ImpuestosTrxRetencion");
			sql = sql.replace("[ESQUEMA]", userSchemaDb);			
	    	pst = Con.prepareStatement(sql);
	    	
			pst.setString(1, establecimento+ptoEmision);//CNuSerie
			pst.setString(2, secuencial);
			pst.setString(3, local);
	    	
	    	
	    	Rs= pst.executeQuery();
	    	listImpuestos.clear();
	    	while (Rs.next()){
	    		ImpuestosRetencion impRet = new ImpuestosRetencion();
	    		impRet.setCodigo(Rs.getString("codigo"));
	    		impRet.setCodigoRetencion(Rs.getString("codigoRetencion"));
	    		impRet.setBaseImponible(Rs.getString("baseImponible"));
	    		impRet.setPorcentajeRetener(Rs.getString("porcentajeRetener"));
	    		impRet.setValorRetenido(Rs.getString("valorRetenido"));
	    		impRet.setCodDocSustento(Rs.getString("codDocSustento"));
	    		impRet.setNumDocSustento(Rs.getString("numDocSustento"));
	    		impRet.setFechaEmisionDocSustento(Rs.getString("fechaEmisionDocSustento"));
	    		
	    		listImpuestos.add(impRet);    		
	    	}
		}catch(Exception e){
	    	e.printStackTrace();
		}finally {
	    	try {
				Rs.close();
		    	pst.close();
		    	//Con.close();
	    	} catch (SQLException e) {
				e.printStackTrace();
			}
	    	
		}
		return listImpuestos;
	}
	
	
}
