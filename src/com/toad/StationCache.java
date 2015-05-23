package com.toad;

import java.util.HashMap;

/**
 * Created by Morta on 23-May-15.
 */
public final class StationCache {

    private static final HashMap<String,Station> STATIONS = new HashMap<String,Station>();
    private static int TOTAL_BIKES_ALL_STATIONS;

    public static void updateCache(String name, int bikes, int total){
        Station st = STATIONS.get(name);
        st.setBikes(bikes);
        TOTAL_BIKES_ALL_STATIONS = total;
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
