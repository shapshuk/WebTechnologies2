<%--
  Created by IntelliJ IDEA.
  User: Sokolover
  Date: 29.06.2020
  Time: 12:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.restraunt.shapshuk.command.CommandType" %>
<%@ page import="com.restraunt.shapshuk.core.constants.CommonAppConstants" %>

<div class="dishPage">

    <jsp:include page="dish_category.jsp"/>
    <jsp:include page="command_result_message.jsp"/>

    <div class="dishContainer">
        <jsp:useBean id="itemList" scope="request" type="java.util.List"/>
        <c:forEach items="${itemList}" var="orderItem">
            <div class="cardContainer">
                <div class="card">
                    <div class="card-image">
                        <figure class="image is-4by3">
                            <img src="data:image/jpg;base64,${orderItem.dish.picture}" alt="no dish picture"/>
                        </figure>
                    </div>
                    <div class="card-content">

                        <p class="card-header-title">
                            <c:out value="${orderItem.dish.name}"/>
                        </p>

                        <div class="box">
                            <c:out value="${orderItem.dish.description}"/>
                        </div>

                        <div class="">
                            <label for="${orderItem.dish.cost}">
                                <fmt:message key="dish.cost"/>
                            </label>
                            <fmt:message var="currency" key="symbol.currency"/>
                            <c:out value=": ${orderItem.dish.cost} ${currency}"/>
                        </div>

                        <div class="">
                            <label for="${orderItem.dishAmount}">
                                <fmt:message key="order.item.dishAmount"/>
                            </label>
                            <c:out value=": ${orderItem.dishAmount}"/>
                        </div>

                        <div class="">
                            <label for="${orderItem.itemCost}">
                                <fmt:message key="order.item.cost"/>
                            </label>
                            <c:out value=": ${orderItem.itemCost} ${currency}"/>
                        </div>

                        <form action="${pageContext.request.contextPath}/order_basket" method="post">
                            <input type="hidden" name="${CommonAppConstants.QUERY_PARAM_COMMAND}"
                                   value="${CommandType.DELETE_ORDER_ITEM_COMMAND}">
                            <input type="hidden" name="orderItem.id" value="${orderItem.id}">
                            <div class="control">
                                <fmt:message var="deleteLabel" key="button.order.item.delete"/>
                                <input class="button is-light secondary" type="submit" value="${deleteLabel}">
                            </div>
                        </form>

                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>


