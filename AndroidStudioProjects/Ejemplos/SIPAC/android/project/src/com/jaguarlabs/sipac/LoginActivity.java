package com.jaguarlabs.sipac;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.jaguarlabs.sipac.db.CommonDAO;
import com.jaguarlabs.sipac.db.SipacOpenHelper;
import com.jaguarlabs.sipac.model.BackupModel;
import com.jaguarlabs.sipac.model.DataModel;
import com.jaguarlabs.sipac.net.RPCHandler;
import com.jaguarlabs.sipac.util.DelayedExtenededInitter;
import com.jaguarlabs.sipac.util.DelayedInitter;
import com.jaguarlabs.sipac.util.ExtendedActivity;
import com.jaguarlabs.sipac.util.IJSONResponseHandler;
import com.jaguarlabs.sipac.vo.ConfiguracionVO;
import com.jaguarlabs.sipac.vo.ProfesionVO;
import com.jaguarlabs.sipac.vo.PromotoriaVO;
import com.jaguarlabs.sipac.vo.UserVO;

public class LoginActivity extends ExtendedActivity implements IJSONResponseHandler, DelayedExtenededInitter{

	private EditText userText;
	private EditText passText;
	private Button deleteButton1;

	private Button deleteButton2;
	private ProgressDialog dialog;
	
	private SharedPreferences shared;
	
	private long fechaLogueo;
	
	public static class ExtendedTextWatcher implements TextWatcher{
		
		private Button showButton;
		public ExtendedTextWatcher(Button pButton){
			showButton  = pButton;
		}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if(s.length() >0){
				showButton.setVisibility(View.VISIBLE);
			}else{
				showButton.setVisibility(View.INVISIBLE);
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
		}
	}
	
	public static class ExtendedClickListener implements View.OnClickListener{
		private EditText clearText;
		public ExtendedClickListener(EditText pText){
			clearText = pText;
		}
		@Override
		public void onClick(View v) {
			clearText.setText("");
		}
	}
	
	@Override
	protected void init() {
		super.init();
		setContentView(R.layout.activity_login);
		shared = this.getSharedPreferences("sipacUser",MODE_PRIVATE);
		userText = (EditText)findViewById(R.id.userText);
		passText = (EditText)findViewById(R.id.passwordText);
		deleteButton1 = (Button)findViewById(R.id.deletebutton1);
		deleteButton2 = (Button)findViewById(R.id.deletebutton2);
		userText.addTextChangedListener(new ExtendedTextWatcher(deleteButton1));
		passText.addTextChangedListener(new ExtendedTextWatcher(deleteButton2));
		deleteButton1.setOnClickListener(new ExtendedClickListener(userText));
		deleteButton2.setOnClickListener(new ExtendedClickListener(passText));
		
		
		Button salirButton = (Button)findViewById(R.id.salir);
		salirButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
			}
		});
		
	}
	
	public void loginClick(View targetView){  
		RPCHandler rpc;
		
		fechaLogueo = new Date().getTime();
		
		if(userText.getText().length()>0 && passText.getText().length()>0){
			rpc = new RPCHandler(this);
			rpc.execute(RPCHandler.OPERATION_LOGIN,
					    new BasicNameValuePair("username",userText.getText().toString()),
					    new BasicNameValuePair("password",passText.getText().toString()));	
		}
		else
		{
			getMessageBuilder().setMessage("Por favor introdcuzca nombre y contraseña");
			getMessageBuilder().create().show();
		}
		
	}

	@Override
	public void onResponse(JSONObject jsonResponse)throws Exception {
		Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
		JSONObject usuario;
		JSONObject promotoria;
		JSONObject configuracion;
		dialog.cancel();
		userText.setText("");
		passText.setText("");
		Boolean erase = false;
		
		if(jsonResponse.has("erase"))
		{
			erase = jsonResponse.getBoolean("erase");
		}
		
		if(jsonResponse.has("error"))
		{
			if(erase == true)
			{
				dialog = ProgressDialog.show( this , "Progreso", "Borrando Datos", true);
				(new EraseDataBaseInflateTask( this, true )).execute();
				return;
			}else
			{
				tryOffline();
			}
		}else
		{
			if(jsonResponse.has("usuario") && jsonResponse.has("promotoria") && 
					jsonResponse.has("configuracion")){
				
				usuario = jsonResponse.getJSONObject("usuario");
				promotoria = jsonResponse.getJSONObject("promotoria");
				configuracion = jsonResponse.getJSONObject("configuracion");
				DataModel.getInstance().setAppUser(new UserVO());
				DataModel.getInstance().getAppUser().setAgente( usuario.getString("Agente") );
				DataModel.getInstance().getAppUser().setNombre( usuario.getString("Nombre") );
				DataModel.getInstance().getAppUser().setSexo( usuario.getString("sexo") );
				DataModel.getInstance().getAppUser().setRfc( usuario.getString("RFC") );
				DataModel.getInstance().getAppUser().setCedula( usuario.getString("Cedula") );
				DataModel.getInstance().getAppUser().setNombreCorto( usuario.getString("nombrecorto") );
				DataModel.getInstance().getAppUser().setIngreso( usuario.getString("fe_ingreso") );
				DataModel.getInstance().getAppUser().setMeta( usuario.getString("Meta_min") );
				DataModel.getInstance().getAppUser().setStatus( usuario.getString("status") );
				DataModel.getInstance().getAppUser().setNick( usuario.getString("nick") );
				DataModel.getInstance().getAppUser().setCorreo( usuario.getString("correo") );
				DataModel.getInstance().getAppUser().setTelefono( usuario.getString("telefono") );
				DataModel.getInstance().getAppUser().setFoto( usuario.getString("foto") );
				DataModel.getInstance().getAppUser().setTelefonoFijo( usuario.getString("telefono_fijo") );
				DataModel.getInstance().getAppUser().setPassword(usuario.getString("Password"));
				
				DataModel.getInstance().setPromotoria( new PromotoriaVO() );
				DataModel.getInstance().getPromotoria().setIdPromotoria( promotoria.getString("id_promot") );
				DataModel.getInstance().getPromotoria().setPromot( promotoria.getString("promot") );
				DataModel.getInstance().getPromotoria().setRazonSocial( promotoria.getString("razon_social") );
				DataModel.getInstance().getPromotoria().setMostrar_agte( promotoria.getInt("mostrar_agte") );
				
				DataModel.getInstance().setConfiguracion( new ConfiguracionVO( configuracion.getString("expiracion"),configuracion.getString("salario") ));
				
				if(!DataModel.getInstance().getPromotoria().getIdPromotoria().equals(shared.getString("id_promot", "")))
				{
					dialog = ProgressDialog.show( this , "Progreso", "Borrando Datos", true);
					(new EraseDataBaseInflateTask( this, false )).execute();
					return;
				}
				
				guardarPreferencias();
				getFechaExpiracion();
				startActivity(intent);
				overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
			}
		}
	}

	
	
	private void tryOffline(){
		
		long expiracion = Integer.parseInt( shared.getString( "expiracion", "0" ) ) * 86400000;
		String username =  shared.getString("nick", "");
		String pwd = shared.getString("Password", "");	
		
		if( shared.getLong("fecha", 0) + expiracion < fechaLogueo  ){
			dialog = ProgressDialog.show( this , "Progreso", "Borrando Datos", true);
			(new EraseDataBaseInflateTask( this , true ) ).execute();
			return;
		}
		
		if(username.equals(userText.getText().toString()))
		{
			if(pwd.equals(md5(passText.getText().toString())))
			{
				DataModel.getInstance().setAppUser(new UserVO());
				DataModel.getInstance().setPromotoria( new PromotoriaVO() );
				
				DataModel.getInstance().getAppUser().setAgente( shared.getString("Agente","") );
				DataModel.getInstance().getAppUser().setNombre( shared.getString("Nombre","") );
				DataModel.getInstance().getAppUser().setSexo( shared.getString("sexo","") );
				DataModel.getInstance().getAppUser().setRfc( shared.getString("RFC","") );
				DataModel.getInstance().getAppUser().setCedula( shared.getString("Cedula","") );
				DataModel.getInstance().getAppUser().setNombreCorto( shared.getString("nombrecorto","") );
				DataModel.getInstance().getAppUser().setIngreso( shared.getString("fe_ingreso","") );
				DataModel.getInstance().getAppUser().setMeta( shared.getString("Meta_min","") );
				DataModel.getInstance().getAppUser().setStatus( shared.getString("status","") );
				DataModel.getInstance().getAppUser().setNick( shared.getString("nick","") );
				DataModel.getInstance().getAppUser().setCorreo( shared.getString("correo","") );
				DataModel.getInstance().getAppUser().setTelefono( shared.getString("telefono","") );
				DataModel.getInstance().getAppUser().setFoto( shared.getString("foto","") );
				DataModel.getInstance().getAppUser().setTelefonoFijo( shared.getString("telefono_fijo","") );
				DataModel.getInstance().getAppUser().setPassword(shared.getString("Password",""));
				
				
				DataModel.getInstance().getPromotoria().setIdPromotoria( shared.getString("id_promot","") );
				DataModel.getInstance().getPromotoria().setPromot( shared.getString("promot","") );
				DataModel.getInstance().getPromotoria().setRazonSocial( shared.getString("razon_social","") );
				DataModel.getInstance().getPromotoria().setMostrar_agte( shared.getInt("mostrar_agte",0) );
				
				DataModel.getInstance().setConfiguracion( new ConfiguracionVO( shared.getString("expiracion",""), shared.getString("salario","") ));
				
				startActivity(new Intent(getApplicationContext(),MenuActivity.class));
				overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
				return;
			}
			
		}
		
		getMessageBuilder().setMessage("Error al intentar ingresar a la Aplicación por favor revise su contraseña y nombre de usuario").show();
		
	}
	
	private void getFechaExpiracion(){
		int dias = Integer.parseInt( DataModel.getInstance().getConfiguracion().getExpiracion() );
		long expiracion = fechaLogueo + ( dias * 86400000 );
		java.sql.Date date = new java.sql.Date( expiracion );
		DataModel.getInstance().getConfiguracion().setExpiracion( date + "" );
	}
	
	public String md5(String s) {
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();

	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i=0; i<messageDigest.length; i++)
	            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
	        return hexString.toString();

	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
	
	private void guardarPreferencias(){
		
		SharedPreferences.Editor datos = shared.edit();		
		
		datos.putString("Agente", DataModel.getInstance().getAppUser().getAgente() );
		datos.putString("Nombre", DataModel.getInstance().getAppUser().getNombre() );
		datos.putString("Password", DataModel.getInstance().getAppUser().getPassword());
		datos.putString("sexo", DataModel.getInstance().getAppUser().getSexo() );
		datos.putString("RFC", DataModel.getInstance().getAppUser().getRfc() );
		datos.putString("Cedula", DataModel.getInstance().getAppUser().getCedula() );
		datos.putString("nombrecorto", DataModel.getInstance().getAppUser().getNombreCorto() );
		datos.putString("fe_ingreso", DataModel.getInstance().getAppUser().getIngreso() );
		datos.putString("Meta_min", DataModel.getInstance().getAppUser().getMeta() );
		datos.putString("status", DataModel.getInstance().getAppUser().getStatus() );
		datos.putString("nick", DataModel.getInstance().getAppUser().getNick() );
		datos.putString("correo", DataModel.getInstance().getAppUser().getCorreo() );
		datos.putString("telefono", DataModel.getInstance().getAppUser().getTelefono() );
		datos.putString("foto", DataModel.getInstance().getAppUser().getFoto() );
		datos.putString("telefono_fijo", DataModel.getInstance().getAppUser().getTelefonoFijo() );
		datos.putString("promot", DataModel.getInstance().getPromotoria().getPromot() );
		datos.putString("id_promot", DataModel.getInstance().getPromotoria().getIdPromotoria() );
		datos.putString("razon_social", DataModel.getInstance().getPromotoria().getRazonSocial() );
		datos.putString("expiracion", DataModel.getInstance().getConfiguracion().getExpiracion() );
		datos.putString("salario", DataModel.getInstance().getConfiguracion().getSalario());
		datos.putLong("fecha", fechaLogueo );
		datos.putInt("mostrar_agte",  DataModel.getInstance().getPromotoria().getMostrar_agte());
		datos.commit();
	}
	
	@Override
	public void onRequest() {
		dialog = ProgressDialog.show(this, "Progreso", "Ingresando a la Aplicación", true);
	}

	@Override
	public void onException(Exception e) {
		dialog.cancel();
		tryOffline();
	}

	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

	@Override
	public void delayedInit(View pView) {
		dialog.cancel();
		guardarPreferencias();
		getFechaExpiracion();
		AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
	    alert.setTitle("Borrado de Datos.").setMessage("Los datos anteriores han sido borrados. Realice una actualización para poder utilizar la aplicación con los datos de su Promtoria").setPositiveButton("Ok", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				startActivity( new Intent(getApplicationContext(),MenuActivity.class) );
				overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
			}
		});
	    alert.show();
	}
	
	@Override	
	public void delayedInitFork() {
		dialog.cancel();
		AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
	    alert.setTitle("Borrado de Datos."); 
	    alert.setMessage("Su periodo de sesión ha expirado o su usuario ya no esta activo.");
	    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				
			}
		});
	    alert.show();
	}
	@Override
	public View inProcess() {
		CommonDAO<ProfesionVO> dao  = new CommonDAO<ProfesionVO>(mContext, ProfesionVO.class, SipacOpenHelper.TABLE_PROFESIONES);
		try {
			dao.open();
			dao.clearDatabase();
		} catch (Exception e) {
			Log.e("Data Backup", "Error al Respaldar Datos");
		} finally {
			dao.close();
		}
		BackupModel.getInstance().setContextActivity(this);
		BackupModel.getInstance().clearDownloadState();
		return null;
	}
	
	class EraseDataBaseInflateTask extends AsyncTask<String, String, String>{

		private DelayedExtenededInitter initter;
		private View generatedView;
		private boolean isExpired;
		
		public EraseDataBaseInflateTask( DelayedExtenededInitter initter, boolean isExpired ){
			this.initter = initter;
			this.isExpired = isExpired;
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			try{
				generatedView = initter.inProcess();
			}catch(Exception error){
				
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(!isExpired){
				initter.delayedInit(generatedView);
			}else{
				initter.delayedInitFork();
			}
			
		}
		
	}
	
	
}
