/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author longh
 */

// This class represents the appointment times but only in the business hours which I made out to be
// 8:00 AM - 5:00 PM CST
// It sets two variables that are available after initialization from the static instance that the applications starting class creates
// one of which represents the business hours in the current locale and the other in the normal CST
public class AppointmentTimes {    
    final Instant THIS_INSTANT = Instant.now();
    final ZoneOffset BUSINESS_OFFSET = ZoneId.of("America/Chicago").getRules().getOffset(THIS_INSTANT);
    public ObservableList<OffsetTime> appTimes;
    public ObservableList<OffsetTime> localAppTimes;
    
    // Business hours are from 8:00 AM to 5:00PM and take place in the America/Chicago TimeZone

    public AppointmentTimes() {
        appTimes = FXCollections.observableArrayList();
        localAppTimes = FXCollections.observableArrayList();
        LocalTime start = LocalTime.of(8, 0);
        for (int i = 0; i < 18; i++) {
            appTimes.add(OffsetTime.of(start, BUSINESS_OFFSET));
            start = start.plusMinutes(30);
        }
        localAppTimes.setAll(appTimes);
        localAppTimes.replaceAll(time -> time.withOffsetSameInstant(ZoneId.systemDefault().getRules().getOffset(THIS_INSTANT)));
    }
}
