<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<w:ifUserCanPerformAction action="editCodes">
  <c:set var="canEdit" value="true"/>
</w:ifUserCanPerformAction>

<h1>
  <c:choose>
    <c:when test="${!canEdit}">
      <fmt:message key="View"/>
    </c:when>
    <c:when test="${form.new}">
      <fmt:message key="Create.New"/>
    </c:when>
    <c:otherwise>
      <fmt:message key="Edit"/>
    </c:otherwise>
  </c:choose>
  <fmt:message key="${form.codeTable}"/> <fmt:message key="Code"/>
</h1> 
<p></p>

<html:form action="saveCode" styleId="codeForm" method="post">
  <html:hidden property="codeTable"/>
  <html:hidden property="new"/>
  <html:hidden property="revisionCount"/>
  
  <c:if test="${!form.new}">
    <html:hidden property="code"/>
  </c:if>

  <fieldset style="width:70%">
    <table class="formInput" style="width:100%">
      <tr>
        <th style="width:100px"><fmt:message key="Code"/>:</th>
        <td>
          <c:choose>
            <c:when test="${form.new}">
              <div style="float:left;width:100px">
                <html:text property="code" size="30" maxlength="10"/>
              </div>
            </c:when>
            <c:otherwise>
              <c:out value="${form.code}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
      <tr>
        <th style="width:100px"><fmt:message key="Description"/>:</th>
        <td>
          <c:choose>
            <c:when test="${canEdit}">
              <html:text property="description" size="80" maxlength="256"/>
            </c:when>
            <c:otherwise>
              <c:out value="${form.description}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
    </table>
  </fieldset>

  <div style="text-align:right;width:70%">
    <w:ifUserCanPerformAction action="editCodes">
      <u:yuiButton buttonId="saveButton" buttonLabel="Save" formId="codeForm"/>
      <c:if test="${!form.new and form.canDelete}">
        <u:yuiButton buttonId="deleteButton" buttonLabel="Delete" formId="codeForm"
           action="deleteCode"/>
      </c:if>
    </w:ifUserCanPerformAction>
    <u:yuiButton buttonId="cancelButton" buttonLabel="Cancel" action="farm265"
       urlParams="codeTable=${form.codeTable}"/>
  </div>

</html:form>
