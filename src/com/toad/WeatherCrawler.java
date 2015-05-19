package com.toad;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Morta on 19-May-15.
 */
public class WeatherCrawler extends ACrawler {
    private final static String url = "http://api.wunderground.com/api/"+Main.WeatherAPIkey+"/conditions/q/59.935571,30.308397.json";
    private static final int repeatMinutes = 15;
    static WeatherCrawler INSTANCE = new WeatherCrawler();
    private Logger logger = Logger.getLogger(this.getClass().getName());


    private WeatherCrawler()  {
        super(repeatMinutes,url,false);
    }

    @Override
    protected void processInput(BufferedReader br) {
        String text="";
        try(BufferedReader in =new BufferedReader(new InputStreamReader(uc.getInputStream()));) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                text = text + inputLine;
            }
        } catch (IOException e){
            logger.log(Level.SEVERE,"Cannot read API response");
            e.printStackTrace();
        }

        setChanged();
        notifyObservers(text);

    }
}
