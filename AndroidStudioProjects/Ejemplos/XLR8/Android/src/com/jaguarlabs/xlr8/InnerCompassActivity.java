package com.jaguarlabs.xlr8;



import org.json.JSONException;
import org.json.JSONObject;

import com.jaguarlabs.xlr8.util.JSONSharedPreferences;

import android.os.Bundle;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;

public class InnerCompassActivity extends Activity {
	private SharedPreferences sharedPref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inner_compass);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inner_compass, menu);
		return true;
	}
	
	private boolean ValidacionJson() {
		JSONSharedPreferences shared = new JSONSharedPreferences();
		sharedPref = getPreferences(Context.MODE_PRIVATE);
		JSONObject temp;
		try {
			temp = shared.loadJSONObject(getApplicationContext(), "json",
					"valores");
			if (temp.toString().equalsIgnoreCase("{}")) {
				return true;
			} else {
				return false;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	
	public void onClick(View view){
		switch (view.getId()) {
		case R.id.Missions:
			if(ValidacionJson()){
				Intent intento=new Intent(this, NewMissionActivity.class);
				startActivity(intento);
			}
			else
			{
				Intent intento=new Intent(this, ShowMissionActivity.class);
				startActivity(intento);
			}
			break;
		}
	}

}
