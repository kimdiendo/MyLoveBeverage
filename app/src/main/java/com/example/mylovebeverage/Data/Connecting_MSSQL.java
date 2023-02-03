package com.example.mylovebeverage.Data;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connecting_MSSQL {
    private static String concat_url = null;
    private static final String username = "ad";
    private static final String password = "Honda@123";
    private Connection connection;

    public Connecting_MSSQL(Connection connection) {
        this.connection = connection;
    }

    public Connection Connecting() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        concat_url = "jdbc:jtds:sqlserver://sqlvmooad.database.windows.net:1433;DatabaseName=MYLOVEBEVERAGE;user=" + username + ";password=" + password + ";encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        try
        {

            Class.forName("net.sourceforge.jtds.jdbc.Driver");//register JDBC driver
            connection = DriverManager.getConnection(concat_url); //success
            Log.e("FLAT", "SUCCESS");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e)
        {
            e.printStackTrace();//failure
        }
        return connection;
    }
}
