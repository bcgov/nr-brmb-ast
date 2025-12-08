<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getScenario var="scenario" pin="${form.pin}" year="${form.year}" scenarioNumber="${form.scenarioNumber}" scope="request"/>


<script type="text/javascript">
  //<![CDATA[
  
  function displayFinancialView(radioButton) {
    var tableId = radioButton.value;
    document.getElementById('capacityTable').style.display = 'none';
    document.getElementById('bpuTable').style.display = 'none';
    document.getElementById('varianceTable').style.display = 'none';
    document.getElementById('adjustmentsTable').style.display = 'none';
    document.getElementById('refYearProdValueTable').style.display = 'none';
    document.getElementById('progYearProdValueTable').style.display = 'none';
    
    document.getElementById(tableId).style.display = '';
  }
  
  
  function displayScMethod(radioButton) {

    var methodCode = $('input:radio[name=structuralChangeMethod]:checked').val();

    displayFinancialViewOptions(methodCode);
    
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
  
  function onPageLoad() {
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
      <th><fmt:message key="Structural.Change.Method"/></th>
      <td style="text-align:center">
        <html-el:radio property="structuralChangeMethod" value="ADD"   onclick="displayScMethod(this)" disabled="${form.readOnly}" /> <fmt:message key="Additive"/>
        <html-el:radio property="structuralChangeMethod" value="RATIO" onclick="displayScMethod(this)" disabled="${form.readOnly}" /> <fmt:message key="Ratio"/>
        <html-el:radio property="structuralChangeMethod" value="NONE"  onclick="displayScMethod(this)" disabled="${form.readOnly}" /> <fmt:message key="None"/>
      </td>
    </tr>
    <tr>
      <td colspan="2" class="rowSpacer"><img src="images/spacer.gif" alt="Spacer" width="1" height="1" /></td>
    </tr>
  </table>

  <br />

  <div>
    <div id="totalsSection">
      <table class="numeric" style="width:100%">
        <tr>
          <th width="200">&nbsp;</th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <th width="100"><c:out value="${rs.year}"/></th>
          </c:forEach>
        </tr>
        <tr>
          <th><fmt:message key="BPU.Set"/></th>
          <c:forEach varStatus="loop" var="rs" items="${scenario.referenceScenarios}">
            <td class="totalAmount" id="bpuLeadTd<c:out value="${loop.index}"/>"><html-el:checkbox property="isLead[${loop.index}]" /><fmt:message key="Lead"/></td>
          </c:forEach>
        </tr>
        <tr>
        <th><fmt:message key="Production.Margin.with.Accrual.Adjustments"/></th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><fmt:formatNumber type="currency" value="${rs.farmingYear.marginTotal.productionMargAccrAdjs}"/></td>
          </c:forEach>
        </tr>
        <tr>
          <td colspan="7" class="rowSpacer"><img src="images/spacer.gif" alt="Spacer" width="1" height="1" /></td>
        </tr>
      </table>
    </div>
    
    
    <div id="ratioSection">
      <table class="numeric" style="width:100%">
        <tr>
          <th width="200">&nbsp;</th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <th width="100"><c:out value="${rs.year}"/></th>
          </c:forEach>
        </tr>
        <tr>
          <th><fmt:message key="Farm.Size.Ratio"/></th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <c:set var="farmSizeRatioBackgroundColour" value=""/>
            <c:if test="${rs.farmingYear.marginTotal.farmSizeRatio >= 2.50 or rs.farmingYear.marginTotal.farmSizeRatio <= -2.50}">
              <c:set var="farmSizeRatioBackgroundColour" value="background-color:#F58516"/>
            </c:if>
            <td class="totalAmount" style="<c:out value="${farmSizeRatioBackgroundColour}"/>"><fmt:formatNumber type="percent" value="${rs.farmingYear.marginTotal.farmSizeRatio}"/></td>
          </c:forEach>
        </tr>
        <tr>
          <th>
            Ratio Method Structural Adjustment
            <c:forEach var="rs" items="${scenario.referenceScenarios}">
              <td><fmt:formatNumber type="currency" value="${rs.farmingYear.marginTotal.ratioStructuralChangeAdjs}"/></td>
            </c:forEach>
          </th>
        </tr>
        <tr>
          <th>Ratio Production Margin after Structural Change</th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><fmt:formatNumber type="currency" value="${rs.farmingYear.marginTotal.ratioProductionMargAftStrChangs}"/></td>
          </c:forEach>
        </tr>
        <tr>
          <th><fmt:message key="Figures.Used.in.Calculation"/></th>
          <c:forEach var="used" items="${form.usedInRatioCalc}">
            <td><u:formatBoolean test="${used}" trueValue="Yes" falseValue="No" /></td>
          </c:forEach>
        </tr>
        <tr>
          <td colspan="7" class="rowSpacer"><img src="images/spacer.gif" alt="Spacer" width="1" height="1" /></td>
        </tr>
      </table>
    </div>
    
    <div id="additiveSection">
      <table class="numeric" style="width:100%">
        <tr>
          <th width="200">&nbsp;</th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <th width="100"><c:out value="${rs.year}"/></th>
          </c:forEach>
        </tr>
        <tr>
          <th>
            Additive Method Structural Adjustment
            <c:forEach var="rs" items="${scenario.referenceScenarios}">
              <td><fmt:formatNumber type="currency" value="${rs.farmingYear.marginTotal.additiveStructuralChangeAdjs}"/></td>
            </c:forEach>
          </th>
        </tr>
        <tr>
          <th>Additive Production Margin after Structural Change</th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><fmt:formatNumber type="currency" value="${rs.farmingYear.marginTotal.additiveProductionMargAftStrChangs}"/></td>
          </c:forEach>
        </tr>
        <tr>
       <th><fmt:message key="Figures.Used.in.Calculation"/></th>
          <c:forEach var="used" items="${form.usedInAdditiveCalc}">
            <td><u:formatBoolean test="${used}" trueValue="Yes" falseValue="No" /></td>
          </c:forEach>
        </tr>
        <tr>
          <td colspan="7" class="rowSpacer"><img src="images/spacer.gif" alt="Spacer" width="1" height="1" /></td>
        </tr>
      </table>
    </div>
  
    <c:if test="${!form.readOnly}">
      <div style="text-align:right" id="saveWrapper">
        <br /><u:yuiButton buttonLabel="Save" buttonId="saveButton" formId="structuralChangeForm"/>
      </div>
    </c:if>
    
    
    <table style="width:100%">
    <tr>
      <td>
        <fieldset id="financialView" >
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
      </tr>
    </table>
  
    
    <!-- START PRODUCTIVE CAPACITY DATA TABLES -->
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
