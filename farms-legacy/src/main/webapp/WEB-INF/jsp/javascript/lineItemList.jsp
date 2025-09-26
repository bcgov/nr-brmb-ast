<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getApplicationCache var="lineItems" scope="request" key="server.list.program.year.line.items"/>

YAHOO.farm.Data = {
  lineItems: [
    <c:forEach var="item" items="${lineItems}">{n:"<c:out value="${item.lineItem}"/> - <c:out value="${item.description}" escapeXml="false"/>",i:<c:out value="${item.lineItem}"/>, desc: "<c:out value="${item.description}" escapeXml="false"/>"},</c:forEach>
  ]
};
