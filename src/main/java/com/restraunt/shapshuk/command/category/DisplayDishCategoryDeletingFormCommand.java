package com.restraunt.shapshuk.command.category;

import com.restraunt.shapshuk.command.Command;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.category.model.DishCategory;
import com.restraunt.shapshuk.entity.category.service.DishCategoryService;
import com.restraunt.shapshuk.core.constants.CommonAppConstants;
import com.restraunt.shapshuk.core.constants.JspName;
import com.restraunt.shapshuk.core.constants.LoggerConstants;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

import static java.lang.String.format;

public class DisplayDishCategoryDeletingFormCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(DisplayDishCategoryDeletingFormCommand.class.getName());

    private final DishCategoryService dishCategoryService;

    public DisplayDishCategoryDeletingFormCommand(DishCategoryService dishCategoryService) {
        this.dishCategoryService = dishCategoryService;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws SQLException, ConnectionException {

        List<DishCategory> dishCategories = dishCategoryService.findAll();
        request.setAttribute(CommonAppConstants.CATEGORY_LIST_JSP_ATTRIBUTE, dishCategories);
        LOGGER.info(String.format(LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE, CommonAppConstants.CATEGORY_LIST_JSP_ATTRIBUTE, dishCategories.toString()));

        return JspName.DELETE_CATEGORY_FORM_JSP;
    }
}
