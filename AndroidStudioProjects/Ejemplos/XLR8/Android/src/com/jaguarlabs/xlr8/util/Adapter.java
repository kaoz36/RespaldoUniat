package com.jaguarlabs.xlr8.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jaguarlabs.xlr8.R;



import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Adapter extends BaseAdapter{
	ArrayList<String> mission;
	ArrayList<String> date;
	Context contexto;
	
	public Adapter(Context contexto, ArrayList<String> mission, ArrayList<String> date) {
		super();
		this.contexto = contexto;
		this.mission=mission;
		this.date= date;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.mission.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.mission.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rootView =convertView;
		String tempMission;
		if(convertView==null){
			LayoutInflater inflado=(LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rootView=inflado.inflate( R.layout.missionlist, parent,false);
		}
		
		TextView missionDetail =(TextView)rootView.findViewById(R.id.missionDesc);
		TextView dateMission= (TextView)rootView.findViewById(R.id.dateMission);
//		if(mission.get(position).length()>20){
//			tempMission=mission.get(position).substring(0,20)+"...";
//		}
//		else
//		{
			tempMission=mission.get(position);
		//}
		missionDetail.setText(tempMission);
		dateMission.setText(date.get(position));
		dateMission.setTextSize(20.0f);
		missionDetail.setTextSize(20.0f);
		
		if(position%2>0){
			missionDetail.setBackgroundColor(Color.WHITE);
			dateMission.setBackgroundColor(Color.WHITE);
		}
		else{
			dateMission.setBackgroundColor(Color.LTGRAY);
			missionDetail.setBackgroundColor(Color.LTGRAY);
		}
		
		rootView.setFocusable(false);
		return rootView;
	}
	
	
}
