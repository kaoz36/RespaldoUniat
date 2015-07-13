package com.jaguarlabs.sipac.model;

import android.util.Log;
import android.view.View;

public class ViewModel {
	
	private static ViewModel instance;
	private View cotizadorView = null;
	
	
	private ViewModel() throws Exception{
		if(instance != null){
			throw new Exception("Error de Singleton");
		}			
	}
	
	public static ViewModel getInstance()
	{
		if(instance == null){
			try{
				instance = new ViewModel();
			}catch(Exception e){
				Log.e("Error",e.getMessage());
			}
		}
		return instance;
	}

	public View getCotizadorView() {
		return cotizadorView;
	}

	public void setCotizadorView(View cotizadorView) {
		this.cotizadorView = cotizadorView;
	}
}
