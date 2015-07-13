package com.jaguarlabs.sancarlos;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class Expedientes extends Activity{
	
	static Expedientes mthis;
	boolean _log = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mthis = this;
		setContentView(R.layout.activity_expedientes);
	}
	
	
	private void log(String tag, String msg){
		if( _log ){
			Log.i(tag, msg);
		}			
	}
}
