package com.toad.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.toad.crawlers.StationCache;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

/**
 * Created by toad on 5/26/15.
 */
class HelloHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
        Headers h = t.getRequestHeaders();

        String qq = "";
        for (List<String> strings : h.values()) {
            qq += strings.toString();
        }
        StringBuilder response = new StringBuilder();
        if ("POST".equalsIgnoreCase(t.getRequestMethod())) {
            Properties prop = new Properties();

            prop.load(t.getRequestBody());//TODO catch every MTF thing here

            response.append("Your mail is ");
            response.append(prop.getProperty("mail"));
            response.append("\nYou want bike ");
            response.append(prop.getProperty("where"));
            response.append("\nAnd you want it ");
            response.append(prop.getProperty("when"));
            response.append("\nMeanwhile, there are " + StationCache.INSTANCE.TOTAL_BIKES_ALL_STATIONS + " bikes in the system");

            t.sendResponseHeaders(200, response.length());
        } else {
            response.append("F. Off");
            t.sendResponseHeaders(406, response.length());
        }


        OutputStream os = t.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();

    }
}
