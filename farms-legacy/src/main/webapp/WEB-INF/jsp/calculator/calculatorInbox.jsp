<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/common/datatable.jsp" %>

<html:form action="farm900" styleId="inboxSearchForm" method="post" onsubmit="showProcessing()">
    <html:hidden property="inboxYear" styleId="inboxYear"/>
    <html:hidden property="inboxSearchType" styleId="inboxSearchType"/>

  <table width="100%" cellpadding="20" cellspacing="20">
    <tr>
      <td valign="top">
        <div class="yui-navset">
          <ul class="yui-nav">
            <li class="selected"><a href="<html:rewrite action="farm900"/>"><em><fmt:message key="Agristability.Inbox"/></em></a></li>
          </ul>
          <div class="yui-content">
            <table>
              <tr>
                <td style="width:100px;">
                  <fieldset>
                  <legend><fmt:message key="Program.Year"/></legend>
                    <u:menuSelect action="farm900.do"
                         name="inboxYearPicker"
                         paramName="inboxYear"
                         additionalFieldIds="inboxSearchType,inProgressCB,verifiedCB,closedCB"
                         options="${form.programYearSelectOptions}"
                         selectedValue="${form.inboxYear}"
                         toolTip="Click here to open a different Program Year."/>
                  </fieldset>
                </td>
                <td style="width:170px">
                  <fieldset>
                  <legend><fmt:message key="Checked.Out.By"/></legend>
                    <u:menuSelect action="farm900.do"
                         name="inboxSearchTypePicker"
                         paramName="inboxSearchType"
                         additionalFieldIds="inboxYear,inProgressCB,verifiedCB,closedCB"
                         options="${form.assignedToSelectOptions}"
                         selectedValue="${form.inboxSearchType}"
                         toolTip="Click here to change between PINs checked out by you, PINs checked out by anyone, and PINs ready for processing."/>
                  </fieldset>
                </td>
                <td>
                  <fieldset>
                  <legend><fmt:message key="Processing.State"/></legend>
                    <table>
                      <tr>
                        <td>
                          <html-el:checkbox property="inProgressCB" styleId="inProgressCB" disabled="${form.stateFiltersDiabled}"/>
                          <fmt:message key="In.Progress"/>
                        </td>
                        <td>
                          <html-el:checkbox property="verifiedCB" styleId="verifiedCB" disabled="${form.stateFiltersDiabled}"/>
                          <fmt:message key="Verified"/>
                        </td>
                        <td>
                          <html-el:checkbox property="closedCB" styleId="closedCB" disabled="${form.stateFiltersDiabled}"/>
                          <fmt:message key="Closed"/>
                        </td>
                      </tr>
                    </table>
                  </fieldset>
                </td>
              </tr>
            </table>

            <div class="searchresults">
              <div id="myAccounts"></div>
           </div>

           </div>
        </div>
      </td>
    </tr>
  </table>
  <p></p>

</html:form>


<script type="text/javascript">
//<![CDATA[

  if(!Array.indexOf){
    Array.prototype.indexOf = function(obj){
     for(var i=0; i<this.length; i++){
      if(this[i]==obj){
       return i;
      }
     }
     return -1;
    };
  }

   var Farm = YAHOO.namespace('farm');
     Farm.myAccounts = {
          "recordsReturned": 2,
          "totalRecords": 2,
          "startIndex":0,
          "sort":name,
          "dir":"asc",
          "pageSize": 10,
          "records":[

<c:forEach var="item" items="${form.inboxItems}">
{name:"<c:out value="${item.name}"/>",
 pin:"<c:out value="${item.pin}"/>",
 stateCode:"<c:out value="${item.scenarioStateCode}"/>",
 state:"<c:out value="${item.scenarioStateCodeDescription}"/>",
 scenarioDate:
  <c:choose>
    <c:when test="${!empty item.lastChangedDate}">
      new Date(<c:out value="${item.lastChangedDate.time}"/>)
    </c:when>
    <c:otherwise>null</c:otherwise>
  </c:choose>,
 totalBenefit:
  <c:choose>
    <c:when test="${!empty item.totalBenefit}">
      <c:out value="${item.totalBenefit}"/>
    </c:when>
    <c:otherwise>null</c:otherwise>
  </c:choose>,
 assignedTo:"<c:out value="${item.assignedToUserIdEscapedForString}"/>",
 receivedDate:
  <c:choose>
    <c:when test="${!empty item.receivedDate}">
      new Date(<c:out value="${item.receivedDate.time}"/>)
    </c:when>
    <c:otherwise>null</c:otherwise>
  </c:choose>
},
</c:forEach>
          ]
       };


       YAHOO.util.Event.onDOMReady(function() {
         Farm.myAcountsColumnDefs = [
            {key:"name", label:"<fmt:message key="Name"/>", sortable:true},
            {key:"pin", label:"<fmt:message key="PIN"/>", sortable:true, className:"tdRight"},
            {key:"state", label:"<fmt:message key="State"/>", sortable:true},
            {key:"scenarioDate", formatter:YAHOO.widget.DataTable.formatDate, label:"<fmt:message key="Last.Changed"/>", sortable:true},
            {key:"totalBenefit", formatter:YAHOO.widget.DataTable.formatCurrency, label:"<fmt:message key="Total.Benefit"/>", sortable:true, className:"tdRight"},
            {key:"assignedTo", label:"<fmt:message key="Checked.Out.By"/>", sortable:true},
            {key:"receivedDate", formatter:YAHOO.widget.DataTable.formatDate, label:"<fmt:message key="Received.Date"/>", sortable:true}
        ];

        Farm.myAccountsDataSource = new YAHOO.util.DataSource(Farm.myAccounts,{
        doBeforeCallback : function (req,raw,res,cb) { 
        // This is the filter function 

        // Get State Filter values
        var stateFilters = new Array();
        if(YAHOO.util.Dom.get('inProgressCB').checked==true) stateFilters.push("IP"); 
        if(YAHOO.util.Dom.get('closedCB').checked==true) stateFilters.push("CLO"); 
        if(YAHOO.util.Dom.get('verifiedCB').checked==true) stateFilters.push("COMP"); 

              var data     = res.results || [], 
                  filtered = [], 
                  i,l; 
   
                  for (i = 0, l = data.length; i < l; ++i) { 
              <c:if test="${!form.stateFiltersDiabled}">
            if (stateFilters.indexOf(data[i].stateCode) != -1) {
              </c:if>
              filtered.push(data[i]); 
              <c:if test="${!form.stateFiltersDiabled}">
            }
              </c:if>
                  } 
                  res.results = filtered; 

              return res; 
          }
    });
        Farm.myAccountsDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
        Farm.myAccountsDataSource.responseSchema = {
            resultsList: "records",
            fields: ["name","pin","state", "stateCode","scenarioDate","totalBenefit","assignedTo","receivedDate"]
        };
    

        Farm.myAccountsConfigs = {
            paginator: new YAHOO.widget.Paginator({ rowsPerPage: 20 })
        };

        function showSelectedRow(oArgs) {
          var oRecord = oArgs.record;
          var pin = oRecord.getData("pin");
          var inboxYearField = document.getElementById("inboxYear");
          var inboxYear = inboxYearField.value;
          var inboxSearchType = document.getElementById("inboxSearchType").value;

          var inProgressCBField = document.getElementById("inProgressCB");
          var verifiedCBField = document.getElementById("verifiedCB");
          var closedCBField = document.getElementById("closedCB");

          var url = "<html:rewrite action="farm800"/>?pin=" + pin + "&year=" + inboxYear + "&refresh=true"
             + "&inboxYear=" + inboxYear + "&inboxSearchType=" + inboxSearchType;

          if(inProgressCBField.checked) {
            url = url + "&inProgressCB=" + inProgressCBField.value;
          }
          if(verifiedCBField.checked) {
            url = url + "&verifiedCB=" + verifiedCBField.value;
          }
          if(closedCBField.checked) {
            url = url + "&closedCB=" + closedCBField.value;
          }

          showProcessing();
          document.location.href = url;
        };

        Farm.myAccountsDataTable = new YAHOO.widget.DataTable("myAccounts", Farm.myAcountsColumnDefs, Farm.myAccountsDataSource, Farm.myAccountsConfigs);
        Farm.myAccountsDataTable.subscribe("rowMouseoverEvent", Farm.myAccountsDataTable.onEventHighlightRow);
        Farm.myAccountsDataTable.subscribe("rowMouseoutEvent", Farm.myAccountsDataTable.onEventUnhighlightRow);
        Farm.myAccountsDataTable.subscribe("rowClickEvent", Farm.myAccountsDataTable.onEventSelectRow);
        Farm.myAccountsDataTable.subscribe("rowSelectEvent", showSelectedRow);

    Farm.updateFilter = function ()  { 
          // Reset sort 
          var state = YAHOO.farm.myAccountsDataTable.getState(); 
              state.sortedBy = {key:'scenarioDate', dir:YAHOO.widget.DataTable.CLASS_ASC}; 
   
          // Get filtered data 
          Farm.myAccountsDataSource.sendRequest(null,{ 
              success : Farm.myAccountsDataTable.onDataReturnInitializeTable, 
              failure : Farm.myAccountsDataTable.onDataReturnInitializeTable, 
              scope   : Farm.myAccountsDataTable, 
              argument: state 
          });
    };

    Farm.updateFilter();  
      YAHOO.util.Event.on('closedCB','click',Farm.updateFilter);      
      YAHOO.util.Event.on('inProgressCB','click',Farm.updateFilter);      
      YAHOO.util.Event.on('verifiedCB','click',Farm.updateFilter);      
      });

  //]]>
</script>              
