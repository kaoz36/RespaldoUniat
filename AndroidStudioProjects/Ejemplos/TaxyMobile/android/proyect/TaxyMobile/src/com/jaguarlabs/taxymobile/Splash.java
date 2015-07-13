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

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Window;

/**
 * Splash class 
 * first activity that show a splash
 ** */

public class Splash extends Activity {

	public static Splash mthis		= null;
	public static boolean debug		= true;
	public static boolean isReached	= true;
	public SharedPreferences pref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mthis 	= this;
		pref 	= mthis.getPreferences(MODE_PRIVATE);
		setContentView(R.layout.activity_splash);
		MyTask init = new MyTask(mthis);
		init.execute = 0;
		init.execute();
	}
	
	/*
	 * canReach
	 * method for know if internet
	 * @receive  
	 *  String address
	 * @return boolean
	 */
	public static boolean canReach(final String address) {
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
					isReached = true;
					if (mConn != null) {
						mConn.disconnect();
						mConn = null;
					}
					if (aURL != null)
						aURL = null;
				} catch (Exception e) {
					Log.w("application", "Exception:" + e);
					isReached = false;
					mConn = null;
					aURL = null;
				}
			}
		}.start();
		while ((System.currentTimeMillis() - startTime < 5000));
		return isReached;
	}
	
	/*
	 * start
	 * method for send the information if the user exists
	 * @receive  
	 * @return void
	 */
	public void start() {
		if( isReached ){
			Intent ac;
			if ( isValidUser() ) {
				pref = mthis.getPreferences(MODE_PRIVATE);
				ac = new Intent(mthis,TaxyMobile.class);
				ac.putExtra("name", pref.getString("name", ""));
				ac.putExtra("number", pref.getString("number", ""));
				ac.putExtra("id", pref.getString("id", ""));
				startActivity(ac);
				finish();
			} else {
				ac = new Intent(mthis, Capture.class);
				startActivity(ac);
			}
			
		} else {
			showAlert();
		}
	}
	
	/*
	 * showAlert
	 * method for show the alert error conection
	 * @receive  
	 * @return void
	 */
	private void showAlert() {
		AlertDialog.Builder internetq = new AlertDialog.Builder(this);
		internetq.setMessage("Error al conectar a internet.");
		internetq.setCancelable(false);
		internetq.setPositiveButton("Salir",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						System.exit(0);						
					}
				});

		internetq.setNegativeButton("Reintentar",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {						
						Intent i = getIntent();
						dialog.cancel();
						finish();
						startActivity(i);
					}
				});
		AlertDialog alertDialog = internetq.create();
		alertDialog.show();
	}

	/*
	 * isValidUser
	 * method for validate if user exists
	 * @receive  
	 * @return boolean
	 */
	private boolean isValidUser(){
		pref = mthis.getPreferences(MODE_PRIVATE);
		String userId=pref.getString("id", "0");		
		if (userId.equals("0")) {
			return false;
		} else {
			return true;
		}
	}
	
	/* capturaDatos
	 * method for save data in server
	 * @receive 
	 * 	String completeName
	 * 	String phoneNumber
	 *  String id
	 * @return void
	 * */
	public void capturaDatos(String completeName, String phoneNumber, String id) {
		pref = mthis.getPreferences(MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString("id", ""+id);
		editor.putString("name", completeName);
		editor.putString("number", phoneNumber);
		editor.putBoolean("saved", true);
		editor.commit();
		finish();
	}
	
	/* log
	 * method for print in the log of the system
	 * @receive 
	 * 	String tag
	 * 	String msg
	 * @return void
	 * */
	public void log(String tag, String msj) {
		if (debug){
			Log.d(tag, msj);
		}			
	}
}
