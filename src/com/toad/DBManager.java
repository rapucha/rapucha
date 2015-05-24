package com.toad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Created by Morta on 17-May-15.
 */
    public enum DBManager {

    INSTANCE;

    static final String JDBC_DRIVER="com.mysql.jdbc.Driver";
    private static Connection conn;
    private  Logger logger = Logger.getLogger(DBManager.class.getName());


    public void initDBManager() {

        try {
            Class.forName(JDBC_DRIVER).newInstance();
            System.out.println("JDBC driver registered ");
        } catch (Exception ex) {
            System.out.println("Cannot register DB driver " + ex);
            logger.severe("Cannot register DB driver " + ex);
            ex.printStackTrace();
        }

    }

    public  Connection getConn(){

        try {
            if( conn == null || ! conn.isValid(10) ){
                conn =
                        //DriverManager.getConnection(SettingsManager.dburl+"/"+SettingsManager.dbschema, SettingsManager.dbuser, SettingsManager.dbpass);
                        DriverManager.getConnection("jdbc:mysql://localhost:3306/rapucha", "rapucha", "DrinhoifaucKuOd3");
            }
        } catch (SQLException e) {
            logger.severe("Cannot check connection health "+e);
            conn = null;
            e.printStackTrace();
        }

        return conn;
    }
}
