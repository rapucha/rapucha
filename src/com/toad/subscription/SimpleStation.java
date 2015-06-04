package com.toad.subscription;

import com.toad.crawlers.StationSnapshot;

/**
 * Created by toad on 6/4/15.
 */
public final class SimpleStation {
    final int bikes;
    final String name;

    public SimpleStation(String name, int bikes) {
        this.name = name;
        this.bikes = bikes;
    }

    public SimpleStation(StationSnapshot st) {
        this.name = st.getName();
        this.bikes = st.getBikes();
    }

}
