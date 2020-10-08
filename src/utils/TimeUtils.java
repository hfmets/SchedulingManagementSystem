/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 *
 * @author longh
 */

// This class contains some methods that were used to convert time into the correct time zone when it was either put into or pulled from the data base.
public class TimeUtils {   
    public static ZonedDateTime sqlTimestampToZonedTime(Timestamp timestamp) {
        return timestamp.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault());
    }
    
    public static Timestamp zonedTimeToSqlDate(ZonedDateTime date) {
        return Timestamp.valueOf(date.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
    }
    
    public static boolean overlaps(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}
