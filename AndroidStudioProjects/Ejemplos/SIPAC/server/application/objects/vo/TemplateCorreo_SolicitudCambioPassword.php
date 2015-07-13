<?php
class TemplateCorreo_SolicitudCambioPassword
{  
	var $url_confirmacion;
	var $codigohtml;
	var $asunto;
	var $email;
	var $cabeceras;
	
	function TemplateCorreo_SolicitudCambioPassword($person,$veritas)
	{
	    //$this->url_confirmacion = "http://www.boomonsters.com/amfphp/services/CHGPSW_request.php?uid=".$person->id."&var=1&veritas=".$veritas;
	    $this->url_confirmacion = "http://www.boomonsters.com/CHGPSW_request.php?uid=".$person->id."&var=1&veritas=".$veritas;
		
		$this->codigohtml = "<table width='100%'>
						<tr>
						<td bgcolor='#00CC33'>
						<h1><font color='#FFCC66'>BooMonsters</font></h1><font color='#FFFFFF'>
				        <h2>Solicitud Cambio de Contraseña</h2></font></td></tr><tr><td bgcolor='#FFFFFF'>
						<h2>Hola ".utf8_decode($person->firstname).",</h2>
						<p>Hace unos momentos se solicit&oacute; un cambio de contrase&ntilde;a de tu cuenta Boomonsters. En dado caso que sea una equivocaci&oacute;n ignora este correo. De lo contrario da click a: <a href='".$this->url_confirmacion."' target='_blank'>".$this->url_confirmacion."</a> para confirmar el cambio de tu contrase&ntilde;a. En instantes recibir&aacute;s un correo con tu nueva contrase&ntilde;a y así puedas ingresar a todos los beneficios de BooMonsters en tu aplicaci&oacute;n de escritorio. Si el enlace anterior no es soportado por tu programa de correo, haz click en el siguiente link o copia y pega en tu navegador de Internet.</p>
						<p><a href='".$this->url_confirmacion."' target='_blank'>".$this->url_confirmacion."</a></p>
						<p>Gracias por adoptar a tu <strong>BooMonster!</strong></p>
						<p>&nbsp;</p>
						</td>
						</tr>
						<tr>
						<td><span class='style9'>--- Trebox Media</span>
						</td>
						</tr>
						</table>";
			
		$this->asunto = 'Solicitud de Cambio de Contraseña Boomonsters';
		$this->email = $person->email;
		
		
		// To send HTML mail, the Content-type header must be set
		$this->cabeceras  = 'MIME-Version: 1.0' . "\r\n";
		$this->cabeceras .= 'Content-type: text/html; charset=iso-8859-1' . "\r\n";
		// Additional headers
		$this->cabeceras .= 'From: BooMonsters <no-reply@boomonsters.com>' . "\r\n";	
	}	
}	


?>
