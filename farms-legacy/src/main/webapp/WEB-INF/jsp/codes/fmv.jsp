<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="yui/2.8.2r1/build/animation/animation-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/autocomplete/autocomplete-min.js"></script>
<script type="text/javascript" src="javascript/jquery.min.js"></script>

<c:if test="${form.new}">
  <script type="text/javascript" src="<html:rewrite action="cropLivestockList"/>?year=<c:out value="${form.fmvYear}"/>"></script>

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

  <c:if test="${not empty form.defaultCropUnitCode and form.cropUnitCode ne form.defaultCropUnitCode}">
    <c:set var="saveDisabled" value="true"/>
    <c:set var="nonDefaultUnitMessage" value="FMV values can be edited only for the default unit (${form.defaultCropUnitCodeDescription}) and will be automatically converted to the units configured under the Crop Unit Conversions tab."/>
  </c:if>

  <c:if test="${saveDisabled}">
    <div class="messages">
      <ul>
        <li><c:out value="${nonDefaultUnitMessage}"/></li>
      </ul>
    </div>
  </c:if>
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
  <fmt:message key="FMV"/>
</h1>
<p></p>

<html:form action="saveFMV" styleId="fmvForm" method="post">
  <html:hidden property="new"/>
  <html:hidden property="fmvYear"/>
  <html:hidden property="inventoryItemCode" styleId="inventoryItemCode"/>
  <html:hidden property="cropUnitCode" styleId="cropUnitCode"/>
  <html:hidden property="defaultCropUnitCode" styleId="defaultCropUnitCode"/>

  <c:choose>
    <c:when test="${form.new}">
      <html:hidden property="inventoryType" styleId="inventoryType"/>
    </c:when>
    <c:otherwise>
      <html:hidden property="municipalityCode" styleId="municipalityCode"/>
    </c:otherwise>
  </c:choose>

  <c:forEach varStatus="periodLoop" var="period" items="${form.periods}">
    <input type="hidden" name="<c:out value="fairMarketValueId[${periodLoop.index}]"/>" value="<c:out value="${form.fairMarketValueIds[periodLoop.index]}"/>" />
    <input type="hidden" name="<c:out value="revisionCount[${periodLoop.index}]"/>" value="<c:out value="${form.revisionCounts[periodLoop.index]}"/>" />
  </c:forEach>

  <fieldset style="width:97%">
    <table class="formInput">
      <tr>
        <th><fmt:message key="Program.Year"/>:</th>
        <td>
          <div style="float:left;width:15%">
            <c:out value="${form.fmvYear}"/>
          </div>
        </td>
      </tr>
    </table>

    <table class="formInput" style="width:97%">
      <tr>
        <th><fmt:message key="Inventory.Code"/>:</th>
        <th><fmt:message key="Municipality"/>:</th>
        <th><fmt:message key="Unit"/>:</th>
      </tr>
      <tr>
        <td style="width:300px">
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
        <td style="width:300px">
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
        <td style="width:300px">
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
    </table>

    <table class="numeric" style="width:100%">
      <tr>
        <th><fmt:message key="Period"/></th>
         <c:forEach var="period" items="${form.periods}">
          <th><c:out value="${period}"/></th>
         </c:forEach>
      </tr>
      <tr>
        <th><fmt:message key="Price"/></th>
        <c:forEach varStatus="priceLoop" var="price" items="${form.prices}">
          <c:choose>
            <c:when test="${canEdit}">
              <td style="text-align:center">
                <html-el:text property="price[${priceLoop.index}]" size="8" maxlength="20"/>
              </td>
            </c:when>
            <c:otherwise>
              <td style="text-align:center"><fmt:formatNumber type="number" value="${price}"/></td>
            </c:otherwise>
          </c:choose>
        </c:forEach>
      </tr>
      <tr>
        <th><fmt:message key="Variance"/> (%)</th>
        <c:forEach varStatus="varianceLoop" var="variance" items="${form.percentVariances}">
          <c:choose>
            <c:when test="${canEdit}">
              <td style="text-align:center">
                <html-el:text property="percentVariance[${varianceLoop.index}]" size="8" maxlength="12"/>
              </td>
            </c:when>
            <c:otherwise>
              <td style="text-align:center"><fmt:formatNumber type="number" value="${variance}"/></td>
            </c:otherwise>
          </c:choose>
        </c:forEach>
      </tr>
    </table>
  </fieldset>

  <div style="text-align:right;width:70%">
    <w:ifUserCanPerformAction action="editCodes">
      
      <u:yuiButton buttonId="saveButton" buttonLabel="Save" formId="fmvForm" disabled="${saveDisabled}" />
      <c:if test="${!form.new}">
        <u:yuiButton buttonId="deleteButton" buttonLabel="Delete" formId="fmvForm" action="deleteFMV" confirmMessage="delete.fmv.warning"/>
      </c:if>
  
      <c:if test="${saveDisabled}">
        <u:yuiTooltip targetId="saveButton" messageText="${nonDefaultUnitMessage}" />
      </c:if>
      
      <u:dirtyFormCheck formId="fmvForm"/>
    </w:ifUserCanPerformAction>
    <u:yuiButton buttonId="cancelButton" buttonLabel="Cancel" action="farm286" />
  </div>
  
  
</html:form>
