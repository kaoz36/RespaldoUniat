<?php
	
	include_once 'conexion.php';
	include_once 'vo/TransaccionVO.php';
	
	class ControlTransaccion
	{
		var $u;
		
		function ControlTransaccion( $usr )
		{
			$this->u = $usr;
		}
		
		function abonarDinero( $t )
		{		
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();
            
            $sql = "update Dinero_usuarios set cantidad = cantidad + ".$t->cantidad." WHERE id_usuario = ".$this->u->id;
			$res = mysql_query($sql,$conexion);// or die( "Error: " . mysql_error() );
			
			$this->registrarTransaccion($t);
            
            $conexion = $objConectar->desconectar();    
			return 1;
		}
		
		function descontarDinero( $t )
		{
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();
            
            $sql = "update Dinero_usuarios set cantidad = cantidad - ".$t->cantidad." WHERE id_usuario = ".$this->u->id;
			$res = mysql_query($sql,$conexion);// or die( "Error: " . mysql_error() );
			
			$this->registrarTransaccion($t);
            
            $conexion = $objConectar->desconectar(); 
			return 1;
		}
		
		function consultarDinero( $t )
		{
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();
            
            $sql = "Select cantidad from Dinero_usuarios where id_usuario = ".$this->u->id;    
              
                        
			$res = mysql_query($sql,$conexion);// or die( "Error: " . mysql_error() );
						            
            $conexion = $objConectar->desconectar();
			
			$r = mysql_result($res,0,0);			
			return $r;
        }
        
        function registrarTransaccion( $t )
        {
        	$objConectar = new conexion();
            $conexion = $objConectar->conectar();
            
            $sql = "INSERT INTO transacciones VALUES(default,".$this->u->id.",".$t->fecha.",".$t->cantidad.",".$t->saldo.",'".$t->accion."',".$t->objeto.",'".$t->detalle."')";
			
			$res = mysql_query($sql,$conexion) or die( "Error: " . mysql_error(). "  ". $sql );
						
            $conexion = $objConectar->desconectar();
		}
		
		function consultarCodigo( $codigo )
        {
        	$objConectar = new conexion();
            $conexion = $objConectar->conectar();
                        
            $sql = "SELECT cantidad, clase, objeto FROM claves_zoopesos WHERE clave = '".$codigo."' and estado = 0";
		    $res = mysql_query($sql,$conexion) or die( "Error: " . mysql_error(). "  ". $sql ) ;	
			
			if( mysql_num_rows( $res ) == 0 )
			{
				$conexion = $objConectar->desconectar();
				return "error del código";
			}
			
			$cantidad = mysql_result($res,0,"cantidad");		   
			$clase = mysql_result($res,0,1);
			$objeto = mysql_result($res,0,2);
			
			$cantidad = (int)$cantidad;
			
	//		$sql_Actualizar_estado = "UPDATE claves_zoopesos SET estado = 1 WHERE clave = '".$codigo."'";
	//		mysql_query($sql_Actualizar_estado,$conexion);
									
			switch($clase)
			{
				case "dinero":
				
					$sql2 = "UPDATE Dinero_usuarios SET cantidad = cantidad + ".$cantidad." WHERE id_usuario = ".$this->u->id; 
					$res2 = mysql_query($sql2,$conexion);//or die( "Error: " . mysql_error(). "  ". $sql );	
					
					$sql3 = "SELECT cantidad FROM Dinero_usuarios WHERE id_usuario = ".$this->u->id;
					$res3 = mysql_query($sql3,$conexion);//or die( "Error: " . mysql_error(). "  ". $sql );	
					
					$nueva_cantidad = mysql_result($res3,0,"cantidad");
					
					$arreglo[0] =$cantidad;
					$arreglo[1] = $nueva_cantidad;
					
					$t = new TransaccionVO();
					$t->idusuario = $this->u->id;
					$t->fecha = time();
					$t->cantidad = $cantidad;	
					$t->saldo = $nueva_cantidad;
					$t->accion = "abonar(regalo)";
					$t->objeto = 0;
					$t->detalle = "Dinero del código";					
										
					$this->registrarTransaccion($t);									
										
					$sqlSacarIDtrans = "SELECT id FROM transacciones where idusuario = ".$this->u->id." AND fecha = ".$t->fecha;					
					$resSacarIDtrans = mysql_query($sqlSacarIDtrans,$conexion);
									
					$id_transaccion =mysql_result($resSacarIDtrans,0,0);
					
					$sqlUpdateZoopesos = "UPDATE claves_zoopesos set idtransaccion = ".$id_transaccion." WHERE clave = '".$codigo."'";
					$resUpdate = mysql_query($sqlUpdateZoopesos,$conexion);				
										
					$conexion = $objConectar->desconectar();			
					return $arreglo;		
					
					
				break;
				
				case "item":
				
					$sql_verificarExistenciaRegistro = "SELECT * FROM itemsusuario where idUsuario = ".$this->u->id." and claveItem = ".$objeto; 
					$registros = mysql_query($sql_verificarExistenciaRegistro,$conexion);
										
					if(mysql_num_rows( $registros )==0 )
					{
						$sql2 = "INSERT INTO itemsusuario (claveItem,idUsuario,cantidad) VALUES(".$objeto.",".$this->u->id.",".(int)$cantidad.")"; 
						$res2 = mysql_query($sql2,$conexion);//or die( "Error: " . mysql_error(). "  ". $sql );	
					}
					else
					{
						$sql2 = "UPDATE itemsusuario SET cantidad = cantidad + ".(int)$cantidad." where idUsuario = ".$this->u->id." and claveItem = ".$objeto; 
						$res2 = mysql_query($sql2,$conexion);//or die( "Error: " . mysql_error(). "  ". $sql );						
					}
										
					$arreglo[0] = (int) $objeto;
					$arreglo[1] = (int) $cantidad;
					
					$t = new TransaccionVO(); 
					$t->idusuario = $this->u->id;
					$t->fecha = time();
					$t->cantidad = $cantidad;	
					$t->saldo = 0;
					$t->accion = "Item A";
					$t->objeto = $objeto;
					$t->detalle = "Item ganado a través de código";
															
					$this->registrarTransaccion($t);
					
					$sqlSacarIDtrans = "SELECT id from transacciones where idusuario = ".$this->u->id." and fecha = ".$t->fecha;					
					$resSacarIDtrans = mysql_query($sqlSacarIDtrans,$conexion);
				
					$id_transaccion =mysql_result($resSacarIDtrans,0,0);
										
					$sqlUpdateZoopesos = "UPDATE claves_zoopesos set idtransaccion = ".$id_transaccion." WHERE clave = '".$codigo."'";
					$resUpdate = mysql_query($sqlUpdateZoopesos,$conexion);				
										
					$sqlInsertarCompra = "INSERT INTO detallecompras (idTransaccion,idItem,cantidad) VALUES(".$id_transaccion.",".$objeto.",".$cantidad.")";								
					$conexion = $objConectar->desconectar();					
					return $arreglo;
					
				break;
			}															            
		}
	}
	
?>
