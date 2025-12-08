<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<c:choose>
  <c:when test="${form.growingForward2013}">
    <c:set var="refMarginLabel"><fmt:message key="1.Reference.Margin"/></c:set>
    <c:set var="allocatedRefMarginLabel"><fmt:message key="3.Reference.Margin.for.Benefit.Calc"/></c:set>
    <c:set var="programYearMarginLabel"><fmt:message key="4.Program.Year.Margin"/></c:set>
    <c:set var="marginDeclineLabel"><fmt:message key="Total.Margin.Decline.3.minus.4"/></c:set>
    <c:set var="summaryTable1Label"><fmt:message key="Margin.Calculation.Summary"/></c:set>
  </c:when>
  <c:otherwise>
    <c:set var="refMarginLabel"><fmt:message key="Reference.Margin"/></c:set>
    <c:set var="allocatedRefMarginLabel"><fmt:message key="Allocation.Ref..Margin"/></c:set>
    <c:set var="programYearMarginLabel"><fmt:message key="Program.Year.Margin"/></c:set>
    <c:set var="marginDeclineLabel"><fmt:message key="Margin.Decline"/></c:set>
    <c:set var="summaryTable1Label"><fmt:message key="Benefit.Calculation.Summary"/></c:set>
  </c:otherwise>
</c:choose>

<table class="numeric benefitsummary">
  <tr>
    <th colspan="2"><c:out value="${summaryTable1Label}"/></th>
  </tr>
  <tr>
    <th width="200"><c:out value="${refMarginLabel}"/></th>
    <td class="totalAmount" width="100"><fmt:formatNumber type="currency" value="${form.benefit.adjustedReferenceMargin}"/></td>
  </tr>
  <c:if test="${form.growingForward2013}">
     <tr>
       <th><fmt:message key="2.Reference.Margin.Limit"/></th>
       <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.referenceMarginLimit}"/></td>
     </tr>
  </c:if>
  <c:if test="${!scenario.inCombinedFarm}">
    <tr>
      <th><fmt:message key="Whole.Farm.Allocation"/></th>
      <td class="totalAmount"><fmt:formatNumber type="percent" value="${form.benefit.wholeFarmAllocation}"/></td>
    </tr>
  </c:if>
  <tr>
    <th><c:out value="${allocatedRefMarginLabel}"/></th>
    <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.allocatedReferenceMargin}"/></td>
  </tr>
  <tr>
    <th><c:out value="${programYearMarginLabel}"/></th>
    <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.programYearMargin}"/></td>
  </tr>
  <tr>
    <th><c:out value="${marginDeclineLabel}"/></th>
    <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.marginDecline}"/></td>
  </tr>
  <c:if test="${!form.growingForward2013}">
    <tr>
      <th><fmt:message key="Benefit"/></th>
      <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.totalBenefit}"/></td>
    </tr>
  </c:if>
</table>
