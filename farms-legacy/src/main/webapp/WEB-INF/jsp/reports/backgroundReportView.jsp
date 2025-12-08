<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript">

  function downloadFile(){
     document.location.href = '<html:rewrite action="backgroundReportDownload"/>?reportType=<c:out value="${form.reportType}"/>';
  }

</script>

<c:choose>
  <c:when test="${form.reportType eq 'REPORT_NATIONAL_SURVEILLANCE_STRATEGY'}">
    <c:set var="reportTitle"><fmt:message key="National.Surveillance.Strategy.Report"/></c:set>
    <c:set var="backButtonAction" value="farm650"/>
  </c:when>
  <c:when test="${form.reportType eq 'REPORT_ANALYTICAL_SURVEILLANCE_STRATEGY'}">
    <c:set var="reportTitle"><fmt:message key="Analytical.Surveillance.Strategy.Report"/></c:set>
    <c:set var="backButtonAction" value="farm655"/>
  </c:when>
</c:choose>

<h1><fmt:message key="Reports"/></h1> 

<h2><c:out value="${reportTitle}"/></h2>

<c:choose>
  <c:when test="${empty form.reportFileDate}">
    
    <c:choose>
      <c:when test="${not empty form.reportRequestDate}">
        <p><fmt:message key="background.report.not.completed"/></p>
        <table class="details">
          <tr>
            <th><fmt:message key="background.report.requestor"/>:</th>
            <td><c:out value="${form.requestorAccountName}"/></td>
          </tr>
          <tr>
            <th><fmt:message key="background.report.request.date"/>:</th>
            <td><c:out value="${form.reportRequestDate}"/></td>
          </tr>
        </table>

        <br />
        <u:yuiButton buttonLabel="Refresh" buttonId="refreshButton" action="backgroundReportView" urlParams="reportType=${form.reportType}"/>
      </c:when>
      <c:otherwise>
        <p><fmt:message key="background.report.not.requested"/></p>
      </c:otherwise>
    </c:choose>

  </c:when>
  <c:otherwise>

    <table class="details">
      <tr>
        <th><fmt:message key="File.Name"/>:</th>
        <td><c:out value="${form.reportFileName}"/></td>
      </tr>
      <tr>
        <th><fmt:message key="Date.Generated"/>:</th>
        <td><c:out value="${form.reportFileDate}"/></td>
      </tr>
    </table>
    <br />

    <u:yuiButton buttonLabel="Save" buttonId="downloadButton" function="downloadFile"/>

  </c:otherwise>
</c:choose>

<u:yuiButton buttonLabel="Back" buttonId="backButton" action="${backButtonAction}"/>
