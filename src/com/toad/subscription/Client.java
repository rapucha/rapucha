package com.toad.subscription;


import com.sun.istack.internal.NotNull;
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
 * Created by Seva Nechaev "Rapucha" on 5/26/15. All rights reserved ;)
 */
public final class Client implements Delayed, ClientListener {
    private final Instant whenCreated, whenNotify;
    private final String email;
    private final int howManyBikes;
    private final List<SimpleStation> atWhatStations;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);
    private boolean done;

    private Client(int minutes, String email, int howManyBikes, List<String> names) {
        whenCreated = Instant.now();
        whenNotify = whenCreated.plus(Duration.ofMinutes(minutes));
        this.email = email;
        this.howManyBikes = howManyBikes;
        atWhatStations =
                names.stream().map(s -> new SimpleStation(StationCache.STATION_CACHE.getStation(s))).collect(Collectors.toList());
    }

    public static Client createRegularClient(int delay, String email, int howmanyBikes, List<String> stationNames) {

        return new Client(delay, email, howmanyBikes, stationNames);
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
        return atWhatStations.stream().map(stationSnapshot -> stationSnapshot.name).collect(Collectors.toList());

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
    public long getDelay(@NotNull TimeUnit unit) {
        long delay = unit.convert(ChronoUnit.SECONDS.between(Instant.now(), whenNotify), TimeUnit.SECONDS);
        logger.info("For client" + this + "Delay is " + TimeUnit.SECONDS.convert(delay, TimeUnit.NANOSECONDS));
        return delay;
    }

    @Override
    public int compareTo(@NotNull Delayed o) {
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

        int sumOfBikes = atWhatStations.stream().
                map(simpleStation -> m.get(simpleStation.name)).
                mapToInt(StationSnapshot::getBikes).sum();

        if (sumOfBikes >= howManyBikes) {

            logger.info("Submitting mail. ");
            executorService.submit(() -> YMailer.sendClientNotification(getEmail(), atWhatStations, howManyBikes));
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
