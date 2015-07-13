package com.jaguarlabs

class Admin {
    Zona zona
    String usuario
    String password
    String nombreCompleto
    static constraints = {
        zona()
        nombreCompleto(blank:false)
        usuario(blank:false)
        password(blank:false)
    }
}
