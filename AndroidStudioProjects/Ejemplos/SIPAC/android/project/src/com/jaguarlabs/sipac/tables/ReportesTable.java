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

package com.jaguarlabs.sipac.tables;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.jaguarlabs.sipac.R;
import com.jaguarlabs.sipac.db.CommonDAO;
import com.jaguarlabs.sipac.db.SipacOpenHelper;
import com.jaguarlabs.sipac.model.DataModel;
import com.jaguarlabs.sipac.util.ICallback;
import com.jaguarlabs.sipac.util.Task;
import com.jaguarlabs.sipac.vo.ProspeccionVO;

public class ReportesTable extends Activity implements ICallback{

	private String where;
	private String html;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		this.requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.show_table_reportes );
		
		headerBar();
		where = extras.getString( "where" );
		Log.i("Where", where);
		searchProspeccion();
	}
	
	private void headerBar(){
		ImageButton backReport = (ImageButton) findViewById( R.id.backReports );
		backReport.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	private void searchProspeccion(){		
		final String htmlHead = "<html>" +
								"<head><style>*{padding:0;margin:0}table{border-collapse:collapse;width:3000px;}table,th, td{border:1px solid black;}th{color:#FFF;background-color: #546E9E;}td{padding:10px;text-align: center;vertical-align:middle;}tr.odd {background-color: #EEEEED;}tr.even {background-color: #849ECE;}</style>"+
								"</head>" +
								"<body><table>" +
								"<tr>" +
								"<th>PLAN</th>" +
								"<th>POLIZA</th>" +
								"<th>N. EMPLEADO</th>" +
								"<th>RFC</th>" +
								"<th>NOMBRE</th>" +
								"<th>F. NACIMIENTO</th>" +
								"<th>FUMA</th>" +
								"<th>SEXO</th>" +
								"<th>ESTADO CIVIL</th>" +
								"<th>PRIMA M.</th>" +
								"<th>S.A.BAS</th>" +
								"<th>CMA</th>" +
								"<th>TIBA</th>" +
								"<th>CII </th>" +
								"<th>BAC</th>" +
								"<th>CAT</th>" +
								"<th>BACY</th>" +
								"<th>GFA</th>" +
								"<th>BIT</th>" +
								"<th>EXC</th>" +
								"<th>GFC</th>" +
								"<th>GFH</th>" +
								"<th>GE</th>" +
								"<th>CATP</th>" +
								"<th>ZONA</th>" +
								"<th>CONCEPTO</th>" +
								"<th>SUB-CARTERA</th>" +
								"<th>RETENEDOR</th>" +
								"<th>FORMA PAGO</th>" +
								"<th>SIGNO RESERVA</th>" +
								"<th>RESERVA</th>" +
								"<th>ULTIMO INCREMENTO</th>" +
								"<th>F. EMI.</th>" +
								"<th>F. U. PAGO</th>" +
								"<th>F. RET. RESERVA</th>" +
								"<th>IMPORTE DE RETENCION</th>" +
								"<th>SIGNO INVERSION</th>" +
								"<th>INVERSION</th>" +
								"<th>Incre20</th>" +
								"</tr>";

//		CommonDAO< ProspeccionVO > prospeccionDS = new CommonDAO<ProspeccionVO>(this, ProspeccionVO.class, 
//				SipacOpenHelper.TABLE_PROSPECCIONES );
//		prospeccionDS.open();
//		DataModel.getInstance().setProspec( prospeccionDS.getFilteredItems( where ) );
	    	
		html = htmlHead;
		Task t = new Task( this );
		t.post_case = 4;
		t.execute();
//		int i=0;
//		for( ProspeccionVO reportes:DataModel.getInstance().getProspec() ){
//				html += reportes.encodeHTML(((i++%2 == 0)?"even":"odd")); 
//		}
//		html+= htmlTail;
//		WebView wv = (WebView) findViewById( R.id.webView1 );
//		wv.getSettings().setBuiltInZoomControls( true );
//		wv.getSettings().setSupportZoom( true );
//		wv.loadData(html, "text/html; charset=UTF-8", null);
//		prospeccionDS.close();
	}
	
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_right);
	}

	private CommonDAO< ProspeccionVO > prospeccionDS;
	
	@Override
	public void callback() {
		
		WebView wv = (WebView) findViewById( R.id.webView1 );
		wv.getSettings().setBuiltInZoomControls( true );
		wv.getSettings().setSupportZoom( true );
		wv.loadData(html, "text/html; charset=UTF-8", null);
		prospeccionDS.close();
	}

	@Override
	public void time() {
		prospeccionDS = new CommonDAO<ProspeccionVO>(this, ProspeccionVO.class,SipacOpenHelper.TABLE_PROSPECCIONES );
		prospeccionDS.open();
		where = where.equals("") ? "" : (" WHERE " + where);
		DataModel.getInstance().setProspec( prospeccionDS.runQuery( "SELECT * FROM " + SipacOpenHelper.TABLE_PROSPECCIONES + where) );
		int i=0;
		final String htmlTail =	"</table></body>" +
		"</html>";
		for( ProspeccionVO reportes:DataModel.getInstance().getProspec() ){
				html += reportes.encodeHTML(((i++%2 == 0)?"even":"odd"));
				if( i > 100 ){
					break;
				}
		}
		html+= htmlTail;		
	}

	@Override
	public void callback(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		
	}
	
}
