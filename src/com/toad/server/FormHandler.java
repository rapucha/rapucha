package com.toad.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.toad.crawlers.StationCache.STATION_CACHE;
import static com.toad.server.HtmlDocuments.part1;
import static com.toad.server.HtmlDocuments.part3;


/**
 * Created by toad on 5/26/15.
 */
class FormHandler implements HttpHandler {


    public void handle(HttpExchange t) throws IOException {
        StringBuilder response = new StringBuilder(part1);

        STATION_CACHE.getStationNames();
        for (int i = 0; i < STATION_CACHE.getStationNames().length; i++) {
            String station = STATION_CACHE.getStationNames()[i];
            System.out.println(station);
            response.append("  <option value=" + STATION_CACHE.getStationNumber(station) + ">");
            response.append(station);
            response.append(" свободно: ");
            response.append(STATION_CACHE.getFreeBikes(station) + "/" + STATION_CACHE.getLocks(station));
            response.append("</option>\n");
        }
        response.append(part3);

        String cookie = CookieProvider.getCookieString();
        List<String> value = new ArrayList<>();
        value.add(cookie);
        Headers respHeaders = t.getResponseHeaders();
        respHeaders.put(CookieProvider.SET_COOKIE, value);

        byte[] reply = response.toString().getBytes();
        t.sendResponseHeaders(200, reply.length);
        OutputStream os = t.getResponseBody();
        os.write(reply);
        os.close();

    }
}
