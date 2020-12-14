package com.restraunt.shapshuk.command.user;

import com.restraunt.shapshuk.command.Command;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.user.model.User;
import com.restraunt.shapshuk.entity.user.service.UserService;
import com.restraunt.shapshuk.validation.BeanValidator;
import com.restraunt.shapshuk.validation.BrokenField;
import com.restraunt.shapshuk.validation.ValidationResult;
import com.restraunt.shapshuk.validation.CreateMessageUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

import static com.restraunt.shapshuk.core.constants.CommonAppConstants.*;
import static com.restraunt.shapshuk.core.constants.JspName.COMMAND_RESULT_MESSAGE_JSP;
import static com.restraunt.shapshuk.core.constants.JspName.REGISTER_JSP;
import static com.restraunt.shapshuk.core.constants.LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE;
import static com.restraunt.shapshuk.core.constants.LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE;
import static com.restraunt.shapshuk.core.constants.ServletName.DISPLAY_REGISTER_SERVLET;
import static com.restraunt.shapshuk.util.Md5EncryptingUtil.encrypt;
import static java.lang.String.format;

public class SubmitUserRegisterCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(SubmitUserRegisterCommand.class.getName());

    private final UserService userService;
    private final BeanValidator validator;

    public SubmitUserRegisterCommand(UserService userService, BeanValidator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ConnectionException {

        String name = request.getParameter(USER_NAME_JSP_PARAM);
        LOGGER.info(format(PARAM_GOT_FROM_JSP_MESSAGE, USER_NAME_JSP_PARAM, name));

        String email = request.getParameter(USER_EMAIL_JSP_PARAM);
        LOGGER.info(format(PARAM_GOT_FROM_JSP_MESSAGE, USER_EMAIL_JSP_PARAM, email));

        String password = request.getParameter(USER_PASSWORD_JSP_PARAM);
        LOGGER.info(format(PARAM_GOT_FROM_JSP_MESSAGE, USER_PASSWORD_JSP_PARAM, "NOT SHOWN"));

        String passwordConfirm = request.getParameter(USER_PASSWORD_CONFIRM_JSP_PARAM);
        LOGGER.info(format(PARAM_GOT_FROM_JSP_MESSAGE, USER_PASSWORD_CONFIRM_JSP_PARAM, "NOT SHOWN"));

        if (passwordConfirm.trim().isEmpty() || !password.equals(passwordConfirm)) {
            String message = "You haven't confirmed password correctly";
            int status = UNSUCCESSFULLY;
            response.sendRedirect(format(REGISTER_REDIRECT_WITH_PARAMS_FORMAT, request.getContextPath(), DISPLAY_REGISTER_SERVLET, QUERY_PARAM_SUCCESS, status, QUERY_PARAM_ERROR, message));
            return REGISTER_JSP;
        }

        String phoneNumber = request.getParameter(USER_PHONE_NUMBER_JSP_PARAM);
        LOGGER.info(format(PARAM_GOT_FROM_JSP_MESSAGE, USER_PHONE_NUMBER_JSP_PARAM, phoneNumber));

        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setPhoneNumber(phoneNumber);

        ValidationResult validationResult = validator.validate(newUser);
        List<BrokenField> brokenFields = validationResult.getBrokenFields();

        if (brokenFields.isEmpty()) {

            String encryptedPassword;

            try {

                encryptedPassword = encrypt(password);
            } catch (NoSuchAlgorithmException e) {

                request.setAttribute(ERROR_JSP_ATTRIBUTE, e.getMessage());
                LOGGER.info(format(ATTRIBUTE_SET_TO_JSP_MESSAGE, ERROR_JSP_ATTRIBUTE, e.getMessage()));
                LOGGER.error(e.getMessage());

                int status = UNSUCCESSFULLY;
                response.sendRedirect(format(REGISTER_REDIRECT_WITH_PARAMS_FORMAT, request.getContextPath(), DISPLAY_REGISTER_SERVLET, QUERY_PARAM_SUCCESS, status, QUERY_PARAM_ERROR, e.getMessage()));

                return REGISTER_JSP;
            }

            newUser.setPassword(encryptedPassword);

            List<User> users = userService.findAll();

            for (User user : users) {

                if (isUserNameRegistered(request, response, name, user)) {
                    return REGISTER_JSP;
                }

                if (isUserEmailRegistered(request, response, email, user)) {
                    return REGISTER_JSP;
                }
            }

            String address = request.getParameter(USER_ADDRESS_JSP_PARAM);
            LOGGER.info(format(PARAM_GOT_FROM_JSP_MESSAGE, USER_ADDRESS_JSP_PARAM, address));
            newUser.getUserAddress().setFullAddress(address);

            userService.register(newUser);

            String message = "You have been registered successfully";
            LOGGER.info(message);

            int status = SUCCESSFULLY;
            response.sendRedirect(format(REGISTER_REDIRECT_WITH_PARAMS_FORMAT, request.getContextPath(), DISPLAY_REGISTER_SERVLET, QUERY_PARAM_SUCCESS, status, QUERY_PARAM_MESSAGE, message));

            return COMMAND_RESULT_MESSAGE_JSP;

        } else {

            String message = CreateMessageUtil.createUrlMessage(brokenFields);
            LOGGER.error(message);

            int status = UNSUCCESSFULLY;
            String formatRedirectUrl = format(REGISTER_REDIRECT_WITH_ERROR_PARAMS_FORMAT, request.getContextPath(), DISPLAY_REGISTER_SERVLET, QUERY_PARAM_SUCCESS, status, message);
            response.sendRedirect(formatRedirectUrl);

            return REGISTER_JSP;
        }

    }

    private boolean isUserNameRegistered(HttpServletRequest request, HttpServletResponse response, String name, User user) throws IOException {

        if (user.getName().equalsIgnoreCase(name)) {

            String message = "User with this name has been registered";
            LOGGER.error(message);

            int status = UNSUCCESSFULLY;
            response.sendRedirect(format(REGISTER_REDIRECT_WITH_PARAMS_FORMAT, request.getContextPath(), DISPLAY_REGISTER_SERVLET, QUERY_PARAM_SUCCESS, status, QUERY_PARAM_ERROR, message));

            return true;
        }
        return false;
    }

    private boolean isUserEmailRegistered(HttpServletRequest request, HttpServletResponse response, String email, User user) throws IOException {

        if (user.getEmail().equalsIgnoreCase(email)) {

            String message = "User with this email has been registered";
            LOGGER.error(message);

            int status = UNSUCCESSFULLY;
            response.sendRedirect(format(REGISTER_REDIRECT_WITH_PARAMS_FORMAT, request.getContextPath(), DISPLAY_REGISTER_SERVLET, QUERY_PARAM_SUCCESS, status, QUERY_PARAM_ERROR, message));

            return true;
        }
        return false;
    }

}
