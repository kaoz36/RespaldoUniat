<?php
	class ControlTransferencia
	{
		
		function ControlTransferencia( )
		{

		}
		
		function validar( $paquete )
		{
			/*
			if( $this->checaClave($paquete->dato1,$paquete->dato3,$paquete->funcion, $paquete->modulo) and $this->checaOrigen($paquete->dato2) and $this->checaUsuario($paquete->dato3,$paquete->funcion, $paquete->modulo) )
			*/
			
			//return "Dato1:  ".$paquete->dato1;
			
			$origen = $this->checaOrigen($paquete->dato2);
			$usr = $this->checaUsuario($paquete->dato3,$paquete->funcion, $paquete->modulo);
			$clave = $this->checaClave($paquete->dato1,$usr);
			
			if(!usr)
			{
				return -3;
			}

			if(!$clave)
			{
				return -1;
			}
			
			if(!$origen)
			{
				return -2;
			}
			
			return $usr;
			
		}
		
		function checaClave( $clave, $usuario )
		{
			//$usuario = $this->checaUsuario( $usuario, $funcion, $modulo );
			$arreglo = $this->generaClave($usuario);
			return $arreglo;
			
			if( $clave ==  $arreglo[0] || $clave ==  $arreglo[1])
			return 1;
			else
			return 0; 
					
			//return $clave;
			
		}
		
		function generaClave($usuario)
		{
		   
		   $secreto = md5($usuario->login . $usuario->contrasenia);
		   $tokenUsuario =  hash_hmac("md5", $usuario->contrasenia,$secreto);
		   
		
		   $secretoMail = md5($usuario->email . $usuario->contrasenia);
		   $tokenMail =  hash_hmac("md5", $usuario->contrasenia,$secretoMail);
		   
		   $token=array($tokenUsuario,$tokenMail);
			
			//$token = "Una string cualquiera";
			
		   return $token;

		}
		
		function checaOrigen( $origen )
		{
			if( $origen == "BooMonstersAirApp" )			
			return 1;
			else
			return 0;
		}
		
		function checaUsuario( $usuario, $funcion, $modulo )
		{
			$ctrUsuario = new ControlUsuario( $usuario );
		
			if( ($modulo == "facebook" && ($funcion == "hasBooMonstersAccount" || $funcion == "registrarUsuario" ) ) || ($modulo == "key" && ($funcion == "consultarKey" )) || ( $modulo == "sociales" && ($funcion == "elUsuarioTWYaTieneBM" || $funcion == "elUsuarioFBYaTieneBM" || $funcion == "registrarUsuarioFB" ) )  || ($modulo == "usuario" && ($funcion == "registrar" || $funcion == "buscaLogin" || $funcion == "buscaEmail" || $funcion == "ifUserConfirmed" || $funcion == "registerPersonWEBFB")) )
			return $usuario;
			else
			return $ctrUsuario->autentificacion( $usuario );
		}
	}
	
?>
