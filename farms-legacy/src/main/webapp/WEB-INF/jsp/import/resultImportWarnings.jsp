<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<p></p> 

<h3><fmt:message key="Warnings"/>:</h3>

<div class="searchresults">
  <div id="importWarnings"></div>
</div> 

<script type="text/javascript">
  var warningsData = {
      "recordsReturned": <c:out value="${details.numberOfWarnings}"/>,
      "totalRecords": <c:out value="${details.numberOfWarnings}"/>,
      "startIndex":0,
      "sort":null,
      "dir":"asc",
      "pageSize": 10,
      "records":[
        <c:forEach varStatus="loop" var="result" items="${details.warnings}">
          {
            "severity": "Warning",  
            "message":"<c:out value="${result.message}" />"
          }<c:if test="${loop.index < (details.numberOfWarnings-1)}">,</c:if>
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
            el.innerHTML = '<img src="images/bcgov_warning.gif" title="Warning">';
          } 
        },
        {key:"message", label:"Message", sortable:false}
    ];

    var dataSource = new YAHOO.util.DataSource(warningsData);
    dataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
    dataSource.responseSchema = {
        resultsList: "records",
        fields: ["severity", "message"]
    };

    var configs = {
        paginator: new YAHOO.widget.Paginator({ rowsPerPage: 10 })
    };

    var dataTable = new YAHOO.widget.DataTable("importWarnings", columnDefs, dataSource, configs);
  });
</script> 
