<%@ include file="/WEB-INF/jsp/common/include.jsp"%>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp"%>

<c:set var="dataExists" value="${form.numCropUnitConversions > 0}" />

<script type="text/javascript">
//<![CDATA[
  function goToUrlPreserveContext(baseUrl) {
    var url = baseUrl;
    <c:if test="${dataExists}">
      var inventoryCodeFilter = escape(document.getElementById('inventoryCodeFilter').value);
      var inventoryDescFilter = escape(document.getElementById('inventoryDescFilter').value);
      if(url.indexOf('?') >= 0) {
        url = url + '&';
      } else {
        url = url + '?';
      }
      url = url + "updateFilterContext=true&inventoryCodeFilter=" + inventoryCodeFilter + "&inventoryDescFilter=" + inventoryDescFilter;
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

<span style="float: left;"> <w:ifUserCanPerformAction action="editCodes">

  <script>
    function newCropUnitConversion() {
      goToUrlPreserveContext('<html:rewrite action="newCropUnitConversion"/>');
    }
  </script>

  <u:yuiButton buttonId="newButton" buttonLabel="New.Crop.Unit.Conversion" function="newCropUnitConversion" />

</w:ifUserCanPerformAction> </span>

<c:if test="${form.numCropUnitConversions == 0}">
  <p><fmt:message key="There.is.no.crop.unit.conversion.data" /></p>
  <html:form action="farm288">
    <html:hidden property="inventoryCodeFilter"
      styleId="inventoryCodeFilter" />
    <html:hidden property="inventoryDescFilter"
      styleId="inventoryDescFilter" />
  </html:form>
</c:if>

<br /><br />

<c:if test="${form.numCropUnitConversions > 0}">

  <div class="searchresults">
  <div id="searchresults"></div>
  </div>

  <iframe id="reportIframe" src="" style="visibility:hidden" onReadyStateChange="checkReadyState(this)"></iframe>

  <script type="text/javascript">
  //<![CDATA[
Farm.codes = {
"recordsReturned": <c:out value="${form.numCropUnitConversions}"/>,
"totalRecords": <c:out value="${form.numCropUnitConversions}"/>,
"startIndex":0,
"sort":null,
"dir":"asc",
"pageSize": 10,
"records":[
<c:forEach varStatus="resultLoop" var="result" items="${form.cropUnitConversions}">{
i:<c:out value="${result.inventoryItemCode}"/>,
d:"<c:out value="${result.inventoryItemCodeDescription}"/>"},
</c:forEach>
]
};


    YAHOO.util.Event.onDOMReady(function() {
      Farm.codesColumnDefs = [
           {key:"i", label:"<fmt:message key="Inv.Code"/><br /><input name='inventoryCodeFilter' type='text' id='inventoryCodeFilter' value='<c:out value="${form.inventoryCodeFilter}"/>' maxlength='10' style='width:40px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"},
           {key:"d", label:"<fmt:message key="Description"/><br /><input name='inventoryDescFilter' type='text' id='inventoryDescFilter' value='<c:out value="${form.inventoryDescFilter}"/>' maxlength='256' style='width:100px;padding:0px;font-size:10px'/>", sortable:false, className:"tdCenter"}
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
          if(value.toLowerCase().indexOf(filter.value.toLowerCase()) >= 0) {
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
               && Farm.filterInvDesc(data[ii].d)) {
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
        fields: ["i","d"]
      };


      Farm.codesConfigs = {
        paginator: new YAHOO.widget.Paginator({ rowsPerPage: 20 })
      };

      Farm.showSelectedRow = function(oArgs) {
        var oRecord = oArgs.record;
        var invCode = oRecord.getData("i");
        var farmAction = "farm289";  // edit page

        showProcessing();
        goToUrlPreserveContext(farmAction + ".do?inventoryItemCode=" + invCode);
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


    });
  //]]>
  </script>

</c:if>

