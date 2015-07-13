<%@ page import="com.jaguarlabs.Peticion" %>


<div class="fieldcontain ${hasErrors(bean: peticionInstance, field: 'taxi', 'error')} control-group">
	<label for="taxi" class="control-label">
		<g:message code="peticion.taxi.label" default="Taxi" />
		
	</label>
        <div class="controls">
          <g:textField name="taxi" value="${peticionInstance?.taxi}"/>
        </div>
</div>
<div class="fieldcontain ${hasErrors(bean: peticionInstance, field: 'minutos', 'error')} control-group">
	<label for="minutos" class="control-label">
		<g:message code="peticion.minutos.label" default="Minutos" />
	</label>
        <div class="controls">
              <g:field name="minutos" value="${fieldValue(bean: peticionInstance, field: 'minutos')}"/>
        </div>
</div>



