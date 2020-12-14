<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" tagdir="/WEB-INF/tags/form" %>
<%@ page import="com.restraunt.shapshuk.command.CommandType" %>
<%@ page import="com.restraunt.shapshuk.core.constants.CommonAppConstants" %>

<div class="loginContainer">

    <jsp:include page="validation_messages.jsp"/>

    <h1 class="title is-1">
        <fmt:message var="register" key="app.form.register"/>
        <c:out value="${register}"/>
    </h1>

    <form action="${pageContext.request.contextPath}/user_register" method="post">
        <input type="hidden" name="${CommonAppConstants.QUERY_PARAM_COMMAND}"
               value="${CommandType.SUBMIT_USER_REGISTER_COMMAND}">
        <div class="">
            <div class="control field">
                <label class="label">
                    <fmt:message key="user.name"/>
                    <input class="input" name="${CommonAppConstants.USER_NAME_JSP_PARAM}" type="text"
                           placeholder="5-30 characters">
                </label>
            </div>
            <div class="message is-info">
                <fmt:message var="nameRule" key="app.message.name.rule"/>
                <c:out value="${nameRule}"/>
            </div>
            <div class="control field">
                <label class="label">
                    <fmt:message key="user.password"/>
                    <input class="input" name="${CommonAppConstants.USER_PASSWORD_JSP_PARAM}" type="password"
                           placeholder="Password input">
                </label>
            </div>
            <div class="message is-info">
                <fmt:message var="passwordRule" key="app.message.password.rule"/>
                <c:out value="${passwordRule}"/>
            </div>
            <div class="control field">
                <label class="label">
                    <fmt:message key="user.password.confirm"/>
                    <input class="input" name="${CommonAppConstants.USER_PASSWORD_CONFIRM_JSP_PARAM}" type="password"
                           placeholder="Password input">
                </label>
            </div>
            <div class="control field">
                <label class="label">
                    <fmt:message key="user.email"/>
                    <input class="input" name="${CommonAppConstants.USER_EMAIL_JSP_PARAM}" type="text"
                           placeholder="ivan.inanov@gmail.com">
                </label>
            </div>
            <div class="message is-info">
                <fmt:message var="emailRule" key="app.message.email.rule"/>
                <c:out value="${emailRule}"/>
            </div>
            <div class="control field">
                <label class="label">
                    <fmt:message key="user.phoneNumber"/>
                    <input class="input" name="${CommonAppConstants.USER_PHONE_NUMBER_JSP_PARAM}" type="text"
                           placeholder="+375291234567">
                </label>
            </div>
            <div class="message is-info">
                <fmt:message var="phoneNumberRule" key="app.message.phoneNumber.rule"/>
                <c:out value="${phoneNumberRule}"/>
            </div>
            <div class="control field">
                <label class="label">
                    <fmt:message key="user.address"/>
                    <input class="input" name="${CommonAppConstants.USER_ADDRESS_JSP_PARAM}" type="text"
                           placeholder="Address input">
                </label>
            </div>
        </div>
        <div class="control field marginTop">
            <fmt:message var="register_label" key="links.person.register"/>
            <input class="button is-light secondary" type="submit" value="${register_label}">
        </div>
    </form>
</div>