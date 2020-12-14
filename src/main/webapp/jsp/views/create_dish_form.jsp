<%--
  Created by IntelliJ IDEA.
  User: Sokolover
  Date: 17.07.2020
  Time: 22:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" tagdir="/WEB-INF/tags/form" %>
<%@ page import="com.restraunt.shapshuk.command.CommandType" %>
<%@ page import="com.restraunt.shapshuk.core.constants.CommonAppConstants" %>

<div class="container createDishContainer">

    <form class="feedbackArea" action="${pageContext.request.contextPath}/" method="post" enctype="multipart/form-data">
        <input type="hidden"
               name="${CommonAppConstants.QUERY_PARAM_COMMAND}"
               value="${CommandType.SUBMIT_DISH_CREATING_FORM_COMMAND}">

        <jsp:include page="validation_messages.jsp"/>

        <div class="">
            <div class="control field">
                <label class="label">
                    <fmt:message key="dish.name"/>
                    <input class="input" name="${CommonAppConstants.DISH_NAME_JSP_PARAM}" type="text"
                           placeholder="Name input">
                </label>
            </div>
            <div class="control field">
                <div class="label">
                    <fmt:message key="dish.cost"/>
                    <input class="input" name="${CommonAppConstants.DISH_COST_JSP_PARAM}"
                           type="number"
                           value="10"
                           step=".01"
                           min="0">
                </div>
            </div>
            <div class="control field">
                <div class="label">
                    <fmt:message key="dish.description"/>
                    <textarea class="textarea" name="${CommonAppConstants.DISH_DESCRIPTION_JSP_PARAM}"
                              placeholder="Description input" rows="10"></textarea>
                </div>
            </div>
            <div class="file field">
                <label class="file-label">
                    <input class="file-input" type="file" name="${CommonAppConstants.DISH_PICTURE_JSP_PARAM}"/>
                    <span class="file-cta">
                            <span class="file-label"><fmt:message key="app.choose.dish.picture"/></span>
                        </span>
                </label>
            </div>
            <label class="label">
                <fmt:message key="dish.category"/>
                <div class="select field">
                    <select name="${CommonAppConstants.DISH_CATEGORY_NAME_JSP_PARAM}">
                        <jsp:useBean id="categoryList" scope="request" type="java.util.List"/>
                        <c:forEach items="${categoryList}" var="category">
                            <option value="${category.categoryName}">${category.categoryName}</option>
                        </c:forEach>
                    </select>
                </div>
            </label>
        </div>
        <div class="control field">
            <fmt:message var="createLabel" key="button.dish.create"/>
            <input class="button is-light secondary marginTop" type="submit" value="${createLabel}">
        </div>

    </form>
</div>

