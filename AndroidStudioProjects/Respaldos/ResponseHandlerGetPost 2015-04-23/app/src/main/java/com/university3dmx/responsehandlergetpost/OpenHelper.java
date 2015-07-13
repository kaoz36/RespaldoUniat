package com.university3dmx.responsehandlergetpost;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Admin on 23/04/2015.
 */
public class OpenHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "open.db";
    public static final int DBVERSION = 1;

    public static OpenHelper instance;

    public static final String TABLE_SIS_USER = "sis_user";

    public static final String CREATE_SIS_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_SIS_USER + "(" +
            " idUser INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            " user VARCHAR(20) NOT NULL," +
            " password VARCHAR(64) NOT NULL," +
            " email VARCHAR(50) NOT NULL," +
            " idPerson INTEGER NOT NULL" +
            ")";

    public OpenHelper(Context context) throws Exception{
        super(context, DBNAME, null, DBVERSION);
        if( instance != null)
            throw new Exception("Error de Singleton");
    }

    public static OpenHelper getInstance(Context context){
        if( instance == null ){
            try {
                instance = new OpenHelper(context);
            }catch (Exception e){
                Log.e("Error", e.getMessage());
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SIS_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIS_USER);
        onCreate(db);
    }

    public void clearDatabase(){
        SQLiteDatabase db = getWritableDatabase();
        onUpgrade(db, 0, 0);
        db.close();
        close();
    }

}
