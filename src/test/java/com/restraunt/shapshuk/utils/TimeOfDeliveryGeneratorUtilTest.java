package com.restraunt.shapshuk.utils;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.restraunt.shapshuk.util.TimeConstants.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TimeOfDeliveryGeneratorUtilTest {

    private static final LocalDateTime t8_00 = LocalDateTime.parse(LocalDate.now() + "T" + "08:00");
    private static final LocalDateTime _08_59 = LocalDateTime.parse(LocalDate.now() + "T" + "08:59");
    private static final LocalDateTime _09_00 = LocalDateTime.parse(LocalDate.now() + "T" + TIME_09_00_AM);
    private static final LocalDateTime t15_00 = LocalDateTime.parse(LocalDate.now() + "T" + "15:00");
    private static final LocalDateTime _23_00 = LocalDateTime.parse(LocalDate.now() + "T" + TIME_23_00_PM);
    private static final LocalDateTime _22_30 = LocalDateTime.parse(LocalDate.now() + "T" + "22:30");
    private static final LocalDateTime _00_00 = LocalDateTime.parse(LocalDate.now() + "T" + TIME_00_00_AM);
    private static final LocalDateTime _23_59 = LocalDateTime.parse(LocalDate.now() + "T" + TIME_23_59_PM);

    static void timeCalculator(LocalDateTime currentTimeTruncatedToMinutes) {

        System.out.println("time = " + currentTimeTruncatedToMinutes);

        currentTimeTruncatedToMinutes = currentTimeTruncatedToMinutes
                .truncatedTo(ChronoUnit.MINUTES);

        LocalDateTime currentTimeTruncatedToHours = currentTimeTruncatedToMinutes
                .truncatedTo(ChronoUnit.HOURS);

        LocalDateTime currentTimeTruncatedToHoursAndAHalf = currentTimeTruncatedToMinutes
                .truncatedTo(ChronoUnit.HOURS)
                .plusMinutes(HALF_AN_HOUR);

        LocalDateTime timeVariant = null;

        if (isCurrentTimeBetweenLastTimeForOrderAndMidnight(currentTimeTruncatedToMinutes)) {

            timeVariant = THIS_DAY_23_59_PM.plusMinutes(1);

        } else if (isCurrentTimeBetweenMidnightAndFirstTimeOfDelivery(currentTimeTruncatedToMinutes)) {

            timeVariant = THIS_DAY_FIRST_TIME_OF_DELIVERY;

        } else if (ifCurrentTimeBetweenFirstAndLastTimeOfDelivery(currentTimeTruncatedToMinutes)) {

            if (isCurrentTimeIsAfterCurrentHourAndAHalf(currentTimeTruncatedToMinutes, currentTimeTruncatedToHoursAndAHalf)) {

                timeVariant = currentTimeTruncatedToHours
                        .plusHours(TWO_HOURS);
            } else {

                timeVariant = currentTimeTruncatedToHours
                        .plusHours(ONE_HOUR);
            }
        }

        List<LocalDateTime> result;
        if (isFirstTimeVariantIsTomorrow(timeVariant)) {
            timeVariant = NEXT_DAY_FIRST_TIME_OF_DELIVERY;
            result = getTimeVariantsList(timeVariant, NEXT_DAY_LAST_TIME_OF_DELIVERY);
        } else {
            result = getTimeVariantsList(timeVariant, THIS_DAY_LAST_TIME_OF_DELIVERY);
        }

        for (LocalDateTime localTime : result) {
            System.out.println(localTime);
        }
    }

    private static List<LocalDateTime> getTimeVariantsList(LocalDateTime timeVariant, LocalDateTime lastTimeOfDelivery) {
        List<LocalDateTime> result = new ArrayList<>();
        result.add(timeVariant);
        while (timeVariant.isBefore(lastTimeOfDelivery)) {
            timeVariant = timeVariant.plusHours(ONE_HOUR);
            result.add(timeVariant);
        }
        return result;
    }

    private static boolean isFirstTimeVariantIsTomorrow(LocalDateTime timeVariant) {
        return timeVariant.truncatedTo(ChronoUnit.DAYS).isAfter(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS));
    }

    private static boolean isCurrentTimeIsAfterCurrentHourAndAHalf(LocalDateTime currentTimeTruncatedToMinutes, LocalDateTime currentTimeTruncatedToHoursAndAHalf) {
        return currentTimeTruncatedToMinutes.isAfter(currentTimeTruncatedToHoursAndAHalf);
    }

    private static boolean ifCurrentTimeBetweenFirstAndLastTimeOfDelivery(LocalDateTime currentTimeTruncatedToMinutes) {
        return (currentTimeTruncatedToMinutes.isAfter(THIS_DAY_FIRST_TIME_OF_DELIVERY)
                && currentTimeTruncatedToMinutes.isBefore(THIS_DAY_LAST_TIME_FOR_ORDER))
                || currentTimeTruncatedToMinutes.compareTo(THIS_DAY_FIRST_TIME_OF_DELIVERY) == 0
                || currentTimeTruncatedToMinutes.compareTo(THIS_DAY_LAST_TIME_FOR_ORDER) == 0;
    }

    private static boolean isCurrentTimeBetweenMidnightAndFirstTimeOfDelivery(LocalDateTime currentTimeTruncatedToMinutes) {
        return (currentTimeTruncatedToMinutes.isAfter(THIS_DAY_MIDNIGHT)
                && currentTimeTruncatedToMinutes.isBefore(THIS_DAY_FIRST_TIME_OF_DELIVERY.minusMinutes(ONE_MINUTE)))
                || currentTimeTruncatedToMinutes.compareTo(THIS_DAY_MIDNIGHT) == 0;
    }

    private static boolean isCurrentTimeBetweenLastTimeForOrderAndMidnight(LocalDateTime currentTimeTruncatedToMinutes) {
        return currentTimeTruncatedToMinutes.isAfter(THIS_DAY_LAST_TIME_FOR_ORDER)
                && currentTimeTruncatedToMinutes.isBefore(THIS_DAY_23_59_PM)
                || currentTimeTruncatedToMinutes.compareTo(THIS_DAY_23_59_PM) == 0;
    }

    @Test
    void time_8_00() {
        timeCalculator(t8_00);
    }

    @Test
    void time_9_00() {
        timeCalculator(_09_00);
    }

    @Test
    void time_15_00() {
        timeCalculator(t15_00);
    }

    @Test
    void time_15_29() {
        timeCalculator(t15_00.plusMinutes(29));
    }

    @Test
    void time_15_30() {
        timeCalculator(t15_00.plusMinutes(30));
    }

    @Test
    void time_15_31() {
        timeCalculator(t15_00.plusMinutes(31));
    }

    @Test
    void time_22_29() {
        timeCalculator(_23_00.minusMinutes(31));
    }

    @Test
    void time_22_30() {
        timeCalculator(_23_00.minusMinutes(30));
    }

    @Test
    void time_22_31() {
        timeCalculator(_23_00.minusMinutes(29));
    }

    @Test
    @Order(1)
    void time_23_00() {
        timeCalculator(_23_00);
    }

    @Test
    @Order(3)
    void time_23_59() {
        timeCalculator(_23_59.minusMinutes(0));
    }

    @Test
    @Order(2)
    void time_23_01() {
        timeCalculator(_23_00.plusMinutes(1));
    }

    @Test
    void time_00_00() {
        timeCalculator(_00_00);
    }

    @Test
    void time_00_01() {
        timeCalculator(_00_00.plusMinutes(1));
    }
}
