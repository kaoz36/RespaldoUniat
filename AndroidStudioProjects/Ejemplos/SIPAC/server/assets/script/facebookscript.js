
/*Esta función la provee la API de FAcebook para poder establecer la conexion 
apropiada y asi obtener los datos*/
window.fbAsyncInit = function() {
					FB.init({
					  appId      : '132846406880696', 						 // Michel App ID
					  channelUrl : '//www.imds.me/channel.html',	 // Archivo de Canal, en caso de que falle la conexion
					  status     : true, 											// check login status
					  cookie     : true, 											// enable cookies to allow the server to access the session
					  xfbml      : true  											// parse XFBML
							});
					
						 FB.getLoginStatus(function(response) {
						 		/* En esta funcion se define las acciones cuando el login de Facebook se ejecuta, 
									si falla el login se dispara un evento de query para ejecutar la accion de desconexion */
							  if (response.status === 'connected') {
							  	getUserFacebookInfo (response.authResponse.userID);
							  } else if (response.status === 'not_authorized') {
								eventControl.eventDispatcher().trigger('facebookDisconnected');
							  } else {
							  	eventControl.eventDispatcher().trigger('facebookDisconnected');
							  }
							 });
					  };

/*Funciones de Login  y Logout de Portal */
function facebookLogin(){
	FB.login(function(response) {
			if (response.authResponse) {
				getUserFacebookInfo(response.authResponse.userID);
			} else {
				eventControl.eventDispatcher().trigger('facebookDisconnected');
			}
		},{scope: 'email'});
}

function facebookLogout(){
	FB.logout(function(response){
		eventControl.eventDispatcher().trigger('facebookDisconnected');
	});
}
   
function getUserFacebookInfo(pId){
	FB.api('/'+pId, function(response){	
			eventControl.eventDispatcher().trigger('facebookConnected',response);
			});
}

/* Funcion  reuqerida para conectarse con Facebook*/
function initFacebook(d){
	 var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
	 if (d.getElementById(id)) {return;}
	 js = d.createElement('script'); js.id = id; js.async = true;
	 js.src = "//connect.facebook.net/es_LA/all.js";
	 ref.parentNode.insertBefore(js, ref);
}



/* Start Point, Punto de arramque del portal ---------------------------------------------------------------------------*/
$(document).ready(function(){	       
			eventControl.addHandlers(document);
  			sectionControl.loadSections(function(){initFacebook(document);});
 });		   