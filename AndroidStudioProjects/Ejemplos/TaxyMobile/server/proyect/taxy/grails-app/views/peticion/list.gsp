
<%@ page import="com.jaguarlabs.Peticion" %>
<%@ page import="com.jaguarlabs.Usuario" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'peticion.label', default: 'Peticion')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
                 <!--<script language="javascript" type="text/javascript" src="${resource(dir:'js', file:'jquery.colorbox.js')}"></script>-->
                <script language="javascript" type="text/javascript" src="${resource(dir:'js', file:'jquery.colorbox-min.js')}"></script>
                <script type="text/javascript">// <![CDATA[
                  $(document).ready(function() {
                  $.ajaxSetup({ cache: false }); // This part addresses an IE bug. without it, IE will only load the first number and will never refresh
                  setInterval(function() {
                    $('#list-peticion').load('/taxi/Peticion/onlyList?view=${params.view}');
                    }, 5000); // the "3000" here refers to the time to refresh the div. it is in milliseconds.
                  });
           // ]]></script>
	</head>
	<body>
		<div  class="row" id="list-peticion" role="main">
                    <div class="span1"></div>
			 <h1>Nuevas</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
                       
			<table class="table span10">
				<thead>
					<tr>
					
						<!--<th><g:message code="peticion.id.label" default="ID" /></th>-->
                                                <th>Nombre</th>
                                                <th>Teléfono</th>
						<th>Localización</th>
						<th>Comentario</th>
                                                <th>Fecha</th>
                                                <th class="span3"> Acciones</th>
					
					</tr>
				</thead>
				<tbody>
                                  
				<g:each in="${peticionInstanceList}" status="i" var="peticionInstance">
                                   <g:if test="${peticionInstance.status=="Nueva"}">
					<tr>                               
						
                                                <td>${peticionInstance.id}<g:link controller="Usuario" action="show" id="${peticionInstance.usuario.id}">${fieldValue(bean: peticionInstance.usuario, field: "nombreCompleto")}</g:link></td>
                                                <td>${peticionInstance.usuario.numero}</td>
                                                <td><a href="https://maps.google.com/maps?q=${peticionInstance.locacion}"  target=”_new”><img src="${resource(dir: 'img', file: 'btn_pin.png')}"/></a></td>					
						<td>${fieldValue(bean: peticionInstance, field: "comentario")}</td>				
						<td><g:formatDate format="MM/dd  KK:mm a" date="${peticionInstance.fecha}" /></td>
                                                <td>
                                                    <g:link controller="Peticion" action="lockAsign" id="${peticionInstance.id}"> <img src="${resource(dir: 'img', file: 'btn_atender.png')}" Title="Tomar Petición"/></g:link>
                                                   <!-- <g:link controller="Peticion" action="Cancelar" id="${peticionInstance.id}"><img src="${resource(dir: 'img', file: 'btn_negar.png')}" Title="Cancelar Petición"/></g:link>-->
                                                </td>
					</tr>
                                        </g:if>
                                  <g:if test="${peticionInstance.status=="En Progreso" && peticionInstance.atendio==user}">
                                    <tr>                               
						
                                                <td><g:link controller="Usuario" action="show" id="${peticionInstance.usuario.id}">${fieldValue(bean: peticionInstance.usuario, field: "nombreCompleto")}</g:link></td>
                                                <td>${peticionInstance.usuario.numero}</td>
                                                <td><a href="https://maps.google.com/maps?q=${peticionInstance.locacion}"  target=”_new”><img src="${resource(dir: 'img', file: 'btn_pin.png')}" Title="Ver Coordenadas en mapa"/></a></td>					
						<td>${fieldValue(bean: peticionInstance, field: "comentario")}</td>				
						<td><g:formatDate format="MM/dd KK:mm a"  date="${peticionInstance.fecha}" /></td>
                                                <td>                                                    
                                                  <a id="attend${i}"><img src="${resource(dir: 'img', file: 'btn_yes.png')}" Title="Asignar Taxi"/></a>
                                                <script>
                                                    $(document).ready(function(){
                                                     $("#attend${i}").colorbox({
                                                         href:"${createLink(controller:"Mobile",action:"Asignando",id:"${peticionInstance.id}")}",
                                                         overlayClose:false,
                                                         width:"50%", 
                                                         height:"50%",
                                                         escKey:false,
                                                         iframe:true
                                                         });
                                                    });	 
                                                </script>
                                                <!--<g:link controller="Peticion" action="Cancelar" id="${peticionInstance.id}"><img src="${resource(dir: 'img', file: 'btn_negar.png')}" Title="Cancelar Petición"/></g:link>-->
                                                <g:link controller="Peticion" action="revertPeticion" id="${peticionInstance.id}"><img src="${resource(dir: 'img', file: 'btn_cancelar.png')}" Title="Omitir Petición"/></g:link>
                                                </td>
					</tr>
                                  </g:if>
				</g:each>
				</tbody>
			</table>
                        
                        <h1>Pendientes</h1>
                    
                        <table class="table span10">
				<thead>
					<tr>
					
						<!--<th><g:message code="peticion.id.label" default="Id" /></th>-->
                                                
                                                <th >Nombre</th>
                                                <th> teléfono</th>
						<th >Localización</th>
					
                                                <th>Comentario</th>
                                                <th>Fecha</th>
						<th  >Taxi</th>
                                                <th  >Acciones</th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${peticionInstanceList}" status="i" var="peticionInstance">
                                    <g:if test="${peticionInstance.status=="Asignada" && peticionInstance.atendio==user}">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                                                <!--<td>${fieldValue(bean: peticionInstance, field: "id")}</td>-->
                                                <td><g:link controller="Usuario" action="show" id="${peticionInstance.usuario.id}">${fieldValue(bean: peticionInstance.usuario, field: "nombreCompleto")}</g:link></td>
                                                <td>${peticionInstance.usuario.numero}</td>
						<td><a href="https://maps.google.com/maps?q=${peticionInstance.locacion}"  target=”_new”><img src="${resource(dir: 'img', file: 'btn_pin.png')}"/></a></td>
					
						<td>${fieldValue(bean: peticionInstance, field: "comentario")}</td>
                                                <td><g:formatDate format="MM/dd  KK:mm a" date="${peticionInstance.fecha}" /></td>
						<td>${fieldValue(bean: peticionInstance, field: "taxi")}</td>
                                                <td>
                                                  
                                                <g:link controller="Peticion" action="Atendida" id="${peticionInstance.id}"><img src="${resource(dir: 'img', file: 'btn_yes.png')}" title="Ya ha sido Atendida"/></g:link>
                                                <g:link controller="Peticion" action="Cancelar" id="${peticionInstance.id}"><img src="${resource(dir: 'img', file: 'btn_no.png')}" title="Cancelar Petición"/></g:link>
                                                </td>
					</tr>
                                        </g:if>
				</g:each>
				</tbody>
			</table>
		</div>
	</body>
</html>