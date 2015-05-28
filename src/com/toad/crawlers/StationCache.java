package com.toad.crawlers;

import com.toad.db.BikesKeeper;

import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Created by Morta on 23-May-15.
 */
public enum StationCache {

    STATION_CACHE;


    private static final TreeMap<String, StationSnapshot> STATIONS = new TreeMap<>();
    private int totalBikesTemp;
    private final AtomicInteger totalBikes = new AtomicInteger();
    private static final Logger logger = Logger.getLogger(StationCache.class.getName());

    public void updateCache(String name, double lat, double lon, int locks, int bikes, int total) {
        StationSnapshot st = STATIONS.get(name);
        if (null == st) {
            st = new StationSnapshot(name, lat, lon, locks, bikes);
            STATIONS.put(name, st);
        }
        st.setBikes(bikes);
        totalBikesTemp = totalBikesTemp + total;
    }


    public String[] getStationNames() {
        return STATIONS.keySet().toArray(new String[STATIONS.keySet().size()]);
    }

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

    public static int getLocks(String name) {

        return STATIONS.get(name).getLocks();
    }


    synchronized public void dropCache() {
        totalBikesTemp = 0;
        totalBikes.set(0);

    }

    public void publishCache() {
        totalBikes.set(totalBikesTemp);
    }


    public int getTotalBikes() {
        return totalBikes.get();
    }

}
