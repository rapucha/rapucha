package com.toad;

import javax.mail.internet.InternetAddress;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;
import java.time.*;

/**
 * Created by Morta on 24-May-15.
 */
public enum SubscriptionProcessor {
    INSTANCE;

    private static final ConcurrentHashMap clients = new ConcurrentHashMap();

    final DelayQueue<Client> queue = new DelayQueue<>();

    public void start(){
        ExecutorService service = Executors.newCachedThreadPool();
        while(true) {
            service.submit( () -> {
                    try {
                      Client client =  queue.take();
                        System.out.println(Instant.now().toString()+ "Sending to "+client);
                       Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            });
        }
    }

    public void addClient(Client client){
            queue.put(client);
    }

    public void notifySubcribers(){


        for (String s : StationCache.INSTANCE.getStationNames()) {

        }
    }
    private static final class Client implements Delayed{
        private  final Instant whenCreated,whenNotify;
        private final InternetAddress email;
        private final int howManyBikes;
        private final String atWhatStation;

        public Client(Instant whenCreated, Duration howLong, InternetAddress email, int howManyBikes, String atWhatStation) {
            this.whenCreated = whenCreated;
            whenNotify = whenCreated.plus(howLong);
            this.email = email;
            this.howManyBikes = howManyBikes;
            this.atWhatStation = atWhatStation;
        }

        public Instant getWhenCreated() {
            return whenCreated;
        }

        public Instant getWhenNotify() {
            return whenNotify;
        }

        public InternetAddress getEmail() {
            return email;
        }

        public int getHowManyBikes() {
            return howManyBikes;
        }

        public String getAtWhatStation() {
            return atWhatStation;
        }

        @Override
        public String toString() {
            return "Client{" +
                    "whenCreated=" + whenCreated +
                    ", whenNotify=" + whenNotify +
                    ", email=" + email +
                    ", howManyBikes=" + howManyBikes +
                    ", atWhatStation='" + atWhatStation + '\'' +
                    '}';
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(ChronoUnit.SECONDS.between(Instant.now(),whenNotify),TimeUnit.SECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
        Client cl = (Client)o;
        long toLapse= ChronoUnit.SECONDS.between(whenNotify, cl.whenNotify);
            if(toLapse < 0){
                return -1;
            }
            if(toLapse > 0){
                return 1;
            }
            return 0;
        }
    }
}
