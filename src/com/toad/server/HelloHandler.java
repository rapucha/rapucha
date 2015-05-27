package com.toad.server;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.toad.crawlers.StationCache;
import com.toad.subscription.Client;
import com.toad.subscription.Processor;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.json.Cookie;
import org.json.JSONObject;

/**
 * Created by toad on 5/26/15.
 */
class HelloHandler implements HttpHandler {
    private Logger logger = Logger.getLogger(this.getClass().getName());


    @Override
    public void handle(HttpExchange t) throws IOException {
        Headers h = t.getRequestHeaders();

        for (String s : h.keySet()) {
            System.out.println(s + " " + h.get(s));
        }
        System.out.println(t.getRemoteAddress());

        int howManyBikes = 1;//parameter reserved for future to request more that 1 bike

        StringBuilder response = new StringBuilder();

        if (requestIsValid(t)) {
            Properties prop = new Properties();
            prop.load(t.getRequestBody());//TODO catch every MTF thing here
            int minutes = convertToMinutes(prop.getProperty(HtmlDocuments.WHEN));
            Client client = new Client(minutes, prop.getProperty(HtmlDocuments.EMAIL), howManyBikes, prop.getProperty(HtmlDocuments.WHERE));
            Processor.INSTANCE.addClient(client);
        }
        response.append("\nMeanwhile, there are " + StationCache.INSTANCE.TOTAL_BIKES_ALL_STATIONS + " free bikes in the system");
        t.sendResponseHeaders(200, response.length());

        OutputStream os = t.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }

    private boolean requestIsValid(HttpExchange t) {
        List<String> cookies = t.getRequestHeaders().get(CookieProvider.COOKIE);
        if (null == cookies|| cookies.size()!=1) {
            logger.fine("no cookie or too much cookies");//FIXME is this correct?
            return false;
        }
        if (!"POST".equalsIgnoreCase(t.getRequestMethod())) {
            logger.fine("not a post request");
            return false;
        }
        if (!(t.getRequestHeaders().get("Referer").contains("http://rapucha.ru/"))) {
            logger.info("Wrong referer");
            return false;
        }
        for (String c : cookies) {
            JSONObject jo= Cookie.toJSONObject(c);
            jo.get("id");
        }
        //List<HttpCookie> cooks = HttpCookie.parse(t.getRequestHeaders());
//        Properties p = new
 //       if (!cookies.contains())
        //TODO implement cookie's life span
        return true;

    }

    private int convertToMinutes(String when) {
        int minutes = 0;
        switch (when) {
            case HtmlDocuments.NOW:
                minutes = 0;
                break;
            case HtmlDocuments.SOON:
                minutes = 1;
                break;
            case HtmlDocuments.LATER:
                minutes = 4;
                break;
            default: {
                logger.severe("Unknown information time: " + when);
                break;
            }
        }
        return minutes;
    }
}
