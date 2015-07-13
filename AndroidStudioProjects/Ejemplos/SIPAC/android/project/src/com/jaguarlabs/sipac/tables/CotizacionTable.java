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

import java.text.NumberFormat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.jaguarlabs.sipac.R;
import com.jaguarlabs.sipac.model.DataModel;
import com.jaguarlabs.sipac.util.CotizadorUtil;
import com.jaguarlabs.sipac.vo.CotizacionVO;

public class CotizacionTable extends Activity {

	private CotizacionVO myEstado;
	private double Prospeccion[];
	private double in;
	private double ca;
	private EditText inInput;
	private EditText caInput;

	private double[] calcularProspeccion(double in, double ca, double exc,
			int n_per) {
		double arr[] = new double[n_per + 1];
		double res_par;

		for (int i = 0; i <= n_per; i++) {
			res_par = 0;
			if (i != 0) {
				res_par = arr[i - 1] * Math.pow((in / 12) + 1, 12);
				res_par += (exc * Math.pow(1 - ca, 2) / 12) * sumatoria(in);
			}
			arr[i] = res_par;
		}

		return arr;
	}

	private double sumatoria(double in) {
		double acumulador = 0;
		for (int i = 0; i < 12; i++) {
			acumulador += Math.pow((in / 12) + 1, 12 - i);
		}
		return acumulador;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_table_cotizacion);
		myEstado = DataModel.getInstance().getEstadoCotizador();
		inInput = (EditText) findViewById(R.id.interesGarantizadoInput);
		caInput = (EditText) findViewById(R.id.costoAdministracionInput);
		Prospeccion = calcularProspeccion(.06, .005,
				myEstado.getPrimaExcedente(), 20);
		cotizacion();
		headerBar();
	}

	private void headerBar() {
		ImageButton backReport = (ImageButton) findViewById(R.id.backReports);
		backReport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	public void calculo(View pView) {
		pView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				in = (inInput.getText().toString().equals("") ? 6 : Double
						.parseDouble(inInput.getText().toString())) / 100;
				ca = (caInput.getText().toString().equals("") ? .5 : Double
						.parseDouble(caInput.getText().toString())) / 100;
				Prospeccion = calcularProspeccion(in, ca,
						myEstado.getPrimaExcedente(), 20);
				cotizacion();
			}
		});
	}

	private void cotizacion() {
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		String basString = myEstado.isDobas() ? ("<tr>"
				+ "<td>BAS</td>"
				+ "<td>"
				+ nf.format((long) CotizadorUtil.getInstance()
						.redondea(myEstado.getBas()))
				+ "</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(
						CotizadorUtil.getInstance().getBAS(
								myEstado.getEdadCalculo(),
								myEstado.getProfesion(), myEstado.getBas())))
				+ "</td>" + "</tr>") : "";

		String bitString = myEstado.isDobit() ? ("<tr>" + "<td>BIT</td>"
				+ "<td>Cubierto</td>" + "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(myEstado.getBit()))
				+ "</td>" + "</tr>") : "";

		String ciiString = myEstado.isDocii() ? ("<tr>"
				+ "<td>CII</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(myEstado.getCii()))
				+ "</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(
						CotizadorUtil.getInstance().getCII(
								myEstado.getEdadCalculo(),
								myEstado.getProfesion(), myEstado.getCii())))
				+ "</td>" + "</tr>") : "";

		String cmaString = myEstado.isDocma() ? ("<tr>"
				+ "<td>CMA</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(myEstado.getCma()))
				+ "</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(
						CotizadorUtil.getInstance().getCMA(
								myEstado.getEdadCalculo(),
								myEstado.getProfesion(), myEstado.getCma())))
				+ "</td>" + "</tr>") : "";

		String tibaString = myEstado.isDotiba() ? ("<tr>"
				+ "<td>TIBA</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(myEstado.getTiba()))
				+ "</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(
						CotizadorUtil.getInstance().getTIBA(
								myEstado.getEdadCalculo(),
								myEstado.getProfesion(), myEstado.getTiba())))
				+ "</td>" + "</tr>") : "";

		String catString = myEstado.isDocat() ? ("<tr>"
				+ "<td>CAT</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(myEstado.getCat()))
				+ "</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(
						CotizadorUtil.getInstance().getCAT(
								myEstado.getEdadReal(), myEstado.getCat(),
								myEstado.getSexo()))) + "</td>" + "</tr>") : "";

		String gfaString = myEstado.isDogfa() ? ("<tr>"
				+ "<td>GFA</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(myEstado.getGfa()))
				+ "</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(
						CotizadorUtil.getInstance().getGFA(
								myEstado.getEdadCalculo(),
								myEstado.getProfesion(), myEstado.getGfa())))
				+ "</td>" + "</tr>") : "";

		String geString = myEstado.isDoge() ? ("<tr>"
				+ "<td>GE</td> "
				+ "<td>"
				+ nf.format((long) CotizadorUtil.getInstance().redondea(myEstado.getGe()))
				+ "</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(
						CotizadorUtil.getInstance().getGE(
								myEstado.getEdadCalculo(),
								myEstado.getProfesion(), myEstado.getGe())))
				+ "</td>" + "</tr>") : "";

		String hijosString = myEstado.isDogfh() ? ("<div class=\"blu\">"
				+ "<h4>Hijos a asegurar:</h4>" + "<p>"
				+ myEstado.getnHijos()
				+ "</p>"
				+ "</div>"
				+

				"<table>"
				+ "<thead>"
				+ "<tr>"
				+ "<th>Cobertura</th>"
				+ "<th>Suma Asegurada</th>"
				+ "<th>Prima A. Total</th>"
				+ "</tr>"
				+ "</thead>"
				+ "<tbody>"
				+ "<tr>"
				+ "<td>GFH</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(myEstado.getGfh()))
				+ "</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(
						CotizadorUtil.getInstance().getGFH(
								myEstado.getSelectedGFH(), myEstado.getGfh())))
				+ "</td>" + "</tr>" + "</tbody>" + "</table>") : "";

		String conyugeString = myEstado.isDoconyuge() ? ("<div class=\"blu\">"
				+ "<h4>Sexo:</h4>"
				+ "<p>"
				+ ((myEstado.getSexo() == CotizadorUtil.FEMENINO) ? "Masculino"
						: "Femenino") + "</p>" + "<h4>Edad:</h4>" + "<p>"
				+ myEstado.getEdadConyuge() + "</p>" + "</div>" + "<table>"
				+ "<thead>" + "<tr>" + "<th>Cobertura</th>"
				+ "<th>Suma Asegurada</th>" + "<th>Prima A. Total</th>"
				+ "</tr>" + "</thead>") : "";

		String bacyString = myEstado.isDobacy() ? ("<tr>"
				+ "<td>BACY</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(myEstado.getBacy()))
				+ "</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(
						CotizadorUtil.getInstance().getBACY(
								myEstado.getEdadConyuge(), myEstado.getBacy())))
				+ "</td>" + "</tr>") : "";

		String gfcString = myEstado.isDogfc() ? ("<tr>"
				+ "<td>GFC</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(myEstado.getGfc()))
				+ "</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(
						CotizadorUtil.getInstance().getBACY(
								myEstado.getEdadConyuge(), myEstado.getGfc())))
				+ "</td>" + "</tr>" + (myEstado.isDogpc() ? "" : "</table>"))
				: "";

		String gpcString = myEstado.isDogpc() ? ("<tr>"
				+ "<td>GPC</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(myEstado.getGpc()))
				+ "</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil
						.getInstance()
						.redondea(
								CotizadorUtil
										.getInstance()
										.getCAT(myEstado.getEdadConyuge(),
												myEstado.getGpc(),
												((myEstado.getSexo() == CotizadorUtil.FEMENINO) ? CotizadorUtil.MASCULINO
														: CotizadorUtil.FEMENINO))))
				+ "</td>" + "</tr>" + "</table>")
				: "";

		String complementariasString = myEstado.isDoccom() ? ("<div class=\"blu\">"
				+ "<h4>Sexo:</h4>" + "<p>"
				+ ((myEstado.getCatcompsexo().getValue() == CotizadorUtil.MASCULINO) ? "Masculino"
						: "Femenino")
				+ "</p>"
				+ "<h4>Edad:</h4>"
				+ "<p>"
				+ myEstado.getEdadcComp()
				+ "</p>"
				+ "</div>"
				+ "<table>"
				+ "<thead>"
				+ "<tr>"
				+ "<th>Cobertura</th>"
				+ "<th>Suma Asegurada</th>"
				+ "<th>Prima A. Total</th>"
				+ "</tr>"
				+ "</thead>"
				+ "<tbody>"
				+ "<tr>"
				+ "<td>BAC</td>"
				+ "<td>"
				+ nf.format((long) CotizadorUtil.getInstance().redondea( myEstado.getCcomp() ))
				+ "</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(
						CotizadorUtil.getInstance().getBACY(
								myEstado.getEdadcComp(), myEstado.getCcomp())))
				+ "</td>" + "</tr>" + "</tbody>" + "</table>")
				: "";

		String catcomp1String = myEstado.isDocatcomp1() ? ("<div class=\"blu\">"
				+ "<h4>Sexo:</h4>" + "<p>"
				+ ((myEstado.getCatcomp1sexo().getValue() == CotizadorUtil.MASCULINO) ? "Masculino"
						: "Femenino")
				+ "</p>"
				+ "<h4>Edad:</h4>"
				+ "<p>"
				+ myEstado.getEdadComp1()
				+ "</p>"
				+ "</div>"
				+

				"<table>"
				+ "<thead>"
				+ "<tr>"
				+ "<th>Cobertura</th>"
				+ "<th>Suma Asegurada</th>"
				+ "<th>Prima A. Total</th>"
				+ "</tr>"
				+ "</thead>"
				+ "<tbody>"
				+ "<tr>"
				+ "<td>Cáncer Plus</td>"
				+ "<td>"
				+  nf.format(CotizadorUtil.getInstance().redondea( myEstado.getCcat1() ))
				+ "</td>"
				+ "<td>"
				+ CotizadorUtil.getInstance().redondea(
						CotizadorUtil.getInstance().getCAT(
								myEstado.getEdadComp1(), myEstado.getCcat1(),
								myEstado.getCatcomp1sexo().getValue()))
				+ "</td>" + "</tr>" + "</tbody>" + "</table>")
				: "";

		String catcomp2String = myEstado.isDocatcomp2() ? ("<div class=\"blu\">"
				+ "<h4>Sexo:</h4>" + "<p>"
				+ ((myEstado.getCatcomp2sexo().getValue() == CotizadorUtil.MASCULINO) ? "Masculino"
						: "Femenino")
				+ "</p>"
				+ "<h4>Edad:</h4>"
				+ "<p>"
				+ myEstado.getEdadComp2()
				+ "</p>"
				+ "</div>"
				+

				"<table>"
				+ "<thead>"
				+ "<tr>"
				+ "<th>Cobertura</th>"
				+ "<th>Suma Asegurada</th>"
				+ "<th>Prima A. Total</th>"
				+ "</tr>"
				+ "</thead>"
				+ "<tbody>"
				+ "<tr>"
				+ "<td>Cáncer Plus</td>"
				+ "<td>"
				+  nf.format(CotizadorUtil.getInstance().redondea( myEstado.getCcat2() ))
				+ "</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(
						CotizadorUtil.getInstance().getCAT(
								myEstado.getEdadComp2(), myEstado.getCcat2(),
								myEstado.getCatcomp2sexo().getValue())))
				+ "</td>" + "</tr>" + "</tbody>" + "</table>")
				: "";

		String catcomp3String = myEstado.isDocatcomp3() ? ("<div class=\"blu\">"
				+ "<h4>Sexo:</h4>" + "<p>"
				+ ((myEstado.getCatcomp3sexo().getValue() == CotizadorUtil.MASCULINO) ? "Masculino"
						: "Femenino")
				+ "</p>"
				+ "<h4>Edad:</h4>"
				+ "<p>"
				+ myEstado.getEdadComp3()
				+ "</p>"
				+ "</div>"
				+

				"<table>"
				+ "<thead>"
				+ "<tr>"
				+ "<th>Cobertura</th>"
				+ "<th>Suma Asegurada</th>"
				+ "<th>Prima A. Total</th>"
				+ "</tr>"
				+ "</thead>"
				+ "<tbody>"
				+ "<tr>"
				+ "<td>Cáncer Plus</td>"
				+ "<td>"
				+  nf.format(CotizadorUtil.getInstance().redondea( myEstado.getCcat3() ))
				+ "</td>"
				+ "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(
						CotizadorUtil.getInstance().getCAT(
								myEstado.getEdadComp3(), myEstado.getCcat3(),
								myEstado.getCatcomp3sexo().getValue())))
				+ "</td>" + "</tr>" + "</tbody>" + "</table>")
				: "";

		String inversionString = "";

		for (int i = 1; i < Prospeccion.length; i++) {
			inversionString += "<tr>"
					+ "<td>"
					+ i
					+ "</td>"
					+ "<td>"
					+ nf.format(CotizadorUtil.getInstance().redondea(Prospeccion[i]))
					+ "</td>"
					+ "<td>"
					+ nf.format((long)(myEstado.getBas() + Prospeccion[i])) + "</td>"
					+ "</tr>";
		}

		double prima = CotizadorUtil.getInstance().redondea(
				myEstado.getPrimaTotal() );
		if( myEstado.getFormaPago().equals( "Quincenal" ) ){
			prima *= 24;
		}else if( myEstado.getFormaPago().equals( "Mensual" ) ){
			prima *= 12;
		}


		String prospeccionString = "<div class=\"blu\">"
				+ "<h4>Proyección financiera del valor en Efectivo</h4>"
				+ "</div>" +

				"<table>" + "<tr>" + "<td>Prima Excedente Anual</td>" + "<td>"
				+ nf.format(CotizadorUtil.getInstance().redondea(
						myEstado.getPrimaExcedente()))
				+ "</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td>Prima Total Anual </td>"
				+ "<td>"
				+ nf.format(prima)
				+ "</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td><strong>Prima Diaria</strong></td>"
				+ "<td><strong>"
				+ nf.format(CotizadorUtil.getInstance().redondea(prima / 360))
				+ "</strong></td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td>Forma de Pago</td>"
				+ "<td>"
				+ myEstado.getFormaPago()
				+ "</td>"
				+ "</tr>"
				+ "</table>"
				+

				"<br/>";

				String  InversionString = myEstado.getPrimaExcedente() == 0.0 ? "" : "<table>"
				+ "<thead>"
				+ "<tr>"
				+ "<th>Año</th>"
				+ "<th>Fondo de Inversión</th>"
				+ "<th>SA x Fallecimiento</th>"
				+ "</tr>"
				+ "</thead>"
				+ "<tbody>"
				+ inversionString
				+ "</tbody>" + "</table>";

		String html = "<html>" + "<head><meta charset=\"utf-8\">"
				+ "<title>Cotización</title>"
				+ "<meta name=\"description\" content=\"\">"
				+ "<meta name=\"viewport\" content=\"width=device-width\">"
				+ "<style type=\"text/css\">" +

				"body {" + "background-color: #FFF;margin: 0;"
				+ "font-family: sans-serif;font-size: 16px;"
				+ "font-size: 100%;" + "}" +

				"h4 {" + "font-size: 1em;" + "font-weight: bold;}"
				+ "div {width: 100%;}" + "div.blu {"
				+ "background-color: #2d73b5;padding: 5px 2%;" + "width: 96%;"
				+ "color: #FFF;" + "margin: 0 auto 1px;" + "}" +

				".blu h4, .blu p {" + "display: inline;"
				+ "padding: 0 4px 0 0;" + "}" +

				"table {" + "border-collapse: collapse;" + "border-spacing: 0;"
				+ "width: 100%;" + "margin: 0 auto 40px;" + "}" +

				"table th {" + "font-size: 0.7em;"
				+ "text-transform: uppercase;" + "color: #FFF;"
				+ "background-color: #2d73b5;" + "padding: 5px 2px 2px;" + "}" +

				"table td {" + "padding: 4px 2px 2px;"
				+ "border-bottom: 1px solid #CCC;" + "}" +

				"td {text-align: center;}" + "td.left {text-align: left;}"
				+ "</style>" + "</head>" +

				"<body>" +

				"<!-- DATOS PERSONALES -->" + "<div class=\"blu\">"
				+ "<h4>Titular:</h4>" + "<p>"
				+ myEstado.getFullName()
				+ "</p>"
				+

				"</div>"
				+

				"<div class=\"blu\">"
				+ "<h4>Hábito:</h4>"
				+ "<p>"
				+ ((!myEstado.is_fumador()) ? "No " : "")
				+ "Fumador</p>"
				+ "<h4>Sexo:</h4>"
				+ "<p>"
				+ (myEstado.getSexo() == CotizadorUtil.MASCULINO ? "Masculino"
						: "Femenino")
				+ "</p>"
				+ "</div>"
				+

				"<div class=\"blu\">"
				+ "<h4>Edad real:</h4>"
				+ "<p>"
				+ myEstado.getEdadReal()
				+ "</p>"
				+ "<h4>Edad Cálculo:</h4>"
				+ "<p>"
				+ myEstado.getEdadCalculo().getEdad()
				+ "</p><"
				+ "/div>"
				+

				"<div class=\"blu\">"
				+ "<h4>Ocupación:</h4>"
				+ "<p>"
				+ myEstado.getProfesion().getNombre()
				+ "</p>"
				+ "</div>"
				+

				"</br>"
				+

				"<!-- COBERTURAS TITULAR -->"
				+ "<table>"
				+

				"<thead>"
				+ "<tr>"
				+ "<th>Coberturas</th>"
				+ "<th>Suma Asegurada</th>"
				+ "<th>Prima A. Total</th>"
				+"</tr>"
				+ "</thead>"
				+

				"<tbody>"
				+

				basString
				+

				bitString
				+

				ciiString
				+

				cmaString
				+

				tibaString
				+

				catString
				+

				gfaString
				+

				geString
				+

				"<tr>"
				+ "<td>ET</td>"
				+ "<td>Otorgado</td>"
				+ "<td>Sin costo</td>" +
				"</tr>" + "</tbody>" +

				"</table>" +

				"<!-- COBERTURAS HIJOS -->" +

				hijosString +

				"<!-- COBERTURAS CONYUGE -->" +

				conyugeString +

				bacyString +

				gfcString +

				gpcString +

				"<!-- COBERTURAS COMPLEMENTARIAS -->" +

				complementariasString +

				catcomp1String +

				catcomp2String +

				catcomp3String +

				"<!-- PROYECCION -->" +

				prospeccionString +
				
				InversionString +

				"</body>" + "</html>";

		WebView wv = (WebView) findViewById(R.id.webView1);
		wv.getSettings().setBuiltInZoomControls(true);
		wv.getSettings().setSupportZoom(true);
		wv.loadData(html, "text/html; charset=UTF-8", null);
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

}
