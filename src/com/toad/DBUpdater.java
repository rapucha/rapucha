package com.toad;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Morta on 17-May-15.
 */
public class DBUpdater {
    private static final String STATIONS_TABLE = "stations";
    private  static final String ID_STATION = "id_station", NAME = "name", LOCATON = "location", LOCKS="locks", BIKES="bikes";

    private static final String STATIONS_HISTORY_TABLE = "stations";
    private static final String WHEN_UPDATED = "when_updated";

    private static final String BIKES_NUMBER_TABLE = "bikes_number";



    static void updateDBState(String name, double lat, double lon, int locks, int subTotal) throws Exception {
        int id = Crawler.getNumber(name);
        Statement stmt = DBManager.getConn().createStatement();
        ResultSet rs;
        rs = stmt.executeQuery("SELECT " + ID_STATION + " FROM " + STATIONS_TABLE);
        if (!rs.isBeforeFirst() ) {
            stmt.executeUpdate("INSERT INTO " + STATIONS_HISTORY_TABLE + " (" + ID_STATION + ", , )");
        }
        //http://dev.mysql.com/doc/connector-j/en/connector-j-usagenotes-statements.html
    }
}
