package com.fightalarm.fightalarm.providers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;

import com.fightalarm.fightalarm.adapters.EventAdapter;
import com.fightalarm.fightalarm.models.Event;
import com.fightalarm.fightalarm.models.Notification;

import android.text.format.DateFormat;
import android.util.Patterns;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Extras {

    String format = "EEEE, MMMM dd yyyy";

    public Extras() {
    }

    public String dateDifference(Long date) {
        try {
            Date startDate = new Date(date);
            Date currentDate = new Date();

            long diff = startDate.getTime() - currentDate.getTime();
            if (diff < 0) {
                return "-0";
            } else {
                long days = diff / (24 * 60 * 60 * 1000);
                return Long.toString(days);
            }
        } catch (Exception e) {
            Log.d("Exception", e.toString());
            return "0";
        }
    }

    public String countdown(Long date, String type) {
        try {
            Date startDate = new Date(date);
            Date currentDate = new Date();

            long diff = currentDate.getTime() - startDate.getTime();

            long days = TimeUnit.MILLISECONDS.toDays(diff);
            if (days < 1) {
                long hours = TimeUnit.MILLISECONDS.toHours(diff);
                if (hours < 1) {
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
                    if (minutes < 1){
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
                        if(type.equalsIgnoreCase("label")){
                            return "second(s)";
                        }
                        return Long.toString(seconds);
                    }
                    if(type.equalsIgnoreCase("label")){
                        return "minute(s)";
                    }
                    return Long.toString(minutes);
                }
                if(type.equalsIgnoreCase("label")){
                    return "hour(s)";
                }
                return Long.toString(hours);

            }
            if(type.equalsIgnoreCase("label")){
                return "day(s)";
            }
            return Long.toString(days);

        } catch (Exception e) {
            Log.d("Exception", e.toString());
            if(type.equalsIgnoreCase("label")){
                return "days";
            }
            return "0";
        }
    }

    public String getCountdownNumbers(Long date, String type) {
        try {
            Date startDate = new Date(date);
            Date currentDate = new Date();

            long diff = currentDate.getTime() - startDate.getTime();

            long days = diff / (24 * 60 * 60 * 1000);
            diff -= days * (24 * 60 * 60 * 1000);
            long hours = diff / (60 * 60 * 1000);
            diff -= hours * (60 * 60 * 1000);
            long minutes = diff / (60 * 1000);
            diff -= minutes * (60 * 1000);
            long seconds = diff / 1000;

            if(type.equalsIgnoreCase("days")){
//                long days = TimeUnit.MILLISECONDS.toDays(diff);
                return String.format("%02d", days);
            } else if (type.equalsIgnoreCase("hours")){
//                long hours = TimeUnit.MILLISECONDS.toHours(diff);
                return String.format("%02d", hours);
            } else if (type.equalsIgnoreCase("minutes")){
//                long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
                return String.format("%02d", minutes);
            } else if (type.equalsIgnoreCase("seconds")){
//                long seconds = TimeUnit.MILLISECONDS.toSeconds(diff) / 1000;
                return String.format("%02d", seconds);
            } else {
                return "00";
            }

        } catch (Exception e) {
            Log.d("Exception", e.toString());
            if(type.equalsIgnoreCase("label")){
                return "days";
            }
            return "00";
        }
    }

    public String extractDate(Long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date));
        return DateFormat.format(format, calendar).toString();
    }


    public String extractDateFormat(Long date) {
        Calendar now = Calendar.getInstance();
        Calendar timeToCheck = Calendar.getInstance();
        timeToCheck.setTimeInMillis(date);

        if (now.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR)) {

            if (now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR)) {
                return "Today";
            } else {
                return DateFormat.format(format, timeToCheck).toString();
            }
        } else {
            return DateFormat.format(format, timeToCheck).toString();
        }
    }


    public boolean checkGreaterThanToday(Long date) {
        Calendar now = Calendar.getInstance();
        Calendar timeToCheck = Calendar.getInstance();
        timeToCheck.setTimeInMillis(date);

        if (now.get(Calendar.YEAR) <= timeToCheck.get(Calendar.YEAR)) {
            if (now.get(Calendar.DAY_OF_YEAR) <= timeToCheck.get(Calendar.DAY_OF_YEAR)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public ArrayList<Event> queryList(ArrayList<Event> events, Long date) {

        ArrayList<Event> newEvents = new ArrayList<Event>();

        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(date);

        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);

            Calendar timeToCheck = Calendar.getInstance();
            timeToCheck.setTimeInMillis(event.getEventdate());

            if (now.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR)) {
                if (now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR)) {
                    newEvents.add(event);
                }
            }
        }
        return newEvents;
    }

    public ArrayList<Event> searchList(ArrayList<Event> events, String query) {

        ArrayList<Event> newEvents = new ArrayList<Event>();

        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            if (event.getPlayer1fname().toLowerCase().contains(query.toLowerCase()) || event.getPlayer2fname().toLowerCase().contains(query.toLowerCase()) || event.getPlayer1lname().toLowerCase().contains(query.toLowerCase()) || event.getPlayer2lname().toLowerCase().contains(query.toLowerCase()) || event.getTitle().toLowerCase().contains(query.toLowerCase())) {
                newEvents.add(event);
            }
        }
        return newEvents;
    }


    public String formatHeader(Event event) {
        if (event.getNow_showing()) {
            return "Now Showing";
        } else {
            return extractDateFormat(event.getEventdate());
        }
    }

    public EventAdapter formatEventList(EventAdapter eventAdapter, ArrayList<Event> events) {

        ArrayList<String> headers = new ArrayList<String>();

        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
//            String date = this.extractDate(event.getEventdate());
//            if (event.getNow_showing()) {
//                if (!headers.contains("now")) {
//                    eventAdapter.addSectionHeaderItem(event);
//                    headers.add("now");
//                }
//            } else {
//                if (!headers.contains(date)) {
//                    eventAdapter.addSectionHeaderItem(event);
//                    headers.add(date);
//                }
//            }
            eventAdapter.addItem(event);
        }

        return eventAdapter;
    }

    public ArrayList<Event> removeScheduled(ArrayList<Event> events, ArrayList<Event> schedules, String returntype) {
        ArrayList<Event> eventArrayList = new ArrayList<Event>();
        ArrayList<Event> newschedules = new ArrayList<Event>();
        if (schedules.size() > 0) {
            for (Event event : events) {
                boolean found = false;
                for (Event schedule : schedules) {
                    if (event.getId().equals(schedule.getId())) {
                        found = true;
                    }
                }
                if (found) {
                    event.setSubscribed(true);
                    eventArrayList.add(event);
                    newschedules.add(event);
                } else {
                    eventArrayList.add(event);
                }
            }
        } else {
            eventArrayList = events;
        }
        if (returntype.equalsIgnoreCase("events")) {
            return eventArrayList;
        } else {
            return newschedules;
        }


    }

    public ArrayList<Notification> removeScheduledID(ArrayList<Notification> notifications, ArrayList<String> schedules) {
        ArrayList<Notification> notificationArrayList = new ArrayList<Notification>();

        if (schedules.size() > 0) {
            for (Notification notification : notifications) {
                boolean found = false;
                for (String schedule : schedules) {
                    if (notification.getUid().equals(schedule) && !notification.getDescription().contains("added")) {
                        found = true;
                    }
                }
                if (found) {
                    notificationArrayList.add(notification);
                }
            }
        }
        return notificationArrayList;


    }

    public void openURL(Context context, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    public boolean isValidEmail(String target) {
        return !TextUtils.isEmpty(target) && EMAIL_ADDRESS_PATTERN.matcher(target).matches();
    }

    public String getID() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String idvalue = Long.toString(date.getTime());
        return idvalue;
    }

    public void openDialog(Event event, Context context){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        String eventt = "subscribed ";
        if(!event.getSubscribed()){
            eventt = "unsubscribed ";
        }
        String from = "to ";
        if(!event.getSubscribed()){
            from = "from ";
        }
        alertDialogBuilder.setMessage("You have successfully " + eventt + from + "the " + event.getPlayer1fname() + " vs " + event.getPlayer2fname() + " fight.");


        alertDialogBuilder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
