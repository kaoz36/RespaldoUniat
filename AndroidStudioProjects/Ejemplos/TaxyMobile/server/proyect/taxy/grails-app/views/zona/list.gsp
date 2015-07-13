
<%@ page import="com.jaguarlabs.Zona" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'zona.label', default: 'Zona')}" />
		<title>Nueva Zona</title>
	</head>
	<body>
          <div class="nav" role="navigation">
				<li><g:link class="create" action="create">Nueva Zona</g:link></li>
		</div>
		<div id="list-zona" class="content scaffold-list" role="main">
			<h1>Lista de Zonas</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table class="table span10">
				<thead>
					<tr>
                                          <th>Id</th>
                                          <th>${message(code: 'zona.nombre.label', default: 'Nombre')} </th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${zonaInstanceList}" status="i" var="zonaInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                                                <td><g:link action="show" id="${zonaInstance.id}">${fieldValue(bean: zonaInstance, field: "id")}</g:link></td>
						<td>${fieldValue(bean: zonaInstance, field: "nombre")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${zonaInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
