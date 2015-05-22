package com.toad;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.toad.crawlers.BikesCrawler;
import com.toad.crawlers.TrafficCrawler;
import com.toad.crawlers.WeatherCrawler;
import com.toad.observers.BikesObserver;
import com.toad.observers.TrafficObserver;
import com.toad.observers.WeatherObserver;

/*    /\[([^\]]+)]/ */
public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {

        logger.fine("reading properties");
        SettingsManager.INSTANCE.loadDefaultSettings();

        WeatherObserver wo = new WeatherObserver();
        BikesObserver bo = new BikesObserver();
        TrafficObserver to = new TrafficObserver();

        WeatherCrawler.INSTANCE.addObserver(wo);
        BikesCrawler.INSTANCE.addObserver(bo);
        TrafficCrawler.INSTANCE.addObserver(to);


        WeatherCrawler.INSTANCE.start();
        BikesCrawler.INSTANCE.start();
        TrafficCrawler.INSTANCE.start();

        Server.start();

        while(true) {
            Thread.sleep(100);
        }
    }

}



