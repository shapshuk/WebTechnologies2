package com.restraunt.shapshuk.command.category;

import com.restraunt.shapshuk.command.Command;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.category.model.DishCategory;
import com.restraunt.shapshuk.entity.category.service.DishCategoryService;
import com.restraunt.shapshuk.validation.BeanValidator;
import com.restraunt.shapshuk.validation.BrokenField;
import com.restraunt.shapshuk.validation.ValidationResult;
import com.restraunt.shapshuk.validation.CreateMessageUtil;
import com.restraunt.shapshuk.core.constants.CommonAppConstants;
import com.restraunt.shapshuk.core.constants.JspName;
import com.restraunt.shapshuk.core.constants.LoggerConstants;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

public class SubmitDishCategoryCreatingFormCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(SubmitDishCategoryCreatingFormCommand.class.getName());

    private final DishCategoryService dishCategoryService;
    private final BeanValidator validator;

    public SubmitDishCategoryCreatingFormCommand(DishCategoryService dishCategoryService, BeanValidator validator) {
        this.dishCategoryService = dishCategoryService;
        this.validator = validator;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws SQLException, ConnectionException {

        String categoryName = request.getParameter(CommonAppConstants.CATEGORY_NAME_JSP_PARAM);
        LOGGER.info(String.format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, CommonAppConstants.CATEGORY_NAME_JSP_PARAM, categoryName));

        if (!isUniqueCategoryName(request, categoryName)) {
            return JspName.CREATE_CATEGORY_FORM_JSP;
        }

        DishCategory dishCategory = new DishCategory();
        dishCategory.setCategoryName(categoryName);

        return validateFields(request, dishCategory);
    }

    private String validateFields(HttpServletRequest request, DishCategory dishCategory) throws SQLException, ConnectionException {

        ValidationResult validationResult = validator.validate(dishCategory);
        List<BrokenField> brokenFields = validationResult.getBrokenFields();

        if (brokenFields.isEmpty()) {

            dishCategoryService.save(dishCategory);

            String message = "New category has been added to menu";
            request.setAttribute(CommonAppConstants.MESSAGE_JSP_ATTRIBUTE, message);
            LOGGER.info(String.format(LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE, CommonAppConstants.MESSAGE_JSP_ATTRIBUTE, message));

            return JspName.COMMAND_RESULT_MESSAGE_JSP;
        } else {

            List<String> messages = CreateMessageUtil.createPageMessageList(brokenFields);
            request.setAttribute(CommonAppConstants.ERRORS_JSP_ATTRIBUTE, messages);
            LOGGER.info(String.format(LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE, CommonAppConstants.ERRORS_JSP_ATTRIBUTE, messages));
            LOGGER.error(messages);

            return JspName.CREATE_CATEGORY_FORM_JSP;
        }
    }

    private boolean isUniqueCategoryName(HttpServletRequest request, String categoryName) throws SQLException, ConnectionException {

        List<DishCategory> dishCategories = dishCategoryService.findAll();

        for (DishCategory category : dishCategories) {

            if (category.getCategoryName().equalsIgnoreCase(categoryName)) {

                String message = "This category is already exist";
                request.setAttribute(CommonAppConstants.ERRORS_JSP_ATTRIBUTE, Collections.singletonList(message));
                LOGGER.info(String.format(LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE, CommonAppConstants.ERRORS_JSP_ATTRIBUTE, message));
                LOGGER.error(message);

                return false;
            }
        }
        return true;
    }
}
