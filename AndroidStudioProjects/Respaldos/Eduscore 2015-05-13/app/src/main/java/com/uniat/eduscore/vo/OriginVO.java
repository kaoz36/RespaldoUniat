package com.uniat.eduscore.vo;

import android.database.Cursor;

import com.uniat.eduscore.interfaces.ICursorDecoder;

/**
 * Created by Admin on 23/04/2015.
 */
public class OriginVO implements ICursorDecoder {

    private String idOrigen;
    private String name;
    private String type;
    private String status;
    private String idCampus;

    public OriginVO() {
    }

    public OriginVO(String idOrigen, String name, String type, String status, String idCampus) {
        this.idOrigen = idOrigen;
        this.name = name;
        this.type = type;
        this.status = status;
        this.idCampus = idCampus;
    }

    public String getIdOrigen() {
        return idOrigen;
    }

    public void setIdOrigen(String idOrigen) {
        this.idOrigen = idOrigen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        setIdOrigen(pCursor.getString(0));
        setName(pCursor.getString(1));
        setType(pCursor.getString(2));
        setStatus(pCursor.getString(3));
        setIdCampus(pCursor.getString(4));
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public void setId(long pId) {

    }

}
