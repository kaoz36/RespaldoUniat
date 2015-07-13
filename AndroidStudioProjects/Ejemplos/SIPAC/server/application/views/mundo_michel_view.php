<!DOCTYPE html>
<html lang="es"  xmlns:fb="https://www.facebook.com/2008/fbml">
	<head prefix="og: http://ogp.me/ns# fb: http://ogp.me/ns/fb# bajiomedia: http://ogp.me/ns/fb/bajiomedia#">

		<meta charset="utf-8" />
		<meta name="description" content="Pagina principal de Michel" />
		<meta name="viewport"  content="width=device-width,initial-scale=1"/>

		<title>Bienvenido Mundo Michel</title>

		<link rel="stylesheet" href="<?=base_url();?>assets/css/normalize.css" type="text/css" media="screen" />
		<link rel="stylesheet" href="<?=base_url();?>assets/css/mundomichel.css" type="text/css" media="screen" />

		<script src="<?=base_url();?>assets/script/jquery.js"> </script>
		<script src="<?=base_url();?>assets/script/homepagescript.js"> </script>
	</head>
	<body> 
		<div id="fb-root"></div>
		<script src="<?=base_url();?>assets/script/facebookscript.js"> </script>
		<script>
			globalVars.controlBaseURL = "<?=base_url();?>";
		</script>
		<header> 
			<nav class="contentHolder">
				<figure id="idCard" class="horizontalItem iconButton"><img src="<?=base_url();?>assets/img/loader.gif" class="loadingAnim"></img></figure>
				<section class="horizontalItem" id="sectionMenu">
				</section> 
				<a id="loginLink" class="iconButton"><img src="<?=base_url();?>assets/img/loader.gif" class="loadingAnim" ></img></a>
			</nav>
		</header>
		<section id="mainContentHolder">
			<section id="mainContent">
				
			</section>
		</section>
		<footer><section class="contentHolder"><p class="legalText">Leyenda Legal</p><section></footer>
	</body>
</html>