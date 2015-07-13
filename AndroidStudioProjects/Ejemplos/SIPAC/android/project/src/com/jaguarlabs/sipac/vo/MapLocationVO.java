package com.jaguarlabs.sipac.vo;

import android.database.Cursor;

public class MapLocationVO implements ICursorDecoder {

	
	private String idUbicacion;
	private String nombre;
	private int retenedor;
	private int uPago;
	private int prospectos;
	private double altitude;
	private double latitude;
	private String direccion;
	private String zona;
	
	public MapLocationVO(double altitude, double latitude) {
		super();
		this.altitude = altitude;
		this.latitude = latitude;
	}
	
	public MapLocationVO(String idUbicacion,  String zona, String nombre, int retenedor, int uPago,
			int prospectos, double altitude, double latitude, String direccion) {
		super();
		this.idUbicacion = idUbicacion;
		this.zona = zona;
		this.nombre = nombre;
		this.retenedor = retenedor;
		this.uPago = uPago;
		this.prospectos = prospectos;
		this.altitude = altitude;
		this.latitude = latitude;
		this.direccion = direccion;
		
	}

	@Override
	public void decode(Cursor pCursor) {
	}

	@Override
	public long getId() {
		return 0;
	}

	@Override
	public void setId(long pId) {
	}

	public String getIdUbicacion(){
		return idUbicacion;
	}
	
	public void setIdUbicacion( String idUbicacion ){
		this.idUbicacion = idUbicacion;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getRetenedor() {
		return retenedor;
	}

	public void setRetenedor(int retenedor) {
		this.retenedor = retenedor;
	}

	public int getuPago() {
		return uPago;
	}

	public void setuPago(int uPago) {
		this.uPago = uPago;
	}

	public int getProspectos() {
		return prospectos;
	}

	public void setProspectos(int prospectos) {
		this.prospectos = prospectos;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public String getDireccion(){
		return direccion;
	}
	
	public void setDireccion( String direccion ){
		this.direccion = direccion;
	}
	
	public String getZona(){
		return zona;
	}
	
	public void setZona( String zona ){
		this.zona = zona;
	}

	public boolean validFields() {
			if(getNombre() != "" && altitude != 0 && latitude != 0)
				return true;
		return false;
	}

}
