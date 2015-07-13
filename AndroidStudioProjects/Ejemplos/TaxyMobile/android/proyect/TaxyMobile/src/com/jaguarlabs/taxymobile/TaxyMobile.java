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

import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * TaxyMobile class 
 * principal activity for the application
 ** */

public class TaxyMobile extends Activity {
	public static TaxyMobile mthis 	= null; 
	public static String base_url	="http://10.0.0.26:8080/taxi/mobile"; //LAP
//	public static String base_url	="http://192.168.1.78:8080/taxi/mobile";
//	public static String base_url	="http://192.168.1.107:8080/taxi/mobile";//Mio
//	public static String base_url	="http://10.150.100.22:8080/taxi/mobile";
//	public static String base_url	="http://192.168.1.76:8080/taxi/mobile";
//	public static String base_url	="http://192.168.1.71:8080/taxi/mobile"; PC
	public static String Token	= "";
	String location				= "";
	String comment				= "";
	volatile String status		= "";
	String ciudad				= "";
	static String android_id 	= "";
	
	int zona					= 1;
	int contador				= 0;
	int threadTime				= 10000;
	
	static String completeName	= "";
	static String phoneNumber	= "";
	static String userId		= "";
	String peticionId			= "";
	static int time 			= 10;
	static String taxy 			= "Taxi";

	Spinner choose;

	boolean debug				= true;
	boolean _menulayout 		= false;
	boolean _setCronometro		= true;
	boolean _menu				= true;
	boolean _rechazada			= false;
	boolean _final				= false;
	static boolean _isReached	= true;
	
	static Thread update 		= null;
	Thread cronometro			= null;
	
	Animation anim;
	
	ArrayList<JSONObject> citys;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mthis 			= this;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);	
		android_id 		= Secure.getString(getContentResolver(), Secure.ANDROID_ID);
		Intent i		= mthis.getIntent();
		completeName	= i.getStringExtra("name");
		phoneNumber		= i.getStringExtra("number");
		userId			= i.getStringExtra("id");
		log("Update", "update: " + update);
		log("Cronometro", "cronometro: " + cronometro);
		showCallTaxy();		
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	/*
	 * showCallTaxy
	 * method for call a taxi
	 * @receive  
	 * @return void
	 */
	public void showCallTaxy() {
		setContentView(R.layout.activity_taxy_mobile);
		final SharedPreferences pref 	= mthis.getPreferences(MODE_PRIVATE);
		_rechazada 						= false;		
		_menu 							= true;		
		final ImageView buttonmenu 		= (ImageView)findViewById(R.id.buttonmenu);
		final ImageButton taxi 			= (ImageButton)findViewById(R.id.calltaxi);
		taxi.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if( event.getAction() == MotionEvent.ACTION_DOWN ){
					taxi.setImageResource(R.drawable.btn_pedir_big_pressed);
					buttonmenu.setEnabled( false );
					return true;
				}else if( event.getAction() == MotionEvent.ACTION_UP ){
					Editor e = pref.edit();
					e.putBoolean( "_stop", false );
					e.commit();
					taxi.setEnabled( false );
					taxi.setImageResource(R.drawable.btn_pedir_big_normal);
					_setCronometro 	= true;
					_final 			= false;
					startLocalization();
					buttonmenu.setEnabled( true );
					return true;
				}
				return true;
			}			
		});
		
		final LinearLayout layoutmenu = (LinearLayout)mthis.findViewById(R.id.menulayout); 
		buttonmenu.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				menu( layoutmenu, buttonmenu );
			}
		});
		
		log( "peticionId pref", "El peticionId es: " + pref.getString("peticionId", "0") );
		log( "_stop pref", "El _stop es: " + pref.getBoolean("_stop", false) );
		if( !pref.getString("peticionId", "0").equals("0") && !pref.getBoolean("_stop", false) ){
			log( "Entra en donde estaba", pref.getString("peticionId", "0") );
			status 		= pref.getString("status", "Nueva");
			peticionId 	= pref.getString("peticionId", "0");
			ciudad 		= pref.getString("ciudad", "");
			log( "status String", status );
			log( "peticionId String", peticionId );
			log( "Ciudad String", ciudad );
			setAssigning();
			create_hilo();
		}else{
			getLocalization();
		}
	}
	
	/*
	 * startLocalization
	 * method for get coordinates
	 * @receive  
	 * @return void
	 */
	public void startLocalization() {		
//		Intent i = new Intent(this, Location.class);
//		mthis.startActivityForResult(i, 5);
	}
	
	/*
	 * startHelp
	 * method for show help
	 * @receive  
	 *  RelativeLayout help
	 * @return void
	 */
	public void startHelp( final RelativeLayout help ){
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
	 * call_taxy
	 * method for create peticion
	 * @receive  
	 * @return void
	 */
	public void call_taxy() {
		if( Splash.mthis.isReached ){
			log("creatingPeticion","start");
			PostTask task = new PostTask();
			task.applicationContext = mthis;
			task.post_case = 0;
			task.execute();
		}else{
			mthis.showAlertDialog("Error de conexión.", "Intenta mas tarde.");
		}
	}
	
	/*
	 * call_update
	 * method for create update
	 * @receive  
	 * @return void
	 */
	public void call_update() {
		log("callingUpdate", "start");
		PostTask task = new PostTask();
		task.applicationContext = mthis;
		task.post_case = 4;
		task.execute();
	}
	
	/*
	 * call_cancel
	 * method for cancel a peticion
	 * @receive  
	 * @return void
	 */
	public void call_cancel() {
		if( Splash.mthis.isReached ){
			log("callingCancel","start");
			SharedPreferences pref 	= mthis.getPreferences(MODE_PRIVATE);
			Editor edit				= pref.edit();
			edit.putBoolean("_stop", false);
			edit.commit();
			PostTask task = new PostTask();
			task.applicationContext = mthis;
			task.post_case = 1;
			task.execute();
		}else{
			mthis.showAlertDialog("Error de conexión.", "Intenta mas tarde.");
			if( mthis.findViewById(R.id.btn_stop) != null)
				mthis.findViewById(R.id.btn_stop).setEnabled( true );
			if( mthis.findViewById(R.id.cancelbutton) != null)
				mthis.findViewById(R.id.cancelbutton).setEnabled( true );
		}
		
	}
	
	/*
	 * call_noAtend
	 * method for notify a no service
	 * @receive  
	 * @return void
	 */
	public void call_noAtend() {
		if( Splash.mthis.isReached ){
			log("callingNotAttend","start");
			PostTask task=new PostTask();
			task.applicationContext = mthis;
			task.post_case = 2;
			task.execute();
		}else{
			mthis.showAlertDialog("Error de conexión.", "Intenta mas tarde.");
		}
	}
	
	/*
	 * call_atendido
	 * method for notify a service
	 * @receive  
	 * @return void
	 */
	public void call_atendido() {
		if( Splash.mthis.isReached ){
			log("callingAtendido", "start");
			PostTask task = new PostTask();
			task.applicationContext = mthis;
			task.post_case = 6;
			task.execute();
		}else{
			mthis.showAlertDialog("Error de conexión.", "Intenta mas tarde.");
		}
	}
	
	/*
	 * post_createpeticion
	 * method for get request the create peticion
	 * @receive  
	 * @return RESTClient
	 */
	public RESTClient post_createpeticion(){
		SharedPreferences pref 	= mthis.getPreferences(MODE_PRIVATE);
		RESTClient rest=new RESTClient(base_url+"/createPeticion");
		try{
			JSONObject post=new JSONObject();
			post.put("DeviceId", android_id);
			post.put("userId", userId + "");
			post.put("sitio", pref.getString("id", ""));
			if( !getGPSStatus( mthis ) ){
				Log.i("GPS", "Desactivado");
				mthis.location = "0, 0";
			}
			post.put("location", mthis.location + "");
			post.put("comentario",mthis.comment + "");
			rest.post=post.toString();
			rest.Execute(RESTClient.RequestMethod.POST);
			log("createResponse",""+rest.getResponse());
		}catch(Exception e){
			log("createError",""+e);
		}
		return rest;
	}
	
	/*
	 * getGPSStatus
	 * method for get the GPS status
	 * @receive 
	 *  Activity act
	 * @return boolean
	 */
	public boolean getGPSStatus(Activity act){
		String allowedLocationProviders = Settings.System.getString(act.getContentResolver(),
	   	Settings.System.LOCATION_PROVIDERS_ALLOWED);
	   	if (allowedLocationProviders == null) {
	   		allowedLocationProviders = "";
	   	}	    
	   	return allowedLocationProviders.contains(LocationManager.GPS_PROVIDER);
	}
	
	/*
	 * post_cancelPeticion
	 * method for get a cancel peticion
	 * @receive
	 * @return RESTClient
	 */
	public RESTClient post_cancelPeticion(){
		RESTClient rest = new RESTClient(base_url+"/cancelPeticion");
		try{
			JSONObject post = new JSONObject();
			post.put("DeviceId", android_id);
			post.put("userId", userId + "");
			post.put("peticion", ""  + mthis.peticionId);
			rest.post = post.toString();
			rest.Execute(RESTClient.RequestMethod.POST);			
			log("cancelResponse", "" + rest.getResponse());
		}catch(Exception e){
			log("cancelError", "" + e);
		}
		return rest;
	}
	
	/*
	 * post_noAtendedPeticion
	 * method for get a notify no service
	 * @receive  
	 * @return RESTClient
	 */
	public RESTClient post_noAtendedPeticion(){
		RESTClient rest = new RESTClient(base_url+"/noService");
		try{
			JSONObject post=new JSONObject();
			post.put("DeviceId", android_id);
			post.put("userId", userId + "");
			post.put("peticion", ""+mthis.peticionId);
			rest.post=post.toString();
			rest.Execute(RESTClient.RequestMethod.POST);
			log("noAtendedPeticion",""+rest.getResponse());
		}catch(Exception e){
			log("noAtendedPeticionError", "" + e);
		}
		return rest;
	}
	
	/*
	 * post_AtendedPeticion
	 * method for get notify a service
	 * @receive  
	 * @return void
	 */
	public RESTClient post_AtendedPeticion(){
		RESTClient rest=new RESTClient(base_url+"/atendido");
		try{
			JSONObject post=new JSONObject();
			post.put("DeviceId", android_id);
			post.put("userId", userId + "");
			post.put("peticion", mthis.peticionId + "");			
			rest.post = post.toString();
			rest.Execute(RESTClient.RequestMethod.POST);
			log("AtendedPeticion", rest.getResponse() + "");
		}catch(Exception e){
			log("AtendedPeticionError", "" + e);
		}
		return rest;
	}
	
	/*
	 * post_update
	 * method for get the update
	 * @receive  
	 * @return RESTClient
	 */
	public RESTClient post_update(){
		RESTClient rest=new RESTClient(base_url+"/update");
		try{
			JSONObject post = new JSONObject();
			post.put("DeviceId", android_id);
			post.put("userId", userId + "");
			post.put("peticion", mthis.peticionId + "");
			rest.post = post.toString();
			rest.Execute(RESTClient.RequestMethod.POST);
			log("updateResponse", rest.getResponse() + "");
		}catch(Exception e){
			log("updateError",""+e);
		}
		return rest;
	}	
	
	/*
	 * post_getLocalization
	 * method for get an area 
	 * @receive  
	 * @return RESTClient
	 */
	public RESTClient post_getLocalization() {
		RESTClient rest=new RESTClient(base_url+"/getZoneList");
		try{
			JSONObject post = new JSONObject();			
			rest.post = post.toString();
			rest.Execute(RESTClient.RequestMethod.POST);
			log("Localization",""+rest.getResponse());
		}catch(Exception e){
			log("noGetLocalizationError", "" + e);
		}
		return rest;
	}	
	
	/*
	 * manage_createpeticion
	 * method for create petition
	 * @receive 
	 *  JSONObject rs
	 * @return void
	 */
	public void manage_createpeticion(JSONObject rs){
		try{
			String success = rs.getString("success");
			if(success.equals("true")){
				log("crearPeticion","success:true");
				peticionId = rs.getString("id");
				status = "Nueva";
				setAssigning();				
				//create thread
				create_hilo();
				
			}else{
				showToast("No se pudo Crear la Peticion");
			}
		}catch(Exception e){
			e.printStackTrace();
			showToast("Error Al Crear Peticion");
		}
	}
	
	/*
	 * setAssigning
	 * method for assigning a taxi
	 * @receive
	 * @return void
	 */
	public void setAssigning(){
		mthis.setContentView(R.layout.assigning_taxy_mobile);
		_menu							= false;
		_menulayout 					= false;
		threadTime 						= 10000;
		final ImageButton stop 			= (ImageButton) mthis.findViewById(R.id.btn_stop);
		final ImageView help 			= (ImageView) mthis.findViewById(R.id.help_icon);
		TextView assigning = (TextView) mthis.findViewById(R.id.buscando);
		loadfont( assigning );
		assigning.setText( assigning.getText() + " " + ciudad + ".");
		
		stop.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if( event.getAction() == MotionEvent.ACTION_DOWN){
					stop.setImageResource(R.drawable.btn_stop_pressed);
					help.setEnabled(false);
					return true;
				}else if( event.getAction() == MotionEvent.ACTION_UP){
					stop.setImageResource(R.drawable.btn_stop_normal);
					stop.setEnabled( false );
					MyTask cancel = new MyTask( mthis );
					cancel.execute = 1;
					cancel.execute();
//					Log.i("Internet stop", getConexion( base_url ) + "");
//					if( getConexion( base_url ) ){
//						
//					}else{
//						Log.i("Internet", getConexion( base_url ) + "");
//					}
					return true;
				}else if( event.getAction() == MotionEvent.ACTION_CANCEL ){
					stop.setImageResource(R.drawable.btn_stop_normal);
					return true;
				}
				return true;
			}
		});
		
		help.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if( event.getAction() == MotionEvent.ACTION_DOWN ){
					stop.setEnabled(false);
					return true;
				}else if( event.getAction() == MotionEvent.ACTION_UP ){
					startHelp( (RelativeLayout) mthis.findViewById(R.id.relative_help_assigning) );
					stop.setEnabled(true);
					return true;
				}
				return true;
			}
		});
	}

	/*
	 * getConexion
	 * method for check if there is internet
	 * @receive 
	 *  Context ctx
	 * @return boolean
	 */	
	public static boolean getConexion( final String address ) {
		mthis.log("AndroidManagementActivity", "Checking Connectivity to : "+ address);
		long startTime = System.currentTimeMillis();		
		new Thread() {
			public void run() {
				URL aURL;
				java.net.HttpURLConnection mConn;
				try {
					aURL = new URL(address);
					mConn = (java.net.HttpURLConnection) aURL.openConnection();
					mConn.connect();
					_isReached = true;
					if (mConn != null) {
						mConn.disconnect();
						mConn = null;
					}
					if (aURL != null)
						aURL = null;
				} catch (Exception e) {
					Log.w("application", "Exception:" + e);
					_isReached = false;
					mConn = null;
					aURL = null;
				}
			}
		}.start();
		while ((System.currentTimeMillis() - startTime < 5000));
		return _isReached;	
	}

	/*
	 * create_hilo
	 * method for create a thread
	 * @receive  
	 * @return void
	 */
	public void create_hilo(){
		Log.i("Update", "update: " + update);
		log("Cronometro", "cronometro: " + cronometro);
		log("Status Create hilo", status + ".");
		update = new Thread() {
			public void run() {
				while( status.equals("Nueva") || status.equals("Asignada") || status.equals("En Progreso") ){
					log("update","sending request");
					Log.i("Status dentro del hilo", status);
					call_update();		
					try{
						Thread.sleep( threadTime );
						Log.i( "ThreadTime", threadTime + " Status: " + status );
					}catch(Exception e){}
				}
			}
		};
		update.start();
		log("Update Se creo el hilo", "update: " + update);
		log("Cronometro se creo hilo", "cronometro: " + cronometro);
	}
	
	/*
	 * manage_cancelPeticion
	 * method for call a feedback
	 * @receive
	 * @return void
	 */
	public void manage_cancelPeticion(){
		SharedPreferences pref = mthis.getPreferences(MODE_PRIVATE);
		status = "Cancelada";
		log("Status: final peticion",status );
		Editor editor = pref.edit();
		editor.putBoolean("_stop", true);
		editor.putString("peticionId", "0");
		editor.commit();
		mthis.startFeedBack();
	}
	
	/*
	 * manage_atendedPeticion
	 * method for call a feedback
	 * @receive
	 * @return void
	 */
	public void manage_atendedPeticion(){
		SharedPreferences pref = mthis.getPreferences(MODE_PRIVATE);
		status = "Atendida";
		log("Status: final peticion",status );
		Editor editor = pref.edit();
		editor.putBoolean("_stop", true);
		editor.putString("peticionId", "0");
		editor.commit();
		mthis.startFeedBack();
	}
	
	/*
	 * manage_noAtendedPeticion
	 * method for call a feedback
	 * @receive
	 * @return void
	 */
	public void manage_noAtendedPeticion(){
		SharedPreferences pref = mthis.getPreferences(MODE_PRIVATE);
		status = "No Atendida";
		log("Status: final peticion",status );
		Editor editor = pref.edit();
		editor.putString("peticionId", "0");
		editor.commit();
		mthis.startFeedBack();
	}

	/*
	 * manage_getLocalization
	 * method for get area be a server
	 * @receive 
	 *  JSONObject rs
	 * @return void
	 */
	public void manage_getLocalization( JSONObject rs ){
		SharedPreferences pref = mthis.getPreferences(MODE_PRIVATE);
		Log.i( rs.toString(), rs.toString() );
		try{
			ArrayList<String> id_tem = new ArrayList<String>();
			ArrayList<String> citys_tem = new ArrayList<String>();
			JSONArray array = rs.getJSONArray("result");
			citys = new ArrayList<JSONObject>();
			
			for( int i = 0; i < array.length(); i++){
				JSONObject tmp = array.getJSONObject( i );
				id_tem.add( tmp.getString("id") );
				citys_tem.add( tmp.getString("nombre") );
				citys.add(tmp);
			}
			log("Array", citys.toString());
			choose = (Spinner) mthis.findViewById(R.id.spinner1);
			
			ArrayAdapter<String> spinnerAdapter = getSpinnerStyle( citys_tem );			
			spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			choose.setAdapter(spinnerAdapter);	
			if( pref.getString("id", "").equals("") ){
				Editor e = pref.edit();
				e.putString("id", "1");
				e.commit();
				final Spinner choose_ciudad = (Spinner) mthis.findViewById(R.id.spinner1);
				JSONObject jsonCiudad = citys.get( choose_ciudad.getSelectedItemPosition() );
				Log.i("JSONObjet", jsonCiudad.toString());
				try {
					log("JSON", jsonCiudad.getString("id") );
					saveCiudad( jsonCiudad.getString("id") );
					ciudad = jsonCiudad.getString("nombre");
				} catch (JSONException er) {
					er.printStackTrace();
				}
				log("chose", "setciudad");
				setCiudad();
				choose.performClick();
				_menulayout = false;
				menu( (LinearLayout)mthis.findViewById(R.id.menulayout), (ImageView)findViewById(R.id.buttonmenu) );
			}else{
				for( int i = 0; i < id_tem.size() ; i++){
					if( id_tem.get(i).equals(pref.getString("id", "1")) ){
						choose.setSelection( i );
						ciudad = choose.getSelectedItem() + "";
						break;
					}
				}				
			}
		}catch( Exception e ){
			log("Crash", e.toString() );
		}
	}	
	
	/*
	 * ArrayAdapter
	 * method for set properties a spinner
	 * @receive 
	 *  ArrayList<String> list
	 * @return ArrayAdapter<String>
	 */
	public ArrayAdapter<String> getSpinnerStyle(ArrayList<String> list) {
		if ( list == null ){
			list = new ArrayList<String>();
		}
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list) {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				((TextView) v).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView) v).setTextSize(20);	
				loadfontCity( (TextView) v);
				return v;
			}
			
			@Override
			public View getDropDownView(int position, View convertView,	ViewGroup parent) {
				View v = super.getDropDownView(position, convertView, parent);
				v.setBackgroundColor(Color.parseColor("#333333"));
				((TextView) v).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView) v).setTextSize(22);
				loadfontCity( (TextView) v);
				return v;
			}
		};
		return dataAdapter;
	}	
	
	/*
	 * setCiudad
	 * method for set a city
	 * @receive  
	 * @return void
	 */
	public void setCiudad(){
		showToast("Debes de seleccionar una zona en el menú.");	
	}
	
	/*
	 * saveCiudad
	 * method for save the city selected
	 * @receive 
	 *  String id
	 * @return void
	 */
	public void saveCiudad( String id ){
		SharedPreferences pref = mthis.getPreferences(MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString("id", id);
		editor.commit();
		log("pref.getciudad", pref.getString("id", ""));
	}

	/*
	 * saveStatus
	 * method for save a status actual
	 * @receive 
	 *  String status
	 * @return void
	 */
	public void saveStatus( String ciudad ){
		SharedPreferences pref = mthis.getPreferences(MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString("ciudad", ciudad);
		editor.putString("peticionId", mthis.peticionId);
		editor.putString("status", status);
		editor.commit();
		log("pref.getCiudad", pref.getString("ciudad", ""));
		log("pref.getPeticion", pref.getString("peticionId", ""));
	}
	
	/*
	 * manage_update
	 * method for get status the petition
	 * @receive 
	 *  JSONObject rs
	 * @return void
	 */
	public void manage_update(JSONObject rs){
		if( status.equals("Cancelada") ){
			Log.i("Aqui queda", "Si aqui quedo");
			return;
		}
		try{
			SharedPreferences pref 	= mthis.getPreferences(MODE_PRIVATE);
			String success 			= rs.getString("success");
			if(success.equals("true")){
				log("update", "success: " + success);
				if( !_final ){
					peticionId 	= rs.getString("id");
				}
				status = rs.getString("status");
				Log.i("Status update", status);
				if( status.equals("Nueva") ){
					if( !_rechazada ){
						setAssigning();
					}else{
						EnProceso();
					}
				}else if( status.equals("Asignada") ){
					time = Integer.parseInt(rs.getString("tiempo"));
					taxy = rs.getString("taxi");
					mthis.setWaittingTaxi();
					threadTime = 30000;
				}else if( status.equals("En Progreso") ){
					mthis.EnProceso();
				}else if( status.equals("Cancelada") ){	
					if( !pref.getBoolean("_stop", true) ){
						mthis.setRetryTaxi();
					}
				}else if( status.equals("Atendida")){
					if( !pref.getBoolean("_stop", true) ){
						mthis.startFeedBack();
						Editor editor = pref.edit();
						editor.putBoolean("_stop", true);
						editor.putString("peticionId", "0");
						editor.commit();
					}			
					return;
				}else if(  status.equals("No Atendida") ){
					return;
				}
				saveStatus( ciudad );
			}else{
				showToast("No se pudo Crear la Peticion");
			}
		}catch(Exception e){
			e.printStackTrace();
			showToast("Error Al Crear Peticion");
		}
	}
	
	/* log
	 * method for print in the log of the system
	 * @receive 
	 * 	String tag
	 * 	String msg
	 * @return void
	 * */
	public void log(String tag, String msj) {
		if (debug)
			Log.d(tag, msj);		
	}
	
	public void getLocalization(){
		PostTask task 				= new PostTask();
		task.applicationContext 	= mthis;
		task.post_case 				= 8;
		task.execute();
	}
	
	/*
	 * showToast
	 * method for show a toast notification
	 * @receive  
	 *  String message
	 * @return void
	 */
	public void showToast(String message){		
		Toast.makeText(mthis, message, Toast.LENGTH_LONG).show();
	}
	//TODO:
	@Override
	protected  void onActivityResult(int requestCode, int resultCode, Intent data){
		log("result","request:"+requestCode+"  result:"+resultCode+"  intent:"+data);
		findViewById(R.id.calltaxi).setEnabled( true );
		if(requestCode == 5){
			if( resultCode == RESULT_OK ){
				mthis.location=data.getExtras().getString("location");
				mthis.comment=data.getExtras().getString("comment");
				MyTask call = new MyTask( mthis );
				call.execute = 2;
				call.execute();
//				if( getConexion( base_url ) ){
//					call_taxy();
//				}
			}
		}
	}
	
	/*
	 * setWaittingTaxi
	 * method for waiting a Taxi arrive
	 * @receive  
	 * @return void
	 */
	public void setWaittingTaxi(){
		if( _setCronometro ){
			mthis.setContentView(R.layout.waiting_taxy_mobile);
			//createNotification( "¡TaxiPlis!", "Se ha asignado un taxi a tu petición.");
			_setCronometro = false;
			
			final ImageButton cancel 		= (ImageButton)mthis.findViewById(R.id.cancelbutton);
			final LinearLayout nollego 		= (LinearLayout) mthis.findViewById(R.id.nollego);		
			final LinearLayout yallego 		= (LinearLayout) mthis.findViewById(R.id.yallego);
			final ImageView btn_nollego 	= (ImageView) mthis.findViewById(R.id.btn_nollego);
			final ImageView btn_llego 		= (ImageView) mthis.findViewById(R.id.btn_yallego);
			
			cancel.setOnTouchListener(new OnTouchListener(){
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					if(arg1.getAction()==MotionEvent.ACTION_DOWN){
						cancel.setImageResource(R.drawable.btn_stop_pressed);
						nollego.setEnabled(false);
						yallego.setEnabled(false);
					}else if(arg1.getAction()==MotionEvent.ACTION_UP){
						cancel.setImageResource(R.drawable.btn_stop_normal);
						cancel.setEnabled( false );
						MyTask cancel = new MyTask( mthis );
						cancel.execute = 3;
						cancel.execute();
//						if( getConexion( base_url ) ){
//							mthis.call_cancel();
//							Log.i("Status", status);
//							_setCronometro = false;
//							contador = -1;					
//						}else{
//							showAlertDialog("Error de conexión.", "Intenta mas tarde.");
//						}			
						nollego.setEnabled(true);
						yallego.setEnabled(true);
						return true;
					}
					return false;
				}			
			});
			
			nollego.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if( event.getAction() == MotionEvent.ACTION_DOWN ){
						btn_nollego.setImageResource( R.drawable.btn_no_pressed );
						cancel.setEnabled(false);
						yallego.setEnabled(false);
						return true;
					}else if( event.getAction() == MotionEvent.ACTION_UP ){
						btn_nollego.setImageResource( R.drawable.btn_no_normal );
						nollego.setEnabled( false );
						MyTask nollegoTask = new MyTask( mthis );
						nollegoTask.execute = 4;
						nollegoTask.execute();
//						if( getConexion( base_url ) ){
//							mthis.call_noAtend();
//							contador = -1;													
//						}else{
//							showAlertDialog("Error de conexión.", "Intenta mas tarde.");
//						}
						cancel.setEnabled(true);
						yallego.setEnabled(true);
						return true;
					}
					return false;
				}
			});

			yallego.setOnTouchListener( new OnTouchListener() {	
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if( event.getAction() == MotionEvent.ACTION_DOWN ){
						btn_llego.setImageResource( R.drawable.btn_yes_pressed );
						cancel.setEnabled(false);
						nollego.setEnabled(false);
						return true;
					}else if( event.getAction() == MotionEvent.ACTION_UP ){
						yallego.setEnabled( false );
						btn_llego.setImageResource( R.drawable.btn_yes_normal );
						MyTask yallegoTask = new MyTask( mthis );
						yallegoTask.execute = 5;
						yallegoTask.execute();
//						Log.i("Coneccion", getConexion( base_url ) + "." );
//						if( getConexion( base_url ) ){
//							mthis.call_atendido();
//							contador = -1;														
//						}else{
//							showAlertDialog("Error de conexión.", "Intenta mas tarde.");
//						}
						cancel.setEnabled(true);
						nollego.setEnabled(true);
						return true;
					}
					return true;
				}
			});
			
			loadfont( (TextView) mthis.findViewById(R.id.listo) );
			loadfont( (TextView) mthis.findViewById(R.id.esperar) );
			loadfont( (TextView) mthis.findViewById(R.id.tiempo) );
			TextView taxi = (TextView) mthis.findViewById(R.id.numero_taxi);
			taxi.setText( "#" + taxy );
			loadfont( taxi );
			loadfont( (TextView) mthis.findViewById(R.id.llegara) );
			
			setCronometro( time, (TextView) mthis.findViewById(R.id.tiempo) );
		}		
	}
	
	/*
	 * EnProceso
	 * method for inform that the reques was attend
	 * @receive  
	 * @return void
	 */
	public void EnProceso(){
		TextView buscando = (TextView) mthis.findViewById(R.id.buscando);
		buscando.setText("Su solicitud está siendo atendida, por favor espere a que se le asigne un taxi.");
		buscando.setTextSize(21);
		_rechazada = true;
	}
	
	/*
	 * setRetryTaxi
	 * method for inform that no a taxi and retry a peticion
	 * @receive  
	 * @return void
	 */
	public void setRetryTaxi(){
		_menulayout = false;
		mthis.setContentView(R.layout.activity_retry);
		_setCronometro = true;
		TextView dis 				= (TextView) mthis.findViewById(R.id.disculpa);
		TextView no 				= (TextView) mthis.findViewById(R.id.no);
		TextView disp 				= (TextView) mthis.findViewById(R.id.disponible);
		TextView pued 				= (TextView) mthis.findViewById(R.id.puedes);
		TextView cancel 			= (TextView) mthis.findViewById(R.id.cancelar);		
		final LinearLayout lCancel 	= (LinearLayout) mthis.findViewById(R.id.layoutcancel);
		final LinearLayout lRetry 	= (LinearLayout) mthis.findViewById(R.id.layoutretry);
		loadfont( dis );
		loadfont( no );
		loadfont( disp );
		loadfont( pued );
		loadfont( cancel );
		final ImageView help = (ImageView) mthis.findViewById(R.id.image_help);
		
		lCancel.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ImageView btn_cancel = (ImageView) mthis.findViewById(R.id.btn_cancel);
				if( event.getAction() == MotionEvent.ACTION_DOWN ){
					btn_cancel.setImageResource(R.drawable.btn_stop_mini_pressed);
					lRetry.setEnabled(false);
					help.setEnabled(false);
					return true;
				}else if( event.getAction() == MotionEvent.ACTION_UP ){
					btn_cancel.setImageResource(R.drawable.btn_stop_mini_normal);
					SharedPreferences pref = mthis.getPreferences(MODE_PRIVATE);
					Editor editor = pref.edit();
					editor.putString("peticionId", "0");
					editor.commit();
					lRetry.setEnabled(true);
					help.setEnabled(true);					
					showCallTaxy();
					return true;
				}
				return true;
			}
		});
		
		lRetry.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ImageView btn_retry = (ImageView) mthis.findViewById(R.id.btn_retry);
				if( event.getAction() == MotionEvent.ACTION_DOWN ){
					btn_retry.setImageResource(R.drawable.btn_pedir_mini_pressed);
					help.setEnabled(false);
					lCancel.setEnabled(false);
					return true;
				}else if( event.getAction() == MotionEvent.ACTION_UP ){
					btn_retry.setImageResource(R.drawable.btn_pedir_mini_normal);
					mthis.setContentView(R.layout.activity_taxy_mobile);
					help.setEnabled(true);
					lCancel.setEnabled(true);
					call_taxy();
					return true;
				}
				return true;
			}
		});
		
		help.setOnTouchListener( new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if( event.getAction() == MotionEvent.ACTION_DOWN ){
					lCancel.setEnabled(false);
					lRetry.setEnabled(false);
					return true;
				}else if( event.getAction() == MotionEvent.ACTION_UP ){
					help.setVisibility(View.VISIBLE);					
					startHelp( (RelativeLayout) findViewById( R.id.relative_help_retry) );
					lCancel.setEnabled(true);
					lRetry.setEnabled(true);
					return true;
				}
				return false;
			}
		});
	}

	/*
	 * setCronometro
	 * method for count the minutes of arrival
	 * @receive 
	 *  int tiempo
	 *  TextView textTime 
	 * @return void
	 */
	private void setCronometro( final int tiempo, final TextView textTime ){
		cronometro 	= new Thread() {
			public void run() {
				contador = tiempo;
				Log.i("Status", status);
				while ( status.equals("Asignada") ){
					while( contador >= 0 ){
						try{
							runOnUiThread(new Runnable() {
								public void run() {
									try{
										if( status.equals("Asignada") ){
											if( contador == 1 ){
												textTime.setText( contador + " minuto." );
											}else{
												textTime.setText( contador + " minutos." );
												if( contador == 0 ){
													log( "status", "Status: " + status );
													textTime.setText( contador + " minutos." );
													createNotification( "¡TaxiPlis!", "Se acabó el tiempo de espera del taxi.");
													mthis.findViewById(R.id.cancelbutton).setVisibility(View.GONE);
												}
											}
										}
										contador--;
									}catch(Exception e){}								
								}
							});
							Thread.sleep(6000);
						}catch(Exception e){}
					}
				}
			}
		};
		cronometro.start();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		final LinearLayout layoutmenu=(LinearLayout)mthis.findViewById(R.id.menulayout); 
		switch( keyCode ){
			////KeyBack
			case 4:
				if( _menulayout ){
					_menulayout = false;
					Animation inmenu = AnimationUtils.loadAnimation(mthis, R.layout.animation_menu_out);
					layoutmenu.startAnimation(inmenu);
					layoutmenu.setVisibility(View.GONE);
					mthis.findViewById(R.id.helptem).setVisibility(View.GONE);
				}else {
					mthis.moveTaskToBack(true);
				}
				break;
			////Ajustes
			case 82:
				if( _menu ){
					menu( layoutmenu, (ImageView)findViewById(R.id.buttonmenu) );
				}
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
	public void menu(final LinearLayout layoutmenu, final ImageView buttonmenu){
		final FrameLayout helptem = (FrameLayout) mthis.findViewById(R.id.helptem);
		if( _menulayout ){
			_menulayout = false;
			Animation inmenu = AnimationUtils.loadAnimation(mthis, R.layout.animation_menu_out);
			inmenu.setAnimationListener(new AnimationListener() {				
				@Override
				public void onAnimationStart(Animation animation) {
					buttonmenu.setEnabled( false );
					helptem.setVisibility( View.GONE );
				}
				@Override
				public void onAnimationRepeat(Animation animation) {}				
				@Override
				public void onAnimationEnd(Animation animation) {
					buttonmenu.setEnabled( true );
				}
			});
			layoutmenu.startAnimation(inmenu);
			
			layoutmenu.setVisibility(View.GONE);
		}else{
			_menulayout = true;			
			layoutmenu.setVisibility(View.VISIBLE);
			Animation inmenu = AnimationUtils.loadAnimation(mthis, R.layout.animation_menu_in);
			inmenu.setAnimationListener(new AnimationListener() {				
				@Override
				public void onAnimationStart(Animation animation) {
					buttonmenu.setEnabled( false );
				}				
				@Override
				public void onAnimationRepeat(Animation animation) {}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					buttonmenu.setEnabled( true );
					helptem.setVisibility( View.VISIBLE );
					
					LinearLayout delete = (LinearLayout) layoutmenu.findViewById(R.id.linear_deletedata);
					delete.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							clearApplicationData();				
						}
					});
					
					final Spinner choose_ciudad = (Spinner) mthis.findViewById(R.id.spinner1);		
					
					
					choose_ciudad.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {		
							Log.i("Click", "Chosseciudad");
							menu(layoutmenu, buttonmenu);
							mthis.findViewById(R.id.helptem).setVisibility(View.GONE);
							JSONObject jsonCiudad = citys.get( choose_ciudad.getSelectedItemPosition() );
							Log.i("JSONObjet", jsonCiudad.toString());
							try {
								log("JSON", jsonCiudad.getString("id") );
								saveCiudad( jsonCiudad.getString("id") );
								ciudad = jsonCiudad.getString("nombre");
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});
					
					
					LinearLayout help = (LinearLayout) layoutmenu.findViewById(R.id.linear_help);
					help.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							startHelp( (RelativeLayout) mthis.findViewById( R.id.relative_help) );
							Animation inmenu = AnimationUtils.loadAnimation(mthis, R.layout.animation_menu_out);
							layoutmenu.startAnimation(inmenu);
							layoutmenu.setVisibility(View.GONE);
							helptem.setVisibility( View.GONE );
							_menulayout = false;
						}
					});	
					
					helptem.setOnClickListener(new OnClickListener() {				
						@Override
						public void onClick(View v) {
							helptem.setVisibility( View.GONE );
							menu(layoutmenu, buttonmenu);
						}
					});
					
				}
			});
			layoutmenu.startAnimation(inmenu);
			
			
		}
		
		loadfontCity( (TextView) mthis.findViewById(R.id.ciudadText) );
		loadfontCity( (TextView) mthis.findViewById(R.id.ayudatext) );
		loadfontCity( (TextView) mthis.findViewById(R.id.textdata) );
		
	}
	
	/*
	 * getPendingIntent
	 * method for get the pending intent
	 * @receive 
	 * @return void
	 */
	private PendingIntent getPendingIntent() {
        Intent i = mthis.getIntent();
        return PendingIntent.getActivity(mthis, 0, i, 0);
    }
	
	/*
	 * createNotification
	 * method for create a notification
	 * @receive 
	 *  String title
	 *  String msj
	 * @return void
	 */
	public void createNotification(String title, String msj){
		NotificationCompat.Builder mBuilder =
			 new NotificationCompat.Builder(mthis)
			       .setSmallIcon(R.drawable.ic_launcher)
			       .setContentTitle(title)
			       .setContentIntent(getPendingIntent())
			       .setContentText(msj)			       
			       .setAutoCancel( true );

		mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	  	notificationManager.notify(2000, mBuilder.build());
	}
	
	/*
	 * startFeedBack
	 * method for open a feedback
	 * @receive  
	 * @return void
	 */
	public void startFeedBack(){
		Intent i = new Intent(mthis, FeedBack.class);
		mthis.startActivity(i);
		mthis.showCallTaxy();
	}
	
	/*
	 * loadfont
	 * method for load a font for aplication
	 * @receive 
	 *  TextView text
	 * @return void
	 */
	public void loadfont(TextView text){
		Typeface myFont = Typeface.createFromAsset(getAssets(),
			"font/myriadpro_boldit.otf");
		text.setTypeface(myFont);
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
	
	/*
	 * clearApplicationData
	 * method for clear a system data
	 * @receive  
	 * @return void
	 */
	public void clearApplicationData(){
		SharedPreferences pref = mthis.getPreferences(MODE_PRIVATE);
		Editor e = Splash.mthis.pref.edit();
		e.clear();
		e.commit();
		e = pref.edit();
		e.clear();
		e.commit();
		Log.i("Clear", pref.getString("peticionId", "0") + "");
		reiniciarAplicacion();
    }
	
	/*
	 * reiniciarAplicacion
	 * method for restart the aplication
	 * @receive  
	 * @return void
	 */
	public void reiniciarAplicacion(){
		Intent i = new Intent(mthis, Splash.class );
		mthis.finish();
		startActivity(i);
	}
	
	/*
	 * showAletDialog
	 * method for show the alert dialog
	 * @receive  
	 *  String tittle
	 *  String msg
	 * @return void
	 */
	public void showAlertDialog( String tittle, String msg){
		final AlertDialog alert = new AlertDialog.Builder(mthis).create();                 
		alert.setTitle( tittle );
		alert.setMessage( msg );
		alert.setButton("Aceptar", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
				dialog.cancel();
        	}
        });
		alert.show();
	}	
}