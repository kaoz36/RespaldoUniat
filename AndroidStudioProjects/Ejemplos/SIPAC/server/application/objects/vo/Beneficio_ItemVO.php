<?php
class Beneficio_ItemVO
{
	// Declare the same properties that are
	
	  var $clave;
	  var $cantifad;
	  var $atributo;
	  var $_explicitType="Beneficio_ItemVO";
	
	  function Beneficio_ItemVO( )
	  { }
	  
	  function setClave($id)
	  {
	   $clave = $id;    
	  }
	 
	  function setCantidad($c)
	  {
	   $cantidad = $c;
	  }
	  
	  function setAtributo($a)
	  {
	   $patributo = $a;
	  }
	  
	  function getClave()
	  {
	  	return $clave;
	  }	
	  
	  function getCantidad()
	  {
	  	return $cantidad;
	  }
	  
	  function getAtributo()
	  {
	  	return $atributo;
	  }
	  
}
?>
