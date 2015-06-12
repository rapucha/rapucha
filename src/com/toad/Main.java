package com.toad;

import com.toad.crawlers.BikesCrawler;
import com.toad.crawlers.SSLCertificateValidation;
import com.toad.crawlers.TrafficCrawler;
import com.toad.crawlers.WeatherCrawler;
import com.toad.db.BikesKeeper;
import com.toad.db.DBManager;
import com.toad.db.TrafficKeeper;
import com.toad.db.WeatherKeeper;
import com.toad.server.CookieProvider;
import com.toad.server.Server;

import java.io.IOException;
import java.util.logging.Logger;

/*    /\[([^\]]+)]/ */
public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {


        logger.fine("reading properties");
        SettingsManager.INSTANCE.loadDefaultSettings();

        DBManager.INSTANCE.initDBManager();

        WeatherKeeper wo = new WeatherKeeper();
        BikesKeeper bo = new BikesKeeper();
        TrafficKeeper to = new TrafficKeeper();

        BikesCrawler.INSTANCE.addObserver(bo);
        WeatherCrawler.INSTANCE.addObserver(wo);
        TrafficCrawler.INSTANCE.addObserver(to);

        SSLCertificateValidation.disable();
        long time = System.currentTimeMillis();
        while (BikesCrawler.INSTANCE.start().isDone()) {
            System.out.print("Waiting for bikes thread to populate the base" + (System.currentTimeMillis() - time));
        }

        WeatherCrawler.INSTANCE.start();

        TrafficCrawler.INSTANCE.start();

        CookieProvider.INSTANCE.init();

        try {
            Server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}




