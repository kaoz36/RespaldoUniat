package com.jaguarlabs.sipac.vo;

import android.database.Cursor;

public class EdadVO implements ICursorDecoder {
	private long edad;
	private float bas;
	private float cii;
	private float cma;
	private float tiba;
	private float bcat;
	private float gfa;
	private float ge;
	private float bit;
	private float basn;
	private float cman;
	private float tiban;
	private float gen;
	private float cpcony;
	private float ciin;
	private float bcatn;
	private float cpconyn;
	private float bitn;
	private float basf;
	private float basfn;

	@Override
	public void decode(Cursor pCursor) {
		setId(pCursor.getLong(0));
		setBas(pCursor.getFloat(1));
		setCii(pCursor.getFloat(2));
		setCma(pCursor.getFloat(3));
		setTiba(pCursor.getFloat(4));
		setBcat(pCursor.getFloat(5));
		setGfa(pCursor.getFloat(6));
		setGe(pCursor.getFloat(7));
		setBit(pCursor.getFloat(8));
		setBasfn(pCursor.getFloat(9));
		setCman(pCursor.getFloat(10));
		setTiban(pCursor.getFloat(11));
		setGen(pCursor.getFloat(12));
		setCpcony(pCursor.getFloat(13));
		setCiin(pCursor.getFloat(14));
		setBcatn(pCursor.getFloat(15));
		setCpconyn(pCursor.getFloat(16));
		setBitn(pCursor.getFloat(17));
		setBasf(pCursor.getFloat(18));
		setBasn(pCursor.getFloat(19));
	}

	public float getCiin() {
		return ciin;
	}

	public void setCiin(float ciin) {
		this.ciin = ciin;
	}

	public float getBcatn() {
		return bcatn;
	}

	public void setBcatn(float bcatn) {
		this.bcatn = bcatn;
	}

	public float getCpconyn() {
		return cpconyn;
	}

	public void setCpconyn(float cpconyn) {
		this.cpconyn = cpconyn;
	}

	@Override
	public long getId() {
		return edad;
	}

	@Override
	public void setId(long pId) {
		edad = pId;
	}

	public float getGen() {
		return gen;
	}

	public void setGen(float gen) {
		this.gen = gen;
	}
	
	public long getEdad() {
		return edad;
	}

	public void setEdad(long edad) {
		this.edad = edad;
	}

	public float getBas() {
		return bas;
	}

	public void setBas(float bas) {
		this.bas = bas;
	}

	public float getCii() {
		return cii;
	}

	public void setCii(float cii) {
		this.cii = cii;
	}

	public float getCma() {
		return cma;
	}

	public void setCma(float cma) {
		this.cma = cma;
	}

	public float getTiba() {
		return tiba;
	}

	public void setTiba(float tiba) {
		this.tiba = tiba;
	}

	public float getBcat() {
		return bcat;
	}

	public void setBcat(float bcat) {
		this.bcat = bcat;
	}

	public float getGfa() {
		return gfa;
	}

	public void setGfa(float gfa) {
		this.gfa = gfa;
	}

	public float getGe() {
		return ge;
	}

	public void setGe(float ge) {
		this.ge = ge;
	}

	public float getBit() {
		return bit;
	}

	public void setBit(float bit) {
		this.bit = bit;
	}

	public float getBasn() {
		return basn;
	}

	public void setBasn(float basn) {
		this.basn = basn;
	}

	public float getCman() {
		return cman;
	}

	public void setCman(float cman) {
		this.cman = cman;
	}

	public float getTiban() {
		return tiban;
	}

	public void setTiban(float tiban) {
		this.tiban = tiban;
	}

	public float getCpcony() {
		return cpcony;
	}

	public void setCpcony(float cpcony) {
		this.cpcony = cpcony;
	}
	
	public String toString(){
		return "" + edad;
		
	}

	public float getBitn() {
		return bitn;
	}

	public void setBitn(float bitn) {
		this.bitn = bitn;
	}

	public float getBasf() {
		return basf;
	}

	public void setBasf(float basf) {
		this.basf = basf;
	}

	public float getBasfn() {
		return basfn;
	}

	public void setBasfn(float basfn) {
		this.basfn = basfn;
	}
}
