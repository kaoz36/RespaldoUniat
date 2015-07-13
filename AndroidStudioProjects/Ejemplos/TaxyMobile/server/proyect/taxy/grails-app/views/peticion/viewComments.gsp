
<%@ page import="com.jaguarlabs.Calificacion" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'calificacion.label', default: 'Calificacion')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		
		<div id="list-calificacion" class="content scaffold-list" role="main">
			<h1>Comentarios</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table class="table span10">
				<thead>
					<tr>
                                          <th>Id</th>
                                          <th>Usuario</th>
                                          <th>Comentario</th>					
                                          <th><center>Calificación</center></th>	
					</tr>
				</thead>
				<tbody>
				<g:each in="${peticionInstanceList}" status="i" var="calificacionInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                                                <td>${fieldValue(bean: calificacionInstance, field: "id")}</td>
                                                <td>${fieldValue(bean: calificacionInstance, field: "usuario")}</td>
						<td>${fieldValue(bean: calificacionInstance.calificacion, field: "comentario")}</td>				
                                                <td>
                                                  <div id="rate${i}">
                                                    <center>
                                                      <!--${fieldValue(bean: calificacionInstance.calificacion, field: "calification")}-->
                                                      <script>
                                                        for (var i=0;i<${calificacionInstance.calificacion.calification};i++){
                                                          document.write("<img src='${resource(dir: 'img', file: 'icon_star.png')}'/>");
                                                        }  
                                                       </script>
                                                    </center>
                                                  </div>                                                  
                                                </td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
                        <div class="span1"></div>
                        <center>
			<div class="pagination">
				<g:paginate total="${peticionInstanceListTotal}" params="[view:'rate']"/>
			</div>
                        </center>
		</div>
	</body>
</html>
