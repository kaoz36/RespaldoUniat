package com.jaguarlabs.sipac.model;

import java.util.List;

import android.util.Log;

import com.jaguarlabs.sipac.vo.CoberturaVO;
import com.jaguarlabs.sipac.vo.ConfiguracionVO;
import com.jaguarlabs.sipac.vo.CotizacionVO;
import com.jaguarlabs.sipac.vo.PolizaVO;
import com.jaguarlabs.sipac.vo.PromotoriaVO;
import com.jaguarlabs.sipac.vo.ProspeccionVO;
import com.jaguarlabs.sipac.vo.ServAffectVO;
import com.jaguarlabs.sipac.vo.ServicioGralVO;
import com.jaguarlabs.sipac.vo.ServicioVentaVO;
import com.jaguarlabs.sipac.vo.UserVO;

public class DataModel {
	private static DataModel instance;
	private CotizacionVO estadoCotizador;
	private PromotoriaVO promotoria;
	private ConfiguracionVO configuracion;
	private List<PolizaVO> polizas;
	private List<CoberturaVO> coberturas;
	private List<ServAffectVO> servicios;
	private List<ServicioVentaVO> serVentas;
	private List<ServicioGralVO> serGral;
	private List<ProspeccionVO> prospec;
	private UserVO appUser;
	
	public CotizacionVO getEstadoCotizador() {
		return estadoCotizador;
	}

	public void setEstadoCotizador(CotizacionVO estadoCotizador) {
		this.estadoCotizador = estadoCotizador;
	}
	
	public PromotoriaVO getPromotoria() {
		return promotoria;
	}

	public void setPromotoria(PromotoriaVO promotoria) {
		this.promotoria = promotoria;
	}

	public ConfiguracionVO getConfiguracion() {
		return configuracion;
	}

	public void setConfiguracion(ConfiguracionVO configuracion) {
		this.configuracion = configuracion;
	}
	
	private DataModel() throws Exception{
		if(instance != null){
			throw new Exception("Error de Singleton");
		}			
	}
	
	public static DataModel getInstance()
	{
		if(instance == null){
			try{
				instance = new DataModel();
			}catch(Exception e){
				Log.e("Error",e.getMessage());
			}
		}
		return instance;
	}

	public List<PolizaVO> getPolizas() {
		return polizas;
	}

	public void setPolizas(List<PolizaVO> polizas) {
		this.polizas = polizas;
	}

	public UserVO getAppUser() {
		return appUser;
	}

	public void setAppUser(UserVO appUser) {
		this.appUser = appUser;
	}

	public List<CoberturaVO> getCoberturas() {
		return coberturas;
	}

	public void setCoberturas(List<CoberturaVO> coberturas) {
		this.coberturas = coberturas;
	}
	
	//TODO:Julio
	public List<ServAffectVO> getServiciosAfect(){
		Log.i("servicios", servicios + " .");
		return servicios;
	}
	
	public void setServiciosAfect(List<ServAffectVO> servicios) {
		this.servicios = servicios;
	}
	
	public List<ServicioVentaVO> getServiciosVentas(){
		Log.i("servicios", serVentas + " .");
		return serVentas;
	}
	
	public void setServiciosVentas(List<ServicioVentaVO> serVentas) {
		this.serVentas = serVentas;
	}
	
	public List<ServicioGralVO> getServiciosGral(){
		Log.i("servicios", serGral + ".");
		return serGral;
	}
	
	public void setServiciosGral(List<ServicioGralVO> serGral) {
		this.serGral = serGral;
	}
	
	public List<ProspeccionVO> getProspec() {
		return prospec;
	}
	
	public void setProspec(List<ProspeccionVO> prospec) {
		this.prospec = prospec;
	}
}
