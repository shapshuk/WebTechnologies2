<%--
  Created by IntelliJ IDEA.
  User: Sokolover
  Date: 26.07.2020
  Time: 22:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.market.angelsel.core.constants.CommonAppConstants" %>

<c:if test="${not empty currentPage and not empty numberOfPages}">
    <nav class="pagination is-centered" aria-label="Navigation for dishes">

        <c:if test="${currentPage != 1}">
            <a class="pagination-previous button is-light secondary"
               href="?${CommonAppConstants.QUERY_PARAM_PAGE}=${currentPage - 1}"><fmt:message
                    key="app.pagination.previous"/></a>
        </c:if>

        <c:if test="${currentPage lt numberOfPages}">
            <a class="pagination-next button is-light secondary"
               href="?${CommonAppConstants.QUERY_PARAM_PAGE}=${currentPage + 1}"><fmt:message
                    key="app.pagination.next"/></a>
        </c:if>

        <ul class="pagination-list">
            <c:forEach begin="1" end="${numberOfPages}" var="i">
                <c:choose>
                    <c:when test="${currentPage eq i}">
                        <li><a class="pagination-link button is-light color">${i}</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li><a class="pagination-link button is-light secondary"
                               href="?${CommonAppConstants.QUERY_PARAM_PAGE}=${i}">${i}</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </ul>

    </nav>
</c:if>
