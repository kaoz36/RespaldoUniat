package com.jaguarlabs.sipaccel.util;

import com.jaguarlabs.sipaccel.vo.ProfesionVO;

public interface IDialogDataRefresh {

	public void refresh( int sexo, int edad, boolean _fuma, ProfesionVO ocupacion, String fullName );
	
}
