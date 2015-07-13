package com.jaguarlabs

import org.springframework.dao.DataIntegrityViolationException

class AdminController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
   def beforeInterceptor = {
       if(!session.user)redirect(url:"/")
    }
    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [adminInstanceList: Admin.list(params), adminInstanceTotal: Admin.count()]
    }

    def create() {        
        [adminInstance: new Admin(params)]
    }

    def save() {
        def adminInstance = new Admin(params)
        if (!adminInstance.save(flush: true)) {
            render(view: "create", model: [adminInstance: adminInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'admin.label', default: 'Admin'), adminInstance.id])
        redirect(action: "show", id: adminInstance.id)
    }

    def show() {
        def adminInstance = Admin.get(params.id)
        if (!adminInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'admin.label', default: 'Admin'), params.id])
            redirect(action: "list")
            return
        }

        [adminInstance: adminInstance]
    }

    def edit() {
        def adminInstance = Admin.get(params.id)
        if (!adminInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'admin.label', default: 'Admin'), params.id])
            redirect(action: "list")
            return
        }

        [adminInstance: adminInstance]
    }

    def update() {
        def adminInstance = Admin.get(params.id)
        if (!adminInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'admin.label', default: 'Admin'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (adminInstance.version > version) {
                adminInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'admin.label', default: 'Admin')] as Object[],
                          "Another user has updated this Admin while you were editing")
                render(view: "edit", model: [adminInstance: adminInstance])
                return
            }
        }

        adminInstance.properties = params

        if (!adminInstance.save(flush: true)) {
            render(view: "edit", model: [adminInstance: adminInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'admin.label', default: 'Admin'), adminInstance.id])
        redirect(action: "show", id: adminInstance.id)
    }

    def delete() {
        def adminInstance = Admin.get(params.id)
        if (!adminInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'admin.label', default: 'Admin'), params.id])
            redirect(action: "list")
            return
        }

        try {
            adminInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'admin.label', default: 'Admin'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'admin.label', default: 'Admin'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
