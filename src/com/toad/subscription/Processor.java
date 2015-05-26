package com.toad.subscription;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.DelayQueue;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Created by Morta on 24-May-15.
 */
public enum Processor {
    INSTANCE;
    final DelayQueue<Client> queue = new DelayQueue<>();
    private Logger logger = Logger.getLogger(this.getClass().getName());
    Consumer<Client> clientConsumer = (c) -> process();

//http://stackoverflow.com/questions/21163108/custom-thread-pool-in-java-8-parallel-stream/22269778#22269778

    public void start() {
        System.out.println("started");
        queue.parallelStream().forEach(clientConsumer);
        System.out.println("ended");
    }

    private void process() {
 //       try {
            Client c = queue.poll();
            YMailer mailer = new YMailer();
            mailer.send(c.getEmail(),c.getAtWhatStation(),c.getHowManyBikes());
  //      }
     /*   catch (InterruptedException e) {
            logger.info("Processing the client was interrupted " + e);
            Thread.currentThread().interrupt();
        } */

    }

    public void addClient(Client client) {
        logger.info("client added at "+client.getWhenNotify());
        queue.offer(client);
        start();
    }


}
