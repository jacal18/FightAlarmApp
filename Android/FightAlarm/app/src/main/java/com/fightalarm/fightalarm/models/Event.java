package com.fightalarm.fightalarm.models;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Event implements Serializable {
    private String id;
    private String title;
    private Integer subscribers;
    private String player1fname;
    private String player2fname;
    private String player1lname;
    private String player2lname;
    private Long eventdate;
    private String eventaddress;
    private String eventcity;
    private String eventstate;
    private String eventcountry;
    private ArrayList<Category> categories;
    private ArrayList<TVStations> tvstations;
    private Boolean now_showing;
    private Boolean subscribed;

    public Event(){

    }

    public Event(String id, String title, Integer subscribers, String player1fname, String player2fname, String player1lname, String player2lname, Long eventdate, String eventaddress, String eventcity, String eventstate, String eventcountry, ArrayList<Category> categories, ArrayList<TVStations> tvstations, Boolean now_showing, Boolean subscribed) {
        this.id = id;
        this.title = title;
        this.subscribers = subscribers;
        this.player1fname = player1fname;
        this.player2fname = player2fname;
        this.player1lname = player1lname;
        this.player2lname = player2lname;
        this.eventdate = eventdate;
        this.eventaddress = eventaddress;
        this.eventcity = eventcity;
        this.eventstate = eventstate;
        this.eventcountry = eventcountry;
        this.categories = categories;
        this.tvstations = tvstations;
        this.now_showing = now_showing;
        this.subscribed = subscribed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Integer subscribers) {
        this.subscribers = subscribers;
    }

    public String getPlayer1fname() {
        return player1fname;
    }

    public void setPlayer1fname(String player1fname) {
        this.player1fname = player1fname;
    }

    public String getPlayer2fname() {
        return player2fname;
    }

    public void setPlayer2fname(String player2fname) {
        this.player2fname = player2fname;
    }

    public String getPlayer1lname() {
        return player1lname;
    }

    public void setPlayer1lname(String player1lname) {
        this.player1lname = player1lname;
    }

    public String getPlayer2lname() {
        return player2lname;
    }

    public void setPlayer2lname(String player2lname) {
        this.player2lname = player2lname;
    }

    public Long getEventdate() {
        return eventdate;
    }

    public void setEventdate(Long eventdate) {
        this.eventdate = eventdate;
    }

    public String getEventaddress() {
        return eventaddress;
    }

    public void setEventaddress(String eventaddress) {
        this.eventaddress = eventaddress;
    }

    public String getEventstate() {
        return eventstate;
    }

    public void setEventstate(String eventstate) {
        this.eventstate = eventstate;
    }

    public String getEventcity() {
        return eventcity;
    }

    public void setEventcity(String eventcity) {
        this.eventcity = eventcity;
    }

    public String getEventcountry() {
        return eventcountry;
    }

    public void setEventcountry(String eventcountry) {
        this.eventcountry = eventcountry;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public ArrayList<TVStations> getTvstations() {
        return tvstations;
    }

    public void setTvstations(ArrayList<TVStations> tvstations) {
        this.tvstations = tvstations;
    }

    public Boolean getNow_showing() {
        return now_showing;
    }

    public void setNow_showing(Boolean now_showing) {
        this.now_showing = now_showing;
    }

    public Boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
    }


}
