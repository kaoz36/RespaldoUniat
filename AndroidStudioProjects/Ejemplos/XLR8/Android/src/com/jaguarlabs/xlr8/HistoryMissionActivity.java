package com.jaguarlabs.xlr8;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.jaguarlabs.xlr8.util.Adapter;
import com.jaguarlabs.xlr8.util.GlobalVar;
import com.jaguarlabs.xlr8.util.JSONSharedPreferences;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class HistoryMissionActivity extends Activity {
	private ArrayList<String> date, mission;
	ListView list;
	public static final String INDEX_JSON="-2";
	public static final String MISSION="";
	GlobalVar variableGlobal =new GlobalVar();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_mission);
		variableGlobal.actividad="History";
		date=new ArrayList<String>();
		mission= new ArrayList<String>();
		GuardaArray();
		list=(ListView)findViewById(R.id.missionList);
		
		list.setAdapter(new Adapter(this, mission,date));
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    public void onItemClick(AdapterView parent, View v, int position, long id){
		        // Start your Activity according to the item just clicked.
		    	sendData(position);
		    }
		});
		
		
	}
	
	public void sendData(int position){
		int index=coutNoItems()-position;
		Intent intento =new Intent(this, EditMissionActivity.class);
		intento.putExtra(INDEX_JSON,String.valueOf(index));
		startActivity(intento);
		finish();
	}
	private void GuardaArray(){
		 JSONObject json,temp;
		 JSONArray array;
		 JSONSharedPreferences shared = new JSONSharedPreferences();
		 try {
			json= shared.loadJSONObject(getApplicationContext(), "json", "valores");
			array=json.getJSONArray("Missions");
			int x=array.length();
			for(int i=x-1; i>=0; i--){
				temp=array.getJSONObject(i);
				mission.add(temp.getString("Mision"));
				date.add(temp.getString("date"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private int coutNoItems() {
		JSONObject json, temp;
		JSONArray array;
		JSONSharedPreferences shared = new JSONSharedPreferences();
		try {
			json = shared.loadJSONObject(getApplicationContext(), "json",
					"valores");
			array = json.getJSONArray("Missions");
			int x = array.length();
			return x-1;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history_mission, menu);
		return true;
	}

}
