<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getApplicationCache var="inventoryItems" scope="request" key="server.list.inventory.valid.items"/>

<c:set var="year" value="${param.year}"/>

YAHOO.farm.Data = {
inputItemsEligible: [
<c:forEach var="item" items="${inventoryItems}"><c:if test="${item.inventoryClassCode eq '3' and item.year eq year and item.isEligible == true}">{n:"<c:out value="${item.value}"/> - <c:out value="${item.label}"/>",i:<c:out value="${item.value}"/>},</c:if></c:forEach>
],
inputItemsIneligible: [
<c:forEach var="item" items="${inventoryItems}"><c:if test="${item.inventoryClassCode eq '3' and item.year eq year and item.isEligible == false}">{n:"<c:out value="${item.value}"/> - <c:out value="${item.label}"/>",i:<c:out value="${item.value}"/>},</c:if></c:forEach>
],
receivableItemsEligible: [
<c:forEach var="item" items="${inventoryItems}"><c:if test="${item.inventoryClassCode eq '4' and item.year eq year and item.isEligible == true}">{n:"<c:out value="${item.value}"/> - <c:out value="${item.label}"/>",i:<c:out value="${item.value}"/>},</c:if></c:forEach>
],
receivableItemsIneligible: [
<c:forEach var="item" items="${inventoryItems}"><c:if test="${item.inventoryClassCode eq '4' and item.year eq year and item.isEligible == false}">{n:"<c:out value="${item.value}"/> - <c:out value="${item.label}"/>",i:<c:out value="${item.value}"/>},</c:if></c:forEach>
],
payableItemsEligible: [
<c:forEach var="item" items="${inventoryItems}"><c:if test="${item.inventoryClassCode eq '5' and item.year eq year and item.isEligible == true}">{n:"<c:out value="${item.value}"/> - <c:out value="${item.label}"/>",i:<c:out value="${item.value}"/>},</c:if></c:forEach>
],
payableItemsIneligible: [
<c:forEach var="item" items="${inventoryItems}"><c:if test="${item.inventoryClassCode eq '5' and item.year eq year and item.isEligible == false}">{n:"<c:out value="${item.value}"/> - <c:out value="${item.label}"/>",i:<c:out value="${item.value}"/>},</c:if></c:forEach>
]
};
