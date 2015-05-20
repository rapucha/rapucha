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


    }
}
