package com.uniat.eduscore;

import android.os.Bundle;

import com.uniat.eduscore.adapters.AdapterFormulario;
import com.uniat.eduscore.model.BackupModel;
import com.uniat.eduscore.util.ExtendedFragmentActivity;
import com.university3dmx.eduscore.R;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Admin on 06/04/2015.
 */
    public class MainActivity extends ExtendedFragmentActivity {

    static ViewPager vViewPager;
    AdapterFormulario adapterFormulario;
    private ImageButton imgBtn1;
    private ImageButton imgBtn2;
    private ImageButton imgBtn3;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.activity_main);
        adapterFormulario = new AdapterFormulario(getSupportFragmentManager(), 3);
        imgBtn1 = (ImageButton) findViewById(R.id.imgBtnSelectedView1);
        imgBtn2 = (ImageButton) findViewById(R.id.imgBtnSelectedView2);
        imgBtn3 = (ImageButton) findViewById(R.id.imgBtnSelectedView3);
        adapterFormulario.setImgBtns(imgBtn1, imgBtn2, imgBtn3);
        vViewPager = (ViewPager) this.findViewById(R.id.pager);
        vViewPager.setAdapter(adapterFormulario);
        vViewPager.setCurrentItem(0);
        vViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        imgBtn1.setImageResource(R.drawable.selector_btn_select_on);
                        imgBtn2.setImageResource(R.drawable.selector_btn_select_off);
                        imgBtn3.setImageResource(R.drawable.selector_btn_select_off);
                        break;
                    case 1:
                        imgBtn1.setImageResource(R.drawable.selector_btn_select_off);
                        imgBtn2.setImageResource(R.drawable.selector_btn_select_on);
                        imgBtn3.setImageResource(R.drawable.selector_btn_select_off);
                        break;
                    case 2:
                        imgBtn1.setImageResource(R.drawable.selector_btn_select_off);
                        imgBtn2.setImageResource(R.drawable.selector_btn_select_off);
                        imgBtn3.setImageResource(R.drawable.selector_btn_select_on);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void onClickActualizar(View pView){
        BackupModel.getInstance().setContextActivity(this);

        if (!wifiOn) {
            getMessageBuilder().setMessage("Es necesario tener una conexion WIFI Activa para reaizar este proceso");
            getMessageBuilder().create().show();
            return;
        }

        BackupModel.getInstance().startBackup();
    }

    public void onClickSelectedView(View pView){
        switch (pView.getId()){
            case R.id.imgBtnSelectedView1:
                imgBtn1.setImageResource(R.drawable.selector_btn_select_on);
                imgBtn2.setImageResource(R.drawable.selector_btn_select_off);
                imgBtn3.setImageResource(R.drawable.selector_btn_select_off);
                vViewPager.setCurrentItem(0);
                break;
            case R.id.imgBtnSelectedView2:
                imgBtn1.setImageResource(R.drawable.selector_btn_select_off);
                imgBtn2.setImageResource(R.drawable.selector_btn_select_on);
                imgBtn3.setImageResource(R.drawable.selector_btn_select_off);
                vViewPager.setCurrentItem(1);
                break;
            case R.id.imgBtnSelectedView3:
                imgBtn1.setImageResource(R.drawable.selector_btn_select_off);
                imgBtn2.setImageResource(R.drawable.selector_btn_select_off);
                imgBtn3.setImageResource(R.drawable.selector_btn_select_on);
                vViewPager.setCurrentItem(2);
                break;
        }
    }

}
