<?php

class TemplateCorreo_Confirmacion
{  
	var $url_confirmacion;
	var $codigohtml;
	var $asunto;
	var $email;
	var $cabeceras;
	
	var $_explicitType="TemplateCorreo_Confirmacion";
	
	function TemplateCorreo_Confirmacion()
	{
	
	}
	
	function setDatos($person, $clave, $confirmacion)
	{
		//return "Los datos de setdatos: Nombre: ".$person->firstname." *Clave: ".$clave." *Confirmacion: ".$confirmacion;
		$this->url_confirmacion = "http://www.boomonsters.com/confirmar.php?uid=".$clave."&cid=".$confirmacion."";
		
		$this->codigohtml = "<table width='100%'>
						<tr>
						<td bgcolor='#00CC33'>
						<h1><font color='#FFCC66'>BooMonsters</font></h1><font color='#FFFFFF'>
				        <h2>Confirmación</h2></font></td></tr><tr><td bgcolor='#FFFFFF'>
						<h2>Hola ".utf8_decode($person->firstname).",</h2>
						<p>Gracias , Por favor haz click <a href='".$this->url_confirmacion."' target='_blank'>aqu&iacute;</a> para confirmar tu Correo. Una vez que confirmes que es tu correo, podr&aacute;s accesar a todos los beneficios de BooMonsters en tu aplicación de escritorio. Si el enlace anterior no es soportado por tu programa de correo, haz click en el siguiente link o copia y pega en tu navegador de Internet.</p>
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
			
		$this->asunto = 'Confirmación BooMonsters';
		$this->email = $person->email;
		
		// To send HTML mail, the Content-type header must be set
		$this->cabeceras  = 'MIME-Version: 1.0' . "\r\n";
		$this->cabeceras .= 'Content-type: text/html; charset=iso-8859-1' . "\r\n";
		// Additional headers
		$this->cabeceras .= 'From: BooMonsters <no-reply@boomonsters.com>' . "\r\n";	
	}
}	


?>
