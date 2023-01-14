package com.anggastudio.sample;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
    public static Connection conexionDB(){

        Connection cnn = null;

        try {
            StrictMode.ThreadPolicy polity = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(polity);

            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            cnn = DriverManager.getConnection(
                    "jdbc:jtds:sqlserver://192.168.1.227;databaseName=APPSVEN_TYP;user=sven;password=@dm19!;integratedSecurity=true;");
        } catch (Exception e) {
            //  Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
        }
        return cnn;

    }
}
