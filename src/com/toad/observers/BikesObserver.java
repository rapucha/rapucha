package com.toad.observers;

import com.toad.DBManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Morta on 19-May-15.
 */
public class BikesObserver  implements Observer {
    private static final String STATIONS_TABLE = "stations";
    private  static final String STATION_NUMBER = "station_number", NAME = "name", LOCATON = "location", LOCKS="locks", BIKES="bikes";

    private static final String STATIONS_HISTORY_TABLE = "stations_history";
    private static final String TIMESTAMP = "timestamp";

    private static final String BIKES_HISTORY = "bikes_history";

    private static final String JSON_NAME="Name";
    private static final String LAT="Latitude";
    private static final String LON="Longitude";
    private static final String TOTAL_LOCKS="TotalLocks";
    private static final String TOTAL_BIKES ="TotalAvailableBikes";

    static final String NAME_PATTERN = "\\[([^\\]]+)];";
    static final Pattern namePattern = Pattern.compile("var stationsData = "+ NAME_PATTERN);
    static final String NUMBER_PATTERN = "[0-9][0-9]{1,3}\\.";
    static final Pattern numberPattern = Pattern.compile(NUMBER_PATTERN);

    private  final Logger logger = Logger.getLogger(this.getClass().getName());
    private Connection conn;

    @Override
    public void update(Observable o, Object arg) {
        Timestamp ts = new Timestamp(new java.util.Date().getTime());
        JSONArray jarr = new JSONArray((String) arg);
        for (int i = 0; i < jarr.length(); i++) {
            JSONObject jsonobject = jarr.getJSONObject(i);
            String name = jsonobject.getString(JSON_NAME);
            double lat = jsonobject.getDouble(LAT);
            double lon = jsonobject.getDouble(LON);
            int locks = jsonobject.getInt(TOTAL_LOCKS);
            int subTotal = jsonobject.getInt(TOTAL_BIKES);
            updateStationState(name, lat, lon, locks, subTotal, ts);
        }

    }


    private void updateStationState(String name, double lat, double lon, int locks, int subTotal, Timestamp ts)  {
        int id = 0;
        try {
            id = getNumber(name);
        } catch (Exception e) {
            logger.severe("Assigning number 0 to station " +name);
        }
         conn = DBManager.getConn();

        String selectStationById = "SELECT " + NAME + " FROM " + STATIONS_TABLE+ " WHERE "+ STATION_NUMBER +" = "+id;
        try(Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectStationById); )
        {
            if (!rs.isBeforeFirst() ) {
                logger.log(Level.INFO,"no record for station"+name+ "in STATIONS table");
//TODO implement history update if something changed
/*              {   logger.log(Level.INFO,"Creating history for "+name);
                String createStationInHistory = "INSERT INTO " + STATIONS_HISTORY_TABLE
                          + " ( " + STATION_NUMBER + " , " + NAME + " , " + LOCATON + "," + LOCKS + " , " + TIMESTAMP + " ) "
                          + " VALUES ( " + id + " , '" + name
                          + "' , GeomFromText( 'POINT(" + lat + " " + lon + ")' ), " + locks + " , '" + ts + "' )";
                  stmt.executeUpdate(createStationInHistory);
              }     */
                { logger.log(Level.INFO,"Creating actual record for "+name);
                    String createStation = "INSERT INTO " + STATIONS_TABLE
                            + " ( " + STATION_NUMBER + " , " + NAME + " , " + LOCATON + " , " + LOCKS + " ) "
                            + " VALUES ( " + id + " , '" + name
                            + "' , GeomFromText( 'POINT(" + lat + " " + lon + ")' ), "
                            + locks + " )";
                    stmt.executeUpdate(createStation); }
            } else{
                logger.log(Level.INFO, "Station " + name + " is already present, updating history");
                String addHistoryBikes = "INSERT INTO " + BIKES_HISTORY
                        + " ( " + TIMESTAMP + " , " + STATION_NUMBER + " , "+ BIKES+" ) "
                        + "VALUES (? , ? , ?)";

                try (PreparedStatement ps = conn.prepareStatement(addHistoryBikes);) {
                    ps.setTimestamp(1, ts);
                    ps.setInt(2, id);
                    ps.setInt(3, subTotal);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

        catch(SQLException e){
            e.printStackTrace();
        }
        try {
            conn.close();
        } catch (SQLException e) {
            logger.severe("Cannot close connection "+e);
            e.printStackTrace();
        }
    }


    private int getNumber(String name) throws Exception {
        Matcher m = numberPattern.matcher(name);
        if (m.find()){
            String number = (m.group().replaceFirst("^0+(?!$)", "").replace(".",""));//remove leading zeros and all dots

            return Integer.parseInt(number);
        } else {
            logger.severe("No number found in station name "+name);
            throw new Exception("No number found in station name "+name);
        }
    }
}