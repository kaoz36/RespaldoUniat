package com.university3dmx.examplemysql;

public class Familia {

    private int id;
    private String nombre;

    public Familia(int i, String n) {
        this.id = i;
        this.nombre = n;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // A�ade o actualiza datos de familias.
    public void update() {
        BDFamilia.updateFamilia(this);
    }

    // Borra datos de familia.
    // Importante, si se est� borrando desde una lista,
    // eliminar el objeto de la misma.
    public void delete() {
        BDFamilia.deleteFamilia(this);
    }
}

