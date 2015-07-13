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
    public static final int DBVERSION = 14;

    public static OpenHelper instance;

    public static final String TABLE_SIS_USER = "dat_email";

    public static final String TABLE_ESC_CAREER = "esc_career";

    public static final String TABLE_DAT_INTEREST = "dat_interest";
    public static final String TABLE_DAT_CHARGE = "dat_charge";
    public static final String TABLE_DAT_PERSON = "dat_person";
    public static final String TABLE_DAT_PERSONDESCRIPTION = "dat_persondescription";
    public static final String TABLE_DAT_MEDIA = "dat_media";
    public static final String TABLE_DAT_CAMPUS = "dat_campus";
    public static final String TABLE_DAT_ORIGIN = "dat_origin";
    public static final String TABLE_DAT_COUNTRY = "dat_country";
    public static final String TABLE_DAT_REGION = "dat_region";
    public static final String TABLE_DAT_CITY = "dat_city";

    /*public static final String CREATE_SIS_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_SIS_USER + "(" +
            " idUser INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " user VARCHAR(20)," +
            " password VARCHAR(64)," +
            " email VARCHAR(50)," +
            " idPerson INTEGER" +
            ")";*/

    /*public static final String CREATE_SIS_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_SIS_USER + "(" +
            " idUser VARCHAR(20) PRIMARY KEY, " +
            " user VARCHAR(20)," +
            " password VARCHAR(64)," +
            " email VARCHAR(50)," +
            " idPerson VARCHAR(20)" +
            ")";*/

    public static final String CREATE_SIS_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_SIS_USER + "(" +
            " idEmail INTEGER PRIMARY KEY AUTOINCREMENT," +
            " email VARCHAR(60))";

    public static final String CREATE_ESC_CAREER = "CREATE TABLE IF NOT EXISTS " + TABLE_ESC_CAREER + "(" +
            " idCareer VARCHAR(20) PRIMARY KEY, " +
            " name VARCHAR(20)," +
            " idCareerType VARCHAR(64)," +
            " prefix VARCHAR(50)" +
            ")";

    public static final String CREATE_DAT_INTEREST = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_INTEREST + "(" +
            " idPerson VARCHAR(5) PRIMARY KEY, " +
            " idCareer VARCHAR(5)" +
            ")";

    public static final String CREATE_DAT_CHARGE = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_CHARGE + "(" +
            " idCharge VARCHAR(5) PRIMARY KEY, " +
            " name VARCHAR(20)," +
            " status VARCHAR(2)" +
            ")";

    public static final String CREATE_DAT_PERSON = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_PERSON + "(" +
            " idPerson VARCHAR(5) PRIMARY KEY, " +
            " comments VARCHAR(50)," +
            " idCharge VARCHAR(5)," +
            " status VARCHAR(2)," +
            " idCamnpus VARCHAR(5)" +
            ")";

    public static final String CREATE_DAT_PERSONDESCRIPTION = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_PERSONDESCRIPTION + "(" +
            " idPerson VARCHAR(5) PRIMARY KEY, " +
            " name VARCHAR(20)," +
            " lastName VARCHAR(25)," +
            " dateBirth VARCHAR(20)," +
            " idCountry VARCHAR(5)," +
            " idRegion VARCHAR(15)," +
            " idCity VARCHAR(15)," +
            " neighborhood VARCHAR(20)," +
            " email VARCHAR(35)," +
            " gender VARCHAR(5)," +
            " idHomeCountry VARCHAR(5)," +
            " idHomeRegion VARCHAR(15)," +
            " idHomeCity VARCHAR(15)," +
            " curp VARCHAR(20)," +
            " rfc VARCHAR(35)," +
            " maritialStatus VARCHAR(15)," +
            " ocupation VARCHAR(15)," +
            " street VARCHAR(20)," +
            " numberExt VARCHAR(5)," +
            " numberInt VARCHAR(5)," +
            " zip VARCHAR(6)" +
            ")";

    public static final String CREATE_DAT_MEDIA = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_MEDIA + "(" +
            " idMedia VARCHAR(5) PRIMARY KEY, " +
            " name VARCHAR(20)," +
            " status VARCHAR(2)," +
            " idCampus VARCHAR(5)" +
            ")";

    public static final String CREATE_DAT_CAMPUS = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_CAMPUS + "(" +
            " idCampus VARCHAR(5) PRIMARY KEY, " +
            " name VARCHAR(20)," +
            " address VARCHAR(64)," +
            " status VARCHAR(2)," +
            " idCountry VARCHAR(5)," +
            " idRegion VARCHAR(15)," +
            " idCity VARCHAR(15)," +
            " nameAlt VARCHAR(25)" +
            ")";

    public static final String CREATE_DAT_ORIGIN = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_ORIGIN + "(" +
            " idOrigin VARCHAR(5) PRIMARY KEY, " +
            " name VARCHAR(20)," +
            " type VARCHAR(20)," +
            " status VARCHAR(2)," +
            " idCampus VARCHAR(5)" +
            ")";

    public static final String CREATE_DAT_COUNTRY = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_COUNTRY + "(" +
            " idContry VARCHAR(5) PRIMARY KEY, " +
            " name VARCHAR(25)" +
            ")";

    public static final String CREATE_DAT_REGION = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_REGION + "(" +
            " idRegion VARCHAR(5) PRIMARY KEY, " +
            " idContry VARCHAR(5), " +
            " name VARCHAR(25)" +
            ")";

    public static final String CREATE_DAT_CITY = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_CITY + "(" +
            " idCity VARCHAR(5) PRIMARY KEY, " +
            " idRegion VARCHAR(5), " +
            " name VARCHAR(25)" +
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
        db.execSQL(CREATE_ESC_CAREER);
        db.execSQL(CREATE_DAT_INTEREST);
        db.execSQL(CREATE_DAT_CHARGE);
        db.execSQL(CREATE_DAT_PERSON);
        db.execSQL(CREATE_DAT_PERSONDESCRIPTION);
        db.execSQL(CREATE_DAT_MEDIA);
        db.execSQL(CREATE_DAT_CAMPUS);
        db.execSQL(CREATE_DAT_ORIGIN);
        db.execSQL(CREATE_DAT_COUNTRY);
        db.execSQL(CREATE_DAT_REGION);
        db.execSQL(CREATE_DAT_CITY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIS_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESC_CAREER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_INTEREST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_CHARGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_PERSON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_PERSONDESCRIPTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_MEDIA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_CAMPUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_ORIGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_COUNTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_REGION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_CITY);
        onCreate(db);
    }

    public void clearDatabase(){
        SQLiteDatabase db = getWritableDatabase();
        onUpgrade(db, 0, 0);
        db.close();
        close();
    }

}
