/** *************************************************************************
 *
 *   Copyright (c)  2013 by Jaguar Labs.
 *   Confidential and Proprietary
 *   All Rights Reserved
 *
 *   This software is furnished under license and may be used and copied
 *   only in accordance with the terms of its license and with the
 *   inclusion of the above copyright notice. This software and any other
 *   copies thereof may not be provided or otherwise made available to any
 *   other party. No title to and/or ownership of the software is hereby
 *   transferred.
 *
 *   The information in this software is subject to change without notice and
 *   should not be construed as a commitment by JaguarLabs.
 *
 * @(#)$Id: $
 * Last Revised By   : efren campillo
 * Last Checked In   : $Date: $
 * Last Version      : $Revision:  $
 *
 * Original Author   : Julio Hernandez  julio.hernadez@jaguarlabs.com
 * Origin            : SEnE -- march 13 @ 11:00 (PST)
 * Notes             :
 *
 ** *************************************************************************/

package com.jaguarlabs.taxymobile;

import org.json.JSONObject;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Capture class
 * This activity is to capture data
 ** */

public class Capture extends Activity {
	static Capture mthis = null;
	
	boolean debug = true;	
	
	static String completeName	= "";
	static String phoneNumber 	= "";
	static String android_id 	= "";
	
	int sitioId = 1;
	
	boolean menulayout = false;
	
	Animation anim;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mthis=this;
		Splash.mthis.finish();
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		android_id = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
		setContentView(R.layout.activity_capture);
		setListeners();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_capture, menu);
		return true;
	}
	
	/*
	 * setListeners
	 * set the screen to capture data
	 * @receive  
	 * @return void
	 */
	private void setListeners() {
		final ImageView buttonmenu = (ImageView)findViewById(R.id.buttonMenuCapture);
		final LinearLayout layoutmenu=(LinearLayout)mthis.findViewById(R.id.menulayoutCapture); 
		buttonmenu.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				menu( layoutmenu, buttonmenu );
				
			}			
		});
		
		TelephonyManager tMgr =(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		EditText editTextNum = (EditText)mthis.findViewById(R.id.editTextPhoneNum);
		editTextNum.setText(tMgr.getLine1Number());
		
		ImageButton imgBttnSave = (ImageButton)mthis.findViewById(R.id.imageButtonSave);
		
		imgBttnSave.setOnTouchListener(new OnTouchListener() {
			
			@Override 
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN){
					ImageButton imgBttnTmp = (ImageButton)mthis.findViewById(R.id.imageButtonSave);
					imgBttnTmp.setImageDrawable(getResources().getDrawable(R.drawable.btn_ok_pressed));					
					
				}else if (event.getAction() == MotionEvent.ACTION_UP) {
					InputMethodManager inputManager = (InputMethodManager)            
					mthis.getSystemService(Context.INPUT_METHOD_SERVICE); 
				    inputManager.hideSoftInputFromWindow(mthis.getCurrentFocus().getWindowToken(),
				    InputMethodManager.HIDE_NOT_ALWAYS);

					ImageButton imgBttnTmp = (ImageButton)mthis.findViewById(R.id.imageButtonSave);
					imgBttnTmp.setImageDrawable(getResources().getDrawable(R.drawable.btn_ok_normal));
					EditText editTextName = (EditText)mthis.findViewById(R.id.editTextName);
					EditText editTextNum = (EditText)mthis.findViewById(R.id.editTextPhoneNum);
					
					completeName = editTextName.getText().toString();
					phoneNumber = editTextNum.getText().toString();
					
					if (completeName.length() > 0 && phoneNumber.toString().length() > 0) {
						PostTask task = new PostTask();
						task.applicationContext = mthis;
						task.post_case = 3;
						task.execute();
						
					} else {
						AlertDialog alert = new AlertDialog.Builder(mthis).create();
						alert.setTitle("Debes llenar todos los campos...");
						alert.setButton("OK", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
							}
						});
						alert.show();
					}
					
				}
				
				return false;
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		final LinearLayout layoutmenu = (LinearLayout)mthis.findViewById(R.id.menulayoutCapture); 
		switch( keyCode ){
			///keyback
			case 4:
				if( menulayout ){
					menulayout = false;
					Animation inmenu = AnimationUtils.loadAnimation(mthis, R.layout.animation_menu_out);
					layoutmenu.startAnimation(inmenu);
					layoutmenu.setVisibility(View.GONE);
				}else{					
					mthis.finish();					
				}	
				break;
			////Ajustes
			case 82:
				menu( layoutmenu,  (ImageView)findViewById(R.id.buttonMenuCapture) );
				break;
		}
	    return true;
	}
	
	/*
	 * menu
	 * method for show the menu
	 * @receive  
	 *  LinearLayout layoutmenu
	 * @return void
	 */
	public void menu(final LinearLayout layoutmenu, final ImageView menuButton){
		final FrameLayout helptem = (FrameLayout) mthis.findViewById(R.id.helptem);
		if( menulayout ){
			menulayout = false;
			Animation inmenu = AnimationUtils.loadAnimation(mthis, R.layout.animation_menu_out);
			inmenu.setAnimationListener(new AnimationListener() {				
				@Override
				public void onAnimationStart(Animation animation) {
					menuButton.setEnabled( false );
				}				
				@Override
				public void onAnimationRepeat(Animation animation) {}				
				@Override
				public void onAnimationEnd(Animation animation) {
					menuButton.setEnabled( true );
					helptem.setVisibility( View.GONE );
				}
			});
			layoutmenu.startAnimation(inmenu);
			
			layoutmenu.setVisibility(View.GONE);
		}else{
			menulayout = true;
			layoutmenu.setVisibility(View.VISIBLE);
			Animation inmenu = AnimationUtils.loadAnimation(mthis, R.layout.animation_menu_in);
			inmenu.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					menuButton.setEnabled( false );
				}
				@Override
				public void onAnimationRepeat(Animation animation) {}				
				@Override
				public void onAnimationEnd(Animation animation) {
					menuButton.setEnabled( true );
					helptem.setVisibility( View.VISIBLE );
				}
			});
			layoutmenu.startAnimation(inmenu);
			
			LinearLayout help = (LinearLayout) layoutmenu.findViewById(R.id.layout_help);
			help.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startHelp();
					Animation inmenu = AnimationUtils.loadAnimation(mthis, R.layout.animation_menu_out);
					layoutmenu.startAnimation(inmenu);
					layoutmenu.setVisibility(View.GONE);
					helptem.setVisibility( View.GONE );
					menulayout = false;
				}
			});	
			
			helptem.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					helptem.setVisibility( View.GONE );
					menu(layoutmenu, menuButton);
				}
			});
		}
		
		loadfontCity( (TextView) mthis.findViewById(R.id.help));
		
	}
	
	/*
	 * startHelp
	 * method for show help
	 * @receive
	 * @return void
	 */
	public void startHelp(){
		final RelativeLayout help = (RelativeLayout) mthis.findViewById(R.id.relative_help_capture);
		anim = AnimationUtils.loadAnimation(mthis, R.anim.help_in);
		anim.setAnimationListener(new AnimationListener() {			
			@Override
			public void onAnimationStart(Animation animation) {
				help.setVisibility(View.VISIBLE);
			}			
			@Override
			public void onAnimationRepeat(Animation animation) {}			
			@Override
			public void onAnimationEnd(Animation animation) {
				help.setOnTouchListener(new OnTouchListener() {			
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if( event.getAction() == MotionEvent.ACTION_DOWN ){
							return true;
						}else if( event.getAction() == MotionEvent.ACTION_UP ){
							anim = AnimationUtils.loadAnimation(mthis, R.anim.help_out);
							anim.setFillAfter(true);
							anim.setAnimationListener(new AnimationListener() {								
								@Override
								public void onAnimationStart(Animation animation) {}								
								@Override
								public void onAnimationRepeat(Animation animation) {}								
								@Override
								public void onAnimationEnd(Animation animation) {
									help.setVisibility( View.GONE );
									help.clearAnimation();
								}
							});
							help.startAnimation(anim);							
							return true;
						}
						return false;
					}
				});
				help.clearAnimation();
			}
		});
		anim.setFillAfter(true);
		help.startAnimation(anim);
	}
	
	/*
	 * post_register
	 * method for send data to server
	 * @receive  
	 * @return RESTClient
	 */
	public RESTClient post_register(){
		RESTClient rest=new RESTClient(TaxyMobile.base_url+"/register");
		try{
			JSONObject post=new JSONObject();
			post.put("DeviceId", android_id);
			post.put("nombre", completeName);
			post.put("numero", phoneNumber);
			post.put("sitio", ""+sitioId);
			rest.post = post.toString();
			rest.Execute(RESTClient.RequestMethod.POST);
			log("registerResponse", ""+rest.getResponse());				
		}catch(Exception e){
			log("registerError",""+e);
		}
		return rest;
	}
	
	/*
	 * manage_register
	 * method for send data to server
	 * @receive 
	 *  JSONObject rs
	 * @return void
	 */
	public void manage_register(JSONObject rs) {
		try{
			String success = rs.getString("success");
			log("Succes", success);
			if(success.equals("true")){
				Splash.mthis.capturaDatos(completeName, phoneNumber, rs.getString("id"));
				Intent i = new Intent(mthis,TaxyMobile.class);
				i.putExtra("name", completeName);
				i.putExtra("number", phoneNumber);
				i.putExtra("id", rs.getString("id"));
				startActivity(i);
				finish();
			}else{
				Toast.makeText(mthis, "No se pudo Guardar la Información", Toast.LENGTH_SHORT).show();
			}
		}catch(Exception e){
			e.printStackTrace();
			Toast.makeText(mthis, "Error Al Guardar Informacion", Toast.LENGTH_SHORT).show();
		}
	}
	
	/* log
	 * method for print in the log of the system
	 * @receive 
	 * 	String tag
	 * 	String msg
	 * @return void
	 * */
	public void log(String tag, String msg) {
		if (debug){
			Log.d(tag, msg);
		}			
	}
	
	/*
	 * loadfontCity
	 * method for load a font for aplication
	 * @receive 
	 *  TextView text
	 * @return void
	 */
	public void loadfontCity(TextView text){
		Typeface myFont = Typeface.createFromAsset(getAssets(),
			"font/myriadpro_regular.otf");
		text.setTypeface(myFont);
	}
	

}
