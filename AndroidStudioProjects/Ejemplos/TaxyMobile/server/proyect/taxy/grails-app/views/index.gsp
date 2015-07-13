<!doctype html>

<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Taxy Mobile</title>
		<style type="text/css" media="screen">
			#status {
				background-color: #eee;
				border: .2em solid #fff;
				margin: 2em 2em 1em;
				padding: 1em;
				width: 12em;
				float: left;
				-moz-box-shadow: 0px 0px 1.25em #ccc;
				-webkit-box-shadow: 0px 0px 1.25em #ccc;
				box-shadow: 0px 0px 1.25em #ccc;
				-moz-border-radius: 0.6em;
				-webkit-border-radius: 0.6em;
				border-radius: 0.6em;
			}

			.ie6 #status {
				display: inline; /* float double margin fix http://www.positioniseverything.net/explorer/doubled-margin.html */
			}

			#status ul {
				font-size: 0.9em;
				list-style-type: none;
				margin-bottom: 0.6em;
				padding: 0;
			}
            
			#status li {
				line-height: 1.3;
			}

			#status h1 {
				text-transform: uppercase;
				font-size: 1.1em;
				margin: 0 0 0.3em;
			}

			#page-body {
				margin: 2em 1em 1.25em 18em;
			}

			h2 {
				margin-top: 1em;
				margin-bottom: 0.3em;
				font-size: 1em;
			}

//			p {
//				line-height: 1.5;
//				margin: 0.25em 0;
//			}

			#controller-list ul {
				list-style-position: inside;
			}

			#controller-list li {
				line-height: 1.3;
				list-style-position: inside;
				margin: 0.25em 0;
			}

			@media screen and (max-width: 480px) {
				#status {
					display: none;
				}

				#page-body {
					margin: 0 1em 1em;
				}

				#page-body h1 {
					margin-top: 0;
				}
			}
		</style>
	</head>
        <body >
          <g:if test="${session.user==null}">
            
                <div class="hero-unit">
                    <h1>Bienvenido</h1>
                    <p>Inicia sesión por favor.</p>
                  
                     <div class="well">
                      <g:form class="form-horizontal" controller="Session" action="loginUser" method="post">
                            <div class="control-group">
                                    <label class="control-label" for="inputEmail">Usuario</label>
                                    <div class="controls">
                                        <input <input id="userName" name='userName' type="text" placeholder="Usuario"/>
                                    </div>
                            </div>
                            <div class="control-group">
                                    <label class="control-label" for="inputPassword">Constraseña</label>
                                    <div class="controls">
                                        <input type="password" id="password"  name='password' placeholder="Contraseña">
                                    </div>
                            </div>                       
                          <div class="control-group">
                               <div class="controls">
                                   <button type="submit" class="btn btn-warning">Entrar</button>
                               </div>
                          </div>
                        </g:form>
                       </div>       
                      <g:if test="${flash.message}">
                       <center>
                         <div style="width: 300px;padding-top: 10px;"> <div class="errors" >${flash.message}</div></div>
                       </center>
                     </g:if>
                  </div> 
            </g:if>
            <g:else>      
                  <TagLib:redirectMainPage params="[view: 'pending']"/>
            </g:else>
	</body>
</html>
