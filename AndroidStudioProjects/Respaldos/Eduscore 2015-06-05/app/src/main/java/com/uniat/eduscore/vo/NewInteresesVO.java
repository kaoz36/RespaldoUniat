package com.uniat.eduscore.vo;

import android.database.Cursor;

import com.uniat.eduscore.interfaces.ICursorDecoder;

/**
 * Created by Admin on 03/06/2015.
 */
public class NewInteresesVO implements ICursorDecoder {

    private String idInteres;
    private String idRegistro;
    private String idCareer;

    public NewInteresesVO() {
    }

    public NewInteresesVO(String idInteres, String idRegistro, String idCareer) {
        this.idInteres = idInteres;
        this.idRegistro = idRegistro;
        this.idCareer = idCareer;
    }

    public String getIdInteres() {
        return idInteres;
    }

    public void setIdInteres(String idInteres) {
        this.idInteres = idInteres;
    }

    public String getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(String idRegistro) {
        this.idRegistro = idRegistro;
    }

    public String getIdCareer() {
        return idCareer;
    }

    public void setIdCareer(String interes) {
        this.idCareer = interes;
    }

    @Override
    public void decode(Cursor pCursor) {
        setIdInteres(pCursor.getString(0));
        setIdRegistro(pCursor.getString(1));
        setIdCareer(pCursor.getString(2));
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
        return getIdRegistro() + ", " + getIdCareer();
    }
}
