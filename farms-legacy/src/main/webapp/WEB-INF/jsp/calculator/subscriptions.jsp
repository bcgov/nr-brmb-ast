<%@ include file="/WEB-INF/jsp/common/include.jsp" %>


<html:form action="farm340" method="post">
  <html:hidden property="pin"/>
  <html:hidden property="year"/>
  <html:hidden property="scenarioNumber"/>
  <html:hidden property="scenarioRevisionCount"/>

  <fieldset>
    <legend><fmt:message key="Subscriptions"/></legend>
    <table class="searchresults"> 
      <tr> 
        <th><fmt:message key="Generated.Date"/></th> 
        <th><fmt:message key="Generated.By"/></th>
        <th><fmt:message key="Activated.Date"/></th> 
        <th><fmt:message key="Activated.By"/></th> 
        <th><fmt:message key="Status"/></th> 
        <th><fmt:message key="Details"/></th>
      </tr> 

      <c:forEach var="subscription" varStatus="loop" items="${requestScope.subscriptions}">
        <tr> 
          <td><fmt:formatDate pattern="yyyy-MM-dd" value="${subscription.generatedDate}" /></td> 
          <td><c:out value="${subscription.generatedByUserid}"/></td> 
          <td><fmt:formatDate pattern="yyyy-MM-dd" value="${subscription.activatedDate}" /></td> 
          <td><c:out value="${subscription.activatedByUserid}"/></td> 

          <c:choose>
            <c:when test="${subscription.subscriptionStatusCode == 'GEN'}">
               <td class="button">
                 <a id="<u:id prefix="button" var="buttons"/>" 
                    href="javascript:invalidate('<c:out value="${subscription.clientSubscriptionId}"/>', '<c:out value="${subscription.revisionCount}"/>');">
                   <fmt:message key="Invalidate"/>
                 </a>
               </td>
            </c:when>
            <c:otherwise>
              <td><c:out value="${subscription.subscriptionStatusCodeDescription}"/></td>
            </c:otherwise>
          </c:choose>

          <td class="button"><a id="<u:id prefix="button" var="buttons"/>" href="javascript:showInvitationDetailPanel('<c:out value="${loop.index}"/>')"><fmt:message key="Details"/></a></td> 
        </tr> 
      </c:forEach>

    </table> 
    <p></p> 

    <a id="generateSubscriptionLetterButton" href="javascript:generateLetter();"><fmt:message key="Generate.Invitation.Letter"/></a> 
    <p></p>
  </fieldset>

  <fieldset>
    <legend>Authorized Users</legend>
    <table class="searchresults"> 
      <tr> 
        <th><fmt:message key="Authorized.User.ID"/></th> 
        <th><fmt:message key="Activated.Date"/></th> 
        <th><fmt:message key="Name"/></th> 
        <th><fmt:message key="Phone"/></th>
        <th></th>
      </tr> 
      <c:forEach var="user" items="${requestScope.authorizedUsers}">
        <tr> 
          <td><c:out value="${user.userid}"/></td>
          <td><fmt:formatDate pattern="yyyy-MM-dd" value="${user.activatedDate}" /></td>
          <td><c:out value="${user.userName}"/></td>
          <td><c:out value="${user.userPhoneNumber}"/></td>
          <td class="button">
            <a id="<u:id prefix="button" var="buttons"/>" 
               href="javascript:removeUser('<c:out value="${user.userName}"/>', '<c:out value="${user.clientSubscriptionId}"/>', '<c:out value="${user.revisionCount}"/>')">
                 
              <fmt:message key="Remove"/>
            </a>
          </td>
        </tr> 
      </c:forEach>
    </table>

    <p style="font-size:smaller;font-style:italic">Note: Authorized Users are added by generating an invitation letter.</p>
  </fieldset>

</html:form>



<div id="panel"></div>

<script type="text/javascript">

  function onPageLoad(){
    <c:if test="${form.reportUrl != null}">
      openNewWindow('<c:out value="${form.reportUrl}" escapeXml="false" />');
    </c:if>
  }
  
  
  function invalidate(clientSubscriptionId, revisionCount) {
    if( confirm("Do you really want to invalidate this invitation?") ) {
      var url = "invalidateSubscription.do?" + 
        "clientSubscriptionId=" + clientSubscriptionId + 
        "&revisionCount=" + revisionCount +
        "&pin=" + <c:out value="${form.pin}" /> +
        "&year=" + <c:out value="${form.year}" /> +
        "&scenarioNumber=" + <c:out value="${form.scenarioNumber}" /> ;
      
      showProcessing();
      document.location.href = url;
    }
  }


  function generateLetter() {
    var url = "generateSubscription.do?" +
        "pin=" + <c:out value="${form.pin}" /> +
        "&year=" + <c:out value="${form.year}" /> +
        "&scenarioNumber=" + <c:out value="${form.scenarioNumber}" /> ;
    
    showProcessing();
    document.location.href = url;
  }


  function removeUser(userName, subId, revisionCount) {
    var msg = "Do you really want to remove " + userName + "?";
    if( confirm(msg) ) {
      var url = "revokeSubscription.do?" +
        "clientSubscriptionId=" + subId + 
        "&revisionCount=" + revisionCount +
        "&pin=" + <c:out value="${form.pin}" /> +
        "&year=" + <c:out value="${form.year}" /> +
        "&scenarioNumber=" + <c:out value="${form.scenarioNumber}" /> ;
        
      showProcessing();
      document.location.href = url;
    }
  }


  new YAHOO.widget.Button("generateSubscriptionLetterButton");
  
  
  <c:forEach var="item" items="${buttons}">
    new YAHOO.widget.Button("<c:out value="${item}"/>");
  </c:forEach>
  
  var panelBodies = new Array();
  
  <c:forEach var="subscription" items="${requestScope.subscriptions}">
    panelBodies.push("<table class=\"details\"> \
      <tr> \
        <th>Generated Date:</th> \
        <td><fmt:formatDate pattern="yyyy-MM-dd" value="${subscription.generatedDate}" /></td> \
      </tr> \
      <tr> \
        <th>Generated By:</th> \
        <td><c:out value="${subscription.javascriptGeneratedByUserid}"/></td> \
      </tr> \
      <tr> \
        <th>Activated Date:</th> \
        <td><fmt:formatDate pattern="yyyy-MM-dd" value="${subscription.activatedDate}" /></td> \
      </tr> \
      <tr> \
        <th>Activated By:</th> \
        <td><c:out value="${subscription.javascriptActivatedByUserid}"/></td> \
      </tr> \
      <tr> \
        <th>Status:</th> \
        <td><c:out value="${subscription.subscriptionStatusCodeDescription}"/></td> \
      </tr> \
      <tr> \
        <th>Subscription Code:</th> \
        <td><c:out value="${subscription.subscriptionNumber}"/></td> \
      </tr> \
    </table>");
  </c:forEach>
  
  function showInvitationDetailPanel(index) { 
    var panel = new YAHOO.widget.Panel("panel", {
      width:"400px",
      constraintoviewport: true, 
      underlay:"shadow", 
      close:true, 
      visible:false, 
      draggable:true,
      dragOnly:true});
  
    panel.setHeader("Details");
    panel.setBody(panelBodies[index]);
    panel.render("bcgov-content-liner");
    panel.show();
    panel.center();
  }
  
</script> 

