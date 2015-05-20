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
    private static Logger logger = Logger.getLogger(DBManager.class.getName());

    private DBManager() {

        try {
            Class.forName(JDBC_DRIVER).newInstance();
        } catch (Exception ex) {
            logger.severe("Cannot connect to DB " + ex);
            ex.printStackTrace();
        }

    }

    public static Connection getConn(){

        try {
            if( conn == null || ! conn.isValid(10) ){
                conn =
                        DriverManager.getConnection(SettingsManager.dburl+"/"+SettingsManager.dbschema, SettingsManager.dbuser, SettingsManager.dbpass);
            }
        } catch (SQLException e) {
            logger.severe("Cannot check connection health "+e);
            conn = null;
            e.printStackTrace();
        }

        return conn;
    }
}
