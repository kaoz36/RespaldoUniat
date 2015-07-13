package com.jaguarlabs.sipac.util;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class SuggestionArrayAdapter <T>extends ArrayAdapter<T> {

	private List<T> list;
	public SuggestionArrayAdapter(Context context, int resource, List<T> objects) {	
		super(context, resource, objects);
		list = objects;
	}
	
	
	public List<T> getList(){
		return list;
	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		
		return v;
	}
	
	

	

}
