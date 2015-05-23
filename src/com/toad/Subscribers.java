package com.toad;

import javax.mail.internet.InternetAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.time.*;
/**
 * Created by Morta on 24-May-15.
 */
public class Subscribers {
    private static final ConcurrentHashMap clients = new ConcurrentHashMap();

    public void addClient(Client client){

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
