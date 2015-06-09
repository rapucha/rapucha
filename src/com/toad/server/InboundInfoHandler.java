package com.toad.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.toad.SettingsManager;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by Seva Nechaev "Rapucha" on 5/28/15. All rights reserved ;)
 */
class InboundInfoHandler implements HttpHandler {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final String strInFile = SettingsManager.bgimage;

    public InboundInfoHandler() {

    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        /*
        String reply
        t.sendResponseHeaders(200, reply.length);
        OutputStream os = t.getResponseBody();
        os.write(reply);
        os.close();
        */
    }
}
