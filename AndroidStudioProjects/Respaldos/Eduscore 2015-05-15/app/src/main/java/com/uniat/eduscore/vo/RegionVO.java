package com.uniat.eduscore.vo;

import android.database.Cursor;

import com.uniat.eduscore.interfaces.ICursorDecoder;

/**
 * Created by Admin on 23/04/2015.
 */
public class RegionVO implements ICursorDecoder {

    private String idRegion;
    private String idContry;
    private String name;

    public RegionVO() {
    }

    public RegionVO(String idRegion, String idContry, String name) {
        this.idRegion = idRegion;
        this.idContry = idContry;
        this.name = name;
    }

    public String getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(String idRegion) {
        this.idRegion = idRegion;
    }

    public String getIdContry() {
        return idContry;
    }

    public void setIdContry(String idContry) {
        this.idContry = idContry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void decode(Cursor pCursor) {
        setIdRegion(pCursor.getString(0));
        setIdContry(pCursor.getString(1));
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
