<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getApplicationCache var="inventoryItemCodes" scope="request" key="server.list.inventory.items"/>

YAHOO.farm.Data = {
inventoryItemCodes: [
<c:forEach var="item" items="${inventoryItemCodes}">{n:"<c:out value="${item.value}"/> - <c:out value="${item.label}"/>",i:<c:out value="${item.value}"/>},</c:forEach>
]};
