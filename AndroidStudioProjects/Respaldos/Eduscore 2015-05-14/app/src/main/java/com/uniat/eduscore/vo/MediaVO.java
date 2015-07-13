package com.uniat.eduscore.vo;

import android.database.Cursor;

import com.uniat.eduscore.interfaces.ICursorDecoder;

/**
 * Created by Admin on 23/04/2015.
 */
public class MediaVO implements ICursorDecoder {

    private String idMedia;
    private String name;
    private String status;
    private String idCampus;

    public MediaVO() {
    }

    public MediaVO(String idMedia, String name, String status, String idCampus) {
        this.idMedia = idMedia;
        this.name = name;
        this.status = status;
        this.idCampus = idCampus;
    }

    public String getIdMedia() {
        return idMedia;
    }

    public void setIdMedia(String idMedia) {
        this.idMedia = idMedia;
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

    public String getIdCampus() {
        return idCampus;
    }

    public void setIdCampus(String idCampus) {
        this.idCampus = idCampus;
    }

    @Override
    public void decode(Cursor pCursor) {
        setIdMedia(pCursor.getString(0));
        setName(pCursor.getString(1));
        setStatus(pCursor.getString(2));
        setIdCampus(pCursor.getString(3));
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public void setId(long pId) {

    }

}
