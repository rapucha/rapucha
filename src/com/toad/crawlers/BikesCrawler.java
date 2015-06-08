package com.toad.crawlers;

import com.toad.SettingsManager;
import com.toad.Util;

import java.io.InputStream;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created by Seva Nechaev "Rapucha" on 19-May-15. All rights reserved ;)
 */
public class BikesCrawler extends ACrawler {
    public static final ACrawler INSTANCE = new BikesCrawler();
    private static final String NAME_PATTERN = "\\[([^\\]]+)];";
    private static final Pattern namePattern = Pattern.compile("var stationsData = " + NAME_PATTERN);
    private static final int REPEAT_SECONDS = 7 * 60;//TODO implement some centarl time scheduling
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private BikesCrawler() {
        super(REPEAT_SECONDS, SettingsManager.bikes_url, false);
    }


    @Override
    protected void processInput(InputStream is) {
        String s = Util.isToString(is);
        setChanged();
        notifyObservers(s.toString());
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
