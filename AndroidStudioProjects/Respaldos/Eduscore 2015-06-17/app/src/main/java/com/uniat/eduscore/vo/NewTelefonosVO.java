package com.uniat.eduscore.vo;

import android.database.Cursor;

import com.uniat.eduscore.interfaces.ICursorDecoder;

/**
 * Created by Admin on 03/06/2015.
 */
public class NewTelefonosVO implements ICursorDecoder{

    private String idTelefono;
    private String idRegistro;
    private String telefono;
    private String tipo;

    public NewTelefonosVO() {
    }

    public NewTelefonosVO(String idTelefono, String idRegistro, String telefono, String tipo) {
        this.idTelefono = idTelefono;
        this.idRegistro = idRegistro;
        this.telefono = telefono;
        this.tipo = tipo;
    }

    public String getIdTelefono() {
        return idTelefono;
    }

    public void setIdTelefono(String idTelefono) {
        this.idTelefono = idTelefono;
    }

    public String getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(String idRegistro) {
        this.idRegistro = idRegistro;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public void decode(Cursor pCursor) {
        setIdTelefono(pCursor.getString(0));
        setIdRegistro(pCursor.getString(1));
        setTelefono(pCursor.getString(2));
        setTipo(pCursor.getString(3));
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
        return getIdRegistro() + ", " + getTelefono() + ", " + getTipo();
    }

    public String getInsert(String idPerson){
        return idPerson + ", '" + getTelefono() + "', '" + getTipo() + "'";
    }

}
