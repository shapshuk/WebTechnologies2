<%--
  Created by IntelliJ IDEA.
  User: Sokolover
  Date: 31.07.2020
  Time: 20:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" tagdir="/WEB-INF/tags/form" %>
<%@ page import="com.market.angelsel.command.CommandType" %>
<%@ page import="com.market.angelsel.core.constants.CommonAppConstants" %>

<div class="loginContainer">

    <c:if test="${not empty error}">
        <fmt:message var="errorLable" key="app.message.error"/>
        <div class="justifyCenter marginTop message is-danger">
            <div class="message-header">
                <p><c:out value="${errorLable}"/></p>
            </div>
            <div class="message-body">
                <c:out value="${error}"/>
            </div>
        </div>
    </c:if>

    <h5 class="title is-5">
        <fmt:message var="deleteCategory" key="app.form.delete.category"/>
        <c:out value="${deleteCategory}"/>
    </h5>

    <form action="${pageContext.request.contextPath}/" method="post">
        <input type="hidden" name="${CommonAppConstants.QUERY_PARAM_COMMAND}"
               value="${CommandType.SUBMIT_DISH_CATEGORY_DELETING_FORM_COMMAND}">
        <div class="select field">
            <select name="${CommonAppConstants.DISH_CATEGORY_NAME_JSP_PARAM}">
                <jsp:useBean id="categoryList" scope="request" type="java.util.List"/>
                <c:forEach items="${categoryList}" var="category">
                    <option value="${category.categoryName}">${category.categoryName}</option>
                </c:forEach>
            </select>
        </div>
        <div class="field control marginTop">
            <fmt:message var="deleteLabel" key="button.category.delete"/>
            <input class="button is-light secondary" type="submit" value="${deleteLabel}">
        </div>
    </form>
</div>
