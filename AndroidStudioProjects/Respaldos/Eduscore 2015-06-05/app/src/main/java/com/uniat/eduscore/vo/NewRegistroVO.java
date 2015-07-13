package com.uniat.eduscore.vo;

import android.database.Cursor;

import com.uniat.eduscore.interfaces.ICursorDecoder;

import java.util.ArrayList;

/**
 * Created by Admin on 01/06/2015.
 */
public class NewRegistroVO implements ICursorDecoder{

    private String idRegistro;
    private String nombre;
    private String apellidos;
    private String tipoInteres;
    private String horarios;
    private String medio;
    private String lugar;
    private String evento;
    private String probabilidad;
    private String paisResidencia;
    private String estadoResidencia;
    private String ciudadResidencia;
    private String calle;
    private String numExt;
    private String numInt;
    private String colonia;
    private String cp;
    private String curp;
    private String paisOrigen;
    private String estadoOrigen;
    private String ciudadOrigen;
    private String fechaNacimiento;

    public NewRegistroVO() {
    }

    public NewRegistroVO(String nombre, String apellidos) {
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    public void setIdRegistro(String idRegistro) {
        this.idRegistro = idRegistro;
    }

    public String getIdRegistro() {
        return idRegistro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTipoInteres() {
        return tipoInteres;
    }

    public void setTipoInteres(String tipoInteres) {
        this.tipoInteres = tipoInteres;
    }

    public String getHorarios() {
        return horarios;
    }

    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }

    public String getMedio() {
        return medio;
    }

    public void setMedio(String medio) {
        this.medio = medio;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(String probabilidad) {
        this.probabilidad = probabilidad;
    }

    public String getPaisResidencia() {
        return paisResidencia;
    }

    public void setPaisResidencia(String paisResidencia) {
        this.paisResidencia = paisResidencia;
    }

    public String getEstadoResidencia() {
        return estadoResidencia;
    }

    public void setEstadoResidencia(String estadoResidencia) {
        this.estadoResidencia = estadoResidencia;
    }

    public String getCiudadResidencia() {
        return ciudadResidencia;
    }

    public void setCiudadResidencia(String ciudadResidencia) {
        this.ciudadResidencia = ciudadResidencia;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumExt() {
        return numExt;
    }

    public void setNumExt(String numExt) {
        this.numExt = numExt;
    }

    public String getNumInt() {
        return numInt;
    }

    public void setNumInt(String numInt) {
        this.numInt = numInt;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public String getEstadoOrigen() {
        return estadoOrigen;
    }

    public void setEstadoOrigen(String estadoOrigen) {
        this.estadoOrigen = estadoOrigen;
    }

    public String getCiudadOrigen() {
        return ciudadOrigen;
    }

    public void setCiudadOrigen(String ciudadOrigen) {
        this.ciudadOrigen = ciudadOrigen;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    @Override
    public void decode(Cursor pCursor) {
        setIdRegistro(pCursor.getString(0));
        setNombre(pCursor.getString(1));
        setApellidos(pCursor.getString(2));
        setTipoInteres(pCursor.getString(3));
        setHorarios(pCursor.getString(4));
        setMedio(pCursor.getString(5));
        setLugar(pCursor.getString(6));
        setEvento(pCursor.getString(7));
        setProbabilidad(pCursor.getString(8));
        setPaisResidencia(pCursor.getString(9));
        setEstadoResidencia(pCursor.getString(10));
        setCiudadResidencia(pCursor.getString(11));
        setCalle(pCursor.getString(12));
        setNumExt(pCursor.getString(13));
        setNumInt(pCursor.getString(14));
        setColonia(pCursor.getString(15));
        setCp(pCursor.getString(16));
        setCurp(pCursor.getString(17));
        setPaisOrigen(pCursor.getString(18));
        setEstadoResidencia(pCursor.getString(19));
        setCiudadResidencia(pCursor.getString(20));
        setFechaNacimiento(pCursor.getString(21));
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
        return "'" + getIdRegistro() + "', '" + getNombre() + "', '" + getApellidos() + "', '" + getTipoInteres() + "', '" + getHorarios() +
                "', '" + getMedio() + "', '" + getLugar() + "', '" + getEvento() + "', '" + getProbabilidad() +
                "', '" + getPaisResidencia() + "', '" + getEstadoResidencia() + "', '" + getCiudadResidencia() +
                "', '" + getCalle() + "', '" + getNumExt() + "', '" + getNumInt() + "', '" + getColonia() +
                "', '" + getCp() + "', '" + getCurp() + "', '" + getPaisOrigen() + "', '" + getEstadoOrigen() +
                "', '" + getCiudadOrigen() + "', '" + getFechaNacimiento() + "'";
    }
}
