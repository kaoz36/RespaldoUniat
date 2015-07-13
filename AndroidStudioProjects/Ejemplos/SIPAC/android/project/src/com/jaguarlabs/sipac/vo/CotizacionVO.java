package com.jaguarlabs.sipac.vo;

public class CotizacionVO {

	private String fullName;
	private ProfesionVO profesion;
	private boolean _fumador;
	private boolean dobas;
	private boolean dobit;
	private boolean docii;
	private boolean docma;
	private boolean dotiba;
	private boolean docat;
	private boolean dogfa;
	private boolean doge;
	private boolean doconyuge;
	private boolean dobacy;
	private boolean dogfc;
	private boolean dogpc;
	private boolean dogfh;
	private boolean doPFT;
	private boolean doccom;
	private boolean docatcom1;
	private boolean docatcom2;
	private boolean docatcom3;
	
	private int sexo;
	
	private ValueVO<Integer> catcompsexo;
	private ValueVO<Integer> catcomp1sexo;
	private ValueVO<Integer> catcomp2sexo;
	private ValueVO<Integer> catcomp3sexo;
	private EdadVO edadReal;
	private EdadVO edadCalculo;
	private EdadVO edadConyuge;
	private EdadVO edadcCom;
	private EdadVO edadComp1;
	private EdadVO edadComp2;
	private EdadVO edadComp3;

	private int nHijos;
	
	private double primaExcedente;
	private double primaTotal;
	private double bas;
	private double bit;
	private double cii;
	private double cma;
	private double tiba;
	private double cat;
	private double gfa;
	private double ge;
	
	private double bacy;
	private double gfc;
	private double gpc;	
	private double gfh;	
	private double ccomp;
	private double ccat1;
	private double ccat2;
	private double ccat3;
	
	private ValueVO<Double> selectedGFH;
	
	private String formaPago;
	
	public CotizacionVO(String fullName, ProfesionVO profesion,
			boolean _fumador, boolean dobas, boolean dobit, boolean docii,
			boolean docma, boolean dotiba, boolean docat, boolean dogfa,
			boolean doge, boolean doconyuge, boolean dobacy, boolean dogfc, boolean dogpc,
			boolean dogfh, boolean doPFT, boolean doccom, boolean docatcom1,
			boolean docatcom2, boolean docatcom3, int sexo, ValueVO<Integer> catcompsexo,
			ValueVO<Integer> catcomp1sexo, ValueVO<Integer> catcomp2sexo,
			ValueVO<Integer> catcomp3sexo, EdadVO edadReal, EdadVO edadCalculo,	EdadVO edadConyuge, 
			EdadVO edadcCom, EdadVO edadComp1, EdadVO edadComp2, EdadVO edadComp3, int nHijos,
			double primaExcedente, double primaTotal, double bas, double bit,
			double cii, double cma, double tiba, double cat, double gfa,
			double ge, double bacy, double gfc, double gpc, double gfh,
			double ccomp, double ccat1, double ccat2, double ccat3, ValueVO<Double> selectedGFH, String formaPago) {
		super();
		this.fullName = fullName;
		this.profesion = profesion;
		this._fumador = _fumador;
		this.dobas = dobas;
		this.dobit = dobit;
		this.docii = docii;
		this.docma = docma;
		this.dotiba = dotiba;
		this.docat = docat;
		this.dogfa = dogfa;
		this.doge = doge;
		this.doconyuge = doconyuge;
		this.dobacy = dobacy;
		this.dogfc = dogfc;
		this.dogpc = dogpc;
		this.dogfh = dogfh;
		this.doPFT = doPFT;
		this.doccom = doccom;
		this.docatcom1 = docatcom1;
		this.docatcom2 = docatcom2;
		this.docatcom3 = docatcom3;
		this.sexo = sexo;
		this.catcompsexo = catcompsexo;
		this.catcomp1sexo = catcomp1sexo;
		this.catcomp2sexo = catcomp2sexo;
		this.catcomp3sexo = catcomp3sexo;
		this.edadReal = edadReal;
		this.edadCalculo = edadCalculo;
		this.edadConyuge = edadConyuge;
		this.edadcCom = edadcCom;
		this.edadComp1 = edadComp1;
		this.edadComp2 = edadComp2;
		this.edadComp3 = edadComp3;
		this.nHijos = nHijos;
		this.primaExcedente = primaExcedente;
		this.primaTotal = primaTotal;
		this.bas = bas;
		this.bit = bit;
		this.cii = cii;
		this.cma = cma;
		this.tiba = tiba;
		this.cat = cat;
		this.gfa = gfa;
		this.ge = ge;
		this.bacy = bacy;
		this.gfc = gfc;
		this.gpc = gpc;
		this.gfh = gfh;
		this.ccomp = ccomp;
		this.ccat1 = ccat1;
		this.ccat2 = ccat2;
		this.ccat3 = ccat3;
		this.selectedGFH = selectedGFH;
		this.formaPago = formaPago;
	}

	public String getFullName() {
		return fullName;
	}

	public ProfesionVO getProfesion() {
		return profesion;
	}

	public boolean is_fumador() {
		return _fumador;
	}

	public boolean isDobas() {
		return dobas;
	}

	public boolean isDobit() {
		return dobit;
	}

	public boolean isDocii() {
		return docii;
	}

	public boolean isDocma() {
		return docma;
	}

	public boolean isDotiba() {
		return dotiba;
	}

	public boolean isDocat() {
		return docat;
	}

	public boolean isDogfa() {
		return dogfa;
	}

	public boolean isDoge() {
		return doge;
	}

	public boolean isDoconyuge() {
		return doconyuge;
	}

	public boolean isDobacy() {
		return dobacy;
	}

	public boolean isDogfc() {
		return dogfc;
	}

	public boolean isDogpc() {
		return dogpc;
	}

	public boolean isDogfh() {
		return dogfh;
	}

	public boolean isDoPFT() {
		return doPFT;
	}

	public boolean isDoccom(){
		return doccom;
	}
	
	public boolean isDocatcomp1(){
		return docatcom1;
	}
	
	public boolean isDocatcomp2(){
		return docatcom2;
	}
	
	public boolean isDocatcomp3(){
		return docatcom3;
	}
	
	public int getSexo() {
		return sexo;
	}

	public ValueVO<Integer> getCatcompsexo() {
		return catcompsexo;
	}

	public ValueVO<Integer> getCatcomp1sexo() {
		return catcomp1sexo;
	}

	public ValueVO<Integer> getCatcomp2sexo() {
		return catcomp2sexo;
	}

	public ValueVO<Integer> getCatcomp3sexo() {
		return catcomp3sexo;
	}

	public EdadVO getEdadReal() {
		return edadReal;
	}

	public EdadVO getEdadCalculo() {
		return edadCalculo;
	}

	public EdadVO getEdadConyuge() {
		return edadConyuge;
	}

	public EdadVO getEdadcComp() {
		return edadcCom;
	}
	
	public EdadVO getEdadComp1() {
		return edadComp1;
	}

	public EdadVO getEdadComp2() {
		return edadComp2;
	}

	public EdadVO getEdadComp3() {
		return edadComp3;
	}

	public int getnHijos() {
		return nHijos;
	}

	public double getPrimaExcedente() {
		return primaExcedente;
	}

	public double getPrimaTotal() {
		return primaTotal;
	}

	public double getBas() {
		return bas;
	}

	public double getBit() {
		return bit;
	}

	public double getCii() {
		return cii;
	}

	public double getCma() {
		return cma;
	}

	public double getTiba() {
		return tiba;
	}

	public double getCat() {
		return cat;
	}

	public double getGfa() {
		return gfa;
	}

	public double getGe() {
		return ge;
	}

	public double getBacy() {
		return bacy;
	}

	public double getGfc() {
		return gfc;
	}

	public double getGpc() {
		return gpc;
	}

	public double getGfh() {
		return gfh;
	}

	public double getCcomp() {
		return ccomp;
	}

	public double getCcat1() {
		return ccat1;
	}

	public double getCcat2() {
		return ccat2;
	}

	public double getCcat3() {
		return ccat3;
	}

	public ValueVO<Double> getSelectedGFH() {
		return selectedGFH;
	}
	
	public String getFormaPago(){
		return formaPago;
	}
	
}
