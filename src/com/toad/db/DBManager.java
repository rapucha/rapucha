package com.toad.db;

import com.toad.SettingsManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Created by Seva Nechaev "Rapucha" on 17-May-15. All rights reserved ;)
 */
public enum DBManager {

    INSTANCE;

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final Logger logger = Logger.getLogger(DBManager.class.getName());


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
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection(SettingsManager.dburl + "/" + SettingsManager.dbschema, SettingsManager.dbuser, SettingsManager.dbpass);
        } catch (SQLException e) {
            logger.severe("Cannot get connection " + e);
            e.printStackTrace();
        }

        return conn;
    }
}
