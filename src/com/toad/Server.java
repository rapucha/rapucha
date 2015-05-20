package com.toad;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Morta on 21-May-15.
 */
public class Server {

    //  HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
    //  server.createContext("/", new MyHandler());
    //  server.setExecutor(null);
    //server.start();

    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            //Util.printFile(Crawler.accessLog, "------------------------" + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()) + "--------------------------------" + System.lineSeparator());
            //Util.printFile(Crawler.accessLog, "Request from " + t.getRemoteAddress() + System.lineSeparator());
            long time = System.currentTimeMillis();
            try {
                //Crawler.crawl();
            } catch (Exception e) {
                e.printStackTrace();
            }
            time = System.currentTimeMillis() - time;
            //String response = "Total free bikes: "+Crawler.FREE_BIKES;
            //t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            //os.write(response.getBytes());
            os.close();
            Headers h = t.getRequestHeaders();
           // Util.printFile(Crawler.accessLog, "Total free bikes: " + Crawler.FREE_BIKES + System.lineSeparator());
            //Util.printFile(Crawler.accessLog, "Processing time: " + time + System.lineSeparator());
            //Util.printFile(Crawler.accessLog, "--=====Headers=====--" + System.lineSeparator());

            for (String s : h.keySet()) {
            //    Util.printFile(Crawler.accessLog, s + " : " + String.valueOf(h.get(s)) + System.lineSeparator());
            }
        //    Util.printFile(Crawler.accessLog, System.lineSeparator());

        }
    }

}
