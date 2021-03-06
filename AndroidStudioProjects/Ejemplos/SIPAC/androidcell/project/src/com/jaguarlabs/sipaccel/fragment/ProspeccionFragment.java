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
 * Original Author   : Julio Hernandez  julio.hernadez@jaguarlabs.com
 * Origin            : SEnE -- April 25 @ 11:00 (PST)
 * Notes             :
 *
 * *************************************************************************/

package com.jaguarlabs.sipaccel.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.jaguarlabs.sipaccel.MainAplicationActivity;
import com.jaguarlabs.sipaccel.R;
import com.jaguarlabs.sipaccel.dialog.DatePickerDialog;
import com.jaguarlabs.sipaccel.model.DataModel;
import com.jaguarlabs.sipaccel.net.RPCHandler;
import com.jaguarlabs.sipaccel.tables.ReportesTable;
import com.jaguarlabs.sipaccel.util.IJSONResponseHandler;
import com.jaguarlabs.sipaccel.util.Utils;

public class ProspeccionFragment extends RightFragment{

	EditText retendor;
	EditText subcartera;
	TextView zona;
	CheckBox sinCMA;
	private CheckBox sinTIBA;
	private CheckBox sinGFA;
	private CheckBox sinCII;
	private CheckBox sinBIT;
	private CheckBox sinBAC;
	private CheckBox sinEXC;
	private CheckBox sinCAT;
	private CheckBox sinGFH;
	private CheckBox sinCATPLUS;
	private CheckBox sinGFC;
	private CheckBox sinBACY;
	private CheckBox sinGE;
	private RadioButton femenino;
	private RadioButton masculino;
	private RadioButton ambos;
	private RadioButton todos;
	private RadioButton soltero;
	private RadioButton casado;
	private RadioButton divorciado;
	private RadioButton viudo;
	private RadioButton union;
	private EditText editPrima;
	private EditText editBas;
	private EditText editRI;
	private Spinner spinPrima;
	private Spinner spinBas;
	private Spinner spinRI;
	private String where;
	private Intent inte;
	private TextView ret_fecha;
	private TextView ultimo_incr;
	
	@Override
	protected void makeView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		ImageButton menu;
		MainAplicationActivity.MainAppTouch iteration = new MainAplicationActivity.MainAppTouch(listener);
		mainView = inflater.inflate( R.layout.fragment_prospeccion, container, false );
		menu = (ImageButton) mainView.findViewById(R.id.menu);
		menu.setOnTouchListener( iteration );
		menu.setOnClickListener( iteration );
		((Button) mainView.findViewById( R.id.reporteButton )).setOnClickListener( getVerResultados() );
		Seters();
		Spinner();
	}
	
	private void Seters(){
		retendor = (EditText) mainView.findViewById( R.id.retendor );
		zona = (TextView) mainView.findViewById( R.id.zona );
		sinCMA = (CheckBox) mainView.findViewById( R.id.sinCMA );
		sinTIBA = (CheckBox) mainView.findViewById( R.id.sinTIBA );
		sinGFA = (CheckBox) mainView.findViewById( R.id.sinGFA);
		sinCII = (CheckBox) mainView.findViewById( R.id.sinCII );
		sinBIT = (CheckBox) mainView.findViewById( R.id.sinBIT );
		sinBAC = (CheckBox) mainView.findViewById( R.id.sinBAC );
		sinEXC = (CheckBox) mainView.findViewById( R.id.sinEXC );
		sinCAT = (CheckBox) mainView.findViewById( R.id.sinCAT );
		sinGFH = (CheckBox) mainView.findViewById( R.id.sinGFH );
		sinCATPLUS = (CheckBox) mainView.findViewById( R.id.sinCATplus );
		sinGFC = (CheckBox) mainView.findViewById( R.id.sinGFC );
		sinBACY = (CheckBox) mainView.findViewById( R.id.sinBACY );
		sinGE = (CheckBox) mainView.findViewById( R.id.sinGE );
		subcartera = (EditText) mainView.findViewById( R.id.subCarteraEdit );
		femenino = (RadioButton) mainView.findViewById( R.id.radioButtonF );
		masculino = (RadioButton) mainView.findViewById( R.id.radioButtonM );
		ambos = (RadioButton) mainView.findViewById( R.id.radioButtonA );
		todos = (RadioButton) mainView.findViewById( R.id.RadioButtonTodos );
		soltero = (RadioButton) mainView.findViewById( R.id.RadioButtonSoltero );
		casado = (RadioButton) mainView.findViewById( R.id.RadioButtonCasado );
		divorciado = (RadioButton) mainView.findViewById( R.id.radioButtonDivorciado );
		viudo = (RadioButton) mainView.findViewById( R.id.RadioButtonViudo );
		union = (RadioButton) mainView.findViewById( R.id.RadioButtonUnion );
		editPrima = (EditText) mainView.findViewById( R.id.editPrimaSearch );
		editBas = (EditText) mainView.findViewById( R.id.editBasSearch );
		editRI = (EditText) mainView.findViewById( R.id.editFriSearch );
		spinPrima = (Spinner) mainView.findViewById( R.id.spinnerPrima );
		spinBas = (Spinner) mainView.findViewById( R.id.spinnerBAS );
		spinRI = (Spinner) mainView.findViewById( R.id.spinnerRI );
		
		ultimo_incr = (TextView) mainView.findViewById( R.id.ventaDe );
		ret_fecha = (TextView) mainView.findViewById( R.id.ventaAl );
		ret_fecha.setOnClickListener( getPickerDate( ret_fecha ) );
		ultimo_incr.setOnClickListener(getPickerDate( ultimo_incr ) );
		
		ambos.setChecked(true);
		todos.setChecked(true);
	}
	
	private void Spinner(){
		
		ArrayList<String> ope = new ArrayList<String>();
		ope.add("<=");
		ope.add("==");
		ope.add(">=");
		
		spinPrima.setAdapter( getSpinnerStyle(ope) );
		spinBas.setAdapter( getSpinnerStyle(ope) );
		spinRI.setAdapter( getSpinnerStyle(ope) );

	}
	
	public ArrayAdapter<String> getSpinnerStyle(ArrayList<String> list) {
		if (list == null)
			list = new ArrayList<String>();
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, list) {
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				return v;
			}

			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {
				View v = super.getDropDownView(position, convertView, parent);
				v.setPadding(0, 5, 0, 5);
				((TextView) v).setGravity( Gravity.CENTER );
				((TextView) v).setTextSize( 20 );				
				return v;
			}
		};
		return dataAdapter;
	}
	
	private OnClickListener getPickerDate(TextView date ){
		return new DatePickerOnClickListener(date,caller); 
	}
	
	private OnClickListener getVerResultados(){
		
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<String> condiciones;
				
				condiciones = new ArrayList<String>();
				
				where = "";
				
				inte = new Intent( caller.getApplicationContext(), ReportesTable.class );
				
				if( retendor.getText().length() > 0 ){
					condiciones.add("ret='" + retendor.getText().toString() + "'");
				}
				if( subcartera.getText().toString().trim().length() > 0 ){
					condiciones.add("subcartera='" + subcartera.getText().toString() + "'");
				}
				
				if (ret_fecha.getText().toString().trim().length() > 0)
				{
					condiciones.add("Datetime(fecha_ret_reserv) < Datetime('"+ret_fecha.getText().toString()+"') AND  Datetime(fecha_ret_reserv) < Datetime('"+ret_fecha.getText().toString()+"') ");					
				}
				
				if (ultimo_incr.getText().toString().trim().length() > 0)
				{
					condiciones.add("Datetime(ultimo_incr) < Datetime('"+ultimo_incr.getText().toString()+"')");					
				}
				
				if( sinCMA.isChecked() ){
					condiciones.add("cma='0'");
				}
				if( sinTIBA.isChecked() ){
					condiciones.add("tiba='0'");
				}
				if( sinGFA.isChecked() ){
					condiciones.add("gfa='0'");
				}
				if( sinCII.isChecked() ){
					condiciones.add("cii='0'");
				}
				if( sinBIT.isChecked() ){
					condiciones.add("bit='0'");
				}
				if( sinBAC.isChecked() ){
					condiciones.add("bac='0'");
				}
				if( sinEXC.isChecked() ){
					condiciones.add("CAST (exc AS DECIMAL (20, 2) )=0");
				}
				if( sinCAT.isChecked() ){
					condiciones.add("bcat='0'");
				}
				if( sinGFH.isChecked() ){
					condiciones.add("gfh='0'");
				}
				if( sinCATPLUS.isChecked() ){
					condiciones.add("bcatplus='0'");
				}
				if( sinGFC.isChecked() ){
					condiciones.add("gfc='0'");
				}
				if( sinBACY.isChecked() ){
					condiciones.add("bacy='0'");
				}
				if( sinGE.isChecked() ){
					condiciones.add("ge='0'");
				}

				if( femenino.isChecked() ){
					condiciones.add("sexo=\"F\"");
				}
				if( masculino.isChecked() ){
					condiciones.add("sexo=\"M\"");
				}
				if( ambos.isChecked() ){}
				if( todos.isChecked() ){}
				if( soltero.isChecked() ){
					condiciones.add("estado_civil=\"S\"");
				}
				if( casado.isChecked() ){
					condiciones.add("estado_civil='C'");
				}
				if( divorciado.isChecked() ){
					condiciones.add("estado_civil='D'");
				}
				if( viudo.isChecked() ){
					condiciones.add("estado_civil='V'");
				}
				if( union.isChecked() ){
					condiciones.add("estado_civil='U'");
				}
				if( editPrima.getText().length() > 0 ){
					condiciones.add("CAST (prima_emi AS DECIMAL(20,2))" + spinPrima.getSelectedItem().toString() + editPrima.getText().toString() );
				}
				if( editBas.getText().length() > 0 ){
					condiciones.add("CAST (sabas AS DECIMAL(20,2))" + spinBas.getSelectedItem().toString() + editBas.getText().toString() );
				}
				if( editRI.getText().length() > 0 ){
					condiciones.add("CAST (reserva AS DECIMAL(20,2))" + spinRI.getSelectedItem().toString() + editRI.getText().toString() );
				}
				where = TextUtils.join(" and ", condiciones);
				
//				where = "SELECT * FROM prospecciones " + where;
				
				Log.i("Consulta", where );
				
				if( inte == null ){
					Log.i("log", "inte = null" );
					return;
				}
				
				inte.putExtra("where", where);
				startActivity(inte);
				caller.overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
				registerReports();
			}
		};
	}
	
	private void registerReports(){
		if( Utils.getGPSStatus(caller) ){
			LocationManager locationManager = ( LocationManager ) caller.getSystemService( Context.LOCATION_SERVICE );
			Criteria criteria = new Criteria();
			String provider = locationManager.getBestProvider( criteria, true );
			Location location = locationManager.getLastKnownLocation( provider );
			if( location != null ){
				saveReport( location );
			}
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	private void saveReport( Location pLocation ){
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String currenDate = formatter.format( new Date() );
		
		(new RPCHandler( new IJSONResponseHandler() {
			
			@Override
			public void onResponse(JSONObject jsonResponse) throws Exception {
				Log.i("Save Search Location", "Localizacion Guardada");				
			}
			
			@Override
			public void onRequest() {
				Log.i("Save Search Location", "Guardando Localización de busqueda");
			}
			
			@Override
			public void onException(Exception e) {
				Log.i("Save Search Location", "Error Guardando Localización de busqueda");
			}
		})).execute( RPCHandler.OPERATION_SAVE_SEARCH, 
				new BasicNameValuePair("id_usuario", DataModel.getInstance().getAppUser().getAgente()),
				new BasicNameValuePair("latitude", pLocation.getLatitude() + ""),
				new BasicNameValuePair("longitude", pLocation.getLongitude() + ""),
				new BasicNameValuePair("fecha", currenDate));
		
	}


}


class DatePickerOnClickListener implements OnClickListener{
	private TextView date;
	private Context context;
	public DatePickerOnClickListener (TextView pDate,Context context){
		date = pDate;
		this.context = context;
	}
	
	public void onClick(View v) {
		DatePickerDialog dialog = new DatePickerDialog(context, date);
		dialog.show();
	}
}
