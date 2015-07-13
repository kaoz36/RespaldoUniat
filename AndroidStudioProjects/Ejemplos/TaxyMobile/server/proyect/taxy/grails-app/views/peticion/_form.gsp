<%@ page import="com.jaguarlabs.Peticion" %>



<div class="fieldcontain ${hasErrors(bean: peticionInstance, field: 'usuario', 'error')} required">
	<label for="usuario">
		<g:message code="peticion.usuario.label" default="Usuario" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="usuario" name="usuario.id" from="${com.jaguarlabs.Usuario.list()}" optionKey="id" required="" value="${peticionInstance?.usuario?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: peticionInstance, field: 'locacion', 'error')} ">
	<label for="locacion">
		<g:message code="peticion.locacion.label" default="Locacion" />
		
	</label>
	<g:textField name="locacion" value="${peticionInstance?.locacion}"/>
</div>


<div class="fieldcontain ${hasErrors(bean: peticionInstance, field: 'fecha', 'error')} required">
	<label for="fecha">
		<g:message code="peticion.fecha.label" default="Fecha" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="fecha" precision="day"  value="${peticionInstance?.fecha}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: peticionInstance, field: 'status', 'error')} ">
	<label for="status">
		<g:message code="peticion.status.label" default="Status" />
		
	</label>
	<g:select name="status" from="${peticionInstance.constraints.status.inList}" value="${peticionInstance?.status}" valueMessagePrefix="peticion.status" noSelection="['': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: peticionInstance, field: 'taxi', 'error')} ">
	<label for="taxi">
		<g:message code="peticion.taxi.label" default="Taxi" />
		
	</label>
	<g:textField name="taxi" value="${peticionInstance?.taxi}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: peticionInstance, field: 'comentario', 'error')} ">
	<label for="comentario">
		<g:message code="peticion.comentario.label" default="Comentario" />
		
	</label>
	<g:textField name="comentario" value="${peticionInstance?.comentario}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: peticionInstance, field: 'atendio', 'error')} ">
	<label for="atendio">
		<g:message code="peticion.atendio.label" default="Atendio" />
		
	</label>
	<g:select id="atendio" name="atendio.id" from="${com.jaguarlabs.Admin.list()}" optionKey="id" value="${peticionInstance?.atendio?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: peticionInstance, field: 'broma', 'error')} ">
	<label for="broma">
		<g:message code="peticion.broma.label" default="Broma" />
		
	</label>
	<g:checkBox name="broma" value="${peticionInstance?.broma}" />
</div>

<div class="fieldcontain ${hasErrors(bean: peticionInstance, field: 'zona', 'error')} required">
	<label for="zona">
		<g:message code="peticion.zona.label" default="Zona" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="zona" name="zona.id" from="${com.jaguarlabs.Zona.list()}" optionKey="id" required="" value="${peticionInstance?.zona?.id}" class="many-to-one"/>
</div>

