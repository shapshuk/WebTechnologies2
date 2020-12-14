<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<jsp:directive.attribute name="fieldName" required="true" description="field name to display"/>
<jsp:directive.attribute name="beanName" required="true" description="some object"/>
<c:if test="${not empty fieldName and not empty beanName}">
    <p>
        <c:set var="bean" value="${requestScope[beanName]}"/>
        <label><fmt:message key="${beanName}.${fieldName}"/>:</label>
        <span><c:out value="${bean[fieldName]}"/></span>
    </p>
</c:if>
