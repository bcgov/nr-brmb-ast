<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getImportVersion var="importVersion" scope="request"/>

<h1>
  <fmt:message key="Fifo.Jobs.Result"/>
</h1> 

<table class="details"> 
  <tr> 
    <th><fmt:message key="Type"/>:</th> 
    <td colspan="3"><c:out value="${importVersion.importClassDescription}"/></td> 
  </tr>
  <tr> 
    <th><fmt:message key="Import.File"/>:</th> 
    <td colspan="3"><c:out value="${importVersion.importFileName}"/></td> 
  </tr>
  <tr> 
    <th><fmt:message key="Description"/>:</th> 
    <td colspan="3"><c:out value="${importVersion.description}"/></td> 
  </tr> 
  <tr> 
    <th><fmt:message key="State"/>:</th> 
    <td colspan="3"><c:out value="${importVersion.importStateDescription}"/></td>
  </tr>
  <tr> 
    <th><fmt:message key="Last.Status.Message"/>:</th> 
    <td colspan="3"><c:out value="${importVersion.lastStatusMessage}"/></td>
  </tr>
  <tr> 
    <th><fmt:message key="Last.Status.Date"/>:</th> 
    <td colspan="3"><c:out value="${importVersion.lastStatusDate}"/></td>
  </tr>
</table> 

<p></p> 

<table class="details">
  <tr>
    <td>
      <i>
        <fmt:message key="text.import.refresh"/>
      </i>
    </td>
  </tr>
</table>

<p></p>

<a id="refreshButton" href="javascript:refreshPage()"><fmt:message key="Refresh"/></a>

<script type="text/javascript"> 
  new YAHOO.widget.Button("refreshButton");

  function refreshPage() {
    showProcessing();
    var farmAction = "farm254";
    document.location.href = farmAction + ".do?importVersionId=" + <c:out value="${importVersion.importVersionId}" />;
  }
</script> 