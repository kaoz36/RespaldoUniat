package com.jaguarlabs.sipac.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.jaguarlabs.sipac.db.CommonDAO;
import com.jaguarlabs.sipac.db.SipacOpenHelper;
import com.jaguarlabs.sipac.net.RPCHandler;
import com.jaguarlabs.sipac.util.AsyncObjectResponseProcessor;
import com.jaguarlabs.sipac.util.AsyncResponseProcessor;
import com.jaguarlabs.sipac.util.ExtendedActivity;
import com.jaguarlabs.sipac.util.IAsyncJSONResponseHandler;
import com.jaguarlabs.sipac.vo.CoberturaVO;
import com.jaguarlabs.sipac.vo.EdadVO;
import com.jaguarlabs.sipac.vo.PolizaVO;
import com.jaguarlabs.sipac.vo.ProfesionVO;
import com.jaguarlabs.sipac.vo.ProspeccionVO;
import com.jaguarlabs.sipac.vo.ServAffectVO;
import com.jaguarlabs.sipac.vo.ServicioGralVO;
import com.jaguarlabs.sipac.vo.ServicioVentaVO;

public class BackupModel {

	/*--------------------------------- Support Classes ---------------------------------------*/

	/*-------------------------------------------------- Manejo de Prospecciones ------------------------------------------------*/
	final class ProspeccionProcessor extends PolizaProcessor {

		public ProspeccionProcessor(IAsyncJSONResponseHandler handler) {
			super(handler);
		}

		@Override
		protected Void doInBackground(Object... params) {
			
			CommonDAO<ProspeccionVO> servicioPDAO = new CommonDAO<ProspeccionVO>(
					mActivity.getMContext(), ProspeccionVO.class,
					SipacOpenHelper.TABLE_PROSPECCIONES);
			saveInDB(servicioPDAO);
			return null;
		}
	}

	/*---------------------------------------- Manejo de Servicios Internos ------------------------------------*/
	final class InternoProcessor extends PolizaProcessor {

		public InternoProcessor(IAsyncJSONResponseHandler handler) {
			super(handler);
		}

		@Override
		protected Void doInBackground(Object... params) {
			
			CommonDAO<ServicioVentaVO> servicioVDAO = new CommonDAO<ServicioVentaVO>(
					mActivity.getMContext(), ServicioVentaVO.class,
					SipacOpenHelper.TABLE_SERV_VENTA);
			
			saveInDB(servicioVDAO);
			return null;
		}
	}

	/*---------------------------------- Manejo de Servicios de Afectacion ----------------------------------------------------*/
	final class AfectacionProcessor extends PolizaProcessor {
		public AfectacionProcessor(IAsyncJSONResponseHandler handler) {
			super(handler);
		}

		@Override
		protected Void doInBackground(Object... params) {
			
			CommonDAO<ServAffectVO> servicioADAO = new CommonDAO<ServAffectVO>(
					mActivity.getMContext(), ServAffectVO.class,
					SipacOpenHelper.TABLE_SERV_AFECT);
			
			saveInDB(servicioADAO);
			return null;
		}
	}

	/*-------------------------------------  Manejo de Servicios ------------------------------------------------------------*/
	final class ServiciosProcessor extends PolizaProcessor {
		public ServiciosProcessor(IAsyncJSONResponseHandler handler) {
			super(handler);
		}

		@Override
		protected Void doInBackground(Object... params) {
			
			CommonDAO<ServicioGralVO> servicioDAO = new CommonDAO<ServicioGralVO>(
					mActivity.getMContext(), ServicioGralVO.class,
					SipacOpenHelper.TABLE_SERV_GRAL);
			
			saveInDB(servicioDAO);
			return null;
		}
	}

	/*--------------------------------- Manejo de  Coberturas ----------------------------------------------------------------*/
	final class CoberturasProcessor extends PolizaProcessor {
		public CoberturasProcessor(IAsyncJSONResponseHandler handler) {
			super(handler);
		}

		@Override
		protected Void doInBackground(Object... params) {
			
			CommonDAO<CoberturaVO> coberturaDAO = new CommonDAO<CoberturaVO>(
					mActivity.getMContext(), CoberturaVO.class,
					SipacOpenHelper.TABLE_COBERTURA);
			
			saveInDB(coberturaDAO);
			return null;
		}
	}

	/*---------------------------------- Manejo de Polizas --------------------------------------------------------------*/
	class PolizaProcessor extends AsyncObjectResponseProcessor {

		protected String[] fields;
		protected String rawFields;
		protected ArrayList<String> dataArray;
		protected Boolean error = false;

		public PolizaProcessor(IAsyncJSONResponseHandler handler) {
			super(handler);
			fields = new String[0];
			dataArray = new ArrayList<String>();
			rawFields = "";
		}

		public void addItems(JSONObject data) {
			String[] auxArray;
			try {
				auxArray = (data.getString("data")).split("\\|");
				if (fields.length == 0) {
					fields = (auxArray[0]).split(",");
					
					rawFields = (auxArray[0]).replace("\"", "");
				}

				if (auxArray.length > 1) {
					for (int i = 1; i < auxArray.length; i++) {
						dataArray.add(auxArray[i]);
					}
				}
			} catch (Exception error) {
				Log.e("Data Backup", "Error al Respaldar Datos");
			}

		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			
			if(!error)
			{
				handler.continueProcessing();	
			}else
			{
				getDialog().setOnCancelListener(null);
				removeDialog();
				clearDownloadState();
				mActivity.getMessageBuilder().setMessage(
				"El proceso de Actualización ha terminado inesperadamente");
					mActivity.getMessageBuilder().create().show();
			}
					
		}
		
		protected void saveInDB(CommonDAO<?> commonDAO){
			error = false;
			String itemArray;
			String sqlQuery;
			try {
				commonDAO.open();
				commonDAO.getDatabase().beginTransaction();
				while (dataArray.size() > 0) {
					itemArray = (dataArray.remove(0));
					sqlQuery = "INSERT INTO "+ commonDAO.getTableId()+" ("+rawFields.substring(0,rawFields.length()-1)+") VALUES ("+itemArray.substring(0,itemArray.length()-1)+")" ;
					commonDAO.runQueryNoReturn(sqlQuery);
				}
				commonDAO.getDatabase().setTransactionSuccessful();
				commonDAO.getDatabase().endTransaction();
			} catch (Exception e) {
				Log.e("Data Backup", "Error al Respaldar Datos");
				error = true;
			} finally {
				commonDAO.close();
				
			}
		}
		
		@Override
		protected Void doInBackground(Object... params) {
			super.doInBackground();
			CommonDAO<PolizaVO> polizasDAO = new CommonDAO<PolizaVO>(
					mActivity.getMContext(), PolizaVO.class,
					SipacOpenHelper.TABLE_POLIZA);
			
			saveInDB(polizasDAO);
			return null;
		}
	}

	/*--------------------------------  Singleton -------------------------------------------*/
	private static BackupModel instance;

	private BackupModel() throws Exception {
		if (instance != null) {
			throw new Exception("Error de Singleton");
		}
	}

	public static BackupModel getInstance() {
		if (instance == null) {
			try {
				instance = new BackupModel();
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
			}
		}
		return instance;
	}

	/*----------------------------------- Functionality ---------------------------------------------*/
	private ExtendedActivity mActivity;
	private ProgressDialog dialog;

	private PolizaProcessor dataProcessor = null;
	private RPCHandler datadownlooader = null;
	private long total;
	private String currentTable;
	private Boolean cancelledBackup;
	private DialogInterface.OnCancelListener cancelBackupListener;
	

	protected ProgressDialog getDialog() {
		if (dialog == null) {
			dialog = ProgressDialog.show(mActivity.getMContext(), "Progreso",
					"", true);
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(false);
			
		}
		return dialog;
	}

	protected void removeDialog() {
		getDialog().cancel();
		dialog = null;
	}
	
	private DialogInterface.OnCancelListener getBackupListener(){
		if (cancelBackupListener == null)
		{
			cancelBackupListener = new DialogInterface.OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					AlertDialog.Builder builder = new AlertDialog.Builder(mActivity.getMContext());
					 builder.setMessage("Desea interrumpir la actualizacion ?").setCancelable(false);
					 builder.setPositiveButton("Sí",new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {	
								cancelledBackup = true;
								cancelBackup();
								if(dataProcessor != null)
								{
									dataProcessor.cancel(true);
								}
								
								if(datadownlooader != null)
								{
									datadownlooader.cancel(true);
								}
							}
						});
				    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {	
						@Override
						public void onClick(DialogInterface dialog, int which) {
							removeDialog();
							getDialog().setOnCancelListener(getBackupListener());
							getDialog().setMessage("Reanudando Actualizacion");
						}
					});
					    
				    builder.create().show();
				    return;
				    }
			};
		}
		return cancelBackupListener;
	}
	
	public void cancelBackup(){
		getDialog().setOnCancelListener(null);
		removeDialog();
		mActivity.getMessageBuilder().setMessage(
				"El proceso de Actualización se ha suspendido, Para reanudarlo presionar el botón de actualización");
		mActivity.getMessageBuilder().create().show();
	}

	public void startBackup() {

		SharedPreferences shared = mActivity.getSharedPreferences("sipacPrefs", Context.MODE_PRIVATE);
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity.getMContext());
		
		cancelledBackup = false;
		total = shared.getLong("totalRespaldo", 0);
		currentTable = shared.getString("tablaRespaldo", "");
		
		getDialog().setTitle("Descarga de Información");
		getDialog().setMessage("Comenzando descarga....");
		getDialog().setOnCancelListener(getBackupListener());
		
		if(currentTable.length() > 0)
		{
			
			    builder.setMessage("Existe un proceso de actualización anterior .Desea continuar este proceso?").setCancelable(false);
			    builder.setPositiveButton("Continuar",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {	
						restoreBackup(currentTable);
					}
				});
			    builder.setNegativeButton("Desde el inicio", new DialogInterface.OnClickListener() {	
					@Override
					public void onClick(DialogInterface dialog, int which) {
						clearDownloadState();
						startBackup();	
					}
				});
			    
			    builder.create().show();
			    return;
			
		}
		backupEdades();
	}
	
	
	public void restoreBackup(String pTable){
		if (pTable.equals(SipacOpenHelper.TABLE_PROFESIONES)) {
			profesionesBackup();
			return;
		}
		if (pTable.equals(SipacOpenHelper.TABLE_POLIZA)) {
			backupCartera(0);
			return;
		}

		if (pTable.equals(SipacOpenHelper.TABLE_COBERTURA)) {
			backupCoberturas(0);
			return;
		}

		if (pTable.equals(SipacOpenHelper.TABLE_SERV_GRAL)) {
			backupServicios(0);
			return;
		}

		if (pTable.equals(SipacOpenHelper.TABLE_SERV_AFECT)) {
			backupServiciosAfect(0);
			return;
		}

		if (pTable.equals(SipacOpenHelper.TABLE_SERV_VENTA)) {
			backupServiciosInternos(0);
			return;
		}

		if (pTable.equals(SipacOpenHelper.TABLE_PROSPECCIONES)) {
			backupProspecciones(0);
			return;
		}
		
		backupEdades();	
	}

	public void setContextActivity(ExtendedActivity pActivity) {
		mActivity = pActivity;
	}

	/*----------------------------- Respaldo de Edades ---------------------------------------*/

	private void backupEdades() {
		getDialog().setTitle("Paso 1/8");
		currentTable = SipacOpenHelper.TABLE_EDADES;
		saveDownLoadState();
		datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {
			final class EdadesProcessor extends AsyncResponseProcessor {
				public EdadesProcessor(IAsyncJSONResponseHandler handler,
						JSONArray data) {
					super(handler, data);
				}

				@Override
				protected Void doInBackground(Object... params) {
					super.doInBackground();
					CommonDAO<EdadVO> edadesDAO = new CommonDAO<EdadVO>(
							mActivity.getMContext(), EdadVO.class,
							SipacOpenHelper.TABLE_EDADES);
					HashMap<String, Object> itemMap;
					

					try {
						edadesDAO.open();
						edadesDAO.clearTable(SipacOpenHelper.CREATE_EDADES);
						edadesDAO.getDatabase().beginTransaction();
						for (int i = 0; i < data.length(); i++) {
							itemMap = new HashMap<String, Object>();
							itemMap.put("edad",
									data.getJSONObject(i).getLong("edad"));
							itemMap.put("BAS",
									data.getJSONObject(i).getDouble("BAS"));
							itemMap.put("CII",
									data.getJSONObject(i).getDouble("CII"));
							itemMap.put("CMA",
									data.getJSONObject(i).getDouble("CMA"));
							itemMap.put("TIBA", data.getJSONObject(i)
									.getDouble("TIBA"));
							itemMap.put("BCAT", data.getJSONObject(i)
									.getDouble("BCAT"));
							itemMap.put("GFA",
									data.getJSONObject(i).getDouble("GFA"));
							itemMap.put("GE",
									data.getJSONObject(i).getDouble("GE"));
							itemMap.put("BIT",
									data.getJSONObject(i).getDouble("BIT"));
							itemMap.put("BASN", data.getJSONObject(i)
									.getDouble("BASN"));
							itemMap.put("CMAN", data.getJSONObject(i)
									.getDouble("CMAN"));
							itemMap.put("TIBAN", data.getJSONObject(i)
									.getDouble("TIBAN"));
							itemMap.put("GEN",
									data.getJSONObject(i).getDouble("GEN"));
							itemMap.put("CPCONY", data.getJSONObject(i)
									.getDouble("CPCONY"));
							itemMap.put("CIIN", data.getJSONObject(i)
									.getDouble("CIIN"));
							itemMap.put("BCATN", data.getJSONObject(i)
									.getDouble("BCATN"));
							itemMap.put("CPCONYN", data.getJSONObject(i)
									.getDouble("CPCONYN"));
							itemMap.put("BITN", data.getJSONObject(i)
									.getDouble("BITN"));
							itemMap.put("BASF", data.getJSONObject(i)
									.getDouble("BASF"));
							itemMap.put("BASFN", data.getJSONObject(i)
									.getDouble("BASFN"));
							edadesDAO.createItem(itemMap);
						}
						edadesDAO.getDatabase().setTransactionSuccessful();
						edadesDAO.getDatabase().endTransaction();
					} catch (Exception e) {
						Log.e("Data Backup", "Error al Respaldar Datos");
					} finally {
						edadesDAO.close();
					}
					return null;

				}
			}

			@Override
			public void onResponse(JSONObject jsonResponse) throws Exception {
				JSONArray result;
				try {
					if (jsonResponse.has("error")) {
						mActivity.getMessageBuilder().setMessage(
								jsonResponse.get("error").toString());
						mActivity.getMessageBuilder().create().show();
					} else {
						if (jsonResponse.has("result")) {
							result = jsonResponse.getJSONArray("result");
							(new EdadesProcessor(this, result)).execute();
						}

					}
				} catch (Exception e) {
					Log.e("Data Backup", "Error al Respaldar Datos");
				}
			}

			@Override
			public void onRequest() {getDialog().setMessage("Respaldando Edades de Cotizador");}

			@Override
			public void onException(Exception e) { cancelBackup();}

			@Override
			public void continueProcessing() {
				if(cancelledBackup) return;
				profesionesBackup();
			}
		});
		datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_EDADES);
	}
	
	

	/*----------------------------- Respaldo de Profesiones ---------------------------------------*/
	private void profesionesBackup() {
		getDialog().setTitle("Paso 2/8");
		currentTable = SipacOpenHelper.TABLE_PROFESIONES;
		saveDownLoadState();
		datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {
			final class ProfesionesProcessor extends AsyncResponseProcessor {

				public ProfesionesProcessor(IAsyncJSONResponseHandler handler,
						JSONArray data) {
					super(handler, data);
				}

				@Override
				protected Void doInBackground(Object... params) {
					super.doInBackground();
					CommonDAO<ProfesionVO> profesionesDAO = new CommonDAO<ProfesionVO>(
							mActivity.getMContext(), ProfesionVO.class,
							SipacOpenHelper.TABLE_PROFESIONES);
					HashMap<String, Object> itemMap;
					

					try {
						profesionesDAO.open();
						profesionesDAO.clearTable(SipacOpenHelper.CREATE_PROFESIONES);
						profesionesDAO.getDatabase().beginTransaction();
						for (int i = 0; i < data.length(); i++) {
							itemMap = new HashMap<String, Object>();
							itemMap.put("nombre", data.getJSONObject(i)
									.getString("nombre"));
							itemMap.put("millar", data.getJSONObject(i)
									.getDouble("millar"));
							itemMap.put("accidente", data.getJSONObject(i)
									.getDouble("accidente"));
							itemMap.put("invalidez", data.getJSONObject(i)
									.getDouble("invalidez"));
							profesionesDAO.createItem(itemMap);
						}
						profesionesDAO.getDatabase().setTransactionSuccessful();
						profesionesDAO.getDatabase().endTransaction();
					} catch (Exception e) {
						Log.e("Data Backup", "Error al Respaldar Datos");
					} finally {
						profesionesDAO.close();
					}
					return null;

				}
			}

			@Override
			public void onResponse(JSONObject jsonResponse) throws Exception {
				JSONArray result;
				try {
					if (jsonResponse.has("error")) {
						mActivity.getMessageBuilder().setMessage(
								jsonResponse.get("error").toString());
						mActivity.getMessageBuilder().create().show();
					} else {
						if (jsonResponse.has("result")) {
							result = jsonResponse.getJSONArray("result");
							(new ProfesionesProcessor(this, result)).execute();
						}

					}
				} catch (Exception e) {
					Log.e("Data Backup", "Error al Respaldar Datos");
				}
			}

			@Override
			public void continueProcessing() {
				if(cancelledBackup) return;
				backupCoberturas(0);
			}

			@Override
			public void onRequest() {getDialog().setMessage("Respaldando Profesiones de Cotización");}

			@Override
			public void onException(Exception e) { cancelBackup();}
		});
		datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_PROFESIONES);
	}

	/*---------------------------- Respaldo de Coberturas ------------------------------------------------*/
	public void backupCoberturas(long offset) {
		currentTable = SipacOpenHelper.TABLE_COBERTURA;
		getDialog().setTitle("Paso 3/8");
		saveDownLoadState();
		if (offset == 0) {
			CommonDAO<CoberturaVO> coberturaDAO = new CommonDAO<CoberturaVO>(
					mActivity.getMContext(), CoberturaVO.class,
					SipacOpenHelper.TABLE_COBERTURA);
			try{
			coberturaDAO.open();
			coberturaDAO.clearTable(SipacOpenHelper.CREATE_COBERTURA);
			}
			catch(Exception e)
			{
				Log.i("Backup Model","Database Error");
			}finally{
				coberturaDAO.close();
			}
			getDialog().setMessage("Comenzando Descarga de Coberturas");
			dataProcessor = null;
		}
	
		
		datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {
			@Override
			public void onResponse(JSONObject jsonResponse) throws Exception {

				try {
					if (jsonResponse.has("error")) {
						mActivity.getMessageBuilder().setMessage(
								jsonResponse.get("error").toString());
						mActivity.getMessageBuilder().create().show();
					} else {
						if (jsonResponse.has("total")
								&& jsonResponse.has("data")) {
							total = jsonResponse.getLong("total");
							dataProcessor = new CoberturasProcessor(this);
							dataProcessor.addItems(jsonResponse);
							dataProcessor.execute();

						}
					}
				} catch (Exception e) {
					Log.e("Data Backup", "Error al Respaldar Datos de Polizas");
				}
			}

			@Override
			public void continueProcessing() {
				if(cancelledBackup) return;
				CommonDAO<CoberturaVO> coberturaDAO = new CommonDAO<CoberturaVO>(
						mActivity.getMContext(), CoberturaVO.class,
						SipacOpenHelper.TABLE_COBERTURA);
				
				try {
					coberturaDAO.open();
					if (total <= coberturaDAO.countItems()) {
						backupServicios(0);
					} else {
						getDialog().setMessage(
								"Progreso: " + coberturaDAO.countItems()
										+ " de " + total
										+ " Coberturas Respaldadas");
						backupCoberturas(coberturaDAO.countItems());

					}
				} catch (Exception e) {
					Log.e("Data Backup", "Error al Respaldar Datos");
				} finally {
					coberturaDAO.close();
				}

			}

			@Override
			public void onRequest() {}

			@Override
			public void onException(Exception e) { cancelBackup();}
		});
		datadownlooader.execute(RPCHandler.OPERATION_BACKUP_COBERTURAS
				+ DataModel.getInstance().getPromotoria().getPromot() + "/"
				+ offset);
	}

	/*--------------------- Respaldo de Servicios Generales -----------------------------------------------------*/
	public void backupServicios(long offset) {
		currentTable = SipacOpenHelper.TABLE_SERV_GRAL;
		getDialog().setTitle("Paso 4/8");
		saveDownLoadState();
		if (offset == 0) {
			CommonDAO<ServicioGralVO> servicioDAO = new CommonDAO<ServicioGralVO>(
					mActivity.getMContext(), ServicioGralVO.class,
					SipacOpenHelper.TABLE_SERV_GRAL);
			try{
				servicioDAO.open();
				servicioDAO.clearTable(SipacOpenHelper.CREATE_SERV_GRAL);
			}
			catch(Exception e)
			{
				Log.i("Backup Model","Database Error");
			}finally{
				servicioDAO.close();
			}
			
			getDialog().setMessage("Comenzando Descarga de Servicios");
			dataProcessor = null;
		}
		
		
		datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {
			@Override
			public void onResponse(JSONObject jsonResponse) throws Exception {

				try {
					if (jsonResponse.has("error")) {
						mActivity.getMessageBuilder().setMessage(
								jsonResponse.get("error").toString());
						mActivity.getMessageBuilder().create().show();
					} else {
						if (jsonResponse.has("total")
								&& jsonResponse.has("data")) {
							total = jsonResponse.getLong("total");
							dataProcessor = new ServiciosProcessor(this);
							dataProcessor.addItems(jsonResponse);
							dataProcessor.execute();

						}
					}
				} catch (Exception e) {
					Log.e("Data Backup", "Error al Respaldar Datos de Polizas");
				}
			}

			@Override
			public void continueProcessing() {
				if(cancelledBackup) return;
				CommonDAO<ServicioGralVO> servicioDAO = new CommonDAO<ServicioGralVO>(
						mActivity.getMContext(), ServicioGralVO.class,
						SipacOpenHelper.TABLE_SERV_GRAL);
				
				try {
					servicioDAO.open();
					if (total <= servicioDAO.countItems()) {
						backupServiciosAfect(0);
					} else {
						getDialog().setMessage(
								"Progreso: " + servicioDAO.countItems()
										+ " de " + total
										+ " Servicios Respaldados");
						backupServicios(servicioDAO.countItems());

					}
				} catch (Exception e) {
					Log.e("Data Backup", "Error al Respaldar Datos");
				} finally {
					servicioDAO.close();
				}

			}

			@Override
			public void onRequest() {}

			@Override
			public void onException(Exception e) {cancelBackup();}
		});
		datadownlooader.execute(RPCHandler.OPERATION_BACKUP_SERVICIOS
				+ DataModel.getInstance().getPromotoria().getPromot() + "/"
				+ offset);
	}

	/*-------------------- Respaldo de Servicios de Afectacion ------------------------------------------------------*/
	public void backupServiciosAfect(long offset) {
		getDialog().setTitle("Paso 5/8");
		currentTable = SipacOpenHelper.TABLE_SERV_AFECT;
		saveDownLoadState();
		if (offset == 0) {
			CommonDAO<ServAffectVO> servicioADAO = new CommonDAO<ServAffectVO>(
					mActivity.getMContext(), ServAffectVO.class,
					SipacOpenHelper.TABLE_SERV_AFECT);
			try{
				servicioADAO.open();
				servicioADAO.clearTable(SipacOpenHelper.CREATE_SERV_AFECT);
			}
			catch(Exception e)
			{
				Log.i("Backup Model","Database Error");
			}finally{
				servicioADAO.close();
			}
			
			getDialog().setMessage(
					"Comenzando Descarga de Servicios de Afectacion");
			dataProcessor = null;
		}
		
		
		datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {
			@Override
			public void onResponse(JSONObject jsonResponse) throws Exception {

				try {
					if (jsonResponse.has("error")) {
						mActivity.getMessageBuilder().setMessage(
								jsonResponse.get("error").toString());
						mActivity.getMessageBuilder().create().show();
					} else {
						if (jsonResponse.has("total")
								&& jsonResponse.has("data")) {
							total = jsonResponse.getLong("total");
							dataProcessor = new AfectacionProcessor(this);
							dataProcessor.addItems(jsonResponse);
							dataProcessor.execute();

						}
					}
				} catch (Exception e) {
					Log.e("Data Backup", "Error al Respaldar Datos de Polizas");
				}
			}

			@Override
			public void continueProcessing() {
				if(cancelledBackup) return;
				CommonDAO<ServAffectVO> servicioADAO = new CommonDAO<ServAffectVO>(
						mActivity.getMContext(), ServAffectVO.class,
						SipacOpenHelper.TABLE_SERV_AFECT);
				
				try {
					servicioADAO.open();
					if (total <= servicioADAO.countItems()) {
						backupProspecciones(0);
					} else {
						getDialog().setMessage(
								"Progreso: " + servicioADAO.countItems()
										+ " de " + total
										+ " Servicios Respaldados");
						backupServiciosAfect(servicioADAO.countItems());

					}
				} catch (Exception e) {
					Log.e("Data Backup", "Error al Respaldar Datos");
				} finally {
					servicioADAO.close();
				}

			}

			@Override
			public void onRequest() {}

			@Override
			public void onException(Exception e) {cancelBackup();}
		});
		datadownlooader.execute(RPCHandler.OPERATION_BACKUP_AFECTACION
				+ DataModel.getInstance().getPromotoria().getPromot() + "/"
				+ offset);
	}

	/*-------------------------------- Respaldo de Prospeccion -----------------------------------------------*/
	public void backupProspecciones(long offset) {
		currentTable = SipacOpenHelper.TABLE_PROSPECCIONES;
		saveDownLoadState();
		getDialog().setTitle("Paso 6/8");
		if (offset == 0) {
			CommonDAO<ProspeccionVO> servicioPDAO = new CommonDAO<ProspeccionVO>(
					mActivity.getMContext(), ProspeccionVO.class,
					SipacOpenHelper.TABLE_PROSPECCIONES);
			try{
				servicioPDAO.open();
				servicioPDAO.clearTable(SipacOpenHelper.CREATE_PROSPECCIONES);
			}
			catch(Exception e)
			{
				Log.i("Backup Model","Database Error");
			}finally{
				servicioPDAO.close();
			}
			
			getDialog().setMessage("Comenzando Descarga de Datos de Prospeccion");
			dataProcessor = null;
		}
		
		
		datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {
			@Override
			public void onResponse(JSONObject jsonResponse) throws Exception {

				try {
					if (jsonResponse.has("error")) {
						mActivity.getMessageBuilder().setMessage(
								jsonResponse.get("error").toString());
						mActivity.getMessageBuilder().create().show();
					} else {
						if (jsonResponse.has("total")
								&& jsonResponse.has("data")) {
							total = jsonResponse.getLong("total");
							dataProcessor = new ProspeccionProcessor(this);
							dataProcessor.addItems(jsonResponse);
							dataProcessor.execute();

						}
					}
				} catch (Exception e) {
					Log.e("Data Backup",
							"Error al Respaldar Servicios Internos");
				}
			}

			@Override
			public void continueProcessing() {
				if(cancelledBackup) return;
				CommonDAO<ProspeccionVO> servicioPDAO = new CommonDAO<ProspeccionVO>(
						 mActivity.getMContext(), ProspeccionVO.class,
						SipacOpenHelper.TABLE_PROSPECCIONES);
				
				try {
					servicioPDAO.open();
					if (total <= servicioPDAO.countItems()) {
						backupServiciosInternos(0);
					} else {
						getDialog().setMessage(
								"Progreso: " + servicioPDAO.countItems()
										+ " de " + total + " Prospecciones");
						backupProspecciones(servicioPDAO.countItems());

					}
				} catch (Exception e) {
					Log.e("Data Backup", "Error al Respaldar Datos");
				} finally {
					servicioPDAO.close();
				}

			}
			@Override
			public void onRequest() {}
			@Override
			public void onException(Exception e) {cancelBackup();}
		});
		datadownlooader.execute(RPCHandler.OPERATION_BACKUP_PROSPECCIONES
				+ DataModel.getInstance().getPromotoria().getPromot() + "/"
				+ offset);
	}

	/*-------------------------------- Respaldo de servicios Internos -----------------------------------------------*/
	public void backupServiciosInternos(long offset) {
		currentTable = SipacOpenHelper.TABLE_SERV_VENTA;
		saveDownLoadState();
		getDialog().setTitle("Paso 7/8");
		if (offset == 0) {
			CommonDAO<ServicioVentaVO> servicioIDAO = new CommonDAO<ServicioVentaVO>(
					mActivity.getMContext(), ServicioVentaVO.class,
					SipacOpenHelper.TABLE_SERV_VENTA);
			try{
				servicioIDAO.open();
				servicioIDAO.clearTable(SipacOpenHelper.CREATE_SERV_VENTA);
			}
			catch(Exception e)
			{
				Log.i("Backup Model","Database Error");
			}finally{
				servicioIDAO.close();
			}
		
			getDialog().setMessage("Comenzando Descarga de Servicios Internos");
			dataProcessor = null;
		}
	
		
		datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {
			@Override
			public void onResponse(JSONObject jsonResponse) throws Exception {

				try {
					if (jsonResponse.has("error")) {
						mActivity.getMessageBuilder().setMessage(
								jsonResponse.get("error").toString());
						mActivity.getMessageBuilder().create().show();
					} else {
						if (jsonResponse.has("total")
								&& jsonResponse.has("data")) {
							total = jsonResponse.getLong("total");
							dataProcessor = new InternoProcessor(this);
							dataProcessor.addItems(jsonResponse);
							dataProcessor.execute();

						}
					}
				} catch (Exception e) {
					Log.e("Data Backup",
							"Error al Respaldar Servicios Internos");
				}
			}

			@Override
			public void continueProcessing() {
				if(cancelledBackup) return;
				CommonDAO<ServicioVentaVO> servicioIDAO = new CommonDAO<ServicioVentaVO>(
						mActivity.getMContext(), ServicioVentaVO.class,
						SipacOpenHelper.TABLE_SERV_VENTA);
				
				try {
					servicioIDAO.open();
					if (total <= servicioIDAO.countItems()) {
						backupCartera(0);
					} else {
						getDialog().setMessage(
								"Progreso: " + servicioIDAO.countItems()
										+ " de " + total
										+ " Servicios Respaldados");
						backupServiciosInternos(servicioIDAO.countItems());

					}
				} catch (Exception e) {
					Log.e("Data Backup", "Error al Respaldar Datos");
				} finally {
					servicioIDAO.close();
				}

			}

			@Override
			public void onRequest() {}

			@Override
			public void onException(Exception e) {cancelBackup();}
		});
		datadownlooader.execute(RPCHandler.OPERATION_BACKUP_SERVINTERNOS
				+ DataModel.getInstance().getPromotoria().getPromot() + "/"
				+ offset);
	}

	/*------------------------- Respaldo de Cartera -----------------------------------------------------*/
	public void backupCartera(long offset) {
		currentTable = SipacOpenHelper.TABLE_POLIZA;
		saveDownLoadState();
		getDialog().setTitle("Paso 8/8");

		if (offset == 0) {
			CommonDAO<PolizaVO> polizasDAO = new CommonDAO<PolizaVO>(
					mActivity.getMContext(), PolizaVO.class,
					SipacOpenHelper.TABLE_POLIZA);
			try{
				polizasDAO.open();
				polizasDAO.clearTable(SipacOpenHelper.CREATE_POLIZA);
			}
			catch(Exception e)
			{
				Log.i("Backup Model","Database Error");
			}finally{
				polizasDAO.close();
			}
			
			getDialog().setMessage("Comenzando Descarga de Polizas");
			dataProcessor = null;
		}

		
		datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {
			@Override
			public void onResponse(JSONObject jsonResponse) throws Exception {

				try {
					if (jsonResponse.has("error")) {
						mActivity.getMessageBuilder().setMessage(
								jsonResponse.get("error").toString());
						mActivity.getMessageBuilder().create().show();
					} else {
						if (jsonResponse.has("total")
								&& jsonResponse.has("data")) {
							total = jsonResponse.getLong("total");
							dataProcessor = new PolizaProcessor(this);
							dataProcessor.addItems(jsonResponse);
							dataProcessor.execute();

						}
					}
				} catch (Exception e) {
					Log.e("Data Backup", "Error al Respaldar Datos de Polizas");
				}
			}

			@Override
			public void continueProcessing() {
				if(cancelledBackup) return;
				CommonDAO<PolizaVO> polizasDAO = new CommonDAO<PolizaVO>(
						mActivity.getMContext(), PolizaVO.class,
						SipacOpenHelper.TABLE_POLIZA);
				
				try {
					polizasDAO.open();
					if (total <= polizasDAO.countItems()) {
						getDialog().setOnCancelListener(null);
						removeDialog();
						clearDownloadState();
						mActivity.getMessageBuilder().setMessage(
						"El proceso de Actualización ha terminado exitosamente");
							mActivity.getMessageBuilder().create().show();
					} else {
						getDialog().setMessage(
								"Progreso: " + polizasDAO.countItems() + " de "
										+ total + " Polizas Respaldadas");
						backupCartera(polizasDAO.countItems());

					}
				} catch (Exception e) {
					Log.e("Data Backup", "Error al Respaldar Datos");
				} finally {
					polizasDAO.close();
				}

			}

			@Override
			public void onRequest() {}

			@Override
			public void onException(Exception e) {cancelBackup();}
		});
		datadownlooader.execute(RPCHandler.OPERATION_BACKUP_POLIZAS
				+ DataModel.getInstance().getPromotoria().getPromot() + "/"
				+ offset);
	}

	private void saveDownLoadState() {
		SharedPreferences shared = mActivity.getSharedPreferences("sipacPrefs",Context.MODE_PRIVATE);
		SharedPreferences.Editor datos = shared.edit();
		datos.putString("tablaRespaldo", currentTable).putLong("totalRespaldo", total).commit();
	}
	
	public void clearDownloadState(){
		SharedPreferences shared = mActivity.getSharedPreferences("sipacPrefs",Context.MODE_PRIVATE);
		SharedPreferences.Editor datos = shared.edit();
		datos.remove("tablaRespaldo").remove("totalRespaldo").commit();
	}
}
