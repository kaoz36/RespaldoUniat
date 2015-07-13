<?php
include_once 'conexion.php';
include_once 'vo/MascotaVO.php';

class ControlMascota
{
	 var $usrGlbl;
	 
	function ControlMascota($usr )
	{
		$this->usrGlbl=$usr;
	}
	 
	
	function updateMascota( $pet )
	{
		$objConectar = new conexion();
        $conexion = $objConectar->conectar();
		
		$i = 0;
		
		$sql = "UPDATE mascotas SET nombre = '".$pet->nombre."',
								  fechaEstado = CURRENT_TIMESTAMP,
								  color = ".$pet->color.",
								  higiene = ".$pet->atributos_masc[$i++].", 
								  cansancio = ".$pet->atributos_masc[$i++].", 
								  animo = ".$pet->atributos_masc[$i++].", 
								  hambre = ".$pet->atributos_masc[$i++].", 
								  salud = ".$pet->atributos_masc[$i++]." WHERE id IDclave".$pet->clave;
								  
//		mysql_query($sql, $conexion) or die("Error: ".mysql_error()." [".$sql."]");
		mysql_query($sql,$conexion);
		
		$objConectar->desconectar();
		
		return 1; //$pet
		
	}
	
	
	function agregaMascota( $pet )
	{
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
		
		//$sql = "INSERT INTO mascotas (idEspecie, IDclave, nombre, color, fechaCreacion, iddueno, fechaEstado, fechaEstado, higiene, cansancio, animo, hambre, salud) VALUES(".$pet->idEspecie.",default,'".$pet->nombre."',".$pet->color.",CURRENT_TIMESTAMP,".$this->usrGlbl->id.",CURRENT_TIMESTAMP,".$pet->atributos_masc[$i++].",".$pet->atributos_masc[$i++].",".$pet->atributos_masc[$i++].",".$pet->atributos_masc[$i++].",".$pet->atributos_masc[$i++].")";

        $sql = "INSERT INTO mascotas (idEspecie, IDclave, nombre, color, fechaCreacion, iddueno, fechaEstado, higiene, cansancio, animo, hambre, salud) VALUES (".$pet->idEspecie.", NULL,'" .$pet->nombre."', ".$pet->color.", CURRENT_TIMESTAMP,".$this->usrGlbl->id.", CURRENT_TIMESTAMP, '".$pet->atributos_masc[$i++]."', '".$pet->atributos_masc[$i++]."', '".$pet->atributos_masc[$i++]."', '".$pet->atributos_masc[$i++]."', '".$pet->atributos_masc[$i++]."')";

		//return $sql;
		
		$res = mysql_query($sql,$conexion);// or die(mysql_error()."[".$sql."]");
				
		
		$sql = "SELECT max(IDclave) mx FROM mascotas WHERE nombre = '".$pet->nombre."' and iddueno = ".$this->usrGlbl->id;
		$res = mysql_query($sql,$conexion);
//		$res = mysql_query($sql,$conexion) or die();
		
		$id = mysql_result($res,0,0);
		
		$objConectar->desconectar();
		
		$arr[0] = "idMascota";
		$arr[1] = $id;
		
		return $arr;

	}
	
	function consultarMascotaPropia( )
	{
	   $objConectar = new conexion();
       $conexion = $objConectar->conectar();
	   
	   $sql = "SELECT idEspecie, IDclave, nombre, color, fechaCreacion, iddueno, fechaEstado, higiene, cansancio, animo, hambre, salud FROM mascotas WHERE iddueno = ".$this->usrGlbl->id;
				
	   $res = mysql_query($sql,$conexion);
	   $fila = mysql_fetch_array($res);
	   	   
	   $objConectar->desconectar();
	   	   
	   return $this->getMascota( $fila );
	}
	
	function leeMascotasDeUsuario($usr )  
	{
		
	   $objConectar = new conexion();
       $conexion = $objConectar->conectar();
	   
	   $mascotasQueMeCompartieron = "SELECT idmascota FROM usuario_mascota WHERE idusuario = ".$this->usrGlbl->id;
	   
	   $sql = "SELECT idEspecie, IDclave, nombre, color, fechaCreacion, iddueno, fechaEstado, higiene, cansancio, animo, hambre, salud FROM mascotas WHERE iddueno = ".$usr->id." or IDclave in (".$mascotasQueMeCompartieron.")";
				
		$res = mysql_query($sql,$conexion);
	   
	   $arrayMascotas = array();
	   $i = 0;
	   
	   while( $fila = mysql_fetch_array($res) )
	   {
		   $arrayMascotas[$i++] = $this->getMascota( $fila );
	   }
	   
	   $objConectar->desconectar();
	   
	   return $arrayMascotas;
	}
	
	function getMascota( $fila )
	{
		$pet = new MascotaVO();
		
		$pet->idEspecie = $fila["idEspecie"];
		$pet->clave = $fila["IDclave"];
		$pet->nombre = $fila["nombre"];
		$pet->color = $fila["color"];
		$pet->f_creacion = $fila["fechaCreacion"];
		$pet->idduenio = $fila["iddueno"];
		$pet->f_estado = $fila["fechaEstado"];
		$pet->idservidor = $fila["IDclave"];
		
		$i = 0;
		
		$pet->atributos_masc[$i++] = $fila["higiene"];
		$pet->atributos_masc[$i++] = $fila["cansancio"];
		$pet->atributos_masc[$i++] = $fila["animo"];
		$pet->atributos_masc[$i++] = $fila["hambre"];
//		$pet->atributos_masc[$i++] = $fila["velocidad"];
		$pet->atributos_masc[$i++] = $fila["salud"];		
		/*
		higiene
		cansancio
		animo
		hambre
		velocidad
		salud
		*/
		
		return $pet;
	}
	
	function compartirMascota( $pet )
	{
		//   Este arreglo debería de tener al menos 2 elementos: 
		//   - La mascota
		//   - A quién se la comparto
		
		$objConectar = new conexion();
		$conexion = $objConectar->conectar();
		
		$sql = "INSERT INTO usuario_mascota VALUES(".$this->usrGlbl->id.", ".$pet->clave.")";
		//$res = mysql_query($sql,$conexion) or die( "Error: ".mysql_error()."[".$sql."]" );
		$res = mysql_query($sql,$conexion);
		
		$objConectar->desconectar();
		
		return 1;
	}

	function desCompartirMascotaDueno( $arreglo )
	{//esta funcion la ejecuta el wey que esta prestando su mascota
		//   Este arreglo debería de tener al menos 2 elementos: 
		
		//   - A quién se la comparto
		//   - La mascota
		
		$usr=$arreglo[0];
		$pet=$arreglo[1];
		$objConectar = new conexion();
		$conexion = $objConectar->conectar();
		
		$sql = "INSERT INTO usuario_mascota VALUES(".$usr->id.", ".$pet->clave.")";
		//$res = mysql_query($sql,$conexion) or die( "Error: ".mysql_error()."[".$sql."]" );
		$res = mysql_query($sql,$conexion);

		$objConectar->desconectar();	

		return 1;
	}
	
	function desCompartirMascotaInvitado( $pet )
	{
		//esta funcion la ejecuta el wey q tiene la mascota prestada 
		//   Este arreglo debería de tener al menos 2 elementos: 
		
		//   - A quién se la comparto
		//   - La mascota
		
		$objConectar = new conexion();
		$conexion = $objConectar->conectar();
		
		$sql = "INSERT INTO usuario_mascota VALUES(".$this->usrGlbl->id.", ".$pet->clave.")";
		//$res = mysql_query($sql,$conexion) or die( "Error: ".mysql_error()."[".$sql."]" );
		$res = mysql_query($sql,$conexion);
			
		$objConectar->desconectar();
		
		return 1;
	}
	
	function getIDMascota()
	{
		$objConectar = new conexion();
		$conexion = $objConectar->conectar();
			
		$sql = "SELECT IDClave from mascotas where iddueno = ".$this->usrGlbl->id;
		
		$res =  mysql_query($sql,$conexion);
		
		$num = mysql_result($res,0,0);	
		
		$objConectar->desconectar();
		
		return $num;
	}
	
	function compararFechas($arreglo)
	{
		 $F = $arreglo[1];
		 $mascota = $arreglo[0]; 
	
		 $objConectar = new conexion();
         $conexion = $objConectar->conectar();
		
		 $sql = "SELECT fechaAct_items FROM mascotas WHERE IDclave = ".$mascota;
		 $res = mysql_query($sql,$conexion) or die ("=/! ".$sql);
		 			 	 		 		 		 
		 //$fecha = $this->formatFecha(mysql_result($res,0,0));
		
		 $fecha = mysql_result($res,0,0);
		
		$arreglo_respuesta[1] = "itemsmascota";
		
		$objConectar->desconectar();
		 		
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


	
	function cambiarColor($color)
	{	
		$objConectar = new conexion();
        $conexion = $objConectar->conectar();
		
		$idMasc = $this->getIDMascota();
		
		$sql = "UPDATE mascotas SET color = ".$color." WHERE IDclave = ".$idMasc;
		$res = mysql_query($sql,$conexion);
		
		$objConectar->desconectar();
		
		return 1;
	}
	
	function cambiarNombre($nombre)
	{	
		$objConectar = new conexion();
        $conexion = $objConectar->conectar();
		
		$idMasc = $this->getIDMascota();
		
		$sql = "UPDATE mascotas SET nombre = '".$nombre."' WHERE IDclave = ".$idMasc;
		$res = mysql_query($sql,$conexion);
		
		$objConectar->desconectar();
		
		return 1;
	}
	
	
}
?>
