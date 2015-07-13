package com.uniat.eduscore.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Admin on 13/04/2015.
 */
public class EduscoreOpenHelper extends SQLiteOpenHelper{

    public static final String DBNAME = "eduscore.db";
    public static final int DBVERSION = 10;

    public static final String TABLE_SIS_USER = "sis_user";

    public static final String TABLE_ESC_CAREER = "esc_career";
    public static final String TABLE_ESC_CAREERTYPE = "esc_careerType";


    public static final String TABLE_DAT_INTEREST = "dat_interest";
    public static final String TABLE_DAT_CHARGE = "dat_charge";
    public static final String TABLE_DAT_PERSON = "dat_person";
    public static final String TABLE_DAT_PERSONDESCRIPTION = "dat_persondescription";
    public static final String TABLE_DAT_MEDIA = "dat_media";
    public static final String TABLE_DAT_CAMPUS = "dat_campus";
    public static final String TABLE_DAT_ORIGIN = "dat_origin";
    public static final String TABLE_DAT_EVENTS = "dat_events";
    public static final String TABLE_DAT_COUNTRY = "dat_country";
    public static final String TABLE_DAT_REGION = "dat_region";
    public static final String TABLE_DAT_CITY = "dat_city";
    public static final String TABLE_DAT_EMAIL = "dat_email";

    public static EduscoreOpenHelper instance;

    public static final String CREATE_SIS_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_SIS_USER + "(" +
            " idTableUser INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " idUser VARCHAR(20), " +
            " user VARCHAR(20), " +
            " password VARCHAR(64), " +
            " idPerson VARCHAR(20) " +
            ")";

    public static final String CREATE_ESC_CAREER = "CREATE TABLE IF NOT EXISTS " + TABLE_ESC_CAREER + "(" +
            " idTableCareer INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " idCareer VARCHAR(20), " +
            " name VARCHAR(20)," +
            " idCareerType VARCHAR(4)," +
            " prefix VARCHAR(50)," +
            " status VARCHAR(2)" +
            ")";

    public static final String CREATE_ESC_CAREERTYPE = "CREATE TABLE IF NOT EXISTS " + TABLE_ESC_CAREERTYPE + "(" +
            " idTableCareerType INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " idCareerType VARCHAR(4), " +
            " name VARCHAR(50)," +
            " status VARCHAR(4)" +
            ")";

    public static final String CREATE_DAT_INTEREST = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_INTEREST + "(" +
            " idTableInterest INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " idPerson VARCHAR(5), " +
            " idCareer VARCHAR(5)" +
            ")";

    public static final String CREATE_DAT_CHARGE = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_CHARGE + "(" +
            " idTableCharge INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " idCharge VARCHAR(5), " +
            " name VARCHAR(20)," +
            " status VARCHAR(2)" +
            ")";

    public static final String CREATE_DAT_PERSON = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_PERSON + "(" +
            " idTablePerson INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " idPerson VARCHAR(5), " +
            " comments VARCHAR(50)," +
            " idCharge VARCHAR(5)," +
            " status VARCHAR(2)," +
            " idCamnpus VARCHAR(5)" +
            ")";

    public static final String CREATE_DAT_PERSONDESCRIPTION = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_PERSONDESCRIPTION + "(" +
            " idTablePersonDescription INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " idPerson VARCHAR(5), " +
            " name VARCHAR(20)," +
            " lastName VARCHAR(25)," +
            " idCountry VARCHAR(5)," +
            " idRegion VARCHAR(15)," +
            " idCity VARCHAR(15)," +
            " gender VARCHAR(5)," +
            " curp VARCHAR(20)," +
            " rfc VARCHAR(35)," +
            " street VARCHAR(20)," +
            " numberExt VARCHAR(5)," +
            " numberInt VARCHAR(5)," +
            " zip VARCHAR(6)" +
            ")";

    public static final String CREATE_DAT_MEDIA = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_MEDIA + "(" +
            " idTableMedia INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " idMedia VARCHAR(5), " +
            " name VARCHAR(20)," +
            " status VARCHAR(2)," +
            " idCampus VARCHAR(5)" +
            ")";

    public static final String CREATE_DAT_CAMPUS = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_CAMPUS + "(" +
            " idTableCampus INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " idCampus VARCHAR(5), " +
            " name VARCHAR(20)," +
            " address VARCHAR(64)," +
            " status VARCHAR(2)," +
            " idCountry VARCHAR(5)," +
            " idRegion VARCHAR(15)," +
            " idCity VARCHAR(15)," +
            " nameAlt VARCHAR(25)" +
            ")";

    public static final String CREATE_DAT_ORIGIN = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_ORIGIN + "(" +
            " idTableOrigin INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " idOrigin VARCHAR(5), " +
            " name VARCHAR(20)," +
            " type VARCHAR(20)," +
            " status VARCHAR(2)," +
            " idCampus VARCHAR(5)" +
            ")";

    public static final String CREATE_DAT_EVENTS = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_EVENTS + "(" +
            " idTableEvent INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " idEvent VARCHAR(5), " +
            " name VARCHAR(80)," +
            " status VARCHAR(2)," +
            " idCampus VARCHAR(5)" +
            ")";

    public static final String CREATE_DAT_COUNTRY = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_COUNTRY + "(" +
            " idTableCountry INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " idCountry VARCHAR(5), " +
            " name VARCHAR(25)" +
            ")";

    public static final String CREATE_DAT_REGION = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_REGION + "(" +
            " idTableRegion INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " idRegion VARCHAR(10), " +
            " idContry VARCHAR(5), " +
            " name VARCHAR(25)" +
            ")";

    public static final String CREATE_DAT_CITY = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_CITY + "(" +
            " idTableCity INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " idCity VARCHAR(15), " +
            " idRegion VARCHAR(5), " +
            " name VARCHAR(25)" +
            ")";

    public static final String CREATE_DAT_EMAIL = "CREATE TABLE IF NOT EXISTS " + TABLE_DAT_EMAIL + "(" +
            " idEmail INTEGER PRIMARY KEY AUTOINCREMENT," +
            " email VARCHAR(60))";



    public EduscoreOpenHelper(Context context) throws Exception{
        super(context, DBNAME, null, DBVERSION);
        if( instance != null)
            throw new Exception("Error de Singleton");
    }

    public static EduscoreOpenHelper getInstance(Context context){
        if( instance == null ){
            try {
                instance = new EduscoreOpenHelper(context);
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
        db.execSQL(CREATE_ESC_CAREERTYPE);
        db.execSQL(CREATE_DAT_INTEREST);
        db.execSQL(CREATE_DAT_CHARGE);
        db.execSQL(CREATE_DAT_PERSON);
        db.execSQL(CREATE_DAT_PERSONDESCRIPTION);
        db.execSQL(CREATE_DAT_MEDIA);
        db.execSQL(CREATE_DAT_CAMPUS);
        db.execSQL(CREATE_DAT_ORIGIN);
        db.execSQL(CREATE_DAT_EVENTS);
        db.execSQL(CREATE_DAT_COUNTRY);
        db.execSQL(CREATE_DAT_REGION);
        db.execSQL(CREATE_DAT_CITY);
        db.execSQL(CREATE_DAT_EMAIL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIS_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESC_CAREER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESC_CAREERTYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_INTEREST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_CHARGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_PERSON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_PERSONDESCRIPTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_MEDIA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_CAMPUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_ORIGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_COUNTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_REGION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_CITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAT_EMAIL);
        onCreate(db);
    }

    public void clearDatabase(){
        SQLiteDatabase db = getWritableDatabase();
        onUpgrade(db, 0, 0);
        db.close();
        close();
    }
}
