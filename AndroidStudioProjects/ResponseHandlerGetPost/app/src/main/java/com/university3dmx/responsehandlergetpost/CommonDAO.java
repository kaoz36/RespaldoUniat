package com.university3dmx.responsehandlergetpost;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonDAO<objectType> {

    private SQLiteDatabase database;
    private OpenHelper helper;
    private String tableId;

    private Class<objectType> objectClass;

    public CommonDAO(Context pContext,
                     Class<objectType> objectClass,
                     String tableId) {
        helper = OpenHelper.getInstance(pContext);
        this.objectClass = objectClass;
        this.tableId = tableId;
    }


    public void clearDatabase() {
        database.execSQL("DROP TABLE IF EXISTS " + OpenHelper.TABLE_SIS_USER);
        database.execSQL("DROP TABLE IF EXISTS " + OpenHelper.TABLE_ESC_CAREER);
        database.execSQL("DROP TABLE IF EXISTS " + OpenHelper.TABLE_DAT_INTEREST);
        database.execSQL("DROP TABLE IF EXISTS " + OpenHelper.TABLE_DAT_CHARGE);
        database.execSQL("DROP TABLE IF EXISTS " + OpenHelper.TABLE_DAT_PERSON);
        database.execSQL("DROP TABLE IF EXISTS " + OpenHelper.TABLE_DAT_PERSONDESCRIPTION);
        database.execSQL("DROP TABLE IF EXISTS " + OpenHelper.TABLE_DAT_MEDIA);
        database.execSQL("DROP TABLE IF EXISTS " + OpenHelper.TABLE_DAT_CAMPUS);
        database.execSQL("DROP TABLE IF EXISTS " + OpenHelper.TABLE_DAT_ORIGIN);
        database.execSQL("DROP TABLE IF EXISTS " + OpenHelper.TABLE_DAT_COUNTRY);
        database.execSQL("DROP TABLE IF EXISTS " + OpenHelper.TABLE_DAT_REGION);
        database.execSQL("DROP TABLE IF EXISTS " + OpenHelper.TABLE_DAT_CITY);

        database.execSQL(OpenHelper.CREATE_SIS_USER);
        database.execSQL(OpenHelper.CREATE_ESC_CAREER);
        database.execSQL(OpenHelper.CREATE_DAT_INTEREST);
        database.execSQL(OpenHelper.CREATE_DAT_CHARGE);
        database.execSQL(OpenHelper.CREATE_DAT_PERSON);
        database.execSQL(OpenHelper.CREATE_DAT_PERSONDESCRIPTION);
        database.execSQL(OpenHelper.CREATE_DAT_MEDIA);
        database.execSQL(OpenHelper.CREATE_DAT_CAMPUS);
        database.execSQL(OpenHelper.CREATE_DAT_ORIGIN);
        database.execSQL(OpenHelper.CREATE_DAT_COUNTRY);
        database.execSQL(OpenHelper.CREATE_DAT_REGION);
        database.execSQL(OpenHelper.CREATE_DAT_CITY);
    }

    public void open() throws SQLException {
        database = helper.getWritableDatabase();
    }

    public void close() {
        // helper.close();
    }

    public void createItem(ContentValues values) {
        long insertId = database.insert(tableId, null, values);
        if (insertId == -1) {
            Log.e("ERROR", "Error al insertar dato");
        }
    }

    public long countItems() {
        Cursor mCount = database.rawQuery("SELECT COUNT(*) FROM " + tableId, null);
        mCount.moveToFirst();
        long count = mCount.getLong(0);
        mCount.close();
        return count;
    }

    public long countItems(String filter) {
        Cursor mCount = database.rawQuery("SELECT COUTN(*) FROM " + tableId + "  WHERE " + filter, null);
        mCount.moveToFirst();
        long count = mCount.getLong(0);
        mCount.close();
        return count;
    }


    public objectType createItem(Map<String, ?> objectMap) {
        ContentValues values = new ContentValues();
        objectType newObject = null;
        for (String key : objectMap.keySet()) {
            values.put(key, "" + objectMap.get(key));
        }
        long insertId = database.insert(tableId, null, values);
        Cursor cursor = database.query(tableId,
                null, null,
                null, null, null, null);


        cursor.moveToFirst();
        try {
            newObject = objectClass.newInstance();
            ((ICursorDecoder) newObject).decode(cursor);
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        } finally {

            cursor.close();
        }
        return newObject;
    }

    public void deleteItem(objectType item) {
        long id = ((ICursorDecoder) item).getId();
        Log.i("DAO", "Comment deleted with id: " + id);
        database.delete(tableId, "id  = " + id, null);
    }

    public List<objectType> getAllItems() {
        List<objectType> items = new ArrayList<objectType>();

        Cursor cursor = database.query(tableId,
                null, null, null, null, null, null);

        cursor.moveToFirst();

        try {
            while (!cursor.isAfterLast()) {
                objectType item = objectClass.newInstance();
                ((ICursorDecoder) item).decode(cursor);
                items.add(item);
                cursor.moveToNext();
            }
        } catch (Exception error) {

        } finally {
            cursor.close();
        }
        return items;
    }

    public List<ValueVO<objectType>> runQuery(String pQuery, int labelField) {
        List<ValueVO<objectType>> items = new ArrayList<ValueVO<objectType>>();
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(pQuery, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                objectType item = objectClass.newInstance();
                ((ICursorDecoder) item).decode(cursor);
                items.add(new ValueVO<objectType>(item, cursor.getString(labelField)));
                cursor.moveToNext();
            }
        } catch (Exception error) {
            Log.e("Error", error.toString());
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return items;
    }

    public void runQueryNoReturn(String pQuery) {
        try {
            database.execSQL(pQuery);
        } catch (Exception error) {
            Log.e("Error", error.toString());
        } finally {
        }
    }

    public List<objectType> runQuery(String pQuery) {
        List<objectType> items = new ArrayList<objectType>();
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(pQuery, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                objectType item = objectClass.newInstance();
                ((ICursorDecoder) item).decode(cursor);
                items.add(item);
                cursor.moveToNext();
            }
        } catch (Exception error) {
            Log.e("Error", error.toString());
        } finally {
            // Make sure to close the cursor
            if (cursor != null)
                cursor.close();
        }
        return items;
    }

    public List<objectType> getFilteredItems(String pFilter) {
        List<objectType> items = new ArrayList<objectType>();

        Cursor cursor = database.query(tableId,
                null, pFilter, null, null, null, null);

        cursor.moveToFirst();

        try {
            while (!cursor.isAfterLast()) {
                objectType item = objectClass.newInstance();
                ((ICursorDecoder) item).decode(cursor);
                items.add(item);
                cursor.moveToNext();
            }
        } catch (Exception error) {

        } finally {
            // Make sure to close the cursor
            cursor.close();
        }
        return items;
    }

    public void clearTable(String pCreateStatement) {
        try {
            database.execSQL("DROP TABLE IF EXISTS " + tableId);
            database.execSQL(pCreateStatement);
        } catch (Exception e) {
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
