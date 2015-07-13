package com.jaguarlabs.sipac.util;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.jaguarlabs.sipac.R;

public class CustomInfoWindow implements InfoWindowAdapter {

	LayoutInflater inflater = null;

	public CustomInfoWindow() {
	}
	
	public CustomInfoWindow(LayoutInflater inflater) {
		this.inflater = inflater;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return (null);
	}

	@Override
	public View getInfoContents(Marker marker) {
		View popup = inflater.inflate(R.layout.marker_layout, null);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		popup.setLayoutParams( params );
		TextView tv = (TextView) popup.findViewById(R.id.title_marker);
		tv.setText(marker.getTitle());
		tv = (TextView) popup.findViewById(R.id.snippet_marker);
		tv.setText(marker.getSnippet());
		return (popup);
	}
}
