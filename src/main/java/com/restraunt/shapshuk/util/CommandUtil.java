package com.restraunt.shapshuk.util;

import com.restraunt.shapshuk.core.constants.CommonAppConstants;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import static java.lang.String.format;
import static java.util.Objects.nonNull;

public final class CommandUtil {

    private static final Logger LOGGER = Logger.getLogger(CommandUtil.class.getName());

    private CommandUtil() {

    }

    public static String getCommandFromRequest(HttpServletRequest request) {

        String commandType;
        if (nonNull(request.getAttribute(CommonAppConstants.QUERY_PARAM_COMMAND))) {
            commandType = String.valueOf(request.getAttribute(CommonAppConstants.QUERY_PARAM_COMMAND));
        } else {
            commandType = request.getParameter(CommonAppConstants.QUERY_PARAM_COMMAND);
        }

        LOGGER.info(format("Getting command from request param [%s]", commandType));

        return commandType;
    }
}