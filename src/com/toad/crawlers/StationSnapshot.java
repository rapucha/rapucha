package com.toad.crawlers;

/**
 * Created by Morta on 23-May-15.
 */
public class StationSnapshot {
    private final String name;
    private final double lat, lon;
    private final int locks;
    private int bikes;

    public StationSnapshot(String name, double lat, double lon, int locks, int bikes) {
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
