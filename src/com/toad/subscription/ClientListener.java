package com.toad.subscription;

import com.toad.crawlers.StationSnapshot;

import java.util.TreeMap;

/**
 * Created by toad on 5/31/15.
 */
public interface ClientListener {
    public void update(TreeMap<String, StationSnapshot> m);

    public boolean isDone();
    public void setDone();
}
