<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp" %>

<br />

<span style="float:right;">
  <u:menuSelect action="farm756.do"
      name="yearFilterPicker"
      paramName="yearFilter"
      options="${form.programYearSelectOptions}"
      selectedValue="${form.yearFilter}"
      toolTip="Click here to open a different Program Year."/>
</span>

<div class="config-parameters-list">
  <div id="config-parameters-list"></div>
</div>
  
<script>
  function goToUrlPreserveContext(baseUrl) {
    var url = baseUrl;
    document.location.href = url;
  } 

  YAHOO.util.Event.onDOMReady(function() {
    Farm.codes = {
      "recordsReturned": <c:out value="${form.numYearConfigurationParameters}"/>,
      "totalRecords": <c:out value="${form.numYearConfigurationParameters}"/>,
      "startIndex":0,
      "sort":null,
      "dir":"asc",
      "pageSize": 10,
      "records":[
        <c:forEach varStatus="loop" var="result" items="${form.yearConfigurationParameters}">
          {
            "id":"<c:out value="${result.id}"/>",
            "name":"<c:out value="${result.name}"/>",
            "value":"<c:out value="${result.shortValue}"/>",
            "type":"<c:out value="${result.typeDescription}"/>"
          }<c:if test="${loop.index < (form.numYearConfigurationParameters-1)}">,</c:if>
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
      var farmAction = "farm757";  // edit page

      showProcessing();
      goToUrlPreserveContext(farmAction + ".do?id=" + encodeURIComponent(id));
    };

    Farm.codesDataTable = new YAHOO.widget.DataTable("config-parameters-list", Farm.codesColumnDefs, Farm.codesDataSource, Farm.codesConfigs);
    Farm.codesDataTable.subscribe("rowMouseoverEvent", Farm.codesDataTable.onEventHighlightRow);
    Farm.codesDataTable.subscribe("rowMouseoutEvent", Farm.codesDataTable.onEventUnhighlightRow);
    Farm.codesDataTable.subscribe("rowClickEvent", Farm.codesDataTable.onEventSelectRow);
    Farm.codesDataTable.subscribe("rowSelectEvent", Farm.showSelectedRow);

   });
</script>