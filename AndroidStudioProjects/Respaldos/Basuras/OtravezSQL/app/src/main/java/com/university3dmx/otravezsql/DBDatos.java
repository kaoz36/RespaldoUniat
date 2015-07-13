package com.university3dmx.otravezsql;

import android.util.Log;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 20/04/2015.
 */
public class DBDatos extends DBConectar {

    public static List<Datos> getDatos() {

        List<Datos> datos = new ArrayList<>();
        String query = "SELECT * FROM datos";
        try {
            crearConexion();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String lastname = rs.getString("lastname");
                datos.add(new Datos(id, name, lastname));
                Log.d("Name: ", name);
            }
            rs.close();
        } catch (Exception e) {
            Log.i("Error:", e.getMessage());
        } finally {
            cerrarConexion();
        }

        return datos;

    }

    // Actualiza datos de la tabla familia en la BD.
    // Recibe como parámetro un objeto Familia.
    public static void updateFamilia(Datos datos) {

        String query;

        if (datos.getId() == 0) {
            // Si el Id de la Familia está a cero es una nueva fila.
            query = "INSERT INTO datos ( id, name, lastname ) VALUES ( " +
                    datos.getId() + ", " +
                    datos.getName() + ", " +
                    datos.getLastName() + ")";
        } else {
            // Si el id es distinto de cero es una actualización del registro.
            query = "UPDATE datos SET " +
                    "name = " + datos.getName() +
                    "WHERE id = " + datos.getId();
        }

        try {
            crearConexion();

            st.executeUpdate(query);
            //Cuando es una nueva fila, averiguo el ultimo Id adsignado por la BD y lo pongo en el objeto Familia.
            if (datos.getId() == 0) {
                ResultSet rs = st.executeQuery("SELECT LAST_INSERT_ID()");
                rs.first();
                datos.setId(rs.getInt(1));
                rs.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            cerrarConexion();
        }
        return;
    }

    // Borra datos de la tabla familia en la BD.
    // Recibe como parámetro un objeto Familia.
    public static void deleteFamilia(Datos datos) {

        String q;
        q = "DELETE FROM datos " +
                "WHERE id = " + datos.getId();

        try {
            crearConexion();
            st.executeUpdate(q);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            cerrarConexion();
        }
        return;
    }

}
