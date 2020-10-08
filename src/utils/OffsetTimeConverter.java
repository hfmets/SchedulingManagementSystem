/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import javafx.util.StringConverter;

/**
 *
 * @author longh
 */

// This class is used to format time values in the appointment creation and update screens through the ChoiceBox fields
public class OffsetTimeConverter extends StringConverter<OffsetTime> {

    @Override
    public String toString(OffsetTime object) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return object.format(formatter);
    }

    @Override
    public OffsetTime fromString(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
