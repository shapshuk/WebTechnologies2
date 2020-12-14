<%--
  Created by IntelliJ IDEA.
  User: Sokolover
  Date: 05.07.2020
  Time: 21:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="/WEB-INF/tld/format/timeFormat.tld" %>
<%@ page import="com.market.angelsel.command.CommandType" %>
<%@ page import="com.market.angelsel.core.constants.CommonAppConstants" %>

<div class="checkoutContainer">

    <jsp:include page="command_result_message.jsp"/>
    <jsp:include page="validation_messages.jsp"/>

    <%--dish table--%>
    <div class="tableContainer">
        <fmt:message var="orderList" key="order.item.list"/>
        <h5 class="title is-5">
            <c:out value="${orderList}"/>
        </h5>
        <fmt:message var="dishName" key="dish.name"/>
        <fmt:message var="dishCost" key="dish.cost"/>
        <fmt:message var="dishAmount" key="order.item.dishAmount"/>
        <fmt:message var="itemCost" key="order.item.cost"/>

        <table class="table is-striped">
            <thead>
            <tr>
                <th>№</th>
                <th><c:out value="${dishName}"/></th>
                <th><c:out value="${dishCost}"/></th>
                <th><c:out value="${dishAmount}"/></th>
                <th><c:out value="${itemCost}"/></th>
            </tr>
            </thead>
            <tbody>
            <c:set var="i" value="1"/>
            <jsp:useBean id="itemList" scope="request" type="java.util.List"/>
            <c:forEach items="${itemList}" var="item">
                <tr>
                    <td><c:out value="${i}"/>
                    <td><c:out value="${item.dish.name}"/>
                    <td><c:out value="${item.dish.cost}"/>
                    <td><c:out value="${item.dishAmount}"/>
                    <td><c:out value="${item.itemCost}"/>
                </tr>
                <c:set var="i" value="${i + 1}"/>
            </c:forEach>
            </tbody>
        </table>
        <%--current user's money amount--%>
        <div class="marginTop">
            <jsp:useBean id="walletCurrentMoneyAmount" scope="request" type="java.math.BigDecimal"/>
            <fmt:message var="walletCurrentMoneyAmountLable" key="app.message.money.amount.current"/>
            <fmt:message var="currency" key="symbol.currency"/>
            <h5 class="title is-5">
                <c:out value="${walletCurrentMoneyAmountLable}: ${walletCurrentMoneyAmount} ${currency}"/>
            </h5>
            <%--total cost--%>
            <jsp:useBean id="totalCost" scope="request" type="java.math.BigDecimal"/>
            <fmt:message var="orderCostString" key="order.total.cost"/>
            <h5 class="title is-5">
                <c:out value="${orderCostString}: ${totalCost} ${currency}"/>
            </h5>
        </div>
    </div>

    <div class="infoContainer">
        <%--input contact info--%>
        <form class="checkoutForm" action="${pageContext.request.contextPath}/order_checkout" method="post">

            <input type="hidden" name="${CommonAppConstants.QUERY_PARAM_COMMAND}"
                   value="${CommandType.SUBMIT_ORDER_CHECKOUT_COMMAND}">

            <div class="paymentRadio">
                <label class="radio">
                    <input type="radio"
                           name="${CommonAppConstants.PAYMENT_METHOD_JSP_PARAM}"
                           class="radio"
                           value="${CommonAppConstants.PAYMENT_FROM_ACCOUNT_JSP_PARAM}">
                    <fmt:message key="order.pay.fromAccount"/>
                </label>
                <label class="radio">
                    <input type="radio"
                           name="${CommonAppConstants.PAYMENT_METHOD_JSP_PARAM}"
                           class="radio"
                           checked
                           value="${CommonAppConstants.PAYMENT_ON_DELIVERY_JSP_PARAM}">
                    <fmt:message key="order.pay.onDelivery"/>
                </label>
            </div>

            <div class="contactInfoContent">

                <div class="field">
                    <h5 class="title is-5">
                        <fmt:message var="contactInfo" key="order.contactInfo"/>
                        <c:out value="${contactInfo}"/>
                    </h5>

                    <div class="control field">
                        <label class="label">
                            <fmt:message key="customer.name"/>
                            <input class="input" name="${CommonAppConstants.USER_NAME_JSP_PARAM}"
                                   type="text">
                        </label>
                    </div>

                    <div class="control field">
                        <label class="label">
                            <fmt:message key="customer.phoneNumber"/>
                            <input class="input" name="${CommonAppConstants.USER_PHONE_NUMBER_JSP_PARAM}"
                                   type="text">
                        </label>
                    </div>

                    <label class="checkbox">
                        <input type="checkbox" name="${CommonAppConstants.DEFAULT_USER_NAME_JSP_PARAM}" value="user_s">
                        <fmt:message var="sendUserName" key="lable.checkbox.sendUserName"/>
                        <c:out value="${sendUserName}"/>
                    </label>

                    <label class="checkbox">
                        <input type="checkbox" name="${CommonAppConstants.DEFAULT_USER_PHONE_NUMBER_JSP_PARAM}"
                               value="user_s">
                        <fmt:message var="sendUserPhoneNumber" key="lable.checkbox.sendUserPhoneNumber"/>
                        <c:out value="${sendUserPhoneNumber}"/>
                    </label>

                    <%--input time of delivery--%>
                    <div class="timeOfDelivery">
                        <h5 class="title is-5">
                            <fmt:message var="timeOfDelivery" key="order.timeOfDelivery"/>
                            <c:out value="${timeOfDelivery}"/>
                        </h5>
                        <div class="select">
                            <select name="${CommonAppConstants.ORDER_TIME_OF_DELIVERY_JSP_PARAM}">
                                <jsp:useBean id="timeList" scope="request" type="java.util.List"/>
                                <c:forEach items="${timeList}" var="time">
                                    <option value="${time}">
                                            ${f:formatLocalDateTime(time, 'dd.MM HH:mm')}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="contactInfoBottomContainer">
                    <fmt:message var="deliveryAddress" key="order.deliveryAddress"/>
                    <h5 class="title is-5">
                        <c:out value="${deliveryAddress}"/>
                    </h5>

                    <div class="control field">
                        <label class="label">
                            <fmt:message key="order.address.locality"/>
                            <input class="input" name="${CommonAppConstants.ORDER_ADDRESS_LOCALITY_JSP_ATTRIBUTE}"
                                   type="text">
                        </label>
                    </div>

                    <div class="control field">
                        <label class="label">
                            <fmt:message key="order.address.street"/>
                            <input class="input" name="${CommonAppConstants.ORDER_ADDRESS_STREET_JSP_ATTRIBUTE}"
                                   type="text">
                        </label>
                    </div>

                    <div class="control field">
                        <label class="label">
                            <fmt:message key="order.address.buildingNumber"/>
                            <input class="input"
                                   name="${CommonAppConstants.ORDER_ADDRESS_BUILDING_NUMBER_JSP_ATTRIBUTE}"
                                   type="text">
                        </label>
                    </div>

                    <div class="control field">
                        <label class="label">
                            <fmt:message key="order.address.flatNumber"/>
                            <input class="input"
                                   name="${CommonAppConstants.ORDER_ADDRESS_FLAT_NUMBER_JSP_ATTRIBUTE}"
                                   type="text">
                        </label>
                    </div>

                    <div class="control field">
                        <label class="label">
                            <fmt:message key="order.address.porch"/>
                            <input class="input"
                                   name="${CommonAppConstants.ORDER_ADDRESS_PORCH_NUMBER_JSP_ATTRIBUTE}"
                                   type="text">
                        </label>
                    </div>

                    <div class="control field">
                        <label class="label">
                            <fmt:message key="order.address.floor"/>
                            <input class="input"
                                   name="${CommonAppConstants.ORDER_ADDRESS_FLOOR_NUMBER_JSP_ATTRIBUTE}"
                                   type="text">
                        </label>
                    </div>

                    <label class="checkbox">
                        <input type="checkbox" name="${CommonAppConstants.DEFAULT_USER_NAME_JSP_PARAM}" value="user_s">
                        <fmt:message var="sendUserAddress" key="lable.checkbox.sendUserAddress"/>
                        <c:out value="${sendUserAddress}"/>
                    </label>

                </div>
            </div>

            <div class="control marginTop justifyCenter">
                <fmt:message var="checkoutLabel" key="button.checkout"/>
                <input class="button is-light secondary" type="submit" value="${checkoutLabel}">
            </div>

        </form>
    </div>
</div>



