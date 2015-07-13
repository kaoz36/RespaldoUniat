package com.uniat.eduscore.vo;

import android.database.Cursor;

import com.uniat.eduscore.interfaces.ICursorDecoder;

/**
 * Created by Admin on 23/04/2015.
 */
public class CareerVO implements ICursorDecoder{

    private String idCareer;
    private String name;
    private String idCareerType;
    private String prefix;
    private String status;

    public CareerVO() {
    }

    public CareerVO(String idCareer, String name, String idCareerType, String prefix, String status) {
        this.idCareer = idCareer;
        this.name = name;
        this.idCareerType = idCareerType;
        this.prefix = prefix;
        this.status = status;
    }

    public String getIdCareer() {
        return idCareer;
    }

    public void setIdCareer(String idCareer) {
        this.idCareer = idCareer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCareerType() {
        return idCareerType;
    }

    public void setIdCareerType(String idCareerType) {
        this.idCareerType = idCareerType;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void decode(Cursor pCursor) {
        setIdCareer(pCursor.getString(1));
        setName(pCursor.getString(2));
        setIdCareerType(pCursor.getString(3));
        setPrefix(pCursor.getString(5));
        setStatus(pCursor.getString(4));
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public void setId(long pId) {

    }

}
