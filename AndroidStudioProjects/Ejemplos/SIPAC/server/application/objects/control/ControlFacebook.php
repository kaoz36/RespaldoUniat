<?PHP

include_once '../vo/UsuarioVO.php';

class ControlFacebook
{
	var $usrGlb;

	function ControlFacebook($usr){
		$this->usrGlb=$usr;
	}
	
	function registrarUsuario($arr){
	//usr:UsuarioVO, foto:ByteArray, token:String,FBid:String, funcion:Function
	
	$person = $arr[0];
		$pet = $person->mascota;
	$foto = $arr[1];
	$fb = $arr[2]; // Facebook token + id
	
	/*** Comienzo a registrar al usuario ***/
	
	
	$objConectar = new conexion();
    $conexion = $objConectar->conectar();
	$dividido = ($person->cumple)/1000;

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

	$sql = "INSERT INTO usuario (clave, nombre, login, password, correo, cumple, foto, Confirmado, fechaCreacion, ip, pais) VALUES(default,'".utf8_decode($person->firstname)."','".utf8_decode($person->login)."','".utf8_decode($person->contrasenia)."','".utf8_decode($person->email)."',DATE_ADD(FROM_UNIXTIME(0), INTERVAL ".$dividido." SECOND),'".$person->foto."',1,(SELECT unix_timestamp(now())),'".$ip."','".$pais."');";
	mysql_query( $sql,$conexion ) or die("/= ! ".$sql);
	
	$res = mysql_query("SELECT clave FROM usuario WHERE correo = '".$person->email."'",$conexion); 
		 
	if(mysql_num_rows($res) > 0)
	{
		$clave = mysql_result($res,0,0);
	}
	
	$person->clave = $clave;
	$person->id = $clave;

	mysql_query("INSERT INTO Dinero_usuarios VALUES(".$clave.", 20000 );",$conexion);
	
	/*** Termino de registrar al usuario ***/
	
	/*** Inicio Agregando foto ***/
	
	$bytearray = $foto["jpegstream"];
	$imageData = $bytearray->data;
	$idimage = "usuario_".$clave.".png";
		
	
      
    $sql = "UPDATE  `usuario` SET  `foto` = 'http://www.boomonsters.com/app/content/data/users/pic/".$idimage."' WHERE  `clave` =".$clave;
    $res = mysql_query($sql,$conexion);
		
	$dato2 = ( $success = file_put_contents("../../app/content/data/users/pic/".$idimage, $imageData) ) ? $idimage : $success;
	
	/*** Termino Agregando foto ***/
	
	
	/*** Inicio Agregando mascota ***/
	
		$sql = "INSERT INTO mascotas (idEspecie, IDclave, nombre, color, fechaCreacion, iddueno, fechaEstado, higiene, cansancio, animo, hambre, salud, fechaAct_items) VALUES (".$pet->idEspecie.", NULL,'" .$pet->nombre."', ".$pet->color.", CURRENT_TIMESTAMP,".$clave.", CURRENT_TIMESTAMP, '".$pet->atributos_masc[$i++]."', '".$pet->atributos_masc[$i++]."', '".$pet->atributos_masc[$i++]."', '".$pet->atributos_masc[$i++]."', '".$pet->atributos_masc[$i++]."',1)";
		
		//return $sql;
		
		$res = mysql_query($sql,$conexion);
		

		$sql2 = "SELECT max(IDclave) mx FROM mascotas WHERE nombre = '".$pet->nombre."' and iddueno = ".$clave;
		$res = mysql_query($sql2,$conexion);
		$id = mysql_result($res,0,0);
		
		

		$arrMascota[0] = "idMascota";
		$arrMascota[1] = $id;
	
	/*** Termino Agregando mascota ***/
	
	/*** Inicio agregar datos facebook ***/
	
	$tk = $fb[0];
	$su = $fb[1];
				
	$sql = "INSERT INTO sesiones_secretos_fb (booid,uid,auth_token) VALUES(".$clave.",'".$su."','".$tk."')";
	$res = mysql_query($sql,$conexion);
	$objConectar->desconectar();
	/*** Termino agregar datos facebook ***/
	
	/* Creando arreglo para retornar */
	
		$arrReturn = array();
		
		$arrReturn[0] = $person; // 
		$arrReturn[1] = $dato2; // Foto
		$arrReturn[2] = $arrMascota; // Mascota
		$arrReturn[3] = $res;
	
	/* Termino arreglo para retornar */
	
	
	return $arrReturn;
	
	
	//true
	}
	
	function hasFaceBookAccount(){
	//usr:UsuarioVO,funcion:Function
	//regresa token y id
		$objConectar = new conexion();
    	$conexion = $objConectar->conectar();
    	
    	$sql = "SELECT auth_token, uid FROM sesiones_secretos_fb WHERE booid = ".$this->usrGlb->id." LIMIT 0,1";
    	
    	$res = mysql_query($sql,$conexion);
    	
    	$val = null;
    	
    	if(mysql_num_rows($res) > 0)
		{
			$val = array();
			
			$val[0] = mysql_result($res,0,0);
        	$val[1] = mysql_result($res,0,1);
		}

		$objConectar->desconectar();
		
		return $val;
	}
	
	function hasBooMonstersAccount($FBid){
	//regresa usuariovo de boomonsters
	//FBid:String,funcion:Function
	
	$objConectar = new conexion();
    $conexion = $objConectar->conectar();
	
		$FBid;
		
		$sql = "SELECT booid FROM sesiones_secretos_fb WHERE uid = ".$FBid." LIMIT 0,1";
	    $res = mysql_query($sql,$conexion);
	    
		if(mysql_num_rows($res) > 0 ) //El usuario Sí existe
		{
			$booid = mysql_result($res,0,0);
	    
	    	$sql = "SELECT nombre, cumple, foto, confirmado, correo, login, fechaAct_items, fechaAct_mensajes, fechaAct_amigos, password FROM usuario WHERE clave = ".$booid;
			$res = mysql_query($sql,$conexion) or die( "Error: " . mysql_error() );
		
			$person = new UsuarioVO();

			$person->firstname = mysql_result($res,0,0);
			$person->cumple = mysql_result($res,0,1);
			$person->foto = mysql_result($res,0,2);
			$person->confirmado = mysql_result($res,0,3);
			$person->email = mysql_result($res,0,4);
			$person->login = mysql_result($res,0,5);
			$person->fechaAct_items = mysql_result($res,0,6);
			$person->fechaAct_mensajes = mysql_result($res,0,7);
			$person->fechaAct_amigos = mysql_result($res,0,8);
			$person->contrasenia = mysql_result($res,0,9);
			$person->id = $booid;
			$person->clave = $booid;
			
			$objConectar->desconectar();
			return $person; //Con la información leída de la base de datos
		}
		else //El usuario NO existe
		{
			$objConectar->desconectar();
			return "error";
		}
	
	}
	
	function vincularFacebookBooMonsters($arr){
	//FBid:String,token:String

		$objConectar = new conexion();
    	$conexion = $objConectar->conectar();
    	
    	
    	$usr = $arr[0];
    	
    	/*
    	$sql = "SELECT clave FROM usuario WHERE login = '".$usr->login."' AND password = '".$usr->contrasenia."'";
    	$res = mysql_query($sql,$conexion);
    	
    	if( mysql_num_rows($res) > 0 )
    	{
    		$booid = mysql_result($res,0,0)
    	}
    	*/
    	
    	$val = $this->hasFaceBookAccount();

    	/*
	    $sql = "SELECT auth_token FROM sesiones_secretos_fb WHERE booid = ".$this->usrGlb->id." LIMIT 0,1";
		$res = mysql_query($sql,$conexion);
		*/
		
		$sk = "";
		$ss = "";
		$su = $arr[1];
		$tk = $arr[2];
		$rs = 1;
		
		if($val)
		{
			//Oh! Ya lo tengo registrado. Voy a actualizar.
			
			$sql = "UPDATE sesiones_secretos_fb SET auth_token = '".$tk."', uid = '".$su."' WHERE booid = ".$this->usrGlb->id;
			$res = mysql_query($sql,$conexion);
			
			$sql = "UPDATE usuario_social SET user = '".$su."' WHERE id_usuario = ".$this->usrGlb->id;
			$res = mysql_query($sql,$conexion);
		}
		else
		{
			//$sql = "INSERT INTO sesiones_secretos_fb VALUES(".$this->usrGlb->id.",'".$su."','".$sk."','".$ss."','".$tk."')";
			$sql = "INSERT INTO sesiones_secretos_fb (booid, uid,auth_token) VALUES(".$this->usrGlb->id.",'".$su."','".$tk."')";
			$res = mysql_query($sql,$conexion);
			
			$sql2 = "INSERT INTO usuario_social VALUES(".$rs.",".$this->usrGlb->id.",'".$su."')";
			$res = mysql_query($sql2,$conexion);
		}
    
    	
    	$objConectar->desconectar();
	
	return "Se vincularon correctamente. Creo.";
	
	}
	
	//function

	
}

?>
