<?php

class TemplateCorreo_PasswordCambiado
{  
	var $url_confirmacion;
	var $codigohtml;
	var $asunto;
	var $email;
	var $cabeceras;
	
	function TemplateCorreo_PasswordCambiado($person)
	{
	
		$this->codigohtml = "<table width='100%'>
						<tr>
						<td bgcolor='#00CC33'>
						<h1><font color='#FFCC66'>BooMonsters</font></h1><font color='#FFFFFF'>
				        <h2>Confirmación</h2></font></td></tr><tr><td bgcolor='#FFFFFF'>
						<h2>Hola ".utf8_decode($person->firstname).",</h2>
						<p>Tu contrase&ntilde;a ha sido restablecida. Te recomendamos que una vez dentro de la aplicaci&oacute;n la cambies para as&iacute; garantizar a&uacute;n m&aacute;s tu seguridad. Gracias,</p> 
						<p> Tu Login: ".$person->login." </p> 
						<p>Tu Password: ".$person->contrasenia."</p>
						
						<p>Gracias por adoptar a tu <strong>BooMonster!</strong></p>
						<p>&nbsp;</p>
						</td>
						</tr>
						<tr>
						<td><span class='style9'>--- Trebox Media</span>
						</td>
						</tr>
						</table>";
			
		$this->asunto = 'Su Contraseña de Boomonsters ha sido cambiada';
		$this->email = $person->email;
		
		
		// To send HTML mail, the Content-type header must be set
		$this->cabeceras  = 'MIME-Version: 1.0' . "\r\n";
		$this->cabeceras .= 'Content-type: text/html; charset=iso-8859-1' . "\r\n";
		// Additional headers
		$this->cabeceras .= 'From: BooMonsters <no-reply@boomonsters.com>' . "\r\n";	
	}
	
}	


?>
