<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<c:set var="currentClaim" value="${scenario.farmingYear.benefit}"/>
<c:set var="currentClaimTotals" value="${scenario.farmingYear.marginTotal}"/>

<h2><fmt:message key="Current.Year"/> (<fmt:message key="includes.all.operations"/>)</h2> 
<table class="details"> 
  <tr> 
    <th><fmt:message key="Reference.Margin"/>:</th> 
    <td style="text-align:right;"><fmt:formatNumber type="currency" value="${currentClaim.adjustedReferenceMargin}"/></td> 
  </tr> 
  <tr>
    <th><fmt:message key="Program.Year.Margin"/> (<fmt:message key="PYM"/>):</th> 
    <td style="text-align:right;"><fmt:formatNumber type="currency" value="${currentClaim.programYearMargin}"/></td> 
  </tr> 
  <tr> 
    <th><fmt:message key="Total.Accrual.Adjustments"/>:</th> 
    <td style="text-align:right;"><fmt:formatNumber type="currency" value="${currentClaimTotals.totalAccrualAdjs}"/></td> 
  </tr> 
  <tr>
    <th><fmt:message key="PYM.with.Accrual.Adjustments"/>:</th> 
    <td style="text-align:right;">
      <c:if test="${currentClaim != null && currentClaimTotals != null}">
        <fmt:formatNumber type="currency" value="${currentClaim.programYearMargin + currentClaimTotals.totalAccrualAdjs}"/>
      </c:if>
    </td> 
  </tr> 
  <tr> 
    <th><fmt:message key="Total.Structural.Change.Adj"/>:</th> 
    <td style="text-align:right;"><fmt:formatNumber type="currency" value="${currentClaimTotals.structuralChangeAdjs}"/></td> 
  </tr> 
  <tr>
    <th><fmt:message key="PYM.w.Acc.and.Struc.Chng.Adj"/>:</th> 
    <td style="text-align:right;">
      <c:if test="${currentClaim != null && currentClaimTotals != null}">
        <fmt:formatNumber type="currency" value="${currentClaim.programYearMargin + currentClaimTotals.totalAccrualAdjs + currentClaimTotals.structuralChangeAdjs}"/>
      </c:if>
    </td> 
  </tr>  
  <tr>
    <th><fmt:message key="Total.Calculated.Benefit"/>:</th> 
    <td style="text-align:right;"><fmt:formatNumber type="currency" value="${currentClaim.totalBenefit}"/></td> 
  </tr> 
</table>