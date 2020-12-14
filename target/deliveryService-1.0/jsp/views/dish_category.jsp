<%--
  Created by IntelliJ IDEA.
  User: Sokolover
  Date: 23.06.2020
  Time: 18:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="/WEB-INF/tld/sort/categorySort.tld" %>
<%@ page import="com.market.angelsel.core.constants.CommonAppConstants" %>

<div class="categoryContainer">

    <p class="menu-label">
        <fmt:message key="app.group.category"/>
    </p>

    <div class="categoryAddContainer">
        <form action="" method="get">
            <jsp:useBean id="categoryList" scope="request" type="java.util.List"/>
            <c:choose>
                <c:when test="${not empty selectedCategories}">
                    <c:forEach items="${categoryList}" var="category">
                        <c:set var="flag" value="false"/>
                        <c:set var="flag" value="${f:setCheckedStates(category, selectedCategories)}"/>
                        <c:choose>
                            <c:when test="${flag}">
                                <input type="checkbox" checked name="${CommonAppConstants.QUERY_PARAM_CATEGORY}"
                                       value="${category.categoryName}"/>
                                ${category.categoryName}
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox" name="${CommonAppConstants.QUERY_PARAM_CATEGORY}"
                                       value="${category.categoryName}"/>
                                ${category.categoryName}
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${categoryList}" var="category">
                        <input type="checkbox" name="${CommonAppConstants.QUERY_PARAM_CATEGORY}"
                               value="${category.categoryName}"/>
                        ${category.categoryName}
                    </c:forEach>
                </c:otherwise>
            </c:choose>

            <fmt:message var="ok" key="button.category.ok"/>
            <input class="button is-light secondary" type="submit" value="${ok}"/>
        </form>

        <form action="" method="get">
            <input type="hidden" name="${CommonAppConstants.QUERY_PARAM_CATEGORY}"
                   value="all">
            <fmt:message var="all" key="button.category.reset"/>
            <input class="button is-light secondary" type="submit" value="${all}"/>
        </form>

    </div>
</div>
