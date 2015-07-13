package com.jaguarlabs.xlr8;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jaguarlabs.xlr8.util.GlobalVar;
import com.jaguarlabs.xlr8.util.JSONSharedPreferences;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class NewMissionActivity extends Activity {
	private EditText mission;
	private TextView title,information,url;
	private ScrollView scrollView;
	GlobalVar variableGlobal =new GlobalVar();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_mission);
		mission = (EditText) findViewById(R.id.textMission);
		information=(TextView)findViewById(R.id.showInformation);
		title=(TextView)findViewById(R.id.titleshowinformation);
		url=(TextView)findViewById(R.id.url);
		scrollView=(ScrollView)findViewById(R.id.scrollinformation);
		if(variableGlobal.actividad.equalsIgnoreCase("ShowMission")){
			information.setVisibility(View.INVISIBLE);
			title.setVisibility(View.INVISIBLE);
			url.setVisibility(View.INVISIBLE);
			scrollView.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_mission, menu);
		return true;
	}

	private String date() {
		Calendar calendario = Calendar.getInstance();
		SimpleDateFormat formato1 = new SimpleDateFormat("dd-MM-yyyy");
		String fecha = formato1.format(calendario.getTime());
		fecha=fecha.replace("-", "/");
		return fecha;
	}

	private boolean saveJson() {
		JSONObject json, jsontemp;
		JSONArray array;
		String fecha = date();

		try {
			JSONSharedPreferences shared = new JSONSharedPreferences();
			jsontemp = shared.loadJSONObject(getApplicationContext(), "json",
					"temporal");
			jsontemp.put("Mision", mission.getText().toString());
			jsontemp.put("date", fecha);
			array = shared.loadJSONArray(getApplicationContext(), "Misiones",
					"Mision");
			array.put(jsontemp);
			shared.saveJSONArray(getApplicationContext(), "Misiones", "Mision",
					array);
			json = shared.loadJSONObject(getApplicationContext(), "json",
					"Valores");
			json.put("Missions", array);

			shared.saveJSONObject(getApplicationContext(), "json", "valores",
					json);
			return true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.Save:
			if(mission.getText().toString().trim().length()>0){
			if (saveJson()) {
				Intent intento = new Intent(this, ShowMissionActivity.class);
				startActivity(intento);
				//finish();
			}
			}else
			{
				Context context = getApplicationContext();
				String texto = "Please insert one mission";
				int duracion = Toast.LENGTH_LONG;
				Toast toast = Toast.makeText(context, texto, duracion);
				toast.show();
			}
			break;
		case R.id.url:
			Uri webpage = Uri.parse("http://www.google.com");
			Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
			startActivity(webIntent);
			break;
		}

	}

}
