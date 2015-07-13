<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

require_once APPPATH . "models/commonmodel.php";

class UsuarioModel extends CommonModel {

	function __construct(){
		parent::__construct();
	}
	
	public function getConfiguraciones(){
		$queryResult = $this->general_database->get('configuracion');
		if($queryResult->num_rows() > 0)
		{
			return $queryResult; 
		}else{
			return false;
		}
	}
	
	public function getUserByCredentials($pUserName,$pPassword,$prefix){
		$array = array('nick' => $pUserName,'Password' => $pPassword);
		$database = $this -> getSelectedDatabse($prefix);
		$database->select('*');
		$database->where($array);
		$queryResult = $database->get('agentes');
		if($queryResult->num_rows() > 0)
		{
			return $queryResult; 
		}else{
			return false;
		}
	}

	public function getPromotoria($idPromotoria){
		$this->general_database->where('promot',$idPromotoria);
		$queryResult = $this->general_database->get('promotorias');
		if($queryResult->num_rows() > 0)
		{
			return $queryResult; 
		}else{
			return false;
		}
	}


	public function getAllHistoriales(){			
		$this->general_database->select('id_historial,id_usuario,latitude,longitude,fecha');
		$queryResult = $this->general_database->get('historial_busqueda');
		if($queryResult->num_rows() > 0)
		{
			return $queryResult; 
		}else{
			return false;
		}
	}

	public function addHistory($historyData){
			 return $this->general_database->insert('historial_busqueda',$historyData);
	}
	
	public function index()
	{
		echo "User model";
	}
}
