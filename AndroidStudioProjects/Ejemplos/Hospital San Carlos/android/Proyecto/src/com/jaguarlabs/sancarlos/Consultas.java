package com.jaguarlabs.sancarlos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.LinearLayout;

public class Consultas extends Activity{

	static Consultas mthis;
	boolean _log = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mthis = this;
		setContentView(R.layout.activity_consultas);
		final LinearLayout reg1 = (LinearLayout) mthis.findViewById(R.id.lenear1);
		reg1.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if( event.getAction() == MotionEvent.ACTION_DOWN ){
					reg1.setBackgroundResource(R.drawable.layoutdata_press);
					return true;
				}else if( event.getAction() == MotionEvent.ACTION_UP ){
					reg1.setBackgroundResource(R.drawable.layoutdata);
					Intent i = new Intent(mthis, Expediente.class);
					mthis.startActivity(i);
					return true;
				}else if( event.getAction() == MotionEvent.ACTION_CANCEL ){
					reg1.setBackgroundResource(R.drawable.layoutdata);
					return true;
				}
				return true;
			}
		});
	}
	
	private void log(String tag, String msg){
		if( _log ){
			Log.i(tag, msg);
		}			
	}
	
}
