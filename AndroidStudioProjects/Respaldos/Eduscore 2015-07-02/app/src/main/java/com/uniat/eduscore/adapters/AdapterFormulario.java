package com.uniat.eduscore.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.ImageButton;

import com.uniat.eduscore.fragments.AdicionalesFragment;
import com.uniat.eduscore.fragments.PersonalesAdicionalesFragment;
import com.uniat.eduscore.fragments.PersonalesFragment;
import com.university3dmx.eduscore.R;

import static com.university3dmx.eduscore.R.id.imgBtnSelectedView1;

/**
 * Created by Admin on 06/04/2015.
 */
public class AdapterFormulario extends FragmentStatePagerAdapter {

    private int size;
    private ImageButton imgBtn1;
    private ImageButton imgBtn2;
    private ImageButton imgBtn3;

    public AdapterFormulario(FragmentManager fm, int size) {
        super(fm);
        this.size = size;
    }

    public void setImgBtns(ImageButton imgBtn1, ImageButton imgBtn2, ImageButton imgBtn3){
        this.imgBtn1 = imgBtn1;
        this.imgBtn2 = imgBtn2;
        this.imgBtn3 = imgBtn3;
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
