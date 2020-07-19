package com.fightalarm.fightalarm.interfaces;

import com.fightalarm.fightalarm.models.Event;

import java.util.ArrayList;

public interface EventCallback {
    void onCallback(ArrayList<Event> events);
}
