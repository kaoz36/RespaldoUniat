<?PHP

	include_once 'conexion.php';
	include_once 'vo/UsuarioVO.php';
	include_once 'geolocation.class.php';
	include_once 'ipinfodb.class.php';
	
class ControlSocial
{
	var $usrGlb;
	
	function ControlSocial($usr){
		$this->usrGlb=$usr;
	}

	function registrarUsuarioFB( $arr )
	{
		$objConectar = new conexion();
        $conexion = $objConectar->conectar();
        	
		$pet = $arr[0];
		$arrFB = $arr[1];
		
		$sk = $arrFB[0];
		$su = $arrFB[1];
		$ss = $arrFB[2];
		$rs = 1; //Facebook
		
		if( $arrFB[3] )
		{
			$rs = $arrFB[3];
		}
		
		/*
			Primero debo insertar al usuario para generar su ID
			Después hacer una consulta y obtengo el ID
			Luego mascota o FB, da igual. Insertaré primero a la mascota
		*/
		
		$person = $this->usrGlb;
		$dividido = ($person->cumple)/1000;
		
		/*
		$geolocation = new geolocation(false); 
		$geolocation->setTimeout(2);
		$geolocation->setIP($_SERVER['REMOTE_ADDR']);
		$locations = $geolocation->getGeoLocation();
		
		$ip = $locations[0]["Ip"];
		$pais = $locations[0]["CountryName"];
		*/
		
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
		
		//$sql = "INSERT INTO usuario (clave, nombre, login, password, correo, cumple, foto, Confirmado) VALUES(default,'".utf8_decode($person->firstname)."','".utf8_decode($person->login)."','".utf8_decode($person->contrasenia)."','".utf8_decode($person->email)."',DATE_ADD(FROM_UNIXTIME(0), INTERVAL ".$dividido." SECOND),'".$person->foto."',1);";
		$sql = "INSERT INTO usuario (clave, nombre, login, password, correo, cumple, foto, Confirmado, fechaCreacion, ip, pais) VALUES(default,'".utf8_decode($person->firstname)."','".utf8_decode($person->login)."','".utf8_decode($person->contrasenia)."','".utf8_decode($person->email)."',DATE_ADD(FROM_UNIXTIME(0), INTERVAL ".$dividido." SECOND),'".$person->foto."',1,(SELECT unix_timestamp(now())),'".$ip."','".$pais."');";
		mysql_query( $sql,$conexion ) or die("/= ! ".$sql);
		
		$sql = "SELECT * FROM usuario WHERE login = '".utf8_decode($person->login)."' LIMIT 0,1";
		//return $sql;
		$res = mysql_query($sql,$conexion);
		
		$i=0;
		if( mysql_num_rows($res) > 0 )
		{
		
			//Inserto a la mascota
			$person = $this->getUsuario( mysql_fetch_array($res) );
			$sql = "INSERT INTO mascotas (idEspecie, IDclave, nombre, color, fechaCreacion, iddueno, fechaEstado, higiene, cansancio, animo, hambre, salud) VALUES (".$pet->idEspecie.", NULL,'" .$pet->nombre."', ".$pet->color.", CURRENT_TIMESTAMP,".$person->id.", CURRENT_TIMESTAMP, '".$pet->atributos_masc[$i++]."', '".$pet->atributos_masc[$i++]."', '".$pet->atributos_masc[$i++]."', '".$pet->atributos_masc[$i++]."', '".$pet->atributos_masc[$i++]."')";
			//return $sql;
			$res = mysql_query($sql,$conexion);// or die(mysql_error()."[".$sql."]");
			
			//Inserto a sus datos de RS
			
			$sql = "INSERT INTO Dinero_usuarios VALUES(".$person->id.",1000)";
			mysql_query($sql,$conexion);
			
			switch( $rs )
			{
				case 1: //FB
					$table = "sesiones_secretos_fb";
				break;
				
				case 2: //TW
					$table = "tokens_secretos_tw";
				break;
			}
			
			
			$sql = "INSERT INTO ".$table." VALUES(".$person->id.",'".$su."','".$sk."','".$ss."')";
			mysql_query($sql,$conexion);
			
			$sql = "INSERT INTO usuario_social VALUES(".$rs.",".$person->id.",'".$su."')";
			mysql_query($sql,$conexion);
			
			
			$sql = "SELECT IDclave FROM mascotas WHERE iddueno = ".$person->id;
			//return  "mascota |".$sql;
			$res = mysql_query($sql,$conexion);
			$idMascota = mysql_result($res,0,0);
			
			$objConectar->desconectar();
			
			//echo $locations[0]["Ip"];
  			//echo $locations[0]["CountryName"];
			
			return $idMascota;

		}
		else
		{
			return "Un error";
		}
			
		$usr = $this->getUsuario( mysql_fetch_array($res) );
		
		
		$objConectar->desconectar();
	}

	function vincularDesdeSocial( $arr )
	{
		$objConectar = new conexion();
        $conexion = $objConectar->conectar();
		//$res[0] = $arr;
		$sk = $arr[0];
		$su = $arr[1];
		$ss = $arr[2];
		
		$rs = 1;
		
		if( $arr[3] )
		{
			$rs = $arr[3];
		}
		
		switch($rs)
		{
			case 1: //Facebook
				$table = "sesiones_secretos_fb";
				$campo1 = "sesion";
			break;
			
			case 2: //Twitter
				$table = " tokens_secretos_tw";
				$campo1 = "token";
			break;
		}
		
		$sql = "SELECT ".$campo1." FROM ".$table." WHERE booid = ".$this->usrGlb->id." LIMIT 0,1";
		$res = mysql_query($sql,$conexion);
		
		if(mysql_num_rows($res) > 0)
		{
			// El usuario ya tenía un FB
			$objConectar->desconectar();
			return "El usuario ya tenía";
		}
		else
		{
			// El usuario NO tenía un FB
			//$sql = "SELECT booid, sesion, secret FROM sesiones_secretos_fb WHERE uid = ".$su;
			$sql = "INSERT INTO ".$table." VALUES(".$this->usrGlb->id.",'".$su."','".$sk."','".$ss."')";
			$res = mysql_query($sql,$conexion);
			
			$sql = "INSERT INTO usuario_social VALUES(".$rs.",".$this->usrGlb->id.",'".$su."')";
			$res = mysql_query($sql,$conexion);
			
			$sql = "SELECT * FROM usuario WHERE clave = ".$this->usrGlb->id." LIMIT 0,1";
			$res = mysql_query($sql,$conexion);
			
			$usr = $this->getUsuario( mysql_fetch_array($res) );
			$objConectar->desconectar();
			return $usr;
		}
		
		//$res[1] = "Bueno, funciona aqui";
		
		$objConectar->desconectar();
		return $res;
	}
	
	function elUsuarioFBYaTieneBM( $arr )
	{
		$objConectar = new conexion();
        $conexion = $objConectar->conectar();
        /*
        arr.push(sK);
		arr.push(sS);
		arr.push(sU);
        */
        
        $sk = $arr[0];
        $ss = $arr[1];
        $su = $arr[2];
        
        $sql = "SELECT booid, sesion, secret FROM sesiones_secretos_fb WHERE uid = ".$su." LIMIT 1";
        $res = mysql_query($sql,$conexion);
        
        if( mysql_num_rows($res) > 0 )
        {
        	//Esto significa... que sí se han vinculadas las cuentas. 
        	//¿Serán iguales? ¿O tendré que reemplazarlas?
        	
        	$savedBoo = mysql_result($res,0,0);
        	$savedSK = mysql_result($res,0,1);
        	$savedSS = mysql_result($res,0,2);
        	
        	if( $sk != $savedSK ) //Tengo que reemplazarlas
        	{
        		$sql = "UPDATE sesiones_secretos_fb SET sesion = '".$sk."', secret = '".$ss."' WHERE uid = ".$su;
        		mysql_query($sql, $conexion);
        	}
        	
        	//Para este momento ya tengo en el servidor los nuevos datos (disque)
        	//Bueno, ahora necesito al usuario correspondiente, no?
        	//$sql = "";
        	
        	$sql = "SELECT clave, nombre, cumple, foto, correo, login, password FROM usuario WHERE clave = ".$savedBoo." and confirmado = 1 LIMIT 0,1";
        	$res = mysql_query($sql,$conexion);
        	
        	$objConectar->desconectar();
        	
        	if(mysql_num_rows($res) > 0 ) //El usuario Sí existe
			{
					/*
					$person->id = mysql_result($res,0,0);
					$person->firstname = mysql_result($res,0,1);
					$person->cumple = mysql_result($res,0,2);
					$person->foto = mysql_result($res,0,3);
					$person->email = mysql_result($res,0,4);
					$person->login = mysql_result($res,0,5);
					$person->confirmado = 1;
					//$person->confirmado = mysql_result($res,0,4);
					*/
					$person = $this->getUsuario( mysql_fetch_array($res) );
					return $person; //Con la información leída de la base de datos
			}
			else //El usuario NO existe (o no está confirmado... algo así)
			{
				return 0;
			}
        	
        }
        else
        {
        	//Esa cuenta de facebook no había sido vinculada antes a una BooMonster Account
        	return "Esa cuenta de facebook no había sido vinculada antes";
        }
        
        
	}
	
	function elUsuarioTWYaTieneBM( $arr )
	{
		$objConectar = new conexion();
        $conexion = $objConectar->conectar();
        
        /*
        arr.push(sK);
		arr.push(sS);
		arr.push(sU);
        */
        
        $sk = $arr[0];
        $ss = $arr[1];
        $su = $arr[2];
        
        $sql = "SELECT booid, token, secret FROM tokens_secretos_tw WHERE uid = ".$su." LIMIT 1";
        $res = mysql_query($sql,$conexion);
        
        if( mysql_num_rows($res) > 0 )
        {
        	//Esto significa... que sí se han vinculadas las cuentas. 
        	//¿Serán iguales? ¿O tendré que reemplazarlas?
        	
        	$savedBoo = mysql_result($res,0,0);
        	$savedTK = mysql_result($res,0,1);
        	$savedTS = mysql_result($res,0,2);
        	
        	
        	
        	if( $sk != $savedTK ) //Tengo que reemplazarlas
        	{
        		$sql = "UPDATE tokens_secretos_tw SET token = '".$sk."', secret = '".$ss."' WHERE uid = ".$su;
        		mysql_query($sql, $conexion);
        		
        	}
        	
        	//Para este momento ya tengo en el servidor los nuevos datos (disque)
        	//Bueno, ahora necesito al usuario correspondiente, no?
        	//$sql = "";
        	
        	$sql = "SELECT clave, nombre, cumple, foto, correo, login, password FROM usuario WHERE clave = ".$savedBoo." and confirmado = 1 LIMIT 0,1";
        	$res = mysql_query($sql,$conexion);
        	
        	$objConectar->desconectar();
        	
        	if(mysql_num_rows($res) > 0 ) //El usuario Sí existe
			{
					/*
					$person->id = mysql_result($res,0,0);
					$person->firstname = mysql_result($res,0,1);
					$person->cumple = mysql_result($res,0,2);
					$person->foto = mysql_result($res,0,3);
					$person->email = mysql_result($res,0,4);
					$person->login = mysql_result($res,0,5);
					$person->confirmado = 1;
					//$person->confirmado = mysql_result($res,0,4);
					*/
					$person = $this->getUsuario( mysql_fetch_array($res) );
					return $person; //Con la información leída de la base de datos
			}
			else //El usuario NO existe (o no está confirmado... algo así)
			{
				return 0;
			}
        	
        }
        else
        {
        	//Esa cuenta de twitter no había sido vinculada antes a una BooMonster Account
        	return "Esa cuenta de twitter no había sido vinculada antes";
        }
        
        
	}
		
	function vincularCuenta( $usr_social )
	{
		$objConectar = new conexion();
        $conexion = $objConectar->conectar();
		
		
		$sql = "SELECT id_red_social FROM usuario_social WHERE id_red_social = ".$usr_social->idSocial." and id_usuario = ".$this->usrGlb->id." LIMIT 1";
		//return $sql;
		$res = mysql_query($sql,$conexion) or die("Error:".mysql_error()." \n".$sql);
		
		if( mysql_num_rows($res) == 0) //Se hace la validación de que el usuario no tiene registrada la misma red social
		{
			$sql = " INSERT INTO usuario_social VALUES(".$usr_social->idSocial.",".$this->usrGlb->id.",'".$usr_social->user."') ";
			mysql_query($sql,$conexion);
			
			return "Success";
		}
		
		return "Fail";
	}
	
	function setSesionSecretFB( $arreglo )
	{
		$objConectar = new conexion();
        $conexion = $objConectar->conectar();
		
		$id = $this->usrGlb->id;
		
		$sql = "SELECT booid FROM sesiones_secretos_fb WHERE booid = ".$id;
		$res = mysql_query($sql, $conexion);
		
		if( mysql_num_rows($res) > 0 ) //A ver si ya había hecho un registro pues lo actualizo
		{
			$sql = "UPDATE sesiones_secretos_fb SET uid = '".$arreglo[0]."', sesion='".$arreglo[1]."' , secret='".$arreglo[2]."' WHERE booid = ".$id;
		}
		else //Si no, inserto uno nuevo
		{
			$sql = "INSERT INTO sesiones_secretos_fb values(".$id.",'".$arreglo[0]."','".$arreglo[1]."','".$arreglo[2]."')";
		}
		
		mysql_query($sql, $conexion);
		
		return 1;

	}
	
	function getTokenSecretTW( )
	{
		$objConectar = new conexion();
        $conexion = $objConectar->conectar();
		
		$id = $this->usrGlb->id;
		
		$sql = "SELECT uid, token, secret FROM tokens_secretos_tw WHERE booid = ".$id;
		$res = mysql_query($sql,$conexion);
		
		if( mysql_num_rows($res) > 0 )
		{
			$i = 0;
			$arreglo[$i++] = mysql_result($res,0,0);
			$arreglo[$i++] = mysql_result($res,0,1);
			$arreglo[$i++] = mysql_result($res,0,2);
		}
		else
		{
			$arreglo = 0;
		}
		
		$objConectar->desconectar();
		return $arreglo;
	}
	
	function getSesionSecretFB( )
	{
		$objConectar = new conexion();
        $conexion = $objConectar->conectar();
		
		$id = $this->usrGlb->id;
		
		$sql = "SELECT uid, sesion, secret FROM sesiones_secretos_fb WHERE booid = ".$id;
		$res = mysql_query($sql,$conexion);
		
		if( mysql_num_rows($res) > 0 )
		{
			$i = 0;
			$arreglo[$i++] = mysql_result($res,0,0);
			$arreglo[$i++] = mysql_result($res,0,1);
			$arreglo[$i++] = mysql_result($res,0,2);
		}
		else
		{
			$arreglo = 0;
		}
		
		$objConectar->desconectar();
		return $arreglo;
	}
	
	function hasBMwantsSN(  )
	{
		// Esta función es para ver si mis amigos de BooMonsters tienen Facebook.
		// 
		// Por tanto, necesitaré saber:
		//
		// Login BM | (por lo pronto) uid amigo facebook
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
		$usr->foto = $fila["foto"];
		$usr->contrasenia = $fila["password"];
		
		return $usr;
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
	
	function hasSNwantsBM( $info )
	{
		//Tras leer mis amigos de facebook he de preguntar...
		//¿Quién de ellos juega BooMonsters?
		//
		// Por tanto, necesitaré saber:
		//
		// Nombre Amigo Facebook | login BM
		
		//$id_usuario = $info[0];
		
		$id_usuario = $this->usrGlb->id;
		
		$uids = $info[1];
		
		$social = $info[2];
		
		$objConectar = new conexion();
        $conexion = $objConectar->conectar();

		/*
		---------------------------------------
		uid      | logginBM    | ¿Es mi BMamigo?
		---------------------------------------
		Jaime     jaimico        Sí
		Iván      jaureguivan    No, agrégalo!
		Wiicochet -             -
		*/
		
		/*
		$misAmigos = "SELECT idamigo FROM lista_amigos WHERE aceptado = 1 and idusuario = ".$id_usuario;
	    $soyAmigo =  "SELECT idusuario FROM lista_amigos WHERE aceptado = 1 and idamigo = ".$id_usuario;
	   
	    //$listaAmigos = "SELECT idamigo FROM lista_amigos WHERE idusuario = ".$usuario->id;
	    $amigosBM = "SELECT clave, login FROM usuario WHERE clave IN ( ".$misAmigos." ) or clave IN ( ".$soyAmigo." )";
		
		//Total de claves que usan facebook
		$usuariosFB = "SELECT id_usuario FROM usuario_social WHERE id_red_social = 1";
		
		$sql = " SELECT us.user, u.login FROM usuario_social us, usuario u ";
		*/
			
//			return "Disque entra...";

/*		
		for($i = 0; $i<count($uids); $i++)
		{
			//Es mi amigo en FB y usa BM
			$sql1 = "SELECT id_usuario FROM usuario_social where id_red_social = ".$social." and user = '".$uids[$i]."'";
			$res = mysql_query($sql1,$conexion);
			
			if(mysql_num_rows($res) > 0) //El usuario FB también tiene BM
			{
			  //El login y clave (no password) de mi amigoFB
			  //$sql2 = "SELECT U.login, U.clave, M.nombre, M.IDclave FROM usuario U, mascotas M WHERE M.iddueno = U.clave and  U.clave in ( '".mysql_result($res,0,0)."' )";
			  $sql2 = "SELECT U.login, U.clave, M.nombre, M.IDclave FROM usuario U, mascotas M WHERE M.iddueno = U.clave and  U.clave in ( ".$sql1." )";
			  $res = mysql_query($sql2,$conexion) or die("Error:".mysql_error()."\n".$sql2);
			  //¿Es mi amigo FB?
			  
			  $login = mysql_result($res,0,0);
			  $clave = mysql_result($res,0,1);
  			     $mn = mysql_result($res,0,2);
 			    $mid = mysql_result($res,0,3);
			  
			  $loginS[$i] = $login;
			  $mascotanombreS[$i] = $mn;
			  $mascotaidS[$i] = $mid;
			  $claveusuarioS[$i] = $clave;
			  
			  
			  $sql3 = "SELECT aceptado FROM lista_amigos WHERE ( idamigo = ".$clave." and idusuario = ".$id_usuario." ) ";
			  $res = mysql_query($sql3,$conexion);
			  
			  if(mysql_num_rows($res) > 0) //Yo le envié una solicitud?
			  {
				 $aceptado = mysql_result($res,0,0);
				 
				 if($aceptado == 1) //Y ya me aceptó D: ?
				 {
					 //$accionS[$i] = "Ya es tu amigo";
					 $accionS[$i] = 1;
				 }
				 else
				 {
					 //$accionS[$i] = "Esperando a que acepte la solicitud";
					 $accionS[$i] = 4;
				 }
			  }
			  else 
			  {
				 $sql4= "SELECT aceptado FROM lista_amigos WHERE ( idusuario = ".$clave." and idamigo = ".$id_usuario." )";
				 $res = mysql_query($sql4,$conexion);
				 
				 if(mysql_num_rows($res) > 0) //Él me la mandó a mí?
				 {
					$aceptado = mysql_result($res,0,0);
				 
					 if($aceptado == 1) //Y lo acepté xD ?
					 {
						 //$accionS[$i] = "Ya es tu amigo";
						 $accionS[$i] = 2;
					 }
					 else
					 {
						 //$accionS[$i] = "Confirma la solicitud";
						 $accionS[$i] = 3;
					 } 
				 }
				 else //Definitivamente no son amigos en boomonsters
				 {
					 //$accionS[$i] = "Enviar solicitud de booamigos";
					 $accionS[$i] = 5;
				 }
				 
			  }
			  
			}
			else //El usuario de FB no usa BM
			{
				$loginS[$i] = "-";
				$accionS[$i] = 6;
				$mascotanombreS[$i] = "-";
			    $mascotaidS[$i] = 0;
				$claveusuarioS[$i] = 0;
			}
		}
*/

/*

SELECT U.clave, U.login, U.foto, U.correo, U.nombre as nombreUSR, M.nombre as nombreMSC, M.IDclave as idMSC
				FROM usuario U, mascotas M
				WHERE U.clave in
				
				(
					SELECT id_usuario FROM usuario_social WHERE user in( ". implode(',',$uids)." ) and id_red_social = ".$social." and M.iddueno = U.clave
				)								
				
				)U
				LEFT JOIN (

*/

       $sql =  "SELECT * FROM (SELECT clave, login, foto, correo, nombreUSR, FB_USER, TW_USER, YT_USER, nombreMSC, idMSC
				FROM (
				
				SELECT login, clave, foto, correo, nombreUSR, FB_USER, TW_USER, nombreMSC, idMSC
				FROM (
				
				SELECT login, clave, foto, correo, nombreUSR, CONCAT(link,user) AS FB_USER, nombreMSC, idMSC
				FROM (
				
				SELECT U.clave, U.login, U.foto, U.correo, U.nombre as nombreUSR, M.nombre as nombreMSC, M.IDclave as idMSC
				FROM usuario U, mascotas M
				WHERE U.clave in
				
				(
					SELECT id_usuario FROM usuario_social WHERE user in( ". implode(',',$uids)." ) and id_red_social = ".$social." and M.iddueno = U.clave
				)								
				
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

                SELECT idamigo COD, aceptado FROM lista_amigos WHERE idusuario = ".$id_usuario."
                UNION
                SELECT idusuario, aceptado - 10 FROM lista_amigos WHERE idamigo = ".$id_usuario."

                ) EA ON H.clave = EA.COD";
       
       //return $sql;
       
		$res = mysql_query($sql,$conexion) or die("Error: ".mysql_error()."\n".$sql);
		
		$i = 0;
		
	   while( $fila = mysql_fetch_array($res) )
	   {
			$amigos[$i] = $this->getAmigoVO( $fila );
			$i++;						
	   }
	   
	   return $amigos;

/// Nuevo algoritmo optimizado en recursos y ejecución


/// Nuevo algoritmo optimizado en recursos y ejecución

		$res = mysql_query($sql,$conexion);
		return $sql;

		//$resultado es un array que contiene 2 elementos:  logginBM y accion
		
		$resultado[0] = $loginS;
		$resultado[1] = $accionS;
		$resultado[2] = $mascotanombreS;
		$resultado[3] = $mascotaidS;
		$resultado[4] = $claveusuarioS;
		
		return $resultado;
		
	}
	
}

?>
