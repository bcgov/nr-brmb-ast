<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript">
  function onPageLoad(){
    <c:if test="${form.reportUrl != null}">
      openNewWindow('<c:out value="${form.reportUrl}" escapeXml="false" />');
    </c:if>
  }
</script>

<h1><fmt:message key="Reports"/></h1>
<h2><fmt:message key="Report.Criteria"/></h2>

<html:form action="report620" styleId="reportForm" onsubmit="showProcessing()">
  <table class="searchcriteria">
    <tr>
      <th><fmt:message key="Report"/>:</th>
      <td>
        <html:select styleId="reportType" property="reportType" onchange="reportChange()">
          <option value="REPORT_610"><fmt:message key="Calculated.Benefits"/></option>
          <option value="REPORT_620" selected><fmt:message key="Submissions.by.SIN.CTN.BN"/></option>
          <option value="REPORT_NATIONAL_SURVEILLANCE_STRATEGY"><fmt:message key="National.Surveillance.Strategy.Report"/></option>
          <option value="REPORT_ANALYTICAL_SURVEILLANCE_STRATEGY"><fmt:message key="Analytical.Surveillance.Strategy.Report"/></option>
        </html:select>
      </td>
    </tr>
    <tr> 
      <th><fmt:message key="Program.Year"/>:</th> 
      <td><html:text property="year" maxlength="4"></html:text></td> 
    </tr> 
    <tr> 
      <th><fmt:message key="Name"/>:</th> 
      <td><html:text property="name"/></td> 
    </tr> 
    <tr> 
      <th><fmt:message key="SIN/CTN/BN"/>:</th> 
      <td><html:text property="sin"/></td> 
    </tr> 
    <tr> 
      <th><fmt:message key="Partnership.PIN"/>:</th> 
      <td><html:text property="pin"/></td> 
    </tr>
  </table>
  <p></p> 
  <u:yuiButton buttonLabel="Generate.Report" buttonId="generateReportButton" formId="reportForm"/>
</html:form>
