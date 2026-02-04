<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp" %>

<u:getStagingResults var="details" scope="request"/>

<c:set var="importStateCode" value="${details.importVersion.importStateCode}"/>

<h1><fmt:message key="Staging.Results"/></h1> 

<table class="details"> 
  <tr> 
    <th><fmt:message key="Type"/>:</th> 
    <td colspan="3"><c:out value="${details.importVersion.importClassDescription}"/></td> 
  </tr>
  <c:if test="${ ! details.importVersion.enrolment }">
    <tr> 
      <th><fmt:message key="Import.File"/>:</th> 
      <td colspan="3"><c:out value="${details.importVersion.importFileName}"/></td> 
    </tr> 
  </c:if>
  <tr> 
    <th><fmt:message key="Description"/>:</th> 
    <td colspan="3"><c:out value="${details.importVersion.description}"/></td> 
  </tr> 
  <tr> 
    <th><fmt:message key="State"/>:</th> 
    <td colspan="3"><c:out value="${details.importVersion.importStateDescription}"/></td>
  </tr>
  <c:if test="${details.importVersion.enrolment}">
    <tr>
      <th><fmt:message key="Status"/>:</th> 
      <td colspan="3"><c:out value="${details.importVersion.lastStatusMessage}"/></td>
    </tr>
  </c:if>
  <tr> 
    <th><fmt:message key="Errors"/>:</th> 
    <td colspan="3"><fmt:formatNumber type="number" value="${details.numberOfErrors}"/></td>
  </tr>

<c:choose>
  <c:when test="${details.importVersion.cra or details.importVersion.enrolment}">
    <tr> 
      <th><fmt:message key="Warnings"/>:</th> 
      <td colspan="3"><fmt:formatNumber type="number" value="${details.numberOfWarnings}"/></td>
    </tr>
  </c:when>
  <c:otherwise>
    <tr> 
      <th><fmt:message key="Number.of.Rows"/>:</th> 
      <td colspan="3"><fmt:formatNumber type="number" value="${details.numberOfItems}"/></td>
    </tr>
  </c:otherwise>
</c:choose>

<c:if test="${details.importVersion.enrolment}">
  <tr> 
    <th><fmt:message key="Number.of.PINs"/>:</th> 
    <td colspan="3"><fmt:formatNumber type="number" value="${details.numberOfItems}"/></td>
  </tr>
</c:if>

</table> 

<p></p> 

<c:if test="${details.numberOfErrors + details.numberOfWarnings > 0}">

    <div class="searchresults">
      <div id="searchresults"></div>
    </div> 

    <script type="text/javascript">
      var data = {
          "recordsReturned": <c:out value="${details.numberOfErrors + details.numberOfWarnings}"/>,
          "totalRecords": <c:out value="${details.numberOfErrors + details.numberOfWarnings}"/>,
          "startIndex":0,
          "sort":null,
          "dir":"asc",
          "pageSize": 10,
          "records":[
            <c:forEach varStatus="loop" var="result" items="${details.errors}">
              {
                "severity": "<fmt:message key="Error"/>",
                <c:choose>
                  <c:when test="${details.importVersion.enrolment}">
                    "pin":"<c:out value="${result.pin}"/>",
                    "message":"<c:out value="${result.failedReason}"/>"
                  </c:when>
                  <c:otherwise>
                    "fileNumber":"<c:out value="${result.fileNumber}"/>",
                    "lineNumber":"<c:out value="${result.lineNumber}"/>",  
                    "message":"<c:out value="${result.message}" />"
                  </c:otherwise>
                </c:choose>
              }<c:if test="${loop.index < (details.numberOfErrors-1)}">,</c:if>
            </c:forEach>
            <c:if test="${details.numberOfErrors > 0 && details.numberOfWarnings > 0}">,</c:if>
            <c:forEach varStatus="loop" var="result" items="${details.warnings}">
              {
                "severity": "<fmt:message key="Warning"/>",
                <c:choose>
                  <c:when test="${details.importVersion.enrolment}">
                    "pin":"<c:out value="${result.pin}"/>",
                    "message":"<c:out value="${result.failedReason}"/>"
                  </c:when>
                  <c:otherwise>
                    "fileNumber":"<c:out value="${result.fileNumber}"/>",
                    "lineNumber":"<c:out value="${result.lineNumber}"/>",  
                    "message":"<c:out value="${result.message}"/>"
                  </c:otherwise>
                </c:choose>
              }<c:if test="${loop.index < (details.numberOfWarnings-1)}">,</c:if>
            </c:forEach>
          ]
       };
         

       YAHOO.util.Event.onDOMReady(function() {
        var columnDefs = [
            {
              key:"severity",  
              label:"<fmt:message key="Severity"/>", 
              sortable:false,
              formatter: function(el, oRecord, oColumn, oData) { 
                if(oData == "<fmt:message key="Error"/>") {
                  el.innerHTML = '<img src="images/error.png" title="<fmt:message key="Error"/>">';
                } else {
                  el.innerHTML = '<img src="images/bcgov_warning.gif" title="Warning">'; 
                }
              } 
            },
            <c:choose>
              <c:when test="${details.importVersion.enrolment}">
                {key:"pin", label:"PIN", sortable:false},
                {key:"message",    label:"Message", sortable:false}
              </c:when>
              <c:when test="${details.importVersion.cra}">
                {key:"fileNumber", label:"File", sortable:false},
                {key:"lineNumber", label:"Line", sortable:false},
                {key:"message",    label:"Message", sortable:false}
              </c:when>
              <c:otherwise>
                {key:"lineNumber", label:"Line", sortable:false},
                {key:"message",    label:"Message", sortable:false}
              </c:otherwise>
            </c:choose>
        ];
  
        var dataSource = new YAHOO.util.DataSource(data);
        dataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
        dataSource.responseSchema = {
            resultsList: "records",
            <c:choose>
              <c:when test="${details.importVersion.enrolment}">
                fields: ["severity", "pin", "message"]
              </c:when>
              <c:otherwise>
                fields: ["severity", "fileNumber","lineNumber","message"]
              </c:otherwise>
            </c:choose>
        };
  
        var configs = {
            paginator: new YAHOO.widget.Paginator({ rowsPerPage: 10 })
        };
  
        var dataTable = new YAHOO.widget.DataTable("searchresults", columnDefs, dataSource, configs);
      });
    </script> 

</c:if>

<p></p> 

<c:choose>
  <c:when test="${details.importVersion.enrolment}">
    <c:set var="confirmButtonLabel"><fmt:message key="Confirm.Enrolments"/></c:set> 
    <c:set var="cancelButtonLabel"><fmt:message key="Cancel.Enrolments"/></c:set> 
  </c:when>
  <c:otherwise>
    <c:set var="confirmButtonLabel"><fmt:message key="Confirm.Import"/></c:set> 
    <c:set var="cancelButtonLabel"><fmt:message key="Cancel.Import"/></c:set> 
  </c:otherwise>
</c:choose>


<c:choose>
  <c:when test='${importStateCode == "SC"}'>
    <a id="confirmButton" href="javascript:confirmImport()"><c:out value="${confirmButtonLabel}"/></a>
    <a id="cancelButton" href="javascript:cancelImport()"><c:out value="${cancelButtonLabel}"/></a> 
  </c:when>
  <c:when test='${importStateCode == "SF"}'>
    <a id="cancelButton" href="javascript:cancelImport()"><c:out value="${cancelButtonLabel}"/></a>
  </c:when>
  <c:when test='${importStateCode == "CAN"}'>
    <c:choose>
      <c:when test="${details.importVersion.enrolment}">
        <a id="doneButton" href="<html:rewrite action="farm251"/>"><fmt:message key="Done"/></a>
      </c:when>
      <c:when test="${details.importVersion.tipReport}">
        <a id="doneButton" href="<html:rewrite action="farm661"/>"><fmt:message key="Done"/></a>
      </c:when>
      <c:otherwise>
        <a id="doneButton" href="<html:rewrite action="farm250"/>"><fmt:message key="Done"/></a>
      </c:otherwise>
    </c:choose>
  </c:when>
  <c:otherwise>
  </c:otherwise>
</c:choose>


<script type="text/javascript"> 
  new YAHOO.widget.Button("confirmButton");
  new YAHOO.widget.Button("cancelButton");
  new YAHOO.widget.Button("doneButton");
  
  function confirmImport() {
    <c:choose>
      <c:when test="${details.importVersion.enrolment}">
        var msg = "<fmt:message key="enrolment.confirm.text"/>";
      </c:when>
      <c:otherwise>
        var msg = "<fmt:message key="import.confirm.text"/>";
      </c:otherwise>
    </c:choose>
    
    if( confirm(msg) ) {
      showProcessing();
      <c:choose>
        <c:when test="${details.importVersion.enrolment}">
          document.location.href = "confirmEnrolmentImport.do?importVersionId=" + <c:out value="${form.importVersionId}" />;
        </c:when>
        <c:otherwise>
          document.location.href = "confirmImport.do?importVersionId=" + <c:out value="${form.importVersionId}" />;
        </c:otherwise>
      </c:choose>
    }
  }
  
  function cancelImport() {
    <c:choose>
      <c:when test="${details.importVersion.enrolment}">
        var msg = "<fmt:message key="enrolment.cancel.confirm.text"/>";
      </c:when>
      <c:otherwise>
        var msg = "<fmt:message key="import.cancel.confirm.text"/>";
      </c:otherwise>
    </c:choose>
    
    if( confirm(msg) ) {
      showProcessing();
      <c:choose>
        <c:when test="${details.importVersion.enrolment}">
          document.location.href = "cancelEnrolmentImport.do?importVersionId=" + <c:out value="${form.importVersionId}" />;
        </c:when>
        <c:otherwise>
        document.location.href = "cancelImport.do?importVersionId=" + <c:out value="${form.importVersionId}" />;
        </c:otherwise>
      </c:choose>
    }
  }
</script> 