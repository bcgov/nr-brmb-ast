<!-- START calculatorSearch.jsp -->
<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp" %>


<c:if test="${form.searchResults != null}">

  <c:if test="${form.numSearchResults == 0}">
  <h3>No accounts were found for the given search criteria.</h3>
  </c:if>

  <c:if test="${form.numSearchResults > 0}">
    <div class="searchresults">
      <div id="searchresults"></div>
    </div> 

    <script type="text/javascript">
     var data = {
          "recordsReturned": <c:out value="${form.numSearchResults}"/>,
          "totalRecords": <c:out value="${form.numSearchResults}"/>,
          "startIndex":0,
          "sort":null,
          "dir":"asc",
          "pageSize": 10,
          "records":[
            <c:forEach varStatus="loop" var="result" items="${form.searchResults}">
              {
                "name":"<c:out value="${result.name}"/>",  
                "pin":"<c:out value="${result.pin}"/>",  
                "address":"<c:out value="${result.address}"/>"
              }<c:if test="${loop.index < (form.numSearchResults-1)}">,</c:if>
            </c:forEach> 
          ]
       };


       YAHOO.util.Event.onDOMReady(function() {
        var columnDefs = [
            {key:"name", label:"Name", sortable:false},
            {key:"pin", label:"PIN", sortable:false},
            {key:"address", label:"Address", sortable:false}
        ];

        var dataSource = new YAHOO.util.DataSource(data);
        dataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
        dataSource.responseSchema = {
            resultsList: "records",
            fields: ["name","pin","address"]
        };

        var configs = {
            paginator: new YAHOO.widget.Paginator({ rowsPerPage: 10 })
        };

        function showSelectedRow(oArgs) {
          var oRecord = oArgs.record;
          var oData = oRecord.getData("pin");

          showProcessing();
          document.location.href = "farm800.do?pin=" + oData + "&refresh=true";
        };

        var dataTable = new YAHOO.widget.DataTable("searchresults", columnDefs, dataSource, configs);
        dataTable.subscribe("rowMouseoverEvent", dataTable.onEventHighlightRow);
        dataTable.subscribe("rowMouseoutEvent", dataTable.onEventUnhighlightRow);
        dataTable.subscribe("rowClickEvent", dataTable.onEventSelectRow);
        dataTable.subscribe("rowSelectEvent", showSelectedRow);
      });
    </script> 
  </c:if>
</c:if>
<!-- END calculatorSearch.jsp -->
