<?php

include_once("control/ControlKey.php");

	class FachadaKey
	{
		var $usuario;
		var $ctrKey;
		function FachadaKey( $usr )
		{
			$this->usuario = $usr;
		}
		
		function recibirTransferencia($funcion, $objetoTransferente)
		{
			
			$ctrKey = new ControlKey($this->usuario);
			
			switch( $funcion )
			{
				case "crearKey":					 					
					 return $ctrKey->insertKey();
				break;
				
				case "consultarKey": 
					return $ctrKey->getKey($objetoTransferente );
				break;
								
				default:
				
				break;
			}
		}		
	}
?>
