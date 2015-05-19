package com.toad;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Morta on 19-May-15.
 */
public class BikesCrawler extends ACrawler {
    private final static String url = "http://spb.velogorod.org";
    private static final int repeatMinutes = 10;
    static BikesCrawler INSTANCE = new BikesCrawler();

    static final String NAME_PATTERN = "\\[([^\\]]+)];";
    static final Pattern namePattern = Pattern.compile("var stationsData = "+ NAME_PATTERN);

    private BikesCrawler()  {
        super(repeatMinutes,url,true);
    }

    @Override
    protected void processInput(BufferedReader br) {

        JSONArray bikesStatus= new JSONArray();
        Timestamp ts = new Timestamp(new java.util.Date().getTime());
        try(BufferedReader in =new BufferedReader(new InputStreamReader(uc.getInputStream()));) {
            String inputLine;
            while ((inputLine = in.readLine()) != null){
                Matcher m = namePattern.matcher(inputLine);
                if(m.find()){
                    String s = ("[" + m.group(1) + "];");
                    setChanged();
                    notifyObservers(s);
                    break;//abort reading to save some traffic
                }

            }
        }

         catch (IOException e) {
            e.printStackTrace();
        }

    }
}
