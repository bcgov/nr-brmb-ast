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
  <fmt:message key="Fruit.Veg.Code"/>
</h1>


<html:form action="/saveFruitVegCodeItem" styleId="codeForm" method="post">
  <html:hidden property="new"/>

  <fieldset style="width:70%">
    <table class="formInput" style="width:100%">
      <tr>
        <th style="width:100px"><fmt:message key="Fruit.Veg.Code"/>:</th>
        <td>
          <c:choose>
            <c:when test="${form.new}">
              <div style="float:left;width:100px">
                <html:text property="name" size="10" maxlength="10"/>
              </div>
            </c:when>
            <c:otherwise>
              <html:hidden property="name"/>
              <c:out value="${form.name}"/>
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
      <tr>
        <th style="width:100px"><fmt:message key="Revenue.Variance.Limit"/>:</th>
        <td>
          <c:choose>
            <c:when test="${canEdit}">
              <html:text property="varianceLimit" size="50" maxlength="20"/>
            </c:when>
            <c:otherwise>
              <c:out value="${form.varianceLimit}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>        
    </table>
  </fieldset>

  <div style="text-align:right;width:70%">
    <w:ifUserCanPerformAction action="editCodes">
      <u:yuiButton buttonId="saveButton" buttonLabel="Save" formId="codeForm"/>
      <c:if test="${!form.new}">
        <u:yuiButton buttonId="deleteButton" buttonLabel="Delete" formId="codeForm" action="deleteFruitVegCodeItem" confirmMessage="delete.fruit.veg.item.warning"/>
      </c:if>
      <u:dirtyFormCheck formId="farmItemForm"/>
    </w:ifUserCanPerformAction>
    <u:yuiButton buttonId="cancelButton" buttonLabel="Cancel" action="farm730"/>
  </div>

</html:form>