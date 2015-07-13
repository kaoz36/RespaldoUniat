package com.jaguarlabs.sipac.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jaguarlabs.sipac.MainApplicationActivity;
import com.jaguarlabs.sipac.model.DataModel;
import com.jaguarlabs.sipac.vo.UserVO;

public class ProfileFragment extends RightFragment {

	@Override
	protected void makeView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ImageButton back;
		MainApplicationActivity.MainApplicationOnTouchListener interaction = new MainApplicationActivity.MainApplicationOnTouchListener(listener);
		mainView = inflater.inflate(com.jaguarlabs.sipac.R.layout.fragment_profile, container,false);
		back = (ImageButton)mainView.findViewById(com.jaguarlabs.sipac.R.id.back);
		back.setOnTouchListener(interaction);
		back.setOnClickListener(interaction);
		llenarDatos();
	}
	
	private void llenarDatos() {
		UserVO userVO = DataModel.getInstance().getAppUser();
		if (userVO != null) {
			TextView tvExpDate = (TextView)mainView.findViewById(com.jaguarlabs.sipac.R.id.expiration_date);
			tvExpDate.setText( DataModel.getInstance().getConfiguracion().getExpiracion() );
			
			TextView tvId = (TextView)mainView.findViewById(com.jaguarlabs.sipac.R.id.identifier);
			tvId.setText(userVO.getAgente());
			
			TextView tvNombre = (TextView)mainView.findViewById(com.jaguarlabs.sipac.R.id.name);
			tvNombre.setText(userVO.getNombre());
			
			TextView tvRfc = (TextView)mainView.findViewById(com.jaguarlabs.sipac.R.id.rfc);
			tvRfc.setText(userVO.getRfc());
			
			TextView tvBirthDate = (TextView)mainView.findViewById(com.jaguarlabs.sipac.R.id.birth_date);
			tvBirthDate.setText(userVO.getCorreo());
		} 
	}

}
