package com.jaguarlabs.sipac.vo;

import android.database.Cursor;

public class ServAffectVO implements  ICursorDecoder {
	
	private long id;
	private String id_poliza;
	private String id_serv;
	private String mas_men_serv;
	private String prima_serv;
	private String fec_emi_serv;
	private String plan_serv;
	
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
		setId_serv(pCursor.getString(2));
		setMas_men_serv(pCursor.getString(3));
		setPrima_serv(pCursor.getString(4));
		setFec_emi_serv(pCursor.getString(5));
		setPlan_serv(pCursor.getString(6));
	}
	
	public String getId_poliza() {
		return id_poliza;
	}
	public void setId_poliza(String id_poliza) {
		this.id_poliza = id_poliza;
	}
	public String getId_serv() {
		return id_serv;
	}
	public void setId_serv(String id_serv) {
		this.id_serv = id_serv;
	}
	public String getMas_men_serv() {
		return mas_men_serv;
	}
	public void setMas_men_serv(String mas_men_serv) {
		this.mas_men_serv = mas_men_serv;
	}
	public String getPrima_serv() {
		return prima_serv;
	}
	public void setPrima_serv(String prima_serv) {
		this.prima_serv = prima_serv;
	}
	public String getFec_emi_serv() {
		return fec_emi_serv;
	}
	public void setFec_emi_serv(String fec_emi_serv) {
		this.fec_emi_serv = fec_emi_serv;
	}
	public String getPlan_serv() {
		return plan_serv;
	}
	public void setPlan_serv(String plan_serv) {
		this.plan_serv = plan_serv;
	}
	
	
}
