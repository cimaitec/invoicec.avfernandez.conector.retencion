package com.cimait.DAO;

public class Transaction {
		
		private String establecimiento;
		private String ptoEmision;		 		
		private String secuencial;
		private String tipoDocumento;
		private String sucursal;
		private String fechaEmision;
		

				
		public Transaction(String establecimiento,
						   String ptoEmision,						  		
						   String secuencial,
						   String tipoDocumento,
						   String sucursal,
						   String fechaEmision
						   ){
			this.secuencial=secuencial;
			this.establecimiento=establecimiento;
			this.ptoEmision=ptoEmision;
			this.tipoDocumento=tipoDocumento;
			this.fechaEmision=fechaEmision;
			this.sucursal = sucursal;
		}
		
		public void mostrar(){			
			System.out.println("establecimiento:" + establecimiento); 			 
			System.out.println("ptoEmision:" + ptoEmision);
			System.out.println("secuencial:" + secuencial);
			System.out.println("Fecha Emision:" + fechaEmision);
			System.out.println("TipoDocumento:" + tipoDocumento);
			System.out.println("sucursal:" + sucursal);
			System.out.println("------------------------------------");
		}

		public String getEstablecimiento() {
			return establecimiento;
		}

		public void setEstablecimiento(String establecimiento) {
			this.establecimiento = establecimiento;
		}

		public String getPtoEmision() {
			return ptoEmision;
		}

		public void setPtoEmision(String ptoEmision) {
			this.ptoEmision = ptoEmision;
		}

		public String getSecuencial() {
			return secuencial;
		}

		public void setSecuencial(String secuencial) {
			this.secuencial = secuencial;
		}

		public String getTipoDocumento() {
			return tipoDocumento;
		}

		public void setTipoDocumento(String tipoDocumento) {
			this.tipoDocumento = tipoDocumento;
		}

		public String getFechaEmision() {
			return fechaEmision;
		}

		public void setFechaEmision(String fechaEmision) {
			this.fechaEmision = fechaEmision;
		}

		public String getSucursal() {
			return sucursal;
		}

		public void setSucursal(String sucursal) {
			this.sucursal = sucursal;
		}
		
		
		
}