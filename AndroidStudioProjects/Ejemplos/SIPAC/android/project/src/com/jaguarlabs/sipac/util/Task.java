package com.jaguarlabs.sipac.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class Task extends  AsyncTask <String, Integer, String>{

	public int post_case = 0;
	private ICallback call;
	private LayoutInflater inflater;
	private ViewGroup container;
	private ProgressDialog dialog;
	
	public Task( ICallback call ){
		this.call = call;
	}
	
	public Task( ICallback call, LayoutInflater inflater, ViewGroup container ){
		this.call = call;
		this.inflater = inflater;
		this.container = container;
	}
	
	@Override
	protected void onPreExecute() {
		
		if( post_case == 3 ){
			this.dialog = ProgressDialog.show( (Context)call, "", "Checando conexión de internet.", true);
		}else if( post_case == 4 ){
			this.dialog = ProgressDialog.show( (Context)call, "", "Cargando los datos.", true);
		}
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		try{
			call.time();
		}catch(Exception e){			
			e.printStackTrace();
		}
		return null;
	}	

	@Override
	protected void onPostExecute(String s) {
		Log.i("Postcase", "Execute");
		switch ( post_case ) {
			case 0:
				call.callback();
				break;
			case 1:
				call.callback(inflater, container);
				break;
			case 3:
				call.callback();
				dialog.cancel();
				break;
			case 4:
				call.callback();
				dialog.cancel();
				break;
			default:
				break;
		}		
	}
}
