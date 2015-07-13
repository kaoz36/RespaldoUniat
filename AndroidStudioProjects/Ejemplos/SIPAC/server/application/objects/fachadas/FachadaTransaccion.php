<?php

include_once("control/ControlTransaccion.php");

	class FachadaTransaccion
	{
		
		var $usuario;
		
		function FachadaTransaccion( $usr )
		{
			$this->usuario = $usr;
		}
		
		function recibirTransferencia($funcion, $objetoTransferente)
		{
			$ctrTransaccion =  new ControlTransaccion( $this->usuario );
								
			switch( $funcion )
			{
				case "abonar":
					return $ctrTransaccion->abonarDinero( $objetoTransferente );
				break;
				
				case "descontar": 
					return $ctrTransaccion->descontarDinero( $objetoTransferente );
				break;
				
				case "consultar":
					return $ctrTransaccion->consultarDinero( $objetoTransferente );
				break;
				
				case "enviarCodigo":
					return $ctrTransaccion->consultarCodigo( $objetoTransferente );
				break;
			}
		}
		
	}

?>
