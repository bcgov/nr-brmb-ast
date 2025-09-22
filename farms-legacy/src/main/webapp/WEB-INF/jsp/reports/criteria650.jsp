<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script src="yui/2.8.2r1/build/event/event-min.js"></script>
<script src="yui/2.8.2r1/build/connection/connection_core-min.js"></script>
<script src="yui/2.8.2r1/build/connection/connection-min.js"></script>

<script type="text/javascript">

  //
  // If the screen is redisplayed with struts validation errors then 
  // the reportUrl attribute will be null.
  //
  function onPageLoad(){
    <c:if test="${form.reportUrl != null}">
      var tokenFieldName = 'org.apache.struts.taglib.html.TOKEN';
      var tokenValue = $('[name="' + tokenFieldName + '"]').val();
      var url = '<c:out value="${form.reportUrl}" escapeXml="false"/>';
      if(url.indexOf('?') >= 0) {
        url = url + '&';
      } else {
        url = url + '?';
      }
      url = url + tokenFieldName + '=' + tokenValue;
      var transaction = YAHOO.util.Connect.asyncRequest('GET', url, {}, null);
      
      showProcessing();
      
      document.location.href = '<html:rewrite action="backgroundReportView"/>?reportType=REPORT_NATIONAL_SURVEILLANCE_STRATEGY';
    </c:if>
  }
  
</script>

<h1><fmt:message key="Reports"/></h1> 
<h2><fmt:message key="Report.Criteria"/></h2>

<html:form action="report650" styleId="report650Form" onsubmit="showProcessing()">
  <table class="searchcriteria">
    <tr>
      <th><fmt:message key="Report"/>:</th>
      <td>
        <html:select styleId="reportType" property="reportType" onchange="reportChange()">
          <option value="REPORT_610"><fmt:message key="Calculated.Benefits"/></option>
          <option value="REPORT_620"><fmt:message key="Submissions.by.SIN.CTN.BN"/></option>
          <option value="REPORT_NATIONAL_SURVEILLANCE_STRATEGY" selected><fmt:message key="National.Surveillance.Strategy.Report"/></option>
          <option value="REPORT_ANALYTICAL_SURVEILLANCE_STRATEGY"><fmt:message key="Analytical.Surveillance.Strategy.Report"/></option>
        </html:select>
      </td>
    </tr>
    <tr> 
      <th><fmt:message key="Program.Year"/>:</th> 
      <td><html:text property="year" maxlength="4"></html:text></td> 
    </tr> 
  </table> 
  <p></p> 
  <u:yuiButton buttonLabel="Generate.Report" buttonId="generateButton" formId="report650Form"/>
  <p>
    <a href="<html:rewrite action="backgroundReportView"/>?reportType=REPORT_NATIONAL_SURVEILLANCE_STRATEGY">
      <fmt:message key="Previously.Generated.Report"/>
    </a>
  </p> 
</html:form>
