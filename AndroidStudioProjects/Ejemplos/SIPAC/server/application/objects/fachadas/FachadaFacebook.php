<?PHP

include_once("control/ControlFacebook.php");

	class FachadaFacebook
	{
		var $usrGlb;
		
		function FachadaFacebook( $usr)
		{
			$this->usrGlb=$usr;
		}
		
		function recibirTransferencia($funcion, $objetoTransferente)
		{
			$ctrFB = new ControlFacebook($this->usrGlb);
			
			switch( $funcion )
			{
				case "registrarUsuario":
				    //return $objetoTransferente->idSocial."-".$objetoTransferente->idUsuario;
					return $ctrFB->registrarUsuario( $objetoTransferente );
				break;
				
				case "hasFaceBookAccount":
					return $ctrFB->hasFaceBookAccount( $objetoTransferente );
				break;
				
				case "hasBooMonstersAccount":
					return $ctrFB->hasBooMonstersAccount( $objetoTransferente );
				break;
				
				case "vincularFacebookBooMonsters":
					return $ctrFB->vincularFacebookBooMonsters( $objetoTransferente );
				break;

				default:
					return "Función no registrada";
				break;
			}
		}
		
	}

?>
