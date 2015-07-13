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

package com.jaguarlabs.sipaccel.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ClearButtonTextWatcher implements TextWatcher, OnClickListener {	
	protected ImageButton clearButton;
	protected EditText textField;
	
	public ClearButtonTextWatcher (ImageButton pButton,EditText pTextField)
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

