<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<!-- combinedFarmSummary2020prev.jsp -->

<table style="width:100%" cellspacing="20" cellpadding="20">
  <tr>
    <td valign="top" align="right">
      <table class="numeric">
         <tr>
          <th><fmt:message key="PIN"/></th>
          <th><fmt:message key="Combined.Farm.Percent"/></th>
          <c:if test="${form.hasEnhancedBenefits}">
            <th><fmt:message key="Program.Year.Benefit"/></th>
            <th><fmt:message key="BC.Enhanced.Benefit"/></th>
          </c:if>
          <c:if test="${form.growingForward2018 and scenario.lateParticipant}">
            <th><fmt:message key="Late.Enrolment.Benefit.Reduction"/></th>
          </c:if>
          <th><fmt:message key="Total.Benefit"/></th>
        </tr>
        <c:forEach var="curScenario" items="${scenario.combinedFarm.scenarios}">
          <c:set var="curPin"><c:out value="${curScenario.client.participantPin}"/></c:set>
  
          <c:choose>
            <c:when test="${curPin eq scenario.client.participantPin}">
              <c:set var="rowStyle" value="font-weight:bold !important;"/>
            </c:when>
            <c:otherwise>
              <c:set var="rowStyle" value=""/>
            </c:otherwise>
          </c:choose>
  
          <tr>
            <td width="100" style="<c:out value="${rowStyle}"/>">
              <c:choose>
                <c:when test="${curPin eq scenario.client.participantPin}">
                  <c:out value="${curPin}"/>
                </c:when>
                <c:otherwise>
                  <a href="<html:rewrite action="farm800"/>?pin=<c:out value="${curPin}"/>&year=<c:out value="${form.year}"/>&scenarioNumber=<c:out value="${curScenario.scenarioNumber}"/>"><c:out value="${curPin}"/></a>
                </c:otherwise>
              </c:choose>
            </td>
            <td>
              <html-el:text property="appliedBenefitPercentMap(${curPin})" style="width:50px;text-align:center" />
            </td>
            <c:if test="${form.hasEnhancedBenefits}">
              <td style="<c:out value="${rowStyle}"/>"><fmt:formatNumber type="currency" value="${curScenario.farmingYear.benefit.standardBenefit}"/></td>
              <td style="<c:out value="${rowStyle}"/>"><fmt:formatNumber type="currency" value="${curScenario.farmingYear.benefit.enhancedAdditionalBenefit}"/></td>
            </c:if>
            <c:if test="${form.growingForward2018 and scenario.lateParticipant}">
              <td style="<c:out value="${rowStyle}"/>"><fmt:formatNumber type="currency" value="${curScenario.farmingYear.benefit.lateEnrolmentPenaltyAfterAppliedBenefitPercent}"/></td>
            </c:if>
            <td style="<c:out value="${rowStyle}"/>"><fmt:formatNumber type="currency" value="${curScenario.farmingYear.benefit.totalBenefit}"/></td>
          </tr>
        </c:forEach>
      </table>
    </td>
  </tr>
</table>
