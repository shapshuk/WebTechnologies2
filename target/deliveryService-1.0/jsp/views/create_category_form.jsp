<%--
  Created by IntelliJ IDEA.
  User: Sokolover
  Date: 24.07.2020
  Time: 14:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" tagdir="/WEB-INF/tags/form" %>
<%@ page import="com.market.angelsel.command.CommandType" %>
<%@ page import="com.market.angelsel.core.constants.CommonAppConstants" %>


<div class="loginContainer">

    <jsp:include page="validation_messages.jsp"/>

    <h5 class="title is-5">
        <fmt:message var="createCategory" key="app.form.create.category"/>
        <c:out value="${createCategory}"/>
    </h5>

    <form action="${pageContext.request.contextPath}/" method="post">
        <input type="hidden" name="${CommonAppConstants.QUERY_PARAM_COMMAND}"
               value="${CommandType.SUBMIT_DISH_CATEGORY_CREATING_FORM_COMMAND}">
        <div class="control field">
            <label class="label">
                <input class="input" name="${CommonAppConstants.CATEGORY_NAME_JSP_PARAM}"
                       type="text">
            </label>
        </div>
        <div class="field control marginTop">
            <fmt:message var="createLabel" key="button.category.create"/>
            <input class="button is-light secondary" type="submit" value="${createLabel}">
        </div>
    </form>
</div>