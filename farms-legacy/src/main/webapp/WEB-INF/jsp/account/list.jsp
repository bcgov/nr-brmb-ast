<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getClientList var="clientList" scope="request"/>

<h1><fmt:message key="My.Accounts"/></h1> 
<div class="links"> 
  <table class="searchresults"> 
    <tr> 
      <th><fmt:message key="Name"/></th> 
      <th><fmt:message key="PIN"/></th> 
      <th><fmt:message key="Address"/></th> 
    </tr> 
    
    <c:forEach var="client" varStatus="loop" items="${clientList}">
      <tr onclick="location.href='<html:rewrite action="farm300"/>?pin=<c:out value="${client.participantPin}"/>'"> 
          <td><c:out value="${client.owner.fullName}"/></td> 
          <td><c:out value="${client.participantPin}"/></td> 
          <td>
            <c:out value="${client.owner.addressLine1} ${client.owner.addressLine2}" />,
            <c:out value="${client.owner.city}" />,
            <c:out value="${client.owner.provinceState}" />,
            <c:out value="${client.owner.postalCode}" />
          </td> 
      </tr> 
    </c:forEach>
    
  </table> 
</div> 