<%@ page import="com.jaguarlabs.Admin" %>



<div class="fieldcontain ${hasErrors(bean: adminInstance, field: 'zona', 'error')} required control-group">
	<label for="zona" class="control-label">
		<g:message code="admin.zona.label" default="Zona" />
		<span class="required-indicator">*</span>
	</label>
        <div class="controls">
          <g:select id="zona" name="zona.id" from="${com.jaguarlabs.Zona.list()}" optionKey="id" required="" value="${adminInstance?.zona?.id}" class="many-to-one"/>
        </div>
</div>

<div class="fieldcontain ${hasErrors(bean: adminInstance, field: 'nombreCompleto', 'error')} control-group">
	<label for="nombreCompleto" class="control-label">
		<g:message code="admin.nombreCompleto.label" default="Nombre completo" />
		
	</label>
        <div class="controls">
          <g:textField name="nombreCompleto" value="${adminInstance?.nombreCompleto}"/>
        </div>
</div>

<div class="fieldcontain ${hasErrors(bean: adminInstance, field: 'usuario', 'error')} control-group">
	<label for="usuario" class="control-label">
		<g:message code="admin.usuario.label" default="Usuario" />
		
	</label>
	<div class="controls">
          <g:textField name="usuario" value="${adminInstance?.usuario}"/>
        </div>
</div>

<div class="fieldcontain ${hasErrors(bean: adminInstance, field: 'Contraseña', 'error')} control-group">
	<label for="password" class="control-label">
		<g:message code="admin.password.label" default="Contraseña" />
		
	</label>
	<div class="controls">
          <g:textField name="password" value="${adminInstance?.password}"/>
        </div>
</div>

