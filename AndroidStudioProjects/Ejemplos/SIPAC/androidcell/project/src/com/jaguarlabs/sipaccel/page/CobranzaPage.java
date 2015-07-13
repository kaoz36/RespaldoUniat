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

package com.jaguarlabs.sipaccel.page;

import java.text.NumberFormat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaguarlabs.sipaccel.R;
import com.jaguarlabs.sipaccel.util.DataRenderer;
import com.jaguarlabs.sipaccel.vo.PolizaVO;

public class CobranzaPage extends DataRenderer<PolizaVO> {
	@Override
	public void build(Context pContext,LayoutInflater inflater,ViewGroup parent) {
		View screenView;
		super.build(pContext, inflater, parent);
		screenView = inflater.inflate(R.layout.page_poliza_cobranza, parent,false);
		super.renderView = screenView;
	}
	
	public void refreshData(){
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		View screenView = getView();
		((TextView)screenView.findViewById(com.jaguarlabs.sipaccel.R.id.ref_cob_banc)).setText(""+getData().getReferencia_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipaccel.R.id.prima)).setText(nf.format(defaultNumber(getData().getPrima_poliza())));
		((TextView)screenView.findViewById(com.jaguarlabs.sipaccel.R.id.prima_esperada)).setText(nf.format(defaultNumber(getData().getPrima_esp_poliza())));
		((TextView)screenView.findViewById(com.jaguarlabs.sipaccel.R.id.prima_desc)).setText(nf.format(defaultNumber(getData().getPrima_desc_poliza())));
		((TextView)screenView.findViewById(com.jaguarlabs.sipaccel.R.id.cpto)).setText(""+getData().getCpto_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipaccel.R.id.sub_ret)).setText(""+getData().getSub_ret_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipaccel.R.id.cva_cob)).setText(""+getData().getCve_cob_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipaccel.R.id.unidad_pago)).setText(""+getData().getUni_pago_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipaccel.R.id.ultimo_pago)).setText(""+getData().getUlt_pago_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipaccel.R.id.quincena_mvto)).setText(""+getData().getQuin_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipaccel.R.id.tipo_movimiento)).setText(""+getData().getTipo_mov_poliza());
	}
}
