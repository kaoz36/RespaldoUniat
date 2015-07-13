<?php

include_once("control/ControlSocial.php");

	class FachadaSocial
	{
		var $usrGlb;
		
		function FachadaSocial( $usr)
		{
			$this->usrGlb=$usr;
			 //ctrTransferencia = new ControlTransferencia( );
			//$ctrUsuario =  new PersonService( );
		}
		
		function recibirTransferencia($funcion, $objetoTransferente)
		{
			$ctrSocial = new ControlSocial($this->usrGlb );
			
			//return $objetoTransferente;
			
			//return "Entro en recibirTransferencia";
			switch( $funcion )
			{
				case "vincularCuenta":
				    //return $objetoTransferente->idSocial."-".$objetoTransferente->idUsuario;
					return $ctrSocial->vincularCuenta( $objetoTransferente );
				break;
				
				case "hasBMwantsSN":
					return $ctrSocial->hasBMwantsSN( $objetoTransferente );
				break;
				
				case "hasSNwantsBM":
					return $ctrSocial->hasSNwantsBM( $objetoTransferente );
				break;
				
				case "getSesionSecretFB":
					return $ctrSocial->getSesionSecretFB(  );
				break;
				
				case "getTokenFacebook":
					return $ctrSocial->getTokenFacebook( );
				break;
				
				case "getTokenSecretTW":
					return $ctrSocial->getTokenSecretTW(  );
				break;
				
				case "setSesionSecretFB":
					return $ctrSocial->setSesionSecretFB( $objetoTransferente );
				break;
				
				case "elUsuarioFBYaTieneBM":
					return $ctrSocial->elUsuarioFBYaTieneBM( $objetoTransferente );
				break;
				
				case "elUsuarioTWYaTieneBM":
					return $ctrSocial->elUsuarioTWYaTieneBM( $objetoTransferente );
				break;
				
				case "vincularDesdeSocial":
					return $ctrSocial->vincularDesdeSocial( $objetoTransferente );
				break;
				
				case "registrarUsuarioFB":
					return $ctrSocial->registrarUsuarioFB( $objetoTransferente );
				break;
				
				default:
					return "Función no registrada";
				break;
			}
		}
		
	}

?>
