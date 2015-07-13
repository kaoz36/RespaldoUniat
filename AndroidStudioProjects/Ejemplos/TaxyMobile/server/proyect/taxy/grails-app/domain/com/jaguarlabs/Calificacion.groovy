package com.jaguarlabs

class Calificacion {
    String comentario;
    float calification=0;
    static constraints = {
        comentario(nullable:true)
        calification()
    }
}
