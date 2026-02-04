<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<!-- calcSummaries2019.jsp -->

<table style="width:100%" cellspacing="20" cellpadding="20">
  <tr>
    <td valign="top">
      <table class="numeric benefitsummary">
        <tr>
          <th colspan="2"><fmt:message key="Margin.Calculation.Summary"/></th>
        </tr>
        <tr>
          <th width="200">1. <fmt:message key="Ratio.Reference.Margin"/></th>
          <td class="totalAmount" width="100"><fmt:formatNumber type="currency" value="${form.benefit.ratioAdjustedReferenceMargin}"/></td>
        </tr>
        <tr>
          <th width="200">2. <fmt:message key="Additive.Reference.Margin"/></th>
          <td class="totalAmount" width="100"><fmt:formatNumber type="currency" value="${form.benefit.additiveAdjustedReferenceMargin}"/></td>
        </tr>
        <tr>
          <th width="200">3. <fmt:message key="Reference.Margin"/></th>
          <td class="totalAmount" width="100"><fmt:formatNumber type="currency" value="${form.benefit.adjustedReferenceMargin}"/></td>
        </tr>
        <tr>
          <th>4. <fmt:message key="Reference.Margin.Limit"/></th>
          <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.referenceMarginLimit}"/></td>
        </tr>
        <tr>
          <th>5. <fmt:message key="Capped.Reference.Margin.Limit"/> (70%)</th>
          <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.referenceMarginLimitCap}"/></td>
        </tr>
        <tr>
          <th>6. <fmt:message key="Reference.Margin.Limit.for.Benefit.Calc"/></th>
          <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.referenceMarginLimitForBenefitCalc}"/></td>
        </tr>
        <c:if test="${!scenario.inCombinedFarm}">
          <tr>
            <th><fmt:message key="Whole.Farm.Allocation"/></th>
            <td class="totalAmount"><fmt:formatNumber type="percent" value="${form.benefit.wholeFarmAllocation}"/></td>
          </tr>
        </c:if>
        <tr>
          <th>7. <fmt:message key="Applied.Reference.Margin.for.Benefit.Calc"/></th>
          <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.allocatedReferenceMargin}"/></td>
        </tr>
        <tr>
          <th>8. <fmt:message key="Program.Year.Margin"/></th>
          <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.programYearMargin}"/></td>
        </tr>
        <tr>
          <th><fmt:message key="Total.Margin.Decline"/> (7-8)</th>
          <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.marginDecline}"/></td>
        </tr>
      </table>
    </td>

    <td valign="top">
      <table class="numeric benefitsummary">
        <tr>
          <th><fmt:message key="Benefit.Calculation.Summary"/></th>
          <th width="100"><fmt:message key="Margin.Decline"/></th>
          <th width="100"><fmt:message key="Benefit"/></th>
        </tr>
        <tr>
          <th>9. <fmt:message key="Positive.Margin"/></th>
          <td><fmt:formatNumber type="currency" value="${form.benefit.tier3MarginDecline}"/></td>
          <td><fmt:formatNumber type="currency" value="${form.benefit.tier3Benefit}"/></td>
        </tr>
        <tr>
          <th>10. <fmt:message key="Negative.Margin"/></th>
          <td><fmt:formatNumber type="currency" value="${form.benefit.negativeMarginDecline}"/></td>
          <td><fmt:formatNumber type="currency" value="${form.benefit.negativeMarginBenefit}"/></td>
        </tr>
        <tr>
          <th><fmt:message key="Total"/> (9+10)</th>
          <td>&nbsp;</td>
          <td><fmt:formatNumber type="currency" value="${form.benefit.benefitBeforeDeductions}"/></td>
        </tr>
        <tr>
          <th>11. <fmt:message key="Deemed.Production.Insurance"/></th>
          <td>
            <html:text property="insuranceBenefit" style="width:93%" />
          </td>
          <td><fmt:formatNumber type="currency" value="${form.benefit.benefitAfterProdInsDeduction}"/></td>
        </tr>
        <tr>
          <th>12. <fmt:message key="Interim.Benefit"/></th>
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
            13.
            <c:choose>
              <c:when test="${scenario.inCombinedFarm}"> <fmt:message key="Combined.Farm.Benefit"/> </c:when>
              <c:otherwise> <fmt:message key="Total.Calculated.Benefit"/> </c:otherwise>
            </c:choose>
          </th>
          <td>&nbsp;</td>
          <td class="totalAmount">
              <fmt:formatNumber type="currency" value="${form.benefit.standardBenefit}"/>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  
</table>

<!-- BC Enhanced Benefit -->


<table style="width:100%" cellspacing="20" cellpadding="20">
  <tr>
    <td valign="top">
      <table class="numeric benefitsummary">
        <tr>
          <th colspan="2"><fmt:message key="BC.Enhanced.Margin.Calculation.Summary"/></th>
        </tr>
        <tr>
          <th width="200">14. <fmt:message key="Reference.Margin"/></th>
          <td class="totalAmount" width="100"><fmt:formatNumber type="currency" value="${form.benefit.adjustedReferenceMargin}"/></td>
        </tr>
        <c:if test="${!scenario.inCombinedFarm}">
          <tr>
            <th><fmt:message key="Whole.Farm.Allocation"/></th>
            <td class="totalAmount"><fmt:formatNumber type="percent" value="${form.benefit.wholeFarmAllocation}"/></td>
          </tr>
        </c:if>
         <tr>
           <th>15. <fmt:message key="Reference.Margin.Limit"/></th>
           <td class="totalAmount">N/A</td>
         </tr>
        <tr>
          <th>16. <fmt:message key="Reference.Margin.for.Benefit.Calc.enhanced"/> <c:out value="${refMarginLabelNumber}"/>
            </th>
          <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.enhancedReferenceMarginForBenefitCalculation}"/></td>
        </tr>
        <tr>
          <th>17. <fmt:message key="Program.Year.Margin"/></th>
          <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.programYearMargin}"/></td>
        </tr>
        <tr>
          <th><fmt:message key="Total.Margin.Decline"/> (16-17)</th>
          <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.enhancedMarginDecline}"/></td>
        </tr>
      </table>
    </td>
    <td valign="top">
      <table class="numeric benefitsummary">
        <tr>
          <th><fmt:message key="BC.Enhanced.Benefit.Calculation.Summary"/></th>
          <th width="100"><fmt:message key="Margin.Decline"/></th>
          <th width="100"><fmt:message key="Benefit"/></th>
        </tr>
        <tr>
          <th>18. <fmt:message key="Positive.Margin"/></th>
          <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedPositiveMarginDecline}"/></td>
          <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedPositiveMarginBenefit}"/></td>
        </tr>
        <tr>
          <th>19. <fmt:message key="Negative.Margin"/></th>
          <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedNegativeMarginDecline}"/></td>
          <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedNegativeMarginBenefit}"/></td>
        </tr>
        <tr>
          <th><fmt:message key="Total"/> (18+19)</th>
          <td>&nbsp;</td>
          <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedBenefitBeforeDeductions}"/></td>
        </tr>
        <tr>
          <th>20. <fmt:message key="Deemed.Production.Insurance"/></th>
          <td><fmt:formatNumber type="currency" value="${form.insuranceBenefit}"/></td>
          <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedBenefitAfterProdInsDeduction}"/></td>
        </tr>
        <tr>
          <th>21. <fmt:message key="Interim.Benefit"/></th>
          <td>
            <c:choose>
              <c:when test="${scenario.interim}">
                <span class="nowrap"><fmt:formatNumber type="percent" value="${form.benefit.interimBenefitPercent}"/></span>
              </c:when>
              <c:otherwise>
                100%
              </c:otherwise>
            </c:choose>
          </td>
          <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedBenefitAfterInterimDeduction}"/></td>
        </tr>
        <tr>
          <th>22.
            <c:choose>
              <c:when test="${scenario.inCombinedFarm}"> <fmt:message key="Combined.Farm.BC.Enhanced.Benefit"/> </c:when>
              <c:otherwise> <fmt:message key="BC.Enhanced.Benefit"/> </c:otherwise>
            </c:choose>
            <c:if test="${not scenario.lateParticipant}">
              <br>(23-13)
            </c:if>
          </th>
          <td>&nbsp;</td>
          <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.enhancedAdditionalBenefit}"/></td>
        </tr>
        <tr>
          <th>23.
            <c:choose>
              <c:when test="${scenario.inCombinedFarm}"> <fmt:message key="Combined.Farm.Benefit"/> </c:when>
              <c:otherwise> <fmt:message key="Total.Calculated.Benefit"/> </c:otherwise>
            </c:choose>
          </th>
          <td>&nbsp;</td>
          <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.enhancedTotalBenefit}"/></td>
        </tr>
      </table>
    </td>
  </tr>
  
</table>
