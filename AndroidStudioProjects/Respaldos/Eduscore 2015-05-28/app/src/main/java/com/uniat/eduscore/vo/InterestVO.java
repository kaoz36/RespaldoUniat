package com.uniat.eduscore.vo;

import android.database.Cursor;

import com.uniat.eduscore.interfaces.ICursorDecoder;

/**
 * Created by Admin on 23/04/2015.
 */
public class InterestVO implements ICursorDecoder {

    private String idPerson;
    private String idCareer;

    public InterestVO() {
    }

    public InterestVO(String idPerson, String idCareer) {
        this.idPerson = idPerson;
        this.idCareer = idCareer;
    }

    public String getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(String idPerson) {
        this.idPerson = idPerson;
    }

    public String getIdCareer() {
        return idCareer;
    }

    public void setIdCareer(String idCareer) {
        this.idCareer = idCareer;
    }

    @Override
    public void decode(Cursor pCursor) {
        setIdPerson(pCursor.getString(0));
        setIdCareer(pCursor.getString(1));
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public void setId(long pId) {

    }

}
