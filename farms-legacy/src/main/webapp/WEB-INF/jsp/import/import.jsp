<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<h1><fmt:message key="Import"/></h1>

<html:form action="importData" styleId="importForm" enctype="multipart/form-data">
  <table class="inputform">
    <tr>
      <th><fmt:message key="Type"/>:</th>
      <td>
        <html:select property="importClassCode" styleId="importClassCode">
			    <w:ifUserCanPerformAction action="importCRA">
            <html:option value="CRA">Canada Revenue Agency</html:option>
			    </w:ifUserCanPerformAction>
			    <w:ifUserCanPerformAction action="importCodes">
            <html:option value="BPU">Benchmark Per Unit</html:option>
            <html:option value="IVPR">Insurable Values & Premium Rates</html:option>
            <html:option value="FMV">Fair Market Value</html:option>
			    </w:ifUserCanPerformAction>
			    <w:ifUserCanPerformAction action="importCRA">
            <html:option value="AARM">Accrual Adjusted Reference Margin</html:option>
            <html:option value="BCCRA">BC Generated CRA</html:option>
			    </w:ifUserCanPerformAction>
        </html:select>
      </td>
    </tr>
    <tr>
      <th><fmt:message key="Import.File"/>:</th>
      <td><html:file property="file" size="60" /></td>
    </tr>
    <tr>
      <th style="vertical-align:top"><fmt:message key="Description"/>:</th>
      <td><html:textarea property="description" style="width:460px" rows="5"></html:textarea></td>
    </tr>
  </table>
  <p></p>
</html:form>

<button id="importButton"><fmt:message key="Import"/></button>


<script type="text/javascript">
  new YAHOO.widget.Button("importButton", {onclick: {fn: doImport}});
  
  function changeProcessingMessage() {
    document.getElementById('processingMessage').innerText = '<fmt:message key="Uploading.File"/>...';
  }

  function doImport() {
    var importClassCode = document.getElementById('importClassCode').value;
    var msg = "<fmt:message key="FMV.Import.Warning"/>";
    if(importClassCode == 'FMV') {
      if(confirm(msg)) {
        submitImportForm();
      }
    } else {
      submitImportForm();
    }
  }

  function submitImportForm() {
    changeProcessingMessage();
    showProcessing();
    document.getElementById('importForm').submit();
  }
</script>
