package com.jaguarlabs.sipac.page;

import java.text.NumberFormat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaguarlabs.sipac.db.SipacOpenHelper;
import com.jaguarlabs.sipac.model.DataModel;
import com.jaguarlabs.sipac.util.DataRenderer;
import com.jaguarlabs.sipac.util.IRowRenderer;
import com.jaguarlabs.sipac.vo.PolizaVO;
import com.jaguarlabs.sipac.vo.ServAffectVO;
import com.jaguarlabs.sipac.vo.ServicioVentaVO;

public class VentasPage extends DataRenderer<PolizaVO> implements IRowRenderer<ServicioVentaVO>{
	@Override
	public void build(Context pContext,LayoutInflater inflater,ViewGroup parent) {
		View screenView;
		super.build(pContext, inflater, parent);
		screenView = inflater.inflate(com.jaguarlabs.sipac.R.layout.page_poliza_ventas, parent,false);
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
		showServiciosVentasTable();
	}
	
	
	private void showServiciosTable(){
		int i = 0;		
		LinearLayout cList = (LinearLayout)getView().findViewById(com.jaguarlabs.sipac.R.id.afectacionesList);
		View row;
		cList.removeAllViews();	for( ServAffectVO servicios:DataModel.getInstance().getServiciosAfect() ){
			row = ((LayoutInflater)super.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
					inflate(com.jaguarlabs.sipac.R.layout.item_serviciosafec, cList,false);
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

	public void renderRow(View row, ServAffectVO servicios) {
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		((TextView)row.findViewById(com.jaguarlabs.sipac.R.id.agente)).setText(""+servicios.getId_serv());
		((TextView)row.findViewById(com.jaguarlabs.sipac.R.id.positivonegativo)).setText(""+servicios.getMas_men_serv());
		((TextView)row.findViewById(com.jaguarlabs.sipac.R.id.prima)).setText(nf.format(defaultNumber(servicios.getPrima_serv())));
		((TextView)row.findViewById(com.jaguarlabs.sipac.R.id.fechaEmicion)).setText(""+servicios.getFec_emi_serv());
		((TextView)row.findViewById(com.jaguarlabs.sipac.R.id.plan)).setText(""+servicios.getPlan_serv());
	}
	
	private void showServiciosVentasTable(){
		int i = 0;
		LinearLayout cList = (LinearLayout)getView().findViewById(com.jaguarlabs.sipac.R.id.ventasList);
		View row;
		cList.removeAllViews();
		
		for( ServicioVentaVO servicios:DataModel.getInstance().getServiciosVentas() ){
			row = ((LayoutInflater)super.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
					inflate(com.jaguarlabs.sipac.R.layout.item_serviciosventas, cList,false);
			row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			if(((i++) % 2) != 0){
			    row.setBackgroundColor(context.getResources().getColor(com.jaguarlabs.sipac.R.color.even_row));
			}else{
			    row.setBackgroundColor(context.getResources().getColor(com.jaguarlabs.sipac.R.color.odd_row));
			}
			renderRow(row, servicios);
			cList.addView(row);
		}	
	}

	public void renderRow(View row, ServicioVentaVO servicios) {
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		if(DataModel.getInstance().getPromotoria().getMostrar_agte() == 1)
		{
			(row.findViewById(com.jaguarlabs.sipac.R.id.agenteSeparator)).setVisibility(View.VISIBLE);
			(row.findViewById(com.jaguarlabs.sipac.R.id.agente)).setVisibility(View.VISIBLE);
			((TextView)row.findViewById(com.jaguarlabs.sipac.R.id.agente)).setText(""+servicios.getId_agente_venta());
		}else
		{
			(row.findViewById(com.jaguarlabs.sipac.R.id.agenteSeparator)).setVisibility(View.GONE);
			(row.findViewById(com.jaguarlabs.sipac.R.id.agente)).setVisibility(View.GONE);
		}
		
		((TextView)row.findViewById(com.jaguarlabs.sipac.R.id.fechaEnvio)).setText(""+servicios.getFec_env_venta());
		((TextView)row.findViewById(com.jaguarlabs.sipac.R.id.fechaEmicion)).setText(""+servicios.getFec_emi_venta());
		((TextView)row.findViewById(com.jaguarlabs.sipac.R.id.fechaEntrada)).setText(""+servicios.getFec_ent_venta());
		((TextView)row.findViewById(com.jaguarlabs.sipac.R.id.fechaAceptacion)).setText(""+servicios.getFec_acc_venta());
		((TextView)row.findViewById(com.jaguarlabs.sipac.R.id.primaInicial)).setText(nf.format(defaultNumber(servicios.getPrima_ini_venta())));
		((TextView)row.findViewById(com.jaguarlabs.sipac.R.id.primaEmitida)).setText(nf.format(defaultNumber(servicios.getPrima_emi_venta())));
		((TextView)row.findViewById(com.jaguarlabs.sipac.R.id.primaTotal)).setText(nf.format(defaultNumber(servicios.getPrima_tot_venta())));
		((TextView)row.findViewById(com.jaguarlabs.sipac.R.id.noServicio)).setText(""+servicios.getServ_venta());
		((TextView)row.findViewById(com.jaguarlabs.sipac.R.id.tipoNegocio)).setText(""+servicios.getTipo_negocio());

	}
	
	
}
