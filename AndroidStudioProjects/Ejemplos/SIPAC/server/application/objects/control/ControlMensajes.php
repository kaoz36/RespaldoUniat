<?PHP
	include_once 'conexion.php';
	include_once 'vo/MensajeVO.php';
	
	class ControlMensajes
	{
		var $usrGlb;
		
		function ControlMensajes( $usr )
		{
			$this->usrGlb=$usr;
		}
		
		function marcarMensajeLeido( $mensaje )
		{
			//Trabajamos bajo la premisa de que sólo el destinatario "marca" los mensajes como leídos.
			
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();
			
			switch( $mensaje->estado )
			{
				case 'N':
					$estado = 'Y';
				break;
				
				case 'M':
					$estado = 'Z';
				break;
				
				default:
					return -1;
				break;
			}
					
					$sql = "UPDATE mensajes SET estado = '".$estado."' WHERE id = ".$mensaje->id;
			
			/*
			switch( $mensaje->tipo )
			{
				
				case "sencillo":
				
					switch( $mensaje->estado )
					{
						case 'N':
							$estado = 'Y';
						break;
						
						case 'M':
							$estado = 'Z';
						break;
						
						default:
							return -1;
						break;
					}
					
					$sql = "UPDATE mensajes SET estado = '".$estado."' WHERE id = ".$mensaje->id;
					
				
				break;
				
				case "solicitud":
				
					$sql = "UPDATE mensajes SET estado = 'F' WHERE id = ".$mensaje->id;
					//$sql = "DELETE FROM mensajes WHERE id = ".$mensaje->id;
				
				break;
				
				case "compartir":
				
					$sql = "UPDATE mensajes SET estado = 'F' WHERE id = ".$mensaje->id;
					//$sql = "DELETE FROM mensajes WHERE id = ".$mensaje->id;
				
				break;
				
				case "item":
					$sql = "UPDATE mensajes SET estado = 'F' WHERE id = ".$mensaje->id;
					//$sql = "DELETE FROM mensajes WHERE id = ".$mensaje->id;
				break;
				
			}
			
			*/
			
			mysql_query($sql,$conexion);
			
			//$sql = "UPDATE mensajes SET estado = '".$estado."' WHERE id = ".$mensaje->id;
			$sql = "SELECT estado FROM mensajes WHERE id = ".$mensaje->id;
			
			$res = mysql_query($sql,$conexion);
			
			$estado = mysql_result($res,0,0);
			
			return $estado;
		}
		
		function eliminarMensaje( $msj )
		{
			//No se hará un borrado físico... por ahora.
			//Un mismo mensaje es leído tanto por el destinatario (en su bandeja de entrada) como por el remitente (en su bandeja de salida),
			//así que al eliminar un mensaje le quitaríamos al otro usuario el "derecho" de conservarlo.
			
			//Por ahora, la forma que se me ocurre para que un usuario pueda "eliminar" el mensaje y que el otro lo conserve es por el campo
			// "estado":
			//
			//    'N' : El destinatario NO lo ha leído y el remitente lo conserva en su bandeja de salida.
			//	  'M' : El destinatario NO lo ha leído y el remitente NO lo conserva.
			//
			//	  'Y' : El destinatario YA lo ha leído, lo conserva y el remitente también lo conserva.
			//	  'Z' : El destinatario YA lo ha leído, lo conserva y el remitente NO lo conserva.
			//
			//    'E' : El destinatario eliminó el mensaje y el remitente lo conserva.
			//	  'F' : El destinatario eliminó el mensaje y el remitente NO lo conserva (En este punto ya podríamos considerar un borrado físico)
			
			
			
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();
			
			if( $this->usrGlb->id == $msj->destinatario) //El usuario es el destinatario
			{
				switch( $msj->estado )
				{
					case 'N': case 'Y':
						$estado = "E";
					break;
					
					case 'M': case 'Z':
						$estado = "F";
					break;
				}
			}
			else  //El usuario es el remitente
			{
				switch( $msj->estado )
				{
					case 'N':
						$estado = "M";
					break;
					
					case 'Y':
						$estado = "Z";
					break;
					
					case 'E':
						$estado = "F";
					break;
				}
			}
			
			$sql = "UPDATE mensajes SET estado = '".$estado."' WHERE id = ".$msj->id;
			$res = mysql_query($sql,$conexion);
			
			$this->cambiarFechaMensajes($msj->destinatario);
			$this->cambiarFechaMensajes($this->usrGlb->id);
			
			return $estado;
		}
		
		function enviarMensaje( $msj )
		{
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();
			
			$sql = "INSERT INTO mensajes VALUES(default, ".$msj->destinatario.", ".$this->usrGlb->id.", '".utf8_decode($msj->texto)."', '".$msj->tipo."', 'N', ".$msj->adjunto.",( SELECT unix_timestamp(now()) ))";
			mysql_query($sql,$conexion);
			
			$sql = "SELECT max(id) FROM mensajes where destinatario = ".$msj->destinatario." and remitente = ".$this->usrGlb->id;
			$res = mysql_query($sql,$conexion);
			
			$id = mysql_result($res,0,0);
			
			$this->cambiarFechaMensajes($msj->destinatario);
			$this->cambiarFechaMensajes($this->usrGlb->id);
			
			return $id;
		}
		
		function consultarMensajesUsuario( ) 
		{
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();
            
			/*
			id	int(11)			No		auto_increment	 	 	 	 	 	 	
			destinatario	int(11)			No			 	 	 	 	 	 	
			remitente	int(11)			No			 	 	 	 	 	 	
			texto	varchar(140)	latin1_swedish_ci		No			 	 	 	 	 	 	 
			tipo	varchar(15)	latin1_swedish_ci		Yes	NULL		 	 	 	 	 	 	 
			estado	char(1)	latin1_swedish_ci		No			 	 	 	 	 	 	 
			adjunto
			*/
			
			//return $this->usrGlb;
			
            $sqlEntrada = "SELECT id, destinatario, remitente, texto, tipo, estado, adjunto, fecha, u.nombre FROM mensajes m, usuario u WHERE remitente = u.clave and destinatario = ".$this->usrGlb->id." order by fecha desc";
			$res1 = mysql_query($sqlEntrada,$conexion) or die ($sqlEntrada);
			
			$sqlSalida  = "SELECT id, destinatario, remitente, texto, tipo, estado, adjunto, fecha, u.nombre FROM mensajes m, usuario u WHERE destinatario = u.clave and remitente = ".$this->usrGlb->id." order by fecha desc";
			$res2 = mysql_query($sqlSalida,$conexion) or die ($sqlSalida);
			
			$e = 0;
			$s = 0;
			
			$entrada = array();
			$salida = array();
			
			while( $fila = mysql_fetch_array($res1))
			{
				$msj = $this->getMensaje($fila);
				$entrada[$e++] = $msj;
			}
			
			 //Esta parte se usa para separar en dos los correos: entrada y salida.
			while( $fila = mysql_fetch_array($res2))
			{
				$msj = $this->getMensaje($fila);
				$salida[$s++] = $msj;
			}
			
            $conexion = $objConectar->desconectar(); 
			
			$arreglo = array();
			
			$arreglo[0] = $entrada; 
			$arreglo[1] = $salida;
			
			return $arreglo;
			
			
			/*
			//----------- Inicia bandejas combinadas
			while( $fila = mysql_fetch_array($res2))
			{
				$msj = $this->getMensaje($fila);
				$entrada[$e++] = $msj;
			}
			//----------- Termina bandejas combinadas
			*/
            $conexion = $objConectar->desconectar(); 
			
			$arreglo = array();
			
			$arreglo[0] = $entrada; 
			$arreglo[1] = $salida;
			
			return $arreglo;
			//return $entrada;
			
		}
		
		function getMensaje($fila)
		{
			$msj = new MensajeVO();
			$i = 0;
			
			$msj->id = (int) $fila[$i++];
			$msj->destinatario = (int) $fila[$i++];
			$msj->remitente = (int) $fila[$i++];
			$msj->texto = $fila[$i++];
			$msj->tipo = $fila[$i++];
			$msj->estado = $fila[$i++];
			$msj->adjunto = $fila[$i++];
			
			//nuevo:
			
			$msj->fechaRecibida = $fila[$i++];
			$msj->nombreRemitente = $fila[$i++];
			
		/*	$f = $fila[$i++];
			$msj->nombreRemitente = $fila[$i++];
			list($fecha, $tiempo) = split(' ', $f);
			
			list($hora, $minutos, $segundos) = split(':', $tiempo);
			list($agno, $mes, $dia) = split('-', $fecha);
			
			//$msj->fecha = $hora." ".$minutos." ".$segundos." ".$mes." ".$dia." ".$agno;
			$msj->fechaRecibida =  date("D M j Y h:i:s", mktime($hora, $minutos, $segundos, $mes, $dia, $agno)); */
						
			return $msj;
		}
		
		function mensajesUsuarioEnviados($arreglo)
		{
			$objConectar = new conexion();
	        $conexion = $objConectar->conectar();
	    			
			$sql_destinatario = "SELECT id, destinatario, remitente, texto, tipo, estado, adjunto, fecha, u.nombre FROM mensajes m, usuario u WHERE remitente = u.clave and destinatario = ".$this->usrGlb->id." and (estado <>  'E' AND estado <>  'F') order by fecha desc";
		    $res1 = mysql_query($sql_destinatario,$conexion);
		    
		    $sql_remitente = "SELECT id, destinatario, remitente, texto, tipo, estado, adjunto, fecha, u.nombre FROM mensajes m, usuario u WHERE destinatario = u.clave and remitente = ".$this->usrGlb->id." and (estado <> 'M' AND estado <> 'Z' AND estado <> 'F' ) order by fecha desc";
		    $res2 = mysql_query($sql_remitente,$conexion);
		    
		    $e = 0;
			$s = 0;
			
			$entrada = array();
			$salida = array();
			
			while( $fila = mysql_fetch_array($res1))
			{
				$msj = $this->getMensaje($fila);
				$entrada[$e++] = $msj;
			}
			
			 //Esta parte se usa para separar en dos los correos: entrada y salida.
			while( $fila = mysql_fetch_array($res2))
			{
				$msj = $this->getMensaje($fila);
				$salida[$s++] = $msj;
			}
		       
		    $c1 = 0;
		    $c2 = 0;
		    	 
		    for($x=0; $x<sizeof($entrada); $x++)
		    {
		    	$id_mensaje = $entrada[$x]->id;
		    	$verificador = false;
		    	
		    	for ($i=0; $i < sizeof($arreglo[0]); $i++)
				{					
					if($arreglo[0][$i][0] == $id_mensaje) // se indica que ese mensaje si se tiene en el servidor
					{
						$arreglo[0][$i][1] = true;
						$verificador = true;					
						break 1;
					}
				}
					
				if ($verificador == false) //indica que ese amigo no se tiene en el cliente
				{						
					$arreglo_destinatario[$c1] = $entrada[$x];					
					$c1++;
				}
		    }	   
		    
		    for($x=0; $x<sizeof($salida); $x++)
		    {
		    	$id_mensaje = (int)$salida[$x]->id;
		    	$verificador = false;		
										
				for ($i=0; $i < sizeof($arreglo[1]); $i++)
				{					
					if($arreglo[1][$i][0] == $id_mensaje) // se indica que ese mensaje si se tiene en el servidor
					{
						$arreglo[1][$i][1] = true;
						$verificador = true;					
						break 1;
					}
				}
					
				if ($verificador == false) //indica que ese amigo no se tiene en el cliente
				{
					$arreglo_remitente[$c2] = $salida[$x];					
					$c2++;
				}				
			}
			
			for($c=0 ;$c < sizeof($arreglo[0]); $c++ )
			{
				if($arreglo[0][$c][1] == false && $arreglo[0][$c][0] != 0)
				{
					$arreglo_destinatario[$c1] = (int)$arreglo[0][$c][0] * (-1);
					$c1++;
				}
			}
			
			for($c=0 ;$c < sizeof($arreglo[1]); $c++ )
			{
				if($arreglo[1][$c][1] == false && $arreglo[1][$c][0] != 0)
				{
					$arreglo_remitente[$c2] = (int)$arreglo[1][$c][0] * (-1);
					$c2++;
				}
			}
		
			$arreglo_final[0] = $arreglo_destinatario;
			$arreglo_final[1] = $arreglo_remitente;
				
			$sql_time = "SELECT fechaAct_mensajes FROM usuario WHERE clave = ".$this->usrGlb->id;
			$res_time =  mysql_query($sql_time,$conexion);
			$fecha =  mysql_result($res_time,0,0);
										
			//Se agrega dicha fecha al arreglo que se regresará al cliente					
			$arreglo_final["nueva_fecha"] = (int)$fecha;
					
			$objConectar->desconectar();						
		    return $arreglo_final;	  	 
				  	    	
		}
		
		function formatFecha($f)
		{			
			list($fecha, $tiempo) = split(' ', $f);
			
			list($hora, $minutos, $segundos) = split(':', $tiempo);
			list($agno, $mes, $dia) = split('-', $fecha);		
			
			//para que la fecha sea en tiempo de unix
			$fecha =  gmmktime($hora, $minutos, $segundos, $mes, $dia, $agno);
			return ($fecha*1000);			
		}
		
		function cambiarFechaMensajes($id)
		{
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();
		
			$sql = "UPDATE usuario SET fechaAct_mensajes = ( SELECT unix_timestamp(now()) ) WHERE clave =".$id;
		    $res = mysql_query($sql,$conexion);
		    
		    $objConectar->desconectar();
		}

	}
	
?>
