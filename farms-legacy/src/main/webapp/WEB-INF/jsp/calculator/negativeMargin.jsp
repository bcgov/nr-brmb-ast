<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<table style="width:100%">
  <tr>
    <td>
      <table>
        <tr>
          <td>
            <fieldset>
            <legend><fmt:message key="Farm.View"/></legend>
              <table>
                <tr>
                  <td>
                    <u:menuSelect action="farm970.do"
                        name="farmViewPicker"
                        paramName="farmView"
                        options="${form.farmViewOptions}"
                        selectedValue="${form.farmView}"
                        urlParams="pin=${form.pin}&year=${form.year}&scenarioNumber=${form.scenarioNumber}"
                        toolTip="Click here to change between years of the farm data."/>
                  </td>
                </tr>
              </table>
            </fieldset>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>

<html:form action="saveNegativeMargins" styleId="negativeMarginsForm" method="post" onsubmit="showProcessing()">
<html:hidden property="pin"/>
<html:hidden property="year"/>
<html:hidden property="scenarioNumber"/>
<html:hidden property="farmView"/>

<div>
  <span style="float:right">
    <a href="javascript:toggleBackgroundMath()">
      <span class="hideBackgroundMath"><fmt:message key="Hide.Background.Math"/></span>
      <span class="showBackgroundMath"><fmt:message key="Show.Background.Math"/></span>
    </a>
  </span>
</div>

<table class="numeric" style="width:1150px">
  <tr>
    <th><fmt:message key="Code"/></th>
    <th><fmt:message key="Description"/></th>
    <th width="80"><fmt:message key="Deductible"/> (%)</th>
    <th width="80"><fmt:message key="IV"/></th>
    <th width="80"><fmt:message key="IV.Purchased"/></th>
    <th width="80"><fmt:message key="Reported"/> (qty unit)</th>
    <th width="80"><fmt:message key="Total.Probable.Yield"/> (qty unit)</th>
    <th width="80"><fmt:message key="Premiums.Paid"/></th>
    <th width="80"><fmt:message key="Claims.Received"/></th>
    <th width="80" class="refColumn"><fmt:message key="Deemed.Received"/></th>
    <th width="80"><fmt:message key="Deemed.PI.Value"/></th>
  </tr>

  <c:forEach var="inventoryItemCode" items="${form.inventoryItemCodes}" varStatus="inventoryItemCodeLoop">
    <tr>
      <td>
        <c:set var="negativeMarginIdFieldName" value="negativeMargin(${inventoryItemCode}).negativeMarginId"/>
        <c:set var="negativeMarginIdFieldId" value="negativeMargin_${inventoryItemCode}_negativeMarginId"/>
        <input type="hidden" name="<c:out value="${negativeMarginIdFieldName}"/>" id="<c:out value="${negativeMarginIdFieldId}"/>" value="<c:out value="${form.negativeMargins[inventoryItemCode].negativeMarginId}"/>"/>

        <c:set var="farmingOperationIdFieldName" value="negativeMargin(${inventoryItemCode}).farmingOperationId"/>
        <c:set var="farmingOperationIdFieldId" value="negativeMargin_${inventoryItemCode}_farmingOperationId"/>
        <input type="hidden" name="<c:out value="${farmingOperationIdFieldName}"/>" id="<c:out value="${farmingOperationIdFieldId}"/>" value="<c:out value="${form.negativeMargins[inventoryItemCode].farmingOperationId}"/>"/>

        <c:set var="inventoryItemCodeFieldName" value="negativeMargin(${inventoryItemCode}).inventoryItemCode"/>
        <c:set var="inventoryItemCodeFieldId" value="negativeMargin_${inventoryItemCode}_inventoryItemCode"/>
        <input type="hidden" name="<c:out value="${inventoryItemCodeFieldName}"/>" id="<c:out value="${inventoryItemCodeFieldId}"/>" value="<c:out value="${form.negativeMargins[inventoryItemCode].inventoryItemCode}"/>"/>

        <c:set var="revisionCountFieldName" value="negativeMargin(${inventoryItemCode}).revisionCount"/>
        <c:set var="revisionCountFieldId" value="negativeMargin_${inventoryItemCode}_revisionCount"/>
        <input type="hidden" name="<c:out value="${revisionCountFieldName}"/>" id="<c:out value="${revisionCountFieldId}"/>" value="<c:out value="${form.negativeMargins[inventoryItemCode].revisionCount}"/>"/>

        <c:set var="inventoryFieldName" value="negativeMargin(${inventoryItemCode}).inventory"/>
        <c:set var="inventoryCountFieldId" value="negativeMargin_${inventoryItemCode}_inventory"/>
        <input type="hidden" name="<c:out value="${inventoryFieldName}"/>" id="<c:out value="${inventoryFieldId}"/>" value="<c:out value="${form.negativeMargins[inventoryItemCode].inventory}"/>"/>

        <c:set var="insurableValueFieldName" value="negativeMargin(${inventoryItemCode}).insurableValue"/>
        <c:set var="insurableValueCountFieldId" value="negativeMargin_${inventoryItemCode}_insurableValue"/>
        <input type="hidden" name="<c:out value="${insurableValueFieldName}"/>" id="<c:out value="${insurableValueFieldId}"/>" value="<c:out value="${form.negativeMargins[inventoryItemCode].insurableValue}"/>"/>

        <c:set var="reportedFieldName" value="negativeMargin(${inventoryItemCode}).reported"/>
        <c:set var="reportedCountFieldId" value="negativeMargin_${inventoryItemCode}_reported"/>
        <input type="hidden" name="<c:out value="${reportedFieldName}"/>" id="<c:out value="${reportedFieldId}"/>" value="<c:out value="${form.negativeMargins[inventoryItemCode].reported}"/>"/>

        <c:set var="deemedReceivedFieldName" value="negativeMargin(${inventoryItemCode}).deemedReceived"/>
        <c:set var="deemedReceivedCountFieldId" value="negativeMargin_${inventoryItemCode}_deemedReceived"/>
        <input type="hidden" name="<c:out value="${deemedReceivedFieldName}"/>" id="<c:out value="${deemedReceivedFieldId}"/>" value="<c:out value="${form.negativeMargins[inventoryItemCode].deemedReceived}"/>"/>

        <c:set var="deemedPiValueFieldName" value="negativeMargin(${inventoryItemCode}).deemedPiValue"/>
        <c:set var="deemedPiValueCountFieldId" value="negativeMargin_${inventoryItemCode}_deemedPiValue"/>
        <input type="hidden" name="<c:out value="${deemedPiValueFieldName}"/>" id="<c:out value="${deemedPiValueFieldId}"/>" value="<c:out value="${form.negativeMargins[inventoryItemCode].deemedPiValue}"/>"/>

        <c:set var="premiumRateFieldName" value="negativeMargin(${inventoryItemCode}).premiumRate"/>
        <c:set var="premiumRateCountFieldId" value="negativeMargin_${inventoryItemCode}_premiumRate"/>
        <input type="hidden" name="<c:out value="${premiumRateFieldName}"/>" id="<c:out value="${premiumRateFieldId}"/>" value="<c:out value="${form.negativeMargins[inventoryItemCode].premiumRate}"/>"/>

        <c:set var="claimsCalculationFieldName" value="negativeMargin(${inventoryItemCode}).claimsCalculation"/>
        <c:set var="claimsCalculationCountFieldId" value="negativeMargin_${inventoryItemCode}_claimsCalculation"/>
        <input type="hidden" name="<c:out value="${claimsCalculationFieldName}"/>" id="<c:out value="${claimsCalculationFieldId}"/>" value="<c:out value="${form.negativeMargins[inventoryItemCode].claimsCalculation}"/>"/>

        <c:set var="premiumFieldName" value="negativeMargin(${inventoryItemCode}).premium"/>
        <c:set var="premiumCountFieldId" value="negativeMargin_${inventoryItemCode}_premium"/>
        <input type="hidden" name="<c:out value="${premiumFieldName}"/>" id="<c:out value="${premiumFieldId}"/>" value="<c:out value="${form.negativeMargins[inventoryItemCode].premium}"/>"/>

        <c:set var="mrpFieldName" value="negativeMargin(${inventoryItemCode}).mrp"/>
        <c:set var="mrpCountFieldId" value="negativeMargin_${inventoryItemCode}_mrp"/>
        <input type="hidden" name="<c:out value="${mrpFieldName}"/>" id="<c:out value="${mrpFieldId}"/>" value="<c:out value="${form.negativeMargins[inventoryItemCode].mrp}"/>"/>

        <c:set var="deemedPremiumFieldName" value="negativeMargin(${inventoryItemCode}).deemedPremium"/>
        <c:set var="deemedPremiumCountFieldId" value="negativeMargin_${inventoryItemCode}_deemedPremium"/>
        <input type="hidden" name="<c:out value="${deemedPremiumFieldName}"/>" id="<c:out value="${deemedPremiumFieldId}"/>" value="<c:out value="${form.negativeMargins[inventoryItemCode].deemedPremium}"/>"/>

        <c:out value="${inventoryItemCode}"/>
      </td>
      <td>
        <c:out value="${form.negativeMargins[inventoryItemCode].inventory}"/>
      </td>
      <td>
        <html-el:text property="negativeMargin(${inventoryItemCode}).deductiblePercentage" size="10" onclick="selectAll(this)"/>
      </td>
      <td>
        <fmt:formatNumber type="currency" value="${form.negativeMargins[inventoryItemCode].insurableValue}" minFractionDigits="3" maxFractionDigits="3"/>
      </td>
      <td>
        <html-el:text property="negativeMargin(${inventoryItemCode}).insurableValuePurchased" size="10" onclick="selectAll(this)"/>
      </td>
      <td>
        <fmt:formatNumber type="number" value="${form.negativeMargins[inventoryItemCode].reported}"/>
      </td>
      <td>
        <html-el:text property="negativeMargin(${inventoryItemCode}).guaranteedProdValue" size="10" onclick="selectAll(this)"/>
      </td>
      <td>
        <html-el:text property="negativeMargin(${inventoryItemCode}).premiumsPaid" size="10" onclick="selectAll(this)"/>
      </td>
      <td>
        <html-el:text property="negativeMargin(${inventoryItemCode}).claimsReceived" size="10" onclick="selectAll(this)"/>
      </td>
      <td class="refColumn">
        <fmt:formatNumber type="currency" value="${form.negativeMargins[inventoryItemCode].deemedReceived}"/>
      </td>
      <td>
        <fmt:formatNumber type="currency" value="${form.negativeMargins[inventoryItemCode].deemedPiValue}"/>
      </td>
    </tr>
  </c:forEach>

  <tr>
    <td colspan="11" class="rowSpacer hideBackgroundMath">
      <img src="images/spacer.gif" width="1" height="1" alt="Spacer"/>
    </td>
    <td colspan="10" class="rowSpacer showBackgroundMath">
      <img src="images/spacer.gif" width="1" height="1" alt="Spacer"/>
    </td>
  </tr>

  <tr>
    <td colspan="10" class="cellWhite hideBackgroundMath">&nbsp;</td>
    <td colspan="9" class="cellWhite showBackgroundMath">&nbsp;</td>
    <td class="totalAmount">
      <fmt:formatNumber type="currency" value="${form.subtotal}"/>
    </td>
  </tr>
  <tr>
    <td colspan="10" class="cellWhite hideBackgroundMath">
      <div align="right">
        &times; <fmt:formatNumber type="percent" value="${form.partnershipPercent}"/>
      </div>
    </td>
    <td colspan="9" class="cellWhite showBackgroundMath">
      <div align="right">
        Partnership Percent (&times; <fmt:formatNumber type="percent" value="${form.partnershipPercent}"/>)
      </div>
    </td>
    <td>
      <fmt:formatNumber type="currency" value="${form.totalForThisPartner}"/>
    </td>
  </tr>
  <tr>
    <td colspan="10" class="cellWhite hideBackgroundMath">
      <div align="right">
        <fmt:message key="Payable"/>
      </div>
    </td>
    <td colspan="9" class="cellWhite showBackgroundMath">
      <div align="right">
        <fmt:message key="Payable"/>
      </div>
    </td>
    <td>
      <fmt:formatNumber type="percent" value="${form.payablePercent}"/>
    </td>
  </tr>
  <tr>
    <td colspan="10" class="cellWhite hideBackgroundMath">
      <div align="right">
        <fmt:message key="Total.Deemed.PI"/>
      </div>
    </td>
    <td colspan="9" class="cellWhite showBackgroundMath">
      <div align="right">
        <fmt:message key="Total.Deemed.PI"/>
      </div>
    </td>
    <td class="totalAmount">
      <fmt:formatNumber type="currency" value="${form.totalPayable}"/>
    </td>
</table>

<br/>

<table class="numeric refTable">
  <tr>
    <th><fmt:message key="Code"/></th>
    <th><fmt:message key="Description"/></th>
    <th width="80"><fmt:message key="Premium.Rate"/></th>
    <th width="80"><fmt:message key="Claims.Calculation"/></th>
    <th width="80"><fmt:message key="Premium"/></th>
    <th width="80"><fmt:message key="MRP"/></th>
    <th width="80"><fmt:message key="Deemed.Premium"/></th>
  </tr>

  <c:forEach var="inventoryItemCode" items="${form.inventoryItemCodes}" varStatus="inventoryItemCodeLoop">
    <tr>
      <td><c:out value="${inventoryItemCode}"/></td>
      <td><c:out value="${form.negativeMargins[inventoryItemCode].inventory}"/></td>
      <td><fmt:formatNumber type="currency" value="${form.negativeMargins[inventoryItemCode].premiumRate}" minFractionDigits="4" maxFractionDigits="4"/></td>
      <td><fmt:formatNumber type="currency" value="${form.negativeMargins[inventoryItemCode].claimsCalculation}"/></td>
      <td><fmt:formatNumber type="currency" value="${form.negativeMargins[inventoryItemCode].premium}"/></td>
      <td><fmt:formatNumber type="currency" value="${form.negativeMargins[inventoryItemCode].mrp}"/></td>
      <td><fmt:formatNumber type="currency" value="${form.negativeMargins[inventoryItemCode].deemedPremium}"/></td>
    </tr>
  </c:forEach>

  <tr>
    <td colspan="7" class="rowSpacer">
      <img src="images/spacer.gif" width="1" height="1" alt="Spacer"/>
    </td>
  </tr>
</table>

<div align="right" style="margin-top:10px">
  <a id="saveButton" href="#"><fmt:message key="Save"/></a>
</div>

<script type="text/javascript">
  //<![CDATA[

  new YAHOO.widget.Button("saveButton");
  function submitFunc() { submitForm(document.getElementById('negativeMarginsForm')); }
  YAHOO.util.Event.addListener(document.getElementById("saveButton"), "click", submitFunc);

  function toggleBackgroundMath() {
    $('.refColumn').toggle();
    $('.refTable').toggle();
    $('.showBackgroundMath').toggle();
    $('.hideBackgroundMath').toggle();
  }

  $('.showBackgroundMath').show();
  $('.hideBackgroundMath').hide();
  $('.refColumn').hide();
  $('.refTable').hide();

  //]]>
</script>

</html:form>
