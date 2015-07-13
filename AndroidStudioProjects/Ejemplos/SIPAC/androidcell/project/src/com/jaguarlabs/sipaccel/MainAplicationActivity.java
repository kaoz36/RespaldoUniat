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

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.jaguarlabs.sipaccel.db.CommonDAO;
import com.jaguarlabs.sipaccel.db.SipacOpenHelper;
import com.jaguarlabs.sipaccel.fragment.CotizadorFragment;
import com.jaguarlabs.sipaccel.fragment.IMenuFragment;
import com.jaguarlabs.sipaccel.fragment.ProfileFragment;
import com.jaguarlabs.sipaccel.fragment.ProspeccionFragment;
import com.jaguarlabs.sipaccel.fragment.SearchFragment;
import com.jaguarlabs.sipaccel.model.BackupModel;
import com.jaguarlabs.sipaccel.model.DataModel;
import com.jaguarlabs.sipaccel.net.RPCHandler;
import com.jaguarlabs.sipaccel.util.ExtendedFragmentActivity;
import com.jaguarlabs.sipaccel.util.IJSONResponseHandler;
import com.jaguarlabs.sipaccel.util.Utils;
import com.jaguarlabs.sipaccel.vo.CoberturaVO;
import com.jaguarlabs.sipaccel.vo.EdadVO;
import com.jaguarlabs.sipaccel.vo.PolizaVO;
import com.jaguarlabs.sipaccel.vo.ProfesionVO;
import com.jaguarlabs.sipaccel.vo.ProspeccionVO;
import com.jaguarlabs.sipaccel.vo.ServAffectVO;
import com.jaguarlabs.sipaccel.vo.ServicioGralVO;
import com.jaguarlabs.sipaccel.vo.ServicioVentaVO;

public class MainAplicationActivity extends ExtendedFragmentActivity implements
		IMenuFragment, OnGestureListener, LocationListener {

	private CommonDAO<PolizaVO> polizaDS;
	private CommonDAO<CoberturaVO> coberturaDS;
	private CommonDAO<ServAffectVO> servAfecDS;
	private CommonDAO<ServicioGralVO> servGralDS;
	private CommonDAO<ServicioVentaVO> servVentasDS;

	private FrameLayout layoutMenu = null;
	private FrameLayout layoutSlide = null;
	private int nuevoLeft = 0;
	private boolean menuOpen = false;
	private boolean animMenu = false;
	private int delta;
	private GestureDetector gDetector;

	AnimParams animParamsMenu = new AnimParams();
	private AnimationListener commonAnimationListener;

	public static class MainAppTouch implements OnTouchListener,
			OnClickListener {

		private IMenuFragment listener;

		public MainAppTouch(IMenuFragment listener) {
			this.listener = listener;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				return listener.onActionDown(event);
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				return listener.onActionMove(event);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				return listener.onActionUp(event);
			}
			return true;
		}

		@Override
		public void onClick(View v) {
			listener.onActionClick();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void init() {
		super.init();
		Bundle extras = getIntent().getExtras();
		setContentView(R.layout.activity_main_app);

		FragmentTransaction fragmentTransaction;

		polizaDS = new CommonDAO<PolizaVO>(this, PolizaVO.class,
				SipacOpenHelper.TABLE_POLIZA);
		coberturaDS = new CommonDAO<CoberturaVO>(this, CoberturaVO.class,
				SipacOpenHelper.TABLE_COBERTURA);
		servAfecDS = new CommonDAO<ServAffectVO>(this, ServAffectVO.class,
				SipacOpenHelper.TABLE_SERV_AFECT);
		servGralDS = new CommonDAO<ServicioGralVO>(this, ServicioGralVO.class,
				SipacOpenHelper.TABLE_SERV_GRAL);
		servVentasDS = new CommonDAO<ServicioVentaVO>(this,
				ServicioVentaVO.class, SipacOpenHelper.TABLE_SERV_VENTA);

		polizaDS.open();
		coberturaDS.open();
		servAfecDS.open();
		servGralDS.open();
		servVentasDS.open();

		layoutMenu = (FrameLayout) findViewById(R.id.menuLayout);
		layoutMenu.setVisibility(View.INVISIBLE);
		layoutSlide = (FrameLayout) findViewById(R.id.contentFragment);

		gDetector = new GestureDetector(this);
		commonAnimationListener = new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				animMenu = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				layoutSlide.layout(animParamsMenu.left, animParamsMenu.top,
						animParamsMenu.right, animParamsMenu.bottom);
				nuevoLeft = animParamsMenu.left;
				layoutSlide.clearAnimation();
				if (!menuOpen) {
					layoutMenu.setVisibility(View.INVISIBLE);
				}
				animMenu = false;
			}
		};

		if (extras != null) {
			fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			nuevoLeft = 0;
			switch (extras.getInt("section")) {
			case R.id.perfilButton:
				fragmentTransaction.add(R.id.contentFragment,
						new ProfileFragment()).commit();
				break;
			case R.id.consultaButton:
				if(!datosCartera())
				{
					getMessageBuilder().setMessage("Por favor descargue los datos necesarios para consultar la cartera (Presione boton de Actualización)").show();
					return;
				}
				fragmentTransaction.add(R.id.contentFragment,
						new SearchFragment()).commit();
				break;
			case R.id.cotizaButton:
				if(!datosCotizador())
				{
					getMessageBuilder().setMessage("Por favor descargue los datos necesarios para el cotizador (Presione boton de Actualización)").show();
					return;
				}
				fragmentTransaction.add(R.id.contentFragment,
						new CotizadorFragment()).commit();
				break;
			case R.id.prospectButton:
				if(!datosProspeccion())
				{
					getMessageBuilder().setMessage("Por favor descargue los datos necesarios para ingresar a prospección (Presione boton de Actualización)").show();
					return;
				}
				fragmentTransaction.add(R.id.contentFragment,
						new ProspeccionFragment()).commit();
				break;
			case R.id.actualizaButton:
				break;
			default:
				break;
			}
		}
		hilo();
	}
	
	private Boolean datosCartera()
	{
		
		Boolean hasData = true;
		CommonDAO<?> auxDAO = new CommonDAO<PolizaVO>(mContext, PolizaVO.class,SipacOpenHelper.TABLE_POLIZA);
		auxDAO.open();
		if(auxDAO.countItems() <= 0) hasData = false;
		auxDAO.close();
		return hasData;
	}
	
	private Boolean datosProspeccion()
	{
		Boolean hasData = true;
		CommonDAO<?> auxDAO = new CommonDAO<ProspeccionVO>(mContext, ProspeccionVO.class,SipacOpenHelper.TABLE_PROSPECCIONES);
		auxDAO.open();
		if(auxDAO.countItems() <= 0) hasData = false;
		auxDAO.close();
		return hasData;
	} 
	private Boolean datosCotizador()
	{
		Boolean hasData = true;
		CommonDAO<?> auxDAO = new CommonDAO<EdadVO>(mContext, EdadVO.class,SipacOpenHelper.TABLE_EDADES);
		auxDAO.open();
		if(auxDAO.countItems() <= 0) hasData = false;
		auxDAO.close();
		
		auxDAO = new CommonDAO<ProfesionVO>(mContext, ProfesionVO.class,SipacOpenHelper.TABLE_PROFESIONES);
		auxDAO.open();
		if(auxDAO.countItems() <= 0) hasData = false;
		auxDAO.close();
		
		return hasData;
	}

	@Override
	protected void onResume() {
		polizaDS.open();
		coberturaDS.open();
		servGralDS.open();
		servVentasDS.open();
		servAfecDS.open();
		stateThread = true;	
		if( t == null ){
			hilo();
		}
		t.start();
		super.onResume();
		
	}

	@Override
	protected void onPause() {
		polizaDS.close();
		coberturaDS.close();
		servGralDS.close();
		servVentasDS.close();
		servAfecDS.close();
		stateThread = false;
		t = null;
		super.onPause();
	}

	@Override
	public boolean onActionMove(MotionEvent ev) {
		if (layoutMenu.getVisibility() == View.INVISIBLE) {
			layoutMenu.setVisibility(View.VISIBLE);
		}
		float x = ev.getX() - delta;
		int offset = (int) (layoutSlide.getLeft() + x);
		if (offset < layoutMenu.getRight() && offset > layoutMenu.getLeft()) {
			nuevoLeft = offset;
			layoutSlide.layout(nuevoLeft, 0, layoutSlide.getMeasuredWidth()
					+ nuevoLeft, layoutSlide.getMeasuredHeight());
		}
		return gDetector.onTouchEvent(ev);
	}

	@Override
	public boolean onActionDown(MotionEvent ev) {
		delta = (int) ev.getX();
		return gDetector.onTouchEvent(ev);
	}

	@Override
	public boolean onActionUp(MotionEvent ev) {
		abrirCerrarMenu(menuOpen).setAnimationListener(commonAnimationListener);
		return gDetector.onTouchEvent(ev);
	}

	@Override
	public void onActionClick() {
		if (!animMenu) {
			abrirCerrarMenu(!menuOpen).setAnimationListener(
					commonAnimationListener);
		}
	}

	private Animation abrirCerrarMenu(boolean pOpen) {
		Animation anim;
		menuOpen = pOpen;

		int left = (int) layoutMenu.getMeasuredWidth();

		if (pOpen) {
			anim = new TranslateAnimation(0, left - nuevoLeft, 0, 0);
			anim.setDuration((left - nuevoLeft) * 500 / left);
			layoutMenu.setVisibility(View.VISIBLE);
			animParamsMenu.init(left, 0, left + layoutSlide.getMeasuredWidth(),
					layoutSlide.getMeasuredHeight());

		} else {
			anim = new TranslateAnimation(0, -nuevoLeft, 0, 0);
			animParamsMenu.init(0, 0, layoutSlide.getMeasuredWidth(),
					layoutSlide.getMeasuredHeight());
			anim.setDuration((nuevoLeft) * 500 / left);
		}

		anim.setFillAfter(true);
		layoutSlide.startAnimation(anim);
		return anim;
	}

	public void onMenuButtonClick(View pTarget) {

		abrirCerrarMenu(false).setAnimationListener((new AnimationListener() {
			private View target;

			private AnimationListener setView(View pView) {
				target = pView;
				return this;
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				layoutSlide.layout(animParamsMenu.left, animParamsMenu.top,
						animParamsMenu.right, animParamsMenu.bottom);
				nuevoLeft = animParamsMenu.left;
				layoutSlide.clearAnimation();
				if (!menuOpen) {
					layoutMenu.setVisibility(View.INVISIBLE);
				}
				FragmentTransaction fragmentTransaction;
				Fragment frag = null;
				fragmentTransaction = getSupportFragmentManager()
						.beginTransaction();
				fragmentTransaction.setCustomAnimations(R.anim.slide_out_left,
						R.anim.slide_in_left, R.anim.slide_out_left,
						R.anim.slide_in_left);

				switch (target.getId()) {
				case R.id.btn_perfil_slide:
					frag = new ProfileFragment();
					Log.i("Seleccionaste ", frag + ". ");
					break;
				case R.id.btn_consulta_cartera_slide:
					if(!datosCartera())
					{
						getMessageBuilder().setMessage("Por favor descargue los datos necesarios para consultar la cartera (Presione boton de Actualización)").show();
						return;
					}
					frag = new SearchFragment();
					Log.i("Seleccionaste ", frag + ". ");
					break;
				case R.id.btn_cotizacion_slide:
					if(!datosCotizador())
					{
						getMessageBuilder().setMessage("Por favor descargue los datos necesarios para el cotizador (Presione boton de Actualización)").show();
						return;
					}
					frag = new CotizadorFragment();
					Log.i("Seleccionaste ", frag + ". ");
					break;
				case R.id.btn_prospeccion_slide:
					if(!datosProspeccion())
					{
						getMessageBuilder().setMessage("Por favor descargue los datos necesarios para ingresar a prospección (Presione boton de Actualización)").show();
						return;
					}
					frag = new ProspeccionFragment();
					Log.i("Seleccionaste ", frag + ". ");
					break;
				case R.id.btn_geolocalizacion_slide:
					Intent i = new Intent(getApplicationContext(),
							MapWrapperActivity.class);
					startActivity(i);
					overridePendingTransition(R.anim.slide_out_left,
							R.anim.slide_in_left);
					frag = null;
					Log.i("Seleccionaste ", frag + ". ");
					break;
				case R.id.btn_salir_slide:
					finish();
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_right);
				default:
					break;
				}
				if (frag != null) {
					fragmentTransaction.replace(R.id.contentFragment, frag)
							.commit();
				}
			}
		}).setView(pTarget));
	}

	static class AnimParams {
		int left, right, top, bottom;

		void init(int left, int top, int right, int bottom) {
			this.left = left;
			this.top = top;
			this.right = right;
			this.bottom = bottom;
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent start, MotionEvent finish,
			float velocityX, float velocityY) {
		if (start.getRawX() < finish.getRawX()) {
			abrirCerrarMenu(true).setAnimationListener(commonAnimationListener);
		} else {
			abrirCerrarMenu(false)
					.setAnimationListener(commonAnimationListener);
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gDetector.onTouchEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if( keyCode == 82 || (android.os.Build.VERSION.SDK_INT < 14) ){
			if (!animMenu) {
				abrirCerrarMenu(!menuOpen).setAnimationListener(
						commonAnimationListener);
			}
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		if (!animMenu) {
			abrirCerrarMenu(!menuOpen).setAnimationListener(
					commonAnimationListener);
		}
	}
	
	boolean _inicia = true;
	Button tem;
	
	public void onClickTab( View pView ){
		ViewFlipper vf = (ViewFlipper) findViewById( R.id.pager_title_strip_flip );
		vf.setInAnimation(this, R.anim.viewflipper_in_right);
        vf.setOutAnimation(this, R.anim.viewflipper_out_left);
		if( _inicia ){
			tem = (Button) findViewById( R.id.titularButton );
			_inicia = false;
		}
		tem.setBackgroundResource( R.drawable.tabskin );
		tem = (Button) findViewById( pView.getId() );
		( (Button) findViewById( pView.getId() ) ).setBackgroundResource( R.drawable.tabskin_press );
		switch ( pView.getId() ) {
			case R.id.titularButton:
				vf.setDisplayedChild( 0 );
				break;
			case R.id.coberturasButton:
				vf.setDisplayedChild( 1 );
				break;
			case R.id.conyugueButton:
				vf.setDisplayedChild( 2 );
				break;
			case R.id.hijosButton:
				vf.setDisplayedChild( 3 );
				break;
			default:
				break;
		}
	}
	
	public void backupData(View pTarget){		
		BackupModel.getInstance().setContextActivity(this);		
		if(!wifiOn)
		{
			getMessageBuilder().setMessage("Es necesario tener una conexion WIFI Activa para reaizar este proceso");
	        getMessageBuilder().create().show();
	        return;
		}		
		BackupModel.getInstance().startBackup();
	}
	
	private int counter = 1;
	private boolean stateThread = true;
	private Thread t;
	
	public void hilo(){
		t = new Thread(){
			public void run(){				
				while( stateThread ){
					try{						
						Thread.sleep(1000);
						if( counter++ == 1 ){
							
							runOnUiThread( new Runnable() {								
								@Override
								public void run() {
									callback();
								}
							});
						}
						if(counter >= 600)counter = 1;
					}catch (Exception e) {
						Log.e("Error", "Error");
					}
				}
			}
		};
	}
	
	//TODO: Tengo error aqui. 
	private void callback(){
		if( Utils.getGPSStatus( this ) ){
			Location location = getLocation();
			if(location != null)
			{
				String agente_id = DataModel.getInstance().getAppUser().getAgente();
				String promotoria_id = DataModel.getInstance().getPromotoria().getPromot();
				String latitude = location.getLatitude() + ""; 
				String longitude = location.getLongitude() + "";
				if( !agente_id.equals("") && !promotoria_id.equals("") 
											&& !latitude.equals("") && !longitude.equals("") ){
					saveLocation( agente_id, promotoria_id, latitude, longitude );
				}
			}
		}
		Toast.makeText(mContext,
				"Ubicación no disponible",
				Toast.LENGTH_SHORT).show();
	}
	
	boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;
	 
	
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private static final long MIN_TIME_BW_UPDATES = 1000 * 10 * 1; // 10 secconds
	private LocationManager locationManager;
	
	public Location getLocation() {
		Location location = null; 
        try {
        	locationManager = (LocationManager)mContext.getSystemService(LOCATION_SERVICE);
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
	
	
	
	private void saveLocation( String agente_id, String promotoria_id, String latitude, String longitude ){

		(new RPCHandler( new IJSONResponseHandler() {
			private JSONObject response;
			@Override
			public void onResponse(JSONObject jsonResponse) throws Exception {
				response = jsonResponse;
				runOnUiThread( new Runnable() {
					
					@Override
					public void run() {
						try{
							Toast.makeText(mContext, response.getString("result") + "", Toast.LENGTH_SHORT).show();
						}catch (Exception e) {
						}
						
					}
				});
			}
			
			@Override
			public void onRequest() {
				Log.i("Save Search Location", "Guardando Localización");
			}
			
			@Override
			public void onException(Exception e) {
				Log.i("Save Search Location", "Error Guardando");
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
		})).execute( RPCHandler.OPERATION_SAVE_LOCATION, 
				new BasicNameValuePair("agente_id", agente_id ),
				new BasicNameValuePair("promotoria_id", promotoria_id ),
				new BasicNameValuePair("latitude", latitude ),
				new BasicNameValuePair("longitude", longitude ) );
		
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub	
	}
	
	public void clickCheckIn( View pView ){
		Log.i("Hace", "Checkin");
	}

}
