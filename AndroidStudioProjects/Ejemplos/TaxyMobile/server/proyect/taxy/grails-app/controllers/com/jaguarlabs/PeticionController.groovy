package com.jaguarlabs

import org.springframework.dao.DataIntegrityViolationException

class PeticionController {
    def beforeInterceptor = {
       if(!session.user)redirect(url:"/")
    }
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

   /* def index() {
        redirect(action: "list", params: params)
    }*/

    def list() {
        println params
       params.max = Math.min(params.max ? params.int('max') : 10, 100)
       def petitionList=null;
         int id;
         int user_id
       try{
         id=session?.sitio?.toInteger()
         user_id=session.user_id
       }catch(Exception e){
           render("Error from session")
       }
       Admin user=Admin.get(user_id)
       if(session.user_id!=0) {       
        Zona zona=Zona.get(id)
        petitionList=Peticion.findAll(sort:"id",order: "desc"){
            zona==zona &&
            status!= "Cancelada" &&
            status!= "Atendida" &&
            status!= "No Atendida"
        }
       }else{
         petitionList=Peticion.list(sort:"id",order: "desc");
       }
       [peticionInstanceList: petitionList,user:user]
    }

    def create() {
        [peticionInstance: new Peticion(params)]
    }

    def save() {
        def peticionInstance = new Peticion(params)
        if (!peticionInstance.save(flush: true)) {
            render(view: "create", model: [peticionInstance: peticionInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'peticion.label', default: 'Peticion'), peticionInstance.id])
        redirect(action: "show", id: peticionInstance.id)
    }

    def show() {
        def peticionInstance = Peticion.get(params.id)
        if (!peticionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'peticion.label', default: 'Peticion'), params.id])
            redirect(action: "list")
            return
        }

        [peticionInstance: peticionInstance]
    }

    def edit() {
        def peticionInstance = Peticion.get(params.id)
        if (!peticionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'peticion.label', default: 'Peticion'), params.id])
            redirect(action: "list")
            return
        }

        [peticionInstance: peticionInstance]
    }

    def update() {
        def peticionInstance = Peticion.get(params.id)
         println "updating ${peticionInstance}"
        if (!peticionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'peticion.label', default: 'Peticion'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (peticionInstance.version > version) {
                peticionInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'peticion.label', default: 'Peticion')] as Object[],
                          "Another user has updated this Peticion while you were editing")
                render(view: "edit", model: [peticionInstance: peticionInstance])
                return
            }
        }

        peticionInstance.properties = params

        if (!peticionInstance.save(flush: true)) {
            render(view: "edit", model: [peticionInstance: peticionInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'peticion.label', default: 'Peticion'), peticionInstance.id])
        redirect(action: "show", id: peticionInstance.id)
    }
    
    def delete() {
        def peticionInstance = Peticion.get(params.id)
        if (!peticionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'peticion.label', default: 'Peticion'), params.id])
            redirect(action: "list")
            return
        }

        try {
            peticionInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'peticion.label', default: 'Peticion'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'peticion.label', default: 'Peticion'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
    def Atendida(){
       int id=params.id.toInteger();
       def petition=Peticion.get(id)
       println params
       if(petition!=null){
           println petition
        if(petition.status=="Asignada"){
         petition.atendida();
         petition.save(flush:true,failOnError:true);
        }
       }
       redirect(action:"list")
    }
     def Cancelar(){
       println "${params}"
       int id=params.id.toInteger();
       def petition=Peticion.get(id)
       println petition
       if(petition!=null){    
        if(petition.status=="Asignada"){
         petition.cancel();
         petition.save(flush:true,failOnError:true);
        }
       }
       redirect(action:"list")
    }
    def onlyList(){
       params.max = Math.min(params.max ? params.int('max') : 10, 100)
       def petitionList=null;
         int id;
         int user_id
       try{
         id=session?.sitio?.toInteger()
         user_id=session.user_id
       }catch(Exception e){
          render("error in session")
       }
        Admin user=Admin.get(user_id)
       if(session.user_id!=0) {       
        Zona zona=Zona.get(id)
        println "looking for sitio:${zona}"
        petitionList=Peticion.findAll(sort:"id",order: "desc"){
            zona==zona &&
            status!= "Cancelada" &&
            status!= "Atendida" &&
            status!= "No Atendida"
        }
       }else{
         petitionList=Peticion.list(sort:"id",order: "desc");
       }
      // println "${petitionList}"
       [peticionInstanceList: petitionList,user:user]
    }
    def viewComments(){
        println session
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
       def petitionList=null;
         int id;
       int user_id
       try{
         id=session?.sitio?.toInteger()
         user_id=session.user_id
       }catch(Exception e){
           return
       }
       int total=0
       Admin user=Admin.get(user_id)
       if(session.user!="jaguar") {       
        Zona zona=Zona.get(id)
        println "looking for sitio:${zona}"
        petitionList=Peticion.findAll(params){
            zona==zona &&
            calificacion!=null&&
            atendio==user
        }
        total=Peticion.findAll(){
            zona==zona &&
            calificacion!=null && 
            atendio==user
        }.size();
       }else{
         petitionList=Peticion.findAll(params){
            calificacion!=null
        }
        total=Peticion.findAll(){
            calificacion!=null
        }.size();
       }
       
       [peticionInstanceList: petitionList,user:user,peticionInstanceListTotal:total]
    }
    
    def viewOlds(){
       println params
       params.max = Math.min(params.max ? params.int('max') : 5, 100)
       def petitionList=null;
       def petitionListcount=null;
       int id;
       int user_id
       try{
         id=session?.sitio?.toInteger()
         user_id=session.user_id
       }catch(Exception e){
          // redirect(url:"/")
       }   
       Admin user=Admin.get(user_id)
       if(session.user!="jaguar") {       
        Zona zona=Zona.get(id)
        petitionList=Peticion.findAll(params){
            (status=="Cancelada"||
            status=="Atendida" ||
            status=="No Atendida") && 
            atendio==user
        }
        petitionListcount=Peticion.findAll(){
            (status=="Cancelada"||
            status=="Atendida" ||
            status=="No Atendida") &&
          atendio==user
        }
       }else{
         petitionList=Peticion.findAll(params){
            (status=="Cancelada"||
            status=="Atendida" ||
            status=="No Atendida" )
        }
        petitionListcount=Peticion.findAll(){
            (status=="Cancelada"||
            status=="Atendida" ||
            status=="No Atendida" )
        }
       }
        [peticionInstanceList: petitionList, peticionInstanceTotal: petitionListcount.size(),view:"olds"]
    }
     def revertPeticion(){
        println params
        println request.JSON
        int peticion=params?.id.toInteger();
        Peticion pet=Peticion.get(peticion);
        if(pet.status=="En Progreso"){
          pet.atendio=null
          pet.status="Nueva"
          pet.save(flush:true,failOnError:true);
           [peticionInstance:pet]
        }
        println pet
        redirect(url:"/")        
    }
     def lockAsign(){
        println params
        println request.JSON
        int peticion=params?.id.toInteger();
        Admin user=Admin.get(session.user_id)
        Peticion pet=Peticion.get(peticion);
        if(pet.status=="Nueva"){
          pet.atendio=user
          pet.inProgress();
          pet.save(flush:true);
           [peticionInstance:pet]
        }
        redirect(url:"/")
    }
}
