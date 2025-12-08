<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getScenario var="scenario" pin="${form.pin}" year="${form.year}" scenarioNumber="${form.scenarioNumber}" scope="request"/>

<h1 style="float:left"><c:out value="${scenario.client.owner.fullName}"/></h1>
<h1 style="float:right;white-space:nowrap">Program Year: <u:programYearSelect action="farm300.do" year="${form.year}" urlParams="pin=${form.pin}"/></h1>

<div style="clear:both"></div>
<div class="yui-navset"> 
  <ul class="yui-nav"> 
    <li><a href="<html:rewrite action="farm300"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>"  onClick="showProcessing()"><em><fmt:message key="Account.Details"/></em></a></li> 
    <li class="selected"><a href="<html:rewrite action="farm500"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>"  onClick="showProcessing()"><em><fmt:message key="Benefit.Overview"/></em></a></li>
    <li><a href="<html:rewrite action="farm400"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>"  onClick="showProcessing()"><em><fmt:message key="Benefit.History"/></em></a></li> 
  </ul> 
  <div class="yui-content"> 
    <%@ include file="/WEB-INF/jsp/account/overview/currentYear.jsp" %>
    <p></p>
    <tiles:insert attribute="subtab" />
  </div> 
</div> 