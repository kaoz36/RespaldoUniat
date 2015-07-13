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

package com.jaguarlabs.sipac.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.jaguarlabs.sipac.R;

public class DismissDialog extends Dialog implements android.view.View.OnClickListener{

	public DismissDialog(Context context) {
		super(context);
	}

	public void dismiss(View v) {
		Animation dismissAnim = AnimationUtils.loadAnimation( getContext(), R.anim.dialog_dismiss);
		dismissAnim.reset();
		dismissAnim.setAnimationListener( new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				dismiss();
			}
		});
		v.clearAnimation();
		v.startAnimation(dismissAnim);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	
	
}
