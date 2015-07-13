<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

	class FachadaUsuario extends CI_Controller
	{
		var $usrGlb;
		
		function FachadaUsuario( $usr )
		{
			$this->usrGlb=$usr;
		}
		
		function recibirTransferencia($funcion, $objetoTransferente)
		{
			$ctrUsuario = new ControlUsuario($this->usrGlb);
			
			switch( $funcion )
			{
				
				case "uploadUserPicture":
					return $ctrUsuario->uploadUserPicture( $objetoTransferente );
				break;
				case "registrar":
					return $ctrUsuario->registerPerson( $objetoTransferente );
				break;
				case "registrarWeb":
					return $ctrUsuario->registerPersonWEB( $objetoTransferente );
				break;
				case "registerPersonWEBFB":
					return $ctrUsuario->registerPersonWEBFB( $objetoTransferente );
				break;
				
				case "autentificar": 
					return $ctrUsuario->autentificacion( $objetoTransferente );
				break;
				
				case "consultaAmigos":
					return $ctrUsuario->consultaAmigos( $objetoTransferente );
				break;
				
				case "agregarAmigo":
					return $ctrUsuario->agregarAmigo( $objetoTransferente );
				break;
				
				case "consultar": 
					return $ctrUsuario->consultarPerson( $objetoTransferente );
				break;
				
				case "buscaUsuario":
					return $ctrUsuario->buscaUsuario( $objetoTransferente );
				break;
				
				case "buscaLogin":
					return $ctrUsuario->buscaLogin( $objetoTransferente );
				break;
				
				case "buscaEmail":
					return $ctrUsuario->buscaEmail( $objetoTransferente );
				break;
				
				case "ifUserConfirmed":
					return $ctrUsuario->ifUserConfirmed( );
				break;
				
				case "buscaActualizacionItems":
					return $ctrUsuario->compararFechasItems($objetoTransferente);
				break;	
				
				case "buscaActualizacionAmigos":
				return $ctrUsuario->compararFechasAmigos( $objetoTransferente );
				break;
				
				case "buscaActualizacionMensajes":
				return $ctrUsuario->compararFechasMensajes( $objetoTransferente );
				break;
				
				case "cambiarPassword":
				return $ctrUsuario->cambiarPassword( $objetoTransferente );
				break;
				
				case "cambiarCorreo":
				return $ctrUsuario->cambiarCorreo( $objetoTransferente );
				break;
				
				case "cambiarNombre":
				return $ctrUsuario->cambiarNombre( $objetoTransferente );
				break;
				
				
			}
		}
		
	}
