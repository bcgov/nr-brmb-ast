<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script src="yui/2.8.2r1/build/event/event-min.js"></script>
<script src="yui/2.8.2r1/build/connection/connection_core-min.js"></script>
<script src="yui/2.8.2r1/build/connection/connection-min.js"></script>

<script type="text/javascript">

  //
  // If the screen is redisplayed with struts validation errors then 
  // the exportUrl attribute will be null.
  //
  function onPageLoad(){
    <c:if test="${form.exportUrl != null}">
      var tokenFieldName = 'org.apache.struts.taglib.html.TOKEN';
      var tokenValue = $('[name="' + tokenFieldName + '"]').val();
      var url = '<c:out value="${form.exportUrl}" escapeXml="false"/>';

      if(url.indexOf('?') >= 0) {
        url = url + '&';
      } else {
        url = url + '?';
      }
      url = url + tokenFieldName + '=' + tokenValue;

      var transaction = YAHOO.util.Connect.asyncRequest('GET', url, {}, null);
      
      showProcessing();
      
      document.location.href = '<html:rewrite action="farm721"/>?exportType=' + document.getElementById("exportType").value;
    </c:if>
  }
  
  function previousReport() {
	  document.location.href = '<html:rewrite action="farm721"/>?exportType=' + document.getElementById("exportType").value;
  }
  
</script>

<h1><fmt:message key="Export"/></h1> 
<h2><fmt:message key="Export.Criteria"/></h2>

<html:form action="export720" styleId="export720Form" onsubmit="showProcessing()">
  <table class="searchcriteria">
    <tr>
      <th><fmt:message key="Export.Type"/>:</th>
      <td>
        <html:select styleId="exportType" property="exportType" onchange="exportChange()" >
          <option value="REPORT_600"><fmt:message key="Detailed.Scenario.Extract"/></option>
          <option value="STA"><fmt:message key="Statement.A.Extract"/></option>
          <option value="AAFM" selected>AAFM Extract</option>
          <option value="AAFMA">Analytical Data Extract</option>
        </html:select>
      </td>
    </tr>
    <tr> 
      <th><fmt:message key="Program.Year"/>:</th> 
      <td>
      	<html:select property="year">
      		<html:optionsCollection name="server.list.admin.years"/>
      	</html:select>
      	</td> 
    </tr>
  </table> 
  <p></p>
  <u:yuiButton buttonLabel="Generate.Export" buttonId="generateButton" formId="export720Form"/>
  <p><a href="javascript:previousReport();"><fmt:message key="Previously.Generated.Report"/></a></p>
</html:form>
