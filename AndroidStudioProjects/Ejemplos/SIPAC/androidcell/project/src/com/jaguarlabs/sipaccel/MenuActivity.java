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

package com.jaguarlabs.sipaccel;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.jaguarlabs.sipaccel.db.CommonDAO;
import com.jaguarlabs.sipaccel.db.SipacOpenHelper;
import com.jaguarlabs.sipaccel.model.BackupModel;
import com.jaguarlabs.sipaccel.util.ExtendedFragmentActivity;
import com.jaguarlabs.sipaccel.vo.EdadVO;
import com.jaguarlabs.sipaccel.vo.PolizaVO;
import com.jaguarlabs.sipaccel.vo.ProfesionVO;
import com.jaguarlabs.sipaccel.vo.ProspeccionVO;

public class MenuActivity extends ExtendedFragmentActivity {

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		setContentView(R.layout.activity_main_menu);
		ImageButton backButton = (ImageButton) findViewById(R.id.back);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	private Boolean datosCartera()
	{
		
		Boolean hasData = true;
		CommonDAO<?> auxDAO = new CommonDAO<PolizaVO>(mContext, PolizaVO.class,SipacOpenHelper.TABLE_POLIZA);
		auxDAO.open();
		if(auxDAO.countItems() <= 0) hasData = false;
		auxDAO.close();
		return hasData;
	}
	
	private Boolean datosProspeccion()
	{
		Boolean hasData = true;
		CommonDAO<?> auxDAO = new CommonDAO<ProspeccionVO>(mContext, ProspeccionVO.class,SipacOpenHelper.TABLE_PROSPECCIONES);
		auxDAO.open();
		if(auxDAO.countItems() <= 0) hasData = false;
		auxDAO.close();
		return hasData;
	} 
	private Boolean datosCotizador()
	{
		Boolean hasData = true;
		CommonDAO<?> auxDAO = new CommonDAO<EdadVO>(mContext, EdadVO.class,SipacOpenHelper.TABLE_EDADES);
		auxDAO.open();
		if(auxDAO.countItems() <= 0) hasData = false;
		auxDAO.close();
		
		auxDAO = new CommonDAO<ProfesionVO>(mContext, ProfesionVO.class,SipacOpenHelper.TABLE_PROFESIONES);
		auxDAO.open();
		if(auxDAO.countItems() <= 0) hasData = false;
		auxDAO.close();
		
		return hasData;
	}
	public void navigateClick(View pTarView){
		Intent sectionIntent = new Intent();
		
		if(pTarView.getId() == R.id.cotizaButton){
			if(!datosCotizador())
			{
				getMessageBuilder().setMessage("Por favor descargue los datos necesarios para el cotizador (Presione boton de Actualización)").show();
				return;
			}
		}
		
		if(pTarView.getId() == R.id.consultaButton)
		{
			if(!datosCartera())
			{
				getMessageBuilder().setMessage("Por favor descargue los datos necesarios para consultar la cartera (Presione boton de Actualización)").show();
				return;
			}
		}
		
		if(pTarView.getId() == R.id.prospectButton)
		{
			if(!datosProspeccion())
			{
				getMessageBuilder().setMessage("Por favor descargue los datos necesarios para ingresar a prospección (Presione boton de Actualización)").show();
				return;
			}
		}
		if(pTarView.getId() == R.id.geolocalizacionButton)
		{
			sectionIntent.setClass(this, MapWrapperActivity.class);
			startActivity(sectionIntent);
			overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
		}else
		{
			sectionIntent.putExtra("section", pTarView.getId());
			sectionIntent.setClass(this, MainAplicationActivity.class);
			startActivity(sectionIntent);
			overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
			finish();
		}
		
	}
	
	public void backupData(View pTarget){
		
		BackupModel.getInstance().setContextActivity(this);
		
		if(!wifiOn)
		{
			getMessageBuilder().setMessage("Es necesario tener una conexion WIFI Activa para reaizar este proceso");
	        getMessageBuilder().create().show();
	        return;
		}
		
		BackupModel.getInstance().startBackup();
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

}
