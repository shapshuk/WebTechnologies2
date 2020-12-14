package com.restraunt.shapshuk.util;

import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.category.model.DishCategory;
import com.restraunt.shapshuk.entity.category.service.DishCategoryService;
import com.restraunt.shapshuk.entity.dish.model.Dish;
import com.restraunt.shapshuk.entity.dish.service.DishService;
import com.restraunt.shapshuk.core.constants.CommonAppConstants;
import com.restraunt.shapshuk.core.constants.LoggerConstants;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

import static java.lang.Long.parseLong;
import static java.lang.String.format;
import static java.util.Objects.isNull;

public class JspUtil {

    private static final Logger LOGGER = Logger.getLogger(JspUtil.class.getName());

    private final DishCategoryService dishCategoryService;
    private final DishService dishService;

    public JspUtil(DishService dishService, DishCategoryService dishCategoryService) {
        this.dishService = dishService;
        this.dishCategoryService = dishCategoryService;
    }

    public void setDishAttributeByDishIdParam(HttpServletRequest request) throws SQLException, ConnectionException {

        String dishId = request.getParameter(CommonAppConstants.DISH_ID_JSP_PARAM);
        if (isNull(dishId)) {
            return;
        }
        LOGGER.info(
            String.format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, CommonAppConstants.DISH_ID_JSP_PARAM, dishId));

        Dish dish = dishService.getById(parseLong(dishId));

        request.setAttribute(CommonAppConstants.DISH_JSP_ATTRIBUTE, dish);
        LOGGER.info(
            String.format(LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE, CommonAppConstants.DISH_JSP_ATTRIBUTE, dish.toString()));
    }

    public void setCategoriesAttribute(HttpServletRequest request) throws SQLException, ConnectionException {

        List<DishCategory> categories = dishCategoryService.findAll();
        request.setAttribute(CommonAppConstants.CATEGORY_LIST_JSP_ATTRIBUTE, categories);
        LOGGER.info(String
            .format(LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE, CommonAppConstants.CATEGORY_LIST_JSP_ATTRIBUTE, categories.toString()));
    }
}
