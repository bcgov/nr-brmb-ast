<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<w:ifUserCanPerformAction action="editCodes">
  <c:set var="canEdit" value="true"/>
</w:ifUserCanPerformAction>

<h1>
  <c:choose>
    <c:when test="${!canEdit}">
      <fmt:message key="View"/>
    </c:when>
    <c:otherwise>
      <fmt:message key="Edit"/>
    </c:otherwise>
  </c:choose>
  <fmt:message key="User"/>
</h1> 
<p></p>

<html:form action="saveUser" styleId="userForm" method="post">
  <html:hidden property="revisionCount"/>
  <html:hidden property="userGuid" />
  
  <fieldset style="width:70%">
    <table class="formInput" style="width:100%">
      <tr>
        <th style="width:100px"><fmt:message key="User"/>:</th>
        <td>
          <c:out value="${form.user.accountName}"/>
        </td>
      </tr>
      <tr>
        <th style="width:100px"><fmt:message key="Email.Address"/>:</th>
        <td>
          <c:out value="${form.user.emailAddress}"/>
        </td>
      </tr>
      <tr>
        <th style="width:100px;vertical-align:middle !important"><fmt:message key="Verifier"/>:</th>
        <td>
          <html:checkbox property="verifier"/>
        </td>
      </tr>
      <tr>
        <th style="width:100px;vertical-align:middle !important"><fmt:message key="Deleted"/>:</th>
        <td>
          <html:checkbox property="deleted"/>
        </td>
      </tr>
    </table>
  </fieldset>

  <div style="text-align:right;width:70%">
    <w:ifUserCanPerformAction action="editCodes">
      <a id="saveButton" href="#"><fmt:message key="Save"/></a>
    </w:ifUserCanPerformAction>
    <a id="cancelButton" href="#"><fmt:message key="Cancel"/></a>
  </div>

</html:form>


<script type="text/javascript">
  //<![CDATA[
  new YAHOO.widget.Button("cancelButton");
  function cancelFunc() {
    document.location.href = '<html:rewrite action="farm776"/>';
  }
  YAHOO.util.Event.addListener(document.getElementById("cancelButton"), "click", cancelFunc);
  //]]>
</script>

<w:ifUserCanPerformAction action="editCodes">
  <script type="text/javascript">
    //<![CDATA[
    new YAHOO.widget.Button("saveButton");
    function submitFunc() { submitForm(document.getElementById('userForm')); }
    YAHOO.util.Event.addListener(document.getElementById("saveButton"), "click", submitFunc);
  
    registerFormForDirtyCheck(document.getElementById("userForm"));
    //]]>
  </script>
</w:ifUserCanPerformAction>

