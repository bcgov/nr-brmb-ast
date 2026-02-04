<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<c:if test="${form.tipsParameter}">
  <w:ifUserCanPerformAction action="editTipCodes">
    <c:set var="canEdit" value="true"/>
  </w:ifUserCanPerformAction>
</c:if>

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
  <fmt:message key="Parameter"/>
</h1>


<html:form action="/saveConfigParam" styleId="codeForm" method="post">
  <html:hidden property="new"/>
  <html:hidden property="id"/>

  <fieldset style="width:95%">
    <table class="formInput" style="width:100%">
      <tr>
        <th style="width:100px;"><fmt:message key="Name"/>:</th>
        <td>
          <html:hidden property="name"/>
          <c:out value="${form.name}"/>
        </td>
      </tr>
      <tr>
        <th style="width:100px;"><fmt:message key="Type"/>:</th>
        <td>
          <html:hidden property="type"/>
          <html:hidden property="typeDescription"/>
          <c:out value="${form.typeDescription}"/>
        </td>
      </tr>     
      <tr>
        <th style="width:100px;"><fmt:message key="Value"/>:</th>
        <td>
          <c:choose>
            <c:when test="${canEdit and form.sensitiveDataInd}">
              <div style="float:left; width:98%;">            
                <html:password property="value" maxlength="4000"/>
              </div>
            </c:when>
            <c:when test="${canEdit}">
              <div style="float:left; width:98%;">            
                <html:text property="value" maxlength="4000"/>
              </div>
            </c:when>
            <c:otherwise>
              <html:hidden property="value"/>
              <c:out value="${form.value}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
    </table>
  </fieldset>

  <div style="text-align:right; width:70%;">
    <c:if test="${canEdit}">
      <u:yuiButton buttonId="saveButton" buttonLabel="Save" formId="codeForm"/>
      <u:dirtyFormCheck formId="codeForm"/>
    </c:if>
    <u:yuiButton buttonId="cancelButton" buttonLabel="Cancel" action="farm750"/>
  </div>

</html:form>