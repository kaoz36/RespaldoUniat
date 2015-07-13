<?PHP

	include_once 'conexion.php';
	include_once 'vo/AmigoVO.php';
	include_once 'vo/TemplateCorreo_Confirmacion.php';
	include_once 'ControlCorreos.php';
	//include_once 'geolocation.class.php';
	include_once 'ipinfodb.class.php';

class ControlUsuario
{
	var $usrGlb;
	
	function ControlUsuario($usr){
		$this->usrGlb=$usr;
	}
	
	function uploadUserPicture ( $pInfos )
	{
		$usuario = $this->usrGlb;
		
		$bytearray = $pInfos["jpegstream"];
		// bytearray is in the ->data property
		$imageData = $bytearray->data;
		$idimage = "usuario_".$usuario->id.".png";
		
		$objConectar = new conexion();
       $conexion = $objConectar->conectar();
       
       $sql = "UPDATE  `usuario` SET  `foto` = 'http://www.boomonsters.com/app/content/data/users/pic/".$idimage."' WHERE  `clave` =".$usuario->id;
       
       //return $sql;
       $res = mysql_query($sql,$conexion);
		
		return ( $success = file_put_contents("../../app/content/data/users/pic/".$idimage, $imageData) ) ? $idimage : $success;
	    
	}

	
	/*function ifUserConfirmed( )
	{
	   $usuario = $this->usrGlb;
	
	   $objConectar = new conexion();
       $conexion = $objConectar->conectar();
       
       $sql = "SELECT clave FROM usuario WHERE (correo = '".$usuario->email."' or login = '".$usuario->email."') and Confirmado = 1";
       
       //return $sql;
       $res = mysql_query($sql,$conexion);
       
       if( mysql_num_rows($res) > 0 )
       {
       		$val = mysql_result($res,0,0);
       		return (int)$val;
       }
       
       return 0;
       
	}*/
	
	/*function buscaEmail( $clave )
	{
	
	   $objConectar = new conexion();
       $conexion = $objConectar->conectar();
       
       $sql = "SELECT clave FROM usuario WHERE correo = '".$clave."'";
       $res = mysql_query($sql,$conexion);
       
       $c = mysql_num_rows($res);
       if($c < 0)
       return -1;
       else
       return $c;
       
	}*/

	/*function buscaLogin( $clave )
	{
	
	   $objConectar = new conexion();
       $conexion = $objConectar->conectar();
       
       $sql = "SELECT clave FROM usuario WHERE login = '".$clave."'";
       $res = mysql_query($sql,$conexion);
       
       $c = mysql_num_rows($res);
       if($c < 0)
       return -1;
       else
       return $c;
	}*/

	/*function buscaUsuario($palabraClave)
	{
	   $objConectar = new conexion();
       $conexion = $objConectar->conectar();
       
       //$usuario = $this->usrGlb;
       //return $this->usrGlb;
           
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
       
		$res = mysql_query($sql,$conexion) or die("Error: ".mysql_error()."\n".$sql);
		
		$i = 0;
		
	   while( $fila = mysql_fetch_array($res) )
	   {
			$amigos[$i] = $this->getAmigoVO( $fila );
			$i++;						
	   }
	   
	   return $amigos;

    }*/
    
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
		$usr->cumple = $fila["cumple"];
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
		
//		$usr->accion = $fila["aceptado"];
		//$redes = new array();
		
		$redes[0] = $fila["FB_USER"];
		$redes[1] = $fila["TW_USER"];
		$redes[2] = $fila["YT_USER"];
		
		$usr->redesSociales = $redes;
		
		return $usr;
	}
		
	function consultaAmigos( $usuario )
	{
	   $objConectar = new conexion();
       $conexion = $objConectar->conectar();
	   
	   $misAmigos = "SELECT idamigo FROM lista_amigos WHERE aceptado = 1 and idusuario = ".$usuario->id;
	   $soyAmigo =  "SELECT idusuario FROM lista_amigos WHERE aceptado = 1 and idamigo = ".$usuario->id;
	   
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
		
		/*
		var $id;
		var $firstname;
		var $login;
		var $cumple;
		var $email;
	    $sql = "SELECT clave, nombre, cumple, foto, correo, login FROM usuario WHERE clave IN ( ".$listaAmigos." )";
		*/
		
		$usr->id = $fila["clave"];
		$usr->firstname = $fila["nombre"];
		$usr->login = $fila["login"];
		$usr->cumple = $fila["cumple"];
		$usr->email = $fila["correo"];
		
		return $usr;
	}	
	
	function autentificacion( $person ) //$person sólo tiene asignados sólamente los valores (email/loggin) y (password). Acceso a la base de datos para obtener la información completa
	{
            
		//He aquí nuestra antigua cadena de conexión
		//$conexion = mysql_connect("10.6.186.15","boobob", "Trebox3");     
                //mysql_select_db("boobob",$conexion)or die ("no se pudo selecionar boobob");
	   
       $objConectar = new conexion();
       $conexion = $objConectar->conectar();

		$sql = "SELECT clave, nombre, cumple, foto, correo, login, fechaAct_items, fechaAct_mensajes, fechaAct_amigos FROM usuario WHERE (correo = '".$person->email."' or login = '".$person->email."') and password = '".$person->contrasenia."' and confirmado = 1 ";
		       
		
		//Busco al usuario en el que coincidan el email y el password, además de que ya tiene que haberse confirmado.
		
		//$sql = "SELECT nombre, cumple, foto, confirmado, correo FROM usuario WHERE clave = ".$person->clave;
		
		//return $sql;
		$res = mysql_query($sql,$conexion) or die("Errorsazo: ".mysql_error()." (".$sql.")");

        $objConectar->desconectar();

		if(mysql_num_rows($res) > 0 ) //El usuario Sí existe
		{
				$person->id = mysql_result($res,0,0);
				$person->firstname = mysql_result($res,0,1);
				$person->cumple = mysql_result($res,0,2);
				$person->foto = mysql_result($res,0,3);
				$person->email = mysql_result($res,0,4);
				$person->login = mysql_result($res,0,5);
				$person->confirmado = 1;
				$person->fechaAct_items = mysql_result($res,0,6);
				$person->fechaAct_mensajes = mysql_result($res,0,7);
				$person->fechaAct_amigos = mysql_result($res,0,8);
				//$person->confirmado = mysql_result($res,0,4);
				
				//sacar el dinero del usuario
				 $objConectar = new conexion();
       			$conexion = $objConectar->conectar();
				$sql = "Select cantidad from Dinero_usuarios where id_usuario = ".$person->id;    
             	$res = mysql_query($sql,$conexion);
       			$objConectar->desconectar();
				$person->dinero = mysql_result($res,0,0);
				return $person; //Con la información leída de la base de datos
		}
		else //El usuario NO existe
		{
			return 0;
		}
	}
		
	//Creo que esta función ya está obsoleta	
	function consultarPerson( $person ) //Recibo un "person" que sólo tiene el dato clave. Acceso a la base de datos y obtendré toda la información
	{
            $objConectar = new conexion();
            $conexion = $objConectar->conectar();
		
		$sql = "SELECT nombre, cumple, foto, confirmado, correo, login, fechaAct_items, fechaAct_mensajes, fechaAct_amigos FROM usuario WHERE clave = ".$person->id;
		$res = mysql_query($sql,$conexion) or die( "Error: " . mysql_error() );
		
		if(mysql_num_rows($res) > 0 ) //El usuario Sí existe
		{
				$person->firstname = mysql_result($res,0,0);
				$person->cumple = mysql_result($res,0,1);
				$person->foto = mysql_result($res,0,2);
				$person->confirmado = mysql_result($res,0,3);
				$person->email = mysql_result($res,0,4);
				$person->login = mysql_result($res,0,5);
				$person->fechaAct_items = mysql_result($res,0,6);
				$person->fechaAct_mensajes = mysql_result($res,0,7);
				$person->fechaAct_amigs = mysql_result($res,0,8);
				
				
				return $person; //Con la información leída de la base de datos
		}
		else //El usuario NO existe
		{
			return "error";
		}
		
	}
	
	function registerPersonWEBFB($arr) //Es un arreglo que recibe UsuarioConMascota, foto y claves de FB (otro arreglo, supongo)
	{
		//Debido a la problemática que enfrentamos para registrar a un usuario desde web
		//(El usuario no se había validado y por ende no se podía agregar la foto o se podía hacer con riesgo de seguridad)
		//Decidimos hacer esta función que resuelve el problema de registro y graba en el servidor la foto sin confirmar al user.
		
		$dato1 = $this->registerPerson( $arr[0] );

		$this->usrGlb = $dato1;
		$this->usrGlb->clave = $dato1->clave;
		
		$arr33 = array();
		$arr33[0] = $dato1;
		$arr33[1] = $this->usrGlb;
		//return $arr33;

		
		//$dato2 = $this->uploadUserPicture( $arr[1] );
		//$usuario = $this->usrGlb;
		
		$bytearray = $arr[1]["jpegstream"];
		// bytearray is in the ->data property
		$imageData = $bytearray->data;
		$idimage = "usuario_".$dato1->clave.".png";
		
		$objConectar = new conexion();
       $conexion = $objConectar->conectar();
       
       $sql = "UPDATE  `usuario` SET  `foto` = 'http://www.boomonsters.com/app/content/data/users/pic/".$idimage."' WHERE  `clave` =".$dato1->clave;
       $res = mysql_query($sql,$conexion);
		
	   $dato2 = ( $success = file_put_contents("../../app/content/data/users/pic/".$idimage, $imageData) ) ? $idimage : $success;
		
		

		//agregarMascota( $arr[0]->mascota );
		$pet = $arr[0]->mascota;
		
		$objConectar = new conexion();
	    $conexion = $objConectar->conectar();
			/*
			idEspecie
			IDclave
			nombre
			color
			fechaCreacion	timestamp			No	CURRENT_TIMESTAMP
			iddueño
			fechaEstado	datetime			No			 	 	 	 	 	 	
			higiene
			cansancio
			animo
			hambre
			salud
			*/
			
		$i = 0;
		$sql = "INSERT INTO mascotas (idEspecie, IDclave, nombre, color, fechaCreacion, iddueno, fechaEstado, higiene, cansancio, animo, hambre, salud) VALUES (".$pet->idEspecie.", NULL,'" .$pet->nombre."', ".$pet->color.", CURRENT_TIMESTAMP,".$dato1->clave.", CURRENT_TIMESTAMP, '".$pet->atributos_masc[$i++]."', '".$pet->atributos_masc[$i++]."', '".$pet->atributos_masc[$i++]."', '".$pet->atributos_masc[$i++]."', '".$pet->atributos_masc[$i++]."')";
		
		$res = mysql_query($sql,$conexion);// or die(mysql_error()."[".$sql."]");
		

		$sql2 = "SELECT max(IDclave) mx FROM mascotas WHERE nombre = '".$pet->nombre."' and iddueno = ".$dato1->clave;
		$res = mysql_query($sql2,$conexion);
		//$res = mysql_query($sql,$conexion) or die();
		
		$arreglo4 = array();
		$arreglo4[0] = $sql;
		$arreglo4[1] = $sql2;
		//return $arreglo4;
		
		$id = mysql_result($res,0,0);
		$objConectar->desconectar();

		$arrMascota[0] = "idMascota";
		$arrMascota[1] = $id;
		
		$arregloReturn = array();
		$arregloReturn[0] = $dato1;
		$arregloReturn[1] = $dato2;
		$arregloReturn[2] = $arrMascota;
		
		/*
			INSERTAR token del usuario de facebook en su tabla correspondiente.
			No compatible con twitter (aún)
		*/
		
		$arrFB = $arr[2];
		
		$rs = 1; //Facebook
		
		if( $arrFB[3] )
		{
			$rs = $arrFB[3];
		}
		
		
		//Corroborar los nombres de variables
		$sql = "INSERT INTO usuario_social VALUES(".$rs.",".$person->id.",'".$su."')";
		mysql_query($sql,$conexion);
		
		switch($rs)
		{
			case 1: //Facebook
			
				$tk = $arrFB[0];
				$su = $arrFB[1];
				//$ss = $arrFB[2];
				
				$sk = "";
				$ss = "";
			
				$table = "sesiones_secretos_fb";
				$sql = "INSERT INTO ".$table." VALUES(".$person->id.",'".$su."','".$sk."','".$ss."','".$tk."')";
			
			break;
			
			case 2: //Twitter
			
				$sk = $arrFB[0];
				$su = $arrFB[1];
				$ss = $arrFB[2];

				$table = "tokens_secretos_tw";
				$sql = "INSERT INTO ".$table." VALUES(".$person->id.",'".$su."','".$sk."','".$ss."')";
				

			break;
		}
		
		mysql_query($sql,$conexion);
		
		//return $arregloReturn;
	}
	
	function registerPersonWEB($arr)
	{
		//Debido a la problemática que enfrentamos para registrar a un usuario desde web
		//(El usuario no se había validado y por ende no se podía agregar la foto o se podía hacer con riesgo de seguridad)
		//Decidimos hacer esta función que resuelve el problema de registro y graba en el servidor la foto sin confirmar al user.
		
		$dato1 = $this->registerPerson( $arr[0] );

		$this->usrGlb = $dato1;
		$this->usrGlb->clave = $dato1->clave;
		
		$arr33 = array();
		$arr33[0] = $dato1;
		$arr33[1] = $this->usrGlb;
		//return $arr33;

		
		//$dato2 = $this->uploadUserPicture( $arr[1] );
		//$usuario = $this->usrGlb;
		
		$bytearray = $arr[1]["jpegstream"];
		// bytearray is in the ->data property
		$imageData = $bytearray->data;
		$idimage = "usuario_".$dato1->clave.".png";
		
		$objConectar = new conexion();
       $conexion = $objConectar->conectar();
       
       $sql = "UPDATE  `usuario` SET  `foto` = 'http://www.boomonsters.com/app/content/data/users/pic/".$idimage."' WHERE  `clave` =".$dato1->clave;
       $res = mysql_query($sql,$conexion);
		
	   $dato2 = ( $success = file_put_contents("../../app/content/data/users/pic/".$idimage, $imageData) ) ? $idimage : $success;
		
		

		//agregarMascota( $arr[0]->mascota );
		$pet = $arr[0]->mascota;
		
		$objConectar = new conexion();
	    $conexion = $objConectar->conectar();
			/*
			idEspecie
			IDclave
			nombre
			color
			fechaCreacion	timestamp			No	CURRENT_TIMESTAMP
			iddueño
			fechaEstado	datetime			No			 	 	 	 	 	 	
			higiene
			cansancio
			animo
			hambre
			salud
			*/
			
		$i = 0;
		$sql = "INSERT INTO mascotas (idEspecie, IDclave, nombre, color, fechaCreacion, iddueno, fechaEstado, higiene, cansancio, animo, hambre, salud) VALUES (".$pet->idEspecie.", NULL,'" .$pet->nombre."', ".$pet->color.", CURRENT_TIMESTAMP,".$dato1->clave.", CURRENT_TIMESTAMP, '".$pet->atributos_masc[$i++]."', '".$pet->atributos_masc[$i++]."', '".$pet->atributos_masc[$i++]."', '".$pet->atributos_masc[$i++]."', '".$pet->atributos_masc[$i++]."')";
		
		$res = mysql_query($sql,$conexion);// or die(mysql_error()."[".$sql."]");
		

		$sql2 = "SELECT max(IDclave) mx FROM mascotas WHERE nombre = '".$pet->nombre."' and iddueno = ".$dato1->clave;
		$res = mysql_query($sql2,$conexion);
		//$res = mysql_query($sql,$conexion) or die();
		
		$arreglo4 = array();
		$arreglo4[0] = $sql;
		$arreglo4[1] = $sql2;
		//return $arreglo4;
		
		$id = mysql_result($res,0,0);
		$objConectar->desconectar();

		$arrMascota[0] = "idMascota";
		$arrMascota[1] = $id;
		
		$arregloReturn = array();
		$arregloReturn[0] = $dato1;
		$arregloReturn[1] = $dato2;
		$arregloReturn[2] = $arrMascota;
		
		return $arregloReturn;
	}
	
	function registerPerson( $person )
	{
        $objConectar = new conexion();
        $conexion = $objConectar->conectar();
		$dividido = ($person->cumple)/1000;

		//DATE_ADD(FROM_UNIXTIME(0), INTERVAL -13391999 SECOND
		/*
		$geolocation2 = new geolocation(false); 
		$geolocation2->setTimeout(2);
		$geolocation2->setIP($_SERVER['REMOTE_ADDR']);
		$locations = $geolocation2->getGeoLocation();
		
		$ip = $locations[0]["Ip"];
		$pais = $locations[0]["CountryName"];
		
		return $ip;
		*/ //<---- RIP
		
		$ip = $_SERVER['REMOTE_ADDR'];
		
		$ipinfodb = new ipinfodb;
		$ipinfodb->setKey('149048918a5419cf857a642d6013c2455eadb3f95e7df8e77f0f859d620fbd93');

		$locations = $ipinfodb->getGeoLocation($ip);
		if (!empty($locations) && is_array($locations))
		{
		  foreach ($locations as $field => $val)
		  {
		    if($field == "CountryName")
		    {
		    	$pais = $val;
		    }
		  }
		}

		 // > //mysql_query( "insert into usuario (clave, nombre, login, password, correo, cumple, foto, Confirmado, fechaCreacion, ip, pais) values(default,'".utf8_decode($person->firstname)."','".utf8_decode($person->login)."','".utf8_decode($person->contrasenia)."','".utf8_decode($person->email)."',DATE_ADD(FROM_UNIXTIME(0), INTERVAL ".$dividido." SECOND),'algo',default);",$conexion); 
		 
		 $sql = "INSERT INTO usuario (clave, nombre, login, password, correo, cumple, foto, Confirmado, fechaCreacion, ip, pais) VALUES(default,'".utf8_decode($person->firstname)."','".utf8_decode($person->login)."','".utf8_decode($person->contrasenia)."','".utf8_decode($person->email)."',DATE_ADD(FROM_UNIXTIME(0), INTERVAL ".$dividido." SECOND),'".$person->foto."',1,(SELECT unix_timestamp(now())),'".$ip."','".$pais."');";
		mysql_query( $sql,$conexion ) or die("/= ! ".$sql);

		// mysql_query( "insert into usuario values(default,'".utf8_decode($person->firstname)."','".$person->login."','".$person->contrasenia."','".$person->email."',FROM_UNIXTIME(".$dividido."),'algo',default);",$conexion); 
		 
		 $confirmacion=md5(date('r', time()));
		 
		 $res = mysql_query("SELECT clave FROM usuario WHERE correo = '".$person->email."'",$conexion); 
		 
		 if(mysql_num_rows($res) > 0)
		 {
		   $clave = mysql_result($res,0,0);
		 }
		 
		 
		 mysql_query("INSERT INTO ConfirmacionTemporal VALUES('".$confirmacion."',".$clave.");",$conexion) ;

		 mysql_query("INSERT INTO Dinero_usuarios VALUES(".$clave.", 1000 );",$conexion);

/*
		$url_confirmacion = "http://www.boomonsters.com/testingSite/confirmar.php?uid=".$clave."&cid=".$confirmacion."";
		
		$codigohtml = "<table width='100%'>
		<tr>
		<td bgcolor='#00CC33'>
		<h1><font color='#FFCC66'>BooMonsters</font></h1><font color='#FFFFFF'>
<h2>Confirmación</h2></font></td></tr><tr><td bgcolor='#FFFFFF'>
		<h2>Hola ".utf8_decode($person->firstname).",</h2>
		<p>Gracias , Por favor haz click <a href='".$url_confirmacion."' target='_blank'>aqu&iacute;</a> para confirmar tu Correo. Una vez que confirmes que es tu correo, podr&aacute;s accesar a todos los beneficios de BooMonsters en tu aplicación de escritorio. Si el enlace anterior no es soportado por tu programa de correo, haz click en el siguiente link o copia y pega en tu navegador de Internet.</p>
		<p><a href='".$url_confirmacion."' target='_blank'>".$url_confirmacion."</a></p>
		<p>Gracias por adoptar a tu <strong>BooMonster!</strong></p>
		<p>&nbsp;</p>
		</td>
		</tr>
		<tr>
		<td><span class='style9'>--- Trebox Media</span>
		</td>
		</tr>
		</table>";
		
		$email = $person->email;
		$asunto = 'Confirmacion BooMonsters';
		
		
		// To send HTML mail, the Content-type header must be set
$cabeceras  = 'MIME-Version: 1.0' . "\r\n";
$cabeceras .= 'Content-type: text/html; charset=iso-8859-1' . "\r\n";
// Additional headers
$cabeceras .= 'From: BooMonsters <no-reply@boomonsters.com>' . "\r\n";

		if(!mail($email,$asunto,$codigohtml,$cabeceras))
		return "Tremendus error with a simple If";
*/		
		$template = new TemplateCorreo_Confirmacion();
		$template->setDatos($person, $clave, $confirmacion);
		//return "Atributos de los templates: ".$template->url_confirmacion." * ".$template->email." * ";
		$ctrlCorreo = new ControlCorreos();
    	$ctrlCorreo->enviarCorreo($template);
		
		$sql = "SELECT nombre, password, correo, cumple, foto FROM usuario";
        
        $consulta = mysql_query($sql,$conexion);
        
        $str="";
        
        while( $fila = mysql_fetch_array($consulta) )
		{ 
		    $str = $str.$fila[0]."\n";
		}     
            
		 mysql_close($conexion); 
	 
		// Here you would register the person information contained in the 'person' parameter, 
		// probably adding it to a database before returning a success flag.

		// Instead, for demonstration purposes, we will modify the firstname property.
		// Notice that we are not using associative array access syntax e.g. person['firstname']
		
		//$person->firstname = $person->firstname . "(Modified by PHP)";
		
		// We can also call methods on our PHP object.
		// The following calls the incrementAge method to increment the instance's age property.
		
		//$person->incrementAge();
		
		
		// Now, when we return the object, it will have two modified properties, 'firstname' and 'age'.
		// It will maintain its type in Flash.
		
		//return $person;
		
        //return utf8_encode($str);	
		$person->clave = $clave;
		return $person;
	}
	
	function compararFechasItems($F)
	{
		 $objConectar = new conexion();
         $conexion = $objConectar->conectar();
		
		 $sql = "SELECT fechaAct_items FROM usuario WHERE clave = ".$this->usrGlb->id;
		 $res = mysql_query($sql,$conexion);
		 			 	 		 		 		 
		 //$fecha = $this->formatFecha(mysql_result($res,0,0));
		 $fecha = mysql_result($res,0,0);
		
		 $arreglo_respuesta[1] = "itemsusuario";
		 		
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
	
	function compararFechasAmigos($F)
	{
	/*	$arreglo_respuesta[0] = true;
		$arreglo_respuesta[1] = "amigosusuario";
		return $arreglo_respuesta;*/
		 $objConectar = new conexion();
         $conexion = $objConectar->conectar();
		
		 $sql = "SELECT fechaAct_amigos FROM usuario WHERE clave = ".$this->usrGlb->id;
		 $res = mysql_query($sql,$conexion);
		 			 	 		 		 		 
		 //$fecha = $this->formatFecha(mysql_result($res,0,0));
		 $fecha = mysql_result($res,0,0);
		
		 $arreglo_respuesta[1] = "amigosusuario";
		 		
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
	
	function compararFechasMensajes($F)
	{	
		 $objConectar = new conexion();
         $conexion = $objConectar->conectar();
		
		 $sql = "SELECT fechaAct_mensajes FROM usuario WHERE clave = ".$this->usrGlb->id;
		 $res = mysql_query($sql,$conexion);
		 			 	 		 		 		 
		 //$fecha = $this->formatFecha(mysql_result($res,0,0));
		 $fecha = mysql_result($res,0,0);
		
		 $arreglo_respuesta[1] = "mensajesusuario";
		 		
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
	
	function cambiarPassword($p)
	{
		$objConectar = new conexion();
        $conexion = $objConectar->conectar();
		
		$sql = "UPDATE usuario SET password = '".$p."' WHERE clave = ".$this->usrGlb->id;
		
		$res = mysql_query($sql,$conexion) or die;
		
		$ar[0] = 1;
		$ar[1] = $this->usrGlb->id;
		$ar[2] = $p;
		
		return $ar;
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
	
	function cambiarCorreo($correo)
	{
		$objConectar = new conexion();
        $conexion = $objConectar->conectar();
		
		$sql = "UPDATE usuario SET correo = ".$correo." WHERE clave = ".$this->usrGlb->id;
		$res = mysql_query($sql,$conexion);
		
		return 1;
	}
	
	function cambiarCorreoNombre($n)
	{
		$objConectar = new conexion();
        $conexion = $objConectar->conectar();
		
		$sql = "UPDATE usuario SET nombre = ".$n." WHERE clave = ".$this->usrGlb->id;
		$res = mysql_query($sql,$conexion);
		
		return 1;
	}
	
}
?>
