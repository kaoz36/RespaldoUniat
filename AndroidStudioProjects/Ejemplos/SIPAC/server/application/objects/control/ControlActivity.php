<?php
include_once 'conexion.php';
include_once 'vo/BooActivityVO.php';

class ControlActivity
{
	 var $usrGlbl;
	 
	function ControlActivity($usr )
	{
		 $this->usrGlbl=$usr;
	}
	 
	
	function getActivity( $activityNumber )
	{
	   $objConectar = new conexion();
       $conexion = $objConectar->conectar();
	   
	   $sql = "SELECT * FROM activities WHERE id = ".$activityNumber;
				
	   $res = mysql_query($sql,$conexion);
	   $fila = mysql_fetch_array($res);
	   	   
	   $objConectar->desconectar();
	   	   
	   return $this->getActVo( $fila );
	}
	
	
	function getNewActivities( $numberActivities )
	{
	   $objConectar = new conexion();
       $conexion = $objConectar->conectar();
	   $sql = "SELECT * FROM  activities ORDER BY id DESC LIMIT ".$numberActivities;
	   $res = mysql_query($sql,$conexion);
	   $return=array();	
	   while ($row = mysql_fetch_array($res)) {
   			array_push($return,$this->getActVo( $row ));
       }
	   $objConectar->desconectar();
	   return $return;
	}
	function getTopActivities( $numberActivities )
	{
	   $objConectar = new conexion();
       $conexion = $objConectar->conectar();
	   $sql = "select * from `activities` as a,(SELECT avg(b.vote) voto,a.id FROM `activities` as a, activities_votes as b WHERE b.activity=a.id group by a.id) as b where a.id=b.id and a.type!='bio' group by voto desc order by visits desc limit ".$numberActivities;
	   $res = mysql_query($sql,$conexion);
	   $return=array();	
	   while ($row = mysql_fetch_array($res)) {
   			array_push($return,$this->getActVo( $row ));
       }
	   $objConectar->desconectar();
	   return $return;
	}
	
	function getSimilarActivities( $obj )
	{
	   $objConectar = new conexion();
       $conexion = $objConectar->conectar();
       
        $cat = $obj->categoria;
       $type = $obj->type;
       $elID = $obj->id;
       
       if(!$elID)
       {
       	return;
       }
       
	   $sql = "SELECT * FROM (	SELECT * FROM  `activities` WHERE (	`type` =  '".$type."'	OR categoria =  '".$cat."'	) AND ( id NOT IN ( ".$elID." ))
			 					UNION 
								SELECT * FROM  `activities` WHERE TYPE =  'advertising')B
				ORDER BY RAND( )";

	   $res = mysql_query($sql,$conexion);
	   $return=array();	
	   while ($row = mysql_fetch_array($res)) {
   			array_push($return,$this->getActVo( $row ));
       }
	   $objConectar->desconectar();
	   return $return;
	}
	
	function getActVo($fila){
		$act = new BooActivityVO();
		
		 $act->id= $fila["id"];
		 $act->swf= $fila["swf"];
		 $act->image= $fila["image"];
		 $act->background= $fila["background"];
		 $act->name= $fila["name"];
		 $act->updateTime= $fila["updateTime"];
		 $act->visits= $fila["visits"];
		 $act->description= $fila["description"];
		 $act->type= $fila["type"];
		 $act->offlineAvailable= $fila["offlineAvailable"];
		 $act->selectedMonsterEnabled= $fila["selectedMonsterEnabled"];
		  $act->edad= $fila["edad"];
		 $act->categoria= $fila["categoria"];
		   $act->tags= $fila["tags"];
		 $act->url= $fila["url"];
		   $act->thumbnail= $fila["thumbnail"];
		 $act->uri_name= $fila["uri_name"];
		 
		return $act;
		}
	function addVote($transf){
	
		$vote=$transf[0];
		$activity=$transf[1];
	   $objConectar = new conexion();
       $conexion = $objConectar->conectar();
	   $ip=$_SERVER['REMOTE_ADDR'];  
	   if($vote>5){
		   $vote=5;
		   }else if($vote<=1){
			   $vote=1;
			   }
	   
	   $sql = "INSERT into activities_votes (activity,vote,ip,user) values (".$activity.",".$vote.",'".$ip."','". $this->usrGlbl->id."')";
				
	   $res = mysql_query($sql,$conexion);
	   	   
	   $objConectar->desconectar();
	   	   
	   return $sql;
		}
	function getVotes( $activityNumber )
	{
	   $objConectar = new conexion();
       $conexion = $objConectar->conectar();
	   
	   $sql = "SELECT ROUND(AVG(vote)) AS vote FROM activities_votes WHERE activity = ".$activityNumber;
		
	   $res = mysql_query($sql,$conexion);
	   $fila = mysql_fetch_array($res);
	   	   
	   $objConectar->desconectar();
	   	   
	   return $fila["vote"];
	}
	
	function getListActivities( $tipos )
	{
		$objConectar = new conexion();
        $conexion = $objConectar->conectar();
       	
        $sql = "SELECT * FROM activities WHERE type in ('".implode("','",$tipos)."')";
        //$sql = "SELECT * FROM activities WHERE type in ('game')";
	    $res = mysql_query($sql,$conexion);
	    
	    //return ($res);
	    
	    //return $sql;
	    
	   	$return=array();	
	   	while ($row = mysql_fetch_array($res))
	   	{
   			array_push($return,$this->getActVo( $row ));
       	}
	   	$objConectar->desconectar();
	   	
	   	return $return;
	}
	
}
?>
