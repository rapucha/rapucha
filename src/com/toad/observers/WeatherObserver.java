package com.toad.observers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Morta on 19-May-15.
 */
public class WeatherObserver implements Observer {
    private  final Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void update(Observable o, Object arg) {
        JSONObject jo = new JSONObject((String) arg);
        jo = jo.getJSONObject("current_observation");
        int observation_epoch = safeInt(jo,"observation_epoch");
        String weather_string = safeString(jo,"weather");
        int temp_c = safeInt(jo,"temp_c");
        int relative_humidity = Integer.parseInt((safeString(jo,"relative_humidity")).replace("%", ""));
        int wind_degrees = safeInt(jo,"wind_degrees");
        int wind_kph = safeInt(jo,"wind_kph");
        int pressure_mb = safeInt(jo,"pressure_mb");
        int pressure_trend = safeInt(jo,"pressure_trend");
        int dewpoint_c = safeInt(jo,"dewpoint_c");
        int heat_index_c = safeInt(jo, "heat_index_c");
        int windchill_c = safeInt(jo,"windchill_c");
        int feelslike_c= safeInt(jo,"feelslike_c");
        int visibility_km = safeInt(jo,"visibility_km");
        String icon = safeString(jo,"icon");
        String addHistoryBikes = "INSERT INTO bikes_history (timestamp, observation_epoch, weather_string," +
                "temp_c, relative_humidity, wind_degrees, wind_kph, pressure_mb, pressure_trend, dewpoint_c" +
                "heat_index_c, windchill_c, feelslike_c, visibility_km, icon) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?)";


    }

    private int safeInt(JSONObject jo, String s) {
        int i = Short.MAX_VALUE;
        try{
             i = jo.getInt(s);
        }
            catch (JSONException j){
                logger.info("No parameter in JSON strinig "+s);
            }

        return i;
    }
    private String safeString(JSONObject jo, String s) {
        String r = "ERROR";
        try{
            r = jo.getString(s);
        }
        catch (JSONException j){
            logger.info("No parameter in JSON strinig "+s);
        }

        return r;
    }


}
