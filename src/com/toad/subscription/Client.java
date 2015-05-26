package com.toad.subscription;

import javax.mail.internet.InternetAddress;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by toad on 5/26/15.
 */
final class Client implements Delayed {
    private final Instant whenCreated, whenNotify;
    private final InternetAddress email;
    private final int howManyBikes;
    private final String atWhatStation;
    private Logger logger = Logger.getLogger(this.getClass().getName());

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
        return unit.convert(ChronoUnit.SECONDS.between(Instant.now(), whenNotify), TimeUnit.SECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        Client cl = (Client) o;
        long toLapse = ChronoUnit.SECONDS.between(whenNotify, cl.whenNotify);
        if (toLapse < 0) {
            return -1;
        }
        if (toLapse > 0) {
            return 1;
        }
        return 0;
    }
}
