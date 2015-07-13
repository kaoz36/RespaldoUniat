package com.uniat.eduscore.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.uniat.eduscore.dialogs.DatePickerDialog;

/**
 * Created by Admin on 13/04/2015.
 */
public class DatePickerOnClickListener implements View.OnClickListener {

    private TextView date;
    private Context context;

    public DatePickerOnClickListener( TextView date, Context context){
        this.date = date;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        DatePickerDialog dialog = new DatePickerDialog(context, date);
        dialog.show();
    }
}
