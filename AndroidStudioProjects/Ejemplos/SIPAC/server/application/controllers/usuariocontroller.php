<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

require_once APPPATH . "controllers/commoncontroller.php";


class UsuarioController extends CommonController {

	function __construct(){
		parent::__construct();
		$this->load->model('usuariomodel');
		$this->load->model('promotorialocationmodel');
		
	}

	public function userLogin(){
		$input_data = json_decode(file_get_contents('php://input'),true); 
		$username =  $input_data['username'];
		$password =  $input_data['password'];
		$deviceId = isset($input_data['deviceId'])?$input_data['deviceId']:'';

		if($username != '' && $password != '' )
		{

			$prefix = substr($username,0,2);
			$promotoria = $this->usuariomodel->getPromotoria($prefix);
			if($promotoria != false)
			{
				$selectedPromotoria = $promotoria->row();
				$usuario = $this->usuariomodel->getUserByCredentials($username,md5($password),$selectedPromotoria->promot);
								
				if($usuario != false)
				{
					$selectedUsuario = $usuario->row();
					if($selectedUsuario->estado != 0 && $selectedUsuario->acceso != 0 )
					{
						if($deviceId  == '')
						{
							$result = array('promotoria' => $selectedPromotoria,'usuario' => $selectedUsuario);
							
							$configuracion = $this->usuariomodel->getConfiguraciones();
							if($configuracion != false)
							{
								$result['configuracion'] = $configuracion->row();
								$result['configuracion']->id_configuracion = $selectedPromotoria->mostrar_agte;
							}
							$result['status'] = "Dispositivo Agregado";
							$result['erase'] = FALSE;
							echo json_encode($result);	
							return;
						}
						$dispositivos = $this->promotorialocationmodel->getDispositivos($selectedPromotoria->promot,$selectedUsuario->Agente);
						
						if($dispositivos != false)
						{
							foreach($dispositivos->result() as $dispositivoItem)
							{
								if($dispositivoItem->id_dispositivo == $deviceId)
								{
									$result = array('promotoria' => $selectedPromotoria,'usuario' => $selectedUsuario);
									$configuracion = $this->usuariomodel->getConfiguraciones();
									if($configuracion != false)
									{
										$result['configuracion'] = $configuracion->row();
										$result['configuracion']->id_configuracion = $selectedPromotoria->mostrar_agte;
									}
									$result['status'] = "Dispositivo valido";
									$result['erase'] = FALSE;
									echo json_encode($result);	
									return;
								}
							}
							if(count($dispositivos->result()) < $selectedPromotoria->max_dispositivos)
							{	
								$insertResult = $this->promotorialocationmodel->addDispositivo($selectedPromotoria->promot,
																		array('id_agente' => $selectedUsuario->Agente,'id_dispositivo' => $deviceId));
								
								if($insertResult > 0)
								{
									$result = array('promotoria' => $selectedPromotoria,'usuario' => $selectedUsuario);
									$configuracion = $this->usuariomodel->getConfiguraciones();
									if($configuracion != false)
									{
										$result['configuracion'] = $configuracion->row();
										$result['configuracion']->id_configuracion = $selectedPromotoria->mostrar_agte;
									}
									$result['status'] = "Dispositivo Agregado";
									$result['erase'] = FALSE;
									echo json_encode($result);	
								}
								else
								{
									echo json_encode(array('error' => "No se pudo agregar el dsipositivo",'erase' => TRUE));
								}

							}else
							{
								echo json_encode(array('error' => "No se puede agregar otro dispositivo a la cuenta de agente",'erase' => TRUE));
								
							}
							return;
						}
						else
						{
							if($selectedPromotoria->max_dispositivos > 0)
							{
								$this->promotorialocationmodel->addDispositivo($selectedPromotoria->promot,
									array('id_agente' => $selectedUsuario->Agente,'id_dispositivo' => $deviceId));
							}else
							{
								echo json_encode(array('error' => "No se puede agregar otro dispositivo a la cuenta de agente",'erase' => TRUE));
								return;
							}

						}return;
						
					}
					else{
						echo json_encode(array('error' => "El usuario se encuentra dado de baja o inactivo",'erase' => TRUE));	
					}
					return;
				}else
				{
					echo json_encode(array('error' => 'Por favor verifique su nombre de Usuario o Password'));
				}
				
			}else
			{
				echo json_encode(array('error' => 'No Existe la promotoria registrada'));
			}
			return;
		}
		echo json_encode(array('error' => 'Por favor verifique su nombre de Usuario o Password'));
	}


	public function registerHistory(){
		$input_data = json_decode(file_get_contents('php://input'),true); 
		$insertResult = $this->usuariomodel->addHistory(array('id_usuario' => $input_data['id_usuario'], 
															'latitude' => $input_data['latitude'],
															'longitude' => $input_data['longitude'],
															'fecha' => $input_data['fecha']));

		
		if($insertResult)
		{
				echo json_encode(array('result' => 'Registro Guardado'));
				return;
		}else
		{
			echo json_encode(array('error' => 'Error al guardar Historial' ));
		}

		
	}

	public function getAllHistories(){
		$histories = $this->usuariomodel->getAllHistoriales();
		if($histories != false)
		{
			$this->load->view('busqueda_view',array('data'=>$histories->result()));
			return;
		}
		echo json_encode(array('error' => "No se pudo encontrar ninguna edad de cotizacion" ));
	}



	public function index()
	{
		echo "Usuario Controller Sipac 2";
	}

}
