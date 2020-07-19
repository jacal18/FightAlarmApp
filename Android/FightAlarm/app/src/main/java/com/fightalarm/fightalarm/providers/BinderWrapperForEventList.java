package com.fightalarm.fightalarm.providers;

import android.os.Binder;

import com.fightalarm.fightalarm.models.Event;

import java.util.ArrayList;

public class BinderWrapperForEventList extends Binder {
    private final ArrayList<Event> events;

    public BinderWrapperForEventList(ArrayList<Event> events) {
        this.events = events;
    }

    public ArrayList<Event> getData() {
        return events;
    }
}
