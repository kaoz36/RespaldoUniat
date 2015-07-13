package com.university3dmx.mysqlconnector;

import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Admin on 20/04/2015.
 */
public class DBConectar {

    public static Connection conn;
    public static Statement st;

    static String url = "jdbc:mysql://10.0.2.2:3306/"; // 10.0.2.2
    static String dbName = "basura";
    static String driver = "com.mysql.jdbc.Driver";
    static String userName = "root";
    static String password = "";
    static String cadCon = url + dbName;

    public static void crearConexion() throws InstantiationException, IllegalAccessException, ClassNotFoundException{
        Class.forName(driver).newInstance();
        try {
            conn = DriverManager.getConnection(cadCon, userName, password);
            st = conn.createStatement();
        }catch (Exception e){
            Log.i("Exception: ", e.getMessage() );
        }
    }

    public static void cerrarConexion(){
        try{
            if ( st != null ){
                st.close();
            }
            if( conn != null ){
                conn.close();
            }
        }catch (SQLException e){

        }
    }
}
