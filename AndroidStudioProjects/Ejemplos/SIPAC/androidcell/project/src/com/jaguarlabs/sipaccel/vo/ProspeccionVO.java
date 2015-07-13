package com.jaguarlabs.sipaccel.vo;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.database.Cursor;

public class ProspeccionVO implements ICursorDecoder {
	
	private long id_prospeccion;
	private String estado_civil;
	private String fecha_nac;
	private String fuma;
	private String sexo;
	private String zona_prom;
	private String Poliza;
	private String fe_emision;
	private String prima_emi;
	private String RFC;
	private String Nombre;
	private String concepto;
	private String subcartera;
	private String ret;
	private String No_empleado;
	private String plan;
	private String forma_pago;
	private String reserva;
	private String signo_reserva;
	private String inversion;
	private String signo_inversion;
	private String ultimo_pago;
	private String incre20;
	private String bas;
	private String sabas;
	private String cma;
	private String tiba;
	private String cii;
	private String bac;
	private String bcat;
	private String bcatplus;
	private String bacy;
	private String gfa;
	private String gfc;
	private String gfh;
	private String bit;
	private String pft;
	private String ge;
	private String exc;
	private String ultimo_incr;
	private String fecha_ret_reserv;
	private String importe_ret_reserv;
	private String fecha_ret_inv;
	private String importe_ret_inv;


	@Override
	public void decode(Cursor pCursor) {
		setId(pCursor.getLong(0));
		setEstado_civil(pCursor.getString(1));
		setFecha_nac(pCursor.getString(2));
		setFuma(pCursor.getString(3));
		setSexo(pCursor.getString(4));
		setZona_prom(pCursor.getString(5));
		setPoliza(pCursor.getString(6));
		setFe_emision(pCursor.getString(7));
		setPrima_emi(pCursor.getString(8));
		setRFC(pCursor.getString(9));
		setNombre(pCursor.getString(10));
		setConcepto(pCursor.getString(11));
		setSubcartera(pCursor.getString(12));
		setRet(pCursor.getString(13));
		setNo_empleado(pCursor.getString(14));
		setPlan(pCursor.getString(15));
		setForma_pago(pCursor.getString(16));
		setReserva(pCursor.getString(17));
		setSigno_reserva(pCursor.getString(18));
		setInversion(pCursor.getString(19));
		setSigno_inversion(pCursor.getString(20));
		setUltimo_pago(pCursor.getString(21));
		setIncre20(pCursor.getString(22));
		setBas(pCursor.getString(23));
		setSabas(pCursor.getString(24));
		setCma(pCursor.getString(25));
		setTiba(pCursor.getString(26));
		setCii(pCursor.getString(27));
		setBac(pCursor.getString(28));
		setBcat(pCursor.getString(29));
		setBcatplus(pCursor.getString(30));
		setBacy(pCursor.getString(31));
		setGfa(pCursor.getString(32));
		setGfc(pCursor.getString(33));
		setGfh(pCursor.getString(34));
		setBit(pCursor.getString(35));
		setPft(pCursor.getString(36));
		setGe(pCursor.getString(37));
		setExc(pCursor.getString(38));
		setUltimo_incr(pCursor.getString(39));
		setFecha_ret_reserv(pCursor.getString(40));
		setImporte_ret_reserv(pCursor.getString(41));
		setFecha_ret_inv(pCursor.getString(42));
		setImporte_ret_inv(pCursor.getString(43));
	}

	@SuppressLint("SimpleDateFormat")
	public String encodeHTML(String pClass){
		SimpleDateFormat fomatter = new SimpleDateFormat("dd-mm-yyyy");
		return "<tr class='"+pClass+"'>" +
				"<td>"+getPlan()+"</td>" +
				"<td>"+getPoliza()+"</td>"+
				"<td>"+getNo_empleado()+"</td>"+
				"<td>"+getRFC()+"</td>"+
				"<td>"+getNombre()+"</td>"+
//				"<td>"+fomatter.format( getFecha_nac() )+"</td>"+
				"<td>"+getFecha_nac() +"</td>"+
				"<td>"+getFuma()+"</td>"+
				"<td>"+getSexo()+"</td>"+
				"<td>"+getEstado_civil()+"</td>"+
				"<td>"+getPrima_emi()+"</td>"+
				"<td>"+getSabas() + "</td>"+
				"<td>"+(getCma().equals("1") ? "CMA" : "") + "</td>"+
				"<td>"+(getTiba().equals("1") ? "TIBA" : "") + "</td>"+
				"<td>"+(getCii().equals("1") ? "CII" : "") + "</td>"+
				"<td>"+(getBac().equals("1") ? "BAC" : "") + "</td>"+
				"<td>"+(getBcat().equals("1") ? "CAT" : "") + "</td>"+
				"<td>"+(getBacy().equals("1") ? "BACY" : "") + "</td>"+
				"<td>"+(getGfa().equals("1") ? "GFA" : "") + "</td>"+
				"<td>"+(getBit().equals("1") ? "BIT" : "") + "</td>"+
				"<td>"+getExc()+"</td>"+
				"<td>"+(getGfc().equals("1") ? "GFC" : "") + "</td>"+
				"<td>"+(getGfh().equals("1") ? "GFH" : "") + "</td>"+
				"<td>"+(getGe().equals("1") ? "GE" : "") + "</td>"+
				"<td>"+(getBcatplus().equals("1") ? "CATP" : "") + "</td>"+
				"<td>"+getZona_prom()+"</td>"+
				"<td>"+getConcepto()+"</td>"+
				"<td>"+getSubcartera()+"</td>"+
				"<td>"+getRet()+"</td>"+
				"<td>"+getForma_pago()+"</td>"+
				"<td>"+getSigno_reserva()+"</td>"+
				"<td>"+getReserva()+"</td>"+
				"<td>"+getUltimo_incr()+"</td>"+
//				"<td>"+fomatter.format( getFe_emision() )+"</td>"+
//				"<td>"+fomatter.format( getUltimo_pago() )+"</td>"+
//				"<td>"+fomatter.format( getFecha_ret_reserv() )+"</td>"+
				"<td>"+getFe_emision() +"</td>"+
				"<td>"+getUltimo_pago() +"</td>"+
				"<td>"+getFecha_ret_reserv()+"</td>"+
				"<td>"+getImporte_ret_inv()+"</td>"+
				"<td>"+getSigno_inversion()+"</td>"+
				"<td>"+getInversion()+"</td>"+
				"<td>"+getIncre20()+"</td>"+
				"</tr>";		
	}
	
	public long getId() {
		return id_prospeccion;
	}


	public void setId(long id_prospeccion) {
		this.id_prospeccion = id_prospeccion;
	}


	public String getEstado_civil() {
		return estado_civil;
	}


	public void setEstado_civil(String estado_civil) {
		this.estado_civil = estado_civil;
	}


	public String getFecha_nac() {
		return fecha_nac;
	}


	public void setFecha_nac(String fecha_nac) {
		this.fecha_nac = fecha_nac;
	}


	public String getFuma() {
		return fuma;
	}


	public void setFuma(String fuma) {
		this.fuma = fuma;
	}


	public String getSexo() {
		return sexo;
	}


	public void setSexo(String sexo) {
		this.sexo = sexo;
	}


	public String getZona_prom() {
		return zona_prom;
	}


	public void setZona_prom(String zona_prom) {
		this.zona_prom = zona_prom;
	}


	public String getPoliza() {
		return Poliza;
	}


	public void setPoliza(String poliza) {
		Poliza = poliza;
	}


	public String getFe_emision() {
		return fe_emision;
	}


	public void setFe_emision(String fe_emision) {
		this.fe_emision = fe_emision;
	}


	public String getPrima_emi() {
		return prima_emi;
	}


	public void setPrima_emi(String prima_emi) {
		this.prima_emi = prima_emi;
	}


	public String getRFC() {
		return RFC;
	}


	public void setRFC(String rFC) {
		RFC = rFC;
	}


	public String getNombre() {
		return Nombre;
	}


	public void setNombre(String nombre) {
		Nombre = nombre;
	}


	public String getConcepto() {
		return concepto;
	}


	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}


	public String getSubcartera() {
		return subcartera;
	}


	public void setSubcartera(String subcartera) {
		this.subcartera = subcartera;
	}


	public String getRet() {
		return ret;
	}


	public void setRet(String ret) {
		this.ret = ret;
	}


	public String getNo_empleado() {
		return No_empleado;
	}


	public void setNo_empleado(String no_empleado) {
		No_empleado = no_empleado;
	}


	public String getPlan() {
		return plan;
	}


	public void setPlan(String plan) {
		this.plan = plan;
	}


	public String getForma_pago() {
		return forma_pago;
	}


	public void setForma_pago(String forma_pago) {
		this.forma_pago = forma_pago;
	}


	public String getReserva() {
		return reserva;
	}


	public void setReserva(String reserva) {
		this.reserva = reserva;
	}


	public String getSigno_reserva() {
		return signo_reserva;
	}


	public void setSigno_reserva(String signo_reserva) {
		this.signo_reserva = signo_reserva;
	}


	public String getInversion() {
		return inversion;
	}


	public void setInversion(String inversion) {
		this.inversion = inversion;
	}


	public String getSigno_inversion() {
		return signo_inversion;
	}


	public void setSigno_inversion(String signo_inversion) {
		this.signo_inversion = signo_inversion;
	}


	public String getUltimo_pago() {
		return ultimo_pago;
	}


	public void setUltimo_pago(String ultimo_pago) {
		this.ultimo_pago = ultimo_pago;
	}


	public String getIncre20() {
		return incre20;
	}


	public void setIncre20(String incre20) {
		this.incre20 = incre20;
	}


	public String getBas() {
		return bas;
	}


	public void setBas(String bas) {
		this.bas = bas;
	}


	public String getSabas() {
		return sabas;
	}


	public void setSabas(String sabas) {
		this.sabas = sabas;
	}


	public String getCma() {
		return cma;
	}


	public void setCma(String cma) {
		this.cma = cma;
	}


	public String getTiba() {
		return tiba;
	}


	public void setTiba(String tiba) {
		this.tiba = tiba;
	}


	public String getCii() {
		return cii;
	}


	public void setCii(String cii) {
		this.cii = cii;
	}


	public String getBac() {
		return bac;
	}


	public void setBac(String bac) {
		this.bac = bac;
	}


	public String getBcat() {
		return bcat;
	}


	public void setBcat(String bcat) {
		this.bcat = bcat;
	}


	public String getBcatplus() {
		return bcatplus;
	}


	public void setBcatplus(String bcatplus) {
		this.bcatplus = bcatplus;
	}


	public String getBacy() {
		return bacy;
	}


	public void setBacy(String bacy) {
		this.bacy = bacy;
	}


	public String getGfa() {
		return gfa;
	}


	public void setGfa(String gfa) {
		this.gfa = gfa;
	}


	public String getGfc() {
		return gfc;
	}


	public void setGfc(String gfc) {
		this.gfc = gfc;
	}


	public String getGfh() {
		return gfh;
	}


	public void setGfh(String gfh) {
		this.gfh = gfh;
	}


	public String getBit() {
		return bit;
	}


	public void setBit(String bit) {
		this.bit = bit;
	}


	public String getPft() {
		return pft;
	}


	public void setPft(String pft) {
		this.pft = pft;
	}


	public String getGe() {
		return ge;
	}


	public void setGe(String ge) {
		this.ge = ge;
	}


	public String getExc() {
		return exc;
	}


	public void setExc(String exc) {
		this.exc = exc;
	}


	public String getUltimo_incr() {
		return ultimo_incr;
	}


	public void setUltimo_incr(String ultimo_incr) {
		this.ultimo_incr = ultimo_incr;
	}


	public String getFecha_ret_reserv() {
		return fecha_ret_reserv;
	}


	public void setFecha_ret_reserv(String fecha_ret_reserv) {
		this.fecha_ret_reserv = fecha_ret_reserv;
	}


	public String getImporte_ret_reserv() {
		return importe_ret_reserv;
	}


	public void setImporte_ret_reserv(String importe_ret_reserv) {
		this.importe_ret_reserv = importe_ret_reserv;
	}


	public String getFecha_ret_inv() {
		return fecha_ret_inv;
	}


	public void setFecha_ret_inv(String fecha_ret_inv) {
		this.fecha_ret_inv = fecha_ret_inv;
	}


	public String getImporte_ret_inv() {
		return importe_ret_inv;
	}


	public void setImporte_ret_inv(String importe_ret_inv) {
		this.importe_ret_inv = importe_ret_inv;
	}


}
