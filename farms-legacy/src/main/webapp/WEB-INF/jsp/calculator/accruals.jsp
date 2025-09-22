<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="yui/2.8.2r1/build/animation/animation-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/autocomplete/autocomplete-min.js"></script>

<logic:messagesPresent name="accrualMessages">
  <div class="messages">
    <ul>
      <html:messages id="message" name="accrualMessages">
        <li><c:out value="${message}"/></li>
      </html:messages>
    </ul>
  </div>
</logic:messagesPresent>

<c:if test="${form.addedNew}">
  <script type="text/javascript" src="<html:rewrite action="accrualItemLists"/>?year=<c:out value="${form.farmViewYear}"/>"></script>

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
  function deleteAdjustment(deleteAdjToolTip, startTT, endTT, lineKey, reportedStart, reportedEnd) {
    deleteAdjToolTip.cfg.setProperty("disabled", "true");
    if(startTT) {
      startTT.cfg.setProperty("disabled", "true");
    }
    if(endTT) {
      endTT.cfg.setProperty("disabled", "true");
    }
    var adjustedFieldStart = document.getElementById("item_" + lineKey + "_totalStartOfYearAmount");
    var adjustedFieldEnd = document.getElementById("item_" + lineKey + "_totalEndOfYearAmount");
    adjustedFieldStart.style.backgroundColor = "white";
    adjustedFieldEnd.style.backgroundColor = "white";
    adjustedFieldStart.value = reportedStart;
    adjustedFieldEnd.value = reportedEnd;
    document.getElementById("deleteAdjust_" + lineKey + "StartContainer").style.display = 'none';
    document.getElementById("deleteAdjust_" + lineKey + "EndContainer").style.display = 'none';
    document.getElementById("deleteAdjust_" + lineKey + "ButtonContainer").style.display = 'none';
    document.getElementById("item_" + lineKey + "_deleted").value = 'true';
    markFormAsDirty();
  }
  //]]>
</script>

<div id="dialog" class="yui3-skin-sam"  title="Import Accruals from Operation" style="display:none;">

    <table class="scenario-table" style="width:100%">
      <tr>
        <th></th>
        <th><fmt:message key="Pgm.Year.Version"/></th>
        <th><fmt:message key="Scenario"/></th>
        <th><fmt:message key="Operation"/></th>
        <th><fmt:message key="Type"/></th>
        <th><fmt:message key="Category"/></th>
        <th><fmt:message key="State"/></th>
        <th><fmt:message key="Created"/></th>
      </tr>
    <c:forEach var="item" items="${form.operationsForImport}">
      
      <tr>
        <td>
          <span id="selectButton<c:out value="${item.scenarioNumber}"/>" class="yui-button yui-push-button select-scenario-button"> 
            <em class="first-child"> 
               <button type="button" name="button<c:out value="${item.scenarioNumber}"/>" onClick="selectScenarioOperation('<c:out value="${item.scenarioNumber}"/>', '<c:out value="${item.alignmentKey}"/>')"><fmt:message key="Select"/></button> 
            </em> 
          </span> 
        </td>
        <td><c:out value="${item.programYearVersion}" /></td>
        <td><fmt:message key="Scenario" /> <c:out value="${item.scenarioNumber}" /></td>
        <td><fmt:message key="Operation"/> <c:out value="${item.alignmentKey}" /></td>
        <td><c:out value="${item.scenarioClassDescription}" /></td>
        <td><c:out value="${item.scenarioCategoryDescription}" /></td>
        <td><c:out value="${item.scenarioStateDescription}" /></td>
        <td><fmt:formatDate value="${item.scenarioCreatedDate}" pattern="yyyy-MM-dd" /></td>
      </tr>
    </c:forEach>
  </table>
</div>

<html:form action="saveAccruals" styleId="accrualsForm" method="post" onsubmit="showProcessing()">
<html:hidden property="pin"/>
<html:hidden property="year"/>
<html:hidden property="scenarioNumber"/>
<html:hidden property="farmView"/>
<html:hidden property="farmViewYear"/>
<html:hidden property="addedNew"/>
<html:hidden property="scenarioRevisionCount"/>
<html:hidden property="importScenarioNumber"/>
<html:hidden property="importOperationSchedule"/>

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
                    <u:menuSelect action="farm940.do"
                        name="farmViewYearPicker"
                        paramName="farmViewYear"
                        additionalFieldIds="inputsRadio,receivablesRadio,payablesRadio,eligibleRadio,ineligibleRadio,adjustedRadio,adjustmentsRadio,craRadio"
                        options="${form.farmViewYearOptions}"
                        selectedValue="${form.farmViewYear}"
                        urlParams="pin=${form.pin}&year=${form.year}&scenarioNumber=${form.scenarioNumber}&farmView=${form.farmView}"
                        toolTip="Click here to change between years of the farm data."/>
                  </td>
                  <td>
                    <u:menuSelect action="farm940.do"
                        name="farmViewPicker"
                        paramName="farmView"
                        additionalFieldIds="inputsRadio,receivablesRadio,payablesRadio,eligibleRadio,ineligibleRadio,adjustedRadio,adjustmentsRadio,craRadio"
                        options="${form.farmViewOptions}"
                        selectedValue="${form.farmView}"
                        urlParams="pin=${form.pin}&year=${form.year}&scenarioNumber=${form.scenarioNumber}&farmViewYear=${form.farmViewYear}"
                        toolTip="Click here to change between views of the farm data."/>
                  </td>
                </tr>
              </table>
            </fieldset>
          </td>
          <td>
            <fieldset>
              <legend><fmt:message key="Type"/></legend>
                <table>
                <tr>
                  <td><html:radio property="itemTypeRadio" styleId="inputsRadio" value="input" onclick="javascript:displayFilterResults();"/><fmt:message key="Input"/></td>
                  <td><html:radio property="itemTypeRadio" styleId="receivablesRadio" value="receivable" onclick="javascript:displayFilterResults();"/><fmt:message key="Receivable"/></td>
                  <td><html:radio property="itemTypeRadio" styleId="payablesRadio" value="payable" onclick="javascript:displayFilterResults();"/><fmt:message key="Payable"/></td>
                </tr>
              </table>
            </fieldset>
          </td>
          <td>
            <fieldset>
              <legend><fmt:message key="Eligible"/>/<fmt:message key="Ineligible"/></legend>
                <table>
                <tr>
                  <td><html:radio property="eligibilityRadio" styleId="eligibleRadio" value="true" onclick="javascript:displayFilterResults();"/><fmt:message key="Eligible"/></td>
                  <td><html:radio property="eligibilityRadio" styleId="ineligibleRadio" value="false" onclick="javascript:displayFilterResults();"/><fmt:message key="Ineligible"/></td>
                </tr>
              </table>
            </fieldset>
          </td>
          <td>
            <fieldset>
              <legend><fmt:message key="Financial.View"/></legend>
                <table>
                <tr>
                  <td><html-el:radio property="financialViewRadio" styleId="adjustedRadio" value="adjusted" onclick="javascript:displayFilterResults();" disabled="${scenario.scenarioTypeCode eq 'CRA' or scenario.scenarioTypeCode eq 'LOCAL' or scenario.scenarioTypeCode eq 'GEN' or scenario.scenarioTypeCode eq 'GEN'  or scenario.scenarioTypeCode eq 'CHEF'}" /><fmt:message key="Adjusted"/></td>
                  <td><html-el:radio property="financialViewRadio" styleId="adjustmentsRadio" value="adjustments" onclick="javascript:displayFilterResults();" disabled="${scenario.scenarioTypeCode eq 'CRA' or scenario.scenarioTypeCode eq 'LOCAL' or scenario.scenarioTypeCode eq 'GEN' or scenario.scenarioTypeCode eq 'GEN'  or scenario.scenarioTypeCode eq 'CHEF'}" /><fmt:message key="Adjustments"/></td>
                  <td><html-el:radio property="financialViewRadio" styleId="craRadio" value="cra" onclick="javascript:displayFilterResults();" disabled="${scenario.scenarioTypeCode eq 'CRA' or scenario.scenarioTypeCode eq 'LOCAL' or scenario.scenarioTypeCode eq 'GEN' or scenario.scenarioTypeCode eq 'GEN'  or scenario.scenarioTypeCode eq 'CHEF'}"/><fmt:message key="CRA" /></td>
                </tr>
              </table>
            </fieldset>
          </td>
<%--
          <td>
            <c:if test="${form.accrualsAvailableForImport and ! form.readOnly}">
              <span id="import-dialog-button" class="yui-button yui-push-button"> 
                <em class="first-child"> 
                  <button type="button" name="import-dialog-button" ><fmt:message key="Import"/></button> 
                </em> 
              </span> 
            </c:if>
          </td>
--%>
        </tr>
      </table>
    </td>
  </tr>
</table>

<c:set var="autoCompleteZIndex" value="${10000}"/>

<logic-el:iterate name="form" property="itemTypes" id="itemType">
<logic-el:iterate name="form" property="eligibilityFilterValues" id="eligibilityFilterValue">

  <div id="total_<c:out value="${itemType}_${eligibilityFilterValue}"/>" style="display:none;">
    <table class="numeric"  style="width:100%">
      <tr>
        <th><fmt:message key="Code"/></th>
        <th><fmt:message key="Description"/></th>
        <th width="100"><fmt:message key="Start.Value"/></th>
        <th width="100"><fmt:message key="End.Value"/></th>
        <th width="100"><fmt:message key="Change.in.Value"/></th>
      </tr>

      <c:choose>
        <c:when test="${itemType eq 'class3' and eligibilityFilterValue eq 'true' }"><c:set var="dataSource" value="new YAHOO.util.LocalDataSource(YAHOO.farm.Data.inputItemsEligible)"/></c:when>
        <c:when test="${itemType eq 'class3' and eligibilityFilterValue eq 'false'}"><c:set var="dataSource" value="new YAHOO.util.LocalDataSource(YAHOO.farm.Data.inputItemsIneligible)"/></c:when>
        <c:when test="${itemType eq 'class4' and eligibilityFilterValue eq 'true' }"><c:set var="dataSource" value="new YAHOO.util.LocalDataSource(YAHOO.farm.Data.receivableItemsEligible)"/></c:when>
        <c:when test="${itemType eq 'class4' and eligibilityFilterValue eq 'false'}"><c:set var="dataSource" value="new YAHOO.util.LocalDataSource(YAHOO.farm.Data.receivableItemsIneligible)"/></c:when>
        <c:when test="${itemType eq 'class5' and eligibilityFilterValue eq 'true' }"><c:set var="dataSource" value="new YAHOO.util.LocalDataSource(YAHOO.farm.Data.payableItemsEligible)"/></c:when>
        <c:when test="${itemType eq 'class5' and eligibilityFilterValue eq 'false'}"><c:set var="dataSource" value="new YAHOO.util.LocalDataSource(YAHOO.farm.Data.payableItemsIneligible)"/></c:when>
      </c:choose>

      <logic-el:iterate name="form" property="lineKeys" id="lineKey">
        <c:set var="autoCompleteZIndex" value="${autoCompleteZIndex - 1}"/>
        <c:if test="${form.items[lineKey].itemType eq itemType and form.items[lineKey].eligible eq eligibilityFilterValue}">
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
              <c:set var="itemTypeFieldName" value="item(${lineKey}).itemType"/>
              <c:set var="itemTypeFieldId" value="item_${lineKey}_itemType"/>
              <c:set var="eligibleFieldName" value="item(${lineKey}).eligible"/>
              <c:set var="eligibleFieldId" value="item_${lineKey}_eligible"/>
              <c:set var="isNewFieldName" value="item(${lineKey}).new"/>
              <c:set var="isNewFieldId" value="item_${lineKey}_new"/>
              <c:set var="isDeletedFieldName" value="item(${lineKey}).deleted)"/>
              <c:set var="isDeletedFieldId" value="item_${lineKey}_deleted"/>
              <input type="hidden" name="<c:out value="${lineKeyFieldName}"/>" id="<c:out value="${lineKeyFieldId}"/>" value="<c:out value="${lineKey}"/>" />
              <input type="hidden" name="<c:out value="${lineCodeFieldName}"/>" id="<c:out value="${lineCodeFieldId}"/>" value="<c:out value="${form.items[lineKey].lineCode}"/>" />
              <input type="hidden" name="<c:out value="${lineCodeDescriptionFieldName}"/>" id="<c:out value="${lineCodeDescriptionFieldId}"/>" value="<c:out value="${form.items[lineKey].lineCodeDescription}"/>" />
              <input type="hidden" name="<c:out value="${itemTypeFieldName}"/>" id="<c:out value="${itemTypeFieldId}"/>" value="<c:out value="${form.items[lineKey].itemType}"/>" />
              <input type="hidden" name="<c:out value="${eligibleFieldName}"/>" id="<c:out value="${eligibleFieldId}"/>" value="<c:out value="${form.items[lineKey].eligible}"/>" />
              <input type="hidden" name="<c:out value="${isNewFieldName}"/>" id="<c:out value="${isNewFieldId}"/>" value="<c:out value="${form.items[lineKey].new}"/>" />
              <input type="hidden" name="<c:out value="${isDeletedFieldName}"/>" id="<c:out value="${isDeletedFieldId}"/>" value="<c:out value="${form.items[lineKey].deleted}"/>" />
              <c:choose>
                <c:when test="${form.items[lineKey].new == true}">
                  <c:set var="searchInputFieldName" value="item(${lineKey}).searchInput"/>
                  <c:set var="searchInputFieldId" value="item_${lineKey}_searchInput"/>
                  <div id="autoCompleteContainer.<c:out value="${lineKey}"/>" style="z-index:<c:out value="${autoCompleteZIndex}"/>;position: relative;">
                    <input type="text" name="<c:out value="${searchInputFieldName}"/>" id="<c:out value="${searchInputFieldId}"/>" value="<c:out value="${form.items[lineKey].searchInput}"/>" style="width:98.7%" />
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
              <th><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
            </c:if>

            <td width="100">
              <c:set var="adjustInputName" value="adjustInput_${lineKey}_start"/>
              <c:choose>
                <c:when test="${form.items[lineKey].startError}">
                  <c:set var="inputClass" value="adjustmentExistsError"/>
                </c:when>
                <c:when test="${form.items[lineKey].showAdjToolTipStart}">
                  <c:set var="inputClass" value="adjustmentExists"/>
                  <script type="text/javascript">
                    //<![CDATA[
                    Farm.<c:out value="${adjustInputName}"/>TT =
                      new YAHOO.widget.Tooltip("<c:out value="${adjustInputName}"/>TT",
                          { context:"<c:out value="${adjustInputName}"/>",
                            text:"<div style='text-align:left'><b>Original Value</b>: <c:out value="${form.items[lineKey].reportedStartOfYearAmount}"/><br /><c:if test="${!empty form.items[lineKey].adjustedByUserId}"><b>Adjusted By</b>: <c:out value="${form.items[lineKey].adjustedByUserId}"/></c:if></div>",
                            autodismissdelay: 7500 });
                    //]]>
                  </script>
                </c:when>
                <c:otherwise>
                  <c:set var="inputClass" value=""/>
                </c:otherwise>
              </c:choose>
              <div id="<c:out value="${adjustInputName}"/>">
                <html-el:text property="item(${lineKey}).totalStartOfYearAmount" styleId="item_${lineKey}_totalStartOfYearAmount" styleClass="${inputClass}" onclick="selectAll(this)" />
              </div>
            </td>

            <td width="100">
              <c:set var="adjustInputName" value="adjustInput_${lineKey}_end"/>
              <c:choose>
                <c:when test="${form.items[lineKey].endError}">
                  <c:set var="inputClass" value="adjustmentExistsError"/>
                </c:when>
                <c:when test="${form.items[lineKey].showAdjToolTipEnd}">
                  <c:set var="inputClass" value="adjustmentExists"/>
                  <script type="text/javascript">
                    //<![CDATA[
                    Farm.<c:out value="${adjustInputName}"/>TT =
                      new YAHOO.widget.Tooltip("<c:out value="${adjustInputName}"/>TT",
                          { context:"<c:out value="${adjustInputName}"/>",
                            text:"<div style='text-align:left'><b>Original Value</b>: <c:out value="${form.items[lineKey].reportedEndOfYearAmount}"/><br /><c:if test="${!empty form.items[lineKey].adjustedByUserId}"><b>Adjusted By</b>: <c:out value="${form.items[lineKey].adjustedByUserId}"/></c:if></div>",
                            autodismissdelay: 7500 });
                    //]]>
                  </script>
                </c:when>
                <c:otherwise>
                  <c:set var="inputClass" value=""/>
                </c:otherwise>
              </c:choose>
              <div id="<c:out value="${adjustInputName}"/>">
                <html-el:text property="item(${lineKey}).totalEndOfYearAmount" styleId="item_${lineKey}_totalEndOfYearAmount" styleClass="${inputClass}" onclick="selectAll(this)" />
              </div>
            </td>

            <td width="100">
              <fmt:formatNumber type="currency" value="${form.items[lineKey].changeInValue}"/>
            </td>
          </tr>
        </c:if>

      </logic-el:iterate>

      <c:if test="${ ! form.addedNew and ! form.readOnly}">
        <tr>
          <th>&nbsp;</th>
          <th>&nbsp;</th>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td><a id="<c:out value="addNewButton_${itemType}_${eligibilityFilterValue}"/>" href="#" class="button"><fmt:message key="Add.New"/></a></td>
        </tr>
      </c:if>

      <tr>
        <td colspan="5" class="rowSpacer"><img src="images/spacer.gif" width="1" height="1" alt="" /></td>
      </tr>
      
      <c:if test="${itemType eq form.typeReceivable and not form.programYear}">
        <tr>
          <th colspan="2" class="cellWhite">&nbsp;</th>
          <th colspan="2">
            <div align="right">
              <fmt:message key="Less.Deferred.Program.Payments"/>
            </div>
          </th>
          <td width="100" class="totalAmount"><fmt:formatNumber type="currency" value="${form.deferredProgramPayments}"/></td>
        </tr>
      </c:if>

      <c:if test="${eligibilityFilterValue eq 'true'}">
      <tr>
        <th colspan="2" class="cellWhite">&nbsp;</th>
        <th colspan="2">
          <div align="right">
            <c:choose>
              <c:when test="${itemType eq form.typeInput}">
                <fmt:message key="Total.Change.in.Value.of.Purchased.Inputs"/>
              </c:when>
              <c:when test="${itemType eq form.typeReceivable}">
                <fmt:message key="Total.Change.in.Value.of.Receivables"/>
              </c:when>
              <c:when test="${itemType eq form.typePayable}">
                <fmt:message key="Total.Change.in.Value.of.Payables"/>
              </c:when>
            </c:choose>
          </div>
        </th>
        <td width="100" class="totalAmount"><fmt:formatNumber type="currency" value="${form.totalValues[itemType]}"/></td>
      </tr>
      </c:if>
    </table>
  </div>


  <div id="adjust_<c:out value="${itemType}_${eligibilityFilterValue}"/>" style="display:none;">
    <table class="numeric"  style="width:100%">
      <tr>
        <th><fmt:message key="Code"/></th>
        <th><fmt:message key="Description"/></th>
        <th width="100"><fmt:message key="Start.Value"/></th>
        <th width="100"><fmt:message key="End.Value"/></th>
        <th width="110"><fmt:message key="Delete.Adjustment"/></th>
      </tr>

      <logic-el:iterate name="form" property="lineKeys" id="lineKey">
        <c:if test="${form.items[lineKey].itemType eq itemType and form.items[lineKey].eligible eq eligibilityFilterValue and ! form.items[lineKey].new}">
          <c:set var="adjustInputName" value="adjustInput_${lineKey}"/>
          <c:set var="deleteAdjustName" value="deleteAdjust_${lineKey}"/>
          <tr>
            <th>
              <c:out value="${form.items[lineKey].lineCode}"/>
            </th>
            <th>
              <c:out value="${form.items[lineKey].lineCodeDescription}"/>
            </th>
            <td width="100">
              <c:if test="${ ! form.items[lineKey].deleted }">
                <div id="<c:out value="${deleteAdjustName}"/>StartContainer">
                  <c:out value="${form.items[lineKey].adjStartOfYearAmount}"/>
                </div>
              </c:if>
            </td>
            <td width="100">
              <c:if test="${ ! form.items[lineKey].deleted }">
                <div id="<c:out value="${deleteAdjustName}"/>EndContainer">
                  <c:out value="${form.items[lineKey].adjEndOfYearAmount}"/>
                </div>
              </c:if>
            </td>
            <td width="100">
              <c:if test="${!empty form.items[lineKey].adjustmentId && !form.items[lineKey].deleted}">
                <div id="<c:out value="${deleteAdjustName}"/>ButtonContainer">
                  <c:if test="${ ! form.readOnly }">
                    <img id="<c:out value="${deleteAdjustName}"/>" src="images/error.png" alt="<fmt:message key="Delete.Adjustment"/>" width="16" height="16" align="middle" onclick="deleteAdjustment(Farm.<c:out value="${deleteAdjustName}"/>TT, Farm.<c:out value="${adjustInputName}_start"/>TT, Farm.<c:out value="${adjustInputName}_end"/>TT, '<c:out value="${lineKey}"/>', '<c:out value="${form.items[lineKey].reportedStartOfYearAmount}"/>', '<c:out value="${form.items[lineKey].reportedEndOfYearAmount}"/>');" />
                  </c:if>
                </div>
                <script type="text/javascript">
                  //<![CDATA[
                  Farm.<c:out value="${deleteAdjustName}"/>TT =
                    new YAHOO.widget.Tooltip("<c:out value="${deleteAdjustName}"/>TT",
                          { context:"<c:out value="${deleteAdjustName}"/>", text:"Click here to delete the adjustment.", autodismissdelay: 7500 });
                  //]]>
                </script>
              </c:if>
            </td>
          </tr>
        </c:if>

      </logic-el:iterate>
    </table>
  </div>


  <div id="cra_<c:out value="${itemType}_${eligibilityFilterValue}"/>" style="display:none;">
    <table class="numeric"  style="width:100%">
      <tr>
        <th><fmt:message key="Code"/></th>
        <th><fmt:message key="Description"/></th>
        <th width="100"><fmt:message key="Start.Value"/></th>
        <th width="100"><fmt:message key="End.Value"/></th>
      </tr>

      <logic-el:iterate name="form" property="lineKeys" id="lineKey">
        <c:if test="${form.items[lineKey].itemType eq itemType and form.items[lineKey].eligible eq eligibilityFilterValue and ! form.items[lineKey].new}">
          <tr>
            <th><c:out value="${form.items[lineKey].lineCode}"/></th>
            <th><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
            <td width="100"><c:out value="${form.items[lineKey].reportedStartOfYearAmount}"/></td>
            <td width="100"><c:out value="${form.items[lineKey].reportedEndOfYearAmount}"/></td>
          </tr>
        </c:if>

      </logic-el:iterate>
    </table>
  </div>

</logic-el:iterate>
</logic-el:iterate>

<a id="outputCsv" href="#"><fmt:message key="Export"/></a>

</html:form>

<div align="right" style="margin-top:10px">
  <c:if test="${ ! form.readOnly }">
    <a id="saveButton" href="#"><fmt:message key="Save"/></a>
  </c:if>
</div>

<script type="text/javascript">
  //<![CDATA[
    
  $( "#dialog" ).dialog({
    modal: true,
    autoOpen: false,
    width: "800",
    open: function(){
      $('.ui-widget-overlay').bind('click',function(){
          $('#dialog').dialog('close');
      })
    }
  });
  
  $( "#import-dialog-button" ).click(function( event ) {
    if(isFormDirty(document.getElementById("accrualsForm"))) {
      alert("Save changes before importing.")
    } else {
      $( "#dialog" ).dialog( "open" );
      event.preventDefault();
    }
  });

  function displayFilterResults() {
    var type, eligibility, view;
    if(document.getElementById('inputsRadio').checked == true) type='class3';
    if(document.getElementById('receivablesRadio').checked == true) type='class4';
    if(document.getElementById('payablesRadio').checked == true) type='class5';

    if(document.getElementById('eligibleRadio').checked == true) eligibility='true';
    if(document.getElementById('ineligibleRadio').checked == true) eligibility='false';

    if(document.getElementById('adjustedRadio').checked == true) view='total';
    if(document.getElementById('adjustmentsRadio').checked == true) view='adjust';
    if(document.getElementById('craRadio').checked == true) view='cra';

    var tableId = view + '_' + type + '_' + eligibility;

    <logic-el:iterate name="form" property="itemTypes" id="itemType">
      <logic-el:iterate name="form" property="eligibilityFilterValues" id="eligibilityFilterValue">
        document.getElementById('total_<c:out value="${itemType}_${eligibilityFilterValue}"/>').style.display = 'none';
        document.getElementById('adjust_<c:out value="${itemType}_${eligibilityFilterValue}"/>').style.display = 'none';
        document.getElementById('cra_<c:out value="${itemType}_${eligibilityFilterValue}"/>').style.display = 'none';
      </logic-el:iterate>
    </logic-el:iterate>

    document.getElementById(tableId).style.display = '';
  }

  displayFilterResults();

  new YAHOO.widget.Button("saveButton");
  function submitFunc() { submitForm(document.getElementById('accrualsForm')); }
  YAHOO.util.Event.addListener(document.getElementById("saveButton"), "click", submitFunc);

  function addNew() {
    showProcessing();
    disableDirtyFormCheck();
    var form = document.getElementById('accrualsForm');
    form.action = '<html:rewrite action="addNewAccruals"/>';
    form.submit();
  }

  function outputCsv() {
	  var itemTypeRadio = $('input:radio[name=itemTypeRadio]:checked').val();
	  var financialView = $('input:radio[name=financialViewRadio]:checked').val();
	
	  document.location.href = "outputAccruals.do?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>" + "&farmViewYear=<c:out value="${form.farmViewYear}"/>" + "&farmView=<c:out value="${form.farmView}"/>" + "&itemTypeRadio=" + itemTypeRadio + "&financialViewRadio=" +  financialView;
  }

  <logic-el:iterate name="form" property="itemTypes" id="itemType">
    <logic-el:iterate name="form" property="eligibilityFilterValues" id="eligibilityFilterValue">
      <c:set var="addNewButtonId" value="addNewButton_${itemType}_${eligibilityFilterValue}"/>
      new YAHOO.widget.Button("<c:out value="${addNewButtonId}"/>");
      YAHOO.util.Event.addListener(document.getElementById("<c:out value="${addNewButtonId}"/>"), "click", addNew);
    </logic-el:iterate>
  </logic-el:iterate>

  new YAHOO.widget.Button("outputCsv");
  YAHOO.util.Event.addListener(document.getElementById("outputCsv"), "click", outputCsv);


  <c:if test="${ ! form.readOnly }">
    registerFormForDirtyCheck(document.getElementById("accrualsForm"));
    excludeFieldFromDirtyCheck('itemTypeRadio');
    excludeFieldFromDirtyCheck('eligibilityRadio');
    excludeFieldFromDirtyCheck('financialViewRadio');
  
    <c:if test="${form.addedNew}">
      markFormAsDirty();
    </c:if>
    
    new YAHOO.widget.Button("import-dialog-button");

    function selectScenarioOperation(scenarioNumber, operationSchedule) {
      $('#dialog').dialog('close');
      disableDirtyFormCheck();
      var form = document.getElementById('accrualsForm');
      form.importScenarioNumber.value = scenarioNumber;
      form.importOperationSchedule.value = operationSchedule;
      form.action = '<html:rewrite action="importAccruals"/>';
      form.submit();
    }
    
    <c:forEach var="item" items="${form.operationsForImport}">  
      new YAHOO.widget.Button("selectButton<c:out value="${item.scenarioNumber}"/>");
    </c:forEach>
  </c:if>

  $(document).ready(function() {
    var farmView = '<c:out value="${ form.farmView }"/>';
    if(farmView == 'WHOLE') {
      $('#accrualsForm').find('input[type=text]').change(function() {
        alert('<fmt:message key="editing.in.whole.farm.view.warning"/>');
      });
    }
  });

  //]]>
</script>
