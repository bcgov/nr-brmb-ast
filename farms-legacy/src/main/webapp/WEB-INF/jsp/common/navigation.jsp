<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getClientList var="clientList" scope="request"/>

<!-- START NAVIGATION -->
<div id="bcgov-navigation">
  <ul>

    <!--
      You can't call a Java array's length attribute in JSTL, to figure out an array's
      length you have to use the JSTL length function. Since we can't use JSTL functions 
      on MOE projects, we are counting the number of items in the array here.
    -->
    <c:set var="numClients" value="0"/>
    <c:forEach varStatus="loop" items="${clientList}">
      <c:set var="numClients" value="${loop.count}"/>
    </c:forEach>
    
    <w:ifUserCanPerformAction action="myAccount">
      <c:choose>
        <c:when test="${numClients == 1}">
          <li><a href="<html:rewrite action="farm300"/>?pin=<c:out value="${clientList[0].participantPin}"/>"><fmt:message key="My.Account"/></a></li>
        </c:when>
        <c:when test="${numClients > 1}">
          <li><a href="<html:rewrite action="farm310"/>"><fmt:message key="My.Accounts"/></a></li>
        </c:when>
       </c:choose>
    </w:ifUserCanPerformAction>
    
    <w:ifUserCanPerformAction action="calculatorInbox">
      <li><a href="<html:rewrite action="farm900"/>"><fmt:message key="Inbox"/></a></li>
    </w:ifUserCanPerformAction>
    <w:ifUserCanPerformAction action="importData">
      <li><a href="<html:rewrite action="farm250"/>"><fmt:message key="Import"/></a></li>
    </w:ifUserCanPerformAction>
    <w:ifUserCanPerformAction action="manageTransfers">
      <li><a href="<html:rewrite action="farm252"/>"><fmt:message key="Transfers"/></a></li>
    </w:ifUserCanPerformAction>
    <w:ifUserCanPerformAction action="viewEnrolments">
      <li><a href="<html:rewrite action="farm360"/>"><fmt:message key="Enrolments"/></a></li>
    </w:ifUserCanPerformAction>
    <w:ifUserCanPerformAction action="exportData">
      <li><a href="<html:rewrite action="farm600"/>"><fmt:message key="Export"/></a></li>
    </w:ifUserCanPerformAction>
    <w:ifUserCanPerformAction action="viewReport">
      <li><a href="<html:rewrite action="farm610"/>"><fmt:message key="Reports"/></a></li>
    </w:ifUserCanPerformAction>
    <w:ifUserCanPerformAction action="viewCodes">
      <li><a href="<html:rewrite action="farm265"/>"><fmt:message key="Code.Management"/></a></li>
    </w:ifUserCanPerformAction>
    <w:ifUserCanPerformAction action="newParticipant">
      <li><a href="<html:rewrite action="farm770"/>"><fmt:message key="NPP.Entry"/></a></li>
    </w:ifUserCanPerformAction>
    <w:ifUserCanPerformAction action="generateTipReports">
      <li><a href="<html:rewrite action="farm660"/>"><fmt:message key="TIP.Reports"/></a></li>
    </w:ifUserCanPerformAction>
    <w:ifUserCanPerformAction action="viewTipCodes">
      <li><a href="<html:rewrite action="farm546"/>"><fmt:message key="TIP.Administration"/></a></li>
    </w:ifUserCanPerformAction>
    <w:ifUserCanPerformAction action="chefsManagement">
      <li><a href="<html:rewrite action="farm256"/>"><fmt:message key="Chefs"/></a></li>
    </w:ifUserCanPerformAction>
    <w:ifUserCanPerformAction action="fifoView">
      <li><a href="<html:rewrite action="farm258"/>"><fmt:message key="Fifo"/></a></li>
    </w:ifUserCanPerformAction>
    <li><u:signOut /></li>
  </ul>
</div>
<!-- END NAVIGATION -->