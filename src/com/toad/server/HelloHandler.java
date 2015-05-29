package com.toad.server;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.toad.Util;
import com.toad.crawlers.StationCache;
import com.toad.subscription.Client;
import com.toad.subscription.Processor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static com.toad.crawlers.StationCache.STATION_CACHE;

/**
 * Created by toad on 5/26/15.
 */
class HelloHandler implements HttpHandler {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private String customMessage = "";

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
            try {
                prop.load(t.getRequestBody());
            } catch (Exception e) {
                logger.severe("Error parsing parameters from main HTML form " + Util.isToString(t.getRequestBody()));
            }
            int minutes = convertToMinutes(prop.getProperty(HtmlDocuments.WHEN));
            Client client = new Client(minutes, prop.getProperty(HtmlDocuments.EMAIL), howManyBikes, STATION_CACHE.getStationName(prop.getProperty(HtmlDocuments.WHERE)));
            Processor.INSTANCE.addClient(client);
        }
        response.append(customMessage);
        response.append("\nMeanwhile, there are " + StationCache.STATION_CACHE.getTotalBikes() + " free bikes in the system");
        t.sendResponseHeaders(200, response.length());

        OutputStream os = t.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }

    private boolean requestIsValid(HttpExchange t) {
        List<String> cookies = t.getRequestHeaders().get(CookieProvider.COOKIE);
        if (null == cookies) {
            logger.fine("no cookie ");
            customMessage = "no cookie ";
            return false;
        }
        if (!"POST".equalsIgnoreCase(t.getRequestMethod())) {
            logger.fine("not a post request");
            customMessage = "not a post request";
            return false;
        }
//        if (somebodyIsTooFast(t) ){ // check the combination of address,user-agent and X-Forward
//            return false;
//        }
        List<String> referers = t.getRequestHeaders().get("Referer");
        if (!(referers.contains("http://rapucha.ru/") || (referers.contains("http://localhost/")))) {
            logger.info("Wrong referer: ");
            t.getRequestHeaders().get("Referer").forEach(logger::fine);
            customMessage = "Wrong referer: ";
            return false;
        }

        List<String> result = cookies.stream().filter(cookie -> cookie.startsWith("id=")).collect(Collectors.toList());
        if (result.size() > 1) {
            logger.info("Cookies size is >1 : " + result.size());
        }
        customMessage = CookieProvider.cookieIsGood(result.get(0));
        return "ok".equals(customMessage);

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
