<?php

include_once("control/ControlItem.php");

	class FachadaItem
	{
		var $usuario;
		var $ctrItem;
		function FachadaItem( $usr )
		{
			$this->usuario = $usr;
			
			 //ctrTransferencia = new ControlTransferencia( );
			//$ctrUsuario = new PersonService( );
		}
		
		function recibirTransferencia($funcion, $objetoTransferente)
		{
			
			$ctrItem = new ControlItem($this->usuario);
			
			//return "Entro en recibirTransferencia";
			switch( $funcion )
			{
				case "consultarItemsUsuario":					 					
					 return $ctrItem->consultarItemsUsuario( );
				break;
				
				case "consultarItemsMascota": 
					return $ctrItem->consultarItemsMascota( $objetoTransferente );
				break;
				
				case "quitarItemUsuario":					
					return $ctrItem->quitarItemAUsuario( $objetoTransferente );
				break;
				
				case "quitarItemMascota":
					return $ctrItem->quitarItemAMascota( $objetoTransferente );
				break;
				
				case "agregarItemUsuario":
					return $ctrItem->agregarItemAUsuario( $objetoTransferente );
				break;
				
				case "agregarItemMascota":
					return $ctrItem->agregarItemAMascota( $objetoTransferente );
				break;
				
				case "obtenerItems":
					return $ctrItem->obtenerItems($objetoTransferente );
				break;
				
				case "getAllItems":
					return $ctrItem->obtenerTodosItems();
				break;
				
				case "getAllMarcas":
					return $ctrItem->getAllMarcas();
				break;
				
				case "getNewItems":
					return $ctrItem->obtenerNuevosItems($objetoTransferente);
				break;
				
				case "enviarItemsUsuario":
					return $ctrItem->itemsUsuarioEnviados($objetoTransferente);
				break;
				
				case "enviarItemsMascota":
					return $ctrItem->itemsMascotaEnviados($objetoTransferente);
				break;
				
				case "buscaActualizacionMarcas":
					return $ctrItem->compararFechasMarcas($objetoTransferente);
				break;			
				
				case "enviarMarcas":
					return $ctrItem->marcasEnviadas($objetoTransferente);
				break;
				
				default:
				
				break;
			}
		}		
	}
?>
