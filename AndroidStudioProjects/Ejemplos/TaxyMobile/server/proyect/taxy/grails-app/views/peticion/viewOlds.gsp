
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
	</head>
	<body>
		<div  class="row" id="list-peticion" role="main">
                    <div class="span1"></div>
			 <h1>Historial</h1>                       
			<table class="table span10">
				<thead>
					<tr>
					
						<th><g:message code="peticion.id.label" default="Id" /></th>
                                                <th>Nombre</th>
                                                <th>Teléfono</th>
						<th>Localización</th>
						<th>Comentario</th>
						<!--<g:sortableColumn action="list" property="fecha" title="${message(code: 'peticion.fecha.label', default: 'Fecha')}" />-->
                                                <th class="span3"> Estado</th>
					
					</tr>
				</thead>
				<tbody>
                                  
				<g:each in="${peticionInstanceList}" status="i" var="peticionInstance">
					<tr>                               
						<td>${fieldValue(bean: peticionInstance, field: "id")}</td>
                                                <td><g:link controller="Usuario" action="show" id="${peticionInstance.usuario.id}">${fieldValue(bean: peticionInstance.usuario, field: "nombreCompleto")}</g:link></td>
                                                <td>${peticionInstance.usuario.numero}</td>
                                                <td><a href="https://maps.google.com/maps?q=${peticionInstance.locacion}"  target=”_new”><img src="${resource(dir: 'img', file: 'btn_pin.png')}"/></a></td>					
						<td>${fieldValue(bean: peticionInstance, field: "comentario")}</td>				
						<!--<td><g:formatDate format="MM/dd/yy" date="${peticionInstance.fecha}" /></td>-->
                                                <td>
                                                    ${fieldValue(bean: peticionInstance, field: "status")}
                                                </td>
					</tr>
				</g:each>
				</tbody>
			</table>
                         <div class="pagination">
				<g:paginate total="${peticionInstanceTotal}" params="[view:'olds']" />
			</div>
		</div>
	</body>
</html>
