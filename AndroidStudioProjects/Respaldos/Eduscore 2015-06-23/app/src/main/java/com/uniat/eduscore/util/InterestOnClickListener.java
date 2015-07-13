package com.uniat.eduscore.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.uniat.eduscore.dialogs.DatePickerDialog;
import com.uniat.eduscore.dialogs.InterestDialog;

/**
 * Created by Admin on 13/04/2015.
 */
public class InterestOnClickListener implements View.OnClickListener {

    private TextView interests;
    private Context context;

    public InterestOnClickListener(TextView interests, Context context){
        this.interests = interests;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        InterestDialog dialog = new InterestDialog(context, interests);
        dialog.show();
    }
}
