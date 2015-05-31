package com.toad.crawlers;

import com.toad.db.BikesKeeper;
import com.toad.subscription.ClientListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    private List<ClientListener> listeners = new ArrayList<>();

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
    }

    synchronized public void removeClientListener(ClientListener c) {
        listeners.remove(c);
    }

    public void notifyClientListeners() {
        listeners.stream().forEach(clientListener -> logger.info("listener present " + clientListener));
        listeners.parallelStream().forEach(clientListener -> clientListener.update(STATIONS));
//        listeners.parallelStream().filter(clientListener -> clientListener.isDone()).forEach(clientListener -> removeClientListener(clientListener));
        Iterator<ClientListener> iter = listeners.iterator();
        while(iter.hasNext()){
            if(iter.next().isDone()){
                logger.fine("removing client ");
                iter.remove();
                BikesCrawler.INSTANCE.setUpdateTime(11 * 60);
            }
        }
        listeners.stream().forEach(clientListener -> logger.info("listener remains " + clientListener));


    }

}
