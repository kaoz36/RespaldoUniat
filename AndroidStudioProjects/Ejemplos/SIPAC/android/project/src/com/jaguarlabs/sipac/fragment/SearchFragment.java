package com.jaguarlabs.sipac.fragment;


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
import android.widget.ToggleButton;

import com.jaguarlabs.sipac.MainApplicationActivity;
import com.jaguarlabs.sipac.R;
import com.jaguarlabs.sipac.ResultActivity;
import com.jaguarlabs.sipac.model.DataModel;
import com.jaguarlabs.sipac.net.RPCHandler;
import com.jaguarlabs.sipac.util.ClearButtonCompleterTextWatcher;
import com.jaguarlabs.sipac.util.IJSONResponseHandler;
import com.jaguarlabs.sipac.util.Utils;

public class SearchFragment extends RightFragment{
	
	private AutoCompleteTextView textField;
	private Button 	deleteButton;
	private Button searchButton;
	private ToggleButton toggleButton;
	private ClearButtonCompleterTextWatcher searchWatcher;
	
	
	@Override
	protected void makeView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Button back;
		MainApplicationActivity.MainApplicationOnTouchListener interaction = new MainApplicationActivity.MainApplicationOnTouchListener(listener);
		
		mainView= inflater.inflate(com.jaguarlabs.sipac.R.layout.fragment_search,container,false);
		textField= (AutoCompleteTextView)mainView.findViewById(R.id.searchText);
		deleteButton = (Button)mainView.findViewById(R.id.deletebutton);
		searchButton = (Button)mainView.findViewById(R.id.searchButton);
		toggleButton = (ToggleButton)mainView.findViewById(R.id.togglebutton);
		
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
		            SpannableStringBuilder sourceAsSpannableBuilder = (SpannableStringBuilder)source;
		            for (int i = end - 1; i >= start; i--) { 
		                char currentChar = source.charAt(i);
		                 if (!Character.isLetterOrDigit(currentChar) && !Character.isSpaceChar(currentChar) && currentChar != '.') {    
		                     sourceAsSpannableBuilder.delete(i, i+1);
		                 }     
		            }
		            return source;
		        } else {
		            StringBuilder filteredStringBuilder = new StringBuilder();
		            for (int i = 0; i < end; i++) { 
		                char currentChar = source.charAt(i);
		                if (Character.isLetterOrDigit(currentChar) || Character.isSpaceChar(currentChar) || currentChar == '.') {    
		                    filteredStringBuilder.append(currentChar);
		                }     
		            }
		            return filteredStringBuilder.toString();
		        }
			}
		};
		
		textField.setFilters(new InputFilter[]{filter}); 
		
		back = (Button)mainView.findViewById(R.id.back);
		back.setOnTouchListener(interaction);
		back.setOnClickListener(interaction);
		
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
				
					if (textField.getText().toString().trim().length() > 0) {
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
	

	
}
