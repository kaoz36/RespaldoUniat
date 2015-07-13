<%@ page import="com.jaguarlabs.Admin" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'admin.label', default: 'Admin')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
		<div class="nav" role="navigation">
			<ul>
				<li><g:link class="list" action="list">Lista de Administradores</g:link></li>
			</ul>
		</div>
		<div id="create-admin" class="content scaffold-create" role="main">
			<h1>Crear Administrador</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${adminInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${adminInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form action="save" class="form-horizontal panel-white" >
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<div class="control-group">
                                          <div class="controls">
                                            <g:submitButton name="create" class="btn btn-inverse save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                                          </div>
                                        </div>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
