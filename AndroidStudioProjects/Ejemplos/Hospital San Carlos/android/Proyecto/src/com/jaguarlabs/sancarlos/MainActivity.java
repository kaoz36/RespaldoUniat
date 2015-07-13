package com.jaguarlabs.sancarlos;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	static MainActivity mthis;
	boolean _log = true;
	
	Animation anim;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mthis = this;
		setContentView(R.layout.activity_menu);
		final ImageButton consultas = (ImageButton) mthis.findViewById(R.id.consultas);
		pressButtom(consultas, Consultas.class);
		final ImageButton expedientes = (ImageButton) mthis.findViewById(R.id.expedientes);
		pressButtom(expedientes, Expedientes.class);
		log("MainActivity", "activity");
	}
		
	@Override
	protected void onResume() {		
		super.onResume();
		final ImageButton expedientes = (ImageButton) mthis.findViewById(R.id.expedientes);
		expedientes.setVisibility(View.VISIBLE);
		final ImageButton consultas = (ImageButton) mthis.findViewById(R.id.consultas);
		consultas.setVisibility(View.VISIBLE);
	}
	
	private void pressButtom(final ImageButton boton, final Class id){
		boton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				anim = AnimationUtils.loadAnimation(mthis, R.anim.press_buttom);
				anim.setAnimationListener(new AnimationListener() {					
					@Override
					public void onAnimationStart(Animation animation) {}
					@Override
					public void onAnimationRepeat(Animation animation) {}
					@Override
					public void onAnimationEnd(Animation animation) {
						Intent i = new Intent(mthis, id);
						mthis.startActivity(i);
						boton.setVisibility(View.INVISIBLE);
						boton.clearAnimation();
					}
				});
				anim.setFillAfter(true);
				boton.startAnimation(anim);
			}
		});
	}
	
	private void log(String tag, String msg){
		if( _log ){
			Log.i(tag, msg);
		}			
	}
	
}
