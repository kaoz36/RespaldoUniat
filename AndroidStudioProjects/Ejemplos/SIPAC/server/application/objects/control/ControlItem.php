<?php
	include_once 'conexion.php';
	include_once ("vo/ItemVO.php");
	
	class ControlItem
	{
		var $u;
		
		function ControlItem($usr)
		{
			$this->u = $usr;			
		}
		
		function agregarItemAMascota( $arreglo ) //recibo item y mascota
		{
		    $item = $arreglo[0];
			$mascota = $arreglo[1];
			$cantidadItems= $arreglo[2];
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();
           
             
			$sql = "SELECT * FROM itemsmascota where iditem = ".$item->clave." and idmascota = ".$mascota->idservidor;	
			$res = mysql_query($sql,$conexion);
			$num = mysql_num_rows($res);
			if($num>0)
			{
				$sql = "update itemsmascota set cantidad = cantidad + ".$cantidadItems." WHERE idmascota = ".$mascota->idservidor." and iditem = ".$item->clave;
			}
			
			if($num==0)			
			{				
				$sql = "insert into itemsmascota (idmascota, iditem, cantidad) values(".$mascota->idservidor.",".$item->clave.",".$cantidadItems.")";				
				
			}           
			$res3 = mysql_query($sql,$conexion);
			
			$this->cambiarFechaItemsMascota($mascota->idservidor);
			
            $objConectar->desconectar();
			return 1;
			 
			 
			 /*
           // $sql = "SELECT Count(IDITEM), iditem FROM itemsmascota AS A, items AS B WHERE A.idmascota=".$this->u->id." AND A.iditem = B.clave AND B.TIPO = ( SELECT tipo FROM items WHERE clave = ".$item->clave.")";
            $sql = "SELECT Count(IDITEM), iditem FROM itemsmascota AS A, items AS B WHERE A.idmascota=".$mascota->idservidor." AND A.iditem = B.clave AND B.TIPO = ( SELECT tipo FROM items WHERE clave = ".$item->clave.")";
                               
          	$res = mysql_query($sql,$conexion);
          	    
          	$iditem = mysql_result($res,0,1);    
          	          	          	          	                                 
            if(mysql_result($res,0,0)>0)
            {           		       		            		
           	    	
           		$sql3 = " INSERT INTO itemsusuario (claveitem, idUsuario, cantidad) values (".$iditem.",".$this->u->id.", 1 ) ";
           		
           		$res3 = mysql_query($sql3,$conexion);
           		
				//$sql4 = "delete from itemsmascota where iditem=".$iditem." and idmascota=".$this->u->id;
				$sql4 = "delete from itemsmascota where iditem=".$iditem." and idmascota=".$mascota->idservidor;
           		           		
           		$res4 = mysql_query($sql4,$conexion);        
           		
            }
				
				//$sql2 = "INSERT INTO itemsmascota VALUES(".$this->u->id.",".$item->clave.")";				
				$sql2 = "INSERT INTO itemsmascota VALUES(".$mascota->idservidor.",".$item->clave.")";
            		
				$res2 = mysql_query($sql2,$conexion); //or die( "Error: " . mysql_error() );
						
            	$objConectar->desconectar();
				
			    $this->cambiarFechaItemsMascota($mascota->idservidor);
			
				return 1;
				
				*/
		}
		
		function agregarItemAUsuario( $array ) 
		{			
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();
            
             $i = $array[0];
			 $cantidadItems = $array[1];
            
			$sql = "SELECT * FROM itemsusuario where claveItem = ".$i->clave." and idUsuario = ".$this->u->id;
			
			$res = mysql_query($sql,$conexion);
			
			$num = mysql_num_rows($res);

			if($num>0)
			{
				$sql = "update itemsusuario set cantidad = cantidad + ".$cantidadItems." WHERE idUsuario = ".$this->u->id." and claveItem = ".$i->clave;
			}
			
			if($num==0)			
			{				
				$sql = "insert into itemsusuario (idUsuario, claveItem, cantidad) values(".$this->u->id.",".$i->clave.",".$cantidadItems.")";				
				
			}           
			 $res3 = mysql_query($sql,$conexion);
			
			$this->cambiarFechaItems();
			
            $objConectar->desconectar();
			return 1;
		}
		
		function quitarItemAUsuario( $i ) 
		{
			
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();                              
            
			$sql = "update itemsusuario set cantidad = cantidad - 1 WHERE idUsuario = ".$this->u->id." and claveItem = ".$i->clave;			
						
			$res = mysql_query($sql,$conexion); //or die( "Error: " . mysql_error() );						
			
			$sql2 = "SELECT cantidad FROM itemsusuario where idUsuario = ".$this->u->id." and claveItem = ".$i->clave;
						
			$res2 = mysql_query($sql2, $conexion);
			
					while ( $fila= mysql_fetch_array($res2) )
					{						
								$cant = $fila["cantidad"];
								
								//return $cant;
														
								if( $cant == 0 )
								{
									$sql3 = "DELETE FROM itemsusuario where idUsuario = ".$this->u->id." and claveItem = ".$i->clave;
									//return $sql3;
									$res3 = mysql_query($sql3, $conexion); 									
								}								
					}
		
			$this->cambiarFechaItems();
		
			$objConectar->desconectar();	
						
			return 1;
		}
		
		function quitarItemAMascota( $array )
		{
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();
			
			$item = $array[0];
			$masc = $array[1];
            
           // $sql = "DELETE FROM itemsmascota WHERE iditem = ".$item->clave." AND idmascota = ".$masc->clave;    //idmascota, iditem
			$sql = "DELETE FROM itemsmascota WHERE iditem = ".$item->clave." AND idmascota = ".$masc->idservidor;
			$res = mysql_query($sql,$conexion);// or die( "Error: " . mysql_error() );						
		
            $objConectar->desconectar();
			 $this->cambiarFechaItemsMascota($masc->idservidor);
			return 1;
		}
		
		function consultarItemsMascota( $m ) // recibo mascota
		{
			
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();
            
            //$sql = "Select iditem from itemsmascota where idmascota = ".$m->clave;  // Select nombre del item from items where id=(select iditem from itemsmascota where idmascota = $m->id)
            $sql = "Select iditem from itemsmascota where idmascota = ".$m->idservidor;
            //return $sql;
			$res = mysql_query($sql,$conexion); //or die( "Error: " . mysql_error() );
						            
            
			
			//$r = mysql_result($res);
			$c = 0;
			
			while( $fila= mysql_fetch_array($res))
			{				
					$ar[$c]	= (int)$fila[0];
					$c=$c+1;
			}			
			
			$objConectar->desconectar();
			
			return $ar;
			
        }
        
        function consultarItemsUsuario( )
		{			
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();
         		
            $sql = "Select claveItem, cantidad from itemsusuario where idUsuario = ".$this->u->id;  // Select claveItem, cantidad from itemsusuario where idUsuario = $u->id)				
						
			$res = mysql_query($sql,$conexion);// or die( "Error: " . mysql_error() );
						            
            $objConectar->desconectar();
			
//			$r = mysql_result($res,0);
			$c = 0;
			
			while( $fila= mysql_fetch_array($res))
			{				
					$ar[$c]	= (int)$fila[0];
					$ar2[$c]	= (int)$fila[1];
					$c=$c+1;
			}			
			
			$arreglo[0] = $ar;
			$arreglo[1] = $ar2;		
			
			return $arreglo;
        }
        
        function getAllMarcas()
        {
        	$objConectar = new conexion();
            $conexion = $objConectar->conectar();
            
            $sql = "SELECT id, nombre, fechaAct FROM marcas WHERE enable = 1";
            
			$res = mysql_query($sql,$conexion);// or die( "Error: " . mysql_error() );
						            
            $objConectar->desconectar();
			
			$c = 0;
			$arrIds;
			$arrNames;
			$arrDates;
			
			$arreglo;
			
			while( $fila= mysql_fetch_array($res))
			{
				$arrIds[$c] = $fila["id"];
				$arrNames[$c] = $fila["nombre"];
				$arrDates[$c] = $fila["fechaAct"];
				$c=$c+1;
			}
			
			$arreglo[0] = $arrIds;
			$arreglo[1] = $arrNames;	
			$arreglo[2] = $arrDates;
				
			return $arreglo;
        }
        
        
        function obtenerTodosItems()
        {
        	$objConectar = new conexion();
            $conexion = $objConectar->conectar();
            
            //$sql = "Select * from items ";  
            
            //sql cuya consulta pretende obtener los campos de nombre de algunos de los atributos del item
            
            $sql = "SELECT a.clave, a.precio, a.nombre, a.url, a.disponible_offline, a.fecha_agregado, a.fecha_modificado, a.fecha_borrado, b.nombre as tipo, c.nombre as categoria, d.nombre as marca FROM items AS a, tipoitem AS b, categorias AS c, marcas AS d WHERE a.tipo = b.id AND a.categoria = c.id AND a.marca = d.id";
            
			$res = mysql_query($sql,$conexion);// or die( "Error: " . mysql_error() );
					
			$c = 0;
			
   //para enviar un arreglo mixto
	
	/*		while( $fila= mysql_fetch_array($res))
			{				
				$ar[$c]	= $fila;
				
				$c=$c+1;
			}	*/
			
			
	//para enviar los datos de string referenciada		
	/*		while( $fila= mysql_fetch_array($res))
			{				
				for($k=0;$k<9;$k++)
				{
					$a[$k] = $fila[$k];
				}
				
				$ar[$c] = $a;
							
				$c=$c+1;
			}*/
			
	//para enviar los datos normales, ya con índice de texto en el arreglo
	/*		while( $fila= mysql_fetch_array($res))
			{				
				$a["clave"] = (int) $fila["clave"];
				$a["precio"] = (int) $fila["precio"];
				$a["nombre"] = "".$fila["nombre"];
				$a["marca"] = "".$fila["marca"];
				$a["url"] = "".$fila["url"];
				$a["disponible_offline"] = "".$fila["precio"];
				$a["tipo"] = (int) $fila["tipo"];
				$a["fecha_agregado"] = date("m/d/Y", strtotime($fila["fecha_agregado"]));
				$a["fecha_modificado"] = date("m/d/Y", strtotime($fila["fecha_modificado"]));
				
				$ar[$c] = $a;								
				
				$c=$c+1;
			} */			
			
		//para enviar los datos en un arreglo de objetos item
			while( $fila= mysql_fetch_array($res))
			{	
				$it = new ItemVO();
				$it->clave = (int) $fila["clave"];
				$it->precio = (int) $fila["precio"];
				$it->nombre = "".$fila["nombre"];
				$it->marca = "".$fila["marca"];
				$it->url = "".$fila["url"];
				$it->categoria = "".$fila["categoria"];
				$it->disponible_offline = "".$fila["precio"];
				$it->tipo = "".$fila["tipo"];
				
				$it->fecha_agregado = (int) $fila["fecha_agregado"];
				$it->fecha_modificado = (int) $fila["fecha_modificado"];
				$it->fecha_borrado = (int) $fila["fecha_borrado"];
								
				$ar[$c] = $it;								
				
				$c=$c+1;
			}								
				
			$objConectar->desconectar();		
				
			return $ar;
        }
		
		function obtenerNuevosItems($i)
		{
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();
/* ANTES                                    
            $dividido = ($i->fecha_agregado)/1000;            
                       
             //sql cuya consulta pretende obtener los campos de nombre de algunos de los atributos del item
            $sql = "SELECT a.clave, a.precio, a.nombre, a.url, a.disponible_offline, a.fecha_agregado, a.fecha_modificado, a.fecha_borrado, b.nombre as tipo, c.nombre as categoria, d.nombre as marca FROM items AS a, tipoitem AS b, categorias AS c, marcas AS d WHERE a.tipo = b.id AND a.categoria = c.id AND a.marca = d.id and (a.fecha_agregado > FROM_UNIXTIME(".$dividido.") or a.fecha_modificado > FROM_UNIXTIME(".$dividido."))"; 
            //$sql = "Select * from items where fecha_agregado >= FROM_UNIXTIME(".$dividido.") or fecha_modificado >= FROM_UNIXTIME(".$dividido.")"; 
                       
			$res = mysql_query($sql,$conexion);// or die( "Error: " . mysql_error() );
						            
            $conexion = $objConectar->desconectar();
			
			$c=0;		
			
	
		//para enviar los datos en un arreglo de objetos item
			while( $fila= mysql_fetch_array($res))
			{	
				$it = new ItemVO();
				$it->clave = (int) $fila["clave"];
				$it->precio = (int) $fila["precio"];
				$it->nombre = "".$fila["nombre"];
				$it->marca = "".$fila["marca"];
				$it->url = "".$fila["url"];
				$it->categoria = "".$fila["categoria"];
				$it->disponible_offline = "".$fila["precio"];
				$it->tipo = "".$fila["tipo"];
				
				$it->fecha_agregado = $this->formatFecha( $fila["fecha_agregado"]);
				$it->fecha_modificado = $this->formatFecha( $fila["fecha_modificado"] );
				$it->fecha_borrado = $this->formatFecha( $fila["fecha_borrado"] );
								
				$ar[$c] = $it;								
				
				$c=$c+1;
			}											
			
			return $ar; */
			
			//sql cuya consulta pretende obtener los campos de nombre de algunos de los atributos del item
            $sql = "SELECT a.clave, a.precio, a.nombre, a.url, a.disponible_offline, a.fecha_agregado, a.fecha_modificado, a.fecha_borrado, b.nombre as tipo, c.nombre as categoria, d.nombre as marca FROM items AS a, tipoitem AS b, categorias AS c, marcas AS d WHERE a.tipo = b.id AND a.categoria = c.id AND a.marca = d.id and (a.fecha_agregado > ".$i->fecha_agregado." or a.fecha_modificado > ".$i->fecha_agregado.")"; 
                       
			$res = mysql_query($sql,$conexion);// or die( "Error: " . mysql_error() );
						            
            $conexion = $objConectar->desconectar();
			
			$c=0;					
	
		    //para enviar los datos en un arreglo de objetos item
			while( $fila= mysql_fetch_array($res))
			{	
				$it = new ItemVO();
				$it->clave = (int) $fila["clave"];
				$it->precio = (int) $fila["precio"];
				$it->nombre = "".$fila["nombre"];
				$it->marca = "".$fila["marca"];
				$it->url = "".$fila["url"];
				$it->categoria = "".$fila["categoria"];
				$it->disponible_offline = "".$fila["precio"];
				$it->tipo = "".$fila["tipo"];
				
				$it->fecha_agregado = (int) $fila["fecha_agregado"];
				$it->fecha_modificado = (int) $fila["fecha_modificado"];
				$it->fecha_borrado = (int) $fila["fecha_borrado"];
								
				$ar[$c] = $it;								
				
				$c=$c+1;
			}			
				
			return $ar; 			
		}
		
		function formatFecha($f)
		{			
			list($fecha, $tiempo) = split(' ', $f);			
			list($hora, $minutos, $segundos) = split(':', $tiempo);
			list($agno, $mes, $dia) = split('-', $fecha);		
			
			//para que la fecha sea en tiempo de unix
			$fecha =  gmmktime($hora, $minutos, $segundos, $mes, $dia, $agno);
			
			
			//$fecha =  date("Y/m/d h:i:s", mktime($hora, $minutos, $segundos, $mes, $dia, $agno));
			 //date("D M j Y h:i:s", mktime($hora, $minutos, $segundos, $mes, $dia, $agno));
						
			return ($fecha*1000);		
			
			
		}
		
		
		function itemsUsuarioEnviados($arreglo)
		{
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();
		
			$sql = "SELECT claveitem, cantidad FROM itemsusuario where idusuario = ".$this->u->id;
		    $res = mysql_query($sql,$conexion);
		    
		    $c = 0;
		    
		    //$eso ="";
		    	    
		    while( $fila = mysql_fetch_array($res))
			{	
				$ar["item"] = (int)$fila[0];		
				$ar["cantidad"] = (int)$fila[1];
				
				if( array_key_exists((int)$ar["item"],$arreglo) )
				{
					 $Nuevacantidad =  $ar["cantidad"] - $arreglo[(int)$ar["item"]][0];
					 					 
					 if($Nuevacantidad != 0)
					 {					  					 
						 $ar2[$c]["item"] = (int)$ar["item"];
						 $ar2[$c]["cantidad"] = (int)$Nuevacantidad;
						 
						 $arreglo[(int)$ar["item"]][1] = true;
						 $c++;		
					 }	 
					 
					 $arreglo[(int)$ar["item"]][1] = true;				 
				}
				else // indica que ese item no está en el cliente
				{
					 $ar2[$c]["item"] = (int)$ar["item"];
					 $ar2[$c]["cantidad"] = (int)$ar["cantidad"];
					 $c++;
				}   				
			}
					
			while($algo = current($arreglo))
			{
				prev($arreglo);
			}		
						
			next($arreglo);
			
			$copia = $arreglo;

			$copia_key = (int)key($copia);
			
			//$eso = $eso."| ".$copia_key;
			
			while( $copia_key )
			{
				if((boolean)$arreglo[(int)$copia_key][1]==false)
				{
					//	$eso = $eso." * ".$arreglo[(int)$copia_key][1];  
						
					$ar2[$c]["item"] = (int)$copia_key;
					$ar2[$c]["cantidad"] = $arreglo[(int)$copia_key][0] * (-1);
					
					$c++;		
					
					$arreglo[(int)$copia_key][1]=true;											 
				}
				
				next($copia);				
				$copia_key = (int)key($copia);
				//$eso = $eso."| ".$copia_key;
			}			
			//return $eso; 
			
	/*		$sql_time = "SELECT unix_timestamp(now())";
			$res_time =  mysql_query ($sql_time,$conexion);
			
			if(mysql_num_rows($res_time) > 0 ) //El usuario Sí existe
			{
				$fecha = mysql_result($res_time,0,0);
			}
			
			//Se inserta dicha fecha en la base de datos
			$sql_InsertarNuevaFecha = "Update usuario set fechaAct_items =FROM_UNIXTIME(".$fecha.") where clave =".$this->u->id;
			$res2 = mysql_query($sql_InsertarNuevaFecha,$conexion); 
						
			//Se agrega dicha fecha al arreglo que se regresará al cliente					
			$ar2[0]["nueva_fecha"] = $fecha * 1000;
					
			$objConectar->desconectar();						
		    return $ar2;			
				*/		
									
									
			//Se crea nueva fecha de actualizacion de items en el usuario en tiempo Unix
			//$fecha = time();
									
			//Se inserta dicha fecha en la base de datos
			//$sql_InsertarNuevaFecha = "Update usuario set fechaAct_items =".$fecha." where clave =".$this->u->id;
			//$res2 = mysql_query($sql_InsertarNuevaFecha,$conexion); 
						
			$sql_time = "SELECT fechaAct_items FROM usuario WHERE clave = ".$this->u->id;
			$res_time =  mysql_query ($sql_time,$conexion);
			
			if(mysql_num_rows($res_time) > 0 ) //El usuario Sí existe
			{
				$fecha =  mysql_result($res_time,0,0);
			} 
								
			//Se agrega dicha fecha al arreglo que se regresará al cliente					
			$ar2[0]["nueva_fecha"] = $fecha;
					
			$objConectar->desconectar();						
		    return $ar2;	  	    		    
		}
		
		function itemsMascotaEnviados($arreglo)
		{
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();
			
			$idServidorMascota=(int)$arreglo[0];
			$arreglo=$arreglo[1];
		
			$sql = "SELECT iditem,cantidad FROM itemsmascota a WHERE idmascota =".$idServidorMascota;
		    $res = mysql_query($sql,$conexion);
		    
		    $c = 0;
		    
		    //$eso ="";
		    	    
		    while( $fila = mysql_fetch_array($res))
			{	
				$ar["item"] = (int)$fila[0];		
				$ar["cantidad"] = (int)$fila[1];
				
				if( array_key_exists((int)$ar["item"],$arreglo) )
				{
					 $Nuevacantidad =  $ar["cantidad"] - $arreglo[(int)$ar["item"]][0];
					 					 
					 if($Nuevacantidad != 0)
					 {					  					 
						 $ar2[$c]["item"] = (int)$ar["item"];
						 $ar2[$c]["cantidad"] = (int)$Nuevacantidad;
						 
						 $arreglo[(int)$ar["item"]][1] = true;
						 $c++;		
					 }	 
					 
					 $arreglo[(int)$ar["item"]][1] = true;				 
				}
				else // indica que ese item no está en el cliente
				{
					 $ar2[$c]["item"] = (int)$ar["item"];
					 $ar2[$c]["cantidad"] = (int)$ar["cantidad"];
					 $c++;
				}   				
			}
					
			while($algo = current($arreglo))
			{
				prev($arreglo);
			}		
						
			next($arreglo);
			
			$copia = $arreglo;

			$copia_key = (int)key($copia);
			
			//$eso = $eso."| ".$copia_key;
			
			while( $copia_key )
			{
				if((boolean)$arreglo[(int)$copia_key][1]==false)
				{
					//	$eso = $eso." * ".$arreglo[(int)$copia_key][1];  
						
					$ar2[$c]["item"] = (int)$copia_key;
					$ar2[$c]["cantidad"] = $arreglo[(int)$copia_key][0] * (-1);
					
					$c++;		
					
					$arreglo[(int)$copia_key][1]=true;											 
				}
				
				next($copia);				
				$copia_key = (int)key($copia);
				//$eso = $eso."| ".$copia_key;
			}			
			
			$sql_time = "SELECT fechaAct_items FROM mascotas WHERE IDclave = ".$idServidorMascota;
			$res_time =  mysql_query ($sql_time,$conexion);
			
			if(mysql_num_rows($res_time) > 0 ) //El usuario Sí existe
			{
				$fecha =  mysql_result($res_time,0,0);
			} 
								
			//Se agrega dicha fecha al arreglo que se regresará al cliente					
			$ar2[0]["nueva_fecha"] = $fecha;
					
			$objConectar->desconectar();						
		    return $ar2;	  	    		    
		}
		
		
		function cambiarFechaItems()
		{
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();
		
			$sql = "UPDATE usuario SET fechaAct_items = ( SELECT unix_timestamp(now()) ) WHERE clave =".$this->u->id;
		    $res = mysql_query($sql,$conexion);
		    
		    $objConectar->desconectar();
		}
		
		function cambiarFechaItemsMascota($id)
		{
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();
		
			$sql = "UPDATE mascotas SET fechaAct_items = ( SELECT unix_timestamp(now()) ) WHERE idclave =".$id;
		    $res = mysql_query($sql,$conexion);
		    
		    $objConectar->desconectar();
		}
		
		function compararFechasMarcas($F)
		{		
			$objConectar = new conexion();
	        $conexion = $objConectar->conectar();
			
			$sql = "SELECT MAX(fechaAct) FROM marcas";
			$res = mysql_query($sql,$conexion);
			 			 	 		 		 		 
			//$fecha = $this->formatFecha(mysql_result($res,0,0));
			$fecha = mysql_result($res,0,0);
			
			$arreglo_respuesta[1] = "marcas";
			 		
			if( $fecha == $F) // si fecha del servidor es igual a la del cliente 
			{
				$arreglo_respuesta[0] = true;			
				return $arreglo_respuesta;
			}
			else 
			{
				$arreglo_respuesta[0] = false;			
				return $arreglo_respuesta;
			}		
		
		}
		
		function marcasEnviadas($arreglo)
		{
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();
		
			$sql = $sql = "SELECT id, nombre, fechaAct FROM marcas WHERE enable = 1";
		    $res = mysql_query($sql,$conexion);
		    
		    $c = 0;
		    		    	    
		    while( $fila = mysql_fetch_array($res))
			{	
				$a["id"] = (int)$fila[0];		
				$a["nombre"] = $fila[1];
				$a["fechaAct"] = (int)$fila[2];
				
				$verificador = false;
				for ($n=0; $n < sizeof($arreglo); $n++)
				{	
						if($arreglo[$n][0] == $a["id"] && (int)$arreglo[$n][1] == $a["fechaAct"])
						{
							$arreglo[$n][2] = true;					
							$verificador = true;										
							break 1;
						}				
				}
										
				if ($verificador == false) //indica que ese amigo no se tiene en el cliente
				{				
					$ar[$c][0] = $a["id"];
					$ar[$c][1] = $a["nombre"];
					$ar[$c][2] = $a["fechaAct"];		
					$c++;				
				}
			}
				
			for($k=0 ;$k < sizeof($arreglo); $k++ )
			{
				if($arreglo[$k][2] == false && $arreglo[$k][0]!= 0 )
				{
					$ar[$c][0] = (int)$arreglo[$k][0]  * (-1);
					$ar[$c][1] = (int)$arreglo[$k][1];
					$c++;
				}
			}
	
			$sql_time = "SELECT MAX(fechaAct) FROM marcas";
			$res_time =  mysql_query ($sql_time,$conexion);
			$fecha =  mysql_result($res_time,0,0);		 
								
			//Se agrega dicha fecha al arreglo que se regresará al cliente					
			$ar["nueva_fecha"] = (int) $fecha;
					
			$objConectar->desconectar();						
		    return $ar;				    		   
		
		}
		
	}	
?>
