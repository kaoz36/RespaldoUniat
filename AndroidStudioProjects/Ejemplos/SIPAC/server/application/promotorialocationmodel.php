<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class PromotoriaLocationModel extends CI_Model {

	function __construct(){
		parent::__construct();
	}
	

	
	public function addPromotoria($promotoriadata){
			 return $this->db->insert('promotorialocation',$promotoriadata);
	}

	public function getAllPromotorias(){
		$this->db->select('id_promotoria, nombre, retenedor,upago,prospectos,altitude,latitude');
		$queryResult = $this->db->get('promotorialocation');
		if($queryResult->num_rows() > 0)
		{
			return $queryResult; 
		}else{
			return false;
		}
	}
	
	public function index()
	{
		echo "Promotorias Model";
	}
}
