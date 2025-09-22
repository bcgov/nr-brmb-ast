<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp" %>

<script type="text/javascript">
	//<![CDATA[
  function goToUrlPreserveContext(baseUrl) {
    var url = baseUrl;
//  <c:if test="${form.numCodes > 0}">
      var codeFilter = escape(document.getElementById('codeFilter').value);
      var descriptionFilter = escape(document.getElementById('descriptionFilter').value);
      var groupFilter = escape(document.getElementById('groupFilter').value);
      
      if(url.indexOf("?") >= 0) {
        url = url + "&";
      } else {
        url = url + "?";
      }
      url = url + "setFilterContext=true" + "&codeFilter=" + codeFilter
								+ "&descriptionFilter=" + descriptionFilter
								+ "&groupFilter=" + groupFilter;
//  </c:if>
    document.location.href = url;
  }
	//]]>
</script>

<c:choose>
  <c:when test="${form.inventoryClassCode == '3'}">
    <c:set var="buttonLabel" value="New.Input"/>
    <c:set var="buttonAction" value="newInputXref"/>
    <c:set var="viewAction" value="farm455"/>
  </c:when>
  <c:when test="${form.inventoryClassCode == '4'}">
    <c:set var="buttonLabel" value="New.Receivable"/>
    <c:set var="buttonAction" value="newReceivableXref"/>
    <c:set var="viewAction" value="farm465"/>
  </c:when>
  <c:when test="${form.inventoryClassCode == '5'}">
    <c:set var="buttonLabel" value="New.Payable"/>
    <c:set var="buttonAction" value="newPayableXref"/>
    <c:set var="viewAction" value="farm475"/>
  </c:when>
</c:choose>

<w:ifUserCanPerformAction action="editCodes">
  <script type="text/javascript">
    //<![CDATA[
    function newButtonFunc() {
      goToUrlPreserveContext('<c:out value="${buttonAction}"/>.do');
    }
    //]]>
  </script>
  <u:yuiButton buttonId="newButton" buttonLabel="${buttonLabel}" function="newButtonFunc"/>
</w:ifUserCanPerformAction>

<br />
<br />

<div class="searchresults">
  <div id="searchresults"></div>
</div> 

<script type="text/javascript">
//<![CDATA[
  Farm.codes = {
      "recordsReturned": <c:out value="${form.numCodes}"/>,
      "totalRecords": <c:out value="${form.numCodes}"/>,
      "startIndex":0,
      "sort":null,
      "dir":"asc",
      "pageSize": 10,
      "records":[
        <c:forEach varStatus="loop" var="result" items="${form.codes}">
          {
            "commodityXrefId":"<c:out value="${result.commodityXrefId}"/>",  
            "code":"<c:out value="${result.inventoryItemCode}"/>",  
            "description":"<c:out value="${result.inventoryItemCodeDescription}"/>",  
            "group":"<c:out value="${result.inventoryGroupCodeDescription}"/>"
          }<c:if test="${loop.index < (form.numCodes-1)}">,</c:if>
        </c:forEach> 
      ]
  };


  YAHOO.util.Event.onDOMReady(function() {
      Farm.codesColumnDefs = [
         {key:"code", width:80, label:"<fmt:message key="Code"/>: <input name='codeFilter' type='text' id='codeFilter' value='<c:out value="${form.codeFilter}"/>' maxlength='10' style='width:40px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
         {key:"description", width:300, label:"<fmt:message key="Description"/>: <input name='descriptionFilter' type='text' id='descriptionFilter' value='<c:out value="${form.descriptionFilter}"/>' maxlength='256' style='width:100px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
         {key:"group", width:300, label:"<fmt:message key="Group"/>: <input name='groupFilter' type='text' id='groupFilter' value='<c:out value="${form.groupFilter}"/>' maxlength='256' style='width:100px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"}
     ];

    Farm.filterCode = function(code) {
      var codeFilter = YAHOO.util.Dom.get('codeFilter');
      if(codeFilter.value) {
        if(code.indexOf(codeFilter.value) == 0) {
          return true;
        } else {
          return false;
        }
      }
      return true;      
    };

    Farm.filterDescription = function(description) {
      var descriptionFilter = YAHOO.util.Dom.get('descriptionFilter');
      if(descriptionFilter.value) {
        if(description.toLowerCase().indexOf(descriptionFilter.value.toLowerCase()) >= 0) {
          return true;
        } else {
          return false;
        }
      }
      return true;      
    };

    Farm.filterGroup = function(group) {
      var groupFilter = YAHOO.util.Dom.get('groupFilter');
      if(groupFilter.value) {
        if(group.toLowerCase().indexOf(groupFilter.value.toLowerCase()) >= 0) {
          return true;
        } else {
          return false;
        }
      }
      return true;      
    };

    Farm.codesDataSource = new YAHOO.util.DataSource(Farm.codes,{
      doBeforeCallback : function (req,raw,res,cb) {
        // This is the filter function 

        var data     = res.results || [], 
            filtered = [], 
            i,l; 

        for (i = 0, l = data.length; i < l; ++i) {
          if(Farm.filterCode(data[i].code)
    	       && Farm.filterDescription(data[i].description)
    	       && Farm.filterGroup(data[i].group)) {
            filtered.push(data[i]);
          } 
        }

        res.results = filtered; 
        return res; 
      }
    });

    Farm.codesDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
    Farm.codesDataSource.responseSchema = {
      resultsList: "records",
      fields: ["commodityXrefId","code","description","group"]
    };


    Farm.codesConfigs = {
      paginator: new YAHOO.widget.Paginator({ rowsPerPage: 20 })
    };

    Farm.showSelectedRow = function(oArgs) {
      var oRecord = oArgs.record;
      var commodityXrefId = oRecord.getData("commodityXrefId");
      var farmAction = "<c:out value="${viewAction}"/>";  // edit page
      
      showProcessing();
      goToUrlPreserveContext(farmAction + ".do?commodityXrefId=" + commodityXrefId);
    };

		Farm.codesDataTable = new YAHOO.widget.DataTable("searchresults", Farm.codesColumnDefs, Farm.codesDataSource, Farm.codesConfigs);
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

    YAHOO.util.Event.on('codeFilter','keyup',Farm.updateFilter);
    YAHOO.util.Event.on('descriptionFilter','keyup',Farm.updateFilter);
    YAHOO.util.Event.on('groupFilter','keyup',Farm.updateFilter);


  });
//]]>
</script>
