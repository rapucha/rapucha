package com.toad.subscription;

import com.toad.crawlers.BikesCrawler;
import com.toad.crawlers.StationCache;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Created by Morta on 24-May-15.
 */
public enum Processor {
    INSTANCE;
    private final Logger logger = Logger.getLogger(this.getClass().getName());


    final DelayQueue<Client> queue = new DelayQueue<>();
    final ExecutorService executorService = Executors.newCachedThreadPool();


//http://stackoverflow.com/questions/21163108/custom-thread-pool-in-java-8-parallel-stream/22269778#22269778

    private void process() {
        try {
            logger.info("processing..");
            Client c = queue.take();//blocking
            logger.info("Client taken from queue.. " + c);
            BikesCrawler.INSTANCE.setUpdateTime(1);
            StationCache.STATION_CACHE.addClientListener(c);
        } catch (Exception e) {
            logger.info("Processing the client was interrupted " + e);
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public void addClient(Client client) {
        logger.info("client added at " + client.getWhenNotify());
        queue.offer(client);
        executorService.submit(this::process);
    }

}
