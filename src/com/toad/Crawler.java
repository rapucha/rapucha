package com.toad;

import org.json.JSONArray;
import org.json.JSONML;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* Created by Morta on 17-May-15.
*/
class Crawler extends AbstarctCrawler{
    private static final String VELOGOROD = "http://spb.velogorod.org/";
    static Timer timer = new Timer();
    static final String NAME_PATTERN = "\\[([^\\]]+)];";
    static final Pattern namePattern = Pattern.compile("var stationsData = "+ NAME_PATTERN);
    static final String NUMBER_PATTERN = "[0-9][0-9]{1,3}\\.";
    static final Pattern numberPattern = Pattern.compile(NUMBER_PATTERN);
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
    public static final char CSV_SEPARATOR = '\t';
    private static final String NAME = "Name";
    private static final String LAT="Latitude";
    private static final String LON="Longitude";
    private static final String LOCKS="TotalLocks";
    private static final String TOTAL_BIKES ="TotalAvailableBikes";
    static final File dataFile = new File("datalog.csv");
    static final File accessLog = new File("accesslog.csv");

    static String FREE_BIKES="unknown";

   static void printFile (File f, String s){

       try (PrintStream ps = new PrintStream(new FileOutputStream(f, true), true, "UTF-8")) {
           ps.append(s);
           ps.close();
       } catch (IOException e) {
           e.printStackTrace();
       }
    }


   static void crawl() throws Exception {


       /**
        * disabled for a Gnome dependency, caused an error in a non-GUI Linux
        * https://java.net/jira/browse/GLASSFISH-18527
        */
       //System.setProperty("java.net.useSystemProxies", "true");
       URL velo = null;
       StringBuilder sb = new StringBuilder();
       try {
           velo = new URL(VELOGOROD);
       } catch (MalformedURLException e) {
           e.printStackTrace();
       }
       URLConnection uc = null;
       try {
           uc = velo.openConnection();
       } catch (IOException e) {
           e.printStackTrace();
       }
       uc.setRequestProperty("User-Agent", generateUserAgent());
       BufferedReader in = null;
       try {
           in = new BufferedReader(new InputStreamReader(
                   uc.getInputStream()));
       } catch (IOException e) {
           e.printStackTrace();
       }
       String inputLine;
       int totalBikes=0;
       JSONArray bikesStatus= new JSONArray();
       Timestamp ts = new Timestamp(new java.util.Date().getTime());
       try {
           while ((inputLine = in.readLine()) != null){
               Matcher m = namePattern.matcher(inputLine);
               if(m.find()){
                   JSONArray jarr = new JSONArray("["+m.group(1)+"];");
                   for (int i = 0; i < jarr.length(); i++) {
                       JSONObject jsonobject = jarr.getJSONObject(i);
                       String name = jsonobject.getString(NAME);
                       double lat = jsonobject.getDouble(LAT);
                       double lon = jsonobject.getDouble(LON);
                       int locks = jsonobject.getInt(LOCKS);
                       int subTotal = jsonobject.getInt(TOTAL_BIKES);
                       totalBikes = totalBikes + subTotal;
                       DBUpdater.updateStations(name, lat, lon, locks, subTotal, ts);
                       JSONObject id = new JSONObject().put("id",getNumber(name));
                       JSONObject bikes = new JSONObject().put("bikes",subTotal);
                       JSONArray station = new JSONArray();
                       station.put(id);
                       station.put(bikes);
                       bikesStatus.put(station);
                   }
                   break;
               }
           }
           //DBUpdater.updateBikesHistory(bikesStatus, ts);
       } catch (IOException e) {
           e.printStackTrace();
       }
       FREE_BIKES = ""+totalBikes;
       System.out.println(FREE_BIKES);
       try {
           in.close();
       } catch (IOException e) {
           e.printStackTrace();
       }

       URL weather = new URL("http://api.wunderground.com/weatherstation/WXDailyHistory.asp?ID=I966&format=XML");
       uc = weather.openConnection();
       uc.setRequestProperty("User-Agent", generateUserAgent());
           String inp, text = "";
           try (BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));) {

               while ((inp = br.readLine()) != null) {
                   text = text + inp;
               }
           }

       JSONArray jarr = JSONML.toJSONArray(text);
       JSONArray last = jarr.getJSONArray(jarr.length() - 1);
       JSONArray pure = new JSONArray();
       pure.put(last.getJSONArray(8));
       pure.put(last.getJSONArray(12));
       pure.put(last.getJSONArray(13));
       pure.put(last.getJSONArray(14));
       pure.put(last.getJSONArray(15));
       pure.put(last.getJSONArray(16));
       pure.put(last.getJSONArray(20));
       pure.put(last.getJSONArray(24));

       DBUpdater.updateBikesHistory(bikesStatus, pure, ts);

   }

    private static String generateUserAgent() {
        return USER_AGENT; //TODO randomly select from several User-Agent strings
    }

     static int getNumber(String name) throws Exception {
        Matcher m = Crawler.numberPattern.matcher(name);
        if (m.find()){
            String number = (m.group().replaceFirst("^0+(?!$)", "").replace(".",""));//remove leading zeros and all dots

            return Integer.parseInt(number);
        } else {
            throw new Exception("No number found in station name "+name);
        }
    }

}