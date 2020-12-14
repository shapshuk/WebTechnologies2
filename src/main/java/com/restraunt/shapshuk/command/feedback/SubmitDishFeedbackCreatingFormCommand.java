package com.restraunt.shapshuk.command.feedback;

import com.restraunt.shapshuk.command.Command;
import com.restraunt.shapshuk.context.SecurityContext;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.dishfeedback.model.DishFeedback;
import com.restraunt.shapshuk.entity.dishfeedback.service.DishFeedbackService;
import com.restraunt.shapshuk.entity.user.model.User;
import com.restraunt.shapshuk.core.constants.CommonAppConstants;
import com.restraunt.shapshuk.core.constants.JspName;
import com.restraunt.shapshuk.core.constants.LoggerConstants;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.String.format;
import static java.util.Objects.isNull;

public class SubmitDishFeedbackCreatingFormCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(SubmitDishFeedbackCreatingFormCommand.class.getName());

    private final DishFeedbackService dishFeedbackService;

    public SubmitDishFeedbackCreatingFormCommand(DishFeedbackService dishFeedbackService) {
        this.dishFeedbackService = dishFeedbackService;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws SQLException, ConnectionException {

        String rating = request.getParameter(CommonAppConstants.FEEDBACK_RATING_JSP_PARAM);
        LOGGER.info(String.format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, CommonAppConstants.FEEDBACK_RATING_JSP_PARAM, rating));

        String comment = request.getParameter(CommonAppConstants.FEEDBACK_TEXT_JSP_PARAM);
        LOGGER.info(String.format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, CommonAppConstants.FEEDBACK_TEXT_JSP_PARAM, comment));

        String dishIdString = request.getParameter(CommonAppConstants.DISH_ID_JSP_PARAM);
        LOGGER.info(String.format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, CommonAppConstants.DISH_ID_JSP_PARAM, dishIdString));

        String sessionId = request.getSession().getId();
        User currentUser = SecurityContext.getInstance().getCurrentUser(sessionId);
        Long currentUserId = currentUser.getId();

        DishFeedback dishFeedback = dishFeedbackService.getByUserIdAndDishId(currentUserId, parseLong(dishIdString));

        String message;
        if (isNull(dishFeedback)) {

            saveNewFeedback(currentUserId, rating, comment, dishIdString);
            message = "New feedback saved";
        } else {

            updateExistingFeedback(rating, comment, dishFeedback);
            message = "Existing feedback updated";
        }

        LOGGER.info(message);
        request.setAttribute(CommonAppConstants.MESSAGE_JSP_ATTRIBUTE, message);
        LOGGER.info(String.format(LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE, CommonAppConstants.MESSAGE_JSP_ATTRIBUTE, message));

        return JspName.COMMAND_RESULT_MESSAGE_JSP;
    }

    private void updateExistingFeedback(String rating, String comment, DishFeedback dishFeedback) throws SQLException, ConnectionException {

        dishFeedback.setDishRating(parseInt(rating));
        dishFeedback.setDishComment(comment);
        dishFeedbackService.update(dishFeedback);
    }

    private void saveNewFeedback(Long currentUserId, String rating, String comment, String dishIdString) throws SQLException, ConnectionException {

        DishFeedback dishFeedback = new DishFeedback();
        dishFeedback.setUserId(currentUserId);
        dishFeedback.setDishRating(parseInt(rating));
        dishFeedback.setDishComment(comment);
        dishFeedback.setDishId(parseLong(dishIdString));
        dishFeedbackService.save(dishFeedback);
    }
}
