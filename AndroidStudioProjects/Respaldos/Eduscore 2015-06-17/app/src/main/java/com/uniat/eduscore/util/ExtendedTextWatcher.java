package com.uniat.eduscore.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Admin on 06/04/2015.
 */
public class ExtendedTextWatcher implements TextWatcher {

    private ImageButton imgButton;

    public ExtendedTextWatcher( ImageButton imgButton ){
        this.imgButton = imgButton;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if( s.length() > 0 ){
            imgButton.setVisibility(View.VISIBLE);
        }else{
            imgButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {}
}
