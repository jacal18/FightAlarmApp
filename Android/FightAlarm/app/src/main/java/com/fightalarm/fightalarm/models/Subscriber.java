package com.fightalarm.fightalarm.models;

public class Subscriber {

    private String id;
    private String email;
    private Integer subscriptions;
    private Long joineddate;

    public Subscriber() {

    }

    public Subscriber(String id, String email, Integer subscriptions, Long joineddate) {
        this.id = id;
        this.email = email;
        this.subscriptions = subscriptions;
        this.joineddate = joineddate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Integer subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Long getJoineddate() {
        return joineddate;
    }

    public void setJoineddate(Long joineddate) {
        this.joineddate = joineddate;
    }

}
