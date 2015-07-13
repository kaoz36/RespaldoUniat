package com.jaguarlabs



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Admin)
class AdminTests {

    void testSomething() {
      Admin admin=new Admin();
      admin.zona=new Zona();
        
      admin.usuario="efren"
      admin.password="mypass"
      admin.nombreCompleto="lalalalallalalalalalallalala"
      if(admin.save()) 
        println "guardado"
      else
       fail " no se guardo"
    }
}
 