package com.jaguarlabs.sipaccel.vo;

import android.database.Cursor;

public class ServicioVentaVO implements  ICursorDecoder {
	
	private long id;
	private String id_poliza;
	private String id_agente_venta;
	private String fec_env_venta;
	private String fec_emi_venta;
	private String fec_ent_venta;
	private String fec_acc_venta;
	private String prima_ini_venta;
	private String prima_emi_venta;
	private String prima_tot_venta;
	private String serv_venta;
	private String tipo_negocio;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	
	
	@Override
	public void decode(Cursor pCursor) {
		setId(pCursor.getLong(0));
		setId_poliza(pCursor.getString(1));
		setId_agente_venta(pCursor.getString(2));
		setFec_env_venta(pCursor.getString(3));
		setFec_emi_venta(pCursor.getString(4));
		setFec_ent_venta(pCursor.getString(5));
		setFec_acc_venta(pCursor.getString(6));
		setPrima_ini_venta(pCursor.getString(7));
		setPrima_emi_venta(pCursor.getString(8));
		setPrima_tot_venta(pCursor.getString(9));
		setServ_venta(pCursor.getString(10));
		setTipo_negocio(pCursor.getString(11));
		
		
	}

	public String getId_poliza() {
		return id_poliza;
	}

	public void setId_poliza(String id_poliza) {
		this.id_poliza = id_poliza;
	}

	public String getId_agente_venta() {
		return id_agente_venta;
	}

	public void setId_agente_venta(String id_agente_venta) {
		this.id_agente_venta = id_agente_venta;
	}

	public String getFec_env_venta() {
		return fec_env_venta;
	}

	public void setFec_env_venta(String fec_env_venta) {
		this.fec_env_venta = fec_env_venta;
	}

	public String getFec_emi_venta() {
		return fec_emi_venta;
	}

	public void setFec_emi_venta(String fec_emi_venta) {
		this.fec_emi_venta = fec_emi_venta;
	}

	public String getFec_ent_venta() {
		return fec_ent_venta;
	}

	public void setFec_ent_venta(String fec_ent_venta) {
		this.fec_ent_venta = fec_ent_venta;
	}

	public String getFec_acc_venta() {
		return fec_acc_venta;
	}

	public void setFec_acc_venta(String fec_acc_venta) {
		this.fec_acc_venta = fec_acc_venta;
	}

	public String getPrima_ini_venta() {
		return prima_ini_venta;
	}

	public void setPrima_ini_venta(String prima_ini_venta) {
		this.prima_ini_venta = prima_ini_venta;
	}

	public String getPrima_emi_venta() {
		return prima_emi_venta;
	}

	public void setPrima_emi_venta(String prima_emi_venta) {
		this.prima_emi_venta = prima_emi_venta;
	}

	public String getPrima_tot_venta() {
		return prima_tot_venta;
	}

	public void setPrima_tot_venta(String prima_tot_venta) {
		this.prima_tot_venta = prima_tot_venta;
	}

	public String getServ_venta() {
		return serv_venta;
	}

	public void setServ_venta(String serv_venta) {
		this.serv_venta = serv_venta;
	}

	public String getTipo_negocio() {
		return tipo_negocio;
	}

	public void setTipo_negocio(String tipo_negocio) {
		this.tipo_negocio = tipo_negocio;
	}
	
}
