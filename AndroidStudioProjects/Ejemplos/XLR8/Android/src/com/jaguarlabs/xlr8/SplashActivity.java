package com.jaguarlabs.xlr8;


import com.jaguarlabs.xlr8.util.ICallback;
import com.jaguarlabs.xlr8.util.TaskInit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class SplashActivity extends Activity implements ICallback {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		TaskInit task = new TaskInit(this);
		task.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
	
	@Override
	public void callback() {
		// TODO Auto-generated method stub
		Intent intento = new Intent(getApplicationContext(),
				HomePageActivity.class);
		startActivity(intento);
		finish();
		// setContentView( R.layout.activity_login );

	}

	@Override
	public void time() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
