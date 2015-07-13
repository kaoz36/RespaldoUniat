import com.jaguarlabs.*;
class BootStrap {
    def init = { servletContext ->
        Zona zona=new Zona(nombre:"Guadalajara");
        zona.save(flush:true,failOnError:true)
        Zona zona1=new Zona(nombre:"Monterrey");
        zona1.save(flush:true,failOnError:true)
        Zona zona2=new Zona(nombre:"DF");
        zona2.save(flush:true,failOnError:true)
    }
    def destroy = {
    }
}
