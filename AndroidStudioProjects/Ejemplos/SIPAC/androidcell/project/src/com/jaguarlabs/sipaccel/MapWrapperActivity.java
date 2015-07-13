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

package com.jaguarlabs.sipaccel;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jaguarlabs.sipaccel.dialog.SaveMapLocationDialog;
import com.jaguarlabs.sipaccel.model.DataModel;
import com.jaguarlabs.sipaccel.net.RPCHandler;
import com.jaguarlabs.sipaccel.util.CustomInfoWindow;
import com.jaguarlabs.sipaccel.util.ExtendedFragmentActivity;
import com.jaguarlabs.sipaccel.util.ExtendedSupporMapFragment;
import com.jaguarlabs.sipaccel.util.ICallback;
import com.jaguarlabs.sipaccel.util.IJSONResponseHandler;
import com.jaguarlabs.sipaccel.util.ISaveLocationHandler;
import com.jaguarlabs.sipaccel.util.ISupportMapHolder;
import com.jaguarlabs.sipaccel.util.Task;
import com.jaguarlabs.sipaccel.vo.MapLocationVO;


public class MapWrapperActivity extends ExtendedFragmentActivity implements LocationListener, 
OnMapLongClickListener, OnMarkerClickListener, ISaveLocationHandler, IJSONResponseHandler, 
ISupportMapHolder, ICallback {
	private GoogleMap googleMap;
	private ProgressDialog dialog;
	private List<MapLocationVO> locations;
	private ArrayList<Marker> arrayM;
	private ImageButton listPromotoria;
	
	boolean markerClicked;
	PolylineOptions rectOptions;
	Polyline polyline;
	
	@Override
	protected void init() {
		super.init();
		
		ImageButton back;
		
		
		setContentView( R.layout.activity_map_wrapper );
		back 			= ( ImageButton ) findViewById(R.id.backActivity);
		listPromotoria	= ( ImageButton ) findViewById(R.id.listPromo);
		listPromotoria.setOnClickListener(new  OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Spinner list = (Spinner) findViewById(R.id.spinner1);
				ArrayList<String> listPromo = new ArrayList<String>();
				for( int i = 0; i < locations.size(); i++ ){
					listPromo.add( locations.get(i).getNombre() );
				}
				
				list.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						if( !arrayM.get( arg0.getSelectedItemPosition() ).isInfoWindowShown() )
							Log.i("Marcador", arrayM.get( arg0.getSelectedItemPosition() ) + "");
						
						LatLng latLng = new LatLng( locations.get( arg0.getSelectedItemPosition() ).getLatitude(),
								locations.get( arg0.getSelectedItemPosition() ).getAltitude() );
						
						arrayM.get( arg0.getSelectedItemPosition() ).showInfoWindow();
						
				        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
				        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));				        
				        
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {}
				});
				
				list.setAdapter( getSpinnerStyle( listPromo ) );
				list.performClick();
			}
		});
		
		back.setOnClickListener( back() );
		
		setupMap();
	}
	
	private OnClickListener back(){
		return new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
			}
		};
	}
	
	public ArrayAdapter<String> getSpinnerStyle( ArrayList<String> list){
		if( list == null ){
			list = new ArrayList<String>();
		}
		
		ArrayAdapter<String> dataAdapter =  new ArrayAdapter<String>( getApplicationContext(), 
				android.R.layout.simple_spinner_item, list){
			
			@Override
			public View getView(int position, View convertView,	ViewGroup parent) {				
				return super.getView(position, convertView, parent);
			}
			
			@Override
			public View getDropDownView(int position, View convertView,	ViewGroup parent) {
				View v = super.getDropDownView(position, convertView, parent);
				v.setPadding(0, 5, 0, 5);
				((TextView) v).setGravity( Gravity.CENTER );
				((TextView) v).setTextColor( new Color().parseColor( "#000000" ) );
				((TextView) v).setTextSize( 20 );				
				return v;
			}
		};
		
		
		return dataAdapter;
	}
	
	private void setupMap(){
	   int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable( getBaseContext() );
	   FragmentTransaction trans;
       if( status != ConnectionResult.SUCCESS ){
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        }else {         	
            ExtendedSupporMapFragment fm = new ExtendedSupporMapFragment();
            fm.setHolder( this );
            trans = getSupportFragmentManager().beginTransaction();
            trans.add(R.id.mapHolder, fm);
            trans.commit();
        }       
	}
	
	
	
	@Override
	public void onMapLoaded(SupportMapFragment fragment) {
		LayoutInflater inflater = getLayoutInflater();
		googleMap = fragment.getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setInfoWindowAdapter( new CustomInfoWindow( inflater ) );

        googleMap.setOnInfoWindowClickListener( new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker marker) {
				saveChekin();
			}
		});
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria 	= new Criteria();
        String provider 	= locationManager.getBestProvider(criteria, true);
        Location location	= locationManager.getLastKnownLocation(provider);
        if( location != null ){
            onLocationChanged( location );
        }        
        getLocations();
	}

	private void getLocations(){
		RPCHandler rpc;
		rpc = new RPCHandler(this);
		rpc.execute(RPCHandler.OPERATION_GET_ALL_LOCATIONS,
			    new BasicNameValuePair( "promotoria_id", DataModel.getInstance().getPromotoria().getPromot() ) );
	}
	
	

	@Override
	public void onLocationChanged(Location location) {
		double latitude = location.getLatitude();
        double longitude = location.getLongitude();
 
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
 	}

	@Override
	public void onProviderDisabled(String arg0) {
	}

	@Override
	public void onProviderEnabled(String arg0) {
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {		
	}

	@Override
	public void onMapLongClick(LatLng mapPosition) {
		markerClicked = false;
		SaveMapLocationDialog dialog = new SaveMapLocationDialog( this,
				new MapLocationVO(mapPosition.longitude,mapPosition.latitude),
				this);
		dialog.show();
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		  return false;
	}
	
	/* Save Location Handler */
	@Override
	public void saveLocation(MapLocationVO loc) {
		RPCHandler rpc;
		rpc = new RPCHandler( this );	
		if( loc.validFields() != false ){
			rpc.execute(RPCHandler.OPERATION_REGISTER_LOCATION,
					new BasicNameValuePair("promotoria_id", loc.getIdUbicacion()),
				    new BasicNameValuePair("nombre",loc.getNombre()),
				    new BasicNameValuePair("retenedor",""+loc.getRetenedor()),
				    new BasicNameValuePair("upago",""+loc.getuPago()),
				    new BasicNameValuePair("prospectos",""+loc.getProspectos()),
				    new BasicNameValuePair("altitude",""+loc.getAltitude()),
				    new BasicNameValuePair("latitude",""+loc.getLatitude()),
				    new BasicNameValuePair("direccion", loc.getDireccion()),
					new BasicNameValuePair("zona",loc.getZona()));
		}
	}

	@Override
	public void onResponse(JSONObject jsonResponse) throws Exception {
		JSONArray promotorias;
		arrayM = new ArrayList<Marker>();
		if(jsonResponse.has("error")){
			getMessageBuilder().setMessage(jsonResponse.get("error").toString());
			getMessageBuilder().create().show();
		}else{	
			if(jsonResponse.has("result")){
				locations = new ArrayList<MapLocationVO>();
				promotorias = jsonResponse.getJSONArray("result");
				for( int i =0; i<promotorias.length(); i++ ){
					
					locations.add(new MapLocationVO(
							promotorias.getJSONObject(i).getString("id_ubicacion"),
							promotorias.getJSONObject(i).getString("zona"),
							promotorias.getJSONObject(i).getString("nombre"), 
							promotorias.getJSONObject(i).getInt("retenedor"),
							promotorias.getJSONObject(i).getInt("upago"),
							promotorias.getJSONObject(i).getInt("prospectos"),
							promotorias.getJSONObject(i).getDouble("altitude"),
							promotorias.getJSONObject(i).getDouble("latitude"),
							promotorias.getJSONObject(i).getString("direccion") ));
				}
				googleMap.clear();
				for(MapLocationVO loc:locations){
					arrayM.add( googleMap.addMarker(new MarkerOptions()
											.position(new LatLng(loc.getLatitude(), loc.getAltitude()))
											.title(loc.getNombre()+" ("+loc.getProspectos()+" prospectos)")
											.snippet("Retenedor: "+loc.getRetenedor()+" U. de pago: "+loc.getuPago())
											.icon(BitmapDescriptorFactory.fromResource(R.drawable.img_pin) ) ) );
				}
				dialog.cancel();
				Task t = new Task( this );
				t.post_case = 0;
				t.execute();
			}
		} 	
	}
	
	@Override
	public void onRequest() {
		dialog = ProgressDialog.show(this, "Progreso", "Buscando Promotorias...", true);	
	}

	@Override
	public void onException(Exception e) {
		dialog.cancel();
		getMessageBuilder().setMessage("Error de conexion por favor intente de nuevo");
		listPromotoria.setVisibility( View.INVISIBLE );
		googleMap.setOnMapLongClickListener(null);
		callback();
		getMessageBuilder().create().show();
	}

	@Override
	public void time() {
		try {
			Thread.sleep(3500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void callback() {
		final ImageView img = (ImageView) findViewById(R.id.imageHelp );
		Animation anim = AnimationUtils.loadAnimation( getApplicationContext(), R.anim.help_out );
		anim.setAnimationListener( new AnimationListener() {			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				img.setVisibility( View.GONE );
			}
		});
		anim.setFillAfter( true );
		img.startAnimation(anim);
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

	@Override
	public void callback(LayoutInflater inflater, ViewGroup container) {		
	}
	
	private void saveChekin(){
		Location location = getLocation();
		if( location == null ){
			AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());                 
		    alert.setTitle("Error GPS");
		    Toast.makeText(mContext, "Por favor verifique que tenga el GPS activado.", Toast.LENGTH_LONG).show();
		    alert.create();
			return;
		}
		String promotoria_id = DataModel.getInstance().getPromotoria().getPromot();
		String agente_id = DataModel.getInstance().getAppUser().getAgente();		
		String latitude = location.getLatitude() + "";
		String longitude = location.getLongitude() + "";
		String ubicacion_id = DataModel.getInstance().getPromotoria().getIdPromotoria();
		
		if( !agente_id.equals("") && !promotoria_id.equals("") && !ubicacion_id.equals("")
									&& !latitude.equals("") && !longitude.equals("") ){
			saveLocation( promotoria_id, agente_id, latitude, longitude, ubicacion_id );
		}
	}
	
	boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;
	 
	Location location; // location
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private static final long MIN_TIME_BW_UPDATES = 1000 * 10 * 1; // 10 seconds
	private LocationManager locationManager;
	
	public Location getLocation() {
        try {
        	locationManager = (LocationManager)mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled) {
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
	
	private void saveLocation( String promotoria_id, String agente_id, String latitude, String longitude, String ubicacion_id ){

		(new RPCHandler( new IJSONResponseHandler() {
			private JSONObject response;
			@Override
			public void onResponse(JSONObject jsonResponse) throws Exception {
				response = jsonResponse;
				runOnUiThread( new Runnable() {
					
					@Override
					public void run() {
						try{
							getMessageBuilder().setMessage(response.getString("result"));
							getMessageBuilder().create().show();
						}catch (Exception e) {
						}
						
					}
				});
			}
			
			@Override
			public void onRequest() {
				Log.i("Save Checkin Location", "Guardando Localización");
			}
			
			@Override
			public void onException(Exception e) {
				Log.i("Save Checkin Location", "Error Guardando");
				runOnUiThread( new Runnable() {
					
					@Override
					public void run() {
						try{
							Toast.makeText(mContext, "Ubicación no disponible.", Toast.LENGTH_SHORT).show();
						}catch (Exception e) {
						}
						
					}
				});
			}
		})).execute( RPCHandler.OPERATION_SAVE_CHEKIN,
				new BasicNameValuePair("promotoria_id", promotoria_id ),
				new BasicNameValuePair("agente_id", agente_id ),				
				new BasicNameValuePair("latitude", latitude ),
				new BasicNameValuePair("longitude", longitude ),
				new BasicNameValuePair("ubicacion_id", ubicacion_id ) );
		
	}
	
}

