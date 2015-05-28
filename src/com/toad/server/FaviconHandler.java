package com.toad.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.sf.image4j.codec.ico.ICOEncoder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 * Created by toad on 5/28/15.
 */
public class FaviconHandler implements HttpHandler {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private String strInFile = "res/Icons-Land-Points-Of-Interest-Bicycle-Green-2.ico";
    private static BufferedImage ico = null;

    public FaviconHandler() {
        java.io.File inFile = new java.io.File(strInFile);
        try {
            java.util.List<java.awt.image.BufferedImage> images = net.sf.image4j.codec.ico.ICODecoder.read(inFile);
            ico = images.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(HttpExchange t) throws IOException {

        t.sendResponseHeaders(200, 0);
        OutputStream os = t.getResponseBody();
        ICOEncoder.write(ico, os);
        os.close();
    }
}
