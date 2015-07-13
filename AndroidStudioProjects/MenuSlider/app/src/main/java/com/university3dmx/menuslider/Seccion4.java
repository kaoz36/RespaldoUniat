package com.university3dmx.menuslider;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by user on 26/08/2014.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Seccion4 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.seccion4, container, false);

        return rootView;
    }
}
