package com.uniat.eduscore.vo;

import android.database.Cursor;

import com.uniat.eduscore.interfaces.ICursorDecoder;

/**
 * Created by Admin on 23/04/2015.
 */
public class UserVO implements ICursorDecoder{

    private String idUser;
    private String user;
    private String password;
    private String idPerson;

    public UserVO() {
    }

    public UserVO(String idUser, String user, String password, String idPerson) {
        this.idUser = idUser;
        this.user = user;
        this.password = password;
        this.idPerson = idPerson;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(String idPerson) {
        this.idPerson = idPerson;
    }

    @Override
    public void decode(Cursor pCursor) {
        setIdUser(pCursor.getString(1));
        setUser(pCursor.getString(2));
        setPassword(pCursor.getString(3));
        setIdPerson(pCursor.getString(4));
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public void setId(long pId) {

    }

}
