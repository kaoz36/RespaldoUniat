package com.jaguarlabs.xlr8;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jaguarlabs.xlr8.util.GlobalVar;
import com.jaguarlabs.xlr8.util.JSONSharedPreferences;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ShowMissionActivity extends Activity {
	private TextView mission;
	public static final String INDEX_JSON="-2";
	public static final String MISSION="";
	GlobalVar variableGlobal =new GlobalVar();
	Intent intento;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_mission);
		mission=(TextView)findViewById(R.id.showMissionText);
		String cadena= lastMission();
		if(!cadena.equals("")){
			mission.setText(cadena);
		}
		variableGlobal.actividad="ShowMission";
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_mission, menu);
		return true;
	}
	
	private String lastMission(){
		 JSONObject json,temp;
		 JSONArray array;
		 JSONSharedPreferences shared = new JSONSharedPreferences();
		 try {
			json= shared.loadJSONObject(getApplicationContext(), "json", "valores");
			array=json.getJSONArray("Missions");
			int x=array.length();
			temp=array.getJSONObject(x-1);
			String cadena=temp.getString("Mision");
			return cadena;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return "";
		}
		
	}
	
	public void onClick(View view){
		switch (view.getId()) {
		case R.id.newMission:
			intento =new Intent(this, NewMissionActivity.class);
			startActivity(intento);
			//finish();
			break;
		case R.id.history:
			intento =new Intent(this, HistoryMissionActivity.class);
			startActivity(intento);
			break;
		case R.id.editMission:
			intento =new Intent(this, EditMissionActivity.class);
			intento.putExtra(INDEX_JSON,"-1");
			startActivity(intento);
			finish();
			break;
			
		case R.id.setReminders:
			intento=new Intent(this, SetReminderActivity.class);
			intento.putExtra(MISSION,mission.getText().toString());
			startActivity(intento);
		}
		
	}

}
