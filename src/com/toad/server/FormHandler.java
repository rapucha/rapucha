package com.toad.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.toad.crawlers.StationCache;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.toad.crawlers.StationCache.STATION_CACHE;
import static com.toad.server.HtmlDocuments.part1;
import static com.toad.server.HtmlDocuments.part3;


/**
 * Created by Seva Nechaev "Rapucha" on 5/26/15. All rights reserved ;)
 */
class FormHandler implements HttpHandler {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public void handle(HttpExchange t) throws IOException {
        Headers h = t.getRequestHeaders();
        StringBuilder sb = new StringBuilder("Main form visited from host ");
        sb.append(t.getRemoteAddress());
        for (String s : h.keySet()) {
            sb.append("\nheader ").append(s).append(" : ").append(h.get(s));
        }
        logger.info(sb.toString());

        StringBuilder response = new StringBuilder(part1);

        STATION_CACHE.getStationNames();
        for (int i = 0; i < STATION_CACHE.getStationNames().length; i++) {
            String station = STATION_CACHE.getStationNames()[i];
            response.append("  <option value=").append(StationCache.getStationNumber(station)).append(">");
            response.append(station);
            response.append(" свободно: ");
            response.append(StationCache.getFreeBikes(station)).append("/").append(StationCache.getLocks(station));
            response.append("</option>\n");
        }
        response.append(part3);

        String cookie = CookieProvider.getCookieString();
        List<String> value = new ArrayList<>();
        value.add(cookie);
        Headers respHeaders = t.getResponseHeaders();
        respHeaders.put(CookieProvider.SET_COOKIE, value);

        byte[] reply = response.toString().getBytes("UTF-8");
        t.sendResponseHeaders(200, reply.length);
        OutputStream os = t.getResponseBody();
        os.write(reply);
        os.close();

    }
}
