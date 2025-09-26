<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getImportVersion var="importVersion" scope="request"/>

<h1>
  <c:choose>
    <c:when test="${importVersion.enrolment}">
      <fmt:message key="Enrolment.Status"/>
    </c:when>
    <c:when test="${importVersion.transfer}">
      <fmt:message key="Transfer.Status"/>
    </c:when>
    <c:when test="${importVersion.tipReport}">
      <fmt:message key="TIP.Report.Status"/>
    </c:when>
    <c:otherwise>
      <fmt:message key="Import.Status"/>
    </c:otherwise>
  </c:choose>
</h1> 

<table class="details"> 
  <tr> 
    <th><fmt:message key="Type"/>:</th> 
    <td colspan="3"><c:out value="${importVersion.importClassDescription}"/></td> 
  </tr>
  <c:if test="${ ! importVersion.enrolment and ! importVersion.transfer }">
    <tr> 
      <th><fmt:message key="Import.File"/>:</th> 
      <td colspan="3"><c:out value="${importVersion.importFileName}"/></td> 
    </tr>
  </c:if>
  <tr> 
    <th><fmt:message key="Description"/>:</th> 
    <td colspan="3"><c:out value="${importVersion.description}"/></td> 
  </tr> 
  <tr> 
    <th><fmt:message key="State"/>:</th> 
    <td colspan="3"><c:out value="${importVersion.importStateDescription}"/></td>
  </tr>
  
<c:if test="${importVersion.cra or importVersion.enrolment or importVersion.transfer or importVersion.tipReport}">
  <tr> 
    <th><fmt:message key="Last.Status.Message"/>:</th> 
    <td colspan="3"><c:out value="${importVersion.lastStatusMessage}"/></td>
  </tr>
  <tr> 
    <th><fmt:message key="Last.Status.Date"/>:</th> 
    <td colspan="3"><c:out value="${importVersion.lastStatusDate}"/></td>
  </tr>
</c:if>

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
    var farmAction;
    <c:choose>
      <c:when test="${importVersion.enrolment}">
        farmAction = "farm206";
      </c:when>
      <c:when test="${importVersion.transfer}">
        farmAction = "farm207";
      </c:when>
      <c:when test="${importVersion.tipReport}">
      farmAction = "farm208";
    </c:when>
      <c:otherwise>
        farmAction = "farm205";
      </c:otherwise>
    </c:choose>
    document.location.href = farmAction + ".do?importVersionId=" + <c:out value="${importVersion.importVersionId}" />;
  }
</script> 