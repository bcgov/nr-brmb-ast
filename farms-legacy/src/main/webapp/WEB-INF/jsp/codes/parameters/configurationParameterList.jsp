<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp" %>

<br />

<div class="searchresults">
  <div id="searchresults"></div>
</div>

<script>
  function goToUrlPreserveContext(baseUrl) {
    var url = baseUrl;
    document.location.href = url;
  } 

  YAHOO.util.Event.onDOMReady(function() {
    Farm.codes = {
      "recordsReturned": <c:out value="${form.numConfigurationParameters}"/>,
      "totalRecords": <c:out value="${form.numConfigurationParameters}"/>,
      "startIndex":0,
      "sort":null,
      "dir":"asc",
      "pageSize": 10,
      "records":[
        <c:forEach varStatus="loop" var="result" items="${form.configurationParameters}">
          {
            "id":"<c:out value="${result.id}"/>",
            "name":"<c:out value="${result.name}"/>",
            "value":"<c:out value="${result.shortValue}"/>",
            "type":"<c:out value="${result.typeDescription}"/>"
          }<c:if test="${loop.index < (form.numConfigurationParameters-1)}">,</c:if>
        </c:forEach> 
      ]
    };

    Farm.codesColumnDefs = [
      {key:"name", label:"<fmt:message key="Name"/>", sortable:false, className:"tdLeft"},
      {key:"value", label:"<fmt:message key="Value"/>", sortable:false, className:"tdCenter"},
      {key:"type", label:"<fmt:message key="Type"/>", sortable:false, className:"tdCenter"}
    ];

    Farm.codesConfigs = {paginator: new YAHOO.widget.Paginator({ rowsPerPage: 20 })};
    Farm.codesDataSource = new YAHOO.util.DataSource(Farm.codes, {});
    Farm.codesDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
    Farm.codesDataSource.responseSchema = {
      resultsList: "records",
      fields: ["id", "name", "value", "type"]
    };

    Farm.showSelectedRow = function(oArgs) {
      var oRecord = oArgs.record;
      var id = oRecord.getData("id");
      var farmAction = "farm755";  // edit page

      showProcessing();
      goToUrlPreserveContext(farmAction + ".do?id=" + encodeURIComponent(id));
    };

    Farm.codesDataTable = new YAHOO.widget.DataTable("searchresults", Farm.codesColumnDefs, Farm.codesDataSource, Farm.codesConfigs);
    Farm.codesDataTable.subscribe("rowMouseoverEvent", Farm.codesDataTable.onEventHighlightRow);
    Farm.codesDataTable.subscribe("rowMouseoutEvent", Farm.codesDataTable.onEventUnhighlightRow);
    Farm.codesDataTable.subscribe("rowClickEvent", Farm.codesDataTable.onEventSelectRow);
    Farm.codesDataTable.subscribe("rowSelectEvent", Farm.showSelectedRow);

   });
</script>