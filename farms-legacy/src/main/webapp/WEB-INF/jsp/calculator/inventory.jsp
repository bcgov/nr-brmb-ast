<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="yui/2.8.2r1/build/animation/animation-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/autocomplete/autocomplete-min.js"></script>

<logic:messagesPresent name="inventoryMessages">
  <div class="messages">
    <ul>
      <html:messages id="message" name="inventoryMessages">
        <li><c:out value="${message}"/></li>
      </html:messages>
    </ul>
  </div>
</logic:messagesPresent>

<c:if test="${form.addedNew}">
  <script type="text/javascript" src="<html:rewrite action="inventoryItemLists"/>?year=<c:out value="${form.farmViewYear}"/>"></script>

  <script type="text/javascript">
    //<![CDATA[

    function getItemSelectHandler(oDS, itemType, searchInputField, lineKey, lineCodeField, lineCodeDescriptionField, isMarketCommodityField) {
      return function() {
        // Use a LocalDataSource
        if(itemType == '<c:out value="${form.typeLivestock}"/>') {
          oDS.responseSchema = {fields : ["n", "i","m"]};
        } else {
          oDS.responseSchema = {fields : ["n", "i"]};
        }

        // Instantiate the AutoComplete
        var oAC = new YAHOO.widget.AutoComplete(searchInputField, "itemContainer." + lineKey, oDS);
        oAC.resultTypeList = false;
        oAC.forceSelection = true;

        // Define an event handler to populate a hidden form field
        // when an item gets selected
        var lineCodeHiddenField = YAHOO.util.Dom.get(lineCodeField);
        var lineCodeDescriptionHiddenField = YAHOO.util.Dom.get(lineCodeDescriptionField);
        var isMarketCommodityHiddenField = YAHOO.util.Dom.get(isMarketCommodityField);
        var selectionHandler = function(sType, aArgs) {
            var myAC = aArgs[0]; // reference back to the AC instance
            var elLI = aArgs[1]; // reference to the selected LI element
            var oData = aArgs[2]; // object literal of selected item's result data

            // update hidden form field with the selected item's ID
            lineCodeHiddenField.value = oData.i;
            lineCodeDescriptionHiddenField.value = oData.n;
            if(itemType == '<c:out value="${form.typeLivestock}"/>') {
              isMarketCommodityHiddenField.value = oData.m;
            }
        };
        oAC.itemSelectEvent.subscribe(selectionHandler);

        var clearHandler = function(sType, aArgs) {
            var myAC = aArgs[0]; // reference back to the AC instance
            var clearedValue = aArgs[1]; // The cleared value that did not match anything

            // update hidden form field with the selected item's ID
            lineCodeHiddenField.value = "";
            lineCodeDescriptionHiddenField.value = "";
            isMarketCommodityHiddenField.value = "";
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

  function deleteAdjustment(
      deleteAdjToolTip, ttQuantityProduced, ttQuantityStart, ttQuantityEnd, ttPriceStart, ttPriceEnd, itemType, lineKey,
      reportedPriceStartOutsideFmvVariance, reportedPriceEndOutsideFmvVariance, reportedQP, reportedQS, reportedQE, reportedPS, reportedPE
  ) {
    deleteAdjToolTip.cfg.setProperty("disabled", "true");
    if(itemType == "<c:out value="${form.typeCrop}"/>") {
      if(ttQuantityProduced) {
        ttQuantityProduced.cfg.setProperty("disabled", "true");
      }
      var adjustedQuantityProducedField = document.getElementById("item_" + lineKey + "_totalQuantityProduced");
      adjustedQuantityProducedField.style.backgroundColor = "white";
      adjustedQuantityProducedField.value = reportedQP;
      document.getElementById("deleteAdjust_" + lineKey + "QuantityProducedContainer").style.display = 'none';
    }

    if(ttQuantityStart) {
      ttQuantityStart.cfg.setProperty("disabled", "true");
    }
    if(ttQuantityEnd) {
      ttQuantityEnd.cfg.setProperty("disabled", "true");
    }

    var adjustedQuantityStartField = document.getElementById("item_" + lineKey + "_totalQuantityStart");
    var adjustedQuantityEndField = document.getElementById("item_" + lineKey + "_totalQuantityEnd");
    var adjustedPriceStartField = document.getElementById("item_" + lineKey + "_totalPriceStart");
    var adjustedPriceEndField = document.getElementById("item_" + lineKey + "_totalPriceEnd");

    adjustedQuantityStartField.className = "";
    adjustedQuantityEndField.className = "";
    adjustedPriceStartField.className = "";
    adjustedPriceEndField.className = "";
    
    if(reportedPriceStartOutsideFmvVariance != "true") {
      adjustedPriceStartField.className = "";
    }
    if(reportedPriceEndOutsideFmvVariance != "true") {
      adjustedPriceEndField.className = "";
    }

    adjustedQuantityStartField.value = reportedQS;
    adjustedQuantityEndField.value = reportedQE;
    adjustedPriceStartField.value = reportedPS;
    adjustedPriceEndField.value = reportedPE;

    document.getElementById("deleteAdjust_" + lineKey + "QuantityStartContainer").style.display = 'none';
    document.getElementById("deleteAdjust_" + lineKey + "QuantityEndContainer").style.display = 'none';
    document.getElementById("deleteAdjust_" + lineKey + "PriceStartContainer").style.display = 'none';
    document.getElementById("deleteAdjust_" + lineKey + "PriceEndContainer").style.display = 'none';

    document.getElementById("deleteAdjust_" + lineKey + "ButtonContainer").style.display = 'none';
    document.getElementById("item_" + lineKey + "_deleted").value = 'true';
    markFormAsDirty();
  }

  //]]>
</script>

<div id="dialog" class="yui3-skin-sam"  title="Import Inventory from Operation" style="display:none;">

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

<html:form action="saveInventory" styleId="inventoryForm" method="post" onsubmit="showProcessing()">
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
                    <u:menuSelect action="farm880.do"
                        name="farmViewYearPicker"
                        paramName="farmViewYear"
                        additionalFieldIds="cropsRadio,livestockRadio,adjustedRadio,adjustmentsRadio,craRadio"
                        options="${form.farmViewYearOptions}"
                        selectedValue="${form.farmViewYear}"
                        urlParams="pin=${form.pin}&year=${form.year}&scenarioNumber=${form.scenarioNumber}&farmView=${form.farmView}"
                        toolTip="Click here to change between years of the farm data."/>
                  </td>
                  <td>
                    <u:menuSelect action="farm880.do"
                        name="farmViewPicker"
                        paramName="farmView"
                        additionalFieldIds="cropsRadio,livestockRadio,adjustedRadio,adjustmentsRadio,craRadio"
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
                  <td><html:radio property="itemTypeRadio" styleId="cropsRadio" value="crop" onclick="javascript:displayFilterResults();"/><fmt:message key="Crop"/></td>
                  <td><html:radio property="itemTypeRadio" styleId="livestockRadio" value="livestock" onclick="javascript:displayFilterResults();"/><fmt:message key="Livestock"/></td>
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
          <td>
            <c:if test="${form.inventoryAvailableForImport and ! form.readOnly}">
              <span id="import-dialog-button" class="yui-button yui-push-button"> 
                <em class="first-child"> 
                  <button type="button" name="import-dialog-button" ><fmt:message key="Import"/></button> 
                </em> 
              </span> 
            </c:if>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>

<c:set var="autoCompleteZIndex" value="${10000}"/>

<logic-el:iterate name="form" property="itemTypes" id="itemType">

  <div id="total_<c:out value="${itemType}"/>" style="display:none;">
  <table class="numeric" style="width:950px">
    <tr>
      <th><fmt:message key="Code"/></th>
      <th><fmt:message key="Description"/></th>
      <c:if test="${itemType eq form.typeCrop}">
        <th><fmt:message key="Units"/></th>
        <th width="50"><fmt:message key="On.Farm.Acres"/></th>
        <th width="50"><fmt:message key="Unseedable.Acres"/></th>
        <th width="80"><fmt:message key="Qty.Produced"/></th>
      </c:if>
      <c:if test="${itemType eq form.typeLivestock}">
        <th><fmt:message key="Market.Commodity"/></th>
      </c:if>
      <th width="80"><fmt:message key="Qty.Start"/></th>
      <th width="80"><fmt:message key="Price.Start"/></th>
      <th width="80"><fmt:message key="Qty.End"/></th>
      <th width="80"><fmt:message key="Price.End"/></th>
      <th width="80"><fmt:message key="Change.in.Value"/></th>
    </tr>

    <c:choose>
      <c:when test="${itemType eq form.typeCrop}"><c:set var="dataSource" value="new YAHOO.util.LocalDataSource(YAHOO.farm.Data.cropItems)"/></c:when>
      <c:when test="${itemType eq form.typeLivestock}"><c:set var="dataSource" value="new YAHOO.util.LocalDataSource(YAHOO.farm.Data.livestockItems)"/></c:when>
    </c:choose>

    <logic-el:iterate name="form" property="lineKeys" id="lineKey">
      <c:set var="autoCompleteZIndex" value="${autoCompleteZIndex - 1}"/>
      <c:if test="${form.items[lineKey].itemType eq itemType}">
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
            <c:set var="isNewFieldName" value="item(${lineKey}).new"/>
            <c:set var="isNewFieldId" value="item_${lineKey}_new"/>
            <c:set var="isDeletedFieldName" value="item(${lineKey}).deleted)"/>
            <c:set var="isDeletedFieldId" value="item_${lineKey}_deleted"/>
            <c:set var="isMarketCommodityFieldName" value="item(${lineKey}).marketCommodity)"/>
            <c:set var="isMarketCommodityFieldId" value="item_${lineKey}_marketCommodity"/>
            <input type="hidden" name="<c:out value="${lineKeyFieldName}"/>" id="<c:out value="${lineKeyFieldId}"/>" value="<c:out value="${lineKey}"/>" />
            <input type="hidden" name="<c:out value="${lineCodeFieldName}"/>" id="<c:out value="${lineCodeFieldId}"/>" value="<c:out value="${form.items[lineKey].lineCode}"/>" />
            <input type="hidden" name="<c:out value="${lineCodeDescriptionFieldName}"/>" id="<c:out value="${lineCodeDescriptionFieldId}"/>" value="<c:out value="${form.items[lineKey].lineCodeDescription}"/>" />
            <input type="hidden" name="<c:out value="${itemTypeFieldName}"/>" id="<c:out value="${itemTypeFieldId}"/>" value="<c:out value="${form.items[lineKey].itemType}"/>" />
            <input type="hidden" name="<c:out value="${isNewFieldName}"/>" id="<c:out value="${isNewFieldId}"/>" value="<c:out value="${form.items[lineKey].new}"/>" />
            <input type="hidden" name="<c:out value="${isDeletedFieldName}"/>" id="<c:out value="${isDeletedFieldId}"/>" value="<c:out value="${form.items[lineKey].deleted}"/>" />
            <input type="hidden" name="<c:out value="${isMarketCommodityFieldName}"/>" id="<c:out value="${isMarketCommodityFieldId}"/>" value="<c:out value="${form.items[lineKey].marketCommodity}"/>" />
            <c:choose>
              <c:when test="${form.items[lineKey].new == true}">
                <c:set var="searchInputFieldName" value="item(${lineKey}).searchInput"/>
                <c:set var="searchInputFieldId" value="item_${lineKey}_searchInput"/>
                <div id="autoCompleteContainer.<c:out value="${lineKey}"/>" style="z-index:<c:out value="${autoCompleteZIndex}"/>;position: relative;">
                  <input type="text" name="<c:out value="${searchInputFieldName}"/>" id="<c:out value="${searchInputFieldId}"/>" value="<c:out value="${form.items[lineKey].searchInput}"/>" style="width:96%" />
                  <div id="itemContainer.<c:out value="${lineKey}"/>" style="position: absolute;"></div>
                </div>
                <script type="text/javascript">
                  //<![CDATA[
                  YAHOO.farm.ItemSelectHandler = getItemSelectHandler(<c:out value="${dataSource},'${itemType}','${searchInputFieldId}','${lineKey}','${lineCodeFieldId}','${lineCodeDescriptionFieldId}','${isMarketCommodityFieldId}'" escapeXml="false"/>);
                  //]]>
                </script>
               </c:when>
              <c:otherwise>
                  <c:out value="${form.items[lineKey].lineCode}"/>
              </c:otherwise>
            </c:choose>
          </th>
          <c:choose>
            <c:when test="${form.items[lineKey].new}">
              <c:if test="${itemType eq form.typeCrop}">
                <th>
                  <html-el:select property="item(${lineKey}).cropUnitCode">
                    <html:option value=""/>
                    <html-el:optionsCollection name="server.list.crop.units"/>
                  </html-el:select>
                </th>
              </c:if>
            </c:when>
            <c:otherwise>
              <th class="row"><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
              <c:if test="${itemType eq form.typeCrop}">
                <th><c:out value="${form.items[lineKey].cropUnitCodeDescription}"/></th>
              </c:if>
            </c:otherwise>
          </c:choose>
          <c:if test="${itemType eq form.typeCrop}">
            <th><c:out value="${form.items[lineKey].onFarmAcres}"/></th>
            <th><c:out value="${form.items[lineKey].unseedableAcres}"/></th>
          </c:if>

          <c:if test="${itemType eq form.typeCrop}">

            <td>
              <c:set var="adjustInputName" value="adjustInput_${lineKey}_QuantityProduced"/>
              <c:choose>
                <c:when test="${form.items[lineKey].errorQuantityProduced}">
                  <c:set var="inputClass" value="adjustmentExistsError"/>
                </c:when>
                <c:when test="${form.items[lineKey].showAdjToolTipQuantityProduced}">
                  <c:set var="inputClass" value="adjustmentExists"/>
                  <script type="text/javascript">
                    //<![CDATA[
                    Farm.<c:out value="${adjustInputName}"/>TT =
                      new YAHOO.widget.Tooltip("<c:out value="${adjustInputName}"/>TT",
                          { context:"<c:out value="${adjustInputName}"/>",
                            text:"<div style='text-align:left'><b>Original Value</b>: <c:out value="${form.items[lineKey].reportedQuantityProduced}"/><br />"
                              <c:if test="${!empty form.items[lineKey].adjustedByUserId}">
                                + "<b>Adjusted By</b>: <c:out value="${form.items[lineKey].adjustedByUserId}"/>"
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
                <html-el:text property="item(${lineKey}).totalQuantityProduced" styleId="item_${lineKey}_totalQuantityProduced" styleClass="${inputClass}" size="10" onclick="selectAll(this)" />
              </div>
            </td>

          </c:if>

          <c:if test="${itemType eq form.typeLivestock}">
            <th>
              <c:if test="${form.items[lineKey].marketCommodity}">
                <img src="images/tick.gif" alt="<fmt:message key="Yes"/>" />
              </c:if>
            </th>
          </c:if>

          <td>
            <c:set var="adjustInputName" value="adjustInput_${lineKey}_QuantityStart"/>
            <c:choose>
              <c:when test="${form.items[lineKey].errorQuantityStart}">
                <c:set var="inputClass" value="adjustmentExistsError"/>
              </c:when>
              <c:when test="${form.items[lineKey].showAdjToolTipQuantityStart}">
                <c:set var="inputClass" value="adjustmentExists"/>
                <script type="text/javascript">
                  //<![CDATA[
                  Farm.<c:out value="${adjustInputName}"/>TT =
                    new YAHOO.widget.Tooltip("<c:out value="${adjustInputName}"/>TT",
                        { context:"<c:out value="${adjustInputName}"/>",
                          text:"<div style='text-align:left'><b>Original Value</b>: <c:out value="${form.items[lineKey].reportedQuantityStart}"/><br />"
                            <c:if test="${!empty form.items[lineKey].adjustedByUserId}">
                              + "<b>Adjusted By</b>: <c:out value="${form.items[lineKey].adjustedByUserId}"/>"
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
              <html-el:text property="item(${lineKey}).totalQuantityStart" styleId="item_${lineKey}_totalQuantityStart" styleClass="${inputClass}" size="10" onclick="selectAll(this)" />
            </div>
          </td>

          <td>
            <c:set var="adjustInputName" value="adjustInput_${lineKey}_PriceStart"/>
            <c:set var="inputClass" value=""/>
            <c:choose>
              <c:when test="${form.items[lineKey].errorPriceStart}">
                <c:set var="inputClass" value="adjustmentExistsError"/>
              </c:when>
              <c:when test="${form.items[lineKey].priceStartOutsideFmvVariance and form.items[lineKey].showAdjToolTipPriceStart}">
                <c:set var="inputClass" value="adjustedPriceOutsideFmvVariance"/>
              </c:when>
              <c:when test="${form.items[lineKey].priceStartOutsideFmvVariance}">
                <c:set var="inputClass" value="priceOutsideFmvVariance"/>
              </c:when>
              <c:when test="${form.items[lineKey].showAdjToolTipPriceStart}">
                <c:set var="inputClass" value="adjustmentExists"/>
              </c:when>
            </c:choose>
            <script type="text/javascript">
              //<![CDATA[
              Farm.<c:out value="${adjustInputName}"/>TT =
                new YAHOO.widget.Tooltip("<c:out value="${adjustInputName}"/>TT",
                    { context:"<c:out value="${adjustInputName}"/>",
                      text:"<div style='text-align:left'>"
                        <c:if test="${(form.items[lineKey].showAdjToolTipPriceStart)}">
                          + "<b>Original Value</b>: <c:out value="${form.items[lineKey].reportedPriceStart}"/>"
                          <c:if test="${!empty form.items[lineKey].adjustedByUserId}">
                            + "<br /><b>Adjusted By</b>: <c:out value="${form.items[lineKey].adjustedByUserId}"/>"
                          </c:if>
                          + "<br />"
                        </c:if>
                        <c:choose>
                          <c:when test="${!empty form.items[lineKey].fmvPreviousYearEnd}"> 
                            + "<b><fmt:message key="FMV.Previous.Year.End"/></b>: <fmt:formatNumber type="currency" value="${form.items[lineKey].fmvPreviousYearEnd}"/>"
                          </c:when>
                          <c:otherwise>
                            + "<b><fmt:message key="FMV.Start"/></b>: <fmt:formatNumber type="currency" value="${form.items[lineKey].fmvStart}"/>"
                          </c:otherwise>
                        </c:choose>
                        + "<br /><b><fmt:message key="FMV.Variance"/></b>: <c:out value="${form.items[lineKey].fmvVariance}"/>"
                        <c:if test="${form.items[lineKey].priceStartOutsideFmvVariance}">
                          + "<br /><b><fmt:message key="WARNING"/>: </b><fmt:message key="price.outside.fmv.variance"/>"
                        </c:if>
                        + "</div>",
                      autodismissdelay: 7500 });
              //]]>
            </script>
            <div id="<c:out value="${adjustInputName}"/>">
              <html-el:text property="item(${lineKey}).totalPriceStart" styleId="item_${lineKey}_totalPriceStart" styleClass="${inputClass}" size="10" onclick="selectAll(this)" />
            </div>
          </td>

          <td>
            <c:set var="adjustInputName" value="adjustInput_${lineKey}_QuantityEnd"/>
            <c:choose>
              <c:when test="${form.items[lineKey].errorQuantityEnd}">
                <c:set var="inputClass" value="adjustmentExistsError"/>
              </c:when>
              <c:when test="${form.items[lineKey].showAdjToolTipQuantityEnd}">
                <c:set var="inputClass" value="adjustmentExists"/>
                <script type="text/javascript">
                  //<![CDATA[
                  Farm.<c:out value="${adjustInputName}"/>TT =
                    new YAHOO.widget.Tooltip("<c:out value="${adjustInputName}"/>TT",
                        { context:"<c:out value="${adjustInputName}"/>",
                          text:"<div style='text-align:left'><b>Original Value</b>: <c:out value="${form.items[lineKey].reportedQuantityEnd}"/><br />"
                          <c:if test="${!empty form.items[lineKey].adjustedByUserId}">
                            + "<b>Adjusted By</b>: <c:out value="${form.items[lineKey].adjustedByUserId}"/>"
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
              <html-el:text property="item(${lineKey}).totalQuantityEnd" styleId="item_${lineKey}_totalQuantityEnd" styleClass="${inputClass}" size="10" onclick="selectAll(this)" />
            </div>
          </td>

          <td>
            <c:set var="adjustInputName" value="adjustInput_${lineKey}_PriceEnd"/>
            <c:set var="inputClass" value=""/>
            <c:choose>
              <c:when test="${form.items[lineKey].errorPriceEnd}">
                <c:set var="inputClass" value="adjustmentExistsError"/>
              </c:when>
              <c:when test="${form.items[lineKey].priceEndOutsideFmvVariance and form.items[lineKey].showAdjToolTipPriceEnd}">
                <c:set var="inputClass" value="adjustedPriceOutsideFmvVariance"/>
              </c:when>
              <c:when test="${form.items[lineKey].priceEndOutsideFmvVariance}">
                <c:set var="inputClass" value="priceOutsideFmvVariance"/>
              </c:when>
              <c:when test="${form.items[lineKey].showAdjToolTipPriceEnd}">
                <c:set var="inputClass" value="adjustmentExists"/>
              </c:when>
            </c:choose>
            <script type="text/javascript">
              //<![CDATA[
              Farm.<c:out value="${adjustInputName}"/>TT =
                new YAHOO.widget.Tooltip("<c:out value="${adjustInputName}"/>TT",
                    { context:"<c:out value="${adjustInputName}"/>",
                      text:"<div style='text-align:left'>"
                        <c:if test="${form.items[lineKey].showAdjToolTipPriceEnd}">
                          + "<b>Original Value</b>: <c:out value="${form.items[lineKey].reportedPriceEnd}"/>"
                          <c:if test="${!empty form.items[lineKey].adjustedByUserId}">
                            + "<br /><b>Adjusted By</b>: <c:out value="${form.items[lineKey].adjustedByUserId}"/>"
                          </c:if>
                          + "<br />"
                        </c:if>
                        + "<b><fmt:message key="Producer.Price"/></b>: <fmt:formatNumber type="currency" value="${form.items[lineKey].endYearProducerPrice}"/>"
                        + "<br /><b><fmt:message key="FMV.End"/></b>: <fmt:formatNumber type="currency" value="${form.items[lineKey].fmvEnd}"/>"
                        + "<br /><b><fmt:message key="FMV.Variance"/></b>: <c:out value="${form.items[lineKey].fmvVariance}"/>"
                        <c:if test="${form.items[lineKey].priceEndOutsideFmvVariance}">
                          + "<br /><b><fmt:message key="WARNING"/>: </b><fmt:message key="price.outside.fmv.variance"/>"
                        </c:if>
                        + "</div>",
                      autodismissdelay: 7500 });
              //]]>
            </script>
            <div id="<c:out value="${adjustInputName}"/>">
              <html-el:text property="item(${lineKey}).totalPriceEnd" styleId="item_${lineKey}_totalPriceEnd" styleClass="${inputClass}" size="10" onclick="selectAll(this)" />
            </div>
          </td>

          <td>
            <fmt:formatNumber type="currency" value="${form.items[lineKey].changeInValue}"/>
          </td>
        </tr>
      </c:if>

    </logic-el:iterate>

    <c:if test="${ ! form.addedNew and ! form.readOnly}">
      <tr>
        <th class="row">&nbsp;</th>
        <th class="row">&nbsp;</th>
        <th>&nbsp;</th>
        <c:if test="${itemType eq form.typeCrop}">
          <th>&nbsp;</th>
          <th>&nbsp;</th>
          <td>&nbsp;</td>
        </c:if>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td><a id="<c:out value="addNewButton_${itemType}"/>" href="#" class="button"><fmt:message key="Add.New"/></a></td>
      </tr>
    </c:if>

    <tr>
      <c:if test="${itemType eq form.typeCrop}">
        <td colspan="11" class="rowSpacer"><img src="images/spacer.gif" width="1" height="1" alt="" /></td>
      </c:if>
      <c:if test="${itemType eq form.typeLivestock}">
        <td colspan="8" class="rowSpacer"><img src="images/spacer.gif" width="1" height="1" alt="" /></td>
      </c:if>
    </tr>
    <tr>
      <c:if test="${itemType eq form.typeCrop}">
        <td colspan="6" class="cellWhite">&nbsp;</td>
      </c:if>
      <c:if test="${itemType eq form.typeLivestock}">
        <td colspan="3" class="cellWhite">&nbsp;</td>
      </c:if>
      <th colspan="4" class="row">
        <div align="right">
          <c:choose>
            <c:when test="${itemType eq form.typeCrop}">
              <fmt:message key="Total.Change.in.Value.of.Crop.Inventory"/>
            </c:when>
            <c:when test="${itemType eq form.typeLivestock}">
              <fmt:message key="Total.Change.in.Value.of.Livestock.Inventory"/>
            </c:when>
          </c:choose>
        </div>
      </th>
      <td class="totalAmount"><fmt:formatNumber type="currency" value="${form.totalValues[itemType]}"/></td>
    </tr>
  </table>
  </div>


  <div id="adjust_<c:out value="${itemType}"/>" style="display:none;">
  <table class="numeric"  style="width:950px">
    <tr>
      <th><fmt:message key="Code"/></th>
      <th><fmt:message key="Description"/></th>
      <c:if test="${itemType eq form.typeCrop}">
        <th><fmt:message key="Units"/></th>
        <th width="50"><fmt:message key="On.Farm.Acres"/></th>
        <th width="50"><fmt:message key="Unseedable.Acres"/></th>
        <th><fmt:message key="Qty.Produced"/></th>
      </c:if>
      <c:if test="${itemType eq form.typeLivestock}">
        <th><fmt:message key="Market.Commodity"/></th>
      </c:if>
      <th><fmt:message key="Qty.Start"/></th>
      <th><fmt:message key="Price.Start"/></th>
      <th><fmt:message key="Qty.End"/></th>
      <th><fmt:message key="Price.End"/></th>
      <th><fmt:message key="Delete.Adjustment"/></th>
    </tr>

    <logic-el:iterate name="form" property="lineKeys" id="lineKey">
      <c:if test="${form.items[lineKey].itemType eq itemType and ! form.items[lineKey].new}">
        <c:set var="adjustInputName" value="adjustInput_${lineKey}"/>
        <c:set var="deleteAdjustName" value="deleteAdjust_${lineKey}"/>
        <tr>
          <th>
            <c:out value="${form.items[lineKey].lineCode}"/>
          </th>
          <th>
            <c:out value="${form.items[lineKey].lineCodeDescription}"/>
          </th>
          <c:if test="${itemType eq form.typeCrop}">
            <th><c:out value="${form.items[lineKey].cropUnitCodeDescription}"/></th>
            <th></th>
            <th></th>
            <td>
              <c:if test="${ ! form.items[lineKey].deleted }">
                <div id="<c:out value="${deleteAdjustName}"/>QuantityProducedContainer">
                  <c:out value="${form.items[lineKey].adjQuantityProduced}"/>
                </div>
              </c:if>
            </td>
          </c:if>

          <c:if test="${itemType eq form.typeLivestock}">
            <th><c:if test="${form.items[lineKey].marketCommodity}"><img src="images/tick.gif" alt="<fmt:message key="Yes"/>" /></c:if></th>
          </c:if>

          <td>
            <c:if test="${ ! form.items[lineKey].deleted }">
              <div id="<c:out value="${deleteAdjustName}"/>QuantityStartContainer"><c:out value="${form.items[lineKey].adjQuantityStart}"/></div>
            </c:if>
          </td>
          <td>
            <c:if test="${ ! form.items[lineKey].deleted }">
              <div id="<c:out value="${deleteAdjustName}"/>PriceStartContainer"><c:out value="${form.items[lineKey].adjPriceStart}"/></div>
            </c:if>
          </td>
          <td>
            <c:if test="${ ! form.items[lineKey].deleted }">
              <div id="<c:out value="${deleteAdjustName}"/>QuantityEndContainer"><c:out value="${form.items[lineKey].adjQuantityEnd}"/></div>
            </c:if>
          </td>
          <td>
            <c:if test="${ ! form.items[lineKey].deleted }">
              <div id="<c:out value="${deleteAdjustName}"/>PriceEndContainer"><c:out value="${form.items[lineKey].adjPriceEnd}"/></div>
            </c:if>
          </td>
          <td>
            <c:if test="${!empty form.items[lineKey].adjustmentId and !form.items[lineKey].deleted}">
              <div id="<c:out value="${deleteAdjustName}"/>ButtonContainer">
                <c:if test="${ ! form.readOnly }">
                  <img id="<c:out value="${deleteAdjustName}"/>" src="images/error.png" alt="<fmt:message key="Delete.Adjustment"/>" width="16" height="16" align="middle" onclick="deleteAdjustment(Farm.<c:out value="${deleteAdjustName}TT, Farm.${adjustInputName}_QuantityProducedTT, Farm.${adjustInputName}_QuantityStartTT, Farm.${adjustInputName}_QuantityEndTT, Farm.${adjustInputName}_PriceStartTT, Farm.${adjustInputName}_PriceEndTT, '${itemType}', '${lineKey}', '${form.items[lineKey].reportedPriceStartOutsideFmvVariance}', '${form.items[lineKey].reportedPriceEndOutsideFmvVariance}', '${form.items[lineKey].reportedQuantityProduced}', '${form.items[lineKey].reportedQuantityStart}', '${form.items[lineKey].reportedQuantityEnd}', '${form.items[lineKey].reportedPriceStart}', '${form.items[lineKey].reportedPriceEnd}'"/>);" />
                </c:if>
              </div>
              <script type="text/javascript">
                //<![CDATA[
                Farm.<c:out value="${deleteAdjustName}"/>TT = new YAHOO.widget.Tooltip("<c:out value="${deleteAdjustName}"/>TT", { context:"<c:out value="${deleteAdjustName}"/>", text:"Click here to delete the adjustment.", autodismissdelay: 7500 });
                //]]>
              </script>
            </c:if>
          </td>
        </tr>
      </c:if>

    </logic-el:iterate>
  </table>
  </div>


  <div id="cra_<c:out value="${itemType}"/>" style="display:none;">
  <table class="numeric"  style="width:950px">
    <tr>
      <th><fmt:message key="Code"/></th>
      <th><fmt:message key="Description"/></th>
      <c:if test="${itemType eq form.typeCrop}">
        <th><fmt:message key="Units"/></th>
        <th width="50"><fmt:message key="On.Farm.Acres"/></th>
        <th width="50"><fmt:message key="Unseedable.Acres"/></th>
        <th width="80"><fmt:message key="Qty.Produced"/></th>
      </c:if>
      <c:if test="${itemType eq form.typeLivestock}">
        <th><fmt:message key="Market.Commodity"/></th>
      </c:if>
      <th width="80"><fmt:message key="Qty.Start"/></th>
      <th width="80"><fmt:message key="Price.Start"/></th>
      <th width="80"><fmt:message key="Qty.End"/></th>
      <th width="80"><fmt:message key="Price.End"/></th>
    </tr>

    <logic-el:iterate name="form" property="lineKeys" id="lineKey">
      <c:if test="${form.items[lineKey].itemType eq itemType and ! form.items[lineKey].new}">
        <tr>
          <th>
            <c:out value="${form.items[lineKey].lineCode}"/>
          </th>
          <th>
            <c:out value="${form.items[lineKey].lineCodeDescription}"/>
          </th>

          <c:if test="${itemType eq form.typeCrop}">
            <th><c:out value="${form.items[lineKey].cropUnitCodeDescription}"/></th>
            <th><c:out value="${form.items[lineKey].onFarmAcres}"/></th>
            <th><c:out value="${form.items[lineKey].unseedableAcres}"/></th>
            <td><c:out value="${form.items[lineKey].reportedQuantityProduced}"/></td>
          </c:if>

          <c:if test="${itemType eq form.typeLivestock}">
            <th>
              <c:if test="${form.items[lineKey].marketCommodity}">
                <img src="images/tick.gif" alt="<fmt:message key="Yes"/>" />
              </c:if>
            </th>
          </c:if>

          <td><c:out value="${form.items[lineKey].reportedQuantityStart}"/></td>
          <td><c:out value="${form.items[lineKey].reportedPriceStart}"/></td>
          <td><c:out value="${form.items[lineKey].reportedQuantityEnd}"/></td>
          <td><c:out value="${form.items[lineKey].reportedPriceEnd}"/></td>

        </tr>
      </c:if>

    </logic-el:iterate>
  </table>
  </div>

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
    if(isFormDirty(document.getElementById("inventoryForm"))) {
      alert("Save changes before importing.")
    } else {
      $( "#dialog" ).dialog( "open" );
      event.preventDefault();
    }
  });

  function displayFilterResults() {
    if(document.getElementById('cropsRadio').checked == true) type='<c:out value="${form.typeCrop}"/>';
    if(document.getElementById('livestockRadio').checked == true) type='<c:out value="${form.typeLivestock}"/>';

    if(document.getElementById('adjustedRadio').checked == true) view='total';
    if(document.getElementById('adjustmentsRadio').checked == true) view='adjust';
    if(document.getElementById('craRadio').checked == true) view='cra';
    var tableId = view + '_' + type;

    <logic-el:iterate name="form" property="itemTypes" id="itemType">
      document.getElementById('total_<c:out value="${itemType}"/>').style.display = 'none';
      document.getElementById('adjust_<c:out value="${itemType}"/>').style.display = 'none';
      document.getElementById('cra_<c:out value="${itemType}"/>').style.display = 'none';
    </logic-el:iterate>

    document.getElementById(tableId).style.display = '';
  }

  displayFilterResults();

  new YAHOO.widget.Button("saveButton");
  function submitFunc() { submitForm(document.getElementById('inventoryForm')); }
  YAHOO.util.Event.addListener(document.getElementById("saveButton"), "click", submitFunc);

  function addNew() {
    showProcessing();
    disableDirtyFormCheck();
    var form = document.getElementById('inventoryForm');
    form.action = '<html:rewrite action="addNewInventory"/>';
    form.submit();
  }

  function outputCsv() {
	  var itemTypeRadio = $('input:radio[name=itemTypeRadio]:checked').val();
	  var financialView = $('input:radio[name=financialViewRadio]:checked').val();
	
	  document.location.href = "outputInventory.do?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>" + "&farmViewYear=<c:out value="${form.farmViewYear}"/>" + "&farmView=<c:out value="${form.farmView}"/>" + "&itemTypeRadio=" + itemTypeRadio + "&financialViewRadio=" +  financialView;
  }

  <logic-el:iterate name="form" property="itemTypes" id="itemType">
    <c:set var="addNewButtonId" value="addNewButton_${itemType}"/>
    new YAHOO.widget.Button("<c:out value="${addNewButtonId}"/>");
    YAHOO.util.Event.addListener(document.getElementById("<c:out value="${addNewButtonId}"/>"), "click", addNew);
  </logic-el:iterate>

  new YAHOO.widget.Button("outputCsv");
  YAHOO.util.Event.addListener(document.getElementById("outputCsv"), "click", outputCsv);

  
  <c:if test="${ ! form.readOnly }">
    registerFormForDirtyCheck(document.getElementById("inventoryForm"));
    excludeFieldFromDirtyCheck('itemTypeRadio');
    excludeFieldFromDirtyCheck('financialViewRadio');
  
    <c:if test="${form.addedNew}">
      markFormAsDirty();
    </c:if>
    
    new YAHOO.widget.Button("import-dialog-button");

    function selectScenarioOperation(scenarioNumber, operationSchedule) {
      $('#dialog').dialog('close');
      disableDirtyFormCheck();
      var form = document.getElementById('inventoryForm');
      form.importScenarioNumber.value = scenarioNumber;
      form.importOperationSchedule.value = operationSchedule;
      form.action = '<html:rewrite action="importInventory"/>';
      form.submit();
    }
    
    <c:forEach var="item" items="${form.operationsForImport}">  
      new YAHOO.widget.Button("selectButton<c:out value="${item.scenarioNumber}"/>");
    </c:forEach>
  </c:if>

  $(document).ready(function() {
    var farmView = '<c:out value="${ form.farmView }"/>';
    if(farmView == 'WHOLE') {
      $('#inventoryForm').find('input[type=text]').change(function() {
        alert('<fmt:message key="editing.in.whole.farm.view.warning"/>');
      });
    }
  });

  //]]>
</script>
