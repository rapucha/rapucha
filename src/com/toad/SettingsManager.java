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
    public static String dburl;
    public static String dbschema;
    public static String dbpass;
    public static String dbuser;
    public static int port;
    public static SettingsManager INSTANCE = new SettingsManager();
    Properties prop = new Properties();
    private Logger logger = Logger.getLogger(this.getClass().getName());

    private SettingsManager() {

    }

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
