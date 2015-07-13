package com.jaguarlabs.sipac.vo;

import com.jaguarlabs.sipac.R;

import android.database.Cursor;

public class PolizaVO implements  ICursorDecoder {
	private long poliza_id;
	private String id_poliza ;
	private String status_poliza ;
	private String ret_poliza ;
	private String nombre_poliza ;
	private String rfc_poliza ;
	private String emp_poliza ;
	private String sexo_poliza ;
	private String estado_civil_poliza ;
	private String fuma_poliza;
	private String nac_poliza;
	private String domicilio_poliza;
	private String cp_poliza;
	private String tel_poliza;
	private String pob_poliza;
	private String email_poliza;
	private String prima_pag_poliza;
	private String res_poliza;
	private String inv_poliza;
	private String prima_pen_poliza;
	private String rec_pen_poliza;
	private String ult_pago_poliza;
	private String prima_emi_poliza;
	private String prima_quin_poliza;
	private String fec_ini_vig_poliza;
	private String fec_ult_mod_poliza;
	private String fec_ult_ret_poliza;
	private String orden_pago_poliza;
	private String monto_poliza;
	private String adicional_poliza;
	private String referencia_poliza;
	private String ref_alfa_poliza;
	private String forma_poliza;
	private String prima_poliza;
	private String prima_esp_poliza;
	private String prima_desc_poliza;
	private String cpto_poliza;
	private String sub_ret_poliza;
	private String cve_cob_poliza;
	private String uni_pago_poliza;
	private String ult_pago2_poliza;
	private String quin_poliza;
	private String tipo_mov_poliza;
	private String signo_reserva;
	private String signo_fondoi;
	
	public PolizaVO(){
		super();
	}
	
	
	
	@Override
	public void decode(Cursor pCursor) {
		setId(pCursor.getInt(0));
		setId_poliza(pCursor.getString(1)).
		setStatus_poliza(pCursor.getString(2)).
		setRet_poliza(pCursor.getString(3)).
		setNombre_poliza(pCursor.getString(4)).
		setRfc_poliza(pCursor.getString(5)).
		setEmp_poliza(pCursor.getString(6)).
		setSexo_poliza(pCursor.getString(7)).
		setEstado_civil_poliza(pCursor.getString(8)).
		setFuma_poliza(pCursor.getString(9)).
		setNac_poliza(pCursor.getString(10)).
		setDomicilio_poliza(pCursor.getString(11)).
		setCp_poliza(pCursor.getString(12)).
		setTel_poliza(pCursor.getString(13)).
		setPob_poliza(pCursor.getString(14)).
		setEmail_poliza(pCursor.getString(15)).
		setPrima_pag_poliza(pCursor.getString(16)).
		setRes_poliza(pCursor.getString(17)).
		setInv_poliza(pCursor.getString(18)).
		setPrima_pen_poliza(pCursor.getString(19)).
		setRec_pen_poliza(pCursor.getString(20)).
		setUlt_pago_poliza(pCursor.getString(21)).
		setPrima_emi_poliza(pCursor.getString(22)).
		setPrima_quin_poliza(pCursor.getString(23)).
		setFec_ini_vig_poliza(pCursor.getString(24)).
		setFec_ult_mod_poliza(pCursor.getString(25)).
		setFec_ult_ret_poliza(pCursor.getString(26)).
		setOrden_pago_poliza(pCursor.getString(27)).
		setMonto_poliza(pCursor.getString(28)).
		setAdicional_poliza(pCursor.getString(29)).
		setReferencia_poliza(pCursor.getString(30)).
		setRef_alfa_poliza(pCursor.getString(31)).
		setForma_poliza(pCursor.getString(32)).
		setPrima_poliza(pCursor.getString(33)).
		setPrima_esp_poliza(pCursor.getString(34)).
		setPrima_desc_poliza(pCursor.getString(35)).
		setCpto_poliza(pCursor.getString(36)).
		setSub_ret_poliza(pCursor.getString(37)).
		setCve_cob_poliza(pCursor.getString(38)).
		setUni_pago_poliza(pCursor.getString(39)).
		setUlt_pago2_poliza(pCursor.getString(40)).
		setQuin_poliza(pCursor.getString(41)).
		setTipo_mov_poliza(pCursor.getString(42)).
		setSigno_reserva(pCursor.getString(43)).
		setSigno_fondoi(pCursor.getString(44));
		
	}



	public long getId() {
		return poliza_id;
	}



	public void setId(long poliza_id) {
		this.poliza_id = poliza_id;
	}



	public String getId_poliza() {
		return id_poliza;
	}



	public PolizaVO setId_poliza(String id_poliza) {
		this.id_poliza = id_poliza;
		return this;
	}



	public String getStatus_poliza() {
		return status_poliza;
	}



	public PolizaVO setStatus_poliza(String status_poliza) {
		this.status_poliza = status_poliza;
		return this;
	}



	public String getRet_poliza() {
		return ret_poliza;
	}



	public PolizaVO setRet_poliza(String ret_poliza) {
		this.ret_poliza = ret_poliza;
		return this;
	}



	public String getNombre_poliza() {
		return nombre_poliza;
	}



	public PolizaVO setNombre_poliza(String nombre_poliza) {
		this.nombre_poliza = nombre_poliza;
		return this;
	}



	public String getRfc_poliza() {
		return rfc_poliza;
	}



	public PolizaVO setRfc_poliza(String rfc_poliza) {
		this.rfc_poliza = rfc_poliza;
		return this;
	}



	public String getEmp_poliza() {
		return emp_poliza;
	}



	public PolizaVO setEmp_poliza(String emp_poliza) {
		this.emp_poliza = emp_poliza;
		return this;
	}



	public String getSexo_poliza() {
		return sexo_poliza;
	}



	public PolizaVO setSexo_poliza(String sexo_poliza) {
		this.sexo_poliza = sexo_poliza;
		return this;
	}



	public String getEstado_civil_poliza() {
		return estado_civil_poliza;
	}



	public PolizaVO setEstado_civil_poliza(String estado_civil_poliza) {
		this.estado_civil_poliza = estado_civil_poliza;
		return this;
	}



	public String getFuma_poliza() {
		return fuma_poliza;
	}



	public PolizaVO setFuma_poliza(String fuma_poliza) {
		this.fuma_poliza = fuma_poliza;
		return this;
	}



	public String getNac_poliza() {
		return nac_poliza;
	}



	public PolizaVO setNac_poliza(String nac_poliza) {
		this.nac_poliza = nac_poliza;
		return this;
	}



	public String getDomicilio_poliza() {
		return domicilio_poliza;
	}



	public PolizaVO setDomicilio_poliza(String domicilio_poliza) {
		this.domicilio_poliza = domicilio_poliza;
		return this;
	}



	public String getCp_poliza() {
		return cp_poliza;
	}



	public PolizaVO setCp_poliza(String cp_poliza) {
		this.cp_poliza = cp_poliza;
		return this;
	}



	public String getTel_poliza() {
		return tel_poliza;
	}



	public PolizaVO setTel_poliza(String tel_poliza) {
		this.tel_poliza = tel_poliza;
		return this;
	}



	public String getPob_poliza() {
		return pob_poliza;
	}



	public PolizaVO setPob_poliza(String pob_poliza) {
		this.pob_poliza = pob_poliza;
		return this;
	}



	public String getEmail_poliza() {
		return email_poliza;
	}



	public PolizaVO setEmail_poliza(String email_poliza) {
		this.email_poliza = email_poliza;
		return this;
	}



	public String getPrima_pag_poliza() {
		return prima_pag_poliza;
	}



	public PolizaVO setPrima_pag_poliza(String prima_pag_poliza) {
		this.prima_pag_poliza = prima_pag_poliza;
		return this;
	}



	public String getRes_poliza() {
		return res_poliza;
	}



	public PolizaVO setRes_poliza(String res_poliza) {
		this.res_poliza = res_poliza;
		return this;
	}



	public String getInv_poliza() {
		return inv_poliza;
	}



	public PolizaVO setInv_poliza(String inv_poliza) {
		this.inv_poliza = inv_poliza;
		return this;
	}



	public String getPrima_pen_poliza() {
		return prima_pen_poliza;
	}



	public PolizaVO setPrima_pen_poliza(String prima_pen_poliza) {
		this.prima_pen_poliza = prima_pen_poliza;
		return this;
	}



	public String getRec_pen_poliza() {
		return rec_pen_poliza;
	}



	public PolizaVO setRec_pen_poliza(String rec_pen_poliza) {
		this.rec_pen_poliza = rec_pen_poliza;
		return this;
	}



	public String getUlt_pago_poliza() {
		return ult_pago_poliza;
	}



	public PolizaVO setUlt_pago_poliza(String ult_pago_poliza) {
		this.ult_pago_poliza = ult_pago_poliza;
		return this;
	}



	public String getPrima_emi_poliza() {
		return prima_emi_poliza;
	}



	public PolizaVO setPrima_emi_poliza(String prima_emi_poliza) {
		this.prima_emi_poliza = prima_emi_poliza;
		return this;
	}



	public String getPrima_quin_poliza() {
		return prima_quin_poliza;
	}



	public PolizaVO setPrima_quin_poliza(String prima_quin_poliza) {
		this.prima_quin_poliza = prima_quin_poliza;
		return this;
	}



	public String getFec_ini_vig_poliza() {
		return fec_ini_vig_poliza;
	}



	public PolizaVO setFec_ini_vig_poliza(String fec_ini_vig_poliza) {
		this.fec_ini_vig_poliza = fec_ini_vig_poliza;
		return this;
	}



	public String getFec_ult_mod_poliza() {
		return fec_ult_mod_poliza;
	}



	public PolizaVO setFec_ult_mod_poliza(String fec_ult_mod_poliza) {
		this.fec_ult_mod_poliza = fec_ult_mod_poliza;
		return this;
	}



	public String getFec_ult_ret_poliza() {
		return fec_ult_ret_poliza;
	}



	public PolizaVO setFec_ult_ret_poliza(String fec_ult_ret_poliza) {
		this.fec_ult_ret_poliza = fec_ult_ret_poliza;
		return this;
	}



	public String getOrden_pago_poliza() {
		return orden_pago_poliza;
	}



	public PolizaVO setOrden_pago_poliza(String orden_pago_poliza) {
		this.orden_pago_poliza = orden_pago_poliza;
		return this;
	}



	public String getMonto_poliza() {
		return monto_poliza;
	}



	public PolizaVO setMonto_poliza(String monto_poliza) {
		this.monto_poliza = monto_poliza;
		return this;
	}



	public String getAdicional_poliza() {
		return adicional_poliza;
	}



	public PolizaVO setAdicional_poliza(String adicional_poliza) {
		this.adicional_poliza = adicional_poliza;
		return this;
	}



	public String getReferencia_poliza() {
		return referencia_poliza;
	}



	public PolizaVO setReferencia_poliza(String referencia_poliza) {
		this.referencia_poliza = referencia_poliza;
		return this;
	}



	public String getRef_alfa_poliza() {
		return ref_alfa_poliza;
	}



	public PolizaVO setRef_alfa_poliza(String ref_alfa_poliza) {
		this.ref_alfa_poliza = ref_alfa_poliza;
		return this;
	}



	public String getForma_poliza() {
		return forma_poliza;
	}



	public PolizaVO setForma_poliza(String forma_poliza) {
		this.forma_poliza = forma_poliza;
		return this;
	}



	public String getPrima_poliza() {
		return prima_poliza;
	}



	public PolizaVO setPrima_poliza(String prima_poliza) {
		this.prima_poliza = prima_poliza;
		return this;
	}



	public String getPrima_esp_poliza() {
		return prima_esp_poliza;
	}



	public PolizaVO setPrima_esp_poliza(String prima_esp_poliza) {
		this.prima_esp_poliza = prima_esp_poliza;
		return this;
	}



	public String getPrima_desc_poliza() {
		return prima_desc_poliza;
	}



	public PolizaVO setPrima_desc_poliza(String prima_desc_poliza) {
		this.prima_desc_poliza = prima_desc_poliza;
		return this;
	}



	public String getCpto_poliza() {
		return cpto_poliza;
	}



	public PolizaVO setCpto_poliza(String cpto_poliza) {
		this.cpto_poliza = cpto_poliza;
		return this;
	}



	public String getSub_ret_poliza() {
		return sub_ret_poliza;
	}



	public PolizaVO setSub_ret_poliza(String sub_ret_poliza) {
		this.sub_ret_poliza = sub_ret_poliza;
		return this;
	}



	public String getCve_cob_poliza() {
		return cve_cob_poliza;
	}



	public PolizaVO setCve_cob_poliza(String cve_cob_poliza) {
		this.cve_cob_poliza = cve_cob_poliza;
		return this;
	}



	public String getUni_pago_poliza() {
		return uni_pago_poliza;
	}



	public PolizaVO setUni_pago_poliza(String uni_pago_poliza) {
		this.uni_pago_poliza = uni_pago_poliza;
		return this;
	}



	public String getUlt_pago2_poliza() {
		return ult_pago2_poliza;
	}



	public PolizaVO setUlt_pago2_poliza(String ult_pago2_poliza) {
		this.ult_pago2_poliza = ult_pago2_poliza;
		return this;
	}



	public String getQuin_poliza() {
		return quin_poliza;
	}



	public PolizaVO setQuin_poliza(String quin_poliza) {
		this.quin_poliza = quin_poliza;
		return this;
	}



	public String getTipo_mov_poliza() {
		return tipo_mov_poliza;
	}



	public PolizaVO setTipo_mov_poliza(String tipo_mov_poliza) {
		this.tipo_mov_poliza = tipo_mov_poliza;
		return this;
	}
	
	public String getData(int resource){
		switch(resource)
		{
		  case R.id.idPoliza:
			  			return getId_poliza();
			  		
		  case R.id.nombre:
			  			return getNombre_poliza();
		
		  case R.id.rfc:
			  			return getRfc_poliza();
		
		  case R.id.uniPago:
			  			return getEmp_poliza();
		
		  case R.id.quincena:
			  			return getUlt_pago2_poliza();
		
		  case R.id.retenedor:
			  			return getRet_poliza();
			  			
		  case R.id.positivonegativo:
			  		   return getPrima_poliza();
		  default:
			  			return "";
			  		
		}
	}



	public String getSigno_reserva() {
		return signo_reserva;
	}



	public PolizaVO setSigno_reserva(String signo_reserva) {
		this.signo_reserva = signo_reserva;
		return this;
	}



	public String getSigno_fondoi() {
		return signo_fondoi;
	}



	public PolizaVO setSigno_fondoi(String signo_fondoi) {
		this.signo_fondoi = signo_fondoi;
		return this;
	}
	

}
