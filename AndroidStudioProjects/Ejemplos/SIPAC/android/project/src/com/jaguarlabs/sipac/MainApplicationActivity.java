package com.jaguarlabs.sipac;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.jaguarlabs.sipac.util.Utils;

import com.jaguarlabs.sipac.components.VerticalButton;
import com.jaguarlabs.sipac.db.CommonDAO;
import com.jaguarlabs.sipac.db.SipacOpenHelper;
import com.jaguarlabs.sipac.fragment.CotizacionFragment;
import com.jaguarlabs.sipac.fragment.IMenuFragment;
import com.jaguarlabs.sipac.fragment.ProfileFragment;
import com.jaguarlabs.sipac.fragment.ProspeccionFragment;
import com.jaguarlabs.sipac.fragment.SearchFragment;
import com.jaguarlabs.sipac.model.BackupModel;
import com.jaguarlabs.sipac.model.DataModel;
import com.jaguarlabs.sipac.net.RPCHandler;
import com.jaguarlabs.sipac.util.ExtendedActivity;
import com.jaguarlabs.sipac.util.IJSONResponseHandler;
import com.jaguarlabs.sipac.vo.EdadVO;
import com.jaguarlabs.sipac.vo.PolizaVO;
import com.jaguarlabs.sipac.vo.ProfesionVO;
import com.jaguarlabs.sipac.vo.ProspeccionVO;
import com.jaguarlabs.sipac.util.Utils;

public class MainApplicationActivity extends ExtendedActivity implements
		IMenuFragment, OnGestureListener, LocationListener {

	private FrameLayout layoutMenu = null;
	private FrameLayout layoutSlide = null;
	private List<Fragment> frags;
	private int delta;
	private int nuevoLeft = 0;
	AnimParams animParamsMenu = new AnimParams();
	private GestureDetector gDetector;
	private boolean menuOpen = false;
	private AnimationListener commonAnimationListener;
	
	

	public static class MainApplicationOnTouchListener implements
			OnTouchListener, OnClickListener {

		private IMenuFragment listener;

		public MainApplicationOnTouchListener(IMenuFragment pListener) {
			this.listener = pListener;
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

	@Override
	protected void init() {
		super.init();
		Bundle extras = getIntent().getExtras();
		setContentView(com.jaguarlabs.sipac.R.layout.activity_main_app);

		FragmentTransaction fragmentTransaction;
		frags = new ArrayList<Fragment>();
		frags.add(new ProfileFragment());
		frags.add(new SearchFragment());
		frags.add(new CotizacionFragment());
		frags.add(new ProspeccionFragment());

		layoutMenu = (FrameLayout) findViewById(R.id.menuLayout);
		layoutMenu.setVisibility(View.INVISIBLE);
		layoutSlide = (FrameLayout) findViewById(R.id.contentFragment);

		gDetector = new GestureDetector(this, this);
		commonAnimationListener = new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				layoutSlide.layout(animParamsMenu.left, animParamsMenu.top,
						animParamsMenu.right, animParamsMenu.bottom);
				nuevoLeft = animParamsMenu.left;
				layoutSlide.clearAnimation();
				if (!menuOpen) {
					layoutMenu.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(Animation animation) {

			}
		};

		if (extras != null) {
			nuevoLeft = 0;
			switch (extras.getInt("section")) {
			case R.id.perfilButton:
				fragmentTransaction = getFragmentManager().beginTransaction();
				fragmentTransaction
						.add(com.jaguarlabs.sipac.R.id.contentFragment,
								frags.get(0)).commit();
				break;

			case R.id.consultaButton:
				if(!datosCartera())
				{
					getMessageBuilder().setMessage("Por favor descargue los datos necesarios para consultar la cartera (Presione boton de Actualización)").show();
					return;
				}
				fragmentTransaction = getFragmentManager().beginTransaction();
				fragmentTransaction
						.add(com.jaguarlabs.sipac.R.id.contentFragment,
								frags.get(1)).commit();
				break;

			case R.id.geolocalizacionButton:
				gotoMap();
				finish();
				return;

			case R.id.prospectButton:
				if(!datosProspeccion())
				{
					getMessageBuilder().setMessage("Por favor descargue los datos necesarios para ingresar a prospección (Presione boton de Actualización)").show();
					return;
				}
				fragmentTransaction = getFragmentManager().beginTransaction();
				fragmentTransaction
						.add(com.jaguarlabs.sipac.R.id.contentFragment,
								frags.get(3)).commit();
				break;

			case R.id.cotizaButton:
				if(!datosCotizador())
				{
					getMessageBuilder().setMessage("Por favor descargue los datos necesarios para el cotizador (Presione boton de Actualización)").show();
					return;
				}
				fragmentTransaction = getFragmentManager().beginTransaction();
				fragmentTransaction
						.add(com.jaguarlabs.sipac.R.id.contentFragment,
								frags.get(2)).commit();
				break;

			}

		}

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
	
	private void gotoMap() {
		Intent mapIntent = new Intent();
		mapIntent.setClass(this, MapWrapperActivity.class);
		startActivity(mapIntent);
		overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
	}

	@Override
	protected void onResume() {
		stateThread = true;	
		if( t == null ){
			hilo();
		}
		t.start();
		super.onResume();
	}

	@Override
	protected void onPause() {
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
		onBackPressed();
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
		if (pTarget.getId() == R.id.btn_actualiza_slide) {
			doUpdate();
			return;
		}
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

				switch (target.getId()) {
				case R.id.btn_perfil_slide:
					frag = frags.get(0);
					break;

				case R.id.btn_consulta_cartera_slide:
					if(!datosCartera())
					{
						getMessageBuilder().setMessage("Por favor descargue los datos necesarios para consultar la cartera (Presione boton de Actualización)").show();
						return;
					}
					frag = frags.get(1);
					break;

				case R.id.btn_geolocalizacion_slide:
					gotoMap();
					break;

				case R.id.btn_logout:
					finish();
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_right);
					break;
				case R.id.btn_prospeccion_slide:
					if(!datosProspeccion())
					{
						getMessageBuilder().setMessage("Por favor descargue los datos necesarios para ingresar a prospección (Presione boton de Actualización)").show();
						return;
					}
					frag = frags.get(3);
					break;

				case R.id.btn_cotizacion_slide:
					if(!datosCotizador())
					{
						getMessageBuilder().setMessage("Por favor descargue los datos necesarios para el cotizador (Presione boton de Actualización)").show();
						return;
					}
					frag = frags.get(2);
					break;
				default:
					break;
				}
				if (frag != null) {
					fragmentTransaction = getFragmentManager()
							.beginTransaction();
					fragmentTransaction.setCustomAnimations(R.animator.fade_in,
							R.animator.fade_out, R.animator.fade_in,
							R.animator.fade_out);
					fragmentTransaction.replace(
							com.jaguarlabs.sipac.R.id.contentFragment, frag)
							.commit();

				}
			}
		}).setView(pTarget));

	}

	private void doUpdate() {
		BackupModel.getInstance().setContextActivity(this);
		BackupModel.getInstance().startBackup();

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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent start, MotionEvent finish,
			float xVelocity, float yVelocity) {

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
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		Log.i("Main Activity", "Scroll");

		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		Log.i("Main Activity", "Show Press");

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		Log.i("Main Activity", "Single Tap up");
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gDetector.onTouchEvent(event);
	}

	@Override
	public void onBackPressed() {
		abrirCerrarMenu(!menuOpen)
				.setAnimationListener(commonAnimationListener);

	}

	public void cotizadorTabClick(View pTarget) {
		ViewFlipper flipper = (ViewFlipper) findViewById(R.id.cotizadorFlipper);
		flipper.setInAnimation(this, R.anim.viewflipper_in_right);
        flipper.setOutAnimation(this, R.anim.viewflipper_out_left);
        VerticalButton []tabs ={(VerticalButton)findViewById(R.id.titularButton),
        		(VerticalButton)findViewById(R.id.coberturaButton),
        		(VerticalButton)findViewById(R.id.conyugueButton),
        		(VerticalButton)findViewById(R.id.hijosButton)};
        
        
        for (VerticalButton btn_tem:tabs)
        {
        	 btn_tem.setBackgroundResource(R.drawable.img_tab_inact);
        }
        ((VerticalButton)findViewById(pTarget.getId())).setBackgroundResource(R.drawable.img_tab_act);
		switch (pTarget.getId()) {
		case R.id.titularButton:
			flipper.setDisplayedChild(0);
			break;
		case R.id.coberturaButton:
			flipper.setDisplayedChild(1);
			break;
		case R.id.conyugueButton:
			flipper.setDisplayedChild(2);
			break;
		case R.id.hijosButton:
			flipper.setDisplayedChild(3);
			break;
		}
	}

	public void backupData(View pTarget) {

		BackupModel.getInstance().setContextActivity(this);

		if (!wifiOn) {
			getMessageBuilder()
					.setMessage(
							"Es necesario tener una conexion WIFI Activa para reaizar este proceso");
			getMessageBuilder().create().show();
			return;
		}

		BackupModel.getInstance().startBackup();
	}

	private int counter = 1;
	private boolean stateThread = true;
	private Thread t;

	public void hilo() {
		t = new Thread() {
			public void run() {
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

	private void callback() {
		if( Utils.getGPSStatus( this ) ){
			Location location = getLocation();
			if(location != null)
			{
				String agente_id = DataModel.getInstance().getAppUser().getAgente();
				String promotoria_id = DataModel.getInstance().getPromotoria().getPromot();
				String latitude = location.getLatitude() + "";
				String longitude = location.getLongitude() + "";
				if (!agente_id.equals("") && !promotoria_id.equals("")
						&& !latitude.equals("") && !longitude.equals("")) {
					saveLocation(agente_id, promotoria_id, latitude, longitude);
					return;
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
	private static final long MIN_TIME_BW_UPDATES = 1000 * 10 * 1; // 10
																	// secconds
	private LocationManager locationManager;

	public Location getLocation() {
		Location location = null;
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			if (!isGPSEnabled) {
				// //TODO: case no network and no gps
				Log.i("resultGPS", "no internet or gps enabled");
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
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return location;
	}

	private void saveLocation(String agente_id, String promotoria_id,
			String latitude, String longitude) {

		(new RPCHandler(new IJSONResponseHandler() {
			private JSONObject response;

			@Override
			public void onResponse(JSONObject jsonResponse) throws Exception {
				response = jsonResponse;
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							Toast.makeText(mContext,
									response.getString("result") + "",
									Toast.LENGTH_SHORT).show();
						} catch (Exception e) {
						}

					}
				});
			}

			@Override
			public void onRequest() {
				Log.i("Save Location", "Guardando Localización");
			}

			@Override
			public void onException(Exception e) {
				Log.i("Save Location", "Error Guardando");
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							Toast.makeText(mContext,
									"Ubicación no disponible.",
									Toast.LENGTH_SHORT).show();
						} catch (Exception e) {
						}

					}
				});
			}
		})).execute(RPCHandler.OPERATION_SAVE_LOCATION, new BasicNameValuePair(
				"agente_id", agente_id), new BasicNameValuePair(
				"promotoria_id", promotoria_id), new BasicNameValuePair(
				"latitude", latitude), new BasicNameValuePair("longitude",
				longitude));

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

}
