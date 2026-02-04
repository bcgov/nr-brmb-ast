<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script>
  function onPageLoad(){
    <c:if test="${form.exportUrl != null}">
      document.location.href = '<c:out value="${form.exportUrl}"/>';
    </c:if>
  }
</script>

<h1><fmt:message key="Export"/></h1> 
<h2><fmt:message key="Export.Criteria"/></h2>

<html:form action="export601" styleId="exportForm" onsubmit="showProcessing()">
  <table class="searchcriteria">
    <tr>
      <th><fmt:message key="Export.Type"/>:</th>
      <td>
        <html:select styleId="exportType" property="exportType" onchange="exportChange()">
          <option value="REPORT_600"><fmt:message key="Detailed.Scenario.Extract"/></option>
          <option value="STA" selected><fmt:message key="Statement.A.Extract"/></option>
          <option value="AAFM">AAFM Extract</option>
          <option value="AAFMA">Analytical Data Extract</option>
        </html:select>
      </td>
    </tr>
    <tr> 
      <th><fmt:message key="Program.Year"/>:</th> 
      <td>
        <html:select property="year">
          <html:optionsCollection property="yearSelectOptions"/>
        </html:select>
      </td> 
    </tr>
  </table> 
  <p></p> 
  <u:yuiButton buttonLabel="Generate.Report" buttonId="generateReportButton" formId="exportForm"/>
</html:form>

