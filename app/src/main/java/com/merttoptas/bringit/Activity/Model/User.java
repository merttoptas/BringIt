package com.merttoptas.bringit.Activity.Model;

public class User {
    private String id;
    private String username;
    private String imageURL;
    private String status;
    private String search;
    private String leads;

    public User(){ }
    public User(String id, String username, String imageURL, String status, String search, String leads) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status =  status;
        this.search = search;
        this.leads = leads;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() { return status; }

    public void setStatus(String mstatus) { this.status = mstatus; }

    public String getSearch() { return search; }

    public void setSearch(String search) { this.search = search; }

    public String getLeads() { return leads;
    }

    public void setLeads(String leads) { this.leads = leads;
    }
}
