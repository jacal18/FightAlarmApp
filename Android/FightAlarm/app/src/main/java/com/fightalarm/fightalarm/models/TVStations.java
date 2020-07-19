package com.fightalarm.fightalarm.models;



public class TVStations {

    private String id;
    private String title;
    private String description;
    private Boolean checked;
    private Long creationdate;
    private String imageURL;

    public TVStations(){

    }

    public TVStations(String id, String title, String description, Boolean checked, Long creationdate, String imageURL) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.checked = checked;
        this.creationdate = creationdate;
        this.imageURL = imageURL;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

}
