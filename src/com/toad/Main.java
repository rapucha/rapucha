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

import com.sun.net.httpserver.Headers;
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
        int port;
        if(args.length == 0){
            System.out.println("Pls specify port as a cl arg, ie java -xxx.jar 80");
            return;
        } else {
            port = Integer.parseInt(args[0]);
        }



        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
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
            Crawler.printFile(Crawler.accessLog, "------------------------" + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()) + "--------------------------------" + System.lineSeparator());
            Crawler.printFile(Crawler.accessLog, "Request from " + t.getRemoteAddress() + System.lineSeparator());
            long time = System.currentTimeMillis();
            try {
                Crawler.crawl();
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

class CrawlerTask extends TimerTask {

    @Override
    public void run() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        try {
            Crawler.crawl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Crawler.printFile(Crawler.dataFile, dateFormat.format(date) + Crawler.CSV_SEPARATOR + data + '\r' + '\n');
    }
}
