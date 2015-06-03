package com.toad.subscription;


import com.toad.crawlers.StationCache;
import com.toad.crawlers.StationSnapshot;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by toad on 5/26/15.
 */
public final class Client implements Delayed, ClientListener {//TODO decompose, add LisenListener field instead
    private final Instant whenCreated, whenNotify;
    private final String email;
    private final int howManyBikes;
    private final List<StationSnapshot> atWhatStations;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    final ExecutorService executorService = Executors.newFixedThreadPool(1);
    private boolean done;

    public Client(int minutes, String email, int howManyBikes, List<String> names) {
        whenCreated = Instant.now();
        whenNotify = whenCreated.plus(Duration.ofMinutes(minutes));
        this.email = email;
        this.howManyBikes = howManyBikes;
        atWhatStations =
                names.stream().map(s -> StationCache.STATION_CACHE.getStation(s)).collect(Collectors.toList());
    }

    public Instant getWhenCreated() {
        return whenCreated;
    }

    public Instant getWhenNotify() {
        return whenNotify;
    }

    public String getEmail() {
        return email;
    }

    public int getHowManyBikes() {
        return howManyBikes;
    }

    public List<String> getAtWhatStations() {
        return atWhatStations.stream().map(stationSnapshot -> stationSnapshot.getName()).collect(Collectors.toList());

    }

    @Override
    public String toString() {
        return "Client{" +
                "whenCreated=" + whenCreated +
                ", whenNotify=" + whenNotify +
                ", email=" + email +
                ", howManyBikes=" + howManyBikes +
                ", atWhatStation='" + atWhatStations + '\'' +
                '}';
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long delay = unit.convert(ChronoUnit.SECONDS.between(Instant.now(), whenNotify), TimeUnit.SECONDS);
        logger.info("For client" + this + "Delay is " + TimeUnit.SECONDS.convert(delay, TimeUnit.NANOSECONDS));
        return delay;
    }

    @Override
    public int compareTo(Delayed o) {
        Client cl = (Client) o;
        long toLapse = ChronoUnit.SECONDS.between(whenNotify, cl.whenNotify);
        logger.info("To lapse is " + toLapse);
        if (toLapse < 0) {
            return -1;
        }
        if (toLapse > 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public void update(TreeMap<String, StationSnapshot> m) {
        int sumOfBikes = 0;
        for (StationSnapshot atWhatStation : atWhatStations) {
            StationSnapshot st = m.get(atWhatStation.getName());
            sumOfBikes = sumOfBikes + st.getBikes();
        }

        if (sumOfBikes >= howManyBikes) {
            YMailer mailer = new YMailer();//TODO make mailer static
            logger.info("Submitting mail. ");
            executorService.submit(() -> mailer.send(getEmail(), atWhatStations,howManyBikes));
            setDone();

        }
    }

    @Override
    public boolean isDone() {
        return done;

    }


    @Override
    public void setDone() {
        done = true;

    }
}
