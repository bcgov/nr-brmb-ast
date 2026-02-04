<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<table class="numeric benefitsummary">
  <tr>
    <th><fmt:message key="Benefit.Calculation.Summary"/></th>
    <th width="100"><fmt:message key="Margin.Decline"/></th>
    <th width="100"><fmt:message key="Benefit"/></th>
  </tr>
  <tr>
    <th><fmt:message key="5.Positive.Margin"/></th>
    <td><fmt:formatNumber type="currency" value="${form.benefit.tier3MarginDecline}"/></td>
    <td><fmt:formatNumber type="currency" value="${form.benefit.tier3Benefit}"/></td>
  </tr>
  <tr>
    <th><fmt:message key="6.Negative.Margin"/></th>
    <td><fmt:formatNumber type="currency" value="${form.benefit.negativeMarginDecline}"/></td>
    <td><fmt:formatNumber type="currency" value="${form.benefit.negativeMarginBenefit}"/></td>
  </tr>
  <tr>
    <th><fmt:message key="Total.5.plus.6"/></th>
    <td>&nbsp;</td>
    <td><fmt:formatNumber type="currency" value="${form.benefit.benefitBeforeDeductions}"/></td>
  </tr>
  <tr>
    <th><fmt:message key="7.Deemed.Production.Insurance"/></th>
    <td>
      <html:text property="insuranceBenefit" style="width:93%" />
    </td>
    <td><fmt:formatNumber type="currency" value="${form.benefit.benefitAfterProdInsDeduction}"/></td>
  </tr>
  <tr>
    <th><fmt:message key="8.Interim.Benefit"/></th>
    <td>
      <c:choose>
        <c:when test="${scenario.interim}">
          <span class="nowrap"><html:text property="interimBenefitPercent" style="width:80%" /> %</span>
        </c:when>
        <c:otherwise>
          100%
        </c:otherwise>
      </c:choose>
    </td>
    <td><fmt:formatNumber type="currency" value="${form.benefit.benefitAfterInterimDeduction}"/></td>
  </tr>
  <tr>
    <th>
      <c:choose>
        <c:when test="${scenario.inCombinedFarm}"> <fmt:message key="Combined.Farm.Benefit"/> </c:when>
        <c:otherwise> <fmt:message key="Total.Calculated.Benefit"/> </c:otherwise>
      </c:choose>
    </th>
    <td>&nbsp;</td>
    <td class="totalAmount">
        <fmt:formatNumber type="currency" value="${form.benefit.totalBenefit}"/>
    </td>
  </tr>
</table>
