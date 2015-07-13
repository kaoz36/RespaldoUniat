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
 * Last Revised By   : Raul Acevedo
 * Last Checked In   : $Date: $
 * Last Version      : $Revision:  $
 *
 * Original Author   : Julio Hernandez  julio.hernadez@jaguarlabs.com
 * Origin            : SEnE -- April 25 @ 11:00 (PST)
 * Notes             :
 *
 * *************************************************************************/

package com.uniat.eduscore.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;

import com.uniat.eduscore.interfaces.IContextGetter;
import com.uniat.eduscore.interfaces.IStateNotifier;
import com.university3dmx.eduscore.R;

public class ExtendedFragmentActivity extends FragmentActivity implements
		IStateNotifier,IContextGetter {

	protected Context mContext;
	private AlertDialog.Builder builder;

	protected Boolean wifiOn = false;

	private IntentFilter filter;
	private WifiDetector wifiDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		init();
	}

	public AlertDialog.Builder getMessageBuilder() {
		if (builder == null) {
			builder = new AlertDialog.Builder(this);
			builder.setCancelable(false);
			builder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
		}
		return builder;
	}

	protected void init() {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		filter = new IntentFilter();
		filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
		wifiDetector = new WifiDetector(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(wifiDetector);
	}

	@Override
	protected void onStart() {
		super.onStart();
		registerReceiver(wifiDetector, filter);
		initialWifiCheck();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	}

	private void initialWifiCheck() {
		try {
			ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			NetworkInfo mWifi = connManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			if (mWifi.isConnected()) {
				successHandler();
			} else {
				failureHandler();
			}
		} catch (Exception error) {
			Log.e("Activity", error.getMessage());
		}
	}

	@Override
	public void successHandler() {
		Log.i("Audio Activity", "Wifi On");
		wifiOn = true;
	}

	@Override
	public void failureHandler() {
		Log.i("Audio Activity", "Wifi Off");
		wifiOn = false;
	}

	public Context getMContext() {
		return mContext;
	}
	
	

}
