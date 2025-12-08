<c:set var="accountsUrl" value="farm230.do?tab=accountsTab&importVersionId=${form.importVersionId}" />
<c:set var="commoditiesUrl" value="farm230.do?tab=commoditiesTab&importVersionId=${form.importVersionId}" />
<c:set var="productionUnitsUrl" value="farm230.do?tab=productionUnitsTab&importVersionId=${form.importVersionId}" />

<div id="detailTabs" class="yui-navset"> 
  <ul class="yui-nav"> 
    <li class="selected"><a href="javascript:showUrl('<c:out value="${accountsUrl}"/>')"><em>Accounts (<c:out value="${details.numberOfAccounts}"/>)</em></a></li> 
    <li><a href="javascript:showUrl('<c:out value="${commoditiesUrl}"/>')"><em>Commodities (<c:out value="${details.numberOfCommodities}"/>)</em></a></li> 
    <li><a href="javascript:showUrl('<c:out value="${productionUnitsUrl}"/>')"><em>Production Units (<c:out value="${details.numberOfProductionUnits}"/>)</em></a></li> 
  </ul>
  <div class="yui-content"> 
    <c:if test="${details.numberOfAccounts > 0}">
      <div class="searchresults"><div id="accountsDatatable"></div></div>
    </c:if>
  </div>
</div>

<c:if test="${details.numberOfAccounts > 0}">
    <script type="text/javascript">
       var data = {
          "recordsReturned": <c:out value="${details.numberOfAccounts}"/>,
          "totalRecords": <c:out value="${details.numberOfAccounts}"/>,
          "startIndex":0,
          "sort":null,
          "dir":"asc",
          "pageSize": 10,
          "records":[
            <c:forEach varStatus="loop" var="result" items="${details.accounts}">
              {
                "pin":"<c:out value="${result.pin}"/>",
                "name":"<c:out value="${result.name}"/>",
                "status":"<c:out value="${result.status}"/>",
                "msg":"<c:out value="${result.errorMessage}"/>"
              }<c:if test="${loop.index < (details.numberOfAccounts-1)}">,</c:if>
            </c:forEach>
          ]
       };

       YAHOO.util.Event.onDOMReady(function() {
        var columnDefs = [
            {key:"pin",    label:"PIN", sortable:false},
            {key:"name",   label:"Name", sortable:false},
            {
              key:"status",  
              label:"Status", 
              sortable:false,
              formatter: function(el, oRecord, oColumn, oData) { 
                if(oData == "ERROR") {
                  el.innerHTML = '<img src="images/error.png" title="Error">';
                } else if (oData == "WARNING") {
                  el.innerHTML = '<img src="images/bcgov_warning.gif" title="Warning">';
                } else {
                  el.innerHTML = '<img src="images/tick.gif" title="Sucess">'; 
                }
              } 
            },
            {key:"msg",    label:"Status Message", sortable:false}
        ];

        var dataSource = new YAHOO.util.DataSource(data);
        dataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
        dataSource.responseSchema = {
            resultsList: "records",
            fields: ["pin", "name", "status", "msg"]
        };

        var configs = {
            paginator: new YAHOO.widget.Paginator({rowsPerPage: 10})
        };

        var dataTable = new YAHOO.widget.DataTable("accountsDatatable", columnDefs, dataSource, configs);
        
        
        function showSelectedRow(args) {
          var selectedRecord = args.record;
          var selectedPin = selectedRecord.getData("pin");
          var url = "farm240Busy.do?pin=" + selectedPin + "&importVersionId="+ <c:out value="${form.importVersionId}" />;
          
          openPlainWindow(url);
        };
        
        dataTable.subscribe("rowMouseoverEvent", dataTable.onEventHighlightRow);
        dataTable.subscribe("rowMouseoutEvent", dataTable.onEventUnhighlightRow);
        dataTable.subscribe("rowClickEvent", dataTable.onEventSelectRow);
        dataTable.subscribe("rowSelectEvent", showSelectedRow);
      });
    </script>
</c:if>