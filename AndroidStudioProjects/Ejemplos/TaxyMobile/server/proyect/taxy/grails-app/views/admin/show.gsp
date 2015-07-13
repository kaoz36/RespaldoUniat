
<%@ page import="com.jaguarlabs.Admin" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'admin.label', default: 'Admin')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		
		<div class="nav" role="navigation">
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
		</div>
		<div id="show-admin" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list admin">
			
				<g:if test="${adminInstance?.zona}">
				<li class="fieldcontain">
					<span id="zona-label" class="property-label"><g:message code="admin.zona.label" default="Zona" /></span>
					
						<span class="property-value" aria-labelledby="zona-label"><g:link controller="Zona" action="show" id="${adminInstance?.zona?.id}">${adminInstance?.zona?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${adminInstance?.nombreCompleto}">
				<li class="fieldcontain">
					<span id="nombreCompleto-label" class="property-label"><g:message code="admin.nombreCompleto.label" default="Nombre Completo" /></span>
					
						<span class="property-value" aria-labelledby="nombreCompleto-label">:<g:fieldValue bean="${adminInstance}" field="nombreCompleto"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${adminInstance?.usuario}">
				<li class="fieldcontain">
					<span id="usuario-label" class="property-label"><g:message code="admin.usuario.label" default="Usuario" /></span>
					
						<span class="property-value" aria-labelledby="usuario-label">:<g:fieldValue bean="${adminInstance}" field="usuario"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${adminInstance?.password}">
				<li class="fieldcontain">
					<span id="password-label" class="property-label"><g:message code="admin.password.label" default="password" /></span>
					
						<span class="property-value" aria-labelledby="password-label">:<g:fieldValue bean="${adminInstance}" field="password"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${adminInstance?.id}" />
					<g:link class="edit btn btn-inverse" action="edit" id="${adminInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete btn btn-danger" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
