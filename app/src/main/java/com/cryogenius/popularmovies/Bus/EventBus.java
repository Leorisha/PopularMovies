package com.cryogenius.popularmovies.Bus;

import com.squareup.otto.Bus;

/**
 * Created by Ana Neto on 26/01/2017.
 */

public class EventBus extends Bus {

    private static EventBus bus = new EventBus();

    public static EventBus getInstance() {
        return bus;
    }

    private EventBus() {
    }
}
