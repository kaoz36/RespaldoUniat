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

import com.jaguarlabs.sipaccel.util.ICallback;
import com.jaguarlabs.sipaccel.util.Task;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

/**
 * SplashActivity
 * Splash of the aplication
 *
 */

public class SplashActivity extends Activity implements ICallback{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		Task t = new Task( this );
		t.post_case = 0;
		t.execute();
	}
	
	@Override
	public void callback(){
		final ImageView img = (ImageView) findViewById(R.id.imageSplash );
		Animation anim = AnimationUtils.loadAnimation( this, R.anim.splash_out);
		anim.setAnimationListener( new AnimationListener() {			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				Intent i = new Intent( getApplicationContext(), LoginActivity.class);
				startActivity( i );
				finish();
			}
		});
		anim.setFillAfter( true );
		img.startAnimation(anim);
	}
	
	@Override
	public void time() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void callback(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		
	}

}
