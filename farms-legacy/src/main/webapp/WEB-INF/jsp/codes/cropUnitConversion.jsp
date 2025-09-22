<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="yui/2.8.2r1/build/animation/animation-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/autocomplete/autocomplete-min.js"></script>
<script type="text/javascript" src="javascript/jquery.min.js"></script>
<script type="text/javascript" src="javascript/vue.min.js"></script>

<c:if test="${form.new}">

  <!-- Hard coded a year here because we need one here but which one is irrelevant as long as the data exists for that year. -->
  <script src="<html:rewrite action="cropList"/>?year=2021"></script>

  <script>
    function getItemSelectHandler(oDS, itemContainer, searchInputField) {
      return function() {
        // Use a LocalDataSource
        oDS.responseSchema = {fields : ["n", "i", "d"]};
        
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

            // update hidden form field with the selected item's ID
            $('#inventoryItemCode').val(oData.i);
            vm.$data.cropUnitConversion.inventoryItemCode = oData.i;
            vm.$data.cropUnitConversion.inventoryItemCodeDescription = oData.d;
        };
        oAC.itemSelectEvent.subscribe(selectionHandler);

        var clearHandler = function(sType, aArgs) {
            var myAC = aArgs[0]; // reference back to the AC instance
            var clearedValue = aArgs[1]; // The cleared value that did not match anything

            // update hidden form field with the selected item's ID
            $('#inventoryItemCode').val('');
            vm.$data.cropUnitConversion.inventoryItemCode = '';
            vm.$data.cropUnitConversion.inventoryItemCodeDescription = '';
        };
        oAC.selectionEnforceEvent.subscribe(clearHandler);

        return {
            oDS: oDS,
            oAC: oAC
        };
      }();
    }

  </script>
</c:if>

<script>

  $(document).ready(function() {
    
    $('#defaultCropUnitCode').change(function() {
      vm.$data.cropUnitConversion.defaultCropUnitCode = $(this).val();
    });
    
  });
    
</script>

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
  <fmt:message key="Crop.Unit.Conversion"/>
</h1>
<p></p>


<html:form action="saveCropUnitConversion" styleId="cropUnitConversionForm" method="post">

    <fieldset style="width:97%">
      <table class="formInput" style="width:97%">
        <tr>
          <th><fmt:message key="Inventory.Code"/>:</th>
          <th><fmt:message key="Default.Crop.Unit"/>:</th>
        </tr>
        <tr>
          <td style="width:300px">
            <c:choose>
              <c:when test="${form.new}">
                <div id="autoInvContainer">
                  <html:text property="inventorySearchInput" styleId="inventorySearchInput" style="width:80%" />
                  <div id="invContainer"></div>
                </div>
              </c:when>
              <c:otherwise>
                <c:out value="${form.inventoryItemCode} - ${form.inventoryItemCodeDescription}"/>
              </c:otherwise>
            </c:choose>
          </td>
          <td style="width:300px">
            <c:choose>
              <c:when test="${canEdit}">
                <html:select property="defaultCropUnitCode" styleId="defaultCropUnitCode" style="width:80%">
                  <html:option value=""/>
                  <html:optionsCollection name="server.list.crop.units"/>
                </html:select>
              </c:when>
              <c:otherwise>
                <c:out value="${form.defaultCropUnitCodeDescription}"/>
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
      </table>
    </fieldset>

  <div id="vueApp">

    <html:hidden property="new"/>
    <html:hidden property="inventoryItemCode" styleId="inventoryItemCode"/>
    <html:hidden property="inventoryItemCodeDescription" styleId="inventoryItemCodeDescription"/>
    <html:hidden property="defaultCropUnitCode" styleId="defaultCropUnitCode"/>
    <html:hidden property="cropUnitConversionJson" styleId="cropUnitConversionJson"/>
    <html:hidden property="cropUnitSelectOptionsJson" styleId="cropUnitSelectOptionsJson"/>
    <input type="hidden" name="cropUnitConversionJsonReactive" id="cropUnitConversionJsonReactive" v-model="JSON.stringify(cropUnitConversion)" />
  
    <fieldset>
      <legend><fmt:message key="Conversion.Factors"/></legend>
      <div class="rTable">
        <div class="rTableRow">
          <div class="rTableHead"><fmt:message key="Crop.Unit"/></div>
          <div class="rTableHead conversionFactor"><fmt:message key="Conversion.Factor"/></div>
          <div class="rTableHead"></div>
        </div>
        <conversion-item
          v-for="(conversionItem, index) in cropUnitConversion.conversionItems"
          v-bind:item="conversionItem"
          v-bind:index="index"
          v-bind:key="conversionItem.id"
          v-bind:select-options="cropUnitOptions"
          v-bind:can-edit="canEdit"
          v-on:remove="cropUnitConversion.conversionItems.splice(index, 1)">
        </conversion-item>
      </div>
      <div v-if="canEdit" v-cloak style="padding: 10px 20px">
        <span class="yui-button yui-push-button">
          <span class="first-child">
            <button type="button" v-on:click='addConversion()'>Add Conversion</button>
          </span>
        </span>
      </div>
    </fieldset>

  </div>
  
  <p>Multiply the value in <span class="instructionTextDefaultUnit"><c:out value="${form.defaultCropUnitCodeDescription}"/></span> by the conversion factor to calculate the value for that unit.</p> 
  <p>Divide the value in a particluar unit by the conversion factor to calculate the value in <span class="instructionTextDefaultUnit"><c:out value="${form.defaultCropUnitCodeDescription}"/></span>.</p> 

  <div style="text-align:right;width:70%">
    <w:ifUserCanPerformAction action="editCodes">
    
      <script>
        function saveCropUnitConversion() {
          $('#cropUnitConversionJson').val( $('#cropUnitConversionJsonReactive').val() );
          submitForm(document.getElementById('cropUnitConversionForm'));
        }
      </script>
    
      <u:yuiButton buttonId="saveButton" buttonLabel="Save" function="saveCropUnitConversion" />
      <c:if test="${!form.new}">
        <u:yuiButton buttonId="deleteButton" buttonLabel="Delete" formId="cropUnitConversionForm" action="deleteCropUnitConversion" confirmMessage="delete.crop.unit.conversion.warning"/>
      </c:if>
  
      <u:dirtyFormCheck formId="cropUnitConversionForm"/>
    </w:ifUserCanPerformAction>
    <u:yuiButton buttonId="cancelButton" buttonLabel="Cancel" action="farm288" />
  </div>

</html:form>

<c:if test="${form.new}">
  <script>
    YAHOO.farm.ItemSelectHandler = getItemSelectHandler(new YAHOO.util.LocalDataSource(YAHOO.farm.Data.inventoryItems), 'invContainer', 'inventorySearchInput');
  </script>
</c:if>

<script>
  $(function(){
	  $('#defaultCropUnitCode').change(function(){
	    $('.instructionTextDefaultUnit').text($( '#defaultCropUnitCode option:selected' ).text());
	  });
	});

  var randomInteger = function () {
    return Math.floor( Math.random() * Math.pow(10, 17) );
  };

  Vue.component('conversion-item', {
    props: ['item','selectOptions','canEdit'],
    template: '\
      <div class="rTableRow numeric alignBottom">\
        <div class="rTableCell">\
          <select v-if="canEdit" v-model="item.targetCropUnitCode">\
            <option value=""></option>\
            <option v-for="option in selectOptions" :value="option.value" v-bind:key="option.id">{{ option.label }}</option>\
          </select>\
          <span v-else>\
            {{item.targetCropUnitCode}} - {{item.targetCropUnitCodeDescription}}\
          </span>\
        </div>\
        <div class="rTableCell alignRight">\
          <input v-if="canEdit" v-model="item.conversionFactor" />\
          <span v-else>{{item.conversionFactor}}</span>\
        </div>\
        <div class="rTableCell alignBottom">\
          <span class="yui-button yui-push-button">\
            <span class="first-child">\
              <button type="button" v-if="canEdit" v-on:click="$emit(\'remove\')">Remove</button>\
            </span>\
          </span>\
        </div>\
      </div>'
  });
  
  var vueData = {
        cropUnitConversion: JSON.parse($('#cropUnitConversionJson').val()),
        cropUnitOptions: JSON.parse($('#cropUnitSelectOptionsJson').val()),
        canEdit: "<c:out value='${canEdit}' />"
      };
  
  for(var i = 0; i < vueData.cropUnitConversion.conversionItems.length; i++) {
    vueData.cropUnitConversion.conversionItems[i].id = randomInteger();
  }
  
  for(var i = 0; i < vueData.cropUnitOptions.length; i++) {
    vueData.cropUnitOptions[i].id = i;
  }
  
  var vm = new Vue({
    el: '#vueApp',
    data: vueData,
    methods: {
      randomInteger: randomInteger,
      addConversion: function (){
        this.cropUnitConversion.conversionItems.push({
          targetCropUnitCode: "",
          targetCropUnitCodeDescription: "",
          conversionFactor: "",
          id: randomInteger()});
      }
    },
    mounted: function (){
      excludeFieldFromDirtyCheck(''); // Vue.js creates fields without IDs 
    },
    updated: function (){
      markFormAsDirty();
    }
  });
  
</script>
