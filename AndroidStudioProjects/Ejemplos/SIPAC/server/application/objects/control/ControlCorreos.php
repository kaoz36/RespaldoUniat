<?php

class ControlCorreos
{
		 
	 function ControlCorreos()
	 {
		 			
	 }
	 
	 function enviarCorreo($template)
	 {
	 	if(!mail($template->email,$template->asunto,$template->codigohtml,$template->cabeceras))
		return "Tremendus error with a simple If";	 
		//return "hey4: ".$template->email.$template->asunto;
	 }	 
}
?>
