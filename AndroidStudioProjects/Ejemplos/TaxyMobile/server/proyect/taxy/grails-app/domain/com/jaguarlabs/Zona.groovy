package com.jaguarlabs

class Zona {
    String nombre
    static constraints = {
        nombre(notEqual:"",nullable:false,size: 2..25,blank:false)
    }
    String toString(){
       nombre       
    }
}
