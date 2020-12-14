package com.restraunt.shapshuk.command.dish;

import com.restraunt.shapshuk.command.Command;
import com.restraunt.shapshuk.context.ApplicationContext;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.category.model.DishCategory;
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
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

public class SubmitDishCreatingFormCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(SubmitDishCreatingFormCommand.class.getName());

    private final DishService dishService;
    private final BeanValidator validator;

    public SubmitDishCreatingFormCommand(DishService dishService, BeanValidator validator) {
        this.dishService = dishService;
        this.validator = validator;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, ConnectionException {

        String name = request.getParameter(CommonAppConstants.DISH_NAME_JSP_PARAM);
        LOGGER.info(String.format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, CommonAppConstants.DISH_NAME_JSP_PARAM, name));

        List<Dish> dishes = dishService.findAll();

        for (Dish dish : dishes) {

            if (dish.getName().equalsIgnoreCase(name)) {

                String message = "Please, choose another name, dish with this name is already exist";
                request.setAttribute(CommonAppConstants.ERROR_JSP_ATTRIBUTE, message);
                LOGGER.error(message);

                return createReturnAnswer(request, Collections.singletonList(message));
            }
        }

        String costString = request.getParameter(CommonAppConstants.DISH_COST_JSP_PARAM);
        LOGGER.info(String.format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, CommonAppConstants.DISH_COST_JSP_PARAM, costString));

        BigDecimal bigDecimalCost;
        try {

            bigDecimalCost = new BigDecimal(costString);
        } catch (NumberFormatException e) {

            String message = "Invalid cost format or empty cost field";
            return createReturnAnswer(request, Collections.singletonList(message));
        }

        String description = request.getParameter(CommonAppConstants.DISH_DESCRIPTION_JSP_PARAM);
        LOGGER.info(String.format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, CommonAppConstants.DISH_DESCRIPTION_JSP_PARAM, description));

        String categoryName = request.getParameter(CommonAppConstants.DISH_CATEGORY_NAME_JSP_PARAM);
        LOGGER.info(String.format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, CommonAppConstants.DISH_CATEGORY_NAME_JSP_PARAM, categoryName));

        Dish dish = new Dish();
        dish.setName(name);
        dish.setCost(bigDecimalCost);
        dish.setDescription(description);
        DishCategory dishCategory = new DishCategory();
        dishCategory.setCategoryName(categoryName);
        dish.setDishCategory(dishCategory);
        setDishPicture(request, dish);

        return validateFields(request, dish);
    }

    private String validateFields(HttpServletRequest request, Dish dish) throws SQLException, ConnectionException {

        ValidationResult validationResult = validator.validate(dish);
        List<BrokenField> brokenFields = validationResult.getBrokenFields();

        if (brokenFields.isEmpty()) {

            dishService.save(dish);

            String message = "Your dish has been created and added to menu";
            request.setAttribute(CommonAppConstants.MESSAGE_JSP_ATTRIBUTE, message);
            LOGGER.info(String.format(LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE, CommonAppConstants.MESSAGE_JSP_ATTRIBUTE, message));

            return JspName.COMMAND_RESULT_MESSAGE_JSP;

        } else {

            List<String> message = CreateMessageUtil.createPageMessageList(brokenFields);

            return createReturnAnswer(request, message);
        }
    }

    private void setDishPicture(HttpServletRequest request, Dish dish) throws IOException, ServletException {

        Part picture = request.getPart(CommonAppConstants.DISH_PICTURE_JSP_PARAM);
        try {

            String stringPicture = PictureEncodingUtil.getPictureEncoded(picture);
            dish.setPicture(stringPicture);
            LOGGER.info(String.format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, CommonAppConstants.DISH_PICTURE_JSP_PARAM, stringPicture.substring(0, 20)));
            LOGGER.info("Dish picture has been uploaded");

        } catch (IllegalArgumentException e) {

            LOGGER.info(String.format("%s: %s", "Dish picture hasn't been uploaded", e.getMessage()));
        }
    }

    private String createReturnAnswer(HttpServletRequest request, List<String> messages) throws SQLException, ConnectionException {

        request.setAttribute(CommonAppConstants.ERRORS_JSP_ATTRIBUTE, messages);
        LOGGER.info(String.format(LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE, CommonAppConstants.ERRORS_JSP_ATTRIBUTE, messages));
        LOGGER.error(messages);

        JspUtil jspUtil = ApplicationContext.getInstance().getBean(JspUtil.class);
        jspUtil.setCategoriesAttribute(request);

        return JspName.CREATE_DISH_FORM_JSP;
    }

}
