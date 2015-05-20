package com.toad.crawlers;

    import com.toad.SettingsManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Morta on 19-May-15.
 */
public class WeatherCrawler extends ACrawler {
    private static final String url = "http://api.wunderground.com/api/"+ SettingsManager.WeatherAPIkey+"/conditions/q/59.935571,30.308397.json";
    private static final int repeatMinutes = 5;
    public static WeatherCrawler INSTANCE = new WeatherCrawler();
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
