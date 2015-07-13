<?php
	include_once 'conexion.php';
	include_once 'vo/AmigoVO.php';
	
class ControlAmigos
{
	var $usrGlb;
	
	function ControlAmigos($usr){
		$this->usrGlb=$usr;
		}
	
	function consultaSolicitudes( )
	{
	   $objConectar = new conexion();
       $conexion = $objConectar->conectar();
	   
	   $usuario = $this->usrGlb;
	   /*
	   $sql =  "SELECT clave, cumple, login, foto, correo, nombreUSR, FB_USER, TW_USER, YT_USER, nombreMSC, idMSC
				FROM (
				
				SELECT login, clave, cumple, foto, correo, nombreUSR, FB_USER, TW_USER, nombreMSC, idMSC
				FROM (
				
				SELECT login, clave, cumple, foto, correo, nombreUSR, CONCAT(link,user) AS FB_USER, nombreMSC, idMSC
				FROM (
				
				SELECT U.clave, U.cumple, U.login, U.foto, U.correo, U.nombre as nombreUSR, M.nombre as nombreMSC, M.IDclave as idMSC
				FROM usuario U, mascotas M
				WHERE (clave IN ( SELECT idamigo FROM lista_amigos WHERE aceptado = 0 and idusuario = ".$usuario->id." )
								 or clave IN ( SELECT idusuario FROM lista_amigos WHERE aceptado = 0 and idamigo = ".$usuario->id." )) and M.iddueno = U.clave
				
				)U
				LEFT JOIN (
				
				SELECT * 
				FROM usuario_social, social
				WHERE id_red_social =1
				AND social.id = id_red_social
				)FB ON FB.id_usuario = U.clave
				)GFB
				LEFT JOIN (
				
				SELECT id_usuario, CONCAT(link,user) AS TW_USER
				FROM usuario_social, social
				WHERE id_red_social =2
				AND social.id = id_red_social
				)TW ON TW.id_usuario = GFB.clave
				)GFT
				LEFT JOIN (
				
				SELECT id_usuario, CONCAT(link,user) AS YT_USER
				FROM usuario_social, social
				WHERE id_red_social =3
				AND social.id = id_red_social
				)YT ON YT.id_usuario = GFT.clave ";	
		*/
		
		
		
		$sql =  "SELECT * FROM (SELECT clave, login, foto, correo, nombreUSR, FB_USER, TW_USER, YT_USER, nombreMSC, idMSC
				FROM (
				
				SELECT login, clave, foto, correo, nombreUSR, FB_USER, TW_USER, nombreMSC, idMSC
				FROM (
				
				SELECT login, clave, foto, correo, nombreUSR, CONCAT(link,user) AS FB_USER, nombreMSC, idMSC
				FROM (
				
				SELECT U.clave, U.login, U.foto, U.correo, U.nombre as nombreUSR, M.nombre as nombreMSC, M.IDclave as idMSC
				FROM usuario U, mascotas M
				
				WHERE (clave IN ( SELECT idamigo FROM lista_amigos WHERE aceptado = 0 and idusuario = ".$usuario->id." )
								 or clave IN ( SELECT idusuario FROM lista_amigos WHERE aceptado = 0 and idamigo = ".$usuario->id." )) and M.iddueno = U.clave
				
				)U
				LEFT JOIN (
				
				SELECT * 
				FROM usuario_social, social
				WHERE id_red_social =1
				AND social.id = id_red_social
				)FB ON FB.id_usuario = U.clave
				)GFB
				LEFT JOIN (
				
				SELECT id_usuario, CONCAT(link,user) AS TW_USER
				FROM usuario_social, social
				WHERE id_red_social =2
				AND social.id = id_red_social
				)TW ON TW.id_usuario = GFB.clave
				)GFT
				LEFT JOIN (
				
				SELECT id_usuario, CONCAT(link,user) AS YT_USER
				FROM usuario_social, social
				WHERE id_red_social =3
				AND social.id = id_red_social
				)YT ON YT.id_usuario = GFT.clave ) H

                LEFT JOIN (

                SELECT idamigo COD, aceptado FROM lista_amigos WHERE idusuario = ".$this->usrGlb->id."
                UNION
                SELECT idusuario, aceptado - 10 FROM lista_amigos WHERE idamigo = ".$this->usrGlb->id."

                ) EA ON H.clave = EA.COD";
		
		
		//////////////
		
		/*
		$palabraClave = "";
		
		$sql =  "SELECT * FROM (SELECT clave, login, foto, correo, nombreUSR, FB_USER, TW_USER, YT_USER, nombreMSC, idMSC
				FROM (
				
				SELECT login, clave, foto, correo, nombreUSR, FB_USER, TW_USER, nombreMSC, idMSC
				FROM (
				
				SELECT login, clave, foto, correo, nombreUSR, CONCAT(link,user) AS FB_USER, nombreMSC, idMSC
				FROM (
				
				SELECT U.clave, U.login, U.foto, U.correo, U.nombre as nombreUSR, M.nombre as nombreMSC, M.IDclave as idMSC
				FROM usuario U, mascotas M
				WHERE (
							(
							U.correo like '".$palabraClave."'
							or U.nombre like '%".$palabraClave."%'
							or U.login like '%".$palabraClave."%'
							)
							and U.clave not in (".$this->usrGlb->id.")
				
				) and M.iddueno = U.clave
				
				)U
				LEFT JOIN (
				
				SELECT * 
				FROM usuario_social, social
				WHERE id_red_social =1
				AND social.id = id_red_social
				)FB ON FB.id_usuario = U.clave
				)GFB
				LEFT JOIN (
				
				SELECT id_usuario, CONCAT(link,user) AS TW_USER
				FROM usuario_social, social
				WHERE id_red_social =2
				AND social.id = id_red_social
				)TW ON TW.id_usuario = GFB.clave
				)GFT
				LEFT JOIN (
				
				SELECT id_usuario, CONCAT(link,user) AS YT_USER
				FROM usuario_social, social
				WHERE id_red_social =3
				AND social.id = id_red_social
				)YT ON YT.id_usuario = GFT.clave ) H

                LEFT JOIN (

                SELECT idamigo COD, aceptado FROM lista_amigos WHERE idusuario = ".$this->usrGlb->id."
                UNION
                SELECT idusuario, aceptado - 10 FROM lista_amigos WHERE idamigo = ".$this->usrGlb->id."

                ) EA ON H.clave = EA.COD";
		*/
		/////////////
				
		$res = mysql_query($sql,$conexion) or die("Error: ".mysql_error()."\n".$sql);
		
		$i = 0;
	   while( $fila = mysql_fetch_array($res) )
	   {
			$amigos[$i] = $this->getAmigoVO( $fila );
			$i++;
	   }
	   
 
	   return $amigos;
	
	}
	
	function consultaAmigosVO( )
	{
	   $objConectar = new conexion();
       $conexion = $objConectar->conectar();
	   
	   $usuario = $this->usrGlb;
	   
	   //$misAmigos = "SELECT idamigo FROM lista_amigos WHERE aceptado = 1 and idusuario = ".$usuario->id;
	   //$soyAmigo =  "SELECT idusuario FROM lista_amigos WHERE aceptado = 1 and idamigo = ".$usuario->id;
	   
	   //$listaAmigos = "SELECT idamigo FROM lista_amigos WHERE idusuario = ".$usuario->id;
	   //$sql = "SELECT clave, nombre, cumple, foto, correo, login FROM usuario WHERE clave IN ( ".$misAmigos." ) or clave IN ( ".$soyAmigo." )";
	   
	   //id, Foto, Nombre, login, *email*, mascota(id, nombre, foto), redes sociales[array],
	   
		//$sql = "SELECT U.clave, U.foto, U.nombre, U.email FROM usuario U WHERE clave IN  ( ".$misAmigos." ) or clave IN ( ".$soyAmigo." )";
		
		/*
		$sql = 	"SELECT clave, login, correo, nombreUSR FB_USER, TW_USER, YT_USER, nombreMSC, idMSC
				FROM (
				
				SELECT login, clave, correo, nombreUSR, FB_USER, TW_USER, nombreMSC, idMSN
				FROM (
				
				SELECT login, clave, correo, nombreUSR, user, nombreMSC, idMSC AS FB_USER
				FROM (
				
				SELECT U.clave, U.login, U.correo, U.nombre as nombreUSR, M.nombre as nombreMSC, M.IDclave as idMSC
				FROM usuario U, mascota M
				WHERE (clave IN ( SELECT idamigo FROM lista_amigos WHERE aceptado = 1 and idusuario = ".$usuario->id." ) or clave IN ( SELECT idusuario FROM lista_amigos WHERE aceptado = 1 and idamigo = ".$usuario->id." )) and M.iddueño = U.clave
				
				)U
				LEFT JOIN (
				
				SELECT * 
				FROM usuario_social
				WHERE id_red_social =1
				)FB ON FB.id_usuario = U.clave
				)GFB
				LEFT JOIN (
				
				SELECT id_usuario, user AS TW_USER
				FROM usuario_social
				WHERE id_red_social =2
				)TW ON TW.id_usuario = GFB.clave
				)GFT
				LEFT JOIN (
				
				SELECT id_usuario, user AS YT_USER
				FROM usuario_social
				WHERE id_red_social =3
				)YT ON YT.id_usuario = GFT.clave 
				
				";
				
		*/
		
		$sql =  "SELECT clave, cumple, login, foto, correo, nombreUSR, FB_USER, TW_USER, YT_USER, nombreMSC, idMSC
				FROM (
				
				SELECT login, clave, cumple, foto, correo, nombreUSR, FB_USER, TW_USER, nombreMSC, idMSC
				FROM (
				
				SELECT login, clave, cumple, foto, correo, nombreUSR, CONCAT(link,user) AS FB_USER, nombreMSC, idMSC
				FROM (
				
				SELECT U.clave, U.cumple, U.login, U.foto, U.correo, U.nombre as nombreUSR, M.nombre as nombreMSC, M.IDclave as idMSC
				FROM usuario U, mascotas M
				WHERE (clave IN ( SELECT idamigo FROM lista_amigos WHERE aceptado = 1 and idusuario = ".$usuario->id." )
								 or clave IN ( SELECT idusuario FROM lista_amigos WHERE aceptado = 1 and idamigo = ".$usuario->id." )) and M.iddueno = U.clave
				
				)U
				LEFT JOIN (
				
				SELECT * 
				FROM usuario_social, social
				WHERE id_red_social =1
				AND social.id = id_red_social
				)FB ON FB.id_usuario = U.clave
				)GFB
				LEFT JOIN (
				
				SELECT id_usuario, CONCAT(link,user) AS TW_USER
				FROM usuario_social, social
				WHERE id_red_social =2
				AND social.id = id_red_social
				)TW ON TW.id_usuario = GFB.clave
				)GFT
				LEFT JOIN (
				
				SELECT id_usuario, CONCAT(link,user) AS YT_USER
				FROM usuario_social, social
				WHERE id_red_social =3
				AND social.id = id_red_social
				)YT ON YT.id_usuario = GFT.clave ";	
				
		$res = mysql_query($sql,$conexion) or die("Error: ".mysql_error()."\n".$sql);
		
		$i = 0;
	   while( $fila = mysql_fetch_array($res) )
	   {
			$amigos[$i] = $this->getAmigoVO( $fila );
			$i++;
	   }
	   
 
	   return $amigos;
	}
	
	function getAmigoVO( $fila )
	{
		$usr = new AmigoVO();
		
		/*
		var $id;
		var $foto;
		var $nombre;
		var $login;
		var $email;
		var $cumple;
		var $email;
		var $mascotaId;
		var $mascotaNombre;
		var $mascotaFoto;
		var $redesSociales; //Facebook, twitter, youtube
		*/
		
		//SELECT clave, login, foto, correo, nombreUSR, FB_USER, TW_USER, YT_USER, nombreMSC, idMSC
		
		$usr->id = $fila["clave"];
		$usr->foto = $fila["foto"];
		$usr->nombre = $fila["nombreUSR"];
		$usr->login = $fila["login"];
		$usr->cumple = $this->formatFecha( $fila["cumple"] );
		$usr->email = $fila["correo"];
		$usr->mascotaId = $fila["idMSC"];
		$usr->mascotaNombre = $fila["nombreMSC"];
		$usr->mascotaFoto = $fila["clave"].".png";
		
		
		//Los "aceptado" negativos son aquellos a los que yo les mandé invitación
		//Los "aceptado" positivos son aquellos que me la mandaron invitación
		
		
		if($fila["aceptado"] == null)
		{
			$usr->accion = 5;
		}
		else
		{
			if( $fila["aceptado"] < 0 )
			{
				switch( $fila["aceptado"] + 10 )
				{
					case 0:
						$usr->accion = 3;
					break;
					
					case 1:
						$usr->accion = 1;
					break;
				}
			}
			else
			{
				switch( $fila["aceptado"] )
				{
					case 0:
						$usr->accion = 4;
					break;
					
					case 1:
						$usr->accion = 2;
					break;
				}
			}
		}
		
		
		$redes[0] = $fila["FB_USER"];
		$redes[1] = $fila["TW_USER"];
		$redes[2] = $fila["YT_USER"];
		
		$usr->redesSociales = $redes;
		
		return $usr;
	}
	/*
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
	*/
	
	function formatFecha($f)
	{			
		list($agno, $mes, $dia) = split('-', $f);
			
		$fecha =  gmmktime(0, 0, 0, (int)$mes, (int)($dia + 1), (int)($agno));
						
		return ($fecha*1000);
	}
	
	
	function consultaAmigos(  )
	{
	   $objConectar = new conexion();
       $conexion = $objConectar->conectar();
	   
	   $misAmigos = "SELECT idamigo FROM lista_amigos WHERE aceptado = 1 and idusuario = ".$this->usrGlb->id;
	   $soyAmigo =  "SELECT idusuario FROM lista_amigos WHERE aceptado = 1 and idamigo = ".$this->usrGlb->id;
	   
	   //$listaAmigos = "SELECT idamigo FROM lista_amigos WHERE idusuario = ".$usuario->id;
	   $sql = "SELECT clave, nombre, cumple, foto, correo, login FROM usuario WHERE clave IN ( ".$misAmigos." ) or clave IN ( ".$soyAmigo." )";
	   
	   $res = mysql_query( $sql , $conexion );
	   
	   $amigos = array();
	   
	   $i=0;
	   while( $fila = mysql_fetch_array($res) )
	   {
			$amigos[$i] = $this->getUsuario( $fila );
			$i++;
	   }
	   
	   return $amigos;
	   
	}
	
	function getUsuario( $fila )
	{
		$usr = new UsuarioVO();
		
		$usr->id = $fila["clave"];
		$usr->firstname = $fila["nombre"];
		$usr->login = $fila["login"];
		$usr->cumple = $fila["cumple"];
		$usr->email = $fila["correo"];
		
		return $usr;
	}
	
	function agregarAmigo( $amigo  ) //El primer elemento es el solicitante y el segundo el solicitado. Este arreglo debe tener sólo 2 elementos
	{
		
	    $objConectar = new conexion();
        	$conexion = $objConectar->conectar();
		
		$usuario = $this->usrGlb;
		
		$sql = "SELECT aceptado FROM lista_amigos WHERE ( idamigo = ".$amigo->id." and idusuario = ".$usuario->id." ) or ( idusuario = ".$amigo->id." and idamigo = ".$usuario->id." )";
	    $res = mysql_query($sql,$conexion) or die("No se pudo leer el vinculo");
	   
	   if( mysql_num_rows($res) ) //Ya hay registro de amistad
	   {
		   $this->aceptarAmigo($amigo); // Se llama a esta función, ya que, en teoría,
		   										  // el que está haciendo la solicitud es el
												  // segundo usuario, pues al primero se le
												  // bloquearía la opción de volver a solicitar amistad.
		   
		   //Cambia la fecha de modificacion de amigos
		   $this->cambiarFechaAmigos($this->usrGlb->id);
		   
		   return mysql_result($res,0,0); //Regreso el estado actual de la amistad
	   }
	   else //No hay rastro alguno de la amistad =P
	   {
		   $sql = " INSERT INTO lista_amigos VALUES(".$amigo->id.",".$usuario->id.",default); ";
		   //return $sql;
		   $res = mysql_query($sql,$conexion) or die("No se pudo agregar el vinculo de amistad");
		   
		   //Cambia la fecha de modificacion de amigos
		   $this->cambiarFechaAmigos($this->usrGlb->id);
		   
		   return $this-> consultaAmigos ( $usuario ) ;
	   }
	   
	   
	   //$listaAmigos = "SELECT idamigo FROM lista_amigos WHERE idusuario = ".$usuario->id;
	   //$sql = "SELECT clave, nombre, cumple, foto, correo, login FROM usuario WHERE clave IN ( ".$misAmigos." ) or clave IN ( ".$soyAmigo." )";
			
	}
	
	function aceptarAmigo( $amigo )
	{
		$objConectar = new conexion();
        $conexion = $objConectar->conectar();
		
		$usuario = $this->usrGlb;
		
		$sql = "UPDATE lista_amigos SET aceptado = 1 WHERE ( idamigo = ".$amigo->id." and idusuario = ".$usuario->id." ) or ( idusuario = ".$amigo->id." and idamigo = ".$usuario->id." )";
		$res = mysql_query($sql,$conexion) or die("No se pudo confirmar el vinculo de amistad");
		
		//return $this->;
		
		$this->cambiarFechaAmigos($this->usrGlb->id);	
	}
	
	function eliminarAmigo( $amigo )
	{
		$objConectar = new conexion();
        $conexion = $objConectar->conectar();
		
		$usuario = $this->usrGlb;
		
		//$sql = "DELETE FROM lista_amigos WHERE ( idamigo = ".$amigo->id." and idusuario = ".$usuario->id." ) or ( idusuario = ".$amigo->id." and idamigo = ".$usuario->id." )";
		$sql = "UPDATE lista_amigos SET aceptado = -1 WHERE ( idamigo = ".$amigo->id." and idusuario = ".$usuario->id." ) or ( idusuario = ".$amigo->id." and idamigo = ".$usuario->id." )";
		$res = mysql_query($sql,$conexion) or die("No se pudo eliminar el vinculo de amistad");
		
		$this->cambiarFechaAmigos($this->usrGlb->id);
		
		return $this-> consultaAmigos ( $usuario ) ;
	}
	
	function amigosUsuarioEnviados($arreglo)
	{
		$objConectar = new conexion();
        $conexion = $objConectar->conectar();
	
		$amigos_servidor = $this->consultaAmigosVO();
							
	    $c = 0;	    
	    $i = 0;
	    
 		for($cont=0 ; $cont < sizeof($amigos_servidor); $cont++)
 		{
 			$verificador = false;
 			
 			for ($n=0; $n < sizeof($arreglo); $n++)
			{	
				if($arreglo[$n][0] == (int)$amigos_servidor[$cont]->id)
				{
					$arreglo[$n][1] = true;					
					$verificador = true;										
					break 1;
				}				
			}  
				
			if ($verificador == false) //indica que ese amigo no se tiene en el cliente
			{				
				$ar[$c] = $amigos_servidor[$cont];
				$c++;				
			}				
		}	
		
		for($k=0 ;$k < sizeof($arreglo); $k++ )
		{
			if($arreglo[$k][1] == false && $arreglo[$k][0]!= 0 )
			{
				$ar[$c] = (int)$arreglo[$k][0]  * (-1);
				$c++;
			}
		}
		
		$sql_time = "SELECT fechaAct_amigos FROM usuario WHERE clave = ".$this->usrGlb->id;
		$res_time =  mysql_query ($sql_time,$conexion);
		$fecha =  mysql_result($res_time,0,0);		 
							
		//Se agrega dicha fecha al arreglo que se regresará al cliente					
		$ar["nueva_fecha"] = (int) $fecha;
				
		$objConectar->desconectar();						
	    return $ar;	  	    	
	}
	
	/*
	function consultaAmigosConId( $arregloAmigos ) //Recibo un arreglo de ussuarios que sólo tienen ID
	{
		for($i=0; $i< count( $arregloAmigos ) ; $i++)
		{
			$arregloAmigos[$i] = $this->consultarPerson( $arregloAmigos[$i] );
		}
		
		return $amigos;
	}
	*/
	
	function cambiarFechaAmigos($id)
	{
		$objConectar = new conexion();
        $conexion = $objConectar->conectar();
	
		$sql = "UPDATE usuario SET fechaAct_amigos = ( SELECT unix_timestamp(now()) ) WHERE clave =".$id;
	    $res = mysql_query($sql,$conexion);
	    
	    $objConectar->desconectar();
	}
	
}

?>
