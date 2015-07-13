
<%@ page import="com.jaguarlabs.Zona" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'zona.label', default: 'Zona')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="show-zona" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
                        <div class="panel-white">
                          <g:if test="${flash.message}">
                          <div class="message" role="status">${flash.message}</div>
                          </g:if>
                          <ol class="property-list zona">

                                  <g:if test="${zonaInstance?.nombre}">
                                  <li class="fieldcontain">
                                          <span id="nombre-label" class="property-label"><g:message code="zona.nombre.label" default="Nombre" /></span>

                                                  <span class="property-value" aria-labelledby="nombre-label">:<g:fieldValue bean="${zonaInstance}" field="nombre"/></span>

                                  </li>
                                  </g:if>

                          </ol>
                          <g:form>
                                  <fieldset class="buttons">
                                          <g:hiddenField name="id" value="${zonaInstance?.id}" />
                                          <g:link class="edit btn btn-inverse" action="edit" id="${zonaInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                                          <g:actionSubmit class="delete btn btn-danger" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                                  </fieldset>
                          </g:form>
                        </div>
		</div>
	</body>
</html>
