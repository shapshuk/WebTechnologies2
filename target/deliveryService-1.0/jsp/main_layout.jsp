<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="lang" tagdir="/WEB-INF/tags" %>

<html>
<head>
    <title>Delivery service</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/bulma.css">
</head>
<body>

<c:choose>
    <c:when test="${not empty requestScope.get('lang')}">
        <fmt:setLocale value="${requestScope.get('lang')}"/>
    </c:when>
    <c:otherwise>
        <fmt:setLocale value="${cookie['lang'].value}"/>
    </c:otherwise>
</c:choose>

<fmt:setBundle basename="/i18n/ApplicationMessages" scope="application"/>
<div class="bodyContainer">
    <section class="myHeader">
        <div class="betweenContainer">
            <div class="alignItemsCenter">
                <span class="appTitle">
                    <fmt:message key="app.title"/>
                </span>
            </div>
            <lang:lang/>
        </div>
        <div class="">
            <jsp:include page="nav_bar.jsp"/>
        </div>
    </section>
    <section class="myContent">
        <c:if test="${not empty viewName}">
            <jsp:include page="views/${viewName}.jsp"/>
        </c:if>
    </section>
    <section class="myFooter">
        <p class="footerText">
            <fmt:message key="app.footer"/>
        </p>
    </section>
</div>
</body>
</html>
