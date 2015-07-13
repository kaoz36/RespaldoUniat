package com.jaguarlabs

import org.springframework.dao.DataIntegrityViolationException

class ZonaController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [zonaInstanceList: Zona.list(params), zonaInstanceTotal: Zona.count()]
    }

    def create() {
        [zonaInstance: new Zona(params)]
    }

    def save() {
        def zonaInstance = new Zona(params)
        if (!zonaInstance.save(flush: true)) {
            render(view: "create", model: [zonaInstance: zonaInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'zona.label', default: 'Zona'), zonaInstance.id])
        redirect(action: "show", id: zonaInstance.id)
    }

    def show(Long id) {
        def zonaInstance = Zona.get(id)
        if (!zonaInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'zona.label', default: 'Zona'), id])
            redirect(action: "list")
            return
        }

        [zonaInstance: zonaInstance]
    }

    def edit(Long id) {
        def zonaInstance = Zona.get(id)
        if (!zonaInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'zona.label', default: 'Zona'), id])
            redirect(action: "list")
            return
        }

        [zonaInstance: zonaInstance]
    }

    def update(Long id, Long version) {
        def zonaInstance = Zona.get(id)
        if (!zonaInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'zona.label', default: 'Zona'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (zonaInstance.version > version) {
                zonaInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'zona.label', default: 'Zona')] as Object[],
                          "Another user has updated this Zona while you were editing")
                render(view: "edit", model: [zonaInstance: zonaInstance])
                return
            }
        }

        zonaInstance.properties = params

        if (!zonaInstance.save(flush: true)) {
            render(view: "edit", model: [zonaInstance: zonaInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'zona.label', default: 'Zona'), zonaInstance.id])
        redirect(action: "show", id: zonaInstance.id)
    }

    def delete(Long id) {
        def zonaInstance = Zona.get(id)
        if (!zonaInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'zona.label', default: 'Zona'), id])
            redirect(action: "list")
            return
        }

        try {
            zonaInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'zona.label', default: 'Zona'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'zona.label', default: 'Zona'), id])
            redirect(action: "show", id: id)
        }
    }
}
