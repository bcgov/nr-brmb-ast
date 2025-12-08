<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getForm var="form" scope="request"/>

<u:getApplicationCache var="inventoryItems" scope="request" key="server.list.puc.inventory.items"/>
<u:getApplicationCache var="structureGroups" scope="request" key="server.list.structure.groups"/>

YAHOO.farm.Data = {
pucs: [
<c:forEach var="item" items="${inventoryItems}">{n:"<c:out value="${item.value}"/> - <c:out value="${item.label}"/>",i:<c:out value="${item.value}"/>,t:"<c:out value="${form.pucTypeInventory}"/>"},</c:forEach>
<c:forEach var="item" items="${structureGroups}">{n:"<c:out value="${item.value}"/> - <c:out value="${item.label}"/>",i:<c:out value="${item.value}"/>,t:"<c:out value="${form.pucTypeStructureGroup}"/>"},</c:forEach>
]};
