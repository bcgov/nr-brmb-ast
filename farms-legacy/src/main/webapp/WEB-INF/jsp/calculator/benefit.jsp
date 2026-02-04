<%@ include file="/WEB-INF/jsp/common/include.jsp" %>


<u:getScenario var="scenario" pin="${form.pin}" year="${form.year}" scenarioNumber="${form.scenarioNumber}" scope="request"/>

<u:formatBoolean var="structuralChangeMade" test="${not empty form.benefit.structuralChangeMethodCodeDescription and form.benefit.structuralChangeMethodCode != 'NONE'}" trueValue="true" falseValue="false" />
<u:formatBoolean var="expenseStructuralChangeMade" test="${not empty form.benefit.expenseStructuralChangeMethodCodeDescription and form.benefit.expenseStructuralChangeMethodCode != 'NONE'}" trueValue="true" falseValue="false" />


<script type="text/javascript">
  //<![CDATA[
  function toggleAccruals() {
    var imageId = "adjustmentsExpanderImage";
    var rowIds = new Array('cropAdjustments','livestockAdjustments','inputsAdjustments','receivablesAdjustments','payablesAdjustments');
  
    toggleDisplayElementsAndImage(rowIds, imageId);
  }
  //]]>
</script>



<table>
  <tr>
      <td>
        <fieldset>
          <legend><fmt:message key="Farm.View"/></legend>
          <table>
            <tr>
              <td>
                <u:menuSelect action="farm850.do"
                    name="farmViewPicker"
                    paramName="farmView"
                    options="${form.farmViewOptions}"
                    selectedValue="${form.farmView}"
                    urlParams="pin=${form.pin}&year=${form.year}&scenarioNumber=${form.scenarioNumber}"
                    toolTip="Click here to change between views of the farm data."/>
              </td>
            </tr>
          </table>
        </fieldset>
      </td>
  </tr>
</table>



<html:form action="saveBenefit" styleId="benefitForm" method="post" onsubmit="showProcessing()">
  <html:hidden property="pin"/>
  <html:hidden property="year"/>
  <html:hidden property="scenarioNumber"/>
  <html:hidden property="scenarioRevisionCount"/>
  <html:hidden property="farmView"/>

    <table class="numeric">
      <tr>
        <th width="210">&nbsp;</th>
        <c:forEach var="year" items="${form.requiredYears}">
          <th width="100"><c:out value="${year}"/></th>
        </c:forEach>
      </tr>
      <tr>
        <th><fmt:message key="Deemed.Farming.Year?"/></th>
        <c:forEach varStatus="loop" var="year" items="${form.referenceYears}">
          <td><html-el:checkbox property="isDeemedFarmYear[${loop.index}]"/></td>
        </c:forEach>
        <td>&nbsp;</td>
      </tr>
      
      <tr>
        <th><fmt:message key="Total.Eligible.Income"/></th>
        <c:forEach var="total" items="${form.totals}">
          <td><fmt:formatNumber type="currency" value="${total.totalAllowableIncome}"/></td>
        </c:forEach>
      </tr>
      <tr>
        <th><fmt:message key="Total.Eligible.Expenses"/></th>
        <c:forEach var="total" items="${form.totals}">
          <td><fmt:formatNumber type="currency" value="${total.totalAllowableExpenses}"/></td>
        </c:forEach>
      </tr>
      <tr>
        <th><fmt:message key="Unadjusted.Production.Margin"/></th>
        <c:forEach var="total" items="${form.totals}">
          <td><fmt:formatNumber type="currency" value="${total.unadjustedProductionMargin}"/></td>
        </c:forEach>
      </tr>
      <tr>
        <th onclick="toggleAccruals()" style="cursor: hand">
          <img id="adjustmentsExpanderImage" src="yui/2.8.2r1/build/assets/skins/sam/menuitem_submenuindicator.png" alt="&gt;" /> 
          <fmt:message key="Accrual.Adjustments.Total"/>
        </th>
        <c:forEach var="total" items="${form.totals}">
          <td><fmt:formatNumber type="currency" value="${total.totalAccrualAdjs}"/></td>
        </c:forEach>
      </tr>
      <tr style="display: none" id="cropAdjustments">
        <th><fmt:message key="Crop.Inventory"/></th>
        <c:forEach var="total" items="${form.totals}">
          <td><fmt:formatNumber type="currency" value="${total.accrualAdjsCropInventory}"/></td>
        </c:forEach>
      </tr>
      <tr style="display: none" id="livestockAdjustments">
        <th><fmt:message key="Livestock.Inventory"/></th>
        <c:forEach var="total" items="${form.totals}">
          <td><fmt:formatNumber type="currency" value="${total.accrualAdjsLvstckInventory}"/></td>
        </c:forEach>
      </tr>
      <tr style="display: none" id="inputsAdjustments">
        <th><fmt:message key="Purchased.Inputs"/></th>
        <c:forEach var="total" items="${form.totals}">
          <td><fmt:formatNumber type="currency" value="${total.accrualAdjsPurchasedInputs}"/></td>
        </c:forEach>
      </tr>
      <tr style="display: none" id="receivablesAdjustments">
        <th><fmt:message key="Receivables"/></th>
        <c:forEach var="total" items="${form.totals}">
          <td><fmt:formatNumber type="currency" value="${total.accrualAdjsReceivables}"/></td>
        </c:forEach>
      </tr>
      <tr style="display: none" id="payablesAdjustments">
        <th><fmt:message key="Payables"/></th>
        <c:forEach var="total" items="${form.totals}">
          <td><fmt:formatNumber type="currency" value="${total.accrualAdjsPayables}"/></td>
        </c:forEach>
      </tr>
      <tr>
        <th><fmt:message key="Production.Margin.with.Accrual.Adjustments"/>
        	<a href="#" onclick="loadWindow('<html:rewrite action="farm855" name="form" property="scenarioParams"/>&accrAdjType=PRODUCTION', '500', '200');"><img alt="info" src="images/bcgov_info.png" /></a>
        </th>
        <c:forEach var="total" items="${form.totals}">
          <td><fmt:formatNumber type="currency" value="${total.productionMargAccrAdjs}"/></td>
        </c:forEach>
      </tr>
      <c:if test="${form.growingForward2013}">
        <tr>
          <th><fmt:message key="Expenses.with.Accrual.Adjustments"/>
          	<a href="#" onclick="loadWindow('<html:rewrite action="farm855" name="form" property="scenarioParams"/>&accrAdjType=EXPENSES', '500', '200');"><img alt="info" src="images/bcgov_info.png" /></a>
          </th>
          <c:forEach var="total" items="${form.totals}">
            <td><fmt:formatNumber type="currency" value="${total.expenseAccrualAdjs}"/></td>
          </c:forEach>
        </tr>
      </c:if>
      
      
<c:if test="${form.farmView == 'WHOLE'}">
  <c:if test="${form.benefit.structuralChangeMethodCode == 'RATIO' and form.scMaterialOverride == 'YES'}">
      <tr>
        <th><fmt:message key="Farm.Size.Ratio"/></th>
        <c:forEach varStatus="loop" var="year" items="${form.referenceYears}">
          <td><fmt:formatNumber type="percent" value="${form.totals[loop.index].farmSizeRatio}"/></td>
        </c:forEach>
        <td>&nbsp;</td>
      </tr>
  </c:if>
  <c:if test="${form.benefit.expenseStructuralChangeMethodCode == 'RATIO' and form.scMaterialOverride == 'YES' and form.growingForward2013}">
      <tr>
        <th><fmt:message key="Expense.Farm.Size.Ratio"/></th>
        <c:forEach varStatus="loop" var="year" items="${form.referenceYears}">
          <td><fmt:formatNumber type="percent" value="${form.totals[loop.index].expenseFarmSizeRatio}"/></td>
        </c:forEach>
        <td>&nbsp;</td>
      </tr>
  </c:if>
      <tr>
        <c:choose>
          <c:when test="${structuralChangeMade and form.scMaterialOverride == 'YES'}">
            <th>
              <c:out value="${form.benefit.structuralChangeMethodCodeDescription}" /> <fmt:message key="Method.Structural.Adjustment"/>
              <c:forEach var="total" items="${form.totals}">
                <td><fmt:formatNumber type="currency" value="${total.structuralChangeAdjs}"/></td>
              </c:forEach>
            </th>
          </c:when>
          <c:otherwise>
            <th>
              <fmt:message key="No.Structural.Adjustment"/>
            </th>
            <c:forEach var="total" items="${form.totals}">
              <td><fmt:formatNumber type="currency" value="0"/></td>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </tr>
      <c:if test="${form.growingForward2013}">
        <tr>
          <c:choose>
            <c:when test="${expenseStructuralChangeMade and form.scMaterialOverride == 'YES'}">
              <th>
                <c:out value="${form.benefit.expenseStructuralChangeMethodCodeDescription}" /> <fmt:message key="Method.Expense.Structural.Adjustment"/>
                <c:forEach var="total" items="${form.totals}">
                  <td><fmt:formatNumber type="currency" value="${total.expenseStructuralChangeAdjs}"/></td>
                </c:forEach>
              </th>
            </c:when>
            <c:otherwise>
               <th>
                 <fmt:message key="No.Expense.Structural.Adjustment"/>
                <c:forEach var="total" items="${form.totals}">
                  <td><fmt:formatNumber type="currency" value="0"/></td>
                </c:forEach>
               </th>
            </c:otherwise>
          </c:choose>
        </tr>
      </c:if>
      <c:if test="${structuralChangeMade or expenseStructuralChangeMade}">
        <tr>
          <th><fmt:message key="Structural.Change.Material?"/></th>
          <td colspan="6" style="text-align: center" >
             <html:radio property="scMaterialOverride" value="YES" /> Yes &nbsp;&nbsp;
             <html:radio property="scMaterialOverride" value="NO" /> No
          </td>
        </tr>
      </c:if>
 
      <tr>
        <th><fmt:message key="Production.Margin.after.Structural.Change"/></th>
        <c:forEach var="total" items="${form.totals}">
          <td><fmt:formatNumber type="currency" value="${total.productionMargAftStrChangs}"/></td>
        </c:forEach>
      </tr>
      <c:if test="${form.growingForward2013}">
	      <tr>
	        <th><fmt:message key="Expenses.after.Structural.Change"/></th>
	        <c:forEach var="total" items="${form.totals}">
	          <td><fmt:formatNumber type="currency" value="${total.expensesAfterStructuralChange}"/></td>
	        </c:forEach>
	      </tr>
      </c:if>
      <tr>
        <th><fmt:message key="Figures.Used.in.Calculation"/></th>
        <c:forEach var="used" items="${form.usedInCalc}">
          <td><u:formatBoolean test="${used}" trueValue="Yes" falseValue="No" /></td>
        </c:forEach>
        <td>&nbsp;</td>
      </tr>
</c:if>

    </table>
    
<c:if test="${form.farmView == 'WHOLE'}">

  <c:choose>
    <c:when test="${form.growingForward2024}">
      <tiles:insert page="/WEB-INF/jsp/calculator/benefit/calcSummaries2024.jsp" />
    </c:when>
    <c:when test="${form.growingForward2023}">
      <tiles:insert page="/WEB-INF/jsp/calculator/benefit/calcSummaries2023.jsp" />
    </c:when>
    <c:when test="${form.growingForward2021}">
      <tiles:insert page="/WEB-INF/jsp/calculator/benefit/calcSummaries2021.jsp" />
    </c:when>
    <c:when test="${form.show2020ViewWithoutRML}">
      <tiles:insert page="/WEB-INF/jsp/calculator/benefit/calcSummaries2020.jsp" />
    </c:when>
    <c:when test="${form.growingForward2019 or form.growingForward2020}">
      <tiles:insert page="/WEB-INF/jsp/calculator/benefit/calcSummaries2019.jsp" />
    </c:when>
    <c:when test="${form.growingForward2018}">
      <tiles:insert page="/WEB-INF/jsp/calculator/benefit/calcSummaries2018.jsp" />
    </c:when>
    <c:otherwise>
      <tiles:insert page="/WEB-INF/jsp/calculator/benefit/calcSummaries2017prev.jsp" />
    </c:otherwise>
  </c:choose>
  
  <c:if test="${scenario.inCombinedFarm}">
    <c:choose>
      <c:when test="${form.growingForward2021}">
        <tiles:insert page="/WEB-INF/jsp/calculator/benefit/combinedFarmSummary2021.jsp" />
      </c:when>
      <c:otherwise>
        <tiles:insert page="/WEB-INF/jsp/calculator/benefit/combinedFarmSummary2020prev.jsp" />
      </c:otherwise>
    </c:choose>
  </c:if>

  <c:if test="${!form.readOnly}">
    <div style="text-align:right;padding-right:20px;padding-top:10px;">
     <p><a id="saveButton" href="#"><fmt:message key="Save"/></a></p>
    </div>
  </c:if>

</c:if>

</html:form>


<c:if test="${form.farmView == 'WHOLE' and !form.readOnly}">
  <c:if test="${!form.readOnly}">
    <script type="text/javascript">
      //<![CDATA[
      new YAHOO.widget.Button("saveButton");
      function submitFunc() { submitForm(document.getElementById('benefitForm')); }
      YAHOO.util.Event.addListener(document.getElementById("saveButton"), "click", submitFunc);
   
      registerFormForDirtyCheck(document.getElementById("benefitForm"));
      //]]>
   </script>
  </c:if>
</c:if>

