<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp" %>

<script type="text/javascript">
//<![CDATA[
  var growingForward2013 = ("<c:out value="${form.growingForward2013}"/>" == "true") ? true : false;
  
  function goToUrlPreserveContext(baseUrl) {
  var url = baseUrl;
  <c:if test="${form.numLineItems > 0}">
      var codeFilter = escape(document.getElementById('codeFilter').value);
      var descriptionFilter = escape(document.getElementById('descriptionFilter').value);
      var sectorFilterSelect = document.getElementById('sectorFilter');
      var fruitVegCodeFilterSelect = document.getElementById('fruitVegCodeFilter');
      var isEligibleFilterSelect = document.getElementById('isEligibleFilter');
      var isYardageFilterSelect = document.getElementById('isYardageFilter');
      var isProgramPaymentFilterSelect = document.getElementById('isProgramPaymentFilter');
      var isContractWorkFilterSelect = document.getElementById('isContractWorkFilter');
      var isSupplyManagedCommodityFilterSelect = document.getElementById('isSupplyManagedCommodityFilter');
      var isIndustryAverageExpenseFilterSelect = document.getElementById('isIndustryAverageExpenseFilter');
      var sectorFilter = escape(sectorFilterSelect.options[sectorFilterSelect.selectedIndex].value);
      var isEligibleFilter = isEligibleFilterSelect.options[isEligibleFilterSelect.selectedIndex].value;
      var isYardageFilter = isYardageFilterSelect.options[isYardageFilterSelect.selectedIndex].value;
      var isProgramPaymentFilter = isProgramPaymentFilterSelect.options[isProgramPaymentFilterSelect.selectedIndex].value;
      var isContractWorkFilter = isContractWorkFilterSelect.options[isContractWorkFilterSelect.selectedIndex].value;
      var isSupplyManagedCommodityFilter = isSupplyManagedCommodityFilterSelect.options[isSupplyManagedCommodityFilterSelect.selectedIndex].value;
      var isIndustryAverageExpenseFilter = isIndustryAverageExpenseFilterSelect.options[isIndustryAverageExpenseFilterSelect.selectedIndex].value;
      var fruitVegCodeFilter = escape(fruitVegCodeFilterSelect.options[fruitVegCodeFilterSelect.selectedIndex].value);

      url = url + "&codeFilter=" + codeFilter + "&descriptionFilter=" + descriptionFilter + "&sectorFilter=" + sectorFilter + "&isEligibleFilter=" + isEligibleFilter + "&isYardageFilter=" + isYardageFilter + "&isProgramPaymentFilter=" + isProgramPaymentFilter + "&isContractWorkFilter=" + isContractWorkFilter + "&isSupplyManagedCommodityFilter=" + isSupplyManagedCommodityFilter + "&isIndustryAverageExpenseFilter=" + isIndustryAverageExpenseFilter + "&fruitVegCodeFilter=" + fruitVegCodeFilter;

      if (growingForward2013) { 
        var isEligibleRefYearsFilterSelect = document.getElementById('isEligibleRefYearsFilter');
        var isExcludeFromRevenueCalculationFilterSelect = document.getElementById('isExcludeFromRevenueCalculationFilter');
        var isEligibleRefYearsFilter = isEligibleRefYearsFilterSelect.options[isEligibleRefYearsFilterSelect.selectedIndex].value;
        var isExcludeFromRevenueCalculationFilter = isExcludeFromRevenueCalculationFilterSelect.options[isExcludeFromRevenueCalculationFilterSelect.selectedIndex].value;
        url = url + "&isEligibleRefYearsFilter=" + isEligibleRefYearsFilter + "&isExcludeFromRevenueCalculationFilter=" + isExcludeFromRevenueCalculationFilter;
      }
    </c:if>
    document.location.href = url;
  }
//]]>
</script>

<span style="float:left;">
  <w:ifUserCanPerformAction action="editCodes">

    <button type="button" id="newButton"><fmt:message key="New.Line.Item"/></button>
    <button type="button" id="copyYearButton"><fmt:message key="Copy.From"/> <c:out value="${form.yearFilter - 1}"/> <fmt:message key="To"/> <c:out value="${form.yearFilter}"/></button>

    <c:choose>
      <c:when test="${form.numLineItems == 0}">
        <script type="text/javascript">
        //<![CDATA[
          function copyYearItem() {
            goToUrlPreserveContext('<html:rewrite action="copyYearLineItems" paramName="form" paramProperty="yearFilter" paramId="yearFilter"/>')
          }
          var newButton = new YAHOO.widget.Button("newButton", {disabled: true});
          var copyYearButton = new YAHOO.widget.Button("copyYearButton", {onclick: {fn: copyYearItem}});
        //]]>
        </script>
      </c:when>
      <c:otherwise>
        <script type="text/javascript">
        //<![CDATA[
          function newLineItem() {
            goToUrlPreserveContext('<html:rewrite action="newLineItem" paramName="form" paramProperty="yearFilter" paramId="yearFilter"/>');
          }
          var newButton = new YAHOO.widget.Button("newButton", {onclick: {fn: newLineItem}});
          var copyYearButton = new YAHOO.widget.Button("copyYearButton", {disabled: true});
        //]]>
        </script>
      </c:otherwise>
    </c:choose>

  </w:ifUserCanPerformAction>
</span>

<span style="float:right;">
 <u:menuSelect action="farm280.do"
      name="yearFilterPicker"
      paramName="yearFilter"
      additionalFieldIds="codeFilter,descriptionFilter,sectorFilter,fruitVegCodeFilter,isEligibleFilter,isEligibleRefYearsFilter,isYardageFilter,isProgramPaymentFilter,isContractWorkFilter,isSupplyManagedCommodityFilter,isExcludeFromRevenueCalculationFilter,isIndustryAverageExpenseFilter, isGrainFilter"
      options="${form.programYearSelectOptions}"
      selectedValue="${form.yearFilter}"
      toolTip="Click here to open a different Program Year."/>
</span>



<c:if test="${form.numLineItems == 0}">
  <p><fmt:message key="There.are.no.Line.Items.for"/> <c:out value="${form.yearFilter}"/>.</p>
  <html:form action="farm280">
    <html:hidden property="codeFilter" styleId="codeFilter"/>
    <html:hidden property="descriptionFilter" styleId="descriptionFilter"/>
    <html:hidden property="sectorFilter" styleId="sectorFilter"/>
    <html:hidden property="isEligibleFilter" styleId="isEligibleFilter"/>
    <html:hidden property="isEligibleRefYearsFilter" styleId="isEligibleRefYearsFilter"/>
    <html:hidden property="isYardageFilter" styleId="isYardageFilter"/>
    <html:hidden property="isProgramPaymentFilter" styleId="isProgramPaymentFilter"/>
    <html:hidden property="isContractWorkFilter" styleId="isContractWorkFilter"/>
    <html:hidden property="isSupplyManagedCommodityFilter" styleId="isSupplyManagedCommodityFilter"/>
    <html:hidden property="isExcludeFromRevenueCalculationFilter" styleId="isExcludeFromRevenueCalculationFilter"/>
    <html:hidden property="isIndustryAverageExpenseFilter" styleId="isIndustryAverageExpenseFilter"/>
    <html:hidden property="fruitVegCodeFilter" styleId="fruitVegCodeFilter"/>
    
  </html:form>
</c:if>

<html:form action="farm280">
  <fieldset style="margin:0px 325px 10px 325px; width:180px;">
    <table>
      <tr>
        <td style="padding-right:15px;">
          <html:radio property="filterSelection" styleId="regularFilter" value="R"/><fmt:message key="Main"/>
        </td>
        <td>
          <html:radio property="filterSelection" styleId="otherFilter" value="O"/><fmt:message key="Other.Farm.Info"/>
        </td>
      </tr>
    </table>
  </fieldset>
</html:form>

<c:if test="${form.numLineItems > 0}">

  <div class="searchresults">
    <div id="searchresults"></div>
  </div>

  <script type="text/javascript">
  //<![CDATA[
    Farm.codes = {
        "recordsReturned": <c:out value="${form.numLineItems}"/>,
        "totalRecords": <c:out value="${form.numLineItems}"/>,
        "startIndex":0,
        "sort":null,
        "dir":"asc",
        "pageSize": 10,
        "records":[
          <c:forEach varStatus="loop" var="result" items="${form.lineItems}">
            {
              "lineItemId":"<c:out value="${result.lineItemId}"/>",  
              "code":"<c:out value="${result.lineItem}"/>",  
              "sector":"<c:out value="${result.sectorCodeDescription}"/>",  
              "description":"<c:out value="${result.description}"/>",  
              "isEligible":"<c:choose><c:when test="${result.isEligible}">Y</c:when><c:otherwise>N</c:otherwise></c:choose>",
              "isEligibleRefYears":"<c:choose><c:when test="${result.isEligibleRefYears}">Y</c:when><c:otherwise>N</c:otherwise></c:choose>",
              "isYardage":"<c:choose><c:when test="${result.isYardage}">Y</c:when><c:otherwise>N</c:otherwise></c:choose>",  
              "isProgramPayment":"<c:choose><c:when test="${result.isProgramPayment}">Y</c:when><c:otherwise>N</c:otherwise></c:choose>",  
              "isContractWork":"<c:choose><c:when test="${result.isContractWork}">Y</c:when><c:otherwise>N</c:otherwise></c:choose>",  
              "isSupplyManagedCommodity":"<c:choose><c:when test="${result.isSupplyManagedCommodity}">Y</c:when><c:otherwise>N</c:otherwise></c:choose>",
              "isExcludeFromRevenueCalculation":"<c:choose><c:when test="${result.isExcludeFromRevenueCalculation}">Y</c:when><c:otherwise>N</c:otherwise></c:choose>",
              "isIndustryAverageExpense":"<c:choose><c:when test="${result.isIndustryAverageExpense}">Y</c:when><c:otherwise>N</c:otherwise></c:choose>",
              "fruitVegCodeDescription":"<c:out value="${result.fruitVegCodeDescription}"/>",
            }<c:if test="${loop.index < (form.numLineItems-1)}">,</c:if>
          </c:forEach> 
        ]
    };
    
    YAHOO.util.Event.onDOMReady(function() {

      var eligibleYrLabel = "<fmt:message key="Eligible"/>";
      if (growingForward2013) {
        eligibleYrLabel = "<fmt:message key="Eligible.Program.Year.multiline"/>";
      }

      Farm.codesColumnDefs = [
           {key:"code", label:"<fmt:message key="Code"/><br /><input name='codeFilter' type='text' id='codeFilter' value='<c:out value="${form.codeFilter}"/>' maxlength='10' style='width:40px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
           {key:"description", label:"<fmt:message key="Description"/><br /><input name='descriptionFilter' type='text' id='descriptionFilter' value='<c:out value="${form.descriptionFilter}"/>' maxlength='256' style='width:225px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
           {key:"sector", label:"<fmt:message key="Farm.Type"/><br /><select name='sectorFilter' id='sectorFilter' style='font-size:10px;'><option value=''></option><c:forEach items="${form.sectors}" var="sector"><option value='<c:out value="${sector.label}"/>' <c:if test="${sector.label == form.sectorFilter}">selected='selected'</c:if>><c:out value="${sector.label}"/></option></c:forEach></select>", sortable:false, className:"tdCenter"},
           {key:"isEligible", label:"<span id='eligibleLabel'>"+eligibleYrLabel+"</span><br /><select name='isEligibleFilter' id='isEligibleFilter' style='font-size:10px;'><option value=''></option><option value='Y' <c:if test="${form.isEligibleFilter=='Y'}">selected='selected'</c:if>>Y</option><option value='N' <c:if test="${form.isEligibleFilter=='N'}">selected='selected'</c:if> >N</option></select>", sortable:false, className:"tdCenter"},
           {key:"isEligibleRefYears", label:"<span id='isEligibleRefYearsLabel'><fmt:message key="Eligible.Reference.Years.multiline"/></span><br /><select name='isEligibleRefYearsFilter' id='isEligibleRefYearsFilter' style='font-size:10px;'><option value=''></option><option value='Y' <c:if test="${form.isEligibleRefYearsFilter=='Y'}">selected='selected'</c:if>>Y</option><option value='N' <c:if test="${form.isEligibleRefYearsFilter=='N'}">selected='selected'</c:if> >N</option></select>", sortable:false, className:"tdCenter"},
           {key:"isYardage", label:"<fmt:message key="Yardage"/><br /><select name='isYardageFilter' id='isYardageFilter' style='font-size:10px;'><option value=''></option><option value='Y' <c:if test="${form.isYardageFilter=='Y'}">selected='selected'</c:if>>Y</option><option value='N' <c:if test="${form.isYardageFilter=='N'}">selected='selected'</c:if> >N</option></select>", sortable:false, className:"tdCenter"},
           {key:"isProgramPayment", label:"<fmt:message key="Program.Payment.multiline"/><br /><select name='isProgramPaymentFilter' id='isProgramPaymentFilter' style='font-size:10px;'><option value=''></option><option value='Y' <c:if test="${form.isProgramPaymentFilter=='Y'}">selected='selected'</c:if>>Y</option><option value='N' <c:if test="${form.isProgramPaymentFilter=='N'}">selected='selected'</c:if> >N</option></select>", sortable:false, className:"tdCenter"},
           {key:"isContractWork", label:"<fmt:message key="Contract.Work.multiline"/><br /><select name='isContractWorkFilter' id='isContractWorkFilter' style='font-size:10px;'><option value=''></option><option value='Y' <c:if test="${form.isContractWorkFilter=='Y'}">selected='selected'</c:if>>Y</option><option value='N' <c:if test="${form.isContractWorkFilter=='N'}">selected='selected'</c:if> >N</option></select>", sortable:false, className:"tdCenter"},
           {key:"isSupplyManagedCommodity", label:"<fmt:message key="Supply.Managed.Commodity.multiline"/><br /><select name='isSupplyManagedCommodityFilter' id='isSupplyManagedCommodityFilter' style='font-size:10px;'><option value=''></option><option value='Y' <c:if test="${form.isSupplyManagedCommodityFilter=='Y'}">selected='selected'</c:if>>Y</option><option value='N' <c:if test="${form.isSupplyManagedCommodityFilter=='N'}">selected='selected'</c:if> >N</option></select>", sortable:false, className:"tdCenter"},
           {key:"isExcludeFromRevenueCalculation", label:"<fmt:message key="Exclude.From.Revenue.Calculation.multiline"/><br /><select name='isExcludeFromRevenueCalculationFilter' id='isExcludeFromRevenueCalculationFilter' style='font-size:10px;'><option value=''></option><option value='Y' <c:if test="${form.isExcludeFromRevenueCalculationFilter=='Y'}">selected='selected'</c:if>>Y</option><option value='N' <c:if test="${form.isExcludeFromRevenueCalculationFilter=='N'}">selected='selected'</c:if> >N</option></select>", sortable:false, className:"tdCenter"},
           {key:"isIndustryAverageExpense", label:"<fmt:message key="Industry.Average.Expense.multiline"/><br /><select name='isIndustryAverageExpenseFilter' id='isIndustryAverageExpenseFilter' style='font-size:10px;'><option value=''></option><option value='Y' <c:if test="${form.isIndustryAverageExpenseFilter=='Y'}">selected='selected'</c:if>>Y</option><option value='N' <c:if test="${form.isIndustryAverageExpenseFilter=='N'}">selected='selected'</c:if> >N</option></select>", sortable:false, className:"tdCenter"},
           {key:"fruitVegCodeDescription", label:"<fmt:message key="Fruit.Veg.Type"/><br /><select name='fruitVegCodeFilter' id='fruitVegCodeFilter' style='font-size:10px;'><option value=''></option><c:forEach items="${form.fruitVegListViewItems}" var="item"><option value='<c:out value="${item.label}"/>' <c:if test="${item.label == form.fruitVegCodeFilter}">selected='selected'</c:if>><c:out value="${item.label}"/></option></c:forEach></select>", sortable:false, className:"tdCenter"},
           ];

      Farm.filterIndicator = function(checkboxFilter, indicator) {
        var result = false;
        if(checkboxFilter.checked) {
          if(indicator == 'Y') {
            result = true;
          }
        } else {
          if(indicator == 'N') {
            result = true;
          }
        }
          return result;      
      };

      Farm.filterSelect = function(selectFilter, value) {
          var result = false;
          var selectedValue = selectFilter.options[selectFilter.selectedIndex].value;
          if(selectedValue == '') {
            result = true;
          } else {
            if(selectedValue == value) {
              result = true;
            }
          }
          return result;      
      };

      Farm.filterCode = function(code) {
        var codeFilter = YAHOO.util.Dom.get('codeFilter');
        if(codeFilter.value) {
          if(code.indexOf(codeFilter.value) == 0) {
            return true;
          } else {
            return false;
          }
        }
        return true;      
      };

      Farm.filterDescription = function(description) {
        var descriptionFilter = YAHOO.util.Dom.get('descriptionFilter');
        if(descriptionFilter.value) {
          if(description.toLowerCase().indexOf(descriptionFilter.value.toLowerCase()) >= 0) {
            return true;
          } else {
            return false;
          }
        }
        return true;      
      };

      Farm.filterSector = function(value) {
        var selectFilter = YAHOO.util.Dom.get('sectorFilter');
        return Farm.filterSelect(selectFilter, value);
      };
      
      Farm.filterFruitVegCode = function(value) {
          var selectFilter = YAHOO.util.Dom.get('fruitVegCodeFilter');
          return Farm.filterSelect(selectFilter, value);
        };      

      Farm.filterIsEligible = function(value) {
        var selectFilter = YAHOO.util.Dom.get('isEligibleFilter');
        return Farm.filterSelect(selectFilter, value);
      };

      if (growingForward2013) {
        Farm.filterIsEligibleRefYears = function(value) {
          var selectFilter = YAHOO.util.Dom.get('isEligibleRefYearsFilter');
          return Farm.filterSelect(selectFilter, value);
        };
        Farm.filterIsExcludeFromRevenueCalculation = function(value) {
          var selectFilter = YAHOO.util.Dom.get('isExcludeFromRevenueCalculationFilter');
          return Farm.filterSelect(selectFilter, value);
        };
      }
      
      Farm.filterIsYardage = function(value) {
        var selectFilter = YAHOO.util.Dom.get('isYardageFilter');
        return Farm.filterSelect(selectFilter, value);
      };        

      Farm.filterIsProgramPayment = function(value) {
        var selectFilter = YAHOO.util.Dom.get('isProgramPaymentFilter');
        return Farm.filterSelect(selectFilter, value);
      };

      Farm.filterIsContractWork = function(value) {
        var selectFilter = YAHOO.util.Dom.get('isContractWorkFilter');
        return Farm.filterSelect(selectFilter, value);
      };

      Farm.filterIsSupplyManagedCommodity = function(value) {
        var selectFilter = YAHOO.util.Dom.get('isSupplyManagedCommodityFilter');
        return Farm.filterSelect(selectFilter, value);
      };

      Farm.filterIsIndustryAverageExpense = function(value) {
        var selectFilter = YAHOO.util.Dom.get('isIndustryAverageExpenseFilter');
        return Farm.filterSelect(selectFilter, value);
      };

      Farm.codesDataSource = new YAHOO.util.DataSource(Farm.codes,{
        doBeforeCallback : function (req,raw,res,cb) {
          // This is the filter function 
  
          var data     = res.results || [], 
              filtered = [], 
              i,l; 
  
          for (i = 0, l = data.length; i < l; ++i) {
            var matched = false;
            if(Farm.filterCode(data[i].code)
               && Farm.filterDescription(data[i].description)
               && Farm.filterSector(data[i].sector)
               && Farm.filterIsEligible(data[i].isEligible)
               && Farm.filterIsYardage(data[i].isYardage)
               && Farm.filterIsProgramPayment(data[i].isProgramPayment)
               && Farm.filterIsContractWork(data[i].isContractWork)
               && Farm.filterIsSupplyManagedCommodity(data[i].isSupplyManagedCommodity)
               && Farm.filterIsIndustryAverageExpense(data[i].isIndustryAverageExpense)
               && Farm.filterFruitVegCode(data[i].fruitVegCodeDescription)) {

                // do not process filterIsEligibleRefYears data if Program Year is before 2013
              if (!growingForward2013) {
                matched = true;
              } else if(Farm.filterIsEligibleRefYears(data[i].isEligibleRefYears)
                    && Farm.filterIsExcludeFromRevenueCalculation(data[i].isExcludeFromRevenueCalculation)) {
                matched = true;
              }
            }
            if(matched) {
              filtered.push(data[i]);
            }
          }
  
          res.results = filtered; 
          return res; 
        }
      });

      Farm.codesDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
      Farm.codesDataSource.responseSchema = {
        resultsList: "records",
        fields: ["lineItemId", "code","description","sector","isEligible","isEligibleRefYears","isYardage", "isProgramPayment", "isContractWork", "isSupplyManagedCommodity", "isExcludeFromRevenueCalculation", "isIndustryAverageExpense", "fruitVegCodeDescription"]
      };

      Farm.codesConfigs = {
        paginator: new YAHOO.widget.Paginator({ rowsPerPage: 20 })
      };

      Farm.showSelectedRow = function(oArgs) {
        var oRecord = oArgs.record;
        var lineItem = oRecord.getData("code");
        var farmAction = "farm285";  // edit page
        
        showProcessing();
        goToUrlPreserveContext(farmAction + ".do?yearFilter=<c:out value="${form.yearFilter}"/>&lineItem=" + lineItem);
      };

      Farm.codesDataTable = new YAHOO.widget.DataTable("searchresults", Farm.codesColumnDefs, Farm.codesDataSource, Farm.codesConfigs);
      Farm.codesDataTable.subscribe("rowMouseoverEvent", Farm.codesDataTable.onEventHighlightRow);
      Farm.codesDataTable.subscribe("rowMouseoutEvent", Farm.codesDataTable.onEventUnhighlightRow);
      Farm.codesDataTable.subscribe("rowClickEvent", Farm.codesDataTable.onEventSelectRow);
      Farm.codesDataTable.subscribe("rowSelectEvent", Farm.showSelectedRow);
      
      Farm.codesDataTable.subscribe('renderEvent', function(oArgs) {
          if(YAHOO.util.Dom.get('otherFilter').checked ) {
            Farm.codesDataTable.hideColumn(1);
            Farm.codesDataTable.hideColumn(2);
            Farm.codesDataTable.hideColumn(3);
            Farm.codesDataTable.hideColumn(4);
            Farm.codesDataTable.hideColumn(5);
            Farm.codesDataTable.hideColumn(6);
            Farm.codesDataTable.hideColumn(7);
            Farm.codesDataTable.hideColumn(8);
            Farm.codesDataTable.hideColumn(9);
            Farm.codesDataTable.hideColumn(10);
            Farm.codesDataTable.showColumn(11);
            Farm.codesDataTable.showColumn(12);
            Farm.codesDataTable.showColumn(13);
            Farm.codesDataTable.showColumn(14);
          } else {
            Farm.codesDataTable.showColumn(1);
            Farm.codesDataTable.showColumn(2);
            Farm.codesDataTable.showColumn(3);
            Farm.codesDataTable.showColumn(4);
            Farm.codesDataTable.showColumn(5);
            Farm.codesDataTable.showColumn(6);
            Farm.codesDataTable.showColumn(7);
            Farm.codesDataTable.showColumn(8);
            Farm.codesDataTable.showColumn(9);
            Farm.codesDataTable.showColumn(10);
            Farm.codesDataTable.hideColumn(11);
            Farm.codesDataTable.hideColumn(12);
            Farm.codesDataTable.hideColumn(13);
            Farm.codesDataTable.hideColumn(14);            
          }
        });      

      
      Farm.updateFilter = function ()  {
          // Reset sort 
          var state = YAHOO.farm.codesDataTable.getState(); 
  
          // Get filtered data 
          Farm.codesDataSource.sendRequest(null,{ 
              success : Farm.codesDataTable.onDataReturnInitializeTable, 
              failure : Farm.codesDataTable.onDataReturnInitializeTable, 
              scope   : Farm.codesDataTable,
              argument: state 
          });   
      };

      YAHOO.util.Event.on('codeFilter','keyup',Farm.updateFilter);
      YAHOO.util.Event.on('descriptionFilter','keyup',Farm.updateFilter);
      YAHOO.util.Event.on('sectorFilter','change',Farm.updateFilter);
      YAHOO.util.Event.on('fruitVegCodeFilter','change',Farm.updateFilter);
      YAHOO.util.Event.on('isEligibleFilter','change',Farm.updateFilter);
      if(growingForward2013) { 
        YAHOO.util.Event.on('isEligibleRefYearsFilter','change',Farm.updateFilter);
        YAHOO.util.Event.on('isExcludeFromRevenueCalculationFilter','change',Farm.updateFilter);
      }
      YAHOO.util.Event.on('isYardageFilter','change',Farm.updateFilter);
      YAHOO.util.Event.on('isProgramPaymentFilter','change',Farm.updateFilter);
      YAHOO.util.Event.on('isContractWorkFilter','change',Farm.updateFilter);
      YAHOO.util.Event.on('isSupplyManagedCommodityFilter','change',Farm.updateFilter);
      YAHOO.util.Event.on('otherFilter','click',Farm.updateFilter);
      YAHOO.util.Event.on('regularFilter','click',Farm.updateFilter);
    });
  //]]>
  </script> 

  <c:choose>
    <c:when test="${form.growingForward2013}">
      <u:yuiTooltip targetId="eligibleLabel" messageKey="eligible.in.program.year.tooltip"/>
      <u:yuiTooltip targetId="isEligibleRefYearsLabel" messageKey="eligible.in.reference.years.tooltip"/>
    </c:when>
    <c:otherwise>
      <u:yuiTooltip targetId="eligibleLabel" messageKey="eligible.tooltip"/>
    </c:otherwise>
  </c:choose>

</c:if>

