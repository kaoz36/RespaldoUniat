package com.jaguarlabs.sipac.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DataRenderer <T>{
	
	private T data;
	protected View renderView;
	protected Context context;
	protected ViewGroup parent;
	public T getData(){
		return data;
	}
	public void setData(T pValue){
		this.data = pValue;	
	}
	
	public void build(Context pContext,LayoutInflater inflater,ViewGroup parent ){
		this.parent =parent;
		this.context = pContext;
	}
	
	public void attach()
	{
		parent.addView(getView());
		refreshData();
	}
	
	public View getView(){
		return renderView;
	}
	
	public void refreshData(){
		
	}
	
	protected Double defaultNumber(String sParam){
		Double d = 0.0;
		try{
			d = Double.valueOf(sParam);
		}catch(Exception error)
		{
			d =0.0;
		}finally{
			
		}
		return d;
	}
}
