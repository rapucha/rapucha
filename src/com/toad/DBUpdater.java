package com.toad;


import java.sql.*;
import java.util.Date;

/**
 * Created by Morta on 17-May-15.
 */
public class DBUpdater {
    private static final String STATIONS_TABLE = "stations";
    private  static final String ID_STATION = "id_station", NAME = "name", LOCATON = "location", LOCKS="locks", BIKES="bikes";

    private static final String STATIONS_HISTORY_TABLE = "stations_history";
    private static final String WHEN_UPDATED = "when_updated";
    private static final Connection conn = DBManager.getConn();
    private static final String BIKES_NUMBER_TABLE = "bikes_number";



    static void updateDBState(String name, double lat, double lon, int locks, int subTotal) throws Exception {
        int id = Crawler.getNumber(name);
        Statement stmt = conn.createStatement();
        ResultSet rs;
        rs = stmt.executeQuery("SELECT " + ID_STATION + " FROM " + STATIONS_TABLE);
        if (!rs.isBeforeFirst() ) {
            Timestamp ts = new Timestamp(new java.util.Date().getTime());
            String createStationInHistory = "INSERT INTO " + STATIONS_HISTORY_TABLE
                            + " ( " + ID_STATION + " , "+NAME+" , "+ LOCATON + ","+LOCKS+" , "+WHEN_UPDATED +" ) "
                            +" VALUES ( "+Crawler.getNumber(name)+" , '"+ name
                    +"' , GeomFromText( 'POINT(" +lat+" "+lon+")' ), "+ locks+ " , '" +ts+ "' )";

            System.out.println(createStationInHistory);
            int i = stmt.executeUpdate(createStationInHistory);
            stmt.close();
        }
        rs.close();

        //http://dev.mysql.com/doc/connector-j/en/connector-j-usagenotes-statements.html
    }
}
