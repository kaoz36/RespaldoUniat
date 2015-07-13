
<%@ page import="com.jaguarlabs.Peticion" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'peticion.label', default: 'Peticion')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="show-peticion" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
                        <center>
			<ol class="property-list peticion">
			
				<g:if test="${peticionInstance?.usuario}">
				<li class="fieldcontain">
					<span id="usuario-label" class="property-label"><g:message code="peticion.usuario.label" default="Usuario" /></span>
					
						<span class="property-value" aria-labelledby="usuario-label"><g:link controller="usuario" action="show" id="${peticionInstance?.usuario?.id}">${peticionInstance?.usuario?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${peticionInstance?.locacion}">
				<li class="fieldcontain">
					<span id="locacion-label" class="property-label"><g:message code="peticion.locacion.label" default="Locacion" /></span>
					
						<span class="property-value" aria-labelledby="locacion-label"><g:fieldValue bean="${peticionInstance}" field="locacion"/></span>
					
				</li>
				</g:if>
			
			
				<g:if test="${peticionInstance?.fecha}">
				<li class="fieldcontain">
					<span id="fecha-label" class="property-label"><g:message code="peticion.fecha.label" default="Fecha" /></span>
					
						<span class="property-value" aria-labelledby="fecha-label"><g:formatDate date="${peticionInstance?.fecha}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${peticionInstance?.status}">
				<li class="fieldcontain">
					<span id="status-label" class="property-label"><g:message code="peticion.status.label" default="Status" /></span>
					
						<span class="property-value" aria-labelledby="status-label"><g:fieldValue bean="${peticionInstance}" field="status"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${peticionInstance?.taxi}">
				<li class="fieldcontain">
					<span id="taxi-label" class="property-label"><g:message code="peticion.taxi.label" default="Taxi" /></span>
					
						<span class="property-value" aria-labelledby="taxi-label"><g:fieldValue bean="${peticionInstance}" field="taxi"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${peticionInstance?.comentario}">
				<li class="fieldcontain">
					<span id="comentario-label" class="property-label"><g:message code="peticion.comentario.label" default="Comentario" /></span>
					
						<span class="property-value" aria-labelledby="comentario-label"><g:fieldValue bean="${peticionInstance}" field="comentario"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${peticionInstance?.atendio}">
				<li class="fieldcontain">
					<span id="atendio-label" class="property-label"><g:message code="peticion.atendio.label" default="Atendio" /></span>
					
						<span class="property-value" aria-labelledby="atendio-label"><g:link controller="admin" action="show" id="${peticionInstance?.atendio?.id}">${peticionInstance?.atendio?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${peticionInstance?.broma}">
				<li class="fieldcontain">
					<span id="broma-label" class="property-label"><g:message code="peticion.broma.label" default="Broma" /></span>
					
						<span class="property-value" aria-labelledby="broma-label"><g:formatBoolean boolean="${peticionInstance?.broma}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${peticionInstance?.zona}">
				<li class="fieldcontain">
					<span id="zona-label" class="property-label"><g:message code="peticion.zona.label" default="Zona" /></span>
					
						<span class="property-value" aria-labelledby="zona-label"><g:link controller="Zona" action="show" id="${peticionInstance?.zona?.id}">${peticionInstance?.zona?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
                          </center>
		</div>
	</body>
</html>
