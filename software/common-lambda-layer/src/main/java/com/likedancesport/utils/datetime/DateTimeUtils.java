package com.likedancesport.utils.datetime;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalUnit;
import java.util.Date;

public class DateTimeUtils {
    public static LocalDateTime fromTimestamp(Long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneOffset.UTC.normalized());
    }

    public static Date generateExpirationDate(long amountToAdd, TemporalUnit unit) {
        return Date.from(Instant.now().plus(amountToAdd, unit));
    }
}
