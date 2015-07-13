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

package com.jaguarlabs.sipaccel.fragment;

import com.jaguarlabs.sipaccel.util.ExtendedFragmentActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RightFragment extends Fragment {
	
	protected View mainView;
	protected IMenuFragment listener;
	protected ExtendedFragmentActivity caller;
		
	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {		
		
			makeView(inflater, container, savedInstanceState);
		
		return mainView;
	}
	
	protected void makeView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if( activity instanceof IMenuFragment ) {
	        listener = (IMenuFragment) activity;	        
	    }else{
	        throw new ClassCastException(activity.toString()
	            + " must implement IMenuFragment");
	    }		
		if( activity instanceof ExtendedFragmentActivity ){
        	caller = (ExtendedFragmentActivity) activity;
        }else{
	        throw new ClassCastException(activity.toString()
		            + " must extend ExtendedActivity");
		}
	}
}
