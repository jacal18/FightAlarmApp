package com.fightalarm.fightalarm.models;


import java.util.ArrayList;

public class Filter {

    private String sortfield;
    private ArrayList<Category> categories;
    private Long startdate;
    private Long enddate;
    private Integer subscribers;
    private Integer noofdays;
    private Integer distance;


    public Filter(){

    }

    public Filter(String sortfield, ArrayList<Category> categories, Long startdate, Long enddate, Integer subscribers, Integer noofdays, Integer distance) {
        this.sortfield = sortfield;
        this.categories = categories;
        this.startdate = startdate;
        this.enddate = enddate;
        this.subscribers = subscribers;
        this.noofdays = noofdays;
        this.distance = distance;
    }

    public String getSortField() {
        return sortfield;
    }

    public void setSortField(String sortfield) {
        this.sortfield = sortfield;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public Long getStartdate() {
        return startdate;
    }

    public void setStartdate(Long startdate) {
        this.startdate = startdate;
    }

    public Long getEnddate() {
        return enddate;
    }

    public void setEnddate(Long enddate) {
        this.enddate = enddate;
    }

    public Integer getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Integer subscribers) {
        this.subscribers = subscribers;
    }

    public Integer getNoofdays() {
        return noofdays;
    }

    public void setNoofdays(Integer noofdays) {
        this.noofdays = noofdays;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}
