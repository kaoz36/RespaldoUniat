package com.jaguarlabs.sipac.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ClearButtonTextWatcher implements TextWatcher,OnClickListener {	
		protected Button clearButton;
		protected EditText textField;
		
		public ClearButtonTextWatcher (Button pButton,EditText pTextField)
		{
			clearButton = pButton;
			textField = pTextField; 
		}
			
		
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if(textField.getText().length() >0){
				clearButton.setVisibility(View.VISIBLE);
			}else{
				clearButton.setVisibility(View.INVISIBLE);

			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {	
		}



		@Override
		public void onClick(View v) {
			textField.setText("");
			
		}
}

