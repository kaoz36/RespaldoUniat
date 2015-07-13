<%@ page import="com.jaguarlabs.Peticion" %>
<!doctype html>
<html>
	<head>
		<g:set var="entityName" value="${message(code: 'peticion.label', default: 'Peticion')}" />
		<title>Asignando Taxi</title>
                <link rel="stylesheet" href="${resource(dir: 'css', file: 'colorbox.css')}" type="text/css">
                <link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap.min.css')}">
                <link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap-responsive.min.css')}">
                <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">
                <script src="${resource(dir: 'js/vendor', file: 'modernizr-2.6.2-respond-1.1.0.min.js')}"></script>
                <script src="/taxi/static/plugins/jquery-1.7.1/js/jquery/jquery-1.7.1.min.js" type="text/javascript" > </script>
	</head>
	<body>
		<div id="update-peticion" >
			<h2>Atendiendo Peticion</h2>
                        <div class="panel-white">
                          <g:hasErrors bean="${peticionInstance}">
                          <ul class="errors" role="alert">
                                  <g:eachError bean="${peticionInstance}" var="error">
                                  <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                                  </g:eachError>
                          </ul>
                          </g:hasErrors>
                          <form action="/taxi/mobile/updateAsignando/${peticionInstance.id}" class="form-horizontal" method="POST">
                                  <fieldset class="form" >
                                      <g:render template="formAsign"/>
                                  </fieldset>
                                  <br/>
                                  <input hidden="true" type="hidden" name="doAction" value="asign"/>
                                  <div class="control-group">
                                    <div class="controls">
                                      <input type="submit" onclick="parent.jQuery.colorbox.close();"  class="save btn btn-success" value="Asignar" />
                                    </div>
                                  </div>
                          </form>
                          <form action="/taxi/mobile/updateAsignando/${peticionInstance.id}" method="POST" class="row-no-under">
                                      <input hidden="true" type="hidden" name="doAction" value="revert"/>
                                      <input type="submit" onclick="parent.jQuery.colorbox.close();"  class="save btn btn-inverse" value="Cancelar" />
                          </form>
                        </div>
		</div>
           <script src="/taxi/static/js/vendor/bootstrap.min.js"></script>
        <script src="/taxi/static/js/main.js"></script>    
        <script src="/taxi/static/js/application.js" type="text/javascript" ></script>
	</body>
</html>
