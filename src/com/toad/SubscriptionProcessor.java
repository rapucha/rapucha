package com.toad;

import javax.mail.internet.InternetAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.time.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Morta on 24-May-15.
 */
public enum SubscriptionProcessor {
    INSTANCE;

    private static final ConcurrentHashMap clients = new ConcurrentHashMap();

    public void start(){
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

            }
        },0,60, TimeUnit.SECONDS);
    }

    public void addClient(Client client){
            //clients.p
    }

    private static final class Client{
        private  final Instant whenCreated;
        private final Duration whenNotify;
        private final InternetAddress email;
        private final int howManyBikes;
        private final String atWhatStation;

        public Client(Instant whenCreated, Duration whenNotify, InternetAddress email, int howManyBikes, String atWhatStation) {
            this.whenCreated = whenCreated;
            this.whenNotify = whenNotify;
            this.email = email;
            this.howManyBikes = howManyBikes;
            this.atWhatStation = atWhatStation;
        }

        public Instant getWhenCreated() {
            return whenCreated;
        }

        public Duration getWhenNotify() {
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
    }
}
