package com.jaguarlabs.sipac.util;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ItemAdapter<datatype> extends BaseAdapter{
	
	private int 			resourceId;
	private List<datatype> 	items;
	private Context 		parentContext;
	private IRowRenderer<datatype> renderer;
	
	public ItemAdapter(Context pContext, 
			int pResourceId,
			List<datatype> pItems, 
			IRowRenderer<datatype> rowRenderer){
		
		this.resourceId = pResourceId;
		Log.i("ItemAdapter",""+pItems.size());
		this.items = pItems;
		this.renderer = rowRenderer;
		parentContext = pContext;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public datatype getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("Item Adapter","getting view: "+position);
		View v;
        LayoutInflater vi = (LayoutInflater)parentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(resourceId, parent,false);
        renderer.renderRow(v, items.get(position));
        return v;
    }

}
