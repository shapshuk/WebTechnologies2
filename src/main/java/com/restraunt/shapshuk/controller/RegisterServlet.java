package com.restraunt.shapshuk.controller;

import com.restraunt.shapshuk.command.Command;
import com.restraunt.shapshuk.command.CommandFactory;
import com.restraunt.shapshuk.context.ApplicationContext;
import com.restraunt.shapshuk.context.SecurityContext;
import com.restraunt.shapshuk.util.CommandUtil;
import com.restraunt.shapshuk.core.constants.CommandReturnValues;
import com.restraunt.shapshuk.core.constants.CommonAppConstants;
import com.restraunt.shapshuk.core.constants.JspName;
import com.restraunt.shapshuk.core.constants.ServletName;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/user_register", name = ServletName.USER_REGISTER_SERVLET)
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = -8104780406678115072L;
    private final CommandFactory commandFactory = ApplicationContext.getInstance().getBean(CommandFactory.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String commandFromRequest = CommandUtil.getCommandFromRequest(req);
        Command command = commandFactory.getCommand(commandFromRequest);
        String viewName = command.apply(req, resp);

        SecurityContext.getInstance().setSecurityAttributes(req);

        switch (viewName) {
            case ServletName.LOGIN_SERVLET:
            case ServletName.MENU_SERVLET:
            case ServletName.USER_REGISTER_SERVLET:
            case ServletName.ORDER_BASKET_SERVLET:
            case ServletName.ORDER_CHECKOUT_SERVLET:
                resp.sendRedirect(req.getContextPath() + "/" + viewName);
                break;
            case CommandReturnValues.LOGOUT_RESULT:
                resp.sendRedirect(req.getContextPath());
                break;
            case JspName.DEFAULT_JSP:
                req.setAttribute(CommonAppConstants.VIEW_NAME_JSP_PARAM, JspName.REGISTER_JSP);
                req.getRequestDispatcher(JspName.MAIN_LAYOUT_JSP).forward(req, resp);
                break;
            default:
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}
