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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.jaguarlabs.sipaccel.db.CommonDAO;
import com.jaguarlabs.sipaccel.db.SipacOpenHelper;
import com.jaguarlabs.sipaccel.dialog.DataDialog;
import com.jaguarlabs.sipaccel.model.DataModel;
import com.jaguarlabs.sipaccel.page.CobranzaPage;
import com.jaguarlabs.sipaccel.page.GeneralPage;
import com.jaguarlabs.sipaccel.page.ServiciosPage;
import com.jaguarlabs.sipaccel.page.VentasPage;
import com.jaguarlabs.sipaccel.util.DataRenderer;
import com.jaguarlabs.sipaccel.util.DelayedInitter;
import com.jaguarlabs.sipaccel.util.ExtendedFragmentActivity;
import com.jaguarlabs.sipaccel.vo.CoberturaVO;
import com.jaguarlabs.sipaccel.vo.PolizaVO;
import com.jaguarlabs.sipaccel.vo.ServAffectVO;
import com.jaguarlabs.sipaccel.vo.ServicioGralVO;
import com.jaguarlabs.sipaccel.vo.ServicioVentaVO;

public class PolizaDetailActivity extends ExtendedFragmentActivity implements DelayedInitter {

	public Button btn_tem;
	private ViewFlipper flipView;
	private ProgressDialog dialog;
	private List<DataRenderer<PolizaVO>> viewPages;
	private PolizaVO selectedPoliza; 

	@Override
	protected void init() {
		super.init();
		setContentView(R.layout.activity_detail_poliza);	
		dialog = ProgressDialog.show(this.getMContext(), "Progreso",
				"Cargando detallles", true);
		(new ActivityInflateTask(this)).execute();
	}
	@Override
	public View inProcess() {
		Bundle extras = getIntent().getExtras();
		int value;
		DataRenderer<PolizaVO> currentRenderer;
		List<? extends Map<String, ?>> pages;
		flipView = (ViewFlipper)findViewById(com.jaguarlabs.sipaccel.R.id.flipController);
		viewPages = new ArrayList<DataRenderer<PolizaVO>>();
		try{
			if (extras != null) {
				 value = extras.getInt("dataIndex");
				 selectedPoliza = DataModel.getInstance().getPolizas().get(value);
				 getPolizaDetails(selectedPoliza);
			     getServiciosAfec(selectedPoliza);
			     getServiciosVentas(selectedPoliza);
			     getServiciosGral(selectedPoliza);
			     pages = getScreens();
			     for (Map<String, ?>page:pages)
			    {
			    	currentRenderer = ((Class<DataRenderer<PolizaVO>>)page.get("renderer")).newInstance();
			    	currentRenderer.setData(selectedPoliza);
			    	currentRenderer.build(this, getLayoutInflater(), flipView);
			    	viewPages.add(currentRenderer);
			    }
			}
		}catch(Exception error){
			Log.e("Error", "Error al Generar Paginas");
		}
		return null;
	}
	
	public void delayedInit(View pView) {
		try{
			btn_tem = (Button) this.findViewById(R.id.generalButton);
			btn_tem.setBackgroundResource(R.drawable.btn_poliza_pressed);
			renderPoliza(selectedPoliza);
			headerBar();
			Button datos = (Button) findViewById(R.id.buttondata);
			datos.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					DataDialog datos = new DataDialog(mContext, selectedPoliza);
					datos.show();
				}
			});
			for(DataRenderer<PolizaVO> page:viewPages)
			{
				page.setAct(this);
				page.attach();
			}
			dialog.cancel();
		}catch(Exception error){
			Log.e("Error", "Error al Generar Paginas");
		}
	}

	private void getPolizaDetails(PolizaVO selectedPoliza) {
		CommonDAO<CoberturaVO> coberturasDS = new CommonDAO<CoberturaVO>(this, CoberturaVO.class, SipacOpenHelper.TABLE_COBERTURA);
		coberturasDS.open();
		DataModel.getInstance().setCoberturas(coberturasDS.getFilteredItems("id_poliza='"+selectedPoliza.getId_poliza()+"'"));
		coberturasDS.close();
	}
	
	private void getServiciosAfec(PolizaVO selectedPoliza) {		
		CommonDAO<ServAffectVO> serviciosDS = new CommonDAO<ServAffectVO>(this, ServAffectVO.class, SipacOpenHelper.TABLE_SERV_AFECT);
		serviciosDS.open();
		DataModel.getInstance().setServAfect(serviciosDS.getFilteredItems("id_poliza='"+selectedPoliza.getId_poliza()+"'"));
		serviciosDS.close();
	}
	
	private void getServiciosVentas(PolizaVO selectedPoliza) {		
		CommonDAO<ServicioVentaVO> serviciosDS = new CommonDAO<ServicioVentaVO>(this, ServicioVentaVO.class, SipacOpenHelper.TABLE_SERV_VENTA);
		serviciosDS.open();
		DataModel.getInstance().setServVent(serviciosDS.getFilteredItems("id_poliza='"+selectedPoliza.getId_poliza()+"'"));
		serviciosDS.close();
	}
	
	private void getServiciosGral(PolizaVO selectedPoliza) {
		CommonDAO<ServicioGralVO> serviciosDS = new CommonDAO<ServicioGralVO>(this, ServicioGralVO.class, SipacOpenHelper.TABLE_SERV_GRAL);
		serviciosDS.open();
		DataModel.getInstance().setServGral(serviciosDS.getFilteredItems("id_poliza='"+selectedPoliza.getId_poliza()+"'"));
		serviciosDS.close();
	}
	
	
	private void renderPoliza(PolizaVO pPoliza){
		Map<Integer, String> holderMap = new HashMap<Integer, String>();
		holderMap.put(R.id.idText, ""+pPoliza.getId_poliza());
		
		Iterator<Map.Entry<Integer, String>> iter = holderMap.entrySet().iterator();
		
		while(iter.hasNext()){
			Map.Entry<Integer, String> mEntry = (Map.Entry<Integer, String>) iter.next();
			((TextView)findViewById(mEntry.getKey())).setText(mEntry.getValue());  
		}	
	}
	

	private List<? extends Map<String, ?>> getScreens() {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		addScreen(data, "General", GeneralPage.class);
		addScreen(data, "Ventas", VentasPage.class);
		addScreen(data, "Servicios", ServiciosPage.class);
		addScreen(data, "Cobranza", CobranzaPage.class);
		return data;
	}

	private void addScreen(List<Map<String, Object>> data, String title, Class <? extends DataRenderer<PolizaVO>> renderClass) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title",title);
		map.put("renderer", renderClass);
		data.add(map);
	}

	public void headerBar() {
		ImageButton backSearch = (ImageButton) findViewById(R.id.backSearch);
		ImageButton backResult = (ImageButton) findViewById(R.id.backResult);

		backSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DataModel.getInstance().getPreviousActivity().finish();
				finish();
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_right);
			}
		});

		backResult.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_right);
			}
		});

	}
	
	public void tabClick(View targetObject){
		
		switch (targetObject.getId()) {
			case R.id.generalButton:
				btn_tem.setBackgroundResource(R.drawable.btn_poliza_unpressed);
				btn_tem = (Button) this.findViewById(R.id.generalButton);
				btn_tem.setBackgroundResource(R.drawable.btn_poliza_pressed);
				flipView.setInAnimation(this, R.anim.viewflipper_in_right);
				flipView.setOutAnimation(this, R.anim.viewflipper_out_left);
				flipView.setDisplayedChild(0);
				break;
			case R.id.ventasButton:				
				btn_tem.setBackgroundResource(R.drawable.btn_poliza_unpressed);
				btn_tem = (Button) this.findViewById(R.id.ventasButton);
				btn_tem.setBackgroundResource(R.drawable.btn_poliza_pressed);
				flipView.setInAnimation(this, R.anim.viewflipper_in_right);
				flipView.setOutAnimation(this, R.anim.viewflipper_out_left);
				flipView.setDisplayedChild(1);
				break;
			case R.id.serviciosButton:					
				btn_tem.setBackgroundResource(R.drawable.btn_poliza_unpressed);
				btn_tem = (Button) this.findViewById(R.id.serviciosButton);
				btn_tem.setBackgroundResource(R.drawable.btn_poliza_pressed);
				flipView.setInAnimation(this, R.anim.viewflipper_in_right);
				flipView.setOutAnimation(this, R.anim.viewflipper_out_left);
				flipView.setDisplayedChild(2);
				break;
			case R.id.cobranzaButton:
				btn_tem.setBackgroundResource(R.drawable.btn_poliza_unpressed);
				btn_tem = (Button) this.findViewById(R.id.cobranzaButton);
				btn_tem.setBackgroundResource(R.drawable.btn_poliza_pressed);
				flipView.setInAnimation(this, R.anim.viewflipper_in_right);
				flipView.setOutAnimation(this, R.anim.viewflipper_out_left);
				flipView.setDisplayedChild(3);
				break;
		}
	}
		
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_right);
	}

}

class ActivityInflateTask extends AsyncTask<String, String, String>
{
	
	
	private DelayedInitter initter;
	private View generatedView;
	
	
	public ActivityInflateTask( DelayedInitter initter){
		this.initter = initter;
		
	}
	protected String doInBackground(String... key) {
		generatedView = initter.inProcess();
	    return null;
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		initter.delayedInit(generatedView);
	}
	
}
