package com.restraunt.shapshuk.command.dish;

import com.restraunt.shapshuk.command.Command;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.dish.service.DishService;
import com.restraunt.shapshuk.core.constants.CommonAppConstants;
import com.restraunt.shapshuk.core.constants.JspName;
import com.restraunt.shapshuk.core.constants.LoggerConstants;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

import static java.lang.Long.parseLong;
import static java.lang.String.format;

public class DeleteDishCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(DeleteDishCommand.class.getName());

    private final DishService dishService;

    public DeleteDishCommand(DishService dishService) {
        this.dishService = dishService;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws SQLException, ConnectionException {

        String dishIdString = request.getParameter(CommonAppConstants.DISH_ID_JSP_PARAM);
        LOGGER.info(String.format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, CommonAppConstants.DISH_ID_JSP_PARAM, dishIdString));

        dishService.deleteById(parseLong(dishIdString));

        String message = "Dish has been deleted from menu";
        request.setAttribute(CommonAppConstants.MESSAGE_JSP_ATTRIBUTE, message);
        LOGGER.info(String.format(LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE, CommonAppConstants.MESSAGE_JSP_ATTRIBUTE, message));

        return JspName.DISH_MENU_JSP;
    }

}
