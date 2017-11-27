package com.searchgo.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tamar.twena on 11/21/2017.
 */

public class EmergencyEvent implements Serializable {

    private String id;

    private String name;

    private String category;

    private String address;

    private Date lastSeen;

    private Date createdOn;

    public EmergencyEvent() {
        createdOn = new Date();
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public Date getCreatedOn() {
        return createdOn;
    }
}
