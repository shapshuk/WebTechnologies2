<%--
  Created by IntelliJ IDEA.
  User: Sokolover
  Date: 03.08.2020
  Time: 15:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${not empty errors}">
    <fmt:message var="errorLable" key="app.message.error"/>
    <c:forEach items="${errors}" var="error">
        <div class="message is-danger">
            <div class="message-header">
                <p><c:out value="${errorLable}"/></p>
            </div>
            <div class="message-body">
                <c:out value="${error}"/>
            </div>
        </div>
    </c:forEach>
</c:if>
