<%@ include file="/WEB-INF/jsp/common/include.jsp"%>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp"%>

<script>
  function goToUrlPreserveContext(baseUrl) {
    var url = baseUrl;
    <c:if test="${form.numFMVs > 0}">
      var inventoryCodeFilter = escape(document.getElementById('inventoryCodeFilter').value);
      var inventoryDescFilter = escape(document.getElementById('inventoryDescFilter').value);
      var municipalityFilter = escape(document.getElementById('municipalityFilter').value);
      var cropUnitFilter = escape(document.getElementById('cropUnitFilter').value);
      if(url.indexOf('?') >= 0) {
        url = url + '&';
      } else {
        url = url + '?';
      }
      url = url + "yearFilter=<c:out value="${form.yearFilter}"/>" + "&inventoryCodeFilter=" + inventoryCodeFilter + "&inventoryDescFilter=" + inventoryDescFilter + "&municipalityFilter=" + municipalityFilter + "&cropUnitFilter=" + cropUnitFilter;
    </c:if>
    document.location.href = url;
  }
  
  function checkReadyState(iframe) {
    if (iframe.readyState == "loading") {
      // undo the showProcessing javascript call
      undoShowProcessing();
    }
  }

</script>

<span style="float: left;">
  <w:ifUserCanPerformAction action="editCodes">

    <c:choose>
      <c:when test="${form.numFMVs == 0}">
        <c:set var="buttonsDisabled" value="true"/>
      </c:when>
      <c:otherwise>
        <script>
  
            function newFMV() {
              goToUrlPreserveContext('<html:rewrite action="newFMV"/>');
            }
  
          </script>
      </c:otherwise>
    </c:choose>
  
    <u:yuiButton buttonId="newButton" buttonLabel="New.FMV" function="newFMV"
       disabled="${buttonsDisabled}" />

  </w:ifUserCanPerformAction>
</span>

<c:if test="${form.numFMVs > 0}">

  <script>
      
    function exportFMVs() {
      var exportUrl = "exportFMVs.do";
      
      exportUrl += "?yearFilter=<c:out value='${form.yearFilter}'/>";
      
      var inventoryCodeFilter = document.getElementById('inventoryCodeFilter').value;
      var inventoryDescFilter = document.getElementById('inventoryDescFilter').value;
      var municipalityFilter = document.getElementById('municipalityFilter').value;
      var cropUnitFilter = document.getElementById('cropUnitFilter').value;
      
      if(inventoryCodeFilter.length > 0) {
        exportUrl += "&inventoryCodeFilter=" + escape(inventoryCodeFilter);
      }
      if(inventoryDescFilter.length > 0) {
        exportUrl += "&inventoryDescFilter=" + escape(inventoryDescFilter);
      }
      if(municipalityFilter.length > 0) {
        exportUrl += "&municipalityFilter=" + escape(municipalityFilter);
      }
      if(cropUnitFilter.length > 0) {
        exportUrl += "&cropUnitFilter=" + escape(cropUnitFilter);
      }
      
      //showProcessing();
      
      var ifm = document.getElementById("reportIframe"); 
      ifm.src = exportUrl;
    }
    
    function missingFMVs() {
      if(confirm("<fmt:message key="missing.fmvs.report.warning" />")) {
        var exportUrl = "exportMissingFMVs.do";
        
        exportUrl += "?yearFilter=<c:out value='${form.yearFilter}'/>";
        
        //showProcessing();
        
        var ifm = document.getElementById("reportIframe"); 
        ifm.src = exportUrl;
      }
    }
  </script>
  
  <u:yuiButton buttonId="exportButton" buttonLabel="Export" function="exportFMVs"
     disabled="${buttonsDisabled}" />
  <u:yuiButton buttonId="missingFmvButton" buttonLabel="Missing.FMVs" function="missingFMVs"
     disabled="${buttonsDisabled}" />
</c:if>

<span style="float: right;"> <u:menuSelect action="farm286.do"
  name="yearFilterPicker" paramName="yearFilter"
  additionalFieldIds="inventoryCodeFilter,inventoryDescFilter,municipalityFilter,cropUnitFilter"
  options="${form.programYearSelectOptions}"
  selectedValue="${form.yearFilter}"
  toolTip="Click here to open a different Program Year." /> </span>
<br />
<br />



<c:if test="${form.numFMVs == 0}">
  <p><fmt:message key="There.are.no.FMVs.for" /> <c:out
    value="${form.yearFilter}" />.</p>
  <html:form action="farm286">
    <html:hidden property="inventoryCodeFilter"
      styleId="inventoryCodeFilter" />
    <html:hidden property="inventoryDescFilter"
      styleId="inventoryDescFilter" />
    <html:hidden property="municipalityFilter"
      styleId="municipalityFilter" />
    <html:hidden property="cropUnitFilter" styleId="cropUnitFilter" />
  </html:form>
</c:if>

<c:if test="${form.numFMVs > 0}">

  <div class="searchresults">
  <div id="searchresults"></div>
  </div>

  <iframe id="reportIframe" src="" style="visibility:hidden" onReadyStateChange="checkReadyState(this)"></iframe>

  <script type="text/javascript">
  //<![CDATA[
Farm.codes = {
"recordsReturned": <c:out value="${form.numFMVs}"/>,
"totalRecords": <c:out value="${form.numFMVs}"/>,
"startIndex":0,
"sort":null,
"dir":"asc",
"pageSize": 10,
"records":[
<c:forEach varStatus="resultLoop" var="result" items="${form.fmvs}">{
i:<c:out value="${result.inventoryItemCode}"/>,
d:"<c:out value="${result.inventoryItemCodeDescription}"/>",
m:<c:out value="${result.municipalityCode}"/>,
e:"<c:out value="${result.municipalityCodeDescription}"/>",
u:<c:out value="${result.cropUnitCode}"/>,
f:"<c:out value="${result.cropUnitCodeDescription}"/>",
<c:forEach varStatus="periodLoop" var="period" items="${result.periods}">
p<c:out value="${periodLoop.index + 1}"/>:"<fmt:formatNumber type="number" value="${period.price}"/>"<c:if test="${periodLoop.index < 11}">,</c:if>
</c:forEach>
}<c:if test="${resultLoop.index < (form.numFMVs-1)}">,</c:if>
</c:forEach>
]
};


    YAHOO.util.Event.onDOMReady(function() {
      Farm.codesColumnDefs = [
           {key:"i", label:"<fmt:message key="Inv.Code"/><br /><input name='inventoryCodeFilter' type='text' id='inventoryCodeFilter' value='<c:out value="${form.inventoryCodeFilter}"/>' maxlength='10' style='width:40px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
           {key:"d", label:"<fmt:message key="Description"/><br /><input name='inventoryDescFilter' type='text' id='inventoryDescFilter' value='<c:out value="${form.inventoryDescFilter}"/>' maxlength='256' style='width:100px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
           {key:"e", label:"<fmt:message key="Municipality"/><br /><input name='municipalityFilter' type='text' id='municipalityFilter' value='<c:out value="${form.municipalityFilter}"/>' maxlength='256' style='width:100px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
           {key:"f", label:"<fmt:message key="Unit"/><br /><input name='cropUnitFilter' type='text' id='cropUnitFilter' value='<c:out value="${form.cropUnitFilter}"/>' maxlength='256' style='width:70px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
           <c:forEach varStatus="periodLoop" var="period" items="${form.periods}">
             {key:"p<c:out value="${period}"/>", label:"<c:out value="${period}"/>", sortable:false, className:"tdCenter"}<c:if test="${period < 12}">,</c:if>
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

      Farm.filterCropUnit = function(value) {
        var filter = YAHOO.util.Dom.get('cropUnitFilter');
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
               && Farm.filterMunicipality(data[ii].e)
               && Farm.filterCropUnit(data[ii].f)) {
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
        fields: ["i","d","m","e","u","f",
                 <c:forEach varStatus="periodLoop" var="period" items="${form.periods}">
                   "p<c:out value="${period}"/>"<c:if test="${period < 12}">,</c:if>
                 </c:forEach>
                ]
      };


      Farm.codesConfigs = {
        paginator: new YAHOO.widget.Paginator({ rowsPerPage: 20 })
      };

      Farm.showSelectedRow = function(oArgs) {
        var oRecord = oArgs.record;
        var invCode = oRecord.getData("i");
        var municipality = oRecord.getData("m");
        var cropUnit = oRecord.getData("u");
        var farmAction = "farm287";  // edit page

        showProcessing();
        goToUrlPreserveContext(farmAction + ".do?inventoryItemCode=" + invCode + "&municipalityCode=" + municipality + "&cropUnitCode=" + cropUnit);
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

      YAHOO.util.Event.on('inventoryCodeFilter','keyup',Farm.updateFilter);
      YAHOO.util.Event.on('inventoryDescFilter','keyup',Farm.updateFilter);
      YAHOO.util.Event.on('municipalityFilter','keyup',Farm.updateFilter);
      YAHOO.util.Event.on('cropUnitFilter','keyup',Farm.updateFilter);


    });
  //]]>
  </script>

</c:if>

