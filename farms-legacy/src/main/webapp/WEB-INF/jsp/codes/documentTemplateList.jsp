<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp" %>

<span style="float:left;">
  <script>
    function goToUrlPreserveContext(baseUrl) {
    var url = baseUrl;
      document.location.href = url;
    } 
  </script>
</span>

<p>
  <fmt:message key="Document.Templates.instruction.text" />
</p>

<div class="document-template-list">
  <div id="document-template-list"></div>
</div>

<script>

  YAHOO.util.Event.onDOMReady(function() {
    Farm.codes = {
           "recordsReturned": <c:out value="${form.numDocumentTemplates}"/>,
           "totalRecords": <c:out value="${form.numDocumentTemplates}"/>,
           "startIndex":0,
           "sort":null,
           "dir":"asc",
           "pageSize": 10,
           "records":[
             <c:forEach varStatus="loop" var="result" items="${form.documentTemplates}">
               {
                 "templateName":"<c:out value="${result.templateName}"/>"
               }<c:if test="${loop.index < (form.numDocumentTemplates-1)}">,</c:if>
             </c:forEach> 
           ]
       };

    Farm.codesColumnDefs = [
         {key:"templateName", label:"<fmt:message key="Template.Name"/>", sortable:false, className:"tdLeft"}
    ];


    Farm.codesConfigs = {paginator: new YAHOO.widget.Paginator({ rowsPerPage: 20 })};
    Farm.codesDataSource = new YAHOO.util.DataSource(Farm.codes, {});
    Farm.codesDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
    Farm.codesDataSource.responseSchema = {
      resultsList: "records",
      fields: ["templateName"]
    };

    Farm.showSelectedRow = function(oArgs) {
        var oRecord = oArgs.record;
        var templateName = oRecord.getData("templateName");
        var farmAction = "farm765";  // edit page

        showProcessing();
        goToUrlPreserveContext(farmAction + ".do?templateName=" + templateName);
    };

    Farm.codesDataTable = new YAHOO.widget.DataTable("document-template-list", Farm.codesColumnDefs, Farm.codesDataSource, Farm.codesConfigs);
    Farm.codesDataTable.subscribe("rowMouseoverEvent", Farm.codesDataTable.onEventHighlightRow);
    Farm.codesDataTable.subscribe("rowMouseoutEvent", Farm.codesDataTable.onEventUnhighlightRow);
    Farm.codesDataTable.subscribe("rowClickEvent", Farm.codesDataTable.onEventSelectRow);
    Farm.codesDataTable.subscribe("rowSelectEvent", Farm.showSelectedRow);

  });
</script>