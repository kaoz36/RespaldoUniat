package com.uniat.eduscore.vo;

import android.database.Cursor;

import com.uniat.eduscore.interfaces.ICursorDecoder;

/**
 * Created by Admin on 23/04/2015.
 */
public class PersonDescriptionVO implements ICursorDecoder {

    private String idPerson;
    private String name;
    private String lastName;
    private String idCountry;
    private String idRegion;
    private String idCity;
    private String gender;
    private String curp;
    private String rfc;
    private String street;
    private String numberExt;
    private String numberInt;
    private String zip;

    public PersonDescriptionVO() {
    }

    public PersonDescriptionVO(String idPerson, String name, String lastName, String idCountry, String idRegion, String idCity, String gender, String curp, String rfc, String street, String numberExt, String numberInt, String zip) {
        this.idPerson = idPerson;
        this.name = name;
        this.lastName = lastName;
        this.idCountry = idCountry;
        this.idRegion = idRegion;
        this.idCity = idCity;
        this.gender = gender;
        this.curp = curp;
        this.rfc = rfc;
        this.street = street;
        this.numberExt = numberExt;
        this.numberInt = numberInt;
        this.zip = zip;
    }

    public String getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(String idPerson) {
        this.idPerson = idPerson;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdCountry() {
        return idCountry;
    }

    public void setIdCountry(String idCountry) {
        this.idCountry = idCountry;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumberExt() {
        return numberExt;
    }

    public void setNumberExt(String numberExt) {
        this.numberExt = numberExt;
    }

    public String getNumberInt() {
        return numberInt;
    }

    public void setNumberInt(String numberInt) {
        this.numberInt = numberInt;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Override
    public void decode(Cursor pCursor) {
        setIdPerson(pCursor.getString(0));
        setName(pCursor.getString(1));
        setLastName(pCursor.getString(2));
        setIdCountry(pCursor.getString(3));
        setIdRegion(pCursor.getString(4));
        setIdCity(pCursor.getString(5));
        setGender(pCursor.getString(6));
        setCurp(pCursor.getString(7));
        setRfc(pCursor.getString(8));
        setStreet(pCursor.getString(9));
        setNumberExt(pCursor.getString(10));
        setNumberInt(pCursor.getString(11));
        setZip(pCursor.getString(12));
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public void setId(long pId) {

    }

}
