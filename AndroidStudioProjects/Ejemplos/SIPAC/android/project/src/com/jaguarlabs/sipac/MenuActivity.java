package com.jaguarlabs.sipac;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jaguarlabs.sipac.db.CommonDAO;
import com.jaguarlabs.sipac.db.SipacOpenHelper;
import com.jaguarlabs.sipac.model.BackupModel;
import com.jaguarlabs.sipac.util.ExtendedActivity;
import com.jaguarlabs.sipac.vo.EdadVO;
import com.jaguarlabs.sipac.vo.PolizaVO;
import com.jaguarlabs.sipac.vo.ProfesionVO;
import com.jaguarlabs.sipac.vo.ProspeccionVO;
import com.jaguarlabs.sipac.vo.ServicioGralVO;

public class MenuActivity extends ExtendedActivity {
	
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		setContentView(R.layout.activity_main_menu);
		Button backButton = (Button)findViewById(R.id.back);
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
			sectionIntent.setClass(this, MainApplicationActivity.class);
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
