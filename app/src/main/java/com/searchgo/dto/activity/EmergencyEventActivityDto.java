package com.searchgo.dto.activity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tamar.twena on 11/27/2017.
 */

public class EmergencyEventActivityDto implements Serializable {

    private String id;

    private String name;

    private String description;

    private String category;

    private String address;

    private Date lastSeen;

    private Date createdOn;

    public EmergencyEventActivityDto() {
        setCreatedOn(new Date());
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
