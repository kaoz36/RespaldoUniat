package com.jaguarlabs.sipac.page;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaguarlabs.sipac.model.DataModel;
import com.jaguarlabs.sipac.util.DataRenderer;
import com.jaguarlabs.sipac.util.IRowRenderer;
import com.jaguarlabs.sipac.vo.CoberturaVO;
import com.jaguarlabs.sipac.vo.PolizaVO;

public class GeneralPage extends DataRenderer<PolizaVO> implements IRowRenderer<CoberturaVO>{
	
	@Override
	public void build(Context pContext,LayoutInflater inflater,ViewGroup parent) {
		View screenView;
		super.build(pContext, inflater, parent);
		screenView = inflater.inflate(com.jaguarlabs.sipac.R.layout.page_poliza_general, parent,false);
		super.renderView = screenView;	
	}
	
	private long calculateEdad(String dateString)
	{
		
		long edad =0;
		try{
		SimpleDateFormat df =  new SimpleDateFormat("yyyy-MM-dd");
		Long baseNum =  df.parse(getData().getNac_poliza()).getTime();
		Long current = (new Date()).getTime();
		edad = (current-baseNum);
		edad /= 1000;
		edad /= 60;
		edad /=  60;
		edad /= 24;
		edad /= 365.242;
		}catch (Exception error)
		{
			android.util.Log.e("Error","Error al calcular Edad");
			edad = 0;
		}finally{
			
		}
		return edad;
	}
	
	public void refreshData(){
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		View screenView = getView();
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.sexoText)).setText(getData().getSexo_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.fumaText)).setText(getData().getFuma_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.nacText)).setText(getData().getNac_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.civilText)).setText(getData().getEstado_civil_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.edadText)).setText(""+calculateEdad(getData().getNac_poliza()));
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.calleText)).setText(getData().getDomicilio_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.poblacionText)).setText(getData().getPob_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.cpText)).setText(getData().getCp_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.telefonoText)).setText(getData().getTel_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.emailText)).setText(getData().getEmail_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.pPagadaText)).setText(nf.format(defaultNumber(getData().getPrima_pag_poliza())));
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.reservaText)).setText("("+((getData().getSigno_reserva().equals("N"))?"-":"+")+")"+nf.format(defaultNumber(getData().getRes_poliza())));
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.invText)).setText("("+((getData().getSigno_fondoi().equals("N"))?"-":"+")+")"+nf.format(defaultNumber(getData().getInv_poliza())));
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.pPendienteText)).setText(nf.format(defaultNumber(getData().getPrima_pen_poliza())));
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.rPendienteText)).setText(getData().getRec_pen_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.uPagoText)).setText(getData().getUlt_pago_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.pEmitidaText)).setText(nf.format(defaultNumber(getData().getPrima_emi_poliza())));
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.pQuincenalText)).setText(nf.format(defaultNumber(getData().getPrima_quin_poliza())));
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.iniVigText)).setText(getData().getFec_ini_vig_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.uModText)).setText(getData().getFec_ult_mod_poliza());
		((TextView)screenView.findViewById(com.jaguarlabs.sipac.R.id.uRetiroText)).setText(getData().getFec_ult_ret_poliza());
		showCoberturasTable();
	}

	private void showCoberturasTable() {
		LinearLayout cList = (LinearLayout)getView().findViewById(com.jaguarlabs.sipac.R.id.coberturaList);
		View row;
		int i =0;
		cList.removeAllViews();
		for(CoberturaVO cobertura:DataModel.getInstance().getCoberturas()){
			
			row = ((LayoutInflater)super.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
					inflate(com.jaguarlabs.sipac.R.layout.item_cobertura, cList,false);
			row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			if(((i++) % 2) !=0)
			{
				row.setBackgroundColor(context.getResources().getColor(com.jaguarlabs.sipac.R.color.even_row));
			}else{
				row.setBackgroundColor(context.getResources().getColor(com.jaguarlabs.sipac.R.color.odd_row));
			}
				
			renderRow(row, cobertura);
			cList.addView(row);
		}
	
		
	}

	@Override
	public void renderRow(View targetRow, CoberturaVO dataItem) {
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		((TextView)targetRow.findViewById(com.jaguarlabs.sipac.R.id.idCobertura)).setText(""+dataItem.getClave_cob());
		((TextView)targetRow.findViewById(com.jaguarlabs.sipac.R.id.prima)).setText(nf.format(defaultNumber(dataItem.getPrima_cob())));
		((TextView)targetRow.findViewById(com.jaguarlabs.sipac.R.id.primaExtra)).setText(nf.format(defaultNumber(dataItem.getPrima_cob())));
		((TextView)targetRow.findViewById(com.jaguarlabs.sipac.R.id.primaAsegurada)).setText(nf.format(defaultNumber(dataItem.getSuma_cob())));
		
	}

	
	
	
	

}
