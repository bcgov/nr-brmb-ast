<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp" %>

<w:ifUserCanPerformAction action="editCodes">
  <a id="newButton" href="<html:rewrite action="newMunicipalityCode"/>"><fmt:message key="New.Code"/></a>
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
         {key:"code", width:80, label:"<fmt:message key="Code"/>: <input name='codeFilter' type='text' id='codeFilter' maxlength='10' style='width:40px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
         {key:"description", width:300, label:"<fmt:message key="Description"/>: <input name='descriptionFilter' type='text' id='descriptionFilter' maxlength='256' style='width:100px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
         {key:"establishedDate", width:90, label:"<fmt:message key="Established.Date"/>", sortable:false, className:"tdCenter"},
         {key:"expiryDate", width:70, label:"<fmt:message key="Expiry.Date"/>", sortable:false, className:"tdCenter"}
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

    Farm.codesDataSource = new YAHOO.util.DataSource(Farm.codes,{
      doBeforeCallback : function (req,raw,res,cb) {
        // This is the filter function 

        var data     = res.results || [], 
            filtered = [], 
            i,l; 

        for (i = 0, l = data.length; i < l; ++i) {
          if(Farm.filterCode(data[i].code)
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
      //paginator: new YAHOO.widget.Paginator({ rowsPerPage: 10 })
    };

    Farm.showSelectedRow = function(oArgs) {
      var oRecord = oArgs.record;
      var code = oRecord.getData("code");
      var farmAction = "farm260";  // edit page
      
      showProcessing();
      document.location.href = farmAction + ".do?code=" + code;
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


  });
//]]>
</script>

<script type="text/javascript">
//<![CDATA[
  new YAHOO.widget.Button("newButton");
//]]>
</script>
