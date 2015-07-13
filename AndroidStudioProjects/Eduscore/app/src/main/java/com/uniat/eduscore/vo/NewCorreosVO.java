package com.uniat.eduscore.vo;

import android.database.Cursor;

import com.uniat.eduscore.interfaces.ICursorDecoder;

/**
 * Created by Admin on 03/06/2015.
 */
public class NewCorreosVO implements ICursorDecoder {

    private String idCorreo;
    private String idRegistro;
    private String correo;

    public NewCorreosVO() {
    }

    public NewCorreosVO(String idCorreo, String idRegistro, String correo) {
        this.idCorreo = idCorreo;
        this.idRegistro = idRegistro;
        this.correo = correo;
    }

    public String getIdCorreo() {
        return idCorreo;
    }

    public void setIdCorreo(String idCorreo) {
        this.idCorreo = idCorreo;
    }

    public String getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(String idRegistro) {
        this.idRegistro = idRegistro;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public void decode(Cursor pCursor) {
        setIdCorreo(pCursor.getString(0));
        setIdRegistro(pCursor.getString(1));
        setCorreo(pCursor.getString(2));
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public void setId(long pId) {

    }

    @Override
    public String toString() {
        return "'" + getIdRegistro() + "', '" + getCorreo() + "'";
    }

    public String getInsert(String idPerson){
        return idPerson + ", '" + getCorreo() + "'";
    }
}
