package com.uniat.eduscore.vo;

import android.database.Cursor;

import com.uniat.eduscore.interfaces.ICursorDecoder;

/**
 * Created by Admin on 23/04/2015.
 */
public class EventVO implements ICursorDecoder {

    private String idEvent;
    private String name;
    private String status;
    private String idCampus;

    public EventVO() {
    }

    public EventVO(String idEvent, String name, String status, String idCampus) {
        this.idEvent = idEvent;
        this.name = name;
        this.status = status;
        this.idCampus = idCampus;
    }

    public String getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
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
        setIdEvent(pCursor.getString(1));
        setName(pCursor.getString(2));
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
