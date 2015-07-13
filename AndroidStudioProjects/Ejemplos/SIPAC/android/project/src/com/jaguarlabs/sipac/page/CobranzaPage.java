package com.jaguarlabs.sipac.page;

import java.text.NumberFormat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaguarlabs.sipac.util.DataRenderer;
import com.jaguarlabs.sipac.vo.PolizaVO;

public class CobranzaPage extends DataRenderer<PolizaVO> {
	@Override
	public void build(Context pContext,LayoutInflater inflater,ViewGroup parent) {
		View screenView;
		super.build(pContext, inflater, parent);
		screenView = inflater.inflate(com.jaguarlabs.sipac.R.layout.page_poliza_cobranza, parent,false);
		super.renderView = screenView;
	}
	
	public void refreshData(){
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		View screenView = getView();
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.ref_cob_banc)).setText(""+getData().getReferencia_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.prima)).setText(nf.format(defaultNumber(getData().getPrima_poliza())));
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.prima_esperada)).setText(nf.format(defaultNumber(getData().getPrima_esp_poliza())));
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.prima_desc)).setText(nf.format(defaultNumber(getData().getPrima_desc_poliza())));
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.cpto)).setText(""+getData().getCpto_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.sub_ret)).setText(""+getData().getSub_ret_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.cva_cob)).setText(""+getData().getCve_cob_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.unidad_pago)).setText(""+getData().getUni_pago_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.ultimo_pago)).setText(""+getData().getUlt_pago_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.quincena_mvto)).setText(""+getData().getQuin_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.tipo_movimiento)).setText(""+getData().getTipo_mov_poliza());
	}
	
}
