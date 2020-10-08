/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import static schedulingmanagementsystem.SchedulingManagementSystem.currentUser;

/**
 *
 * @author longh
 */
public class DatabaseObject {
    private ZonedDateTime createDate;
    private String createdBy;
    private ZonedDateTime lastUpdate;
    private String lastUpdateBy;
    
    public DatabaseObject() {
        this.createDate = ZonedDateTime.now(ZoneId.systemDefault());
        this.lastUpdate = ZonedDateTime.now(ZoneId.systemDefault());
        this.createdBy = currentUser;
        this.lastUpdateBy = currentUser;
    }
    
    public DatabaseObject(ZonedDateTime cd, ZonedDateTime lu, String cb, String lub) {
        this.createDate = cd;
        this.lastUpdate = lu;
        this.createdBy = cb;
        this.lastUpdateBy = lub;
    }
    
    public ZonedDateTime getCreateDate() {
        return createDate;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }
    
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
}
