/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import javafx.util.StringConverter;
import model.City;

/**
 *
 * @author longh
 */

// This class is used to display city names in the ChoiceBoxes in customer creation and updates
public class CityConverter extends StringConverter<City> {

    @Override
    public String toString(City object) {
        return object.getCityName();
    }

    @Override
    public City fromString(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
