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
public class City extends DatabaseObject {
    private int cityId;
    private String cityName;
    private int countryId;
    
    public City() {
        super();
    }
    
    public City(int cityId, int countryId, String cityName, ZonedDateTime cd, ZonedDateTime lu, String cb, String lub) {
        super(cd, lu, cb, lub);
        this.cityId = cityId;
        this.cityName = cityName;
        this.countryId = countryId;
    }
    
    public int getCityId() {
        return cityId;
    }
    
    public String getCityName() {
        return cityName;
    }
    
    public int getCountryId() {
        return countryId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
