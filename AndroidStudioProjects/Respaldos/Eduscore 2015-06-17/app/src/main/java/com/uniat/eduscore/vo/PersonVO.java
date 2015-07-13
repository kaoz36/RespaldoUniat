package com.uniat.eduscore.vo;

import android.database.Cursor;

import com.uniat.eduscore.interfaces.ICursorDecoder;

/**
 * Created by Admin on 23/04/2015.
 */
public class PersonVO implements ICursorDecoder{

    private String idPerson;
    private String comments;
    private String idCharge;
    private String status;
    private String idCampus;

    public PersonVO() {
    }

    public PersonVO(String idPerson, String comments, String idCharge, String status, String idCampus) {
        this.idPerson = idPerson;
        this.comments = comments;
        this.idCharge = idCharge;
        this.status = status;
        this.idCampus = idCampus;
    }

    public String getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(String idPerson) {
        this.idPerson = idPerson;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getIdCharge() {
        return idCharge;
    }

    public void setIdCharge(String idCharge) {
        this.idCharge = idCharge;
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
        setIdPerson(pCursor.getString(1));
        setComments(pCursor.getString(2));
        setIdCharge(pCursor.getString(3));
        setStatus(pCursor.getString(4));
        setIdCampus(pCursor.getString(5));
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public void setId(long pId) {

    }

}
