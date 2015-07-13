package com.jaguarlabs.sipac.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.plus.model.people.Person.ObjectType;
import com.jaguarlabs.sipac.vo.ICursorDecoder;
import com.jaguarlabs.sipac.vo.ValueVO;

public class CommonDAO <objectType>{
	
	 private SQLiteDatabase database;
	 private SipacOpenHelper helper;
	 private String tableId;
	 
	 private Class<objectType> objectClass;

	 public CommonDAO(Context pContext,
			 		  Class<objectType> objectClass,
			 		 String tableId){
		 helper = SipacOpenHelper.getInstance(pContext);
		 this.objectClass = objectClass;
		 this.tableId = tableId;
	 }
	 
	 
	 public  void clearDatabase(){
		database.execSQL("DROP TABLE IF EXISTS " + SipacOpenHelper.TABLE_POLIZA);
		database.execSQL("DROP TABLE IF EXISTS " + SipacOpenHelper.TABLE_COBERTURA);
		database.execSQL("DROP TABLE IF EXISTS " + SipacOpenHelper.TABLE_SERV_GRAL);
		database.execSQL("DROP TABLE IF EXISTS " + SipacOpenHelper.TABLE_SERV_VENTA);
		database.execSQL("DROP TABLE IF EXISTS " + SipacOpenHelper.TABLE_SERV_AFECT);
		database.execSQL("DROP TABLE IF EXISTS " + SipacOpenHelper.TABLE_EDADES);
		database.execSQL("DROP TABLE IF EXISTS " + SipacOpenHelper.TABLE_PROFESIONES);
		database.execSQL("DROP TABLE IF EXISTS " + SipacOpenHelper.TABLE_PROSPECCIONES);
		
		database.execSQL(SipacOpenHelper.CREATE_POLIZA);
		database.execSQL(SipacOpenHelper.CREATE_COBERTURA);
		database.execSQL(SipacOpenHelper.CREATE_SERV_GRAL);
		database.execSQL(SipacOpenHelper.CREATE_SERV_VENTA);
		database.execSQL(SipacOpenHelper.CREATE_SERV_AFECT);
		database.execSQL(SipacOpenHelper.CREATE_EDADES);
		database.execSQL(SipacOpenHelper.CREATE_PROFESIONES);
		database.execSQL(SipacOpenHelper.CREATE_PROSPECCIONES);
	 }
	 
	 public void open() throws SQLException {
		    database = helper.getWritableDatabase();    
	 }
	 public void close() {
		   // helper.close();
	 }
	 
	 public void createItem(ContentValues values){
		 long insertId = database.insert(tableId, null,values);
		 if(insertId == -1)
		 {
			 Log.e("PolizaDAO", "Error al insertar dato");
		 }
	 }
	 
	 public long countItems(){
		 Cursor mCount= database.rawQuery("select count(*) from "+tableId, null);
		 mCount.moveToFirst();
		 long count= mCount.getLong(0);
		 mCount.close();
		 return count;
	 }
	 
	 public long countItems(String filter){
		 Cursor mCount= database.rawQuery("select count(*) from "+tableId+"  WHERE "+filter, null);
		 mCount.moveToFirst();
		 long count= mCount.getLong(0);
		 mCount.close();
		 return count;
	 }
	 
	 
	 
	 public objectType createItem(Map<String, ?> objectMap) {
		    ContentValues values = new ContentValues();
		    objectType newObject = null;
		    for(String key:objectMap.keySet())
		    {
		    	values.put(key, ""+objectMap.get(key));
		    }
		    long insertId = database.insert(tableId, null,values);
		    Cursor cursor = database.query(tableId,
		    							null,null, 
		    							null, null, null, null);
		    
		    
		    cursor.moveToFirst();
		    try{
		    	newObject = objectClass.newInstance();
		    ((ICursorDecoder)newObject).decode(cursor);
		    }catch(Exception e){
		    	Log.e("PolizaDAO", e.getMessage());
		    }
		    finally{
		    	
		    	cursor.close();
		    }
		    return newObject;
	 }
	 
	 public void deleteItem(objectType item) {
		    long id =((ICursorDecoder)item).getId();
		    Log.i("DAO","Comment deleted with id: " + id);
		    database.delete(tableId,"id  = " + id, null);
		  }
	 
	 public List<objectType> getAllItems() {
		    List<objectType> items = new ArrayList<objectType>();

		    Cursor cursor = database.query(tableId,
		    			null, null, null, null, null, null);

		    cursor.moveToFirst();
		    
		    try{
			    while (!cursor.isAfterLast()) {
			      objectType item = objectClass.newInstance();
			      ((ICursorDecoder)item).decode(cursor);
			      items.add(item);
			      cursor.moveToNext();
			    }
		    }catch(Exception error){
		    	
		    }
		    finally{
		    // Make sure to close the cursor
		    	cursor.close();
		    }
		    return items;
	}
	 
	 public List<ValueVO<objectType>> runQuery(String pQuery,int labelField){
		 List<ValueVO<objectType>> items = new ArrayList<ValueVO<objectType>>();
		 Cursor cursor = null;
		 try{
			 cursor = database.rawQuery(pQuery, null);
			 cursor.moveToFirst();
			    while (!cursor.isAfterLast()) {
			    	
			       objectType item = objectClass.newInstance();
			      ((ICursorDecoder)item).decode(cursor);
			      items.add(new ValueVO<objectType>(item,cursor.getString(labelField)));
			      cursor.moveToNext();
			    }
		    }catch(Exception error){
		    	Log.e("Error",error.toString());
		    }
		    finally{
		    // Make sure to close the cursor
		    	if(cursor != null)
		    	cursor.close();
		 }
		 return items;
	 }
	 
	 public void runQueryNoReturn(String pQuery){
		 try{
			 database.execSQL(pQuery); 
		    }catch(Exception error){
		    	Log.e("Error",error.toString());
		    }
		    finally{
		 }
	 }
	 
	 public List<objectType> runQuery(String pQuery){
		 List<objectType> items = new ArrayList<objectType>();
		 Cursor cursor = null;
		 try{
			 cursor = database.rawQuery(pQuery, null);
			 cursor.moveToFirst();
			    while (!cursor.isAfterLast()) {
			    	
			       objectType item = objectClass.newInstance();
			      ((ICursorDecoder)item).decode(cursor);
			      items.add(item);
			      cursor.moveToNext();
			    }
		    }catch(Exception error){
		    	Log.e("Error",error.toString());
		    }
		    finally{
		    // Make sure to close the cursor
		    	if(cursor != null)
		    	cursor.close();
		 }
		 return items;
	 }
	 
	 public List<objectType> getFilteredItems(String pFilter) {
		    List<objectType> items = new ArrayList<objectType>();

		    Cursor cursor = database.query(tableId,
		    			null, pFilter, null, null, null, null);

		    cursor.moveToFirst();
		    
		    try{
			    while (!cursor.isAfterLast()) {
			      objectType item = objectClass.newInstance();
			      ((ICursorDecoder)item).decode(cursor);
			      items.add(item);
			      cursor.moveToNext();
			    }
		    }catch(Exception error){
		    	
		    }
		    finally{
		    // Make sure to close the cursor
		    	cursor.close();
		    }
		    return items;
	}
	 
	 public void clearTable(String pCreateStatement){
		 try{
			database.execSQL("DROP TABLE IF EXISTS " + tableId);
			database.execSQL(pCreateStatement);
		 }catch(Exception e){
			 Log.e("CommonDAO", "error de base de datos");
		 }
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}


	public String getTableId() {
		return tableId;
	}
	 
}
