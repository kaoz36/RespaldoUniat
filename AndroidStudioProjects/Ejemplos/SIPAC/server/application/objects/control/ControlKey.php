<?php
    include_once 'conexion.php';
//	include_once ("vo/UsuarioVO.php");
	class ControlKey
	{
		var $u;
			
		function ControlKey()
		{ 
			$argv = func_get_args();
		    switch(func_num_args())
		    { 
		        case 0: 
		           // echo "Ejecución de miFuncion() sin parámetros";
		        break; 
		        
		        case 1: 
		           // echo "Ejecución de miFunction() con UN parámetro<br />\n"; 
		            $this->u = $argv[0];
		            break; 
		
		        default: 
		            echo "Error!.. número incorrecto de parámetros"; 
		    } 
		}
		
		/*
		function ControlKey($usr)
		{
			$this->u = $usr;			
		}   */
				
		
		function insertKey()
		{
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();

			$l = time()*2;                      
            $r =Rand(111,999);
            
            $llave = $l.$r;    
                      
            $sql = "INSERT INTO `boobob`.`keys` (`cod`, `idusuario`) VALUES (".$r.", ".$this->u->id.")";
            
			$res = mysql_query($sql,$conexion)or die ("Error: ".mysql_error()."\n".$sql);
			
			$objConectar->desconectar();
			
			return $r;
		}
		
		function deleteKey($cod)
		{
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();
                        
			$sql = "DELETE FROM `keys` where `cod` = ".$cod;	
			$res = mysql_query($sql,$conexion);
			
			$objConectar->desconectar();
		}
		
		function getKey($cod)
		{
			$objConectar = new conexion();
            $conexion = $objConectar->conectar();
                        
			$sql = 'SELECT `idusuario`'
                 . ' FROM `keys` where `cod` = '.$cod
                 . ' LIMIT 0, 30 ';	
			
			$res = mysql_query($sql,$conexion);
			
			$idusuario = mysql_result($res,0,0);
			
			$sql2 = "SELECT clave, nombre, cumple, foto, correo, login, fechaAct_items, fechaAct_mensajes, fechaAct_amigos, password FROM usuario WHERE clave = ".$idusuario;
			$res2 = mysql_query($sql2,$conexion);
			
			$person = new UsuarioVO();
			
			if(mysql_num_rows($res2) > 0 ) //El usuario Sí existe
			{
				$person->id = mysql_result($res2,0,0);
				$person->contrasenia = mysql_result($res2,0,9);
				$person->firstname = mysql_result($res2,0,1);
				$person->cumple = mysql_result($res2,0,2);
				$person->foto = mysql_result($res2,0,3);
				$person->email = mysql_result($res2,0,4);
				$person->login = mysql_result($res2,0,5);
				$person->confirmado = 1;
				$person->fechaAct_items = mysql_result($res2,0,6);
				$person->fechaAct_mensajes = mysql_result($res2,0,7);
				$person->fechaAct_amigos = mysql_result($res2,0,8);
								
				//sacar el dinero del usuario
				
				$sqlDinero = "Select cantidad from Dinero_usuarios where id_usuario = ".$person->id;    
             	$resDinero = mysql_query($sqlDinero,$conexion);
             	
       			
				$person->dinero = mysql_result($resDinero,0,0);
				
				$this->deleteKey($cod);
				
				$objConectar->desconectar();
				return $person; //Con la información leída de la base de datos
			}
			else //El usuario NO existe
			{
				$objConectar->desconectar();
				return 0;
			}
			
		}
		
		
	}
	

?>
