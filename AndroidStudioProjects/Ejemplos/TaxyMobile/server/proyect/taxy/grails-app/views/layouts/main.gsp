<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>Taxi Plis!</title><!-- validate if is loged -->
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
		<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
		
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'mobile.css')}" type="text/css">
                <link rel="stylesheet" href="${resource(dir: 'css', file: 'colorbox.css')}" type="text/css">
                <link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap.min.css')}">
                <link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap-responsive.min.css')}">
                <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">
                <script src="${resource(dir: 'js/vendor', file: 'modernizr-2.6.2-respond-1.1.0.min.js')}"></script>
                <!--<script language="javascript" type="text/javascript" src="${resource(dir:'js', file:'jquery.min.js')}"></script>-->
                <!--<g:javascript library="jquery" />   -->
                <r:layoutResources />
		<g:layoutHead/>   
	</head>
        <body>
          <div id="wrap">
            <div class="navbar navbar-inverse navbar-fixed-top">
                  <div class="navbar-inner">
                      <div class="container">
                          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                              <span class="icon-bar"></span>
                              <span class="icon-bar"></span>
                              <span class="icon-bar"></span>
                          </a>
                          <a href="${createLink(uri: '/')}" class="brand"><img src="${resource(dir: 'img', file: 'logo.png')}" /></a>
                          <div class="nav-collapse collapse">
                              <ul class="nav">
                                <g:if test="${session.user!=null}">
                                  <g:if test="${params.view!=null}">
                                    <li><a href="${createLink(uri: '/')}">Nuevo</a></li>
                                  </g:if>
                                  <g:else>
                                     <li class="active"><a href="${createLink(uri: '/')}">Nuevo</a></li>
                                  </g:else>
                                  <g:if test="${params.view=="olds"}">
                                    <li class="active"><g:link controller="Peticion" action="viewOlds" params="[view: 'olds']">historial</g:link></li>
                                   </g:if>
                                  <g:else>
                                    <li><g:link controller="Peticion" action="viewOlds" params="[view:'olds']">historial</g:link></li>
                                  </g:else>
                                  <g:if test="${params.view=="rate"}">
                                    <li class="active"><g:link controller="Calificacion" action="list" params="[view: 'rate']">Comentarios</g:link> </li>
                                   </g:if>
                                  <g:else>
                                    <li><g:link controller="Peticion" action="viewComments" params="[view: 'rate']">Comentarios</g:link> </li>
                                  </g:else>
                                <g:if test="${session.jaguar!=null}"> 
                                  <li><g:link class="buttons" controller="Admin" action="list">Administradores</g:link></li>
                                  <li><g:link class="buttons" controller="Zona" action="list">Zonas</g:link> </li>
                                </g:if>
                                
                                  <li class="logout" >
                                     <a href="${createLink(uri: '/Session/logout')}" onclick="return confirm('${message(code: 'default.button.exit.confirm.message', default: 'Are you sure?')}');">
                                     <i class="icon-off icon-white"></i>Salir</a>
                                  </li>
                                
                                </g:if>
                              </ul>  
                             <g:if test="${session.user!=null}">
                               <ul  style="position: absolute;right: 0px;">
                              <li><a >${session?.user} en ${session?.sitio_name}</a></li>
                            </ul>
                             </g:if>
                          </div><!--/.nav-collapse -->
                      </div>
                  </div>

              </div>
            <div class="container">
           <!-- <g:if test="${session.user!=null}">
                <div id="navmenu" class="navmenu" role="navigation">   
                  <a class="buttons" href="${createLink(uri: '/')}">Home</a> 
                  <g:link class="buttons" controller="Usuario" action="list">Usuarios</g:link> 
                  <g:if test="${session.jaguar!=null}">
                    <g:link class="buttons" controller="Admin" action="list">Administradores</g:link>
                    <g:link class="buttons" controller="Zona" action="list">Zonas</g:link> 
                  </g:if> 
                </div>
            </g:if>  -->
        
	  <g:layoutBody/>
            </div>
            <div id="push"></div>
        </div>
        <div id="footer">
            <div class="container">
                <p>&copy; <a href="http://www.jaguarlabs.com" target="_blank">Jaguar Labs</a> 2013</p>
            </div>
        </div>
       <!-- <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="js/vendor/jquery-1.9.1.min.js"><\/script>')</script>-->
        <script src="${resource(dir: 'js/vendor', file: 'bootstrap.min.js')}"></script>
        <script src="${resource(dir: 'js', file: 'main.js')}"></script>
        <g:javascript library="application"/>
        <r:layoutResources />
	</body>
</html>