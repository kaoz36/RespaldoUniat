/** *************************************************************************
 *
 *   Copyright (c)  2013 by Jaguar Labs.
 *   Confidential and Proprietary
 *   All Rights Reserved
 *
 *   This software is furnished under license and may be used and copied
 *   only in accordance with the terms of its license and with the
 *   inclusion of the above copyright notice. This software and any other
 *   copies thereof may not be provided or otherwise made available to any
 *   other party. No title to and/or ownership of the software is hereby
 *   transferred.
 *
 *   The information in this software is subject to change without notice and
 *   should not be construed as a commitment by JaguarLabs.
 *
 * @(#)$Id: $
 * Last Revised By   : Raul Acevedo
 * Last Checked In   : $Date: $
 * Last Version      : $Revision:  $
 *
 * Original Author   : Raul Acevedo raul.acevedo@jaguarlabs.com
 * Origin            : SEnE -- April 25 @ 11:00 (PST)
 * Notes             :
 *
 * *************************************************************************/

package com.jaguarlabs.sipac.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SipacOpenHelper extends SQLiteOpenHelper{
	
	

	public static final String TABLE_POLIZA = "poliza";
	public static final String TABLE_COBERTURA = "cobertura";
	public static final String TABLE_SERV_VENTA = "servicio_venta";
	public static final String TABLE_SERV_AFECT = "servicio_afect";
	public static final String TABLE_SERV_GRAL = "servicio_general";
	
	public static final String TABLE_EDADES = "cotizador_edad";
	public static final String TABLE_PROFESIONES = "cotizador_profesion";
	public static final String TABLE_PROSPECCIONES = "prospecciones";
	
	public static final String DBNAME = "sipac.db";
	public static final int DBVERSION = 19;
	

	public static final String CREATE_PROSPECCIONES = "CREATE TABLE IF NOT EXISTS "+TABLE_PROSPECCIONES+ "(" +
	"  id_prospeccion INTEGER not null primary key autoincrement," +
	"  estado_civil  varchar(3) NOT NULL," +
	"  fecha_nac varchar(20) NOT NULL," +
	"  fuma varchar(20) NOT NULL," +
	"  sexo varchar(3) NOT NULL," +
	"  zona_prom varchar(10) NOT NULL," +
	"  Poliza varchar(10) NOT NULL," +
	"  fe_emision varchar(20) NOT NULL," +
	"  prima_emi varchar(20) NOT NULL," +
	"  RFC varchar(20) NOT NULL," +
	"  Nombre varchar(100) NOT NULL," +
	"  concepto varchar(10) NOT NULL," +
	"  subcartera varchar(10) NOT NULL," +
	"  ret varchar(10) NOT NULL," +
	"  No_empleado varchar(15) NOT NULL," +
	"  plan varchar(10) NOT NULL," +
	"  forma_pago varchar(3) NOT NULL," +
	"  reserva varchar(20) NOT NULL," +
	"  signo_reserva varchar(3) NOT NULL," +
	"  inversion varchar(20) NOT NULL," +
	"  signo_inversion varchar(3) NOT NULL," +
	"  ultimo_pago varchar(20) NOT NULL," +
	"  incre20 varchar(3) NOT NULL," +
	"  bas varchar(3) NOT NULL," +
	"  sabas varchar(3) NOT NULL," +
	"  cma varchar(3) NOT NULL," +
	"  tiba varchar(3) NOT NULL," +
	"  cii varchar(3) NOT NULL," +
	"  bac varchar(3) NOT NULL," +
	"  bcat varchar(3) NOT NULL," +
	"  bcatplus varchar(3) NOT NULL," +
	"  bacy varchar(3) NOT NULL," +
	"  gfa varchar(3) NOT NULL," +
	"  gfc varchar(3) NOT NULL," +
	"  gfh varchar(3) NOT NULL," +
	"  bit varchar(3) NOT NULL," +
	"  pft varchar(3) NOT NULL," +
	"  ge varchar(3) NOT NULL," +
	"  exc varchar(3) NOT NULL," +
	"  ultimo_incr varchar(20) NOT NULL," +
	"  fecha_ret_reserv varchar(20) NOT NULL," +
	"  importe_ret_reserv varchar(20) NOT NULL," +
	"  fecha_ret_inv varchar(20) NOT NULL," +
	"  importe_ret_inv  varchar(20) NOT NULL" +
	")";
	
	public static final String CREATE_PROFESIONES = "CREATE TABLE IF NOT EXISTS "+ TABLE_PROFESIONES+" (" +
			" id_profesion INTEGER not null primary key autoincrement," +
			" nombre  varchar(50) NOT NULL," +
			" millar float NOT NULL," +
			" accidente float NOT NULL," +
			" invalidez  float NOT NULL" +
			" )";
	
	public static final String CREATE_EDADES="CREATE TABLE IF NOT EXISTS "+TABLE_EDADES+"(" +
	" edad int(11) NOT NULL," +
	" BAS float NOT NULL," +
	" CII float NOT NULL," +
	" CMA float NOT NULL," +
	" TIBA float NOT NULL," +
	" BCAT float NOT NULL," +
	" GFA float NOT NULL," +
	" GE float NOT NULL," +
	" BIT float NOT NULL,"+
	" BASN float NOT NULL,"+	
	" CMAN float NOT NULL,"+
	" TIBAN float NOT NULL,"+	
	" GEN float NOT NULL,"+
	" CPCONY float NOT NULL,"+
	" CIIN float NOT NULL,"+
	" BCATN float NOT NULL,"+
	" CPCONYN float NOT NULL," +
	" BITN float NOT NULL,"+
	" BASF float NOT NULL,"+
	" BASFN float NOT NULL,"+
	" PRIMARY KEY  (edad))";
	
	public static final String CREATE_POLIZA= "CREATE TABLE "+TABLE_POLIZA+" (" +
			"poliza_id INTEGER not null primary key autoincrement," +
			"id_poliza VARCHAR(10) NOT NULL," +
			"status_poliza VARCHAR(5) NOT NULL," +
			"ret_poliza VARCHAR(10) NOT NULL," +
			"nombre_poliza VARCHAR(30) NOT NULL," +
			"rfc_poliza VARCHAR(10) NOT NULL," +
			"emp_poliza VARCHAR(5) NOT NULL," +
			"sexo_poliza VARCHAR(5) NOT NULL," +
			"estado_civil_poliza VARCHAR(5) NOT NULL," +
			"fuma_poliza VARCHAR(5) NOT NULL," +
			"nac_poliza VARCHAR(15)  NOT NULL," +
			"domicilio_poliza VARCHAR(100) NOT NULL," +
			"cp_poliza VARCHAR(20) NOT NULL," +
			"tel_poliza VARCHAR(20) NOT NULL," +
			"pob_poliza VARCHAR(20) NOT NULL," +
			"email_poliza VARCHAR(20) NOT NULL," +
			"prima_pag_poliza VARCHAR(10) NOT NULL," +
			"res_poliza VARCHAR(10) NOT NULL," +
			"inv_poliza VARCHAR(10) NOT NULL," +
			"prima_pen_poliza VARCHAR(10) NOT NULL," +
			"rec_pen_poliza VARCHAR(10) NOT NULL," +
			"ult_pago_poliza VARCHAR(10) NOT NULL," +
			"prima_emi_poliza VARCHAR(10) NOT NULL," +
			"prima_quin_poliza VARCHAR(10) NOT NULL," +
			"fec_ini_vig_poliza VARCHAR(10) NOT NULL," +
			"fec_ult_mod_poliza VARCHAR(10) NOT NULL," +
			"fec_ult_ret_poliza VARCHAR(10) NOT NULL," +
			"orden_pago_poliza VARCHAR(10) NOT NULL," +
			"monto_poliza VARCHAR(10) NOT NULL," +
			"adicional_poliza VARCHAR(10) NOT NULL," +
			"referencia_poliza VARCHAR(10) NOT NULL," +
			"ref_alfa_poliza VARCHAR(10) NOT NULL," +
			"forma_poliza VARCHAR(10) NOT NULL," +
			"prima_poliza VARCHAR(10) NOT NULL," +
			"prima_esp_poliza VARCHAR(10) NOT NULL," +
			"prima_desc_poliza VARCHAR(45) NOT NULL," +
			"cpto_poliza VARCHAR(45) NOT NULL," +
			"sub_ret_poliza VARCHAR(10) NOT NULL," +
			"cve_cob_poliza VARCHAR(10) NOT NULL," +
			"uni_pago_poliza VARCHAR(45) NOT NULL," +
			"ult_pago2_poliza VARCHAR(10) NOT NULL," +
			"quin_poliza VARCHAR(10) NOT NULL," +
			"tipo_mov_poliza VARCHAR(10) NOT NULL," +
			"signo_reserva VARCHAR(3) NOT NULL," +
			"signo_fondoi VARCHAR(3) NOT NULL"+
			")";
	
	public static final String CREATE_COBERTURA= "CREATE TABLE "+TABLE_COBERTURA+" (" +
			"  id INTEGER not null primary key autoincrement," +
			"  id_poliza VARCHAR(10) NOT NULL," +
			"  prima_cob VARCHAR(10) NOT NULL," +
			"  prima_ext_cob VARCHAR(10) NOT NULL," +
			"  suma_cob VARCHAR(10) NOT NULL," +
			"  clave_cob VARCHAR(10) NOT NULL" +
			")";	
	
	
	public static final String CREATE_SERV_VENTA = "CREATE TABLE "+TABLE_SERV_VENTA+" " +
			"(id INTEGER not null primary key autoincrement," +
			"  id_poliza VARCHAR(20) NOT NULL," +
			"  id_agente_venta VARCHAR(20) NOT NULL," +
			"  fec_env_venta VARCHAR(20) NOT NULL," +
			"  fec_emi_venta VARCHAR(20) NOT NULL," +
			"  fec_ent_venta VARCHAR(20) NOT NULL," +
			"  fec_acc_venta VARCHAR(20) NOT NULL," +
			"  prima_ini_venta IVARCHAR(20) NOT NULL," +
			"  prima_emi_venta VARCHAR(20) NOT NULL,"+
			"  prima_tot_venta VARCHAR(20) NOT NULL,"+
			"  serv_venta VARCHAR(20) NOT NULL,"+
			"  tipo_negocio VARCHAR(20) NOT NULL"+
			")";	
	
	public static String CREATE_SERV_AFECT = "CREATE TABLE "+TABLE_SERV_AFECT+" (" +
			"  id INTEGER not null primary key autoincrement," +
			"  id_poliza VARCHAR(20) NOT NULL," +
			"  id_serv VARCHAR(20) NOT NULL," +
			"  mas_men_serv VARCHAR(5) NOT NULL," +
			"  prima_serv VARCHAR(20) NOT NULL," +
			"  fec_emi_serv VARCHAR(25) NOT NULL," +
			"  plan_serv VARCHAR(15) NOT NULL" +
			")";

	public static String CREATE_SERV_GRAL = "CREATE TABLE "+TABLE_SERV_GRAL+" (" +
			"  id INTEGER not null primary key autoincrement," +
			"  id_poliza VARCHAR(10) NOT NULL," +
			"  agente_serv VARCHAR(20) NOT NULL," +
			"  id_serv VARCHAR(10) NOT NULL," +
			"  desc_serv VARCHAR(40) NOT NULL,"  +
			"  fecha_serv VARCHAR(10) NOT NULL,"  +
			"  orden_pago VARCHAR(5) NOT NULL,"  +
			"  monto VARCHAR(5) NOT NULL"  +
			")";

	public static SipacOpenHelper instance;
	
	
	public SipacOpenHelper(Context context)throws Exception {
			super(context, DBNAME, null, DBVERSION);
			if(instance != null) throw new Exception("Error de Singleton");
	}
	
	public static SipacOpenHelper getInstance(Context context) {
		if (instance == null) {
			try {
				instance = new SipacOpenHelper(context);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
			}
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_POLIZA);
		db.execSQL(CREATE_COBERTURA);
		db.execSQL(CREATE_SERV_GRAL);
		db.execSQL(CREATE_SERV_VENTA);
		db.execSQL(CREATE_SERV_AFECT);
		db.execSQL(CREATE_EDADES);
		db.execSQL(CREATE_PROFESIONES);
		db.execSQL(CREATE_PROSPECCIONES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_POLIZA);
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_COBERTURA);
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERV_GRAL);
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERV_VENTA);
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERV_AFECT);
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDADES);
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFESIONES);
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROSPECCIONES);
		 onCreate(db);
	}
	
	public void clearDatabase(){
		SQLiteDatabase db = getWritableDatabase();
		onUpgrade(db, 0, 0);
		db.close();
		close();
	}
	
	

}
