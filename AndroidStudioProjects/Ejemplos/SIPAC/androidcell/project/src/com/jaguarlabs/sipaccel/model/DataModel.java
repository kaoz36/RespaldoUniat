/** *************************************************************************
 *
 *   Copyright (c)  2013 by Jaguar Labs.
 *   Confidential and Proprietary
 *   All Rights Reserved
 *
 *   This software is furnished under license and may be used and copied
 *   only in accordance with the terms of its license and with the
 *   inclusion of the above copyright notice. This software and any other
 *   copies thereof may not be provided or otherwise made available to any
 *   other party. No title to and/or ownership of the software is hereby
 *   transferred.
 *
 *   The information in this software is subject to change without notice and
 *   should not be construed as a commitment by JaguarLabs.
 *
 * @(#)$Id: $
 * Last Revised By   : Raul Acevedo
 * Last Checked In   : $Date: $
 * Last Version      : $Revision:  $
 *
 * Original Author   : Julio Hernandez  julio.hernadez@jaguarlabs.com
 * Origin            : SEnE -- April 25 @ 11:00 (PST)
 * Notes             :
 *
 * *************************************************************************/

package com.jaguarlabs.sipaccel.model;

import java.util.List;

import android.app.Activity;
import android.util.Log;

import com.jaguarlabs.sipaccel.vo.CoberturaVO;
import com.jaguarlabs.sipaccel.vo.ConfiguracionVO;
import com.jaguarlabs.sipaccel.vo.CotizacionVO;
import com.jaguarlabs.sipaccel.vo.EdadVO;
import com.jaguarlabs.sipaccel.vo.PolizaVO;
import com.jaguarlabs.sipaccel.vo.ProfesionVO;
import com.jaguarlabs.sipaccel.vo.PromotoriaVO;
import com.jaguarlabs.sipaccel.vo.ProspeccionVO;
import com.jaguarlabs.sipaccel.vo.ServAffectVO;
import com.jaguarlabs.sipaccel.vo.ServicioGralVO;
import com.jaguarlabs.sipaccel.vo.ServicioVentaVO;
import com.jaguarlabs.sipaccel.vo.UserVO;

public class DataModel {

	public static DataModel instance;
	private CotizacionVO estadoCotizador;
	private PromotoriaVO promotoria;
	private ConfiguracionVO configuracion;
	private List<PolizaVO> polizas;
	private List<CoberturaVO> coberturas;
	private List<ServAffectVO> servAfect;
	private List<ServicioGralVO> servGral;
	private List<ServicioVentaVO> servVent;
	private List<ProspeccionVO> prospec;
	private List<EdadVO> edades;
	private List<ProfesionVO> profesiones;

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

	public List<EdadVO> getEdades() {
		return edades;
	}

	public void setEdades(List<EdadVO> edades) {
		this.edades = edades;
	}

	public List<ProfesionVO> getProfesiones() {
		return profesiones;
	}

	public void setProfesiones(List<ProfesionVO> profesiones) {
		this.profesiones = profesiones;
	}

	private UserVO appUser;	
	private Activity previousActivity;
	
	public Activity getPreviousActivity() {
		return previousActivity;
	}

	public void setPreviousActivity(Activity previousActivity) {
		this.previousActivity = previousActivity;
	}

	private DataModel() throws Exception{
		if(instance != null){
			throw new Exception("Error de Singleton");
		}			
	}
	
	public static DataModel getInstance(){
		if( instance == null ){
			try{
				instance = new DataModel();
			}catch(Exception e){
				Log.e( "Error", e.getMessage() );
			}
		}
		return instance;
	}
	
	public List<PolizaVO> getPolizas(){
		return polizas;
	}
	
	public void setPolizas( List<PolizaVO> polizas ){
		this.polizas = polizas;
	}

	public List<CoberturaVO> getCoberturas() {
		return coberturas;
	}

	public void setCoberturas(List<CoberturaVO> coberturas) {
		this.coberturas = coberturas;
	}

	public List<ServAffectVO> getServAfect() {
		return servAfect;
	}

	public void setServAfect(List<ServAffectVO> servAfect) {
		this.servAfect = servAfect;
	}

	public List<ServicioGralVO> getServGral() {
		return servGral;
	}

	public void setServGral(List<ServicioGralVO> servGral) {
		this.servGral = servGral;
	}

	public List<ServicioVentaVO> getServVent() {
		return servVent;
	}

	public void setServVent(List<ServicioVentaVO> servVent) {
		this.servVent = servVent;
	}

	public UserVO getAppUser() {
		return appUser;
	}

	public void setAppUser(UserVO appUser) {
		this.appUser = appUser;
	}
	
	public List<ProspeccionVO> getProspec() {
		return prospec;
	}

	public void setProspec(List<ProspeccionVO> prospec) {
		this.prospec = prospec;
	}
	
}
