<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<!-- calcSummaries2023.jsp -->

<table style="width:100%" cellspacing="20" cellpadding="20">
  <tr>
    <td valign="top">
      <table class="numeric benefitsummary" style="width: 320px;">
        <tr>
          <th colspan="2"><fmt:message key="Margin.Calculation.Summary"/></th>
        </tr>
        <tr>
          <th width="200">
            <c:out value="${form.nextLineNumber}"/>.
            <fmt:message key="Ratio.Reference.Margin"/>
          </th>
          <td class="totalAmount" width="100"><fmt:formatNumber type="currency" value="${form.benefit.ratioAdjustedReferenceMargin}"/></td>
        </tr>
        <tr>
          <th width="200">
             <c:out value="${form.nextLineNumber}"/>.
             <fmt:message key="Additive.Reference.Margin"/>
           </th>
          <td class="totalAmount" width="100"><fmt:formatNumber type="currency" value="${form.benefit.additiveAdjustedReferenceMargin}"/></td>
        </tr>
        <tr>
          <th width="200">
             <c:out value="${form.nextLineNumber}"/>. <c:set var="refMarginLineNumber" value="${form.lineNumber}" />
            <fmt:message key="Reference.Margin"/>
          </th>
          <td class="totalAmount" width="100"><fmt:formatNumber type="currency" value="${form.benefit.adjustedReferenceMargin}"/></td>
        </tr>
        <tr>
          <th>
            <c:out value="${form.nextLineNumber}"/>.
            Payment Trigger (<fmt:formatNumber type="percent" value="${form.paymentTriggerFactor}"/> of 3)</th>
          <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.tier3Trigger}"/></td>
        </tr>
        <tr>
          <th>
            <c:out value="${form.nextLineNumber}"/>. <c:set var="programYearMarginLineNumber" value="${form.lineNumber}" />
            <fmt:message key="Program.Year.Margin"/>
          </th>
          <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.programYearMargin}"/></td>
        </tr>
        <tr>
          <th>
            <c:out value="${form.nextLineNumber}"/>. <c:set var="marginDeclineLineNumber" value="${form.lineNumber}" />
            Decline from Reference Margin (<c:out value="${refMarginLineNumber}"/>-<c:out value="${programYearMarginLineNumber}"/>)
          </th>
          <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.marginDecline}"/></td>
        </tr>
        <c:if test="${form.paymentCapEnabled}">
          <tr>
            <th>
              <c:out value="${form.nextLineNumber}"/>. <c:set var="paymentLimitationNumber" value="${form.lineNumber}" />
              <fmt:message key="Payment.Limitation"/> (<fmt:formatNumber type="percent" value="${form.paymentCapPercentageOfMarginDecline}"/> of <c:out value="${marginDeclineLineNumber}"/>)
            </th>
            <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.paymentCap}"/></td>
          </tr>
        </c:if>
      </table>
    </td>

    <td valign="top">
      <table class="numeric benefitsummary" style="width: 500px; float: right;">
        <tr>
          <th>AgriStability Benefit Calculation Summary</th>
          <th width="100"><fmt:message key="Margin.Decline"/></th>
          <th width="100"><fmt:message key="Benefit"/></th>
        </tr>
        <tr>
          <th>
            <c:out value="${form.nextLineNumber}"/>. <c:set var="positiveMarginLineNumber" value="${form.lineNumber}" />
            Positive Margin (Payment Trigger - PM &times; <fmt:formatNumber type="percent" value="${form.standardPositiveMarginCompensationRate}"/>)
          </th>
          <td><fmt:formatNumber type="currency" value="${form.benefit.tier3MarginDecline}"/></td>
          <td><fmt:formatNumber type="currency" value="${form.benefit.tier3Benefit}"/></td>
        </tr>
        <tr>
          <th>
            <c:out value="${form.nextLineNumber}"/>. <c:set var="negativeMarginLineNumber" value="${form.lineNumber}" />
            <fmt:message key="Negative.Margin"/>
            (<fmt:formatNumber type="percent" value="${form.standardNegativeMarginCompensationRate}"/> compensation)
          </th>
          <td><fmt:formatNumber type="currency" value="${form.benefit.negativeMarginDecline}"/></td>
          <td><fmt:formatNumber type="currency" value="${form.benefit.negativeMarginBenefit}"/></td>
        </tr>
        <tr>
          <th>
            <c:out value="${form.nextLineNumber}"/>. <c:set var="totalLineNumber" value="${form.lineNumber}" />
              <fmt:message key="Total"/>
              (<c:out value="${positiveMarginLineNumber}"/>+<c:out value="${negativeMarginLineNumber}"/>)
          </th>
          <td><fmt:formatNumber type="currency" value="${form.benefit.marginDeclineFromPaymentTrigger}"/></td>
          <td><fmt:formatNumber type="currency" value="${form.benefit.benefitBeforeDeductions}"/></td>
        </tr>
        <tr>
          <th>
            <c:out value="${form.nextLineNumber}"/>.
            <fmt:message key="Deemed.Production.Insurance"/> &times; <fmt:formatNumber type="percent" value="${form.productionInsuranceFactor}"/>
          </th>
          <td>
            <html:text property="insuranceBenefit" style="width:93%" />
          </td>
          <td><fmt:formatNumber type="currency" value="${form.benefit.productionInsuranceDeduction}"/></td>
        </tr>
        <tr>
          <th>
            <c:out value="${form.nextLineNumber}"/>.
            Production Insurance Deducted
          </th>
          <td>&nbsp;</td>
          <td><fmt:formatNumber type="currency" value="${form.benefit.benefitAfterProdInsDeduction}"/></td>
        </tr>
        <tr>
          <th>
            <c:out value="${form.nextLineNumber}"/>.
            <fmt:message key="Interim.Benefit"/>
          </th>
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
            <c:out value="${form.nextLineNumber}"/>.
            Apply Payment Limitation (lesser of <c:out value="${paymentLimitationNumber}"/> and <c:out value="${totalLineNumber}"/>)
          </th>
          <td></td>
          <td><fmt:formatNumber type="currency" value="${form.benefit.benefitAfterPaymentCap}"/></td>
        </tr>
        <c:if test="${scenario.lateParticipant}">
          <tr>
            <th>
              <c:out value="${form.nextLineNumber}"/>. <fmt:message key="Late.Enrolment.Benefit.Reduction"/>
            </th>
            <td>20%</td>
            <td>
                <fmt:formatNumber type="currency" value="${form.benefit.lateEnrolmentPenalty * -1}"/>
            </td>
          </tr>
        </c:if>
        <tr>
          <th>
            <c:out value="${form.nextLineNumber}"/>. <c:set var="standardTotalBenefitLineNumber" value="${form.lineNumber}" />
            <c:choose>
              <c:when test="${scenario.inCombinedFarm}"> <fmt:message key="Combined.Farm.Benefit"/> </c:when>
              <c:otherwise> <fmt:message key="Total.Calculated.Benefit"/> </c:otherwise>
            </c:choose>
          </th>
          <td>&nbsp;</td>
          <td class="totalAmount">
            <c:choose>
              <c:when test="${form.hasEnhancedBenefits}"> <fmt:formatNumber type="currency" value="${form.benefit.standardBenefit}"/> </c:when>
              <c:otherwise> <fmt:formatNumber type="currency" value="${form.benefit.totalBenefit}"/> </c:otherwise>
            </c:choose>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  
</table>


<c:if test="${form.hasEnhancedBenefits}">
  <!-- BC Enhanced Benefit -->

	<table style="width:100%" cellspacing="20" cellpadding="20">
	  <tr>
	    <td valign="top">
	      <!-- BC Enhanced Margin Calculation Summary removed because RML was eliminated from AgriStability calculation. -->
	    </td>
	    <td valign="top">
	      <table class="numeric benefitsummary" style="width: 500px; float: right;">
	        <tr>
	          <th><fmt:message key="BC.Enhanced.Benefit.Calculation.Summary"/></th>
	          <th width="100"><fmt:message key="Margin.Decline"/></th>
	          <th width="100"><fmt:message key="Benefit"/></th>
	        </tr>
	        <tr>
	          <th>
              <c:out value="${form.nextLineNumber}"/>. <c:set var="enhancedPositiveMarginLineNumber" value="${form.lineNumber}" />
              Positive Margin (Payment Trigger - PM &times; <fmt:formatNumber type="percent" value="${form.enhancedPositiveMarginCompensationRate}"/>)
	          </th>
	          <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedPositiveMarginDecline}"/></td>
	          <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedPositiveMarginBenefit}"/></td>
	        </tr>
	        <tr>
	          <th>
              <c:out value="${form.nextLineNumber}"/>. <c:set var="enhancedNegativeMarginLineNumber" value="${form.lineNumber}" />
              <fmt:message key="Negative.Margin"/>
              (<fmt:formatNumber type="percent" value="${form.enhancedNegativeMarginCompensationRate}"/> compensation)
            </th>
	          <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedNegativeMarginDecline}"/></td>
	          <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedNegativeMarginBenefit}"/></td>
	        </tr>
	        <tr>
	          <th>
              <c:out value="${form.nextLineNumber}"/>. <c:set var="enhancedTotalLineNumber" value="${form.lineNumber}" />
                <fmt:message key="Total"/>
                (<c:out value="${enhancedPositiveMarginLineNumber}"/>+<c:out value="${enhancedNegativeMarginLineNumber}"/>)
            </th>
	          <td><fmt:formatNumber type="currency" value="${form.benefit.marginDeclineFromPaymentTrigger}"/></td>
	          <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedBenefitBeforeDeductions}"/></td>
	        </tr>
	        <tr>
	          <th>
              <c:out value="${form.nextLineNumber}"/>.
              <fmt:message key="Deemed.Production.Insurance"/> &times; <fmt:formatNumber type="percent" value="${form.productionInsuranceFactor}"/>
            </th>
	          <td>
	            <fmt:formatNumber type="currency" value="${form.insuranceBenefit}"/>
	          </td>
	          <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedProductionInsuranceDeduction}"/></td>
	        </tr>
          <tr>
            <th>
              <c:out value="${form.nextLineNumber}"/>.
              Production Insurance Deducted
            </th>
            <td>&nbsp;</td>
            <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedBenefitAfterProdInsDeduction}"/></td>
          </tr>
	        <tr>
	          <th>
              <c:out value="${form.nextLineNumber}"/>.
              <fmt:message key="Interim.Benefit"/>
            </th>
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
            <th>
              <c:out value="${form.nextLineNumber}"/>.
              Apply Payment Limitation (lesser of <c:out value="${paymentLimitationNumber}"/> and <c:out value="${enhancedTotalLineNumber}"/>)
            </th>
            <td></td>
            <td><fmt:formatNumber type="currency" value="${form.benefit.enhancedBenefitAfterPaymentCap}"/></td>
          </tr>
          <tr>
            <th>
              <c:set var="enhancedTotalBenefitLineNumber" value="${form.nextLineNumber}" />
              <c:out value="${enhancedTotalBenefitLineNumber}"/>.
              <c:choose>
                <c:when test="${scenario.inCombinedFarm}"> <fmt:message key="Combined.Farm.Benefit"/> </c:when>
                <c:otherwise> <fmt:message key="Total.Calculated.Benefit"/> </c:otherwise>
              </c:choose>
            </th>
            <td>&nbsp;</td>
            <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.enhancedTotalBenefit}"/></td>
          </tr>
	        <tr>
	          <th>
              <c:out value="${form.nextLineNumber}"/>.
	            <c:choose>
	              <c:when test="${scenario.inCombinedFarm}"> <fmt:message key="Combined.Farm.BC.Enhanced.Benefit"/> </c:when>
	              <c:otherwise> BC Enhancement Benefit </c:otherwise>
	            </c:choose>
	            <c:if test="${not scenario.lateParticipant}">
	              (<c:out value="${enhancedTotalBenefitLineNumber}"/>-<c:out value="${standardTotalBenefitLineNumber}"/>)
	            </c:if>
	          </th>
	          <td>&nbsp;</td>
	          <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.benefit.enhancedAdditionalBenefit}"/></td>
	        </tr>
	        <c:if test="${scenario.lateParticipant}">
	          <tr>
	            <th>
	              <c:out value="${form.nextLineNumber}"/>. <fmt:message key="Late.Enrolment.Benefit.Reduction"/>
	            </th>
	            <td>20%</td>
	            <td>
	                <fmt:formatNumber type="currency" value="${form.benefit.enhancedLateEnrolmentPenalty * -1}"/>
	            </td>
	          </tr>
	        </c:if>
	      </table>
	    </td>
	  </tr>
	  
	</table>
</c:if>
