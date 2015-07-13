package com.jaguarlabs



import org.junit.*
import grails.test.mixin.*

@TestFor(ZonaController)
@Mock(Zona)
class ZonaControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/zona/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.zonaInstanceList.size() == 0
        assert model.zonaInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.zonaInstance != null
    }

    void testSave() {
        controller.save()

        assert model.zonaInstance != null
        assert view == '/zona/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/zona/show/1'
        assert controller.flash.message != null
        assert Zona.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/zona/list'

        populateValidParams(params)
        def zona = new Zona(params)

        assert zona.save() != null

        params.id = zona.id

        def model = controller.show()

        assert model.zonaInstance == zona
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/zona/list'

        populateValidParams(params)
        def zona = new Zona(params)

        assert zona.save() != null

        params.id = zona.id

        def model = controller.edit()

        assert model.zonaInstance == zona
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/zona/list'

        response.reset()

        populateValidParams(params)
        def zona = new Zona(params)

        assert zona.save() != null

        // test invalid parameters in update
        params.id = zona.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/zona/edit"
        assert model.zonaInstance != null

        zona.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/zona/show/$zona.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        zona.clearErrors()

        populateValidParams(params)
        params.id = zona.id
        params.version = -1
        controller.update()

        assert view == "/zona/edit"
        assert model.zonaInstance != null
        assert model.zonaInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/zona/list'

        response.reset()

        populateValidParams(params)
        def zona = new Zona(params)

        assert zona.save() != null
        assert Zona.count() == 1

        params.id = zona.id

        controller.delete()

        assert Zona.count() == 0
        assert Zona.get(zona.id) == null
        assert response.redirectedUrl == '/zona/list'
    }
}
