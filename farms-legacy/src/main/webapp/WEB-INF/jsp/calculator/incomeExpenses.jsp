<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="yui/2.8.2r1/build/animation/animation-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/autocomplete/autocomplete-min.js"></script>


<c:if test="${form.addedNew}">
  <script type="text/javascript" src="<html:rewrite action="incomeExpenseLists" name="form" property="scenarioParams"/>"></script>

  <script type="text/javascript">
     //<![CDATA[

    function getItemSelectHandler(oDS, searchInputField, lineKey, lineCodeField, lineCodeDescriptionField) {
      return function() {
        // Use a LocalDataSource
        oDS.responseSchema = {fields : ["n", "i"]};

        // Instantiate the AutoComplete
        var oAC = new YAHOO.widget.AutoComplete(searchInputField, "itemContainer." + lineKey, oDS);
        oAC.resultTypeList = false;
        oAC.forceSelection = true;

        // Define an event handler to populate a hidden form field
        // when an item gets selected
        var lineCodeHiddenField = YAHOO.util.Dom.get(lineCodeField);
        var lineCodeDescriptionHiddenField = YAHOO.util.Dom.get(lineCodeDescriptionField);
        var selectionHandler = function(sType, aArgs) {
            var myAC = aArgs[0]; // reference back to the AC instance
            var elLI = aArgs[1]; // reference to the selected LI element
            var oData = aArgs[2]; // object literal of selected item's result data

            // update hidden form field with the selected item's ID
            lineCodeHiddenField.value = oData.i;
            lineCodeDescriptionHiddenField.value = oData.n;
        };
        oAC.itemSelectEvent.subscribe(selectionHandler);

        var clearHandler = function(sType, aArgs) {
            var myAC = aArgs[0]; // reference back to the AC instance
            var clearedValue = aArgs[1]; // The cleared value that did not match anything

            // update hidden form field with the selected item's ID
            lineCodeHiddenField.value = "";
            lineCodeDescriptionHiddenField.value = "";
        };
        oAC.selectionEnforceEvent.subscribe(clearHandler);

        return {
            oDS: oDS,
            oAC: oAC
        };
      }();
    }

    //]]>
  </script>
</c:if>

<script type="text/javascript">
  //<![CDATA[

  function deleteAdjustment(deleteAdjToolTip, adjustedTT, lineKey, year, reportedValue) {
    var deleteAdjustName = "deleteAdjust_" + lineKey + "_" + year;
    deleteAdjToolTip.cfg.setProperty("disabled", "true");
    if(adjustedTT) {
      adjustedTT.cfg.setProperty("disabled", "true");
    }
    var adjustedField = document.getElementById("item_" + lineKey + "_adjusted_" + year);
    adjustedField.style.backgroundColor = "white";
    adjustedField.value = reportedValue;
    document.getElementById(deleteAdjustName + "Container").style.display = 'none';
    document.getElementById("item_" + lineKey + "_deletedAdjustment_" + year).value = 'true';
    markFormAsDirty();
  }

  //]]>
</script>


<html:form action="saveIncomeExpenses" styleId="incomeExpensesForm" method="post" onsubmit="showProcessing()">
<html:hidden property="pin"/>
<html:hidden property="year"/>
<html:hidden property="scenarioNumber"/>
<html:hidden property="farmView"/>
<html:hidden property="addedNew"/>
<html:hidden property="scenarioRevisionCount"/>

<table>
  <tr>
    <td>
      <fieldset>
        <legend><fmt:message key="Farm.View"/></legend>
        <table>
          <tr>
            <td>
              <u:menuSelect action="farm870.do"
                  name="farmViewPicker"
                  paramName="farmView"
                  additionalFieldIds="incomeRadio,expenseRadio,eligibleRadio,ineligibleRadio,adjustedRadio,adjustmentsRadio,craRadio"
                  options="${form.farmViewOptions}"
                  selectedValue="${form.farmView}"
                  urlParams="pin=${form.pin}&year=${form.year}&scenarioNumber=${form.scenarioNumber}"
                  toolTip="Click here to change between views of the farm data."/>
            </td>
          </tr>
        </table>
      </fieldset>
    </td>
    <td>
      <fieldset>
        <legend><fmt:message key="Income"/>/<fmt:message key="Expenses"/></legend>
        <table>
          <tr>
            <td><html:radio property="incomeExpenseRadio" styleId="incomeRadio" value="income" onclick="javascript:displayFilterResults();"/><fmt:message key="Income"/></td>
            <td><html:radio property="incomeExpenseRadio" styleId="expenseRadio" value="expense" onclick="javascript:displayFilterResults();"/><fmt:message key="Expenses"/></td>
          </tr>
        </table>
      </fieldset>
    </td>
    <td>
      <fieldset>
        <legend><fmt:message key="Eligible"/>/<fmt:message key="Ineligible"/></legend>
        <table>
          <tr>
            <td><html:radio property="eligibilityRadio" styleId="eligibleRadio" value="eligible" onclick="javascript:displayFilterResults();"/><fmt:message key="Eligible"/></td>
            <td><html:radio property="eligibilityRadio" styleId="ineligibleRadio" value="ineligible" onclick="javascript:displayFilterResults();"/><fmt:message key="Ineligible"/></td>
          </tr>
        </table>
      </fieldset>
    </td>
    <td>
      <fieldset>
        <legend><fmt:message key="Financial.View"/></legend>
        <table>
          <tr>
            <td><html-el:radio property="financialViewRadio" styleId="adjustedRadio" value="adjusted" onclick="javascript:displayFilterResults();" disabled="${scenario.scenarioTypeCode eq 'CRA' or scenario.scenarioTypeCode eq 'LOCAL' or scenario.scenarioTypeCode eq 'GEN'  or scenario.scenarioTypeCode eq 'CHEF'}" /><fmt:message key="Adjusted"/></td>
            <td><html-el:radio property="financialViewRadio" styleId="adjustmentsRadio" value="adjustments" onclick="javascript:displayFilterResults();" disabled="${scenario.scenarioTypeCode eq 'CRA' or scenario.scenarioTypeCode eq 'LOCAL' or scenario.scenarioTypeCode eq 'GEN'  or scenario.scenarioTypeCode eq 'CHEF'}" /><fmt:message key="Adjustments"/></td>
            <td><html-el:radio property="financialViewRadio" styleId="craRadio" value="cra" onclick="javascript:displayFilterResults();" disabled="${scenario.scenarioTypeCode eq 'CRA' or scenario.scenarioTypeCode eq 'LOCAL' or scenario.scenarioTypeCode eq 'GEN'  or scenario.scenarioTypeCode eq 'CHEF'}" /><fmt:message key="CRA"/></td>
          </tr>
        </table>
      </fieldset>
    </td>
  </tr>
</table>

<c:set var="autoCompleteZIndex" value="${10000}"/>

<logic-el:iterate name="form" property="incomeExpenseFilterValues" id="incomeExpenseFilterValue">
<logic-el:iterate name="form" property="incomeEligibilityFilterValues" id="incomeEligibilityFilterValue">

<div id="total_<c:out value="${incomeExpenseFilterValue}_${incomeEligibilityFilterValue}"/>" style="display:none;">
  <table class="numeric" style="width:100%">
    <tr>
      <td rowspan="7" class="cellWhite">&nbsp;</td>
      <th><fmt:message key="Description"/></th>
        <logic-el:iterate name="form" property="requiredYears" id="year">
          <th width="80"><c:out value="${year}"/></th>
        </logic-el:iterate>
      <th class="refYearColumn" width="80">5 <fmt:message key="Year"/></th>
    </tr>
    <c:choose>
      <c:when test="${incomeExpenseFilterValue}">
        <tr>
          <th class="row"><fmt:message key="Unadjusted.Eligible.Expenses"/></th>
          <logic-el:iterate name="form" property="requiredYears" id="year">
            <td><fmt:formatNumber type="currency" value="${form.marginTotalMap[year].unadjustedAllowableExpenses}"/></td>
          </logic-el:iterate>
          <td><fmt:formatNumber type="currency" value="${form.marginTotalMap['5yearAverage'].unadjustedAllowableExpenses}"/></td>
        </tr>
        <tr>
          <th class="row"><fmt:message key="Less.Yardage"/><a href="#" onclick="loadWindow('<html:rewrite action="farm875" name="form" property="scenarioParams"/>&deductionLineItemType=YARDAGE', '1000', '450');"><img alt="info" src="images/bcgov_info.png" /></a></th>
          <logic-el:iterate name="form" property="requiredYears" id="year">
            <td><fmt:formatNumber type="currency" value="${form.marginTotalMap[year].yardageExpenses}"/></td>
          </logic-el:iterate>
          <td><fmt:formatNumber type="currency" value="${form.marginTotalMap['5yearAverage'].yardageExpenses}"/></td>
        </tr>
        <tr>
          <th class="row"><fmt:message key="Less.Contract.Work"/><a href="#" onclick="loadWindow('<html:rewrite action="farm875" name="form" property="scenarioParams"/>&deductionLineItemType=CONTRACT_WORK', '1000', '250');"><img alt="info" src="images/bcgov_info.png" /></a></th>
          <logic-el:iterate name="form" property="requiredYears" id="year">
            <td><fmt:formatNumber type="currency" value="${form.marginTotalMap[year].contractWorkExpenses}"/></td>
          </logic-el:iterate>
          <td><fmt:formatNumber type="currency" value="${form.marginTotalMap['5yearAverage'].contractWorkExpenses}"/></td>
        </tr>
        <tr>
          <th class="row"><fmt:message key="Total.Eligible.Expenses"/></th>
          <logic-el:iterate name="form" property="requiredYears" id="year">
            <td><fmt:formatNumber type="currency" value="${form.marginTotalMap[year].totalAllowableExpenses}"/></td>
          </logic-el:iterate>
          <td><fmt:formatNumber type="currency" value="${form.marginTotalMap['5yearAverage'].totalAllowableExpenses}"/></td>
        </tr>
        <tr>
          <th class="row"><fmt:message key="Total.Ineligible.Expenses"/></th>
          <logic-el:iterate name="form" property="requiredYears" id="year">
            <td><fmt:formatNumber type="currency" value="${form.marginTotalMap[year].totalUnallowableExpenses}"/></td>
          </logic-el:iterate>
          <td><fmt:formatNumber type="currency" value="${form.marginTotalMap['5yearAverage'].totalUnallowableExpenses}"/></td>
        </tr>
      </c:when>
      <c:otherwise>
        <tr>
          <th class="row"><fmt:message key="Supply.Managed.Commodities"/></th>
          <logic-el:iterate name="form" property="requiredYears" id="year">
            <td><fmt:formatNumber type="currency" value="${form.marginTotalMap[year].supplyManagedCommodityIncome}"/></td>
          </logic-el:iterate>
          <td><fmt:formatNumber type="currency" value="${form.marginTotalMap['5yearAverage'].supplyManagedCommodityIncome}"/></td>
        </tr>
        <tr>
          <th class="row"><fmt:message key="Unadjusted.Eligible.Income"/></th>
          <logic-el:iterate name="form" property="requiredYears" id="year">
            <td><fmt:formatNumber type="currency" value="${form.marginTotalMap[year].unadjustedAllowableIncome}"/></td>
          </logic-el:iterate>
          <td><fmt:formatNumber type="currency" value="${form.marginTotalMap['5yearAverage'].unadjustedAllowableIncome}"/></td>
        </tr>
        <tr>
          <th class="row"><fmt:message key="Less.Yardage"/><a href="#" onclick="loadWindow('<html:rewrite action="farm875" name="form" property="scenarioParams"/>&deductionLineItemType=YARDAGE', '1000', '450');"><img alt="info" src="images/bcgov_info.png" /></a></th>
          <logic-el:iterate name="form" property="requiredYears" id="year">
            <td><fmt:formatNumber type="currency" value="${form.marginTotalMap[year].yardageIncome}"/></td>
          </logic-el:iterate>
          <td><fmt:formatNumber type="currency" value="${form.marginTotalMap['5yearAverage'].yardageIncome}"/></td>
        </tr>
        <tr>
          <th class="row"><fmt:message key="Less.Program.Payments"/><a href="#" onclick="loadWindow('<html:rewrite action="farm875" name="form" property="scenarioParams"/>&deductionLineItemType=PROGRAM_PAYMENT', '1000', '600');"><img alt="info" src="images/bcgov_info.png" /></a></th>
          <logic-el:iterate name="form" property="requiredYears" id="year">
            <td><fmt:formatNumber type="currency" value="${form.marginTotalMap[year].programPaymentIncome}"/></td>
          </logic-el:iterate>
          <td><fmt:formatNumber type="currency" value="${form.marginTotalMap['5yearAverage'].programPaymentIncome}"/></td>
        </tr>
        <tr>
          <th class="row"><fmt:message key="Total.Eligible.Income"/></th>
          <logic-el:iterate name="form" property="requiredYears" id="year">
            <td><fmt:formatNumber type="currency" value="${form.marginTotalMap[year].totalAllowableIncome}"/></td>
          </logic-el:iterate>
          <td><fmt:formatNumber type="currency" value="${form.marginTotalMap['5yearAverage'].totalAllowableIncome}"/></td>
        </tr>
        <tr>
          <th class="row"><fmt:message key="Total.Ineligible.Income"/></th>
          <logic-el:iterate name="form" property="requiredYears" id="year">
            <td><fmt:formatNumber type="currency" value="${form.marginTotalMap[year].totalUnallowableIncome}"/></td>
          </logic-el:iterate>
          <td><fmt:formatNumber type="currency" value="${form.marginTotalMap['5yearAverage'].totalUnallowableIncome}"/></td>
        </tr>
      </c:otherwise>
    </c:choose>
    <tr>
      <td colspan="10" class="rowSpacer"><img src="images/spacer.gif" alt="" width="1" height="1"/></td>
    </tr>
    <tr>
      <th><fmt:message key="Code"/></th>
      <th><fmt:message key="Description"/></th>
        <logic-el:iterate name="form" property="requiredYears" id="year">
          <th width="80"><c:out value="${year}"/></th>
        </logic-el:iterate>
      <th class="refYearColumn">5 <fmt:message key="Year"/></th>
    </tr>

      <c:choose>
        <c:when test="${incomeEligibilityFilterValue}"><c:set var="dataSource" value="new YAHOO.util.LocalDataSource(YAHOO.farm.Data.lineItemsEligible)"/></c:when>
        <c:otherwise><c:set var="dataSource" value="new YAHOO.util.LocalDataSource(YAHOO.farm.Data.lineItemsIneligible)"/></c:otherwise>
      </c:choose>

      <logic-el:iterate name="form" property="lineKeys" id="lineKey">
        <c:set var="autoCompleteZIndex" value="${autoCompleteZIndex - 1}"/>
        <c:if test="${form.items[lineKey].expense == incomeExpenseFilterValue
                      and form.items[lineKey].eligible == incomeEligibilityFilterValue}">
          <tr>
            <c:choose>
              <c:when test="${form.items[lineKey].new == true}">
                <c:set var="colSpan" value="2"/>
              </c:when>
              <c:otherwise>
                <c:set var="colSpan" value="1"/>
              </c:otherwise>
            </c:choose>
            <th class="row" colspan="<c:out value="${colSpan}"/>">
              <c:set var="lineKeyFieldName" value="item(${lineKey}).lineKey"/>
              <c:set var="lineKeyFieldId" value="item_${lineKey}_lineKey"/>
              <c:set var="lineCodeFieldName" value="item(${lineKey}).lineCode"/>
              <c:set var="lineCodeFieldId" value="item_${lineKey}_lineCode"/>
              <c:set var="lineCodeDescriptionFieldName" value="item(${lineKey}).lineCodeDescription"/>
              <c:set var="lineCodeDescriptionFieldId" value="item_${lineKey}_lineCodeDescription"/>
              <c:set var="isNewFieldName" value="item(${lineKey}).new"/>
              <c:set var="isNewFieldId" value="item_${lineKey}_new"/>
              <c:set var="isExpenseFieldName" value="item(${lineKey}).expense"/>
              <c:set var="isExpenseFieldId" value="item_${lineKey}_expense"/>
              <c:set var="isEligibleFieldName" value="item(${lineKey}).eligible"/>
              <c:set var="isEligibleFieldId" value="item_${lineKey}_eligible"/>
              <input type="hidden" name="<c:out value="${lineKeyFieldName}"/>" id="<c:out value="${lineKeyFieldId}"/>" value="<c:out value="${lineKey}"/>" />
              <input type="hidden" name="<c:out value="${lineCodeFieldName}"/>" id="<c:out value="${lineCodeFieldId}"/>" value="<c:out value="${form.items[lineKey].lineCode}"/>" />
              <input type="hidden" name="<c:out value="${lineCodeDescriptionFieldName}"/>" id="<c:out value="${lineCodeDescriptionFieldId}"/>" value="<c:out value="${form.items[lineKey].lineCodeDescription}"/>" />
              <input type="hidden" name="<c:out value="${isNewFieldName}"/>" id="<c:out value="${isNewFieldId}"/>" value="<c:out value="${form.items[lineKey].new}"/>" />
              <input type="hidden" name="<c:out value="${isExpenseFieldName}"/>" id="<c:out value="${isExpenseFieldId}"/>" value="<c:out value="${form.items[lineKey].expense}"/>" />
              <input type="hidden" name="<c:out value="${isEligibleFieldName}"/>" id="<c:out value="${isEligibleFieldId}"/>" value="<c:out value="${form.items[lineKey].eligible}"/>" />
              <c:choose>
                <c:when test="${form.items[lineKey].new == true}">
                  <c:set var="searchInputFieldName" value="item(${lineKey}).searchInput"/>
                  <c:set var="searchInputFieldId" value="item_${lineKey}_searchInput"/>
                  <div id="autoCompleteContainer.<c:out value="${lineKey}"/>" style="z-index:<c:out value="${autoCompleteZIndex}"/>;position: relative;">
                    <input type="text" name="<c:out value="${searchInputFieldName}"/>" id="<c:out value="${searchInputFieldId}"/>" value="<c:out value="${form.items[lineKey].searchInput}"/>" style="width:98%" />
                    <div id="itemContainer.<c:out value="${lineKey}"/>" style="position: absolute;"></div>
                  </div>
                  <script type="text/javascript">
                    //<![CDATA[
                    YAHOO.farm.ItemSelectHandler = getItemSelectHandler(<c:out value="${dataSource},'${searchInputFieldId}','${lineKey}','${lineCodeFieldId}','${lineCodeDescriptionFieldId}'" escapeXml="false"/>);
                    //]]>
                  </script>
                </c:when>
                <c:otherwise>
                  <c:out value="${form.items[lineKey].lineCode}"/>
                </c:otherwise>
              </c:choose>
            </th>

            <c:if test="${form.items[lineKey].new == false}">
              <th class="row"><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
            </c:if>

            <logic-el:iterate name="form" property="requiredYears" id="year">
              <td width="80">
                <c:set var="adjustInputName" value="adjustInput_${lineKey}_${year}"/>
                <c:choose>
                  <c:when test="${form.items[lineKey].errors[year]}">
                    <c:set var="inputClass" value="adjustmentExistsError"/>
                  </c:when>
                  <c:when test="${!empty form.items[lineKey].adjustmentValues[year] && !form.items[lineKey].deletedAdjustments[year]}">
                    <c:set var="inputClass" value="adjustmentExists"/>
                    <script type="text/javascript">
                      //<![CDATA[
                      Farm.<c:out value="${adjustInputName}"/>TT = new YAHOO.widget.Tooltip("<c:out value="${adjustInputName}"/>TT",
                            { context:"<c:out value="${adjustInputName}"/>",
                              text:"<div style='text-align:left'><b>Original Value</b>: <c:out value="${form.items[lineKey].craValues[year]}"/><br />"
                              <c:if test="${!empty form.items[lineKey].adjustmentUsers[year]}">
                                + "<b>Adjusted By</b>: <c:out value="${form.items[lineKey].adjustmentUsers[year]}"/>"
                              </c:if>
                              + "</div>", autodismissdelay: 7500 });
                      //]]>
                    </script>
                  </c:when>
                  <c:otherwise>
                    <c:set var="inputClass" value=""/>
                  </c:otherwise>
                </c:choose>
                <div id="<c:out value="${adjustInputName}"/>">
                  <c:if test="${ form.farmView eq 'WHOLE' or ! empty form.yearOperationMap[year] }">
                    <c:set var="isDeletedFieldName" value="item(${lineKey}).deletedAdjustment(${year})"/>
                    <c:set var="isDeletedFieldId" value="item_${lineKey}_deletedAdjustment_${year}"/>
                    <input type="hidden" name="<c:out value="${isDeletedFieldName}"/>" id="<c:out value="${isDeletedFieldId}"/>" value="<c:out value="${form.items[lineKey].deletedAdjustments[year]}"/>" />
                    <html-el:text property="item(${lineKey}).adjusted(${year})" styleId="item_${lineKey}_adjusted_${year}" styleClass="${inputClass}" size="10" onclick="selectAll(this)" />
                  </c:if>
                </div>
              </td>
            </logic-el:iterate>
            <td class="refYearColumn"><fmt:formatNumber type="currency" value="${form.items[lineKey].adjustedFiveYearAverage}"/></td>
          </tr>
        </c:if>
      </logic-el:iterate>

      <c:if test="${ ! form.addedNew and ! form.readOnly}">
        <tr>
          <th class="row">&nbsp;</th>
          <th class="row">&nbsp;</th>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td><a id="addNewButton_<c:out value="${incomeExpenseFilterValue}_${incomeEligibilityFilterValue}"/>" href="#"><fmt:message key="Add.New"/></a></td>
        </tr>
      </c:if>
    <tr>
      <td colspan="10" class="rowSpacer"><img src="images/spacer.gif" alt="" width="1" height="1"/></td>
    </tr>
  </table>
  <a id="outputCsv_total_<c:out value="${incomeExpenseFilterValue}_${incomeEligibilityFilterValue}"/>" href="#"><fmt:message key="Export"/></a>
</div>

<div id="adjust_<c:out value="${incomeExpenseFilterValue}_${incomeEligibilityFilterValue}"/>" style="display:none;">
  <table class="numeric" style="width:100%">
    <tr>
        <th><fmt:message key="Code"/></th>
        <th><fmt:message key="Description"/></th>
      <logic-el:iterate name="form" property="requiredYears" id="year">
        <th width="80"><c:out value="${year}"/></th>
      </logic-el:iterate>
    </tr>

    <logic-el:iterate name="form" property="lineKeys" id="lineKey">
      <c:if test="${form.items[lineKey].new == false
                    and form.items[lineKey].expense == incomeExpenseFilterValue
                    and form.items[lineKey].eligible == incomeEligibilityFilterValue}">
        <tr>
          <th class="row"><c:out value="${form.items[lineKey].lineCode}"/></th>
          <th class="row"><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
          <logic-el:iterate name="form" property="requiredYears" id="year">
            <td width="80">
              <c:if test="${!empty form.items[lineKey].adjustmentValues[year] && !form.items[lineKey].deletedAdjustments[year]}">
                <c:set var="adjustInputName" value="adjustInput_${lineKey}_${year}"/>
                <c:set var="deleteAdjustName" value="deleteAdjust_${lineKey}_${year}"/>
                <div id="<c:out value="${deleteAdjustName}"/>Container">
                  <c:out value="${form.items[lineKey].adjustmentValues[year]}"/>
                  <c:if test="${ ! form.readOnly }">
                    <img id="<c:out value="${deleteAdjustName}"/>" src="images/error.png" alt="Delete Adjustment" width="16" height="16" align="middle" onclick="deleteAdjustment(Farm.<c:out value="${deleteAdjustName}"/>TT, Farm.<c:out value="${adjustInputName}"/>TT, '<c:out value="${lineKey}"/>', '<c:out value="${year}"/>', '<c:out value="${form.items[lineKey].craValues[year]}"/>');"/>
                  </c:if>
                </div>
                <script type="text/javascript">
                  //<![CDATA[
                  Farm.<c:out value="${deleteAdjustName}"/>TT =
                    new YAHOO.widget.Tooltip("<c:out value="${deleteAdjustName}"/>TT", { context:"<c:out value="${deleteAdjustName}"/>",
                          text:"Click here to delete the adjustment.", autodismissdelay: 7500 });
                  //]]>
                </script>
              </c:if>
            </td>
          </logic-el:iterate>
        </tr>
      </c:if>
    </logic-el:iterate>

    <tr>
      <td colspan="8" class="rowSpacer"><img src="images/spacer.gif" alt="" width="1" height="1"/></td>
    </tr>
  </table>
  <a id="outputCsv_adjust_<c:out value="${incomeExpenseFilterValue}_${incomeEligibilityFilterValue}"/>" href="#"><fmt:message key="Export"/></a>
</div>

<div id="cra_<c:out value="${incomeExpenseFilterValue}_${incomeEligibilityFilterValue}"/>" style="display:none;">
  <table class="numeric" style="width:100%">
    <tr>
        <th><fmt:message key="Code"/></th>
        <th><fmt:message key="Description"/></th>
      <logic-el:iterate name="form" property="requiredYears" id="year">
        <th width="80"><c:out value="${year}"/></th>
      </logic-el:iterate>
    </tr>

    <logic-el:iterate name="form" property="lineKeys" id="lineKey">
      <c:if test="${form.items[lineKey].new == false
                    and form.items[lineKey].expense == incomeExpenseFilterValue
                    and form.items[lineKey].eligible == incomeEligibilityFilterValue}">
        <tr>
          <th class="row"><c:out value="${form.items[lineKey].lineCode}"/></th>
          <th class="row"><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
          <logic-el:iterate name="form" property="requiredYears" id="year">
            <td width="80">
              <c:out value="${form.items[lineKey].craValues[year]}"/>
            </td>
          </logic-el:iterate>
        </tr>
      </c:if>
    </logic-el:iterate>

    <tr>
      <td colspan="8" class="rowSpacer"><img src="images/spacer.gif" alt="" width="1" height="1"/></td>
    </tr>
  </table>
  <a id="outputCsv_cra_<c:out value="${incomeExpenseFilterValue}_${incomeEligibilityFilterValue}"/>" href="#"><fmt:message key="Export"/></a>
</div>

</logic-el:iterate>
</logic-el:iterate>

</html:form>

<div align="right" style="margin-top:10px">
  <c:if test="${ ! form.readOnly }">
    <a id="saveButton" href="#"><fmt:message key="Save"/></a>
  </c:if>
</div>


<script type="text/javascript">
  //<![CDATA[

  function displayFilterResults() {
    var displayExpense = false;
    var displayEligible = true;
    var view;

    if(document.getElementById('expenseRadio').checked == true) displayExpense = true;
    if(document.getElementById('ineligibleRadio').checked == true) displayEligible = false;

    if(document.getElementById('adjustedRadio').checked == true) view='total';
    if(document.getElementById('adjustmentsRadio').checked == true) view='adjust';
    if(document.getElementById('craRadio').checked == true) view='cra';

    <logic-el:iterate name="form" property="incomeExpenseFilterValues" id="incomeExpenseFilterValue">
      <logic-el:iterate name="form" property="incomeEligibilityFilterValues" id="incomeEligibilityFilterValue">

        document.getElementById('total_<c:out value="${incomeExpenseFilterValue}_${incomeEligibilityFilterValue}"/>').style.display = 'none';
        document.getElementById('adjust_<c:out value="${incomeExpenseFilterValue}_${incomeEligibilityFilterValue}"/>').style.display = 'none';
        document.getElementById('cra_<c:out value="${incomeExpenseFilterValue}_${incomeEligibilityFilterValue}"/>').style.display = 'none';

      </logic-el:iterate>
    </logic-el:iterate>

    document.getElementById(view + '_' + displayExpense + '_' + displayEligible).style.display = '';
  }

  displayFilterResults();

  new YAHOO.widget.Button("saveButton");
  function submitFunc() { submitForm(document.getElementById('incomeExpensesForm')); }
  YAHOO.util.Event.addListener(document.getElementById("saveButton"), "click", submitFunc);

  function addNew() {
    disableDirtyFormCheck();
    var form = document.getElementById('incomeExpensesForm');
    form.action = '<html:rewrite action="addNewIncomeExpenses"/>';
    form.submit();
  }

  <logic-el:iterate name="form" property="incomeExpenseFilterValues" id="incomeExpenseFilterValue">
    <logic-el:iterate name="form" property="incomeEligibilityFilterValues" id="incomeEligibilityFilterValue">
      <c:set var="addNewButtonId" value="addNewButton_${incomeExpenseFilterValue}_${incomeEligibilityFilterValue}"/>
      new YAHOO.widget.Button("<c:out value="${addNewButtonId}"/>");
      YAHOO.util.Event.addListener(document.getElementById("<c:out value="${addNewButtonId}"/>"), "click", addNew);
    </logic-el:iterate>
  </logic-el:iterate>

  <c:if test="${ ! form.readOnly }">
    registerFormForDirtyCheck(document.getElementById("incomeExpensesForm"));
    excludeFieldFromDirtyCheck('incomeExpenseRadio');
    excludeFieldFromDirtyCheck('eligibilityRadio');
    excludeFieldFromDirtyCheck('financialViewRadio');
  </c:if>

  <c:if test="${form.addedNew}">
    markFormAsDirty();
  </c:if>

  function outputCsv() {
	  var incomeExpense = $('input:radio[name=incomeExpenseRadio]:checked').val();
	  var eligibility = $('input:radio[name=eligibilityRadio]:checked').val();
	  var financialView = $('input:radio[name=financialViewRadio]:checked').val();
	   	  
	  document.location.href = "outputIncomeExpenses.do?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>" + "&farmView=<c:out value="${form.farmView}"/>" + "&incomeExpenseRadio=" + incomeExpense + "&eligibilityRadio=" + eligibility + "&financialViewRadio=" + financialView;
  }

  <logic-el:iterate name="form" property="incomeExpenseFilterValues" id="incomeExpenseFilterValue">
    <logic-el:iterate name="form" property="incomeEligibilityFilterValues" id="incomeEligibilityFilterValue">
      <c:set var="outputCsvId" value="outputCsv_total_${incomeExpenseFilterValue}_${incomeEligibilityFilterValue}"/>
      new YAHOO.widget.Button("<c:out value="${outputCsvId}"/>");
      YAHOO.util.Event.addListener(document.getElementById("<c:out value="${outputCsvId}"/>"), "click", outputCsv);

      <c:set var="outputCsvId" value="outputCsv_adjust_${incomeExpenseFilterValue}_${incomeEligibilityFilterValue}"/>
      new YAHOO.widget.Button("<c:out value="${outputCsvId}"/>");
      YAHOO.util.Event.addListener(document.getElementById("<c:out value="${outputCsvId}"/>"), "click", outputCsv);

      <c:set var="outputCsvId" value="outputCsv_cra_${incomeExpenseFilterValue}_${incomeEligibilityFilterValue}"/>
      new YAHOO.widget.Button("<c:out value="${outputCsvId}"/>");
      YAHOO.util.Event.addListener(document.getElementById("<c:out value="${outputCsvId}"/>"), "click", outputCsv);
    </logic-el:iterate>
  </logic-el:iterate>

  $(document).ready(function() {
    var farmView = '<c:out value="${ form.farmView }"/>';
    var missingStatementAReceivedDates = '<c:out value="${ form.missingStatementAReceivedDates }"/>';
    if(farmView == 'WHOLE') {
      $('#incomeExpensesForm').find('input[type=text]').change(function() {
        alert('<fmt:message key="editing.in.whole.farm.view.warning"/>');
      });
    } else if(missingStatementAReceivedDates == 'true') {
    	$('#incomeExpensesForm').find('input[type=text]').change(function() {
    	  alert('<fmt:message key="error.local.statement.a.received.date.adjustment.screens"/>');
      });
    }
  });

  //]]>
</script>
