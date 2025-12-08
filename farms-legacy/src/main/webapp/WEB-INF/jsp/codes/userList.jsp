<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp" %>

<div class="searchresults">
  <div id="searchresults"></div>
</div> 

<script type="text/javascript">
//<![CDATA[
  Farm.codes = {
      "recordsReturned": <c:out value="${form.numUsers}"/>,
      "totalRecords": <c:out value="${form.numUsers}"/>,
      "startIndex":0,
      "sort":null,
      "dir":"asc",
      "pageSize": 10,
      "records":[
        <c:forEach varStatus="loop" var="result" items="${form.users}">
          {
            "userGuid":"<c:out value="${result.userGuid}" />",
            "user":"<c:out value="${result.accountName}"/>",  
            "emailAddress":"<c:out value="${result.emailAddress}"/>",  
            "verifier":"<c:out value="${result.verifierInd}" />",
            "deleted":"<c:out value="${result.deletedInd}" />"
          }<c:if test="${loop.index < (form.numUsers-1)}">,</c:if>
        </c:forEach> 
      ]
  };


  YAHOO.util.Event.onDOMReady(function() {
      Farm.codesColumnDefs = [
         {key:"user", width:180, label:"<fmt:message key="Account.Name"/>: <input name='userFilter' type='text' id='userFilter' maxlength='100' style='width:100px;padding:0px;font-size:10px'/>", sortable:false, className:""},
         {key:"emailAddress", width:180, label:"<fmt:message key="Email.Address"/>: <input name='emailFilter' type='text' id='emailFilter' maxlength='256' style='width:150px;padding:0px;font-size:10px'/>", sortable:false, className:""},
         {key:"verifier", width:50, label:"<fmt:message key="Verifier"/>", sortable:false, className:"tdCenter"},
         {key:"deleted", width:50, label:"<fmt:message key="Deleted"/>", sortable:false, className:"tdCenter"}
     ];

    Farm.filterCode = function(user) {
      var userFilter = YAHOO.util.Dom.get('userFilter');
      if(userFilter.value) {
        if(user.toUpperCase().indexOf(userFilter.value.toUpperCase()) >= 0) {
          return true;
        } else {
          return false;
        }
      }
      return true;      
    };

    Farm.filterDescription = function(email) {
      var emailFilter = YAHOO.util.Dom.get('emailFilter');
      if(emailFilter.value) {
        if(email.toLowerCase().indexOf(emailFilter.value.toLowerCase()) >= 0) {
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
          if(Farm.filterCode(data[i].user)
    	       && Farm.filterDescription(data[i].emailAddress)) {
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
      fields: ["user","emailAddress","verifier","deleted", "userGuid"]
    };

    Farm.codesConfigs = {
      //paginator: new YAHOO.widget.Paginator({ rowsPerPage: 10 })
    };

    Farm.showSelectedRow = function(oArgs) {
      var oRecord = oArgs.record;
      var userGuid = oRecord.getData("userGuid");
      var farmAction = "farm777";  // edit page
      showProcessing();
      document.location.href = farmAction + ".do?userGuid=" + userGuid;
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

    YAHOO.util.Event.on('userFilter','keyup',Farm.updateFilter);
    YAHOO.util.Event.on('emailFilter','keyup',Farm.updateFilter);


  });
//]]>
</script>