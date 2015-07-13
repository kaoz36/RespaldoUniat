package com.university3dmx.basura;

import java.io.Serializable;

/**
 * Created by Admin on 03/06/2015.
 */
public class MyObject implements Serializable {

    private static final long serialVersionUID = 425356831899738508L;

    private int intgr;
    private String strg;

    public int getIntgr() {

        return intgr;
    }

    public void setIntgr(int intgr) {

        this.intgr = intgr;
    }

    public String getStrg() {

        return strg;
    }

    public void setStrg(String strg) {

        this.strg = strg;
    }

}