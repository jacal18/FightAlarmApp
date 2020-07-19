package com.fightalarm.fightalarm.providers;

import android.content.Context;
import android.content.SharedPreferences;
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
public class ScheduleLocalStore {
    public static final String SP_NAME  ="scheduleDetails";
    public static final String SPID_NAME  ="scheduleIDDetails";
    SharedPreferences scheduleLocalDatabase;
    SharedPreferences scheduleIDLocalDatabase;

    private Extras extras = new Extras();
    Gson gson = new Gson();
    Context context;

    OtherLocalStore otherLocalStore;
    public ScheduleLocalStore(Context context){
        this.context = context;
        scheduleLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
        scheduleIDLocalDatabase = context.getSharedPreferences(SPID_NAME, 0);
        otherLocalStore = new OtherLocalStore(context);
    }

    public void storeScheduleData(ArrayList<Event> scheduleList){
        String json = gson.toJson(scheduleList);
        SharedPreferences.Editor spEditor = scheduleLocalDatabase.edit();
        spEditor.putString("listOfSchedules", json);
        spEditor.commit();
    }

    public void storeScheduleIDData(ArrayList<String> scheduleIDList){
        String json = gson.toJson(scheduleIDList);
        SharedPreferences.Editor spEditor = scheduleIDLocalDatabase.edit();
        spEditor.putString("listOfScheduleID", json);
        spEditor.commit();
    }

    public ArrayList<String> getScheduleIDData(){
        String listOfScheduleID = scheduleIDLocalDatabase.getString("listOfScheduleID", "");
        ArrayList<String> scheduleIDList = new ArrayList<String>();

        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        scheduleIDList = gson.fromJson(listOfScheduleID, type);
        if(scheduleIDList != null) {
            return scheduleIDList;
        } else {
            return new ArrayList<String>();
        }
    }

    public ArrayList<Event> getScheduleData(){
        String listOfSchedule = scheduleLocalDatabase.getString("listOfSchedules", "");
        ArrayList<Event> scheduleList = new ArrayList<Event>();

        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
        scheduleList = gson.fromJson(listOfSchedule, type);
        if(scheduleList != null) {
            return scheduleList;
        } else {
            return new ArrayList<Event>();
        }
    }


    public ArrayList<Event> getScheduleDataGreaterThanToday(){
        ArrayList<Event> scheduleData = getScheduleData();
        ArrayList<Event> scheduleList = new ArrayList<Event>();

        for (Iterator<Event> eventIterator = scheduleData.listIterator(); eventIterator.hasNext(); ) {
            Event schedule = eventIterator.next();
            if (extras.checkGreaterThanToday(schedule.getEventdate()) || schedule.getNow_showing()) {
                scheduleList.add(schedule);
            }
        }
        storeScheduleData(scheduleList);
        return scheduleList;
    }

    public void updateSingleEventID(Event event){
        ArrayList<String> eventIDs = getScheduleIDData();

        if (event.getSubscribed()){
            Boolean found = false;
            for (Iterator<String> eventIterator = eventIDs.listIterator(); eventIterator.hasNext(); ) {
                String id = eventIterator.next();
                if (id.equalsIgnoreCase(event.getId())) {
                    found = true;
                }
            }
            if(!found){
                eventIDs.add(event.getId());
                storeScheduleIDData(eventIDs);
            }
        } else {
            for (Iterator<String> eventIterator = eventIDs.listIterator(); eventIterator.hasNext(); ) {
                String id = eventIterator.next();
                if (id.equalsIgnoreCase(event.getId()) ) {
                    eventIterator.remove();
                }
            }
            storeScheduleIDData(eventIDs);
        }
    }

    public void updateSingleEvent(Event event){
        Database database = new Database(this.context);
        ArrayList<Event> events = getScheduleData();

        String userid = otherLocalStore.getUserInfo();
        String timeid = extras.getID();
        if (event.getSubscribed()){
            event.setSubscribers(event.getSubscribers() + 1);
            Boolean found = false;
            for (Iterator<Event> eventIterator = events.listIterator(); eventIterator.hasNext(); ) {
                Event event1 = eventIterator.next();
                if (event1.getId().equalsIgnoreCase(event.getId())) {
                    found = true;
                }
            }
            if(!found){
                events.add(event);
                storeScheduleData(events);
            }

            database.updateEvent(event, "subscribers", true);
            database.updateUser(userid,"subscriptions", true);
            database.saveNotification(new Notification(timeid, timeid, "Event Subscription ", "User Subscribed to Event " + event.getTitle(), "all", "admin", Long.valueOf(timeid), Boolean.FALSE));

        } else {
            event.setSubscribers(event.getSubscribers() - 1);
            for (Iterator<Event> eventIterator = events.listIterator(); eventIterator.hasNext(); ) {
                Event event1 = eventIterator.next();
                if (event1.getId().equalsIgnoreCase(event.getId()) ) {
                    eventIterator.remove();
                }
            }
            storeScheduleData(events);

            database.updateEvent(event, "subscribers", false);
            database.updateUser(userid,"subscriptions", false);
            database.saveNotification(new Notification(timeid, timeid, "Event Un-Subscription ", "User Un-Subscribed from Event " + event.getTitle(), "all", "admin", Long.valueOf(timeid), Boolean.FALSE));
        }
        updateSingleEventID(event);
    }

    public void updateStorage(ArrayList<Event> events){
        ArrayList<Event> schedules = getScheduleData();

        ArrayList<Event> newschedules = extras.removeScheduled(events, schedules, "schedules");
        storeScheduleData(newschedules);
    }

}
