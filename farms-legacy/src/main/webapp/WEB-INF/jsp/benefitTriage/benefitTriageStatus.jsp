<%@ include file="/WEB-INF/jsp/common/include.jsp"%>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp"%>

<table>
  <tr>
    <td style="width: 100px;">
      <fieldset>
        <legend>
          <fmt:message key="Program.Year" />
        </legend>
        <u:menuSelect action="farm258.do" name="inboxYearPicker"
          paramName="year"
          additionalFieldIds="inboxSearchType,inProgressCB,verifiedCB,closedCB"
          options="${form.programYearSelectOptions}"
          selectedValue="${form.year}"
          toolTip="Click here to open a different Program Year." />
      </fieldset>
    </td>
  </tr>
</table>

<c:if test="${form.numStatusResults == 0}">
	<p>
		<fmt:message key="There.are.no.Benefit.Triage.Status.items.for" /> <c:out value="${form.year}" />.
	</p>
	<html:form action="farm258">
		<html:hidden property="pinFilter" styleId="pinFilter" />
		<html:hidden property="nameFilter" styleId="nameFilter" />
		<html:hidden property="statusFilter" styleId="statusFilter" />
		<html:hidden property="isPaymentFileFilter" styleId="isPaymentFileFilter" />
	</html:form>
</c:if>

<c:if test="${form.numStatusResults > 0}">

	<div class="searchresults">
		<div id="searchresults"></div>
	</div>

	<script type="text/javascript">
  //<![CDATA[
    Farm.codes = {
        "recordsReturned": <c:out value="${form.numStatusResults}"/>,
        "totalRecords": <c:out value="${form.numStatusResults}"/>,
        "startIndex":0,
        "sort":null,
        "dir":"asc",
        "pageSize": 10,
        "records":[
          <c:forEach varStatus="loop" var="result" items="${form.statusResults}">
            {
              "participantPin":"<c:out value="${result.participantPin}"/>",  
              "clientName":"<c:out value="${result.clientName}"/>",  
              "status":"<c:out value="${result.scenarioStateCodeDesc}"/>",  
              "estimatedBenefit":"<c:out value="${result.estimatedBenefit}"/>",  
              "isPaymentFile":"<c:out value="${result.isPaymentFile}"/>",  
              "scenarioNumber":"<c:out value="${result.scenarioNumber}"/>",  
            }<c:if test="${loop.index < (form.numStatusResults-1)}">,</c:if>
          </c:forEach> 
        ]
    };
    
    YAHOO.util.Event.onDOMReady(function() {

      Farm.codesColumnDefs = [
           {key:"participantPin", label:"<fmt:message key="PIN"/><br /><input name='pinFilter' type='text' id='pinFilter' value='<c:out value="${form.pinFilter}"/>' maxlength='10' style='width:40px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
           {key:"clientName", label:"<fmt:message key="Name"/><br /><input name='nameFilter' type='text' id='nameFilter' value='<c:out value="${form.nameFilter}"/>' maxlength='256' style='width:225px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
           {key:"status", label:"<fmt:message key="Status"/><br /><select name='statusFilter' id='statusFilter' style='font-size:10px;'><option value=''></option><c:forEach items="${form.scenarioStateSelectOptions}" var="item"><option value='<c:out value="${item.label}"/>' <c:if test="${item.label == form.statusFilter}">selected='selected'</c:if>><c:out value="${item.label}"/></option></c:forEach></select>", sortable:false, className:"tdCenter"},
           {key:"estimatedBenefit", label:"<fmt:message key="Estimated.Benefit.Amount"/>", sortable:false, className:"tdCenter"},
           {key:"isPaymentFile", label:"<span id='isPaymentFileLabel'><fmt:message key="Is.Payment.File"/></span><br /><select name='isPaymentFileFilter' id='isPaymentFileFilter' style='font-size:10px;'><option value=''></option><option value='Y' <c:if test="${form.isPaymentFileFilter=='Y'}">selected='selected'</c:if>>Y</option><option value='N' <c:if test="${form.isPaymentFileFilter=='N'}">selected='selected'</c:if> >N</option></select>", sortable:false, className:"tdCenter"},
           ];

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
      
      Farm.filterPin = function(pin) {
        var pinFilter = YAHOO.util.Dom.get('pinFilter');
        if(pinFilter.value && pin.indexOf(pinFilter.value) < 0) {
          return false;
        }
        return true;      
      };

      Farm.filterDescription = function(description) {
        var nameFilter = YAHOO.util.Dom.get('nameFilter');
        if(nameFilter.value && description.toLowerCase().indexOf(nameFilter.value.toLowerCase()) < 0) {
          return false;
        }
        return true;      
      };
      
      Farm.filterStatus = function(status) {
        var stausFilter = YAHOO.util.Dom.get('statusFilter');
        return Farm.filterSelect(stausFilter, status);
      };

      Farm.filterIsEligibleRefYears = function(value) {
        var selectFilter = YAHOO.util.Dom.get('isPaymentFileFilter');
        return Farm.filterSelect(selectFilter, value);
      };
        

      Farm.codesDataSource = new YAHOO.util.DataSource(Farm.codes,{
        doBeforeCallback : function (req,raw,res,cb) {
          // This is the filter function 
          var data = res.results || [], filtered = [], i; 
          var l = data.length;
          for (i = 0; i < l; ++i) {
            if (Farm.filterPin(data[i].participantPin) &&
            	 Farm.filterDescription(data[i].clientName) &&
            	 Farm.filterStatus(data[i].status) &&
            	 Farm.filterIsEligibleRefYears(data[i].isPaymentFile)) {
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
        fields: ["participantPin","clientName","status","estimatedBenefit","isPaymentFile","scenarioNumber"]
      };

      Farm.codesConfigs = {
        paginator: new YAHOO.widget.Paginator({ rowsPerPage: 20 })
      };

      Farm.showSelectedRow = function(oArgs) {
    	  var oRecord = oArgs.record;
        var participantPin = oRecord.getData("participantPin");
        var scenarioNumber = oRecord.getData("scenarioNumber");
        var farmAction = "farm830";
        var year = <c:out value="${form.year}"/>;
        showProcessing();
        var url = farmAction + ".do?pin=" + participantPin + "&year=" + year + "&scenarioNumber=" + scenarioNumber + "&refresh=true"
        document.location.href = url;
      };

      Farm.codesDataTable = new YAHOO.widget.DataTable("searchresults", Farm.codesColumnDefs, Farm.codesDataSource, Farm.codesConfigs);
      Farm.codesDataTable.subscribe("rowMouseoverEvent", Farm.codesDataTable.onEventHighlightRow);
      Farm.codesDataTable.subscribe("rowMouseoutEvent", Farm.codesDataTable.onEventUnhighlightRow);
      Farm.codesDataTable.subscribe("rowClickEvent", Farm.codesDataTable.onEventSelectRow);
      Farm.codesDataTable.subscribe("rowSelectEvent", Farm.showSelectedRow);
      
			Farm.updateFilter = function ()  {
			  // Reset sort 
			  var state = YAHOO.farm.codesDataTable.getState(); 
			
			  // Get filtered data 
			  Farm.codesDataSource.sendRequest(null, { 
			      success : Farm.codesDataTable.onDataReturnInitializeTable, 
			      failure : Farm.codesDataTable.onDataReturnInitializeTable, 
			      scope   : Farm.codesDataTable,
			      argument: state 
			  });   
			};

      YAHOO.util.Event.on('pinFilter','keyup',Farm.updateFilter);
      YAHOO.util.Event.on('nameFilter','keyup',Farm.updateFilter);
      YAHOO.util.Event.on('statusFilter','change',Farm.updateFilter);
      YAHOO.util.Event.on('isPaymentFileFilter','change',Farm.updateFilter);
    });
  //]]>
  </script>

</c:if>

