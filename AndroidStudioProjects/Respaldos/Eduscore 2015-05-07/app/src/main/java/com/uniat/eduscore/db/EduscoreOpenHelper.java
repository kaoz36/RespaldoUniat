package com.uniat.eduscore.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Admin on 13/04/2015.
 */
public class EduscoreOpenHelper extends SQLiteOpenHelper{

    public static final String DBNAME = "eduscore.db";
    public static final int DBVERSION = 1;

    public static final String TABLE_SIS_USERS = "sis_user";

    public static final String CREATE_SIS_USERS = "CREATE TABLE IF NOT EXISTS " + TABLE_SIS_USERS + "(" +
            "idUser INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "user VARCHAR(20) NOT NULL," +
            "password VARCHAR(64) NOT NULL," +
            "email VARCHAR(50)," +
            "idPerson INTEGER NOT NULL" +
            ")";

    public static EduscoreOpenHelper instance;

    public EduscoreOpenHelper(Context context) throws Exception{
        super(context, DBNAME, null, DBVERSION);
        if( instance != null)
            throw new Exception("Error de Singleton");
    }

    public static EduscoreOpenHelper getInstance( Context context ){
        if( instance == null ){
            try{
                instance = new EduscoreOpenHelper(context);
            }catch ( Exception e ){
                e.printStackTrace();
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SIS_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIS_USERS);
    }

    public void clearDatabase(){
        SQLiteDatabase db = getWritableDatabase();
        onUpgrade(db, 0, 0);
        db.close();
        close();
    }
}
