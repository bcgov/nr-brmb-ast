<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="yui/2.8.2r1/build/animation/animation-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/autocomplete/autocomplete-min.js"></script>

<c:if test="${form.new}">
  <script type="text/javascript" src="<html:rewrite action="productiveUnitLists"/>"></script>

  <script type="text/javascript">
    //<![CDATA[
    function getItemSelectHandler(itemContainer, searchInputField, codeField) {
      return function() {
        // Use a LocalDataSource
        var oDS = new YAHOO.util.LocalDataSource(YAHOO.farm.Data.pucs);
        oDS.responseSchema = {fields : ["n", "i", "t"]};

        // Instantiate the AutoComplete
        var oAC = new YAHOO.widget.AutoComplete(searchInputField, itemContainer, oDS);
        oAC.resultTypeList = false;
        oAC.forceSelection = true;

        // Define an event handler to populate a hidden form field
        // when an item gets selected
        var codeHiddenField = YAHOO.util.Dom.get(codeField);
        var selectionHandler = function(sType, aArgs) {
            var myAC = aArgs[0]; // reference back to the AC instance
            var elLI = aArgs[1]; // reference to the selected LI element
            var oData = aArgs[2]; // object literal of selected item's result data

            // update hidden form field with the selected item's ID
            codeHiddenField.value = oData.i;
        };
        oAC.itemSelectEvent.subscribe(selectionHandler);

        var clearHandler = function(sType, aArgs) {
            var myAC = aArgs[0]; // reference back to the AC instance
            var clearedValue = aArgs[1]; // The cleared value that did not match anything

            // update hidden form field with the selected item's ID
            lineCodeHiddenField.value = "";
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
  <fmt:message key="BPU"/>
</h1>
<p></p>

<html:form action="saveBPU" styleId="bpuForm" method="post">
  <html:hidden property="new"/>
  <html:hidden property="yearFilter"/>
  <html:hidden property="bpuId"/>
  <html:hidden property="inventoryCodeFilter"/>
  <html:hidden property="inventoryDescFilter"/>
  <html:hidden property="municipalityFilter"/>
  <html:hidden property="marginExpenseFilter"/>
  <html:hidden property="invSgCode" styleId="invSgCode" />
  
  <c:if test="${!form.new}">
    <html:hidden property="municipalityCode" styleId="municipalityCode"/>
  </c:if>
  
  <c:forEach varStatus="yearLoop" var="year" items="${form.years}">
    <input type="hidden" name="<c:out value="revisionCounts[${yearLoop.index}]"/>" value="<c:out value="${form.revisionCounts[yearLoop.index]}"/>" />
    <input type="hidden" name="<c:out value="years[${yearLoop.index}]"/>" value="<c:out value="${form.years[yearLoop.index]}"/>" />
  </c:forEach>

  <fieldset style="width:97%">
    <table class="formInput" border="1">
      <tr>
        <th style="width:120px"><fmt:message key="Program.Year"/>:</th>
        <td style="width:50px"> 
           <c:out value="${form.yearFilter}"/>
        </td>
        <th style="width:80px"><fmt:message key="Inventory"/>:</th>
        <td style="width:250px">
          <c:choose>
            <c:when test="${form.new}">
              <div id="autoInvContainer">
                <html:text property="inventorySearchInput" styleId="inventorySearchInput" style="width:80%" />
                <div id="invContainer"></div>
              </div>
              <script type="text/javascript">
                //<![CDATA[
                YAHOO.farm.ItemSelectHandler = getItemSelectHandler('invContainer', 'inventorySearchInput','invSgCode');
                //]]>
              </script>
            </c:when>
            <c:otherwise>
              <c:out value="${form.invSgCode} - ${form.invSgCodeDescription}"/>
            </c:otherwise>
          </c:choose>
        </td>
        <th style="width:100px"><fmt:message key="Municipality"/>:</th>
        <td style="width:200px">
          <c:choose>
            <c:when test="${form.new}">
              <html:select property="municipalityCode" style="width:80%">
                <html:option value=""/>
                <html:optionsCollection name="server.list.municipalities"/>
              </html:select>
            </c:when>
            <c:otherwise>
              <c:out value="${form.municipalityCodeDescription}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
    </table>

    <table class="numeric" style="width:100%">
      <tr>
        <th><fmt:message key="Year"/></th>
         <c:forEach var="year" items="${form.years}">
          <th><c:out value="${year}"/></th>
         </c:forEach>
      </tr>
      <tr>
        <th><fmt:message key="Margin"/></th>
        <c:forEach varStatus="marginLoop" var="averageMargin" items="${form.averageMargins}">
          <c:choose>
            <c:when test="${canEdit}">
              <td style="text-align:center">
                <html-el:text property="averageMargins[${marginLoop.index}]" size="8" maxlength="20"/>
              </td>
            </c:when>
            <c:otherwise>
              <td style="text-align:center"><fmt:formatNumber type="number" value="${averageMargin}"/></td>
            </c:otherwise>
          </c:choose>
        </c:forEach>
      </tr>
      <c:if test="${form.growingForward2013}">
	      <tr>
	        <th><fmt:message key="Expense"/></th>
	        <c:forEach varStatus="expenseLoop" var="averageExpense" items="${form.averageExpenses}">
	          <c:choose>
	            <c:when test="${canEdit}">
	              <td style="text-align:center">
	                <html-el:text property="averageExpenses[${expenseLoop.index}]" size="8" maxlength="20"/>
	              </td>
	            </c:when>
	            <c:otherwise>
	              <td style="text-align:center"><fmt:formatNumber type="number" value="${averageExpense}"/></td>
	            </c:otherwise>
	          </c:choose>
	        </c:forEach>
	      </tr>
      </c:if>
    </table>
  </fieldset>
  
  <p />

  <div style="text-align:right;width:70%">
    <w:ifUserCanPerformAction action="editCodes">
      <u:yuiButton buttonId="saveButton" buttonLabel="Save" formId="bpuForm" />
      <c:if test="${!form.new}">
        <u:yuiButton buttonId="deleteButton" buttonLabel="Delete" formId="bpuForm" action="deleteBPU" confirmMessage="delete.bpu.warning"/>
      </c:if>
      <u:dirtyFormCheck formId="bpuForm"/>
    </w:ifUserCanPerformAction>
    <u:yuiButton buttonId="cancelButton" buttonLabel="Cancel" action="farm290" />
  </div>

</html:form>
