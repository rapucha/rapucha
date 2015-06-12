package com.toad.spider;

import com.toad.Util;
import com.toad.crawlers.ACrawler;

import java.io.InputStream;
import java.util.logging.Logger;

import static com.toad.SettingsManager.bikes_api_url;

/**
 * Created by toad on 6/9/15.
 */
public class BikeSpider extends ACrawler {

    public static final ACrawler INSTANCE = new BikeSpider();
    private static final int REPEAT_SECONDS = 60;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private BikeSpider() {
        super(REPEAT_SECONDS, 0, bikes_api_url, false);
    }


    @Override
    protected String getCrawlerThreadName() {
        return null;
    }

    @Override
    protected void processInput(InputStream is) {
        String s = Util.isToString(is);
        setChanged();
        notifyObservers(s);
    }

    @Override
    protected void reportProblem(Exception e) {
        logger.severe("Error in bike thread: " + e);
    }


    @Override
    protected void reportInfo(String s) {
        logger.info(s);
    }

}
