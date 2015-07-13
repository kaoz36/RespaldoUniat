package com.uniat.eduscore.com.uniat.eduscore.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.uniat.eduscore.com.uniat.eduscore.fragments.AdicionalesFragment;
import com.uniat.eduscore.com.uniat.eduscore.fragments.PersonalesAdicionalesFragment;
import com.uniat.eduscore.com.uniat.eduscore.fragments.PersonalesFragment;

/**
 * Created by Admin on 06/04/2015.
 */
public class AdapterFormulario extends FragmentStatePagerAdapter {

    private int size;

    public AdapterFormulario(FragmentManager fm, int size) {
        super(fm);
        this.size = size;
    }

    @Override
    public Fragment getItem(int i) {

        switch (i){
            case 0:
                return new PersonalesFragment();
            case 1:
                return new AdicionalesFragment();
            case 2:
                return new PersonalesAdicionalesFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String titulo = "";
        switch (position){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
        }
        return titulo;
    }


}
