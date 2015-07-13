package com.jaguarlabs.sipac.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.Editable;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.jaguarlabs.sipac.db.CommonDAO;
import com.jaguarlabs.sipac.db.SipacOpenHelper;
import com.jaguarlabs.sipac.vo.PolizaVO;
import com.jaguarlabs.sipac.vo.ValueVO;

public class ClearButtonCompleterTextWatcher extends ClearButtonTextWatcher  {

	private AutoCompleteTextView completer;
	
	private Boolean activated;
	private Activity activity;
	
	
	public ClearButtonCompleterTextWatcher (Activity pActivity ,Button pButton,AutoCompleteTextView pTextField)
	{
		super(pButton, pTextField);
		completer = pTextField;
		activated = false;
		activity = pActivity;
		
	}
	
	
	
	private void getSuggestions(){
		new GetSuggestions(activity,activated,completer).execute(completer.getText().toString());
	}

	public Boolean getActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
		if(this.activated)
		{
			getSuggestions();
			
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		super.afterTextChanged(s);
		getSuggestions();
	}

	
}

class GetSuggestions extends AsyncTask<String, String, String>
{
	private AutoCompleteTextView completer;
	private Boolean activated;
	private Activity activity;
	private List<ValueVO<PolizaVO>>results;
	
	public GetSuggestions(Activity pActivity , Boolean pActivated,AutoCompleteTextView pTextField){
		
		activity = pActivity;
		activated = pActivated;
		completer = pTextField;
	}
	protected String doInBackground(String... key) {
		results = new ArrayList<ValueVO<PolizaVO>>();
		CommonDAO<PolizaVO> polizasDAO; 
		String searchTerm;
		
		if(key[0].length() >= 4 && activated == true)
		{
			searchTerm = key[0];
			searchTerm = "%"+searchTerm.trim().toUpperCase().replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U")+"%";
			
			polizasDAO = new CommonDAO<PolizaVO>(activity, PolizaVO.class,SipacOpenHelper.TABLE_POLIZA);
			polizasDAO.open();
		    results = polizasDAO.runQuery("SELECT * FROM "+SipacOpenHelper.TABLE_POLIZA+" WHERE nombre_poliza LIKE '"+searchTerm+"' GROUP BY nombre_poliza LIMIT 100",4);
		    if(results.size() <= 0)
		    {
		    	results = polizasDAO.runQuery("SELECT * FROM "+SipacOpenHelper.TABLE_POLIZA+" WHERE id_poliza LIKE '"+searchTerm+"' GROUP BY id_poliza LIMIT 100",1);
		    }
		    if(results.size() <= 0)
		    {
		    	results = polizasDAO.runQuery("SELECT * FROM "+SipacOpenHelper.TABLE_POLIZA+" WHERE rfc_poliza LIKE '"+searchTerm+"' GROUP BY rfc_poliza LIMIT 100",5);	
		    }
		    
		    if(results.size() <= 0)
		    {
		    	results = polizasDAO.runQuery("SELECT * FROM "+SipacOpenHelper.TABLE_POLIZA+" WHERE emp_poliza LIKE '"+searchTerm+"' GROUP BY emp_poliza LIMIT 100",6);	
		    }
		}
		activity.runOnUiThread(new Runnable(){
            public void run(){
            	ArrayAdapter<ValueVO<PolizaVO>> aAdapter;
            	completer.setAdapter(aAdapter = new ArrayAdapter<ValueVO<PolizaVO>>(activity,android.R.layout.simple_dropdown_item_1line,results));
                aAdapter.notifyDataSetChanged();
            }
		});
	   return null;
	}
}
