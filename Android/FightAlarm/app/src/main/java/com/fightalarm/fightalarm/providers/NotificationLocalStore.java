package com.fightalarm.fightalarm.providers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fightalarm.fightalarm.models.Event;
import com.fightalarm.fightalarm.models.Notification;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Abiwax on 16-01-01.
 */
public class NotificationLocalStore {
    public static final String SP_NAME  ="notificationDetails";
    SharedPreferences notificationLocalDatabase;

    Gson gson = new Gson();
    ArrayList<Notification> notifications = new ArrayList<Notification>();



    public NotificationLocalStore(Context context){
        notificationLocalDatabase = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void storeNotificationData(ArrayList<Notification> notificationList){
        String json = gson.toJson(notificationList);
        SharedPreferences.Editor spEditor = notificationLocalDatabase.edit();
        spEditor.putString("listOfNotifications", json);
        spEditor.commit();
    }

    public ArrayList<Notification> getNotificationData(){


        String listOfNotification = notificationLocalDatabase.getString("listOfNotifications", "");
        ArrayList<Notification> notificationList = new ArrayList<Notification>();

        Type type = new TypeToken<ArrayList<Notification>>() {}.getType();
        notificationList = gson.fromJson(listOfNotification, type);
        if(notificationList != null){
            return notificationList;
        } else {
            return new ArrayList<Notification>();
        }
    }

    public void deleteNotification(Notification notification){
        ArrayList<Notification> notifications = getNotificationData();
        for (Iterator<Notification> notificationIterator = notifications.listIterator(); notificationIterator.hasNext(); ) {
            Notification notification1 = notificationIterator.next();
            if (notification1.getId().equalsIgnoreCase(notification.getId())) {
                notificationIterator.remove();
            }
        }
        storeNotificationData(notifications);
    }

    public void updateNotifications(){
        ArrayList<Notification> notifications = getNotificationData();
        ArrayList<Notification> newnotifications = new ArrayList<Notification>();
        for (Iterator<Notification> notificationIterator = notifications.listIterator(); notificationIterator.hasNext(); ) {
            Notification notification1 = notificationIterator.next();
            notification1.setRead(Boolean.TRUE);
            newnotifications.add(notification1);
        }
        storeNotificationData(newnotifications);
    }

    public void addNotification(Notification notification){
        ArrayList<Notification> notifications = getNotificationData();
        notifications.add(notification);
        storeNotificationData(notifications);
    }

    public Integer countUnread(){
        ArrayList<Notification> notifications = getNotificationData();
        ArrayList<Notification> newnotifications = new ArrayList<Notification>();
        for (Iterator<Notification> notificationIterator = notifications.listIterator(); notificationIterator.hasNext(); ) {
            Notification notification1 = notificationIterator.next();
            if (!notification1.getRead()) {
                newnotifications.add(notification1);
            }
        }
        return newnotifications.size();
    }
}
