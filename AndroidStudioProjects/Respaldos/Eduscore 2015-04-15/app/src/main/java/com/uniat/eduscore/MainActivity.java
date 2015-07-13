package com.uniat.eduscore;

import android.os.Bundle;

import com.uniat.eduscore.com.uniat.eduscore.adapters.AdapterFormulario;
import com.university3dmx.eduscore.R;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

/**
 * Created by Admin on 06/04/2015.
 */
    public class MainActivity extends FragmentActivity {

    static ViewPager vViewPager;
    AdapterFormulario adapterFormulario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapterFormulario = new AdapterFormulario(getSupportFragmentManager(), 3);
        vViewPager = (ViewPager) this.findViewById(R.id.pager);
        vViewPager.setAdapter(adapterFormulario);
        vViewPager.setCurrentItem(0);
    }
}
