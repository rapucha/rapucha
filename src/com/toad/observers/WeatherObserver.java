package com.toad.observers;

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
        int observation_epoch = jo.getInt("observation_epoch");
        String weather_string = jo.getString("weather");
        int temp_c = jo.getInt("temp_c");
        int relative_humidity = Integer.parseInt((jo.getString("relative_humidity")).replace("%",""));
        int wind_degrees = jo.getInt("wind_degrees");
        int wind_kph = jo.getInt("wind_kph");
        int pressure_mb = jo.getInt("pressure_mb");
        int pressure_trend = jo.getInt("pressure_trend");
        int dewpoint_c = jo.getInt("dewpoint_c");
        int heat_index_c = jo.getInt("heat_index_c");
        int windchill_c = jo.getInt("windchill_c");
        int feelslike_c= jo.getInt("feelslike_c");
        int visibility_km = jo.getInt("visibility_km");
        String icon = jo.getString("icon");

        //TODO make all getters safe to faulty values
    }
}
