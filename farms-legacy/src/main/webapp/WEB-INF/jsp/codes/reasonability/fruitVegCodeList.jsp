<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp" %>

<script>
  function goToUrlPreserveContext(baseUrl) {
  var url = baseUrl;
    document.location.href = url;
  }
</script>

<span style="float:left;">
  <w:ifUserCanPerformAction action="editCodes">

    <button type="button" id="newButton"><fmt:message key="New.Fruit.and.Veg.Type.Code"/></button>

    <script>
      function newFruitVegItem() {
        goToUrlPreserveContext('<html:rewrite action="newFruitVegCodeItem" />');
      }
      var newButton = new YAHOO.widget.Button("newButton", {onclick: {fn: newFruitVegItem}});
    </script>
  </w:ifUserCanPerformAction>
  
</span>

  <br />
  <br />
  <br />

  <div class="fruit-veg-list">
    <div id="fruit-veg-list"></div>
  </div>
  
    <script>
    
      YAHOO.util.Event.onDOMReady(function() {
        Farm.codes = {
             "recordsReturned": <c:out value="${form.numFruitVegCodes}"/>,
             "totalRecords": <c:out value="${form.numFruitVegCodes}"/>,
             "startIndex":0,
             "sort":null,
             "dir":"asc",
             "pageSize": 10,
             "records":[
               <c:forEach varStatus="loop" var="result" items="${form.fruitVegCodes}">
                 {
                   "code":"<c:out value="${result.name}"/>",
                   "description":"<c:out value="${result.description}"/>",
                   "variance":"<c:out value="${result.varianceLimit}"/>"
                 }<c:if test="${loop.index < (form.numFruitVegCodes-1)}">,</c:if>
               </c:forEach> 
             ]
         };

        Farm.codesColumnDefs = [
           {key:"code", label:"<fmt:message key="Code"/>", sortable:false, className:"tdCenter"},
           {key:"description", label:"<fmt:message key="Description"/>", sortable:false, className:"tdCenter"},
           {key:"variance", label:"<fmt:message key="Revenue.Variance.Limit"/>", sortable:false, className:"tdCenter"}
       ];


        Farm.codesConfigs = {paginator: new YAHOO.widget.Paginator({ rowsPerPage: 20 })};
        Farm.codesDataSource = new YAHOO.util.DataSource(Farm.codes, {});
        Farm.codesDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
        Farm.codesDataSource.responseSchema = {
          resultsList: "records",
          fields: ["code", "description", "variance"]
        };

        Farm.showSelectedRow = function(oArgs) {
          var oRecord = oArgs.record;
          var code = oRecord.getData("code");
          var farmAction = "farm735";  // edit page
          
          showProcessing();
          goToUrlPreserveContext(farmAction + ".do?name=" + code);
        };

        Farm.codesDataTable = new YAHOO.widget.DataTable("fruit-veg-list", Farm.codesColumnDefs, Farm.codesDataSource, Farm.codesConfigs);
        Farm.codesDataTable.subscribe("rowMouseoverEvent", Farm.codesDataTable.onEventHighlightRow);
        Farm.codesDataTable.subscribe("rowMouseoutEvent", Farm.codesDataTable.onEventUnhighlightRow);
        Farm.codesDataTable.subscribe("rowClickEvent", Farm.codesDataTable.onEventSelectRow);
        Farm.codesDataTable.subscribe("rowSelectEvent", Farm.showSelectedRow);

   	});
</script>