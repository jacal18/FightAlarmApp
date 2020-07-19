package com.fightalarm.fightalarm.models;

public class Message {


    private String id;
    private String name;
    private String email;
    private String message;
    private Long creationdate;

    public Message() {

    }

    public Message(String id, String name, String email, String message, Long creationdate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.message = message;
        this.creationdate = creationdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Long creationdate) {
        this.creationdate = creationdate;
    }

}
