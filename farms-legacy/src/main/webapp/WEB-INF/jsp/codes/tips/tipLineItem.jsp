<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<script type="text/javascript" src="yui/2.8.2r1/build/animation/animation-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/autocomplete/autocomplete-min.js"></script>
<script type="text/javascript" src="<html:rewrite action="lineItemLists"/>"></script>

<c:if test="${!form.new}">
  <script type="text/javascript">
    var description;
	var lineItem = <c:out value="${form.lineItem}"/>;
	for (i = 0; i < YAHOO.farm.Data.lineItems.length; i++) {
	  if (YAHOO.farm.Data.lineItems[i].i == lineItem) {
	    description = YAHOO.farm.Data.lineItems[i].desc;
	  }
	}
  </script>
</c:if>

<c:if test="${form.new}">

   <script type="text/javascript">
     //<![CDATA[

    function getItemSelectHandler(oDS, searchInputField, lineCodeField, lineCodeDescriptionField) {
      return function() {
        // Use a LocalDataSource
        oDS.responseSchema = {fields : ["n", "i"]};

        // Instantiate the AutoComplete
        var oAC = new YAHOO.widget.AutoComplete(searchInputField, "itemContainer", oDS);
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
            document.getElementById('lineItem').value = oData.i;
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

<w:ifUserCanPerformAction action="editTipCodes">
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
  <fmt:message key="Line.Item"/>
</h1>

<c:set var="autoCompleteZIndex" value="${10000}"/>

<html:form action="/saveTipLineItem" styleId="codeForm" method="post">
  <html:hidden property="new"/>
  <html:hidden property="id"/>

  <fieldset style="width:70%">
    <table class="formInput" style="width:100%">
      <tr>
        <th style="width:100px"><fmt:message key="Line.Item"/>:</th>
        <td>
          <c:choose>
            <c:when test="${form.new}">
            <c:set var="dataSource" value="new YAHOO.util.LocalDataSource(YAHOO.farm.Data.lineItems)"/>
              <c:set var="lineCodeFieldId" value="itemLineCode"/>
              <c:set var="lineCodeDescriptionFieldName" value="itemlineCodeDescription"/>
              <c:set var="lineCodeDescriptionFieldId" value="itemlineCodeDescription"/>
              
              <input type="hidden" id="<c:out value="${lineCodeFieldId}"/>"  />
              <input type="hidden" name="<c:out value="${lineCodeDescriptionFieldName}"/>" id="<c:out value="${lineCodeDescriptionFieldId}"/>"  />
              <div style="float:left;width:50%">
               <c:set var="searchInputFieldName" value="itemSearchInputName"/>
               <c:set var="searchInputFieldId" value="itemSearchInputId"/>
               <div id="autoCompleteContainer.<c:out value=""/>" style="z-index:<c:out value="${autoCompleteZIndex}"/>;position: relative;">
                 <input type="text" name="<c:out value="${searchInputFieldName}"/>" id="<c:out value="${searchInputFieldId}"/>"  style="width:100%" property="lineItem" />
                 <html:hidden property="lineItem" styleId="lineItem" />
                 <div id="itemContainer" style="position: absolute;"></div>
               </div>
               <script type="text/javascript">
                 //<![CDATA[
                 YAHOO.farm.ItemSelectHandler = getItemSelectHandler(<c:out value="${dataSource},'${searchInputFieldId}','${lineCodeFieldId}','${lineCodeDescriptionFieldId}'" escapeXml="false"/>);
                 //]]>
               </script>
              </div>
            </c:when>
            <c:otherwise>
              <html:hidden property="lineItem"/>
              <c:out value="${form.lineItem}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
      <c:if test="${!form.new}">
	      <tr>
	        <th><fmt:message key="Description"/>:</th>
	        <td>
	        <span class="description"></span>
	        	<script type="text/javascript">$('.description').text(description);</script>
	        </td>
	      </tr>
	 </c:if>
      <tr>
        <th style="width:100px"><fmt:message key="Farm.Type.1"/>:</th>
        <td>
        <div style="width:150px";>
          <c:choose>
            <c:when test="${canEdit}">
	            <html:select property="farmSubtypeB">
	            	<option value=""></option>
	 	            <html:optionsCollection property="farmSubtypeBListViewItems" /> 
	            </html:select>
            </c:when>
            <c:otherwise>
              <c:out value="${form.farmSubtypeB}"/>
              <html:hidden property="farmSubtypeB"/>
            </c:otherwise>
          </c:choose>
        </div>
        </td>
      </tr>
      
      <tr>
        <th style="width:100px"><fmt:message key="Operating.Cost"/>:</th>
        <td>
        <div style="width:150px";>
          <html-el:select property="isUsedInOpCost">
            <html-el:option value="true">Y</html-el:option>
            <html-el:option value="false">N</html-el:option>
          </html-el:select>
        </div>
        </td>
      </tr>
      
      <tr>
        <th style="width:100px"><fmt:message key="Direct.Expense"/>:</th>
        <td>
        <div style="width:150px";>
          <html-el:select property="isUsedInDirectExpense">
            <html-el:option value="true">Y</html-el:option>
            <html-el:option value="false">N</html-el:option>
          </html-el:select>
        </div>
        </td>
      </tr>
      
      <tr>
        <th style="width:100px"><fmt:message key="Machinery.Expense"/>:</th>
        <td>
        <div style="width:150px";>
          <html-el:select property="isUsedInMachineryExpense">
            <html-el:option value="true">Y</html-el:option>
            <html-el:option value="false">N</html-el:option>
          </html-el:select>
        </div>
        </td>
      </tr>
      
      <tr>
        <th style="width:100px"><fmt:message key="Land.And.Building.Expense"/>:</th>
        <td>
        <div style="width:150px";>
          <html-el:select property="isUsedInLandAndBuildingExpense">
            <html-el:option value="true">Y</html-el:option>
            <html-el:option value="false">N</html-el:option>
          </html-el:select>
        </div>
        </td>
      </tr>
      
      <tr>
        <th style="width:100px"><fmt:message key="Overhead.Expense"/>:</th>
        <td>
        <div style="width:150px";>
          <html-el:select property="isUsedInOverheadExpense">
            <html-el:option value="true">Y</html-el:option>
            <html-el:option value="false">N</html-el:option>
          </html-el:select>
        </div>
        </td>
      </tr>
      
      <tr>
        <th style="width:100px"><fmt:message key="Program.Payment.for.TIPS"/>:</th>
        <td>
        <div style="width:150px";>
          <html-el:select property="isProgramPaymentForTips">
            <html-el:option value="true">Y</html-el:option>
            <html-el:option value="false">N</html-el:option>
          </html-el:select>
        </div>
        </td>
      </tr>

      <tr>
        <th style="width:100px"><fmt:message key="Convert.to.9896.Other"/>:</th>
        <td>
        <div style="width:150px";>
          <html-el:select property="isOther">
            <html-el:option value="true">Y</html-el:option>
            <html-el:option value="false">N</html-el:option>
          </html-el:select>
        </div>
        </td>
      </tr>

    </table>
  </fieldset>

   <div style="text-align:right;width:70%">
    <w:ifUserCanPerformAction action="editTipCodes">
      <u:yuiButton buttonId="saveButton" buttonLabel="Save" formId="codeForm" />
      <c:if test="${!form.new}">
        <u:yuiButton buttonId="deleteButton" buttonLabel="Delete" formId="codeForm" action="deleteTipLineItem" confirmMessage="delete.tip.line.item.warning" />
      </c:if>
      <u:dirtyFormCheck formId="codeForm"/>
    </w:ifUserCanPerformAction>
    <u:yuiButton buttonId="cancelButton" buttonLabel="Cancel" action="farm545"/>
  </div>

</html:form>