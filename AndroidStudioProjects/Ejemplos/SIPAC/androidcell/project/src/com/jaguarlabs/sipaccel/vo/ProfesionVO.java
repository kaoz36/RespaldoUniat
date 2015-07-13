package com.jaguarlabs.sipaccel.vo;

import android.database.Cursor;

public class ProfesionVO implements ICursorDecoder {

	private long id;
	private String nombre;
	private float millar;
	private float accidente;
	private float invalidez;
	
	
	@Override
	public void decode(Cursor pCursor) {		
		setId(pCursor.getLong(0));
		setNombre(pCursor.getString(1));
		setMillar(pCursor.getFloat(2));
		setAccidente(pCursor.getFloat(3));
		setInvalidez(pCursor.getFloat(4));
	}
	
	public ProfesionVO() {
		super();
	}

	
	public ProfesionVO(String nombre, float millar, float accidente,
			float invalidez) {
		super();
		this.nombre = nombre;
		this.millar = millar;
		this.accidente = accidente;
		this.invalidez = invalidez;
	}
	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(long pId) {
		// TODO Auto-generated method stub
		id = pId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public float getMillar() {
		return millar;
	}

	public void setMillar(float millar) {
		this.millar = millar;
	}

	public float getAccidente() {
		return accidente;
	}

	public void setAccidente(float accidente) {
		this.accidente = accidente;
	}

	public float getInvalidez() {
		return invalidez;
	}

	public void setInvalidez(float invalidez) {
		this.invalidez = invalidez;
	}
	
	public String toString(){
		return nombre;
	}

}
