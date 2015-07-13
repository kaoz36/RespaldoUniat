package com.jaguarlabs.sancarlos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class Splash extends Activity{
	static Splash mthis;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		mthis = this;
		Task t = new Task();
		t.post_case = 0;
		t.execute();
	}
	
	public void callback(){
		Intent i = new Intent(mthis, Login.class);
		mthis.startActivity(i);
		mthis.finish();
	}
}
