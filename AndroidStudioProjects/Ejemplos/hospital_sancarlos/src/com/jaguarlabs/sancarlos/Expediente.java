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

public class Expediente extends Activity{
	
	static Expediente mthis;
	boolean _log = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mthis = this;
		setContentView(R.layout.activity_expedientes);
		
		press_layout( (LinearLayout) mthis.findViewById(R.id.linearlayout1) );
		press_layout( (LinearLayout) mthis.findViewById(R.id.linearlayout2) );
		press_layout( (LinearLayout) mthis.findViewById(R.id.linearlayout3) );
		press_layout( (LinearLayout) mthis.findViewById(R.id.linearlayout4) );
		press_layout( (LinearLayout) mthis.findViewById(R.id.linearlayout5) );
		press_layout( (LinearLayout) mthis.findViewById(R.id.linearlayout6) );
		press_layout( (LinearLayout) mthis.findViewById(R.id.linearlayout7) );
		press_layout( (LinearLayout) mthis.findViewById(R.id.linearlayout8) );
		press_layout( (LinearLayout) mthis.findViewById(R.id.linearlayout9) );
		press_layout( (LinearLayout) mthis.findViewById(R.id.linearlayout10) );
		press_layout( (LinearLayout) mthis.findViewById(R.id.linearlayout11) );
		
	}
	
	private void press_layout( final LinearLayout layout){
		layout.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if( event.getAction() == MotionEvent.ACTION_DOWN ){
					layout.setBackgroundResource( R.drawable.layoutdata_press );
					return true;
				}else if( event.getAction() == MotionEvent.ACTION_UP ){
					layout.setBackgroundResource( R.drawable.layoutdata );
					TextView text = (TextView) layout.findViewById(R.id.nameexp);
					Intent i = new Intent(mthis, ListaExpediente.class);
					i.putExtra("name", text.getText().toString());
					mthis.startActivity(i);
					return true;
				}else if( event.getAction() == MotionEvent.ACTION_CANCEL ){
					layout.setBackgroundResource( R.drawable.layoutdata );
					return true;
				}
				return false;
			}
		});
	}
	
	private void log(String tag, String msg){
		if( _log ){
			Log.i(tag, msg);
		}			
	}
}
