<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp" %>

<script type="text/javascript">
   //<![CDATA[
  function goToUrlPreserveContext(baseUrl) {
	var url = baseUrl;
    document.location.href = url;
  }
   //]]>
</script>

<span style="float:left;">
  <w:ifUserCanPerformAction action="editTipCodes">

    <button type="button" id="newButton"><fmt:message key="New.Farm.Type"/></button>

    <script type="text/javascript">
    //<![CDATA[ 
      function newFarmSubtypeB() {
        goToUrlPreserveContext('<html:rewrite action="newFarmSubtypeBItem" />');
      }
      var newButton = new YAHOO.widget.Button("newButton", {onclick: {fn: newFarmSubtypeB}});
    //]]>
    </script>
  </w:ifUserCanPerformAction>
</span>

  <br />
  <br />
  <br />

  <div class="farm-types-list">
    <div id="farm-types-list"></div>
  </div>
  
    <script type="text/javascript">
  //<![CDATA[
    
   	YAHOO.util.Event.onDOMReady(function() {
   	    Farm.codes = {
   	         "recordsReturned": <c:out value="${form.numFarmSubtypes}"/>,
   	         "totalRecords": <c:out value="${form.numFarmSubtypes}"/>,
   	         "startIndex":0,
   	         "sort":null,
   	         "dir":"asc",
   	         "pageSize": 10,
   	         "records":[
   	           <c:forEach varStatus="loop" var="result" items="${form.farmSubtypeBItems}">
   	             {
  	               "parent":"<c:out value="${result.secondaryParentName}"/>",
   	               "subtypeA":"<c:out value="${result.parentName}"/>",
   	               "subtypeB":"<c:out value="${result.name}"/>",
   	               "id":"<c:out value="${result.id}"/>"
   	             }<c:if test="${loop.index < (form.numFarmSubtypes-1)}">,</c:if>
   	           </c:forEach> 
   	         ]
   	     };   		
   		
   		Farm.codesColumnDefs = [
  		   {key:"parent", label:"<fmt:message key="Farm.Type.3"/>", sortable:false, className:"tdCenter"},
           {key:"subtypeA", label:"<fmt:message key="Farm.Type.2"/>", sortable:false, className:"tdCenter"},
           {key:"subtypeB", label:"<fmt:message key="Farm.Type.1"/>", sortable:false, className:"tdCenter"}
       ];
   		
	   	
	    Farm.codesConfigs = {paginator: new YAHOO.widget.Paginator({ rowsPerPage: 20 })};
	    Farm.codesDataSource = new YAHOO.util.DataSource(Farm.codes, {});
        Farm.codesDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
	    Farm.codesDataSource.responseSchema = {
	      resultsList: "records",
	      fields: ["parent", "subtypeA", "subtypeB", "id"]
	    };
		  
	    Farm.showSelectedRow = function(oArgs) {
          var oRecord = oArgs.record;
          var id = oRecord.getData("id");
          var farmAction = "farm540";  // edit page
	        
          showProcessing();
          goToUrlPreserveContext(farmAction + ".do?id=" + id);
   		};

  		Farm.codesDataTable = new YAHOO.widget.DataTable("farm-types-list", Farm.codesColumnDefs, Farm.codesDataSource, Farm.codesConfigs);
  		Farm.codesDataTable.subscribe("rowMouseoverEvent", Farm.codesDataTable.onEventHighlightRow);
  		Farm.codesDataTable.subscribe("rowMouseoutEvent", Farm.codesDataTable.onEventUnhighlightRow);
  		Farm.codesDataTable.subscribe("rowClickEvent", Farm.codesDataTable.onEventSelectRow);
  		Farm.codesDataTable.subscribe("rowSelectEvent", Farm.showSelectedRow);
		  		   	
   	});
</script>
