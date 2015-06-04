package com.toad.subscription;

import com.toad.crawlers.StationSnapshot;

/**
 * Created by Seva Nechaev "Rapucha" on 6/4/15. All rights reserved ;)
 */
final class SimpleStation {
    final int bikes;
    final String name;


    public SimpleStation(StationSnapshot st) {
        this.name = st.getName();
        this.bikes = st.getBikes();
    }

    @Override
    public String toString() {
        return "SimpleStation{" +
                "name=" + name +
                ", bikes='" + bikes + '\'' +
                '}';
    }
}
