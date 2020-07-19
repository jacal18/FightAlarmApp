package com.fightalarm.fightalarm.models;


import android.util.Log;

public class Category {

    private String id;
    private String title;
    private String description;
    private Boolean checked;
    private Long creationdate;

    public Category(){

    }

    public Category(String id, String title, String description, Boolean checked, Long creationdate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.checked = checked;
        this.creationdate = creationdate;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Long getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Long creationdate) {
        this.creationdate = creationdate;
    }


}
