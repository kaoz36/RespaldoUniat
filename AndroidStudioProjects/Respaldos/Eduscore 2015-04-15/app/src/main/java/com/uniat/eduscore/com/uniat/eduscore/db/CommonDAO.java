package com.uniat.eduscore.com.uniat.eduscore.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.uniat.eduscore.com.uniat.eduscore.util.ICursorDecoder;
import com.uniat.eduscore.com.uniat.eduscore.vo.ValueVO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 13/04/2015.
 */
public class CommonDAO <objectType> {

    private SQLiteDatabase database;
    private EduscoreOpenHelper helper;
    private String tableID;

    private Class<objectType> objectTypeClass;

    public CommonDAO(Context context, Class<objectType> objectTypeClass, String tableID){
        this.helper = EduscoreOpenHelper.getInstance(context);
        this.objectTypeClass = objectTypeClass;
        this.tableID = tableID;
    }

    public void clearDatabase(){
        database.execSQL("DROP TABLE IF EXISTS " + EduscoreOpenHelper.TABLE_SIS_USERS );

        database.execSQL(EduscoreOpenHelper.CREATE_SIS_USERS);
    }

    public void open() throws SQLException{
        database = helper.getWritableDatabase();
    }

    public void createItem( ContentValues values ){
        long insertId = database.insert(tableID, null, values);
        if( insertId == -1 ){
            Log.i(tableID, "Error al insertar el dato");
        }
    }

    public long countItems(){
        Cursor mCount = database.rawQuery("SELECT COUNT(*) FROM " + tableID, null );
        mCount.moveToFirst();
        long count = mCount.getLong(0);
        mCount.close();
        return count;
    }

    public long countItems( String filter ){
        Cursor mCount = database.rawQuery("SELECT COUNT(*) FROM " + tableID + " WHERE " + filter, null );
        mCount.moveToFirst();
        long count = mCount.getLong(0);
        mCount.close();
        return count;
    }

    public objectType createItem(Map<String, ?> objetcMap){
        ContentValues values = new ContentValues();
        objectType newObjetc = null;
        for( String key : objetcMap.keySet()){
            values.put(key, objetcMap.get(key).toString() );
        }
        long insertId = database.insert(tableID, null, values);
        Cursor cursor = database.query(tableID, null, null, null, null, null, null);
        cursor.moveToFirst();
        try{
            newObjetc = objectTypeClass.newInstance();
            ((ICursorDecoder) newObjetc).decode(cursor);
        }catch (Exception e){
            Log.i(tableID, "Error: " + e.getMessage());
        }finally {
            if( cursor != null ){
                cursor.close();
            }
        }
        return newObjetc;
    }

    public void deleteItem( objectType item ){
        long id = ((ICursorDecoder)item).getId();
        Log.i("DAO", "Comment deleted whit id: " + id);
        database.delete(tableID, "id = " + id, null);
    }

    public List<objectType> getAllItems(){
        List<objectType> items = new ArrayList<>();
        Cursor cursor = database.query(tableID, null, null, null, null, null, null);
        cursor.moveToFirst();
        try{
            while( !cursor.isAfterLast() ){
                objectType item = objectTypeClass.newInstance();
                ((ICursorDecoder)item).decode(cursor);
                items.add(item);
                cursor.moveToNext();
            }
        }catch (Exception e){

        }finally {
            if( cursor != null ){
                cursor.close();
            }
        }
        return items;
    }

    public List<ValueVO<objectType>> runQuery(String pQuery, int labelField){
        List<ValueVO<objectType>> items = new ArrayList<>();
        Cursor cursor = null;
        try{
            cursor = database.rawQuery(pQuery, null);
            cursor.moveToFirst();
            while( !cursor.isAfterLast()){
                objectType item = objectTypeClass.newInstance();
                ((ICursorDecoder) item).decode(cursor);
                items.add(new ValueVO<objectType>(item, cursor.getString(labelField)));
                cursor.moveToNext();
            }
        }catch (Exception e){
            Log.e("Error: " + pQuery, e.getMessage());
        }finally {
            if( cursor != null ){
                cursor.close();
            }
        }
        return items;
    }

    public void runQueryNoReturn( String pQuery ){
        try{
            database.execSQL(pQuery);
        }catch (Exception e){
            Log.e(tableID, pQuery );
        }
    }

    public List<objectType> runQuery(String pQuery){
        List<objectType> items = new ArrayList<>();
        Cursor cursor = null;
        try{
            cursor = database.rawQuery(pQuery, null);
            cursor.moveToFirst();
            while( !cursor.isAfterLast()){
                objectType item = objectTypeClass.newInstance();
                ((ICursorDecoder) item).decode(cursor);
                items.add(item);
                cursor.moveToNext();
            }
        }catch (Exception e){
            Log.e("Error: " + pQuery, e.getMessage());
        }finally {
            if( cursor != null ){
                cursor.close();
            }
        }
        return items;
    }

    public List<objectType> getFilteredItems(String pFilter){
        List<objectType> items = new ArrayList<>();
        Cursor cursor = null;
        try{
            cursor = database.query(tableID, null, pFilter, null, null, null, null);
            cursor.moveToFirst();
            while( !cursor.isAfterLast()){
                objectType item = objectTypeClass.newInstance();
                ((ICursorDecoder) item).decode(cursor);
                items.add(item);
                cursor.moveToNext();
            }
        }catch (Exception e){
            Log.e("Error: " + pFilter, e.getMessage());
        }finally {
            if( cursor != null ){
                cursor.close();
            }
        }
        return items;
    }

    public void createTable(String pCreateStatement ){
        try{
            database.execSQL("DROP TABLE IF EXIST " + tableID);
            database.execSQL(pCreateStatement);
        }catch (Exception e){
            Log.e("CommomDAO", pCreateStatement);
        }
    }

    public SQLiteDatabase getDatabase(){
        return database;
    }

    public void setDatabase(SQLiteDatabase database){
        this.database = database;
    }

    public  String getTableID(){
        return tableID;
    }

}
