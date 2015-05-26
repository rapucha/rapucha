package com.toad.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import static com.toad.SettingsManager.port;


/**
 * Created by Morta on 21-May-15.
 */
public class Server {

    private static Logger logger = Logger.getLogger(Server.class.getName());
    public final static Server INSTANCE = new Server();
    private static HttpServer httpServer;


    private Server() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            httpServer.createContext("/", new FormHandler());
            httpServer.createContext("/hello", new HelloHandler());
            httpServer.setExecutor(Executors.newCachedThreadPool());
            logger.info("HTTP com.toad.server created");

        } catch (IOException e) {
            logger.severe("Cannot create http server on port " + port);
            e.printStackTrace();
        }
    }

    public static void start() {
        logger.info("HTTP server started");
        httpServer.start();
    }

}
