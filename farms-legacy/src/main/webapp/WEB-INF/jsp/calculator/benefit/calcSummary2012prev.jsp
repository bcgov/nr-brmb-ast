<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<table class="numeric">
  <tr>
    <th><fmt:message key="Payment.Tier"/></th>
    <th width="100"><fmt:message key="Tier.Trigger"/></th>
    <th width="100"><fmt:message key="Margin.Decline"/></th>
    <th width="100"><fmt:message key="Benefit"/></th>
  </tr>
  <tr>
    <th><fmt:message key="Tier.2.70.to.85.percent"/></th>
    <td><fmt:formatNumber type="currency" value="${form.benefit.tier2Trigger}"/></td>
    <td><fmt:formatNumber type="currency" value="${form.benefit.tier2MarginDecline}"/></td>
    <td><fmt:formatNumber type="currency" value="${form.benefit.tier2Benefit}"/></td>
  </tr>
  <tr>
    <th><fmt:message key="Supply.Managed.Adjustment"/></th>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td><fmt:formatNumber type="currency" value="${form.benefit.supplyManagedCommoditiesAdj}"/></td>
  </tr>
  <tr>
    <th><fmt:message key="Tier.3.0.to70.percent"/></th>
    <td><fmt:formatNumber type="currency" value="${form.benefit.tier3Trigger}"/></td>
    <td><fmt:formatNumber type="currency" value="${form.benefit.tier3MarginDecline}"/></td>
    <td><fmt:formatNumber type="currency" value="${form.benefit.tier3Benefit}"/></td>
  </tr>
  <tr>
    <th><fmt:message key="Negative.Margin"/></th>
    <td>&nbsp;</td>
    <td><fmt:formatNumber type="currency" value="${form.benefit.negativeMarginDecline}"/></td>
    <td><fmt:formatNumber type="currency" value="${form.benefit.negativeMarginBenefit}"/></td>
  </tr>
  <tr>
    <th><fmt:message key="Total"/></th>
    <td colspan="2">&nbsp;</td>
    <td><fmt:formatNumber type="currency" value="${form.benefit.benefitBeforeDeductions}"/></td>
  </tr>
  <tr>
    <th><fmt:message key="LESS.Production.Insurance.Deemed.Benefit"/></th>
    <td colspan="2">
      <html:text property="insuranceBenefit" />
    </td>
    <td><fmt:formatNumber type="currency" value="${form.benefit.benefitAfterProdInsDeduction}"/></td>
  </tr>
  <tr>
    <th><fmt:message key="Interim.Benefit"/></th>
    <td colspan="2">
      <c:choose>
        <c:when test="${scenario.interim}">
          <html:text property="interimBenefitPercent"/>
        </c:when>
        <c:otherwise>
          100%
        </c:otherwise>
      </c:choose>
    </td>
    <td><fmt:formatNumber type="currency" value="${form.benefit.benefitAfterInterimDeduction}"/></td>
  </tr>
  <c:if test="${!scenario.inCombinedFarm}">
    <tr>
      <th><fmt:message key="Applied.Benefit.Percentage"/></th>
      <td colspan="2">
        <html:text property="appliedBenefitPercent" />
      </td>
      <td><fmt:formatNumber type="currency" value="${form.benefit.benefitAfterAppliedBenefitPercent}"/></td>
    </tr>
  </c:if>
  <tr>
    <th>
      <c:choose>
        <c:when test="${scenario.inCombinedFarm}"> <fmt:message key="Combined.Farm.Benefit"/> </c:when>
        <c:otherwise> <fmt:message key="Calculated.Benefit"/> </c:otherwise>
      </c:choose>
    </th>
    <td colspan="2">&nbsp;</td>
    <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.totalBenefit}"/></td>
  </tr>
</table>
