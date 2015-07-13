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

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.*;
import android.view.View.OnTouchListener;
import android.widget.*;
import android.widget.RatingBar.OnRatingBarChangeListener;
//TODO: falta canreach
/**
 * FeedBack class
 * This activity is to show the feedback
 ** */
public class FeedBack extends Activity {
	static FeedBack mthis	= null;
	boolean debug			= true;
	float Rate				= 0;
	String coment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mthis=this;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_feed_back);
		RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar1);
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {	 
					Rate = rating; 
				}
		});
		
		ratingBar.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if( event.getAction() == MotionEvent.ACTION_DOWN){
					return false;
				}else if( event.getAction() == MotionEvent.ACTION_CANCEL ){
					return false;
				}else if( event.getAction() == MotionEvent.ACTION_MOVE ){
					return true;
				}
				return false;
			}
		});
		
		final ImageView send=(ImageView)findViewById(R.id.btn_send);
		send.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if(arg1.getAction()==MotionEvent.ACTION_DOWN){
					send.setImageResource(R.drawable.btn_send_pressed);
					return true;
				}else if(arg1.getAction() == MotionEvent.ACTION_UP){
					send.setImageResource(R.drawable.btn_send_normal);
					send.setEnabled( false );
					TextView comentario = (TextView) mthis.findViewById(R.id.comentario);
					coment = comentario.getText().toString();
					Log.i("Comentario", coment);
					mthis.sendFeedback();
					return true;
				}
				return false;
			}			
		});
		TextView gracias 	= (TextView) mthis.findViewById(R.id.gracias);
		TextView taxi 		= (TextView) mthis.findViewById(R.id.taxi);
		TextView cali 		= (TextView) mthis.findViewById(R.id.calif);
		TaxyMobile.mthis.loadfont( gracias );
		TaxyMobile.mthis.loadfont( taxi );
		loadfont( cali );
	}
	
	/*
	 * sendFeedback
	 * method for send a feedback
	 * @receive  
	 * @return void
	 */
	public void sendFeedback(){
		PostTask task = new PostTask();
		task.applicationContext = mthis;
		task.post_case = 5;
		task.execute();
	}
	
	/*
	 * post_feedback
	 * method for send data to feedback
	 * @receive  
	 * @return RESTClient
	 */
	public RESTClient post_feedback(){
		RESTClient rest=new RESTClient(TaxyMobile.base_url+"/rateservice");
		try{
			JSONObject post=new JSONObject();
			post.put("userId", TaxyMobile.mthis.userId +"");
			post.put("peticion", ""+TaxyMobile.mthis.peticionId);
			post.put("rate", ""+Rate + "");
			post.put("coment", coment);
			rest.post=post.toString();
			rest.Execute(RESTClient.RequestMethod.POST);
			log("feedResponse",""+rest.getResponse());
		}catch(Exception e){
			log("feedError",""+e);
		}
		return rest;
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
	
	/* manage_feedback
	 * method for close to activity
	 * @receive 
	 * @return void
	 * */
	public void manage_feedback(){
		mthis.finish();
	}
	
	/*
	 * loadfont
	 * method for load a font for aplication
	 * @receive 
	 *  TextView text
	 * @return void
	 */
	public void loadfont(TextView text) {
		Typeface myFont = Typeface.createFromAsset(getAssets(),
			"font/myriadpro_regular.otf");
		text.setTypeface(myFont);
	}

}
