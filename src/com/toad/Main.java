package com.toad;

import com.toad.crawlers.BikesCrawler;
import com.toad.crawlers.TrafficCrawler;
import com.toad.crawlers.WeatherCrawler;
import com.toad.db.BikesObserver;
import com.toad.db.DBManager;
import com.toad.db.TrafficObserver;
import com.toad.db.WeatherObserver;
import com.toad.server.Server;
import com.toad.subscription.Processor;

import java.util.logging.Logger;

/*    /\[([^\]]+)]/ */
public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {


        logger.fine("reading properties");
        SettingsManager.INSTANCE.loadDefaultSettings();

        //DBManager.INSTANCE.initDBManager();

        WeatherObserver wo = new WeatherObserver();
        BikesObserver bo = new BikesObserver();
        TrafficObserver to = new TrafficObserver();

        WeatherCrawler.INSTANCE.addObserver(wo);
        BikesCrawler.INSTANCE.addObserver(bo);
        TrafficCrawler.INSTANCE.addObserver(to);


        //WeatherCrawler.INSTANCE.start();
        //BikesCrawler.INSTANCE.start();
        //TrafficCrawler.INSTANCE.start();

        Server.INSTANCE.start();

        while (true) {
            Thread.sleep(100);
        }
    }

}



