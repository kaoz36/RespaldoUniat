package com.university3dmx.responsehandlergetpost;

import android.database.Cursor;

/**
 * Created by Admin on 23/04/2015.
 */
public class EmailVO implements ICursorDecoder {

    private String idEmail;
    private String idPerson;
    private String email;

    public EmailVO() {
    }

    public EmailVO(String idEmail, String idPerson, String email) {
        this.idEmail = idEmail;
        this.idPerson = idPerson;
        this.email = email;
    }

    public String getIdEmail() {
        return idEmail;
    }

    public void setIdEmail(String idEmail) {
        this.idEmail = idEmail;
    }

    public String getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(String idPerson) {
        this.idPerson = idPerson;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void decode(Cursor pCursor) {
        setIdEmail(pCursor.getString(0));
        setIdPerson(pCursor.getString(1));
        setEmail(pCursor.getString(2));
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public void setId(long pId) {

    }

}