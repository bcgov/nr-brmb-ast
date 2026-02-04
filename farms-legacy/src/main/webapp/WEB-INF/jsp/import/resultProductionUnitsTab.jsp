<c:set var="accountsUrl" value="farm230.do?tab=accountsTab&importVersionId=${form.importVersionId}" />
<c:set var="commoditiesUrl" value="farm230.do?tab=commoditiesTab&importVersionId=${form.importVersionId}" />
<c:set var="productionUnitsUrl" value="farm230.do?tab=productionUnitsTab&importVersionId=${form.importVersionId}" />

<div id="detailTabs" class="yui-navset"> 
  <ul class="yui-nav"> 
    <li><a href="javascript:showUrl('<c:out value="${accountsUrl}"/>')"><em>Accounts (<c:out value="${details.numberOfAccounts}"/>)</em></a></li> 
    <li><a href="javascript:showUrl('<c:out value="${commoditiesUrl}"/>')"><em>Commodities (<c:out value="${details.numberOfCommodities}"/>)</em></a></li> 
    <li class="selected"><a href="javascript:showUrl('<c:out value="${productionUnitsUrl}"/>')"><em>Production Units (<c:out value="${details.numberOfProductionUnits}"/>)</em></a></li> 
  </ul>
  <div class="yui-content"> 
    <c:if test="${details.numberOfProductionUnits > 0}">
      <div class="searchresults"><div id="productionUnitsDatatable"></div></div>
    </c:if>
  </div>
</div>

<c:if test="${details.numberOfProductionUnits > 0}">
    <script type="text/javascript">
       var data = {
          "recordsReturned": <c:out value="${details.numberOfProductionUnits}"/>,
          "totalRecords": <c:out value="${details.numberOfProductionUnits}"/>,
          "startIndex":0,
          "sort":null,
          "dir":"asc",
          "pageSize": 10,
          "records":[
            <c:forEach varStatus="loop" var="result" items="${details.productionUnits}">
              {
                "code":"<c:out value="${result.code}"/>",
                "errMsg":"<c:out value="${result.errorMessage}"/>",
                <c:choose>
                  <c:when test="${result.errorMessage != null}">
                    "desc":"<c:out value="${result.errorMessage}"/>"
                  </c:when>
                  <c:when test="${result.oldDescription != null}">
                    "desc":"New: <c:out value="${result.newDescription}"/>, Old: <c:out value="${result.oldDescription}"/>"
                  </c:when>
                  <c:otherwise>
                    "desc":"New: <c:out value="${result.newDescription}"/>"
                  </c:otherwise>
                </c:choose>
                
              }<c:if test="${loop.index < (details.numberOfProductionUnits-1)}">,</c:if>
            </c:forEach>
          ]
       };

       YAHOO.util.Event.onDOMReady(function() {
        var columnDefs = [
            {key:"code", label:"Code", sortable:false},
            {
              key:"errMsg",  
              label:"Status", 
              sortable:false,
              formatter: function(el, oRecord, oColumn, oData) { 
                if(oData.length > 0) {
                  el.innerHTML = '<img src="images/error.png" title="Error">';
                } else {
                  el.innerHTML = '<img src="images/tick.gif" title="Sucess">'; 
                }
              } 
            },
            {key:"desc", label:"Status Message", sortable:false}
        ];

        var dataSource = new YAHOO.util.DataSource(data);
        dataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
        dataSource.responseSchema = {
            resultsList: "records",
            fields: [ "code", "errMsg", "desc"]
        };

        var configs = {
            paginator: new YAHOO.widget.Paginator({ rowsPerPage: 10 })
        };

        var dataTable = new YAHOO.widget.DataTable("productionUnitsDatatable", columnDefs, dataSource, configs);
      });
    </script>
</c:if>