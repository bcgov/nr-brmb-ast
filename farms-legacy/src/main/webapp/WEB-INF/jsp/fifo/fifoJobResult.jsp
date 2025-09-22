<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp" %>

<u:getImportResults var="details" scope="request"/>

<c:set var="importStateCode" value="${details.importVersion.importStateCode}"/>

<h1>
  <fmt:message key="Fifo.Jobs.Result"/>
</h1> 

<table class="details"> 
  <tr> 
    <th style="width:100px !important"><fmt:message key="Type"/>:</th> 
    <td style="width:450px !important"><c:out value="${details.importVersion.importClassDescription}"/></td> 
  </tr>
    <tr> 
      <th><fmt:message key="Import.File"/>:</th> 
      <td><c:out value="${details.importVersion.importFileName}"/></td> 
    </tr> 
  <tr> 
    <th><fmt:message key="Description"/>:</th> 
    <td><c:out value="${details.importVersion.description}"/></td>
  </tr> 
  <tr> 
    <th><fmt:message key="State"/>:</th> 
    <td><c:out value="${details.importVersion.importStateDescription}"/></td>
  </tr>
  <tr>
    <th><fmt:message key="Status"/>:</th> 
    <td colspan="3"><c:out value="${details.importVersion.lastStatusMessage}"/></td>
  </tr>
  <tr> 
    <th><fmt:message key="Number.of.Participants"/>:</th> 
    <td><fmt:formatNumber type="number" value="${form.numFifoItemResults}"/></td>
  </tr>
  <c:if test="${! empty form.fifoResults.unexpectedError}">
   <tr> 
    <th><fmt:message key="Unexpected.Error"/>:</th> 
    <td><c:out value="${form.fifoResults.unexpectedError}"/></td>
  </tr>
  </c:if>
</table>

<p></p> 
<u:yuiButton buttonLabel="Done" buttonId="doneButton" action="farm259"/>

<c:if test="${details.importVersion.transfer and importStateCode == 'IF'}">
  <u:yuiButton buttonLabel="Retry" buttonId="retryButton" action="retryTransfer" urlParams="importVersionId=${details.importVersion.importVersionId}"/>
</c:if>

<script> 
  function showUrl(aUrl) {
    showProcessing();
    document.location.href = aUrl;
  }
  
</script> 
<p></p> 
<c:if test="${form.numFifoItemResults > 0}">

    <div class="searchresults">
      <div id="searchresults"></div>
    </div> 

    <script type="text/javascript">
     var data = {
          "recordsReturned": <c:out value="${form.numFifoItemResults}"/>,
          "totalRecords": <c:out value="${form.numFifoItemResults}"/>,
          "startIndex":0,
          "sort":null,
          "dir":"asc",
          "pageSize": 10,
          "records":[
            <c:forEach varStatus="loop" var="result" items="${form.fifoResults.fifoItemResults}">
              {
                "clientName":"<c:out value="${result.clientName}"/>",  
                "participantPin":"<c:out value="${result.participantPin}"/>",
                "programYear":"<c:out value="${result.programYear}"/>",  
                "estimatedBenefit":"<c:out value="${result.estimatedBenefit}"/>",
                "scenarioStateCodeDesc":"<c:out value="${result.scenarioStateCodeDesc}"/>",
                "scenarioNumber":"<c:out value="${result.scenarioNumber}"/>",
                "errorMessages":"<c:forEach var="err" items="${result.errorMessages}"><c:out value="${err}" /><br/></c:forEach>",
              }<c:if test="${loop.index < (form.numFifoItemResults-1)}">,</c:if>
            </c:forEach> 
          ]
       };
         

       YAHOO.util.Event.onDOMReady(function() {
        var columnDefs = [
            {key:"clientName", label:"<fmt:message key="Name"/>", sortable:false},
            {key:"participantPin", label:"<fmt:message key="PIN"/>", sortable:false},
            {key:"programYear", label:"<fmt:message key="Year"/>", sortable:false},
            {key:"estimatedBenefit", label:"<fmt:message key="Estimated.Benefit"/>", sortable:false},
            {key:"errorMessages", label:"<fmt:message key="Error.Messages"/>", sortable:false},
            {key:"scenarioStateCodeDesc", label:"<fmt:message key="Status"/>", sortable:false}
        ];
  
        var dataSource = new YAHOO.util.DataSource(data);
        dataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
        dataSource.responseSchema = {
            resultsList: "records",
            fields: ["participantPin","programYear","clientName","estimatedBenefit","scenarioStateCodeDesc","scenarioNumber","errorMessages"]
        };
  
        var configs = {
            paginator: new YAHOO.widget.Paginator({ rowsPerPage: 10 })
        };
  
        function showSelectedRow(oArgs) {
          var oRecord = oArgs.record;
          var year = oRecord.getData("programYear");
          var pin = oRecord.getData("participantPin");
          var scenarioNumber = oRecord.getData("scenarioNumber");
          var farmAction = "farm830";
          
          showProcessing();
          var url = farmAction + ".do?pin=" + pin + "&year=" +  year + "&scenarioNumber=" + scenarioNumber + "&refresh=true"
          document.location.href = url;
        };
  
        var dataTable = new YAHOO.widget.DataTable("searchresults", columnDefs, dataSource, configs);
        dataTable.subscribe("rowMouseoverEvent", dataTable.onEventHighlightRow);
        dataTable.subscribe("rowMouseoutEvent", dataTable.onEventUnhighlightRow);
        dataTable.subscribe("rowClickEvent", dataTable.onEventSelectRow);
        dataTable.subscribe("rowSelectEvent", showSelectedRow);
      });
    </script> 

</c:if>

