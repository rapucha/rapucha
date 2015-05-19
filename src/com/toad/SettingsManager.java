package com.toad;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Morta on 19-May-15.
 */
public class SettingsManager {
    public static String WeatherAPIkey;
    private static String dburl;
    private static String dbschema;
    private static String dbpass;
    private static String  dbuser;
    private static int port;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    Properties prop = new Properties();

    private SettingsManager(){

    }
    public static SettingsManager INSTANCE = new SettingsManager();

    public void loadDefaultSettings() {
        loadSettings("config.properties");

    }
    public void loadSettings(String file) {

        try (InputStream input = new FileInputStream(file);) {
            prop.load(input);
            dbuser = prop.getProperty("dbuser");
            dbpass = prop.getProperty("dbpass");
            dbschema = prop.getProperty("dbschema");
            dburl = prop.getProperty("dburl");
            port = Integer.parseInt(prop.getProperty("serverPort"));
            WeatherAPIkey = prop.getProperty("WeatherAPIkey");
        } catch (IOException | NumberFormatException e) {
            logger.log(Level.SEVERE, "Error initializing from properties");
            e.printStackTrace();
        }

    }
}
