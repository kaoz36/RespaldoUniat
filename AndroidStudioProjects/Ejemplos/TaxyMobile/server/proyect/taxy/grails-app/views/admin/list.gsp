
<%@ page import="com.jaguarlabs.Admin" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'admin.label', default: 'Admin')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<div class="nav" role="navigation">
				<li><g:link class="create" action="create">Nuevo Administrador</g:link></li>
		</div>
		<div id="list-admin" class="content scaffold-list" role="main">
			<h1>Lista de Administradores</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table class="table span10">
				<thead>
					<tr>
                                                <th>Id</th>
						<th><g:message code="admin.zona.label" default="Zona" /></th>
					
                                                <th>${message(code: 'admin.nombreCompleto.label', default: 'Nombre Completo')} </th>
					
                                                <th>${message(code: 'admin.usuario.label', default: 'Usuario')} </th>
					
                                                <th>Contraseña<!--${message(code: 'admin.password.label', default: 'password')} --></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${adminInstanceList}" status="i" var="adminInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                                                <td><g:link action="show" id="${adminInstance.id}"> ${adminInstance.id}</g:link></td>
						<td><g:link controller="Zona" action="show" id="${adminInstance.zona.id}">${fieldValue(bean: adminInstance, field: "zona")}</g:link></td>
					
						<td>${fieldValue(bean: adminInstance, field: "nombreCompleto")}</td>
					
						<td>${fieldValue(bean: adminInstance, field: "usuario")}</td>
					
						<td>${fieldValue(bean: adminInstance, field: "password")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${adminInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
