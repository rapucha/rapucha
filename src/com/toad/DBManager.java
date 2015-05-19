package com.toad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Created by Morta on 17-May-15.
 */
    public class DBManager {
    static final String JDBC_DRIVER="com.mysql.jdbc.Driver";
    private static Connection conn;
    public static final DBManager DBMANAGER = new DBManager();
    private Logger logger = Logger.getLogger(this.getClass().getName());

    private DBManager() {

        try {
            Class.forName(JDBC_DRIVER).newInstance();
        } catch (Exception ex) {
            logger.severe("Cannot connect to DB "+ex);
            ex.printStackTrace();
        }
        try {
            conn =
                    DriverManager.getConnection(SettingsManager.dburl, SettingsManager.dbuser, SettingsManager.dbpass);

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
