package com.toad;

import com.toad.crawlers.BikesCrawler;
import com.toad.crawlers.TrafficCrawler;
import com.toad.crawlers.WeatherCrawler;
import com.toad.db.BikesKeeper;
import com.toad.db.TrafficKeeper;
import com.toad.db.WeatherKeeper;
import com.toad.server.CookieProvider;
import com.toad.server.Server;

import java.util.logging.Logger;

/*    /\[([^\]]+)]/ */
public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {


        logger.fine("reading properties");
        SettingsManager.INSTANCE.loadDefaultSettings();

        //DBManager.INSTANCE.initDBManager();

        WeatherKeeper wo = new WeatherKeeper();
        BikesKeeper bo = new BikesKeeper();
        TrafficKeeper to = new TrafficKeeper();

        WeatherCrawler.INSTANCE.addObserver(wo);
        BikesCrawler.INSTANCE.addObserver(bo);
        TrafficCrawler.INSTANCE.addObserver(to);


        WeatherCrawler.INSTANCE.start();
        BikesCrawler.INSTANCE.start();
        TrafficCrawler.INSTANCE.start();

        CookieProvider.INSTANCE.init();
        Server.INSTANCE.start();

        while (true) {
            Thread.sleep(100);
        }
    }

}



