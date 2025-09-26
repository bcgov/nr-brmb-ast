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
  <fmt:message key="Municipality"/> <fmt:message key="Code"/>
</h1> 
<p></p>

<html:form action="saveMunicipalityCode" styleId="codeForm" method="post">
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
              <html:text property="code" size="30" maxlength="10"/>
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
      <tr>
        <th style="width:100px;vertical-align:middle !important"><fmt:message key="Regional.Offices"/>:</th>
        <td>
          <logic-el:iterate name="form" property="regionCodes" id="regionCode">
            <html-el:checkbox property="regionCodeSelection(${regionCode.code})"/>
            <c:out value="${regionCode.description}"/> <br />
          </logic-el:iterate>
        </td>
      </tr>
    </table>
  </fieldset>

  <div style="text-align:right;width:70%">
    <w:ifUserCanPerformAction action="editCodes">
      <a id="saveButton" href="#"><fmt:message key="Save"/></a>
      <c:if test="${!form.new}">
        <a id="deleteButton" href="#"><fmt:message key="Delete"/></a>
      </c:if>
    </w:ifUserCanPerformAction>
    <a id="cancelButton" href="#"><fmt:message key="Cancel"/></a>
  </div>

</html:form>


<script type="text/javascript">
  //<![CDATA[
  new YAHOO.widget.Button("cancelButton");
  function cancelFunc() {
    document.location.href = '<html:rewrite action="farm255"/>';
  }
  YAHOO.util.Event.addListener(document.getElementById("cancelButton"), "click", cancelFunc);
  //]]>
</script>

<w:ifUserCanPerformAction action="editCodes">
  <c:if test="${!form.new}">
    <script type="text/javascript">
      //<![CDATA[
      new YAHOO.widget.Button("deleteButton");
      function deleteFunc() {
        var form = document.getElementById('codeForm');
        form.action = "<html:rewrite action="deleteMunicipalityCode"/>";
        submitForm(form);
      }
      YAHOO.util.Event.addListener(document.getElementById("deleteButton"), "click", deleteFunc);
      //]]>
    </script>
  </c:if>
</w:ifUserCanPerformAction>

<w:ifUserCanPerformAction action="editCodes">
  <script type="text/javascript">
    //<![CDATA[
    new YAHOO.widget.Button("saveButton");
    new YAHOO.widget.Button("cancelButton");
    function submitFunc() { submitForm(document.getElementById('codeForm')); }
    YAHOO.util.Event.addListener(document.getElementById("saveButton"), "click", submitFunc);
  
    registerFormForDirtyCheck(document.getElementById("codeForm"));
    //]]>
  </script>
</w:ifUserCanPerformAction>
