package com.toad;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.toad.crawlers.BikesCrawler;
import com.toad.crawlers.WeatherCrawler;
import com.toad.observers.BikesObserver;
import com.toad.observers.WeatherObserver;

/*    /\[([^\]]+)]/ */
public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {

    SettingsManager.INSTANCE.loadDefaultSettings();

      //  HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
      //  server.createContext("/", new MyHandler());
      //  server.setExecutor(null);
        //server.start();

        WeatherObserver wo = new WeatherObserver();
        BikesObserver bo = new BikesObserver();
        WeatherCrawler.INSTANCE.addObserver(wo);
        BikesCrawler.INSTANCE.addObserver(bo);

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



