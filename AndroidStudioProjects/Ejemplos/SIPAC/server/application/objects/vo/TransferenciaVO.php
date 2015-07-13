<?php
	//Clase transferencia
	
	class TransferenciaVO
	{
		/*
		  Contiene los atributos:
		  
		  - usuario (email/loggin  y  password)
		  - origen
		  - objeto a enviar
		  - Clave de autentificacion
		  
		  Muchas veces, el *objeto a enviar* será un usuario con la misma información que el *usuario*, pero nos conviene
		  esa redundancia porque va encaminada para fines distintos y además para el resto de las transferencias donde
		  *objeto a enviar* no será un usuario 
		
		*/
		var $_explicitType="TransferenciaVO";
		var $dato1;
		var $dato2;
		var $dato3;
		var $objetoTransferente;
		var $funcion;
		var $modulo;
	}

?>
