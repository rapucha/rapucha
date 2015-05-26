package com.toad.server;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.toad.crawlers.StationCache;
import com.toad.subscription.Client;
import com.toad.subscription.Processor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by toad on 5/26/15.
 */
class HelloHandler implements HttpHandler {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void handle(HttpExchange t) throws IOException {
        Headers h = t.getRequestHeaders();

        int howManyBikes = 1;

        StringBuilder response = new StringBuilder();
        if ("POST".equalsIgnoreCase(t.getRequestMethod())) {
            Properties prop = new Properties();
            prop.load(t.getRequestBody());//TODO catch every MTF thing here
            int minutes = convertToMinutes(prop.getProperty("when"));
            Client client = new Client(minutes, prop.getProperty("mail"), howManyBikes, prop.getProperty("where"));
            Processor.INSTANCE.addClient(client);
            response.append("\nMeanwhile, there are " + StationCache.INSTANCE.TOTAL_BIKES_ALL_STATIONS + " free bikes in the system");
            t.sendResponseHeaders(200, response.length());
        } else {
            response.append("F. Off");
            t.sendResponseHeaders(406, response.length());
        }
        OutputStream os = t.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }

    private int convertToMinutes(String when) {
        int minutes = 0;
        switch (when) {
            case "Now":
                minutes = 0;
                break;
            case "Soon":
                minutes = 1;
                break;
            case "Loon":
                minutes = 4;
                break;
            default: {
                logger.severe("Unknown when string: " + when);
                break;
            }
        }
        return minutes;
    }
}
