package com.restraunt.shapshuk.controller;

import com.restraunt.shapshuk.command.Command;
import com.restraunt.shapshuk.command.CommandFactory;
import com.restraunt.shapshuk.command.CommandType;
import com.restraunt.shapshuk.context.ApplicationContext;
import com.restraunt.shapshuk.context.SecurityContext;
import com.restraunt.shapshuk.util.CommandUtil;
import com.restraunt.shapshuk.core.constants.CommandReturnValues;
import com.restraunt.shapshuk.core.constants.JspName;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.restraunt.shapshuk.core.constants.CommonAppConstants.VIEW_NAME_JSP_PARAM;
import static com.restraunt.shapshuk.core.constants.ServletName.*;

@WebServlet(urlPatterns = "/order_basket", name = ORDER_BASKET_SERVLET)
public class OrderBasketServlet extends HttpServlet {

    private static final long serialVersionUID = -79412450294725257L;
    private final CommandFactory commandFactory = ApplicationContext.getInstance().getBean(CommandFactory.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String commandFromRequest = CommandUtil.getCommandFromRequest(req);
        Command command = commandFactory.getCommand(commandFromRequest);
        String viewName = command.apply(req, resp);

        SecurityContext.getInstance().setSecurityAttributes(req);

        switch (viewName) {
            case LOGIN_SERVLET:
            case MENU_SERVLET:
            case ORDER_BASKET_SERVLET:
            case ORDER_CHECKOUT_SERVLET:
                resp.sendRedirect(req.getContextPath() + "/" + viewName);
                break;
            case CommandReturnValues.LOGOUT_RESULT:
                resp.sendRedirect(req.getContextPath());
                break;
            case JspName.DEFAULT_JSP:
            case JspName.ORDER_ITEM_LIST_JSP:
                String commandName = String.valueOf(CommandType.DISPLAY_ORDER_ITEM_LIST_COMMAND);
                command = commandFactory.getCommand(commandName);
                String commandResult = command.apply(req, resp);
                req.setAttribute(VIEW_NAME_JSP_PARAM, commandResult);
                req.getRequestDispatcher(JspName.MAIN_LAYOUT_JSP).forward(req, resp);
                break;
            default:
                req.setAttribute(VIEW_NAME_JSP_PARAM, viewName);
                req.getRequestDispatcher(JspName.MAIN_LAYOUT_JSP).forward(req, resp);
                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }

}
