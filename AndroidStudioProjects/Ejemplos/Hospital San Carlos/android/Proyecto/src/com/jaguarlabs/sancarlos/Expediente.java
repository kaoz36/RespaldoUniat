package com.jaguarlabs.sancarlos;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class Expediente extends Activity{
	
	static Expediente mthis;
	boolean _log = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mthis = this;
		setContentView(R.layout.activity_historiaclinica);
	}
	
	
	private void log(String tag, String msg){
		if( _log ){
			Log.i(tag, msg);
		}			
	}
}
