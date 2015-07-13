package com.jaguarlabs.sipac.page;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jaguarlabs.sipac.util.DataRenderer;

public class ScreenAdapter <dataType> extends BaseAdapter {
	private LayoutInflater inflater;
	private List<? extends Map<String, ?>> items;
	private dataType dataItem;
	private Context parentContext;
		
	public ScreenAdapter(Context context, 
						 List<? extends Map<String, ?>> pItems,
						 dataType pDataItem) {
		inflater = LayoutInflater.from(context);
		items = pItems;
		dataItem = pDataItem;
		parentContext = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
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
		View layout = convertView;
		Map<String, Object> renderDescriptor = (Map<String, Object>)items.get(position); 
		DataRenderer<dataType> renderer;
		
		try{
			if(renderDescriptor.get("renderer")!= null )
			{
				if(renderDescriptor.get("renderInstance") == null)
				{
					Log.i("ScreenAdapter","create renderer");
					renderer = (DataRenderer<dataType>)((Class<?>)renderDescriptor.get("renderer")).newInstance();
					renderer.setData(dataItem);
					renderDescriptor.put("renderInstance", renderer);
					renderer.build(parentContext,inflater,parent); 
					
				}else
				{
					Log.i("ScreenAdapter","obtain renderer");
					renderer = (DataRenderer<dataType>)renderDescriptor.get("renderInstance");
					renderer.setData(dataItem);
					renderer.refreshData();
					layout = renderer.getView();
				}
			}
		}catch(Exception error)
		{
			Log.e("ItemRenderer", "Error de Instancia");
		}
		return layout;
	}
	
	

}
