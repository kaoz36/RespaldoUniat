<?php

include_once("control/ControlActivity.php");

	class FachadaActivity
	{
		var $usuario;
		var $ctrActivity;
		function FachadaActivity( $usr )
		{
			$this->usuario = $usr;
			
			 //ctrTransferencia = new ControlTransferencia( );
		}
		
		function recibirTransferencia($funcion, $objetoTransferente)
		{
			
			$ctrActivity = new ControlActivity($this->usuario);
			
					
			//return "Entro en recibirTransferencia";
			switch( $funcion )
			{
				case "getActivity":					 					
					 return $ctrActivity->getActivity($objetoTransferente );
				break;
				case "addVote":					 					
					 return $ctrActivity->addVote($objetoTransferente );
				break;
				case "getVotes":					 					
					 return $ctrActivity->getVotes($objetoTransferente );
				break;
				case "getNewActivities":					 					
					 return $ctrActivity->getNewActivities($objetoTransferente );
				break;
				case "getTopActivities":					 					
					 return $ctrActivity->getTopActivities($objetoTransferente );
				break;
				
				case "getSimilarActivities":					 					
					 return $ctrActivity->getSimilarActivities($objetoTransferente );
				break;
				
				case "getListActivities":
					return $ctrActivity->getListActivities($objetoTransferente );
				break;
				
				default:
				
				break;
			}
		}		
	}
?>
