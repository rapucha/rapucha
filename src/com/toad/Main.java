package com.toad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.*;

/*    /\[([^\]]+)]/ */
class Main {


    public static void main(String[] args) throws Exception {
        Crawler.timer.scheduleAtFixedRate(new CrawlerTask(), 500, 600); //delay in milliseconds
        while(true) {
            Thread.sleep(100);
        }
    }

}
 class Crawler{
     static Timer timer = new Timer();
     static final String PATTERN = "\\[([^\\]]+)];";
     public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";


    static void crawl() throws IOException {
        Pattern pattern = Pattern.compile("var stationsData = "+ PATTERN);
        System.setProperty("java.net.useSystemProxies", "true");
        URL velo = null;
        try {
            velo = new URL("http://spb.velogorod.org/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection uc = null;
        try {
            uc = velo.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        uc.setRequestProperty("User-Agent", USER_AGENT);
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    uc.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String inputLine;
        int total=0;
        while ((inputLine = in.readLine()) != null){
            Matcher m = pattern.matcher(inputLine);
            if(m.find()){
                JSONArray jarr = new JSONArray("["+m.group(1)+"];");
                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject jsonobject = jarr.getJSONObject(i);
                    String name = jsonobject.getString("Name");
                    int subTotal = jsonobject.getInt("TotalAvailableBikes");
                    total = total + subTotal;
                    System.out.println(name +" "+subTotal);
                }

            }
        }
        System.out.println(total);
        in.close();

    }

 }
class CrawlerTask extends TimerTask {

    @Override
    public void run() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        //Crawler.timer.cancel();

    }
}
