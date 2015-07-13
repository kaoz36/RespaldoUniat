package com.uniat.eduscore.vo;

import android.database.Cursor;

import com.uniat.eduscore.interfaces.ICursorDecoder;

/**
 * Created by Admin on 23/04/2015.
 */
public class CityVO implements ICursorDecoder {

    private String idCity;
    private String idRegion;
    private String name;

    public CityVO() {
    }

    public CityVO(String idCity, String idRegion, String name) {
        this.idCity = idCity;
        this.idRegion = idRegion;
        this.name = name;
    }

    public String getIdCity() {
        return idCity;
    }

    public void setIdCity(String idCity) {
        this.idCity = idCity;
    }

    public String getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(String idRegion) {
        this.idRegion = idRegion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void decode(Cursor pCursor) {
        setIdCity(pCursor.getString(0));
        setIdRegion(pCursor.getString(1));
        setName(pCursor.getString(2));
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public void setId(long pId) {

    }

}
