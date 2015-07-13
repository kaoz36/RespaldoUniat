package com.jaguarlabs

import org.springframework.dao.DataIntegrityViolationException

class UsuarioController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def beforeInterceptor = {
       if(!session.user)redirect(url:"/")
    }
   /* def index() {
        redirect(action: "list", params: params)
    }*/

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [usuarioInstanceList: Usuario.list(params), usuarioInstanceTotal: Usuario.count()]
    }

    def create() {
        [usuarioInstance: new Usuario(params)]
    }

    def save() {
        def usuarioInstance = new Usuario(params)
        if (!usuarioInstance.save(flush: true)) {
            render(view: "create", model: [usuarioInstance: usuarioInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'usuario.label', default: 'Usuario'), usuarioInstance.id])
        redirect(action: "show", id: usuarioInstance.id)
    }

    def show() {
        def usuarioInstance = Usuario.get(params.id)
        if (!usuarioInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'usuario.label', default: 'Usuario'), params.id])
            redirect(action: "list")
            return
        }
        def peticionListAtendida=Peticion.findAll(){
            usuario==usuarioInstance &&
            status=="Atendida"
        }
        def peticionListCancelada=Peticion.findAll(){
            usuario==usuarioInstance &&
            status=="Cancelada"
        }
        def peticionListNa=Peticion.findAll(){
            usuario==usuarioInstance &&
            status=="No Atendida"
        }
        int atendidas=peticionListAtendida.size()
        int canceladas=peticionListCancelada.size()
        int noatendidas=peticionListNa.size()
        [usuarioInstance: usuarioInstance,atendidas:atendidas,canceladas:canceladas,noatendidas:noatendidas]
    }

    def edit() {
        def usuarioInstance = Usuario.get(params.id)
        if (!usuarioInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'usuario.label', default: 'Usuario'), params.id])
            redirect(action: "list")
            return
        }

        [usuarioInstance: usuarioInstance]
    }

    def update() {
        def usuarioInstance = Usuario.get(params.id)
        if (!usuarioInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'usuario.label', default: 'Usuario'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (usuarioInstance.version > version) {
                usuarioInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'usuario.label', default: 'Usuario')] as Object[],
                          "Another user has updated this Usuario while you were editing")
                render(view: "edit", model: [usuarioInstance: usuarioInstance])
                return
            }
        }

        usuarioInstance.properties = params

        if (!usuarioInstance.save(flush: true)) {
            render(view: "edit", model: [usuarioInstance: usuarioInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'usuario.label', default: 'Usuario'), usuarioInstance.id])
        redirect(action: "show", id: usuarioInstance.id)
    }

    def delete() {
        def usuarioInstance = Usuario.get(params.id)
        if (!usuarioInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'usuario.label', default: 'Usuario'), params.id])
            redirect(action: "list")
            return
        }

        try {
            usuarioInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'usuario.label', default: 'Usuario'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'usuario.label', default: 'Usuario'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
