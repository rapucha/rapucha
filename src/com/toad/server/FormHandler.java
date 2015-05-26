package com.toad.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

import static com.toad.server.HtmlDocuments.mainForm;

/**
 * Created by toad on 5/26/15.
 */
class FormHandler implements HttpHandler {
    public void handle(HttpExchange t) throws IOException {
        //Util.printFile(Crawler.accessLog, "------------------------" + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()) + "--------------------------------" + System.lineSeparator());
        //Util.printFile(Crawler.accessLog, "Request from " + t.getRemoteAddress() + System.lineSeparator());
        //Headers h = t.getRequestHeaders();
        String response = mainForm;
        t.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();

        // Util.printFile(Crawler.accessLog, "Total free bikes: " + Crawler.FREE_BIKES + System.lineSeparator());
        //Util.printFile(Crawler.accessLog, "Processing time: " + time + System.lineSeparator());
        //Util.printFile(Crawler.accessLog, "--=====Headers=====--" + System.lineSeparator());


    }
}
