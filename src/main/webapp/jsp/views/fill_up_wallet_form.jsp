<%--
  Created by IntelliJ IDEA.
  User: Sokolover
  Date: 01.08.2020
  Time: 13:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" tagdir="/WEB-INF/tags/form" %>
<%@ page import="com.restraunt.shapshuk.command.CommandType" %>
<%@ page import="com.restraunt.shapshuk.core.constants.CommonAppConstants" %>

<div class="loginContainer">

    <jsp:include page="command_result_message.jsp"/>

    <h5 class="title is-5">
        <fmt:message var="moneyAdd" key="app.form.money.add"/>
        <c:out value="${moneyAdd}"/>
    </h5>

    <div class="">
        <label for="${walletCurrentMoneyAmount}">
            <fmt:message key="app.message.money.amount.current"/>
        </label>
        <fmt:message var="currency" key="symbol.currency"/>
        <c:out value=": ${walletCurrentMoneyAmount} ${currency}"/>
    </div>

    <form action="${pageContext.request.contextPath}/" method="post">
        <input type="hidden" name="${CommonAppConstants.QUERY_PARAM_COMMAND}"
               value="${CommandType.SUBMIT_FILLING_UP_WALLET_FORM_COMMAND}">
        <div class="control field">
            <label class="label">
                <input class="input"
                       name="${CommonAppConstants.WALLET_NEW_MONEY_AMOUNT_JSP_PARAM}"
                       type="number"
                       value="10"
                       step=".01"
                       min="0">
            </label>
        </div>
        <div class="field control marginTop">
            <fmt:message var="addLabel" key="button.money.add"/>
            <input class="button is-light secondary" type="submit" value="${addLabel}">
        </div>
    </form>
</div>
