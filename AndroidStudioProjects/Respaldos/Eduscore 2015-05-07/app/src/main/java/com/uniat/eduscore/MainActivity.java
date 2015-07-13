package com.uniat.eduscore;

import android.os.Bundle;

import com.uniat.eduscore.adapters.AdapterFormulario;
import com.uniat.eduscore.util.ExtendedFragmentActivity;
import com.university3dmx.eduscore.R;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

/**
 * Created by Admin on 06/04/2015.
 */
    public class MainActivity extends ExtendedFragmentActivity {

    static ViewPager vViewPager;
    AdapterFormulario adapterFormulario;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.activity_main);
        adapterFormulario = new AdapterFormulario(getSupportFragmentManager(), 3);
        vViewPager = (ViewPager) this.findViewById(R.id.pager);
        vViewPager.setAdapter(adapterFormulario);
        vViewPager.setCurrentItem(0);
    }
}
