<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<h1><fmt:message key="Import.Pins"/></h1>

<html:form action="importPins" styleId="importForm" enctype="multipart/form-data">
  <table class="inputform">
    <tr>
      <th><fmt:message key="Import.File"/>:</th>
      <td><html:file property="file" size="60" /></td>
    </tr>
  </table>
  <p></p>
</html:form>

<button id="importButton"><fmt:message key="Import.Pins"/></button>


<script type="text/javascript">
  new YAHOO.widget.Button("importButton", {onclick: {fn: submitImportForm}});

  function submitImportForm() {
    showProcessing();
    document.getElementById('importForm').submit();
  }
</script>