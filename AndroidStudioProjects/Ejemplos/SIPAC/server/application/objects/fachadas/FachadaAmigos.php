<?php

include_once("control/ControlAmigos.php");

	class FachadaAmigos
	{
		var $usrGlb;
		
		function FachadaAmigos( $usr )
		{
			$this->usrGlb=$usr;
			 //ctrTransferencia = new ControlTransferencia( );
		}
		
		function recibirTransferencia($funcion, $objetoTransferente)
		{
			$ctrAmigos = new ControlAmigos($this->usrGlb);
			
			//return "Entro en recibirTransferencia";
			switch( $funcion )
			{
				case "consultaAmigosVO":
					return $ctrAmigos->consultaAmigosVO( );
				break;
				
				case "consultaAmigos":
					return $ctrAmigos->consultaAmigos(  );
				break;
				
				case "consultaSolicitudes":
					return $ctrAmigos->consultaSolicitudes(  );
				break;
				
				case "agregarAmigo":
					return $ctrAmigos->agregarAmigo( $objetoTransferente );
				break;
				
				case "eliminarAmigo":
					return $ctrAmigos->eliminarAmigo( $objetoTransferente );
				break;
				
				case "enviarAmigosUsuario":
					return $ctrAmigos->amigosUsuarioEnviados( $objetoTransferente );
				break;
				
				default:
				
				break;
			}
		}
		
	}

?>
