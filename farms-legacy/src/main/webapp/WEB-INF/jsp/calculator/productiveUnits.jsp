<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="yui/2.8.2r1/build/animation/animation-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/autocomplete/autocomplete-min.js"></script>

<logic:messagesPresent name="productiveUnitMessages">
  <div class="messages">
    <ul>
      <html:messages id="message" name="productiveUnitMessages">
        <li><c:out value="${message}"/></li>
      </html:messages>
    </ul>
  </div>
</logic:messagesPresent>

<c:if test="${form.addedNew}">
  <script type="text/javascript" src="<html:rewrite action="productiveUnitLists"/>"></script>

  <script type="text/javascript">
    //<![CDATA[

    function getItemSelectHandler(searchInputField, lineKey, lineCodeField, lineCodeDescriptionField, typeField) {
      return function() {
        // Use a LocalDataSource
        var oDS = new YAHOO.util.LocalDataSource(YAHOO.farm.Data.pucs);
        oDS.responseSchema = {fields : ["n", "i", "t"]};

        // Instantiate the AutoComplete
        var oAC = new YAHOO.widget.AutoComplete(searchInputField, "itemContainer." + lineKey, oDS);
        oAC.resultTypeList = false;
        oAC.forceSelection = true;

        // Define an event handler to populate a hidden form field
        // when an item gets selected
        var lineCodeHiddenField = YAHOO.util.Dom.get(lineCodeField);
        var lineCodeDescriptionHiddenField = YAHOO.util.Dom.get(lineCodeDescriptionField);
        var typeHiddenField = YAHOO.util.Dom.get(typeField);
        var selectionHandler = function(sType, aArgs) {
            var myAC = aArgs[0]; // reference back to the AC instance
            var elLI = aArgs[1]; // reference to the selected LI element
            var oData = aArgs[2]; // object literal of selected item's result data

            // update hidden form field with the selected item's ID
            lineCodeHiddenField.value = oData.i;
            lineCodeDescriptionHiddenField.value = oData.n;
            typeHiddenField.value = oData.t;
        };
        oAC.itemSelectEvent.subscribe(selectionHandler);

        var clearHandler = function(sType, aArgs) {
            var myAC = aArgs[0]; // reference back to the AC instance
            var clearedValue = aArgs[1]; // The cleared value that did not match anything

            // update hidden form field with the selected item's ID
            lineCodeHiddenField.value = "";
            lineCodeDescriptionHiddenField.value = "";
            typeHiddenField.value = "";
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

  function deleteAdjustment(deleteAdjToolTip, adjustedTT, lineKey, participantDataSrcCode, year, reportedValue) {
    var deleteAdjustName = "deleteAdjust_" + lineKey + "_" + participantDataSrcCode + "_" + year;
    deleteAdjToolTip.cfg.setProperty("disabled", "true");
    if(adjustedTT) {
      adjustedTT.cfg.setProperty("disabled", "true");
    }
    var adjustedFieldName = "item_" + lineKey + "_" + participantDataSrcCode + "_adjusted_" + year;
    var adjustedField = document.getElementById(adjustedFieldName);
    adjustedField.style.backgroundColor = "white";
    adjustedField.value = reportedValue;
    document.getElementById(deleteAdjustName + "Container").style.display = 'none';
    document.getElementById("item_" + lineKey + "_" + participantDataSrcCode + "_deletedAdjustment_" + year).value = 'true';
    markFormAsDirty();
  }
  
  //]]>
</script>

<div id="updateDataSetUsedDialog" class="yui3-skin-sam"  title="Data Set Used" style="display:none;">
<p>Changes you made may not be saved.</p>
</div>
<div id="dialog" class="yui3-skin-sam"  title="Import Productive Units from Operation" style="display:none;">

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
			
			<tr class="<c:out value="${item.participantDataSrcCodesSpaceDelimitedString}"/>">
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

<div class="instructions">
  <p class="instructions">
    <fmt:message key="text.productive.units.rollup.message"/>
  </p>
</div>

<html:form action="saveProductiveUnits" styleId="productiveUnitsForm" method="post" onsubmit="showProcessing()">
  <html:hidden property="pin"/>
  <html:hidden property="year"/>
  <html:hidden property="scenarioNumber"/>
  <html:hidden property="importScenarioNumber"/>
  <html:hidden property="importOperationSchedule"/>
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
                <u:menuSelect action="farm890.do"
                    name="farmView"
                    paramName="farmView"
                    additionalFieldIds="adjustedRadio,adjustmentsRadio,craRadio,rollupRadio,onFarmRadio,unseedableRadio,dataSetUsedCraRadio,dataSetUsedLocalSupRadio,viewDataSetCraRadio,viewDataSetLocalSupRadio"
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
        <legend><fmt:message key="Financial.View"/></legend>
          <table>
          <tr>
            <td><html-el:radio property="financialViewRadio" styleId="adjustedRadio" value="${form.financialViewAdjusted}" onclick="javascript:clickFinancialView();" disabled="${scenario.scenarioTypeCode eq 'CRA' or scenario.scenarioTypeCode eq 'LOCAL' or scenario.scenarioTypeCode eq 'GEN'  or scenario.scenarioTypeCode eq 'CHEF' or form.unitsViewRadio ne form.unitsViewRollup}" /><fmt:message key="Adjusted"/></td>
            <td><html-el:radio property="financialViewRadio" styleId="adjustmentsRadio" value="${form.financialViewAdjustments}" onclick="javascript:clickFinancialView();" disabled="${scenario.scenarioTypeCode eq 'CRA' or scenario.scenarioTypeCode eq 'LOCAL' or scenario.scenarioTypeCode eq 'GEN'  or scenario.scenarioTypeCode eq 'CHEF' or form.unitsViewRadio ne form.unitsViewRollup}" /><fmt:message key="Adjustments"/></td>
            <td><html-el:radio property="financialViewRadio" styleId="craRadio" value="${form.financialViewCra}" onclick="javascript:clickFinancialView();" disabled="${scenario.scenarioTypeCode eq 'CRA' or scenario.scenarioTypeCode eq 'LOCAL' or scenario.scenarioTypeCode eq 'GEN'  or scenario.scenarioTypeCode eq 'CHEF' or form.unitsViewRadio ne form.unitsViewRollup}" /><fmt:message key="CRA"/></td>
          </tr>
        </table>
        </fieldset>
      </td>
      <td>
        <fieldset>
        <legend><fmt:message key="Units.View"/></legend>
          <table>
          <tr>
            <td><html-el:radio property="unitsViewRadio" styleId="rollupRadio" value="${form.unitsViewRollup}" onclick="javascript:clickUnitsView();" /><fmt:message key="Rollup"/></td>
            <td><html-el:radio property="unitsViewRadio" styleId="onFarmRadio" value="${form.unitsViewOnFarm}" onclick="javascript:clickUnitsView();" /><fmt:message key="On.Farm"/></td>
            <td><html-el:radio property="unitsViewRadio" styleId="unseedableRadio" value="${form.unitsViewUnseedable}" onclick="javascript:clickUnitsView();" /><fmt:message key="Unseedable"/></td>
          </tr>
        </table>
        </fieldset>
      </td>
      <td>
      </td>
    </tr>
    <tr>
      <td>
      </td>
      <td>
        <%-- Hide the Data Set radio buttons for Combined Farms when in Whole Farm View because --%>
        <%-- the selected participantDataSrcCode may be LOCAL for some PINs and CRA for others. --%>
        <c:if test="${not (scenario.inCombinedFarm and form.wholeFarmView)}">
          <fieldset>
          <legend><fmt:message key="Data.Set.Used"/></legend>
            <table>
              <tr>
                <td><html-el:radio property="dataSetUsedRadio" styleId="dataSetUsedCraRadio" value="${form.dataSetCra}" disabled="${form.readOnly}" onclick="clickDataSetUsed(event)"/><fmt:message key="CRA"/></td>
                <%-- <td><html-el:radio property="dataSetUsedRadio" styleId="dataSetUsedProdInsurRadio" value="${form.dataSetCra}" disabled="${form.readOnly}" onclick="clickDataSetUsed()"/><fmt:message key="Production.Insurance"/></td> --%>
                <td><html-el:radio property="dataSetUsedRadio" styleId="dataSetUsedLocalSupRadio" value="${form.dataSetLocalSupplemental}" disabled="${form.readOnly}" onclick="clickDataSetUsed(event)" /><fmt:message key="Local.Supplemental"/></td>
              </tr>
            </table>
          </fieldset>
        </c:if>
      </td>
      <td>
        <c:if test="${not (scenario.inCombinedFarm and form.wholeFarmView)}">
          <fieldset>
          <legend><fmt:message key="View.Data.Set"/></legend>
             <table>
              <tr>
                <td><html-el:radio property="viewDataSetRadio" styleId="viewDataSetCraRadio" value="${form.viewDataCra}" onclick="clickViewDataSet(this.value)"/><fmt:message key="CRA"/></td>
                <%-- <td><html-el:radio property="viewDataSetRadio" styleId="viewDataSetProdInsurRadio" value="${form.viewDataproductionInsurance}"  onclick="clickViewDataSet(event)"/><fmt:message key="Production.Insurance"/></td> --%>
                <td><html-el:radio property="viewDataSetRadio" styleId="viewDataSetLocalSupRadio" value="${form.viewDataLocalSupplemental}"  onclick="clickViewDataSet(this.value)"/><fmt:message key="Local.Supplemental"/></td>
              </tr>
            </table>
          </fieldset>
        </c:if>
      </td>
      <td>
        <span id="import-dialog-button" class="yui-button yui-push-button" style="display:none;"> 
          <em class="first-child"> 
            <button type="button" name="import-dialog-button" ><fmt:message key="Import"/></button> 
          </em> 
        </span> 
      </td>
    </tr>
  </table>

  <c:set var="autoCompleteZIndex" value="${10000}"/>

  <div id="rolledUpTotal" style="display:none;">
    <c:if test="${scenario.productiveUnitsRollupEnabled == true}">
      <table class="numeric" style="width:100%">
        <tr>
          <th><fmt:message key="Code"/></th>
          <th><fmt:message key="Description"/></th>
          <logic-el:iterate name="form" property="requiredYears" id="year">
            <th width="80"><c:out value="${year}"/></th>
          </logic-el:iterate>
        </tr>

        <logic-el:iterate name="form" property="rolledUpLineKeys" id="rolledUpLineKey">
          <tr>
            <th class="row"><c:out value="${form.rolledUpItems[rolledUpLineKey].lineCode}"/></th>
            <th class="row"><c:out value="${form.rolledUpItems[rolledUpLineKey].lineCodeDescription}"/></th>
            <logic-el:iterate name="form" property="requiredYears" id="year">
              <c:forEach var="participantDataSrcCode" items="${form.participantDataSrcCodesByYear[year]}">
                <td width="80" class="<c:if test="${year eq form.year}"><c:out value="${participantDataSrcCode}"/></c:if>">
                  <c:out value="${form.rolledUpItems[rolledUpLineKey].records[participantDataSrcCode].adjustedValues[year]}"/>
                </td>
              </c:forEach>
            </logic-el:iterate>
          </tr>
        </logic-el:iterate>

        <tr>
          <td colspan="8" class="rowSpacer"><img src="images/spacer.gif" width="1" height="1" alt="" /></td>
        </tr>
      </table>
    </c:if>
  </div>

  <div id="total" style="display:none;">
    <table class="numeric" style="width:100%">
        <tr>
            <th><fmt:message key="Code"/></th>
            <th><fmt:message key="Description"/></th>
          <logic-el:iterate name="form" property="requiredYears" id="year">
            <th width="80"><c:out value="${year}"/></th>
          </logic-el:iterate>
        </tr>

      <logic-el:iterate name="form" property="lineKeys" id="lineKey">
        <c:set var="autoCompleteZIndex" value="${autoCompleteZIndex - 1}"/>
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
            <c:set var="typeFieldName" value="item(${lineKey}).type"/>
            <c:set var="typeFieldId" value="item_${lineKey}_type"/>
            <c:set var="isNewFieldName" value="item(${lineKey}).new"/>
            <c:set var="isNewFieldId" value="item_${lineKey}_new"/>
            <input type="hidden" name="<c:out value="${lineKeyFieldName}"/>" id="<c:out value="${lineKeyFieldId}"/>" value="<c:out value="${lineKey}"/>" />
            <input type="hidden" name="<c:out value="${lineCodeFieldName}"/>" id="<c:out value="${lineCodeFieldId}"/>" value="<c:out value="${form.items[lineKey].lineCode}"/>" />
            <input type="hidden" name="<c:out value="${lineCodeDescriptionFieldName}"/>" id="<c:out value="${lineCodeDescriptionFieldId}"/>" value="<c:out value="${form.items[lineKey].lineCodeDescription}"/>" />
            <input type="hidden" name="<c:out value="${typeFieldName}"/>" id="<c:out value="${typeFieldId}"/>" value="<c:out value="${form.items[lineKey].type}"/>" />
            <input type="hidden" name="<c:out value="${isNewFieldName}"/>" id="<c:out value="${isNewFieldId}"/>" value="<c:out value="${form.items[lineKey].new}"/>" />
            <c:choose>
              <c:when test="${form.items[lineKey].new == true}">
                <c:set var="addedNew" value="true"/>
                <c:set var="searchInputFieldName" value="item(${lineKey}).searchInput"/>
                <c:set var="searchInputFieldId" value="item_${lineKey}_searchInput"/>
                <div id="autoCompleteContainer.<c:out value="${lineKey}"/>" style="z-index: <c:out value="${autoCompleteZIndex}"/>;position: relative;">
                  <input type="text" name="<c:out value="${searchInputFieldName}"/>" id="<c:out value="${searchInputFieldId}"/>" value="<c:out value="${form.items[lineKey].searchInput}"/>" style="width:98%" />
                  <div id="itemContainer.<c:out value="${lineKey}"/>" style="position: absolute;"></div>
                </div>
                <script type="text/javascript">
                  //<![CDATA[
                  YAHOO.farm.ItemSelectHandler = getItemSelectHandler(<c:out value="'${searchInputFieldId}','${lineKey}','${lineCodeFieldId}','${lineCodeDescriptionFieldId}','${typeFieldId}'" escapeXml="false"/>);
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
            <c:forEach var="participantDataSrcCode" items="${form.participantDataSrcCodesByYear[year]}">
              <td width="80" class="<c:if test="${year eq form.year}"><c:out value="${participantDataSrcCode}"/></c:if>">
                <c:set var="adjustInputName" value="adjustInput_${lineKey}_${participantDataSrcCode}_${year}"/>
                <c:choose>
                  <c:when test="${form.items[lineKey].errors[year]}">
                    <c:set var="inputClass" value="adjustmentExistsError"/>
                  </c:when>
                  <c:when test="${!empty form.items[lineKey].records[participantDataSrcCode].adjustmentValues[year] && !form.items[lineKey].records[participantDataSrcCode].deletedAdjustments[year]}">
                    <c:set var="inputClass" value="adjustmentExists"/>
                    <script type="text/javascript">
                      //<![CDATA[
                      Farm.<c:out value="${adjustInputName}"/>TT =
                        new YAHOO.widget.Tooltip("<c:out value="${adjustInputName}"/>TT",
                            { context:"<c:out value="${adjustInputName}"/>",
                              text:"<div style='text-align:left'><b>Original Value</b>: <c:out value="${form.items[lineKey].records[participantDataSrcCode].craValues[year]}"/><br />"
                              <c:if test="${!empty form.items[lineKey].records[participantDataSrcCode].adjustmentUsers[year]}">
                                + "<b>Adjusted By</b>: <c:out value="${form.items[lineKey].records[participantDataSrcCode].adjustmentUsers[year]}"/>"
                              </c:if>
                              + "</div>",
                              autodismissdelay: 7500 });
                      //]]>
                    </script>
                  </c:when>
                  <c:otherwise>
                    <c:set var="inputClass" value=""/>
                  </c:otherwise>
                </c:choose>
                <div id="<c:out value="${adjustInputName}"/>">
                  <c:if test="${ form.wholeFarmView or ! empty form.yearOperationMap[year] }">
                    <c:set var="isDeletedFieldName" value="item(${lineKey}).record(${participantDataSrcCode}).deletedAdjustment(${year})"/>
                    <c:set var="isDeletedFieldId" value="item_${lineKey}_${participantDataSrcCode}_deletedAdjustment_${year}"/>
                    <input type="hidden" name="<c:out value="${isDeletedFieldName}"/>" id="<c:out value="${isDeletedFieldId}"/>" value="<c:out value="${form.items[lineKey].records[participantDataSrcCode].deletedAdjustments[year]}"/>" />
                    <html-el:text property="item(${lineKey}).record(${participantDataSrcCode}).adjusted(${year})" styleId="item_${lineKey}_${participantDataSrcCode}_adjusted_${year}" styleClass="${inputClass}" size="10" onclick="selectAll(this)" />
                  </c:if>
                </div>
              </td>
            </c:forEach>
          </logic-el:iterate>
        </tr>
      </logic-el:iterate>

      <c:if test="${ ! addedNew and ! form.readOnly}">
        <tr>
          <th class="row">&nbsp;</th>
          <th class="row">&nbsp;</th>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td><a id="addNewButton" href="#"><fmt:message key="Add.New"/></a></td>
        </tr>
      </c:if>

      <tr>
        <td colspan="8" class="rowSpacer"><img src="images/spacer.gif" width="1" height="1" alt="" /></td>
      </tr>
    </table>
  </div>

  <div id="rolledUpAdjust" style="display:none;">
    <c:if test="${scenario.productiveUnitsRollupEnabled == true}">
      <table class="numeric" style="width:100%">
        <tr>
          <th><fmt:message key="Code"/></th>
          <th><fmt:message key="Description"/></th>
          <logic-el:iterate name="form" property="requiredYears" id="year">
            <th width="80"><c:out value="${year}"/></th>
          </logic-el:iterate>
        </tr>

        <logic-el:iterate name="form" property="rolledUpLineKeys" id="rolledUpLineKey">
          <tr>
            <th class="row"><c:out value="${form.rolledUpItems[rolledUpLineKey].lineCode}"/></th>
            <th class="row"><c:out value="${form.rolledUpItems[rolledUpLineKey].lineCodeDescription}"/></th>
            <logic-el:iterate name="form" property="requiredYears" id="year">
              <c:forEach var="participantDataSrcCode" items="${form.participantDataSrcCodesByYear[year]}">
                <td width="80" class="<c:if test="${year eq form.year}"><c:out value="${participantDataSrcCode}"/></c:if>">
                  <c:out value="${form.rolledUpItems[rolledUpLineKey].records[participantDataSrcCode].adjustmentValues[year]}"/>
                </td>
              </c:forEach>
            </logic-el:iterate>
          </tr>
        </logic-el:iterate>

        <tr>
          <td colspan="8" class="rowSpacer"><img src="images/spacer.gif" width="1" height="1" alt="" /></td>
        </tr>
      </table>
    </c:if>
  </div>

  <div id="adjust" style="display:none;">
    <table class="numeric" style="width:100%">
      <tr>
          <th><fmt:message key="Code"/></th>
          <th><fmt:message key="Description"/></th>
        <logic-el:iterate name="form" property="requiredYears" id="year">
          <th width="80"><c:out value="${year}"/></th>
        </logic-el:iterate>
      </tr>

      <logic-el:iterate name="form" property="lineKeys" id="lineKey">
        <c:if test="${form.items[lineKey].new == false}">
          <tr>
            <th class="row"><c:out value="${form.items[lineKey].lineCode}"/></th>
            <th class="row"><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
            <logic-el:iterate name="form" property="requiredYears" id="year">
              <c:forEach var="participantDataSrcCode" items="${form.participantDataSrcCodesByYear[year]}">
                <td width="80" class="<c:if test="${year eq form.year}"><c:out value="${participantDataSrcCode}"/></c:if>">
                  <c:if test="${!empty form.items[lineKey].records[participantDataSrcCode].adjustmentValues[year] && !form.items[lineKey].records[participantDataSrcCode].deletedAdjustments[year]}">
                    <c:set var="adjustInputName" value="adjustInput_${lineKey}_${participantDataSrcCode}_${year}"/>
                    <c:set var="deleteAdjustName" value="deleteAdjust_${lineKey}_${participantDataSrcCode}_${year}"/>
                    <div id="<c:out value="${deleteAdjustName}"/>Container">
                      <c:out value="${form.items[lineKey].records[participantDataSrcCode].adjustmentValues[year]}"/>
                      <c:if test="${ ! form.readOnly }">
                        <img id="<c:out value="${deleteAdjustName}"/>" src="images/error.png" alt="Delete Adjustment" width="16" height="16" align="middle" onclick="deleteAdjustment(Farm.<c:out value="${deleteAdjustName}"/>TT, Farm.<c:out value="${adjustInputName}"/>TT, '<c:out value="${lineKey}"/>', '<c:out value="${participantDataSrcCode}"/>', '<c:out value="${year}"/>', '<c:out value="${form.items[lineKey].records[participantDataSrcCode].craValues[year]}"/>')" />
                      </c:if>
                    </div>
                    <script type="text/javascript">
                      //<![CDATA[
                      Farm.<c:out value="${deleteAdjustName}"/>TT = new YAHOO.widget.Tooltip("<c:out value="${deleteAdjustName}"/>TT",
                        { context:"<c:out value="${deleteAdjustName}"/>", text:"Click here to delete the adjustment.", autodismissdelay: 7500 });
                      //]]>
                    </script>
                  </c:if>
                </td>
              </c:forEach>
            </logic-el:iterate>
          </tr>
        </c:if>
      </logic-el:iterate>

      <tr>
        <td colspan="8" class="rowSpacer"><img src="images/spacer.gif" alt="" width="1" height="1" /></td>
      </tr>
    </table>
  </div>

  <div id="rolledUpCra" style="display:none;">
    <c:if test="${scenario.productiveUnitsRollupEnabled == true}">
      <table class="numeric" style="width:100%">
        <tr>
          <th><fmt:message key="Code"/></th>
          <th><fmt:message key="Description"/></th>
          <logic-el:iterate name="form" property="requiredYears" id="year">
            <th width="80"><c:out value="${year}"/></th>
          </logic-el:iterate>
        </tr>

        <logic-el:iterate name="form" property="rolledUpLineKeys" id="rolledUpLineKey">
          <tr>
            <th class="row"><c:out value="${form.rolledUpItems[rolledUpLineKey].lineCode}"/></th>
            <th class="row"><c:out value="${form.rolledUpItems[rolledUpLineKey].lineCodeDescription}"/></th>
            <logic-el:iterate name="form" property="requiredYears" id="year">
              <c:forEach var="participantDataSrcCode" items="${form.participantDataSrcCodesByYear[year]}">
                <td width="80" class="<c:if test="${year eq form.year}"><c:out value="${participantDataSrcCode}"/></c:if>">
                  <c:out value="${form.rolledUpItems[rolledUpLineKey].records[participantDataSrcCode].craValues[year]}"/>
                </td>
              </c:forEach>
            </logic-el:iterate>
          </tr>
        </logic-el:iterate>

        <tr>
          <td colspan="8" class="rowSpacer"><img src="images/spacer.gif" alt="" width="1" height="1" /></td>
        </tr>
      </table>
    </c:if>
  </div>

  <div id="cra" style="display:none;">
    <table class="numeric" style="width:100%">
      <tr>
          <th><fmt:message key="Code"/></th>
          <th><fmt:message key="Description"/></th>
        <logic-el:iterate name="form" property="requiredYears" id="year">
          <th width="80"><c:out value="${year}"/></th>
        </logic-el:iterate>
      </tr>

      <logic-el:iterate name="form" property="lineKeys" id="lineKey">
        <c:if test="${form.items[lineKey].new == false}">
          <tr>
            <th class="row"><c:out value="${form.items[lineKey].lineCode}"/></th>
            <th class="row"><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
            <logic-el:iterate name="form" property="requiredYears" id="year">
              <c:forEach var="participantDataSrcCode" items="${form.participantDataSrcCodesByYear[year]}">
                <td width="80" class="<c:if test="${year eq form.year}"><c:out value="${participantDataSrcCode}"/></c:if>">
                  <c:out value="${form.items[lineKey].records[participantDataSrcCode].craValues[year]}"/>
                </td>
              </c:forEach>
            </logic-el:iterate>
          </tr>
        </c:if>
      </logic-el:iterate>

      <tr>
        <td colspan="8" class="rowSpacer"><img src="images/spacer.gif" alt="" width="1" height="1" /></td>
      </tr>
    </table>
  </div>

  <div id="rolledUpOnFarm" style="display:none;">
    <c:if test="${scenario.productiveUnitsRollupEnabled == true}">
      <table class="numeric" style="width:100%">
        <tr>
          <th><fmt:message key="Code"/></th>
          <th><fmt:message key="Description"/></th>
          <logic-el:iterate name="form" property="requiredYears" id="year">
            <th width="80"><c:out value="${year}"/></th>
          </logic-el:iterate>
        </tr>

        <logic-el:iterate name="form" property="rolledUpLineKeys" id="rolledUpLineKey">
          <tr>
            <th class="row"><c:out value="${form.rolledUpItems[rolledUpLineKey].lineCode}"/></th>
            <th class="row"><c:out value="${form.rolledUpItems[rolledUpLineKey].lineCodeDescription}"/></th>
            <logic-el:iterate name="form" property="requiredYears" id="year">
              <td width="80">
                <c:out value="${form.rolledUpItems[rolledUpLineKey].onFarmAcresValues[year]}"/>
              </td>
            </logic-el:iterate>
          </tr>
        </logic-el:iterate>

        <tr>
          <td colspan="8" class="rowSpacer"><img src="images/spacer.gif" alt="" width="1" height="1" /></td>
        </tr>
      </table>
    </c:if>
  </div>

  <div id="onFarm" style="display:none;">
    <table class="numeric" style="width:100%">
      <tr>
          <th><fmt:message key="Code"/></th>
          <th><fmt:message key="Description"/></th>
        <logic-el:iterate name="form" property="requiredYears" id="year">
          <th width="80"><c:out value="${year}"/></th>
        </logic-el:iterate>
      </tr>

      <logic-el:iterate name="form" property="lineKeys" id="lineKey">
        <c:if test="${form.items[lineKey].new == false}">
          <tr>
            <th class="row"><c:out value="${form.items[lineKey].lineCode}"/></th>
            <th class="row"><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
            <logic-el:iterate name="form" property="requiredYears" id="year">
              <td width="80">
                <c:out value="${form.items[lineKey].onFarmAcresValues[year]}"/>
              </td>
            </logic-el:iterate>
          </tr>
        </c:if>
      </logic-el:iterate>

      <tr>
        <td colspan="8" class="rowSpacer"><img src="images/spacer.gif" alt="" width="1" height="1" /></td>
      </tr>
    </table>
  </div>

  <div id="rolledUpUnseedable" style="display:none;">
    <c:if test="${scenario.productiveUnitsRollupEnabled == true}">
      <table class="numeric" style="width:100%">
        <tr>
          <th><fmt:message key="Code"/></th>
          <th><fmt:message key="Description"/></th>
          <logic-el:iterate name="form" property="requiredYears" id="year">
            <th width="80"><c:out value="${year}"/></th>
          </logic-el:iterate>
        </tr>

        <logic-el:iterate name="form" property="rolledUpLineKeys" id="rolledUpLineKey">
          <tr>
            <th class="row"><c:out value="${form.rolledUpItems[rolledUpLineKey].lineCode}"/></th>
            <th class="row"><c:out value="${form.rolledUpItems[rolledUpLineKey].lineCodeDescription}"/></th>
            <logic-el:iterate name="form" property="requiredYears" id="year">
              <td width="80">
                <c:out value="${form.rolledUpItems[rolledUpLineKey].unseedableAcresValues[year]}"/>
              </td>
            </logic-el:iterate>
          </tr>
        </logic-el:iterate>

        <tr>
          <td colspan="8" class="rowSpacer"><img src="images/spacer.gif" alt="" width="1" height="1" /></td>
        </tr>
      </table>
    </c:if>
  </div>

  <div id="unseedable" style="display:none;">
    <table class="numeric" style="width:100%">
      <tr>
          <th><fmt:message key="Code"/></th>
          <th><fmt:message key="Description"/></th>
        <logic-el:iterate name="form" property="requiredYears" id="year">
          <th width="80"><c:out value="${year}"/></th>
        </logic-el:iterate>
      </tr>

      <logic-el:iterate name="form" property="lineKeys" id="lineKey">
        <c:if test="${form.items[lineKey].new == false}">
          <tr>
            <th class="row"><c:out value="${form.items[lineKey].lineCode}"/></th>
            <th class="row"><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
            <logic-el:iterate name="form" property="requiredYears" id="year">
              <td width="80">
                <c:out value="${form.items[lineKey].unseedableAcresValues[year]}"/>
              </td>
            </logic-el:iterate>
          </tr>
        </c:if>
      </logic-el:iterate>

      <tr>
        <td colspan="8" class="rowSpacer"><img src="images/spacer.gif" alt="" width="1" height="1" /></td>
      </tr>
    </table>
  </div>

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
	  if(isFormDirty(document.getElementById("productiveUnitsForm"))) {
	    alert("Save changes before importing.")
	  } else {
  	  $( "#dialog" ).dialog( "open" );
  	  event.preventDefault();
	  }
	});

  var financialViewLastSelected = $('input:radio[name=financialViewRadio]:checked').val();
  
  function clickFinancialView() {
    financialViewLastSelected = $('input:radio[name=financialViewRadio]:checked').val();
    displayFilterResults();
  }
  
  function clickDataSetUsed(event) {
	  var dataSetUsed = $('input:radio[name=dataSetUsedRadio]:checked').val();
	  var form = document.getElementById('productiveUnitsForm');
	  showProcessing();
	  $.ajax({
	      url: "updateDataSetUsed.do?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>" + "&farmView=<c:out value="${form.farmView}"/>" + "&dataSetUsedRadio=" +  dataSetUsed, 
	      success: function(result){
	    	  undoShowProcessing();
	    	  console.log(result);
	    	  clickViewDataSet(result.participantDataSrcCode);
	    	  form.viewDataSetRadio = result.participantDataSrcCode;
	    	  form.scenarioRevisionCount = result.revisionCount;
	      }
	  });
  }

  function clickViewDataSet(value) {
    if (value == '<c:out value="${form.viewDataCra}"/>') {
      $('.<c:out value="${form.viewDataCra}"/>').show(); 
      $('.<c:out value="${form.viewDataLocalSupplemental}"/>').hide();
      $('#viewDataSetCraRadio').prop('checked', true);

      if('<c:out value="${form.craProductiveUnitsAvailableForImport}"/>' === 'true'
          && '<c:out value="${form.readOnly}"/>' === 'false') {
        $('#import-dialog-button').show();
      } else {
        $('#import-dialog-button').hide();
      }
    }

    if (value == '<c:out value="${form.viewDataLocalSupplemental}"/>') {
      $('.<c:out value="${form.viewDataCra}"/>').hide(); 
      $('.<c:out value="${form.viewDataLocalSupplemental}"/>').show(); 
      $('#viewDataSetLocalSupRadio').prop('checked', true);

      if('<c:out value="${form.localProductiveUnitsAvailableForImport}"/>' === 'true'
        && '<c:out value="${form.readOnly}"/>' === 'false') {
        $('#import-dialog-button').show();
      } else {
        $('#import-dialog-button').hide();
      }
    }
  }
  
  function clickUnitsView() {
    var unitsView = $('input:radio[name=unitsViewRadio]:checked').val();
    var financialViewRadioId = '';
    
    if(unitsView == '<c:out value="${form.unitsViewRollup}"/>') {
      switch(financialViewLastSelected) {
        case '<c:out value="${form.financialViewAdjusted}"/>':
          financialViewRadioId = 'adjustedRadio';
          break;
        case '<c:out value="${form.financialViewAdjustments}"/>':
          financialViewRadioId = 'adjustmentsRadio';
          break;
        case '<c:out value="${form.financialViewCra}"/>':
          financialViewRadioId = 'craRadio';
          break;
      }

      // <c:if test="${scenario.scenarioTypeCode ne 'CRA' and scenario.scenarioTypeCode ne 'LOCAL' and scenario.scenarioTypeCode ne 'CHEF' and scenario.scenarioTypeCode ne 'GEN'}">
      $('#adjustedRadio').prop("disabled", false);
      $('#adjustmentsRadio').prop("disabled", false);
      $('#craRadio').prop("disabled", false);
      // </c:if>

    } else {
      financialViewRadioId = 'craRadio';

      $('#adjustedRadio').prop("disabled", true);
      $('#adjustmentsRadio').prop("disabled", true);
      $('#craRadio').prop("disabled", true);
    }
    
    $('#' + financialViewRadioId).prop("checked", true);
    
    displayFilterResults();
  }
  
  function displayFilterResults() {
    var financialView = $('input:radio[name=financialViewRadio]:checked').val();
    var unitsView = $('input:radio[name=unitsViewRadio]:checked').val();
    var rolledUpView = '';
    var view = '';
    
    if(unitsView == '<c:out value="${form.unitsViewRollup}"/>') {
      switch(financialView) {
        case '<c:out value="${form.financialViewAdjusted}"/>':
          rolledUpView = 'rolledUpTotal';
          view = 'total';
          break;
        case '<c:out value="${form.financialViewAdjustments}"/>':
          rolledUpView = 'rolledUpAdjust';
          view = 'adjust';
          break;
        case '<c:out value="${form.financialViewCra}"/>':
          rolledUpView = 'rolledUpCra';
          view = 'cra';
          break;
      }
      
    } else {

      switch(unitsView) {
        case '<c:out value="${form.unitsViewOnFarm}"/>':
          rolledUpView = 'rolledUpOnFarm';
          view = 'onFarm';
          break;
        case '<c:out value="${form.unitsViewUnseedable}"/>':
          rolledUpView = 'rolledUpUnseedable';
          view = 'unseedable';
          break;
      }

    }
    var rolledUpTableId = rolledUpView;
    var tableId = view;

    document.getElementById('rolledUpTotal').style.display = 'none';
    document.getElementById('rolledUpAdjust').style.display = 'none';
    document.getElementById('rolledUpCra').style.display = 'none';
    document.getElementById('rolledUpOnFarm').style.display = 'none';
    document.getElementById('rolledUpUnseedable').style.display = 'none';
    document.getElementById(rolledUpTableId).style.display = '';

    document.getElementById('total').style.display = 'none';
    document.getElementById('adjust').style.display = 'none';
    document.getElementById('cra').style.display = 'none';
    document.getElementById('onFarm').style.display = 'none';
    document.getElementById('unseedable').style.display = 'none';
    document.getElementById(tableId).style.display = '';
  }

  displayFilterResults();

  new YAHOO.widget.Button("saveButton");
  function submitFunc() { submitForm(document.getElementById('productiveUnitsForm')); }
  YAHOO.util.Event.addListener(document.getElementById("saveButton"), "click", submitFunc);

  new YAHOO.widget.Button("addNewButton");
  function addNew() {
    disableDirtyFormCheck();
    var form = document.getElementById('productiveUnitsForm');
    form.action = '<html:rewrite action="addNewProductiveUnits"/>';
    form.submit();
  }
  YAHOO.util.Event.addListener(document.getElementById("addNewButton"), "click", addNew);
  
  function outputCsv() {
    var financialView = $('input:radio[name=financialViewRadio]:checked').val();
    var unitsView = $('input:radio[name=unitsViewRadio]:checked').val();
    var dataSetView = $('input:radio[name=viewDataSetRadio]:checked').val();
    document.location.href = "outputProductiveUnits.do?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>" + "&farmView=<c:out value="${form.farmView}"/>" + "&financialViewRadio=" +  financialView + "&unitsViewRadio=" +  unitsView + "&viewDataSetRadio=" +  dataSetView;
  }

  new YAHOO.widget.Button("outputCsv");
  YAHOO.util.Event.addListener(document.getElementById("outputCsv"), "click", outputCsv);
  
  <c:if test="${ ! form.readOnly }">
    registerFormForDirtyCheck(document.getElementById("productiveUnitsForm"));
    excludeFieldFromDirtyCheck('financialViewRadio');
    excludeFieldFromDirtyCheck('unitsViewRadio');
    excludeFieldFromDirtyCheck('dataSetUsedRadio');
    excludeFieldFromDirtyCheck('viewDataSetRadio');
    new YAHOO.widget.Button("import-dialog-button");
  </c:if>

  <c:if test="${addedNew}">
    markFormAsDirty();
  </c:if>
  

  function selectScenarioOperation(scenarioNumber, operationSchedule) {
	  $('#dialog').dialog('close');
	  disableDirtyFormCheck();
	  var form = document.getElementById('productiveUnitsForm');
	  form.importScenarioNumber.value = scenarioNumber;
	  form.importOperationSchedule.value = operationSchedule;
	  form.action = '<html:rewrite action="importProductiveUnits"/>';
	  form.submit();
  }
  
  <c:forEach var="item" items="${form.operationsForImport}">  
    new YAHOO.widget.Button("selectButton<c:out value="${item.scenarioNumber}"/>");
  </c:forEach>

  $(document).ready(function() {
    var farmView = '<c:out value="${ form.farmView }"/>';
    if(farmView == 'WHOLE') {
    $('#productiveUnitsForm').find('input[type=text]').keyup(function() {
        alert('<fmt:message key="editing.in.whole.farm.view.warning"/>');
      });
    }
    clickViewDataSet('<c:out value="${form.viewDataSetRadio}"/>');
  });
  
  //]]>
</script>
