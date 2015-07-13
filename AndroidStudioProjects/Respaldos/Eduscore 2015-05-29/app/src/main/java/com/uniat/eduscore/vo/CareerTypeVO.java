package com.uniat.eduscore.vo;

import android.database.Cursor;

import com.uniat.eduscore.interfaces.ICursorDecoder;

/**
 * Created by Admin on 23/04/2015.
 */
public class CareerTypeVO implements ICursorDecoder{

    private String idCareerType;
    private String name;
    private String status;

    public CareerTypeVO() {
    }

    public CareerTypeVO(String idCareerType, String name, String status) {
        this.idCareerType = idCareerType;
        this.name = name;
        this.status = status;
    }

    public String getIdCareerType() {
        return idCareerType;
    }

    public void setIdCareerType(String idCareerType) {
        this.idCareerType = idCareerType;
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
        setIdCareerType(pCursor.getString(1));
        setName(pCursor.getString(2));
        setStatus(pCursor.getString(3));
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public void setId(long pId) {

    }

}
