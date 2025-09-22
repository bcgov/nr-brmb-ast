<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getScenario var="scenario" pin="${form.pin}" year="${form.year}" scenarioNumber="${form.scenarioNumber}" scope="request"/>


<script type="text/javascript">
  //<![CDATA[

  var growingForward2013 = false;
  
  function showMismatchWarning(setVisible) {
    var divs = document.getElementsByTagName('div');
    for (var ii = 0; ii < divs.length; ii++) {
      if (divs[ii].className == 'warnings') {
        if (setVisible) {
          divs[ii].style.display = '';
        } else {
          divs[ii].style.display = 'none';
        }
        break;
      }
    }
  }
  
  function requiresRecalculation() {
    var recalcNeeded = false;
    var inputs = document.getElementsByTagName("input");
    
    // check which structural change type to be used for financial view warning icon
    var scRadioName = 'structuralChangeMethod';
    if (growingForward2013) {
      var scTypeRadioGroup = document.getElementsByName('scTypeViewRadio');
      for (var jj = 0; jj < scTypeRadioGroup.length; jj++) {
        if(scTypeRadioGroup[jj].checked && scTypeRadioGroup[jj].value == 'expenseSCT') {
          scRadioName = 'expenseStructuralChangeMethod';
        }
      }
    }
    
    for(var ii = 0; ii < inputs.length; ii++) {
      if(inputs[ii].type == 'checkbox') {
        if(inputs[ii].checked != inputs[ii].defaultChecked) {
          recalcNeeded = true;
          inputs[ii].parentNode.style.backgroundColor = "#CC3333";
        } else {
          inputs[ii].parentNode.style.backgroundColor = "#DFDFDF";
        }
      } else if(inputs[ii].name == scRadioName) {
        if(inputs[ii].checked != inputs[ii].defaultChecked) {
          recalcNeeded = true;
        }
      }
    }
    
    return recalcNeeded;
  }


  function checkRecalcWarning() {
    if(requiresRecalculation()) {
      document.getElementById('recalcWarning').style.display = '';
    } else {
      document.getElementById('recalcWarning').style.display = 'none';
    }
  }
  

  function displayFinancialView(radioButton) {
    var tableId = radioButton.value;
    document.getElementById('capacityTable').style.display = 'none';
    document.getElementById('bpuTable').style.display = 'none';
    document.getElementById('varianceTable').style.display = 'none';
    document.getElementById('adjustmentsTable').style.display = 'none';
    document.getElementById('refYearProdValueTable').style.display = 'none';
    document.getElementById('progYearProdValueTable').style.display = 'none';
    if (growingForward2013) {
      document.getElementById('bpuTable_expense').style.display = 'none';
      document.getElementById('adjustmentsTable_expense').style.display = 'none';
      document.getElementById('refYearProdValueTable_expense').style.display = 'none';
      document.getElementById('progYearProdValueTable_expense').style.display = 'none';
      
      var sameTableForMarginAndExpense = (tableId == 'capacityTable' || tableId == 'varianceTable');
      if(!sameTableForMarginAndExpense) {
        var scTypeViewRadio = document.getElementsByName('scTypeViewRadio');
        for (var jj = 0; jj < scTypeViewRadio.length; jj++) {
          if (scTypeViewRadio[jj].checked && scTypeViewRadio[jj].value == 'expenseSCT') {
            tableId = tableId + '_expense';
          }
        }
      }
    }
    
    document.getElementById(tableId).style.display = '';
  }
  

  function defaultsDisplay() {
    document.getElementById('totalsSection').style.display = '';
    document.getElementById('knownRatioTotalsRow').style.display = 'none';
    document.getElementById('unknownRatioTotalsRow').style.display = 'none';
    document.getElementById('knownTotalsRow').style.display = 'none';
    document.getElementById('unknownTotalsRow').style.display = 'none';
    if (growingForward2013) {
      document.getElementById('knownExpenseRatioTotalsRow').style.display = 'none';
      document.getElementById('unknownExpenseRatioTotalsRow').style.display = 'none';
      document.getElementById('knownExpenseTotalsRow').style.display = 'none';
      document.getElementById('unknownExpenseTotalsRow').style.display = 'none';
    }
  }
  
  function displayRatioTotalsRow(isOriginalMethod, isOriginalExpenseMethod) {
    if (isOriginalMethod || isOriginalExpenseMethod) {
      document.getElementById('knownRatioTotalsRow').style.display = '';
    }
    else {
     document.getElementById('unknownRatioTotalsRow').style.display = '';
    }
  }
  
  function displayTotalRow(rowIdSuffix, isOriginalMethod) {
    var rowId;
    if (isOriginalMethod) {
      rowId = 'known' + rowIdSuffix;
    }
    else {
      rowId = 'unknown' + rowIdSuffix;
    }
      document.getElementById(rowId).style.display = '';
  }
  
  
  function displayScMethod(radioButton) {

    var marginMethodCode = $('input:radio[name=structuralChangeMethod]:checked').val();
    var originalMarginMethodCode = '<c:out value="${form.structuralChangeMethod}"/>';
    var expenseMethodCode = null;
    var originalExpenseMethodCode = '<c:out value="${form.expenseStructuralChangeMethod}"/>';

    var isOriginalMarginMethod = (marginMethodCode == originalMarginMethodCode);
    var isOriginalExpenseMethod = true;

    var selectedStructuralChangeType = 'marginSCT';

    if (growingForward2013) {
      selectedStructuralChangeType = $('input:radio[name=scTypeViewRadio]:checked').val();
      expenseMethodCode = $('input:radio[name=expenseStructuralChangeMethod]:checked').val();
      isOriginalExpenseMethod = (expenseMethodCode == originalExpenseMethodCode);
    }
  
    //
    // show or hide warning sign for mismatching structural change methods
    //
    if (growingForward2013) {
      if (marginMethodCode == expenseMethodCode) {
        showMismatchWarning(false);
      } else {
        showMismatchWarning(true);
      }
    }
    else {
      showMismatchWarning(false);
    }
  
    defaultsDisplay();
  
    //
    // show/hide fields based on the method
    //
    if(marginMethodCode == 'ADD') {
        displayTotalRow('TotalsRow', isOriginalMarginMethod);
    } else if(marginMethodCode == 'RATIO') {
      displayTotalRow('RatioTotalsRow', isOriginalMarginMethod);
      displayTotalRow('TotalsRow', isOriginalMarginMethod);
    }
    
    var displayExpenseTotals = false;
    if (growingForward2013) {
      if (expenseMethodCode == 'ADD') {
        displayTotalRow('ExpenseTotalsRow', isOriginalExpenseMethod);
        displayExpenseTotals = true;

      } else if (expenseMethodCode == 'RATIO') {
        displayTotalRow('ExpenseRatioTotalsRow', isOriginalExpenseMethod);
        displayTotalRow('ExpenseTotalsRow', isOriginalExpenseMethod);
        displayExpenseTotals = true;
      }
    }
    
    if(selectedStructuralChangeType == 'marginSCT') {
      displayFinancialViewOptions(marginMethodCode);
    } else {
      displayFinancialViewOptions(expenseMethodCode);
    }
    
    var displayMarginTotals = marginMethodCode != 'NONE';
    if(!displayMarginTotals && !displayExpenseTotals) {
      document.getElementById('totalsSection').style.display = 'none';
    }

    
    financialViewCheck();
  }
  
  
  function displayFinancialViewOptions(smcCode) {
    if(smcCode == 'ADD') {
      document.getElementById('varFV').style.display = '';
      document.getElementById('saFV').style.display = '';
      document.getElementById('rypyFV').style.display = 'none';
      document.getElementById('pypyFV').style.display = 'none';
    } else if(smcCode == 'RATIO') {
      document.getElementById('varFV').style.display = 'none';
      document.getElementById('saFV').style.display = 'none';
      document.getElementById('rypyFV').style.display = '';
      document.getElementById('pypyFV').style.display = '';
    } else {
      document.getElementById('varFV').style.display = 'none';
      document.getElementById('saFV').style.display = 'none';
      document.getElementById('rypyFV').style.display = 'none';
      document.getElementById('pypyFV').style.display = 'none';
    }
  }
  
  
  function financialViewCheck() {
    checkRecalcWarning();

    //
    // Set the Financial View if it was on an option that doesn't
    // apply to this method
    //
    var fvRadio = document.getElementsByName('financialViewRadio');
    
    for(var ii = 0; ii < fvRadio.length; ii++) {
      if(fvRadio[ii].checked && fvRadio[ii].parentNode.style.display == 'none') {
        
        fvRadio[0].checked = true;
        displayFinancialView(fvRadio[0]);
        return true;
      }
    }
  }
  
  function switchScType(radioButton) {
    var scCode = null;
    var scRadio;
    if(radioButton.value == 'marginSCT') {
      scRadio = document.getElementsByName('structuralChangeMethod');
    } else {
      scRadio = document.getElementsByName('expenseStructuralChangeMethod');
    }
    
    for (var ii = 0; ii < scRadio.length; ii++) {
      if (scRadio[ii].checked) {
        scCode = scRadio[ii].value;
      }
    }

    if(scCode == 'ADD') {
      document.getElementById('varFV').style.display = '';
      document.getElementById('saFV').style.display = '';
      document.getElementById('rypyFV').style.display = 'none';
      document.getElementById('pypyFV').style.display = 'none';
    }
    else if(scCode == 'RATIO') {
      document.getElementById('varFV').style.display = 'none';
      document.getElementById('saFV').style.display = 'none';
      document.getElementById('rypyFV').style.display = '';
      document.getElementById('pypyFV').style.display = '';
    }
    else {
      document.getElementById('varFV').style.display = 'none';
      document.getElementById('saFV').style.display = 'none';
      document.getElementById('rypyFV').style.display = 'none';
      document.getElementById('pypyFV').style.display = 'none';
    }
    
    var updated = financialViewCheck();
    
    // if financialViewCheck did not update financial view data tables, update them here
    if (!updated) {
      var fvRadio = document.getElementsByName('financialViewRadio');
      for(var jj = 0; jj < fvRadio.length; jj++) {
        if(fvRadio[jj].checked) {
          displayFinancialView(fvRadio[jj]);
          break;
        }
      }
    }
  }
  
  function onPageLoad() {
    growingForward2013 = ("<c:out value="${form.growingForward2013}"/>" == "true");
  
    var radioButton = document.getElementsByName("structuralChangeMethod");  
    for(var ii = 0; ii < radioButton.length; ii++) {
      if(radioButton[ii].checked) {
        displayScMethod(radioButton[ii]);
        break;
      }
    }
  }
  
  //]]>
</script>
  
<%-- Constant variables for Growing Forward 2013 changes --%>
<c:choose>
  <c:when test="${form.growingForward2013}">
    <c:set var="growingForward2013" value="true"/>
    <c:set var="labelStructuralChangeMethod" value="Margin.Structural.Change.Method"/>
    <c:set var="labelTotalStructuralChange" value="Total.Margin.Structural.Change"/>
  </c:when>
  <c:otherwise>
    <c:set var="growingForward2013" value="false"/>
    <c:set var="labelStructuralChangeMethod" value="Structural.Change.Method"/>
    <c:set var="labelTotalStructuralChange" value="Total.Structural.Change"/>
  </c:otherwise>
</c:choose>

<c:if test="${!growingForward2013}">
  <table style="width:100%">
    <tr>
      <td>
        <fieldset id="additiveFinancialView" >
          <legend><fmt:message key="Financial.View"/></legend>
            <table>
            <tr>
              <td>
                <input type="radio"
                       name="financialViewRadio" 
                       value="capacityTable"
                       checked="checked"
                       onclick="displayFinancialView(this)" />
                <fmt:message key="Adjusted.Productive.Units"/>
              </td>
              <td>
                <input type="radio"
                       name="financialViewRadio" 
                       value="bpuTable" 
                       onclick="displayFinancialView(this)" />
                <fmt:message key="BPU"/>
              </td>
              <td id="saFV" >
                <input type="radio"
                       name="financialViewRadio" 
                       value="adjustmentsTable" 
                       onclick="displayFinancialView(this)" />
                <fmt:message key="Structural.Adjustments"/>
              </td>
              <td id="varFV">
                <input type="radio"
                       name="financialViewRadio" 
                       value="varianceTable" 
                       onclick="displayFinancialView(this)" />
                <fmt:message key="Productive.Unit.Variance"/> 
              </td>
              <td id="rypyFV" style="display: none" >
                <input type="radio"
                       name="financialViewRadio" 
                       value="refYearProdValueTable" 
                       onclick="displayFinancialView(this)" />
                <fmt:message key="Reference.Year.Productive.Value"/>
              </td>
              <td id="pypyFV" style="display: none" >
                <input type="radio"
                       name="financialViewRadio" 
                       value="progYearProdValueTable" 
                       onclick="displayFinancialView(this)" />
                <fmt:message key="Program.Year.Productive.Value"/>
              </td>
            </tr>
          </table>
        </fieldset>
      </td>
      <td id="recalcWarning" style="text-align: right;" >
        <img src="images/bcgov_warning.gif" title="<fmt:message key="Recalculation.required"/>" alt="Warning"/>
      </td>
    </tr>
  </table>
</c:if>


<html:form action="saveStructuralChange" styleId="structuralChangeForm" method="post" onsubmit="showProcessing()">
  <html:hidden property="pin"/>
  <html:hidden property="year"/>
  <html:hidden property="scenarioNumber"/>
  <html:hidden property="scenarioRevisionCount"/>

  <table class="numeric" style="width:100%">
    <tr>
      <td colspan="2" class="rowSpacer"><img src="images/spacer.gif" alt="Spacer" width="1" height="1" /></td>
    </tr>
    <tr>
      <th><fmt:message key="${labelStructuralChangeMethod}"/></th>
      <td style="text-align:center">
        <html-el:radio property="structuralChangeMethod" value="ADD"   onclick="displayScMethod(this)" disabled="${form.readOnly}" /> <fmt:message key="Additive"/>
        <html-el:radio property="structuralChangeMethod" value="RATIO" onclick="displayScMethod(this)" disabled="${form.readOnly}" /> <fmt:message key="Ratio"/>
        <html-el:radio property="structuralChangeMethod" value="NONE"  onclick="displayScMethod(this)" disabled="${form.readOnly}" /> <fmt:message key="None"/>
      </td>
    </tr>
    <c:if test="${growingForward2013}">
      <tr>
        <th><fmt:message key="Expense.Structural.Change.Method"/></th>
        <td style="text-align:center">
          <html-el:radio property="expenseStructuralChangeMethod" value="ADD"   onclick="displayScMethod(this)" disabled="${form.readOnly}" /> <fmt:message key="Additive"/>
          <html-el:radio property="expenseStructuralChangeMethod" value="RATIO" onclick="displayScMethod(this)" disabled="${form.readOnly}" /> <fmt:message key="Ratio"/>
          <html-el:radio property="expenseStructuralChangeMethod" value="NONE"  onclick="displayScMethod(this)" disabled="${form.readOnly}" /> <fmt:message key="None"/>
        </td>
      </tr>
    </c:if>
    <tr>
      <td colspan="2" class="rowSpacer"><img src="images/spacer.gif" alt="Spacer" width="1" height="1" /></td>
    </tr>
  </table>

  <br />

  <div>
    <div id="totalsSection">
      <table class="numeric" style="width:100%">
        <tr>
          <th width="100" class="cellWhite">&nbsp;</th>
          <th width="200">&nbsp;</th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <th width="100"><c:out value="${rs.year}"/></th>
          </c:forEach>
        </tr>
        <tr>
          <td class="cellWhite">&nbsp;</td>
          <th><fmt:message key="BPU.Set"/></th>
          <c:forEach varStatus="loop" var="rs" items="${scenario.referenceScenarios}">
            <td class="totalAmount" id="bpuLeadTd<c:out value="${loop.index}"/>"><html-el:checkbox property="isLead[${loop.index}]" onclick="checkRecalcWarning()" /><fmt:message key="Lead"/></td>
          </c:forEach>
        </tr>
        <tr id="knownRatioTotalsRow">
          <td class="cellWhite">&nbsp;</td>
          <th><fmt:message key="Farm.Size.Ratio"/></th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td class="totalAmount"><fmt:formatNumber type="percent" value="${rs.farmingYear.marginTotal.farmSizeRatio}"/></td>
          </c:forEach>
        </tr>
        <tr id="unknownRatioTotalsRow">
          <td class="cellWhite">&nbsp;</td>
          <th><fmt:message key="Farm.Size.Ratio"/> <br /> (<fmt:message key="requires.calcuation"/>)</th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td style="background-color: #CC3333">&nbsp;</td>
          </c:forEach>
        </tr>
        <c:if test="${form.growingForward2013}">
          <tr id="knownExpenseRatioTotalsRow">
            <td class="cellWhite">&nbsp;</td>
            <th><fmt:message key="Expense.Farm.Size.Ratio"/></th>
            <c:forEach var="rs" items="${scenario.referenceScenarios}">
              <td class="totalAmount"><fmt:formatNumber type="percent" value="${rs.farmingYear.marginTotal.expenseFarmSizeRatio}"/></td>
            </c:forEach>
          </tr>
        </c:if>
        <c:if test="${form.growingForward2013}">
          <tr id="unknownExpenseRatioTotalsRow">
            <td class="cellWhite">&nbsp;</td>
            <th><fmt:message key="Expense.Farm.Size.Ratio"/> <br /> (<fmt:message key="requires.calcuation"/>)</th>
            <c:forEach var="rs" items="${scenario.referenceScenarios}">
              <td style="background-color: #CC3333">&nbsp;</td>
            </c:forEach>
          </tr>
        </c:if>
        <tr id="knownTotalsRow">
          <td class="cellWhite">&nbsp;</td>
          <th><fmt:message key="${labelTotalStructuralChange}"/></th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td class="totalAmount"><fmt:formatNumber type="currency" value="${rs.farmingYear.marginTotal.structuralChangeAdjs}"/></td>
          </c:forEach>
        </tr>
        <tr id="unknownTotalsRow">
          <td class="cellWhite">&nbsp;</td>
          <th><fmt:message key="${labelTotalStructuralChange}"/> <br /> (<fmt:message key="requires.calcuation"/>)</th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td style="background-color: #CC3333">&nbsp;</td>
          </c:forEach>
        </tr>
        <c:if test="${growingForward2013}">
          <tr id="knownExpenseTotalsRow">
            <td class="cellWhite">&nbsp;</td>
            <th><fmt:message key="Total.Expense.Structural.Change"/></th>
            <c:forEach var="rs" items="${scenario.referenceScenarios}">
              <td class="totalAmount"><fmt:formatNumber type="currency" value="${rs.farmingYear.marginTotal.expenseStructuralChangeAdjs}"/></td>
            </c:forEach>
          </tr>
          <tr id="unknownExpenseTotalsRow">
            <td class="cellWhite">&nbsp;</td>
            <th><fmt:message key="Total.Expense.Structural.Change"/> <br /> (<fmt:message key="requires.calcuation"/>)</th>
            <c:forEach var="rs" items="${scenario.referenceScenarios}">
              <td style="background-color: #CC3333">&nbsp;</td>
            </c:forEach>
          </tr>
        </c:if>
        <tr>
          <td colspan="7" class="rowSpacer"><img src="images/spacer.gif" alt="Spacer" width="1" height="1" /></td>
        </tr>
      </table>
    </div>
    
  <c:if test="${growingForward2013}">
  
    <c:if test="${!form.readOnly}">
      <div style="text-align:right">
        <br /><u:yuiButton buttonLabel="Save" buttonId="saveButton" formId="structuralChangeForm"/>
      </div>
    </c:if>
    
  <table>
    <tr>
      <td>
        <fieldset style="width:100%;">
          <legend><fmt:message key="Structural.Change.Type"/></legend>
          <table>
            <tr>
              <td style="padding-right:15px;">
                <input type="radio" name="scTypeViewRadio" value="marginSCT" checked="checked" onclick="switchScType(this)" />
                <fmt:message key="Margin"/>
               </td>
              <td>
                <input type="radio" name="scTypeViewRadio" value="expenseSCT" onclick="switchScType(this)" />
                <fmt:message key="Expense"/>
              </td>
           </tr>
          </table>
        </fieldset>
      </td>
    </tr>
  </table>
    
    <table style="width:100%">
    <tr>
      <td>
        <fieldset id="additiveFinancialView" >
          <legend><fmt:message key="Financial.View"/></legend>
            <table>
            <tr>
              <td>
                <input type="radio"
                       name="financialViewRadio" 
                       value="capacityTable"
                       checked="checked"
                       onclick="displayFinancialView(this)" />
                <fmt:message key="Adjusted.Productive.Units"/>
              </td>
              <td>
                <input type="radio"
                       name="financialViewRadio" 
                       value="bpuTable" 
                       onclick="displayFinancialView(this)" />
                <fmt:message key="BPU"/>
              </td>
              <td id="saFV" >
                <input type="radio"
                       name="financialViewRadio" 
                       value="adjustmentsTable" 
                       onclick="displayFinancialView(this)" />
                <fmt:message key="Structural.Adjustments"/>
              </td>
              <td id="varFV">
                <input type="radio"
                       name="financialViewRadio" 
                       value="varianceTable" 
                       onclick="displayFinancialView(this)" />
                <fmt:message key="Productive.Unit.Variance"/> 
              </td>
              <td id="rypyFV" style="display: none" >
                <input type="radio"
                       name="financialViewRadio" 
                       value="refYearProdValueTable" 
                       onclick="displayFinancialView(this)" />
                <fmt:message key="Reference.Year.Productive.Value"/>
              </td>
              <td id="pypyFV" style="display: none" >
                <input type="radio"
                       name="financialViewRadio" 
                       value="progYearProdValueTable" 
                       onclick="displayFinancialView(this)" />
                <fmt:message key="Program.Year.Productive.Value"/>
              </td>
            </tr>
            </table>
          </fieldset>
        </td>
        <td id="recalcWarning" style="text-align: right;" >
          <img src="images/bcgov_warning.gif" title="<fmt:message key="Recalculation.required"/>" alt="Warning"/>
        </td>
      </tr>
    </table>
  
  </c:if>
    
    
    <!-- START PRODUCTIVE CAPACITY DATA TABLES -->
    <!-- The values for these tables do not change when switching between Margin and Expense. -->
    <table class="numeric" id="capacityTable" style="width:100%">
      <tr>
        <th width="100"><fmt:message key="Code"/></th>
        <th width="200"><fmt:message key="Description"/></th>
        <c:forEach var="rs" items="${scenario.referenceScenarios}">
          <th width="100"><c:out value="${rs.year}"/></th>
        </c:forEach>
      </tr>
      <c:forEach var="row" items="${form.rows}">
        <tr>
          <th><c:out value="${row.code}"/></th>
          <th><c:out value="${row.description}"/></th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><fmt:formatNumber type="number" value="${row.capacityYearMap[rs.year]}"/></td>
          </c:forEach>
        </tr>
      </c:forEach>
      <tr>
        <td colspan="7" class="rowSpacer"><img src="images/spacer.gif" alt="Spacer" width="1" height="1" /></td>
      </tr>
    </table>
    
    <table class="numeric" id="varianceTable" style="display:none; width:100%">
      <tr>
        <th width="100"><fmt:message key="Code"/></th>
        <th width="200"><fmt:message key="Description"/></th>
        <c:forEach var="rs" items="${scenario.referenceScenarios}">
          <th width="100"><c:out value="${rs.year}"/></th>
        </c:forEach>
      </tr>
      <c:forEach var="row" items="${form.rows}">
        <tr>
          <th><c:out value="${row.code}"/></th>
          <th><c:out value="${row.description}"/></th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><fmt:formatNumber type="number" value="${row.varianceYearMap[rs.year]}"/></td>
          </c:forEach>
        </tr>
      </c:forEach>
      <tr>
        <td colspan="7" class="rowSpacer"><img src="images/spacer.gif" alt="Spacer" width="1" height="1" /></td>
      </tr>
    </table>
    <!-- END PRODUCTIVE CAPACITY DATA TABLES -->
    
    <!-- START MARGIN STRUCTURAL CHANGE DATA TABLES -->
    <table class="numeric" id="bpuTable" style="display:none; width:100%">
      <tr>
        <th width="100"><fmt:message key="Code"/></th>
        <th width="200"><fmt:message key="Description"/></th>
        <c:forEach var="rs" items="${scenario.referenceScenarios}">
          <th width="100"><c:out value="${rs.year}"/></th>
        </c:forEach>
      </tr>
      <c:forEach var="row" items="${form.rows}">
        <tr>
          <th><c:out value="${row.code}"/></th>
          <th><c:out value="${row.description}"/></th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><fmt:formatNumber type="currency" value="${row.bpuYearMap[rs.year]}"/></td>
          </c:forEach>
        </tr>
      </c:forEach>
      <tr>
        <td colspan="7" class="rowSpacer"><img src="images/spacer.gif" alt="Spacer" width="1" height="1" /></td>
      </tr>
    </table>
    
    <table class="numeric" id="adjustmentsTable" style="display:none; width:100%">
      <tr>
        <th width="100"><fmt:message key="Code"/></th>
        <th width="200"><fmt:message key="Description"/></th>
        <c:forEach var="rs" items="${scenario.referenceScenarios}">
          <th width="100"><c:out value="${rs.year}"/></th>
        </c:forEach>
      </tr>
      <c:forEach var="row" items="${form.rows}">
        <tr>
          <th><c:out value="${row.code}"/></th>
          <th><c:out value="${row.description}"/></th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><fmt:formatNumber type="currency" value="${row.adjustmentsYearMap[rs.year]}"/></td>
          </c:forEach>
        </tr>
      </c:forEach>
      <tr>
        <td colspan="7" class="rowSpacer"><img src="images/spacer.gif" alt="Spacer" width="1" height="1" /></td>
      </tr>
    </table>
    
    <table class="numeric" id="refYearProdValueTable" style="display:none; width:100%">
      <tr>
        <th width="100"><fmt:message key="Code"/></th>
        <th width="200"><fmt:message key="Description"/></th>
        <c:forEach var="rs" items="${scenario.referenceScenarios}">
          <th width="100"><c:out value="${rs.year}"/></th>
        </c:forEach>
      </tr>
      <c:forEach var="row" items="${form.rows}">
        <tr>
          <th><c:out value="${row.code}"/></th>
          <th><c:out value="${row.description}"/></th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><fmt:formatNumber type="currency" value="${row.refYearProdValueYearMap[rs.year]}"/></td>
          </c:forEach>
        </tr>
      </c:forEach>
      <tr>
        <td colspan="7" class="rowSpacer"><img src="images/spacer.gif" alt="Spacer" width="1" height="1" /></td>
      </tr>
    </table>
    
    <table class="numeric" id="progYearProdValueTable" style="display:none; width:100%">
      <tr>
        <th width="100"><fmt:message key="Code"/></th>
        <th width="200"><fmt:message key="Description"/></th>
        <c:forEach var="rs" items="${scenario.referenceScenarios}">
          <th width="100"><c:out value="${rs.year}"/></th>
        </c:forEach>
      </tr>
      <c:forEach var="row" items="${form.rows}">
        <tr>
          <th><c:out value="${row.code}"/></th>
          <th><c:out value="${row.description}"/></th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><fmt:formatNumber type="currency" value="${row.progYearProdValueYearMap[rs.year]}"/></td>
          </c:forEach>
        </tr>
      </c:forEach>
      <tr>
        <td colspan="7" class="rowSpacer"><img src="images/spacer.gif" alt="Spacer" width="1" height="1" /></td>
      </tr>
    </table>
    <!-- END MARGIN STRUCTURAL CHANGE DATA TABLES -->
    
    <!-- START EXPENSE STRUCTURAL CHANGE DATA TABLES -->
    <table class="numeric" id="bpuTable_expense" style="display:none; width:100%">
      <tr>
        <th width="100"><fmt:message key="Code"/></th>
        <th width="200"><fmt:message key="Description"/></th>
        <c:forEach var="rs" items="${scenario.referenceScenarios}">
          <th width="100"><c:out value="${rs.year}"/></th>
        </c:forEach>
      </tr>
      <c:forEach var="row" items="${form.rows}">
        <tr>
          <th><c:out value="${row.code}"/></th>
          <th><c:out value="${row.description}"/></th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><fmt:formatNumber type="currency" value="${row.expenseBpuYearMap[rs.year]}"/></td>
          </c:forEach>
        </tr>
      </c:forEach>
      <tr>
        <td colspan="7" class="rowSpacer"><img src="images/spacer.gif" alt="Spacer" width="1" height="1" /></td>
      </tr>
    </table>
    
    <table class="numeric" id="adjustmentsTable_expense" style="display:none; width:100%">
      <tr>
        <th width="100"><fmt:message key="Code"/></th>
        <th width="200"><fmt:message key="Description"/></th>
        <c:forEach var="rs" items="${scenario.referenceScenarios}">
          <th width="100"><c:out value="${rs.year}"/></th>
        </c:forEach>
      </tr>
      <c:forEach var="row" items="${form.rows}">
        <tr>
          <th><c:out value="${row.code}"/></th>
          <th><c:out value="${row.description}"/></th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><fmt:formatNumber type="currency" value="${row.expenseAdjustmentsYearMap[rs.year]}"/></td>
          </c:forEach>
        </tr>
      </c:forEach>
      <tr>
        <td colspan="7" class="rowSpacer"><img src="images/spacer.gif" alt="Spacer" width="1" height="1" /></td>
      </tr>
    </table>
    
    <table class="numeric" id="refYearProdValueTable_expense" style="display:none; width:100%">
      <tr>
        <th width="100"><fmt:message key="Code"/></th>
        <th width="200"><fmt:message key="Description"/></th>
        <c:forEach var="rs" items="${scenario.referenceScenarios}">
          <th width="100"><c:out value="${rs.year}"/></th>
        </c:forEach>
      </tr>
      <c:forEach var="row" items="${form.rows}">
        <tr>
          <th><c:out value="${row.code}"/></th>
          <th><c:out value="${row.description}"/></th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><fmt:formatNumber type="currency" value="${row.expenseRefYearProdValueYearMap[rs.year]}"/></td>
          </c:forEach>
        </tr>
      </c:forEach>
      <tr>
        <td colspan="7" class="rowSpacer"><img src="images/spacer.gif" alt="Spacer" width="1" height="1" /></td>
      </tr>
    </table>
    
    <table class="numeric" id="progYearProdValueTable_expense" style="display:none; width:100%">
      <tr>
        <th width="100"><fmt:message key="Code"/></th>
        <th width="200"><fmt:message key="Description"/></th>
        <c:forEach var="rs" items="${scenario.referenceScenarios}">
          <th width="100"><c:out value="${rs.year}"/></th>
        </c:forEach>
      </tr>
      <c:forEach var="row" items="${form.rows}">
        <tr>
          <th><c:out value="${row.code}"/></th>
          <th><c:out value="${row.description}"/></th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><fmt:formatNumber type="currency" value="${row.expenseProgYearProdValueYearMap[rs.year]}"/></td>
          </c:forEach>
        </tr>
      </c:forEach>
      <tr>
        <td colspan="7" class="rowSpacer"><img src="images/spacer.gif" alt="Spacer" width="1" height="1" /></td>
      </tr>
    </table>
    <!-- END EXPENSE STRUCTURAL CHANGE DATA TABLES -->
    
  <c:if test="${!growingForward2013}">
    <c:if test="${!form.readOnly}">
      <div style="text-align:right">
        <br /><p><u:yuiButton buttonLabel="Save" buttonId="saveButton" formId="structuralChangeForm"/></p>
      </div>
    </c:if>
  </c:if>
  
  </div>
</html:form>


<c:if test="${!form.readOnly}">
  <u:dirtyFormCheck formId="structuralChangeForm"/>
  
  <script type="text/javascript">
    //<![CDATA[
    excludeFieldFromDirtyCheck("scTypeViewRadio");
    excludeFieldFromDirtyCheck("financialViewRadio");
  //]]>
  </script>
  
</c:if>
