package com.jaguarlabs.sipac;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jaguarlabs.sipac.db.CommonDAO;
import com.jaguarlabs.sipac.db.SipacOpenHelper;
import com.jaguarlabs.sipac.model.DataModel;
import com.jaguarlabs.sipac.util.ExtendedActivity;
import com.jaguarlabs.sipac.util.PolizaAdapter;
import com.jaguarlabs.sipac.vo.PolizaVO;

public class ResultActivity extends ExtendedActivity {
	
	
	@Override
	protected void init() {
		super.init();
		Bundle extras = getIntent().getExtras();
		setContentView(R.layout.activity_result);
		
		ListView polizaList =(ListView)findViewById(R.id.polizaList); 
		
		polizaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id){
				lauchDetailView(position);
			}
		});
		
		if(extras != null)
		{
			searchPolizas(extras.getString("searchTerm"));
			
		}
	}
	
	private void searchPolizas(String pSearchTerm){
		ListView polizaList =(ListView)findViewById(R.id.polizaList); 
		TextView header = (TextView)findViewById(R.id.resultHeader);
		new SearchTask(this,polizaList,header).execute(pSearchTerm);
	}
	
	
	public void lauchDetailView(int pIndex){
		Intent detailIntent = new Intent(mContext, PolizaDetailActivity.class);
		detailIntent.putExtra("dataIndex", pIndex);
		startActivity(detailIntent);
		overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
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


class SearchTask extends AsyncTask<String, String, String>
{
	
	private Activity activity;
	private ListView list;
	private List<PolizaVO>results;
	private Long countResults;
	private TextView textView;
	private  String searchTerm;
	private Dialog dialog;
	
	public SearchTask(Activity pActivity,ListView pList, TextView pTextView){
		
		activity = pActivity;
		list = pList;
		textView = pTextView;
		
	}
	protected String doInBackground(String... key) {
		
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				dialog = ProgressDialog.show(activity, "Progreso","Obteniendo Resultados");
		}
		});
		CommonDAO<PolizaVO> polizasDAO; 
		if(key[0].length() >= 0)
		{
			searchTerm = key[0];
			searchTerm = "%"+searchTerm.trim().toUpperCase().replace("�", "A").replace("�", "E").replace("�", "I").replace("�", "O").replace("�", "U")+"%";
			
			polizasDAO = new CommonDAO<PolizaVO>(activity, PolizaVO.class,SipacOpenHelper.TABLE_POLIZA);
			polizasDAO.open();
		    results = polizasDAO.runQuery("SELECT * FROM "+SipacOpenHelper.TABLE_POLIZA+" WHERE nombre_poliza LIKE '"+searchTerm+"' OR id_poliza LIKE '"+searchTerm+"' OR rfc_poliza LIKE '"+searchTerm+"' OR emp_poliza LIKE '"+searchTerm+"'");
		    countResults = polizasDAO.countItems("nombre_poliza LIKE '"+searchTerm+"' OR id_poliza LIKE '"+searchTerm+"' OR rfc_poliza LIKE '"+searchTerm+"' OR emp_poliza LIKE '"+searchTerm+"'");
		    DataModel.getInstance().setPolizas(results);
		}
		activity.runOnUiThread(new Runnable(){
            public void run(){
            	
            	if(countResults > 0 )
            	{
	            	PolizaAdapter adapter = new PolizaAdapter(activity,R.layout.item_poliza,results);
	                list.setAdapter(adapter);
	                adapter.notifyDataSetChanged();
	                textView.setText(countResults +" coincidencia"+((countResults > 1)?"s":"")+" para \""+searchTerm.replace("%", "")+"\"");
	                dialog.dismiss();
            	}else
            	{
            		dialog.dismiss();
            		(new AlertDialog.Builder(activity)).setMessage("No se encontraron coincidencias. Por favor realice otra b�squeda").setPositiveButton("Realizar nueva b�squeda",
            				new DialogInterface.OnClickListener() {
    					
    					@Override
    					public void onClick(DialogInterface dialog, int which) {	
    						activity.onBackPressed();
    					}
    				}).show();
            	}
            }
		});
	   return null;
	}
}
