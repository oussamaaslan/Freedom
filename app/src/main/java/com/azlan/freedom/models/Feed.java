package com.azlan.freedom.models;

import java.util.Date;

public class Feed {
    // variables for storing data
    // of our recycler view item
    private String id;
    private String idPublisher;
    private String media_type;
    private String media_url;
    private String username;
    private String caption;
    private String timestamp;
    private int likescount;

    private int cemmentscount;
    private String description;
    private Date publish_date;

    public int getCemmentscount() {
        return cemmentscount;
    }

    public void setCemmentscount(int cemmentscount) {
        this.cemmentscount = cemmentscount;
    }






    public String getIdPublisher() {
        return idPublisher;
    }

    public void setIdPublisher(String idPublisher) {
        this.idPublisher = idPublisher;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getLikescount() {
        return likescount;
    }

    public void setLlikescount(int likescount) {
        this.likescount = likescount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Feed() {

    }

    public Feed(String idPublisher, String media_url, String username, String timestamp, int likescount, int cemmentscount, String description, Date publishDate) {
        this.idPublisher = idPublisher;
        this.media_url = media_url;
        this.username = username;
        this.timestamp = timestamp;
        this.likescount = likescount;
        this.cemmentscount=cemmentscount;
        this.description = description;
        this.publish_date = publishDate;
    }


    public Date getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(Date publish_date) {
        this.publish_date = publish_date;
    }
}

