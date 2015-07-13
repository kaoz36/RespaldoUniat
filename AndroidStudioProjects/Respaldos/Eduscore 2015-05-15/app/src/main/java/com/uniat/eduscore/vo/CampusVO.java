package com.uniat.eduscore.vo;

import android.database.Cursor;

import com.uniat.eduscore.interfaces.ICursorDecoder;

/**
 * Created by Admin on 23/04/2015.
 */
public class CampusVO implements ICursorDecoder {

    private String idCampus;
    private String name;
    private String address;
    private String status;
    private String idContry;
    private String idRegion;
    private String idCity;
    private String nameAlt;

    public CampusVO() {
    }

    public String getIdCampus() {
        return idCampus;
    }

    public void setIdCampus(String idCampus) {
        this.idCampus = idCampus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdContry() {
        return idContry;
    }

    public void setIdContry(String idContry) {
        this.idContry = idContry;
    }

    public String getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(String idRegion) {
        this.idRegion = idRegion;
    }

    public String getIdCity() {
        return idCity;
    }

    public void setIdCity(String idCity) {
        this.idCity = idCity;
    }

    public String getNameAlt() {
        return nameAlt;
    }

    public void setNameAlt(String nameAlt) {
        this.nameAlt = nameAlt;
    }

    public CampusVO(String idCampus, String name, String address, String status, String idContry, String idRegion, String idCity, String nameAlt) {

        this.idCampus = idCampus;
        this.name = name;
        this.address = address;
        this.status = status;
        this.idContry = idContry;
        this.idRegion = idRegion;
        this.idCity = idCity;
        this.nameAlt = nameAlt;
    }

    @Override
    public void decode(Cursor pCursor) {
        setIdCampus(pCursor.getString(0));
        setName(pCursor.getString(1));
        setAddress(pCursor.getString(2));
        setStatus(pCursor.getString(3));
        setIdContry(pCursor.getString(4));
        setIdRegion(pCursor.getString(5));
        setIdCity(pCursor.getString(6));
        setNameAlt(pCursor.getString(7));

    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public void setId(long pId) {

    }

}
