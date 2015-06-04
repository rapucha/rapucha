package com.toad.subscription;

import com.toad.crawlers.StationSnapshot;

import java.util.TreeMap;

/**
 * Created by Seva Nechaev "Rapucha" on 5/31/15. All rights reserved ;)
 */
public interface ClientListener {
    void update(TreeMap<String, StationSnapshot> m);

    boolean isDone();

    void setDone();
}
