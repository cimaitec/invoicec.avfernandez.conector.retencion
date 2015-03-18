package com.cimait.xml;
import java.util.ArrayList;
import java.util.Formatter;
import java.io.File;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.cimait.DAO.FacEmpresa;
import com.cimait.DAO.FacEstablecimiento;
import com.cimait.DAO.FacPuntoEmision;
import com.cimait.DAO.ImpuestosRetencion;
import com.cimait.DAO.InfoAdicional;
import com.cimait.DAO.InfoRetencion;
import com.cimait.DAO.InfoTributaria;
import com.cimait.Util.Util;


public class RetencionXml {

	private FacEmpresa empresa;	
	private InfoTributaria informacionTributaria = new InfoTributaria();
	private InfoRetencion infoRetencion = new InfoRetencion();
	
	public RetencionXml(String version, FacEmpresa emp,
			FacEstablecimiento facEstablecimiento, FacPuntoEmision emision,
			String secuencial, InfoRetencion infRet) {
		empresa = emp;
		informacionTributaria.setVersion(version);
		informacionTributaria.setAmbiente(Integer.parseInt(emision
				.getTipoAmbiente()));
		informacionTributaria.setTipoEmision(1);

		informacionTributaria.setRazonSocial(emp.getRazonSocial());
		informacionTributaria.setNombreComercial(emp.getRazonComercial());
		informacionTributaria.setRuc(emp.getRuc());
		informacionTributaria.setCodDoc("07");
		int numero = Integer.parseInt(facEstablecimiento
				.getCodEstablecimiento());

		// int numero = Integer.parseInt("1");
		Formatter fmt = new Formatter();
		fmt.format("%03d", numero);
		informacionTributaria.setEstab(fmt.toString());

		numero = Integer.parseInt(emision.getCodPuntoEmision());
		// numero = Integer.parseInt("1");
		fmt = new Formatter();
		fmt.format("%03d", numero);
		informacionTributaria.setPtoEmi(fmt.toString());
		numero = Integer.parseInt(secuencial);
		fmt = new Formatter();
		fmt.format("%09d", numero);
		informacionTributaria.setSecuencial(fmt.toString());
		informacionTributaria.setDirMatriz(emp.getDireccionMatriz());

		infoRetencion = infRet;
	}
	

	public boolean crearRetencion(String version) throws Exception {
		String ls_tipo_documento = "comprobanteRetencion";
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Attr attr = null;

			ls_tipo_documento = "comprobanteRetencion";
			// ////////////////////Cabecera Xml///////////////////////////////
			Document doc = docBuilder.newDocument();
			Element document = doc.createElement(ls_tipo_documento);
			doc.appendChild(document);
			attr = doc.createAttribute("id");
			attr.setValue("comprobante");
			document.setAttributeNode(attr);
			if (!((version != null)))
				version = "1.1.0";
			if (version.equals(""))
				version = "1.1.0";
			attr = doc.createAttribute("version");
			attr.setValue(version);
			document.setAttributeNode(attr);
			// ////////////////////Cabecera Xml///////////////////////////////

			// ////////////////////infoTributaria////////////////////////////
			Element infoTrib = doc.createElement("infoTributaria");
			document.appendChild(infoTrib);

			Element ambiente = doc.createElement("ambiente");
			ambiente.appendChild(doc.createTextNode(new Integer(
					informacionTributaria.getAmbiente()).toString()));
			infoTrib.appendChild(ambiente);

			Element tipoEmision = doc.createElement("tipoEmision");
			tipoEmision.appendChild(doc.createTextNode(new Integer(
					informacionTributaria.getTipoEmision()).toString()));
			infoTrib.appendChild(tipoEmision);

			Element razonSocial = doc.createElement("razonSocial");
			razonSocial.appendChild(doc.createTextNode(informacionTributaria
					.getRazonSocial()));
			infoTrib.appendChild(razonSocial);

			Element nombreComercial = doc.createElement("nombreComercial");
			nombreComercial
					.appendChild(doc.createTextNode(informacionTributaria
							.getNombreComercial()));
			infoTrib.appendChild(nombreComercial);

			Element ruc = doc.createElement("ruc");
			ruc.appendChild(doc.createTextNode(informacionTributaria.getRuc()));
			infoTrib.appendChild(ruc);

			Element codDoc = doc.createElement("codDoc");
			codDoc.appendChild(doc.createTextNode(informacionTributaria
					.getCodDoc()));
			infoTrib.appendChild(codDoc);

			Element estab = doc.createElement("estab");
			estab.appendChild(doc.createTextNode(informacionTributaria
					.getEstab()));
			infoTrib.appendChild(estab);

			Element ptoEmi = doc.createElement("ptoEmi");
			ptoEmi.appendChild(doc.createTextNode(informacionTributaria
					.getPtoEmi()));
			infoTrib.appendChild(ptoEmi);

			String secuenciaTrx = "";

			secuenciaTrx = informacionTributaria.getSecuencial();

			Element secuencial = doc.createElement("secuencial");
			secuencial.appendChild(doc.createTextNode(secuenciaTrx));
			infoTrib.appendChild(secuencial);

			Element dirMatriz = doc.createElement("dirMatriz");
			dirMatriz.appendChild(doc.createTextNode(informacionTributaria
					.getDirMatriz()));
			infoTrib.appendChild(dirMatriz);
			// }

			// ////////////////////infoTributaria////////////////////////////
			// ////////////////////infoRetencion////////////////////////////

			Element infoDocu = doc.createElement("infoCompRetencion");
			document.appendChild(infoDocu);

			Element fechaEmision = doc.createElement("fechaEmision");
			fechaEmision.appendChild(doc.createTextNode(infoRetencion
					.getFechaEmision()));
			infoDocu.appendChild(fechaEmision);

			if (infoRetencion.getDirEstablecimiento().length() > 0) {
				Element dirEstablecimiento = doc
						.createElement("dirEstablecimiento");
				dirEstablecimiento.appendChild(doc.createTextNode(infoRetencion
						.getDirEstablecimiento()));
				infoDocu.appendChild(dirEstablecimiento);
			}

			if ((Integer.parseInt(infoRetencion.getContribuyenteEspecial()) > 99)
					&& (Integer.parseInt(infoRetencion
							.getContribuyenteEspecial()) <= 99999)) {
				Element contribuyenteEspecial = doc
						.createElement("contribuyenteEspecial");
				contribuyenteEspecial.appendChild(doc
						.createTextNode(infoRetencion
								.getContribuyenteEspecial()));
				infoDocu.appendChild(contribuyenteEspecial);
			}

			if (infoRetencion.getObligadoContabilidad().length() > 0) {
				Element obligadoContabilidad = doc
						.createElement("obligadoContabilidad");
				obligadoContabilidad
						.appendChild(doc.createTextNode(infoRetencion
								.getObligadoContabilidad()));
				infoDocu.appendChild(obligadoContabilidad);
			}

			if (infoRetencion.getTipoIdentificacionSujetoRetenido() != null) {
				Element tipoIdentificacionSujetoRetenido = doc
						.createElement("tipoIdentificacionSujetoRetenido");
				tipoIdentificacionSujetoRetenido.appendChild(doc
						.createTextNode(infoRetencion
								.getTipoIdentificacionSujetoRetenido()));
				infoDocu.appendChild(tipoIdentificacionSujetoRetenido);
			}

			System.out.println("razonSocialSujetoRetenido::"
					+ infoRetencion.getRazonSocialSujetoRetenido());
			if ((infoRetencion.getRazonSocialSujetoRetenido() != null)
					&& (infoRetencion.getRazonSocialSujetoRetenido().length() > 0)) {
				Element razonSocialSujetoRetenido = doc
						.createElement("razonSocialSujetoRetenido");
				razonSocialSujetoRetenido.appendChild(doc.createTextNode(Util
						.normalizeValue(
								infoRetencion.getRazonSocialSujetoRetenido(),
								300, "razonSocialSujetoRetenido")));
				infoDocu.appendChild(razonSocialSujetoRetenido);
			}

			System.out.println("identificacionSujetoRetenido::"
					+ infoRetencion.getIdentificacionSujetoRetenido());
			if ((infoRetencion.getIdentificacionSujetoRetenido() != null)
					&& (infoRetencion.getIdentificacionSujetoRetenido()
							.length() > 0)) {
				Element identificacionSujetoRetenido = doc
						.createElement("identificacionSujetoRetenido");
				identificacionSujetoRetenido.appendChild(doc
						.createTextNode(infoRetencion
								.getIdentificacionSujetoRetenido()));
				infoDocu.appendChild(identificacionSujetoRetenido);
			}
			
			Element periodoFiscal = doc.createElement("periodoFiscal");
			periodoFiscal.appendChild(doc.createTextNode(infoRetencion
					.getPeriodoFiscal()));
			infoDocu.appendChild(periodoFiscal);
			// ////////////////////////////////////////////////////////
			// ///////////////////////Detalles/////////////////////////
			// ////////////////////////////////////////////////////////
			if (infoRetencion.getListImpuestosRetencion() != null) {
				if (infoRetencion.getListImpuestosRetencion().size() > 0) {

					Element impuestos = doc.createElement("impuestos");
					document.appendChild(impuestos);
					for (int x = 0; x <= infoRetencion
							.getListImpuestosRetencion().size() - 1; x++) {
						ImpuestosRetencion detImpTmp = new ImpuestosRetencion();
						detImpTmp = (ImpuestosRetencion) infoRetencion
								.getListImpuestosRetencion().get(x);
						Element impuesto = doc.createElement("impuesto");
						impuestos.appendChild(impuesto);

						Element codigo = doc.createElement("codigo");
						codigo.appendChild(doc.createTextNode(detImpTmp
								.getCodigo()));
						impuesto.appendChild(codigo);

						if (detImpTmp.getCodigoRetencion() != null) {
							if (detImpTmp.getCodigoRetencion().length() > 0) {
								Element codigoRetencion = doc
										.createElement("codigoRetencion");
								codigoRetencion.appendChild(doc
										.createTextNode(detImpTmp
												.getCodigoRetencion()));
								impuesto.appendChild(codigoRetencion);
							}
						}

						Element baseImponible = doc
								.createElement("baseImponible");
						baseImponible.appendChild(doc.createTextNode(detImpTmp
								.getBaseImponible()));
						impuesto.appendChild(baseImponible);

						Element porcentajeRetener = doc
								.createElement("porcentajeRetener");
						porcentajeRetener.appendChild(doc
								.createTextNode(detImpTmp
										.getPorcentajeRetener()));
						impuesto.appendChild(porcentajeRetener);

						Element valorRetenido = doc
								.createElement("valorRetenido");
						valorRetenido.appendChild(doc.createTextNode(detImpTmp
								.getValorRetenido()));
						impuesto.appendChild(valorRetenido);

						Element codDocSustento = doc
								.createElement("codDocSustento");
						codDocSustento.appendChild(doc.createTextNode(detImpTmp
								.getCodDocSustento()));
						impuesto.appendChild(codDocSustento);

						Element numDocSustento = doc
								.createElement("numDocSustento");
						numDocSustento.appendChild(doc.createTextNode(detImpTmp
								.getNumDocSustento()));
						impuesto.appendChild(numDocSustento);

						Element fechaEmisionDocSustento = doc
								.createElement("fechaEmisionDocSustento");
						fechaEmisionDocSustento.appendChild(doc
								.createTextNode(detImpTmp
										.getFechaEmisionDocSustento()));
						impuesto.appendChild(fechaEmisionDocSustento);
					}

				}
			}
			// INFORMACION ADICIONAL
			Attr attrib = null;
			Element infoAdicional = doc.createElement("infoAdicional");
			document.appendChild(infoAdicional);

			for (int z = 0; z <= infoRetencion.getListInfoAdicional().size() - 1; z++) {
				InfoAdicional infoAdic = new InfoAdicional();
				infoAdic = infoRetencion.getListInfoAdicional().get(z);
				Element campoAdicional = doc.createElement("campoAdicional");
				String dato = Util.normalizeValue(infoAdic.getValor(), 300,
						infoAdic.getNombre().toUpperCase());
				campoAdicional.appendChild(doc
						.createTextNode((dato == null ? "notiene" : (dato
								.equals("") ? "notiene" : dato))));
				infoAdicional.appendChild(campoAdicional);
				attrib = doc.createAttribute("nombre");
				attrib.setValue((infoAdic.getNombre() == null ? "notiene"
						: (infoAdic.getNombre().equals("") ? "notiene"
								: infoAdic.getNombre())));
				campoAdicional.setAttributeNode(attrib);
			}

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			System.out.println("Ruta:" + empresa.getPathCompGenerados()
					+ informacionTributaria.getAmbiente()
					+ informacionTributaria.getRuc()
					+ informacionTributaria.getCodDoc()
					+ informacionTributaria.getEstab()
					+ informacionTributaria.getPtoEmi()
					+ informacionTributaria.getSecuencial() + ".xml");
			File file = new File(empresa.getPathCompGenerados()
					+ informacionTributaria.getAmbiente()
					+ informacionTributaria.getRuc()
					+ informacionTributaria.getCodDoc()
					+ informacionTributaria.getEstab()
					+ informacionTributaria.getPtoEmi()
					+ informacionTributaria.getSecuencial() + ".xml");
			StreamResult result = new StreamResult(file);

			transformer.transform(source, result);

			return true;
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
			throw new Exception(
					"ERRORGENERAXML,crearFactura Error en el Parseo de "
							+ pce.toString() + ",Empresa");
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
			throw new Exception(
					"ERRORGENERAXML,crearFactura Error en el Transformer de "
							+ tfe.toString() + ",Empresa");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"ERRORGENERAXML,crearFactura Error en el Exception de "
							+ e.toString() + ",Empresa");
		}
	}
	
	public FacEmpresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(FacEmpresa empresa) {
		this.empresa = empresa;
	}

	public InfoTributaria getInfoTrib() {
		return informacionTributaria;
	}

	public void setInfoTrib(InfoTributaria infoTrib) {
		this.informacionTributaria = infoTrib;
	}


	public InfoTributaria getInformacionTributaria() {
		return informacionTributaria;
	}


	public void setInformacionTributaria(InfoTributaria informacionTributaria) {
		this.informacionTributaria = informacionTributaria;
	}


	public InfoRetencion getInfoRetencion() {
		return infoRetencion;
	}


	public void setInfoRetencion(InfoRetencion infoRetencion) {
		this.infoRetencion = infoRetencion;
	}

}
