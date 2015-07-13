package com.jaguarlabs.sipaccel.util;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;

import com.jaguarlabs.sipaccel.db.CommonDAO;
import com.jaguarlabs.sipaccel.db.SipacOpenHelper;
import com.jaguarlabs.sipaccel.net.RPCHandler;
import com.jaguarlabs.sipaccel.vo.EdadVO;
import com.jaguarlabs.sipaccel.vo.ProfesionVO;

public class Actualizar {

//	public void backUpData(View pTarget) {
//		(new RPCHandler(new IAsyncJSONResponseHandler() {
//
//			final class EdadesProcessor extends AsyncResponseProcessor {
//
//				public EdadesProcessor(IAsyncJSONResponseHandler handler,
//						JSONArray data) {
//					super(handler, data);
//				}
//
//				@Override
//				protected Void doInBackground(Object... params) {
//					super.doInBackground();
//					CommonDAO<EdadVO> edadesDAO = new CommonDAO<EdadVO>(
//							mContext, EdadVO.class,
//							SipacOpenHelper.TABLE_EDADES);
//					HashMap<String, Object> itemMap;
//					edadesDAO.open();
//
//					try {
//						edadesDAO.clearTable(SipacOpenHelper.CREATE_EDADES);
//						for (int i = 0; i < data.length(); i++) {
//							itemMap = new HashMap<String, Object>();
//							itemMap.put("edad", data.getJSONObject(i).getLong("edad"));
//							itemMap.put("BAS", data.getJSONObject(i).getDouble("BAS"));
//							itemMap.put("CII", data.getJSONObject(i).getDouble("CII"));
//							itemMap.put("CMA", data.getJSONObject(i).getDouble("CMA"));
//							itemMap.put("TIBA", data.getJSONObject(i).getDouble("TIBA"));
//							itemMap.put("BCAT", data.getJSONObject(i).getDouble("BCAT"));
//							itemMap.put("GFA", data.getJSONObject(i).getDouble("GFA"));
//							itemMap.put("GE", data.getJSONObject(i).getDouble("GE"));
//							itemMap.put("BIT", data.getJSONObject(i).getDouble("BIT"));
//							itemMap.put("BASN", data.getJSONObject(i).getDouble("BASN"));
//							itemMap.put("CMAN", data.getJSONObject(i).getDouble("CMAN"));
//							itemMap.put("TIBAN", data.getJSONObject(i).getDouble("TIBAN"));
//							itemMap.put("GEN", data.getJSONObject(i).getDouble("GEN"));
//							itemMap.put("CPCONY", data.getJSONObject(i).getDouble("CPCONY"));
//							itemMap.put("CIIN", data.getJSONObject(i).getDouble("CIIN"));
//							itemMap.put("BCATN", data.getJSONObject(i).getDouble("BCATN"));
//							itemMap.put("CPCONYN", data.getJSONObject(i).getDouble("CPCONYN"));
//							edadesDAO.createItem(itemMap);
//						}
//					} catch (Exception e) {
//						Log.e("Data Backup", "Error al Respaldar Datos");
//					} finally {
//						edadesDAO.close();
//					}
//					return null;
//
//				}
//			}
//
//			@Override
//			public void onResponse(JSONObject jsonResponse) throws Exception {
//				JSONArray result;
//				try {
//					if (jsonResponse.has("error")) {
//						getMessageBuilder().setMessage(
//								jsonResponse.get("error").toString());
//						getMessageBuilder().create().show();
//					} else {
//						if (jsonResponse.has("result")) {
//							result = jsonResponse.getJSONArray("result");
//							(new EdadesProcessor(this, result)).execute();
//						}
//
//					}
//				} catch (Exception e) {
//					Log.e("Data Backup", "Error al Respaldar Datos");
//				}
//			}
//
//			@Override
//			public void onRequest() {
//				dialog = ProgressDialog.show(mContext, "Progreso",
//						"Respaldando Edades de Cotizador", true);
//			}
//
//			@Override
//			public void onException(Exception e) {
//				dialog.cancel();
//				getMessageBuilder().setMessage(
//						"Error de conexion por favor intente de nuevo");
//				getMessageBuilder().create().show();
//			}
//
//			@Override
//			public void continueProcessing() {
//				profesionesBackup();
//			}
//		})).execute(RPCHandler.OPERATION_GET_ALL_EDADES);
//	}
//
////	private void prospeccionBackup() {
////		(new RPCHandler(new IAsyncJSONResponseHandler() {
////
////			final class ProspeccionProcessor extends AsyncResponseProcessor {
////
////				public ProspeccionProcessor(IAsyncJSONResponseHandler handler,
////						JSONArray data) {
////					super(handler, data);
////				}
////
////				@Override
////				protected Void doInBackground(Object... params) {
////					super.doInBackground();
////					CommonDAO<ProspeccionVO> prospeccionesDAO = new CommonDAO<ProspeccionVO>(
////							mContext, ProspeccionVO.class,
////							SipacOpenHelper.TABLE_PROSPECCIONES);
////					HashMap<String, Object> itemMap;
////					prospeccionesDAO.open();
////
////					try {
////						prospeccionesDAO
////								.clearTable(SipacOpenHelper.CREATE_PROSPECCIONES);
////						for (int i = 0; i < data.length(); i++) {
////							itemMap = new HashMap<String, Object>();
////							itemMap.put("poliza", data.getJSONObject(i)
////									.getString("poliza"));
////							itemMap.put("plan", data.getJSONObject(i)
////									.getString("plan"));
////							itemMap.put("nombre", data.getJSONObject(i)
////									.getString("nombre"));
////							itemMap.put("edad",
////									data.getJSONObject(i).getInt("edad"));
////							itemMap.put("prima", data.getJSONObject(i)
////									.getDouble("prima"));
////							itemMap.put("bas",
////									data.getJSONObject(i).getDouble("bas"));
////							itemMap.put("cma",
////									data.getJSONObject(i).getInt("cma"));
////							itemMap.put("tiba",
////									data.getJSONObject(i).getInt("tiba"));
////							itemMap.put("cii",
////									data.getJSONObject(i).getInt("cii"));
////							itemMap.put("bac",
////									data.getJSONObject(i).getInt("bac"));
////							itemMap.put("cat",
////									data.getJSONObject(i).getInt("cat"));
////							itemMap.put("bacy",
////									data.getJSONObject(i).getInt("bacy"));
////							itemMap.put("gfa",
////									data.getJSONObject(i).getInt("gfa"));
////							itemMap.put("bit",
////									data.getJSONObject(i).getInt("bit"));
////							itemMap.put("exc",
////									data.getJSONObject(i).getDouble("exc"));
////							itemMap.put("gfc",
////									data.getJSONObject(i).getInt("gfc"));
////							itemMap.put("gfh",
////									data.getJSONObject(i).getInt("gfh"));
////							itemMap.put("ge", data.getJSONObject(i)
////									.getInt("ge"));
////							itemMap.put("catp",
////									data.getJSONObject(i).getInt("catp"));
////							itemMap.put("zona", data.getJSONObject(i)
////									.getString("zona"));
////							itemMap.put("f_emi", data.getJSONObject(i)
////									.getString("f_emi"));
////							itemMap.put("f_u_vta", data.getJSONObject(i)
////									.getString("f_u_vta"));
////							itemMap.put("f_2216", data.getJSONObject(i)
////									.getString("f_2216"));
////							itemMap.put("res_disp", data.getJSONObject(i)
////									.getDouble("res_disp"));
////							itemMap.put("inversion", data.getJSONObject(i)
////									.getDouble("inversion"));
////							itemMap.put("municipio", data.getJSONObject(i)
////									.getString("municipio"));
////							itemMap.put("fuma",
////									data.getJSONObject(i).getInt("fuma"));
////							itemMap.put("sexo",
////									data.getJSONObject(i).getInt("sexo"));
////							itemMap.put("estado_civil", data.getJSONObject(i)
////									.getInt("estado_civil"));
////							prospeccionesDAO.createItem(itemMap);
////						}
////					} catch (Exception e) {
////						Log.e("Prospeccion Backup", "Error al Respaldar prospeccion");
////					} finally {
////						prospeccionesDAO.close();
////					}
////					return null;
////
////				}
////			}
////
////			@Override
////			public void onResponse(JSONObject jsonResponse) throws Exception {
////				JSONArray result;
////				try {
////					if (jsonResponse.has("error")) {
////						getMessageBuilder().setMessage(
////								jsonResponse.get("error").toString());
////						getMessageBuilder().create().show();
////					} else {
////						if (jsonResponse.has("result")) {
////							result = jsonResponse.getJSONArray("result");
////							(new ProspeccionProcessor(this, result)).execute();
////						}
////
////					}
////				} catch (Exception e) {
////					Log.e("Data Backup", "Error al Respaldar Datos");
////				}
////			}
////
////			@Override
////			public void onRequest() {
////				dialog.setMessage("Respaldando Datos de Prospeccion");
////			}
////
////			@Override
////			public void onException(Exception e) {
////				dialog.cancel();
////				getMessageBuilder().setMessage(
////						"Error de conexion por favor intente de nuevo");
////				getMessageBuilder().create().show();
////			}
////
////			@Override
////			public void continueProcessing() {
////				profesionesBackup();
////			}
////		})).execute(RPCHandler.OPERATION_GET_ALL_PROSPECCIONES);
////
////	}
//
//	private void profesionesBackup() {
//		(new RPCHandler(new IAsyncJSONResponseHandler() {
//
//			final class ProfesionesProcessor extends AsyncResponseProcessor {
//
//				public ProfesionesProcessor(IAsyncJSONResponseHandler handler,
//						JSONArray data) {
//					super(handler, data);
//				}
//
//				@Override
//				protected Void doInBackground(Object... params) {
//					super.doInBackground();
//					CommonDAO<ProfesionVO> profesionesDAO = new CommonDAO<ProfesionVO>(
//							mContext, ProfesionVO.class,
//							SipacOpenHelper.TABLE_PROFESIONES);
//					HashMap<String, Object> itemMap;
//					profesionesDAO.open();
//
//					try {
//						profesionesDAO
//								.clearTable(SipacOpenHelper.CREATE_PROFESIONES);
//						for (int i = 0; i < data.length(); i++) {
//							itemMap = new HashMap<String, Object>();
//							itemMap.put("nombre", data.getJSONObject(i)
//									.getString("nombre"));
//							itemMap.put("millar", data.getJSONObject(i)
//									.getDouble("millar"));
//							itemMap.put("accidente", data.getJSONObject(i)
//									.getDouble("accidente"));
//							itemMap.put("invalidez", data.getJSONObject(i)
//									.getDouble("invalidez"));
//							profesionesDAO.createItem(itemMap);
//						}
//					} catch (Exception e) {
//						Log.e("Data profesiones", "Error al Respaldar Profesiones");
//					} finally {
//						profesionesDAO.close();
//					}
//					return null;
//
//				}
//			}
//
//			@Override
//			public void onResponse(JSONObject jsonResponse) throws Exception {
//				JSONArray result;
//				try {
//					if (jsonResponse.has("error")) {
//						getMessageBuilder().setMessage(
//								jsonResponse.get("error").toString());
//						getMessageBuilder().create().show();
//					} else {
//						if (jsonResponse.has("result")) {
//							result = jsonResponse.getJSONArray("result");
//							(new ProfesionesProcessor(this, result)).execute();
//						}
//
//					}
//				} catch (Exception e) {
//					Log.e("Data profesiones", "Error al Respaldar Profesiones");
//				}
//			}
//
//			@Override
//			public void continueProcessing() {
//				dialog.cancel();
//				getMessageBuilder().setMessage(
//						"El Respaldo de Datos \n ha sido exitoso");
//				getMessageBuilder().create().show();
//			}
//
//			@Override
//			public void onRequest() {
//				dialog.setMessage("Respaldando Profesiones de Cotización");
//			}
//
//			@Override
//			public void onException(Exception e) {
//				dialog.cancel();
//				getMessageBuilder().setMessage(
//						"Error de conexion por favor intente de nuevo");
//				getMessageBuilder().create().show();
//			}
//		})).execute(RPCHandler.OPERATION_GET_ALL_PROFESIONES);
//
//	}
	
}
