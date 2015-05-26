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
public enum Server {
    INSTANCE;
    private static Logger logger = Logger.getLogger(Server.class.getName());

    private static HttpServer httpServer;


    public static void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext("/", new FormHandler());
        httpServer.createContext("/hello", new HelloHandler());
        httpServer.setExecutor(Executors.newCachedThreadPool());
        logger.info("HTTP com.toad.server created");

        logger.info("HTTP server started");
        httpServer.start();
    }

}
