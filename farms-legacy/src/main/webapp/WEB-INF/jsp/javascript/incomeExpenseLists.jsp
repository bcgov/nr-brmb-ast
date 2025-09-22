<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getForm var="form" scope="request"/>

YAHOO.farm.Data = {
lineItemsEligible: [
<c:forEach var="item" items="${form.lineItems}"><c:if test="${item.eligible}">{n:"<c:out value="${item.lineItem}"/> - <c:out value="${item.description}" escapeXml="false"/>",i:<c:out value="${item.lineItem}"/>},</c:if></c:forEach>
],

lineItemsIneligible: [
<c:forEach var="item" items="${form.lineItems}"><c:if test="${item.ineligible}">{n:"<c:out value="${item.lineItem}"/> - <c:out value="${item.description}" escapeXml="false"/>",i:<c:out value="${item.lineItem}"/>},</c:if></c:forEach>
]
};
