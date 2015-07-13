<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

require_once APPPATH . "controllers/commoncontroller.php";


class ProspeccionController extends CommonController {

	function __construct(){
		parent::__construct();
		$this->load->model('prospeccionmodel');
		
	}


	public function getAllPropecciones(){
		$prospecciones = $this->prospeccionmodel->getAllProspecciones();
		if($prospecciones != false)
		{
			echo json_encode(array('result' => $prospecciones->result()));
			return;
		}
		echo json_encode(array('error' => "No se pudo encontrar ninguna promotoria" ));
	}

	public function registerProspeccion()
	{
		$input_data = json_decode(file_get_contents('php://input'),true); 

		//var_dump($input_data);

	foreach ($input_data['items'] as &$item) {
		$insertResult = $this->prospeccionmodel->addProspeccion(array('poliza' => $item['poliza'], 
																			'plan' => $item['plan'],
																			'nombre' => $item['nombre'],
																			'edad' => $item['edad'],
																			'prima' => $item['prima'],
																			'bas' => $item['bas'],
																			'cma' => $item['cma'],
																			'tiba' => $item['tiba'],
																			'cii' => $item['cii'],
																			'bac' => $item['bac'],
																			'cat' => $item['cat'],
																			'bacy' => $item['bacy'],
																			'gfa' => $item['gfa'],
																			'bit' => $item['bit'],
																			'exc' => $item['exc'],
																			'gfc' => $item['gfc'],
																			'gfh' => $item['gfh'],
																			'ge' => $item['ge'],
																			'catp' => $item['catp'],
																			'zona' => $item['zona'],
																			'f_emi' =>date_format(date_parse($item['f_emi']),'Y-m-d'),
																			'f_u_vta' =>date_format(date_parse($item['f_u_vta']),'Y-m-d'),
																			'f_2216' => date_format(date_parse($item['f_2216']),'Y-m-d'),
																			'res_disp' => $item['res_disp'],
																			'inversion' => $item['inversion'],
																			'municipio' => $item['municipio'],
																			'fuma' => $item['fuma'],
																			'sexo' => $item['sexo'],
																			'estado_civil' => $item['estado_civil']));

		}
		$prospecciones = $this->prospeccionmodel->getAllProspecciones();
		if($prospecciones != false)
		{
				echo json_encode(array('result' => $prospecciones->result()));
				return;
		}else
		{
			echo json_encode(array('error' => 'Error al guardar el registro, por favor intentelo mas tarde' ));
		}
		
	}


	public function index()
	{
		echo "Prospeccion Controller";
	}

}
