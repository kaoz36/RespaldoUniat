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

package com.jaguarlabs.sipaccel.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DataRenderer <T>{
	
	private T data;
	protected View renderView;
	protected Context context;
	protected ViewGroup parent;
	protected Activity act;
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
	public Activity getAct() {
		return act;
	}
	public void setAct(Activity act) {
		this.act = act;
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
