<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="yui/2.8.2r1/build/animation/animation-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/autocomplete/autocomplete-min.js"></script>

<w:ifUserCanPerformAction action="editCodes">
  <c:set var="canEdit" value="true"/>
</w:ifUserCanPerformAction>

<c:choose>
  <c:when test="${form.inventoryClassCode == '3'}">
    <c:set var="accrualTypeLabel" value="Input"/>
    <c:set var="saveAction" value="saveInputXref"/>
    <c:set var="deleteAction" value="deleteInputXref"/>
    <c:set var="cancelAction" value="farm450"/>
  </c:when>
  <c:when test="${form.inventoryClassCode == '4'}">
    <c:set var="accrualTypeLabel" value="Receivable"/>
    <c:set var="saveAction" value="saveReceivableXref"/>
    <c:set var="deleteAction" value="deleteReceivableXref"/>
    <c:set var="cancelAction" value="farm460"/>
  </c:when>
  <c:when test="${form.inventoryClassCode == '5'}">
    <c:set var="accrualTypeLabel" value="Payable"/>
    <c:set var="saveAction" value="savePayableXref"/>
    <c:set var="deleteAction" value="deletePayableXref"/>
    <c:set var="cancelAction" value="farm470"/>
  </c:when>
</c:choose>

<c:if test="${form.new}">
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
        var lineCodeHiddenField = YAHOO.util.Dom.get("inventoryItemCode");
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
</c:if>

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
  <fmt:message key="${accrualTypeLabel}"/>
</h1> 
<p></p>

<html-el:form action="${saveAction}" styleId="codeForm" method="post">
  <html:hidden property="new"/>
  <html:hidden property="commodityXrefId"/>
  <html:hidden property="inventoryItemCode" styleId="inventoryItemCode"/>
  <html:hidden property="inventoryItemCodeDescription"/>
  <html:hidden property="inventoryClassCode"/>
  <html:hidden property="marketCommodity" value="true"/>
  <html:hidden property="revisionCount"/>

  <fieldset style="width:70%">
    <table class="formInput" style="width:100%">
      <tr>
        <th style="width:140px"><fmt:message key="Inventory.Item.Code"/>:</th>
        <td>
          <c:choose>
            <c:when test="${form.new}">
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
              <c:out value="${form.inventoryItemCode}"/> - <c:out value="${form.inventoryItemCodeDescription}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
      <tr>
        <th style="width:140px"><fmt:message key="Inventory.Group"/>:</th>
        <td>
          <c:choose>
            <c:when test="${canEdit}">
              <div style="float:left;width:250px">
                <html:select property="inventoryGroupCode">
                  <html:option value=""></html:option>
                  <html:optionsCollection name="server.list.inventory.group.codes"/>
                </html:select>
              </div>
            </c:when>
            <c:otherwise>
              <c:out value="${form.inventoryGroupCodeDescription}"/>
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
           action="${deleteAction}"/>
      </c:if>
      <u:dirtyFormCheck formId="codeForm"/>
    </w:ifUserCanPerformAction>
    <u:yuiButton buttonId="cancelButton" buttonLabel="Cancel" action="${cancelAction}"/>
  </div>

</html-el:form>
