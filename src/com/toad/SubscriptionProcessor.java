package com.toad;

import javax.mail.internet.InternetAddress;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.time.*;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by Morta on 24-May-15.
 */
public enum SubscriptionProcessor {
    INSTANCE;

    private static final ConcurrentHashMap clients = new ConcurrentHashMap();

    //DelayQueue<>

    public void start(){
        //Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(,0,60, TimeUnit.SECONDS);
    }

    public void addClient(Client client){
            //clients.
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
