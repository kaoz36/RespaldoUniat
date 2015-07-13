package com.jaguarlabs.sipac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.jaguarlabs.sipac.components.VerticalButton;
import com.jaguarlabs.sipac.db.CommonDAO;
import com.jaguarlabs.sipac.db.SipacOpenHelper;
import com.jaguarlabs.sipac.model.DataModel;
import com.jaguarlabs.sipac.page.CobranzaPage;
import com.jaguarlabs.sipac.page.GeneralPage;
import com.jaguarlabs.sipac.page.ScreenAdapter;
import com.jaguarlabs.sipac.page.ServiciosPage;
import com.jaguarlabs.sipac.page.VentasPage;
import com.jaguarlabs.sipac.util.DataRenderer;
import com.jaguarlabs.sipac.util.DelayedInitter;
import com.jaguarlabs.sipac.util.ExtendedActivity;
import com.jaguarlabs.sipac.util.PolizaAdapter;
import com.jaguarlabs.sipac.vo.CoberturaVO;
import com.jaguarlabs.sipac.vo.PolizaVO;
import com.jaguarlabs.sipac.vo.ServAffectVO;
import com.jaguarlabs.sipac.vo.ServicioGralVO;
import com.jaguarlabs.sipac.vo.ServicioVentaVO;

public class PolizaDetailActivity extends ExtendedActivity implements DelayedInitter {
	
	
	private ViewFlipper flipView;
	private VerticalButton btn_tem;
	private ProgressDialog dialog;
	private List<DataRenderer<PolizaVO>> viewPages;
	
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
		PolizaVO selectedPoliza; 
		int value;
		DataRenderer<PolizaVO> currentRenderer;
		List<? extends Map<String, ?>> pages;
		flipView = (ViewFlipper)findViewById(com.jaguarlabs.sipac.R.id.flipController);
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
	
	@Override
	public void delayedInit(View pView) {
		Bundle extras = getIntent().getExtras();
		PolizaVO selectedPoliza; 
		int value;
		try{
			if (extras != null) {
				value = extras.getInt("dataIndex");
				selectedPoliza = DataModel.getInstance().getPolizas().get(value);
				btn_tem = (VerticalButton) this.findViewById(R.id.generalButton);
				btn_tem.setBackgroundResource(R.drawable.img_tab_act);
				renderPoliza(selectedPoliza);
				for(DataRenderer<PolizaVO> page:viewPages)
				{
					page.attach();
				}
				dialog.cancel();
			}
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
		DataModel.getInstance().setServiciosAfect(serviciosDS.getFilteredItems("id_poliza='"+selectedPoliza.getId_poliza()+"'"));
		serviciosDS.close();
	}
	
	private void getServiciosVentas(PolizaVO selectedPoliza) {		
		CommonDAO<ServicioVentaVO> serviciosDS = new CommonDAO<ServicioVentaVO>(this, ServicioVentaVO.class, SipacOpenHelper.TABLE_SERV_VENTA);
		serviciosDS.open();
		DataModel.getInstance().setServiciosVentas(serviciosDS.getFilteredItems("id_poliza='"+selectedPoliza.getId_poliza()+"'"));
		serviciosDS.close();
	}
	
	private void getServiciosGral(PolizaVO selectedPoliza) {
		CommonDAO<ServicioGralVO> serviciosDS = new CommonDAO<ServicioGralVO>(this, ServicioGralVO.class, SipacOpenHelper.TABLE_SERV_GRAL);
		serviciosDS.open();
		DataModel.getInstance().setServiciosGral(serviciosDS.getFilteredItems("id_poliza='"+selectedPoliza.getId_poliza()+"'"));
		serviciosDS.close();
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
	
	private void renderPoliza(PolizaVO pPoliza){
		Map<Integer, String> holderMap = new HashMap<Integer, String>();
		holderMap.put(R.id.idText, ""+pPoliza.getId_poliza());
		holderMap.put(R.id.statusText, pPoliza.getStatus_poliza());
		holderMap.put(R.id.retenedorText, pPoliza.getRet_poliza());
		holderMap.put(R.id.nombreText, pPoliza.getNombre_poliza());
		holderMap.put(R.id.empleadoText, pPoliza.getEmp_poliza());
		holderMap.put(R.id.rfcText, pPoliza.getRfc_poliza());
		
		Iterator<Map.Entry<Integer, String>> iter = holderMap.entrySet().iterator();
		
		while(iter.hasNext()){
			Map.Entry<Integer, String> mEntry = (Map.Entry<Integer, String>) iter.next();
			((TextView)findViewById(mEntry.getKey())).setText(mEntry.getValue());  
		}	
	}
	
	public void tabClick(View targetObject){
				
		switch (targetObject.getId()) {
			case R.id.generalButton:
				btn_tem.setBackgroundResource(R.drawable.img_tab_inact);
				btn_tem = (VerticalButton) this.findViewById(R.id.generalButton);
				btn_tem.setBackgroundResource(R.drawable.img_tab_act);
				flipView.setInAnimation(this, R.anim.viewflipper_in_right);
				flipView.setOutAnimation(this, R.anim.viewflipper_out_left);
				flipView.setDisplayedChild(0);
				break;
			case R.id.ventasButton:				
				btn_tem.setBackgroundResource(R.drawable.img_tab_inact);
				btn_tem = (VerticalButton) this.findViewById(R.id.ventasButton);
				btn_tem.setBackgroundResource(R.drawable.img_tab_act);
				flipView.setInAnimation(this, R.anim.viewflipper_in_right);
				flipView.setOutAnimation(this, R.anim.viewflipper_out_left);
				flipView.setDisplayedChild(1);
				break;
			case R.id.serviciosButton:					
				btn_tem.setBackgroundResource(R.drawable.img_tab_inact);
				btn_tem = (VerticalButton) this.findViewById(R.id.serviciosButton);
				btn_tem.setBackgroundResource(R.drawable.img_tab_act);
				flipView.setInAnimation(this, R.anim.viewflipper_in_right);
				flipView.setOutAnimation(this, R.anim.viewflipper_out_left);
				flipView.setDisplayedChild(2);
				break;
			case R.id.cobranzasButton:
				btn_tem.setBackgroundResource(R.drawable.img_tab_inact);
				btn_tem = (VerticalButton) this.findViewById(R.id.cobranzasButton);
				btn_tem.setBackgroundResource(R.drawable.img_tab_act);
				flipView.setInAnimation(this, R.anim.viewflipper_in_right);
				flipView.setOutAnimation(this, R.anim.viewflipper_out_left);
				flipView.setDisplayedChild(3);
				break;
		}
	}
	
	

	public void onClickHandler(View target){
		onBackPressed();
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
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
