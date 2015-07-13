package com.jaguarlabs.sipaccel.vo;

import android.database.Cursor;

public class ServicioGralVO implements  ICursorDecoder {
	private long id;
	private String id_poliza;
	private String agente_serv;
	private String id_serv;
	private String desc_serv;
	private String fecha_serv;
	private String orden_pago;
	private String monto;
	
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
		setAgente_serv(pCursor.getString(2));
		setId_serv(pCursor.getString(3));
		setDesc_serv(pCursor.getString(4));
		setFecha_serv(pCursor.getString(5));
		setOrden_pago(pCursor.getString(6));
		setMonto(pCursor.getString(7));
		
	}
	public String getId_poliza() {
		return id_poliza;
	}
	public void setId_poliza(String id_poliza) {
		this.id_poliza = id_poliza;
	}
	public String getAgente_serv() {
		return agente_serv;
	}
	public void setAgente_serv(String agente_serv) {
		this.agente_serv = agente_serv;
	}
	public String getId_serv() {
		return id_serv;
	}
	public void setId_serv(String id_serv) {
		this.id_serv = id_serv;
	}
	public String getDesc_serv() {
		return desc_serv;
	}
	public void setDesc_serv(String desc_serv) {
		this.desc_serv = desc_serv;
	}
	public String getFecha_serv() {
		return fecha_serv;
	}
	public void setFecha_serv(String fecha_serv) {
		this.fecha_serv = fecha_serv;
	}
	public String getOrden_pago() {
		return orden_pago;
	}
	public void setOrden_pago(String orden_pago) {
		this.orden_pago = orden_pago;
	}
	public String getMonto() {
		return monto;
	}
	public void setMonto(String monto) {
		this.monto = monto;
	}
	
	
}
