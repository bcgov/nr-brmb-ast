<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp" %>

<script type="text/javascript">
	//<![CDATA[
  function goToUrlPreserveContext(baseUrl) {
    var url = baseUrl;
      var sectorCodeFilter = escape(document.getElementById('sectorCodeFilter').value);
      var sectorCodeDescriptionFilter = escape(document.getElementById('sectorCodeDescriptionFilter').value);
      
      if(url.indexOf("?") >= 0) {
        url = url + "&";
      } else {
        url = url + "?";
      }
      url = url + "setFilterContext=true" + "&sectorCodeFilter=" + sectorCodeFilter + "&sectorCodeDescriptionFilter=" + sectorCodeDescriptionFilter;
    document.location.href = url;
  }
	//]]>
</script>

<p><fmt:message key="Farm.Types.instruction.text"/></p>

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
            "code":"<c:out value="${result.code}"/>",  
            "description":"<c:out value="${result.description}"/>",  
            "establishedDate":"<fmt:formatDate pattern="yyyy-MM-dd" value="${result.establishedDate}" />",
            "expiryDate":"<fmt:formatDate pattern="yyyy-MM-dd" value="${result.expiryDate}" />"
          }<c:if test="${loop.index < (form.numCodes-1)}">,</c:if>
        </c:forEach> 
      ]
  };


  YAHOO.util.Event.onDOMReady(function() {
      Farm.codesColumnDefs = [
         {key:"code", width:80, label:"<fmt:message key="Code"/>: <input name='sectorCodeFilter' type='text' id='sectorCodeFilter' value='<c:out value="${form.sectorCodeFilter}"/>' maxlength='10' style='width:40px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
         {key:"description", width:300, label:"<fmt:message key="Description"/>: <input name='sectorCodeDescriptionFilter' type='text' id='sectorCodeDescriptionFilter' value='<c:out value="${form.sectorCodeDescriptionFilter}"/>' maxlength='256' style='width:100px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
         {key:"establishedDate", width:90, label:"<fmt:message key="Established.Date"/>", sortable:false, className:"tdCenter"},
         {key:"expiryDate", width:70, label:"<fmt:message key="Expiry.Date"/>", sortable:false, className:"tdCenter"}
     ];

    Farm.filterSectorCode = function(sectorCode) {
      var sectorCodeFilter = YAHOO.util.Dom.get('sectorCodeFilter');
      if(sectorCodeFilter.value) {
        if(sectorCode.toLowerCase().indexOf(sectorCodeFilter.value.toLowerCase()) >= 0) {
          return true;
        } else {
          return false;
        }
      }
      return true;      
    };

    Farm.filterDescription = function(description) {
      var sectorCodeDescriptionFilter = YAHOO.util.Dom.get('sectorCodeDescriptionFilter');
      if(sectorCodeDescriptionFilter.value) {
        if(description.toLowerCase().indexOf(sectorCodeDescriptionFilter.value.toLowerCase()) >= 0) {
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
          if(Farm.filterSectorCode(data[i].code)
    	       && Farm.filterDescription(data[i].description)) {
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
      fields: ["code","description","establishedDate","expiryDate"]
    };


    Farm.codesConfigs = {
      paginator: new YAHOO.widget.Paginator({ rowsPerPage: 20 })
    };

    Farm.showSelectedRow = function(oArgs) {
      var oRecord = oArgs.record;
      var code = oRecord.getData("code");
      var farmAction = "farm773";  // edit page
      
      showProcessing();
      goToUrlPreserveContext(farmAction + ".do?code=" + code);
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

    YAHOO.util.Event.on('sectorCodeFilter','keyup',Farm.updateFilter);
    YAHOO.util.Event.on('sectorCodeDescriptionFilter','keyup',Farm.updateFilter);


  });
//]]>
</script>
