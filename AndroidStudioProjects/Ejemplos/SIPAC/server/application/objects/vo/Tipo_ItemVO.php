<?php
class Tipo_ItemVO
{
	// Declare the same properties that are
	// declared in our AS PersonVO class.
	  var $nombre;
	  var $id;	  
	  var $_explicitType="Tipo_ItemVO";
	
	  function Beneficio_ItemVO( )
	  { }
	  
	  function setID($i)
	  {
	   $id = $i;    
	  }
	 
	  function setNombre($n)
	  {
	   $nombre = $n;
	  }
	  
	  function getID()
	  {
	  	return $id;
	  }	
	  
	  function getNombre()
	  {
	  	return $nombre;
	  }	  
}
?>
