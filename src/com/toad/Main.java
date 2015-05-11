package com.toad;

import java.io.*;
import java.net.InetSocketAddress;
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

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.*;

/*    /\[([^\]]+)]/ */
class Main {
    static final int SECOND = 1000;
    static final int MINUTE = 60 * SECOND;
    static final long delay = SECOND;
    static final long period =  15 * MINUTE;


    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8888), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null);
        server.start();

        Crawler.timer.scheduleAtFixedRate(new CrawlerTask(), delay, period);
        while(true) {
            Thread.sleep(100);
        }
    }
    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String response = "Total free bikes: "+Crawler.FREE_BIKES;
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}
 class Crawler{
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
     static final File file = new File("log5.csv");

     static String FREE_BIKES="unknown";
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
        try {
            try {
                PrintStream ps = new PrintStream(new FileOutputStream(Crawler.file, true), true, "UTF-8");
                ps.append(s);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

     }


    static String crawl()  {


        /**
         * disabled for a Gnome dependency, caused an error in a non-GUI Linux
         * https://java.net/jira/browse/GLASSFISH-18527
         */
        //System.setProperty("java.net.useSystemProxies", "true");
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
        int totalBikes=0;
        int stations=0;
        try {
            while ((inputLine = in.readLine()) != null){
                Matcher m = namePattern.matcher(inputLine);
                if(m.find()){
                    JSONArray jarr = new JSONArray("["+m.group(1)+"];");
                    stations = jarr.length();
                    for (int i = 0; i < jarr.length(); i++) {
                        JSONObject jsonobject = jarr.getJSONObject(i);
                        String name = jsonobject.getString(NAME);
                        double lat = jsonobject.getDouble(LAT);
                        double lon = jsonobject.getDouble(LON);
                        int locks = jsonobject.getInt(LOCKS);
                        int subTotal = jsonobject.getInt(TOTAL_BIKES);
                        totalBikes = totalBikes + subTotal;
                        //sb.append(lat);
                        //sb.append(CSV_SEPARATOR);
                        //sb.append(lon);
                        //sb.append(CSV_SEPARATOR);
                        sb.append(getNumber(name));
                        sb.append(CSV_SEPARATOR);
                        //sb.append(locks);
                        //sb.append(CSV_SEPARATOR);
                        sb.append(subTotal);
                        sb.append(CSV_SEPARATOR);
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //sb.append(stations);
        //sb.append(CSV_SEPARATOR);
        sb.append(totalBikes);
        FREE_BIKES = ""+totalBikes;
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

     private static String getNumber(String name) {
         Matcher m = Crawler.numberPattern.matcher(name);
         if(m.find()){
             m.toString();
             return m.group();
         } else return null; //throw some exception
     }

 }
class CrawlerTask extends TimerTask {

    @Override
    public void run() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        String data = Crawler.crawl();
        System.out.println(data);
        Crawler.printFile(dateFormat.format(date)+Crawler.CSV_SEPARATOR+data+'\r'+'\n');
    }
}
