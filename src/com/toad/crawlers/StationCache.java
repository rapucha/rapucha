package com.toad.crawlers;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Morta on 23-May-15.
 */
public enum StationCache {

    INSTANCE;


    private static final HashMap<String, Station> STATIONS = new HashMap<String, Station>();
    private  int totalBikesTemp;
    private  AtomicInteger totalBikes = new AtomicInteger();

    public void updateCache(String name, double lat, double lon, int locks, int bikes, int total) {
        Station st = STATIONS.get(name);
        if (null == st) {
            st = new Station(name, lat, lon, locks, bikes);
            STATIONS.put(name, st);
        }
        st.setBikes(bikes);
        totalBikesTemp = totalBikesTemp + total;
    }


    public String[] getStationNames() {
        return STATIONS.keySet().toArray(new String[STATIONS.keySet().size()]);
    }

     synchronized public void dropCache() {
         totalBikesTemp = 0;
         totalBikes.set(0);

    }

    public void publishCache() {
        totalBikes.set(totalBikesTemp);;
    }


     public int getTotalBikes() {
        return totalBikes.get();
    }

    /**
     * Created by Morta on 23-May-15.
     */
    public static class Station {
        private final String name;
        private final double lat, lon;
        private final int locks;
        private int bikes;

        public Station(String name, double lat, double lon, int locks, int bikes) {
            this.name = name;
            this.lat = lat;
            this.lon = lon;
            this.locks = locks;
            this.bikes = bikes;
        }

        public String getName() {
            return name;
        }

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }

        public int getLocks() {
            return locks;
        }

        public int getBikes() {
            return bikes;
        }

        public void setBikes(int n) {
            bikes = n;
        }

    }
}
