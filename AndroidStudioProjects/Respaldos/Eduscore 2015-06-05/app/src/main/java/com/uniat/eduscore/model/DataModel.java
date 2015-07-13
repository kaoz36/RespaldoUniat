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

package com.uniat.eduscore.model;

import android.app.Activity;
import android.util.Log;

import com.uniat.eduscore.vo.UserVO;

public class DataModel {

	public static DataModel instance;

	private UserVO appUser;
	private Activity previousActivity;
	
	public Activity getPreviousActivity() {
		return previousActivity;
	}

	public void setPreviousActivity(Activity previousActivity) {
		this.previousActivity = previousActivity;
	}

	private DataModel() throws Exception {
		if(instance != null){
			throw new Exception("Error de Singleton");
		}			
	}
	
	public static DataModel getInstance(){
		if( instance == null ){
			try{
				instance = new DataModel();
			}catch(Exception e){
				Log.e("Error", e.getMessage());
			}
		}
		return instance;
	}


	public UserVO getAppUser() {
		return appUser;
	}

	public void setAppUser(UserVO appUser) {
		this.appUser = appUser;
	}
	

}
