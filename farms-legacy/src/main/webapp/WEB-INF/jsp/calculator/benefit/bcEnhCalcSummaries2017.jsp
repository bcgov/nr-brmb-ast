<%@ include file="/WEB-INF/jsp/common/include.jsp" %>


<table style="width:100%" cellspacing="20" cellpadding="20">
  <tr>
    <td valign="top">
      <table class="numeric benefitsummary">
        <tr>
          <th colspan="2"><fmt:message key="BC.Enhanced.Margin.Calculation.Summary"/></th>
        </tr>
        <tr>
          <th width="200">
            <c:choose>
              <c:when test="${scenario.lateParticipant}"> 1. </c:when>
              <c:otherwise> 10. </c:otherwise>
            </c:choose>
            <fmt:message key="Reference.Margin"/></th>
          <td class="totalAmount" width="100"><fmt:formatNumber type="currency" value="${form.benefit.adjustedReferenceMargin}"/></td>
        </tr>
        <c:if test="${!scenario.inCombinedFarm}">
          <tr>
            <th><fmt:message key="Whole.Farm.Allocation"/></th>
            <td class="totalAmount"><fmt:formatNumber type="percent" value="${form.benefit.wholeFarmAllocation}"/></td>
          </tr>
        </c:if>
         <tr>
           <th>
            <c:choose>
              <c:when test="${scenario.lateParticipant}"> 2. </c:when>
              <c:otherwise> 11. </c:otherwise>
            </c:choose>
            <fmt:message key="Reference.Margin.Limit"/></th>
           <td class="totalAmount">N/A</td>
         </tr>
        <tr>
          <th>
            <c:choose>
              <c:when test="${scenario.lateParticipant}"> 3. <c:set var="refMarginLabelNumber" value="(1)"/> </c:when>
              <c:otherwise> 12. <c:set var="refMarginLabelNumber" value="(10)"/> </c:otherwise>
            </c:choose>
            <fmt:message key="Reference.Margin.for.Benefit.Calc.enhanced"/> <c:out value="${refMarginLabelNumber}"/>
            </th>
          <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.enhancedReferenceMarginForBenefitCalculation}"/></td>
        </tr>
        <tr>
          <th>
            <c:choose>
              <c:when test="${scenario.lateParticipant}"> 4. </c:when>
              <c:otherwise> 13. </c:otherwise>
            </c:choose>
            <fmt:message key="Program.Year.Margin"/></th>
          <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.programYearMargin}"/></td>
        </tr>
        <tr>
          <th>
            <fmt:message key="Total.Margin.Decline"/>
            <c:choose>
              <c:when test="${scenario.lateParticipant}"> (3-4) </c:when>
              <c:otherwise> (12-13) </c:otherwise>
            </c:choose>
          </th>
          <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.enhancedMarginDecline}"/></td>
        </tr>
        <c:if test="${!form.growingForward2013}">
          <tr>
            <th><fmt:message key="Benefit"/></th>
            <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.totalBenefit}"/></td>
          </tr>
        </c:if>
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
          <th>
            <c:choose>
              <c:when test="${scenario.lateParticipant}"> 5. </c:when>
              <c:otherwise> 14. </c:otherwise>
            </c:choose>
            <fmt:message key="Positive.Margin"/>
          </th>
          <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedPositiveMarginDecline}"/></td>
          <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedPositiveMarginBenefit}"/></td>
        </tr>
        <tr>
          <th>
            <c:choose>
              <c:when test="${scenario.lateParticipant}"> 6. </c:when>
              <c:otherwise> 15. </c:otherwise>
            </c:choose>
            <fmt:message key="Negative.Margin"/></th>
          <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedNegativeMarginDecline}"/></td>
          <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedNegativeMarginBenefit}"/></td>
        </tr>
        <tr>
          <th>
            <fmt:message key="Total"/>
            <c:choose>
              <c:when test="${scenario.lateParticipant}"> (5+6) </c:when>
              <c:otherwise> (14+15) </c:otherwise>
            </c:choose>
            </th>
          <td>&nbsp;</td>
          <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedBenefitBeforeDeductions}"/></td>
        </tr>
        <tr>
          <th>
            <c:choose>
              <c:when test="${scenario.lateParticipant}"> 7. </c:when>
              <c:otherwise> 16. </c:otherwise>
            </c:choose>
            <fmt:message key="Deemed.Production.Insurance"/></th>
          <td>
            <c:choose>
              <c:when test="${scenario.lateParticipant}">
                <html:text property="insuranceBenefit" style="width:93%" />
              </c:when>
              <c:otherwise>
                <fmt:formatNumber type="currency" value="${form.insuranceBenefit}"/>
              </c:otherwise>
            </c:choose>
          </td>
          <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedBenefitAfterProdInsDeduction}"/></td>
        </tr>
        <tr>
          <th>
            <c:choose>
              <c:when test="${scenario.lateParticipant}"> 8. </c:when>
              <c:otherwise> 17. </c:otherwise>
            </c:choose>
            <fmt:message key="Interim.Benefit"/></th>
          <td>
            <c:choose>
              <c:when test="${scenario.interim}">
                <c:choose>
                  <c:when test="${scenario.lateParticipant}">
                    <span class="nowrap"><html:text property="interimBenefitPercent" style="width:80%" /> %</span>
                  </c:when>
                  <c:otherwise>
                    <span class="nowrap"><fmt:formatNumber type="percent" value="${form.benefit.interimBenefitPercent}"/></span>
                  </c:otherwise>
                </c:choose>
              </c:when>
              <c:otherwise>
                100%
              </c:otherwise>
            </c:choose>
          </td>
          <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedBenefitAfterInterimDeduction}"/></td>
        </tr>
        <tr>
          <th>
            <c:choose>
              <c:when test="${scenario.lateParticipant}"> 9. </c:when>
              <c:otherwise> 18. </c:otherwise>
            </c:choose>
            <c:choose>
              <c:when test="${scenario.inCombinedFarm}"> <fmt:message key="Combined.Farm.BC.Enhanced.Benefit"/> </c:when>
              <c:otherwise> <fmt:message key="BC.Enhanced.Benefit"/> </c:otherwise>
            </c:choose>
            <c:if test="${not scenario.lateParticipant}">
              (19-9)
            </c:if>
          </th>
          <td>&nbsp;</td>
          <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.enhancedAdditionalBenefit}"/></td>
        </tr>
        <tr>
          <th>
            <c:choose>
              <c:when test="${scenario.lateParticipant}"> 10. </c:when>
              <c:otherwise> 19. </c:otherwise>
            </c:choose>
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

