package com.uniat.eduscore.com.uniat.eduscore.util;

import android.view.View;
import android.widget.EditText;

/**
 * Created by Admin on 06/04/2015.
 */
public class ExtendedClickListener implements View.OnClickListener{

    private EditText clearText;

    public ExtendedClickListener ( EditText clearText ){
        this.clearText = clearText;
    }

    @Override
    public void onClick(View v) {
        clearText.setText("");
    }
}
