<?php

	include_once("control/ControlMensajes.php");

	class FachadaMensajes
	{
		var $usrGlb;
		function FachadaMensajes($usr )
		{
			$this->usrGlb=$usr;
		}
		
		function recibirTransferencia($funcion, $objetoTransferente)
		{
			$ctrMensajes = new ControlMensajes($this->usrGlb );
			
			//return  "Entro en recibirTransferencia";
			switch( $funcion )
			{
				case "consultarMensajesUsuario":
					return $ctrMensajes->consultarMensajesUsuario(  );
				break;
				
				case "enviarMensaje":
					return $ctrMensajes->enviarMensaje( $objetoTransferente );
				break;
				
				case "eliminarMensaje":
					return $ctrMensajes->eliminarMensaje( $objetoTransferente );
				break;
				
				case "marcarMensajeLeido":
					return $ctrMensajes->marcarMensajeLeido( $objetoTransferente );
				break;
				
				case "enviarMensajesUsuario":
					return $ctrMensajes->mensajesUsuarioEnviados( $objetoTransferente );
				break;
			}
		}
		
	}

?>
