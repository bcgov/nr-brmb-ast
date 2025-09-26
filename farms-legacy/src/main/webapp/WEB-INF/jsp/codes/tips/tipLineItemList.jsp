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

    <button type="button" id="newButton"><fmt:message key="New.Line.Item"/></button>

    <script type="text/javascript">
    //<![CDATA[
      function newTipLineItem() {
        goToUrlPreserveContext('<html:rewrite action="newTipLineItem" />');
      }
      var newButton = new YAHOO.widget.Button("newButton", {onclick: {fn: newTipLineItem}});
    //]]>
    </script>
  </w:ifUserCanPerformAction>
  
</span>

  <br />
  <br />
  <br />

  <div class="tip-line-item-list">
    <div id="tip-line-item-list"></div>
  </div>
  
    <script type="text/javascript">
  //<![CDATA[
    
   	YAHOO.util.Event.onDOMReady(function() {
   	    Farm.codes = {
   	         "recordsReturned": <c:out value="${form.numLineItems}"/>,
   	         "totalRecords": <c:out value="${form.numLineItems}"/>,
   	         "startIndex":0,
   	         "sort":null,
   	         "dir":"asc",
   	         "pageSize": 10,
   	         "records":[
   	           <c:forEach varStatus="loop" var="result" items="${form.lineItems}">
   	             {
  	               "lineItem":"<c:out value="${result.lineItem}"/>",
   	               "description":"<c:out value="${result.description}"/>",
   	               "subtypeB":"<c:out value="${result.farmSubtypeBName}"/>",
   	               "id":"<c:out value="${result.id}"/>"
   	             }<c:if test="${loop.index < (form.numLineItems-1)}">,</c:if>
   	           </c:forEach> 
   	         ]
   	     };   		
   		
   		Farm.codesColumnDefs = [
  		   {key:"lineItem", label:"<fmt:message key="Line.Item"/><br /><input name='lineItemFilter' type='text' id='lineItemFilter' value='<c:out value="${form.lineItemFilter}"/>' maxlength='10' style='width:40px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
           {key:"description", label:"<fmt:message key="Description"/><br /><input name='descFilter' type='text' id='descFilter' value='<c:out value="${form.descFilter}"/>' maxlength='10' style='width:150px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
           {key:"subtypeB", label:"<fmt:message key="Farm.Type.1"/><br /><input name='farmSubtypeBFilter' type='text' id='farmSubtypeBFilter' value='<c:out value="${form.farmSubtypeBFilter}"/>' maxlength='10' style='width:100px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"}
       ];
   		
        Farm.filterLineItem = function(value) {
            var filter = YAHOO.util.Dom.get('lineItemFilter');
            if(filter.value) {
              if((value+'').indexOf(filter.value) == 0) {
                return true;
              } else {
                return false;
              }
            }
            return true;
          };
          
          Farm.filterDesc = function(value) {
              var filter = YAHOO.util.Dom.get('descFilter');
              if(filter.value) {
                if(value.toLowerCase().indexOf(filter.value.toLowerCase()) >= 0) {
                  return true;
                } else {
                  return false;
                }
              }
              return true;
            };
            
            Farm.filterFarmSubtypeB = function(value) {
                var filter = YAHOO.util.Dom.get('farmSubtypeBFilter');
                if(filter.value) {
                	if(value.toLowerCase().indexOf(filter.value.toLowerCase()) >= 0) {
                    return true;
                  } else {
                    return false;
                  }
                }
                return true;
              };
	   	
	    Farm.codesConfigs = {paginator: new YAHOO.widget.Paginator({ rowsPerPage: 20 })};	    
		Farm.codesDataSource = new YAHOO.util.DataSource(Farm.codes,{
		    doBeforeCallback : function (req,raw,res,cb) {
		      // This is the filter function
		      var data     = res.results || [],
		          filtered = [],
		          ii,l;
		
		      for (ii = 0, l = data.length; ii < l; ii++) {
		        if(Farm.filterLineItem(data[ii].lineItem) 
		           && Farm.filterDesc(data[ii].description)
		           && Farm.filterFarmSubtypeB(data[ii].subtypeB)) {
		          filtered.push(data[ii]);
		        }
		      }
			  
		      res.results = filtered;
		      return res;
		    }
		  });
	    
        Farm.codesDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
	    Farm.codesDataSource.responseSchema = {
	      resultsList: "records",
	      fields: ["lineItem", "description", "subtypeB", "id"]
	    };
		  
	    Farm.showSelectedRow = function(oArgs) {
          var oRecord = oArgs.record;
          var id = oRecord.getData("id");
          var farmAction = "farm550";  // edit page
	        
          showProcessing();
          goToUrlPreserveContext(farmAction + ".do?id=" + id);
   		};

  		Farm.codesDataTable = new YAHOO.widget.DataTable("tip-line-item-list", Farm.codesColumnDefs, Farm.codesDataSource, Farm.codesConfigs);
  		Farm.codesDataTable.subscribe("rowMouseoverEvent", Farm.codesDataTable.onEventHighlightRow);
  		Farm.codesDataTable.subscribe("rowMouseoutEvent", Farm.codesDataTable.onEventUnhighlightRow);
  		Farm.codesDataTable.subscribe("rowClickEvent", Farm.codesDataTable.onEventSelectRow);
  		Farm.codesDataTable.subscribe("rowSelectEvent", Farm.showSelectedRow);
  		
        Farm.updateFilter = function ()  {
            // Reset sort
            var state = YAHOO.farm.codesDataTable.getState();

            // Get filtered data
            Farm.codesDataSource.sendRequest(null,{
                success : Farm.codesDataTable.onDataReturnInitializeTable,
                failure : Farm.codesDataTable.onDataReturnInitializeTable,
                scope   : Farm.codesDataTable,
                argument: state
            });
        };
  		
  		YAHOO.util.Event.on('lineItemFilter','keyup', Farm.updateFilter);
  		YAHOO.util.Event.on('descFilter','keyup', Farm.updateFilter);  	
  		YAHOO.util.Event.on('farmSubtypeBFilter','keyup', Farm.updateFilter);  	
   	});
</script>