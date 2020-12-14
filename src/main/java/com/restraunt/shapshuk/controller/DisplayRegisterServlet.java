package com.restraunt.shapshuk.controller;

import com.restraunt.shapshuk.command.Command;
import com.restraunt.shapshuk.command.CommandFactory;
import com.restraunt.shapshuk.context.ApplicationContext;
import com.restraunt.shapshuk.context.SecurityContext;
import com.restraunt.shapshuk.util.CommandUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.restraunt.shapshuk.core.constants.CommonAppConstants.*;
import static com.restraunt.shapshuk.core.constants.JspName.*;
import static com.restraunt.shapshuk.core.constants.ServletName.DISPLAY_REGISTER_SERVLET;

@WebServlet(urlPatterns = "/display_register", name = DISPLAY_REGISTER_SERVLET)
public class DisplayRegisterServlet extends HttpServlet {

    private static final long serialVersionUID = -8104780406678115072L;

    private static final Logger LOGGER = Logger.getLogger(DisplayRegisterServlet.class.getName());
    private final CommandFactory commandFactory = ApplicationContext.getInstance().getBean(CommandFactory.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        SecurityContext.getInstance().setSecurityAttributes(req);

        try {
            int success = Integer.parseInt(req.getParameter(QUERY_PARAM_SUCCESS));

            if (success == SUCCESSFULLY) {

                String message = req.getParameter(QUERY_PARAM_MESSAGE);
                req.setAttribute(MESSAGE_JSP_ATTRIBUTE, message);
                req.setAttribute(VIEW_NAME_JSP_PARAM, LOGIN_JSP);
                req.getRequestDispatcher(MAIN_LAYOUT_JSP).forward(req, resp);
            } else {

                String[] errorArray = req.getParameterValues(QUERY_PARAM_ERROR);
                List<String> errorList = Arrays.asList(errorArray);
                req.setAttribute(ERRORS_JSP_ATTRIBUTE, errorList);
                req.setAttribute(VIEW_NAME_JSP_PARAM, REGISTER_JSP);
                req.getRequestDispatcher(MAIN_LAYOUT_JSP).forward(req, resp);
            }
        } catch (NumberFormatException e) {

            LOGGER.error(e.getMessage());

            String commandFromRequest = CommandUtil.getCommandFromRequest(req);
            Command command = commandFactory.getCommand(commandFromRequest);
            String viewName = command.apply(req, resp);

            resp.sendRedirect(String.format(REDIRECT_FORMAT, req.getContextPath(), viewName));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }

}
