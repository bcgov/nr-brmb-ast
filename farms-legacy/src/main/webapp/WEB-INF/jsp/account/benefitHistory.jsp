<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getScenario var="scenario" pin="${form.pin}" year="${form.year}" scenarioNumber="${form.scenarioNumber}" scope="request"/>
<u:getFirstProvAdminYear var="firstProvAdminYear" scope="request"/>

<h1 style="float:left"><c:out value="${scenario.client.owner.fullName}"/></h1>
<h1 style="float:right;white-space:nowrap">Program Year: <u:programYearSelect action="farm300.do" year="${form.year}" urlParams="pin=${form.pin}"/></h1>

<div style="clear:both"></div>
<div class="yui-navset"> 
  <ul class="yui-nav"> 
    <li><a href="<html:rewrite action="farm300"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>"  onClick="showProcessing()"><em><fmt:message key="Account.Details"/></em></a></li> 
    <li><a href="<html:rewrite action="farm500"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>"  onClick="showProcessing()"><em><fmt:message key="Benefit.Overview"/></em></a></li>
    <li class="selected"><a href="<html:rewrite action="farm400"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>"  onClick="showProcessing()"><em><fmt:message key="Benefit.History"/></em></a></li> 
  </ul> 
  <div class="yui-content"> 
    <table class="numeric">
      <table class="numeric">
         <tr>
            <th></th>
            <c:forEach var="rs" items="${scenario.referenceScenarios}">
              <th><c:out value="${rs.year}"/></th>
            </c:forEach>
            <th><c:out value="${scenario.year}"/></th>
          </tr>
          
          <tr>
            <th><fmt:message key="Tier.1"/></th>
            <c:forEach var="rs" items="${scenario.referenceScenarios}">
              <td><fmt:formatNumber type="currency" value="0"/></td>
            </c:forEach>
            <td><fmt:formatNumber type="currency" value="0"/></td>
          </tr>
          
          <tr>
            <th><fmt:message key="Tier.2"/></th>
            <c:forEach var="rs" items="${scenario.referenceScenarios}">
              <td><fmt:formatNumber type="currency" value="${rs.farmingYear.benefit.tier2Benefit}"/></td>
            </c:forEach>
            <td><fmt:formatNumber type="currency" value="${scenario.farmingYear.benefit.tier2Benefit}"/></td>
          </tr>
          
          <tr>
            <th><fmt:message key="Tier.3"/></th>
            <c:forEach var="rs" items="${scenario.referenceScenarios}">
              <td><fmt:formatNumber type="currency" value="${rs.farmingYear.benefit.tier3Benefit}"/></td>
            </c:forEach>
            <td><fmt:formatNumber type="currency" value="${scenario.farmingYear.benefit.tier3Benefit}"/></td>
          </tr>
          
          <tr>
            <th><fmt:message key="Negative"/></th>
            <c:forEach var="rs" items="${scenario.referenceScenarios}">
              <td><fmt:formatNumber type="currency" value="${rs.farmingYear.benefit.negativeMarginBenefit}"/></td>
            </c:forEach>
            <td><fmt:formatNumber type="currency" value="${scenario.farmingYear.benefit.negativeMarginBenefit}"/></td>
          </tr>

          <tr>
            <th><fmt:message key="Total.Benefit"/></th>
            <c:forEach var="rs" items="${scenario.referenceScenarios}">
              <td><fmt:formatNumber type="currency" value="${rs.farmingYear.benefit.totalBenefit}"/></td>
            </c:forEach>
            <td><fmt:formatNumber type="currency" value="${scenario.farmingYear.benefit.totalBenefit}"/></td>
          </tr>
          
          <tr>
            <th><fmt:message key="Administration"/></th>
            <c:forEach var="rs" items="${scenario.referenceScenarios}">
              <td>
                  <c:choose>
                    <c:when test="${rs.year < firstProvAdminYear}">
                       <fmt:message key="Administration.Fed"/>
                    </c:when>
                    <c:otherwise>
                      <fmt:message key="Administration.Prov"/>
                    </c:otherwise>
                  </c:choose>
              </td>
            </c:forEach>
            <td><fmt:message key="Administration.Prov"/></td>
          </tr>
          
    </table>
  </div> 
</div> 