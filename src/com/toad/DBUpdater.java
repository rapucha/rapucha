package com.toad;


import org.json.JSONArray;

import java.sql.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Morta on 17-May-15.
 */
public class DBUpdater {
    private static final String STATIONS_TABLE = "stations";
    private  static final String ID_STATION = "id_station", NAME = "name", LOCATON = "location", LOCKS="locks", BIKES="bikes";

    private static final String STATIONS_HISTORY_TABLE = "stations_history";
    private static final String WHEN_UPDATED = "when_updated";

    private static final String BIKES_HISTORY = "bikes_history";
    private static final String BIKES_ARRAY = "bikes_array";

    private static final Connection conn = DBManager.getConn();

    private static final Logger logger = Logger.getLogger(DBUpdater.class.getName());


    static void updateStations(String name, double lat, double lon, int locks, int subTotal, Timestamp ts) throws Exception {
        int id = Crawler.getNumber(name);
        String selectStationById = "SELECT " + NAME + " FROM " + STATIONS_TABLE+ " WHERE "+ID_STATION+" = "+id;

        try(Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectStationById); )
        {

          if (!rs.isBeforeFirst() ) {
          logger.log(Level.INFO,"no record for station"+name+ "in STATIONS table");
//TODO implement history update if something changed
/*              {   logger.log(Level.INFO,"Creating history for "+name);
                String createStationInHistory = "INSERT INTO " + STATIONS_HISTORY_TABLE
                          + " ( " + ID_STATION + " , " + NAME + " , " + LOCATON + "," + LOCKS + " , " + WHEN_UPDATED + " ) "
                          + " VALUES ( " + id + " , '" + name
                          + "' , GeomFromText( 'POINT(" + lat + " " + lon + ")' ), " + locks + " , '" + ts + "' )";
                  stmt.executeUpdate(createStationInHistory);
              }     */
              { logger.log(Level.INFO,"Creating actual record for "+name);
                  String createStation = "INSERT INTO " + STATIONS_TABLE
                          + " ( " + ID_STATION + " , " + NAME + " , " + LOCATON + " , " + LOCKS + " , "+BIKES+" ) "
                          + " VALUES ( " + id + " , '" + name
                          + "' , GeomFromText( 'POINT(" + lat + " " + lon + ")' ), "
                          + locks + " , "+subTotal+ " )";
                  stmt.executeUpdate(createStation); }
          } else{
              logger.log(Level.INFO, "Station " + name + " is already present");
              {  String updateStation = "UPDATE " + STATIONS_TABLE
                          + " SET " + BIKES + " = " + subTotal + " WHERE " + ID_STATION + " = " +  id;
                  logger.log(Level.INFO, "Updating number of bikes for station:  \n" + updateStation);
                  stmt.executeUpdate(updateStation); }
          }

        }

        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void updateBikesHistory(JSONArray bikes, Timestamp ts) {
        {
            String addHistoryBikes = "INSERT INTO " + BIKES_HISTORY
                    + " ( " + BIKES_ARRAY + " , " + WHEN_UPDATED + " ) "
                    + "VALUES (? , ? )";
            logger.log(Level.INFO, "Updating history of bikes:  \n" + addHistoryBikes);

            try (PreparedStatement ps = conn.prepareStatement(addHistoryBikes);) {
                ps.setString(1, bikes.toString());
                ps.setTimestamp(2,ts);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }
}
