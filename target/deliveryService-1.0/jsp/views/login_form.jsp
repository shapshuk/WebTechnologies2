<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" tagdir="/WEB-INF/tags/form" %>
<%@ page import="com.market.angelsel.command.CommandType" %>
<%@ page import="com.market.angelsel.core.constants.CommonAppConstants" %>

<div class="loginContainer">

    <jsp:include page="validation_messages.jsp"/>
    <jsp:include page="command_result_message.jsp"/>

    <h1 class="title is-1">
        <fmt:message var="login" key="app.form.login"/>
        <c:out value="${login}"/>
    </h1>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <input type="hidden" name="${CommonAppConstants.QUERY_PARAM_COMMAND}"
               value="${CommandType.SUBMIT_USER_LOGIN_COMMAND}">
        <div class="">
            <div class="control field">
                <label class="label">
                    <fmt:message key="user.email"/>
                    <input class="input" id="user.email" name="${CommonAppConstants.USER_EMAIL_JSP_PARAM}" type="text">
                </label>
            </div>
            <div class="control field">
                <label class="label">
                    <fmt:message key="user.password"/>
                    <input class="input" id="user.password" name="${CommonAppConstants.USER_PASSWORD_JSP_PARAM}"
                           type="password">
                </label>
            </div>
        </div>
        <div class="field control marginTop">
            <fmt:message var="loginLabel" key="button.person.login"/>
            <input class="button is-light secondary" type="submit" value="${loginLabel}">
        </div>
    </form>
</div>
