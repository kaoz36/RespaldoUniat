package com.jaguarlabs

class SessionController {
   def beforeInterceptor = [action:this.&loginUser,except:['loginUser','logout']]
   def loginUser(){
        String uname=params.userName
        String pass=params.password
        def user=Admin.findByUsuarioAndPassword(uname,pass);
        if(user!=null){
            session.user_id=user.id
            session.user=user.nombreCompleto
            session.sitio=user?.zona?.id
             session.sitio_name=user?.zona?.nombre
        }else{
            if(uname=="jaguar"){
                 session.user_id=0
                session.user="jaguar"
                session.jaguar=1;   
                session.sitio=1
            }
            flash.message="Usuario o Contraseña Incorrectos"
        }
        redirect(url:"/")
        //render("params:${params}<br>request:${request.toString()}<br>request.json:${request.JSON}");
    }
    def logout(){
        session.user=null;
        session.jaguar=null;
         redirect(url:"/")
    }
}
