<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

require_once APPPATH . "models/commonmodel.php";

class CotizadorModel extends CommonModel {

	function __construct(){
		parent::__construct();
	}
	
	
	public function getAllProfesiones(){
		$this->general_database->select('nombre,millar,accidente,invalidez');
		$queryResult = $this->general_database->get('cotizador_profesion');
		if($queryResult->num_rows() > 0)
		{
			return $queryResult; 
		}else{
			return false;
		}
	}

	public function addProfesion($profesiondata){
		 return $this->general_database->insert('cotizador_profesion',$profesiondata);
	}


	public function getAllEdades(){
		$this->general_database->select('edad,BAS,CII,CMA,TIBA,BCAT,GFA,GE,BIT,BASN,CMAN,TIBAN,GEN,CPCONY,CIIN,BCATN,CPCONYN,BITN,BASF,BASFN');
		$queryResult = $this->general_database->get('cotizador_edad');
		if($queryResult->num_rows() > 0)
		{
			return $queryResult; 
		}else{
			return false;
		}
	}
	
	public function addEdad($edaddata){
		 return $this->general_database->insert('cotizador_edad',$edaddata);
	}

	public function index()
	{
		echo "Controller model";
	}
}
