package com.jaguarlabs

class Usuario {
    String nombreCompleto
    String numero
    String deviceId
    static constraints = {
        nombreCompleto()
        numero()
        deviceId()
    }
    String toString(){
        nombreCompleto
    }
}
