<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<div class="yui-navset"> 
  <ul class="yui-nav"> 
    <li><a href="<html:rewrite action="farm300"/>?pin=<c:out value="${account.client.participantPin}"/>&year=<c:out value="${account.programYear}"/>"><em><fmt:message key="Status"/></em></a></li> 
    <li class="selected"><a href="<html:rewrite action="farm340"/>"><em><fmt:message key="Authorized.Users"/></em></a></li> 
    <w:ifUserCanPerformAction action="viewInvitation">
      <li><a href="<html:rewrite action="farm330"/>"><em><fmt:message key="Invitations"/></em></a></li> 
    </w:ifUserCanPerformAction>
    <w:ifUserCanPerformAction action="viewReasonabilityTests">
      <li><a href="<html:rewrite action="farm350"/>"><em><fmt:message key="Reasonability.Tests"/></em></a></li> 
    </w:ifUserCanPerformAction>
  </ul> 
  
  
  <div class="yui-content"> 
    <table class="searchresults"> 
      <tr> 
        <th><fmt:message key="Authorized.User.ID"/></th> 
        <th><fmt:message key="Activated.Date"/></th> 
        <th><fmt:message key="Name"/></th> 
        <th><fmt:message key="Phone"/></th>
        
        <w:ifUserCanPerformAction action="editInvitation">
            <th></th>
        </w:ifUserCanPerformAction>
      </tr> 
      
      <c:forEach var="user" varStatus="loop" items="${account.authorizedUsers}">
        <tr> 
          <td><c:out value="${user.userid}"/></td>
          <td><fmt:formatDate pattern="yyyy-MM-dd" value="${user.activatedDate}" /></td>
          <td><c:out value="${user.userName}"/></td>
          <td><c:out value="${user.userPhoneNumber}"/></td>
          
          <w:ifUserCanPerformAction action="editInvitation">
            <td class="button">
              <a id="<u:id prefix="button" var="buttons"/>" 
                 href="javascript:removeUser('<c:out value="${user.userName}"/>', '<c:out value="${user.clientSubscriptionId}"/>', '<c:out value="${user.revisionCount}"/>')">
                 
                <fmt:message key="Remove"/>
              </a>
            </td> 
          </w:ifUserCanPerformAction>
        </tr> 
      </c:forEach>
    </table>
  </div> 
  <p style="font-size:smaller;font-style:italic">Note: Authorized Users are added by generating an invitation letter.</p>
</div>


<w:ifUserCanPerformAction action="editInvitation">

  <script type="text/javascript"> 
    <c:forEach var="item" items="${buttons}">
      new YAHOO.widget.Button("<c:out value="${item}"/>");
    </c:forEach>
    
    function removeUser(userName, subId, revisionCount) {
      var msg = "Do you really want to remove " + userName + "?";
      if( confirm(msg) ) {
        document.location.href = "revokeSubscription.do?clientSubscriptionId=" + subId + "&revisionCount=" + revisionCount;
      }
    }
    
  </script>
  
</w:ifUserCanPerformAction>