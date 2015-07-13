<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

require_once APPPATH . "models/commonmodel.php";

class PromotoriaLocationModel extends CommonModel {

	function __construct(){
		parent::__construct();
	}
	
	public function addPromotoria($promotoriadata,$prefix){
			$database = $this->getSelectedDatabse($prefix);
			$database->insert('app_ubicaciones',$promotoriadata);
			return $database->insert_id();
	}

	public function getAllPromotorias($prefix){
		$database = $this->getSelectedDatabse($prefix);
		$database -> select('id_ubicacion,zona,nombre,retenedor,upago,prospectos,altitude,latitude,direccion');
		$queryResult = $database->get('app_ubicaciones');
		if($queryResult->num_rows() > 0)
		{
			return $queryResult; 
		}else{
			return false;
		}
	}

	public function addDispositivo($prefix,$data)
	{
		$database = $this->getSelectedDatabse($prefix);
		$database->insert('agentes_dispositivo',$data);
		return $database->insert_id();
	}

	public function getDispositivos($prefix,$agente)
	{
		$database = $this->getSelectedDatabse($prefix);
		$database->select('*');
		$database->where(array('id_agente'=>$agente));
		$results = $database->get('agentes_dispositivo');
		if($results->num_rows() > 0)
		{
			return $results; 
		}else{
			return false;
		}
	}

/*-----------------------------------   Polizas ----------------------------------------------*/
	public function countPolizas($prefix){
		$database = $this->getSelectedDatabse($prefix);
		$queryResult = $database->query("SELECT count(poliza) as total FROM app_poliza");
		if($queryResult->num_rows() > 0)
		{
			return $queryResult->row(); 
		}else{
			return false;
		}
	}

	public function getAllPolizas($prefix,$from){
		$database = $this->getSelectedDatabse($prefix);
		$query = $database->query("SELECT poliza AS id_poliza,
										  estatus AS status_poliza,
										  ret AS ret_poliza,
										  nombre AS nombre_poliza,
										  rfc AS rfc_poliza,
										  no_empleado AS emp_poliza,
										  sexo AS sexo_poliza,
										  estado_civil AS estado_civil_poliza,
										  fumador AS fuma_poliza,
										  fecha_nac AS nac_poliza,
										  domicilio AS domicilio_poliza,
										  cp AS cp_poliza,
										  tel AS tel_poliza,
										  cd AS pob_poliza,
										  email  AS email_poliza,
										  primas_pag AS prima_pag_poliza,
										  reserva AS res_poliza,
										  fondo_inv AS inv_poliza,
										  primas_pend AS prima_pen_poliza,
										  recibos_pend AS  rec_pen_poliza,
										  fecha_ult_desc AS ult_pago_poliza, 
										   prima_cobro AS prima_emi_poliza, 
										   prima_quincenal AS prima_quin_poliza, 
										   fe_inicial AS fec_ini_vig_poliza, 
										   fe_ultima AS fec_ult_mod_poliza, 
										   ultimo_ret AS fec_ult_ret_poliza, 
										   '0' AS orden_pago_poliza, 
										   '0' AS monto_poliza, 
										   '0' AS adicional_poliza, 
										   ref_bancaria_cob AS referencia_poliza, 
										   '' AS ref_alfa_poliza,
										   forma_pago AS forma_poliza, 
										   prima_cob AS prima_poliza, 
										   prima_esp_cob AS prima_esp_poliza, 
										   prima_desc_cob AS prima_desc_poliza, 
										   cpto_cob AS cpto_poliza, 
										   subret_cob AS sub_ret_poliza, 
										   ret_cob AS cve_cob_poliza, 
										   unidad_pago_cob AS uni_pago_poliza, 
										   ultimo_pago_cob AS ult_pago2_poliza, 
										   prima_quincenal AS quin_poliza, 
										   tp_mvto_cob AS tipo_mov_poliza,
										   signo_reserva,signo_fondoi,
										   quincena_aux_cob  FROM app_poliza LIMIT ".$from.",1000");
		return $query;
	}

/*------------------------------------  Servicios ---------------------------------------------*/
	public function countServicios($prefix){
		$database = $this->getSelectedDatabse($prefix);
		$queryResult = $database->query("SELECT count(poliza) as total FROM app_servicio");
		if($queryResult->num_rows() > 0)
		{
			return $queryResult->row(); 
		}else{
			return false;
		}
	}

	public function getAllServicios($prefix,$from){
		$database = $this->getSelectedDatabse($prefix);

		
		$query = $database->query("SELECT poliza AS id_poliza,
										  agente AS agente_serv,
										  clave AS id_serv,
										  descripcion AS desc_serv,
										  fecha_cap AS fecha_serv,
										  orden_pago,monto
										   FROM app_servicio LIMIT ".$from.",1000");
		return $query;
	}
	
/*------------------------------------ Coberturas -----------------------------------------------*/	

	public function countCoberturas($prefix){
		$database = $this->getSelectedDatabse($prefix);
		$queryResult = $database->query("SELECT count(Poliza) as total FROM app_cober");
		if($queryResult->num_rows() > 0)
		{
			return $queryResult->row(); 
		}else{
			return false;
		}
	}

	public function getAllCoberturas($prefix,$from){
		$database = $this->getSelectedDatabse($prefix);
		$query = $database->query("SELECT Poliza AS id_poliza,
										  prima AS prima_cob,clave_cob,
										  extra_prima AS prima_ext_cob,
										  sa AS suma_cob FROM  app_cober LIMIT ".$from.",1000");
		return $query;
	}

/*------------------------------------- Servicios Afectacion ---------------------------------------*/

	public function countServiciosVentaAfec($prefix){
		$database = $this->getSelectedDatabse($prefix);
		$queryResult = $database->query("SELECT count(poliza) as total FROM app_serv_venta_afecta");
		if($queryResult->num_rows() > 0)
		{
			return $queryResult->row(); 
		}else{
			return false;
		}
	}

 
	public function getAllServiciosVentaAfec($prefix,$from){
		$database = $this->getSelectedDatabse($prefix);
		$query = $database->query("SELECT poliza AS id_poliza,
										  clave_serv AS id_serv,
										  signo_prima AS mas_men_serv,
										  prima_serv AS prima_serv,
										  fecha_emi AS fec_emi_serv,
										  plan AS plan_serv FROM app_serv_venta_afecta LIMIT ".$from.",1000");
		return $query;
	}
	
/*------------------------------------- Servicios Internos --------------------------------------------*/

	public function countServiciosVentaInterno($prefix){
		$database = $this->getSelectedDatabse($prefix);
		$queryResult = $database->query("SELECT count(poliza) as total FROM app_serv_venta_interno");
		if($queryResult->num_rows() > 0)
		{
			return $queryResult->row(); 
		}else{
			return false;
		}
	}



	public function getAllServiciosVentaInterno($prefix,$from){
		$database = $this->getSelectedDatabse($prefix);
		$query = $database->query("SELECT poliza AS id_poliza,
										  agente AS id_agente_venta, 
										  fe_envio AS fec_env_venta, 
										  fe_emision AS fec_emi_venta, 
										  fe_entrega AS fec_ent_venta, 
										  fe_acepta AS fec_acc_venta, 
										  prima_inic AS prima_ini_venta, 
										  prima_emi AS prima_emi_venta, 
										  prima_em AS prima_tot_venta, 
										  no_servicio AS serv_venta,tipo_negocio FROM app_serv_venta_interno LIMIT ".$from.",1000");
		return $query;
	}

/*------------------------------------- Prospeccion --------------------------------------------*/

	public function countProspeccion($prefix){
		$database = $this->getSelectedDatabse($prefix);
		$queryResult = $database->query("SELECT count(Poliza) as total FROM app_prospeccion");
		if($queryResult->num_rows() > 0)
		{
			return $queryResult->row(); 
		}else{
			return false;
		}
	}

	public function getAllProspecciones($prefix,$from){
		$database = $this->getSelectedDatabse($prefix);
		$query = $database->query("SELECT estado_civil,
											fecha_nac,
											fuma,
											sexo,
											zona_prom,
											Poliza,
											fe_emision,
											prima_emi,
											RFC,
											Nombre,
											concepto,
											subcartera,
											ret,
											No_empleado,
											plan,
											forma_pago,
											reserva,
											signo_reserva,
											inversion,
											signo_inversion,
											ultimo_pago,
											incre20,
											bas,
											sabas,
											cma,
											tiba,
											cii,
											bac,
											bcat,
											bcatplus,
											bacy,
											gfa,
											gfc,
											gfh,
											bit,
											pft,
											ge,
											exc,
											ultimo_incr,
											fecha_ret_reserv,
											importe_ret_reserv,
											fecha_ret_inv,
											importe_ret_inv FROM app_prospeccion LIMIT ".$from.",1000");
		return $query;
	}

	/*----------------------------------------------------------------------------------------------*/

	public function saveLocation ($prefix,$data){
		$database = $this->getSelectedDatabse($prefix);
		$database->insert('agentes_ubicaciones',$data);
		return $database->insert_id();
	}

	public function saveChekin ($prefix,$data){
		$database = $this->getSelectedDatabse($prefix);
		$database->insert('agentes_checkin',$data);
		return $database->insert_id();
	}


	public function index()
	{
		echo "Promotorias Model";
	}
}


