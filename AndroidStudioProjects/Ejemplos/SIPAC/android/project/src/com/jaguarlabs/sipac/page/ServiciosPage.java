package com.jaguarlabs.sipac.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaguarlabs.sipac.model.DataModel;
import com.jaguarlabs.sipac.util.DataRenderer;
import com.jaguarlabs.sipac.vo.PolizaVO;
import com.jaguarlabs.sipac.vo.ServicioGralVO;

public class ServiciosPage extends DataRenderer<PolizaVO> {
	
	@Override
	public void build(Context pContext,LayoutInflater inflater,ViewGroup parent) {
		View screenView;
		super.build(pContext, inflater, parent);
		screenView = inflater.inflate(com.jaguarlabs.sipac.R.layout.page_poliza_servicios, parent, false);
		super.renderView = screenView;	
	}
	
	@Override
	public void attach()
	{
		super.attach();
		if(DataModel.getInstance().getPromotoria().getMostrar_agte() == 1)
		{
			renderView.findViewById(com.jaguarlabs.sipac.R.id.agenteHeader).setVisibility(View.VISIBLE);
			renderView.findViewById(com.jaguarlabs.sipac.R.id.agenteHeaderSeparator).setVisibility(View.VISIBLE);
		}else
		{
			renderView.findViewById(com.jaguarlabs.sipac.R.id.agenteHeader).setVisibility(View.GONE);
			renderView.findViewById(com.jaguarlabs.sipac.R.id.agenteHeaderSeparator).setVisibility(View.GONE);
		}
		showServiciosTable();
	}
	
	private void showServiciosTable(){
		int i = 0;
		LinearLayout cList = (LinearLayout)getView().findViewById(com.jaguarlabs.sipac.R.id.serviciosList);
		View row;
		cList.removeAllViews();	
		for( ServicioGralVO servicios:DataModel.getInstance().getServiciosGral() ){
			row = ((LayoutInflater)super.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
					inflate(com.jaguarlabs.sipac.R.layout.item_serviciosgeneral, cList,false);
			row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			if(((i++) % 2) !=0){
			    row.setBackgroundColor(context.getResources().getColor(com.jaguarlabs.sipac.R.color.even_row));
			}else{
			    row.setBackgroundColor(context.getResources().getColor(com.jaguarlabs.sipac.R.color.odd_row));
			}
			renderRow(row, servicios);
			cList.addView(row);
		}	
	}

	public void renderRow(View row, ServicioGralVO servicios) {
		if(DataModel.getInstance().getPromotoria().getMostrar_agte() == 1)
		{
			(row.findViewById(com.jaguarlabs.sipac.R.id.agenteSeparator)).setVisibility(View.VISIBLE);
			(row.findViewById(com.jaguarlabs.sipac.R.id.agente)).setVisibility(View.VISIBLE);
			((TextView)row.findViewById(com.jaguarlabs.sipac.R.id.agente)).setText(""+servicios.getAgente_serv());
		}else
		{
			(row.findViewById(com.jaguarlabs.sipac.R.id.agenteSeparator)).setVisibility(View.GONE);
			(row.findViewById(com.jaguarlabs.sipac.R.id.agente)).setVisibility(View.GONE);
		}
		
		((TextView)row.findViewById(com.jaguarlabs.sipac.R.id.noServicio)).setText(""+servicios.getId_serv());
		((TextView)row.findViewById(com.jaguarlabs.sipac.R.id.descripcion)).setText(""+servicios.getDesc_serv());
		((TextView)row.findViewById(com.jaguarlabs.sipac.R.id.fecha)).setText(""+servicios.getFecha_serv());
		((TextView)row.findViewById(com.jaguarlabs.sipac.R.id.ordenpago)).setText(""+servicios.getOrden_pago());
		((TextView)row.findViewById(com.jaguarlabs.sipac.R.id.monto)).setText(""+servicios.getMonto());
	}	

}
