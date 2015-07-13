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
import android.widget.TextView;

public class Consultas extends Activity{

	static Consultas mthis;
	boolean _log = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mthis = this;
		setContentView(R.layout.activity_consultas);
		
		pressLinear( (LinearLayout) mthis.findViewById(R.id.linear1) );
		pressLinear( (LinearLayout) mthis.findViewById(R.id.linear2) );
		pressLinear( (LinearLayout) mthis.findViewById(R.id.linear3) );
		pressLinear( (LinearLayout) mthis.findViewById(R.id.linear4) );
		pressLinear( (LinearLayout) mthis.findViewById(R.id.linear5) );
		pressLinear( (LinearLayout) mthis.findViewById(R.id.linear6) );
		
	}
	
	private void pressLinear(final LinearLayout layout){
		layout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if( event.getAction() == MotionEvent.ACTION_DOWN ){
					layout.setBackgroundResource(R.drawable.layoutdata_press);
					return true;
				}else if( event.getAction() == MotionEvent.ACTION_UP ){
					layout.setBackgroundResource(R.drawable.layoutdata);
					Intent i = new Intent(mthis, HistorialClinico.class);
					TextView text = (TextView) layout.findViewById(R.id.name);
					i.putExtra("name", text.getText().toString());
					mthis.startActivity(i);
					return true;
				}else if( event.getAction() == MotionEvent.ACTION_CANCEL ){
					layout.setBackgroundResource(R.drawable.layoutdata);
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
