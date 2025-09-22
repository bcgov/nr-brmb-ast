<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp" %>


<table>
<tr>
<td><h1><fmt:message key="Chefs.title"/></h1></td>
<td style="width:360px;">
  <div style="float:right;text-align:center">
	  <fieldset style="width:100%;">
	     <legend><fmt:message key="Chefs.select.form.type"/></legend>
	     <u:menuSelect action="farm256.do"
	         name="formTypePicker"
	         paramName="formType"
	         options="${form.formTypes}"
	         selectedValue="${form.formType}"
	         toolTip="Click here to open a different Program Year."/>
    </fieldset>
  </div>
</td>
</tr>
</table>

<table style="width:100%; margin-bottom:15px;">
  <tr>
    <th>Form Type</th>
    <th>CDOGS Template Guid</th>
    <th>Status</th>
  </tr>
<c:forEach var="template" items="${form.cdogsTemplates}">
  <tr>
    <td><c:out value="${template.description}"/></td>
    <td><c:out value="${template.templateGuid}"/></td>
    <td>
      <c:choose>
        <c:when test="${template.isCached}">
           <span style="color:green;">OK</span>
        </c:when>
        <c:otherwise>
          <span style="color:red;">ERROR</span>
        </c:otherwise>
      </c:choose> 
    </td>
  </tr>
</c:forEach>
</table>
 

<c:choose>
  <c:when test="${form.numSearchResults > 0}">
    <div style="margin-bottom: 5px;">
      <label for='guidFilter'style="font-weight: normal"><fmt:message key="Chefs.submissionGuid"/></label>
      <input name='guidFilter' type='text' id='guidFilter' value='<c:out value="${result.submissionGuid}"/>' maxlength='256'  style='width:230px;padding:0px;font-size:11px'/>
    </div>
    <div class="searchresults">
      <div id="searchresults"></div>
    </div> 

    <script type="text/javascript">
     var data = {
          "recordsReturned": <c:out value="${form.numSearchResults}"/>,
          "totalRecords": <c:out value="${form.numSearchResults}"/>,
          "startIndex":0,
          "sort":null,
          "dir":"asc",
          "pageSize": 10,
          "records":[
            <c:forEach varStatus="loop" var="result" items="${form.searchResults}">
              {
                "submissionGuid":"<c:out value="${result.submissionGuid}"/>",  
                "formTypeCode":"<c:out value="${result.formTypeCode}"/>",  
                "submissionStatusCode":"<fmt:message key="Chefs.${result.submissionStatusCode}"/>",
                "userFormTypeCode":"<c:out value="${result.userFormTypeCode}"/>",
                "updated":"<fmt:formatDate value="${result.updatedDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
              }<c:if test="${loop.index < (form.numSearchResults-1)}">,</c:if>
            </c:forEach> 
          ]
       };
         

       YAHOO.util.Event.onDOMReady(function() {
        var columnDefs = [
            {key:"submissionGuid", label:"<fmt:message key="Chefs.submissionGuid"/>", sortable:true},
            {key:"submissionStatusCode", label:"<fmt:message key="Chefs.submissionStatusCode"/>", sortable:true},
            {key:"userFormTypeCode", label:"<fmt:message key="Chefs.userFormTypeCode"/>", sortable:true},
            {key:"updated", label:"<fmt:message key="Chefs.updated.date"/>", sortable:true}
        ];
  
        Farm.codesDataSource = new YAHOO.util.DataSource(data,{
          doBeforeCallback : function (req,raw,res,cb) {
            // This is the filter function 
            var data     = res.results || [], 
                filtered = [], 
                i,l; 
    
            for (i = 0, l = data.length; i < l; ++i) {
              if(Farm.filterSubmissionGuid(data[i].submissionGuid)) {
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
            fields: [
            	{key:"submissionGuid"},
            	{key:"formTypeCode"},
            	{key:"submissionStatusCode"},
            	{key:"userFormTypeCode"},
            	{key:"updated", parser:"string"}
            ]
        };
  
        var configs = {
            paginator: new YAHOO.widget.Paginator({ rowsPerPage: 20 }),
            sortedBy : {key:"updated", dir:YAHOO.widget.DataTable.CLASS_DESC},
        };
  
        function showSelectedRow(oArgs) {
          var oRecord = oArgs.record;
          var submissionGuid = oRecord.getData("submissionGuid");
          var formTypeCode = oRecord.getData("formTypeCode");
          var farmAction = "farm257";
          
          showProcessing();
          var url = farmAction + ".do?submissionGuid=" + submissionGuid + "&formType=" + formTypeCode;
          document.location.href = url;
        };
        
        Farm.filterSubmissionGuid = function(guid) {
          var guidFilter = YAHOO.util.Dom.get('guidFilter');
          if(guidFilter.value) {
            if(guid.toLowerCase().indexOf(guidFilter.value.toLowerCase()) < 0) {
              return false;
            } 
          }
          return true;      
        };
        
        Farm.codesDataTable = new YAHOO.widget.DataTable("searchresults", columnDefs, Farm.codesDataSource, configs);
        Farm.codesDataTable.subscribe("rowMouseoverEvent", Farm.codesDataTable.onEventHighlightRow);
        Farm.codesDataTable.subscribe("rowMouseoutEvent", Farm.codesDataTable.onEventUnhighlightRow);
        Farm.codesDataTable.subscribe("rowClickEvent", Farm.codesDataTable.onEventSelectRow);
        Farm.codesDataTable.subscribe("rowSelectEvent", showSelectedRow);
        
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

        YAHOO.util.Event.on('guidFilter','keyup',Farm.updateFilter);
        
      });
    </script> 

  </c:when>
  <c:otherwise>
    No submissions processed yet.
  </c:otherwise>

</c:choose>
