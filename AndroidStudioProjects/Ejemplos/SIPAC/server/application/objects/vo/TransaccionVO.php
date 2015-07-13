<?php
class TransaccionVO
{
	// Declare the same properties that are
	// declared in our AS PersonVO class.
	var $_explicitType="TransaccionVO";
	var $id;
	var $idusuario;
	var $fecha;
	var $cantidad;
	var $saldo;
	var $accion;
	var $objeto;
	var $detalle;
	
	function TransaccionVO( )
	{
		
	}
	/*
	function setId($i)
	{
		$id = $i;
	}
	
	
	function getId()
	{
		return $id;
	}
	
	function setIdUsuario($iu)	
	{
		$idusuario = $iu;
	}
	
	function getIdUsuario()	
	{
		return $idusuario;
	}
	
	function setFecha($f)
	{
		$fecha = $f;
	}
        
        function getFecha()
        {
                return $fecha;
        }

        function setCantidad($ca)
        {
            $cantidad = $ca;
        }

        function getCantidad()
        {
            return $cantidad;
        }

        function setSaldo($sal)
        {
            $saldo = $sal;
        }

        function getSaldo()
        {
            return $saldo;
        }

        function setAccion($a)
        {
            $accion = $a;
        }

        function getAccion()
        {
            return $accion;
        }
        
        function setObjeto($ob)
        {
                $objeto = $ob;
        }

        function getObjeto()
        {
            return $objeto;
        }

        function setDetalle($de)
        {
            $detalle= $de;
        }

        function getDetalle()
        {
            return $detalle;
        }	
		*/
}
?>
