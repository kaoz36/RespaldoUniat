<!DOCTYPE html>
<html lang="es" 
      xmlns:fb="https://www.facebook.com/2008/fbml"> 
	<head prefix="og: http://ogp.me/ns# fb: http://ogp.me/ns/fb# bajiomedia: http://ogp.me/ns/fb/bajiomedia#">
		<!-- HTML 5 *******************************************************************  -->
		<meta charset="utf-8" />
		<meta name="description" content="Integracion con Facebook y uso de widgets" />
		<meta name="viewport"  content="width=device-width,initial-scale=1"/>
		
		<!-- Facebook Tags ************************************************************  -->
		<meta property="fb:app_id" content="457701257612554" />
		<meta property="og:type"   content="bajiomedia:recipe" />
		<meta property="og:title"  content="Sample Recipe" />
		<meta property="og:image"  content="http://bajiomedia.com/siteTest/assets/img/lettuce.png" />
		<meta property="og:url"    content="http://www.bajiomedia.com/siteTest/index.php/facebookpage" />
		<meta property="og:description" content="Pagina de prueba para Integracion con Facebook" />
		
		<title>Prueba Integración con Facebook</title>
		<link rel="stylesheet" href="../assets/css/normalize.css" type="text/css" media="screen" />
		<link rel="stylesheet" href="../assets/css/styles.css" type="text/css" media="screen" />
	</head>
	<body>
		<div id="fb-root"></div>
		<header>
			<h1>
				<a>
					<img class="fade" alt="Pagina de prueba" />
				</a>
			</h1>
			<nav>
				<ul>
					<li><a href="inicio.html">Inicio</a></li>
					<li><a href="acerca.html">Acerca</a></li>
					<li><a href="servicios.html">Servicios</a></li>
					<li><a href="trabajos.html">Trabajos</a></li>
					<li><a href="contacto.html">Contacto</a></li>
				</ul>
			<nav>
		</header>

		<section id="contenido">
			<section id="principal">
				<article id="galeria-inicio">
				<p>Prueba widget social</p>
				<!--<fb:activity 
				site="http://www.jerrycain.com"
				app_id="457701257612554">
				</fb:activity>-->
				<fb:activity actions="bajiomedia:cook" app_id="457701257612554" /></fb:activity>
				<article>
				
			</section>
			<aside>
				
				<fb:login-button show-faces="true" width="200" max-rows="1" scope="publish_actions">
				</fb:login-button>
				<p>
					<img title="Stuffed Cookies" 
						 src="http://bajiomedia.com/siteTest/assets/img/lettuce.png" 
						 width="300"/>
				</p>
				<form>
					<input type="button" value="Cook" onclick="postCook()" />
				</form>
				<br/>
				<br/>
				
			</aside>
		</section>
		
		<footer>
				<p>Informacion adicional</p>
				<p>llamada al muro de chocoface</p>
				<fb:like href="http://www.facebook.com/ChocoFaceMx"  send="false"  layout="box_count" show-faces="true" font="tahoma" />
		</footer>
		<script>
		  function postCook()
		  {
			 
			  FB.getLoginStatus(function(response) {
			  if (response.status === 'connected') {
				// connected
				submitCook(response.authResponse.accessToken);
				
			  } else if (response.status === 'not_authorized') {
				// not_authorized
				alert("Sin autorizacion");
			  } else {
				// not_logged_in
				alert("Sin Loggear!!");
			  }
			 });
			  
		  }
		  
		  function submitCook(token){
			FB.api(
				'/me/bajiomedia:cook',
				'post',
				{ recipe: 'http://www.bajiomedia.com/siteTest/index.php/facebookpage',
				  access_token: token},
				function(response) {
				   if (!response || response.error) {
					  alert('Error occured');
				   } else {
					  alert('Cook was successful! Action ID: ' + response.id);
				   }
				});
		  }

		window.fbAsyncInit = function() {
		  FB.init({
			appId      : '457701257612554', // App ID
			status     : true, // check login status
			cookie     : true, // enable cookies to allow the server to access the session
			xfbml      : true  // parse XFBML
		  });
		};

		// Load the SDK Asynchronously
		(function(d){
		  var js, id = 'facebook-jssdk'; if (d.getElementById(id)) {return;}
		  js = d.createElement('script'); js.id = id; js.async = true;
		  js.src = "//connect.facebook.net/es_LA/all.js";
		  d.getElementsByTagName('head')[0].appendChild(js);
		}(document));
	  </script>
</body>
</html>