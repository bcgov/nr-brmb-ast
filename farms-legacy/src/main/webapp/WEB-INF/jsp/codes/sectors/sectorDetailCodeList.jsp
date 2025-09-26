<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp" %>

<script type="text/javascript">
	//<![CDATA[
  function goToUrlPreserveContext(baseUrl) {
    var url = baseUrl;
      var sectorCodeDescriptionFilter = escape(document.getElementById('sectorCodeDescriptionFilter').value);
      var sectorDetailCodeFilter = escape(document.getElementById('sectorDetailCodeFilter').value);
      var sectorDetailCodeDescriptionFilter = escape(document.getElementById('sectorDetailCodeDescriptionFilter').value);
      
      if(url.indexOf("?") >= 0) {
        url = url + "&";
      } else {
        url = url + "?";
      }
      url = url + "setFilterContext=true" + "&sectorDetailCodeDescriptionFilter=" + sectorDetailCodeDescriptionFilter + "&sectorDetailCodeFilter=" + sectorDetailCodeFilter + "&sectorCodeDescriptionFilter=" + sectorCodeDescriptionFilter;
    document.location.href = url;
  }
	//]]>
</script>

<p><fmt:message key="Farm.Types.instruction.text"/></p>

<w:ifUserCanPerformAction action="editCodes">
  <script type="text/javascript">
    //<![CDATA[
    function newButtonFunc() {
      goToUrlPreserveContext('newSectorDetailCode.do');
    }
    //]]>
  </script>
  <u:yuiButton buttonId="newButton" buttonLabel="New.Code" function="newButtonFunc"/>
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
            "sectorCodeDescription":"<c:out value="${result.sectorCodeDescription}"/>",  
            "sectorDetailCode":"<c:out value="${result.sectorDetailCode}"/>",  
            "sectorDetailCodeDescription":"<c:out value="${result.sectorDetailCodeDescription}"/>",  
            "establishedDate":"<fmt:formatDate pattern="yyyy-MM-dd" value="${result.establishedDate}" />",
            "expiryDate":"<fmt:formatDate pattern="yyyy-MM-dd" value="${result.expiryDate}" />"
          }<c:if test="${loop.index < (form.numCodes-1)}">,</c:if>
        </c:forEach> 
      ]
  };


  YAHOO.util.Event.onDOMReady(function() {
      Farm.codesColumnDefs = [
         {key:"sectorCodeDescription", width:120, label:"<fmt:message key="Farm.Type"/>: <input name='sectorCodeDescriptionFilter' type='text' id='sectorCodeDescriptionFilter' value='<c:out value="${form.sectorCodeDescriptionFilter}"/>' maxlength='256' style='width:100px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
         {key:"sectorDetailCode", width:200, label:"<fmt:message key="Farm.Type.Detailed.Code"/>: <input name='sectorDetailCodeFilter' type='text' id='sectorDetailCodeFilter' value='<c:out value="${form.sectorDetailCodeFilter}"/>' maxlength='10' style='width:40px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
         {key:"sectorDetailCodeDescription", width:200, label:"<fmt:message key="Description"/>: <input name='sectorDetailCodeDescriptionFilter' type='text' id='sectorDetailCodeDescriptionFilter' value='<c:out value="${form.sectorDetailCodeDescriptionFilter}"/>' maxlength='256' style='width:100px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"}
     ];

      Farm.filterSectorCodeDescription = function(description) {
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

    Farm.filterSectorDetailCode = function(sectorCode) {
      var sectorDetailCodeFilter = YAHOO.util.Dom.get('sectorDetailCodeFilter');
      if(sectorDetailCodeFilter.value) {
        if(sectorCode.toLowerCase().indexOf(sectorDetailCodeFilter.value.toLowerCase()) >= 0) {
          return true;
        } else {
          return false;
        }
      }
      return true;
    };

    Farm.filterSectorDetailCodeDescription = function(description) {
      var sectorDetailCodeDescriptionFilter = YAHOO.util.Dom.get('sectorDetailCodeDescriptionFilter');
      if(sectorDetailCodeDescriptionFilter.value) {
        if(description.toLowerCase().indexOf(sectorDetailCodeDescriptionFilter.value.toLowerCase()) >= 0) {
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
          if(Farm.filterSectorCodeDescription(data[i].sectorCodeDescription)
             && Farm.filterSectorDetailCode(data[i].sectorDetailCode)
    	       && Farm.filterSectorDetailCodeDescription(data[i].sectorDetailCodeDescription)) {
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
      fields: ["sectorCodeDescription","sectorDetailCode","sectorDetailCodeDescription","establishedDate","expiryDate"]
    };


    Farm.codesConfigs = {
      paginator: new YAHOO.widget.Paginator({ rowsPerPage: 20 })
    };

    Farm.showSelectedRow = function(oArgs) {
      var oRecord = oArgs.record;
      var sectorDetailCode = oRecord.getData("sectorDetailCode");
      var farmAction = "farm775";  // edit page
      
      showProcessing();
      goToUrlPreserveContext(farmAction + ".do?sectorDetailCode=" + sectorDetailCode);
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

    YAHOO.util.Event.on('sectorCodeDescriptionFilter',      'keyup',Farm.updateFilter);
    YAHOO.util.Event.on('sectorDetailCodeFilter',           'keyup',Farm.updateFilter);
    YAHOO.util.Event.on('sectorDetailCodeDescriptionFilter','keyup',Farm.updateFilter);


  });
//]]>
</script>
