package com.restraunt.shapshuk.util;

import java.time.LocalDateTime;

import static java.time.LocalDate.now;

public final class TimeConstants {

    public static final int HALF_AN_HOUR = 30;
    public static final int ONE_HOUR = 1;
    public static final int TWO_HOURS = 2;
    public static final int ONE_MINUTE = 1;
    public static final String TIME_23_00_PM = "23:00";
    public static final String TIME_09_00_AM = "09:00";
    public static final LocalDateTime THIS_DAY_FIRST_TIME_OF_DELIVERY = LocalDateTime.parse(now() + "T" + TIME_09_00_AM);
    public static final LocalDateTime NEXT_DAY_FIRST_TIME_OF_DELIVERY = LocalDateTime.parse(now().plusDays(1) + "T" + TIME_09_00_AM);
    public static final String TIME_23_59_PM = "23:59";
    public static final LocalDateTime THIS_DAY_23_59_PM = LocalDateTime.parse(now() + "T" + TIME_23_59_PM);
    public static final String TIME_00_00_AM = "00:00";
    public static final LocalDateTime THIS_DAY_MIDNIGHT = LocalDateTime.parse(now() + "T" + TIME_00_00_AM);
    public static final LocalDateTime THIS_DAY_LAST_TIME_OF_DELIVERY = LocalDateTime.parse(now() + "T" + TIME_23_00_PM);
    public static final LocalDateTime NEXT_DAY_LAST_TIME_OF_DELIVERY = LocalDateTime.parse(now().plusDays(1) + "T" + TIME_23_00_PM);
    private static final String TIME_22_30_PM = "22:30";
    public static final LocalDateTime THIS_DAY_LAST_TIME_FOR_ORDER = LocalDateTime.parse(now() + "T" + TIME_22_30_PM);

    private TimeConstants() {

    }
}
