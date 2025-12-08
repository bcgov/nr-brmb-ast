<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="yui/2.8.2r1/build/animation/animation-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/autocomplete/autocomplete-min.js"></script>

<w:ifUserCanPerformAction action="editCodes">
  <c:set var="canEdit" value="true"/>
</w:ifUserCanPerformAction>

<script type="text/javascript" src="<html:rewrite action="structureGroupCodeList"/>"></script>
<script type="text/javascript">
  //<![CDATA[

  function getItemSelectHandler() {
    return function() {
      // Use a LocalDataSource
      var oDS = new YAHOO.util.LocalDataSource(YAHOO.farm.Data.structureGroupCodes);
      oDS.responseSchema = {fields : ["n", "i"]};

      // Instantiate the AutoComplete
      var searchInputField = "structureGroupSearchInput";
      var oAC = new YAHOO.widget.AutoComplete(searchInputField, "itemContainer", oDS);
      oAC.resultTypeList = false;
      oAC.forceSelection = true;

      // Define an event handler to populate a hidden form field
      // when an item gets selected
      var lineCodeHiddenField = YAHOO.util.Dom.get("rollupStructureGroupCode");
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
  <fmt:message key="Structure.Group"/> <fmt:message key="Code"/>
</h1> 
<p></p>

<html:form action="saveStructureGroupCode" styleId="codeForm" method="post">
  <html:hidden property="new"/>
  <html:hidden property="rollupStructureGroupCode" styleId="rollupStructureGroupCode"/>
  <html:hidden property="rollupStructureGroupCodeDescription"/>
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
        <th style="width:100px"><fmt:message key="Rollup.Structure.Group.Code"/>:</th>
        <td>
          <c:choose>
            <c:when test="${canEdit}">
              <div id="autoCompleteContainer">
                <html:text property="structureGroupSearchInput" styleId="structureGroupSearchInput" style="width:96%" onkeypress="return disableEnterKey(event)" />
                <div id="itemContainer"></div>
              </div>
              <script type="text/javascript">
                //<![CDATA[
                YAHOO.farm.ItemSelectHandler = getItemSelectHandler();
                //]]>
              </script>
            </c:when>
            <c:otherwise>
              <c:out value="${form.rollupStructureGroupCode}"/> - <c:out value="${form.rollupStructureGroupCodeDescription}"/>
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
           action="deleteStructureGroupCode"/>
      </c:if>
      <u:dirtyFormCheck formId="codeForm"/>
    </w:ifUserCanPerformAction>
    <u:yuiButton buttonId="cancelButton" buttonLabel="Cancel" action="farm292"/>
  </div>

</html:form>
