package com.toad;

import java.io.*;
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
    static int SECOND = 1000;
    static int MINUTE = 60 * SECOND;
    static long delay = SECOND;
    static long period =   MINUTE/10;
    public static void main(String[] args) throws Exception {
        Crawler.timer.scheduleAtFixedRate(new CrawlerTask(), delay, period);
        while(true) {
            Thread.sleep(100);
        }
    }

}
 class Crawler{
     static Timer timer = new Timer();
     static final String PATTERN = "\\[([^\\]]+)];";
     public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
     static final File file = new File("log.txt");

    Crawler(){
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    static void printFile (String s){
         try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Crawler.file, true)))) {
             out.println(s);
         }catch (IOException e) {
             e.printStackTrace();
         }
     }

    static String crawl()  {
        Pattern pattern = Pattern.compile("var stationsData = "+ PATTERN);
        System.setProperty("java.net.useSystemProxies", "true");
        URL velo = null;
        StringBuilder sb = new StringBuilder();
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
        try {
            while ((inputLine = in.readLine()) != null){
                Matcher m = pattern.matcher(inputLine);
                if(m.find()){
                    JSONArray jarr = new JSONArray("["+m.group(1)+"];");
                    for (int i = 0; i < jarr.length(); i++) {
                        JSONObject jsonobject = jarr.getJSONObject(i);
                        String name = jsonobject.getString("Name");
                        int subTotal = jsonobject.getInt("TotalAvailableBikes");
                        total = total + subTotal;
                        sb.append(name);
                        sb.append(';');
                        sb.append(subTotal);
                        sb.append(';');
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        sb.append(total);
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

 }
class CrawlerTask extends TimerTask {

    @Override
    public void run() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        String data = Crawler.crawl();
        Crawler.printFile(dateFormat.format(date)+';'+data);
    }
}
