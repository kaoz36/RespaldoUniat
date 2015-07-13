<?php

include_once("control/ControlMascota.php");

	class FachadaMascotas
	{
		var $usrGlbl;
		var $ctrMascota;
		function FachadaMascotas($usr )
		{
			 $this->usrGlbl=$usr;
			 //ctrTransferencia = new ControlTransferencia( );
			//$ctrUsuario = new PersonService( );
		}
		
		function recibirTransferencia($funcion, $objetoTransferente)
		{
			$ctrMascota = new ControlMascota( $this->usrGlbl );
			
			//return  "Entro en recibirTransferencia";
			switch( $funcion )
			{
				case "agregar":
					return $ctrMascota->agregaMascota( $objetoTransferente );
				break;
				
				case "consultarMascotasDe":
					return $ctrMascota->leeMascotasDeUsuario(  $objetoTransferente);
				break;
				
				case "compartirMascota":
					return $ctrMascota->compartirMascota( $objetoTransferente );
				break;
				
				case "updateMascota":
					return $ctrMascota->updateMascota( $objetoTransferente );
				break;
				case "desCompartirMascotaDueno":
					return $ctrMascota->desCompartirMascotaDueno( $objetoTransferente );
				break;
				case "desCompartirMascotaInvitado":
					return $ctrMascota->desCompartirMascotaInvitado( $objetoTransferente );
				break;
				
				case "consultarMascotaPropia":
					return $ctrMascota->consultarMascotaPropia( );
				break;				
				
				case "buscaActualizacionItems":
				    return $ctrMascota->compararFechas($objetoTransferente);
				break;
				
				case "updateColor":
					return $ctrMascota->cambiarColor($objetoTransferente);
				break;
				
				case "updateName":
					return $ctrMascota->cambiarNombre($objetoTransferente);
				break;	
				
					
				
			}
		}
		
	}

?>
