package com.fightalarm.fightalarm.providers;

import android.content.Context;
import android.util.Log;

import com.fightalarm.fightalarm.interfaces.CategoryCallback;
import com.fightalarm.fightalarm.interfaces.EventCallback;
import com.fightalarm.fightalarm.interfaces.NotificationCallback;
import com.fightalarm.fightalarm.models.Category;
import com.fightalarm.fightalarm.models.Event;
import com.fightalarm.fightalarm.models.Message;
import com.fightalarm.fightalarm.models.Notification;
import com.fightalarm.fightalarm.models.Subscriber;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Database {
    Context context;
    ScheduleLocalStore scheduleLocalStore;
    OtherLocalStore otherLocalStore;
    Extras extras = new Extras();
    private static FirebaseDatabase firebaseDatabase;

    public Database(Context context) {
        this.context = context;
        this.scheduleLocalStore = new ScheduleLocalStore(this.context);
        this.otherLocalStore = new OtherLocalStore(this.context);
        this.setPersistence();
    }

    public void setPersistence() {

        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
        }
    }

    public ArrayList<Event> getEvents(final EventCallback eventCallback) {

        final ArrayList<Event> events = new ArrayList<Event>();

        // Get a reference to our posts
        DatabaseReference ref = firebaseDatabase.getReference("/events");

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                events.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Event event = postSnapshot.getValue(Event.class);
                    events.add(event);
                }
                ArrayList<Event> schedules = scheduleLocalStore.getScheduleData();

                ArrayList<Event> newEvents = extras.removeScheduled(events, schedules, "events");
                eventCallback.onCallback(newEvents);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        ArrayList<Event> schedules = this.scheduleLocalStore.getScheduleData();
        ArrayList<Event> newEvents = this.extras.removeScheduled(events, schedules, "events");

        return newEvents;
    }


    public void updateEvent(Event event, String type, final Boolean increment) {
        // Get a reference to events
        DatabaseReference ref = firebaseDatabase.getReference("/events").child(event.getId()).child(type);

        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.getValue() == null) {
                    if (increment) {
                        mutableData.setValue(1);
                    } else {
                        mutableData.setValue(0);
                    }
                } else {
                    int count = mutableData.getValue(Integer.class);
                    if (increment) {
                        mutableData.setValue(count + 1);
                    } else {
                        mutableData.setValue(count - 1);
                    }
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean success, DataSnapshot dataSnapshot) {
                // Analyse databaseError for any error during increment
            }
        });
    }

    public void updateUser(String userid, String type, final Boolean increment) {

        // Get a reference to subscriber
        userid = userid.toLowerCase().split("user")[1];

        DatabaseReference ref = firebaseDatabase.getReference("/subscribers").child(userid).child(type);

        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.getValue() == null) {
                    if (increment) {
                        mutableData.setValue(1);
                    } else {
                        mutableData.setValue(0);
                    }
                } else {
                    int count = mutableData.getValue(Integer.class);
                    if (increment) {
                        mutableData.setValue(count + 1);
                    } else {
                        mutableData.setValue(count - 1);
                    }
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean success, DataSnapshot dataSnapshot) {
                // Analyse databaseError for any error during increment
            }
        });
    }

    public ArrayList<Event> filterEventsBySubscription(ArrayList<Event> eventArrayList) {
        ArrayList<Event> events = new ArrayList<Event>();
        for (Event event : eventArrayList) {
            Integer datediff = Integer.valueOf(this.extras.dateDifference(event.getEventdate()));
            if (!event.getSubscribed() && datediff >= 0) {
                events.add(event);
            }
        }
        return events;
    }

    public ArrayList<Event> filterEventsByNowShowing(ArrayList<Event> eventArrayList) {
        ArrayList<Event> events = new ArrayList<Event>();
        for (Event event : eventArrayList) {
            if (!event.getNow_showing()) {
                events.add(event);
            }
        }
        return events;
    }

    public ArrayList<Category> getCategories(final CategoryCallback categoryCallback) {
        final ArrayList<Category> categories = new ArrayList<Category>();

        // Get a reference to our posts
        DatabaseReference ref = firebaseDatabase.getReference("/categories");

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categories.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    categories.add(postSnapshot.getValue(Category.class));
                }
                categoryCallback.onCallback(categories);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        return categories;
    }

    public ArrayList<Notification> getNotifications(final NotificationCallback notificationCallback) {
        final ArrayList<Notification> notifications = new ArrayList<Notification>();

        // Get a reference to our posts
        DatabaseReference ref = firebaseDatabase.getReference("/notifications");

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Notification notification = postSnapshot.getValue(Notification.class);
                    if (notification.getType().equalsIgnoreCase("user")) {
                        notifications.add(postSnapshot.getValue(Notification.class));
                    }
                }
                ArrayList<String> scheduleIDs = scheduleLocalStore.getScheduleIDData();
                ArrayList<Notification> newNotifications = extras.removeScheduledID(notifications, scheduleIDs);

                notificationCallback.onCallback(newNotifications);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        return notifications;
    }

    public void saveNotification(Notification notification) {
        DatabaseReference ref = firebaseDatabase.getReference("/notifications");
        ref.child(notification.getId()).setValue(notification);
    }

    public void saveMessage(Message message) {
        DatabaseReference ref = firebaseDatabase.getReference("/messages");
        ref.child(message.getId()).setValue(message);
    }

    public void saveSubscribers(Subscriber subscriber) {
        DatabaseReference ref = firebaseDatabase.getReference("/subscribers");
        ref.child(subscriber.getId()).setValue(subscriber);
    }

    public ArrayList<Event> filterEventsByCategories(ArrayList<Event> eventList, String category) {
//        if(eventList.size() < 1){
//            eventList = this.getEvents();
//        }

        ArrayList<Event> events = new ArrayList<Event>();
        for (Event event : eventList) {
            Boolean found = false;
            for (Category category1 : event.getCategories()) {
                if (category.equalsIgnoreCase(category1.getId())) {
                    if (!found) {
                        found = true;
                    }
                }
            }
            if (found) {
                events.add(event);
            }
        }
        return events;
    }

    public ArrayList<Event> filterEventsBySubscribers(ArrayList<Event> eventList, Integer subscribers, Integer subscribers2) {
        ArrayList<Event> events = new ArrayList<Event>();
        for (Event event : eventList) {
            if (event.getSubscribers() >= subscribers && event.getSubscribers() <= subscribers2) {
                events.add(event);
            }
        }
        return events;
    }

    public ArrayList<Event> filterEventsByDateRange(ArrayList<Event> eventList, Long startdate, Long enddate) {
        ArrayList<Event> events = new ArrayList<Event>();
        for (Event event : eventList) {
            if (event.getEventdate() >= startdate && event.getEventdate() <= enddate) {
                events.add(event);
            }
        }
        return events;
    }

    public ArrayList<Event> filterEventsByDaysLeft(ArrayList<Event> eventList, Integer number, Integer number2) {
        ArrayList<Event> events = new ArrayList<Event>();
        for (Event event : eventList) {
            Integer eventDays = Integer.parseInt(extras.dateDifference(event.getEventdate()));
            if (eventDays >= number && eventDays <= number) {
                events.add(event);
            }
        }
        return events;
    }

    public ArrayList<Event> sortEvents(ArrayList<Event> eventList, String field) {

        if (field.equalsIgnoreCase("fname")) {
            Collections.sort(eventList, new Comparator<Event>() {
                public int compare(Event event1, Event event2) {
                    return event1.getPlayer1fname().compareTo(event2.getPlayer1fname());
                }
            });
            Collections.sort(eventList, new Comparator<Event>() {
                public int compare(Event event1, Event event2) {
                    return event1.getPlayer2fname().compareTo(event2.getPlayer2fname());
                }
            });
        } else if (field.equalsIgnoreCase("lname")) {
            Collections.sort(eventList, new Comparator<Event>() {
                public int compare(Event event1, Event event2) {
                    return event1.getPlayer1lname().compareTo(event2.getPlayer1lname());
                }
            });
            Collections.sort(eventList, new Comparator<Event>() {
                public int compare(Event event1, Event event2) {
                    return event1.getPlayer2lname().compareTo(event2.getPlayer2lname());
                }
            });
        } else if (field.equalsIgnoreCase("address")) {
            Collections.sort(eventList, new Comparator<Event>() {
                public int compare(Event event1, Event event2) {
                    return event1.getEventaddress().compareTo(event2.getEventaddress());
                }
            });

        } else if (field.equalsIgnoreCase("state")) {
            Collections.sort(eventList, new Comparator<Event>() {
                public int compare(Event event1, Event event2) {
                    return event1.getEventstate().compareTo(event2.getEventstate());
                }
            });

        } else if (field.equalsIgnoreCase("country")) {
            Collections.sort(eventList, new Comparator<Event>() {
                public int compare(Event event1, Event event2) {
                    return event1.getEventcountry().compareTo(event2.getEventcountry());
                }
            });

        } else if (field.equalsIgnoreCase("date")) {
            Collections.sort(eventList, new Comparator<Event>() {
                public int compare(Event event1, Event event2) {
                    return event1.getEventdate().compareTo(event2.getEventdate());
                }
            });

        }

        return eventList;
    }
}
