package com.toad.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.toad.server.HtmlDocuments.mainForm;

/**
 * Created by toad on 5/26/15.
 */
class FormHandler implements HttpHandler {


    public void handle(HttpExchange t) throws IOException {
        String response = mainForm;
        String cookie = CookieProvider.getCookieString();
        List<String> value = new ArrayList<String>();
        value.add(cookie);
        Headers respHeaders = t.getResponseHeaders();
        respHeaders.put(CookieProvider.SET_COOKIE, value);
        t.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }
}
