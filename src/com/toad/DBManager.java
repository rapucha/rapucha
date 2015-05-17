package com.toad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Morta on 17-May-15.
 */
    public class DBManager {
    static final String JDBC_DRIVER="com.mysql.jdbc.Driver";
    private static final String DB_PASSWORD ="DrinhoifaucKuOd3";//FIXME change LOL)))
    private static final String DB_USER ="rapucha";
    private static final String DB_URL="jdbc:mysql://localhost/rapucha";
    private static Connection conn;
    public static final DBManager DBMANAGER = new DBManager();

    private DBManager() {

        try {
            Class.forName(JDBC_DRIVER).newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            conn =
                    DriverManager.getConnection(DBManager.DB_URL, DBManager.DB_USER, DBManager.DB_PASSWORD);

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

    }

    public static Connection getConn(){
        return conn;
    }
}
