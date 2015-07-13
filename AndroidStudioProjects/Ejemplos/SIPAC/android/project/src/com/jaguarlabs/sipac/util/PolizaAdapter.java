package com.jaguarlabs.sipac.util;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jaguarlabs.sipac.R;
import com.jaguarlabs.sipac.vo.PolizaVO;

public class PolizaAdapter extends ArrayAdapter<PolizaVO> {
	private int resourceId;
	private List<PolizaVO> items;
	private Context parentContext;
	
	public PolizaAdapter(Context pContext, int pResourceId,List<PolizaVO> pItems){
		super(pContext, pResourceId, pItems);
		this.resourceId = pResourceId;
		this.items = pItems;
		parentContext = pContext;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
        TextView tView;
        LayoutInflater vi = (LayoutInflater)parentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(resourceId, parent,false);
        
        PolizaVO item = items.get(position);
        int[] resources = {R.id.idPoliza,R.id.nombre,R.id.rfc,R.id.uniPago,R.id.positivonegativo,R.id.quincena,R.id.retenedor,R.id.positivonegativo};
        for(int currentResource=0;currentResource < resources.length; currentResource++)
        {
        	tView = (TextView) v.findViewById(resources[currentResource]);
        	if(tView != null)
        	{
        		tView.setText(item.getData(resources[currentResource]));
        	}
        }
        return v;
    }
}
