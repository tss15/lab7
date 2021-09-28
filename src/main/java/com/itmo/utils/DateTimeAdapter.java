package com.itmo.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.sql.Date;
import java.time.ZoneId;

public class DateTimeAdapter {
    public static LocalDateTime convertToLocalDateViaInstant(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate().atStartOfDay();
    }
}
