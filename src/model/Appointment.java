/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.ZonedDateTime;

/**
 *
 * @author longh
 */
public class Appointment extends DatabaseObject {
    private int appointmentId;
    private int customerId;
    private int userId;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
    private ZonedDateTime start;
    private ZonedDateTime end;
    
    public Appointment() {
        super();
    }
    
    public Appointment(int appId, int customerId, int userId, String title, String desc, String loc, String cont, String type, String url, ZonedDateTime start, ZonedDateTime end) {
        super();
        this.appointmentId = appId;
        this.customerId = customerId;
        this.userId = userId;
        this.title = title;
        this.description = desc;
        this.location = loc;
        this.contact = cont;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
    }
    
    public Appointment(int appId, int customerId, int userId, String title, String desc, String loc, String cont, String type, String url, ZonedDateTime start, ZonedDateTime end, ZonedDateTime cd, ZonedDateTime lu, String cb, String lub) {
        super(cd, lu, cb, lub);
        this.appointmentId = appId;
        this.customerId = customerId;
        this.userId = userId;
        this.title = title;
        this.description = desc;
        this.location = loc;
        this.contact = cont;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
    }
    
    public int getAppointmentId() {
        return appointmentId;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getLocation() {
        return location;
    }
    
    public String getContact() {
        return contact;
    }
    
    public String getType() {
        return type;
    }
    
    public String getUrl() {
        return url;
    }
    
    public ZonedDateTime getStart() {
        return start;
    }
    
    public ZonedDateTime getEnd() {
        return end;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }
}
