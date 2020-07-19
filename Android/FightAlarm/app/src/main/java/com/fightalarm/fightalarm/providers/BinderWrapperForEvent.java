package com.fightalarm.fightalarm.providers;

import android.os.Binder;

import com.fightalarm.fightalarm.models.Event;

public class BinderWrapperForEvent extends Binder {
    private final Event event;

    public BinderWrapperForEvent(Event event) {
        this.event = event;
    }

    public Event getData() {
        return event;
    }
}
