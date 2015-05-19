package com.toad;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Morta on 19-May-15.
 */
public class TestObserver implements Observer {
    private  final Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof WeatherCrawler) {

            logger.log(Level.INFO,(String) arg);
        }
        if (o instanceof BikesCrawler){

            logger.log(Level.INFO, (String) arg);

        }
    }
}
