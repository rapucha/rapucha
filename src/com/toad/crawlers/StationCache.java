package com.toad.crawlers;

import com.toad.db.BikesKeeper;
import com.toad.subscription.ClientListener;

import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Created by Seva Nechaev "Rapucha" on 23-May-15. All rights reserved ;)
 */
public enum StationCache {

    STATION_CACHE;

    private static final TreeMap<String, StationSnapshot> STATIONS = new TreeMap<>();
    private static final Logger logger = Logger.getLogger(StationCache.class.getName());
    private final AtomicInteger totalBikes = new AtomicInteger();
    private final ConcurrentLinkedQueue<ClientListener> listeners = new ConcurrentLinkedQueue<>();
    private int totalBikesTemp;

    public static int getStationNumber(String name) {

        try {
            return BikesKeeper.getNumber(name);
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("Cannot render station number");
        }
        return 0;
    }

    public static int getFreeBikes(String name) {

        return STATIONS.get(name).getBikes();
    }

    public static int getFreeBikes(int number) {

        for (String name : STATIONS.keySet()) {
            int n = getStationNumber(name);
            if (n == number) {
                return STATIONS.get(name).getBikes();
            }
        }
        logger.severe("error searching station by number");
        return 0;
    }

    public static int getLocks(String name) {

        return STATIONS.get(name).getLocks();
    }

    public void updateCache(String name, double lat, double lon, int locks, int bikes, int total) {
        StationSnapshot st = STATIONS.get(name);
        if (null == st) {
            st = new StationSnapshot(name, lat, lon, locks, bikes);
            STATIONS.put(name, st);
        }
        st.setBikes(bikes);
        totalBikesTemp = totalBikesTemp + total;
    }

    public StationSnapshot getStation(String name) {
        return STATIONS.get(name);
    }

    public String[] getStationNames() {
        return STATIONS.keySet().toArray(new String[STATIONS.keySet().size()]);
    }

    synchronized public void dropCache() {
        totalBikesTemp = 0;
        totalBikes.set(0);

    }

    public void publishCache() {
        totalBikes.set(totalBikesTemp);
        notifyClientListeners();
        logger.info("Stations cache updated");
    }


    public int getTotalBikes() {
        return totalBikes.get();
    }

    public String getStationName(String number) {

        int num = Integer.parseInt(number);

        for (String name : STATIONS.keySet()) {
            int n = getStationNumber(name);
            if (n == num) {
                return name;
            }
        }
        logger.severe("error searching station by number");
        return "";

    }

    public void addClientListener(ClientListener c) {
        listeners.add(c);
        BikesCrawler.INSTANCE.setUpdateTime(2 * 60);
    }

    public void notifyClientListeners() {

        if (listeners.isEmpty()) {
            logger.info("No listeners");
            return;
        }

        listeners.stream().forEach(clientListener -> logger.info("listener present " + clientListener));
        listeners.parallelStream().forEach(clientListener -> clientListener.update(STATIONS));
        listeners.removeIf(ClientListener::isDone);
        listeners.stream().forEach(clientListener -> logger.info("listener remains " + clientListener));


        if (listeners.isEmpty()) {
            logger.info("restoring normal polling time");
            BikesCrawler.INSTANCE.setUpdateTime(7 * 60);
        }


    }

}
