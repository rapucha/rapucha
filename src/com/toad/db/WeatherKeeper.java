package com.toad.db;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import static com.toad.Util.*;

/**
 * Created by Seva Nechaev "Rapucha" on 19-May-15. All rights reserved ;)
 */
public class WeatherKeeper implements Observer {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void update(Observable o, Object arg) {

        Connection conn = DBManager.INSTANCE.getConn();

        JSONObject jo = (new JSONObject((String) arg)).getJSONObject("current_observation");

        int observation_epoch = safeInt(jo, "observation_epoch");
        String weather_string = safeString(jo, "weather");
        int temp_c = safeInt(jo, "temp_c");
        int relative_humidity = Integer.parseInt((safeString(jo, "relative_humidity")).replace("%", ""));
        int wind_degrees = safeInt(jo, "wind_degrees");
        int wind_kph = safeInt(jo, "wind_kph");
        int pressure_mb = safeInt(jo, "pressure_mb");
        String pressure_trend = safeString(jo, "pressure_trend");
        int dewpoint_c = safeInt(jo, "dewpoint_c");
        int heat_index_c = safeInt(jo, "heat_index_c");
        int windchill_c = safeInt(jo, "windchill_c");
        int feelslike_c = safeInt(jo, "feelslike_c");
        double visibility_km = safeDouble(jo, "visibility_km");
        String icon = safeString(jo, "icon");
        Timestamp ts = new Timestamp(new java.util.Date().getTime());

        String addObservation = "INSERT INTO observations (timestamp, observation_epoch, weather_string, " +
                "temp_c, relative_humidity, wind_degrees, wind_kph, pressure_mb, pressure_trend, dewpoint_c, " +
                "heat_index_c, windchill_c, feelslike_c, visibility_km, icon) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(addObservation)) {
            ps.setTimestamp(1, ts);
            ps.setInt(2, observation_epoch);
            ps.setString(3, weather_string);
            ps.setInt(4, temp_c);
            ps.setInt(5, relative_humidity);
            ps.setInt(6, wind_degrees);
            ps.setInt(7, wind_kph);
            ps.setInt(8, pressure_mb);
            ps.setString(9, pressure_trend);
            ps.setInt(10, dewpoint_c);
            ps.setInt(11, heat_index_c);
            ps.setInt(12, windchill_c);
            ps.setInt(13, feelslike_c);
            ps.setFloat(14, (float) visibility_km);
            ps.setString(15, icon);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.severe("cannot paste " + e.getMessage());
            e.printStackTrace();
        }

        try {
            conn.close();
        } catch (SQLException e) {
            logger.severe("Cannot close connection " + e);
            e.printStackTrace();
        }

    }


}
