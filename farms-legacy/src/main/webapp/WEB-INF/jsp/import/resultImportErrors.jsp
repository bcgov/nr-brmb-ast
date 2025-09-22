<p></p> 

<div class="searchresults">
  <div id="searchresults"></div>
</div> 

<script type="text/javascript">
  var data = {
      "recordsReturned": <c:out value="${details.numberOfErrors}"/>,
      "totalRecords": <c:out value="${details.numberOfErrors}"/>,
      "startIndex":0,
      "sort":null,
      "dir":"asc",
      "pageSize": 10,
      "records":[
        <c:forEach varStatus="loop" var="result" items="${details.errors}">
          {
            "severity": "Error",  
            "message":"<c:out value="${result.message}" />"
          }<c:if test="${loop.index < (details.numberOfErrors-1)}">,</c:if>
        </c:forEach>
      ]
   };


   YAHOO.util.Event.onDOMReady(function() {
    var columnDefs = [
        {
          key:"severity",  
          label:"Severity", 
          sortable:false,
          formatter: function(el, oRecord, oColumn, oData) { 
            el.innerHTML = '<img src="images/error.png" title="Error">';
          } 
        },
        {key:"message", label:"Message", sortable:false}
    ];

    var dataSource = new YAHOO.util.DataSource(data);
    dataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
    dataSource.responseSchema = {
        resultsList: "records",
        fields: ["severity", "message"]
    };

    var configs = {
        paginator: new YAHOO.widget.Paginator({ rowsPerPage: 10 })
    };

    var dataTable = new YAHOO.widget.DataTable("searchresults", columnDefs, dataSource, configs);
  });
</script> 
