package com.jaguarlabs



import org.junit.*
import grails.test.mixin.*

@TestFor(AdminController)
@Mock(Admin)
class AdminControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/admin/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.adminInstanceList.size() == 0
        assert model.adminInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.adminInstance != null
    }

    void testSave() {
        controller.save()

        assert model.adminInstance != null
        assert view == '/admin/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/admin/show/1'
        assert controller.flash.message != null
        assert Admin.count() == 1
    }

    void testShow() {
        controller.params.id=1
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/admin/list'


        populateValidParams(params)
        def admin = new Admin(params)

        assert admin.save() != null

        params.id = admin.id

        def model = controller.show()

        assert model.adminInstance == admin
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/admin/list'


        populateValidParams(params)
        def admin = new Admin(params)

        assert admin.save() != null

        params.id = admin.id

        def model = controller.edit()

        assert model.adminInstance == admin
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/admin/list'

        response.reset()


        populateValidParams(params)
        def admin = new Admin(params)

        assert admin.save() != null

        // test invalid parameters in update
        params.id = admin.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/admin/edit"
        assert model.adminInstance != null

        admin.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/admin/show/$admin.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        admin.clearErrors()

        populateValidParams(params)
        params.id = admin.id
        params.version = -1
        controller.update()

        assert view == "/admin/edit"
        assert model.adminInstance != null
        assert model.adminInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/admin/list'

        response.reset()

        populateValidParams(params)
        def admin = new Admin(params)

        assert admin.save() != null
        assert Admin.count() == 1

        params.id = admin.id

        controller.delete()

        assert Admin.count() == 0
        assert Admin.get(admin.id) == null
        assert response.redirectedUrl == '/admin/list'
    }
}
