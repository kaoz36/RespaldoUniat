<?php
class ItemVO
{
	// Declare the same properties that are
	// declared in our AS PersonVO class.
	  var $clave;
	  var $precio;
	  var $nombre;
	  var $marca;
	  var $url;
	  var $disponible_offline;
  	  var $tipo;
	  var $fecha_agregado;
	  var $fecha_modificado;
	  var $categoria;
	  var $_explicitType="ItemVO";
	
	  function ItemVO( )
	  { }
	  
	  function setIdClave($id)
	  {
	   $clave = $id;    
	  }
	 
	  function setNombre($nom)
	  {
	   $nombre = $nom;
	  }
	  
	  function setPrecio($p)
	  {
	   $precio = $p;
	  }
	  
	  function setMarca($m)
	  {
	   $marca = $m;
	  }
	  
	  function setUrl($ur)
	  {
	   $url = $ur;
	  }
	  
	  function setTipo($t)
	  {
	   $tipo = $t;
	  }
	  
	  function setDisponibilidad($d)
	  {
	  	$disponible_offline = $d;
	  }
	   
	  function setFecha($f)
	  {
	  	$fecha_agregado = $f;
	  }
	  
	  function setFechaModificado($fm)
	  {
	  	$fecha_modificado = $fm;
	  }
	  
	  function getClave()
	  {
	  	return $clave;
	  }	
	  
	  function getNombre()
	  {
	  	return $nombre;
	  }
	  
	  function getMarca()
	  {
	  	return $marca;
	  }
	  
	  function getUrl()
	  {
	  	return $url;
	  }
	  
	  function getPrecio()
	  {
	  	return $precio;
	  }
	  
	  function getDisponibilidad ()
	  {
	  	return $disponible_offline;
	  }
	  
	  function getFecha()
	  {
	  	return $fecha_agregado;
	  }	
	  
	  function getFechaModificado()
	  {
	  	return $fecha_modificado;
	  }
	  
	  function getTipo()
	  {
	  	return $tipo;
	  }				
}
?>
