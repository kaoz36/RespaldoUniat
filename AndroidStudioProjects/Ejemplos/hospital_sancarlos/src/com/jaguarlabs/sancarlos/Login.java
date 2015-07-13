package com.jaguarlabs.sancarlos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity{

	static Login mthis;
	boolean _log = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mthis = this;
		setContentView(R.layout.activity_login);
		final EditText user = (EditText) mthis.findViewById(R.id.editText1);
		final EditText pass = (EditText) mthis.findViewById(R.id.editText2);
		loadfont( user );
		loadfont( pass );
		
		
		TextView aceptar = (TextView) mthis.findViewById( R.id.aceptar );
		aceptar.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				//if( "Doctor".equals( user.getText().toString()) && "Jaguarlabs".equals(pass.getText().toString())){
					Intent i = new Intent( mthis, MainActivity.class );
					mthis.startActivity(i);
					mthis.finish();
				/*}else{
					final AlertDialog preciso = new AlertDialog.Builder(mthis).create();
					preciso.setTitle("Error al entrar");
					preciso.setMessage("Por favor, escribe bien el usuario y la contraseña");
					preciso.setButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							preciso.cancel();
						}
					});
					preciso.show();
				}	*/
			}		
		});
		
		TextView salir = (TextView) mthis.findViewById(R.id.salir);
		salir.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				mthis.finish();				
			}
		});		
	}	
	
	public void loadfont(EditText view){
		Typeface myFont = Typeface.createFromAsset(getAssets(),
			"font/helveticalight.ttf");
		view.setTypeface(myFont);
	}
}

