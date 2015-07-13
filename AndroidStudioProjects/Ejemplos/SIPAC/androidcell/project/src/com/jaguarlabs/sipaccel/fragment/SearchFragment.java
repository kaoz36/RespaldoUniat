/** *************************************************************************
 *
 *   Copyright (c)  2013 by Jaguar Labs.
 *   Confidential and Proprietary
 *   All Rights Reserved
 *
 *   This software is furnished under license and may be used and copied
 *   only in accordance with the terms of its license and with the
 *   inclusion of the above copyright notice. This software and any other
 *   copies thereof may not be provided or otherwise made available to any
 *   other party. No title to and/or ownership of the software is hereby
 *   transferred.
 *
 *   The information in this software is subject to change without notice and
 *   should not be construed as a commitment by JaguarLabs.
 *
 * @(#)$Id: $
 * Last Revised By   : Raul Acevedo
 * Last Checked In   : $Date: $
 * Last Version      : $Revision:  $
 *
 * Original Author   : Julio Hernandez  julio.hernadez@jaguarlabs.com
 * Origin            : SEnE -- April 25 @ 11:00 (PST)
 * Notes             :
 *
 * *************************************************************************/

package com.jaguarlabs.sipaccel.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.jaguarlabs.sipaccel.MainAplicationActivity;
import com.jaguarlabs.sipaccel.R;
import com.jaguarlabs.sipaccel.ResultActivity;
import com.jaguarlabs.sipaccel.model.DataModel;
import com.jaguarlabs.sipaccel.net.RPCHandler;
import com.jaguarlabs.sipaccel.util.ClearButtonCompleterTextWatcher;
import com.jaguarlabs.sipaccel.util.IJSONResponseHandler;
import com.jaguarlabs.sipaccel.util.Utils;

public class SearchFragment extends RightFragment {
	private AutoCompleteTextView textField;
	private ImageButton deleteButton;
	private Button searchButton;
	private ToggleButton toggleButton;
	private ClearButtonCompleterTextWatcher searchWatcher;

	@Override
	protected void makeView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ImageButton menu;
		MainAplicationActivity.MainAppTouch interaction = new MainAplicationActivity.MainAppTouch(
				listener);

		mainView = inflater.inflate(R.layout.fragment_search, container, false);
		textField = (AutoCompleteTextView) mainView.findViewById(R.id.searchText);
		deleteButton = (ImageButton) mainView.findViewById(R.id.deletebutton);
		searchButton = (Button) mainView.findViewById(R.id.searchButton);
		toggleButton = (ToggleButton) mainView.findViewById(R.id.togglebutton);

		searchWatcher = new ClearButtonCompleterTextWatcher(caller,deleteButton, textField);
		textField.addTextChangedListener(searchWatcher);

		deleteButton.setOnClickListener(searchWatcher);
		searchWatcher.setActivated(false);

		searchButton.setOnClickListener(getSearchClickHandler());

		InputFilter filter = new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				// TODO Auto-generated method stub
				if (source instanceof SpannableStringBuilder) {
					SpannableStringBuilder sourceAsSpannableBuilder = (SpannableStringBuilder) source;
					for (int i = end - 1; i >= start; i--) {
						char currentChar = source.charAt(i);
						if (!Character.isLetterOrDigit(currentChar)
								&& !Character.isSpaceChar(currentChar) && currentChar != '.') {
							sourceAsSpannableBuilder.delete(i, i + 1);
						}
					}
					return source;
				} else {
					StringBuilder filteredStringBuilder = new StringBuilder();
					for (int i = 0; i < end; i++) {
						char currentChar = source.charAt(i);
						if (Character.isLetterOrDigit(currentChar)
								|| Character.isSpaceChar(currentChar) || currentChar == '.') {
							filteredStringBuilder.append(currentChar);
						}
					}
					return filteredStringBuilder.toString();
				}
			}
		};

		textField.setFilters(new InputFilter[] { filter });

		menu = (ImageButton) mainView.findViewById(R.id.menu);
		menu.setOnTouchListener(interaction);
		menu.setOnClickListener(interaction);

		toggleButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Boolean on = ((ToggleButton) v).isChecked();
				searchWatcher.setActivated(on);
			}
		});
	}

	private OnClickListener getSearchClickHandler() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (textField.getText().length() > 0) {
					Intent intent = new Intent(caller.getApplicationContext(),ResultActivity.class);
					EditText textField = (EditText)mainView.findViewById(R.id.searchText);
					String value = textField.getText().toString();
					intent.putExtra("searchTerm", value);
					registerSearch();
					startActivity(intent);
					caller.overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
				} else {
					caller.getMessageBuilder().setMessage("Por favor llene el campo para buscar");
					caller.getMessageBuilder().show();
				}	
				
			}
		};
	}

	private void registerSearch() {
		if( Utils.getGPSStatus(caller) ){
			LocationManager locationManager = (LocationManager)caller.getSystemService(Context.LOCATION_SERVICE);
	        Criteria criteria = new Criteria();
	        String provider = locationManager.getBestProvider(criteria, true);
	        Location location = locationManager.getLastKnownLocation(provider);
	        if(location!=null){
	            saveSearch(location);
	        }
		}
	}

	private void saveSearch(Location pLocation) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = formatter.format(new Date());
		
		(new RPCHandler(new IJSONResponseHandler() {
			
			@Override
			public void onResponse(JSONObject jsonResponse) throws Exception {
				// TODO Auto-generated method stub
				Log.i("Save Search Location", "Localizacion Guardada");
			}
			
			@Override
			public void onRequest() {
				// TODO Auto-generated method stub
				Log.i("Save Search Location", "Guardando Localización de busqueda");
			}
			
			@Override
			public void onException(Exception e) {
				// TODO Auto-generated method stub
				Log.i("Save Search Location", "Error Guardando Localización de busqueda");
			}
		})).execute(RPCHandler.OPERATION_SAVE_SEARCH,
					new BasicNameValuePair("id_usuario",DataModel.getInstance().getAppUser().getAgente()),
					new BasicNameValuePair("latitude",""+pLocation.getLatitude()),
					new BasicNameValuePair("longitude",""+pLocation.getLongitude()),
					new BasicNameValuePair("fecha",currentDate));
	}

}
