<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class CommonController extends CI_Controller {

	protected function getDateInfo(){
		date_default_timezone_set('America/Mexico_City');
		$date = date('Y-m-d H:i:s',time());
		return $date;
	}
}