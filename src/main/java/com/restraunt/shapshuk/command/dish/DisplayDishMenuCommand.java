package com.restraunt.shapshuk.command.dish;

import com.restraunt.shapshuk.command.Command;
import com.restraunt.shapshuk.context.ApplicationContext;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.dish.model.Dish;
import com.restraunt.shapshuk.entity.dish.service.DishService;
import com.restraunt.shapshuk.util.CategoryNameUtil;
import com.restraunt.shapshuk.util.JspUtil;
import com.restraunt.shapshuk.core.constants.CommonAppConstants;
import com.restraunt.shapshuk.core.constants.JspName;
import com.restraunt.shapshuk.core.constants.LoggerConstants;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

public class DisplayDishMenuCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(DisplayDishMenuCommand.class.getName());

    private final DishService dishService;

    public DisplayDishMenuCommand(DishService dishService) {
        this.dishService = dishService;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws SQLException, ConnectionException {

        JspUtil jspUtil = ApplicationContext.getInstance().getBean(JspUtil.class);
        jspUtil.setCategoriesAttribute(request);

        List<String> categoryNames = CategoryNameUtil.getCategoryNamesFromRequest(request);

        request.setAttribute(CommonAppConstants.SELECTED_CATEGORIES_JSP_ATTRIBUTE, categoryNames);

        if (isDisplayAllCategories(categoryNames)) {

            return displayAllDishes(request);
        }

        return displayFilteredDishes(request, categoryNames);
    }

    private boolean isDisplayAllCategories(List<String> categoryNames) {

        return categoryNames.isEmpty() || categoryNames.get(0).equals(CategoryNameUtil.ALL_CATEGORIES);
    }

    private String displayFilteredDishes(HttpServletRequest request, List<String> categoryNames) throws ConnectionException, SQLException {

        List<Dish> filteredDishes = new ArrayList<>();
        for (String categoryName : categoryNames) {
            List<Dish> dishes = dishService.getByCategory(categoryName);
            if (dishes.isEmpty()) {
                continue;
            }
            filteredDishes.addAll(dishes);
        }

        request.setAttribute(CommonAppConstants.DISHES_JSP_ATTRIBUTE, filteredDishes);
        LOGGER.info(String.format(LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE, CommonAppConstants.DISHES_JSP_ATTRIBUTE, filteredDishes.toString()));
        LOGGER.info("Filtered dishes will be shown");

        return JspName.DISH_MENU_JSP;
    }

    private String displayAllDishes(HttpServletRequest request) throws SQLException, ConnectionException {

        String currentPageString = request.getParameter(CommonAppConstants.QUERY_PARAM_PAGE);

        int currentPage;

        if (Objects.isNull(currentPageString) || currentPageString.isEmpty()) {
            currentPage = CommonAppConstants.FIRST_PAGE;
        } else {
            currentPage = Integer.parseInt(currentPageString);
        }

        List<Dish> all = dishService.findAll(currentPage, CommonAppConstants.RECORDS_PER_PAGE);
        request.setAttribute(CommonAppConstants.DISHES_JSP_ATTRIBUTE, all);
        LOGGER.info(String.format(LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE, CommonAppConstants.DISHES_JSP_ATTRIBUTE, all));
        LOGGER.info("All dishes will be shown");

        int numberOfPages = getNumberOfPages();
        request.setAttribute(CommonAppConstants.NUMBER_OF_PAGES_JSP_ATTRIBUTE, numberOfPages);
        LOGGER.info(String.format(LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE, CommonAppConstants.NUMBER_OF_PAGES_JSP_ATTRIBUTE, numberOfPages));

        request.setAttribute(CommonAppConstants.CURRENT_PAGE_JSP_ATTRIBUTE, currentPage);
        LOGGER.info(String.format(LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE, CommonAppConstants.CURRENT_PAGE_JSP_ATTRIBUTE, currentPage));

        return JspName.DISH_MENU_JSP;
    }

    private int getNumberOfPages() throws ConnectionException, SQLException {

        int rows = dishService.getNumberOfRows();
        int numberOfPages = rows / CommonAppConstants.RECORDS_PER_PAGE;
        if (numberOfPages % CommonAppConstants.RECORDS_PER_PAGE > 0) {
            numberOfPages++;
        }
        return numberOfPages;
    }

}
