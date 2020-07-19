package com.fightalarm.fightalarm.models;

public class Notification {

    private String id;
    private String uid;



    private String title;
    private String description;
    private String type;
    private String topic;
    private Long creationdate;
    private Boolean read;

    public Notification(){

    }

    public Notification(String id,String uid, String title, String description, String type, String topic, Long creationdate, Boolean read) {
        this.id = id;
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.type = type;
        this.topic = topic;
        this.creationdate = creationdate;
        this.read = read;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Long getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Long creationdate) {
        this.creationdate = creationdate;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }


}
