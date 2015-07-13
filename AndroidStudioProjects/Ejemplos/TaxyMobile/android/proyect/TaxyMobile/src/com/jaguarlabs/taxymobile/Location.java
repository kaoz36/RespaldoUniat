///** *************************************************************************
// *
// *   Copyright (c)  2013 by Jaguar Labs.
// *   Confidential and Proprietary
// *   All Rights Reserved
// *
// *   This software is furnished under license and may be used and copied
// *   only in accordance with the terms of its license and with the
// *   inclusion of the above copyright notice. This software and any other
// *   copies thereof may not be provided or otherwise made available to any
// *   other party. No title to and/or ownership of the software is hereby
// *   transferred.
// *
// *   The information in this software is subject to change without notice and
// *   should not be construed as a commitment by JaguarLabs.
// *
// * @(#)$Id: $
// * Last Revised By   : efren campillo
// * Last Checked In   : $Date: $
// * Last Version      : $Revision:  $
// *
// * Original Author   : Julio Hernandez  julio.hernadez@jaguarlabs.com
// * Origin            : SEnE -- march 13 @ 11:00 (PST)
// * Notes             :
// *
// ** *************************************************************************/
//
//package com.jaguarlabs.taxymobile;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.graphics.Typeface;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnTouchListener;
//import android.view.Window;
//import android.view.animation.Animation;
//import android.view.animation.Animation.AnimationListener;
//import android.view.animation.AnimationUtils;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
///**
// * Location class 
// * activity for locate a device
// ** */
//
//public class Location extends MapActivity {
//	static Location mthis	=null;
//    MapView mapa 			= null;
//    
//    double lat 				= 0;
//    double lon 				= 0;
//    
//    String comment 			= "";
//    GPSlocalize gps;
//    
//    boolean _debug			= true;
//    boolean _menulayout		= false;
//    boolean _cancel 		= true;
////    boolean _msj			= false;
//    
//    Animation anim;
//    
//    AlertDialog nota 	= null;
//    
//    @Override   
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mthis = this;    
//        setContentView(R.layout.activity_location);   
//        mapa = (MapView)findViewById(R.id.mapa);
//        mapa.setBuiltInZoomControls(true);        
//        setButtons();
//        GPSlocalize gps = new GPSlocalize(mthis);
//        if( gps.canGetLocation() ){
//        	updatePosition( gps.getLocation() );
//        	msjLocation();
//        }else{
//        	gps.showSettingsAlert();        	
//        }
//       
//    }
//    
//    
//    @Override
//	protected  void onActivityResult(int requestCode, int resultCode, Intent data){
//		log("result","request:"+requestCode+"  result:"+resultCode+"  intent:"+data);
//		if( requestCode == 11 ){
//			GPSlocalize gps = new GPSlocalize(mthis);
//	        if( gps.canGetLocation() ){
//	        	updatePosition(gps.getLocation());
//	        	msjLocation();
//	        }else{
//	        	gps.showSettingsAlert();
//	        }
//		}
//	}
//    
//    public void log(String tag, String msg) {
//		if( _debug ){
//			Log.i(tag, msg);
//		}					
//	}
//    
//    /*
//     * setButtons
//     * method for set buttons 
//     * @receive
//     * @return void
//     */
//	public void setButtons() {
//		final String url 		= TaxyMobile.base_url;
//		final ImageButton ok 	= (ImageButton)this.findViewById(R.id.imageButton1);  
//		ok.setOnTouchListener(new OnTouchListener(){
//			@Override
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				if( arg1.getAction() == MotionEvent.ACTION_DOWN ){
//					ok.setImageResource( R.drawable.btn_ok_pressed );
//					return true;
//				}else if( arg1.getAction() == MotionEvent.ACTION_UP ){
////					if( TaxyMobile.getConexion( url ) ){
//						SharedPreferences pref 	= mthis.getPreferences(MODE_PRIVATE);
//						Editor e = pref.edit();
//						e.putBoolean( "_stop", false );
//						e.commit();
//						log("_stop cambio", "_stop" + pref.getBoolean("_stop: ", true) );
//						ok.setImageResource(R.drawable.btn_ok_normal);
//						if( TaxyMobile.mthis.findViewById(R.id.calltaxi) != null ){
//							TaxyMobile.mthis.findViewById(R.id.calltaxi).setEnabled( true );
//							Log.i("Paso", TaxyMobile.mthis.findViewById(R.id.calltaxi) + "");
//						}else{
//							Log.i("Ubiera Crashado", TaxyMobile.mthis.findViewById(R.id.calltaxi) + "");
//						}
//							
//						Intent output = new Intent();
//						output.putExtra( "location", lat + "," + lon );
//						output.putExtra( "comment", comment );
//						setResult(RESULT_OK, output);
////					}else{
////						TaxyMobile.mthis.showAlertDialog("Error de conexión.", "Intenta mas tarde.");
////						TaxyMobile.mthis.log("Error de conexión", "Intenta mas tarde");
////					}
//					mthis.finish();
//					return true;					
//				}
//				return false;
//			}		
//		});
//		
//		ImageButton back = (ImageButton)mthis.findViewById(R.id.back_button);
//		back.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View arg0) {
//				setResult(RESULT_CANCELED);
//				//gps.stopUsingGPS();//TODO: desactivate GPS-
//				TaxyMobile.mthis.findViewById(R.id.calltaxi).setEnabled( true );
//				mthis.finish();		
//			}
//			
//		});
//		
//		LinearLayout addcomment = (LinearLayout) mthis.findViewById(R.id.addnote);
//		addcomment.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View arg0) {
//				addComment();
//			}			
//		});
//		
//		LinearLayout getUbicacion = (LinearLayout) mthis.findViewById(R.id.ubicacion);
//		getUbicacion.setOnClickListener(new OnClickListener() {			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				GPSlocalize gps = new GPSlocalize(mthis);
//		        if( gps.canGetLocation() ){
//		        	updatePosition( gps.getLocation() );
//		        	msjLocation();
//		        }else{
//		        	gps.showSettingsAlert();        	
//		        }
//			}
//		});
//		
//		final ImageView buttonmenu 			= (ImageView)findViewById(R.id.btn_menu);
//		final LinearLayout layoutmenu 		= (LinearLayout)mthis.findViewById(R.id.menulayout); 
//		buttonmenu.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View arg0) {
//				menu( layoutmenu, buttonmenu );				
//			}			
//		});
//	}
//	
//	 /*
//     * addComment
//     * method for add comments
//     * @receive
//     * @return void
//     */
//	public void addComment(){
//		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mthis);
//		alertDialog.setTitle("Nota");
//        LayoutInflater inflater = mthis.getLayoutInflater();
//		final View comment = inflater.inflate(R.layout.taxymobile_agregar_nota, null);
//		alertDialog.setView(comment);
//        alertDialog.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog,int which) {
//            	dialog.dismiss();
//            }
//        });
//        alertDialog.setNegativeButton("Agregar", new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				EditText text=(EditText)comment.findViewById(R.id.editText1);
//	              mthis.comment=text.getText().toString();
//			}
//		});
//        alertDialog.setCancelable( false );
//        alertDialog.show();
//	}
//	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		final LinearLayout layoutmenu = (LinearLayout)mthis.findViewById(R.id.menulayout); 
//		switch( keyCode ){
//			////KeyBack
//			case 4:
//				if( _menulayout ){
//					_menulayout = false;
//					Animation inmenu = AnimationUtils.loadAnimation(mthis, R.layout.animation_menu_out);
//					layoutmenu.startAnimation(inmenu);
//					layoutmenu.setVisibility(View.GONE);
//				}else{
//					TaxyMobile.mthis.findViewById(R.id.calltaxi).setEnabled( true );
//					mthis.finish();					
//				}	
//				break;
//			////Ajustes
//			case 82:
//				menu( layoutmenu, (ImageView)findViewById(R.id.btn_menu) );
//				break;
//		}			   
//	    return true;
//	}
//	
//	/*
//	 * menu
//	 * method for show the menu
//	 * @receive  
//	 *  LinearLayout layoutmenu
//	 * @return void
//	 */
//	public void menu(final LinearLayout layoutmenu, final ImageView buttonmenu){
//		final FrameLayout helptem = (FrameLayout) mthis.findViewById(R.id.helptem);
//		if( _menulayout ){
//			_menulayout = false;
//			Animation inmenu = AnimationUtils.loadAnimation(mthis, R.layout.animation_menu_out);
//			inmenu.setAnimationListener(new AnimationListener() {				
//				@Override
//				public void onAnimationStart(Animation animation) {
//					buttonmenu.setEnabled( false );
//				}
//				@Override
//				public void onAnimationRepeat(Animation animation) {					
//				}				
//				@Override
//				public void onAnimationEnd(Animation animation) {
//					buttonmenu.setEnabled( true );
//					helptem.setVisibility( View.GONE );
//				}
//			});
//			layoutmenu.startAnimation(inmenu);
//			
//			layoutmenu.setVisibility(View.GONE);
//		}else{
//			_menulayout = true;	
//			layoutmenu.setVisibility(View.VISIBLE);
//			Animation inmenu = AnimationUtils.loadAnimation(mthis, R.layout.animation_menu_in);
//			inmenu.setAnimationListener(new AnimationListener() {				
//				@Override
//				public void onAnimationStart(Animation animation) {
//					buttonmenu.setEnabled( false );
//				}
//				@Override
//				public void onAnimationRepeat(Animation animation) {					
//				}				
//				@Override
//				public void onAnimationEnd(Animation animation) {
//					buttonmenu.setEnabled( true );
//					helptem.setVisibility( View.VISIBLE );
//				}
//			});
//			layoutmenu.startAnimation(inmenu);
//			
//			LinearLayout help = (LinearLayout) layoutmenu.findViewById(R.id.linear_help);
//			help.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					startHelp();
//					Animation inmenu = AnimationUtils.loadAnimation(mthis, R.layout.animation_menu_out);
//					layoutmenu.startAnimation(inmenu);
//					layoutmenu.setVisibility(View.GONE);
//					helptem.setVisibility( View.GONE );
//					_menulayout = false;
//				}
//			});
//			
//			LinearLayout delete = (LinearLayout) mthis.findViewById(R.id.linear_deletedata);
//			delete.setOnClickListener(new OnClickListener() {				
//				@Override
//				public void onClick(View v) {
////					if( TaxyMobile.getConexion( mthis ) ){
//					mthis.finish();
//					TaxyMobile.mthis.clearApplicationData();
//						
////					}
//					
//				}
//			});
//			
//			helptem.setOnClickListener(new OnClickListener() {				
//				@Override
//				public void onClick(View v) {
//					helptem.setVisibility( View.GONE );
//					menu(layoutmenu, buttonmenu );
//				}
//			});
//		}
//		
//		loadfontCity( (TextView) mthis.findViewById(R.id.help) );
//		loadfontCity( (TextView) mthis.findViewById(R.id.deletedata) );
//	}
//	
//	/*
//	 * msjLocation
//	 * method for show the mesagge the GPS
//	 * @receive 
//	 * @return void
//	 */
//	private void msjLocation(){
//		nota = new AlertDialog.Builder(mthis).create();
//		nota.setTitle("GPS localizando ubicación.");
//		nota.setMessage("Esto puede tomar unos segundos, por favor espera...");
//		nota.setButton("Aceptar", new DialogInterface.OnClickListener() {			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				nota.cancel();
//			}
//		});
//		nota.show();
//	}
//	
//	/*
//	 * startHelp
//	 * method for show help
//	 * @receive 
//	 * @return void
//	 */
//	public void startHelp(){
//		final RelativeLayout help = (RelativeLayout) mthis.findViewById(R.id.help_location);
//		anim = AnimationUtils.loadAnimation(mthis, R.anim.help_in);
//		anim.setAnimationListener(new AnimationListener() {			
//			@Override
//			public void onAnimationStart(Animation animation) {
//				help.setVisibility(View.VISIBLE);
//			}			
//			@Override
//			public void onAnimationRepeat(Animation animation) {}			
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				help.setOnTouchListener(new OnTouchListener() {			
//					@Override
//					public boolean onTouch(View v, MotionEvent event) {
//						if( event.getAction() == MotionEvent.ACTION_DOWN ){
//							return true;
//						}else if( event.getAction() == MotionEvent.ACTION_UP ){
//							anim = AnimationUtils.loadAnimation(mthis, R.anim.help_out);
//							anim.setFillAfter(true);
//							anim.setAnimationListener(new AnimationListener() {
//								
//								@Override
//								public void onAnimationStart(Animation animation) {}
//								
//								@Override
//								public void onAnimationRepeat(Animation animation) {}
//								
//								@Override
//								public void onAnimationEnd(Animation animation) {
//									help.setVisibility( View.GONE );
//									help.clearAnimation();
//								}
//							});
//							help.startAnimation(anim);							
//							return true;
//						}
//						return false;
//					}
//				});
//				help.clearAnimation();
//			}
//		});
//		anim.setFillAfter(true);
//		help.startAnimation(anim);
//	}
//	
//    public void updatePosition(android.location.Location location){
//    	if( location != null ){
//	    	lat 								= location.getLatitude();
//	    	lon 								= location.getLongitude();
//	    	GeoPoint point						= new GeoPoint((int)(location.getLatitude() * 1E6), (int)(location.getLongitude() * 1E6));
//	        OverlayItem overlayitem 			= new OverlayItem(point, "tittle", "message");
//	        mapa.getOverlays().clear();
//	        List<Overlay> mapOverlays 			= mapa.getOverlays();
//	        Drawable drawable 					= this.getResources().getDrawable(R.drawable.icon_location_green);
//	        HelloOverlayItems itemizedoverlay 	= new HelloOverlayItems(drawable, this);
//	        itemizedoverlay.addOverlay( overlayitem );
//	        mapOverlays.add( itemizedoverlay );
//	        mapa.getController().setCenter( point );
//	        mapa.getController().setZoom( 16 );
//    	}
//    }
//	@Override
//    protected boolean isRouteDisplayed() {
//        return false;
//    }
//	
//	/*
//	 * loadfontCity
//	 * method for load a font for aplication
//	 * @receive 
//	 *  TextView text
//	 * @return void
//	 */
//	public void loadfontCity(TextView text){
//		Typeface myFont = Typeface.createFromAsset(getAssets(),
//			"font/myriadpro_regular.otf");
//		text.setTypeface(myFont);
//	}
//}
//
//class HelloOverlayItems extends ItemizedOverlay{
//	
//	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
//	Context mContext=null;
//	
//	public HelloOverlayItems(Drawable defaultMarker) {
//		super(boundCenterBottom(defaultMarker));
//	}
//	public HelloOverlayItems(Drawable defaultMarker, Context context) {
//		  super(boundCenterBottom(defaultMarker));
//		  mContext = context;
//	}
//	@Override
//	protected OverlayItem createItem(int i) {
//	  return mOverlays.get(i);
//	}
//
//	@Override
//	public int size() {
//	  return mOverlays.size();
//	}
//	public void addOverlay(OverlayItem overlay) {
//		mOverlays = new ArrayList<OverlayItem>();
//	    mOverlays.add(overlay);
//	    populate();
//	}
//
//}