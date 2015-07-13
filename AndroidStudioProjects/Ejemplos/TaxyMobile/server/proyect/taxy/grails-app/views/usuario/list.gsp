
<%@ page import="com.jaguarlabs.Usuario" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'usuario.label', default: 'Usuario')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
<!--		<a href="#list-usuario" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>-->
		<div id="list-usuario" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table class="table">
				<thead>
					<tr>
                                                <g:sortableColumn property="id" title="${message(code: 'usuario.id.label', default: 'Id')}" />
						<g:sortableColumn property="nombreCompleto" title="${message(code: 'usuario.nombreCompleto.label', default: 'Nombre Completo')}" />
					
						<g:sortableColumn property="numero" title="${message(code: 'usuario.numero.label', default: 'Numero')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${usuarioInstanceList}" status="i" var="usuarioInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                                                <td>${fieldValue(bean: usuarioInstance, field: "id")}</td>
						<td><g:link action="show" id="${usuarioInstance.id}">${fieldValue(bean: usuarioInstance, field: "nombreCompleto")}</g:link></td>
					
						<td>${fieldValue(bean: usuarioInstance, field: "numero")}</td>
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${usuarioInstanceTotal}" />
			</div>
		</div>
	</body>
</html>