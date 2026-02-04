<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="yui/2.8.2r1/build/animation/animation-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/autocomplete/autocomplete-min.js"></script>

<w:ifUserCanPerformAction action="editCodes">
  <c:set var="canEdit" value="true"/>
</w:ifUserCanPerformAction>

<script type="text/javascript" src="<html:rewrite action="inventoryItemCodeList"/>"></script>
<script type="text/javascript">
  //<![CDATA[

  function getItemSelectHandler() {
    return function() {
      // Use a LocalDataSource
      var oDS = new YAHOO.util.LocalDataSource(YAHOO.farm.Data.inventoryItemCodes);
      oDS.responseSchema = {fields : ["n", "i"]};

      // Instantiate the AutoComplete
      var searchInputField = "inventorySearchInput";
      var oAC = new YAHOO.widget.AutoComplete(searchInputField, "itemContainer", oDS);
      oAC.resultTypeList = false;
      oAC.forceSelection = true;

      // Define an event handler to populate a hidden form field
      // when an item gets selected
      var lineCodeHiddenField = YAHOO.util.Dom.get("rollupInventoryItemCode");
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
  <fmt:message key="Inventory.Item"/> <fmt:message key="Code"/>
</h1> 
<p></p>

<html:form action="saveInventoryItemCode" styleId="codeForm" method="post">
  <html:hidden property="new"/>
  <html:hidden property="rollupInventoryItemCode" styleId="rollupInventoryItemCode"/>
  <html:hidden property="rollupInventoryItemCodeDescription"/>
  <html:hidden property="revisionCount"/>

  <fieldset style="width:70%">
    <table class="formInput" style="width:100%">
      <tr>
        <th style="width:100px"><fmt:message key="Code"/>:</th>
        <td>
          <c:choose>
            <c:when test="${form.new}">
              <div style="float:left;width:100px">
                <html:text property="code" size="30" maxlength="10"/>
              </div>
            </c:when>
            <c:otherwise>
              <html:hidden property="code"/>
              <c:out value="${form.code}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
      <tr>
        <th style="width:100px"><fmt:message key="Description"/>:</th>
        <td>
          <c:choose>
            <c:when test="${canEdit}">
              <html:text property="description" size="80" maxlength="256"/>
            </c:when>
            <c:otherwise>
              <c:out value="${form.description}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
      <tr>
        <th style="width:100px"><fmt:message key="Rollup.Inventory.Item.Code"/>:</th>
        <td>
          <c:choose>
            <c:when test="${canEdit}">
              <div id="autoCompleteContainer">
                <html:text property="inventorySearchInput" styleId="inventorySearchInput" style="width:96%" onkeypress="return disableEnterKey(event)" />
                <div id="itemContainer"></div>
              </div>
              <script type="text/javascript">
                //<![CDATA[
                YAHOO.farm.ItemSelectHandler = getItemSelectHandler();
                //]]>
              </script>
            </c:when>
            <c:otherwise>
              <c:out value="${form.rollupInventoryItemCode}"/> - <c:out value="${form.rollupInventoryItemCodeDescription}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
    </table>
  </fieldset>

  <table class="numeric">
      <tr>
        <th style="width:50px"><fmt:message key="Year"/></th>
        <th style="width:60px;text-align:center;"><fmt:message key="Eligible"/></th>
        <th style="width:60px;text-align:center;"><fmt:message key="Commodity.Type"/></th>
        <th style="width:60px;text-align:center;"><fmt:message key="Fruit.Veg.Type"/></th>
        <th style="width:60px;text-align:center;"><fmt:message key="Line.Item"/></th>
        <th style="width:60px;text-align:center;"><fmt:message key="Insurable.Value"/></th>
        <th style="width:60px;text-align:center;"><fmt:message key="Premium.Rate"/></th>
      </tr>
      
    <c:forEach var="year" items="${form.years}" varStatus="yearLoop">
      <tr>
        <td style="text-align:center;"><c:out value="${year}"/></td>
        <td style="text-align:center;">

          <c:set var="inventoryItemDetailIdFieldName" value="detail(${year}).inventoryItemDetailId"/>
          <c:set var="inventoryItemDetailIdFieldId" value="detail_${year}_inventoryItemDetailId"/>
          <c:set var="eligibleOriginalFieldName" value="detail(${year}).eligibleOriginal"/>
          <c:set var="eligibleOriginalFieldId" value="detail_${year}_eligibleOriginal"/>
          <c:set var="revisionCountFieldName" value="detail(${year}).revisionCount"/>
          <c:set var="revisionCountFieldId" value="detail_${year}_revisionCount"/>

          <input type="hidden" name="<c:out value="${inventoryItemDetailIdFieldName}"/>" id="<c:out value="${inventoryItemDetailIdFieldId}"/>" value="<c:out value="${form.details[year].inventoryItemDetailId}"/>" />
          <input type="hidden" name="<c:out value="${eligibleOriginalFieldName}"/>" id="<c:out value="${eligibleOriginalFieldId}"/>" value="<c:out value="${form.details[year].eligibleOriginal}"/>" />
          <input type="hidden" name="<c:out value="${revisionCountFieldName}"/>" id="<c:out value="${revisionCountFieldId}"/>" value="<c:out value="${form.details[year].revisionCount}"/>" />

          <html-el:select property="detail(${year}).eligible">
            <html-el:option value="true">Y</html-el:option>
            <html-el:option value="false">N</html-el:option>
          </html-el:select>
        </td>
        <td style="text-align:center;">
          <c:set var="commodityTypeCodeNameOriginalFieldName" value="detail(${year}).commodityTypeCodeNameOriginal"/>
          <c:set var="commodityTypeCodeNameOriginalFieldId" value="detail_${year}_commodityTypeCodeNameOriginal"/>

          <input type="hidden" name="<c:out value="${commodityTypeCodeNameOriginalFieldName}"/>" id="<c:out value="${commodityTypeCodeNameOriginalFieldId}"/>" value="<c:out value="${form.details[year].commodityTypeCodeNameOriginal}"/>" />
           <html-el:select property="detail(${year}).commodityTypeCodeName">
           	<option value=""></option>
	            <html:optionsCollection property="commodityTypesListViewItems"/> 
           </html-el:select>
        </td>
        <td style="text-align:center;">
          <c:set var="fruitVegCodeNameOriginalFieldName" value="detail(${year}).fruitVegCodeNameOriginal"/>
          <c:set var="fruitVegCodeNameOriginalFieldId" value="detail_${year}_fruitVegCodeNameOriginal"/>

          <input type="hidden" name="<c:out value="${fruitVegCodeNameOriginalFieldName}"/>" id="<c:out value="${fruitVegCodeNameOriginalFieldId}"/>" value="<c:out value="${form.details[year].fruitVegCodeNameOriginal}"/>" />
          <html-el:select property="detail(${year}).fruitVegCodeName">
           	<option value=""></option>
	          <html:optionsCollection property="fruitVegListViewItems"/> 
          </html-el:select>
        </td>
        <td>
          <c:choose>
            <c:when test="${canEdit}">
              <input type="hidden" name="<c:out value="${originalLineItem}"/>" id="<c:out value="${originalLineItem}"/>" value="<c:out value="${form.details[year].originalLineItem}"/>" />
              <html-el:text property="detail(${year}).lineItem" size="80" maxlength="4" style="width:100px;text-align:left;"/>
            </c:when>
            <c:otherwise>
            </c:otherwise>
          </c:choose>
        </td>
        <td>
          <c:choose>
            <c:when test="${canEdit}">
              <input type="hidden" name="<c:out value="${insurableValueOriginal}"/>" id="<c:out value="${insurableValueOriginal}"/>" value="<c:out value="${form.details[year].insurableValueOriginal}"/>" />
              <html-el:text property="detail(${year}).insurableValue" size="80" maxlength="8" style="width:100px;text-align:left;"/>
            </c:when>
            <c:otherwise>
            </c:otherwise>
          </c:choose>
        </td>
        <td>
          <c:choose>
            <c:when test="${canEdit}">
              <input type="hidden" name="<c:out value="${premiumRateOriginal}"/>" id="<c:out value="${premiumRateOriginal}"/>" value="<c:out value="${form.details[year].premiumRateOriginal}"/>" />
              <html-el:text property="detail(${year}).premiumRate" size="80" maxlength="8" style="width:100px;text-align:left;"/>
            </c:when>
            <c:otherwise>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
    </c:forEach>
  </table>

  <div style="text-align:right;width:70%">
    <w:ifUserCanPerformAction action="editCodes">
      <u:yuiButton buttonId="saveButton" buttonLabel="Save" formId="codeForm"/>
      <c:if test="${!form.new}">
        <u:yuiButton buttonId="deleteButton" buttonLabel="Delete" formId="codeForm"
           action="deleteInventoryItemCode"/>
      </c:if>
      <u:dirtyFormCheck formId="codeForm"/>
    </w:ifUserCanPerformAction>
    <u:yuiButton buttonId="cancelButton" buttonLabel="Cancel" action="farm420"/>
  </div>

</html:form>
