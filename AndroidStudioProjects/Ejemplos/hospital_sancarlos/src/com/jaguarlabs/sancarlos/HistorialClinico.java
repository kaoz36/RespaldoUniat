package com.jaguarlabs.sancarlos;

import android.app.Activity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class HistorialClinico extends Activity{
	
	static HistorialClinico mthis;
	boolean _log = true;
	ImageView id_anterior = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mthis = this;
		setContentView(R.layout.activity_historiaclinica);
		
		TextView name = (TextView) mthis.findViewById(R.id.namehistorial);
		
		name.setText( getIntent().getStringExtra("name") );
		
		
		FrameLayout pestana1 = (FrameLayout) mthis.findViewById(R.id.pestana1);
		FrameLayout pestana2 = (FrameLayout) mthis.findViewById(R.id.pestana2);
		FrameLayout pestana3 = (FrameLayout) mthis.findViewById(R.id.pestana3);
		FrameLayout pestana4 = (FrameLayout) mthis.findViewById(R.id.pestana4);
		FrameLayout pestana5 = (FrameLayout) mthis.findViewById(R.id.pestana5);
		FrameLayout pestana6 = (FrameLayout) mthis.findViewById(R.id.pestana6);
		
		press_layout(pestana1, R.layout.layout_datos, (ImageView) mthis.findViewById(R.id.img1));
		press_layout(pestana2, R.layout.layout_antecedentes, (ImageView) mthis.findViewById(R.id.img2));
		press_layout(pestana3, R.layout.layout_exploracion, (ImageView) mthis.findViewById(R.id.img3));
		press_layout(pestana4, R.layout.layout_laboratorio, (ImageView) mthis.findViewById(R.id.img4));
		press_layout(pestana5, R.layout.layout_diagnostico, (ImageView) mthis.findViewById(R.id.img5));
		press_layout(pestana6, R.layout.layout_tratamiento, (ImageView) mthis.findViewById(R.id.img6));
		
		id_anterior = (ImageView) mthis.findViewById(R.id.img1);
		
		ViewGroup view = (ViewGroup)mthis.findViewById(R.id.layerlayout);
	    view.removeAllViews();
	    LayoutInflater inflater = getLayoutInflater();
	    View layoutlinear = inflater.inflate( R.layout.layout_datos, null );
	    view.addView(layoutlinear);
	}
	
	private void press_layout( final FrameLayout layout, final int id, final ImageView img){
		layout.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if( event.getAction() == MotionEvent.ACTION_DOWN ){
					id_anterior.setVisibility( View.INVISIBLE );
					return true;
				}else if( event.getAction() == MotionEvent.ACTION_UP ){
					id_anterior = img;
					img.setVisibility( View.VISIBLE );
					ViewGroup view = (ViewGroup)mthis.findViewById(R.id.layerlayout);
				    view.removeAllViews();
				    LayoutInflater inflater = getLayoutInflater();
				    View layoutlinear = inflater.inflate( id, null );
				    view.addView(layoutlinear);
					return true;
				}else if( event.getAction() == MotionEvent.ACTION_CANCEL ){
					id_anterior.setVisibility( View.VISIBLE );
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
