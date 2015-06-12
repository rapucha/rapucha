package com.toad.db;

import com.toad.crawlers.StationCache;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.toad.Util.*;

/**
 * Created by Seva Nechaev "Rapucha" on 19-May-15. All rights reserved ;)
 */
public class BikesKeeper implements Observer {
    private static final String NAME_PATTERN = "\\[([^\\]]+)];";
    static final Pattern namePattern = Pattern.compile("var stationsData = " + NAME_PATTERN);
    private static final String NUMBER_PATTERN = "[0-9][0-9]{1,3}\\.";
    private static final Pattern numberPattern = Pattern.compile(NUMBER_PATTERN);
    private static final String STATIONS_TABLE = "stations";
    private static final String STATION_NUMBER = "station_number", NAME = "name", LOCATON = "location", LOCKS = "locks", BIKES = "bikes";
    private static final String STATION_HISTORY_TABLE = "station_history";
    private static final String TIMESTAMP = "timestamp";
    private static final String BIKES_HISTORY = "bikes_history";
    private static final String JSON_NAME = "Name";
    private static final String LAT = "Latitude";
    private static final String LON = "Longitude";
    private static final String TOTAL_LOCKS_PER_STATION = "TotalLocks";
    private static final String BIKES_PER_STATION = "AvailableBikes";
    private static final Logger logger = Logger.getLogger(BikesKeeper.class.getName());
    private static final String COUNT = "Count";
    private static final String LOCKED_IN_EXTERNAL_LOCK_COUNT = "LockedInExternalLockCount";
    private static final String LOCATIONS = "Locations";

    public static int getNumber(String name) throws Exception {
        Matcher m = numberPattern.matcher(name);
        if (m.find()) {
            String number = (m.group().replaceFirst("^0+(?!$)", "").replace(".", ""));//remove leading zeros and all dots

            return Integer.parseInt(number);
        } else {
            logger.severe("No number found in station name " + name);
            throw new Exception("No number found in station name " + name);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Timestamp ts = new Timestamp(new java.util.Date().getTime());
        int total = 0;

        StationCache.STATION_CACHE.dropCache();
        JSONObject jo = new JSONObject((String) arg);
        JSONArray jarr = jo.getJSONArray(LOCATIONS);
        for (int i = 0; i < jarr.length(); i++) {
            JSONObject jsonStation = jarr.getJSONObject(i);
            String name = safeString(jsonStation, JSON_NAME);
            double lat = safeDouble(jsonStation, LAT);
            double lon = safeDouble(jsonStation, LON);
            int locks = safeInt(jsonStation, TOTAL_LOCKS_PER_STATION);
            int subTotal = 0;
            JSONArray bikesDescr = jsonStation.getJSONArray(BIKES_PER_STATION);
            if (bikesDescr.length() != 0) {
                JSONObject jBikes = bikesDescr.getJSONObject(0);
                subTotal = safeInt(jBikes, COUNT);
                int external = jBikes.isNull(LOCKED_IN_EXTERNAL_LOCK_COUNT) ? 0 : safeInt(jBikes, LOCKED_IN_EXTERNAL_LOCK_COUNT);
                total = +subTotal + external;
            }

            StationCache.STATION_CACHE.updateCache(name, lat, lon, locks, subTotal, total);// TODO this double update should become DAO access one day
            updateStationState(name, lat, lon, locks, subTotal, ts);

        }
        StationCache.STATION_CACHE.publishCache();
    }

    private void updateStationState(String name, double lat, double lon, int locks, int subTotal, Timestamp ts) {
        int id = -1;
        try {
            id = getNumber(name);
        } catch (Exception e) {

            logger.severe("Cannot parse station number, assigning number -1 to station " + name);
        }

        Connection conn = DBManager.INSTANCE.getConn();

        String selectStationById = "SELECT " + NAME + ", X(" + LOCATON + "), Y(" + LOCATON + "), " + LOCKS + " " + " FROM " + STATIONS_TABLE + " WHERE " + STATION_NUMBER + " = " + id;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectStationById)) {
            if (!rs.isBeforeFirst()) {
                logger.info("no record for station" + name + "in STATIONS table");
//TODO implement history update if something changed
/*              {   logger.log(Level.INFO,"Creating history for "+name);
                String createStationInHistory = "INSERT INTO " + STATIONS_HISTORY_TABLE
                          + " ( " + STATION_NUMBER + " , " + NAME + " , " + LOCATON + "," + LOCKS + " , " + TIMESTAMP + " ) "
                          + " VALUES ( " + id + " , '" + name
                          + "' , GeomFromText( 'POINT(" + lat + " " + lon + ")' ), " + locks + " , '" + ts + "' )";
                  stmt.executeUpdate(createStationInHistory);
              }     */
                {
                    logger.fine("Creating actual record for " + name);
                    String createStation = "INSERT INTO " + STATIONS_TABLE
                            + " ( " + STATION_NUMBER + " , " + NAME + " , " + LOCATON + " , " + LOCKS + " ) "
                            + " VALUES ( " + id + " , '" + name
                            + "' , GeomFromText( 'POINT(" + lat + " " + lon + ")' ), "
                            + locks + " )";

                    stmt.executeUpdate(createStation);
                }
            } else {
                logger.finest("Checking if station has changed");
                while (!rs.isClosed() && rs.next()) {
                    String oldName = rs.getString(NAME);
                    double oldLocX = rs.getDouble("X(" + LOCATON + ")");
                    double oldLocY = rs.getDouble("Y(" + LOCATON + ")");
                    int oldLocks = rs.getInt(LOCKS);
                    if (wasStationUpdated(name, lat, lon, locks, oldName, oldLocX, oldLocY, oldLocks)) {
                        logger.info("updading history for " + name);
                        conn.setAutoCommit(false);
                        String updateStation = "UPDATE " + STATIONS_TABLE + " SET " + NAME + "='" + name + "'," +
                                LOCKS + "=" + locks + "," + LOCATON + "=GeomFromText( 'POINT(" + lat + " " + lon + ")' ) WHERE " + STATION_NUMBER +
                                "=" + id + ";\n";
                        String udpateStationInHistory = "INSERT INTO " + STATION_HISTORY_TABLE
                                + " ( " + STATION_NUMBER + " , " + NAME + " , " + LOCATON + "," + LOCKS + " , " + TIMESTAMP + " ) "
                                + " VALUES ( " + id + " , '" + oldName
                                + "' , GeomFromText( 'POINT(" + oldLocX + " " + oldLocY + ")' ), " + oldLocks + " , '" + ts + "' );";


                        stmt.executeUpdate(updateStation + udpateStationInHistory);
                        conn.commit();
                        conn.setAutoCommit(true);
//https://dev.mysql.com/doc/refman/5.7/en/gis-point-property-functions.html#function_st-x
                    }
                }
                logger.finest("Station " + name + " is already present, updating history");
                String addHistoryBikes = "INSERT INTO " + BIKES_HISTORY
                        + " ( " + TIMESTAMP + " , " + STATION_NUMBER + " , " + BIKES + " ) "
                        + "VALUES (? , ? , ?)";

                try (PreparedStatement ps = conn.prepareStatement(addHistoryBikes)) {
                    ps.setTimestamp(1, ts);
                    ps.setInt(2, id);
                    ps.setInt(3, subTotal);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            conn.close();
        } catch (SQLException e) {
            logger.severe("Cannot close connection " + e);
            e.printStackTrace();
        }
    }

    private boolean wasStationUpdated(String name, double lat, double lon, int locks, String oldName, double oldLocX, double oldLocY, int oldLocks) {
        if (!name.equals(oldName)) {
            logger.info("updating station due to" + name + "!= " + oldName);
            return true;
        }
        if (locks != oldLocks) {
            logger.info("updating station due to" + locks + "!= " + oldLocks);
            return true;
        }

        if (Double.compare(lat, oldLocX) != 0) {
            logger.info("updating station due to" + lat + "!= " + oldLocX);
            return true;
        }
        if (Double.compare(lon, oldLocY) != 0) {
            logger.info("updating station due to" + lon + "!= " + oldLocY);
            return true;
        }

        return false;
    }
}
