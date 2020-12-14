package com.restraunt.shapshuk.util;

import com.restraunt.shapshuk.core.constants.LoggerConstants;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.String.format;

public final class LocalDateTimeFormattingUtil {

    private static final Logger LOGGER = Logger.getLogger(LocalDateTimeFormattingUtil.class.getName());

    private LocalDateTimeFormattingUtil() {

    }

    public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern) {

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        String formattedLocalDateTime = localDateTime.format(DateTimeFormatter.ofPattern(pattern));

        LOGGER.info(String.format(LoggerConstants.CLASS_INVOKED_METHOD_AND_GOT_MESSAGE, LocalDateTimeFormattingUtil.class.getSimpleName(), nameOfCurrentMethod, formattedLocalDateTime));
        return formattedLocalDateTime;
    }
}