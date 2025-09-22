<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getApplicationCache var="structureGroupCodes" scope="request" key="server.list.structure.groups"/>

YAHOO.farm.Data = {
structureGroupCodes: [
<c:forEach var="item" items="${structureGroupCodes}">{n:"<c:out value="${item.value}"/> - <c:out value="${item.label}"/>",i:<c:out value="${item.value}"/>},</c:forEach>
]};
