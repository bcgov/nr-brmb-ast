<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getApplicationCache var="inventoryItems" scope="request" key="server.list.inventory.valid.items"/>

<c:set var="year" value="${param.year}"/>

YAHOO.farm.Data = {
cropItems: [
<c:forEach var="item" items="${inventoryItems}"><c:if test="${item.inventoryClassCode eq '1' and item.year eq year}">{n:"<c:out value="${item.value}"/> - <c:out value="${item.label}"/>",i:<c:out value="${item.value}"/>},</c:if></c:forEach>
],
livestockItems: [
<c:forEach var="item" items="${inventoryItems}"><c:if test="${item.inventoryClassCode eq '2' and item.year eq year}">{n:"<c:out value="${item.value}"/> - <c:out value="${item.label}"/>",i:<c:out value="${item.value}"/>,m:"<c:out value="${item.isMarketCommodity}"/>"},</c:if></c:forEach>
]
};
