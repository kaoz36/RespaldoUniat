package com.uniat.eduscore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.uniat.eduscore.util.ICallback;
import com.uniat.eduscore.util.Task;
import com.university3dmx.eduscore.R;


public class SplashActivity extends Activity implements ICallback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Task t = new Task( this );
        t.execute();
    }

    @Override
    public void callback() {
        final ImageView img = (ImageView) findViewById(R.id.imageSplash);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.splash_out );
        anim.setAnimationListener( new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent( getApplicationContext(), LoginActivity.class );
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        anim.setFillAfter(true);
        img.startAnimation(anim);
    }

    @Override
    public void time() {
        try {
            Thread.sleep( 2000 );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
