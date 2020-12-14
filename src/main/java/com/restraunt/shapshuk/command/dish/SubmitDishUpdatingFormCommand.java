package com.restraunt.shapshuk.command.dish;

import com.restraunt.shapshuk.command.Command;
import com.restraunt.shapshuk.context.ApplicationContext;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.category.model.DishCategory;
import com.restraunt.shapshuk.entity.category.service.DishCategoryService;
import com.restraunt.shapshuk.entity.dish.model.Dish;
import com.restraunt.shapshuk.entity.dish.service.DishService;
import com.restraunt.shapshuk.util.JspUtil;
import com.restraunt.shapshuk.util.PictureEncodingUtil;
import com.restraunt.shapshuk.validation.BeanValidator;
import com.restraunt.shapshuk.validation.BrokenField;
import com.restraunt.shapshuk.validation.ValidationResult;
import com.restraunt.shapshuk.validation.CreateMessageUtil;
import com.restraunt.shapshuk.core.constants.CommonAppConstants;
import com.restraunt.shapshuk.core.constants.JspName;
import com.restraunt.shapshuk.core.constants.LoggerConstants;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static java.lang.Long.parseLong;
import static java.lang.String.format;

public class SubmitDishUpdatingFormCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(SubmitDishUpdatingFormCommand.class.getName());

    private final DishService dishService;
    private final DishCategoryService dishCategoryService;
    private final BeanValidator validator;

    public SubmitDishUpdatingFormCommand(DishService dishService, DishCategoryService dishCategoryService, BeanValidator validator) {
        this.dishService = dishService;
        this.dishCategoryService = dishCategoryService;
        this.validator = validator;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, ConnectionException {

        String dishIdString = request.getParameter(CommonAppConstants.DISH_ID_JSP_PARAM);
        LOGGER.info(String.format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, CommonAppConstants.DISH_ID_JSP_PARAM, dishIdString));

        Dish dish = dishService.getById(parseLong(dishIdString));

        updateDishName(request, dish);
        if (!updateDishCost(request, dish)) {
            return JspName.UPDATE_DISH_FORM_JSP;
        }
        updateDishDescription(request, dish);
        updateDishCategory(request, dish);
        updateDishPicture(request, dish);

        ValidationResult validationResult = validator.validate(dish);
        List<BrokenField> brokenFields = validationResult.getBrokenFields();

        if (brokenFields.isEmpty()) {

            dishService.update(dish);

            String message = "Your dish has been updated!";
            request.setAttribute(CommonAppConstants.MESSAGE_JSP_ATTRIBUTE, message);
            LOGGER.info(String.format(LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE, CommonAppConstants.MESSAGE_JSP_ATTRIBUTE, message));

            return JspName.COMMAND_RESULT_MESSAGE_JSP;

        } else {

            List<String> message = CreateMessageUtil.createPageMessageList(brokenFields);

            return createReturnAnswer(request, message);
        }
    }

    private void updateDishPicture(HttpServletRequest request, Dish dish) throws IOException, ServletException {

        Part picture = request.getPart(CommonAppConstants.DISH_PICTURE_JSP_PARAM);
        try {

            String stringPicture = PictureEncodingUtil.getPictureEncoded(picture);
            dish.setPicture(stringPicture);
            LOGGER.info(String.format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, CommonAppConstants.DISH_PICTURE_JSP_PARAM, stringPicture.substring(0, 20)));
            LOGGER.info("Dish picture has been updated");

        } catch (IllegalArgumentException e) {

            String message = String.format("%s: %s", "Dish picture hasn't been updated", e.getMessage());
            LOGGER.info(message);
        }
    }

    private void updateDishCategory(HttpServletRequest request, Dish dish) {

        String category = request.getParameter(CommonAppConstants.DISH_CATEGORY_NAME_JSP_PARAM);
        LOGGER.info(String.format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, CommonAppConstants.DISH_CATEGORY_NAME_JSP_PARAM, category));

        if (!category.trim().isEmpty()) {

            DishCategory dishCategory = new DishCategory();
            dishCategory.setCategoryName(category);
            dish.setDishCategory(dishCategory);
        }
    }

    private void updateDishDescription(HttpServletRequest request, Dish dish) {

        String description = request.getParameter(CommonAppConstants.DISH_DESCRIPTION_JSP_PARAM);
        LOGGER.info(String.format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, CommonAppConstants.DISH_DESCRIPTION_JSP_PARAM, description));

        if (!description.trim().isEmpty()) {

            dish.setDescription(description);
        }
    }

    private boolean updateDishCost(HttpServletRequest request, Dish dish) throws SQLException, ConnectionException {

        String costString = request.getParameter(CommonAppConstants.DISH_COST_JSP_PARAM);
        LOGGER.info(String.format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, CommonAppConstants.DISH_COST_JSP_PARAM, costString));

        if (!costString.trim().isEmpty()) {

            BigDecimal bigDecimalCost;
            try {

                bigDecimalCost = new BigDecimal(costString);
            } catch (NumberFormatException e) {

                request.setAttribute(CommonAppConstants.ERROR_JSP_ATTRIBUTE, "Invalid cost format");
                List<DishCategory> dishCategories = dishCategoryService.findAll();
                request.setAttribute(CommonAppConstants.CATEGORY_LIST_JSP_ATTRIBUTE, dishCategories);
                return false;
            }

            dish.setCost(bigDecimalCost);
        }
        return true;
    }

    private void updateDishName(HttpServletRequest request, Dish dish) {

        String name = request.getParameter(CommonAppConstants.DISH_NAME_JSP_PARAM);
        LOGGER.info(String.format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, CommonAppConstants.DISH_NAME_JSP_PARAM, name));

        if (!name.trim().isEmpty()) {

            dish.setName(name);
        }
    }

    private String createReturnAnswer(HttpServletRequest request, List<String> messages) throws SQLException, ConnectionException {

        request.setAttribute(CommonAppConstants.ERRORS_JSP_ATTRIBUTE, messages);
        LOGGER.info(String.format(LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE, CommonAppConstants.ERRORS_JSP_ATTRIBUTE, messages));
        LOGGER.error(messages);

        JspUtil jspUtil = ApplicationContext.getInstance().getBean(JspUtil.class);
        jspUtil.setCategoriesAttribute(request);
        jspUtil.setDishAttributeByDishIdParam(request);

        return JspName.UPDATE_DISH_FORM_JSP;
    }
}
