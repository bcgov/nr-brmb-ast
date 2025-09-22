<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript">

  function downloadFile(){
     document.location.href = '<html:rewrite action="aafmExtractDownload"/>';
  }

</script>

<h1><fmt:message key="Export"/></h1>
 
 <c:choose>
  <c:when test="${'AAFMA' == form.exportType}">
	<h2><fmt:message key="AAFMA.Extract.File"/></h2>
  </c:when>
  <c:otherwise>
  	<h2><fmt:message key="AAFM.Extract.File"/></h2>
  </c:otherwise>
  
</c:choose>
 

<c:choose>
  <c:when test="${empty form.exportFileDate}">

    <c:choose>
      <c:when test="${not empty form.reportRequestDate}">
        <p><fmt:message key="aafm.extract.not.completed"/></p>
        <table class="details">
          <tr>
            <th><fmt:message key="aafm.extract.requestor"/>:</th>
            <td><c:out value="${form.requestorAccountName}"/></td>
          </tr>
          <tr>
            <th><fmt:message key="aafm.extract.request.date"/>:</th>
            <td><c:out value="${form.reportRequestDate}"/></td>
          </tr>
        </table>

        <br />
        <u:yuiButton buttonLabel="Refresh" buttonId="refreshButton" action="farm721" urlParams="exportType=${form.exportType}"/>
      </c:when>
      <c:otherwise>
        <p><fmt:message key="aafm.extract.not.requested"/></p>
      </c:otherwise>
    </c:choose>

  </c:when>
  <c:otherwise>

    <table class="details">
      <tr>
        <th><fmt:message key="File.Name"/>:</th>
        <td><c:out value="${form.exportFileName}"/></td>
      </tr>
      <tr>
        <th><fmt:message key="Date.Generated"/>:</th>
        <td><c:out value="${form.exportFileDate}"/></td>
      </tr>
    </table>
    <br />

   <%-- <u:yuiButton buttonLabel="Save" buttonId="downloadButton" function="downloadFile"/> --%>
	<u:yuiButton buttonLabel="Save" buttonId="downloadButton" action="aafmExtractDownload" urlParams="exportType=${form.exportType}"/>
  </c:otherwise>
</c:choose>

<c:choose>
  <c:when test="${'AAFMA' == form.exportType}">
	<u:yuiButton buttonLabel="Back" buttonId="backButton" action="farm719"/>
  </c:when>
  <c:otherwise>
  	<u:yuiButton buttonLabel="Back" buttonId="backButton" action="farm720"/>
  </c:otherwise>
  
</c:choose>
