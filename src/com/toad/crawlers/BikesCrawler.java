package com.toad.crawlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Morta on 19-May-15.
 */
public class BikesCrawler extends ACrawler {
    private static final String NAME_PATTERN = "\\[([^\\]]+)];";
    private static final Pattern namePattern = Pattern.compile("var stationsData = " + NAME_PATTERN);
    private static final String address = "http://spb.velogorod.org";
    private static final int repeatMinutes = 7;
    public static final ACrawler INSTANCE = new BikesCrawler();
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private BikesCrawler() {
        super(repeatMinutes, address, true);
    }


    @Override
    protected void processInput(InputStream is) {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(is))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                Matcher m = namePattern.matcher(inputLine);
                if (m.find()) {
                    String s = ("[" + m.group(1) + "];");
                    setChanged();
                    notifyObservers(s);
                    break;//abort reading to save some traffic
                }
            }
        } catch (IOException e) {
            logger.severe("cannot process input: " + e);
            e.printStackTrace();
        }

    }

    @Override
    protected void reportProblem(Exception e) {
        logger.severe("Error in bike thread: " + e);
    }
}
