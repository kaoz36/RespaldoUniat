package com.uniat.eduscore.util;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.university3dmx.eduscore.R;

/**
 * Created by Admin on 27/05/2015.
 */
public class ItemInterest {

    private String name;
    private ImageButton check;
    private TextView text;
    private boolean status;

    public ItemInterest(String name, ImageButton check, TextView text, boolean status) {
        this.name = name;
        this.check = check;
        this.text = text;
        this.status = status;
        event();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageButton getCheck() {
        return check;
    }

    public void setText(TextView text) {
        this.text = text;
    }

    public TextView getText() {
        return text;
    }

    public void setCheck(ImageButton check) {
        this.check = check;
    }


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void event(){
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status){
                    check.setImageResource(R.drawable.selector_check_interest_off);
                    status = false;
                }else{
                    check.setImageResource(R.drawable.selector_check_interest_on);
                    status = true;
                }
            }
        });

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status){
                    check.setImageResource(R.drawable.selector_check_interest_off);
                    status = false;
                }else{
                    check.setImageResource(R.drawable.selector_check_interest_on);
                    status = true;
                }
            }
        });
    }
}
