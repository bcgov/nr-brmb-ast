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
  <fmt:message key="Farm.Type.Detailed.Code"/>
</h1> 
<p></p>

<html:form action="saveSectorDetailCode" styleId="codeForm" method="post">
  <html:hidden property="new"/>
  <html:hidden property="revisionCount"/>

  <fieldset style="width:70%">
    <table class="formInput" style="width:100%">
      <tr>
        <th style="width:100px"><fmt:message key="Farm.Type"/>:</th>
        <td>
          <c:choose>
            <c:when test="${canEdit && !form.mixed}">
              <html:select property="sectorCode" styleId="sectorCode">
                <option value=""></option>
                <html:optionsCollection name="server.list.sectors"/>
              </html:select>
            </c:when>
            <c:otherwise>
              <html:hidden property="sectorCode"/>
              <c:out value="${form.sectorCodeDescription}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
      <tr>
        <th style="width:100px"><fmt:message key="Farm.Type.Detailed.Code"/>:</th>
        <td>
          <c:choose>
            <c:when test="${form.new}">
              <div style="float:left;width:100px">
                <html:text property="sectorDetailCode" size="30" maxlength="10"/>
              </div>
            </c:when>
            <c:otherwise>
              <html:hidden property="sectorDetailCode"/>
              <c:out value="${form.sectorDetailCode}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
      <tr>
        <th style="width:100px"><fmt:message key="Farm.Type.Detailed.Description"/>:</th>
        <td>
          <c:choose>
            <c:when test="${canEdit}">
              <html:text property="sectorDetailCodeDescription" size="80" maxlength="256"/>
            </c:when>
            <c:otherwise>
              <c:out value="${form.sectorDetailCodeDescription}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
    </table>
  </fieldset>

  <div style="text-align:right;width:70%">
    <w:ifUserCanPerformAction action="editCodes">
      <u:yuiButton buttonId="saveButton" buttonLabel="Save" formId="codeForm"/>
      <c:if test="${!form.new && !form.mixed}">
        <u:yuiButton buttonId="deleteButton" buttonLabel="Delete" formId="codeForm" action="deleteSectorDetailCode"/>
      </c:if>
      <u:dirtyFormCheck formId="codeForm"/>
    </w:ifUserCanPerformAction>
    <u:yuiButton buttonId="cancelButton" buttonLabel="Cancel" action="farm774"/>
  </div>

</html:form>
