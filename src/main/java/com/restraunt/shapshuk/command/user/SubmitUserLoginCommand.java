package com.restraunt.shapshuk.command.user;

import com.restraunt.shapshuk.command.Command;
import com.restraunt.shapshuk.context.SecurityContext;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.user.model.User;
import com.restraunt.shapshuk.entity.user.service.UserService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Objects;

import static com.restraunt.shapshuk.core.constants.CommonAppConstants.*;
import static com.restraunt.shapshuk.core.constants.JspName.COMMAND_RESULT_MESSAGE_JSP;
import static com.restraunt.shapshuk.core.constants.JspName.LOGIN_JSP;
import static com.restraunt.shapshuk.core.constants.LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE;
import static com.restraunt.shapshuk.core.constants.LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE;
import static com.restraunt.shapshuk.util.Md5EncryptingUtil.encrypt;
import static java.lang.String.format;

public class SubmitUserLoginCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(SubmitUserLoginCommand.class.getName());

    private final UserService userService;

    public SubmitUserLoginCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws SQLException, ConnectionException {

        String email = request.getParameter(USER_EMAIL_JSP_PARAM);
        LOGGER.info(format(PARAM_GOT_FROM_JSP_MESSAGE, USER_EMAIL_JSP_PARAM, email));

        String password = request.getParameter(USER_PASSWORD_JSP_PARAM);
        LOGGER.info(format(PARAM_GOT_FROM_JSP_MESSAGE, USER_PASSWORD_JSP_PARAM, "NOT SHOWN"));

        String encryptedPassword;
        try {

            encryptedPassword = encrypt(password);
        } catch (NoSuchAlgorithmException e) {

            request.setAttribute(ERRORS_JSP_ATTRIBUTE, Collections.singletonList(e.getMessage()));
            LOGGER.info(format(ATTRIBUTE_SET_TO_JSP_MESSAGE, ERRORS_JSP_ATTRIBUTE, e.getMessage()));
            LOGGER.error(e.getMessage());

            return LOGIN_JSP;
        }

        LOGGER.info(format(PARAM_GOT_FROM_JSP_MESSAGE, USER_PASSWORD_JSP_PARAM, encryptedPassword));

        User user = userService.login(email, encryptedPassword);

        if (Objects.isNull(user)) {

            String message = "Wrong email address or password";
            request.setAttribute(ERRORS_JSP_ATTRIBUTE, Collections.singletonList(message));
            LOGGER.info(format(ATTRIBUTE_SET_TO_JSP_MESSAGE, ERRORS_JSP_ATTRIBUTE, message));

            return LOGIN_JSP;
        }

        String sessionId = request.getSession().getId();
        SecurityContext.getInstance().login(user, sessionId);
        LOGGER.info(format("User with [%s - %s] has logged in", "email", user.getEmail()));

        String message = "You have logged in successfully";
        request.setAttribute(MESSAGE_JSP_ATTRIBUTE, message);
        LOGGER.info(format(ATTRIBUTE_SET_TO_JSP_MESSAGE, MESSAGE_JSP_ATTRIBUTE, message));

        return COMMAND_RESULT_MESSAGE_JSP;
    }
}
