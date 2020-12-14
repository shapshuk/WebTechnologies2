package com.restraunt.shapshuk.util;

import com.restraunt.shapshuk.entity.category.model.DishCategory;
import com.restraunt.shapshuk.core.constants.LoggerConstants;
import org.apache.log4j.Logger;

import java.util.List;

import static java.lang.String.format;

public final class CheckedStateSetterUtil {

    private static final Logger LOGGER = Logger.getLogger(CheckedStateSetterUtil.class.getName());

    private CheckedStateSetterUtil() {

    }

    public static boolean setCheckedState(DishCategory categoryFromList, List<String> checkedCategories) {

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        LOGGER.info(String.format(LoggerConstants.CLASS_INVOKED_METHOD_MESSAGE, LocalDateTimeFormattingUtil.class.getSimpleName(), nameOfCurrentMethod));

        for (String checkedCategory : checkedCategories) {
            if (categoryFromList.getCategoryName().equals(checkedCategory)) {
                return true;
            }
        }

        return false;
    }
}
