package com.jaguarlabs.sipaccel.vo;

import android.database.Cursor;

public class CoberturaVO implements  ICursorDecoder{
	private long id;
	private String id_poliza;
	private String prima_cob;
	private String prima_ext_cob;
	private String suma_cob;
	private String clave_cob;
	
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
		setPrima_cob(pCursor.getString(2));
		setPrima_ext_cob(pCursor.getString(3));
		setSuma_cob(pCursor.getString(4));
		setClave_cob(pCursor.getString(5));
		
	}
	public String getId_poliza() {
		return id_poliza;
	}
	public void setId_poliza(String id_poliza) {
		this.id_poliza = id_poliza;
	}
	public String getPrima_cob() {
		return prima_cob;
	}
	public void setPrima_cob(String prima_cob) {
		this.prima_cob = prima_cob;
	}
	public String getPrima_ext_cob() {
		return prima_ext_cob;
	}
	public void setPrima_ext_cob(String prima_ext_cob) {
		this.prima_ext_cob = prima_ext_cob;
	}
	public String getSuma_cob() {
		return suma_cob;
	}
	public void setSuma_cob(String suma_cob) {
		this.suma_cob = suma_cob;
	}
	public String getClave_cob() {
		return clave_cob;
	}
	public void setClave_cob(String clave_cob) {
		this.clave_cob = clave_cob;
	}

}
