<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getScenario var="scenario" pin="${form.pin}" year="${form.year}" scenarioNumber="${form.scenarioNumber}" scope="request"/>


<h1 style="float:left"><c:out value="${scenario.client.owner.fullName}"/></h1>
<h1 style="float:right;white-space:nowrap">Program Year: <u:programYearSelect action="farm300.do" year="${form.year}" urlParams="pin=${form.pin}"/></h1>


<div style="clear:both"></div> 
<div class="yui-navset"> 
  <ul class="yui-nav"> 
    <li class="selected"><a href="<html:rewrite action="farm300"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>"><em><fmt:message key="Account.Details"/></em></a></li> 
    
    <!--
      If the claim hasn't been verified, then don't show it
    -->
   <c:if test="${scenario.verified}">
     <li><a href="<html:rewrite action="farm500"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Benefit.Overview"/></em></a></li>
     <li><a href="<html:rewrite action="farm400"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Benefit.History"/></em></a></li> 
   </c:if>
  </ul> 
  
  <div class="yui-content"> 
    <fieldset>
      <legend>Contact Information</legend>
      <table class="details"> 
        <tr> 
          <th><fmt:message key="PIN"/>:</th> 
          <td><c:out value="${scenario.client.participantPin}"/></td> 
          <th><fmt:message key="Address"/>:</th> 
          <td>
            <c:out value="${scenario.client.owner.addressLine1}<br/>${scenario.client.owner.addressLine2}" escapeXml="no"/>
          </td> 
        </tr> 
        <tr> 
          <th><fmt:message key="Daytime.Phone"/>:</th> 
          <td><c:out value="${scenario.client.owner.daytimePhone}"/></td> 
          <th></th> 
          <td><c:out value="${scenario.client.owner.city},${scenario.client.owner.provinceState}"/></td> 
        </tr> 
        <tr> 
          <th><fmt:message key="Evening.Phone"/>:</th> 
          <td><c:out value="${scenario.client.owner.eveningPhone}"/></td> 
          <th></th> 
          <td><c:out value="${scenario.client.owner.postalCode}"/></td> 
        </tr> 
        <tr> 
          <th><fmt:message key="Fax"/>:</th> 
          <td><c:out value="${scenario.client.owner.faxNumber}"/></td> 
          <th colspan="2"></th> 
        </tr> 
      </table> 
    </fieldset>

    <fieldset>
      <legend>Claim Data</legend>
      <table class="details"> 

        <tr> 
          <th><fmt:message key="Program.Year.Tax.Data"/>:</th> 
          <td>
            <c:choose>
              <c:when test="${form.newBaseDataArrived[scenario.year]}">
                <img src="images/tick.gif" />
              </c:when>
              <c:when test="${form.taxYearDataGenerated[scenario.year]}">
                <img src="images/tick.gif" />
              </c:when>
              <c:when test="${scenario.farmingYear.programYearVersionId != null}">
                <img src="images/tick.gif" />
              </c:when>
              <c:otherwise>
                <img src="images/cross.gif"/>
              </c:otherwise>
            </c:choose>
          </td>
        </tr> 
        <tr> 
          <th><fmt:message key="Program.Year.Supplemental"/>:</th>
          <td>
            <c:choose>
              <c:when test="${form.hasProgramYearSupplemental}">
                <img src="images/tick.gif" alt="Yes" />
              </c:when>
              <c:otherwise>
                <img src="images/cross.gif" alt="No"/>
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
        
      <c:forEach var="year" varStatus="yearLoop" items="${form.refYears}">
        <tr>
          <th><fmt:message key="Reference.Year.Data"/> (<c:out value="${year}"/>):</th>
          <td>
            <c:choose>
              <c:when test="${form.newBaseDataArrived[year]}">
                <img src="images/tick.gif" />
              </c:when>
              <c:when test="${form.taxYearDataGenerated[year]}">
                <img src="images/tick.gif" />
              </c:when>
              <c:when test="${form.yearScenarioMap[year].farmingYear.programYearVersionId != null}">
                <img src="images/tick.gif" />
              </c:when>
              <c:otherwise>
                <img src="images/cross.gif"/>
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
      </c:forEach>
      
        <tr> 
          <th>Claim Status:</th> 
          <td><c:out value="${scenario.scenarioStateCodeDescription}"/></td> 
        </tr> 
      </table>
    </fieldset>
  
    <fieldset>
      <legend>Authorized Users</legend>
      <table class="searchresults"> 
        <tr> 
          <th><fmt:message key="Authorized.User.ID"/></th> 
          <th><fmt:message key="Activated.Date"/></th> 
          <th><fmt:message key="Name"/></th> 
          <th><fmt:message key="Phone"/></th>
        </tr> 

        <c:forEach var="user" items="${requestScope.authorizedUsers}">
          <tr> 
            <td><c:out value="${user.userid}"/></td>
            <td><fmt:formatDate pattern="yyyy-MM-dd" value="${user.activatedDate}" /></td>
            <td><c:out value="${user.userName}"/></td>
            <td><c:out value="${user.userPhoneNumber}"/></td>
          </tr> 
        </c:forEach>
      </table>
    
      <p style="font-size:smaller;font-style:italic">Note: Authorized Users are added by generating an invitation letter.</p>
    </fieldset>
    
  </div>
</div> 
