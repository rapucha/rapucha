package com.toad;

import java.io.*;
import java.net.InetSocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/*    /\[([^\]]+)]/ */
class Main {

    static int port;
    static String  dbuser, dbpass, dbschema, dburl, WeatherAPIkey;

    static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {

        Properties prop = new Properties();
        try(InputStream input  = new FileInputStream("config.properties");) {
            prop.load(input);
            dbuser = prop.getProperty("dbuser");
            dbpass = prop.getProperty("dbpass");
            dbschema = prop.getProperty("dbschema");
            dburl = prop.getProperty("dburl");
            port = Integer.parseInt(prop.getProperty("serverPort"));
            WeatherAPIkey = prop.getProperty("WeatherAPIkey");
        }
        catch (IOException | NumberFormatException e){
            logger.log(Level.SEVERE,"Error initializing from properties");
            e.printStackTrace();
        }

      //  HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
      //  server.createContext("/", new MyHandler());
      //  server.setExecutor(null);
        //server.start();

        TestObserver to = new TestObserver();

        WeatherCrawler.INSTANCE.addObserver(to);
        BikesCrawler.INSTANCE.addObserver(to);

        WeatherCrawler.INSTANCE.start();
        BikesCrawler.INSTANCE.start();

        while(true) {
            Thread.sleep(100);
        }
    }
    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            Crawler.printFile(Crawler.accessLog, "------------------------" + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()) + "--------------------------------" + System.lineSeparator());
            Crawler.printFile(Crawler.accessLog, "Request from " + t.getRemoteAddress() + System.lineSeparator());
            long time = System.currentTimeMillis();
            try {
                //Crawler.crawl();
            } catch (Exception e) {
                e.printStackTrace();
            }
            time = System.currentTimeMillis() - time;
            String response = "Total free bikes: "+Crawler.FREE_BIKES;
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            Headers h = t.getRequestHeaders();
            Crawler.printFile(Crawler.accessLog,"Total free bikes: "+Crawler.FREE_BIKES+System.lineSeparator());
            Crawler.printFile(Crawler.accessLog,"Processing time: "+time+System.lineSeparator());
            Crawler.printFile(Crawler.accessLog, "--=====Headers=====--"+System.lineSeparator());

            for (String s : h.keySet()) {
                Crawler.printFile(Crawler.accessLog,s+" : "+ String.valueOf(h.get(s))+System.lineSeparator());
            }
            Crawler.printFile(Crawler.accessLog, System.lineSeparator());

        }
    }

}



