package com.uniat.eduscore.vo;

import android.database.Cursor;

import com.uniat.eduscore.interfaces.ICursorDecoder;

/**
 * Created by Admin on 23/04/2015.
 */
public class CountryVO implements ICursorDecoder {

    private String idContry;
    private String name;

    public CountryVO() {
    }

    public CountryVO(String idContry, String name) {
        this.idContry = idContry;
        this.name = name;
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
        setIdContry(pCursor.getString(0));
        setName(pCursor.getString(1));
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public void setId(long pId) {

    }

}
