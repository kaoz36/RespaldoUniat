package com.uniat.eduscore.com.uniat.eduscore.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.university3dmx.eduscore.R;

/**
 * Created by Admin on 10/04/2015.
 */
public class DismissDialog extends Dialog implements View.OnClickListener {

    public DismissDialog(Context context){
        super(context);
    }

    public void dismiss(View v){
        Animation dismissAnim = AnimationUtils.loadAnimation(getContext(), R.anim.dialog_dismiss);
        dismissAnim.reset();
        dismissAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.clearAnimation();
        v.startAnimation(dismissAnim);
    }

    @Override
    public void onClick(View v) {
    }
}
