<?php
 
 class MensajeVO
 {
	/*
	id	int(11)			No		auto_increment	 	 	 	 	 	 	
	destinatario	int(11)			No			 	 	 	 	 	 	
	remitente	int(11)			No			 	 	 	 	 	 	
	texto	varchar(140)	latin1_swedish_ci		No			 	 	 	 	 	 	 
	tipo	varchar(15)	latin1_swedish_ci		Yes	NULL		 	 	 	 	 	 	 
	estado	char(1)	latin1_swedish_ci		No			 	 	 	 	 	 	 
	item
	*/
	 
	 
  var $id;
  var $destinatario;
  var $remitente;
  var $texto;
  var $tipo;
  var $estado;
  var $adjunto;
  var $fecha;
  var $fechaRecibida;
  var $nombreRemitente;
  
  var $_explicitType="MensajeVO";

 }
 
?>
