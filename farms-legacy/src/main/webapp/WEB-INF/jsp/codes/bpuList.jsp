<%@ include file="/WEB-INF/jsp/common/include.jsp"%>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp"%>

<script type="text/javascript">
//<![CDATA[
  var growingForward2013 = ("<c:out value="${form.growingForward2013}"/>" == "true") ? true : false;
  
  function goToUrlPreserveContext(baseUrl) {
    var url = baseUrl;
    <c:if test="${form.numBPUs > 0}">
      var inventoryCodeFilter = escape(document.getElementById('inventoryCodeFilter').value);
      var inventoryDescFilter = escape(document.getElementById('inventoryDescFilter').value);
      var municipalityFilter = escape(document.getElementById('municipalityFilter').value);
      var marginExpenseFilter = $('input:radio[name=marginExpenseFilter]:checked').val();
      if(url.indexOf('?') >= 0) {
        url = url + '&';
      } else {
        url = url + '?';
      }
      url = url + "yearFilter=<c:out value="${form.yearFilter}"/>" + "&inventoryCodeFilter=" + inventoryCodeFilter + "&inventoryDescFilter=" 
          + inventoryDescFilter + "&municipalityFilter=" + municipalityFilter + "&marginExpenseFilter=" + marginExpenseFilter;
    </c:if>
    document.location.href = url;
  }
  
  function checkReadyState(iframe) {
    if (iframe.readyState == "loading") {
      // undo the showProcessing javascript call
      undoShowProcessing();
    }
  }

//]]>
</script>

<span style="float: left;">
  <w:ifUserCanPerformAction action="editCodes">
  
    <c:choose>
      <c:when test="${form.numBPUs == 0}">
        <c:set var="buttonsDisabled" value="true"/>
      </c:when>
      <c:otherwise>
        <script>
  
            function newBPU() {
              goToUrlPreserveContext('<html:rewrite action="newBPU"/>');
            }
  
          </script>
      </c:otherwise>
    </c:choose>
  
    <u:yuiButton buttonId="newButton" buttonLabel="New.BPU" function="newBPU" disabled="${buttonsDisabled}" />
  
  </w:ifUserCanPerformAction>
</span>


<script>
  
    function exportBPUs() {
      var exportUrl = "exportBPUs.do";
      
      exportUrl += "?yearFilter=<c:out value='${form.yearFilter}'/>";
      
      var inventoryCodeFilter = document.getElementById('inventoryCodeFilter').value;
      var inventoryDescFilter = document.getElementById('inventoryDescFilter').value;
      var municipalityFilter = document.getElementById('municipalityFilter').value;
      var marginExpenseFilter = $('input:radio[name=marginExpenseFilter]:checked').val();
      
      if(inventoryCodeFilter.length > 0) {
        exportUrl += "&inventoryCodeFilter=" + escape(inventoryCodeFilter);
      }
      if(inventoryDescFilter.length > 0) {
        exportUrl += "&inventoryDescFilter=" + escape(inventoryDescFilter);
      }
      if(municipalityFilter.length > 0) {
        exportUrl += "&municipalityFilter=" + escape(municipalityFilter);
      }
      if(marginExpenseFilter.length > 0) {
          exportUrl += "&marginExpenseFilter=" + escape(marginExpenseFilter);
      }

      var ifm = document.getElementById("reportIframe"); 
      ifm.src = exportUrl;
      
      //showProcessing();
      
    }
    
    function missingBPUs() {
      if(confirm("<fmt:message key="missing.bpus.report.warning" />")) {
        var exportUrl = "exportMissingBPUs.do";
        
        exportUrl += "?yearFilter=<c:out value='${form.yearFilter}'/>";
        
        //showProcessing();
        
        var ifm = document.getElementById("reportIframe"); 
        ifm.src = exportUrl;
      }
    }
  </script>

<u:yuiButton buttonId="exportButton" buttonLabel="Export" function="exportBPUs" disabled="${buttonsDisabled}" />
<u:yuiButton buttonId="missingBpuButton" buttonLabel="Missing.BPUs" function="missingBPUs" disabled="${buttonsDisabled}" />

<span style="float: right;">    
<u:menuSelect action="farm290.do"
  name="yearFilterPicker" paramName="yearFilter"
  additionalFieldIds="inventoryCodeFilter,inventoryDescFilter,municipalityFilter,marginFilter,expenseFilter"
  options="${form.programYearSelectOptions}"
  selectedValue="${form.yearFilter}"
  toolTip="Click here to open a different Program Year." /> 
</span>


<c:choose>
  <c:when test="${form.numBPUs == 0}">
    <p><fmt:message key="There.are.no.BPUs.for" /> <c:out value="${form.yearFilter}" />.</p>
    <html:form action="farm290">
      <html:hidden property="inventoryCodeFilter" styleId="inventoryCodeFilter" />
      <html:hidden property="inventoryDescFilter" styleId="inventoryDescFilter" />
      <html:hidden property="municipalityFilter" styleId="municipalityFilter" />
      <html:hidden property="marginExpenseFilter" styleId="marginExpenseFilter" />
    </html:form>
  </c:when>
  <c:otherwise>
    <html:form action="farm290">
      <fieldset id="bpuMarginExpense" style="margin:0px 325px 10px 325px;">
      <table>
      <tr><td style="padding-right:15px;">
        <html:radio property="marginExpenseFilter" styleId="marginFilter" value="M"/><fmt:message key="Margin"/>
      </td><td>
        <html:radio property="marginExpenseFilter" styleId="expenseFilter" value="E"/><fmt:message key="Expense"/>
      </td></tr>
      </table>
      </fieldset>
    </html:form>
  </c:otherwise>
</c:choose>

<c:if test="${form.numBPUs > 0}">

  <div class="searchresults">
  <div id="searchresults"></div>
  </div>

  <iframe id="reportIframe" src="" style="visibility:hidden" onReadyStateChange="checkReadyState(this)"></iframe>

  <script type="text/javascript">
  //<![CDATA[
Farm.codes = {
"recordsReturned": <c:out value="${form.numBPUs}"/>,
"totalRecords": <c:out value="${form.numBPUs}"/>,
"startIndex":0,
"sort":null,
"dir":"asc",
"pageSize": 10,
"records":[
<c:forEach varStatus="resultLoop" var="result" items="${form.bpus}">{
i:"<c:out value="${result.invSgCode}"/>",
d:"<c:out value="${result.invSgCodeDescription}"/>",
m:"<c:out value="${result.municipalityCode}"/>",
e:"<c:out value="${result.municipalityCodeDescription}"/>",
x:"<c:out value="${result.bpuId}"/>",
<c:forEach varStatus="yearLoop" var="year" items="${result.years}">
y<c:out value="${yearLoop.index}"/>:"<fmt:formatNumber type="number" value="${year.averageMargin}"/>",
z<c:out value="${yearLoop.index}"/>:"<fmt:formatNumber type="number" value="${year.averageExpense}"/>"<c:if test="${yearLoop.index < 5}">,</c:if>
</c:forEach>
}<c:if test="${resultLoop.index < (form.numBPUs-1)}">,</c:if>
</c:forEach>
]
};

YAHOO.util.Event.onDOMReady(function() { 
  if (!growingForward2013) {
    document.getElementById("bpuMarginExpense").setAttribute("style","visibility:hidden;");
  }
  
  Farm.codesColumnDefs = [
           {key:"i", label:"<fmt:message key="Code"/><br /><input name='inventoryCodeFilter' type='text' id='inventoryCodeFilter' value='<c:out value="${form.inventoryCodeFilter}"/>' maxlength='10' style='width:40px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
           {key:"d", label:"<fmt:message key="Description"/><br /><input name='inventoryDescFilter' type='text' id='inventoryDescFilter' value='<c:out value="${form.inventoryDescFilter}"/>' maxlength='256' style='width:100px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
           {key:"e", label:"<fmt:message key="Municipality"/><br /><input name='municipalityFilter' type='text' id='municipalityFilter' value='<c:out value="${form.municipalityFilter}"/>' maxlength='256' style='width:100px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
           <c:forEach varStatus="yearLoop" var="year" items="${form.years}">
             {key:"y<c:out value="${yearLoop.index}"/>", label:"<c:out value="${year}"/>", sortable:false, className:"tdCenter"},
             {key:"z<c:out value="${yearLoop.index}"/>", label:"<c:out value="${year}"/>", sortable:false, className:"tdCenter"}<c:if test="${yearLoop.index < 5}">,</c:if>
           </c:forEach>
      ];

      Farm.filterInvCode = function(value) {
        var filter = YAHOO.util.Dom.get('inventoryCodeFilter');
        if(filter) {
          if((value+'').indexOf(filter.value) == 0) {
            return true;
          } else {
            return false;
          }
        }
        return true;
      };

      Farm.filterInvDesc = function(value) {
        var filter = YAHOO.util.Dom.get('inventoryDescFilter');
        if(filter) {
          if(value.toLowerCase().indexOf(filter.value.toLowerCase()) == 0) {
            return true;
          } else {
            return false;
          }
        }
        return true;
      };

      Farm.filterMunicipality = function(value) {
        var filter = YAHOO.util.Dom.get('municipalityFilter');
        if(filter) {
          if(value.toLowerCase().indexOf(filter.value.toLowerCase()) == 0) {
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
              ii,l;

          for (ii = 0, l = data.length; ii < l; ii++) {
            if(   Farm.filterInvCode(data[ii].i)
               && Farm.filterInvDesc(data[ii].d)
               && Farm.filterMunicipality(data[ii].e)) {
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
        fields: ["i","d","m","e","x",
                 <c:forEach varStatus="yearLoop" var="year" items="${form.years}">
                   "y<c:out value="${yearLoop.index}"/>",
                   "z<c:out value="${yearLoop.index}"/>"<c:if test="${yearLoop.index < 5}">,</c:if>
                 </c:forEach>
                ]
      };


      Farm.codesConfigs = {
        paginator: new YAHOO.widget.Paginator({ rowsPerPage: 20 })
      };

      Farm.showSelectedRow = function(oArgs) {
        var oRecord = oArgs.record;
        var bpuId = oRecord.getData("x");
        var farmAction = "farm291";  // edit page

        showProcessing();
        goToUrlPreserveContext(farmAction + ".do?bpuId=" + bpuId);
      };

      Farm.codesDataTable = new YAHOO.widget.DataTable("searchresults", Farm.codesColumnDefs, Farm.codesDataSource, Farm.codesConfigs);
      Farm.codesDataTable.subscribe("rowMouseoverEvent", Farm.codesDataTable.onEventHighlightRow);
      Farm.codesDataTable.subscribe("rowMouseoutEvent", Farm.codesDataTable.onEventUnhighlightRow);
      Farm.codesDataTable.subscribe("rowClickEvent", Farm.codesDataTable.onEventSelectRow);
      Farm.codesDataTable.subscribe("rowSelectEvent", Farm.showSelectedRow);
      
      Farm.codesDataTable.subscribe('renderEvent', function(oArgs) {         
        if(YAHOO.util.Dom.get('expenseFilter').checked && growingForward2013) {
          // hide six margin year columns
          Farm.codesDataTable.hideColumn(3);
          Farm.codesDataTable.hideColumn(5);
          Farm.codesDataTable.hideColumn(7);
          Farm.codesDataTable.hideColumn(9);
          Farm.codesDataTable.hideColumn(11);
          Farm.codesDataTable.hideColumn(13);
          // show six expense year columns
          Farm.codesDataTable.showColumn(4);
          Farm.codesDataTable.showColumn(6);
          Farm.codesDataTable.showColumn(8);
          Farm.codesDataTable.showColumn(10);
          Farm.codesDataTable.showColumn(12);
          Farm.codesDataTable.showColumn(14);
        } else {
          // show six margin year columns
          Farm.codesDataTable.showColumn(3);
          Farm.codesDataTable.showColumn(5);
          Farm.codesDataTable.showColumn(7);
          Farm.codesDataTable.showColumn(9);
          Farm.codesDataTable.showColumn(11);
          Farm.codesDataTable.showColumn(13);
          // hide six expense year columns
          Farm.codesDataTable.hideColumn(4);
          Farm.codesDataTable.hideColumn(6);
          Farm.codesDataTable.hideColumn(8);
          Farm.codesDataTable.hideColumn(10);
          Farm.codesDataTable.hideColumn(12);
          Farm.codesDataTable.hideColumn(14);
        }
      });

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

      YAHOO.util.Event.on('inventoryCodeFilter','keyup',Farm.updateFilter);
      YAHOO.util.Event.on('inventoryDescFilter','keyup',Farm.updateFilter);
      YAHOO.util.Event.on('municipalityFilter','keyup',Farm.updateFilter);
      YAHOO.util.Event.on('marginFilter','click',Farm.updateFilter);
      YAHOO.util.Event.on('expenseFilter','click',Farm.updateFilter);
  
  });
  //]]>
  </script>

</c:if>

