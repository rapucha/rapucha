package com.toad.crawlers;

/**
 * Created by Seva Nechaev "Rapucha" on 23-May-15. All rights reserved ;)
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

    @Override
    public String toString() {
        return "StationSnapshot{" +
                "number='" + name.substring(0, 1) + '\'' +
                ", bikes=" + bikes +
                '}';
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
