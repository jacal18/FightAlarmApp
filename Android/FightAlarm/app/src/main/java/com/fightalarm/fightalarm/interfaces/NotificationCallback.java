package com.fightalarm.fightalarm.interfaces;

import com.fightalarm.fightalarm.models.Notification;

import java.util.ArrayList;

public interface NotificationCallback {
    void onCallback(ArrayList<Notification> notifications);
}
