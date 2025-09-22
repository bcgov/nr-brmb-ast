<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp" %>

<u:getImportResults var="details" scope="request"/>

<c:set var="importStateCode" value="${details.importVersion.importStateCode}"/>

<h1>
  <c:choose>
    <c:when test="${details.importVersion.enrolment}">
      <fmt:message key="Enrolment.Result"/>
    </c:when>
    <c:when test="${details.importVersion.transfer}">
      <fmt:message key="Transfer.Result"/>
    </c:when>
    <c:when test="${details.importVersion.tipReport}">
      <fmt:message key="TIP.Report.Status"/>
    </c:when>
    <c:otherwise>
      <fmt:message key="Import.Result"/>
    </c:otherwise>
  </c:choose>
</h1> 

<table class="details"> 
  <tr> 
    <th style="width:100px !important"><fmt:message key="Type"/>:</th> 
    <td style="width:450px !important"><c:out value="${details.importVersion.importClassDescription}"/></td> 
  </tr>
  <c:if test="${ !details.importVersion.enrolment and !details.importVersion.transfer and !details.importVersion.tipReport}">
    <tr> 
      <th><fmt:message key="Import.File"/>:</th> 
      <td><c:out value="${details.importVersion.importFileName}"/></td> 
    </tr> 
  </c:if>
  <tr> 
    <th><fmt:message key="Description"/>:</th> 
    <td><c:out value="${details.importVersion.description}"/></td>
  </tr> 
  <tr> 
    <th><fmt:message key="State"/>:</th> 
    <td><c:out value="${details.importVersion.importStateDescription}"/></td>
  </tr>
  <c:if test="${details.importVersion.enrolment or details.importVersion.transfer}">
    <tr>
      <th><fmt:message key="Status"/>:</th> 
      <td colspan="3"><c:out value="${details.importVersion.lastStatusMessage}"/></td>
    </tr>
  </c:if>
  <c:if test="${ !details.importVersion.enrolment and !details.importVersion.transfer and !details.importVersion.tipReport}">
    <tr> 
      <th><fmt:message key="Number.of.Rows"/>:</th> 
      <td><fmt:formatNumber type="number" value="${details.numberOfItems}"/></td>
    </tr>
  </c:if>
  <c:if test="${details.importVersion.bpu or details.importVersion.fmv}">
    <tr> 
      <th><fmt:message key="New.Data"/>:</th> 
      <td><fmt:formatNumber type="number" value="${details.numberOfAdditions}"/></td>
    </tr>
    <tr> 
      <th><fmt:message key="Updates"/>:</th> 
      <td><fmt:formatNumber type="number" value="${details.numberOfUpdates}"/></td>
    </tr>
  </c:if>
  <c:if test="${details.importVersion.bpu}">
    <tr> 
      <th><fmt:message key="Value.Changes"/>:</th> 
      <td><fmt:formatNumber type="number" value="${details.numberOfValueUpdates}"/></td>
    </tr>
  </c:if>
</table>

<c:if test="${details.hasTopLevelWarnings}">
  <%@ include file="resultImportWarnings.jsp" %>
</c:if>

<p></p>
  <c:choose>
    <c:when test="${details.hasTopLevelErrors}">
      <%@ include file="resultImportErrors.jsp" %>
    </c:when>
    <c:otherwise>
     <c:if test="${details.importVersion.cra}">
        <c:choose>
         <c:when test="${form.tab == 'commoditiesTab'}">
           <%@ include file="resultCommoditiesTab.jsp" %>
         </c:when>
         <c:when test="${form.tab == 'productionUnitsTab'}">
           <%@ include file="resultProductionUnitsTab.jsp" %>
         </c:when>
         <c:otherwise>
           <%@ include file="resultAccountsTab.jsp" %>
         </c:otherwise>
        </c:choose>
      </c:if>
    </c:otherwise>
  </c:choose>  


<p></p> 
<c:choose>
  <c:when test="${details.importVersion.enrolment}">
    <u:yuiButton buttonLabel="Done" buttonId="doneButton" action="farm251"/>
  </c:when>
  <c:when test="${details.importVersion.transfer}">
    <u:yuiButton buttonLabel="Done" buttonId="doneButton" action="farm252"/>
  </c:when>
  <c:when test="${details.importVersion.tipReport}">
    <u:yuiButton buttonLabel="Done" buttonId="doneButton" action="farm661"/>
    <c:if test="${form.hasFileForDownload}">
      <u:yuiButton buttonLabel="Download.Reports" buttonId="downloadTipBatchButton" action="downloadTipBatch" urlParams="importVersionId=${form.importVersionId}"/>
      <p><span style="font-weight:bold;"><fmt:message key="Note"/>:</span> <fmt:message key="Download.Reports.note"/></p>
    </c:if>
  </c:when>
  <c:otherwise>
    <u:yuiButton buttonLabel="Done" buttonId="doneButton" action="farm250"/>
  </c:otherwise>
</c:choose>

<c:if test="${details.importVersion.transfer and importStateCode == 'IF'}">
  <u:yuiButton buttonLabel="Retry" buttonId="retryButton" action="retryTransfer" urlParams="importVersionId=${details.importVersion.importVersionId}"/>
</c:if>

<script> 
  function showUrl(aUrl) {
    showProcessing();
    document.location.href = aUrl;
  }
  
</script> 