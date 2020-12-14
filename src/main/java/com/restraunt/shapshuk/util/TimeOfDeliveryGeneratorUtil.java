package com.restraunt.shapshuk.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.restraunt.shapshuk.util.TimeConstants.*;

public final class TimeOfDeliveryGeneratorUtil {

    private TimeOfDeliveryGeneratorUtil() {
    }

    public static List<LocalDateTime> findTimeVariants() {

        LocalDateTime currentTimeTruncatedToMinutes = LocalDateTime
                .now()
                .truncatedTo(ChronoUnit.MINUTES);

        LocalDateTime currentTimeTruncatedToHours = LocalDateTime
                .now()
                .truncatedTo(ChronoUnit.HOURS);

        LocalDateTime currentTimeTruncatedToHoursAndAHalf = LocalDateTime
                .now()
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

        if (isFirstTimeVariantIsTomorrow(timeVariant)) {
            timeVariant = NEXT_DAY_FIRST_TIME_OF_DELIVERY;
            return getTimeVariantsList(timeVariant, NEXT_DAY_LAST_TIME_OF_DELIVERY);
        } else {
            return getTimeVariantsList(timeVariant, THIS_DAY_LAST_TIME_OF_DELIVERY);
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
}
