<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

require_once APPPATH . "controllers/commoncontroller.php";


class PromotoriaLocationController extends CommonController {

	function __construct(){
		parent::__construct();
		$this->load->model('promotorialocationmodel');
		
	}


	public function getAllPromotorias(){
		$input_data = json_decode(file_get_contents('php://input'),true); 
		$prefijo =  $input_data['promotoria_id'];
		if($prefijo != '')
		{
			$promotorias = $this->promotorialocationmodel->getAllPromotorias($prefijo);
			if($promotorias != false)
			{
				echo json_encode(array('result' => $promotorias->result()));
				return;
			}
			echo json_encode(array('error' => "No se pudo encontrar ninguna promotoria" ));
		}
		else
		{
			echo json_encode(array('error' => 'Clave de Promotoria incorrecto' ));
		}
	}

	public function registerPromotoria()
	{
		$input_data = json_decode(file_get_contents('php://input'),true); 
		$prefijo =  $input_data['promotoria_id'];
		if($prefijo != '')
		{
			$insertResult = $this->promotorialocationmodel->addPromotoria(array('nombre' => $input_data['nombre'], 
																			'retenedor' => $input_data['retenedor'],
																			'upago' => $input_data['upago'],
																			'prospectos' => $input_data['prospectos'],
																			'altitude' => $input_data['altitude'],
																			'latitude' => $input_data['latitude'],
																			'direccion' => $input_data['direccion'],
																			'zona' => $input_data['zona']),
																			 $prefijo);
			$promotorias = $this->promotorialocationmodel->getAllPromotorias($prefijo);
			if($promotorias != false)
			{
					echo json_encode(array('result' => $promotorias->result(),'promotoriaId' => $insertResult));
					return;
			}else
			{
				echo json_encode(array('error' => 'Error al guardar el registro, por favor intentelo mas tarde' ));
			}
		}else
		{
			echo json_encode(array('error' => 'Clave de Promotoria incorrecto' ));
		}
	}


	public function backupPolizas()
	{
		$prefijo = $this->uri->segment(3);
		$from = $this->uri->segment(4);

		$this->load->dbutil();
		if($prefijo != FALSE)
		{
			echo json_encode(array('total' => $this->promotorialocationmodel->countPolizas($prefijo)->total ,
								   'data' => $this->dbutil->csv_from_result($this->promotorialocationmodel->getAllPolizas($prefijo,$from),",","|")));
			return;	
		}
		else
		{
			echo json_encode(array('error' => 'Clave de Promotoria incorrecto' ));
		}
	}


	public function backupCoberturas()
	{
		$prefijo = $this->uri->segment(3);
		$from = $this->uri->segment(4);

		$this->load->dbutil();
		if($prefijo != FALSE)
		{
			echo json_encode(array('total' => $this->promotorialocationmodel->countCoberturas($prefijo)->total ,
								   'data' => $this->dbutil->csv_from_result($this->promotorialocationmodel->getAllCoberturas($prefijo,$from),",","|")));
			return;	
		}
		else
		{
			echo json_encode(array('error' => 'Clave de Promotoria incorrecto' ));
		}
	}

	public function backupServicios()
	{
		$prefijo = $this->uri->segment(3);
		$from = $this->uri->segment(4);

		$this->load->dbutil();
		if($prefijo != FALSE)
		{
			echo json_encode(array('total' => $this->promotorialocationmodel->countServicios($prefijo)->total ,
								   'data' => $this->dbutil->csv_from_result($this->promotorialocationmodel->getAllServicios($prefijo,$from),",","|")));
			return;	
		}
		else
		{
			echo json_encode(array('error' => 'Clave de Promotoria incorrecto' ));
		}
	}

	public function backupServiciosAfectacion()
	{
		$prefijo = $this->uri->segment(3);
		$from = $this->uri->segment(4);

		$this->load->dbutil();
		if($prefijo != FALSE)
		{
			echo json_encode(array('total' => $this->promotorialocationmodel->countServiciosVentaAfec($prefijo)->total ,
								   'data' => $this->dbutil->csv_from_result($this->promotorialocationmodel->getAllServiciosVentaAfec($prefijo,$from),",","|")));
			return;	
		}
		else
		{
			echo json_encode(array('error' => 'Clave de Promotoria incorrecto' ));
		}
	}

	public function backupServiciosInternos()
	{
		$prefijo = $this->uri->segment(3);
		$from = $this->uri->segment(4);

		$this->load->dbutil();
		if($prefijo != FALSE)
		{
			echo json_encode(array('total' => $this->promotorialocationmodel->countServiciosVentaInterno($prefijo)->total ,
								   'data' => $this->dbutil->csv_from_result($this->promotorialocationmodel->getAllServiciosVentaInterno($prefijo,$from),",","|")));
			return;	
		}
		else
		{
			echo json_encode(array('error' => 'Clave de Promotoria incorrecto' ));
		}
	}

	public function backupProspecciones()
	{
		$prefijo = $this->uri->segment(3);
		$from = $this->uri->segment(4);

		$this->load->dbutil();
		if($prefijo != FALSE)
		{
			echo json_encode(array('total' => $this->promotorialocationmodel->countProspeccion($prefijo)->total ,
								   'data' => $this->dbutil->csv_from_result($this->promotorialocationmodel->getAllProspecciones($prefijo,$from),",","|")));
			return;	
		}
		else
		{
			echo json_encode(array('error' => 'Clave de Promotoria incorrecto' ));
		}
	}


	public function saveLocation()
	{
		$input_data = json_decode(file_get_contents('php://input'),true); 
		$prefijo =  $input_data['promotoria_id'];
		$agente_id =  $input_data['agente_id'];
		$latitude =  $input_data['latitude'];
		$longitude =  $input_data['longitude'];

		if(strlen($agente_id) > 0 &&
		   strlen($prefijo) > 0  &&
		   is_numeric($latitude) &&
		   is_numeric($longitude))
		{
			
			$insertResult = $this->promotorialocationmodel->saveLocation($prefijo,
				array('agente' => $agente_id, 
				      'fh' => $this->getDateInfo(),
					  'latitude' => $latitude+0,
					  'longitude' => $longitude+0));

			if($insertResult > 0){
				echo json_encode(array('result' => 'Ubicacion Guardada Exitosamente'));
			}else
			{
				echo json_encode(array('error' => 'Error al guardar la ubicacion'));
			}

		}else
		{
			echo json_encode(array('error' => 'Error al guardar la ubicacion'));
		}

	}

	public function getAllLocations()
	{

	}

	public function saveChekin()
	{
		$input_data = json_decode(file_get_contents('php://input'),true); 
		$prefijo =  $input_data['promotoria_id'];
		$agente_id =  $input_data['agente_id'];
		$latitude =  $input_data['latitude'];
		$longitude =  $input_data['longitude'];
		$ubicacion = $input_data['ubicacion_id'];
		if(strlen($agente_id) > 0 &&
		   strlen($prefijo) > 0  &&
		   strlen($ubicacion) > 0  &&
		   is_numeric($latitude) &&
		   is_numeric($longitude))
		{
			$insertResult = $this->promotorialocationmodel->saveLocation($prefijo,
				array('agente' => $agente_id, 
				      'fh' => $this->getDateInfo(),
					  'latitude' => $latitude+0,
					  'longitude' => $longitude+0));

			if($insertResult > 0){
				$insertResultCheckin = $this->promotorialocationmodel->saveChekin($prefijo,
					array('id_ubicacion' => $ubicacion, 
				          'id_localizacion' => $insertResult));
				if($insertResultCheckin > 0){
					echo json_encode(array('result' => 'Checkin Guardado Exitosamente'));
				}
			}

		}else
		{
			echo json_encode(array('error' => 'Error al guardar la ubicacion'));
		}

	}

	public function getAllChekin()
	{
		$input_data = json_decode(file_get_contents('php://input'),true); 
		$prefijo =  $input_data['promotoria_id'];

	}




	public function index()
	{
		echo "Promotorias Location Controller";
	}

}
