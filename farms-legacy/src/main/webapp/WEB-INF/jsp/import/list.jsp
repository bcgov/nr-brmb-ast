<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp" %>

<h1>
  <c:choose>
    <c:when test="${form.jobType == 'STANDARD'}">
      <fmt:message key="Import.Batch.Jobs"/>
      <c:set var="importedByLabel"><fmt:message key="Imported.By"/></c:set>
    </c:when>
    <c:when test="${form.jobType == 'ENROL'}">
      <fmt:message key="Enrolment.Batch.Jobs"/>
      <c:set var="importedByLabel"><fmt:message key="Generated.By"/></c:set>
    </c:when>
    <c:when test="${form.jobType == 'TRANSFER'}">
      <fmt:message key="Transfers"/>
      <c:set var="importedByLabel"><fmt:message key="Initiated.By"/></c:set>
    </c:when>
  </c:choose>
</h1>
<p></p>

<c:if test="${form.numSearchResults > 0}">

    <div class="searchresults">
      <div id="searchresults"></div>
    </div> 

    <script type="text/javascript">
     var data = {
          "recordsReturned": <c:out value="${form.numSearchResults}"/>,
          "totalRecords": <c:out value="${form.numSearchResults}"/>,
          "startIndex":0,
          "sort":null,
          "dir":"asc",
          "pageSize": 10,
          "records":[
            <c:forEach varStatus="loop" var="result" items="${form.searchResults}">
              {
                "importVersionId":"<c:out value="${result.importVersionId}"/>",
                "importedBy":"<c:out value="${result.importedBy}"/>",  
                "description":"<c:out value="${result.description}"/>",  
                "stateCode":"<c:out value="${result.stateCode}"/>",
                "stateDescription":"<c:out value="${result.stateDescription}"/>",
                "classCode":"<c:out value="${result.classCode}"/>",
                "updateDate":"<fmt:formatDate pattern="yyyy-MM-dd" value="${result.updateDate}" />"
              }<c:if test="${loop.index < (form.numSearchResults-1)}">,</c:if>
            </c:forEach> 
          ]
       };
         

       YAHOO.util.Event.onDOMReady(function() {
        var columnDefs = [
            {key:"classCode", label:"<fmt:message key="Type"/>", sortable:false},
            {key:"importedBy", label:"<c:out value="${importedByLabel}"/>", sortable:false},
            {key:"description", label:"<fmt:message key="Description"/>", sortable:false},
            {key:"updateDate", label:"<fmt:message key="Last.Updated"/>", sortable:false},
            {key:"stateDescription", label:"<fmt:message key="Status"/>", sortable:false}
        ];
  
        var dataSource = new YAHOO.util.DataSource(data);
        dataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
        dataSource.responseSchema = {
            resultsList: "records",
            fields: ["importVersionId","importedBy","description","stateCode","stateDescription","classCode","updateDate"]
        };
  
        var configs = {
            paginator: new YAHOO.widget.Paginator({ rowsPerPage: 10 })
        };
  
        function showSelectedRow(oArgs) {
          var oRecord = oArgs.record;
          var importVersionId = oRecord.getData("importVersionId");
          var stateCode = oRecord.getData("stateCode");
          var farmAction;
          // status page
          <c:choose>
            <c:when test="${form.jobType == 'STANDARD'}">
              farmAction = "farm205";
            </c:when>
            <c:when test="${form.jobType == 'ENROL'}">
              farmAction = "farm206";
            </c:when>
            <c:when test="${form.jobType == 'TRANSFER'}">
              farmAction = "farm207";
            </c:when>
          </c:choose>
          
          if(stateCode == "SF" || stateCode == "SC" || stateCode == "CAN") {
            // staging results
            <c:choose>
              <c:when test="${form.jobType == 'STANDARD'}">
                farmAction = "farm210";
              </c:when>
              <c:when test="${form.jobType == 'ENROL'}">
                farmAction = "farm211";
              </c:when>
              <c:when test="${form.jobType == 'TRANSFER'}">
                farmAction = "farm212";
              </c:when>
            </c:choose>
          } else if(stateCode == "IF" || stateCode == "IC" || stateCode == "IPC") {
            // import results
            <c:choose>
              <c:when test="${form.jobType == 'STANDARD'}">
                farmAction = "farm230";
              </c:when>
              <c:when test="${form.jobType == 'ENROL'}">
                farmAction = "farm231";
              </c:when>
              <c:when test="${form.jobType == 'TRANSFER'}">
                farmAction = "farm232";
              </c:when>
            </c:choose>
          }
          
          showProcessing();
          var url = farmAction + ".do?importVersionId=" + importVersionId;
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

<c:choose>
  <c:when test="${form.jobType == 'ENROL'}">
    <a id="backToEnrolmentsButton" href="<html:rewrite action="farm360"/>"><fmt:message key="Back"/></a>
    <script type="text/javascript">
      new YAHOO.widget.Button("backToEnrolmentsButton");
    </script> 
  </c:when>
  <c:when test="${form.jobType == 'TRANSFER'}">
    <a id="clearSuccessfulTransfers" href="<html:rewrite action="clearSuccessfulTransfers"/>"><fmt:message key="Clear.Successful.Transfers"/></a>
    <script type="text/javascript">
      new YAHOO.widget.Button("clearSuccessfulTransfers");
    </script> 
  </c:when>
  <c:otherwise>
  </c:otherwise>
</c:choose>

<c:if test="${form.newImportAllowed}">
  <a id="importButton" href="<html:rewrite action="farm200"/>"><fmt:message key="New.Import"/></a>

  <script type="text/javascript">
    new YAHOO.widget.Button("importButton");
  </script> 
</c:if>
