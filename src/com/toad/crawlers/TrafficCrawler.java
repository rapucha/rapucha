package com.toad.crawlers;

import java.io.BufferedReader;

/**
 * Created by Morta on 21-May-15.
 */
public class TrafficCrawler extends ACrawler {
    /**
     * @param time     minutes between crawling actions
     * @param address  where to go
     * @param beRandom add some jitter to requests ;)
     //* @throws MalformedURLException
     */
    public TrafficCrawler(int time, String address, boolean beRandom) {
        super(time, address, beRandom);
    }

    @Override
    protected void processInput(BufferedReader br) {

    }
}
