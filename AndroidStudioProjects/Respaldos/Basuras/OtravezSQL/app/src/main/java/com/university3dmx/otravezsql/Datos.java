package com.university3dmx.otravezsql;

/**
 * Created by Admin on 20/04/2015.
 */
public class Datos {
    private int id;
    private String name;
    private String lastName;

    public Datos() {
    }

    public Datos(int id, String name, String lastName) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
