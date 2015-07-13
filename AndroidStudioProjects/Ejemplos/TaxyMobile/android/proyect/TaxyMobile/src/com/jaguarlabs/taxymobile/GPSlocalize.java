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

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
 

public class GPSlocalize extends Service implements LocationListener {
 
//	com.jaguarlabs.taxymobile.Location mContext;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
 
    Location location; // location
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 15 * 1; // 30 secconds
    private LocationManager locationManager;
 
    public GPSlocalize(Context context) {
//        this.mContext = (com.jaguarlabs.taxymobile.Location)context;
        getLocation();
    }
 
    public Location getLocation() {
        try {
//            locationManager = (LocationManager)mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled) {
                // //TODO: case no network and no gps
            	Log.i("resultGPS","no internet or gps enabled");
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if ( isGPSEnabled ) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }
 
    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSlocalize.this);
        }
    }
    
    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            return location.getLatitude();
        }
        return 0;
    }
 
    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            return location.getLongitude();
        }
        return 0;
    }
 
    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }
 
    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
//        alertDialog.setTitle("GPS des-habilitado.");
//        alertDialog.setMessage("Para localizar tu ubicación, por favor habilita el GPS en las Configuraciones del celular.");
//        alertDialog.setPositiveButton("Ir a Configuración", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog,int which) {
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////                mContext.startActivityForResult(intent, 11);
//            }
//        });
//        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//            	msjAddNota();
//            }
//        });
//        alertDialog.setCancelable( false );
//        alertDialog.show();
    }
 
    @Override
    public void onLocationChanged(Location location) {
    	this.location = location;
//    	mContext.updatePosition(location);
//    	com.jaguarlabs.taxymobile.Location.mthis.nota.cancel();
    }
 
    @Override
    public void onProviderDisabled(String provider) {
    }
 
    @Override
    public void onProviderEnabled(String provider) {
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
 
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void msjAddNota(){
//		final AlertDialog nota = new AlertDialog.Builder(mContext).create();
//		nota.setTitle("¿A dónde enviamos tu taxi?");
//		nota.setMessage("Por favor agrega la dirección en la sección de Notas.");
//		nota.setButton("Aceptar", new DialogInterface.OnClickListener() {			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				nota.cancel();
//			}
//		});
//		nota.setCancelable( false );
//		nota.show();
	}
    
}