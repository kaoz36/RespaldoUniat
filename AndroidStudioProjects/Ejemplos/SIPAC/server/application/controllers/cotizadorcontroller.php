<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

require_once APPPATH . "controllers/commoncontroller.php";


class CotizadorController extends CommonController {

	function __construct(){
		parent::__construct();
		$this->load->model('cotizadormodel');
		
	}



	public function getAllProfesionesCotizador(){
		$profesiones = $this->cotizadormodel->getAllProfesiones();
		if($profesiones != false)
		{
			echo json_encode(array('result' => $profesiones->result()));
			return;
		}
		echo json_encode(array('error' => "No se pudo encontrar ninguna edad de cotizacion" ));
	}


	public function registerProfesiones()
	{
		$input_data = json_decode(file_get_contents('php://input'),true);
		foreach ($input_data['items'] as &$item) {
			$insertResult = $this->cotizadormodel->addProfesion(array('nombre' => $item['nombre'], 
															'millar' => $item['millar'],
															'accidente' => $item['accidente'],
															'invalidez' => $item['invalidez']));	    
		} 

		$profesiones = $this->cotizadormodel->getAllProfesiones();
		if($profesiones != false)
		{
			echo json_encode(array('result' => $profesiones->result()));
			return;
		}else{
			echo json_encode(array('error' => 'Error al guardar el registro, por favor intentelo mas tarde' ));
		}
	}


	public function getAllEdadesCotizador(){
		$edades = $this->cotizadormodel->getAllEdades();
		if($edades != false)
		{
			echo json_encode(array('result' => $edades->result()));
			return;
		}
		echo json_encode(array('error' => "No se pudo encontrar ninguna edad de cotizacion" ));
	}



	public function registerEdades()
	{
		$input_data = json_decode(file_get_contents('php://input'),true);

		foreach ($input_data['items'] as &$item) {
			$insertResult = $this->cotizadormodel->addEdad(array('edad' => $item['edad'], 
															'BAS' => $item['BAS'],
															'CII' => $item['CII'],
															'CMA' => $item['CMA'],
															'TIBA' => $item['TIBA'],
															'BCAT' => $item['BCAT'],
															'GFA' => $item['GFA'],
															'GE' => $item['GE'],
															'BIT' => $item['BIT'],
															'BASN' => $item['BASN'],
															'CMAN' => $item['CMAN'],
															'TIBAN' => $item['TIBAN']));	    
		} 

		$edades = $this->cotizadormodel->getAllEdades();
		if($edades != false)
		{
			echo json_encode(array('result' => $edades->result()));
			return;
		}else{
			echo json_encode(array('error' => 'Error al guardar el registro, por favor intentelo mas tarde' ));
		}
	}


	public function index()
	{
		echo "Cotizador Controller";
	}

}
