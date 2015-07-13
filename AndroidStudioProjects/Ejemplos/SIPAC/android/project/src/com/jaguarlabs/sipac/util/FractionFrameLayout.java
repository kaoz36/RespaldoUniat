package com.jaguarlabs.sipac.util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

public class FractionFrameLayout extends LinearLayout {

	
	
	public FractionFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// Log.i("FractionFrameLayout","Fraction Framelayout");
	}
	
	public FractionFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		//Log.i("FractionFrameLayout","Fraction Framelayout");
	}
	
	public FractionFrameLayout(Context context) {
		super(context);
		//Log.i("FractionFrameLayout","Fraction Framelayout");
	}

	public float getXFraction() {  
        final int width = getWidth();  
         // Log.i("FractionFrameLayout","x: "+getX()+" width: "+getWidth());
        if(width != 0)  
        {  
             return getX() / getWidth();  
        }  
        else  
        {  
             return getX();  
        }  
   }  

   public void setXFraction(float xFraction) {      
        final int width = getWidth();  
        setX((width > 0) ? (xFraction * width) : -9999);  
   }  

}
