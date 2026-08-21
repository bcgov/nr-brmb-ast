<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp" %>
<script type="text/javascript">
  //<![CDATA[
  
  
  function viewPin(pin, programYear, scenarioNumber) {
    window.open('<html:rewrite action="farm800"/>?pin=' + pin
        + '&year=' + programYear
        + (scenarioNumber ? '&scenarioNumber=' + scenarioNumber : '')
        + '&refresh=true',
        '_blank');
  }

  //]]>
</script>

<h1><fmt:message key="Chefs.title"/></h1>


<html:form action="chefsUpdateForm" styleId="updateForm" method="post" style="margin-top:10px;">
  <html:hidden property="submissionGuid"/>
  <html:hidden property="userFormType"/>
  <html:hidden property="formType"/>
  
  <table>
		<tr><td>Submission Guid:</td><td><a href="<c:out value="${form.submissionUrl}"/>" target="_blank"><c:out value="${form.submissionGuid}"/></a></td></tr>
		<tr>
      <td>PIN:</td>
      <td>
        <c:choose>
          <c:when test="${not empty form.participantPin and not empty form.programYear and form.pinExists}">
            <a href="#" onclick="viewPin(<c:out value="${form.participantPin}"/>, <c:out value="${form.programYear}"/>, <c:out value="${form.scenarioNumber}"/>)">
              <c:out value="${form.participantPin}"/>
            </a>
          </c:when>
          <c:otherwise>
            <c:out value="${form.participantPin}"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
		<tr><td>User Form Type:</td><td><c:out value="${form.userFormType}"/></td></tr>
		<tr><td>Form Type Description:</td><td><c:out value="${form.submission.formTypeDescription}"/></td></tr>
		<tr><td>Submission Id:</td><td><c:out value="${form.submission.submissionId}"/></td></tr>
		<tr><td>Validation Task Guid:</td>
    <c:if test="${form.validationTaskUrl != null}">
		    <td><a href="<c:out value="${form.validationTaskUrl}"/>" target="_blank"><c:out value="${form.submission.validationTaskGuid}"/></a></td>
    </c:if>
		</tr>
		<tr><td>Main Task Guid:</td>
    <c:if test="${form.mainTaskUrl != null}">
		    <td><a href="<c:out value="${form.mainTaskUrl}"/>" target="_blank"><c:out value="${form.submission.mainTaskGuid}"/></a></td>
    </c:if>
		</tr>
		<tr><td>Created Date:</td><td><fmt:formatDate value="${form.submission.createdDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td></tr>
		<tr><td>Updated Date:</td><td><fmt:formatDate value="${form.submission.updatedDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td></tr>
		<tr><td>Submission Status Code:</td>
				<td>
				  <div>
					  <html:select property="submissionStatusCode">
			      <html:optionsCollection name="server.list.chef.submssn.status.codes"/></html:select> 
				  </div>
        </td>
    </tr>
    <tr><td>
          <div style="margin-top:5px;">
            <u:yuiButton buttonLabel="Back" buttonId="doneButton" action="farm256"/>
          </div>
        </td>
        <td>
          <div style="margin-top:5px;">
	          <u:yuiButton buttonId="updateButton" buttonLabel="Chefs.update.status" formId="updateForm"/>
          </div>
        </td>
    </tr>
	</table>
  
</html:form>

<div style="margin-top:20px; font-weight:bold;">
<c:if test="${form.resourceJson == null}">
<div>Form submission not found.</div>
</c:if>
<c:if test="${form.submission.bceidFormInd == null}">
<div>BCeID Form Type is not set.</div>
</c:if>
</div>
    
<c:if test="${form.resourceJson != null}" >
<html:form action="chefsUploadForm" styleId="uploadForm" method="post" style="margin-top:10px;">
  <html:hidden property="submissionGuid"/>
  <html:hidden property="userFormType"/>
  <html:hidden property="formType"/>
   
  <div>
      <u:yuiButton buttonId="saveButton" buttonLabel="Chefs.retry.upload" formId="uploadForm"/>
      <u:dirtyFormCheck formId="uploadForm"/>
  </div>
</html:form>
<pre class="prettyprint" style="margin-top:10px;"><c:out value="${form.resourceJson}"/></pre>
</c:if>


