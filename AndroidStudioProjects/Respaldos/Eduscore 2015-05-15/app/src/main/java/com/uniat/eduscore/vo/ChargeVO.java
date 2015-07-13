package com.uniat.eduscore.vo;

import android.database.Cursor;

import com.uniat.eduscore.interfaces.ICursorDecoder;

/**
 * Created by Admin on 23/04/2015.
 */
public class ChargeVO implements ICursorDecoder {

    private String idCharge;
    private String name;
    private String status;

    public ChargeVO() {
    }

    public ChargeVO(String idCharge, String name, String status) {
        this.idCharge = idCharge;
        this.name = name;
        this.status = status;
    }

    public String getIdCharge() {
        return idCharge;
    }

    public void setIdCharge(String idCharge) {
        this.idCharge = idCharge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void decode(Cursor pCursor) {
        setIdCharge(pCursor.getString(0));
        setName(pCursor.getString(1));
        setStatus(pCursor.getString(2));
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public void setId(long pId) {

    }

}
