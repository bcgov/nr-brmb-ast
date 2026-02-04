<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp" %>

<h1><fmt:message key="TIP.Reports"/></h1>

<html:form action="tipReportError" styleId="tipReportErrorForm" target="popupWindow" method="post">
  <html:hidden property="tipReportErrorText" styleId="tipReportErrorText"/>
</html:form>

<html:form action="generateTipReports" styleId="tipReportForm" onsubmit="showProcessing()">
  <html:hidden property="farmingOperationIds" styleId="farmingOperationIds"/>
  <html:hidden property="year" styleId="year"/>
  <table>
    <tr>
      <td>
        <fieldset>
          <legend><fmt:message key="Year"/></legend>
          <u:menuSelect action="farm660.do"
                        name="yearPicker" paramName="year"
                        additionalFieldIds="tipParticipantIndFilter,agriStabilityParticipantIndFilter,reportStatusFilter,ungeneratedFilter,generatedFilter,ungroupedFilter,unclassifiedFilter,pinFilter"
                        options="${form.yearSelectOptions}"
                        selectedValue="${form.year}"
                        toolTip="Click here to open a different Year." />
        </fieldset>
      </td>
      <td>
        <fieldset class="alignCenter">
          <legend><fmt:message key="TIP.Participant"/></legend>
          <html:select property="tipParticipantIndFilter" styleId="tipParticipantIndFilter">
            <html:option value=""></html:option>
            <html:option value="true">Yes</html:option>
            <html:option value="false">No</html:option>
          </html:select>
        </fieldset>
      </td>
      <td>
        <fieldset class="alignCenter">
          <legend><fmt:message key="AS.Participant"/></legend>
          <html:select property="agriStabilityParticipantIndFilter" styleId="agriStabilityParticipantIndFilter">
            <html:option value=""></html:option>
            <html:option value="true">Yes</html:option>
            <html:option value="false">No</html:option>
          </html:select>
        </fieldset>
      </td>
      <td>
        <fieldset>
          <legend><fmt:message key="TIP.Report.Status"/></legend>
          <table>
            <tr>
              <td class="button"><html:radio property="reportStatusFilter" styleId="ungeneratedFilter" value="UNGENERATED" /><fmt:message key="Ungenerated"/></td>
              <td class="button"><html:radio property="reportStatusFilter" styleId="generatedFilter" value="GENERATED" /><fmt:message key="Generated"/></td>
              <td class="button"><html:radio property="reportStatusFilter" styleId="ungroupedFilter" value="UNGROUPED" /><fmt:message key="Ungrouped"/></td>
              <td class="button"><html:radio property="reportStatusFilter" styleId="unclassifiedFilter" value="UNCLASSIFIED" /><fmt:message key="Unclassified"/></td>
            </tr>
          </table>
        </fieldset>
      </td>
    </tr>
    <tr>
      <td><fmt:message key="Find.PIN"/>:</td>
      <td><html:text property="pinFilter" styleId="pinFilter" maxlength="10" style='width:100px;padding:0px;font-size:10px'/></td>
      <td></td>
      <td></td>
    </tr>
  </table>
  <c:if test="${not form.benchmarksMatchConfig}">
    <div class="warnings">
      <ul>
        <li><fmt:message key="Benchmark.configuration.changed"/></li>
      </ul>
    </div>
  </c:if>
</html:form>
<br />

<div class="searchresults">
  <div id="search-results"></div>
</div>

<script>
  function saveContext() {
    var tipReportForm = $('#tipReportForm')
    var url = '<html:rewrite action="saveTipReportsContext" />';
    var serializedForm = tipReportForm.serialize();
    $.ajax({
      type: 'POST',
      async: true,
      url: url,
      data: serializedForm
    });
  }


  // Disable submitting the form with the Enter key
  $('#tipReportForm').keypress(function(event){
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if(keycode == '13'){
      event.preventDefault();
      return false;
    }
  });

  function promptForGenerate() {
    var count = Farm.getNumberSelected();
    if(count == 1 ) {
      setSelectedFarmingOperationIds();
      var farmingOperationIdsField = document.getElementById("farmingOperationIds");
      var opIds = farmingOperationIdsField.value.split(",");
      var farmingOperationId = opIds[0];
      openTipReport("", farmingOperationId, true);
    } else if(count > 1) {
      var message = '<fmt:message key="Are.you.sure.you.want.to.generate.TIP.Reports.for.the"/> ' + Farm.getNumberSelected()  + ' <fmt:message key="selected.operations"/>';
      var answer = confirm(message);
      if(answer) {
        setSelectedFarmingOperationIds();
        scheduleTipReports();
      }
    } else {
      var message = '<fmt:message key="You.must.select.some.operations.before.using.this.button"/>';
      var answer = alert(message);
    }     
  }

  function promptForGenerateAll() {
    var records = YAHOO.farm.operationsDataTable.getRecordSet().getRecords();
    
    if(records.length == 0) {
      alert('<fmt:message key="tip.report.no.operations.listed"/>');
    } else {
	    var message = '<fmt:message key="Are.you.sure.you.want.to.generate.TIP.Reports.for.all.listed.operations"/>';
	    var answer = confirm(message);
	    if(answer) {
	      setSelectAllFarmingOperationIds();
	      scheduleTipReports();
	    }  
    }
  }

  function scheduleTipReports() {
    showProcessing();
    var form = document.getElementById('tipReportForm');
    form.action = '<html:rewrite action="generateTipReports"/>';
    form.submit();
  }

  function setSelectedFarmingOperationIds() {
    var records = YAHOO.farm.operationsDataTable.getRecordSet().getRecords();
    var farmingOperationIds = "";
    for (i=0; i < records.length; i++) {
      if(records[i].getData().selectedInd == true) {
        farmingOperationIds = farmingOperationIds + records[i].getData().farmOpId + ",";
      }
    }
    
    var farmingOperationIdsField = document.getElementById("farmingOperationIds");
    farmingOperationIdsField.value = farmingOperationIds;
  }

  function setSelectAllFarmingOperationIds() {
    var records = YAHOO.farm.allCodesInCategory;
    var farmingOperationIds = "";
    for (i=0; i < records.length; i++) {
      farmingOperationIds = farmingOperationIds + records[i].farmOpId + ",";
    }

    var farmingOperationIdsField = document.getElementById("farmingOperationIds");
    farmingOperationIdsField.value = farmingOperationIds;
  }

  Farm.getNumberSelected = function () {
    var records = YAHOO.farm.operationsDataTable.getRecordSet().getRecords();
    var count = 0;
    for (i=0; i < records.length; i++) {
      if(records[i].getData().selectedInd == true) {
        count++;
      }
    }
    return count;
  }

  function setTipReportDocId(farmingOperationId, tipReportDocId) {
    var records = Farm.operations.records;
    for (i=0; i < records.length; i++) {
      var record = records[i];
      if(record.farmOpId == farmingOperationId) {
        record.tipReportStatusCode = 'GENERATED';
        record.tipReportDocId = tipReportDocId;
        break;
      }
    }
  }

  function getTipReportDocId(farmingOperationId) {
    var tipReportDocId;
    var records = Farm.operations.records;
    for (i=0; i < records.length; i++) {
      var record = records[i];
      if(record.farmOpId == farmingOperationId) {
        tipReportDocId = record.tipReportDocId;
        break;
      }
    }
    return tipReportDocId;
  }

  function openTipReport(tipReportDocId, farmingOperationId, regenerate) {
    if (!tipReportDocId && !regenerate) {
      tipReportDocId = getTipReportDocId(farmingOperationId);
    }
    if (!tipReportDocId || regenerate) {
      showProcessing();
      var url = '<html:rewrite action="generateTipReport" />?farmingOperationId=' + farmingOperationId;
      $.ajax({
        type: 'GET',
        async: true,
        url: url,
        success: function(data) {
          tipReportDocId = data.tipReportDocId;
          if(tipReportDocId) {
            setTipReportDocId(farmingOperationId, tipReportDocId);
            openTipReportWindow(tipReportDocId);
          } else {
            $('#tipReportErrorText').val(data);
            openNewWindow('<html:rewrite action="tipReportError" />');
            
            var form = document.getElementById('tipReportErrorForm');
            form.submit();
            undoShowProcessing();
          }
        }
      });
    } else {
      openTipReportWindow(tipReportDocId);
    }
  }
  
  function openTipReportWindow(tipReportDocId) {
    openNewWindow('<html:rewrite action="viewTipReport" />?tipReportDocId='+tipReportDocId);
    undoShowProcessing();
  }

  function viewJobs() {
    document.location.href = "<html:rewrite action="farm661" />";
  }
    
  YAHOO.util.Event.onDOMReady(function() {
    Farm.operations = {
      "recordsReturned": <c:out value="${form.numFarmOps}"/>,
      "totalRecords": <c:out value="${form.numFarmOps}"/>,
      "startIndex":0,
      "sort":null,
      "dir":"asc",
      "pageSize": 10,
      "records":[
        <c:forEach varStatus="loop" var="result" items="${form.farmOps}">
          {
            "pin": <c:out value="${result.pin}"/>,
            <c:choose>
              <c:when test="${result.tipReportStatusCode eq 'UNGENERATED' or result.tipReportStatusCode eq 'GENERATED'}">
                "pinDisplay": "<a href='javascript:openTipReport(&apos;<c:out value="${result.tipReportDocId}"/>&apos;, <c:out value="${result.farmOpId}"/>);'><c:out value="${result.pin}"/></a>",
              </c:when>
              <c:otherwise>
                "pinDisplay": "<c:out value="${result.pin}"/>",
              </c:otherwise>
            </c:choose>
            "farmOpId":"<c:out value="${result.farmOpId}"/>",
            "partnershipPin":"<c:out value="${result.partnershipPin}"/>",
            "tipReportDocId":"<c:out value="${result.tipReportDocId}"/>",
            "producerName":"<c:out value="${result.producerName}"/>",
            "tipReportStatusCode":"<c:out value="${result.tipReportStatusCode}"/>",
            "tipParticipant":<c:out value="${result.isTipParticipant}"/>,
            "asParticipant":<c:out value="${result.isAgriStabilityParticipant}"/>,
            selectedInd: false,
          }<c:if test="${loop.index < (form.numFarmOps-1)}">,</c:if>
        </c:forEach> 
      ]
    };       

    Farm.operationsColumnDefs = [
      {key:"selectedInd", label:"", formatter:"checkbox", sortable:true, className:"tdCenter"},
      {key:"pinDisplay", label:"<fmt:message key="PIN"/>", sortable:true, className:"tdCenter"},
      {key:"producerName", label:"<fmt:message key="Producer.Name"/>", sortable:true, className:"tdCenter"},
      {key:"partnershipPin", label:"<fmt:message key="Partnership.PIN"/>", sortable:true, className:"tdCenter"},
      {key:"tipParticipant", label:"<fmt:message key="TIP.Participant"/>", formatter:"checkbox", sortable:true, className:"tdCenter"}
    ];
       
    Farm.operationsConfigs = {paginator: new YAHOO.widget.Paginator({ rowsPerPage: 20 })};
    Farm.allCodesInCategory = [];
    Farm.operationsDataSource = new YAHOO.util.DataSource(Farm.operations, {
      doBeforeCallback : function (req,raw,res,cb) {
        // This is the filter function 

        var filter;
        if(YAHOO.util.Dom.get('ungeneratedFilter').checked) {
          filter = 'UNGENERATED';
        } else if (YAHOO.util.Dom.get('generatedFilter').checked) {
          filter = 'GENERATED';
        } else if (YAHOO.util.Dom.get('ungroupedFilter').checked) {
          filter = 'UNGROUPED';
        } else if (YAHOO.util.Dom.get('unclassifiedFilter').checked) {
          filter = 'UNCLASSIFIED';
        }
  
        var data = res.results || [], 
            filtered = [], 
            i,
            l; 

        for (i = 0, l = data.length; i < l; ++i) {
          var pinMatch = false;
          var statusMatch = false;
          var tipParticipantIndMatch = false;
          var agriStabilityParticipantIndMatch = false;
          let operation = data[i];

          pinMatch = Farm.filterPin(operation.pin);
          
          statusMatch = (filter == operation.tipReportStatusCode);
          
          tipParticipantIndMatch = Farm.filterTipParticipantInd(operation.tipParticipant);
          
          agriStabilityParticipantIndMatch = Farm.filterAgriStabilityParticipantInd(operation.asParticipant);
        
          if (pinMatch && statusMatch && tipParticipantIndMatch && agriStabilityParticipantIndMatch) {
            filtered.push(operation);
          }
        }

        res.results = filtered;
        Farm.allCodesInCategory = filtered;
        return res; 
      }
        
    });

    Farm.operationsDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
      Farm.operationsDataSource.responseSchema = {
        resultsList: "records",
        fields: ["pin", "pinDisplay", "selectedInd", "farmOpId", "partnershipPin", "tipReportDocId", "producerName", "tipReportStatusCode", "tipParticipant", "asParticipant"]
      };
      
      Farm.filterPin = function(pin) {
        var pinFilter = YAHOO.util.Dom.get('pinFilter');
        if(pinFilter.value) {
          if(String(pin).indexOf(String(pinFilter.value)) == 0) {
            return true;
          } else {
            return false;
          }
        }
        return true;      
      };
      
      Farm.filterTipParticipantInd = function(tipParticipantInd) {
        var tipParticipantIndFilter = $("#tipParticipantIndFilter").val();
        if(tipParticipantIndFilter) {
          if(String(tipParticipantInd).indexOf(String(tipParticipantIndFilter)) == 0) {
            return true;
          } else {
            return false;
          }
        }
        return true;      
      };
      
      Farm.filterAgriStabilityParticipantInd = function(agriStabilityParticipantInd) {
        var agriStabilityParticipantIndFilter = $("#agriStabilityParticipantIndFilter").val();
        if(agriStabilityParticipantIndFilter) {
          if(String(agriStabilityParticipantInd).indexOf(String(agriStabilityParticipantIndFilter)) == 0) {
            return true;
          } else {
            return false;
          }
        }
        return true;      
      };
      
      Farm.updateTipParticipantFlag = function(pin, isTipParticipant) {
        var records = Farm.operations.records;
        for (i=0; i < records.length; i++) {
          var record = records[i];
          if(record.pin == pin) {
            record.tipParticipant = isTipParticipant;
          }
        }

        var url = '<html:rewrite action="updateTipParticipantFlag" />?participantPins=' + pin + '&isTipParticipant=' + isTipParticipant;
        $.ajax({
          type: 'GET',
          async: true,
          url: url
        });
      };

      Farm.operationsDataTable = new YAHOO.widget.DataTable("search-results", Farm.operationsColumnDefs, Farm.operationsDataSource, Farm.operationsConfigs);
      Farm.operationsDataTable.subscribe("rowMouseoverEvent", Farm.operationsDataTable.onEventHighlightRow);
      Farm.operationsDataTable.subscribe("rowMouseoutEvent", Farm.operationsDataTable.onEventUnhighlightRow);
      
      Farm.operationsDataTable.subscribe('checkboxClickEvent', function(oArgs) {
          var elCheckbox = oArgs.target;
          var newValue = elCheckbox.checked;
          var record = this.getRecord(elCheckbox);
          var data = record.getData();
          var column = this.getColumn(elCheckbox);
          record.setData(column.key,newValue);
          
          if(column.key == "tipParticipant") {
            Farm.updateTipParticipantFlag(data.pin, newValue);
          }
        });
        
      Farm.filterInitialized = false;
      Farm.updateFilter = function ()  {

        if(YAHOO.util.Dom.get('ungeneratedFilter').checked) {
          $('#generateSelectedButton').show();
          $('#regenerateSelectedButton').hide();
          $('#regenerateAllButton').hide();
          $('#downloadButtons').hide();
          
          if($('#tipParticipantIndFilter').val() == 'true') {
            $('#generateAllButton').show();
          } else {
            $('#generateAllButton').hide();
          }
        } else if (YAHOO.util.Dom.get('generatedFilter').checked) {
          $('#generateSelectedButton').hide();
          $('#regenerateSelectedButton').show();
          $('#regenerateAllButton').show();
          $('#downloadButtons').show();
          
          $('#generateAllButton').hide();
        } else if (YAHOO.util.Dom.get('unclassifiedFilter').checked || YAHOO.util.Dom.get('ungroupedFilter').checked) {
          $('#generateSelectedButton').hide();
          $('#regenerateSelectedButton').hide();
          $('#regenerateAllButton').hide();
          $('#downloadButtons').hide();
          
          $('#generateAllButton').hide();
        }
        
        if(Farm.filterInitialized) {
          saveContext();
        }
        Farm.filterInitialized = true;

        // Reset sort 
        var state = YAHOO.farm.operationsDataTable.getState();
     
        // Get filtered data 
        Farm.operationsDataSource.sendRequest(null,{
          success : Farm.operationsDataTable.onDataReturnInitializeTable, 
          failure : Farm.operationsDataTable.onDataReturnInitializeTable, 
          scope   : Farm.operationsDataTable, 
          argument: state 
        });   
      };
   
      YAHOO.util.Event.on('ungeneratedFilter','click',Farm.updateFilter);      
      YAHOO.util.Event.on('generatedFilter','click',Farm.updateFilter);      
      YAHOO.util.Event.on('ungroupedFilter','click',Farm.updateFilter);
      YAHOO.util.Event.on('unclassifiedFilter','click',Farm.updateFilter);
      YAHOO.util.Event.on('pinFilter','keyup', Farm.updateFilter);
      YAHOO.util.Event.on('tipParticipantIndFilter','change', Farm.updateFilter);
      YAHOO.util.Event.on('agriStabilityParticipantIndFilter','change', Farm.updateFilter);

      Farm.updateFilter();
        
    });
</script>

<div>
  <w:ifUserCanPerformAction action="generateTipReports">
    <c:if test="${form.benchmarkDataGenerated}">
      <button type="button" id="generateSelectedButton"><fmt:message key="Generate.Selected"/></button>
      <button type="button" id="generateAllButton"><fmt:message key="Generate.All.Listed"/></button>
      
      <button type="button" id="regenerateSelectedButton"><fmt:message key="Regenerate.Selected"/></button>
      <button type="button" id="regenerateAllButton"><fmt:message key="Regenerate.All.Listed"/></button>
      
      <a id="viewJobsButton" href="javascript:viewJobs()"><fmt:message key="View.Jobs"/></a>
      
          <script type="text/javascript">
          //<![CDATA[
            new YAHOO.widget.Button("generateSelectedButton", {onclick: {fn: promptForGenerate}, disabled: <c:out value="${form.jobInProgress}"/>});
            new YAHOO.widget.Button("generateAllButton", {onclick: {fn: promptForGenerateAll}, disabled: <c:out value="${form.jobInProgress}"/>});
            
            new YAHOO.widget.Button("regenerateSelectedButton", {onclick: {fn: promptForGenerate}, disabled: <c:out value="${form.jobInProgress}"/>});
            new YAHOO.widget.Button("regenerateAllButton", {onclick: {fn: promptForGenerateAll}, disabled: <c:out value="${form.jobInProgress}"/>});
            
            new YAHOO.widget.Button("viewJobsButton");
          //]]>
          </script>
    </c:if>
  </w:ifUserCanPerformAction>
</div>
<%--
<div id="downloadButtons" style="margin-top: 20px;">
  <w:ifUserCanPerformAction action="generateTipReports">
    <c:if test="${form.benchmarkDataGenerated}">
    	<script>
        function downloadSelected() {
          var count = Farm.getNumberSelected();
          if(count > 0) {
            showProcessing();
            setSelectedFarmingOperationIds();
            var form = document.getElementById('tipReportForm');
            form.action = '<html:rewrite action="downloadTipReports"/>';
            form.submit();
            undoShowProcessing();
          } else {
            var message = '<fmt:message key="You.must.select.some.operations.before.using.this.button"/>';
            var answer = alert(message);
          }
        }
        function downloadAll() {
          showProcessing();
          setSelectAllFarmingOperationIds();
          var form = document.getElementById('tipReportForm');
          form.action = '<html:rewrite action="downloadTipReports"/>';
          form.submit();
          undoShowProcessing();
        }
    	</script>
    	<u:yuiButton buttonLabel="Download.Selected" buttonId="downloadSelectedButton" function="downloadSelected" />
    	<u:yuiButton buttonLabel="Download.All" buttonId="downloadAllButton" function="downloadAll" />
    </c:if>
  </w:ifUserCanPerformAction>
</div>
--%>
