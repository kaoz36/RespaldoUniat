package com.jaguarlabs

class Peticion {
    Usuario usuario 
    Zona zona
    String taxi
    String locacion
    String comentario
    String status="Nueva"
    Date fecha=new Date()
    Admin atendio
    Calificacion calificacion;
    boolean broma=false
    int minutos=0;
    static constraints = {
        usuario()
        locacion(nullable:true)
        fecha()
        status(inList:["Nueva","En Progreso","Asignada","Atendida","Cancelada","No Atendida"])
        taxi(nullable:true)
        comentario(nullable:true)
        atendio(nullable:true)
        calificacion(nullable:true)
    }
    String toString(){
        "${id}"
    }
    void cancel(){
        status="Cancelada"
    }
    void noService(){
        status="No Atendida"
    }
    void inProgress(){
        status="En Progreso"
    }
    void asignar(){
        status="Asignada"
    }
    void atendida(){
        status="Atendida"
    }
}
