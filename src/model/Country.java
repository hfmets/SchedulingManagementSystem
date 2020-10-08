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
public class Country extends DatabaseObject {
    private int countryId;
    private String countryName;
    
    public Country() {
        super();
    }
    
    public Country(int countryId, String countryName, ZonedDateTime cd, ZonedDateTime lu, String cb, String lub) {
        super(cd, lu, cb, lub);
        this.countryId = countryId;
        this.countryName = countryName;
    }
    
    public int getCountryId() {
        return countryId;
    }
    
    public String getCountryName() {
        return countryName;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
