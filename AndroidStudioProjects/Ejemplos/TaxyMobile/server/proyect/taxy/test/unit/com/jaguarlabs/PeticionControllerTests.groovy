package com.jaguarlabs



import org.junit.*
import grails.test.mixin.*

@TestFor(PeticionController)
@Mock(Peticion)
class PeticionControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/peticion/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.peticionInstanceList.size() == 0
        assert model.peticionInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.peticionInstance != null
    }

    void testSave() {
        controller.save()

        assert model.peticionInstance != null
        assert view == '/peticion/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/peticion/show/1'
        assert controller.flash.message != null
        assert Peticion.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/peticion/list'


        populateValidParams(params)
        def peticion = new Peticion(params)

        assert peticion.save() != null

        params.id = peticion.id

        def model = controller.show()

        assert model.peticionInstance == peticion
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/peticion/list'


        populateValidParams(params)
        def peticion = new Peticion(params)

        assert peticion.save() != null

        params.id = peticion.id

        def model = controller.edit()

        assert model.peticionInstance == peticion
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/peticion/list'

        response.reset()


        populateValidParams(params)
        def peticion = new Peticion(params)

        assert peticion.save() != null

        // test invalid parameters in update
        params.id = peticion.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/peticion/edit"
        assert model.peticionInstance != null

        peticion.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/peticion/show/$peticion.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        peticion.clearErrors()

        populateValidParams(params)
        params.id = peticion.id
        params.version = -1
        controller.update()

        assert view == "/peticion/edit"
        assert model.peticionInstance != null
        assert model.peticionInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/peticion/list'

        response.reset()

        populateValidParams(params)
        def peticion = new Peticion(params)

        assert peticion.save() != null
        assert Peticion.count() == 1

        params.id = peticion.id

        controller.delete()

        assert Peticion.count() == 0
        assert Peticion.get(peticion.id) == null
        assert response.redirectedUrl == '/peticion/list'
    }
}
