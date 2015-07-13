
<%@ page import="com.jaguarlabs.Usuario" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'usuario.label', default: 'Usuario')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		
		<div id="show-usuario" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
                        <div class="panel-white">
                          <ol class="unstyled">
                                  <g:if test="${usuarioInstance?.nombreCompleto}">
                                  <li>
                                          <span id="nombreCompleto-label" class="property-label"><g:message code="usuario.nombreCompleto.label" default="Nombre Completo" /></span>

                                                  <span class="property-value" aria-labelledby="nombreCompleto-label"><g:fieldValue bean="${usuarioInstance}" field="nombreCompleto"/></span>

                                  </li>
                                  </g:if>

                                  <g:if test="${usuarioInstance?.numero}">
                                  <li class="fieldcontain">
                                          <span id="numero-label" class="property-label"><g:message code="usuario.numero.label" default="Numero" /></span>

                                                  <span class="property-value" aria-labelledby="numero-label"><g:fieldValue bean="${usuarioInstance}" field="numero"/></span>

                                  </li>
                                  </g:if>

                          </ol>
                        </div>
                        Peticiones<br>
                        <ul>
                          <li>Atendida(s):${atendidas}</li>
                          <li>Cancelada(s):${canceladas}</li>
                          <li>No Atendida(s):${noatendidas}</li>
                        </ul>
			<!--<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${usuarioInstance?.id}" />
					<g:link class="edit" action="edit" id="${usuarioInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>-->
		</div>
	</body>
</html>
