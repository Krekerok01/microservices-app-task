package com.specificgroup.blog.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Util class for working with date and time
 */
public class DateTimeUtil {

    public static LocalDateTime getMinskCurrentTime(){
        return ZonedDateTime.now(ZoneId.of("Europe/Minsk")).toLocalDateTime();
    }
}