<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="yui/2.8.2r1/build/animation/animation-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/autocomplete/autocomplete-min.js"></script>
<script type="text/javascript" src="javascript/jquery.min.js"></script>

<c:if test="${form.new}">
  <script type="text/javascript" src="<html:rewrite action="cropLivestockList"/>?year=2018"></script>

  <script>
    function getItemSelectHandler(oDS, itemContainer, searchInputField) {
      return function() {
        // Use a LocalDataSource
        oDS.responseSchema = {fields : ["n", "i", "t", "d"]};
        
        // Instantiate the AutoComplete
        var oAC = new YAHOO.widget.AutoComplete(searchInputField, itemContainer, oDS);
        oAC.resultTypeList = false;
        oAC.forceSelection = true;

        // Define an event handler to populate a hidden form field
        // when an item gets selected
        var selectionHandler = function(sType, aArgs) {
            var myAC = aArgs[0]; // reference back to the AC instance
            var elLI = aArgs[1]; // reference to the selected LI element
            var oData = aArgs[2]; // object literal of selected item's result data
            
            if(oData.t == '2') {
              oData.d = getLivestockUnitCode(oData.i);
            }

            // update hidden form field with the selected item's ID
            $('#inventoryItemCode').val(oData.i);
            $('#inventoryType').val(oData.t);
            if(oData.d == '32') {
              $('#cropUnitCode').val(oData.d);
              $('#cropUnitCodeSelect').val('');
              $('#cropUnitCodeSelect').hide();
              $('#headCropUnitCodeDescription').show();
              $('#defaultCropUnitCode').val('');
            } else if(oData.d) {
              $('#cropUnitCode').val(oData.d);
              $('#cropUnitCodeSelect').val(oData.d);
              $('#cropUnitCodeSelect').prop('disabled', true);
              $('#cropUnitCodeSelect').show();
              $('#headCropUnitCodeDescription').hide();
              $('#defaultCropUnitCode').val(oData.d);
            } else {
              $('#cropUnitCode').val('');
              $('#cropUnitCodeSelect').val('');
              $('#cropUnitCodeSelect').prop('disabled', false);
              $('#cropUnitCodeSelect').show();
              $('#headCropUnitCodeDescription').hide();
              $('#defaultCropUnitCode').val('');
            }
        };
        oAC.itemSelectEvent.subscribe(selectionHandler);

        var clearHandler = function(sType, aArgs) {
            var myAC = aArgs[0]; // reference back to the AC instance
            var clearedValue = aArgs[1]; // The cleared value that did not match anything

            // update hidden form field with the selected item's ID
            $('#inventoryItemCode').val('');
            $('#cropUnitCode').val('');
            $('#cropUnitCodeSelect').val('');
            $('#cropUnitCodeSelect').prop('disabled', false);
            $('#cropUnitCodeSelect').hide();
            $('#headCropUnitCodeDescription').hide();
            $('#defaultCropUnitCode').val('');
            $('#inventoryType').val('');
        };
        oAC.selectionEnforceEvent.subscribe(clearHandler);

        return {
            oDS: oDS,
            oAC: oAC
        };
      }();
    }


    $(document).ready(function() {
      var defaultCropUnit = $("#defaultCropUnitCode").val();
      var cropUnit = $("#cropUnitCode").val();
      if(defaultCropUnit && cropUnit) {
        $("#cropUnitCodeSelect").val(cropUnit);
        $("#cropUnitCodeSelect").prop('disabled', true);
      }
      
      $("#cropUnitCodeSelect").change(function() {
        $("#cropUnitCode").val($(this).val());
      });
      
      if($('#inventoryType').val() == '1') {
        $('#cropUnitCodeSelect').show();
      } else if($('#inventoryType').val() == '2') {
        if(cropUnit == '32') {
          $('#headCropUnitCodeDescription').show();
        } else {
          $('#cropUnitCodeSelect').show();
          $('#cropUnitCodeSelect').val(cropUnit);
          $('#cropUnitCodeSelect').prop('disabled', true);
        }
      }

    });

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
  <fmt:message key="Expected.Production"/>
</h1>


<html:form action="/saveExpectedProductionItem" styleId="codeForm" method="post">
  <html:hidden property="new"/>
  <html:hidden property="expectedProductionId"/>
  <html:hidden property="inventoryItemCode" styleId="inventoryItemCode"/>
  <html:hidden property="cropUnitCode" styleId="cropUnitCode"/>
  <html:hidden property="cropUnitCodeDescription" />
  <html:hidden property="inventoryItemCodeDescription" />

  <c:choose>
    <c:when test="${form.new}">
      <html:hidden property="inventoryType" styleId="inventoryType"/>
    </c:when>
    <c:otherwise>
    </c:otherwise>
  </c:choose>
    
  <fieldset style="width:70%">
    <table class="formInput" style="width:100%">
      <tr>
        <th style="width:100px"><fmt:message key="Inventory.Code"/>:</th>
        <td>
          <c:choose>
            <c:when test="${form.new}">
              <div id="autoInvContainer">
                <html:text property="inventorySearchInput" styleId="inventorySearchInput" style="width:80%" />
                <div id="invContainer"></div>
              </div>
              <script type="text/javascript">
                //<![CDATA[
                YAHOO.farm.ItemSelectHandler = getItemSelectHandler(new YAHOO.util.LocalDataSource(YAHOO.farm.Data.inventoryItems), 'invContainer', 'inventorySearchInput');
                //]]>
              </script>            
            </c:when>
            <c:otherwise>
              <c:out value="${form.inventoryItemCode} - ${form.inventoryItemCodeDescription}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
      <tr>
        <th style="width:100px"><fmt:message key="Crop.Unit"/>:</th>
        <td>
          <c:choose>
            <c:when test="${form.new}">
              <html:select property="cropUnitCodeSelect" styleId="cropUnitCodeSelect" style="width:80%; display:none">
                <html:option value=""/>
                <html:optionsCollection name="server.list.crop.units"/>
              </html:select>
              <div id="headCropUnitCodeDescription" style="display:none">Head (Livestock)</div>            
            </c:when>
            <c:otherwise>
              <c:out value="${form.cropUnitCodeDescription}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
      <tr>
        <th style="width:100px"><fmt:message key="Expected.Production"/>:</th>
        <td>
          <c:choose>
            <c:when test="${canEdit or form.new}">
              <div style="float:left;width:100px">
                <html:text property="expectedProductionPerAcre" size="10" maxlength="10"/>
              </div>
            </c:when>
            <c:otherwise>
              <html:hidden property="expectedProductionPerAcre"/>
              <c:out value="${form.expectedProductionPerAcre}"/>
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
        <u:yuiButton buttonId="deleteButton" buttonLabel="Delete" formId="codeForm" action="deleteExpectedProductionItem" confirmMessage="delete.expected.production.item.warning"/>
      </c:if>
      <u:dirtyFormCheck formId="codeForm"/>
    </w:ifUserCanPerformAction>
    <u:yuiButton buttonId="cancelButton" buttonLabel="Cancel" action="farm740"/>
  </div>

</html:form>