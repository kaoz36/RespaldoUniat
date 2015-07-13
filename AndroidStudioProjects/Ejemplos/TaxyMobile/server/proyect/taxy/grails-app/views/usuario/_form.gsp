<%@ page import="com.jaguarlabs.Usuario" %>



<div class="fieldcontain ${hasErrors(bean: usuarioInstance, field: 'nombreCompleto', 'error')} ">
	<label for="nombreCompleto">
		<g:message code="usuario.nombreCompleto.label" default="Nombre Completo" />
		
	</label>
	<g:textField name="nombreCompleto" value="${usuarioInstance?.nombreCompleto}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: usuarioInstance, field: 'numero', 'error')} ">
	<label for="numero">
		<g:message code="usuario.numero.label" default="Numero" />
		
	</label>
	<g:textField name="numero" value="${usuarioInstance?.numero}"/>
</div>

