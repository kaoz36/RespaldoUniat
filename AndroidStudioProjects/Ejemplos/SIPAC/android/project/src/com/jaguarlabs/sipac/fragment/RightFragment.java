package com.jaguarlabs.sipac.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaguarlabs.sipac.model.DataModel;
import com.jaguarlabs.sipac.net.RPCHandler;
import com.jaguarlabs.sipac.util.ExtendedActivity;
import com.jaguarlabs.sipac.util.IJSONResponseHandler;
import com.jaguarlabs.sipac.util.Utils;

public class RightFragment extends Fragment {
	
	protected  View mainView;
	protected  IMenuFragment listener;
	protected ExtendedActivity caller;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		makeView(inflater,container,savedInstanceState);
		return mainView;
	}
	
	protected void makeView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof IMenuFragment) {
	        listener = (IMenuFragment) activity;
	        
	      } else {
	        throw new ClassCastException(activity.toString()
	            + " must implement IMenuFragment");
	   }
		
		 if(activity instanceof ExtendedActivity)
        {
        	caller = (ExtendedActivity) activity;
        } else {
	        throw new ClassCastException(activity.toString()
		            + " must extend ExtendedActivity");
		   }
	}
	
	protected void registerSearch(){
		if( Utils.getGPSStatus(caller) ){
			LocationManager locationManager = (LocationManager)caller.getSystemService(Context.LOCATION_SERVICE);
	        Criteria criteria = new Criteria();
	        String provider = locationManager.getBestProvider(criteria, true);
	        Location location = locationManager.getLastKnownLocation(provider);
	        if(location!=null){
	            saveSearch(location);
	        }
		}
	}
	
	private void saveSearch(Location pLocation){
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = formatter.format(new Date());
		
		(new RPCHandler(new IJSONResponseHandler() {
			
			@Override
			public void onResponse(JSONObject jsonResponse) throws Exception {
				// TODO Auto-generated method stub
				Log.i("Save Search Location", "Localizacion Guardada");
			}
			
			@Override
			public void onRequest() {
				// TODO Auto-generated method stub
				Log.i("Save Search Location", "Guardando Localización de busqueda");
			}
			
			@Override
			public void onException(Exception e) {
				// TODO Auto-generated method stub
				Log.i("Save Search Location", "Error Guardando Localización de busqueda");
			}
		})).execute(RPCHandler.OPERATION_SAVE_SEARCH,
					new BasicNameValuePair("id_usuario",DataModel.getInstance().getAppUser().getAgente()),
					new BasicNameValuePair("latitude",""+pLocation.getLatitude()),
					new BasicNameValuePair("longitude",""+pLocation.getLongitude()),
					new BasicNameValuePair("fecha",currentDate));
	}

}
