package com.jaguarlabs

import grails.converters.*

class MobileController {
 static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def createPeticion(){
        println request.JSON
        int userId=request.JSON.userId.toInteger();
        int sitioId=request.JSON.sitio.toInteger();
        String location=request.JSON.location
        String comentario=request.JSON.comentario
        println "${userId} ${sitioId}"
        Zona zona=Zona.get(sitioId)
        Usuario user=Usuario.get(userId)
        Peticion pet=new Peticion(
                 usuario:user,
                 zona:zona,
                 locacion:location,
                 comentario:comentario
            )
          
        if(pet.save(flush:true,failOnError:true)){
            println "success save"
          render("{\"success\":\"true\",\"id\":\"${pet.id}\"}");
        }else{
             println "not save"
           render("{\"success\":\"false\"}")
        }
        
    }
    def cancelPeticion(){
        println params
        println request.JSON
        int peticion=request.JSON.peticion.toInteger()
        int user=request.JSON.userId.toInteger()
        try{
            Peticion pet=Peticion.get(peticion);
            if(pet.status=="Nueva"||pet.status=="En Progreso"||pet.status=="Asignada"){
                pet.cancel();
                if(pet.save(flush:true,failOnError:true)){
                    render("{\"success\":\"true\",\"id\":\"${pet.id}\",\"status\":\"${pet.status}\"}");
                }else{
                    render("{\"success\":\"false\"}")
                }   
            }else{
                render("{\"success\":\"true\",\"id\":\"${pet.id}\",\"status\":\"${pet.status}\"}");
            }
        }catch(Exception e){
             render("{\"success\":\"false\"}")
        }
              
    }
    def register(){
        println params
        println request.JSON
        String nombre=request.JSON.nombre
        String numero=request.JSON.numero
        String deviceId=request.JSON.DeviceId
        println "${nombre} ${numero} ${deviceId}"
        Usuario user=Usuario.findByDeviceId(deviceId);
        if(user==null){
            user=new Usuario(nombreCompleto:nombre,numero:numero,deviceId:deviceId);
            if(user.save(flush:true,failOnError:true)){
                println "success created"
                render("{\"success\":\"true\",\"id\":\"${user.id}\"}")
            }else{
                println "fail created"
                render("{\"success\":\"false\"}")
            }   
        }else{
            user.nombreCompleto=nombre
            user.numero=numero            
            if(user.save(flush:true,failOnError:true)){
                println "success updated"
                render("{\"success\":\"true\",\"id\":\"${user.id}\"}")
            }else{
                println "fail updated"
                render("{\"success\":\"false\"}")
            }
        }              
    }
    def noService(){
        println params
        println request.JSON  
        try{
            int peticion=request.JSON.peticion.toInteger()
            int userId=request.JSON.userId.toInteger()
            Peticion pet=Peticion.get(peticion);
            if(pet.status=="Asignada"){
                pet.noService();
                if(pet.save(flush:true,failOnError:true)){
                    render("{\"success\":\"true\",\"id\":\"${pet.id}\",\"status\":\"${pet.status}\"}");
                }else{
                    render("{\"success\":\"false\"}")
                }  
            }else{
                 render("{\"success\":\"true\",\"id\":\"${pet.id}\",\"status\":\"${pet.status}\"}");
            }            
        }catch(Exception e){
             render("{\"success\":\"false\"}")
        }
              
    }
    def atendido(){ 
        println params
        println request.JSON
        try{
           int user=request.JSON.userId.toInteger()
           int peticion=request.JSON.peticion.toInteger()
            Peticion pet=Peticion.get(peticion);
            if(pet.status=="Asignada"){
                pet.atendida();
                if(pet.save(flush:true,failOnError:true)){
                    render("{\"success\":\"true\",\"id\":\"${pet.id}\",\"status\":\"${pet.status}\"}");
                }else{
                    render("{\"success\":\"false\"}")
                }    
            }else{
                 render("{\"success\":\"false\",\"id\":\"${pet.id}\",\"status\":\"${pet.status}\"}");
            }
        }catch(Exception e){
            render("{\"success\":\"false\"}")
        }
              
    }
    def update(){
        int id= request.JSON.peticion.toInteger()
        Peticion pet=Peticion.get(id)
        String location=request.JSON.location
        if(pet!=null){
            if(location!=null){
                pet.locacion=location
                pet.save(flush:true);
            }
             render("{\"success\":\"true\",\"id\":\"${pet.id}\",\"status\":\"${pet.status}\",\"taxi\":\"${pet.taxi}\",\"tiempo\":\"${pet.minutos}\"}");
        }else{
             render("{\"success\":\"false\"}");
        }
    }
    def rateservice(){
        println request.JSON
        try{
           int user=request.JSON.userId.toInteger()
           int peticion=request.JSON.peticion.toInteger()
           def rate=request.JSON.rate.toFloat()
           String coment=request.JSON.coment.toString()
           Peticion pet=Peticion.get(peticion);
           println "${coment} ${rate}"
           pet.calificacion=new Calificacion(comentario:coment,calification:rate);
           pet.calificacion.save(flush:true);
           if(pet.save(flush:true,failOnError:true)){
               println "corect save"  
               render("{\"success\":\"true\",\"id\":\"${pet.id}\",\"status\":\"${pet.status}\"}")       
            }else{   
                println "no calif saved"
                render("{\"success\":\"false\"}")
            }         
        }catch(Exception e){
            println e
            render("{\"success\":\"false\"}")
        }
    }
    def Asignando(){
        println params
        println request.JSON
        int peticion=params?.id.toInteger();
        Peticion pet=Peticion.get(peticion);
        pet.inProgress();
        pet.save(flush:true);
        [peticionInstance:pet]
    }
    def updateAsignando() {
        println params
        String taxi=params.taxi
        String min=params.minutos
        println "${taxi} ${min}"
        def  peticionInstance = Peticion.get(params.id)
        try{
            int minutos= min.toInteger()   
            println "updating ${peticionInstance}"
            peticionInstance.minutos=minutos
            peticionInstance.taxi=taxi
            peticionInstance.asignar();
            println "asignando ${peticionInstance}"
         }catch(Exception e){
            println e
         }
         if (!peticionInstance.save(flush: true)) {
            render("peticion no actualizada");
            return
         }         
        render("peticion actualizada")
    }
    def getZoneList(){
        def list=Zona.list().collect(){
            [id:it.id,
            nombre:it.nombre]
        }
       println list
        render(contentType:"text/json"){
            result=list
        }
    }  
   
}
