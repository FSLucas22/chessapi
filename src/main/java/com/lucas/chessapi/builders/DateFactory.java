package com.lucas.chessapi.builders;

import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateFactory {
    public static Date today() {
        return new Date();
    }

    public static Date expirationDate(Date issueDate, Long expirationMillis) {
        return Date.from(issueDate.toInstant().plus(expirationMillis, ChronoUnit.MILLIS));
    }
}
