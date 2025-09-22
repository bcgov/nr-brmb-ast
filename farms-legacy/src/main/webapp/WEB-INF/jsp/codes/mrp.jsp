<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<w:ifUserCanPerformAction action="editCodes">
  <c:set var="canEdit" value="true"/>
</w:ifUserCanPerformAction>

<h1>
  <c:choose>
    <c:when test="${!canEdit}">
      <fmt:message key="View"/>
    </c:when>
    <c:when test="${form.new}">
      <fmt:message key="Create.New"/>
    </c:when>
    <c:otherwise>
      <fmt:message key="Edit"/>
    </c:otherwise>
  </c:choose>
  <fmt:message key="Market.Rate.Premium"/> <fmt:message key="Code"/>
</h1>
<p></p>

<html:form action="saveMarketRatePremium" styleId="codeForm" method="post">
  <html:hidden property="new"/>
  <html:hidden property="marketRatePremiumId"/>
  <html:hidden property="revisionCount"/>

  <fieldset style="width:70%">
    <table class="formInput" style="width:100%">
      <tr>
        <th style="width:100px"><fmt:message key="Min.Total.Premium.Amount"/>:</th>
        <td>
          <c:choose>
            <c:when test="${canEdit}">
              <html:text property="minTotalPremiumAmount" size="11" maxlength="11"/>
            </c:when>
            <c:otherwise>
              <c:out value="${form.minTotalPremiumAmount}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
      <tr>
        <th style="width:100px"><fmt:message key="Max.Total.Premium.Amount"/>:</th>
        <td>
          <c:choose>
            <c:when test="${canEdit}">
              <html:text property="maxTotalPremiumAmount" size="11" maxlength="11"/>
            </c:when>
            <c:otherwise>
              <c:out value="${form.maxTotalPremiumAmount}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
      <tr>
        <th style="width:100px"><fmt:message key="Risk.Charge.Flat.Amount"/>:</th>
        <td>
          <c:choose>
            <c:when test="${canEdit}">
              <html:text property="riskChargeFlatAmount" size="11" maxlength="11"/>
            </c:when>
            <c:otherwise>
              <c:out value="${form.riskChargeFlatAmount}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
      <tr>
        <th style="width:100px"><fmt:message key="Risk.Charge.Percentage.Premium"/>:</th>
        <td>
          <c:choose>
            <c:when test="${canEdit}">
              <html:text property="riskChargePercentagePremium" size="11" maxlength="11"/>
            </c:when>
            <c:otherwise>
              <c:out value="${form.riskChargePercentagePremium}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
      <tr>
        <th style="width:100px"><fmt:message key="Adjust.Charge.Flat.Amount"/>:</th>
        <td>
          <c:choose>
            <c:when test="${canEdit}">
              <html:text property="adjustChargeFlatAmount" size="11" maxlength="11"/>
            </c:when>
            <c:otherwise>
              <c:out value="${form.adjustChargeFlatAmount}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
    </table>
  </fieldset>

  <div style="text-align:right;width:70%">
    <w:ifUserCanPerformAction action="editCodes">
      <u:yuiButton buttonId="saveButton" buttonLabel="Save" formId="codeForm"/>
      <c:if test="${!form.new}">
        <u:yuiButton buttonId="deleteButton" buttonLabel="Delete" formId="codeForm"
           action="deleteMarketRatePremium"/>
      </c:if>
      <u:dirtyFormCheck formId="codeForm"/>
    </w:ifUserCanPerformAction>
    <u:yuiButton buttonId="cancelButton" buttonLabel="Cancel" action="farm778"/>
  </div>

</html:form>
