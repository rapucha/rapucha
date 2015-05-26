package com.toad.db;

import com.toad.SettingsManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Created by Morta on 17-May-15.
 */
public enum DBManager {

    INSTANCE;

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static Connection conn;
    private Logger logger = Logger.getLogger(DBManager.class.getName());


    public void initDBManager() {

        try {
            Class.forName(JDBC_DRIVER).newInstance();
            logger.info("JDBC driver registered ");
        } catch (Exception ex) {
            logger.severe("Cannot register DB driver " + ex);
            ex.printStackTrace();
        }

    }

    public Connection getConn() {

        try {
            if (conn == null || !conn.isValid(10)) {
                conn =
                        DriverManager.getConnection(SettingsManager.dburl + "/" + SettingsManager.dbschema, SettingsManager.dbuser, SettingsManager.dbpass);
            }
        } catch (SQLException e) {
            logger.severe("Cannot check connection health " + e);
            conn = null;
            e.printStackTrace();
        }
        if(conn == null) {
            logger.severe("Cannot get JDBS connection");
            throw new RuntimeException("cannot get JDBC connection");
        }

        return conn;
    }
}