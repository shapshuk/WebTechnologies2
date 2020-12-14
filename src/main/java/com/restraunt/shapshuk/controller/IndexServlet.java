package com.restraunt.shapshuk.controller;

import com.restraunt.shapshuk.command.Command;
import com.restraunt.shapshuk.command.CommandFactory;
import com.restraunt.shapshuk.context.ApplicationContext;
import com.restraunt.shapshuk.context.SecurityContext;
import com.restraunt.shapshuk.util.CommandUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.restraunt.shapshuk.core.constants.CommandReturnValues.LOGOUT_RESULT;
import static com.restraunt.shapshuk.core.constants.CommonAppConstants.VIEW_NAME_JSP_PARAM;
import static com.restraunt.shapshuk.core.constants.JspName.MAIN_LAYOUT_JSP;
import static com.restraunt.shapshuk.core.constants.ServletName.*;

@MultipartConfig
@WebServlet(urlPatterns = "/", name = INDEX_SERVLET)
public class IndexServlet extends HttpServlet {

    private static final long serialVersionUID = 6154677369722697748L;
    private final CommandFactory commandFactory = ApplicationContext.getInstance().getBean(CommandFactory.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String commandFromRequest = CommandUtil.getCommandFromRequest(req);
        Command command = commandFactory.getCommand(commandFromRequest);
        String viewName = command.apply(req, resp);
/*
todo
    1. При удалении категории предупреждать что надо удалить эту категорию из всех блюд
    2. При отправки запроса /menu&_page=4 или _page=-1 или _page=a выбрасывается exception
    3. При добавлении блюда в корзину оставаться на той же странице, на которой блюдо находилось
 */
        SecurityContext.getInstance().setSecurityAttributes(req);

        switch (viewName) {
            case LOGIN_SERVLET:
            case MENU_SERVLET:
            case USER_REGISTER_SERVLET:
            case ORDER_BASKET_SERVLET:
            case ORDER_CHECKOUT_SERVLET:
                resp.sendRedirect(req.getContextPath() + "/" + viewName);
                break;
            case LOGOUT_RESULT:
                resp.sendRedirect(req.getContextPath());
                break;
            default:
                req.setAttribute(VIEW_NAME_JSP_PARAM, viewName);
                req.getRequestDispatcher(MAIN_LAYOUT_JSP).forward(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }

}
