<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script>
  //
  // If the screen is redisplayed with struts validation errors then 
  // the exportUrl attribute will be null.
  //
  function onPageLoad(){
    YAHOO.util.Dom.get("enrolmentsForm").style.display = '';
    YAHOO.util.Dom.get("buttonsDiv").style.display = '';
    
    <c:if test="${form.exportUrl != null}">
      var url = '<c:out value="${form.exportUrl}" escapeXml="false"/>';

      var ifm = document.getElementById("reportIframe"); 
      ifm.src = url;
    </c:if>
  }
  
  function checkReadyState(iframe) {
    if (iframe.readyState == "loading") {
      // undo the showProcessing javascript call
      undoShowProcessing();
    }
  }
  
  $(window).on('beforeunload', function() {

    var enrolmentsForm = $('#enrolmentsForm')
    var url = '<html:rewrite action="saveEnrolmentContext" />';
    var serializedForm = enrolmentsForm.serialize();
    $.ajax({
      type: 'POST',
      async: false,
      url: url,
      data: serializedForm
    });
  });

</script>

<w:ifUserCanPerformAction action="generateEnrolments" var="canGenerate"></w:ifUserCanPerformAction>

<h1><fmt:message key="Agristability.Enrolments"/></h1>

<html:form action="farm360" styleId="enrolmentsForm" method="post" onsubmit="showProcessing()" style="display:none;">
  <html:hidden property="year" styleId="year"/>
  <html:hidden property="pins" styleId="pins"/>
  <html:hidden property="regionalOfficeCode" styleId="regionalOfficeCode"/>

  <table>
    <tr>
      <td>
        <fieldset>
          <legend><fmt:message key="Year"/></legend>
          <u:menuSelect action="farm360.do"
                        name="yearPicker" paramName="year"
                        additionalFieldIds="regionalOfficeCode,ungeneratedFilter,generatedFilter,failedFilter,allFilter,verifiedFilter,unverifiedFilter,enInProgressFilter,enCompleteFilter,startDateFilter,endDateFilter,pinFilter,failedReasonFilter"
                        options="${form.enrolmentYearSelectOptions}"
                        selectedValue="${form.year}"
                        toolTip="Click here to open a different Enrolment Year." />
        </fieldset>
      </td>
      <td style="min-width:135px;">
        <fieldset>
          <legend><fmt:message key="Regional.Office"/></legend>
          <u:menuSelect action="farm360.do"
                        name="regionPicker" paramName="regionalOfficeCode"
                        additionalFieldIds="year,ungeneratedFilter,generatedFilter,failedFilter,allFilter,verifiedFilter,unverifiedFilter,enInProgressFilter,enCompleteFilter,startDateFilter,endDateFilter,pinFilter,failedReasonFilter"
                        options="${form.regionSelectOptions}"
                        selectedValue="${form.regionalOfficeCode}"
                        toolTip="Click here to open a different Regional Office." />
        </fieldset>
      </td>
      <td>
        <fieldset>
        <legend><fmt:message key="Enrolment.Status"/></legend>
          <table>
            <tr>
              <td class="button"><html:radio property="enrolmentStatusFilter" styleId="ungeneratedFilter" value="ungenerated" /><fmt:message key="Ungenerated"/></td>
              <td class="button"><html:radio property="enrolmentStatusFilter" styleId="generatedFilter" value="generated" /><fmt:message key="Generated"/></td>
              <td class="button"><html:radio property="enrolmentStatusFilter" styleId="failedFilter" value="failed" /><fmt:message key="Failed"/></td>
            </tr>
          </table>
        </fieldset>
      </td>
      <td id="failedReasonMenuColumn" style="display:none;">
        <fieldset>
          <legend><fmt:message key="Reason"/></legend>
          <html-el:select property="failedReasonFilter" styleId="failedReasonFilter" style="font-size:10px;">
            <html-el:option value=""></html-el:option>
            <html-el:option value="${form.reasonConstantExcessiveMargin}"><fmt:message key="Excessive.Margin"/></html-el:option>
            <html-el:option value="${form.reasonConstantInsufficientMargin}"><fmt:message key="Insufficient.Reference.Margin"/></html-el:option>
            <html-el:option value="${form.reasonConstantMissingBpu}"><fmt:message key="Missing.BPUs"/></html-el:option>
          </html-el:select>
        </fieldset>
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <table>
          <tr id="startDateFilterRow" style="display:none;">
            <td><fmt:message key="Start.Date"/>:</td>
            <td><html:text property="startDateFilter" styleId="startDateFilter" maxlength="10" style='width:100px;padding:0px;font-size:10px'/></td>
            <td><u:datePicker fieldId='startDateFilter' onUpdateFunc='Farm.updateFilter'/></td>
          </tr>
          <tr id="endDateFilterRow" style="display:none;">
            <td><fmt:message key="End.Date"/>:</td>
            <td><html:text property="endDateFilter" styleId="endDateFilter" maxlength="10" style='width:100px;padding:0px;font-size:10px'/></td>
            <td><u:datePicker fieldId='endDateFilter' onUpdateFunc='Farm.updateFilter'/></td>
          </tr>
          <tr>
            <td><fmt:message key="Find.PIN"/>:</td>
            <td><html:text property="pinFilter" styleId="pinFilter" maxlength="10" style='width:100px;padding:0px;font-size:10px'/></td>
            <td></td>
          </tr>
        </table>
      </td>
      <td>
        <fieldset>
        <legend><fmt:message key="Scenario.State"/></legend>
          <table>
            <tr>
              <td class="button"><html:radio property="scenarioStateFilter" styleId="allFilter" value="all" /><fmt:message key="All"/></td>
              <td class="button"><html:radio property="scenarioStateFilter" styleId="verifiedFilter" value="verified" /><fmt:message key="Verified"/></td>
              <td class="button"><html:radio property="scenarioStateFilter" styleId="unverifiedFilter" value="unverified" /><fmt:message key="Unverified"/></td>
              <td class="button"><html:radio property="scenarioStateFilter" styleId="enInProgressFilter" value="enInProgress" /><fmt:message key="EN.In.Progress"/></td>
              <td class="button"><html:radio property="scenarioStateFilter" styleId="enCompleteFilter" value="enComplete" /><fmt:message key="EN.Complete"/></td>
            </tr>
          </table>
        </fieldset>
      </td>
      <td>
      </td>
    </tr>
    <tr>
      <td colspan="2">
      </td>
      <td colspan="2">
        <div style="float:right">
          <w:ifUserCanPerformAction action="generateEnrolments">
            <a id="importPinsButton" href="javascript:importPins()"><fmt:message key="Import.Pins"/></a>
          </w:ifUserCanPerformAction>
        </div>
      </td>
    </tr>
  </table>

  <div class="searchresults">
    <div id="myAccounts"></div>
  </div>

  <table style="width:100%;">
  <tr>
    <td>
      <div style="float:right; margin-bottom: 10px;">
      <input type="checkbox" name="createTaskInBarn" id="createTaskInBarn" /><fmt:message key="Create.Task.in.BARN" />
      </div>
    </td>
  </tr>
  </table>

</html:form>

<div id="buttonsDiv" style="display:none;">
  <div style="float:left">
      <a id="viewJobsButton" href="javascript:viewJobs()"><fmt:message key="View.Jobs"/></a>
      <a id="exportSelectedButton" href="javascript:promptForExportCsvSelected()"><fmt:message key="Export.Selected"/></a>
      <a id="exportAllButton" href="javascript:promptForExportCsvAll()"><fmt:message key="Export.All.Listed"/></a>
  </div>

  <div style="float:right">
      <button type="button" id="regenerateEnrolmentsButton"><fmt:message key="Regenerate.Selected"/></button>
      <button type="button" id="generateEnrolmentsButton"><fmt:message key="Generate.Selected"/></button>
      <button type="button" id="retryGenerateEnrolmentsButton"><fmt:message key="Retry.Generate.Selected"/></button>

      <button type="button" id="regenerateAllEnrolmentsButton"><fmt:message key="Regenerate.All.Listed"/></button>
      <button type="button" id="generateAllEnrolmentsButton"><fmt:message key="Generate.All.Listed"/></button>
      <button type="button" id="retryGenerateAllEnrolmentsButton"><fmt:message key="Retry.Generate.All.Listed"/></button>
  </div>
  <br />
</div>

<iframe id="reportIframe" src="" style="visibility:hidden" onReadyStateChange="checkReadyState(this)"></iframe>

  <script>

    new YAHOO.widget.Button("importPinsButton");
    new YAHOO.widget.Button("viewJobsButton");
    new YAHOO.widget.Button("exportSelectedButton");
    new YAHOO.widget.Button("exportAllButton");

    YAHOO.util.Dom.get('regenerateEnrolmentsButton').style.display = 'none';
    YAHOO.util.Dom.get('generateEnrolmentsButton').style.display = 'none';
    YAHOO.util.Dom.get('retryGenerateEnrolmentsButton').style.display = 'none';
    YAHOO.util.Dom.get('regenerateAllEnrolmentsButton').style.display = 'none';
    YAHOO.util.Dom.get('generateAllEnrolmentsButton').style.display = 'none';
    YAHOO.util.Dom.get('retryGenerateAllEnrolmentsButton').style.display = 'none';

    function importPins() {
      document.location.href = "<html:rewrite action="farm361" />";
    }

    function viewJobs() {
      document.location.href = "<html:rewrite action="farm251" />";
    }

    function promptForGenerate() {
      var count = Farm.getNumberSelected();
      if(count != 0 ) {
        var message = '<fmt:message key="Are.you.sure.you.want.to.generate.Enrolments.for.the"/> ' + Farm.getNumberSelected()  + ' <fmt:message key="selected.PINs"/>';
        var answer = confirm(message);
        if(answer) {
          setSelectedPins();
          scheduleEnrolments();
        }
      } else {
        var message = '<fmt:message key="You.must.select.some.PINS.before.using.this.button."/>';
        var answer = alert(message);
      }     
    }

    function promptForRegenerate() {
      var count = Farm.getNumberSelected();
      if(count != 0 ) {
        var message = '<fmt:message key="Are.you.sure.you.want.to.regenerate.Enrolments.for.the"/> ' + Farm.getNumberSelected()  + ' <fmt:message key="selected.PINs"/>';
        var answer = confirm(message);
        if(answer) {
          setSelectedPins();
          scheduleEnrolments();
        }
      } else {
        var message = '<fmt:message key="You.must.select.some.PINS.before.using.this.button."/>';
        var answer = alert(message);
      }     
    }

    function promptForGenerateAll() {
      var count = Farm.getNumberSelected();
      var message = '<fmt:message key="Are.you.sure.you.want.to.generate.Enrolments.for.all.the.PINs.Listed"/>';
      var answer = confirm(message);
      if(answer) {
        setSelectedPinsAll();
        if(document.getElementById("pins").value != "") {
          scheduleEnrolments();
        } else {
          var message = '<fmt:message key="There.are.no.PINs.to.generate.for"/>';
          var answer = alert(message);
        }     
      }
    }

    function promptForRegenerateAll() {
      var count = Farm.getNumberSelected();
      var message = '<fmt:message key="Are.you.sure.you.want.to.regenerate.Enrolments.for.all.the.PINs.Listed"/>';
      var answer = confirm(message);
      if(answer) {
        setSelectedPinsAll();
        if(document.getElementById("pins").value != "") {
          scheduleEnrolments();
        } else {
          var message = '<fmt:message key="There.are.no.PINs.to.regenerate.for"/>';
          var answer = alert(message);
        }     
      }
    }

    function scheduleEnrolments() {
      var form = document.getElementById('enrolmentsForm');
      form.action = '<html:rewrite action="generateEnrolments"/>';
      form.submit();
    }

    function promptForExportCsvSelected() {
      var count = Farm.getNumberSelected();
      if(count != 0 ) {
        var message = '<fmt:message key="Are.you.sure.you.want.to.export.Enrolments.for.the"/> ' + Farm.getNumberSelected()  + ' <fmt:message key="selected.PINs"/>';
        var answer = confirm(message);
        if(answer) {
          setSelectedPins();
          exportCsv();
        }
      } else {
        var message = '<fmt:message key="You.must.select.some.PINS.before.using.this.button."/>';
        var answer = alert(message);
      }     
    }

    function promptForExportCsvAll() {
      var count = Farm.getNumberSelected();
      if(count != 0 ) {
        var message = '<fmt:message key="Are.you.sure.you.want.to.export.Enrolments.for.all.the.PINs.Listed"/> ';
        var answer = confirm(message);
        if(answer) {
          setSelectedPinsAll();
          exportCsv();
        }
      } else {
        var message = '<fmt:message key="You.must.select.some.PINS.before.using.this.button."/>';
        var answer = alert(message);
      }     
    }

    function promptForExportCsvAll() {
      var count = Farm.getNumberSelected();
      var message = '<fmt:message key="Are.you.sure.you.want.to.export.Enrolments.for.all.the.PINs.Listed"/> ';
      var answer = confirm(message);
      if(answer) {
        setSelectedPinsAll();
        if(document.getElementById("pins").value != "") {
          exportCsv();
        } else {
          var message = '<fmt:message key="There.are.no.PINs.to.export"/>';
          var answer = alert(message);
        }     
      }
    }

    function exportCsv() {
      var form = document.getElementById('enrolmentsForm');
      form.action = '<html:rewrite action="enrolmentCSV"/>';
      form.submit();
    }

    function setSelectedPins() {
      var records = YAHOO.farm.myAccountsDataTable.getRecordSet().getRecords();
      var pins = "";
      for (i=0; i < records.length; i++) {
        if(records[i].getData().selectedInd == true) {
          pins = pins + records[i].getData().pin + ",";
        }
      }
      var pinsField = document.getElementById("pins");
      pinsField.value = pins;
    }

    function setSelectedPinsAll() {
      var records = YAHOO.farm.myAccountsDataTable.getRecordSet().getRecords();
      var pins = "";
      for (i=0; i < records.length; i++) {
        pins = pins + records[i].getData().pin + ",";
      }
      var pinsField = document.getElementById("pins");
      pinsField.value = pins;
    }
    
    function formatDateField(strDate) {
        var dateSplit = "";
        if (strDate.indexOf("-") != -1) {
          dateSplit = strDate.split("-");
        }
        if (strDate.indexOf("/") != -1) {
          dateSplit = strDate.split("/");
        }
        if (dateSplit != "" && dateSplit.length == 3) {
          for (var i=0; i<dateSplit.length; i++) {
            if (dateSplit[i].length == 1) {
              dateSplit[i] = "0" + dateSplit[i];
            }
          }
          strDate = dateSplit[0] + "/" + dateSplit[1] + "/" + dateSplit[2];
        }
        if (strDate.length == 10) {
          return strDate;
        } else {
          return "";
        }
    }
    
    function redirectToScenario(pin, programYear) {
      document.location.href = "<html:rewrite action="farm830"/>?pin=" + pin + "&year=" + programYear + "&purpose=ENROLMENT";
    }

  </script>

  <c:choose>
    <c:when test="${form.allowedToGenerate}">
      <script>
        new YAHOO.widget.Button("regenerateEnrolmentsButton", {onclick: {fn: promptForRegenerate}});
        new YAHOO.widget.Button("generateEnrolmentsButton", {onclick: {fn: promptForGenerate}});
        new YAHOO.widget.Button("retryGenerateEnrolmentsButton", {onclick: {fn: promptForGenerate}});
        new YAHOO.widget.Button("regenerateAllEnrolmentsButton", {onclick: {fn: promptForRegenerateAll}});
        new YAHOO.widget.Button("generateAllEnrolmentsButton", {onclick: {fn: promptForGenerateAll}});
        new YAHOO.widget.Button("retryGenerateAllEnrolmentsButton", {onclick: {fn: promptForGenerateAll}});
      </script>
    </c:when>
    <c:otherwise>
      <script>
        new YAHOO.widget.Button("regenerateEnrolmentsButton", {disabled: true});
        new YAHOO.widget.Button("generateEnrolmentsButton", {disabled: true});
        new YAHOO.widget.Button("retryGenerateEnrolmentsButton", {disabled: true});
        new YAHOO.widget.Button("regenerateAllEnrolmentsButton", {disabled: true});
        new YAHOO.widget.Button("generateAllEnrolmentsButton", {disabled: true});
        new YAHOO.widget.Button("retryGenerateAllEnrolmentsButton", {disabled: true});
      </script>
    </c:otherwise>
  </c:choose>

  <p></p>

<script src="yui/2.8.2r1/build/utilities/utilities.js"></script>
<script src="yui/2.8.2r1/build/yahoo-dom-event/yahoo-dom-event.js"></script>
<script src="yui/2.8.2r1/build/element/element-min.js"></script>
<script src="yui/2.8.2r1/build/datasource/datasource-min.js"></script>
<script src="yui/2.8.2r1/build/json/json-min.js"></script>
<script src="yui/2.8.2r1/build/datatable/datatable-min.js"></script>
<script src="yui/2.8.2r1/build/paginator/paginator-min.js"></script>

<script src="<html:rewrite action="enrolmentDisplayData"/>?year=<c:out value="${form.year}"/>&regionalOfficeCode=<c:out value="${form.regionalOfficeCode}"/>"></script>

<script>

YAHOO.util.Event.onDOMReady(function() {
     Farm.myAcountsColumnDefs = [
        {key:"selectedInd", label:"", formatter:"checkbox", sortable:true, className:"tdCenter"},
        {key:"pinDisplay", label:"<fmt:message key="PIN"/>", sortable:true, className:"tdCenter"},
        {key:"producer", label:"<fmt:message key="Producer"/>", sortable:true, className:"tdLeft", minWidth:200},
        {key:"generatedDate", formatter:YAHOO.widget.DataTable.formatDate, label:"<fmt:message key="Generated.Date"/>", sortable:true, className:"tdCenter"},
        {key:"failedDate", formatter:YAHOO.widget.DataTable.formatDate, label:"<fmt:message key="Failed.Date"/>", sortable:true, className:"tdCenter"},
        {key:"fee", formatter:YAHOO.widget.DataTable.formatCurrency, label:"<fmt:message key="Fee"/>", sortable:true, className:"tdRight"},
        {key:"note", label:"<fmt:message key="Reason"/>",  className:"tdLeft"},
        {key:"fromCra", label:"<fmt:message key="From.CRA"/>", sortable:true, className:"tdLeft"}
    ];
         
    Farm.filterPin = function(pin) {
      var pinFilter = YAHOO.util.Dom.get('pinFilter');
      if(pinFilter.value && pinFilter.value.trim()) {
        var trimmedPin = pinFilter.value.trim();
        if(pin.indexOf(trimmedPin) == 0) {
          return true;
        } else {
          return false;
        }
      }
      return true;      
    };
    
    Farm.filterDate = function(date) {
      var startDate = new Date(formatDateField(YAHOO.util.Dom.get('startDateFilter').value));
      var endDate = new Date(formatDateField(YAHOO.util.Dom.get('endDateFilter').value));
      var startCheck = false;
      var endCheck = false;
      
      // if startDate is invalid Date object
      if (isNaN(startDate.getTime())) {
        startCheck = true;
      } else if (date.getTime() > startDate.getTime()) {
        startCheck = true;
      }
      // if endDate is invalid Date object
      if (isNaN(endDate.getTime())) {
        endCheck = true;
      } else {
        // change hours to end of day
        endDate.setDate(endDate.getDate()+1);
        endDate.setMilliseconds(-1);
        if (date.getTime() < endDate.getTime()) {
          endCheck = true;
        }
      }
      
      return startCheck && endCheck;
    };
    
    Farm.myAccountsDataSource = new YAHOO.util.DataSource(Farm.myAccounts,{
      doBeforeCallback : function (req,raw,res,cb) {
              // This is the filter function 
                
        var filter;
        if(YAHOO.util.Dom.get('ungeneratedFilter').checked) {
          filter = 'ungenerated';
          YAHOO.util.Dom.get('regenerateEnrolmentsButton').style.display = 'none';
          YAHOO.util.Dom.get('retryGenerateEnrolmentsButton').style.display = 'none';
          YAHOO.util.Dom.get('generateEnrolmentsButton').style.display = '';
          YAHOO.util.Dom.get('regenerateAllEnrolmentsButton').style.display = 'none';
          YAHOO.util.Dom.get('retryGenerateAllEnrolmentsButton').style.display = 'none';
          YAHOO.util.Dom.get('generateAllEnrolmentsButton').style.display = '';
          YAHOO.util.Dom.get('failedReasonMenuColumn').style.display = 'none';
          YAHOO.util.Dom.get('startDateFilterRow').style.display = 'none';
          YAHOO.util.Dom.get('endDateFilterRow').style.display = 'none';
        } else {
          if(YAHOO.util.Dom.get('generatedFilter').checked) {
            filter = 'generated';
            YAHOO.util.Dom.get('regenerateEnrolmentsButton').style.display = '';
            YAHOO.util.Dom.get('generateEnrolmentsButton').style.display = 'none';
            YAHOO.util.Dom.get('retryGenerateEnrolmentsButton').style.display = 'none';
            YAHOO.util.Dom.get('regenerateAllEnrolmentsButton').style.display = '';
            YAHOO.util.Dom.get('generateAllEnrolmentsButton').style.display = 'none';
            YAHOO.util.Dom.get('retryGenerateAllEnrolmentsButton').style.display = 'none';
            YAHOO.util.Dom.get('failedReasonMenuColumn').style.display = 'none';
            YAHOO.util.Dom.get('startDateFilterRow').style.display = '';
            YAHOO.util.Dom.get('endDateFilterRow').style.display = '';
          } else {
            if(YAHOO.util.Dom.get('failedFilter').checked) {
              filter = 'failed';
              YAHOO.util.Dom.get('regenerateEnrolmentsButton').style.display = 'none';
              YAHOO.util.Dom.get('generateEnrolmentsButton').style.display = 'none';
              YAHOO.util.Dom.get('retryGenerateEnrolmentsButton').style.display = '';
              YAHOO.util.Dom.get('regenerateAllEnrolmentsButton').style.display = 'none';
              YAHOO.util.Dom.get('generateAllEnrolmentsButton').style.display = 'none';
              YAHOO.util.Dom.get('retryGenerateAllEnrolmentsButton').style.display = '';
              YAHOO.util.Dom.get('failedReasonMenuColumn').style.display = '';
              YAHOO.util.Dom.get('startDateFilterRow').style.display = '';
              YAHOO.util.Dom.get('endDateFilterRow').style.display = '';
            }
          }
        }

<c:if test="${!canGenerate}">
        YAHOO.util.Dom.get('regenerateEnrolmentsButton').style.display = 'none';
        YAHOO.util.Dom.get('generateEnrolmentsButton').style.display = 'none';
        YAHOO.util.Dom.get('retryGenerateEnrolmentsButton').style.display = 'none';
        YAHOO.util.Dom.get('regenerateAllEnrolmentsButton').style.display = 'none';
        YAHOO.util.Dom.get('generateAllEnrolmentsButton').style.display = 'none';
        YAHOO.util.Dom.get('retryGenerateAllEnrolmentsButton').style.display = 'none';
</c:if>

      var data     = res.results || [], 
          filtered = [], 
          i,l; 
        
      for (i = 0, l = data.length; i < l; ++i) {
        var pinCheck = false;
        var statusCheck = false;
        var stateCheck = false;

        if(Farm.filterPin(data[i].pin)) {
          pinCheck = true;
        }

        if (filter == 'ungenerated') { 
          if (data[i].status == "<c:out value="${form.statusConstantUngenerated}"/>" ) { 
            statusCheck = true;
          }
        } else if (filter == 'generated') {
          if (data[i].status == "<c:out value="${form.statusConstantGenerated}"/>" ) { 
            // check for date range when status='generated'
            if (Farm.filterDate(data[i].generatedDate)) {
              statusCheck = true;
            }
          }
        } else if(filter == 'failed') {
          if(data[i].status == "<c:out value="${form.statusConstantFailedToGenerate}"/>") {
            if (Farm.filterDate(data[i].failedDate)) {
              // check for failed reason filter when status='failed'
              var selectFilter = YAHOO.util.Dom.get('failedReasonFilter');
              var selectedValue = selectFilter.options[selectFilter.selectedIndex].value;
              if(selectedValue == '') {
                statusCheck = true;
              } else if(data[i].note.indexOf(selectedValue) != -1) {
                statusCheck = true;
              }
            }
          }
        }

        if (YAHOO.util.Dom.get('allFilter').checked) {
          stateCheck = true;
        } else if (YAHOO.util.Dom.get('verifiedFilter').checked) {
          if (data[i].state == "<c:out value="${form.stateConstantVerified}"/>" ) {
            stateCheck = true;
          }
        } else if(YAHOO.util.Dom.get('enInProgressFilter').checked) {
          if(data[i].state == "<c:out value="${form.stateConstantEnInProgress}"/>") {
            stateCheck = true;
          }
        } else if(YAHOO.util.Dom.get('enCompleteFilter').checked) {
          if(data[i].state == "<c:out value="${form.stateConstantEnComplete}"/>") {
            stateCheck = true;
          }
        } else if(YAHOO.util.Dom.get('unverifiedFilter').checked) {
          if(data[i].state != "<c:out value="${form.stateConstantVerified}"/>") {
            stateCheck = true;
          }
        }

        if (pinCheck && statusCheck && stateCheck) {
          filtered.push(data[i]);
        }
      }

        res.results = filtered;
        return res;
      }
    });

    Farm.myAccountsDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
    Farm.myAccountsDataSource.responseSchema = {
        resultsList: "records",
        fields: ["pin","pinDisplay","status","state","producer","generatedDate","failedDate","fee","fromCra","selectedInd","note"]
    };

    Farm.myAccountsConfigs = {
        paginator: new YAHOO.widget.Paginator({ rowsPerPage: 10 })
    };

    Farm.myAccountsDataTable = new YAHOO.widget.DataTable("myAccounts", Farm.myAcountsColumnDefs, Farm.myAccountsDataSource, Farm.myAccountsConfigs);
    Farm.myAccountsDataTable.subscribe("rowMouseoverEvent", Farm.myAccountsDataTable.onEventHighlightRow);
    Farm.myAccountsDataTable.subscribe("rowMouseoutEvent", Farm.myAccountsDataTable.onEventUnhighlightRow);

    Farm.myAccountsDataTable.subscribe('checkboxClickEvent', function(oArgs) {
      var elCheckbox = oArgs.target;
      var newValue = elCheckbox.checked;
      var record = this.getRecord(elCheckbox);
      var column = this.getColumn(elCheckbox);
      record.setData(column.key,newValue);
    });

    Farm.myAccountsDataTable.subscribe('renderEvent', function(oArgs) {
      if(YAHOO.util.Dom.get('failedFilter').checked) {
        Farm.myAccountsDataTable.showColumn(0);  // Selected
        Farm.myAccountsDataTable.showColumn(2);  // Producer
        Farm.myAccountsDataTable.hideColumn(3);  // Generated Date
        Farm.myAccountsDataTable.showColumn(4);  // Failed Date
        Farm.myAccountsDataTable.hideColumn(5);  // Fee
        Farm.myAccountsDataTable.showColumn(6);  // Note
        Farm.myAccountsDataTable.hideColumn(7);  // From CRA
      } else {
        if(YAHOO.util.Dom.get('ungeneratedFilter').checked) {
          Farm.myAccountsDataTable.showColumn(0);
          Farm.myAccountsDataTable.showColumn(2);
          Farm.myAccountsDataTable.hideColumn(3);
          Farm.myAccountsDataTable.hideColumn(4);
          Farm.myAccountsDataTable.hideColumn(5);
          Farm.myAccountsDataTable.hideColumn(6);
          Farm.myAccountsDataTable.hideColumn(7);
        } else {
          Farm.myAccountsDataTable.showColumn(0);
          Farm.myAccountsDataTable.showColumn(2);
          Farm.myAccountsDataTable.showColumn(3);
          Farm.myAccountsDataTable.hideColumn(4);
          Farm.myAccountsDataTable.showColumn(5);
          Farm.myAccountsDataTable.hideColumn(6);
          Farm.myAccountsDataTable.showColumn(7);
        }
      }
    });

    Farm.getNumberSelected = function () {
      var records = YAHOO.farm.myAccountsDataTable.getRecordSet().getRecords();
      var count = 0;
        for (i=0; i < records.length; i++) {
        if(records[i].getData().selectedInd == true) count++;
      }
      return count;
    }

    Farm.updateFilter = function ()  {
          // Reset sort
          var state = YAHOO.farm.myAccountsDataTable.getState();

          // Get filtered data
          Farm.myAccountsDataSource.sendRequest(null,{
              success : Farm.myAccountsDataTable.onDataReturnInitializeTable,
              failure : Farm.myAccountsDataTable.onDataReturnInitializeTable,
              scope   : Farm.myAccountsDataTable,
              argument: state
          });
    };

      YAHOO.util.Event.on('ungeneratedFilter','click',Farm.updateFilter);
      YAHOO.util.Event.on('generatedFilter','click',Farm.updateFilter);
      YAHOO.util.Event.on('failedFilter','click',Farm.updateFilter);
      YAHOO.util.Event.on('allFilter','click',Farm.updateFilter);
      YAHOO.util.Event.on('verifiedFilter','click',Farm.updateFilter);
      YAHOO.util.Event.on('unverifiedFilter','click',Farm.updateFilter);
      YAHOO.util.Event.on('enInProgressFilter','click',Farm.updateFilter);
      YAHOO.util.Event.on('enCompleteFilter','click',Farm.updateFilter);
      YAHOO.util.Event.on('pinFilter','keyup',Farm.updateFilter);
      YAHOO.util.Event.on('startDateFilter','keyup',Farm.updateFilter);
      YAHOO.util.Event.on('endDateFilter','keyup',Farm.updateFilter);
      YAHOO.util.Event.on('failedReasonFilter','change',Farm.updateFilter);

});

Farm.updateFilterIfCleared = function(e) {
  var $input = $(this);
  var oldValue = $input.val();

  if(oldValue == "") {
    return;
  }

  setTimeout(function() {
    var newValue = $input.val();
    if(newValue == "") {
      Farm.updateFilter();
    }
  }, 1);
}

$("#pinFilter").on("mouseup", Farm.updateFilterIfCleared);
$("#startDateFilter").on("mouseup", Farm.updateFilterIfCleared);
$("#endDateFilter").on("mouseup", Farm.updateFilterIfCleared);
</script>
