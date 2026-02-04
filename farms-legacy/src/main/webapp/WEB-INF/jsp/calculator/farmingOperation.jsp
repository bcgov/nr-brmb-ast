<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

	<logic:messagesPresent name="infoMessages">
	  <div class="messages">
	    <ul>
	      <html:messages id="message" name="infoMessages">
	        <li><c:out value="${message}"/></li>
	      </html:messages>
	    </ul>
	  </div>
	</logic:messagesPresent>

  <html:form action="saveFarmingOperation" styleId="farmingOperationForm" method="post" onsubmit="showProcessing()">
    <html:hidden property="pin"/>
    <html:hidden property="year"/>
    <html:hidden property="scenarioNumber"/>
    <html:hidden property="operationNumber"/>
    <html:hidden property="schedule"/>
    <html:hidden property="scenarioRevisionCount"/>
    <html:hidden property="operationRevisionCount"/>
    <html:hidden property="new"/>

    <fieldset>
      <legend><fmt:message key="Operation.Detail"/></legend>
      <table class="formInput" style="width:100%">
        <tr>
          <th><fmt:message key="Operation"/>:</th>
          <td>
            <c:choose>
              <c:when test="${form.new}">
                <html:hidden property="schedule"/>
                <c:out value="${form.schedule}"/>
              </c:when>
              <c:when test="${empty scenario.farmingYear.farmingOperations}">
                <fmt:message key="no.operations.message"/>
              </c:when>
              <c:otherwise>
                <u:menuSelect action="farm825.do"
                    name="schedulePicker"
                    paramName="schedule"
                    options="${form.scheduleOptions}"
                    selectedValue="${form.schedule}"
                    urlParams="pin=${form.pin}&year=${form.year}&scenarioNumber=${form.scenarioNumber}"
                    toolTip="Click here to open a different Operation Schedule."/>
              </c:otherwise>
            </c:choose>
            <c:if test="${!form.new and form.locallyGenerated}">
              <fmt:message key="Locally.Generated"/>
            </c:if>
          </td>
          <th><fmt:message key="Partnership.PIN"/>:</th>
          <td><html:text property="partnershipPin" size="30" maxlength="9"/></td>
        </tr>
        <tr>
          <th><fmt:message key="Accounting.Code"/>:</th>
          <td>
            <html:select property="agristabAccountingCode">
              <html:option value=""/>
              <html:optionsCollection name="server.list.federal.accounting"/>
            </html:select>
          </td>
          <th><fmt:message key="Partnership.Name"/>:</th>
          <td><html:text property="partnershipName" size="30" maxlength="42"/></td>
        </tr>
        <tr>
          <th><fmt:message key="Fiscal.Start"/>:</th>
          <td>
            <div style="float:left;width:90%">
              <html:text property="fiscalYearStart" styleId="fiscalYearStart" size="30" maxlength="10"/>
            </div>
            <u:datePicker fieldId="fiscalYearStart"/>
          </td>
          <th><fmt:message key="Partnership.Percent"/>:</th>
          <td><html:text property="partnershipPercent" size="30" maxlength="8"/></td>
        </tr>
        <tr>
          <th><fmt:message key="Fiscal.End"/>:</th>
          <td>
            <div style="float:left;width:90%">
              <html:text property="fiscalYearEnd" styleId="fiscalYearEnd" size="30" maxlength="10"/>
            </div>
            <u:datePicker fieldId="fiscalYearEnd"/>
          </td>
          <th><fmt:message key="Crop.Disaster"/>:</th>
          <td><html:checkbox property="cropDisaster"/></td>
        </tr>
        <tr>
          <th><fmt:message key="Crop.Share"/>:</th>
          <td><html:checkbox property="cropShare"/></td>
          <th><fmt:message key="Livestock.Disaster"/>:</th>
          <td><html:checkbox property="livestockDisaster"/></td>
        </tr>
        <tr>
          <th><fmt:message key="Feeder.Member"/>:</th>
          <td><html:checkbox property="feederMember"/></td>
          <th><fmt:message key="Landlord"/>:</th>
          <td><html:checkbox property="landlord"/></td>
        </tr>
        <tr>
       	  <th><fmt:message key="Tip.Report"/>:</th>
       	  <td>
              <c:choose>
                <c:when test="${form.tipReportGenerated}">
				  <a id="viewTipReportButton" href="javascript:viewTipReport();"><fmt:message key="View.Tip.Report"/></a>
                </c:when>
                <c:otherwise>
                
                </c:otherwise>
              </c:choose>
       	  </td>
        </tr>
      </table>
    </fieldset>

    <fieldset>
      <legend><fmt:message key="Financial.Overview.as.reported"/></legend>
      <table class="formInput" style="width:100%">
        <tr>
          <th><fmt:message key="Expenses"/>:</th>
          <td><html:text property="expenses" size="30" maxlength="15"/></td>
          <th><fmt:message key="Gross.Income"/>:</th>
          <td><html:text property="grossIncome" size="30" maxlength="15"/></td>
        </tr>
        <tr>
          <th><fmt:message key="Business.Use.Home.Expense"/>:</th>
          <td><html:text property="businessUseHomeExpense" size="30" maxlength="15"/></td>
          <th><fmt:message key="Net.Farm.Income"/>:</th>
          <td><html:text property="netFarmIncome" size="30" maxlength="15"/></td>
        </tr>
        <tr>
          <th><fmt:message key="Other.Deductions"/>:</th>
          <td><html:text property="otherDeductions" size="30" maxlength="15"/></td>
          <th><fmt:message key="Net.Income.Before.Adj."/>:</th>
          <td><html:text property="netIncomeBeforeAdj" size="30" maxlength="15"/></td>
        </tr>
        <tr>
          <th><fmt:message key="Inventory.Adjustments"/>:</th>
          <td><html:text property="inventoryAdjustments" size="30" maxlength="15"/></td>
          <th><fmt:message key="Net.Income.After.Adj."/>:</th>
          <td><html:text property="netIncomeAfterAdj" size="30" maxlength="15"/></td>
        </tr>
    </table>
    </fieldset>

    <fieldset>
      <legend><fmt:message key="Production.Insurance"/></legend>
      <table class="formInput" style="width:100%">
        <tr>
          <th><fmt:message key="Policy.Number"/>:</th>
          <td><html:text property="productionInsuranceNumber1" size="30" maxlength="12"/></td>
          <th><fmt:message key="Policy.Number"/>:</th>
          <td><html:text property="productionInsuranceNumber2" size="30" maxlength="12"/></td>
        </tr>
        <tr>
          <th><fmt:message key="Policy.Number"/>:</th>
          <td><html:text property="productionInsuranceNumber3" size="30" maxlength="12"/></td>
          <th><fmt:message key="Policy.Number"/>:</th>
          <td><html:text property="productionInsuranceNumber4" size="30" maxlength="12"/></td>
        </tr>
      </table>
    </fieldset>

    <c:if test="${ ! form.readOnly }">
      <div style="text-align:right">
        <c:if test="${ !form.new }">
           <w:ifUserCanPerformAction action="newOperation">
            <button type="button" id="newButton"><fmt:message key="New"/></button>
          </w:ifUserCanPerformAction>
        </c:if>
        <a id="saveButton" href="#"><fmt:message key="Save"/></a>
        <c:if test="${ !form.new }">
          <w:ifUserCanPerformAction action="newOperation">
            <button type="button" id="deleteButton"><fmt:message key="Delete"/></button>
          </w:ifUserCanPerformAction>
        </c:if>
        <c:if test="${ form.new }">
        <script>
            function cancel() {
                document.location.href = '<html:rewrite action="farm825"/>?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>';
            }
            var newButton = new YAHOO.widget.Button("newButton", {onclick: {fn: newOperation}});
          </script>
          <u:yuiButton buttonId="cancelButton" buttonLabel="Cancel" action="farm825"
             function="cancel" />
        </c:if>
      </div>
    </c:if>

  </html:form>

  <c:if test="${ ! form.readOnly and !form.new }">
    <c:choose>
      <c:when test="${ !form.pyvHasVerifiedScenario }">
        <script>
            function newOperation() {
              var msg = "<fmt:message key="new.operation.warning"/>";
              if(confirm(msg)) {
                document.location.href = '<html:rewrite action="newFarmingOperation"/>?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>';
              }
            }
            var newButton = new YAHOO.widget.Button("newButton", {onclick: {fn: newOperation}});
          </script>
      </c:when>
      <c:otherwise>
        <script>
            var newButton = new YAHOO.widget.Button("newButton", {disabled: true});
            var newButtonTT =
              new YAHOO.widget.Tooltip("newButtonTT",
                    { context:"newButton", text:"<fmt:message key="cannot.create.new.operation"/>", autodismissdelay: 7500, effect:{effect:YAHOO.widget.ContainerEffect.FADE,duration:0.5} });
          </script>
      </c:otherwise>
    </c:choose>

    <c:choose>
      <c:when test="${ !form.pyvHasVerifiedScenario and form.locallyGenerated }">
        <script>
            var deleteButton = new YAHOO.widget.Button("deleteButton", {onclick: {fn: deleteOperation}});
            function deleteOperation() {
              var msg = "<fmt:message key="delete.operation.warning"/>";
              if(confirm(msg)) {
                var form = document.getElementById('farmingOperationForm');
                form.action = '<html:rewrite action="deleteFarmingOperation"/>';
                form.submit();
              }
            }
          </script>
      </c:when>
      <c:otherwise>
        <c:choose>
          <c:when test="${ !form.locallyGenerated }">
            <c:set var="deleteTTText"><fmt:message key="cannot.delete.operation.not.locally.generated"/></c:set>
          </c:when>
          <c:when test="${ form.pyvHasVerifiedScenario }">
            <c:set var="deleteTTText"><fmt:message key="cannot.delete.operation.verified.scenario"/></c:set>
          </c:when>
        </c:choose>
        <script>
            var deleteButton = new YAHOO.widget.Button("deleteButton", {disabled: true});
            var deleteButtonTT =
              new YAHOO.widget.Tooltip("deleteButtonTT",
                    { context:"deleteButton", text:"<c:out value="${deleteTTText}"/>",
                      autodismissdelay: 7500, effect:{effect:YAHOO.widget.ContainerEffect.FADE,duration:0.5} });
          </script>
      </c:otherwise>
    </c:choose>
  </c:if>

  <script>
    <c:if test="${form.tipReportGenerated}">
	  new YAHOO.widget.Button("viewTipReportButton");
	
	  function viewTipReport() {
	    openNewWindow('<html:rewrite action="viewTipReport"/>?tipReportDocId=<c:out value="${form.tipReportDocId}" escapeXml="false"/>');
	  }
    </c:if>
  
    new YAHOO.widget.Button("saveButton");
    function submitFunc() { submitForm(document.getElementById('farmingOperationForm')); }
    YAHOO.util.Event.addListener(document.getElementById("saveButton"), "click", submitFunc);

    <c:if test="${ ! form.readOnly }">
      registerFormForDirtyCheck(document.getElementById("farmingOperationForm"));
    </c:if>
    
    function cancel() {
      document.location.href = '<html:rewrite action="farm825"/>?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>';
    }
  </script>
