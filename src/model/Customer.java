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
public class Customer extends DatabaseObject {
    private int customerId;
    private String customerName;
    private int addressId;
    private boolean active;
    
    public Customer() {
        super();
    }
    
    public Customer(int customerId, int addressId, String customerName, boolean active, ZonedDateTime cd, ZonedDateTime lu, String cb, String lub) {
        super(cd, lu, cb, lub);
        this.customerId = customerId;
        this.customerName = customerName;
        this.addressId = addressId;
        this.active = active;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public int getAddressId() {
        return addressId;
    }
    
    public boolean isActive() {
        return active;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
