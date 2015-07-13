package com.jaguarlabs.xlr8;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jaguarlabs.xlr8.util.GlobalVar;
import com.jaguarlabs.xlr8.util.JSONSharedPreferences;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


@TargetApi(19)
public class EditMissionActivity extends Activity {
	EditText edit;
	int index;
	String message=""; 
	GlobalVar variableGlobal =new GlobalVar();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_mission);
		Intent intent = getIntent();
	    message = intent.getStringExtra(ShowMissionActivity.INDEX_JSON);
		if (message.equals("-2")) {
			message = intent.getStringExtra(HistoryMissionActivity.INDEX_JSON);
		}
		index = Integer.valueOf(message);
		edit = (EditText) findViewById(R.id.EditTextMissionEdit);
		edit.setText(TextEdit(index));

	}

	private String TextEdit(int index) {
		JSONObject json, temp;
		JSONArray array;
		JSONSharedPreferences shared = new JSONSharedPreferences();

		try {
			json = shared.loadJSONObject(getApplicationContext(), "json",
					"valores");
			array = json.getJSONArray("Missions");
			if (index == -1) {
				int x = array.length();
				temp = array.getJSONObject(x - 1);
			} else {
				temp = array.getJSONObject(index);
			}
			String cadena = temp.getString("Mision");
			return cadena;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return "";
		}

	}
	
	private void saveEdit() {
		JSONObject json, jsontemp;
		JSONArray array;
		JSONSharedPreferences shared = new JSONSharedPreferences();
		try {
			jsontemp = shared.loadJSONObject(getApplicationContext(),"json", "valores");
			array =jsontemp.getJSONArray("Missions");
			if(index==-1){
				int x = array.length();
				json=array.getJSONObject(x-1);
				json.put("Mision", edit.getText().toString());
				//array.remove(x);
				//array.put(json);
				//jsontemp.put("Missions", array);
			}
			else
			{
				json=array.getJSONObject(index);
				json.put("Mision", edit.getText().toString());
			}
			shared.saveJSONObject(getApplicationContext(), "json", "valores",
					jsontemp);
		} catch (JSONException e) {

		}
	}
	
	public void onClick(View view) {
		if (edit.getText().toString().trim().length() > 0) {
			saveEdit();
			if (variableGlobal.actividad.equalsIgnoreCase("ShowMission")) {
				Intent intento = new Intent(this, ShowMissionActivity.class);
				startActivity(intento);
			}
			if (variableGlobal.actividad.equalsIgnoreCase("History")) {
				Intent intento = new Intent(this, HistoryMissionActivity.class);
				startActivity(intento);
			}
			finish();
		} else {
			Context context = getApplicationContext();
			String texto = "Please insert one mission";
			int duracion = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, texto, duracion);
			toast.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_mission, menu);
		return true;
	}

}
