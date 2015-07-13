package com.jaguarlabs.sancarlos;

import android.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListaExpediente extends Activity{
	
	static ListaExpediente mthis;
	boolean _log = true;
	String name;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mthis = this;
		setContentView(R.layout.activity_expedientes_info);
		TextView nameT = (TextView) mthis.findViewById( R.id.expname );
		name = getIntent().getStringExtra("name");
		nameT.setText( name );
		
		RelativeLayout histo_clinic 			= (RelativeLayout) mthis.findViewById(R.id.hitoriaclinica);		
		//TODO: Faltan
		RelativeLayout info_final 				= (RelativeLayout) mthis.findViewById(R.id.informefinal);
		RelativeLayout esfuerzo_final 			= (RelativeLayout) mthis.findViewById(R.id.pruebadeesfuerzofinal);
		RelativeLayout seguimiento_diario 		= (RelativeLayout) mthis.findViewById(R.id.hojadeseguimiento);
		RelativeLayout electrocardiograficos 	= (RelativeLayout) mthis.findViewById(R.id.trazoselectrocardiograficos);
		RelativeLayout glucometrias 			= (RelativeLayout) mthis.findViewById(R.id.glucometrias);
		RelativeLayout hoja_eventualidades 		= (RelativeLayout) mthis.findViewById(R.id.hojaeventualidades);
		RelativeLayout objetivos_personales 	= (RelativeLayout) mthis.findViewById(R.id.objetivospersonales);
		RelativeLayout estratificacion 			= (RelativeLayout) mthis.findViewById(R.id.estratificacion);
		RelativeLayout esfuerzo_inicial 		= (RelativeLayout) mthis.findViewById(R.id.pruebaesfuerzoinicial);
		RelativeLayout evaluacion_nutricional 	= (RelativeLayout) mthis.findViewById(R.id.evaluacionnutricional);
		RelativeLayout evaluacion_psicologica 	= (RelativeLayout) mthis.findViewById(R.id.evaluacionpsicologica);
		RelativeLayout consentimiento 			= (RelativeLayout) mthis.findViewById(R.id.consentimientoinformado);
		
		presslayout(histo_clinic, HistorialClinico.class);
		presslayout(info_final, HistorialClinico.class);
		presslayout(esfuerzo_final, HistorialClinico.class);
		presslayout(seguimiento_diario, HistorialClinico.class);
		presslayout(electrocardiograficos, HistorialClinico.class);
		presslayout(glucometrias, HistorialClinico.class);
		presslayout(hoja_eventualidades, HistorialClinico.class);
		presslayout(objetivos_personales, HistorialClinico.class);
		presslayout(estratificacion, HistorialClinico.class);
		presslayout(esfuerzo_inicial, HistorialClinico.class);
		presslayout(evaluacion_nutricional, HistorialClinico.class);
		presslayout(evaluacion_psicologica, HistorialClinico.class);
		presslayout(consentimiento, HistorialClinico.class);
		
	}
	
	private void presslayout( final RelativeLayout layout, final Class id){
		
		layout.setOnTouchListener(new OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if( event.getAction() == MotionEvent.ACTION_DOWN ){
					layout.setBackgroundResource(R.drawable.layoutdata_press);
					return true;
				}else if( event.getAction() == MotionEvent.ACTION_UP ){
					layout.setBackgroundResource(R.drawable.layoutdata);
					if( !id.equals( Splash.class)){
						Intent i = new Intent(mthis, id);
						i.putExtra("name", name);
						mthis.startActivity(i);
					}
					return true;
				}else if( event.getAction() == MotionEvent.ACTION_CANCEL ){
					layout.setBackgroundResource(R.drawable.layoutdata);
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
