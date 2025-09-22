<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp" %>

<w:ifUserCanPerformAction action="editCodes">
  <script type="text/javascript">
    //<![CDATA[
    function newButtonFunc() {
      document.location.href = 'newMarketRatePremium.do';
    }
    //]]>
  </script>
  <u:yuiButton buttonId="newButton" buttonLabel="New.Row" function="newButtonFunc"/>
</w:ifUserCanPerformAction>

<br />
<br />

<div class="searchresults">
  <div id="searchresults"></div>
</div>

<script type="text/javascript">
//<![CDATA[
  Farm.mrps = {
      "recordsReturned": <c:out value="${form.numMarketRatePremiums}"/>,
      "totalRecords": <c:out value="${form.numMarketRatePremiums}"/>,
      "startIndex":0,
      "sort":null,
      "dir":"asc",
      "pageSize": 10,
      "records":[
        <c:forEach varStatus="loop" var="result" items="${form.marketRatePremiums}">
          {
            "marketRatePremiumId":"<c:out value="${result.marketRatePremiumId}" />",
            "minTotalPremiumAmount":"<c:out value="${result.minTotalPremiumAmount}"/>",
            "maxTotalPremiumAmount":"<c:out value="${result.maxTotalPremiumAmount}"/>",
            "riskChargeFlatAmount":"<c:out value="${result.riskChargeFlatAmount}" />",
            "riskChargePercentagePremium":"<c:out value="${result.riskChargePercentagePremium}" />",
            "adjustChargeFlatAmount":"<c:out value="${result.adjustChargeFlatAmount}" />"
          }<c:if test="${loop.index < (form.numMarketRatePremiums-1)}">,</c:if>
        </c:forEach>
      ]
  };


  YAHOO.util.Event.onDOMReady(function() {
      Farm.codesColumnDefs = [
         {key:"minTotalPremiumAmount", label:"<fmt:message key="Min.Total.Premium.Amount"/>", sortable:false, className:"tdCenter"},
         {key:"maxTotalPremiumAmount", label:"<fmt:message key="Max.Total.Premium.Amount"/>", sortable:false, className:"tdCenter"},
         {key:"riskChargeFlatAmount", label:"<fmt:message key="Risk.Charge.Flat.Amount"/>", sortable:false, className:"tdCenter"},
         {key:"riskChargePercentagePremium", label:"<fmt:message key="Risk.Charge.Percentage.Premium"/>", sortable:false, className:"tdCenter"},
         {key:"adjustChargeFlatAmount", label:"<fmt:message key="Adjust.Charge.Flat.Amount"/>", sortable:false, className:"tdCenter"}
     ];

    Farm.codesDataSource = new YAHOO.util.DataSource(Farm.mrps);

    Farm.codesDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
    Farm.codesDataSource.responseSchema = {
      resultsList: "records",
      fields: ["marketRatePremiumId","minTotalPremiumAmount","maxTotalPremiumAmount","riskChargeFlatAmount","riskChargePercentagePremium", "adjustChargeFlatAmount"]
    };

    Farm.codesConfigs = {
      //paginator: new YAHOO.widget.Paginator({ rowsPerPage: 10 })
    };

    Farm.showSelectedRow = function(oArgs) {
      var oRecord = oArgs.record;
      var marketRatePremiumId = oRecord.getData("marketRatePremiumId");
      var farmAction = "farm779";  // edit page
      showProcessing();
      document.location.href = farmAction + ".do?marketRatePremiumId=" + marketRatePremiumId;
    };

		Farm.codesDataTable = new YAHOO.widget.DataTable("searchresults", Farm.codesColumnDefs, Farm.codesDataSource, Farm.codesConfigs);
		Farm.codesDataTable.subscribe("rowMouseoverEvent", Farm.codesDataTable.onEventHighlightRow);
		Farm.codesDataTable.subscribe("rowMouseoutEvent", Farm.codesDataTable.onEventUnhighlightRow);
		Farm.codesDataTable.subscribe("rowClickEvent", Farm.codesDataTable.onEventSelectRow);
		Farm.codesDataTable.subscribe("rowSelectEvent", Farm.showSelectedRow);


  });
//]]>
</script>