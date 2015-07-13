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

package com.jaguarlabs.sipaccel.dialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.jaguarlabs.sipaccel.R;
import com.jaguarlabs.sipaccel.util.DismissDialog;

public class DatePickerDialog extends DismissDialog{

	private TextView textDate;
	private DatePicker datePicker;
	private Button btnAcep;
	private Button btnClear;
	
	public DatePickerDialog(Context context, TextView textDate) {
		super(context);
		this.textDate = textDate;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView( R.layout.dialog_date_picker );
		setlisteners();
	}
	
	private void setlisteners(){
		datePicker = (DatePicker) this.findViewById( R.id.datePicker );
		btnAcep = (Button) this.findViewById( R.id.acep_picker );
		btnClear = (Button) this.findViewById( R.id.clear_picker );
		btnAcep.setOnClickListener( this );
		btnClear.setOnClickListener(this);
		try
		{
		if(textDate.getText().toString().trim().length() > 0)
		{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			
			Date date = formatter.parse(textDate.getText().toString().trim());
			c.setTime(date);
			datePicker.updateDate(c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		}
		
		}catch (Exception error) {
			Log.e("Date","Error al Parsear la Fecha");
		}
	}

	@Override
	public void onClick(View v) {
		
		String dia = "00";
		String mes = "00";
		String anio = "";
		View dialogPicker = findViewById( R.id.dialogpicker );
		if(v == btnAcep)
		{
			dia += datePicker.getDayOfMonth();
			mes += ( datePicker.getMonth() + 1 );
			anio += datePicker.getYear();
			textDate.setText( anio + "-" + mes.substring( mes.length() - 2) + "-" + dia.substring( dia.length() - 2) );
		}else
		{
			textDate.setText("");
		}
		super.dismiss( dialogPicker );
		
		
		
		
	}

	
	
	
	
}
