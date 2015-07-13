<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class CommonModel extends CI_Model {

	protected $general_database;

	function __construct(){
		parent::__construct();
		$this->general_database = $this->load->database('sipac',TRUE);
	}

	protected function getSelectedDatabse($prefix){
		return $this->load->database($prefix,TRUE);
	}
	
	
	public function index()
	{
		echo "Common Controller";
	}
}
