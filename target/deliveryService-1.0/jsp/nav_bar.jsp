<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.market.angelsel.command.CommandType" %>
<%@ page import="com.market.angelsel.core.constants.CommonAppConstants" %>

<aside class="menu">

    <jsp:useBean id="securityContext" scope="application" class="com.market.angelsel.context.SecurityContext"/>
    <div class="navbar-brand">
        <c:choose>
            <c:when test="${userLoggedIn}">
                <div class="navbar-start navBarContainer">
                    <div class="navbar navBarBackground">

                        <a class="navbar-item button is-light secondary"
                           href="?${CommonAppConstants.QUERY_PARAM_COMMAND}=${CommandType.MENU_SERVLET_SWITCH}"><fmt:message
                                key="links.dish.menu"/></a>

                        <a class="navbar-item button is-light secondary"
                           href="?${CommonAppConstants.QUERY_PARAM_COMMAND}=${CommandType.ORDER_BASKET_SERVLET_SWITCH}"><fmt:message
                                key="links.basket.display"/></a>

                        <a class="navbar-item button is-light secondary"
                           href="?${CommonAppConstants.QUERY_PARAM_COMMAND}=${CommandType.ORDER_CHECKOUT_SERVLET_SWITCH}"><fmt:message
                                key="links.order.checkout"/></a>

                        <a class="navbar-item button is-light secondary"
                           href="?${CommonAppConstants.QUERY_PARAM_COMMAND}=${CommandType.DISPLAY_FILLING_UP_WALLET_FORM_COMMAND}"><fmt:message
                                key="links.wallet.money.add"/></a>
                    </div>

                    <div class="navbar navBarBackground marginTop">
                        <c:if test="${securityContext.canExecute(CommandType.DISPLAY_DISH_CATEGORY_CREATING_FORM_COMMAND, sessionId)}">
                            <a class="navbar-item button is-light secondary"
                               href="?${CommonAppConstants.QUERY_PARAM_COMMAND}=${CommandType.DISPLAY_DISH_CATEGORY_CREATING_FORM_COMMAND}"><fmt:message
                                    key="links.category.create"/></a>
                        </c:if>
                        <c:if test="${securityContext.canExecute(CommandType.DISPLAY_DISH_CATEGORY_DELETING_FORM_COMMAND, sessionId)}">
                            <a class="navbar-item button is-light secondary"
                               href="?${CommonAppConstants.QUERY_PARAM_COMMAND}=${CommandType.DISPLAY_DISH_CATEGORY_DELETING_FORM_COMMAND}"><fmt:message
                                    key="links.category.delete"/></a>
                        </c:if>
                        <c:if test="${securityContext.canExecute(CommandType.DISPLAY_DISH_CREATING_FORM_COMMAND, sessionId)}">
                            <a class="navbar-item button is-light secondary"
                               href="?${CommonAppConstants.QUERY_PARAM_COMMAND}=${CommandType.DISPLAY_DISH_CREATING_FORM_COMMAND}"><fmt:message
                                    key="links.dish.create"/></a>
                        </c:if>
                    </div>
                </div>

                <div class="navbar-end"><a class="navbar-item button is-light secondary"
                                           href="?${CommonAppConstants.QUERY_PARAM_COMMAND}=${CommandType.LOGOUT}"><fmt:message
                        key="links.person.logout"/></a>
                </div>

            </c:when>
            <c:otherwise>
                <div class="navbar-start">
                    <a class="navbar-item button is-light secondary"
                       href="?${CommonAppConstants.QUERY_PARAM_COMMAND}=${CommandType.MENU_SERVLET_SWITCH}"><fmt:message
                            key="links.dish.menu"/></a>

                </div>
                <div class="navbar-end">
                    <a class="navbar-item button is-light secondary"
                       href="?${CommonAppConstants.QUERY_PARAM_COMMAND}=${CommandType.REGISTER_SERVLET_SWITCH}"><fmt:message
                            key="links.person.register"/></a>


                    <a class="navbar-item button is-light secondary"
                       href="?${CommonAppConstants.QUERY_PARAM_COMMAND}=${CommandType.LOGIN_SERVLET_SWITCH}"><fmt:message
                            key="links.person.login"/></a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</aside>