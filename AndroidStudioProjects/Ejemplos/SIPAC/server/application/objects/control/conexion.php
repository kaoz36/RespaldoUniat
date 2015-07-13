<?
	Class conexion{
		
		var $laConexion;

		function conectar( ){

			if( !$this->laConexion = @mysql_connect("p50mysql207.secureserver.net", "boobob", "BellaBoo333", "TRUE", "65536" ) )
			{
				return -1;
				exit( );
			}
			if( !mysql_select_db( "boobob", $this->laConexion ) )
			{
				echo "The data base doesn't exist";
				exit( );
			}
			return $this->laConexion;
		}

		function desconectar(){

			mysql_close( $this->laConexion );
		}

	}
?>
