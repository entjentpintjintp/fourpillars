package com.kolloseum.fourpillars.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    private static final DateTimeFormatter
            TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static String getCurrentTimeFormatted()
    {
        return LocalDateTime.now().format(TIME_FORMATTER);
    }

}
