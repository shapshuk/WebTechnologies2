package com.restraunt.shapshuk.command.category;

import com.restraunt.shapshuk.command.Command;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.category.service.DishCategoryService;
import com.restraunt.shapshuk.core.constants.JspName;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

import static com.restraunt.shapshuk.core.constants.CommonAppConstants.DISH_CATEGORY_NAME_JSP_PARAM;
import static com.restraunt.shapshuk.core.constants.CommonAppConstants.MESSAGE_JSP_ATTRIBUTE;
import static com.restraunt.shapshuk.core.constants.LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE;
import static com.restraunt.shapshuk.core.constants.LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE;
import static java.lang.String.format;

public class SubmitDishCategoryDeletingFormCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(SubmitDishCategoryDeletingFormCommand.class.getName());

    private final DishCategoryService dishCategoryService;

    public SubmitDishCategoryDeletingFormCommand(DishCategoryService dishCategoryService) {
        this.dishCategoryService = dishCategoryService;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws SQLException, ConnectionException {

        String categoryName = request.getParameter(DISH_CATEGORY_NAME_JSP_PARAM);
        LOGGER.info(format(PARAM_GOT_FROM_JSP_MESSAGE, DISH_CATEGORY_NAME_JSP_PARAM, categoryName));

        dishCategoryService.deleteByName(categoryName);

        String message = "Dish category has been deleted from menu";
        request.setAttribute(MESSAGE_JSP_ATTRIBUTE, message);
        LOGGER.info(format(ATTRIBUTE_SET_TO_JSP_MESSAGE, MESSAGE_JSP_ATTRIBUTE, message));

        return JspName.COMMAND_RESULT_MESSAGE_JSP;
    }
}
